import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class Client {
	
	private Integer cID;
	private Socket sock;					//client is only connected to one server at a time
	
	public Client(Integer cID, Integer port)
	{
		this.cID = cID;
		try
		{
			sock = new Socket("localhost", port);
			addShutdownHooks(this);
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
			PrintWriter tester = new PrintWriter(sock.getOutputStream(), true);
			tester.println(song);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String toString()
	{
		return "Client ID: " + cID;
	}

	public void disconnect() {
		//TODO
	}
	
	private void addShutdownHooks(final Client client)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			public void run()
			{
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
