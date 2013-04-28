import java.util.HashMap;


public class VersionVector {

	private HashMap<Integer, Long> vector;	//a map of sID -> latest acceptStamp
	
	public VersionVector()
	{
		vector = new HashMap<Integer, Long>();
	}

	public void changeLatestAccept(int sID, long acceptStamp) {
		vector.put(sID, acceptStamp);
	}
	
}
