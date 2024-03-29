import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XPath2SQL {

	String path2DTD;
	String path2XML;
	String xpathQuery;
	String regularXpathQuery;
	static DTDGraph dtdgraph;

	//	 this one can give you the subquery list
	// for instance /DBLP//inproceedings[year=2009]/author/year//title
	// will be stored as
	// empty/DBLP
	// DBLP//inproceedings[year=2009]
	// inproceedings[year=2009]/author
	// author/year
	// year//title
	private static ArrayList<String> xpathParser(String xpathQuery) {
		String xpathExpr = xpathQuery;
		System.err.println("Parsing...:"+xpathQuery);
		// handle path within predicate, replace '/' by '?' within [] at this
		// stage
		int isInPredicate = 0;
		String tempExpr = "";
		for (int i = 0; i < xpathExpr.length(); i++) {
			if (xpathExpr.charAt(i) == '[') {
				isInPredicate++;
			} else if (xpathExpr.charAt(i) == ']') {
				isInPredicate--;
			}
			if (isInPredicate != 0) {
				if (xpathExpr.charAt(i) == '/') {
					tempExpr = tempExpr + '?';
				}

				else {
					tempExpr = tempExpr + xpathExpr.charAt(i);
				}
			} else {
				tempExpr = tempExpr + xpathExpr.charAt(i);
			}

		}
		xpathExpr = tempExpr;
		if (xpathQuery.charAt(0) == '/') {
			xpathExpr = "#" + xpathExpr;
		}

		String[] element = xpathExpr.split("/");
		ArrayList<String> subquery = new ArrayList<String>();
		for (int i = 0; i < element.length; i++) {
			if (i + 1 < element.length) {
				if (!element[i + 1].isEmpty()) {
					element[i] = element[i].replace('?', '/');
					element[i + 1] = element[i + 1].replace('?', '/');
					String temp = element[i] + "/" + element[i + 1];
					subquery.add(temp);
					if(element[i + 1].endsWith("]"))
					{
						subquery.add(element[i+1].substring(element[i+1].indexOf("[")));
					}
					
				} else {
					if (i + 2 < element.length) {
						element[i] = element[i].replace('?', '/');
						element[i + 2] = element[i + 2].replace('?', '/');
						String temp = element[i] + "//" + element[i + 2];
						subquery.add(temp);
						if(element[i + 2].endsWith("]"))
						{
							subquery.add(element[i+2].substring(element[i+2].indexOf("[")));
						}
						i++;
					}
				}
			}
		}
		System.err.println("...Parsed: "+subquery);
		return subquery;
	}
	
	public static RelationalQuery xpath2sql(String xpath, DTDGraph dtdgraph) {
		RelationalQuery newQuery = new RelationalQuery();
		Pattern pattern = Pattern.compile("(([a-z*]*)|#)");
		Matcher matcher = pattern.matcher(xpath);
		
		// REGEX PATTERN
		String predicateRegex = "\\[(/([a-z]+|[*]))+='[a-z0-9]+'\\]";
		String elementEmptyRegex = "([a-z]*|#|[*])";
		String elementRegex = "([a-z]*|[*])";
		String emptyRegex = "#";
		
		// COMPILE PATTERN
		Pattern slashPattern = Pattern.compile(elementEmptyRegex+"("+predicateRegex+"){0,1}/"+elementRegex+"("+predicateRegex+"){0,1}");
		Pattern starPattern = Pattern.compile(elementEmptyRegex+"("+predicateRegex+"){0,1}"+"/[*]"+"("+predicateRegex+"){0,1}");
		Pattern doubleSlashPattern = Pattern.compile(elementEmptyRegex+"("+predicateRegex+"){0,1}//"+elementRegex+"("+predicateRegex+"){0,1}");
		Pattern elementPattern = Pattern.compile("[a-z]*");
		Pattern emptyPattern = Pattern.compile(emptyRegex);
		Pattern predicatePattern = Pattern.compile(predicateRegex);
		
		Matcher t = predicatePattern.matcher(xpath);	
		ArrayList<String> subqueries;
		if(matcher.matches() | t.matches()){
			subqueries = new ArrayList<String>();
			subqueries.add(xpath);
		}
		else{
			subqueries = xpathParser(xpath);
		}
		Iterator<String> iter = subqueries.iterator();
		while(iter.hasNext()){
			String subquery = iter.next();
			System.err.println("Current subquery: "+subquery+";");
			
			// MATCH PATTERN
			Matcher slash = slashPattern.matcher(subquery);
			Matcher star = starPattern.matcher(subquery);
			Matcher doubleSlash = doubleSlashPattern.matcher(subquery);
			Matcher element = elementPattern.matcher(subquery);
			Matcher empty = emptyPattern.matcher(subquery);
			Matcher predicate = predicatePattern.matcher(subquery);
			
			// Case A
			if (element.matches() & dtdgraph.isInGraph(subquery)) {
				System.err.println("Case A: " + subquery);
				Vector<String> paths = dtdgraph.mapping(subquery);
				System.err.println(paths.toString());
				Iterator<String> pathIte = paths.iterator();
				while (pathIte.hasNext()) {
					String[] split = pathIte.next().split("=");
					Pattern p = Pattern.compile("table");
					Matcher m = p.matcher(split[0]);
					// if it's a table
					if (m.matches()) {
						newQuery.addFromItem(new FromItem(split[1]));
					}
					// if it's an attribute
					else {
						Pattern j = Pattern.compile("publish\\.journal");
						Matcher jm = j.matcher(split[1]);
						if(jm.matches()){
							System.err.println("Case journal.id");
							newQuery.addFromItem(new FromItem(new JoinItem("journal","publish","journal.id","publish.journal")));
						}
						newQuery.addSelectItem(new SelectItem(split[1]));
					}
				}
			}
			// Case *
			else if (star.matches()){
				System.err.println("Case *: A/*");
				String queryMinusPredicate = subquery.replaceAll(predicateRegex,"");
				String[] qsplit = queryMinusPredicate.split("/");
				Pattern s = Pattern.compile("[*]");
				Matcher sm = s.matcher(qsplit[0]);
				if(sm.matches()){qsplit[0]="dblp";}
				String[] nodes = dtdgraph.getChildren(qsplit[0]);
				String nextQuery = "";
				if(iter.hasNext()){
					nextQuery = iter.next();
					if(predicatePattern.matcher(subquery).matches()){
						newQuery.merge(xpath2sql(nextQuery,dtdgraph));
						nextQuery = iter.next();
					}
				}
				for(int i=0;i<nodes.length;i++){
					RelationalQuery r;
					if(qsplit[0]=="#"){
						r = xpath2sql("/"+nodes[i],dtdgraph);
					}
					else{
						r = xpath2sql(qsplit[0]+"/"+nodes[i],dtdgraph);
					}
					newQuery.merge(r);
					if(!nextQuery.isEmpty()){
						r = xpath2sql(nextQuery.replaceFirst("[*]",nodes[i]),dtdgraph);
					}
					newQuery.merge(r);
				}
			}
			else if (empty.matches()){
				System.err.println("Empty element... do nothing");
			}
			// Case p1/p2
			else if (slash.matches()) {
				String queryMinusPredicate = subquery.replaceAll(predicateRegex,"");
				String[] qsplit = queryMinusPredicate.split("/");
				String q1 = qsplit[0];
				String q2 = qsplit[qsplit.length-1];
				System.err.println("Case p1/p2: " + q1 + "/" + q2);
				RelationalQuery r1 = xpath2sql(q1, dtdgraph);
				RelationalQuery r2 = xpath2sql(q2, dtdgraph);
				newQuery.merge(RelationalQuery.merge(r1, r2));
			}
			// Case [q=c]
			else if (predicate.matches()){
				System.err.println("Case [q=c]: "+subquery);
				subquery = subquery.substring(1,subquery.length()-1);
				String[] split = subquery.split("=");
				// Get left part of predicate
				String left = split[split.length-2];
				// Get right part of predicate
				String right = split[split.length-1];
				right = right.substring(1,right.length()-1);
				RelationalQuery subRelationalQuery = xpath2sql(left, dtdgraph);
				subRelationalQuery.cleanUp();
				newQuery.addWhereItem(new WhereItem(subRelationalQuery,right));
			}
			// Case p1//p2
			else if (doubleSlash.matches()){
				String queryMinusPredicate = subquery.replaceAll(predicateRegex,"");
				String[] qsplit = queryMinusPredicate.split("//");
				System.err.println(subquery+"**********"+queryMinusPredicate);
				String q1 = qsplit[0];
				String q2 = qsplit[1];
				System.err.println("Case p1//p2: " + q1 + "//" + q2);
				String[] paths = dtdgraph.getPath(q1,q2);
				for(int i=0;i<paths.length;i++){
					System.err.println("Path from p1 to p2: "+paths[i]);
					RelationalQuery r = xpath2sql(paths[i], dtdgraph);
					newQuery.merge(r);
				}
			}
			else{
				System.err.println("Warning: query is not handled");
			}
		}
		return newQuery;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		try{System.setErr(new PrintStream(new File("/dev/null")));}catch(FileNotFoundException e){}

		dtdgraph = new DTDGraph();

		//RelationalQuery query = xpath2sql(args[0], dtdgraph);
		RelationalQuery query = xpath2sql("/*/*/*", dtdgraph);
		query.cleanUp();
		System.out.println(query);
	}

}
