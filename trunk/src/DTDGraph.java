import jdsl.core.api.*;
import jdsl.core.ref.*;
import jdsl.graph.api.EdgeDirection;
import jdsl.graph.api.Vertex;
import jdsl.graph.api.VertexIterator;
import jdsl.graph.ref.IncidenceListGraph;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

public class DTDGraph {
	static public enum Type {ATTRIBUTE, TABLE}
	private IncidenceListGraph graph;
	private Vertex root;
	private HashSet<String> elements;
	private HashMap<String,Vector<String>> mapping;
	private HashMap<Pair<String,String>,String[]> paths;
	
	DTDGraph(){
		this.elements = new HashSet<String>();
		this.mapping = new HashMap<String,Vector<String>>();
		this.paths = new HashMap<Pair<String,String>,String[]>();
		this.graph = new IncidenceListGraph();
		this.root = this.graph.insertVertex("dblp");
		
		// Create proceedings element
		Vertex proceedingsElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"proceedings","table=conference"));
		this.graph.attachVertexFrom(proceedingsElement,"booktitle","attribute=conference.id");
		this.graph.attachVertexFrom(proceedingsElement,"title","attribute=conference.title");
		
		// Create inproceedingsElement
		Vertex inproceedingsElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"inproceedings","table=publish"));
		this.graph.attachVertexFrom(inproceedingsElement,"author","attribute=publish.author");
		this.graph.attachVertexFrom(inproceedingsElement,"title","attribute=publish.title");
		this.graph.attachVertexFrom(inproceedingsElement,"booktitle","attribute=publish.id");
		this.graph.attachVertexFrom(inproceedingsElement,"year","attribute=publish.year");
		
		// Create article element
		Vertex articleElement = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"proceedings","table=publish"));
		this.graph.attachVertexFrom(articleElement,"author","attribute=publish.author");
		this.graph.attachVertexFrom(articleElement,"title","attribute=publish.title");
		this.graph.attachVertexFrom(articleElement,"journal","attribute=publish.id");
		this.graph.attachVertexFrom(articleElement,"year","attribute=publish.year");
		
		// Store elements for mapping, map each node value to the edge it is associated to
		VertexIterator ite = this.graph.vertices();
		while(ite.hasNext()){
			Vertex vertex = ite.nextVertex();
			String key = vertex.element().toString();
			this.elements.add(vertex.element().toString());
			String value = this.graph.anIncidentEdge(vertex).element().toString();
			if(this.mapping.keySet().contains(key)){
				this.mapping.get(key).add(value);
			}
			else{
				Vector<String> list = new Vector<String>();
				list.add(value);
				this.mapping.put(key,list);
			}
		}
		
		// Get all paths from a node A to B (from root to leaves), do that for all nodes
		paths.put(new Pair<String,String>("proceedings","booktitle"),new String[]{"proceedings/booktitle"});
		paths.put(new Pair<String,String>("proceedings","title"),new String[]{"proceedings/title"});
		
		paths.put(new Pair<String,String>("inproceedings","author"),new String[]{"inproceedings/author"});
		paths.put(new Pair<String,String>("inproceedings","booktitle"),new String[]{"inproceedings/booktitle"});
		paths.put(new Pair<String,String>("inproceedings","year"),new String[]{"inproceedings/year"});
		paths.put(new Pair<String,String>("inproceedings","title"),new String[]{"inproceedings/title"});
		
		paths.put(new Pair<String,String>("article","author"),new String[]{"article/author"});
		paths.put(new Pair<String,String>("article","journal"),new String[]{"article/journal"});
		paths.put(new Pair<String,String>("article","year"),new String[]{"article/year"});
		paths.put(new Pair<String,String>("article","title"),new String[]{"article/title"});
		
		paths.put(new Pair<String,String>("dblp","proceedings"),new String[]{"dblp/proceedings"});
		paths.put(new Pair<String,String>("dblp","inproceedings"),new String[]{"dblp/inproceedings"});
		paths.put(new Pair<String,String>("dblp","article"),new String[]{"dblp/article"});
		
		paths.put(new Pair<String,String>("dblp","author"),new String[]{"dblp/inproceedings/author","dblp/article/author"});
		paths.put(new Pair<String,String>("dblp","title"),new String[]{"dblp/proceedings/title","dblp/inproceedings/title","dblp/article/title"});
		paths.put(new Pair<String,String>("dblp","booktitle"),new String[]{"dblp/proceedings/booktitle","dblp/inproceedings/author"});
		paths.put(new Pair<String,String>("dblp","year"),new String[]{"dblp/inproceedings/author","dblp/article/author"});
		paths.put(new Pair<String,String>("dblp","journal"),new String[]{"dblp/article/journal"});
	}
	
	// Get all paths from A to B
	public String[] getPath(String a,String b){
		return this.paths.get(new Pair<String,String>(a,b));
	}

	/*
	private void breadthFirstSearch(Vertex startNode){
		// Get all outgoing node from starting node
		VertexIterator vite = this.graph.adjacentVertices(startNode, EdgeDirection.OUT);
		while(vite.hasNext()){
			HashMap<String,LinkedList<String>> entry = new HashMap<String,LinkedList<String>>();
			LinkedList<String> path = new LinkedList<String>();
			path.add();
			entry.put(vite.nextVertex().element().toString(),path);
			this.paths.put(startNode.element().toString(),);
		}
	}*/
	
	// Return the mapping name or names in the table
	public Vector<String> mapping(String node){
		return this.mapping.get(node);
	}
	
	public boolean isInGraph(String node){
		return this.elements.contains(node);
	}
		
	@Override
	public String toString() {
		return this.graph.toString();
	}
	
}
