import java.util.HashMap;
import java.util.Scanner;


/**
* Bayou
* @author Jeff
*/

public class Runner
{
	private static Integer unusedPort = 9000;
	private final static String scriptName = "sample_script";
	private static HashMap<Integer, Integer> serverPorts = new HashMap<Integer, Integer>();	//maps Integers to ports ie {0 : 9000}
	private static HashMap<Integer, Client> clients = new HashMap<Integer, Client>();
	private static HashMap<Integer, Server> servers = new HashMap<Integer, Server>();
	private static boolean paused = false;
	
	public static void main(String[] args)
	{
		System.out.println("Hello Chao.  Welcome to Bayou.");
		Scanner input = new Scanner(System.in);
		while(true)
		{
			if(!input.hasNext())
				break;
			String[] cmdArgs = input.nextLine().split(" ");
			if(cmdArgs[0].equals("startClient"))
				startClient(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]));
			if(cmdArgs[0].equals("join"))
				join(Integer.parseInt(cmdArgs[1]));
			if(cmdArgs[0].equals("user"))
				clients.get(cmdArgs[1]).userRequest(cmdArgs);
		}
	}
	
	private static void startClient(Integer cID, Integer server)
	{
		System.out.println("wtf");
		Client client = new Client(cID, serverPorts.get(server));
		clients.put(cID, client);
	}
	
	private static void join(Integer sID)
	{
		servers.put(unusedPort, new Server(unusedPort));
		serverPorts.put(sID, unusedPort++);
	}
	
	private static void clientDisconnect(Integer cID)
	{
		//TODO
	}
	
	private static void clientReconnect(Integer cID, Integer sID)
	{
		//TODO
	}
	
	private static void pause()
	{
		//TODO
	}
	
	private static void printLogs()
	{
		//TODO
	}
	
	private static void printLog(Integer sID)
	{
		//TODO
	}
	
	private static void isolate(Integer sID)
	{
		//TODO
	}
	
	private static void reconnect(Integer sID)
	{
		//TODO
	}
	
	private static void breakConnection(Integer sA, Integer sB)
	{
		//TODO
	}
	
	private static void recoverConnection(Integer sA, Integer sB)
	{
		//TODO
	}
	
	private static void leave(Integer sID)
	{
		//TODO
	}
}