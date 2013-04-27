import java.util.Collections;
import java.util.Comparator;
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
		writes = Collections.synchronizedSortedSet(new TreeSet<Write>(new Comparator<Write>()
		{
			public int compare(Write a, Write b)
			{
				return (int)(a.time - b.time);
			}}));
	}
	
	/**
	 * Log a 3-tuple of {acceptTime, replicaID, userOp}
	 * @param write
	 */
	public void log(Write w) 
	{
		writes.add(w);
	}
	
	public String toString()
	{
		return null;
	}
	
}
