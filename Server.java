import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {

	private ServerSocket recvsock;
	private HashMap<Socket, PrintWriter> ostreams;
	private HashMap<String, String> playList;
	private HashMap<Integer, Integer> versionVector;	//Make this a separate data structure?
	private Log log;
	
	public Server(int port)
	{
		ostreams = new HashMap<Socket, PrintWriter>();
		playList = new HashMap<String, String>();
		versionVector = new HashMap<Integer, Integer>();
		try
		{
			recvsock = new ServerSocket(port);
			addShutdownHooks(this);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		Runnable listener = new Runnable()
		{
			public void run()
			{
				listen();
			}
		};
		Thread listenThread = new Thread(listener);	//this is janky but necessary to cede control to Runner
		listenThread.start();
	}
	
	public void connectToServer(int port)
	{
		//TODO
	}

	private void listen()
	{
		while(true)
		{
			Socket sock;
			try
			{
				sock = recvsock.accept();
				PrintWriter dout = new PrintWriter(sock.getOutputStream(), true);
				ostreams.put(sock, dout);
				new ReplicaThread(this, sock);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void addShutdownHooks(final Server server)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			public void run()
			{
				server.shutdown();
			}
		}));
	}
	
	private void shutdown()
	{
		try 
		{
			recvsock.close();
			for(Socket sock : ostreams.keySet())
			{
				ostreams.get(sock).close();
				sock.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void printLog() {
		//TODO
	}

	public void handleReplicaMessage(String msg)
	{
		if(msg != null)
			System.out.println(msg);
	}
	
}
