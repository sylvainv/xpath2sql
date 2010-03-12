import jdsl.core.api.*;
import jdsl.core.ref.*;
import jdsl.graph.api.Vertex;
import jdsl.graph.ref.IncidenceListGraph;

import com.wutka.dtd.*;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class DTDGraph {
	
	private String path;
	private File file;
	private Reader dtdreader;
	private DTDParser parser;
	private IncidenceListGraph graph;
	private Vertex root;
	private HashMap<String,HashMap<String,String>> rec;
	private HashMap<String,HashMap<String,String>> reach;
	
	DTDGraph(String path2DTD){
		this.graph = new IncidenceListGraph();
		this.root = this.graph.insertVertex(new String("article"));
		this.graph.attachVertexFrom(this.root,"p.author","attribute");
		this.graph.attachVertexFrom(this.root,"p.title","attribute");
		Vertex publishID = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"p.id","attribute"));
		this.graph.attachVertexFrom(this.root,"p.year","attribute");
		
		Vertex conferenceElement = this.graph.opposite(publishID,this.graph.attachVertexFrom(publishID,"proceeding","*"));
		this.graph.attachVertexFrom(conferenceElement,"c.id","attribute");
		this.graph.attachVertexFrom(conferenceElement,"c.name","attribute");
		
		Vertex journalElement = this.graph.opposite(publishID,this.graph.attachVertexFrom(publishID,"journal","*"));
		this.graph.attachVertexFrom(journalElement,"j.id","attribute");
		this.graph.attachVertexFrom(journalElement,"j.name","attribute");
		
		/*
		this.path = path2DTD;
		this.file = new File(this.path);
		try{
			this.dtdreader = new FileReader(file);
		}
		catch(FileNotFoundException e){
			System.out.println("DTD file was not found:"+e);
		}
		this.parser = new DTDParser(this.dtdreader);
		try{
			dtd = this.parser.parse();
		}
		catch(IOException e){
			System.out.println("IO Exception, no idea what it means, search by yourself.");
		}
		Set elements = dtd.elements.keySet();
		int size = elements.size();
		System.out.println(size);
		int i = 0;
		*/
	}
		
	public void precomputeRec(){
		HashMap<String,String> AJ = new HashMap<String,String>();
		AJ.put("journal", "article/id*/journal");
		rec.put("article",AJ);
		HashMap<String,String> AP = new HashMap<String,String>();
		AP.put("proceeding", "article/id*/proceeding");
		rec.put("article",AP);
	}
	
	public void precomputeReach(){
		HashMap<String,String> AJ = new HashMap<String,String>();
		AJ.put("journal", "article/id*/journal");
		recAB.put("article",AJ);
		HashMap<String,String> AP = new HashMap<String,String>();
		AP.put("proceeding", "article/id*/proceeding");
		recAB.put("article",AP);
	}
	
	@Override
	public String toString() {
		return this.graph.toString();
	}
	
}
