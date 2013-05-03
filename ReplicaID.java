public class ReplicaID {
	private long accept_stamp;
	private int accept_sID;
	private int my_sID;
	
	public ReplicaID(long accept_stamp, int accept_sID, int my_sID) {
		this.accept_stamp = accept_stamp;
		this.accept_sID = accept_sID;
		this.my_sID = my_sID;
	}
	
	public ReplicaID(String stringifiedID) {
		String[] args = stringifiedID.split("$$$");
		
		this.accept_stamp = Integer.parseInt(args[0]);
		this.accept_sID = Integer.parseInt(args[1]);
		this.my_sID = Integer.parseInt(args[2]);		
	}
	
	public long getTimeStamp() {
		return this.accept_stamp;
	}
	
	public int getAcceptSID() {
		return this.accept_sID;
	}
	
	public int getMySID() {
		return this.my_sID;
	}
	
	public String toString() {
		return this.accept_stamp + "$$$" + this.accept_sID + "$$$" + this.my_sID;
	}

	public String toPrintString() {
		return "<" + this.accept_stamp + " " + this.accept_sID + " " + this.my_sID + ">";
	}
}
