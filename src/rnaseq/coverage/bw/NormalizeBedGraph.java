package rnaseq.coverage.bw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Multiply the bedgraph reads with a particular factor
 * @author tshaw
 *
 */
public class NormalizeBedGraph {

	public static String type() {
		return "COVERAGE";
	}
	public static String description() {
		return "Multiply the bedgraph reads with a particular factor. After multiplication should result in a RPM like value.";
	}
	public static String parameter_info() {
		return "[inputBedGraph] [factor] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputBedGraph = args[0];
			double factor = new Double(args[1]);
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputBedGraph);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double value = new Double(split[3]) * factor;
				out.write(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + new Double(Math.round(value * 10)) / 10 + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
