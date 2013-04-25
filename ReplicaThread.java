import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


public class ReplicaThread extends Thread {
	
	private Server server;
	private Socket socket;
	
	public ReplicaThread(Server serv, Socket sock)
	{
		server = serv;
		socket = sock;
		start();
	}
	
	private void run()
	{
		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		String inputMsg;
		
		while((inputMsg = in.readLine()) != null)
		{
			//BAYOU.exe
		}
	}
	
}
