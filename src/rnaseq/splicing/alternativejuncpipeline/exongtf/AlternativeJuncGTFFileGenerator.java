package rnaseq.splicing.alternativejuncpipeline.exongtf;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class AlternativeJuncGTFFileGenerator {

	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Generate the exon GTF file for the htseq quantification.\n";
	}
	public static String parameter_info() {
		return "[inputGTFFile] [outputExonGTFFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputGtfFile = args[0];
			String outputExonGTFFile = args[1];
			

			FileWriter fwriter = new FileWriter(outputExonGTFFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputGtfFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[2].equals("exon")) {
					String meta = split[8];
					//String geneid = GTFFile.grabMeta(split[8], "gene_id");
					String gene_name = GTFFile.grabMeta(split[8], "gene_name");
					String transcript_id = GTFFile.grabMeta(split[8], "transcript_id");
					String new_gene_id = gene_name + "_" + split[0] + "_" + split[3] + "_" + split[4] + "_" + split[6];
					out.write(split[0] + "\tTimExon\tgene\t" + split[3] + "\t" + split[4] + "\t" + split[5] + "\t" + split[6] + "\t" + split[7] + "\tgene_id \"" + new_gene_id + "\"\n");
					out.write(split[0] + "\tTimExon\texon\t" + split[3] + "\t" + split[4] + "\t" + split[5] + "\t" + split[6] + "\t" + split[7] + "\tgene_id \"" + new_gene_id + "\"; transcript_id \"" + transcript_id + "\"; gene_type \"Tim_defined\"; gene_name \"" + gene_name + "\"; transcript_type \"Tim_defined\"; transcript_name \"" + new_gene_id + "\"; exon_number 1; exon_id \"" + new_gene_id + "\"; level 1;\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
