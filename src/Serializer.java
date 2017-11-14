import org.jdom2.*;
import java.lang.reflect.*;
import java.util.IdentityHashMap;

public class Serializer {

	public static Document serialize(Object obj) throws IllegalArgumentException, IllegalAccessException{
		System.out.println("In serializer");
		Document doc = new Document(new Element("serialized"));
		IdentityHashMap hashMap = new IdentityHashMap();
		
		hashMap.put(obj, Integer.toString(hashMap.size()));
		
		// Create an object element
		Element objectElement = new Element("object");
		System.out.println("Created object element");
		objectElement.setAttribute("class", obj.getClass().getName());
		objectElement.setAttribute("id", Integer.toString(hashMap.size()));
		
		doc.getRootElement().addContent(objectElement);
		
		if(obj.getClass().isArray()){
			System.out.println("Is array");
		}else{
			System.out.println("Is not array");
			
			Class reflectClass = obj.getClass();
			Field[] classFields = reflectClass.getDeclaredFields();
			System.out.println("\nField info below for: " + reflectClass);
			for (Field classField : classFields){
				classField.setAccessible(true);
				Element fieldElement = new Element("field");
				fieldElement.setAttribute("name", classField.getName());
				fieldElement.setAttribute("declaringclass", classField.getDeclaringClass().getName());
				
				Object fieldObject = classField.get(obj);
				if (fieldObject != null){
					if (classField.getType().isPrimitive()){
						// Primitive field 
						fieldElement.addContent(new Element("value").setText(fieldObject.toString()));
					}else{
						// Reference field
						Element referenceElement = new Element("reference");
						if(hashMap.containsKey(fieldObject)){
							referenceElement.setText(hashMap.get(fieldObject).toString());
						}else{
							// Not yet saved in identity hashMap
							referenceElement.setText(Integer.toString(hashMap.size()));
							// Need to change method to allow for recursion for reference values
						}
						
					}
				}else{
					fieldElement.addContent(new Element("null"));
				}	
				objectElement.addContent(fieldElement);
			}

		}		
		return doc;
	}
}
