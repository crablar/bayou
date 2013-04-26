import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Client {
	
	private Integer cID;
	private Socket sock;					//client is only connected to one server at a time
	
	public Client(Integer cID, Integer port)
	{
		this.cID = cID;
		try
		{
			sock = new Socket("localhost", port);
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

	public void userRequest(String[] cmdArgs) 
	{
		if(cmdArgs[2].equals("add"))
			playlistAdd(cmdArgs[3], cmdArgs[4]);
		if(cmdArgs[2].equals("delete"))
			playlistDelete(cmdArgs[3]);
		if(cmdArgs[2].equals("edit"))
			playlistEdit(cmdArgs[3], cmdArgs[4]);
	}

	private void playlistEdit(String string, String string2) {
		// TODO Auto-generated method stub
		
	}

	private void playlistDelete(String string) {
		// TODO Auto-generated method stub
		
	}

	private void playlistAdd(String song, String url) 
	{
		try 
		{
			sock.getOutputStream().write(("add " + song + " " + url).getBytes());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public String toString()
	{
		return "Client ID: " + cID;
	}

	public void disconnect() {
		//TODO
	}
	
}
