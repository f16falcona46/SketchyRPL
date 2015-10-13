import java.util.ArrayList;
import java.util.Arrays;


public class RPLName extends RPLObject {
	private String name;
	private static final ArrayList<String> supportedOpsAL = new ArrayList<String>(Arrays.asList(new String[]{"STO","RCL","PURGE","EVAL"}));
	
	public RPLName(String init) {
		super(init);
	}

	@Override
	public void set(String input) {
		name=input;
	}

	@Override
	public int getType() {
		return 6;
	}

	@Override
	public String toString() {
		return "'"+name+"'";
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
				stack.pop();
				{
					switch (supportedOpsAL.indexOf(op)) {
					case 0: //STO
						stack.purge(name);
						stack.sto(new RPLVariable(name, stack.pop()));
						break;
					case 1: //RCL
						stack.rcl(name);
						break;
					case 2: //PURGE
						stack.purge(name);
						break;
					case 3: //EVAL
						stack.push(this);
						CommandParser.parse(stack, "RCL EVAL");
						break;
					default:
						throw new BadOpException();
					}
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
		return new RPLName(name);
	}

}
