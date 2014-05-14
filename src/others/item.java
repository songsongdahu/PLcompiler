package others;

public class item {
	private int tml;//0 �ս�� 1 ���ս�� 2��
	private String dsb;//����
	private String place;//�������
	private String sym;//�������
	
	public item(int tml){
		this.tml = tml;
		this.dsb = "";
	}
	
	public item(int tml, String dsb){
		this.tml = tml;
		this.dsb = dsb;
	}
	
	public item(String dsb, String place, String sym){
		this.tml = 0;
		this.dsb = dsb;
		this.place = place;
		this.sym = sym;
	}
	
	public void setTml(int tml){
		this.tml = tml;
	}
	
	public int getTml(){
		return tml;
	}

	public String getDsb() {
		return dsb;
	}

	public void setDsb(String dsb) {
		this.dsb = dsb;
	}
	
	public boolean equals(item anit){
		if(this.tml==anit.tml&&this.dsb.equals(anit.dsb)){
			return true;
		} else {
			return false;
		}
	}
	
	public String toString(){
		return dsb;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getSym() {
		return sym;
	}

	public void setSym(String sym) {
		this.sym = sym;
	}
}
