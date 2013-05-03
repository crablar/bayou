import java.util.Scanner;

public class ReplicaInfo {
	private long curr_accept_stamp;
	private long start_stamp;
	private ReplicaInfo accepted_rInfo;
	private int myID;
	
	public ReplicaInfo(long start_stamp, ReplicaInfo accepted_rInfo, int myID) {
		this.start_stamp = start_stamp;
		this.accepted_rInfo = accepted_rInfo;
		this.myID = myID;
//		this.curr_accept_stamp = this.start_stamp + 1;
	}
	
	public ReplicaInfo(String stringifiedID) {
		Scanner tokens = new Scanner(stringifiedID);
		
		this.start_stamp = tokens.nextLong();
		this.curr_accept_stamp = tokens.nextLong();
		this.myID = tokens.nextInt();
		
		String remaining = tokens.nextLine();
		if((remaining.trim()).equals("null")) {
			this.accepted_rInfo = null;
		}
		else {
			this.accepted_rInfo = new ReplicaInfo(remaining);	
		}
		
		tokens.close();
	}
	
	public int getMyID() {
		return this.myID;
	}
	
	public long getAcceptStamp() {
		return this.curr_accept_stamp;
	}
	
	public void setAcceptStamp(long new_acceptStamp) {
		this.curr_accept_stamp  = new_acceptStamp;
	}
	
	public long getStartedStamp() {
		return this.start_stamp;
	}
	
	public int getAcceptedSID() {
		return this.accepted_rInfo.getMyID();
	}
	
	public ReplicaInfo getAcceptedInfo() {
		return this.accepted_rInfo;
	}
	
	public String toString() {
		if(accepted_rInfo == null) {
			return this.start_stamp + " " + this.curr_accept_stamp + " " + this.myID + " " + null ;
		}
		else {
			return this.start_stamp + " " + this.curr_accept_stamp + " " + this.myID + " " + this.accepted_rInfo ;
		}
	}

	public String toPrintString() {
		if(accepted_rInfo == null) {
			return "<" + this.start_stamp + " " + this.accepted_rInfo + " " + this.curr_accept_stamp + " " + null + ">";	
		}
		else {
			return "<" + this.start_stamp + " " + this.accepted_rInfo + " " + this.curr_accept_stamp + " " + this.accepted_rInfo.toPrintString() + ">";
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
