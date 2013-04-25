import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

/**
* Bayou
* @author Jeff
*/

public class Runner
{
	private static int startingPort = 9000;
	private final static String scriptName = "sample_script";
	private static HashMap<Integer, Integer> serverPorts = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Client> clients = new HashMap<Integer, Client>();
	
	public static void main(String[] args)
	{
		System.out.println("Hello Chao.  Welcome to Bayou.");
	}
	
	private void startClient(Integer cID, Integer server)
	{
		Client client = new Client(serverPorts.get(server));
		clients.put(cID, client);
	}
	
	private void join(Integer sID)
	{
		//TODO
	}
	
	private void clientDisconnect(Integer cID)
	{
		//TODO
	}
	
	private void clientReconnect(Integer cID, Integer sID)
	{
		//TODO
	}
	
	private void pause()
	{
		//TODO
	}
	
	private void printLogs()
	{
		//TODO
	}
	
	private void printLog(Integer sID)
	{
		//TODO
	}
	
	private void isolate(Integer sID)
	{
		//TODO
	}
	
	private void reconnect(Integer sID)
	{
		//TODO
	}
	
	private void breakConnection(Integer sA, Integer sB)
	{
		//TODO
	}
	
	private void recoverConnection(Integer sA, Integer sB)
	{
		//TODO
	}
	
	private void leave(Integer sID)
	{
		//TODO
	}
}