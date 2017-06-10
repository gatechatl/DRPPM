package MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class GenerateGSEADataset {

	public static String type() {
		return "GSEA";
	}
	public static String description() {
		return "Generate GSEA gmt file";
	}
	public static String parameter_info() {
		return "[inputGeneSetFile] [outputgmtFile]";
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
				String TF = split[0];
				out.write(TF);
				String inputFile2 = split[1];
				FileInputStream fstream2 = new FileInputStream(inputFile2);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));				
				while (in2.ready()) {					
					String str2 = in2.readLine();
					out.write("\t" + str2);
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
