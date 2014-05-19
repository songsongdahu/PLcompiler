import java.util.ArrayList;

import others.DAGtree;
import others.triAddState;


public class Code_optimizer {
	
	public ArrayList<ArrayList<triAddState>> optimizer(ArrayList<triAddState> triTable){
		ArrayList<ArrayList<triAddState>> oldTable = classify(triTable);
		/*ArrayList<ArrayList<triAddState>> newTable = new ArrayList<ArrayList<triAddState>>();
		
		for(int i=0;i<oldTable.size();i++){
			ArrayList<triAddState> tempTable = oldTable.get(i);
			tempTable = DAGoptimizer(tempTable);
			newTable.add(tempTable);
		}
		System.out.println("This is the answer of Code_optimizer");
		for(int i=0;i<newTable.size();i++){
			for(int j=0;j<newTable.get(i).size();j++){
				System.out.println(newTable.get(i).get(j));
			}
			System.out.println();
		}*/
		return oldTable;
	}
	
	public ArrayList<ArrayList<triAddState>> classify(ArrayList<triAddState> triTable){
		ArrayList<ArrayList<triAddState>> rtnTable = new ArrayList<ArrayList<triAddState>>();
		ArrayList<Integer> entr = new ArrayList<Integer>();
		entr.add(100);
		entr.add(100+triTable.size());
		for(int i=0;i<triTable.size();i++){
			if(triTable.get(i).getOpt().charAt(0)=='j'){
				entr.add(Integer.parseInt(triTable.get(i).getX()));
				entr.add(triTable.get(i).getSeq()+1);
			}
		}
		for(int i=0;i<entr.size();i++){
			for(int j=0;j<entr.size()-1;j++){
				if(entr.get(j)>entr.get(j+1)){
					//交换
					int temp = entr.get(j);
					entr.set(j, entr.get(j+1));
					entr.set(j+1, temp);
				} else if(entr.get(j)==entr.get(j+1)){
					entr.remove(j);
				}
			}
		}
		for(int i=0;i<entr.size()-1;i++){
			ArrayList<triAddState> temp = new ArrayList<triAddState>();
			for(int j=entr.get(i);j<entr.get(i+1);j++){
				temp.add(triTable.get(j-100));
			}
			rtnTable.add(temp);
		}
		System.out.println("This is the answer of classify");
		for(int i=0;i<rtnTable.size();i++){
			for(int j=0;j<rtnTable.get(i).size();j++){
				System.out.println(rtnTable.get(i).get(j));
			}
			System.out.println();
		}
		return rtnTable;
	}
	
