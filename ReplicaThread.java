import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ReplicaThread extends Thread {
	
	private Server server;
	private Socket sock;
	
	public ReplicaThread(Server serv, Socket sock)
	{
		server = serv;
		this.sock = sock;
		start();
	}
	
	public void run()
	{
		BufferedReader in;
		try
		{
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String inputMsg;
			System.out.println("somethings");
			while((inputMsg = in.readLine()) != null)
			{
				System.out.println(inputMsg);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
