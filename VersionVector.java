import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;


public class VersionVector {

	private HashMap<Integer, Long> vector;	//a map of sID -> latest acceptStamp

	public VersionVector()
	{
		vector = new HashMap<Integer, Long>();
	}

	public VersionVector(String stringifiedVector) {
		vector = new HashMap<Integer, Long>();

		String[] pairs = stringifiedVector.split(":::");
		Scanner parsePairs;

		int key;
		long stamp;

		for(String s: pairs) {
			if(s != null && !(s.trim()).equals("")) {
				parsePairs = new Scanner(s);
				key = parsePairs.nextInt();
				stamp = parsePairs.nextLong();

				vector.put(key, stamp);
				parsePairs.close();
			}
		}
	}
	
	public void addEntry(int sID, long init_stamp) {
		vector.put(sID, init_stamp);
	}

	public void changeLatestAccept(int sID, long acceptStamp) {
		vector.put(sID, acceptStamp);
	}

	public void changeifMax(int sID, long acceptStamp) {
		if(vector.get(sID) < acceptStamp) {
			vector.put(sID, acceptStamp);
		}

	}
	
	public boolean containsEntry(int sID) {
		return vector.get(sID) != null;
	}

	public boolean remove(int sID) {
		return vector.remove(sID) != null;
	}
	
	public long getStamp(int sID) {
		Long res = vector.get(sID);

		return (res == null) ? 0 : res;
	}

	public String toString() {
		String res = "";

		Iterator<Integer> it = (vector.keySet()).iterator();
		int id;

		while(it.hasNext()) {
			id = it.next();

			res += id + " " + vector.get(id);

			if(it.hasNext()) {
				res += ":::";
			}
		}

		return res;
	}

	public String toStringForTesting() {
		String res = "";

		Iterator<Integer> it = (vector.keySet()).iterator();
		int id;

		while(it.hasNext()) {
			id = it.next();

			res += id + " " + vector.get(id);

			if(it.hasNext()) {
				res += "\n";
			}
		}

		return res;		
	}

}
