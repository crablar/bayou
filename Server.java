import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {

	private int port;
	private ServerSocket recvsock;
	private ArrayList<Socket> socks;
	private HashMap<Socket, PrintWriter> ostreams;
	private HashMap<String, String> playList;
	private HashMap<Integer, Integer> versionVector;	//Make this a separate data structure?
	private Log log;
	
	public Server(int p)
	{
		port = p;
		socks = new ArrayList<Socket>();
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
		Thread listenThread = new Thread(listener);	//this is to give control back to Runner
		listenThread.start();
	}
	
	public void connectToServer(int otherPort)
	{
		try {
			Socket sock = new Socket();
			sock.connect(new InetSocketAddress(InetAddress.getLocalHost(), otherPort));
			System.out.println(this + ": connect to server " + otherPort);
			ostreams.put(sock,  new PrintWriter(sock.getOutputStream()));
			ostreams.get(sock).write(this + ": requesting ACK from " + otherPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listen()
	{
		while(true)
		{
			Socket sock;
			try
			{
				sock = recvsock.accept();
				if(port == sock.getLocalPort())
					continue;
				System.out.println(this + " receiving connection from " + sock.getLocalPort());
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
			System.out.println(this + ": " + msg);
	}
	
	public String toString()
	{
		return "Server on port " + port;
	}
	
}
