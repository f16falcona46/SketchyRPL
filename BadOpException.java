public class BadOpException extends Throwable {
	@Override
	public void printStackTrace()
	{
		System.out.println("(BadOpException exception) Error! That operation is invalid.");
	}
}
