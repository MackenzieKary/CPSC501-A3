import org.jdom2.*;
import java.lang.reflect.*;
import java.util.IdentityHashMap;

public class Serializer {

	public static Document serialize(Object obj) throws IllegalArgumentException, IllegalAccessException{
		Document doc = new Document(new Element("serialized"));
		IdentityHashMap hashMap = new IdentityHashMap();
		
		hashMap.put(obj, Integer.toString(hashMap.size()));
		
		// Create an object element
		Element objectElement = new Element("object");
		objectElement.setAttribute("class", obj.getClass().getName());
		objectElement.setAttribute("id", Integer.toString(hashMap.size()));
		
		doc.getRootElement().addContent(objectElement);
		
		if(obj.getClass().isArray()){
			
		}else{
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
						Element referenceElement = new Element("reference");
						// Reference field
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
				
				
				
				
			}

		}		
		return null;
	}
}
