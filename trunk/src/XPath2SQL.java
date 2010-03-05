
public class XPath2SQL {

	String path2DTD;
	String path2XML;
	String xpathQuery;
	String regularXpathQuery;
	static DTDGraph dtdgraph;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		dtdgraph = new DTDGraph("bin/dblp.dtd");
	}

}
