package expression.matrix.summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

import statistics.general.MathTools;

public class CalculateMatrixSampleSummary {

	public static String description() {
		return "Calculate the basic statistics of the matrix.";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [ExprCutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			double cutoff = new Double(args[1]);
			String outputFile = args[2];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tMin\t1stQuartile\t2ndQuartile\t3rdQuartile\t4thQuartile\tAverage\tLength\n");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split = header.split("\t");
			in.close();
			for (int i = 1; i < header_split.length;  i = i + 100) {
				LinkedList[] list = new LinkedList[100];
				for (int j = 0; j < 100; j++) {
					list[j] = new LinkedList();
				}
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					for (int j = 0; j < 100; j++) {
						if (i + j < header_split.length) {
							if (new Double(split[i + j]) >= cutoff) {
								list[j].add(new Double(split[i + j]));
							}
						}
					}
				}
				in.close();
				for (int j = 0; j < 100; j++) {
					if (list[j].size() > 0) {
						double[] values = MathTools.convertListDouble2Double(list[j]);
						double double_0 = MathTools.quartile(values, 0);
						double double_25 = MathTools.quartile(values, 25);
						double double_50 = MathTools.quartile(values, 50);
						double double_75 = MathTools.quartile(values, 75);
						double double_100 = MathTools.quartile(values, 99);
						double average = MathTools.mean(values);
						out.write(header_split[i + j] + "\t" + double_0 + "\t" + double_25 + "\t" + double_50 + "\t" + double_75 + "\t" + double_100 + "\t" + average + "\t" + list[j].size() + "\n");
					}
				}
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
