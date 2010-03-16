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
import javax.xml.xpath.*;
import java.sql.*;
import org.modeshape.jcr.xpath.*;

public class XPath2SQL {

	String path2DTD;
	String path2XML;
	String xpathQuery;
	String regularXpathQuery;
	static DTDGraph dtdgraph;

	// this one can give you the subquery list
	// for instance /DBLP//inproceedings[year=2009]/author/year//title
	// will be stored as
	// empty/DBLP
	// DBLP//inproceedings[year=2009]
	// inproceedings[year=2009]/author
	// author/year
	// year//title
	private static ArrayList<String> xpathParser(String xpathQuery) {
		String xpathExpr = xpathQuery;
		Pattern slashPattern = Pattern.compile("/");
		Matcher slash = slashPattern.matcher(xpathQuery.substring(0,1));
		if(slash.matches()){
			xpathExpr = "#" + xpathExpr;
		}
		String[] element = xpathExpr.split("/");
		ArrayList<String> subquery = new ArrayList<String>();
		for (int i = 0; i < element.length; i++) {
			System.out.println(element[i]);
			if (i + 1 < element.length) {
				if (!element[i + 1].isEmpty()) {
					String temp = element[i] + "/" + element[i + 1];
					subquery.add(temp);
				} else {
					if (i + 2 < element.length) {
						String temp = element[i] + "//" + element[i + 2];
						subquery.add(temp);
						i++;
					}
				}
			}
		}
		return subquery;
	}	
	
	public static RelationalQuery xpath2sql(String xpath, DTDGraph dtdgraph) {
		RelationalQuery newQuery = new RelationalQuery();
		
		ArrayList<String> list = xpathParser(xpath);
		Iterator<String> iter = list.iterator();
		System.out.println("Subquery:"+list);
		while(iter.hasNext()){
			Pattern slashPattern = Pattern.compile("[a-z]*/[a-z]*");
			Matcher slash = slashPattern.matcher(iter.next());

			Pattern elementPattern = Pattern.compile("[a-z]*");
			Matcher element = elementPattern.matcher(iter.next());			
			
			// Case p1/p2
			if (slash.matches()) {
				String[] qsplit = xpath.split("/");
				String q1 = qsplit[0];
				String q2 = qsplit[1];
				System.out.println("Case p1/p2: " + q1 + "/" + q2);
				RelationalQuery r1 = xpath2sql(q1, dtdgraph);
				RelationalQuery r2 = xpath2sql(q2, dtdgraph);
				newQuery = RelationalQuery.merge(newQuery, RelationalQuery.merge(
						r1, r2));
			}
			// Case A
			else if (element.matches() & dtdgraph.isInGraph(xpath)) {
				System.out.println("Case A: " + xpath);
				Vector<String> paths = dtdgraph.mapping(xpath);
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
		RelationalQuery query = xpath2sql("/dblp/proceedings/booktitle", dtdgraph);
		System.out.println(query);
	}

}
