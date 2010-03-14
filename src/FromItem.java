
public class FromItem {

	private String label;
	private JoinItem join;
	
	public FromItem(String label){
		this.label = label;
	}
	
	public FromItem(JoinItem join){
		this.join = join;
	}
	
	public String toString(){
		return (this.label=="")?this.join.toString():this.label;
	}

	public String getLabel() {
		return label;
	}

	public JoinItem getJoin() {
		return join;
	}
	
}
