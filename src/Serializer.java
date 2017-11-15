import org.jdom2.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class Serializer {

	public static Document serialize(Object obj) throws IllegalArgumentException, IllegalAccessException{
		Document doc = new Document(new Element("serialized"));
		IdentityHashMap hashMap = new IdentityHashMap();
		return recurseSerialize(obj, hashMap, doc);
	}
	public static Document recurseSerialize(Object obj, IdentityHashMap hashMap, Document doc) throws IllegalArgumentException, IllegalAccessException{
		String id = Integer.toString(hashMap.size());
		hashMap.put(obj, id);
		// Create an object element
		Element objectElement = new Element("object");
		
		objectElement.setAttribute("class", obj.getClass().getName());
		objectElement.setAttribute("id", id); ////// **** <----- this was the source of the crashing. Needed to put -1 for size, because size was already incremented above. 
		
		doc.getRootElement().addContent(objectElement);
		
		if(obj.getClass().isArray()){
			
			objectElement.setAttribute("length", Integer.toString(Array.getLength(obj)));
			Class arrayType = obj.getClass().getComponentType();
			for (int i=0; i < Array.getLength(obj); i++){
				Object subObj = Array.get(obj, i);
				
				// Almost identical duplicate code from below, could extract into its own method
				if (subObj != null){
					if (arrayType.isPrimitive()){
						// Primitive field 
						objectElement.addContent(new Element("value").setText(subObj.toString()));
					}else{
						// Reference field
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
					objectElement.addContent(new Element("null"));
				}
			}
			
			
		}else{
			
			Class reflectClass = obj.getClass();			
			
			//Field[] classFields = reflectClass.getDeclaredFields();
			
			List validFields = new ArrayList();
			Field[] fields = reflectClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (!Modifier.isStatic(fields[i].getModifiers())) {
					validFields.add(fields[i]);
				}
			}	
			Field[] classFields = (Field[]) validFields.toArray(new Field[validFields.size()]);

			for (Field classField : classFields){
				classField.setAccessible(true);
				
				Element fieldElement = new Element("field");
				fieldElement.setAttribute("name", classField.getName());
				fieldElement.setAttribute("declaringclass", classField.getDeclaringClass().getName());
				
				Object fieldObject = classField.get(obj);
				// From textbook (if statement)
				if (Modifier.isTransient(classField.getModifiers())) { 
					fieldObject = null; 
				} 
				
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
							recurseSerialize(fieldObject, hashMap, doc);
						}
						fieldElement.addContent(referenceElement);
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
