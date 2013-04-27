import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
			System.out.println("port is " + port);
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
			sendsock = new Socket();
			sendsock.connect(new InetSocketAddress(InetAddress.getLocalHost(), otherPort));
			System.out.println(this + ": connect to server " + otherPort);
			PrintWriter dout = new PrintWriter(sendsock.getOutputStream(), true);
			dout.println("=====> " + this.recvsock.getLocalPort() + ": requesting ACK from " + otherPort);

			//sendsock.getOutputStream().write((this + ": requesting ACK from " + otherPort).getBytes());
			new ReplicaThread(this, sendsock);
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
				System.out.println(this + " receiving connection from " + sock.getPort());
				PrintWriter dout = new PrintWriter(sock.getOutputStream(), true);
				dout.println("=======> I am " + recvsock.getLocalPort());
				ostreams.put(sock, dout);
				new ReplicaThread(this, sock);
			}
			catch(SocketException e)
			{
				//TODO ???????????????????????
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
		catch(SocketException e)
		{
			e.printStackTrace();
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
		System.out.println("handling a message!");
		if(msg != null)
			System.out.println(this + ": " + msg);
	}
	
	public String toString()
	{
		return "Server on port " + port;
	}
	
}
