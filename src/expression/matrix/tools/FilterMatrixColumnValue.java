package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


public class FilterMatrixColumnValue {

	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Filter gene expression based on value of column number";
	}
	public static String parameter_info() {
		return "[matrixFile] [colNum] [minCutoff (any number)] [greater than or equal? true/false] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			
			String matrixFile = args[0];
			int colNum = new Integer(args[1]);
			double minCutoff = new Double(args[2]);			
			boolean greater = new Boolean(args[3]);
			String outputFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
						
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");
			//int freq = new Double(zeroFrequency * (split_header.length - 1)).intValue();
			//System.out.println(freq);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				boolean passMinCutoff = true;
				if (greater) {
					if (new Double(split[colNum]) >= minCutoff) {
						out.write(str + "\n");
					}
				} else {
					if (new Double(split[colNum]) <= minCutoff) {
						out.write(str + "\n");
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
