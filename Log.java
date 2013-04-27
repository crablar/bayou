import java.util.TreeSet;

public class Log 
{
	
	private TreeSet<String> writes;
	
	public Log()
	{
		writes = new TreeSet<String>(new Comparator<String>()
		{
			public int compare(String a, String b)
			{
				return a.split(" ")[0].parseInt() - b.split(" ")[0].parseInt();
			}}
		);
	}
	
	public void log(String write) 
	{
		
	}
	
	public String toString()
	{
		return null;
	}
	
}
