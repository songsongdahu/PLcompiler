package others;

//����ַ���
public class triAddState {
	public int seq;//���
	public String x;//���
	public String y;//����1
	public String z;//����2
	public String opt;//�����
	
	public triAddState(int seq, String x, String y, String z, String opt){
		this.seq = seq;
		this.x = x;
		this.y = y;
		this.z = z;
		this.opt = opt;
	}
	
	public void setX(String x){
		this.x = x;
	}
	
	public String toString(){
		if(z.equals("")){
			return seq+"\t"+opt+"\t"+y+"\t\t"+x;
		} else {
			return seq+"\t"+opt+"\t"+y+"\t"+z+"\t"+x;
		}
	}
}
