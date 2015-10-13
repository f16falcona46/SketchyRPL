import java.util.ArrayList;
import java.util.Arrays;

public class TestRPLObject extends RPLObject {
	private int data;
	private static final ArrayList<String> supportedOpsAL = new ArrayList<String>(Arrays.asList(new String[]{"+", "EVAL"}));
	
	TestRPLObject(String init) {
		super(init);
	}
	
	@Override
	public void set(String inputString) {
		data=Integer.parseInt(inputString);
	}
	
	@Override
	public int getType() {
		return 65535;
	}

	@Override
	public String toString() {
		return Integer.toString(data);
	}
	
	@Override
	public ArrayList<String> supportedOps() {
		return supportedOpsAL;
	}
	
	@Override
	public void doOp(String op, RPLStack stack) {
		if (op.equals("+") && (stack.get(1).getType() == 65535)) {
			data = Integer.parseInt(stack.get(1).toString())+data;
		}
		stack.pop();
		stack.pop();
		stack.push(this);
	}
	
	@Override
	public RPLObject copy() {
		TestRPLObject other = new TestRPLObject(Integer.toString(data));
		return other;
	}
}
