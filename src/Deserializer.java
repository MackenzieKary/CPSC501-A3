import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Deserializer {

	public static void main(String[] args) throws UnknownHostException, IOException, JDOMException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
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
		
		Object obj = deserialize(document); 
		
		System.out.println("Object = " + obj);
	}
	
	public static Object deserialize(Document document) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		Element classElement = document.getRootElement();	
		List<Element> classObjectList = classElement.getChildren();
		HashMap mapOfObjects = new HashMap();
		for(Element objectElement : classObjectList){
			Class objClass = Class.forName(objectElement.getAttributeValue("class"));
			Object classInstance = null;
			// Now that we know the class of the element, we can recreate it, then load in it's field values
		
			// Check if array of classes, or just one class
			if (objClass.isArray()){
				int length = Integer.parseInt(objectElement.getAttributeValue("length"));
				classInstance = Array.newInstance(objClass.getComponentType(), length);
			}else{
				// Just a single class, instantiate it
				
				classInstance = objClass.getDeclaredConstructor(null).newInstance(null); 
			}
			Attribute objectAttribute = objectElement.getAttribute("id");
			mapOfObjects.put(objectAttribute.getValue(), classInstance);
			
			System.out.println("mapOfObjects: " + mapOfObjects.get("0"));
			
			// Now set the field values for the classes
			List<Element> fieldElementList = objectElement.getChildren();
			if (classInstance.getClass().isArray()){
				// Is an array
				
			}else{
				// Not an array, so get fields to set their values for class
				for (Element fieldElement : fieldElementList){
					Class declaringClass = Class.forName(fieldElement.getAttributeValue("declaringclass"));
					Field field = declaringClass.getDeclaredField(fieldElement.getAttributeValue("name"));
					
					Element fieldValue = fieldElement.getChildren().get(0);
					
					// Set value for the field
					System.out.println("Field name = " + field.getName());
					if (fieldValue.getName().equals("value")){
						// Primitive field type
						System.out.println("-Is a value");
						if(field.getType().getName().equals("boolean")){
							System.out.println("--Type boolean");
							if(fieldValue.getText().equals("true")){
								field.set(classInstance, Boolean.TRUE);
							}else{
								field.set(classInstance, Boolean.FALSE);
							}
						}else if(field.getType().getName().equals("int")){
							System.out.println("--Type int");
							field.set(classInstance, Integer.valueOf(fieldValue.getText()));
						}else if(field.getType().getName().equals("char")){
							System.out.println("--Type char");
							field.set(classInstance, new Character(fieldValue.getText().charAt(0)));
						}
					}else if(fieldValue.getName().equals("reference")){
						System.out.println("-Is a reference");
						field.set(classInstance, mapOfObjects.get(fieldValue.getText()));
					}else if(fieldValue.getName().equals("null")){
						System.out.println("-Is null");
						field.set(classInstance, null);
					}
				}
			}
		} 
		 
		System.out.println("mapOfObjects index 0 = " + mapOfObjects.get("0"));
		System.out.println("mapOfObjects size? = " + mapOfObjects.size());
		return mapOfObjects.get("1");
		
	}
}
