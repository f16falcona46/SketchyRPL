import java.util.ArrayList;
import java.util.Arrays;

public class RPLList extends RPLObject {
	private String list;
	private static final ArrayList<String> supportedOpsAL = new ArrayList<String>(Arrays.asList(new String[]{"+", "SIZE", "OBJ->","EVAL"}));
	
	
	public RPLList(String value) {
		super(value);
	}

	@Override
	public void set(String input) {
		list = input;
	}

	@Override
	public int getType() {
		return 5;
	}

	@Override
	public String toString() {
		return "{ " + list + " }";
	}

	public String toStringNoBrackets()
	{
		return list;
	}
	
	@Override
	public ArrayList<String> supportedOps() {
		return supportedOpsAL;
	}

	@Override
	public void doOp(String op, RPLStack stack) throws BadOpException {
		boolean pushThis = true;
		try 
		{
			super.doOp(op, stack);
		} 
		catch(NoOpException e1) 
		{
			try 
			{
				stack.pop();
				switch (supportedOpsAL.indexOf(op)) {
				case 0:
					RPLObject other = stack.pop();
					this.insert(0, other);
					break;
				case 1://SIZE
					String[] listSize = list.split(" ");
					pushThis = false;
					stack.push(new RPLReal(Double.toString((double)listSize.length)));
					break;
				case 2:
					{
						if(list.length() == 0)
							stack.push(new RPLReal("0.0"));
						else {
							stack.push(this);
							CommandParser.parse(stack, "DUP SIZE");
							RPLReal theSize = (RPLReal)stack.pop();
							CommandParser.parse(stack, "EVAL");
							stack.push(theSize);
						}
					}
					pushThis = false;
					break;
				case 3://EVAL
					CommandParser.parse(stack, this.toStringNoBrackets());
					pushThis = false;
					break;
				}
				if(pushThis)
				stack.push( this );
			}
			catch(Exception e)
			{
				stack.push(this);
				throw new BadOpException();
			}
		}
	}

	@Override
	public RPLObject copy() {
		return new RPLList(list);
	}
	
	public void insert(int index, RPLObject other){
		ArrayList<Integer> spaceIndexes = new ArrayList<Integer>();
		int listLevel = 0;
		String listCopy = " " + list + " ";
		for(int a=0;a<listCopy.length();a++)
		{
			switch(listCopy.charAt(a))
			{
			case ' ':
				if(listLevel == 0)
					spaceIndexes.add(a);
				break;
			case '[':
				listLevel++;
				break;
			case '{':
				listLevel++;
				break;
			case '}':
				listLevel--;
				break;
			case ']':
				listLevel--;
				break;
			}
		}
		list = list.substring(0, spaceIndexes.get(index)) + other + " " + list.substring(spaceIndexes.get(index));
	}
	
	public void replace(int index, RPLObject other)
	{
		ArrayList<Integer> spaceIndexes = new ArrayList<Integer>();
		int listLevel = 0;
		String listCopy = " " + list + " ";
		for(int a=0;a<listCopy.length();a++)
		{
			switch(listCopy.charAt(a))
			{
			case ' ':
				if(listLevel == 0)
					spaceIndexes.add(a);
				break;
			case '[':
				listLevel++;
				break;
			case '{':
				listLevel++;
				break;
			case '}':
				listLevel--;
				break;
			case ']':
				listLevel--;
				break;
			}
		}
		if( index == ( spaceIndexes.size( ) - 1 ) )
			list = list.substring(0, spaceIndexes.get((index))) + other;
		else
			list = list.substring(0, spaceIndexes.get((index))) + other + " " + list.substring(spaceIndexes.get(index + 1));
	}
	
	public RPLObject get(int index)
	{
		return null;
	}
	
	public void append(RPLObject other){
		String text;
		
		if(other.getType() == 5)
		{
			RPLList a = (RPLList)other;
			text = a.toStringNoBrackets();
		}
		else
			text = other.toString();
		
		if(list.length() != 0)
			list += " " + text;
		else
			list = text;
	}
}
