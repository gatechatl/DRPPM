package misc.tcga.datacleaning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class TCGACleanGeneSymbols {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		String description = "Clean the sampleName and gene names\n";
		
		return description;
	}
	public static String parameter_info() {
		return "[InputMatrixFile] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = "/Users/4472414/Projects/TCGA/LUAD_EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.tsv";
					//+ "/Users/4472414/Projects/TCGA"; //args[0];
			String outputFile = "/Users/4472414/Projects/TCGA/LUAD_EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.clean.tsv";
					//+ "/Users/4472414/Projects/TCGA"; //args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header.replaceAll("-", ".").replaceAll("\"", "") + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[0].replaceAll("\"", "").split("\\|")[0];
				if (!gene.equals("?") && !gene.equals("NA") && !gene.equals("")) {
					out.write(gene);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
					}
					out.write("\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
