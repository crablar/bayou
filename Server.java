import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {

	private ServerSocket recvsock;
	private HashMap<Socket, PrintWriter> ostreams;
	private HashMap<String, String> playList;
	private HashMap<Integer, Integer> versionVector;
	
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

	private void listen()
	{
		System.out.println("listen entered");
		while(true)
		{
			Socket sock;
			try
			{
				sock = recvsock.accept();
				System.out.println("socket accepted");
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
		}
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		for(Socket sock : ostreams.keySet())
		{
			ostreams.get(sock).close();
			try 
			{
				sock.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
}
