package MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * After R outputed result, we often need to manually remove the quotation in the outputFile
 * The program will assist in removing the quotations
 * @author tshaw
 *
 */
public class RemoveQuotations {

	public static String description() {
		return "Remove quotations within a R outputed file";
	}
	public static String type() {
		return "MISC";
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
			out.write("GeneName\t" + header.replaceAll("\"", "") + "\n");
			while (in.ready()) {
				String str = in.readLine();
				out.write(str.replaceAll("\"", "") + "\n");								
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
