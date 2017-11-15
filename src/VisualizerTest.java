import static org.junit.Assert.*;
import java.lang.reflect.Method;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class VisualizerTest {

	Method inspectionMethod = null;
	Method getClassNameMethod = null;
	Object ObjInspector = null;
	Class objInspectClass = null;
	
	
	@Before
	public void setUp() throws Exception {
		try{
			objInspectClass = Class.forName("Visualizer");
			ObjInspector = objInspectClass.newInstance();
	    }catch(Exception e) {
	    	throw new Exception("Unable create instance of your object inspector");
	    }
		try{
			Class[] param = { Object.class, boolean.class };
			inspectionMethod = objInspectClass.getDeclaredMethod("visualize",param);
			getClassNameMethod = objInspectClass.getDeclaredMethod("getClassName",null);
	    }catch(Exception e) {
	    	throw new Exception("Unable to find required method: public void inspect(Object obj,boolean recursive)");
	    }
	}

	/*
	 * testGetClassNameClassA() case will test the visualizer to make sure that it correctly retrieves the correct class name of the simple object class (ClassA). 
	 */
	@Test
	public void testGetClassNameClassA() throws Exception{
		Object[] param = { new ClassA(), new Boolean(false) };
		inspectionMethod.invoke(ObjInspector, param);
		
		String className = (String) getClassNameMethod.invoke(ObjInspector);
		
		assertEquals("ClassA", className);
	}
}
