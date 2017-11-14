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
		ServerSocket ss = new ServerSocket(9999);
		
		Socket s = ss.accept();
		
		DataInputStream dis = new DataInputStream(s.getInputStream());
		
		System.out.println(dis.readUTF());
	}
}
