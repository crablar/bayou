import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

/**
 * A thread that is created whenever a Server accepts a new Socket connection
 * 
 * @author jam4879
 *
 */

public class ReplicaThread extends Thread {
	
	private Server server;
	private Socket sock;
	private BufferedReader in;
	private boolean listeningToClient;
	private Integer otherID;
	
	public ReplicaThread(Server serv, Socket sock)
	{
		otherID = -1;
		listeningToClient = false;
		in = null;
		server = serv;
		this.sock = sock;
		System.out.println("starting ReplicaThread for " + server + " at local port " + sock.getLocalPort() + ", listening to " + sock.getPort());
		start();
	}
	
	public void run()
	{
		try
		{
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String line;
			boolean keepGoing;
			while((line = in.readLine()) != null)
			{
				if(line.startsWith("I'm a client"))
				{
					listeningToClient = true;
					server.setClientWriter(sock.getOutputStream());
					server.messageClient("I'm listening to you!");
				} 
				if(listeningToClient)
					keepGoing = handleClientMessage(line);
				else 
					keepGoing = handleReplicaMessage(line);
				if(!keepGoing)
					break;
			}
		}
		catch(SocketException e)
		{
			System.out.println("ReplicaThread for " + server + " closing");
			removeMetaDataFor();
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("finally...ReplicaThread for " + server + " closing");
			removeMetaDataFor();
			
			try {
				in.close();
				sock.close();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void removeMetaDataFor() {
		if(otherID != -1)
			server.closeConnectionTo(otherID);
		else
			server.closeConnectionTo(sock.getPort());
	}
	

	private boolean handleClientMessage(String msg) {
		System.out.println(server + " received: " + msg);
		if(msg.startsWith("client disconnecting"))
		{
			System.out.println("ReplicaThread closing connection with client on " + sock.getPort());
			return false;
		}
		else if(msg.startsWith("printPlaylist"))
			server.printPlaylist();
		else if(msg.startsWith("add"))
		{
			String[] msgArgs = msg.split(" ", 3);
			server.addToPlaylist(msgArgs[1], msgArgs[2]);
		}
		else if(msg.startsWith("edit"))
		{
			String[] msgArgs = msg.split(" ", 3);
			server.editPlaylist(msgArgs[1], msgArgs[2]);
		}
		else if(msg.startsWith("delete"))
		{
			String[] msgArgs = msg.split(" ", 2);
			server.deleteFromPlaylist(msgArgs[1]);
		}
		else if(msg.startsWith("playlistStabilityRequest"))
		{
			String[] msgArgs = msg.split(" ");
			server.sendStabilityResponse(Integer.parseInt(msgArgs[1]), Long.parseLong(msgArgs[2]), Long.parseLong(msgArgs[3]));
		}
			return true;
	}
	
	private boolean handleReplicaMessage(String msg)
	{
		System.out.println(server + " received: " + msg);
			if(msg.startsWith("test connection"))
					server.print(" receiving test message from server on " + sock.getPort());
			else if(msg.startsWith("serverConnect"))
			{
				otherID = Integer.parseInt(msg.split(" ")[1]);
				server.addConnectionToMap(otherID, sock);
			}
			else if(msg.startsWith(Constants.begin_entropySession_msg)) {
				int server_Id = Integer.parseInt(msg.split(" ")[1]);
				
				if(this.otherID == server_Id) {
					server.sendResponseToEntropyRequest(sock);
				}
				else {
					server.print("---------> Beging entropy from wrong server!");
				}
			}
//			else if(msg.startsWith(Constants.entropyReject_msg)) {
//				server.startEntropySession();
//			}
			else if(msg.startsWith(Constants.entropyAck_msg)) {
				server.setFreeForEntropy(false);
				
				Scanner tokens = new Scanner(msg);
				String s_msg = tokens.next();
				assert (Constants.entropyAck_msg).equals(s_msg);
				
				int server_Id = tokens.nextInt();
				long r_csn = tokens.nextLong();
				
				VersionVector r_vector = new VersionVector(tokens.nextLine());
				
				if(this.otherID == server_Id) {
					server.basic_anti_entropy_protocol(sock, r_vector, r_csn);
				}
				else {
					server.print("-------------> entropy ACk to wrong sender!");
				}
				
				tokens.close();
			}
			else if(msg.startsWith(Constants.commit_notification)) {
				Scanner tokens = new Scanner(msg);
				String s_msg = tokens.next();
				assert (Constants.commit_notification).equals(s_msg);
				
				int msg_num = tokens.nextInt();
				long w_accept_stamp = tokens.nextLong();
				int w_server_Id = tokens.nextInt();
				long w_csn = tokens.nextLong();
				
				tokens.close();
				
				server.commitExistingWrite(msg_num, w_accept_stamp, w_server_Id, w_csn);
			}
			else if(msg.startsWith(Constants.server_write_msg)) {
				Scanner tokens = new Scanner(msg);
				String s_msg = tokens.next();
				assert (Constants.server_write_msg).equals(s_msg);
				
				int msg_num = tokens.nextInt();
				Write update = new Write(tokens.nextLine());
				
				tokens.close();
				
				
				server.addServerWrite(msg_num, update);				
			}
			else if(msg.startsWith(Constants.entropy_finish_msg)) {
				int num_msgs = Integer.parseInt(msg.split(" ")[1]);
				
				server.finishEntropyProcessing(num_msgs, this.otherID);
			}
		return true;
	}
	
	
}
