package NetworkTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This contains a series of network tools
 * @author tshaw
 *
 */
public class NetworkTools {
	
	public static LinkedList overlapModuleWithGraph(String inputFile, String sifDB) {
		LinkedList list = new LinkedList();
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * Each protein contains a linkedlist of proteins
	 * @param fileName
	 * @return
	 */
	public static HashMap readHumInterDB(String fileName) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String protein1 = split[1].toUpperCase();
				String protein2 = split[3].toUpperCase();
				if (map.containsKey(protein1)) {
					LinkedList list = (LinkedList)map.get(protein1);
					if (!list.contains(protein2) && !protein1.equals(protein2)) {
						list.add(protein2);
					}
					map.put(protein1, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(protein2);
					map.put(protein1, list);
				}
				if (map.containsKey(protein2)) {
					LinkedList list = (LinkedList)map.get(protein2);
					if (!list.contains(protein1) && !protein2.equals(protein1)) {
						list.add(protein1);
					}
					map.put(protein1, list);					
				} else {
					LinkedList list = new LinkedList();
					list.add(protein1);
					map.put(protein2, list);					
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
