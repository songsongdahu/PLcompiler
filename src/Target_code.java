import java.util.ArrayList;

import others.objCode;
import others.reg;
import others.saSymbol;
import others.tcSymbol;
import others.triAddState;


public class Target_code {
	private ArrayList<tcSymbol> symTable;
	private ArrayList<reg> rvalue;//格式R+数字
	
	public ArrayList<objCode> code_generation(ArrayList<triAddState> table){
		initReg();
		ArrayList<objCode> rtnCode = new ArrayList<objCode>();
		for(int i=0;i<table.size();i++){
			triAddState tempTriAdd = table.get(i);
			if(tempTriAdd.getOpt().equals("=")){
				//赋值
				String regX = getReg(tempTriAdd);
				tcSymbol tempX = getFromSymTable(tempTriAdd.getX());
				tcSymbol tempY = getFromSymTable(tempTriAdd.getY());
				String Y;//如果Y的位置和regX一样则什么都不用做
				if(tempY==null){
					Y = tempTriAdd.getY();
				} else {
					Y = tempY.getAvalue();
				}
				if(!Y.equals(regX)){
					rtnCode.add(new objCode("LD", regX, Y));
				}
				tempX.setAvalue(regX);
				rvalue.get(Integer.parseInt(regX.substring(1))).add(tempX.getId());
				if(!Y.equals(tempTriAdd.getY())&&tempTriAdd.getInfoY().equals("U,U")){
					//如果寄存器中的Y不再被引用
					tempY.setAvalue(tempY.getId());
					rvalue.get(Integer.parseInt(regX.substring(1))).remove(tempY.getId());
				}
			} else if(tempTriAdd.getOpt().substring(0, 1).equals("j")) {
				//跳转
				if(tempTriAdd.getY().equals("")){
					//无条件跳转
					rtnCode.add(new objCode("J", "", tempTriAdd.getX()));
				} else {
					tcSymbol tempY = getFromSymTable(tempTriAdd.getY());
					tcSymbol tempZ = getFromSymTable(tempTriAdd.getZ());
					String Y = tempY.getAvalue(), Z = tempZ.getAvalue();
					if(!Y.equals(tempY.getId())){
						//如果Y在寄存器中
						rtnCode.add(new objCode("CMP", Y, Z));
					} else {
						String regY = getReg(tempTriAdd);
						rtnCode.add(new objCode("LD", regY, Y));
						rtnCode.add(new objCode("CMP", regY, Z));
					}
					rtnCode.add(new objCode("J", tempTriAdd.getOpt().substring(1), tempTriAdd.getX()));
				}
			} else {
				String opt="";
				//四则运算
				switch(tempTriAdd.getOpt()){
				case "+":opt="ADD";break;
				case "-":opt="SUB";break;
				case "*":opt="MUL";break;
				case "/":opt="DIV";break;
				}
				String regX = getReg(tempTriAdd);//存放X现行值的寄存器
				tcSymbol tempX = getFromSymTable(tempTriAdd.getX());
				tcSymbol tempY = getFromSymTable(tempTriAdd.getY());
				tcSymbol tempZ = getFromSymTable(tempTriAdd.getZ());
				String Y,Z;
				if(tempY==null){
					Y = tempTriAdd.getY();
				} else {
					Y = tempY.getAvalue();
				}
				if(tempZ==null){
					Z = tempTriAdd.getZ();
				} else {
					Z = tempZ.getAvalue();
				}
				if(Y.equals(regX)){
					rtnCode.add(new objCode(opt,Y,Z));
				} else {
					rtnCode.add(new objCode("LD", regX, Y));
					rtnCode.add(new objCode(opt, regX, Z));
				}
				if(Y.equals(regX)){
					tempY.setAvalue(tempY.getId());
				}
				if(Z.equals(regX)){
					tempZ.setAvalue(tempZ.getId());
				}
				tempX.setAvalue(regX);
				rvalue.get(Integer.parseInt(regX.substring(1))).add(tempX.getId());
				if(!Y.equals(tempTriAdd.getY())&&tempTriAdd.getInfoY().equals("U,U")){
					//如果寄存器中的Y不再被引用
					tempY.setAvalue(tempY.getId());
					rvalue.get(Integer.parseInt(regX.substring(1))).remove(tempY.getId());
				}
				if(!Z.equals(tempTriAdd.getZ())&&tempTriAdd.getInfoZ().equals("U,U")){
					//如果寄存器中的Z不再被引用
					tempZ.setAvalue(tempZ.getId());
					rvalue.get(Integer.parseInt(regX.substring(1))).remove(tempZ.getId());
				}
			}
		}
		return rtnCode;
	}
	
