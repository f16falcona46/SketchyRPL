import java.util.Scanner;
import java.io.*;

public class FileReader {
	
	public static void main(String args[]) throws IOException {
		System.out.print("Enter filename: ");
		Scanner input = new Scanner(System.in);
		Scanner file = new Scanner(new File(input.nextLine()));
		
		RPLStack stack = new RPLStack();
		
		while (file.hasNext()) {
			CommandParser.parse(stack, file.nextLine());
		}
		
		stack.repl();
	}
}
