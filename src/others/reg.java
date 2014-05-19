package others;

import java.util.ArrayList;

public class reg {
	private String id;
	private ArrayList<String> rvalue;
	
	public reg(String id){
		this.id = id;
		rvalue = new ArrayList<String> ();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<String> getRvalue() {
		return rvalue;
	}

	public void setRvalue(ArrayList<String> rvalue) {
		this.rvalue = rvalue;
	}
	
	public void add(String r){
		rvalue.add(r);
	}
	
	public boolean has(String r){
		for(int i=0;i<rvalue.size();i++){
			if(rvalue.get(i).equals(r)){
				return true;
			}
		}
		return false;
	}
	
	public void remove(String r){
		for(int i=0;i<rvalue.size();i++){
			if(rvalue.get(i).equals(r)){
				rvalue.remove(i);
				break;
			}
		}
	}
	
	public String toString(){
		String rtnStr = id+"\t";
		for(int i=0;i<rvalue.size();i++){
			rtnStr+=rvalue.get(i)+" ";
		}
		return rtnStr;
	}

}
