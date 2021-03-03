package rnaseq.splicing.csiminer.reference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class CSIMinerCandidate2BED {

	public static String description() {
		return "Convert candidates 2 bed file.";
	}
	public static String type() {
		return "CSIMINER";
	}
	public static String parameter_info() {
		return "[CSIminer_Exons_inputFile] [outputBED]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputBED = args[1];

			FileWriter fwriter_bed = new FileWriter(outputBED);
			BufferedWriter out_bed = new BufferedWriter(fwriter_bed);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				str = str.replaceAll("_PanCan_", "_");
				str = str.replaceAll("_ECM_", "_");
				String[] split = str.split("\t");
				String chr = split[0].split("_")[1];
				String start = split[0].split("_")[2];
				String end = split[0].split("_")[3];
				String tag = split[0];
				String type = "1.0";
				String direction = split[0].split("_")[4];
				out_bed.write(chr + "\t" + start + "\t" + end + "\t" + tag + "\t" + 1 + "\t" + direction + "\n");
				
			}
			in.close();
			out_bed.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
