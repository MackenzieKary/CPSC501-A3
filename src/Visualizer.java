import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.lang.reflect.Array;


/*
 * This code was taken from my assignment 2,
 * where an inspection was performed on an object, to 
 * visualize the object and its components
 * 
 */
public class Visualizer {
	public Visualizer() { }
	/* TODO
	 * 
	 * setAccessible for private/protected/etc stuff
	 * 
	 */
	private static ArrayList<String> recursedClasses = new ArrayList<String>();
	private static ArrayList<String> recursedSuperClasses = new ArrayList<String>();
	private String classNameToTest = "";
	private String fieldValuesToTest = "";
	
	public void visualize(Object obj, boolean recursive) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		// Check if object passed in is an array
		if (obj.getClass().isArray()){
			int length = Array.getLength(obj);
		    for (int i = 0; i < length; i ++) {
		    	Object x = Array.get(obj, i);
				if (x == null){
					// Null when an array of objects has been created, but not yet instantiated 
					System.out.println("\tArray value for index " + i + " = " + x);
				}else{
					visualize(x, recursive);
				}
		        System.out.println("\tArray reference: " + obj.toString() +"\n");
		    }
		}else{
			Class reflectionClass = obj.getClass();
			
			System.out.println("Inspecting Object: " + obj + " (recursive = "+recursive+")");
			// Inspect current class
			getClassName(reflectionClass);
			// Get remaining information
			getInformation(reflectionClass, obj, recursive);
		}
	}
	
	// Four methods below are used for testing 
	private void setClassName(String cName){
		classNameToTest = cName;
	}
	public String getClassName(){
		return classNameToTest;
	}
	private void setFieldValues(String fValues){
		fieldValuesToTest = fieldValuesToTest + " " +fValues;
	}
	private String getFieldValues(){
		return fieldValuesToTest;
	}
	
	// Get the name of the class
	private void getClassName(Class reflectClass){
		String className = reflectClass.getName();
		System.out.println("Class Name: " + className);
		setClassName(className);
	}
	private void getInformation(Class reflectClass, Object obj, boolean recursive) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		getSuperclassName(reflectClass, obj, recursive);
		getInterfaceNames(reflectClass, obj, recursive);
		getClassConstructors(reflectClass);
		getClassMethods(reflectClass);
		getClassFields(reflectClass);
		getClassFieldsValues(reflectClass, obj, recursive);
	}
	
	// Get the name of the superclass
	private void getSuperclassName(Class reflectClass, Object obj, boolean recursive) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class reflectionClassSuper = reflectClass.getSuperclass();
		
		// Traverse through the hierarchy of superclasses 
		while (reflectionClassSuper != null) {
			System.out.println("Superclass: "+reflectionClassSuper.getName());
			System.out.println("\t>->->->->-> Traversing superclass: " + reflectionClassSuper.getName()+" >->->->->->");
			getInformation(reflectionClassSuper, obj, recursive);
			System.out.println("\t<-<-<-<-<-< End of traversal of superclass: " + reflectionClassSuper.getName()+" <-<-<-<-<-<");
			reflectionClassSuper = reflectionClassSuper.getSuperclass();		  
		}
	}
	
	// Get the name of the interfaces
	private void getInterfaceNames(Class reflectClass, Object obj, boolean recursive) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		Class[] interfaces = reflectClass.getInterfaces();	
		
		// Traverse through the hierarchy of interfaces
		for(Class classInterface : interfaces){
			System.out.println("Interface Name: " + classInterface.getName());
			System.out.println("\t>>>>>>>>>>> Traversing Interface: " + classInterface.getName()+" >>>>>>>>>>>");
			getInformation(classInterface, obj, recursive);
			System.out.println("\t<<<<<<<<<<< End of traversal of interface: " + classInterface.getName()+" <<<<<<<<<<<");
		}
	}
	
	// Get the methods in class and their info
	private void getClassMethods(Class reflectClass){
		// Return the following:
			// Name
			// Exceptions thrown
			// Parameter Type
			// Return Type
			// Modifiers
		
		Method[] classMethods = reflectClass.getDeclaredMethods();
		System.out.println("-Methods below for class : "+ reflectClass.getName() +", number of total methods= "+classMethods.length);
		for (Method classMethod : classMethods){
			classMethod.setAccessible(true);
			// Get method name 
			String methodName = classMethod.getName();
			System.out.println("\tMethod name: " + methodName);
			
			// Get Method Exception Types 
			Class[] exceptions = classMethod.getExceptionTypes();
			for (Class exception : exceptions){
				String exceptionType = exception.getName();
				System.out.println("\t\tException type: " + exceptionType);
			}
			
			// Get Parameter Types
			Class[] parameters = classMethod.getParameterTypes();
			for (Class parameter : parameters){
				String parameterType = parameter.getName();
				System.out.println("\t\tParameter type: " + parameterType);
			}
			
			// Get Return Type
			Class returnType = classMethod.getReturnType();
			System.out.println("\t\tReturn Type: " + returnType);
			
			// Get Modifiers
			int methodModifiers = classMethod.getModifiers();
			System.out.println("\t\tModifiers: " + Modifier.toString(methodModifiers));
		}
		System.out.println("-Done looking at methods for class : "+ reflectClass.getName());
	}
	
	// Get the constructors in class and their info
	private void getClassConstructors(Class reflectClass){
		// Print the following:
			// Parameter Types
			// Modifiers
		
		// Get all constructors 
		Constructor[] classConstructors = reflectClass.getDeclaredConstructors();
		for (Constructor classConstructor : classConstructors){
			classConstructor.setAccessible(true);
			// Get Constructor Name
			String constructorName = classConstructor.getName();
			System.out.println("Constructor Name: "+ constructorName);
			
			// Get Parameter Types
			Class[] parameters = classConstructor.getParameterTypes();
			for (Class parameter : parameters){
				String parameterType = parameter.getName();
				System.out.println("\tParameter type: " + parameterType);
			}
			
			// Get Modifiers
			int methodModifiers = classConstructor.getModifiers();
			System.out.println("\tModifiers: " + Modifier.toString(methodModifiers));
		}
	}
	
	// Get the fields in the class and their info
	private void getClassFields(Class reflectClass){
		// Print the following:
			// Type
			// Modifiers

		// Get all fields (declared)
		Field[] classFields = reflectClass.getDeclaredFields();
		System.out.println("\nField info below for: " + reflectClass);
		for (Field classField : classFields){
			classField.setAccessible(true);
			// Get Field Name
			String fieldName = classField.getName();
			System.out.println("\tField Name: "+ fieldName);
			
			// Get Field Type
			Class fieldType = classField.getType();
			System.out.println("\t\tField Type: " + fieldType);
			
			// Get Field Modifiers
			int fieldModifiers = classField.getModifiers();
			System.out.println("\t\tField Modifiers: "+ Modifier.toString(fieldModifiers));
		}	
		System.out.println("\nEnd of fields info for: " + reflectClass);
	}
	
	// Get the values of the fields within the class
	private void getClassFieldsValues(Class reflectClass, Object obj, boolean recursive) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		// Print current value of each field

		// Get all fields (declared)
		Field[] classFields = reflectClass.getDeclaredFields();
		System.out.println("\nField values below for: " + reflectClass);
		for (Field classField : classFields){
			classField.setAccessible(true);
			// Get Field Name
			String fieldName = classField.getName();
			System.out.println("\tField Name: "+ fieldName);
			
			// Check if field is an array
			Class fieldType = classField.getType();
			if (fieldType.isArray()){
				// if it is an array, we need to figure out the type of array
				Class arrType = fieldType.getComponentType();
				System.out.println("\t\tArray Type = "+arrType);
				
				if(!arrType.isPrimitive()){
					// If array is not primitive, then it is objects. (Likely calling another class)
					Object arrValues = null;
					try {
						arrValues = classField.get(obj);
						System.out.println("\t\tArray Reference Value = " +arrValues+"\n");
						int length = Array.getLength(arrValues);
						
						
						for (int i = 0; i < length; i++) {
							if (recursive){
								Object x = Array.get(arrValues, i);
								if (x == null){
									// Null when an array of objects has been created, but not yet instantiated 
									System.out.println("\t\tArray value for index " + i + " = " + x);
								}else{
									Class cls = Class.forName(arrType.getName());
					                Object object = cls.newInstance();
					                System.out.println("Object  = " + object);
					                visualize(object, recursive);
								}
							}else{
								// Print reference values & identity hashcode
								System.out.println("\tField Reference Value: "+ arrValues.getClass().getName());
								System.out.println("\tField Identity HashCode: "+ System.identityHashCode(arrValues));
							}
							System.out.println("\t        ________________ ");
						}			
					} catch (IllegalArgumentException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}else{
					// Is primitive array
					Object arrValues = null;
					try {
						arrValues = classField.get(obj);
						int length = Array.getLength(arrValues);
						
						
						for (int i = 0; i < length; i++) {
								// Print primitive array value
							
							char x = Array.getChar(arrValues, i);

							System.out.println("\tField Value: "+ x);
						}			
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
				}

			}else{
				// Not an array
				// Get Field Value (might need to use recursion) 
				Object fieldValue = null;
				try {
					fieldValue = classField.get(obj); 
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				
				if (fieldValue.getClass().toString().equals("class java.util.ArrayList")){				
					System.out.println("\t\tField Value: " + fieldValue.getClass());  
				}
				else{
					System.out.println("\t\tField Value ("+reflectClass+"): " + fieldValue); 
				}
				if (recursive){
					if (fieldValue != null && !fieldValue.getClass().isPrimitive()){
						setFieldValues(fieldValue.toString());
						// This code is responsible for recursing when a field is set to a class object. (Go into the class object)
						if (Object.class.isAssignableFrom(classField.getType()) && !recursedClasses.contains(fieldType.getName()) ){
							System.out.println("\t-------Field Value " + fieldName + " is a class object: "+fieldValue.getClass()+", RECURSING NOW");
							recursedClasses.add(fieldType.getName());
							visualize(fieldValue, recursive);
							System.out.println("\t-------Done recursing class: " + fieldValue.getClass() +" for value: " + fieldName+" <-- Belonging to "+reflectClass+"\n\n");
						}
					}
				}
				recursedClasses.clear();
			}
		}	
		System.out.println("\nEnd of fields values for: " + reflectClass);
	}
	
}
