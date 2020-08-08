package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class FilterCNVkitcnrfiles {

	
	public static String type() {
		return "LEVENTAKI";
	}
	public static String description() {
		return "Filter the CNVkit based on log2FC";
	}
	public static String parameter_info() {
		return "[CNRFile] [log2FC cutoff] [outputCNRfile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			double minCutoff = new Double(args[1]);
			String outputFile = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (new Double(split[4]) > minCutoff) {
					out.write(str + "\n");
				}
			}
			in.close();						
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
