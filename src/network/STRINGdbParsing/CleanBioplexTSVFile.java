package network.STRINGdbParsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Clean up bioplex tsv file
 * @author tshaw
 *
 */
public class CleanBioplexTSVFile {

	public static String description() {
		return "Clean up bioplex tsv file";
	}
	public static String type() {
		return "NETWORK";
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
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[5].split("-").length == 2) {
					if (split[5].split("-")[1].equals("Sep")) {
						split[5] = "SEPT" + split[5].split("-")[0];
					}
				}
				if (split[5].split("-").length == 2) {
					if (split[5].split("-")[1].equals("Mar")) {
						split[5] = "MARCH" + split[5].split("-")[0];
					}
				}
				if (split[4].split("-").length == 2) {
					if (split[4].split("-")[1].equals("Sep")) {
						split[4] = "SEPT" + split[5].split("-")[0];
					}
				}
				if (split[4].split("-").length == 2) {
					if (split[4].split("-")[1].equals("Mar")) {
						split[4] = "MARCH" + split[5].split("-")[0];
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
