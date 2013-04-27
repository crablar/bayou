import java.util.Comparator;
import java.util.TreeSet;

/**
 * A Log of writes, where each write is a 3-tuple of {acceptTime, replicaID, userOp}
 * @author jam4879
 *
 */

public class Log 
{
	
	private TreeSet<String[]> writes;
	
	public Log()
	{
		writes = new TreeSet<String[]>(new Comparator<String[]>()
		{
			public int compare(String[] a, String[] b)
			{
				return Integer.parseInt(a[0]) - Integer.parseInt(b[0]);
			}});
	}
	
	/**
	 * Log a 3-tuple of {acceptTime, replicaID, userOp}
	 * @param write
	 */
	public void log(String[] write) 
	{
		writes.add(write);
	}
	
	public String toString()
	{
		return null;
	}
	
}
