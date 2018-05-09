package metagenomics.assembly;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class MergeFastQ {

	public static String type() {
		return "METAGENOME";
	}
	public static String description() {
		return "Merge Fastq for Velvet";
	}
	public static String parameter_info() {
		return "[fastq1] [fastq2] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String inputFile2 = args[1];
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			FileInputStream fstream2 = new FileInputStream(inputFile2);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in.ready() && in2.ready()) {
				String str1 = in.readLine();
				String str2 = in.readLine();
				String str3 = in.readLine();
				String str4 = in.readLine();
				
				String str5 = in2.readLine();
				String str6 = in2.readLine();
				String str7 = in2.readLine();
				String str8 = in2.readLine();
			
				out.write(str1 + "\n");
				out.write(str2 + "\n");
				out.write(str3 + "\n");
				out.write(str4 + "\n");
				out.write(str5 + "\n");
				out.write(str6 + "\n");
				out.write(str7 + "\n");
				out.write(str8 + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
