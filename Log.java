import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A Log of writes, where each write is a 3-tuple of {acceptTime, replicaID, userOp}
 * @author jam4879
 *
 */

public class Log 
{
	
	private SortedSet<Write> writes;
	
	public Log()
	{
		writes = Collections.synchronizedSortedSet(new TreeSet<Write>());
//		writes = new TreeSet<Write>
	}
	
	/**
	 * Log a 3-tuple of {acceptTime, replicaID, userOp}
	 * @param write
	 */
	public void log(Write w) 
	{
		writes.add(w);
//		System.out.println(writes.add(w));
	}
	
	public synchronized Write removeWrite(long accept_stamp, int sID) {
		Iterator<Write> it = writes.iterator();
		
		Write res;
		
		while(it.hasNext()) {
			res = it.next();
			
			if((res.getAcceptStamp() == accept_stamp) && (res.getServerId() == sID)) {
				it.remove();
				return res;
			}
		}
		
		return null;		
	}
	
	public String toString()
	{
		String result = "";
		for(Write w : writes)
			result += "\n" + w.toStringForPrinting();
		return result;
	}
	
	public Iterator<Write> iterator() {
		return writes.iterator();
	}
	
	public int size() {
		return writes.size();
	}
	
}
