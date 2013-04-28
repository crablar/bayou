import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {
	
	private Integer cID;
	private Server server;
	
	public Client(Integer cID, Server server)
	{
		this.server = server;
		this.cID = cID;
	}

	public void userRequest(String[] cmdArgs) 
	{
		if(cmdArgs[2].equals("add"))
			playlistAdd(cmdArgs[3], cmdArgs[4]);
		if(cmdArgs[2].equals("delete"))
			playlistDelete(cmdArgs[3]);
		if(cmdArgs[2].equals("edit"))
			playlistEdit(cmdArgs[3], cmdArgs[4]);
		if(cmdArgs[2].equals("printPlaylist"))
			System.out.print(server.getPlaylistString());
	}

	private void playlistEdit(String song, String url) {
		server.editPlaylist(song, url);
	}

	private void playlistDelete(String song) {
		server.deleteFromPlaylist(song);
	}

	private void playlistAdd(String song, String url) 
	{
		server.addToPlaylist(song, url);
	}
	
	public String toString()
	{
		return "Client ID: " + cID;
	}

	public void disconnect() {
		server = null;
	}

}
