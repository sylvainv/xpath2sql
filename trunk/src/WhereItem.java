
public class WhereItem {

	private RelationalQuery left;
	private String right;

	public WhereItem(RelationalQuery left, String right){
		this.left = left;
		this.right = right;
	}
	
	public String toString(){
		return "'"+this.right+"' in "+"("+this.left+")";
	}

}
