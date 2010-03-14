import java.util.Vector;

import jdsl.core.ref.ToString;


public class RelationalQuery {

	private Vector<SelectItem> select;
	private Vector<FromItem> from;
	private Vector<WhereItem> where;
	
	public RelationalQuery(){
		this.select = new Vector<SelectItem>();
		this.from = new Vector<FromItem>();
		this.where = new Vector<WhereItem>();
	}
	
	public static RelationalQuery merge(RelationalQuery query1, RelationalQuery query2){
		RelationalQuery newQuery = new RelationalQuery();
		Vector<SelectItem> select1 = query1.getSelect();
		Vector<SelectItem> select2 = query2.getSelect();
		Vector<FromItem> from1 = query1.getFrom();
		Vector<FromItem> from2 = query2.getFrom();
		
		int sizeSelect1 = select1.size();
		int sizeSelect2 = select2.size();
		int sizeFrom1 = from1.size();
		int sizeFrom2 = from2.size();

		if(!from1.isEmpty()){
			for(int i=0;i<sizeSelect1;i++){
				newQuery.addSelectItem(
						(SelectItem) (select1.get(i).isSubQuery()?
								select1.get(i).getQuery():
								new SelectItem(from1.get(0).getLabel())));
			}
		}
		if(!from2.isEmpty()){
			for(int i=0;i<sizeSelect2;i++){
				newQuery.addSelectItem(
						(SelectItem) (select2.get(i).isSubQuery()?
								select2.get(i).getQuery():
								new SelectItem(from2.get(0).getLabel())));
			}
		}
		newQuery.addFromItems(from1);
		newQuery.addFromItems(from2);
		newQuery.addWhereItems(query1.getWhere());
		newQuery.addWhereItems(query2.getWhere());
		return new RelationalQuery();
	}
	
	public String toString(){
		return "";
	}
	
	public void addSelectItem(SelectItem e){
		this.select.add(e);
	}
	
	public void addFromItem(FromItem e){
		this.from.add(e);
	}
	
	public void addFromItems(Vector<FromItem> v){
		this.from.addAll(v);
	}	
	
	public void addWhereItem(WhereItem e){
		this.where.add(e);
	}
	
	public void addWhereItems(Vector<WhereItem> v){
		this.where.addAll(v);
	}	

	public SelectItem getSelectItem(int index) {
		return select.get(index);
	}

	public FromItem getFromItem(int index) {
		return from.get(index);
	}
	
	public WhereItem getWhere(int index) {
		return where.get(index);
	}

	public Vector<SelectItem> getSelect() {
		return select;
	}

	public Vector<FromItem> getFrom() {
		return from;
	}

	public Vector<WhereItem> getWhere() {
		return where;
	}
}
