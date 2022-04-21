package moffitt.projects.danielgrass.dnaseqparsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * A support class for parsing Dr. Grass's DNAseq result
 * @author 4472414
 *
 */
public class MoffittDanGrassParsingDNAseq {
	
	
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "/Users/4472414/Projects/ProteinPaint/SNV_Example/penile_cancer.combined.summary.txt";
			String outputFile = "/Users/4472414/Projects/ProteinPaint/SNV_Example/output_penile_cancer.combined.summary.txt";
			double vaf_cutoff = 0.1;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write("disease\tsampletype\tannovar_type\tannovar_gene\tannovar_class\tannovar_aachange\tannovar_isoform\tpatient\tsample\tgene\tchromosome\tstart\taachange\tclass\tvaf\tannotation_impact\n");
			//disease	sampletype	annovar_type	annovar_gene	annovar_class	annovar_aachange	annovar_isoform	patient	gene	chromosome	start	aachange	class
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] split_header = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String disease = "PenileCancer";
				String sampletype = "diagnosis";
				String annovar_type = split[5];
				String annovar_gene = split[26];
				String annovar_class = split[24].split("&")[0].replaceAll("missense_variant", "missense").replaceAll("3_prime_UTR_variant", "utr_3").replaceAll("5_prime_UTR_variant", "utr_5").replaceAll("synonymous_variant", "silent").replaceAll("disruptive_inframe_deletion", "proteinDel").replaceAll("conservative_inframe_deletion", "proteinDel").replaceAll("disruptive_inframe_insertion", "proteinIns").replaceAll("conservative_inframe_insertion", "proteinIns").replaceAll("splice_region_variant", "splice_region").replaceAll("intron_variant", "intron").replaceAll("frameshift_variant", "frameshift").replaceAll("stop_gained", "nonsense").replaceAll("splice_donor_variant", "splice_region").replaceAll("splice_acceptor_variant", "splice_region").replaceAll("5_prime_UTR_premature_start_codon_gain_variant", "utr_5").replaceAll("protein_protein_contact", "missense").replaceAll("structural_interaction_variant", "missense");
				String annovar_aachange = split[33];
				String annovar_variant = split[32];
				if (annovar_aachange.equals("")) {
					annovar_aachange = annovar_variant;
				}
				
				String annovar_isoform = split[29];
				//String sample = "";
				String gene = annovar_gene;
				String chromosome = split[0];
				String start = split[1];
				String aachange = annovar_aachange;
				if (aachange.equals("")) {
					aachange = annovar_variant;
				}
				String class_str = annovar_class;
				String annotation_impact = split[25];
				//if (!annovar_class.equals("structural_interaction_variant") && split[2].contains("COSM") && (annotation_impact.equals("MOEDERATE") || annotation_impact.equals("HIGH"))) {
				//if (split[2].contains("COSM") && (annotation_impact.equals("MODIFIER") || annotation_impact.equals("MOEDERATE") || annotation_impact.equals("HIGH"))) {	
				//if (annotation_impact.equals("LOW") || annotation_impact.equals("MODIFIER") || annotation_impact.equals("MOEDERATE") || annotation_impact.equals("HIGH")) {
					for (int i = 6; i < 23; i++) {
						if (!split[i].trim().equals("")) {
							double vaf = new Double(split[i].split(" ")[0].replaceAll("VMF=", "").split(",")[0]);
							//if (((vaf < 0.4 && vaf > vaf_cutoff) || (vaf > 0.6 && vaf < (1 - vaf_cutoff)))) {
							if (((vaf < 0.4 && vaf > vaf_cutoff))) {
								String sample = split_header[i];
								if (!annovar_class.equals("silent") && !annovar_class.equals("structural_interaction_variant") && !annovar_class.equals("protein_protein_contact") && !annovar_class.equals("upstream_gene_variant") && !annovar_class.equals("downstream_gene_variant")) {
								out.write(disease + "\t" + sampletype + "\t" + annovar_type + "\t" + annovar_gene + "\t" + annovar_class + "\t" + annovar_aachange + "\t" + annovar_isoform + "\t" + sample + "\t" + sample + "\t" + gene + "\t" + chromosome + "\t" + start + "\t" + aachange + "\t" + class_str + "\t" + vaf + "\t" + annotation_impact + "\n");
								}
							}
						}
						
					}
				//}
				
			}
			in.close();
			out.close();
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
