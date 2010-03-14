
public class JoinItem {

	private String label1;
	private String label2;
	private String joinOn;
	
	public JoinItem(String label1, String label2, String joinOn){
		this.label1 = label1;
		this.label2 = label2;
		this.joinOn = joinOn;
	}
	
	public String toString(){
		return this.label1+" JOIN "+this.label2+" ON "+this.label1+"."+this.joinOn+"="+this.label2+"."+this.joinOn;
	}
	
}
