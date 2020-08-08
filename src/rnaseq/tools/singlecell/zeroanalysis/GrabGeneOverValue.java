package rnaseq.tools.singlecell.zeroanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Grab geneName that surpass a particular cutoff
 * @author tshaw
 *
 */
public class GrabGeneOverValue {

	public static String type() {
		return "SCRNASEQ";
	}
	public static String description() {
		return "Grab geneName that surpass a particular cutoff";
	}
	public static String parameter_info() {
		return "[inputFile] [sampleName] [cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String sampleName = args[1];
			double cutoff = new Double(args[2]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					if (split_header[i].equals(sampleName)) {
						double value = new Double(split[i]);
						if (value >= cutoff) {
							out.write(split[0] + "\n");
						}
					}
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
