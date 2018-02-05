package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

public class ExpandGeneNames {

	public static void main(String[] args) {
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void execute(String[] args) {
		String fileName = args[0];
		String outputFile = args[1];
		expandRows(fileName, outputFile);
	}
	public static void expandRows(String fileName, String outputFile) {
		try {
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {					
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
			}
			in.close();
			
			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {					
				String str = in.readLine();
				String[] split = str.split("\t");
				String rest = getRest(str);
				if (split[0].contains("__//_")) {
					String[] split2 = split[0].split("__//_");
					if (split2.length >= 2) {
						for (int j = 0; j < split2.length; j++) {
							if (!map.containsKey(split2[j])) {
								out.write(split2[j] + "\t" + rest + "\n");
								map.put(split2[j], split2[j]);
							}
						}
					}
				} else {					
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public static String getRest(String str) {
		String[] split = str.split("\t");
		String rest = "";
		for (int i = 1; i < split.length; i++) {
			if (rest.equals("")) {
				rest += split[i];
			} else {
				rest += "\t" + split[i];
			}
		}
		return rest;
	}
}
