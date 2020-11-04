package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class FilterReadsForSDScore {

	public static String description() {
		return "Filter reads for number of reads. Run this program after running -AppendMatrixTogether and -FilterMatrixFile.";
	}
	public static String type() {
		return "RNASEQ";
	}
	public static String parameter_info() {
		return "[inputFile] [numReadsFilter] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			double numReadsFilter = new Double(args[1]);
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
				boolean filter = false;
				for (int i = 1; i < split.length; i++) {
					String SplicingDeficiency = split[i];
					i++;
					double numIntronReads = new Double(split[i]);
					i++;
					double intronLength = new Double(split[i]);
					i++;					
					double numExonReads = new Double(split[i]);
					i++;
					double exonLength = new Double(split[i]);
					if (numIntronReads < numReadsFilter) {
						filter = true;
					}
				}
				if (!filter) {
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
