package rnaseq.splicing.csiminer.exonannotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CSIMinerGenerateExonTranslationPipeline {

	
	public static String type() {
		return "CSI-Miner";
	}
	public static String description() {
		return "Ran the R script for annotating exons and grab putative protein coding region.";
	}
	public static String parameter_info() {
		return "[input exon coord] [R script output file] [outputAnnotationFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0]; //"/Users/4472414/References/genome/Exon_Annotation_All_Surfaceome/all_surfaceome_exons.txt"; //args[0];
			
			String inputFile_protein = args[1]; //"/Users/4472414/References/genome/Exon_Annotation_All_Surfaceome/exon_coordinates_all_protein.txt"; //args[0];
						
			String outputFile = args[2]; //"/Users/4472414/References/genome/Exon_Annotation_All_Surfaceome/exon_coordinates_all_protein_Rprotein.txt"; //args[0]; 
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			HashMap map = new HashMap();
			HashMap map2 = new HashMap();

			HashMap hit = new HashMap();
			FileInputStream fstream2 = new FileInputStream(inputFile_protein);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));			
			while (in2.ready()) {
				String str2 = in2.readLine();
				
				String[] split2 = str2.split(", ");
				String chr = "chr" + split2[0].split(":")[0];
				String start = split2[0].split(":")[1].split("-")[0];
				String end = split2[0].split(":")[1].split("-")[1];
				if (!split2[1].equals("NA")) {
					String sequence = "";
					String ensembl_transcript = "";
					String ensembl_protein = "";
					for (int i = 1; i < split2.length; i++) {
						if (split2[i].contains("ENST")) {
							ensembl_transcript += split2[i] + ",";
						} else if (split2[i].contains("ENSP")) {
							ensembl_protein += split2[i] + ",";
						} else {
							sequence += split2[i] + ",";
						}
					}
					hit.put("chr" + split2[0], sequence + "\t" + ensembl_protein + "\t" + ensembl_transcript);
				}
			}
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\t" + "EnsemblPep" + "\t" + "EnsemblTranscript" + "\t" + "EnsemblProteinID" + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				String chr = split[0].split("_")[1];
				String start = split[0].split("_")[2];
				String end = split[0].split("_")[3];
				String coord = chr + ":" + start + "-" + end;
				if (hit.containsKey(coord)) {
					String seq = (String)hit.get(coord);
					System.out.println(coord + "\t" + seq);
					out.write(str + "\t" + seq + "\n");
				} else {
					out.write(str + "\t" + "NA" + "\t" + "NA" + "\t" + "NA" + "\n");
				}			
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
