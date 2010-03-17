
public class FromItem{

	private String label;
	
	public FromItem(String label){
		this.label = label;
	}

	public String toString(){
		return this.label;
	}

	public String getLabel() {
		return this.label;
	}

	@Override
	public boolean equals(Object item) {
		return this.label.equals(((FromItem)item).getLabel());
	}
}
