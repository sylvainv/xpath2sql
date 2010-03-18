
public class FromItem{

	private String label;
	private JoinItem join;
	
	public FromItem(String label){
		this.label = label;
		this.join = null;
	}
	
	public FromItem(JoinItem join){
		this.label = "";
		this.join = join;
	}	

	public String toString(){
		return this.label.isEmpty()?this.join.toString():this.label;
	}

	public String getLabel() {
		return this.label.isEmpty()?this.join.toString():this.label;
	}

	@Override
	public boolean equals(Object item) {
		if(this.label.isEmpty()){
			return this.label.equals(((FromItem)item).toString());
		}
		else{
			return this.label.equals(((FromItem)item).getLabel());
		}
	}
}
