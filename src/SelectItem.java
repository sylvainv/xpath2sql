
public class SelectItem{

	private String label;
	private RelationalQuery query;
	
	public SelectItem(String label){
		this.label = label;
		this.query = null;
	}
	
	public SelectItem(RelationalQuery query){
		this.label = "";
		this.query = query;
	}
	
	public String toString(){
		return (label=="")?query.toString():this.label;
	}
	
	public boolean isSubQuery(){
		return this.label=="";
	}

	public String getLabel() {
		return label;
	}

	public RelationalQuery getQuery() {
		return query;
	}
}
