import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import java.io.*;

public class ObjectCreator {

	public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
		// TODO Auto-generated method stub
		Object objToSend = null;
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
			if (input.equals("1") || input.equals("(1)")){
				ClassA createdObj = new ClassA();
				setSimpleObject(createdObj);
				objToSend = createdObj;
				break;
			}else if (input.equals("2") || input.equals("(2)")){
				ClassB createdObj = new ClassB();
				setSimpleObject(createdObj.classA);
				objToSend = createdObj;
				break;
			}
			else if (input.equals("3") || input.equals("(3)")){
				ClassC createdObj = new ClassC();
				createdObj.characterArray = new char[10];
				System.out.println("Creating character array");
				for(int i = 0; i < createdObj.characterArray.length; i++){
					System.out.printf("Set character for index %d:\n", i);
					System.out.print(" > ");
					
					input = buffRead.readLine();
					createdObj.characterArray[i] = input.charAt(0);
				}	
				break;
			}
			else if (input.equals("4") || input.equals("(4)")){
				ClassD createdObj = new ClassD();
				objToSend = createdObj;
				break;
			}
			else if (input.equals("5") || input.equals("(5)")){
				
				break;
			}
			else{
				System.err.println("Please enter a valid input");
				System.out.print(" > ");
			}
		}
		Document doc = Serializer.serialize(objToSend);
		System.out.println("After serializer");
//			XMLOutputter outputXML = new XMLOutputter();
//			outputXML.output(doc, new FileOutputStream("docToSend.xml"));

		System.out.println("Doc: " + doc);
		new XMLOutputter().output(doc, new FileOutputStream("docToSend.xml"));
		System.out.println("After XMLOutputter");
		
		/*
		 * Start of network connection part
		 * 
		 * XML object should be created as new file at this point and called "docToSend.xml"
		 * 
		 */
		
		int port = 9998; 	// create the port number (accepting end will need same port number)
		//ServerSocket serverSocket = new ServerSocket(port);
		Socket connectedSocket = new Socket("localhost", 9998);
		System.out.println("Listening...");
		//Socket connectedSocket = serverSocket.accept();
		System.out.println("Connection Aquired");
		
		File fileToSend = new File("docToSend.xml");		// Turn XMLOuputter file into File type
		int fileToSendLength = (int) fileToSend.length();
		//byte[] buffer = new byte[fileToSendLength];
		int count;
		byte[] buffer = new byte[fileToSendLength];

		OutputStream out = connectedSocket.getOutputStream();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream("docToSend.xml"));
		
		while ((count = in.read(buffer)) > 0) {
			  out.write(buffer, 0, count);
		}
		connectedSocket.close();
			
	}
	public static void setSimpleObject(Object obj) throws IOException{
		// Set the field values of this class using reflection
		Class objClass = obj.getClass();
		Field[] classFields = objClass.getDeclaredFields();
		for (Field classField : classFields){
			classField.setAccessible(true);
			String fieldName = classField.getName();
			String fieldType = classField.getType().getName();
			System.out.println("Input value for "+ fieldName + " of Type: " + fieldType);
			System.out.print(" > ");
			BufferedReader buffRead = new BufferedReader(new InputStreamReader(System.in));
			String input;
			
			while ((input = buffRead.readLine()) != null){
				try{
					if (fieldType.equals("boolean")){
						if(input.toLowerCase().equals("true") || input.toLowerCase().equals("false")){
							classField.set(obj, Boolean.parseBoolean(input));
							break;
						}else{
							System.err.print("Please enter a valid input \n");
							System.err.flush();
							System.out.print(" > ");
						}
					}else if (fieldType.equals("int")){
						classField.set(obj, Integer.parseInt(input));
						break;
					}else if (fieldType.equals("char")){
						classField.set(obj, input.charAt(0));
						break;
					}
				}catch(Exception e){
					// Will loop again until valid input is received
					System.err.print("Please enter a valid input \n");
					System.out.print(" > ");
				}
			}
			
		}
	}

}
