package rnaseq.quantification.kallisto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GenerateScriptForKallisto {

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Generate script for kallisto mapping";
	}
	public static String parameter_info() {
		return "[fastq_lst: 3 column] [kallisto_index] [outputScript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fastq_lst = args[0];
			String index = args[1];
<<<<<<< HEAD

=======
			
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
			String outputScript = args[2];
								
			FileWriter fwriter = new FileWriter(outputScript);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(fastq_lst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write("kallisto quant -i " + index + " " + split[0] + " " + split[1] + " -o " + split[2] + "_kallisto\n");

			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
