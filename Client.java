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

	public Client(Integer cID, Integer port) {
		this.cID = cID;
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
			listenThread.start();
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
					System.out.println(this + ": " + line);
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
		ostream.println("printPlaylist " + cID);
	}
	
	private void playlistEdit(String song, String url) {
		ostream.println("edit " + song + " " + url);
	}

	private void playlistDelete(String song) {
		ostream.println("delete " + song);
	}

	private void playlistAdd(String song, String url) {
		ostream.println("add " + song + " " + url);
	}

	public String toString() {
		return "Client " + cID;
	}

	public void disconnect() {
		savePlaylistToCache();
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
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void savePlaylistToCache() {
		// get uncommitted writes?
		
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
