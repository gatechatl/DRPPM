package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class FilterMatrixExpression {
	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Filter genes that satisfy a particular cutoff";
	}
	public static String parameter_info() {
		return "[matrixFile] [minCutoff (any number)] [zeroFrequency 0-1] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			
			String matrixFile = args[0];
			double minCutoff = new Double(args[1]);
			double zeroFrequency = new Double(args[2]);
			String outputFile = args[3];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");
			int freq = new Double(zeroFrequency * (split_header.length - 1)).intValue();
			//System.out.println(freq);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				boolean passMinCutoff = true;
				
				int count = 0;
				for (int i = 1; i < split.length; i++) {
					double value = new Double(split[i]);
					
					if (value >= minCutoff) {
						if (value <= 0) {
							//System.out.println("value: " + value);
						}
						count++;
					}
				}				
				if (count >= freq) {
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
