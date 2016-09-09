package IDConversion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

public class GeneId2Uniprot {
public static void execute(String[] args) {
		
		try {
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap uniprot2geneID(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				map.put(split[0], split[1]);
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap geneID2uniprot(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[1])) {
					LinkedList list = (LinkedList)map.get(split[1]);
					list.add(split[0]);
					map.put(split[1], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[0]);
					map.put(split[1], list);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	
}
