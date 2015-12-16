import java.util.*;

public class HashStructure {
	HashMap<String, Integer> distances;
	HashMap<String, String> path;
	public HashStructure(HashMap<String, Integer> d, HashMap<String,String> p) {
		distances = d;
		path = p;
	}
}