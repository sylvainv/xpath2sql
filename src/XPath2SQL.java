import java.io.*;
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

public class XPath2SQL {

	String path2DTD;
	String path2XML;
	String xpathQuery;
	String regularXpathQuery;
	static DTDGraph dtdgraph;
	
	public static RelationalQuery xpath2sql(String query,DTDGraph dtdgraph){
		Pattern slashPattern = Pattern.compile("[a-z]*/[a-z]*");
		Matcher slash = slashPattern.matcher(query);
		
		Pattern elementPattern = Pattern.compile("[a-z]*");
		Matcher element = elementPattern.matcher(query);
	    
		if(slash.matches()){
			String[] qsplit = query.split("/");
			String q1 = qsplit[0];
			String q2 = qsplit[1];
			System.out.println(q1+" "+q2);
			RelationalQuery r1 = xpath2sql(q1,dtdgraph);
			RelationalQuery r2 = xpath2sql(q2,dtdgraph);
			return RelationalQuery.merge(r1, r2);
		}
		else if(element.matches() & dtdgraph.elements().contains(query)){
			return new RelationalQuery();
		}
		else{
			return new RelationalQuery();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		dtdgraph = new DTDGraph();
		//System.out.println(dtdgraph);
		xpath2sql("proceeding/booktitle",dtdgraph);
	}

}
