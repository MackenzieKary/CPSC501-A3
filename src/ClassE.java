import java.util.ArrayList;
import java.util.Collection;

public class ClassE {
	public Collection<ClassA> classAList; 
	
	public ClassE() {
		classAList = new ArrayList<ClassA>();
		for(int i=0; i<3; i++){
			// Create 3 instances of of ClassA
			classAList.add(new ClassA());
		}
	}
}



