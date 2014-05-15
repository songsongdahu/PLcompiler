import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import others.*;


public class Grammar_analyzer {
	String[] sym1 = {
			"<����>","<�ֳ���>","<����˵������>","<����˵������>","<����˵������>",
			"<���>","<��������>","<��������ADD>","IDENT","NUMBER",
			"<��ʶ��ADD>","<�����ײ�>","<��ֵ���>","<�������>","<����ѭ�����>",
			"<���̵������>","<�����>","<д���>","<�������>","<���ʽ>",
			"<���ADD>","<��ϵ�����>","<��>","<�Ӽ�������>","<�Ӽ������>",
			"<����>","<�˳�������>","<�˳������>","<����>",//29�����ս��
			"SYM_const","SYM_var","SYM_procedure","SYM_begin","SYM_end","SYM_ood","SYM_if","SYM_then","SYM_call","SYM_while","SYM_do","SYM_read","SYM_write",
			"SYM_+","SYM_-","SYM_*","SYM_/","SYM_=","SYM_#","SYM_<","SYM_<=","SYM_>","SYM_>=","SYM_.","SYM_:=","SYM_,","SYM_;","SYM_(","SYM_)","$"//13+17���ս��
			};
	private String[][] table;
	private Lexical_analyzer la;
	
	//�﷨����
	public void analyze(LR1Items G, Semantic_analyzer sa){
		//��ʼ��
		Stack<String> statuStack = new Stack<String>();
		Stack<item> inputStack = new Stack<item>();
		statuStack.push("0");
		LAS input = getword();
		
		String state = "0";
		while(state!=null){
			String act = ""+table[Integer.parseInt(state)][rtnPos(sym1,input.getSym())];
			if(act.charAt(0)=='s'){
				//����
				statuStack.push(act.substring(1));
				state = act.substring(1);
				inputStack.push(toItem(input));
				input = getword();
			} else if(act.charAt(0)=='r'){
				//��Լ
				String sts = "";
				String str = G.get(Integer.parseInt(act.substring(1))).getLeftsymbol().getDsb();
				
				ArrayList<item> forSaItem = new ArrayList<item>();
				if(G.get(Integer.parseInt(act.substring(1))).getProduction().get(0).getTml()==2){
					//�չ�Լ
				} else {
					for(int i=0;i<G.get(Integer.parseInt(act.substring(1))).getProduction().size();i++){
						statuStack.pop();
						forSaItem.add(inputStack.pop());
					}
				}
				//�������
				inputStack.push(sa.analyzer(G, Integer.parseInt(act.substring(1)), forSaItem));
				
				sts = statuStack.pop();
				statuStack.push(sts);
				String st = table[Integer.parseInt(sts)][rtnPos(sym1,str)];
				statuStack.push(st.substring(1));
				state = st.substring(1);
			} else if(act.equals("acc")){
				//����
				System.out.println("Accept!");
				state = null;
			} else {
				//����
				System.out.println("Error!");
				System.out.println(Integer.parseInt(state));
				System.out.println(rtnPos(sym1,input.getSym()));
				state = null;
			}
		}
	}
	
	public LAS getword(){
		return la.getWord(); 
	}
	
	public int rtnPos(String[] s, String a){
		for(int i=0;i<s.length;i++){
			if(a.equals(s[i])){
				return i;
			}
		}
		return -1;
	}
	
	public static LR1Items readtxt(){
		LR1Items G = new LR1Items(999);
		try {
			File f = new File("PL0pro.txt");
			BufferedReader br = new BufferedReader(new FileReader(f));
			String nl = br.readLine();
			while(nl!=null){
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
				nl = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return G;
	}	
	
	public void initTable(String[][] t){
		this.table = t;
	}
	
	public void initInput(String s){
		this.la = new Lexical_analyzer(s);
		this.la.init();
	}
	
	public void printLA(){
		this.la.getsys();
		this.la.print();
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
	
	public item toItem(LAS las){
		if(las.getSym().equals("IDENT")){
			return new item(las.getSym(), las.getId(), "");
		} else if(las.getSym().equals("NUMBER")) {
			return new item(las.getSym(), las.getNum(), "");
		} else {
			return new item(las.getSym(), "", "");
		}
	}
	
	public static void main(String[] args) {
		LR1Items G = readtxt();
		Grammar_analyzer ga = new Grammar_analyzer();
		LR1 lr = new LR1();
		ga.initTable(lr.items(G));
		
		//��ȡ����
		String str = "";
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input:");
		while(scan.hasNextLine()){
			String next = scan.nextLine();
			if(next.equals("END"))
				break;
			str +='\n'+next;
		}
		System.out.println(str);
		ga.initInput(str);
		Semantic_analyzer sa = new Semantic_analyzer();
		ga.analyze(G,sa);
		sa.print();
		Code_optimizer co = new Code_optimizer();
		co.optimizer(sa.getTriTable());
	}
}
