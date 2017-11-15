import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketIn {
	public static void receiveDoc() throws IOException{
		ServerSocket ss = new ServerSocket(9998);
		System.out.println("Open Connection...");
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
