
public class JoinItem {

	private String table1;
	private String table2;
	private String joinOn1;
	private String joinOn2;
	
	public JoinItem(String label1, String label2, String joinOn1, String joinOn2){
		this.table1 = label1;
		this.table2 = label2;
		this.joinOn1 = joinOn1;
		this.joinOn2 = joinOn2;
	}
	
	public String toString(){
		return this.table1+" JOIN "+this.table2+" ON "+this.joinOn1+"="+this.joinOn2;
	}
	
}
