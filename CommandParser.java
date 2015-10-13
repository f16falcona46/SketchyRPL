
class IntHolder
{
	public IntHolder( int integer )
	{
		this.integer = integer;
	}
	public int integer;
}

public class CommandParser {
	
	private static String parseList(String openDelimiter, String closeDelimiter, String[] commandTokens, IntHolder startIndex)
	{
		String progOut = "";
		int depthCount = 1;
		for(;(depthCount > 0)&&(startIndex.integer < commandTokens.length);startIndex.integer++)
		{
			if(commandTokens[startIndex.integer].equals(openDelimiter))
				depthCount++;
			if(commandTokens[startIndex.integer].equals(closeDelimiter))
				depthCount--;
			if(depthCount > 0)
				progOut += commandTokens[startIndex.integer] + " ";
		}
		progOut = progOut.trim();
		return progOut;
	}
	
	public static void parse(RPLStack stack, String commandLine)
	{
		commandLine = commandLine.toUpperCase();
		commandLine = commandLine.trim();
		if (commandLine.equals("")) {
			stack.doOp("DUP");
			return;
		}
		String[] tokens = commandLine.split(" ");
		for(int i=0;i<tokens.length;i++)
		{
			if(tokens[i].length() == 0)
				continue;
			switch(tokens[i].charAt(0))
			{
			case '{':
				{
					i++;
					IntHolder iHolder = new IntHolder(i);
					String list = parseList("{", "}", tokens, iHolder);
					stack.push(new RPLList(list));
					i = iHolder.integer;
				}
				break;
			case '(':
				stack.push(new RPLComplex(tokens[i]));
				break;
			case '[':
				{
					i++;
					IntHolder iHolder = new IntHolder(i);
					String prog = parseList("[", "]", tokens, iHolder);
					stack.push(new RPLProg(prog));
					i = iHolder.integer-1;
				}
				break;
			case '\'':
				stack.push(new RPLName(tokens[i].substring(1,tokens[i].length()-1)));
				break;
			case '-':
				if (tokens[i].length()>1) {
					stack.push(new RPLReal(tokens[i]));
				}
				else {
					stack.doOp(tokens[i]);
				}
				break;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				stack.push(new RPLReal(tokens[i]));
				break;
			default:
				stack.doOp(tokens[i]);
				break;
			}
		}
	}
}
