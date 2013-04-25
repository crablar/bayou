import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class Client {
	
	private String name;
	private ArrayList<Socket> socks;
	
	public Client(int port)
	{
		try
		{
			Socket sock = new Socket("localhost", port);
		}
		catch(SocketException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
