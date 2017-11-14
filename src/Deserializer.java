import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Deserializer {

	public static void main(String[] args) throws UnknownHostException, IOException, JDOMException{
		ServerSocket ss = new ServerSocket(9998);
		System.out.println("Open Connection...");
		//Socket connectedSocket = new Socket("localhost", 9998);
		Socket connectedSocket = ss.accept();
		System.out.println("Connection Accepted");
		byte[] buffer = new byte[99999]; // Set byte array to something large
		int inputBytes = connectedSocket.getInputStream().read(buffer, 0, buffer.length);
		BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream("docReceived.xml"));
		buffStream.write(buffer, 0, inputBytes);
		buffStream.close();
		connectedSocket.close();
		ss.close();
		
		
		/*
		 * Start of deserialization
		 */
		File inputFile = new File("docReceived.xml");
			
		
		SAXBuilder saxBuilder = new SAXBuilder();
		
		Document document  = saxBuilder.build(inputFile);
		
		deserialize(document); 
	}
	
	public static Object deserialize(Document document){
		Element classElement = document.getRootElement();	
		
		List<Element> studentList = classElement.getChildren();
		
		
		/* EXAMPLE FROM TUTORIAL:
		 * Element classElement = document.getRootElement();	
			
			List<Element> studentList = classElement.getChildren();
			System.out.println("------------------------");
			
			for(int i = 0; i < studentList.size(); i++){
				Element student = studentList.get(i);
				System.out.println("\nCurrent Element: "+ student.getName());
				Attribute attribute = student.getAttribute("id");
				System.out.println("Student id: " +attribute.getValue());
				System.out.println("First name: "+ student.getChild("firstName").getText());
				System.out.println("Last name: "+ student.getChild("lastName").getText());
				System.out.println("Grade: "+ student.getChild("marks").getText());			
			}
		 */
		return null;
	}
}
