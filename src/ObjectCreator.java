import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ObjectCreator {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Object obj = null;
		System.out.println("Choose an object to create:");
		System.out.println("(1) Simple object of primitives \n"
						+ "(2) Object containing references to other objects \n"
						+ "(3) Object containing array of primitives \n"
						+ "(4) Object containing array of references to other objects \n"
						+ "(5) Object that uses instance of Java's collection classes "
						+ "to refer to several other classes. ");
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(" > ");
		String input;
		while ((input = buffRead.readLine()) != null){
			//String input = buffRead.readLine();
			if (input.equals("1") || input.equals("(1)")){
				
				break;
			}else if (input.equals("2") || input.equals("(2)")){
				break;
			}
			else if (input.equals("3") || input.equals("(3)")){
				break;
			}
			else if (input.equals("4") || input.equals("(4)")){
				break;
			}
			else if (input.equals("5") || input.equals("(5)")){
				break;
			}
			else{
				System.out.println("Please enter a valid input");
				System.out.print(" > ");
			}
		}
	}

}
