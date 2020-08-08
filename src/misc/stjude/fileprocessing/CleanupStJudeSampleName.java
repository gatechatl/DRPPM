package misc.stjude.fileprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CleanupStJudeSampleName {


	public static String type() {
		return "MISC";
	}
	public static String description() {
		String description = "Clean the sample name from SJAML040557_D1-PANYGP to SJAML040557_D1\n";
		
		return description;
	}
	public static String parameter_info() {
		return "[InputMatrixFile] [outputMatrixFile]";
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
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				String clean1 = split_header[i].split("-")[0].split("\\.")[0];
				String new_sampleName = clean1.split("_")[0] + "_" + clean1.split("_")[1];
				out.write("\t" + new_sampleName);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				out.write(str + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
