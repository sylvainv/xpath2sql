import jdsl.core.api.*;
import jdsl.core.ref.*;
import jdsl.graph.api.Vertex;
import jdsl.graph.ref.IncidenceListGraph;

import com.wutka.dtd.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class DTDGraph {
	
	private String path;
	private File file;
	private Reader dtdreader;
	private DTDParser parser;
	private IncidenceListGraph graph;
	private Vertex root;
	
	DTDGraph(String path2DTD){
		this.graph = new IncidenceListGraph();
		this.root = this.graph.insertVertex(new String("publish"));
		this.graph.attachVertexFrom(this.root,"p.author","");
		this.graph.attachVertexFrom(this.root,"p.title","");
		Vertex publishID = this.graph.opposite(this.root,this.graph.attachVertexFrom(this.root,"p.id",""));
		this.graph.attachVertexFrom(this.root,"p.year","");
		
		Vertex conferenceElement = this.graph.opposite(publishID,this.graph.attachVertexFrom(publishID,"conference",""));
		this.graph.attachVertexFrom(conferenceElement,"c.id","");
		this.graph.attachVertexFrom(conferenceElement,"c.name","");
		
		Vertex journalElement = this.graph.opposite(publishID,this.graph.attachVertexFrom(publishID,"journal",""));
		this.graph.attachVertexFrom(journalElement,"j.id","");
		this.graph.attachVertexFrom(journalElement,"j.name","");
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
	
	@Override
	public String toString() {
		return this.graph.toString();
	}
	
}
