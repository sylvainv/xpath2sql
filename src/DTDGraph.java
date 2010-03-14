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
	
	private IncidenceListGraph graph;
	private Vertex root;
	
	DTDGraph(){
		this.graph = new IncidenceListGraph();
		this.root = this.graph.insertVertex("dblp");
		
		// Create proceeding element
		Vertex proceedingElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"proceeding","*"));
		this.graph.attachVertexFrom(proceedingElement,"booktitle","");
		this.graph.attachVertexFrom(proceedingElement,"title","");
		
		// Create inproceedingElement
		Vertex inproceedingElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"inproceeding",""));
		this.graph.attachVertexFrom(inproceedingElement,"author","");
		this.graph.attachVertexFrom(inproceedingElement,"title","");
		this.graph.attachVertexFrom(inproceedingElement,"booktitle","");
		this.graph.attachVertexFrom(inproceedingElement,"year","");
		
		// Create article element
		Vertex articleElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"proceeding","*"));
		this.graph.attachVertexFrom(articleElement,"author","");
		this.graph.attachVertexFrom(articleElement,"title","");
		this.graph.attachVertexFrom(articleElement,"journal","");
		this.graph.attachVertexFrom(articleElement,"year","");		

	}
		
	@Override
	public String toString() {
		return this.graph.toString();
	}
	
}
