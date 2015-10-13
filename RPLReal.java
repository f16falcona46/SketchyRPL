import java.util.ArrayList;
import java.util.Arrays;

public class RPLReal extends RPLObject {
	private double data;
	private static final ArrayList<String> supportedOpsAL = new ArrayList<String>(Arrays.asList(new String[]{"+","-","*","/","SIN","COS","TAN","ASIN","ACOS","ATAN","EXP","LN","^","R->C", "GET", "PUT", "EVAL"}));
	
	RPLReal(String init) {
		super(init);
	}
	
	@Override
	public void set(String input) {
		data=Double.parseDouble(input);
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public String toString() {
		return Double.toString(data);
	}

	@Override
	public ArrayList<String> supportedOps() {
		return supportedOpsAL;
	}

	@Override
	public void doOp(String op, RPLStack stack) throws BadOpException {
		try {
			super.doOp(op, stack);
		}
		catch(NoOpException eList) {
			try {
				boolean pushThis = true;
				stack.pop();
				RPLObject other=null;
				{
					switch (supportedOpsAL.indexOf(op)) {
					case 0: //+
						other = stack.pop();
						if (other.getType()==1) { //promote real to complex
							stack.push(other);
							stack.push(this);
							CommandParser.parse(stack, "0 R->C +");
							pushThis=false;
							break;
						}

						this.set(Double.toString(Double.parseDouble(other.toString())+data));
						break;
					case 1: //-
						other = stack.pop();
						if (other.getType()==1) { //promote real to complex
							stack.push(other);
							stack.push(this);
							CommandParser.parse(stack, "0 R->C -");
							pushThis=false;
							break;
						}

						this.set(Double.toString(Double.parseDouble(other.toString())-data));
						break;
					case 2: //*
						other = stack.pop();
						if (other.getType()==1) { //promote real to complex
							stack.push(other);
							stack.push(this);
							CommandParser.parse(stack, "0 R->C *");
							pushThis=false;
							break;
						}

						this.set(Double.toString(Double.parseDouble(other.toString())*data));
						break;
					case 3: //divide
						other = stack.pop();
						if (other.getType()==1) { //promote real to complex
							stack.push(other);
							stack.push(this);
							CommandParser.parse(stack, "0 R->C /");
							pushThis=false;
							break;
						}

						this.set(Double.toString(Double.parseDouble(other.toString())/data));
						break;
					case 4: //SIN
						this.set(Double.toString(Math.sin(data)));
						break;
					case 5: //COS
						this.set(Double.toString(Math.cos(data)));
						break;
					case 6: //TAN
						this.set(Double.toString(Math.tan(data)));
						break;
					case 7: //ASIN
						if (Math.abs(data)>1) {
							CommandParser.parse(stack, this.toString()+" 0 R->C ASIN");
							pushThis=false;
							break;
						}
						this.set(Double.toString(Math.asin(data)));
						break;
					case 8: //ACOS
						if (Math.abs(data)>1) {
							CommandParser.parse(stack, this.toString()+" 0 R->C ACOS");
							pushThis=false;
							break;
						}
						this.set(Double.toString(Math.acos(data)));
						break;
					case 9: //ATAN
						this.set(Double.toString(Math.atan(data)));
						break;
					case 10: //EXP
						this.set(Double.toString(Math.exp(data)));
						break;
					case 11: //LN
						if (data<0) {
							CommandParser.parse(stack, this.toString()+" 0 R->C LN");
							pushThis=false;
							break;
						}
						this.set(Double.toString(Math.log(data)));
						break;
					case 12: //^
						other = stack.pop();	
						if (other.getType()==1) { //promote real to complex
							stack.push(other);
							stack.push(this);
							CommandParser.parse(stack, "0 R->C ^");
							pushThis=false;
							break;
						}
						if ((Double.parseDouble(other.toString())<0) && !(data%1==0)) {
							stack.push(other);
							CommandParser.parse(stack, "0 R->C");
							stack.push(this);
							CommandParser.parse(stack, "0 R->C ^");
							pushThis=false;
							break;
						}

						this.set(Double.toString(Math.pow(Double.parseDouble(other.toString()),data)));
						break;
					case 13: //R->C
						other = stack.pop();
						stack.push(new RPLComplex("(" + other + "," + this + ")"));
						pushThis = false;
						break;
					case 14://GET
						{
							other = stack.pop();
							if(other.getType() == 5)
							{
								stack.push(other);
								CommandParser.parse(stack, "OBJ-> "+Integer.toString((int)data)+" - DROPN");
								for (int counter = 0; counter < (int)data-1; counter++) {
									CommandParser.parse(stack, "SWAP DROP");
								}
								pushThis=false;
							}
						}
						break;
					case 15://PUT
						{
							other = stack.pop();
							RPLObject otherList = stack.pop();
							
							if(otherList.getType() == 5 && other.getType( ) == 0)
							{
								RPLReal pos = (RPLReal)other;
								RPLList list = (RPLList)otherList;
								list.replace((int)pos.getValue(), this);
								pushThis = false;
								stack.push(list);
							}
						}
						break;
					}
					if(pushThis)
						stack.push(this);
				}
			}
			catch (Exception e) {
				stack.push(this);
				throw new BadOpException();
			}
		}
	}

	@Override
	public RPLObject copy() {
		RPLReal other = new RPLReal(Double.toString(data));
		return other;
	}
	
	public double getValue()
	{
		return data;
	}

}