	public ArrayList<triAddState> DAGoptimizer(ArrayList<triAddState> triTable){
		ArrayList<DAGtree> treeList = new ArrayList<DAGtree>();
		int treeCount = 1;
		for(int i=0;i<triTable.size();i++){
			//获取三地址语句
			triAddState tempTAS = triTable.get(i);
			DAGtree NodeX = null, NodeY = null, NodeZ = null;
			
			//如果NODE(Y)无定义则构造一个标记为Y的叶节点
			int definedY = ifExist(tempTAS.getY(), treeList);
			if(definedY==-1){
				NodeY = new DAGtree(treeCount,new ArrayList<String>());
				NodeY.addNode(tempTAS.getY());
				treeCount++;
				treeList.add(NodeY);
 			} else {
				NodeY = treeList.get(definedY);
			}
			if(tempTAS.getOpt().equals("=")){
			//0型
				addNodeX(treeList,NodeY,tempTAS);
			} else if(tempTAS.getOpt().equals("-")&&tempTAS.getZ().equals("")){
			//1型
				try{
					//执行op Y,此处op一定为"-",数字一定在第一个
					int tempInt = -Integer.parseInt(NodeY.getNodes().get(0));
					//如果是新构造出来的node则删除
					if(definedY==-1){
						treeList.remove(treeList.size()-1);
						treeCount--;
					}
					int definedP = ifExist(tempInt+"",treeList);//-Y
					//如果没有定义-Y
					if(definedP==-1){
						NodeX = new DAGtree(treeCount,new ArrayList<String>());
						NodeX.addNode(tempInt+"");
						treeCount++;
						treeList.add(NodeX);
					}
					addNodeX(treeList,NodeX,tempTAS);
				} catch(Exception e) {
					//检查公共子表达式
					boolean hasX = false;
					for(int j=0;j<treeList.size();j++){
						if(ifExist(tempTAS.getY(),treeList.get(j).getChild1())&&treeList.get(j).getChild2()==null&&treeList.get(j).getOpt().equals("-")){
							NodeX = treeList.get(j);
							hasX = true;
						}
					}
					if(!hasX){
						//没有则构造
						NodeX = new DAGtree(treeCount,new ArrayList<String>());
						NodeX.setChild(NodeY, null);
						NodeX.setOpt("-");
						treeCount++;
						treeList.add(NodeX);
					}
					addNodeX(treeList,NodeX,tempTAS);
				} 
			}else if(tempTAS.getOpt().charAt(0)!='j') {
			//2型
				int definedZ = ifExist(tempTAS.getZ(), treeList);
				if(definedZ==-1){
					NodeZ = new DAGtree(treeCount,new ArrayList<String>());
					NodeZ.addNode(tempTAS.getZ());
					treeCount++;
					treeList.add(NodeZ);
				} else {
					NodeZ = treeList.get(definedZ);
				}
				try{
					int tempInt = 0;
					int tempInt1 = Integer.parseInt(NodeY.getNodes().get(0));
					int tempInt2 = Integer.parseInt(NodeZ.getNodes().get(0));
					switch(tempTAS.getOpt()){
					case "+":
						tempInt = tempInt1 + tempInt2;
						break;
					case "-":
						tempInt = tempInt1 - tempInt2;
						break;
					case "*":
						tempInt = tempInt1 * tempInt2;
						break;
					case "/":
						tempInt = tempInt1 / tempInt2;
						break;
					}
					if(definedZ==-1){
						treeList.remove(treeList.size()-1);
						treeCount--;
					}
					if(definedY==-1){
						treeList.remove(treeList.size()-1);
						treeCount--;
					}
					int definedP = ifExist(tempInt+"",treeList);//P = Y op Z
					//如果没有定义P
					if(definedP==-1){
						NodeX = new DAGtree(treeCount,new ArrayList<String>());
						NodeX.addNode(tempInt+"");
						treeCount++;
						treeList.add(NodeX);
					} else {
						NodeX = treeList.get(definedP);
					}
					addNodeX(treeList,NodeX,tempTAS);
				} catch(Exception e) {
					//检查公共子表达式
					boolean hasX = false;
					for(int j=0;j<treeList.size();j++){
						if((ifExist(tempTAS.getY(),treeList.get(j).getChild1())&&ifExist(tempTAS.getZ(),treeList.get(j).getChild2())||
								ifExist(tempTAS.getY(),treeList.get(j).getChild2())&&ifExist(tempTAS.getZ(),treeList.get(j).getChild1()))
								&&treeList.get(j).getOpt().equals(tempTAS.getOpt())){
							NodeX = treeList.get(j);
							hasX = true;
						}
					}
					if(!hasX){
						//没有则构造
						NodeX = new DAGtree(treeCount,new ArrayList<String>());
						NodeX.setChild(NodeY, NodeZ);
						NodeX.setOpt(tempTAS.getOpt());
						treeCount++;
						treeList.add(NodeX);
					}
					addNodeX(treeList,NodeX,tempTAS);
				}
			} else {
				treeList.add(new DAGtree(-1,tempTAS));
			}
		}
		for(int i=0;i<treeList.size();i++){
			System.out.println(treeList.get(i));
		}
		
		ArrayList<triAddState> rtnTable = new ArrayList<triAddState>();
		int triSeq = 100;
		for(int i=0;i<treeList.size();i++){
			if(treeList.get(i).getId()==-1){
				rtnTable.add(treeList.get(i).getOthTas());
			} else {
				if(treeList.get(i).getChild1()!=null&&treeList.get(i).getChild2()!=null){
					rtnTable.add(new triAddState(triSeq, treeList.get(i).getNodes().get(0), treeList.get(i).getChild1().getNodes().get(0), treeList.get(i).getChild2().getNodes().get(0), treeList.get(i).getOpt()));
					triSeq++;
				} else if(treeList.get(i).getChild1()!=null&&treeList.get(i).getChild2()==null){
					rtnTable.add(new triAddState(triSeq, treeList.get(i).getNodes().get(0), treeList.get(i).getChild1().getNodes().get(0), "", treeList.get(i).getOpt()));
					triSeq++;
				}
				if(treeList.get(i).getNodes().size()>1){
					for(int j=1;j<treeList.get(i).getNodes().size();j++){
						try{
							Integer.parseInt(treeList.get(i).getNodes().get(0));
							rtnTable.add(new triAddState(triSeq, treeList.get(i).getNodes().get(j), treeList.get(i).getNodes().get(0), "", "="));
							triSeq++;
						} catch(Exception e){
							rtnTable.add(new triAddState(triSeq, treeList.get(i).getNodes().get(j), treeList.get(i).getNodes().get(j-1), "", "="));
							triSeq++;
						}
					}
				}
			}
		}
		
		return rtnTable;
	}
	
