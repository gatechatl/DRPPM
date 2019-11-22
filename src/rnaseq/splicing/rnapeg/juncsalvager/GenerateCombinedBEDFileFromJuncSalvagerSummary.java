package rnaseq.splicing.rnapeg.juncsalvager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GenerateCombinedBEDFileFromJuncSalvagerSummary {

	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String description() {
		return "Combine the output into a single BED files.\n";
	}
	public static String parameter_info() {
		return "[file1] [file2] [file...]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap uniq = new HashMap();
			for (String inputFile: args) {
				//System.out.println(inputFile);
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String[] parts = split[0].split(":");
					
					String chr = parts[1];
					String start = parts[2];
					String end = parts[3];
					String name = split[0].replaceAll(":", "_");
					String score = "1";
					String direction = "+";
					if (parts[4].equals("for")) {
						direction = "+";
					} else {
						direction = "-";
					}
					String line = chr + "\t" + start + "\t" + end + "\t" + name + "\t" + score + "\t" + direction;
					if (!uniq.containsKey(line)) {											
						uniq.put(line, line);
						
						System.out.println(line);
					}
				}
				in.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
