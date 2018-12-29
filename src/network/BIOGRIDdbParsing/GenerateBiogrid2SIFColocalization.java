package network.BIOGRIDdbParsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GenerateBiogrid2SIFColocalization {

	public static String description() {
		return "Generate SIF from biogrid file.";
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
				String[] splitGeneA = split[2].split(":");
				String[] splitGeneB = split[3].split(":");
				if (split[11].contains("colocalization")) {
					out.write(splitGeneA[splitGeneA.length - 1] + "\tconnections\t" + splitGeneB[splitGeneB.length - 1] + "\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
