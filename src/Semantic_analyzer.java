import java.util.ArrayList;
import java.util.Stack;

import others.*;

public class Semantic_analyzer {
	ArrayList<triAddState> triTable = new ArrayList<triAddState>();
	ArrayList<saSymbol> saTable = new ArrayList<saSymbol>();
	Stack<ArrayList<saSymbol>> saTableStack = new Stack<ArrayList<saSymbol>>();
	Stack<ArrayList<triAddState>> triTableStack = new Stack<ArrayList<triAddState>>();
	Stack<Integer> backStack = new Stack<Integer>();
	int triSeq = 100;
	int tempCount = 0;
	
	public item analyzer(LR1Items G, int num, ArrayList<item> fsi){
		item rtn = new item(G.get(num).getLeftsymbol().getDsb(), null, null);
		
		switch(num){
		case 6:
			//<��������> -> <��ʶ��>=<�޷�������>
			enter(fsi.get(2).getPlace(), fsi.get(0).getPlace());
			break;
		case 7:
			//<����˵������> -> var<��ʶ��><��ʶ��ADD>;
			enter(fsi.get(2).getPlace(), "");
			break;
		case 9:
			//<��ʶ��ADD> -> ,<��ʶ��><��ʶ��ADD>
			enter(fsi.get(1).getPlace(), "");
			break;
		case 11:
			//<����˵������> -> <�����ײ�><�ֳ���>;<����˵������>;
			saTable = saTableStack.pop();
			triTable = triTableStack.pop();
			triSeq = 100;
			break;
		case 13:
			//<�����ײ�> -> procedure<��ʶ��>;
			enter(fsi.get(1).getPlace(),"procedure");
			triTableStack.push(triTable);
			saTableStack.push(saTable);
			triTable = saTable.get(saTable.size()-1).getTriTable();
			saTable = saTable.get(saTable.size()-1).getSaTable();
			break;
		case 22:
			//<��ֵ���> -> <��ʶ��>:=<���ʽ>
			gen("=", fsi.get(0).getPlace(), "", fsi.get(2).getPlace());
			break;
		case 26:
			//<����> -> <���ʽ><��ϵ�����><���ʽ>
			gen("j"+fsi.get(1).getSym(), fsi.get(2).getPlace(), fsi.get(0).getPlace(), triSeq+2+"");
			gen("j", "", "", "!!!");
			backStack.push(triSeq-1);
			break;
		case 27:
			//<����> -> ood<���ʽ>
			String tempOod = newTemp();
			gen("%", "2", fsi.get(0).getPlace() ,tempOod);
			gen("j=","0",tempOod,triSeq+2+"");
			gen("j", null, null, "0");
			break;
		case 28:
			//<���ʽ> -> <��><�Ӽ�������>
			if(fsi.get(0).getPlace()!=null){
				rtn.setPlace(newTemp());
				gen(fsi.get(0).getSym(), fsi.get(1).getPlace(), fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				rtn.setPlace(fsi.get(1).getPlace());
			}
			break;
		case 29:
			//<���ʽ> -> +<��><�Ӽ�������>
			if(fsi.get(0).getPlace()!=null){
				rtn.setPlace(newTemp());
				gen(fsi.get(0).getSym(), fsi.get(1).getPlace(), fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				rtn.setPlace(fsi.get(1).getPlace());
			}
			break;
		case 30:
			//<���ʽ> -> -<��><�Ӽ�������>
			if(fsi.get(0).getPlace()!=null){
				String tempAdd = newTemp();
				gen("-", fsi.get(1).getPlace(),"", tempAdd);
				rtn.setPlace(newTemp());
				gen(fsi.get(0).getSym(), tempAdd, fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				String tempAdd = newTemp();
				gen("-", fsi.get(1).getPlace(),"", tempAdd);
				rtn.setPlace(tempAdd);
			}
			break;
		case 31:
			//<�Ӽ�������> -> <�Ӽ������><��><�Ӽ�������>
			if(fsi.get(0).getPlace()!=null){
				rtn.setPlace(newTemp());
				rtn.setSym(fsi.get(2).getSym());
				gen(fsi.get(0).getSym(), fsi.get(1).getPlace(), fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				rtn.setPlace(fsi.get(1).getPlace());
				rtn.setSym(fsi.get(2).getSym());
			}
			break;
		case 33:
			//<��> -> <����><�˳�������>
			if(fsi.get(0).getPlace()!=null){
				rtn.setPlace(newTemp());
				gen(fsi.get(0).getSym(), fsi.get(1).getPlace(), fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				rtn.setPlace(fsi.get(1).getPlace());
			}
			break;
		case 34:
			//<�˳�������> -> <�˳������><����><�˳�������>
			if(fsi.get(0).getPlace()!=null){
				rtn.setPlace(newTemp());
				rtn.setSym(fsi.get(2).getSym());
				gen(fsi.get(0).getSym(), fsi.get(1).getPlace(), fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				rtn.setPlace(fsi.get(1).getPlace());
				rtn.setSym(fsi.get(2).getSym());
			}
			break;
		case 36:
			//<����> -> <��ʶ��>
			rtn.setPlace(fsi.get(0).getPlace());
			break;
		case 37:
			//<����> -> <�޷�������>
			rtn.setPlace(fsi.get(0).getPlace());
			break;
		case 38:
			//<����> -> (<���ʽ>)
			rtn.setPlace(fsi.get(1).getPlace());
			break;
		case 39:
			//<�Ӽ������> -> +
			rtn.setSym("+");
			break;
		case 40:
			//<�Ӽ������> -> -
			rtn.setSym("-");
			break;
		case 41:
			//<�˳������> -> *
			rtn.setSym("*");
			break;
		case 42:
			//<�˳������> -> /
			rtn.setSym("/");
			break;
		case 43:
			//<��ϵ�����> -> =
			rtn.setSym("=");
			break;
		case 44:
			//<��ϵ�����> -> #
			rtn.setSym("#");
			break;
		case 45:
			//<��ϵ�����> -> <
			rtn.setSym("<");
			break;
		case 46:
			//<��ϵ�����> -> <=
			rtn.setSym("<=");
			break;
		case 47:
			//<��ϵ�����> -> >
			rtn.setSym(">");
			break;
		case 48:
			//<��ϵ�����> -> >=
			rtn.setSym(">=");
			break;
		case 49:
			//<�������> -> if<����>then<���>
			triTable.get(backStack.pop()-100).setX(triSeq+"");//����
			break;
		case 50:
			//<���̵������> -> call<��ʶ��>
			gen("call","","",fsi.get(0).getPlace());
			break;
		case 51:
			//<����ѭ�����> -> while<����>do<���>
			int temp = backStack.pop();
			gen("j","","",temp-1+"");
			triTable.get(temp-100).setX(triSeq+"");//����
			break;
		case 52:
			//<�����> -> read(<��ʶ��><��ʶ��ADD>)
			break;
		case 53:
			//<д���> -> write(<��ʶ��><��ʶ��ADD>)
			break;
		}
		return rtn;
	}
	
	public void enter(String s1, String s2){
		saSymbol s = new saSymbol(s1,s2);
		if(s2.equals("procedure")){
			s.setSaTable(new ArrayList<saSymbol>());
			s.setTriTable(new ArrayList<triAddState>());
		}
		saTable.add(s);
	}
	
	public void gen(String op, String y, String z, String x){
		triTable.add(new triAddState(triSeq,x,y,z,op));
		triSeq++;
	}
	
	public String newTemp()
	{
		tempCount++;
		return "T"+tempCount;
	}
	
	public void print(){
		System.out.println("This is the answer of saTable");
		for(int i=0;i<saTable.size();i++){
			System.out.println(saTable.get(i));
		}
		System.out.println("This is the answer of triTable");
		for(int i=0;i<triTable.size();i++){
			System.out.println(triTable.get(i));
		}
	}
}
