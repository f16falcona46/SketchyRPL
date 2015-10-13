import java.util.ArrayList;

public abstract class RPLObject {
	RPLObject(String init) {
		this.set(init);
	}
	public abstract void set(String input);
	public abstract int getType();
	public abstract String toString();
	public abstract ArrayList<String> supportedOps();
	public void doOp(String op, RPLStack stack) throws BadOpException, NoOpException
	{
		if(op.equals("+"))
		{
			stack.pop();
			RPLObject other = stack.pop();
			if(other.getType() == 5)
			{
				RPLList list = (RPLList) other;
				list.append(this);
				stack.push(list);
			}
			else
			{
				stack.push(other);
				stack.push(this);
				throw new NoOpException();
			}
		}
		else
			throw new NoOpException();
		
	}
	public abstract RPLObject copy();
}
