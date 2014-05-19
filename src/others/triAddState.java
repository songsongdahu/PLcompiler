package others;

//����ַ���
public class triAddState {
	public int seq;//���
	public String x;//���
	public String y;//����1
	public String z;//����2
	public String opt;//�����
	public String infoX,infoY,infoZ;
	
	public triAddState(int seq, String x, String y, String z, String opt){
		this.seq = seq;
		this.x = x;
		this.y = y;
		this.z = z;
		this.opt = opt;
		this.infoX="";
		this.infoY="";
		this.infoZ="";
	}
	
	public int getSeq(){
		return seq;
	}
	
	public void setX(String x){
		this.x = x;
	}
	
	public String getX(){
		return x;
	}
	
	public String getY(){
		return y;
	}

	public String getZ(){
		return z;
	}
	
	public String getOpt(){
		return opt;
	}
	
	public void setInfoX(String infoX){
		this.infoX = infoX;
	}
	
	public String getInfoX(){
		return infoX;
	}
	
	public void setInfoY(String infoY){
		this.infoY = infoY;
	}
	
	public String getInfoY(){
		return infoY;
	}
	
	public void setInfoZ(String infoZ){
		this.infoZ = infoZ;
	}
	
	public String getInfoZ(){
		return infoZ;
	}
	
	public String toString(){
		if(z.equals("")){
			return seq+"\t"+opt+"\t"+y+"\t\t"+x+getInfo();
		} else {
			return seq+"\t"+opt+"\t"+y+"\t"+z+"\t"+x+getInfo();
		}
	}
	public String getInfo(){
		return "\t"+infoY+"\t"+infoZ+"\t"+infoX;
	}
}
