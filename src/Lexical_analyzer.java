import java.util.ArrayList;
import java.util.Scanner;

public class Lexical_analyzer {
	ArrayList<String> sym;
	ArrayList<String> id;
	ArrayList<String> num;
	char[] symbol ={'+','-','*','/','=','#','(',')',';',','};
	String[] symbolsym ={"SYM_+","SYM_-","SYM_*","SYM_/","SYM_=","SYM_#","SYM_(","SYM_)","SYM_;","SYM_,"};
	String[] reserved ={"const","var","procedure","begin","end","odd","if","then","call","while","do","read","write"};
	String[] reservedsym ={"SYM_const","SYM_var","SYM_procedure","SYM_begin","SYM_end","SYM_odd","SYM_if","SYM_then","SYM_call","SYM_while","SYM_do","SYM_read","SYM_write"};
	int status;
	int pos;
	char[] ch;
	String str;
	String word;
	int err;
	
	public Lexical_analyzer(String str){
		this.str=str;
	}
	
	public char getch(){
		try{
			return ch[pos];
		}catch(ArrayIndexOutOfBoundsException e){
			return ' ';
		}
	}
	
	public char getchByPos(int pos){
		try{
			return ch[pos];
		}catch(ArrayIndexOutOfBoundsException e){
			return ' ';
		}
	}
	
	public void init(){
		//初始化
		sym = new ArrayList<String>();
		id = new ArrayList<String>();
		num = new ArrayList<String>();
		status = 0;
		ch = str.toCharArray();
		pos = 0;
		word = "";
	}
	
	public void getsys(){	
		while(pos<=ch.length){
			char ch_pos = getch();
			if(legal(ch_pos)){
			//如果字符合法
				switch(status){
					case 0:{
					//还未开始读
						word ="";
						if(ch_pos==' '||ch_pos=='\n'||ch_pos=='\t'){
						//如果是空格 换行 或者制表符 继续读下一个
							pos++;
						}else if(ch_pos>='0'&&ch_pos<='9'){
						//如果是数字 转入状态1(开始读数字)
							word += ch_pos;
							status = 1;
							pos++;
						}else if(ch_pos>=97&&ch_pos<=122){
						//如果是字母 转入状态3(开始读标识符or保留字)
							word += ch_pos;
							status = 2;
							pos++;
						}else if(ch_pos=='>'||ch_pos=='<'){
						//如果是>< 转入状态5
							word += ch_pos;
							status = 3;
							pos++;
						}else if(ch_pos==':'){
						//如果是: 转入状态7
							word += ch_pos;
							status = 5;
							pos++;
						}else if(existence(ch_pos,symbol)!=-1){
						//如果是单字节符号 转入状态9
							word += ch_pos;
							status = 7;
						}else if(ch_pos=='.'){
							add("SYM_.",null,null);
							pos++;
							break;
						}
						break;	
					}
					case 1:{
						if(ch_pos>='0'&&ch_pos<='9'){
						//数字 继续读
							word += ch_pos;
							pos++;
						}else if(ch_pos>=97&&ch_pos<=122){
						//字母 报错
							System.out.println("Error:非法字符"+ch_pos);
							break;
						}else{
						//结束
							add("NUMBER",null,word);
							status = 0;
							break;
						}
						break;
					}
					case 2:{
						if(ch_pos>=97&&ch_pos<=122||ch_pos>='0'&&ch_pos<='9'){
						//字母或者数字 继续读
						
							word += ch_pos;
							pos++;
						}else{
						//结束
							if(existence(word,reserved)!=-1){
							//如果是保留字
								add(reservedsym[existence(word,reserved)],null,null);
							}else{
								add("IDENT",word,null);
							}
							status = 0;
							break;
						}
						break;
					}
					case 3:{
						if(ch_pos=='='){
							word += ch_pos;
							status = 4;
						}else if(word.equals(">")){
							add("SYM_>",null,null);	
							status = 0;
						}else if(word.equals("<")){
							add("SYM_<",null,null);
							status = 0;
						}
						break;
					}
					case 4:{
						if(word.equals(">=")){
							add("SYM_>=",null,null);
						}else if(word.equals("<=")){
							add("SYM_<=",null,null);
						}
						status = 0;
						pos++;
						break;
					}
					case 5:{
						if(ch_pos=='='){
							word += ch_pos;
							pos++;
							status = 6;
						}else{
							err = 1; 
							break;
						}
						break;
					}
					case 6:{
						add("SYM_:=",null,null);
						status = 0;
						break;
					}
					case 7:{
						add(symbolsym[existence(ch_pos,symbol)],null,null);
						pos++;
						status = 0;
						break;
					}
				}		
			}else{
			//非法字符
				System.out.println("Error:非法字符"+ch_pos);
				break;
			}
		}
	}
	
