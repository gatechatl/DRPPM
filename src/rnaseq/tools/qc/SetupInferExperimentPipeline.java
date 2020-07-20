package rnaseq.tools.qc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class SetupInferExperimentPipeline {


	public static String type() {
		return "RNAseq";
	}
	public static String description() {
		return "Infer RNAseq experiment";
	}
	public static String parameter_info() {
		return "[inputFile] [refbed] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String refbed = args[1];
			String outputFile = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
		    BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream); 
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write("infer_experiment.py -r " + refbed + " -i " + split[1] + " > " + split[0] + ".output\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
