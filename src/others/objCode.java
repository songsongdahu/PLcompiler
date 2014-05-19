package others;

public class objCode {
	private String opt;
	private String add1;
	private String add2;
	public objCode(String opt, String add1, String add2){
		this.opt = opt;
		this.add1 = add1;
		this.add2 = add2;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getAdd1() {
		return add1;
	}
	public void setAdd1(String add1) {
		this.add1 = add1;
	}
	public String getAdd2() {
		return add2;
	}
	public void setAdd2(String add2) {
		this.add2 = add2;
	}
	public String toString(){
		return opt+"\t"+add1+"\t"+add2;
	}
}
