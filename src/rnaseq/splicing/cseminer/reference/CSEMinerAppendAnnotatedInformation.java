package rnaseq.splicing.cseminer.reference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CSEMinerAppendAnnotatedInformation {


	public static String description() {
		return "CSI Miner Annotate the Exon Information";
	}
	public static String type() {
		return "CSIMINER";
	}
	public static String parameter_info() {
		return "[inputMatrix] [inuputAnnotation] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrix = args[0];
			String inuputAnnotation = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap comprehensive = new HashMap();
			HashMap map = new HashMap();
			int sample1_len = 0;
			int sample2_len = 0;
			FileInputStream fstream = new FileInputStream(inuputAnnotation);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header1 = in.readLine();
			String[] split_header = header1.split("\t");
			String refined_header = split_header[1];
			String blank = "NA";
			for (int i = 2; i < split_header.length; i++) {
				refined_header += "\t" + split_header[i];
				blank += "\tNA";
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String values = split[1];
				for (int i = 2; i < split.length; i++) {
					values += "\t" + split[i];
				}
				map.put(split[0], values);
				
				String[] split_split = split[0].split("_");
				if (split_split.length > 4) {
					String new_name = split_split[0] + "_ECM_" + split_split[1] + "_" + split_split[2] + "_" + split_split[3] + "_" + split_split[4];
					map.put(new_name, values);
					
					String new_name2 = split_split[0] + "_PanCan_" + split_split[1] + "_" + split_split[2] + "_" + split_split[3] + "_" + split_split[4];
					map.put(new_name2, values);
				}
				
			}
			in.close();
			
			
			fstream = new FileInputStream(inputMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\t" + refined_header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					out.write(str + "\t" + map.get(split[0]) + "\n");
				} else {
					out.write(str + "\t" + blank + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
