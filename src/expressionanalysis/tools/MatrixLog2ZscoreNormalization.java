package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import statistics.general.MathTools;

public class MatrixLog2ZscoreNormalization {
	public static String description() {
		return "Normalize the matrix for each gene with log2 then Z-score.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double[] values = new double[split.length - 1];
				for (int i = 1; i < split.length; i++) {
					values[i - 1] = new Double(split[i]);
				}
				double[] normalized = MathTools.log2zscores(values);
				String line = split[0];
				for (int i = 0; i < normalized.length; i++) {
					line += "\t" + normalized[i];
				}
				out.write(line + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
