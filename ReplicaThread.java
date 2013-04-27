import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * A thread that is created whenever a Server accepts a new Socket connection
 * 
 * @author jam4879
 *
 */

public class ReplicaThread extends Thread {
	
	private Server server;
	private Socket sock;
	
	public ReplicaThread(Server serv, Socket sock)
	{
		server = serv;
		this.sock = sock;
		System.out.println("starting ReplicaThread for " + server + ", listening to " + sock.getPort());
		start();
	}
	
	public void run()
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String line;
			while((line = in.readLine()) != null)
			{
				server.handleReplicaMessage(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
