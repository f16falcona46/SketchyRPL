import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//[ MKWIN 0 [ DUP 131 < ] [ DUP 40 R->C SETBLK DUP DUP 65 - 6.5 / SIN 4 * 40 + R->C SETBLK 1 + ] LOOPR DROP 0 [ DUP 80 < ] [ DUP 65 SWAP R->C SETBLK 1 + ] LOOPR DROP ]

public class RPLStack {
	private RPLObject[] stack;
	private GraphWindow graph;
	private ArrayList<RPLVariable> vars = new ArrayList<RPLVariable>();
	private static final ArrayList<String> stackOpsAL = new ArrayList<String>(Arrays.asList(new String[]{"SWAP","DUP","DROP","ROLL","ROLLD","PICK","QUIT","DEPTH","IFT","==","LOOPR", "IFTE",">","<",">=","<=","NOT","AND","OR","OVER","ROT","UNROT","DUPN","DROPN","DECLVARS","MKWIN","CLOSEWIN","TYPE"}));
	
	public RPLStack() {
		stack = new RPLObject[0];
	}
	
	public void doOp(String op) {
		try {
			switch (stackOpsAL.indexOf(op)) {
			case 0: //SWAP
				{
					RPLObject x = this.pop();
					RPLObject y = this.pop();
					this.push(x);
					this.push(y);
				}
				break;
			case 1: //DUP
				this.push(this.get(0).copy());
				break;
			case 2: //DROP
				if (stack.length>0) {
					this.pop();
				}
				else {
					throw new BadOpException();
				}
				break;
			case 3: //ROLL
				{
					RPLObject toRollSave = this.pop();
					int toRoll = (int)Double.parseDouble(toRollSave.toString());
					if ((toRoll <= stack.length) && (toRoll>0)) {
						RPLStack tempStack = new RPLStack();
						for (int counter = 0; counter < toRoll - 1; counter++) {
							tempStack.push(this.pop());
						}
						RPLObject base = this.pop();
						for (int counter = 0; counter < toRoll - 1; counter++) {
							this.push(tempStack.pop());
						}
						this.push(base);
					}
					else if (toRoll>stack.length) {
						this.push(toRollSave);
						throw new BadOpException();
					}
				}
			break;
			case 4: //ROLLD
				{
					RPLObject toRollSave = this.pop();
					int toRoll = (int)Double.parseDouble(toRollSave.toString());
					if ((toRoll <= stack.length) && (toRoll>0)) {
						RPLStack tempStack = new RPLStack();
						RPLObject base = this.pop();
						for (int counter = 0; counter < toRoll - 1; counter++) {
							tempStack.push(this.pop());
						}
						this.push(base);
						for (int counter = 0; counter < toRoll - 1; counter++) {
							this.push(tempStack.pop());
						}
					}
					else if (toRoll>stack.length) {
						this.push(toRollSave);
						throw new BadOpException();
					}
				}
				break;
			case 5: //PICK
				{
					RPLObject toPickSave = this.pop();
					int pick = (int)Double.parseDouble(toPickSave.toString())-1;
					if (pick > 0 && pick <= stack.length) {
						this.push(this.get(pick).copy());
					}
					else {
						this.push(toPickSave);
						throw new BadOpException();
					}
				}
				break;
			case 6: //QUIT
				System.out.println("Stopping");
				System.exit(0);
			case 7: //DEPTH
				this.push(new RPLReal(Integer.toString(stack.length)));
				break;
			case 8: //IFT
				this.doOp("SWAP");
				if (!this.pop().toString().equals("0.0")) {
					this.doOp("EVAL");
				}
				else {
					this.doOp("DROP");
				}
				break;
			case 9: //==
				{
					RPLObject x = this.pop();
					RPLObject y = this.pop();
					if (x.toString().equals(y.toString())) {
						this.push(new RPLReal("1"));
					}
					else {
						this.push(new RPLReal("0"));
					}
				}
				break;
			case 10: //LOOPR
				{
					RPLObject body = this.pop();
					RPLObject conditional = this.pop();
					
					this.push(conditional);
					this.doOp("EVAL");
					while (!this.pop().toString().equals("0.0")) {
						this.push(body);
						this.doOp("EVAL");
						this.push(conditional);
						this.doOp("EVAL");
					}
				}
				break;
			case 11: //IFTE
				{
					RPLObject notTrue = this.pop();
					RPLObject trueFunc = this.pop();
					RPLObject conditional = this.pop();
	
					if (!conditional.toString().equals("0.0")) {
						this.push(trueFunc);
						this.doOp("EVAL");
					}
					else {
						this.push(notTrue);
						this.doOp("EVAL");
					}
				}
				break;
			case 12: //>
				{
					RPLObject x = this.pop();
					RPLObject y = this.pop();
					if (Double.parseDouble(y.toString()) > Double.parseDouble(x.toString())) {
						this.push(new RPLReal("1"));
					}
					else {
						this.push(new RPLReal("0"));
					}
				}
			break;
			case 13: //<
				{
					RPLObject x = this.pop();
					RPLObject y = this.pop();
					if (Double.parseDouble(y.toString()) < Double.parseDouble(x.toString())) {
						this.push(new RPLReal("1"));
					}
					else {
						this.push(new RPLReal("0"));
					}
				}
				break;
			case 14: //>=
				{
					RPLObject x = this.pop();
					RPLObject y = this.pop();
					if (Double.parseDouble(y.toString()) >= Double.parseDouble(x.toString())) {
						this.push(new RPLReal("1"));
					}
					else {
						this.push(new RPLReal("0"));
					}
				}
				break;
			case 15: //<=
				{
					RPLObject x = this.pop();
					RPLObject y = this.pop();
					if (Double.parseDouble(y.toString()) <= Double.parseDouble(x.toString())) {
						this.push(new RPLReal("1"));
					}
					else {
						this.push(new RPLReal("0"));
					}
				}
				break;
			case 16: //NOT
				{
					if (this.pop().toString().equals("0.0")) {
						this.push(new RPLReal("1"));
					}
					else {
						this.push(new RPLReal("0"));
					}
				}
				break;
			case 17: //AND
				{
					RPLObject x = this.pop();
					RPLObject y = this.pop();
					this.push(new RPLReal(Double.toString(Double.parseDouble(y.toString())*Double.parseDouble(x.toString()))));
				}
				break;
			case 18: //OR
				{
					RPLObject x = this.pop();
					RPLObject y = this.pop();
					this.push(new RPLReal(Double.toString(Double.parseDouble(y.toString())+Double.parseDouble(x.toString()))));
				}
				break;
			case 19: //OVER
				CommandParser.parse(this, "2 PICK");
				break;
			case 20: //ROT
				CommandParser.parse(this, "3 ROLL");
				break;
			case 21: //UNROT
				CommandParser.parse(this, "3 ROLLD");
				break;
			case 22: //DUPN
				{
					int toDup = (int)Double.parseDouble(this.pop().toString());
					for (int counter = 0; counter<toDup; counter++) {
						this.push(stack[toDup-1].copy());
					}
				}
				break;
			case 23: //DROPN
				{
					int toDup = (int)Double.parseDouble(this.pop().toString());
					for (int counter = 0; counter<toDup; counter++) {
						this.pop();
					}
				}
				break;
			case 24: //DECLVARS
				if (vars.size()>0) {
					System.out.println("Declared variables: ");
					for (RPLVariable var : vars) {
						System.out.println(var.getName()+": "+var.getData().toString());
					}
				}
				else {
					System.out.println("No variables declared.");
				}
				System.out.println();
				break;
			case 25: //MKWIN
				if (graph!=null) {
					graph.setVisible(false);
					graph.dispose();
					graph=null;
				}
				graph = new GraphWindow(262, 160);
				graph.displayWindow();
				break;
			case 26: //CLOSEWIN
				if (graph!=null) {
					graph.setVisible(false);
					graph.dispose();
					graph=null;
				}
				break;
			case 27: //TYPE
				this.push(new RPLReal(Integer.toString(this.pop().getType())));
				break;
			default:
				if (stack.length > 0) {
					stack[0].doOp(op, this);
				}
				else {
					System.out.println("Too few arguments");
				}
				break;
			}
		}
		catch (Throwable e) {
			System.out.println("Bad operation");
		}
	}
	