	public int ifExist(String a, ArrayList<DAGtree> s){
		for(int i=0;i<s.size();i++){
			for(int j=0;j<s.get(i).getNodes().size();j++){
				if(a.equals(s.get(i).getNodes().get(j))){
					return i;
				}
			}
		}
		return -1;
	}
	
	public boolean ifExist(String a, DAGtree s){
		if(s==null){
			return false;
		}
		for(int i=0;i<s.getNodes().size();i++){
			if(a.equals(s.getNodes().get(i))){
				return true;
			}
		}
		return false;
	}
	
	public void addNodeX(ArrayList<DAGtree> treeList, DAGtree Node, triAddState tempTAS){
		//如果Node(X)无定义
		int definedX = ifExist(tempTAS.getX(), treeList);
		if(definedX==-1){
			Node.addNode(tempTAS.getX());
		} else {
			//如果X是叶结点则不删除
			boolean isLeaf = false;
			for(int j=0;j<treeList.size()-1;j++){
				if(ifExist(tempTAS.getX(), treeList.get(j).getChild1())||ifExist(tempTAS.getX(), treeList.get(j).getChild2())){
					isLeaf = true;
				}
			}
			if(!isLeaf){
				treeList.get(definedX).removeNode(tempTAS.getX());
			}
			Node.addNode(tempTAS.getX());
		}
	}
	
	public static void main(String[] args) {
		ArrayList<triAddState> triTable = new ArrayList<triAddState>();
		triAddState s1 = new triAddState(100,"T0","3","","=");
		triAddState s2 = new triAddState(101,"T1","2","T0","*");
		triAddState s3 = new triAddState(102,"T2","R","r","+");
		triAddState s4 = new triAddState(103,"A","T1","T2","*");
		triAddState s5 = new triAddState(104,"B","A","","=");
		triAddState s6 = new triAddState(105,"T3","2","T0","*");
		triAddState s7 = new triAddState(106,"T4","R","r","+");
		triAddState s8 = new triAddState(107,"T5","T3","T4","*");
		triAddState s9 = new triAddState(108,"T6","R","r","-");
		triAddState s10 = new triAddState(109,"B","T5","T6","*");
		triTable.add(s1);
		triTable.add(s2);
		triTable.add(s3);
		triTable.add(s4);
		triTable.add(s5);
		triTable.add(s6);
		triTable.add(s7);
		triTable.add(s8);
		triTable.add(s9);
		triTable.add(s10);
		for(int i=0;i<triTable.size();i++){
			System.out.println(triTable.get(i));
		}
		new Code_optimizer().DAGoptimizer(triTable);
	}
}
