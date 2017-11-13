import org.jdom2.*;
import java.lang.reflect.*;
import java.util.IdentityHashMap;

public class Serializer {

	public static Document serialize(Object obj){
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
			
		}
		
		
		return null;
	}
}
