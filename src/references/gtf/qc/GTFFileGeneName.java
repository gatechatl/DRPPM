package references.gtf.qc;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GTFFileGeneName {


	public static String type() {
		return "GTFFILE";
	}
	public static String description() {
		return "Append the gene symbol to the gtf file.";
	}
	public static String parameter_info() {
		return "[inputGTFFile] [kg2xref] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();

			//String gtfFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\PengMMDrugResistance\\common\\cab\\N40K\\References\\hg19.knownGene.gtf"; //UCSC_hg19_Genes_knownGene_gene.gtf
			String gtfFile = args[0]; //"\\\\gsc.stjude.org\\project_space\\penggrp\\PengMMDrugResistance\\common\\cab\\N40K\\References\\UCSC_hg19_Genes_knownGene_gene.gtf";
			String kg2xref = args[1]; //"\\\\gsc.stjude.org\\project_space\\penggrp\\PengMMDrugResistance\\common\\cab\\N40K\\References\\kgXref.txt";
			
			String outputFile = args[2]; //"\\\\gsc.stjude.org\\project_space\\penggrp\\PengMMDrugResistance\\common\\cab\\N40K\\References\\UCSC_hg19_Genes_knownGene_gene_symbol.gtf";
			//String outputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\PengMMDrugResistance\\common\\cab\\N40K\\References\\hg19.knownGene_geneName.gtf";

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(kg2xref);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[4]);
			}
			in.close();
			

			fstream = new FileInputStream(gtfFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (split.length > 8) {
					String chr = split[0];
					String start = split[3];
					String end = split[4];
					String direct = split[6];
					String meta = split[8];
					if (meta.contains("transcript_id")) {
						String transcript_id = GTFFile.grabMeta(meta, "transcript_id");
						String replace = transcript_id;
						if (map.containsKey(transcript_id)) {
							//System.out.println(transcript_id + "\t" + map.get(transcript_id));
							replace = (String)map.get(transcript_id);
						} else {
							System.out.println("Missing: " + transcript_id);
						}
						String gene_name = GTFFile.grabMeta(meta, "gene_name");
						if (str.contains("gene_name")) {
							str = str.replaceAll("gene_name \"" + gene_name + "\"", "gene_name \"" + replace + "\"");
						} else {
							if (!replace.equals("")) {
								str += " gene_name \"" + replace + "\";";
							}
						}
						//System.out.println("Original: " + transcript_id);
						out.write(str + "\n");
					}	
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
