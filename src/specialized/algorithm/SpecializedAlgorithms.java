package specialized.algorithm;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

public class SpecializedAlgorithms {
	
	public static TreeMap<String, Double> sortMapByValue(HashMap<String, Double> map){
		//Comparator<String> comparator = new ValueComparator(map);
		Comparator<String> comparator = new ValueComparator(map);
		//TreeMap is a map sorted by its keys. 
		//The comparator is used to sort the TreeMap by keys. 
		TreeMap<String, Double> result = new TreeMap<String, Double>(comparator);
		result.putAll(map);
		return result;
	}
}
