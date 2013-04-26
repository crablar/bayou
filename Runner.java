import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


/**
* A Runner that handles input
*/

public class Runner
{
	private static Integer unusedPort = 9000;						
	private static boolean scriptMode = false;												
	private static HashMap<Integer, Integer> serverPorts = new HashMap<Integer, Integer>();	//maps Integers to ports ie {0 : 9000}
	private static HashMap<Integer, Client> clients = new HashMap<Integer, Client>();
	private static HashMap<Integer, Server> servers = new HashMap<Integer, Server>();
	private static boolean paused = false;
	private static long delay_interval = 100;
	private static Scanner scanner = null;
	
	public static void main(String[] args)
	{
		System.out.println("Hello Chao.  Welcome to Bayou.");
		if(args == null)
			scanner = new Scanner(System.in);
		else
		{
			try 
			{
				scanner = new Scanner(new File(args[0]));
				scriptMode = true;
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		run();
	}
	
	private static void startClient(Integer cID, Integer server)
	{
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
		clients.get(cID).disconnect();
	}
	
	private static void clientReconnect(Integer cID, Integer sID)
	{
		//TODO
	}
	
	private static void printLog(Integer sID)
	{
		servers.get(sID).printLog();
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
	
	private static void delay()
	{
		long t = System.currentTimeMillis();
		while(System.currentTimeMillis() - delay_interval < t);
	}
	
	private static void run()
	{
		while(true)
		{
			if(!scanner.hasNext())
				break;
			delay();
			String command = scanner.nextLine();
			String[] cmdArgs = command.split(" ");
			if(scriptMode)
				System.out.println("SCRIPT: " + command);
			if(cmdArgs[0].equals("user"))
				clients.get(Integer.parseInt(cmdArgs[1])).userRequest(cmdArgs);
			if(cmdArgs[0].equals("startClient"))
				startClient(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]));
			if(cmdArgs[0].equals("join"))
				join(Integer.parseInt(cmdArgs[1]));
			if(cmdArgs[0].equals("clientDisconnect"))
				clientDisconnect(Integer.parseInt(cmdArgs[1]));
			if(cmdArgs[0].equals("clientReconnect"))
				clientReconnect(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]));
			if(cmdArgs[0].equals("printLog"))
				printLog(Integer.parseInt(cmdArgs[1]));
			if(cmdArgs[0].equals("printLogs"))
				for(Integer sID : servers.keySet())
					printLog(sID);
			if(cmdArgs[0].equals("isolate"))
				isolate(Integer.parseInt(cmdArgs[1]));
			if(cmdArgs[0].equals("leave"))
				leave(Integer.parseInt(cmdArgs[1]));
			if(cmdArgs[0].equals("reconnect"))
				reconnect(Integer.parseInt(cmdArgs[1]));
			if(cmdArgs[0].equals("breakConnection"))
				breakConnection(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]));
			if(cmdArgs[0].equals("recoverConnection"))
				recoverConnection(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]));
			if(cmdArgs[0].equals("pause"))
				paused = true;
			if(cmdArgs[0].equals("continue"))
				paused = false;
			if(cmdArgs[0].equals("quit"))
				System.exit(0);
			if(cmdArgs[0].equals("switch to console mode"))
				scanner = new Scanner(System.in);
		}
	}
	
}