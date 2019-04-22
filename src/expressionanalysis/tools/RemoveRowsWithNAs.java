package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class RemoveRowsWithNAs {

	public static String description() {
		return "Remove rows with NAs.";
	}
	public static String type() {
		return "EXPRESSION";
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
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				boolean containsNA = false;
				for (int i = 1; i < split.length; i++) {
					if (split[i].equals("NA") || split[i].equals("Inf")) {
						containsNA = true;
					}
				}
				if (!containsNA) {
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
