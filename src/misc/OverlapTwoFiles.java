package misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class OverlapTwoFiles {


	public static String description() {
		return "Overlapping two files, generate flag to mark the overlapping entries. NAs are treated as 'Not_Found'.";
	}
	public static String type() {
		return "MISC";
	}

	public static String parameter_info() {
		return "[queryFile] [geneList] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile1 = args[0];	
			String inputFile2 = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile2);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//File f = new File(str);				
				//map.put(f.getName(), "Not Found");
				if (!split[0].equals("NA")) {
					map.put(split[0], "Not Found");
				}
			}
			in.close();			
				
			HashMap map_reverse = new HashMap();
			fstream = new FileInputStream(inputFile1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\tMatchFlag\n");
			//System.out.println(in.readLine() + "\tMatchFlag");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					//System.out.println(str + "\tFound");
					out.write(str + "\tFound\n");
					map.put(split[0],  "Found");
				} else {
					//System.out.println(str + "\tNot_Found");
					out.write(str + "\tNot_Found\n");
				}
				if (!split[0].equals("NA")) {
					map_reverse.put(split[0], "Not Found");
				}
				/*Iterator itr = map.keySet().iterator();
				boolean found = false;
				while (itr.hasNext()) {
					String key = (String)itr.next();					
					
					
				}*/
				
			}
			in.close();			
			out.close();
			
			System.out.println(map_reverse.size());
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//File f = new File(str);				
				//map.put(f.getName(), "Not Found");
				
				if (!split[0].equals("NA") && map_reverse.containsKey(split[0])) {
					//System.out.println(str + "\tFound");					
					map_reverse.put(split[0],  "Found");
				}
			}
			in.close();
			int overlap = 0;
			
			int uniq_file2 = 0;
			Iterator itr = map.keySet().iterator();			
			while (itr.hasNext()) {			
				String key = (String)itr.next();
				String found = (String)map.get(key);
				if (found.equals("Found")) {
					overlap++;
				} else {
					uniq_file2++;
				}
				//System.out.println(found + "\t" + key);
			}
			
			int uniq_file1 = 0;
			Iterator itr2 = map_reverse.keySet().iterator();			
			while (itr2.hasNext()) {			
				String key = (String)itr2.next();
				String found = (String)map_reverse.get(key);
				if (found.equals("Found")) {
					//overlap++;
				} else {
					uniq_file1++;
				}
				//System.out.println(found + "\t" + key);
			}
			
			System.out.println("After removing NAs");
			System.out.println("'" + inputFile1 + "' has a total of: " + map_reverse.size() + " uniq geneNames.");
			System.out.println("'" + inputFile2 + " has a total of: " + map.size() + " uniq geneNames.");
			System.out.println("Number of uniq genes overlapping the two geneset: " + overlap);
			System.out.println("Number of uniq genes in '" + inputFile1 + "' only: " + uniq_file1);
			System.out.println("Number of uniq genes in '" + inputFile2 + "' only: " + uniq_file2);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
