import java.util.Iterator;
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
		newQuery.addSelectItems(query1.getSelect());
		newQuery.addSelectItems(query2.getSelect());
		newQuery.addFromItems(query1.getFrom());
		newQuery.addFromItems(query2.getFrom());
		newQuery.addWhereItems(query1.getWhere());
		newQuery.addWhereItems(query2.getWhere());
		return newQuery;
	}
	
	public void merge(RelationalQuery query){
		this.addSelectItems(query.getSelect());
		this.addFromItems(query.getFrom());
		this.addWhereItems(query.getWhere());
	}	
	
	public String toString(){
		String str = "SELECT ";
		if(!this.select.isEmpty()){
			Iterator<SelectItem> selectIte = this.select.iterator();
			while(selectIte.hasNext()){str+=selectIte.next().toString()+", ";}
			str = str.substring(0, str.length()-2);
		}
		else{
			str += "*";
		}
		str += " FROM ";
		Iterator<FromItem> fromIte = this.from.iterator();
		while(fromIte.hasNext()){str+=fromIte.next().toString()+", ";}
		str = str.substring(0, str.length()-2);
		if(!this.where.isEmpty()){
			
			str += " WHERE ";
			Iterator<WhereItem> whereIte = this.where.iterator();
			while(whereIte.hasNext()){str+=whereIte.next().toString()+" AND ";}
		}
		return str;
	}
	
	public void addSelectItem(SelectItem e){
		if(!this.select.contains(e))
			this.select.add(e);
	}
	
	public void addSelectItems(Vector<SelectItem> v){
		Iterator<SelectItem> iter = v.iterator();
		while(iter.hasNext()){
			this.addSelectItem(iter.next());
		}
	}
	
	public void addFromItem(FromItem e){
		if(!this.from.contains(e)){
			this.from.add(e);
		}
	}
	
	public void addFromItems(Vector<FromItem> v){
		Iterator<FromItem> iter = v.iterator();
		while(iter.hasNext()){
			this.addFromItem( iter.next());
		}
	}
	
	public void addWhereItem(WhereItem e){
		if(!this.where.contains(e))
			this.where.add(e);
	}
	
	public void addWhereItems(Vector<WhereItem> v){
		Iterator<WhereItem> iter = v.iterator();
		while(iter.hasNext()){
			this.addWhereItem(iter.next());
		}
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
	
	public void cleanUp(){
		Iterator<SelectItem> selectIter = this.select.iterator();
		Vector<String> toBeRemoved = new Vector<String>();
		while(selectIter.hasNext()){
			SelectItem select = selectIter.next();
			String[] split = select.getLabel().split("\\.");
			if(!this.from.contains(new FromItem(split[0]))){
				toBeRemoved.add(split[0]+"."+split[1]);
			}
		}
		Iterator<String> iter = toBeRemoved.iterator();
		while(iter.hasNext()){
			this.select.removeElement(new SelectItem(iter.next()));
		}
	}
}
