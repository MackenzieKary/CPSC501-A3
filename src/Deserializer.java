import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.*;


import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class Deserializer {

	public static void main(String[] args) throws UnknownHostException, IOException, JDOMException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException{
		
		SocketIn socketIn = new SocketIn();
		socketIn.receiveDoc();
		
		/*
		 * Start of deserialization
		 */
		File inputFile = new File("docReceived.xml");
			
		
		SAXBuilder saxBuilder = new SAXBuilder();
		
		Document document  = saxBuilder.build(inputFile);
		
		Object obj = deserialize(document); 
		
		System.out.println("Object = " + obj);
		
		Visualizer visualizer = new Visualizer();
		boolean recursive =  false;
		visualizer.visualize(obj, recursive);
		
		
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
				// >>>
				Constructor c = objClass.getDeclaredConstructor(null);
				if (!Modifier.isPublic(c.getModifiers()))
					c.setAccessible(true);
				classInstance = c.newInstance(null);
				// <<<
				//classInstance = objClass.getDeclaredConstructor(null).newInstance(null); 
			}
			Attribute objectAttribute = objectElement.getAttribute("id");
			mapOfObjects.put(objectAttribute.getValue(), classInstance);
			System.out.println("Put instance in map: "+ objectAttribute.getValue());
			
			/// I think all this needs to be completed before we can move on to the field values. All instances need to be made.
			// Take the stuff before, and put it outside of the first for loop.
			
			
			
			System.out.println("mapOfObjects: " + mapOfObjects.get("0"));
			
		} 
	// Now set the field values for the classes
		
		for(Element objectElement : classObjectList){
			Object classInstance = mapOfObjects.get(objectElement.getAttributeValue("id"));
			List<Element> fieldElementList = objectElement.getChildren(); 
			
			if (classInstance.getClass().isArray()){
				System.out.println("is an array");
				// Is an array
				for (int i = 0; i < fieldElementList.size(); i++){
					Element fieldElement = fieldElementList.get(i);
					
					
					// Set value for the field
					if (fieldElement.getName().equals("value")){
						// Primitive field type
						System.out.println("-Is a value");
						if(classInstance.getClass().getComponentType().getName().equals("boolean")){
							System.out.println("--Type boolean");
							if(fieldElement.getText().equals("true")){
								Array.set(classInstance, i , Boolean.TRUE);
							}else{
								Array.set(classInstance, i , Boolean.FALSE);
							}
						}else if(classInstance.getClass().getComponentType().getName().equals("int")){
							System.out.println("--Type int");
							Array.set(classInstance, i , Integer.valueOf(fieldElement.getText()));
						}else if(classInstance.getClass().getComponentType().getName().equals("char")){
							System.out.println("--Type char");
							Array.set(classInstance, i , new Character(fieldElement.getText().charAt(0)));
						}else{
							Array.set(classInstance, i, fieldElement);
						}
					}else if(fieldElement.getName().equals("reference")){
						System.out.println("-Is an array reference");
						Array.set(classInstance, i , mapOfObjects.get(fieldElement.getText()));
						
					}else if(fieldElement.getName().equals("null")){
						System.out.println("-Is null");
						Array.set(classInstance, i , null);
					}
					
					
				}
			}else{
				// Not an array, so get fields to set their values for class
				for (Element fieldElement : fieldElementList){
					Class declaringClass = Class.forName(fieldElement.getAttributeValue("declaringclass"));
					Field field = declaringClass.getDeclaredField(fieldElement.getAttributeValue("name"));
					field.setAccessible(true);
					System.out.println("Field: " + field.getName());
					
					Element fieldValue = (Element) fieldElement.getChildren().get(0);
					
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
		return mapOfObjects.get("0");
		
	}
}
