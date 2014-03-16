
public class LAS {
	private String sym;
	private String id;
	private String num;
	
	public LAS(String s1, String s2, String s3){
		sym = s1;
		id = s2;
		num = s3;
	}
	
	public String getSym(){
		return sym;
	}
	
	public String getId(){
		return id;
	}
	
	public String getNum(){
		return num;
	}
	
	public void print(){
		System.out.println(sym+"\t"+id+"\t"+num);
	}
}
