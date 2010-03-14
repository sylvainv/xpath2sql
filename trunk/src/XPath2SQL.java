
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.xml.xpath.*;
import java.sql.*;

public class XPath2SQL {

	String path2DTD;
	String path2XML;
	String xpathQuery;
	String regularXpathQuery;
	static DTDGraph dtdgraph;
	
	private void xpath2reg(String query,DTDGraph dtdgraph){
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		dtdgraph = new DTDGraph();
		System.out.println(dtdgraph);
	}

}
