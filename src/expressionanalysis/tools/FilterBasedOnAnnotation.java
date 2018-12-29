package expressionanalysis.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Filter based on a particular column's value
 * @author tshaw
 *
 */
public class FilterBasedOnAnnotation {

	public static String description() {
		return "Filter matrix based on a particular column's value.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [index for column] [cutoff: value greater or equals to] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int index = new Integer(args[1]);
			double cutoff = new Double(args[2]);
			String outputFile = args[3];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header1 = in.readLine();
			out.write(header1);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (new Double(split[index]) >= cutoff) {
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
