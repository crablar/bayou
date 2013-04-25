import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Server {

	private ServerSocket recvsock;
	private HashMap<Socket, PrintWriter> ostreams;
	
	public Server(int port)
	{
		ostreams = new HashMap<Socket, PrintWriter>();
		try
		{
			recvsock = new ServerSocket(port);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		listen();
	}
	
	private void listen()
	{
		while(true)
		{
			Socket sock;
			try
			{
				sock = recvsock.accept();
				PrintWriter dout = new PrintWriter(sock.getOutputStream(), true);
				ostreams.put(sock, dout);
				new
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			
		}
	}
	
}
