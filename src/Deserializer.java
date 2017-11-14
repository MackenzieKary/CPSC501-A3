import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Deserializer {

	public static void main(String[] args) throws UnknownHostException, IOException{
		ServerSocket ss = new ServerSocket(9998);
		System.out.println("Open Connection...");
		//Socket connectedSocket = new Socket("localhost", 9998);
		Socket connectedSocket = ss.accept();
		System.out.println("Connection Accepted");
		byte[] buffer = new byte[99999]; // Set byte array to something large
		int inputBytes = connectedSocket.getInputStream().read(buffer, 0, buffer.length);
		BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream("docReceived.xml"));
		buffStream.write(buffer, 0, inputBytes);
		buffStream.close();
		connectedSocket.close();
		ss.close();
		
	}
}
