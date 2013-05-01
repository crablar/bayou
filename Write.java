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
	
	public Write(String stringifiedWrite) {
		Scanner tokens = new Scanner(stringifiedWrite);
		
		this.acceptStamp = tokens.nextLong();
		this.csn = tokens.nextLong();
		this.sID = tokens.nextInt();
		String[] lineArr = tokens.nextLine().split(":::");
		this.command = lineArr[0].trim();
		this.song = lineArr[1].trim();
		this.URL = lineArr[2].trim();

		committed = (this.csn != Constants.INFINITY);
		
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
	
	public String getUpdateCmd() {
		return this.command;
	}
	
	public String getSong() {
		return this.song;
	}
	
	public String getURL() {
		return this.URL;
	}
	
	public long getCSN() {
		return this.csn;
	}
	
	public boolean isCommitted() {
		return this.committed;
	}
	
	public void setAcceptStamp(long accept_stamp) {
		this.acceptStamp = accept_stamp;
	}
	
	public void setCSN(long csn) {
		this.csn = csn;
	}
	
	public void setSID(int sID) {
		this.sID = sID;
	}
	
	public void setCommitted(boolean committed) {
		this.committed = committed;
	}
	
	public String toString() {
		return "" + this.acceptStamp + " " + this.csn + " " + this.sID + " " + this.command + ":::" + song + ":::" + URL;
	}

	public String toStringForPrinting()
	{
		return "" + this.acceptStamp + " " + this.csn + " " + this.sID + " \"" + this.command + " " + song + " " + URL + "\"";

	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Write) {
			Write new_w = (Write) obj;
			
			if((new_w.csn == this.csn) && 
				(new_w.sID == this.sID) &&
				(new_w.acceptStamp == this.acceptStamp) &&
				((new_w.command).equals(this.command)) &&
				((new_w.song).equals(this.song)) &&
				((new_w.URL).equals(this.URL)) &&
				(new_w.committed == this.committed)) {
				return true;
			}	
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
}


























