import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Playlist {
	
	private Map<String, String> map;
	
	public Playlist()
	{
		map = Collections.synchronizedMap(new HashMap<String, String>());
	}
	
	public void add(String song, String url)
	{
		map.put(song, url);
	}
	
	public String toString()
	{
		String result = "Playlist:";
		for(String s : map.keySet())
			result +=  "\n" + s + " " + map.get(s);
		return result;
	}

	public void edit(String song, String url) {
		if(!map.containsKey(song))
			System.out.println("Error: playlist does not contain " + song);
			map.put(song, url);
	}

	public void delete(String song) {
		map.remove(song);
	}
	
}