	public GraphWindow getGraph(){
		return graph;
	}
	
	public void printStack() {
		if (stack.length==0) {
			System.out.println("Empty Stack");
		}
		for (int counter = 0; counter<stack.length; counter++) {
			System.out.println((counter+1)+": "+stack[counter].toString());
		}
	}
	
	public RPLObject get(int pos) {
		return stack[pos];
	}
	
	public void push(RPLObject newObject) {
		RPLObject[] newStack = new RPLObject[stack.length+1];
		for (int counter = 0; counter<stack.length; counter++) {
			newStack[counter+1] = stack[counter];
		}
		newStack[0]=newObject;
		stack=newStack;
	}
	
	public RPLObject pop() {
		RPLObject[] newStack = new RPLObject[stack.length-1];
		for (int counter = 0; counter<stack.length-1; counter++) {
			newStack[counter] = stack[counter+1];
		}
		RPLObject popped = stack[0];
		stack=newStack;
		return popped;
	}
	
	public String toString() {
		String stackString="";
		for (int counter = stack.length-1; counter>-1; counter--) {
			stackString=stackString+(counter+1)+": "+stack[counter].toString()+"\n";
		}
		if (stack.length == 0) {
			return "Empty Stack";
		}
		return stackString;
	}
	
	public void repl() {
		Scanner userInput = new Scanner(System.in);
		System.out.println(this);
		while(true)
		{
			System.out.print("> ");
			CommandParser.parse(this, userInput.nextLine());
			System.out.println(this);
		}
	}
	
	public void sto(RPLVariable newVar) {
		vars.add(newVar);
	}
	
	public void rcl(String name) {
		for (RPLVariable var : vars) {
			if (var.getName().equals(name)) {
				this.push(var.getData());
				break;
			}
		}
	}
	
	public void purge(String name) {
		int max = vars.size();
		for (int counter = 0; counter<max; counter++) {
			if (vars.get(counter).getName().equals(name)) {
				vars.remove(counter);
				counter--;
				max--;
			}
		}
	}
	
	public void setPix(double x, double y) {
		if (!graph.testPixel(x, y)) {
			graph.setPixel(x, y);
		}
	}
	
	public void clearPix(double x, double y) {
		if (graph.testPixel(x, y)) {
			graph.resetPixel(x, y);
		}
	}
	
	public boolean testPix(double x, double y) {
		return graph.testPixel(x,y);
	}
	
	public void drawLine(double x1, double y1, double x2, double y2) {
		graph.drawLine(x1, y1, x2, y2);
	}
	
	public static void main(String[] args) {
		RPLStack testStack = new RPLStack();
		testStack.repl();
	}
}
