package others;

import java.util.ArrayList;

public class DAGtree {
	private int id;
	private ArrayList<String> Nodes;
	private DAGtree child1;
	private DAGtree child2;
	private String opt;
	private triAddState othTas;
	
	public DAGtree(int id, ArrayList<String> Nodes){
		this.id = id;
		this.Nodes = Nodes;
	}
	
	public DAGtree(int id, triAddState othTas){
		this.id = id;
		this.setOthTas(othTas);
	}
	
	public int getId(){
		return id;
	}
	
	public String getOpt(){
		return opt;
	}
	
	public void setOpt(String opt){
		this.opt = opt;
	}
	
	public ArrayList<String> getNodes(){
		return Nodes;
	}
	
	public void addNode(String Node){
		Nodes.add(Node);
	}
	
	public void removeNode(String Node){
		for(int i=0;i<Nodes.size();i++){
			if(Node.equals(Nodes.get(i))){
				Nodes.remove(i);
				break;
			}
		}
	}
	
	public DAGtree getChild1(){
		return child1;
	}
	
	public DAGtree getChild2(){
		return child2;
	}
	
	public void setChild(DAGtree child1, DAGtree child2){
		this.child1 = child1;
		this.child2 = child2;
	}
	
	public String toString(){
		String rtn ="n"+id+"\t";
		if(Nodes!=null){
			for(int i=0;i<Nodes.size();i++){
				rtn += Nodes.get(i)+" ";
			}
		}
		if(child1!=null){
			rtn +="\n"+"child1:n"+child1.getId();
		}
		if(child2!=null){
			rtn +="\n"+"child2:n"+child2.getId();
		}
		return rtn;
	}

	public triAddState getOthTas() {
		return othTas;
	}

	public void setOthTas(triAddState othTas) {
		this.othTas = othTas;
	}
}
