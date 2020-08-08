package rnaseq.pcpa;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Specialized class for appending meta data to a column
 * @author tshaw
 *
 */
public class PCPAAppendMetaDeta {

	public static String type() {
		return "PCPA";
	}
	public static String description() {
		return "Specialized class for appending meta data to a column";
	}
	public static String parameter_info() {
		return "[inputFile] [metaFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String metaFile = args[1];
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(metaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();		
				String[] split = str.split("\t");
				map.put(split[0].split("_")[0], split[1]);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();		
				String[] split = str.split("\t");
				String file = split[8].replaceAll(".dist", "");
				String type = "";
				if (map.containsKey(file)) {
					type = (String)map.get(file);
				}
				System.out.println(str + "\t" + type);
				
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
