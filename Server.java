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

	private int sID;									//the server ID
	private int port;									//this server's port number
	private Socket sendsock;							//the socket used for connecting and sending to this server
	private ServerSocket recvsock;						//socket to accept replica and client connections
	private ArrayList<Socket> socks;					//list of replica sockets
	private HashMap<Socket, PrintWriter> ostreams;
	private HashMap<String, String> playList;
	private VersionVector versionVector;
	private Log writeLog;
	
	public Server(int p, int sID)
	{
		this.sID = sID;
		port = p;
		socks = new ArrayList<Socket>();
		ostreams = new HashMap<Socket, PrintWriter>();
		playList = new HashMap<String, String>();
		versionVector = new VersionVector();
		try
		{
			recvsock = new ServerSocket(port);
			recvsock.setReuseAddress(true);
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
			sendsock = new Socket();
			sendsock.connect(new InetSocketAddress(InetAddress.getLocalHost(), otherPort));
			System.out.println(this + ": connect to server " + otherPort);
			sendsock.getOutputStream().write((this + ": requesting ACK from " + otherPort).getBytes());
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
				System.out.println(this + " is waiting for connections");
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
