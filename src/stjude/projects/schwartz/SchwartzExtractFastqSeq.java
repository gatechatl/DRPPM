package stjude.projects.schwartz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class SchwartzExtractFastqSeq {

	public static String description() {
		return "Extract fastq file from sam file.";
	}
	public static String type() {
		return "SCHWARTZ";
	}
	public static String parameter_info() {
		return "[inputSAMFile] [outputFastq]";
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
				String name = ">" + split[0] + "_" + split[20];
				String seq = split[9];
				String qual = split[10];
				out.write(name + "\n");
				out.write(seq + "\n");
				out.write("+\n");
				out.write(qual + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
