package GeneNetworkDisplay;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import GeneNetworkDisplay.CreateNetworkDisplay.KEGGentry;

public class GenerateMetadata {

	public static void execute() {
		try {
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap readEntryFromFile(String inputFile) {
		LinkedList conn_list = new LinkedList();
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
				map.put(split[2], split[2]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
}
