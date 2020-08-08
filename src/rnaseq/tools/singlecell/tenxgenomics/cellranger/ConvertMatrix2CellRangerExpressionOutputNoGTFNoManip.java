package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ConvertMatrix2CellRangerExpressionOutputNoGTFNoManip {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Convert the matrix into three file similar to what's presented in cell ranger. No GTF is required and not multiplied by 100.";
	}
	public static String parameter_info() {
		return "[inputFileMatrix] [outputGeneFile] [outputSampleFile] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap gene2index = new HashMap();
			HashMap sample2index = new HashMap();
			HashMap index2gene = new HashMap();
			HashMap index2sample = new HashMap();
			HashMap gene_sample2expr = new HashMap();
			int gene_idx = 0;
			int total = 0;
			
			String inputFileMatrix = args[0];
			
			String outputGeneFile = args[1];
			String outputSampleFile = args[2];
			String outputMatrixFile = args[3];
			FileWriter fwriter_gene = new FileWriter(outputGeneFile);
			BufferedWriter out_gene = new BufferedWriter(fwriter_gene);
			FileWriter fwriter_sample = new FileWriter(outputSampleFile);
			BufferedWriter out_sample = new BufferedWriter(fwriter_sample);
			FileWriter fwriter_matrix = new FileWriter(outputMatrixFile);
			BufferedWriter out_matrix = new BufferedWriter(fwriter_matrix);
			FileInputStream fstream = new FileInputStream(inputFileMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				index2sample.put(i, split_header[i]);
				out_sample.write(split_header[i] + "\n");
				sample2index.put(split_header[i], i);
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!(split[0].equals("NA") || split[0].replaceAll("\"", "").equals("NA"))) {
					//if (gtf.geneid2geneName.containsKey(split[0])) {
					gene_idx++;
					gene2index.put(split[0], gene_idx);
					index2gene.put(gene_idx, split[0]);				
					
					out_gene.write(split[0] + "\t" + split[0] + "\n");
					for (int j = 1; j < split.length; j++) {
						double round = new Double(split[j]);
						total += round;
						//out_matrix.write(gene_idx + " " + j + " " + round + "\n");
					}
					//}
				}
			}
			in.close();
			
			out_matrix.write("%%MatrixMarket matrix coordinate integer general\n");
			out_matrix.write("%\n");
			out_matrix.write(gene_idx + " " + (split_header.length - 1) + " " + total + "\n");
			
			gene_idx = 0;
			fstream = new FileInputStream(inputFileMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {

			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");		
				if (!(split[0].equals("NA") || split[0].replaceAll("\"", "").equals("NA"))) {
					//if (gtf.geneid2geneName.containsKey(split[0])) {
					gene_idx++;
					for (int j = 1; j < split.length; j++) {
						int round = new Double(split[j]).intValue();
						if (round > 0) {
							out_matrix.write(gene_idx + " " + j + " " + round + "\n");
						}
					}
					//}
				}
			}
			in.close();
			
			out_gene.close();
			out_sample.close();
			out_matrix.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
