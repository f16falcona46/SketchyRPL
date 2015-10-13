
public class NoOpException extends Exception {
	
	/*
	 * dont actually print any exception error
	 * this exception is meant to allow the subclass doOp to be ran if the superclass doOp isnt ran
	 */
	@Override
	public void printStackTrace()
	{
		
	}

}
