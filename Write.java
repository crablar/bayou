import java.util.Scanner;


public class Write implements Comparable {
	
	private long acceptStamp;
	private long csn;
	private int sID;
	private String command;
	private boolean committed;
	private String song;
	private String URL;
	
	public Write(long acceptStamp, long csn, int sID, boolean committed, String command, String song, String URL)
	{
		this.acceptStamp = acceptStamp;
		this.csn = csn;
		this.sID = sID;
		this.command = command;
		this.committed = committed;
		this.song = song;
		this.URL = URL;
	}
	
	public Write(boolean committed, String stringifiedWrite) {
		Scanner tokens = new Scanner(stringifiedWrite);
		
		this.acceptStamp = tokens.nextLong();
		this.csn = tokens.nextLong();
		this.sID = tokens.nextInt();
		String[] lineArr = tokens.nextLine().split(":::");
		this.command = lineArr[0];
		this.song = lineArr[1];
		this.URL = lineArr[2];
		this.committed = committed;
		tokens.close();
	}

	@Override
	public int compareTo(Object other)
	{
		Write otherWrite = (Write)other;
		if(otherWrite.committed && otherWrite.committed)
			return (int) (this.csn - otherWrite.csn);
		if(otherWrite.committed && !this.committed)
			return 1;
		if(!otherWrite.committed && this.committed)
			return -1;
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
		return this.command;
	}
	
	public String toString() {
		return "" + this.acceptStamp + " " + this.csn + " " + this.sID + " " + this.command + ":::" + song + ":::" + URL;
	}

	public String toStringForPrinting()
	{
		return "" + this.acceptStamp + " " + this.csn + " " + this.sID + " \"" + this.command + " " + song + " " + URL + "\"";

	}
}


























