import org.jdom2.*;
import java.lang.reflect.*;
import java.util.IdentityHashMap;

public class Serializer {

	public static Document serialize(Object obj) throws IllegalArgumentException, IllegalAccessException{
		System.out.println("In serializer");
		Document doc = new Document(new Element("serialized"));
		IdentityHashMap hashMap = new IdentityHashMap();
		
		
		
		return recurseSerialize(obj, hashMap, doc);
	}
	public static Document recurseSerialize(Object obj, IdentityHashMap hashMap, Document doc) throws IllegalArgumentException, IllegalAccessException{
		hashMap.put(obj, Integer.toString(hashMap.size()));
		// Create an object element
		Element objectElement = new Element("object");
		System.out.println("Created object element");
		objectElement.setAttribute("class", obj.getClass().getName());
		objectElement.setAttribute("id", Integer.toString(hashMap.size()-1)); ////// **** <----- this was the source of the crashing. Needed to put -1 for size, because size was already incremented above. 
		
		doc.getRootElement().addContent(objectElement);
		
		if(obj.getClass().isArray()){
			System.out.println("Is array");
			
			//TODO: Logic for if it is an array. Need to have the form:
			/*
			 * <object class=”[C” id=”8” length=”5”>
					<value>S</value>
					<value>m</value>
					<value>i</value>
					<value>t</value>
					<value>h</value>
				</object>

			 */
			
			objectElement.setAttribute("length", Integer.toString(Array.getLength(obj)));
			Class arrayType = obj.getClass().getComponentType();
			for (int i=0; i < Array.getLength(obj); i++){
				Object subObj = Array.get(obj, i);
				
				// Almost identical duplicate code from below, could extract into its own method
				if (subObj != null){
					if (arrayType.isPrimitive()){
						// Primitive field 
						System.out.println("Is primitive type");
						objectElement.addContent(new Element("value").setText(subObj.toString()));
					}else{
						// Reference field
						System.out.println("Is reference type");
						Element referenceElement = new Element("reference");
						if(hashMap.containsKey(subObj)){
							referenceElement.setText(hashMap.get(subObj).toString());
						}else{
							// Not yet saved in identity hashMap
							referenceElement.setText(Integer.toString(hashMap.size()));
							// Need to change method to allow for recursion for reference values
							recurseSerialize(subObj, hashMap, doc);
						}
						objectElement.addContent(referenceElement);
						//objectElement.addContent(referenceElement);
						
					}
				}else{
					System.out.println("Is null type");
					objectElement.addContent(new Element("null"));
				}
			}
			
			
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
						System.out.println("Is primitive type");
						fieldElement.addContent(new Element("value").setText(fieldObject.toString()));
					}else{
						// Reference field
						System.out.println("Is reference type");
						Element referenceElement = new Element("reference");
						if(hashMap.containsKey(fieldObject)){
							referenceElement.setText(hashMap.get(fieldObject).toString());
						}else{
							// Not yet saved in identity hashMap
							referenceElement.setText(Integer.toString(hashMap.size()));
							// Need to change method to allow for recursion for reference values
							recurseSerialize(fieldObject, hashMap, doc);
						}
						fieldElement.addContent(referenceElement);
						//objectElement.addContent(referenceElement);
						
					}
				}else{
					System.out.println("Is null type");
					fieldElement.addContent(new Element("null"));
				}	
				objectElement.addContent(fieldElement);
			}

		}		
		return doc;
	}
}