	public String getReg(triAddState tempTriAdd){
		tcSymbol tempY = getFromSymTable(tempTriAdd.getY());
		String rtnReg="";
		if(tempY!=null){
			if((!tempY.getAvalue().equals(tempY.getId())&&rvalue.get(Integer.parseInt(tempY.getAvalue().substring(1))).getRvalue().size()==1)
				//如果Y在寄存器中,且寄存器中只包含Y
					||tempY.getId().equals(tempTriAdd.getX())
				//或者Y和X为同一标识符
					||tempTriAdd.getInfoY().equals("U,U")){
				//或者Y之后不会被引用
				rtnReg = tempY.getAvalue();
			}
		} else if(!getVoidReg().equals("")){
			rtnReg = getVoidReg();
		} else {
			//随机返回一个
			rtnReg = "R"+ (int)(Math.random()*rvalue.size());
			for(int i=0;i<rvalue.get(Integer.parseInt(rtnReg.substring(1))).getRvalue().size();i++){
				tcSymbol tempM = getFromSymTable(rvalue.get(Integer.parseInt(rtnReg.substring(1))).getRvalue().get(i));
				if(!tempM.equals(tempTriAdd.getX())||
						(tempM.getId().equals(tempTriAdd.getX())&&tempM.getId().equals(tempTriAdd.getZ())&&!tempM.getId().equals(tempTriAdd.getY())&&rvalue.get(Integer.parseInt(rtnReg.substring(1))).has(tempM.getId()))){
					if(tempM.equals(tempM.getId().equals(tempTriAdd.getY()))||(tempM.getId().equals(tempTriAdd.getZ())&&rvalue.get(Integer.parseInt(rtnReg.substring(1))).has(tempTriAdd.getY()))){
						tempM.setAvalue(rtnReg);
					}
				}
				rvalue.get(Integer.parseInt(rtnReg.substring(1))).remove(tempM.getId());
			}
		}
		return rtnReg;
	}
	
	//生成待用信息和活跃信息
	public void usedInfo(ArrayList<saSymbol> saTable,ArrayList<ArrayList<triAddState>> table,int tempCount){
		symTable = new ArrayList<tcSymbol>();
		for(int i=0;i<saTable.size();i++){
			//取出变量
			if(saTable.get(i).getValue().equals("")){
				symTable.add(new tcSymbol(saTable.get(i).getId()));
			}
		}
		for(int i=1;i<tempCount+1;i++){
			symTable.add(new tcSymbol("T"+i));
		}
		
		for(int i=0;i<table.size();i++){
			ArrayList<triAddState> tempTable = table.get(table.size()-i-1);
			for(int j=0;j<tempTable.size();j++){
				triAddState tempTriAdd = tempTable.get(tempTable.size()-j-1);
				tcSymbol tempX = getFromSymTable(tempTriAdd.getX());
				if(tempX!=null){//理论不会出现tempX==null
					tempTriAdd.setInfoX(tempX.getInfo());
					tempX.setInfo("U,U");
				}
				tcSymbol tempY = getFromSymTable(tempTriAdd.getY());
				if(tempY!=null){
					tempTriAdd.setInfoY(tempY.getInfo());
					tempY.setInfo(tempTriAdd.getSeq()+",A");
				}
				tcSymbol tempZ = getFromSymTable(tempTriAdd.getZ());
				if(tempZ!=null){
					tempTriAdd.setInfoZ(tempZ.getInfo());
					tempZ.setInfo(tempTriAdd.getSeq()+",A");
				}
			}
			//出口设置
			for(int j=0;j<symTable.size();j++){
				String st = symTable.get(j).getInfo().split(",")[0];
				if(!st.equals("U")){
					symTable.get(j).setInfo("U,A");
				}
			}
		}
		for(int i=0;i<table.size();i++){
			for(int j=0;j<table.get(i).size();j++){
				System.out.println(table.get(i).get(j));
			}
			System.out.println();
		}
	}
	
	public void initReg(){
		rvalue = new ArrayList<reg>();
		for(int i=0;i<10;i++){
			rvalue.add(new reg(""+i));
		}
	}
	
	public tcSymbol getFromSymTable(String s){
		for(int i=0;i<symTable.size();i++){
			if(symTable.get(i).getId().equals(s)){
				return symTable.get(i);
			}
		}
		return null;
	}
	
	public String getVoidReg(){
		for(int i=0;i<rvalue.size();i++){
			if(rvalue.get(i).getRvalue().size()==0){
				return "R"+i;
			}
		}
		return "";
	}
}
