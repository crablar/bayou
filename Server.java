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

/**
 * A Bayou replica.
 * 
 * @author jam4879
 *
 */

public class Server {

	private int sID;									//the server ID
	private int port;									//this server's port number
	//private Socket sendsock;							//the socket used for connecting and sending to this server
	private ServerSocket recvsock;						//socket to accept replica and client connections
	private HashMap<Integer, Socket> socks;				//map of sID -> replica sockets
	private HashMap<Socket, PrintWriter> ostreams;		
	private Playlist playlist;
	private VersionVector versionVector;
	private Log tentativeWrites;						//a log of writes that haven't been committed
	private Log rollbackLog;							//a log of all writes that have not been garbage collected
	private int csn;
	private boolean isPrimary;							//says whether this server is the primary
	
	
	public Server(int p, int sID)
	{
		this.sID = sID;
		port = p;
		isPrimary = false;
		socks = new HashMap<Integer, Socket>();
		ostreams = new HashMap<Socket, PrintWriter>();
		playlist = new Playlist();
		versionVector = new VersionVector();
		tentativeWrites = new Log();					//the tentative writes to this server
		rollbackLog = new Log();						//the stable writes that this server is aware of
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
			//sendsock = new Socket("localhost", otherPort);
			
			Socket sendsock = new Socket();
			sendsock.connect(new InetSocketAddress(InetAddress.getLocalHost(), otherPort));
			
			//System.out.println(this + ": connect to server " + otherPort);
			//PrintWriter dout = new PrintWriter(sendsock.getOutputStream(), true);
			//dout.println("=====> " + this.recvsock.getLocalPort() + ": requesting ACK from " + otherPort);

			//sendsock.getOutputStream().write((this + ": requesting ACK from " + otherPort).getBytes());
			
			PrintWriter dout = new PrintWriter(sendsock.getOutputStream(), true);
			ostreams.put(sendsock, dout);
			
			socks.put(otherPort, sendsock);
			new ReplicaThread(this, sendsock);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnectionTo(int otherPort)
	{
		try {
			socks.remove(otherPort).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
				ostreams.put(sock, dout);
				
				//dout.println("=======> I am " + recvsock.getLocalPort());
				socks.put(sock.getPort(), sock);
				
				new ReplicaThread(this, sock);
			}
			catch(SocketException e)
			{
				System.exit(0);
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

	
	public void printForUser(int cID) {
		System.out.println(playlist.toString());
	}

	public String toString()
	{
		return "Server on port " + port;
	}

	public synchronized void addToPlaylist(String song, String url) 
	{
		long acceptStamp = System.currentTimeMillis();
		Write w = new Write(acceptStamp, sID, false, "add " + song + " " + url);
		versionVector.changeLatestAccept(sID, acceptStamp);
		tentativeWrites.log(w);
		playlist.add(song, url);
	}


	public synchronized void editPlaylist(String song, String url) {
		long acceptStamp = System.currentTimeMillis();
		Write w = new Write(acceptStamp, sID, false, "edit " + song + " " + url);
		versionVector.changeLatestAccept(sID, acceptStamp);
		tentativeWrites.log(w);
		playlist.edit(song, url);
	}

	public synchronized void deleteFromPlaylist(String song) {
		long acceptStamp = System.currentTimeMillis();
		Write w = new Write(acceptStamp, sID, false, "delete " + song);
		versionVector.changeLatestAccept(sID, acceptStamp);
		tentativeWrites.log(w);
		playlist.delete(song);		
	}
	
	public String getPlaylistString()
	{
		return playlist.toString();
	}
	
	/**
	 * The primary stabilizes all writes up to the earliest acceptStamp in its version vector.
	 */
	public synchronized void stabilizeWrites()
	{
		if(!isPrimary)
			return;
		//TODO
	}

	public void makePrimary() {
		isPrimary = true;
		//TODO
	}
	
	public void sendMessageToServer(int otherPort, String message)
	{
		System.out.println(otherPort);
		System.out.println(ostreams.get(socks.get(otherPort)));
		ostreams.get(socks.get(otherPort)).println(message);
	}
	
	
	public synchronized void sendVersionVectorTo(int sID) {
		Socket sock = socks.get(sID);
		PrintWriter dout = (PrintWriter) (ostreams.get(sock));
		
		String msg = Constants.versionVector_msg;
		msg += sock.getLocalPort() + " " + (this.versionVector).toString();
		
		dout.println(msg);
	}
	
	
	public synchronized void anti_entropy(int sID) {
		
	}
	
}
