import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 * A thread that is created whenever a Server accepts a new Socket connection
 * 
 * @author jam4879
 *
 */

public class ReplicaThread extends Thread {
	
	private Server server;
	private Socket sock;
	private BufferedReader in;
	private boolean listeningToClient;
	
	public ReplicaThread(Server serv, Socket sock)
	{
		listeningToClient = false;
		in = null;
		server = serv;
		this.sock = sock;
		System.out.println("starting ReplicaThread for " + server + ", listening to " + sock.getPort());
		start();
	}
	
	public void run()
	{
		try
		{
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String line;
			boolean keepGoing;
			while((line = in.readLine()) != null)
			{
				if(line.startsWith("I'm a client"))
					listeningToClient = true;
				if(listeningToClient)
					keepGoing = handleClientMessage(line);
				else 
					keepGoing = handleReplicaMessage(line);
				if(!keepGoing)
					return;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				in.close();
			} catch (NullPointerException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private boolean handleClientMessage(String msg) {
		System.out.println(server + " received: " + msg);
		try
		{
			if(msg.startsWith("client disconnecting"))
			{
				System.out.println("ReplicaThread closing connection with client on " + sock.getPort());
				sock.close();
				in.close();
				return false;
			}
			else if(msg.startsWith("printPlaylist"))
				server.printPlaylist();
			else if(msg.startsWith("add"))
			{
				String[] msgArgs = msg.split(" ", 3);
				server.addToPlaylist(msgArgs[1], msgArgs[2]);
			}
			else if(msg.startsWith("edit"))
			{
				String[] msgArgs = msg.split(" ", 3);
				server.editPlaylist(msgArgs[1], msgArgs[2]);
			}
			else if(msg.startsWith("delete"))
			{
				String[] msgArgs = msg.split(" ", 2);
				server.deleteFromPlaylist(msgArgs[1]);
			}
		}
		catch(IOException e)
		{
			System.out.println("Problem with client-server in ReplicaThread");
		}
			return true;
	}
	
	private boolean handleReplicaMessage(String msg)
	{
		System.out.println(server + " received: " + msg);
		try
		{
			if(msg.startsWith("test connection"))
					server.print(" receiving test message from server on " + sock.getPort());
			else if(msg.startsWith("server disconnecting"))
			{
				System.out.println("ReplicaThread closing connection with " + server);
				server.closeConnectionTo(Integer.parseInt(msg.split(" ")[1]));
				in.close();
				return false;
			}
			else if(msg.startsWith("serverConnect"))
			{
				Integer otherID = Integer.parseInt(msg.split(" ")[1]);
				server.addConnectionToMap(otherID, sock);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}
}
