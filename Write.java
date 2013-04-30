import java.util.Scanner;


public class Write implements Comparable {
	
	private long acceptStamp;
	private int sID;
	private String userUpdate;
	private boolean committed;
	
	public Write(long acceptStamp, int sID, boolean committed, String update)
	{
		this.acceptStamp = acceptStamp;
		this.sID = sID;
		this.userUpdate = update;
		this.committed = committed;
	}
	
	public Write(String stringifiedWrite) {
		Scanner tokens = new Scanner(stringifiedWrite);
		
		this.acceptStamp = tokens.nextLong();
		this.sID = tokens.nextInt();
		this.userUpdate = tokens.nextLine();
		
		committed = false;
		
		tokens.close();
	}

	@Override
	public int compareTo(Object other)
	{
		int diff = (int)(this.acceptStamp - ((Write)other).acceptStamp);
		return diff != 0 ? diff : sID - ((Write)other).sID;
	}
	
	public long getAcceptStamp() {
		return acceptStamp;
	}
	
	public int getServerId() {
		return this.sID;
	}
	
	public String getUserUpdate() {
		return this.userUpdate;
	}
	
	public String toString() {
		return "" + this.acceptStamp + " " + this.sID + " " + this.userUpdate;
	}

}


























