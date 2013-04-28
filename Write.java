
public class Write implements Comparable {
	
	public long time;
	public int cID;
	public String userCmd;
	public boolean committed;
	
	public Write(long time, int cID, boolean committed, String userCmd)
	{
		this.time = time;
		this.cID = cID;
		this.userCmd = userCmd;
		this.committed = committed;
	}

	@Override
	public int compareTo(Object other)
	{
		return (int)(this.time - ((Write)other).time);
	}

}

