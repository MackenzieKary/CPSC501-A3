import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

public class SocketOut {

	public static void sendDoc(Document doc) throws FileNotFoundException, IOException{

		System.out.println("Doc: " + doc);
		new XMLOutputter().output(doc, new FileOutputStream("docToSend.xml"));

		
		/*
		 * Start of network connection part
		 * 
		 * XML object should be created as new file at this point and called "docToSend.xml"
		 * 
		 */
		
		int port = 9998; 	// create the port number (accepting end will need same port number)

		Socket connectedSocket = new Socket("localhost", 9998);
		System.out.println("Listening...");

		System.out.println("Connection Aquired");
		
		File fileToSend = new File("docToSend.xml");		// Turn XMLOuputter file into File type
		int fileToSendLength = (int) fileToSend.length();
;
		int count;
		byte[] buffer = new byte[fileToSendLength];

		OutputStream out = connectedSocket.getOutputStream();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream("docToSend.xml"));
		
		while ((count = in.read(buffer)) > 0) {
			  out.write(buffer, 0, count);
		}
		connectedSocket.close();
			
	}
}
