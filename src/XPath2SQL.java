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
		System.out.println("Parsing...:"+xpathQuery);
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
				} else {
					if (i + 2 < element.length) {
						element[i] = element[i].replace('?', '/');
						element[i + 2] = element[i + 2].replace('?', '/');
						String temp = element[i] + "//" + element[i + 2];
						subquery.add(temp);
						i++;
					}
				}
			}
		}
		//draw predicate from the element which contains a predicate that is after /
		for (int i = 0; i < subquery.size(); i++) {
			if (subquery.get(i).endsWith("]")) {
				String temp = subquery.get(i).substring(subquery.get(i).indexOf("["));
				subquery.add(i+1, temp);
				i++;
			}
		}
		System.out.println("...Parsed: "+subquery);
		return subquery;
	}
	
	public static RelationalQuery xpath2sql(String xpath, DTDGraph dtdgraph) {
		RelationalQuery newQuery = new RelationalQuery();
		Pattern pattern = Pattern.compile("(([a-z]*)|#)");
		Matcher matcher = pattern.matcher(xpath);
		ArrayList<String> subqueries;
		if(!matcher.matches()){
			subqueries = xpathParser(xpath);
		}
		else{
			subqueries = new ArrayList<String>();
			subqueries.add(xpath);
		}
		Iterator<String> iter = subqueries.iterator();
		while(iter.hasNext()){
			String subquery = iter.next();
			Pattern slashPattern = Pattern.compile("(([a-z]*)|#)/[a-z]*");
			Matcher slash = slashPattern.matcher(subquery);

			Pattern elementPattern = Pattern.compile("[a-z]*");
			Matcher element = elementPattern.matcher(subquery);
			
			// Case A
			if (element.matches() & dtdgraph.isInGraph(subquery)) {
				System.out.println("Case A: " + subquery);
				Vector<String> paths = dtdgraph.mapping(subquery);
				System.out.println(paths.toString());
				Iterator<String> pathIte = paths.iterator();
				while (pathIte.hasNext()) {
					String[] split = pathIte.next().split("=");
					Pattern p = Pattern.compile("table");
					Matcher m = p.matcher(split[0]);
					if (m.matches()) {
						newQuery.addFromItem(new FromItem(split[1]));
					} else {
						newQuery.addSelectItem(new SelectItem(split[1]));
					}
				}
			}
			else if (subquery=="#"){
				System.out.println("Empty element... do nothing");
			}
			// Case p1/p2
			else if (slash.matches()) {
				String[] qsplit = subquery.split("/");
				String q1 = qsplit[0];
				String q2 = qsplit[1];
				System.out.println("Case p1/p2: " + q1 + "/" + q2);
				RelationalQuery r1 = xpath2sql(q1, dtdgraph);
				RelationalQuery r2 = xpath2sql(q2, dtdgraph);
				newQuery = RelationalQuery.merge(newQuery, RelationalQuery.merge(r1, r2));
			}	
		}
		return newQuery;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		dtdgraph = new DTDGraph();
		// System.out.println(dtdgraph);
		RelationalQuery query = xpath2sql("/dblp/proceedings[/dblp/proceedings/booktitle='toto']", dtdgraph);
		query.cleanUp();
		System.out.println(query);
	}

}
