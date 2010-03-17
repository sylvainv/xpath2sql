import jdsl.core.api.*;
import jdsl.core.ref.*;
import jdsl.graph.api.Vertex;
import jdsl.graph.api.VertexIterator;
import jdsl.graph.ref.IncidenceListGraph;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class DTDGraph {
	
	private IncidenceListGraph graph;
	private Vertex root;
	private HashMap<String,Vertex> elements;
	
	DTDGraph(){
		this.elements = new HashMap<String,Vertex>();
		this.graph = new IncidenceListGraph();
		this.root = this.graph.insertVertex("dblp");
		
		// Create proceeding element
		Vertex proceedingElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"proceeding","table"));
		this.graph.attachVertexFrom(proceedingElement,"booktitle","attribute");
		this.graph.attachVertexFrom(proceedingElement,"title","attribute");
		
		// Create inproceedingElement
		Vertex inproceedingElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"inproceeding","table"));
		this.graph.attachVertexFrom(inproceedingElement,"author","attribute");
		this.graph.attachVertexFrom(inproceedingElement,"title","attribute");
		this.graph.attachVertexFrom(inproceedingElement,"booktitle","attribute");
		this.graph.attachVertexFrom(inproceedingElement,"year","attribute");
		
		// Create article element
		Vertex articleElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"proceeding","table"));
		this.graph.attachVertexFrom(articleElement,"author","attribute");
		this.graph.attachVertexFrom(articleElement,"title","attribute");
		this.graph.attachVertexFrom(articleElement,"journal","attribute");
		this.graph.attachVertexFrom(articleElement,"year","attribute");
		
		VertexIterator ite = this.graph.vertices();
		while(ite.hasNext()){
			Vertex v = ite.nextVertex();
			this.elements.put(v.element().toString(),v);
		}

	}
	
	/*// Return whether node is a table or an attribute
	public String mapping(String node){
		return this.graph.anIncidentEdge(this.graph.);
	}
	
	public Vector<String> elements(){
		return this.elements.;
	}*/
		
	@Override
	public String toString() {
		return this.graph.toString();
	}
	
}
