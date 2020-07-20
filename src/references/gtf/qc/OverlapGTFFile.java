package references.gtf.qc;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Check the GTF File
 * @author tshaw
 *
 */
public class OverlapGTFFile {

	public static void main(String[] args) {
		
		try {
			
			String inputGTFFile1 = "\\\\gsc.stjude.org\\project_space\\penggrp\\PengMMDrugResistance\\common\\cab\\N40K\\References\\UCSC_hg19_Genes_knownGene_gene.gtf";
			//GTFFile gtf = new GTFFile();
			//gtf.initialize(inputGTFFile1);
			
			String inputGTFFile2 = "\\\\gsc.stjude.org\\project_space\\penggrp\\PengMMDrugResistance\\common\\cab\\N40K\\References\\hg19.knownGene.gtf";
			//GTFFile gtf2 = new GTFFile();
			//gtf2.initialize(inputGTFFile2);
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\PengMMDrugResistance\\common\\cab\\N40K\\References\\UCSC_hg19_Genes_knownGene_gene_updated.gtf";
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			/*
			Iterator itr = gtf.geneid2geneName.keySet().iterator();
			while (itr.hasNext()) {
				String geneid = (String)itr.next();
				if (!gtf2.geneid2geneName.containsKey(geneid)) {
					System.out.println(geneid);
				}
			}*/
			
			HashMap gtf1 = new HashMap();
			HashMap gtf2 = new HashMap();
			FileInputStream fstream = new FileInputStream(inputGTFFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 8) {
					String chr = split[0];
					String start = split[3];
					String end = split[4];
					String direct = split[6];
					String meta = split[8];
					
					// grab all the meta data information from the GTF file
					String gene_id = "";
					String transcript_id = ""; 
					String exon_number = ""; 
					String gene_biotype = "";
					
					if (meta.contains("gene_id")) {
						transcript_id = GTFFile.grabMeta(meta, "transcript_id");
						gtf1.put(transcript_id, transcript_id);
						//System.out.println("Original: " + transcript_id);
					}
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputGTFFile2);
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
					
					// grab all the meta data information from the GTF file
					String gene_id = "";
					String transcript_id = ""; 
					String exon_number = ""; 
					String gene_biotype = "";
					
					if (meta.contains("transcript_id")) {
						transcript_id = GTFFile.grabMeta(meta, "transcript_id");
						if (gtf1.containsKey(transcript_id)) {
							out.write(str + "\n");
							//System.out.println(transcript_id);
						}
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
