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

public class GenerateReverseReference {

	public static String type() {
		return "RNApeg";
	}
	public static String description() {
		String description = "Here's a mechanism for reversing the coordinate file";

		return description;
	}
	public static String parameter_info() {
		return "[reference_coordinate_file: two column] [inputFile] [chr_index] [start_index] [end_index] [direction_index] [forward_iterator/descending_iterator: true/false] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap chr_total_length_map = new HashMap();
			String reference_coordinate_file = args[0];
			String inputFile = args[1];
			int chr_index = new Integer(args[2]);
			int start_index = new Integer(args[3]);
			int end_index = new Integer(args[4]);
			int direction_index = new Integer(args[5]);
			boolean forward = new Boolean(args[6]);
			String outputFile = args[7];
			
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
				String chr = split[chr_index].replaceAll("chr", "");
				String new_line = "";
				int start = new Integer(split[start_index]);
				int end = new Integer(split[end_index]);
				String direction = "";
				if (direction_index >= 0) {
					direction = split[direction_index];
				}
				for (int i = 0; i < split.length; i++) {
					String buffer = "\t";
					if (i == 0) {
						buffer = "";
					}
					if (i == start_index) {
						if (chr_total_length_map.containsKey(chr)) {
							int total_length = (Integer)chr_total_length_map.get(chr);
							end = total_length - end;
							new_line += buffer + end;
						}  else {
							new_line += buffer + start;
						}
						
					} else if (i == end_index) {
						if (chr_total_length_map.containsKey(chr)) {
							int total_length = (Integer)chr_total_length_map.get(chr);
							start = total_length - start;
							new_line += buffer + start;
						}  else {
							new_line += buffer + end;
						}
						
					} else if (i == direction_index) {
						if (direction.equals("+")) {
							direction = "-";
						} else if (direction.equals("-")) {
							direction = "+";
						}
						new_line += buffer + direction;
					} else {
						new_line += buffer + split[i];
					}															
				}
				list.add(new_line);
			}
			in.close();
			
			if (forward) {
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					out.write(line + "\n");
				}
			} else if (!forward) {
				Iterator itr = list.descendingIterator();
				while (itr.hasNext()) {
					String line = (String)itr.next();
					out.write(line + "\n");
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
