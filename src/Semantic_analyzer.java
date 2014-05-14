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
			//<常量定义> -> <标识符>=<无符号整数>
			enter(fsi.get(2).getPlace(), fsi.get(0).getPlace());
			break;
		case 7:
			//<变量说明部分> -> var<标识符><标识符ADD>;
			enter(fsi.get(2).getPlace(), "");
			break;
		case 9:
			//<标识符ADD> -> ,<标识符><标识符ADD>
			enter(fsi.get(1).getPlace(), "");
			break;
		case 11:
			//<过程说明部分> -> <过程首部><分程序>;<过程说明部分>;
			saTable = saTableStack.pop();
			triTable = triTableStack.pop();
			triSeq = 100;
			break;
		case 13:
			//<过程首部> -> procedure<标识符>;
			enter(fsi.get(1).getPlace(),"procedure");
			triTableStack.push(triTable);
			saTableStack.push(saTable);
			triTable = saTable.get(saTable.size()-1).getTriTable();
			saTable = saTable.get(saTable.size()-1).getSaTable();
			break;
		case 22:
			//<赋值语句> -> <标识符>:=<表达式>
			gen("=", fsi.get(0).getPlace(), "", fsi.get(2).getPlace());
			break;
		case 26:
			//<条件> -> <表达式><关系运算符><表达式>
			gen("j"+fsi.get(1).getSym(), fsi.get(2).getPlace(), fsi.get(0).getPlace(), triSeq+2+"");
			gen("j", "", "", "!!!");
			backStack.push(triSeq-1);
			break;
		case 27:
			//<条件> -> ood<表达式>
			String tempOod = newTemp();
			gen("%", "2", fsi.get(0).getPlace() ,tempOod);
			gen("j=","0",tempOod,triSeq+2+"");
			gen("j", null, null, "0");
			break;
		case 28:
			//<表达式> -> <项><加减运算项>
			if(fsi.get(0).getPlace()!=null){
				rtn.setPlace(newTemp());
				gen(fsi.get(0).getSym(), fsi.get(1).getPlace(), fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				rtn.setPlace(fsi.get(1).getPlace());
			}
			break;
		case 29:
			//<表达式> -> +<项><加减运算项>
			if(fsi.get(0).getPlace()!=null){
				rtn.setPlace(newTemp());
				gen(fsi.get(0).getSym(), fsi.get(1).getPlace(), fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				rtn.setPlace(fsi.get(1).getPlace());
			}
			break;
		case 30:
			//<表达式> -> -<项><加减运算项>
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
			//<加减运算项> -> <加减运算符><项><加减运算项>
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
			//<项> -> <因子><乘除运算项>
			if(fsi.get(0).getPlace()!=null){
				rtn.setPlace(newTemp());
				gen(fsi.get(0).getSym(), fsi.get(1).getPlace(), fsi.get(0).getPlace(), rtn.getPlace());
			} else {
				rtn.setPlace(fsi.get(1).getPlace());
			}
			break;
		case 34:
			//<乘除运算项> -> <乘除运算符><因子><乘除运算项>
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
			//<因子> -> <标识符>
			rtn.setPlace(fsi.get(0).getPlace());
			break;
		case 37:
			//<因子> -> <无符号整数>
			rtn.setPlace(fsi.get(0).getPlace());
			break;
		case 38:
			//<因子> -> (<表达式>)
			rtn.setPlace(fsi.get(1).getPlace());
			break;
		case 39:
			//<加减运算符> -> +
			rtn.setSym("+");
			break;
		case 40:
			//<加减运算符> -> -
			rtn.setSym("-");
			break;
		case 41:
			//<乘除运算符> -> *
			rtn.setSym("*");
			break;
		case 42:
			//<乘除运算符> -> /
			rtn.setSym("/");
			break;
		case 43:
			//<关系运算符> -> =
			rtn.setSym("=");
			break;
		case 44:
			//<关系运算符> -> #
			rtn.setSym("#");
			break;
		case 45:
			//<关系运算符> -> <
			rtn.setSym("<");
			break;
		case 46:
			//<关系运算符> -> <=
			rtn.setSym("<=");
			break;
		case 47:
			//<关系运算符> -> >
			rtn.setSym(">");
			break;
		case 48:
			//<关系运算符> -> >=
			rtn.setSym(">=");
			break;
		case 49:
			//<条件语句> -> if<条件>then<语句>
			triTable.get(backStack.pop()-100).setX(triSeq+"");//回填
			break;
		case 50:
			//<过程调用语句> -> call<标识符>
			gen("call","","",fsi.get(0).getPlace());
			break;
		case 51:
			//<当型循环语句> -> while<条件>do<语句>
			int temp = backStack.pop();
			gen("j","","",temp-1+"");
			triTable.get(temp-100).setX(triSeq+"");//回填
			break;
		case 52:
			//<读语句> -> read(<标识符><标识符ADD>)
			break;
		case 53:
			//<写语句> -> write(<标识符><标识符ADD>)
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
