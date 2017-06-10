package specialized.algorithm;

import java.util.Comparator;
import java.util.HashMap;

//a comparator that compares Strings
public class ValueComparator implements Comparator<String>{
	 
	HashMap<String, Double> map = new HashMap<String, Double>();
 
	public ValueComparator(HashMap<String, Double> map){
		this.map.putAll(map);
	}
 
	public int compare(String s1, String s2) {
		if(map.get(s1) >= map.get(s2)){
			return -1;
		}else{
			return 1;
		}	
	}
}
