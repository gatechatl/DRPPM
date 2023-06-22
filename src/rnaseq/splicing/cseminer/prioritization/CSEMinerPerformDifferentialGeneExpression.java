package rnaseq.splicing.cseminer.prioritization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * First the initial exon filtering based on expression
 * @author 4472414
 *
 */
public class CSEMinerPerformDifferentialGeneExpression {

	
	public static void main(String[] args) {
		
		
		try {
			
			int count_sig_exons = 0;
			int total_max_weight = 0;
			HashMap unique_exons = new HashMap();
			HashMap overlap_exon_list = new HashMap();
			HashMap overlap_gene_list = new HashMap();
			HashMap OuterMembrane_map = new HashMap();
			HashMap HPA_Predicted_Secreted_map = new HashMap();
			HashMap Final_ECM_map = new HashMap();
			HashMap Final_Surfaceome_map = new HashMap();			
			HashMap surfaceome_exon_annotation = new HashMap();
			HashMap good_gene_symbols = new HashMap();
			HashMap has_protein = new HashMap();
			HashMap manual_override = new HashMap();
			HashMap not_detected_in_all_map = new HashMap();
			HashMap not_detected_in_many_map = new HashMap();

			FileWriter fwriter_67502_exons = new FileWriter("/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/CSEMinerExonList/67502_Exon_list_complete_pipeline.txt");
			BufferedWriter out_67502_exons = new BufferedWriter(fwriter_67502_exons);
			
			
			FileWriter fwriter_de_exons = new FileWriter("/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/differentially_expressed_exon_surfaceome_protein_annotation_03282023.txt");
			BufferedWriter out_de_exons = new BufferedWriter(fwriter_de_exons);
			

			FileWriter fwriter_potentially_spliced_out_exons = new FileWriter("/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/differentially_spliced_out_exon_surfaceome_protein_annotation_03282023.txt");
			BufferedWriter out_potentially_spliced_out_exons = new BufferedWriter(fwriter_potentially_spliced_out_exons);
			
			FileWriter fwriter_sig_exons = new FileWriter("/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/significant_exon_surfaceome_protein_annotation_03282023.txt");
			BufferedWriter out_sig_exons = new BufferedWriter(fwriter_sig_exons);
			
			String additional_manual_override_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/ManualOverrideFromTS.txt";
			FileInputStream fstream_override = new FileInputStream(additional_manual_override_file);
			DataInputStream din_override = new DataInputStream(fstream_override);
			BufferedReader in_override = new BufferedReader(new InputStreamReader(din_override));									
			String header_override = in_override.readLine();
			while (in_override.ready()) {
				String str = in_override.readLine();
				String[] split = str.split("\t");
				manual_override.put(split[0], split[0]);
				System.out.println(split[0]);
			}
			in_override.close();
			
			String overlap_exon_list_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/OverlappingExons.txt";
			FileInputStream fstream_exonlist = new FileInputStream(overlap_exon_list_file);
			DataInputStream din_exonlist = new DataInputStream(fstream_exonlist);
			BufferedReader in_exonlist = new BufferedReader(new InputStreamReader(din_exonlist));									
			String header_exonlist = in_exonlist.readLine();
			while (in_exonlist.ready()) {
				String str = in_exonlist.readLine();
				String[] split = str.split("\t");
				overlap_exon_list.put(split[0], split[0]);
				overlap_gene_list.put(split[1], split[1]);
			}
			in_exonlist.close();
			
			String restricted_gene_list = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/GeneList.txt";			
			FileInputStream fstream_genelist = new FileInputStream(restricted_gene_list);
			DataInputStream din_genelist = new DataInputStream(fstream_genelist);
			BufferedReader in_genelist = new BufferedReader(new InputStreamReader(din_genelist));									
			String header_genelist = in_genelist.readLine();
			while (in_genelist.ready()) {
				String str = in_genelist.readLine();
				String[] split = str.split("\t");
				good_gene_symbols.put(split[0], split[0]);
			}
			in_genelist.close();
			
			//String exon_surfaceome_annotation = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/exon_surfaceome_protein_annotation_04202023.txt";			
			String exon_surfaceome_annotation = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/exon_surfaceome_protein_annotation_04272023.txt";
			FileInputStream fstream = new FileInputStream(exon_surfaceome_annotation);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String rna_tissue_distribution = split[17];
				boolean not_detected_in_all = rna_tissue_distribution.contains("all");
				if (not_detected_in_all) {
					not_detected_in_all_map.put(split[0], split[0]);
				}
				boolean not_detected_in_many = rna_tissue_distribution.contains("many");
				if (not_detected_in_many) {
					not_detected_in_many_map.put(split[0], split[0]);
				}
				boolean removed_questionable = split[27].equalsIgnoreCase("true");				
				boolean removed_membrane_list = split[28].equalsIgnoreCase("true");
				
				boolean hasProtein = !split[8].equalsIgnoreCase("NA") || !split[2].equalsIgnoreCase("NA"); // check both column 2 ensembl peptide and blat derived peptide 
				//boolean hasProtein = true;
				boolean OuterMembrane = split[13].equalsIgnoreCase("true");
				boolean PartialOuterMembrane = split[14].equalsIgnoreCase("true");
				//if (!OuterMembrane) {
				//	OuterMembrane = split[13].equalsIgnoreCase("NA");
				//}
				//if (!PartialOuterMembrane) {
				//	PartialOuterMembrane = split[14].equalsIgnoreCase("NA");
				//}
				if (OuterMembrane || PartialOuterMembrane) {
					OuterMembrane_map.put(split[0], split[0]);
				}
				boolean HPA_Predicted_Secreted = split[39].equalsIgnoreCase("true");
				
				boolean Final_ECM = split[40].equalsIgnoreCase("true");
				boolean Final_Surfaceome = split[41].equalsIgnoreCase("true");
				if (!Final_Surfaceome) {
					Final_Surfaceome = split[13].equalsIgnoreCase("true") || split[14].equalsIgnoreCase("true"); 
				}
				if (manual_override.containsKey(split[0].split("_")[0])) {
					OuterMembrane = true;
				}
				if (hasProtein && HPA_Predicted_Secreted && !removed_membrane_list && !removed_questionable) {
					HPA_Predicted_Secreted_map.put(split[0], split[0]);
				}
				if (hasProtein && Final_ECM && !removed_membrane_list && !removed_questionable) {
					Final_ECM_map.put(split[0], split[0]);
				}
				
				if (hasProtein && Final_Surfaceome && (OuterMembrane || PartialOuterMembrane) && !removed_membrane_list && !removed_questionable) {
				//if (hasProtein && Final_Surfaceome && (OuterMembrane) && !removed_membrane_list && !removed_questionable) {
					Final_Surfaceome_map.put(split[0], split[0]);
				}
				
			}
			in.close();
			
			System.out.println("");
			String inputFile = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/Combined_PCGP_TARGET_Clinical_GTEx_SUMMARIZED_METAANALYSIS_RESULT.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out_67502_exons.write(header + "\tMatrisome\tSurfaceome\tSharedSurfaceMatrisome\tCheckDE\tCheckSignificant\n");
			out_de_exons.write(header + "\tMatrisome\tSurfaceome\tSharedSurfaceMatrisome\n");
			out_sig_exons.write(header + "\tMatrisome\tSurfaceome\tSharedSurfaceMatrisome\n");
			out_potentially_spliced_out_exons.write(header + "\tMatrisome\tSurfaceome\tSharedSurfaceMatrisome\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[0].split("_")[0];
				if (good_gene_symbols.containsKey(gene)) {
				//if (true) {
					if (!unique_exons.containsKey(split[0])) {

						boolean check_surfaceome = false;
						boolean check_matrisome = false;
						boolean check_surfacematrisome = false;
						if (Final_Surfaceome_map.containsKey(split[0])) {
							check_surfaceome = true;
						}
						if (Final_ECM_map.containsKey(split[0])) {
							check_matrisome = true;
						}
						if (check_matrisome && check_surfaceome) {
							check_surfacematrisome = true;
						}
						out_67502_exons.write(str + "\t" + check_matrisome + "\t" + check_surfaceome + "\t" + check_surfacematrisome);
						boolean check_de = false;
						boolean check_significant = false;
						unique_exons.put(split[0], split[0]);
						if (!split[0].contains("\\|")) {
							
							double max_weight = new Double(split[1]);
							double tumor_score = new Double(split[2]);
							double normal_score = new Double(split[3]);
							boolean tumor_greater_than_normal = (split[4].equalsIgnoreCase("true"));
							int high_tumor_count = new Integer(split[5]);
							int high_normal_count = new Integer(split[6]);
							if (max_weight > 1 && tumor_greater_than_normal) {

							}
							/*
							if (split[0].equals("USH2A_chr1_216327591_216327654_-")) {
								System.out.println("max_weight: " + max_weight);
								System.out.println("high_normal_count: " + high_normal_count);
								System.out.println("high_tumor_count: " + high_tumor_count);
								System.out.println("tumor_score: " + tumor_score);
								System.out.println("normal_score: " + normal_score);
								System.out.println("tumor_greater_than_normal: " + tumor_greater_than_normal);
								System.out.println("Final_ECM_map.containsKey(split[0]): " + Final_ECM_map.containsKey(split[0]));
								System.out.println("Final_Surfaceome_map(split[0]): " + Final_Surfaceome_map.containsKey(split[0]));
							}*/
							
							//if (max_weight > 1 && high_normal_count <= 5 && high_tumor_count >= 1 && tumor_greater_than_normal && normal_score < 1) {
							if (max_weight > 1 && high_normal_count <= 5 && high_tumor_count >= 1 && (tumor_score > normal_score) && normal_score <= 1) {
								total_max_weight++;
								check_de = true;								
								out_de_exons.write(str + "\t" + check_matrisome + "\t" + check_surfaceome + "\t" + check_surfacematrisome + "\n");
								if (normal_score < 0.1) {
									out_potentially_spliced_out_exons.write(str + "\t" + check_matrisome + "\t" + check_surfaceome + "\t" + check_surfacematrisome + "\n");	
								}
								//if (HPA_Predicted_Secreted_map.containsKey(split[0]) || Final_ECM_map.containsKey(split[0]) || Final_Surfaceome_map.containsKey(split[0])) {
								if ((Final_ECM_map.containsKey(split[0]) || Final_Surfaceome_map.containsKey(split[0])) && overlap_gene_list.containsKey(split[0].split("_")[0])) {
									count_sig_exons++;
									check_significant = true;
									out_sig_exons.write(str + "\t" + check_matrisome + "\t" + check_surfaceome + "\t" + check_surfacematrisome + "\n");
								}
							}
						}
						out_67502_exons.write("\t" + check_de + "\t" + check_significant + "\n");						
					}
				}
			}			
			in.close();
			System.out.println("Overall total differentially expressed exons: " +  total_max_weight);
			System.out.println("Overall total sig exons: " + count_sig_exons + " on a protein region outside the cell\n");
			out_de_exons.close();
			out_sig_exons.close();
			out_67502_exons.close();
			out_potentially_spliced_out_exons.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
