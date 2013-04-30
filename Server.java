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
import java.util.Iterator;

/**
 * A Bayou replica.
 * 
 * @author jam4879
 *
 */

public class Server {

	private int sID;									//the server ID
	private int port;									//this server's port number
	private Socket sendsock;							//the socket used for connecting and sending to this server
	private ServerSocket recvsock;						//socket to accept replica and client connections
	private HashMap<Integer, Socket> socks;				//map of sID/ports -> replica sockets
	private HashMap<Socket, PrintWriter> ostreams;		
	private Playlist playlist;
	private VersionVector versionVector;
	private Log write_log;						//a log of writes that haven't been committed
	private Log rollbackLog;							//a log of all writes that have not been garbage collected
	private int csn;
	private boolean isPrimary;							//says whether this server is the primary
	private ArrayList<Integer> lastContacts;
	private boolean freeForEntropy;
	
	public Server(int p, int sID)
	{
		this.sID = sID;
		port = p;
		isPrimary = false;
		socks = new HashMap<Integer, Socket>();
		ostreams = new HashMap<Socket, PrintWriter>();
		playlist = new Playlist();
		versionVector = new VersionVector();
		write_log = new Log();					//the tentative writes to this server
		rollbackLog = new Log();						//the stable writes that this server is aware of
		
		lastContacts = new ArrayList<Integer>();
		freeForEntropy = true;
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
	
	public void connectToServer(int otherPort, int otherID)
	{
		if(!(socks.containsKey(otherID) || socks.containsKey(otherPort))){
			try {
				sendsock = new Socket();
				sendsock.connect(new InetSocketAddress(InetAddress.getLocalHost(), otherPort));
				//System.out.println(this + ": connect to server " + otherPort);
				PrintWriter dout = new PrintWriter(sendsock.getOutputStream(), true);
				//dout.println("=====> " + this.recvsock.getLocalPort() + ": requesting ACK from " + otherPort);
				//sendsock.getOutputStream().write((this + ": requesting ACK from " + otherPort).getBytes());
				dout.println("serverConnect " + sID);
				socks.put(otherID, sendsock);
				ostreams.put(sendsock, dout);
				new ReplicaThread(this, sendsock);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeConnectionTo(int otherPort)
	{
		try {
			Socket temp = socks.remove(otherPort);
			if(temp != null)
			{
				ostreams.remove(temp).close();
				temp.close();
			}	
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
				//dout.println("=======> I am " + recvsock.getLocalPort());
				ostreams.put(sock, dout);
				socks.put(sock.getPort(), sock);
				new ReplicaThread(this, sock);
				dout.println("serverConnect " + sID);
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

	
	public void printPlaylist() {
		System.out.println(playlist);
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
		write_log.log(w);
		playlist.add(song, url);
	}


	public synchronized void editPlaylist(String song, String url) {
		long acceptStamp = System.currentTimeMillis();
		Write w = new Write(acceptStamp, sID, false, "edit " + song + " " + url);
		versionVector.changeLatestAccept(sID, acceptStamp);
		write_log.log(w);
		playlist.edit(song, url);
	}

	public synchronized void deleteFromPlaylist(String song) {
		long acceptStamp = System.currentTimeMillis();
		Write w = new Write(acceptStamp, sID, false, "delete " + song);
		versionVector.changeLatestAccept(sID, acceptStamp);
		write_log.log(w);
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
	
	public void print(String msg)
	{
		System.out.println(msg);
	}
	
	public void sendMessageToServer(int otherID, String message)
	{
		System.out.println(message);
		ostreams.get(socks.get(otherID)).println(message);
	}

	public void addConnectionToMap(Integer otherID, Socket sock) {
		socks.remove(sock.getPort());
		socks.put(otherID, sock);
	}

	public void printConnections() {
		System.out.println("Server" + this.sID  + " connections");
		for(Integer sID : socks.keySet())
			System.out.println(sID + " -> " + " remote port: " + socks.get(sID).getPort() + " local port " + socks.get(sID).getLocalPort());
	}
	
	public void isolate()
	{
		for(Socket sock : socks.values())
		{
			ostreams.get(sock).close();
			try {
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		socks.clear();
		ostreams.clear();
			
	}

	public void breakConnectionWith(Integer sB) {
		Socket sockB = socks.remove(sB);
		if(sockB != null)
			ostreams.remove(sockB).close();
		else
			System.out.println("failure in breakconnection");
	}
	
	
	//Sender calls this
	public synchronized void startEntropySession() {
		
		if(lastContacts.size() == socks.keySet().size()) {
			lastContacts.clear();
		}
		
		for(Integer i: socks.keySet()) {
			if(!lastContacts.contains(i)){
				lastContacts.add(i);
				break;
			}	
		}

		Socket socket = socks.get(lastContacts.get(lastContacts.size() - 1));
		
		if(socket != null) {
			PrintWriter dout = (PrintWriter)ostreams.get(socket);
			
			String msg = Constants.entropySession_msg + " " + this.sID;
			dout.println(msg);
		}
		else {
			System.out.println("No contact found for new Entropy session --> startEntropySession");
		}
	}
	
	//Receiver does this
	public synchronized void sendResponseToEntropyRequest(Socket otherSock) {
		PrintWriter dout = (PrintWriter)ostreams.get(otherSock);
		String msg;
		
		if(freeForEntropy) {
			msg = Constants.entropyAck_msg + " " + this.sID + " " + this.versionVector.toString();
			
			
		}
		else {
			msg = Constants.entropyReject_msg + " " + sID;
		}
		
		dout.println(msg);
	}
	
	public synchronized void basic_anti_entropy_protocol(Socket otherSock, VersionVector r_vector) {
		Iterator<Write> it = write_log.iterator();
		Write nextWrite;
		
		if(it.hasNext()) {
			//nextWrite = it.next();
			String new_writes = "";

			
			while(it.hasNext()) {
				nextWrite = it.next();
				
				if(r_vector.getStamp(nextWrite.getServerId()) < nextWrite.getAcceptStamp()) {
					new_writes += nextWrite.toString();
					
					if(it.hasNext()) {
						new_writes += ":::";
					}
				}
			}
			
			PrintWriter dout = (PrintWriter)ostreams.get(otherSock);
			dout.println(new_writes);
			
		}
		else {
			System.out.println("No writes can be found in the log for server: " + sID);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