	public LAS getWord(){
		LAS rtn_las = new LAS("", "", "");
		while(pos<=ch.length){
			char ch_pos = getchByPos(pos);
			if(!rtn_las.getSym().equals("")){
				return rtn_las;
			}
			if(legal(ch_pos)){
			//如果字符合法
				switch(status){
					case 0:{
					//还未开始读
						word ="";
						if(ch_pos==' '||ch_pos=='\n'||ch_pos=='\t'){
						//如果是空格 换行 或者制表符 继续读下一个
							pos++;
						}else if(ch_pos>='0'&&ch_pos<='9'){
						//如果是数字 转入状态1(开始读数字)
							word += ch_pos;
							status = 1;
							pos++;
						}else if(ch_pos>=97&&ch_pos<=122){
						//如果是字母 转入状态3(开始读标识符or保留字)
							word += ch_pos;
							status = 2;
							pos++;
						}else if(ch_pos=='>'||ch_pos=='<'){
						//如果是>< 转入状态5
							word += ch_pos;
							status = 3;
							pos++;
						}else if(ch_pos==':'){
						//如果是: 转入状态7
							word += ch_pos;
							status = 5;
							pos++;
						}else if(existence(ch_pos,symbol)!=-1){
						//如果是单字节符号 转入状态9
							word += ch_pos;
							status = 7;
						}else if(ch_pos=='.'){
							rtn_las = new LAS("SYM_.",null,null);
							pos++;
							break;
						}
						break;	
					}
					case 1:{
						if(ch_pos>='0'&&ch_pos<='9'){
						//数字 继续读
							word += ch_pos;
							pos++;
						}else if(ch_pos>=97&&ch_pos<=122){
						//字母 报错
							System.out.println("Error:非法字符"+ch_pos);
							break;
						}else{
						//结束
							rtn_las = new LAS("NUMBER",null,word);
							status = 0;
							break;
						}
						break;
					}
					case 2:{
						if(ch_pos>=97&&ch_pos<=122||ch_pos>='0'&&ch_pos<='9'){
						//字母或者数字 继续读
						
							word += ch_pos;
							pos++;
						}else{
						//结束
							if(existence(word,reserved)!=-1){
							//如果是保留字
								rtn_las = new LAS(reservedsym[existence(word,reserved)],null,null);
							}else{
								rtn_las = new LAS("IDENT",word,null);
							}
							status = 0;
							break;
						}
						break;
					}
					case 3:{
						if(ch_pos=='='){
							word += ch_pos;
							status = 4;
						}else if(word.equals(">")){
							rtn_las = new LAS("SYM_>",null,null);	
							status = 0;
						}else if(word.equals("<")){
							rtn_las = new LAS("SYM_<",null,null);
							status = 0;
						}
						break;
					}
					case 4:{
						if(word.equals(">=")){
							rtn_las = new LAS("SYM_>=",null,null);
						}else if(word.equals("<=")){
							rtn_las = new LAS("SYM_<=",null,null);
						}
						status = 0;
						pos++;
						break;
					}
					case 5:{
						if(ch_pos=='='){
							word += ch_pos;
							pos++;
							status = 6;
						}else{
							err = 1; 
							break;
						}
						break;
					}
					case 6:{
						rtn_las = new LAS("SYM_:=",null,null);
						status = 0;
						break;
					}
					case 7:{
						rtn_las = new LAS(symbolsym[existence(ch_pos,symbol)],null,null);
						pos++;
						status = 0;
						break;
					}
				}		
			}else{
			//非法字符
				System.out.println("Error:非法字符"+ch_pos);
				break;
			}
		}
		return rtn_las;
	}
	
	//元素是否在数组中
	public int existence(char ch, char[] chr){
		for(int i=0;i<chr.length;i++){
			if(ch==chr[i])
				return i;
		}
		return -1;
	}
	public int existence(String ch, String[] chr){
		for(int i=0;i<chr.length;i++){
			if(ch.equals(chr[i]))
				return i;
		}
		return -1;
	}
	//向3数组中添加元素
	public void add(String sym,String id,String num){
		this.sym.add(sym);
		this.id.add(id);
		this.num.add(num);
	}
	//判断字符是否合法
	public boolean legal(char c){
		if(c==' '||c=='\n'||c=='\t'||c>=97&&c<=122||c>='0'&&c<='9'||existence(c,symbol)!=-1||c=='>'||c=='<'||c==':'||c=='.')
			return true;
		return false;
	}
	//输出3个数组
	public void print(){
		System.out.println("--------This is the result of Lexical_analyzer---------");
		System.out.println("\tSYM\tID\tNUM\t");
		for(int i=0;i<sym.size();i++)
		{
			System.out.println(i+"\t"+sym.get(i)+"\t"+id.get(i)+"\t"+num.get(i));
		}
		System.out.println("-------------------------------------------------------");
	}
	
	public ArrayList<String> getsym(){
		return sym;
	}
	public ArrayList<String> getid(){
		return id;
	}
	public ArrayList<String> getnum(){
		return num;
	}
	
	public static void main(String[] args){
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
		Lexical_analyzer la = new Lexical_analyzer(str);
		la.init();
		la.getsys();
		la.print();
		/*LAS rtn_las = la.getWord();
		while(!rtn_las.getSym().equals("")){
			rtn_las.print();
			rtn_las = la.getWord();
		}*/
	}
}
