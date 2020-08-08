package graph.figures;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Specialized java program to convert a expression matrix to a 
 * @author tshaw
 *
 */
public class ConvertssGSEAMatrix2BoxplotMatrix {

	public static String type() {
		return "GRAPH";
	}
	public static String description() {
		return "Convert ssGSEA matrix conversion to boxplot matrix.";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
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
			out.write("SampleName\tSampleType");
			for (int i = 1; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleType = split[0].split("0")[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
				out.write(split[0] + "\t" + sampleType);
				for (int i = 1; i < split.length; i++) {
					out.write("\t" + split[i]);
				}
				out.write("\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
