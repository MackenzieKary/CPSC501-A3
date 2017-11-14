import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Deserializer {

	public static void main(String[] args) throws UnknownHostException, IOException{
		ServerSocket ss = new ServerSocket(9998);
		System.out.println("Before accept");
		Socket s = ss.accept();
		System.out.println("After accept");
		DataInputStream dis = new DataInputStream(s.getInputStream());
		System.out.println("After DIS");
		System.out.println("Dis = " + dis);
		System.out.println(dis.readUTF());
	}
}
