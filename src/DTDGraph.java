import jdsl.core.api.*;
import jdsl.core.ref.*;
import com.wutka.dtd.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

public class DTDGraph {
	
	private String path;
	private File file;
	private Reader dtdreader;
	private DTDParser parser;
	private DTD dtd;
	
	DTDGraph(String path2DTD){
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
		Hashtable elements = dtd.elements;
		int size = elements.size();
		int i = 0;
		while(elements.keys().hasMoreElements() | i<size){
			System.out.println(elements.keys().nextElement());
			i++;
		}
		for(i=0;i<elements.size();i++){
			System.out.println(elements.get(i));
		}	
	}
	
}
