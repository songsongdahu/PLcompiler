package LR1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class LR1 {
	//String[] sym1 = {"S","A","a","$"};
	String[] sym1 = {
			"<����>","<�ֳ���>","<����˵������>","<����˵������>","<����˵������>",
			"<���>","<��������>","<��������ADD>","<��ʶ��>","<�޷�������>",
			"<��ʶ��ADD>","<�����ײ�>","<��ֵ���>","<�������>","<����ѭ�����>",
			"<���̵������>","<�����>","<д���>","<�������>","<���ʽ>",
			"<���ADD>","<��ϵ�����>","<��>","<�Ӽ�������>","<�Ӽ������>",
			"<����>","<�˳�������>","<�˳������>","<����>",//29�����ս��
			"const","var","procedure","begin","end","ood","if","then","call","while","do","read","write",
			"+","-","*","/","=","#","<","<=",">",">=",".",":=",",",";","(",")","$"//13+17���ս��
			};

	//һ���ķ����ŵ�First
	public ArrayList<item> First1(ArrayList<item> s,LR1Items G){
		ArrayList<item> result = new ArrayList<item>();
		int i=0;
		while(i<s.size()&&ifExist(new item(2),First2(s.get(i),G))){
			i++;
		}
		for(int j=0;j<i+1;j++){
			if(j<s.size()){
				result.addAll(First2(s.get(j),G));
			}
		}
		//ȥ���ظ����0
		selectDistinct1(result);
		if(i==s.size()){
			//���еĶ������Ƴ����ա�
			result.add(new item(2));
		}
		return result;
	}
	
	//�����ķ����ŵ�First
	public ArrayList<item> First2(item s,LR1Items G){
		ArrayList<item> result = new ArrayList<item>();
		if(isTerminal(s)){
			//�ս����
			result.add(s);
		} else {
			//���ս����
			for(int i=0;i<G.size();i++){
				//����s->xxx�Ĳ���ʽ
				if(G.get(i).getLeftsymbol().equals(s)){
					int j=0;
					ArrayList<item> pro = G.get(i).getProduction();
					//����s->0�Ĳ���ʽ
					if(pro.get(0).getTml()==2){
						result.add(new item(2));
					} else {
						while(j<pro.size()&&ifExist(new item(2),First2(pro.get(j),G))){				
							j++;
						}
						for(int k=0;k<j+1;k++){
							if(k<pro.size()){
								result.addAll(First2(pro.get(k),G));
							}
						}
						//ȥ���ظ����0
						selectDistinct1(result);
						if(j==pro.size()){
							//���еĶ������Ƴ����ա�
							result.add(new item(2));
						}
					}
				}
			}
		}
		return result;
	}
	
	//�հ�
	public ArrayList<LR1Item> Closure(ArrayList<LR1Item> I,LR1Items G){
		Queue<LR1Item> que = new LinkedList<LR1Item>();
		ArrayList<LR1Item> I2 = (ArrayList<LR1Item>) I.clone();
		for(int i=0;i<I2.size();i++){
			que.add(I2.get(i));
		}
		LR1Item tempI = que.poll();
		while(tempI!=null){
			if(!tempI.isTerminal()){
				//����Ƿ��ս��
				for(int i=0;i<G.size();i++){
					if(G.get(i).getLeftsymbol().equals(tempI.getProduction().get(tempI.getPosition()))){
						//��a�洢��s��
						ArrayList<item> s = new ArrayList<item>();
						ArrayList<item> r = new ArrayList<item>();
						for(int j=tempI.getPosition()+1;j<tempI.getProduction().size();j++){
							r.add(tempI.getProduction().get(j));
						}
						for(int j=0;j<tempI.getLookahead().size();j++){
							ArrayList<item> t = (ArrayList<item>)r.clone();
							t.add(tempI.getLookahead().get(j));
							t = First1(t,G);
							s.addAll(t);
						}
						selectDistinct1(s);
						//��B->.��,b���뵽����I��
						LR1Item tempJ = new LR1Item(G.get(i).getLeftsymbol(), G.get(i).getProduction(), G.get(i).getLookahead(), G.get(i).getPosition());
						tempJ.setLookahead(s);
						que.add(tempJ);
					}
				}
			}
			I2.add(tempI);
			tempI = que.poll();
		}
		selectDistinct2(I2);
		return I2;
	}
	
	//GOTO
	public ArrayList<LR1Item> Goto(LR1Items I, String x, LR1Items G){
		ArrayList<LR1Item> J = new ArrayList<LR1Item>();
		for(int i=0;i<I.size();i++){
			//��ֹǳclone
			LR1Item temp = new LR1Item(I.get(i).getLeftsymbol(), I.get(i).getProduction(), I.get(i).getLookahead(), I.get(i).getPosition());
			if(temp.getPosition()<temp.getProduction().size()){
				if(temp.getProduction().get(temp.getPosition()).getDsb().equals(x)){
					temp.setPosition(temp.getPosition()+1);
					J.add(temp);
				}
			}
		}
		return Closure(J, G);
	}
	
	//main
	public String[][] items(LR1Items G){
		// initialize
		String[][] table = new String[275][sym1.length];
		ArrayList<item> sub1 = new ArrayList<item>();
		sub1.add(G.get(0).getLeftsymbol());
		ArrayList<item> s1lh = new ArrayList<item>();
		s1lh.add(new item(0,"$"));
		LR1Item s1 = new LR1Item(new item(1,"S'"),sub1,s1lh);
		LR1Items s = new LR1Items(0);
		s.add(s1);
		s.setArray(Closure(s.getArray(), G));
		ArrayList<LR1Items> C = new ArrayList<LR1Items>();
		C.add(s);
		
		// loop
		int num = 1;
		for(int i=0;i<C.size();i++){
			//�ж��Ƿ�Ϊ��Լ��
			boolean gy = false;
			int gynum = 0;
			for(int j=0;j<C.get(i).size();j++){
				if(C.get(i).get(j).getPosition()==C.get(i).get(j).getProduction().size()||(C.get(i).get(j).getPosition()==0&&C.get(i).get(j).getProduction().get(0).getTml()==2)){
					gy = true;
					gynum = j;
				}
			}
			//�ǹ�Լ���rx�������
			if(gy){
				LR1Item temp = C.get(i).get(gynum);
				for(int j=0;j<temp.getLookahead().size();j++){
					for(int k=0;k<sym1.length;k++){
						//�жϹ�Լ�������һ��
						if(temp.getLookahead().get(j).getDsb().equals(sym1[k])){
							//Ѱ�ҹ�Լ�ڼ�����Ŀ
							if(temp.getLeftsymbol().getDsb().equals("S'")){
								table[i][k] = "acc";
							} else {
								for(int l=0;l<G.size();l++){
									if(temp.equalsExcpLa(G.get(l))){
										table[i][k] = "r"+l;
									}
								}
							}
						}
					}
				}
			}
			for(int j=0;j<sym1.length;j++){
				ArrayList<LR1Item> temp = Goto(C.get(i), sym1[j], G);
				LR1Items temps = new LR1Items(num);
				temps.setArray(temp);
				
				//����ǿղ��Ҳ���C����rtnΪtrue
				boolean rtn = true;
				if(temp.size()==0){
					rtn = false;
				} else {
					for(int k=0;k<C.size();k++){
						//GOTO(I,X)��C��
						if(temps.equals(C.get(k))){
							table[i][j] = "s"+k;
							rtn = false;
						}
					}
				}
				if(rtn){
					C.add(temps);
					table[i][j] = "s"+(C.size()-1);
					num++;
				}
		
			}
		}
		//System.out.println(C);
		//printTable(table);
		return table;
	}
	
	public void printTable(String[][] t){
		System.out.print("\t");
		for(int i=0;i<t[0].length;i++){
			System.out.print(sym1[i]+"\t");
		}
		System.out.println();
		for(int i=0;i<t.length;i++){
			System.out.print("I"+i+"\t");
			for(int j=0;j<t[0].length;j++){
				System.out.print(t[i][j]+"\t");
			}
			System.out.println();
		}
	}
	
	//�ж��Ƿ�Ϊ�ս��
	public boolean isTerminal(item s){
		if(s.getTml()==0){
			return true;
		} else {
			return false;
		}
	}
	
	//�ж��ַ��Ƿ���������
	public boolean ifExist(item s,ArrayList<item> a){
		for(int i=0;i<a.size();i++){
			if(s.equals(a.get(i))){
				return true;
			}
		}
		return false;
	}
	
	//ȥ���ظ����0
	public void selectDistinct1(ArrayList<item> a){
		for(int i=0;i<a.size();i++){
			if(a.get(i).getTml()==2){
				a.remove(i);
			}
			for(int j=i+1;j<a.size();j++){
				if(a.get(i).equals(a.get(j))){
					a.remove(j);
					j--;
				}
			}
		}
	}
	
	//ȥ���ظ��Ĳ���ʽ
	public void selectDistinct2(ArrayList<LR1Item> a){
		for(int i=0;i<a.size();i++){
			for(int j=i+1;j<a.size();j++){
				if(a.get(i).equals(a.get(j))){
					a.remove(j);
					j--;
				}
			}
		}
	}
	
	//�ж�item�Ƿ��Ѿ�����
	public boolean ifExist(LR1Items lr, ArrayList<LR1Items> ar){
		for(int i=0;i<ar.size();i++){
			if(lr.equals(ar.get(i))){
				return true;
			}
		}
		return false;
	}
	
	//print
	public void printList(ArrayList<String> a){
		String prt = "";
		for(int i=0;i<a.size();i++){
			prt += a.get(i)+" ";
		}
		System.out.println(prt);
	}
	
	//��ȡ����ʽ
	public static LR1Items readInput(){
		LR1Items G = new LR1Items(999);
		Scanner scan = new Scanner(System.in);
		String nl = scan.nextLine();
		while(!nl.equals("")){
			String[] nls = nl.split("@");
			item ls = new item(1, nls[0]);
			ArrayList<item> pro = new ArrayList<item>();
			for(int i=1;i<nls.length;i++){
				item p;
				if(nls[i].equals("0")){
					p = new item(2);
				} else if(nls[i].charAt(0)!='!'){//���ս��
					p = new item(1, nls[i]);
				} else {
					p = new item(0, nls[i].substring(1));
				}
				pro.add(p);
			}
			LR1Item s = new LR1Item(ls, pro);
			G.add(s);
			nl = scan.nextLine();
		}
		scan.close();
		return G;
	}
	
	public static void main(String[] args) {
		LR1Items G = readInput();
		System.out.println(G);
		LR1 la = new LR1();
		System.out.println(la.items(G));
	}
}
