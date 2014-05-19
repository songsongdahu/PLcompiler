package others;

public class tcSymbol {
	private String id;
	private String info;
	private String avalue;//当前变量的位置
	
	public tcSymbol(String id){
		this.id = id;
		this.setInfo("U,U");
		this.avalue = id;//默认在内存中
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public String toString(){
		return id+" ("+info+")";
	}

	public String getAvalue() {
		return avalue;
	}

	public void setAvalue(String avalue) {
		this.avalue = avalue;
	}
}
