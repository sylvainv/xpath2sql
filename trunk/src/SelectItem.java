
public class SelectItem{

	private String label;
	
	public SelectItem(String label){
		this.label = label;
	}

	public String toString(){
		return this.label;
	}
	
	public String getLabel() {
		return label;
	}
	
	@Override
	public boolean equals(Object item) {
		return this.label.equals(((SelectItem)item).getLabel());
	}
}
