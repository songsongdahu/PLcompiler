package others;

public class tcSymbol {
	private String id;
	private String info;
	private String avalue;//��ǰ������λ��
	
	public tcSymbol(String id){
		this.id = id;
		this.setInfo("U,U");
		this.avalue = id;//Ĭ�����ڴ���
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
