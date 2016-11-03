package RNAseqTools.SingleCell.Bootstrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Filter0PSamples {

	public static String type() {
		return "SingleCell";
	}
	public static String description() {
		return "Filter out the 0P samples";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [outputFilteredMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
		    BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream); 
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				if (!(split_header[i].contains("_0P_") || split_header[i].contains("_0PQ_"))) {
					out.write("\t" + split_header[i]);					
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String newLine = split[0];
				for (int i = 1; i < split_header.length; i++) {
					if (!(split_header[i].contains("_0P_") || split_header[i].contains("_0PQ_"))) {
						newLine += "\t" + split[i];
					}
				}
				out.write(newLine + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
