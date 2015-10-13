import java.util.ArrayList;
import java.util.Arrays;

public class RPLProg extends RPLObject {
	private String progString;
	private static final ArrayList<String> supportedOpsAL = new ArrayList<String>(Arrays.asList(new String[]{"EVAL"}));
	
	public RPLProg(String init) {
		super(init);
	}

	@Override
	public void set(String input) {
		progString = input;
	}

	@Override
	public int getType() {
		return 8;
	}

	@Override
	public String toString() {
		return "[ "+progString+" ]";
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
			if (op.equals("EVAL")) {
				stack.pop();
				CommandParser.parse(stack,progString);
			}
		}
	}

	@Override
	public RPLObject copy() {
		return new RPLProg(progString);
	}
}
