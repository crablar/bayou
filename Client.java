import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {

	private Integer cID;
	private Socket sock;
	private PrintWriter ostream;
	private Playlist cachedPlaylist;	//for "read your writes"

	public Client(Integer cID, Integer port) {
		this.cID = cID;
		try {
			sock = new Socket("localhost", port);
			addShutdownHooks(this);
			ostream = new PrintWriter(sock.getOutputStream(), true);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void userRequest(String[] cmdArgs) {
		if (cmdArgs[2].equals("add"))
			playlistAdd(cmdArgs[3], cmdArgs[4]);
		if (cmdArgs[2].equals("delete"))
			playlistDelete(cmdArgs[3]);
		if (cmdArgs[2].equals("edit"))
			playlistEdit(cmdArgs[3], cmdArgs[4]);
		if (cmdArgs[2].equals("printPlaylist"))
			playlistPrint();
	}
	
	private void playlistPrint(){
		ostream.println("print " + cID);
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
		return "Client ID: " + cID;
	}

	public void disconnect() {
		savePlaylistToCache();
		//TODO
	}

	private void savePlaylistToCache() {
		// get uncommitted writes?
		
	}

	private void addShutdownHooks(final Client client) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}));
	}

}
