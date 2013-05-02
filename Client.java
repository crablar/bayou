import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {

	private Integer cID;
	private Socket sock;
	private PrintWriter ostream;
	private BufferedReader in;
	private Playlist cachedPlaylist;	//for "read your writes"
	private int connectedServerID;
	private int previousServer;
	private long timeOfLastDisconnect;
	private long lastSafeCSN;
	private boolean serverUpToDate;
	

	public Client(Integer cID, Integer port) {
		this.cID = cID;
		serverUpToDate = true;
		previousServer = Constants.NO_CONNECTION;
		cachedPlaylist = new Playlist();
		try {
			sock = new Socket("localhost", port);
			addShutdownHooks(this);
			ostream = new PrintWriter(sock.getOutputStream(), true);
			ostream.println("I'm a client. My ID is :::" + cID);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			Runnable listener = new Runnable()
			{
				public void run()
				{
					listen();
				}
			};
			Thread listenThread = new Thread(listener);
			//listenThread.start();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void listen() {
		while(true)
		{
			try
			{
				String line;
				while((line = in.readLine()) != null)
				{
					if(line.startsWith("serverConnect"))
						connectedServerID = Integer.parseInt(line.split(" ")[1]);
					else
						handleServerMessage(line);
				}
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

	private void handleServerMessage(String line) {
		System.out.println(this + ": message from " + connectedServerID + ":\n" + line);
		if(line.startsWith("playlistStabilityResponse"))
		{
			if(line.split(" ")[1].equals("stable"))
				serverUpToDate = true;
			else
				serverUpToDate = false;
		}
	}

	public void userRequest(String[] cmdArgs) {
		if(sock == null)
		{
			System.out.println("Client disconnected, unable to process request.");
			return;
		}
		if (cmdArgs[2].equals("add"))
			playlistAdd(cmdArgs[3], cmdArgs[4]);
		else if (cmdArgs[2].equals("delete"))
			playlistDelete(cmdArgs[3]);
		else if (cmdArgs[2].equals("edit"))
			playlistEdit(cmdArgs[3], cmdArgs[4]);
		else if (cmdArgs[2].equals("printPlaylist"))
			playlistPrint();
	}
	
	private void playlistPrint(){
		System.out.println(cachedPlaylist.toStringForCache());
	}
	
	private void playlistEdit(String song, String url) {
		ostream.println("edit " + song + " " + url);
		cachedPlaylist.edit(song, url);
	}

	private void playlistDelete(String song) {
		ostream.println("delete " + song);
		cachedPlaylist.edit(song, "DELETED");
	}

	private void playlistAdd(String song, String url) {
		ostream.println("add " + song + " " + url);
		cachedPlaylist.add(song, url);
	}

	public String toString() {
		return "Client " + cID;
	}

	public void disconnect() {
		serverUpToDate = false;
		previousServer = this.connectedServerID;
		connectedServerID = Constants.NO_CONNECTION;
		timeOfLastDisconnect = System.currentTimeMillis();
		try {
			ostream.println("client disconnecting");
			ostream.close();
			in.close();
			sock.close();
			sock = null;
			ostream = null;
			in = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e)
		{
			System.out.println("Disconnection error in client " + cID);
		}
		
	}
	
	public void reconnect(int port)
	{
		if(sock != null)
		{
			System.out.println("Request failed, client already connected.");
		}
		else
		{
			try {
				sock = new Socket("localhost", port);
				ostream = new PrintWriter(sock.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				ostream.println("I'm a client");
				Runnable updateChecker = new Runnable(){
					public void run() {
						while(!serverUpToDate)
						{
							checkPlaylistStability();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}}};
				Thread updateThread = new Thread(updateChecker);
				//updateThread.start();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void checkPlaylistStability() {
		ostream.println("playlistStabilityRequest " + previousServer + " " + timeOfLastDisconnect + " " + lastSafeCSN);
	}

	private void addShutdownHooks(final Client client) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					if(sock != null)
						sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}));
	}

	public void printConnections() {
		if(sock != null)
			System.out.println("Client " + cID + " connected to server on port " + sock.getPort());
		else
			System.out.println("Client " + cID + " has no connections.");
	}

}
