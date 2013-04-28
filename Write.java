
public class Write implements Comparable {
	
	public long acceptStamp;
	public int cID;
	public String userCmd;
	public boolean committed;
	
	public Write(long acceptStamp, int cID, boolean committed, String userCmd)
	{
		this.acceptStamp = acceptStamp;
		this.cID = cID;
		this.userCmd = userCmd;
		this.committed = committed;
	}

	@Override
	public int compareTo(Object other)
	{
		int diff = (int)(this.acceptStamp - ((Write)other).acceptStamp);
		return diff != 0 ? diff : cID - ((Write)other).cID;
	}

}

