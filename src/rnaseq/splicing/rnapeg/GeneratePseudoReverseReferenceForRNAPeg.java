package rnaseq.splicing.rnapeg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GeneratePseudoReverseReferenceForRNAPeg {

	public static String type() {
		return "RNApeg";
	}
	public static String description() {
		String description = "Here's a mechanism for reversing the coordinate file";

		return description;
	}
	public static String parameter_info() {
		return "[reference_coordinate_file: two column] [inputFile] [chr_index] [start_index] [end_index] [direction_index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap chr_total_length_map = new HashMap();
			String reference_coordinate_file = args[0];
			String inputFile = args[1];						
			String outputFile = args[2];
			
			FileInputStream fstream = new FileInputStream(reference_coordinate_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				chr_total_length_map.put(split[0].replaceAll("chr", ""), new Integer(split[1]));
			}
			in.close();			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			LinkedList list = new LinkedList();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0].split(",")[0].split(":")[0].replaceAll("chr", "");
				int start = new Integer(split[0].split(",")[0].split(":")[1]);
				String direction = split[0].split(",")[0].split(":")[2];
				int end = new Integer(split[0].split(",")[1].split(":")[1]);
				
				if (chr_total_length_map.containsKey(chr)) {
					int total_length = (Integer)chr_total_length_map.get(chr);
					end = total_length - end;					
					start = total_length - start;					
				}
				String junction = "chr" + chr + ":" + end + ":" + direction + "," + "chr" + chr + ":" + start + ":" + direction;
				String new_line = junction;
				for (int i = 1; i < split.length; i++) {
					new_line += "\t" + split[i];
				}
				list.add(new_line);
			}
			in.close();
			
			Iterator itr = list.descendingIterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				out.write(line + "\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
