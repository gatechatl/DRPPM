package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Correct the human and mouse gene names containing Sep and Mar 
 * @author tshaw
 *
 */
public class CorrectMarSeptGeneName {
	public static String description() {
		return "Correct the human and mouse gene names containing Sep and Mar";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [organism] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String organism = args[1];
			String outputFile = args[2];
			

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
				split[0] = split[0].replaceAll("\"", "");
				for (int i = 1; i < 20; i++) {
					if (organism.equals("human")) {
						if (split[0].equals(i + "-" + "Sep")) {
							split[0] = "SEPT" + i;
						}
						if (split[0].equals(i + "-" + "Mar")) {
							split[0] = "MARCH" + i;
						}
					}
					if (organism.equals("mouse")) {
						if (split[0].equals(i + "-" + "Sep")) {
							split[0] = "Sept" + i;
						}
						if (split[0].equals(i + "-" + "Mar")) {
							split[0] = "March" + i;
						}
					}
				}
				out.write(split[0]);
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
