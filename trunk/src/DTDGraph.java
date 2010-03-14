import jdsl.core.api.*;
import jdsl.core.ref.*;
import jdsl.graph.api.Vertex;
import jdsl.graph.ref.IncidenceListGraph;

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
		

	}
		
	@Override
	public String toString() {
		return this.graph.toString();
	}
	
}
