package rnaseq.expression.transcriptionfactornetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Convert expression matrix file to aracne friendly file
 * @author tshaw
 *
 */
public class GenerateAracneInputFile {

	public static String type() {
		return "ARACNE";
	}
	public static String parameter_info() {
		return "[expressionFile] [outputFile]";
	}
	public static String description() { 
		return "Convert expression matrix file to Aracne friendly file.";
	}
	public static void execute(String[] args) {
		
		try {
			
			String expressionFile = args[0];
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(expressionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write("Gene\tAnnotation");
			for (int i = 1; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0] + "\t" + split[0]);
				for (int i = 1; i < split_header.length; i++) {
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
