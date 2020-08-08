package rnaseq.tools.genelengthanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendGeneLength {
	
	public static String type() {
		return "GENELENGTH";
	}
	public static String description() {
		return "Append Length to file";
	}
	public static String parameter_info() {
		return "[inputFile: geneName first column] [transcript Length File] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String transcriptLengthFile = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap transcriptLength = new HashMap();			
			FileInputStream fstream = new FileInputStream(transcriptLengthFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));	
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				transcriptLength.put(split[0], split[1]);
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			out.write(header + "\t" + "GeneLength\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String length = "NA";
				if (transcriptLength.containsKey(split[0])) {
					length = (String)transcriptLength.get(split[0]);
				}
				out.write(str + "\t" + length + "\n");
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
