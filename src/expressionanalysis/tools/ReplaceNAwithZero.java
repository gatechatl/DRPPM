package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Multiply a matrix with a particular factor with the option of taking its int value. 
 * @author tshaw
 *
 */
public class ReplaceNAwithZero {
	public static String description() {
		return "Replace NA with zero.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String outputMatrixFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputMatrixFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA")) {
						out.write("\t" + "0.0");
					} else {
						out.write("\t" + split[i]);
					}
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
