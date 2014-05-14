package others;

import java.util.ArrayList;

public class saSymbol {
	private String id;
	private String value;
	private ArrayList<saSymbol> saTable;
	private ArrayList<triAddState> triTable;
	
	public saSymbol(String id, String value){
		this.id = id;
		this.value = value;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ArrayList<saSymbol> getSaTable() {
		return saTable;
	}
	public void setSaTable(ArrayList<saSymbol> saTable) {
		this.saTable = saTable;
	}
	public String toString(){
		String rtn = "";
		if(saTable!=null){
			rtn = id+"\t"+value+"\n";
			for(int i=0;i<saTable.size();i++){
				rtn += saTable.get(i)+"\n";
			}
			rtn+="\n";
			for(int i=0;i<triTable.size();i++){
				rtn += triTable.get(i)+"\n";
			}
			rtn += "pro "+id+" over";
		} else {
			rtn = id+"\t"+value;
		}
		return rtn;
	}
	public ArrayList<triAddState> getTriTable() {
		return triTable;
	}
	public void setTriTable(ArrayList<triAddState> triTable) {
		this.triTable = triTable;
	}
}
