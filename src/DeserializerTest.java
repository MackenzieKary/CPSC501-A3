import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jdom2.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DeserializerTest {

	ClassA classAObj;
	ClassC classCObj;
	@Before
	public void setUp() throws Exception {
		classAObj = new ClassA(false, 8, 'z');
		
		String someString = "abcdefgh";   
		char[] charArr = someString.toCharArray();
		classCObj = new ClassC(charArr);
		
	}


	@Test
	public void testDeserialClassA() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, IOException {
		
		Document serDoc = Serializer.serialize(classAObj);
		// Now deserialize the serialized object
		ClassA desDoc = (ClassA) Deserializer.deserialize(serDoc);
		
		FileOutputStream beforeSerFOS = new FileOutputStream("testBeforeSerialization.txt");
		PrintStream streamSer = new PrintStream(beforeSerFOS);
		System.setOut(streamSer);
		String contentBefore = readFile("testBeforeSerialization.txt", StandardCharsets.UTF_8);
		// Call the visualizer class to get the output of the non-serialzied object
		Visualizer visualize = new Visualizer();
		// Visualize the original object
		visualize.visualize(classAObj, false);	
		beforeSerFOS.close();
		
		
		FileOutputStream afterDesFOS = new FileOutputStream("testAfterDeserialization.txt");
		PrintStream streamDes = new PrintStream(afterDesFOS);
		System.setOut(streamDes);
		String contentAfter = readFile("testAfterDeserialization.txt", StandardCharsets.UTF_8);
		// Call the visualizer class again to get the output of the deserialized object (Make sure deserialization is working)
		visualize.visualize(desDoc, false);	
		afterDesFOS.close();

		
		// Compare the resulting contents of the visualized object as a string. 
		assertEquals(contentBefore, contentAfter);
		
	}
	
	@Test
	public void testDeserialClassC() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, IOException {
		
		Document serDoc = Serializer.serialize(classCObj);
		// Now deserialize the serialized object
		ClassC desDoc = (ClassC) Deserializer.deserialize(serDoc);
		
		FileOutputStream beforeSerFOS = new FileOutputStream("testBeforeSerialization.txt");
		PrintStream streamSer = new PrintStream(beforeSerFOS);
		System.setOut(streamSer);
		String contentBefore = readFile("testBeforeSerialization.txt", StandardCharsets.UTF_8);
		// Call the visualizer class to get the output of the non-serialzied object
		Visualizer visualize = new Visualizer();
		// Visualize the original object
		visualize.visualize(classAObj, false);	
		beforeSerFOS.close();
		
		
		FileOutputStream afterDesFOS = new FileOutputStream("testAfterDeserialization.txt");
		PrintStream streamDes = new PrintStream(afterDesFOS);
		System.setOut(streamDes);
		String contentAfter = readFile("testAfterDeserialization.txt", StandardCharsets.UTF_8);
		// Call the visualizer class again to get the output of the deserialized object (Make sure deserialization is working)
		visualize.visualize(desDoc, false);	
		afterDesFOS.close();

		
		// Compare the resulting contents of the visualized object as a string. 
		assertEquals(contentBefore, contentAfter);
		
	}
	// This readFile method is taken from URL: https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
}
