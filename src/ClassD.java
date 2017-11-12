
public class ClassD {
	public ClassA[] classAObjArray;
	
	public ClassD(){
		// Initialize object array
		classAObjArray = new ClassA[10];
		
		for (int i = 0; i < classAObjArray.length; i++) {
			classAObjArray[i] = new ClassA();
		}
	}
	
}
