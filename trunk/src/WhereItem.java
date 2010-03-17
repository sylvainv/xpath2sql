
public class WhereItem {

	private RelationalQuery left;
	private String right;
	private String operator;

	public WhereItem(RelationalQuery left,  String operator, String right){
		this.left = left;
		this.right = right;
		this.operator = operator;
	}
	
	public String toString(){
		return "("+this.left+")"+this.operator+"'"+this.right+"'";
	}

}
