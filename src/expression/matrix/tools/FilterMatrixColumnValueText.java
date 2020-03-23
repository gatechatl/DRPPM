package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


public class FilterMatrixColumnValueText {

	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Filter gene expression based on value of column number";
	}
	public static String parameter_info() {
		return "[matrixFile] [colNum] [value to look for. e.g. 'protein_coding'] [set true to keep; set false to remove] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			
			String matrixFile = args[0];
			int colNum = new Integer(args[1]);
			String value = args[2];			
			boolean keep_remove = new Boolean(args[3]);
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
				if (keep_remove) {
					if (split[colNum].equals(value)) {
						out.write(str + "\n");
					}
				} else {
					if (!split[colNum].equals(value)) {
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
