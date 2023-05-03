package rnaseq.splicing.cseminer.prioritization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;


/**
 * Perform the entire classification.
 * Criteria for removal	HighNormalExonProp>0.3_Flag
 * Criteria for removal	HighRiskTumorNormalMatch
 * Criteria for removal	CheckForHighIHCNormalFlag
 * Criteria for removal	CheckForHighMediumInIHCBrainTissues
 * To Tier 2	IHC_AboveIntermediateHigh(Exclude Testis Ovary)
 * To Tier 2	BoneMarrowHigh
 * To Tier 2	HighProteomicsHitInGTExForExon
 * To Tier 2	LowRiskTumorNormalPair
 * To Tier 2	ExcludeTestisOvaryRNAExpr
 * @author 4472414
 *
 */
public class CSEminerFigure1ExonClassificationFullPipeline {

	public static void main(String[] args) {
		
		try {
			
			String inputFile_exon_candidate_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/CSEMinerExonList/67502_Exon_list_complete_pipeline.txt";
			
			String outputFirstFilter = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/OutputFilteredCandidates/CSEMiner_Significant_Exons_FullPipeline.txt";
			FileWriter fwriter_FirstFilter = new FileWriter(outputFirstFilter);
			BufferedWriter out_FirstFilter = new BufferedWriter(fwriter_FirstFilter);
									
			String outputTieredCategoryFile = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/OutputFilteredCandidates/CSEMiner_Tiered_Exons_FullPipeline.txt";
			FileWriter fwriter_TieredCategory = new FileWriter(outputTieredCategoryFile);
			BufferedWriter out_TieredCategory = new BufferedWriter(fwriter_TieredCategory);
			
			
			// the GTEx files were generated from CSEMinerTumorNormalSampleTypeMatch.java
			String gene_expression_level_tumornormal_pairing_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/GTEx_Exon_Annotation/Gene_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt";
			String exon_expression_level_tumornormal_pairing_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/GTEx_Exon_Annotation/Exon_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt";
			String inputFile_alternatively_spliced_exon = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/AfterManualReview/CSEminer_alternatively_spliced_exon_candidates_updated_20230325.txt";
			String blacklist_genes_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/AfterManualReview/blacklist_targetlist.txt";
			String okaytokeeporfilter_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/AfterManualReview/OkayToKeepOrFilter.txt";
			String inputFile_IHC_staining_high_only = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/IHC_Protein_Annotation/tim_summary_ihc_normal_tissue_high_only.txt";
			String inputFile_IHC_staining_med_high = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/IHC_Protein_Annotation/tim_summary_ihc_normal_tissue_medium_or_high.txt";			
			String bone_marrow_expression_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/MicroarrayDerivedBoneMarrowExpression/GeneAnnotationOfBoneMarrowExpression.txt";
			String gtex_proteomics_gene_expression_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/ProteomicsAnnotation/gene_abundance_normPSM_median.txt"; // distribution indicate cutoff should be around 0.1323528
			String input_exon_folder = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/csiminer_heatmap/gene_exonexp";
			String gene_transcript_exon_file = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/csiminer_heatmap/subset_gencodev31_exonname_ENSG_ENST.txt";			
			String check_for_existing_target_file = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/existing_car_targets_2021.txt";			
			//String differentially_expressed_exon_list_file = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/CSEMinerExonList/DifferentiallyExpressed_Exons_AfterCutoff_20211118.bed";
			String differentially_expressed_exon_list_file = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/differentially_expressed_exon_surfaceome_protein_annotation_03282023.txt";
			String differentially_spliced_out_exon_list_file = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/cseminer_data/initializing_the_surfaceome_de_gene_list/differentially_spliced_out_exon_surfaceome_protein_annotation_03282023.txt";
			
			// double checking the prioritized target list
			String exon_target_list_2022version_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/CSEMinerExonList/check_candidate_exon_list.txt";			
			HashMap double_check_exon_candidates = double_check_exon_candidates(exon_target_list_2022version_file);
			
			
			// load transcript information
			HashMap transcript2exon = get_transcript2exon(gene_transcript_exon_file);
			HashMap exon2transcript = get_exon2transcript(gene_transcript_exon_file);
			HashMap geneName2transcript = get_geneName2transcript(gene_transcript_exon_file);
			HashMap transcript2geneName = get_transcript2geneName(gene_transcript_exon_file);
			System.out.println("Finished loading transcript information");
			
			// gab differentially expressed exons from our intial screening
			HashMap grab_differentially_expressed_exons = grab_differentially_expressed_exons(differentially_expressed_exon_list_file);			
			HashMap grab_differentially_spliced_out_exons = grab_differentially_expressed_exons(differentially_spliced_out_exon_list_file);			
			
			// inputFile_IHC_staining_med_high
			HashMap ihc_medium_high_brain = grab_ihc_medium_high_brain(inputFile_IHC_staining_med_high); // medium high ihc brain									
			HashMap geneWithAtLeastOneMedHighTissue = grab_TissueHighInAtLeastOneNormal(inputFile_IHC_staining_med_high); // exclude testis and ovary
			HashMap ihcMedHighTissues = grabIHCMedHighTissues(inputFile_IHC_staining_med_high);
			System.out.println("Finished Locading IHC_staining_med_high...\\\\n");
			
			// inputFile_IHC_staining_high_only
			HashMap ihc_high_tissues = grabIHCHighTissues(inputFile_IHC_staining_high_only);
			HashMap geneWithAtLeastOneHighTissue = grab_TissueHighInAtLeastOneNormal(inputFile_IHC_staining_high_only); // exclude testis and ovary
			HashMap geneWithAtLeastOneHighIHCinKeyTissue = grabGeneWithAtLeastOneHighIHCinKeyTissue(inputFile_IHC_staining_high_only); // high ihc brain, lung, pancreas, pituitary, liver
			System.out.println("Finished Locading IHC_staining_high_only...\\\\n");
			
			// gene_expression_level_tumornormal_pairing_file
			HashMap grabGeneProportion_Brain = grabGeneProportion(gene_expression_level_tumornormal_pairing_file, "Brain_HiExonProp");
			System.out.println("Finished Locading Brain_HiExonProp...");
			HashMap grabGeneProportion_Lung = grabGeneProportion(gene_expression_level_tumornormal_pairing_file, "Lung_HiExonProp");
			System.out.println("Finished Locading Lung_HiExonProp...");
			HashMap grabGeneProportion_Liver = grabGeneProportion(gene_expression_level_tumornormal_pairing_file, "Liver_HiExonProp");
			System.out.println("Finished Locading Liver_HiExonProp...");
			HashMap grabGeneProportion_Pituitary = grabGeneProportion(gene_expression_level_tumornormal_pairing_file, "Pituitary_HiExonProp");
			System.out.println("Finished Locading Pituitary_HiExonProp...");
			HashMap grabGeneProportion_Pancreas = grabGeneProportion(gene_expression_level_tumornormal_pairing_file, "Pancreas_HiExonProp");
			System.out.println("Finished Locading Pancreas_HiExonProp...");
			HashMap grabGeneProportion_Nerve = grabGeneProportion(gene_expression_level_tumornormal_pairing_file, "Nerve_HiExonProp");
			System.out.println("Finished Locading Nerve_HiExonProp...");
			HashMap grabGeneProportion_num_normal_tissue_above_median = grabGeneProportion(gene_expression_level_tumornormal_pairing_file, "NumNormalTissueAboveMedianOverall");
			System.out.println("Finished Locading NumNormalTissueAboveMedianOverall...");
									
			HashMap blacklist_gene_list = new HashMap();
			FileInputStream fstream_blacklist_file = new FileInputStream(blacklist_genes_file);
			DataInputStream din_gene_blacklist_file = new DataInputStream(fstream_blacklist_file);
			BufferedReader in_gene_blacklist_file = new BufferedReader(new InputStreamReader(din_gene_blacklist_file));
			while (in_gene_blacklist_file.ready()) {
				String str = in_gene_blacklist_file.readLine();
				String[] split = str.split("\t");
				blacklist_gene_list.put(split[0], split[0]);
			}
			in_gene_blacklist_file.close();
			System.out.println("Finished Locading Blacklist...");
			
			HashMap okay2keeporfilter = okay2keeporfilter(okaytokeeporfilter_file);
			
			
			HashMap surfaceome_gene = new HashMap();
			HashMap matrisome_gene = new HashMap();
			
			HashMap grab_gene_list = grab_gene_list(check_for_existing_target_file); // 
			System.out.println("Load previously reported CAR targets...");
			HashMap all_sig_exon = new HashMap();
			HashMap all_gene_exon = new HashMap();
			HashMap all_gene_exon_sig = new HashMap();
			FileInputStream fstream_exon_candidate_file = new FileInputStream(inputFile_exon_candidate_file);
			DataInputStream din_exon_candidate_file = new DataInputStream(fstream_exon_candidate_file);
			BufferedReader in_exon_candidate_file = new BufferedReader(new InputStreamReader(din_exon_candidate_file));
			String header_exon_candidate = in_exon_candidate_file.readLine();
			out_FirstFilter.write(header_exon_candidate + "\n");
			while (in_exon_candidate_file.ready()) {
				String str = in_exon_candidate_file.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String geneName = split[0].split("_")[0];			
				if (all_gene_exon.containsKey(geneName)) {
					LinkedList list = (LinkedList)all_gene_exon.get(geneName);
					list.add(exon);
					all_gene_exon.put(geneName, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(exon);
					all_gene_exon.put(geneName, list);
				}
				
				if (split[24].equalsIgnoreCase("true")) {
					matrisome_gene.put(geneName, geneName);
				}
				if (split[25].equalsIgnoreCase("true")) {
					surfaceome_gene.put(geneName, geneName);
				}
				// restrict to significant candidates
				// significant candidates were filetered based on Restricted to <= 5 normal samples and at least >= 1 Tumor expression 
				// Tumor score must be higher than normal score.
				// Meta Score > 1
				// filter black list genes
				if (!blacklist_gene_list.containsKey(geneName)) {
					//if (split[7].contains("MATRISOME") || split[7].contains("SURFACEOME")) {
					if (split[24].equalsIgnoreCase("true") || (split[25].equalsIgnoreCase("true"))) {
						//if (split[17].equals("Significant")) {
						if (split[28].equalsIgnoreCase("true")) {
							all_sig_exon.put(exon, exon);
							if (all_gene_exon_sig.containsKey(geneName)) {
								LinkedList list = (LinkedList)all_gene_exon_sig.get(geneName);
								list.add(exon);
								all_gene_exon_sig.put(geneName, list);
							} else {
								LinkedList list = new LinkedList();
								list.add(exon);
								all_gene_exon_sig.put(geneName, list);
							}
							
							out_FirstFilter.write(str + "\n");
						}
					}
				}
				
			}
			in_exon_candidate_file.close();
			out_FirstFilter.close();
			System.out.println("Performed initial exon candidate filtering...");
			
			// check significant exons in a gene based on expression alone
			HashMap calculate_max_percentage_exon_sig_for_each_gene = new HashMap();
			HashMap calculate_min_percentage_exon_sig_for_each_gene = new HashMap();
			Iterator itr = all_gene_exon.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				double keeping_track_of_max_overall_prop_hits = Double.MIN_VALUE;
				double keeping_track_of_min_overall_prop_hits = Double.MAX_VALUE;
				if (geneName2transcript.containsKey(geneName)) {
					LinkedList transcript_list = (LinkedList)geneName2transcript.get(geneName);
					Iterator itr3 = transcript_list.iterator();
					while (itr3.hasNext()) {
						String transcriptID = (String)itr3.next();
						LinkedList exon_list = (LinkedList)transcript2exon.get(transcriptID);
						double count_hits = 0;
						Iterator itr4 = exon_list.iterator();
						while (itr4.hasNext()) {
							String exon_id = (String)itr4.next();
							if (grab_differentially_expressed_exons.containsKey(exon_id)) {
								count_hits++;
							}
						}
						double prop_hits = count_hits / exon_list.size();
						if (keeping_track_of_max_overall_prop_hits < prop_hits) {
							keeping_track_of_max_overall_prop_hits = prop_hits;
						}
						if (keeping_track_of_min_overall_prop_hits > prop_hits && prop_hits > 0) {
							keeping_track_of_min_overall_prop_hits = prop_hits;
						}
					}
				} else {
					keeping_track_of_max_overall_prop_hits = 99;
				}
				/*
				if (all_gene_exon_sig.containsKey(geneName)) {
					LinkedList all_exon = (LinkedList)all_gene_exon.get(geneName);
					LinkedList sig_exon = (LinkedList)all_gene_exon_sig.get(geneName);
					Iterator itr2 = all_exon.iterator();
					while (itr2.hasNext()) {
						String exon = (String)itr2.next();
						if (sig_exon.contains(exon)) {
							sig++;
							if (geneName.equals("CDH4")) {
								System.out.println("Significant: " + exon);
							}
						} else {
							if (geneName.equals("CDH4")) {
								System.out.println("Not Significant: " + exon);
							}
						}
						
					}
					sig = sig / all_exon.size();					
				}
				*/
				calculate_max_percentage_exon_sig_for_each_gene.put(geneName, keeping_track_of_max_overall_prop_hits);
				calculate_min_percentage_exon_sig_for_each_gene.put(geneName, keeping_track_of_min_overall_prop_hits);
			}
			
			HashMap grab_first_tier_candidates = grab_all_exon_list(outputFirstFilter);
			System.out.println("Grab initial candidate list...");
			
			// exon_expression_level_tumornormal_pairing_file
			HashMap grab_high_risk_tumornormal_pairs = grab_tumor_highrisknormaltissue_paired_exon(exon_expression_level_tumornormal_pairing_file, grab_first_tier_candidates);
			System.out.println("Load High Risk Tumor Normal Paired Gene List...");
			
			HashMap grab_low_risk_tumornormal_pairs = grab_tumor_lowrisknormaltissue_paired_exon(exon_expression_level_tumornormal_pairing_file, grab_first_tier_candidates);
			System.out.println("Load High Risk Tumor Normal Paired Gene List...");
			
			// bone marrow expression check
			HashMap grab_bone_marrow = grab_High_BoneMarrow_Expressed_Genes(bone_marrow_expression_file);
			System.out.println("Load Bone Marrow Filter...");
			HashMap grab_overall_protein_abundance_GTEx = grab_overall_protein_abundance_GTEx(gtex_proteomics_gene_expression_file);
			System.out.println("Load Protein Abundance Information...");
			
			// check exon coverage
			HashMap check_for_exon_coverage_bias_in_gtex_samples = check_for_exon_coverage_bias_in_gtex_samples(input_exon_folder);
			System.out.println("check for exon coverage bias in any gene...");
			
			// Grab Manually Annotated Alternatively Spliced Exon
			HashMap grab_alternatively_spliced_exon = grab_alternatively_spliced_exon(inputFile_alternatively_spliced_exon);
			System.out.println("check alternative spliced exon...");
			HashMap grab_alternatively_spliced_gene = grab_alternatively_spliced_gene(inputFile_alternatively_spliced_exon);
			System.out.println("check alternative spliced genes...");
			
			
			int tier1_count = 0;
			HashMap tier1_genes = new HashMap();
			HashMap tier1_AS_genes = new HashMap();
			int tier2_count = 0;
			HashMap tier2_genes = new HashMap();
			HashMap tier2_AS_genes = new HashMap();
			int not_prioritized_count = 0;
			HashMap not_prioritized_genes = new HashMap();
			
			
			HashMap tier1_gene_surfaceome_map = new HashMap();
			HashMap tier2_gene_surfaceome_map = new HashMap();
			HashMap tier1_gene_matrisome_map = new HashMap();
			HashMap tier2_gene_matrisome_map = new HashMap();
			HashMap tier1_gene_shared_surfmat_map = new HashMap();
			HashMap tier2_gene_shared_surfmat_map = new HashMap();
			
			int tier1_exon_surfaceome = 0;			
			int tier2_exon_surfaceome = 0;						
			int tier1_exon_matrisome = 0;			
			int tier2_exon_matrisome = 0;						
			int tier1_exon_shared_surfmat = 0;			
			int tier2_exon_shared_surfmat = 0;
			
			
			HashMap tier1_AS_gene_surfaceome_map = new HashMap();
			HashMap tier2_AS_gene_surfaceome_map = new HashMap();
			HashMap tier1_AS_gene_matrisome_map = new HashMap();
			HashMap tier2_AS_gene_matrisome_map = new HashMap();
			HashMap tier1_AS_gene_shared_surfmat_map = new HashMap();
			HashMap tier2_AS_gene_shared_surfmat_map = new HashMap();
			
			int tier1_AS_exon_surfaceome = 0;			
			int tier2_AS_exon_surfaceome = 0;						
			int tier1_AS_exon_matrisome = 0;			
			int tier2_AS_exon_matrisome = 0;						
			int tier1_AS_exon_shared_surfmat = 0;			
			int tier2_AS_exon_shared_surfmat = 0;
			
			HashMap all_exon_candidates = new HashMap();
			HashMap all_exon_candidates_tier = new HashMap();
			
			HashMap candidates_not_removed_after_manual_check = new HashMap();

			HashMap candidates_not_removed_after_manual_check_status = new HashMap();
			
			double rna_cutoff = 0.3;
			double protein_cutoff = 0.1323528;
			FileInputStream fstream_filtered_candidate_file = new FileInputStream(outputFirstFilter);
			DataInputStream din_filtered_candidate_file = new DataInputStream(fstream_filtered_candidate_file);
			BufferedReader in_filtered_candidate_file = new BufferedReader(new InputStreamReader(din_filtered_candidate_file));
			String header_str = in_filtered_candidate_file.readLine();
			out_TieredCategory.write(header_str + "\tGene\tAS_Flag\tSigExonMaxProp\tSigExonMinProp\tSigExonPropFlag");
			out_TieredCategory.write("\tHighNormalExonProp>0.3_Flag\tHighRiskTumorNormalMatch\tCheckForHighIHCNormalFlag\tCheckForHighMediumInBrain\tCheckForExonCoverageBias");
			out_TieredCategory.write("\tIHC_AboveIntermediate(Exclude Testis Ovary)\tBoneMarrowHigh\tHighProteomicsHitInGTExForGene\tLowRiskTumorNormalPair\tExcludeTestisOvaryRNAExpr");
			// IHC_AboveIntermediateHigh(Exclude Testis Ovary)	BoneMarrowHigh	HighProteomicsHitInGTExForExon	LowRiskTumorNormalPair	ExcludeTestisOvaryRNAExpr
			out_TieredCategory.write("\tFinalPrioritization\tSurfaceome\tMatrisome\tSharedSurfaceMatrisome");
			out_TieredCategory.write("\n");
			while (in_filtered_candidate_file.ready()) {
				String str = in_filtered_candidate_file.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				all_exon_candidates.put(exon, exon);
				String geneName = split[0].split("_")[0];
				String line_written = "";
				double max_prop_sig_exon_in_gene = (Double)calculate_max_percentage_exon_sig_for_each_gene.get(geneName);
				double min_prop_sig_exon_in_gene = (Double)calculate_min_percentage_exon_sig_for_each_gene.get(geneName);
				boolean prop_sig_exon_in_gene_flag = false;
				String exon_prioritization = "Tier1";
				if (!(grab_alternatively_spliced_gene.containsKey(geneName) && !grab_alternatively_spliced_exon.containsKey(exon))) {
					String spliced_exon_status = "Gene";
					if (grab_alternatively_spliced_exon.containsKey(exon)) {
						spliced_exon_status = "AS";
					}
					// check if at least 50% gene coverage
					if (min_prop_sig_exon_in_gene <= 0.2) {
						prop_sig_exon_in_gene_flag = true;
					}
					line_written += str + "\t" + geneName + "\t" + spliced_exon_status + "\t" + max_prop_sig_exon_in_gene + "\t" + min_prop_sig_exon_in_gene + "\t" + prop_sig_exon_in_gene_flag;
					out_TieredCategory.write(str + "\t" + geneName + "\t" + spliced_exon_status + "\t" + max_prop_sig_exon_in_gene + "\t" + min_prop_sig_exon_in_gene + "\t" + prop_sig_exon_in_gene_flag);
					boolean highNormalFlag = false;
					
					if (grabGeneProportion_Brain.containsKey(geneName)) {
						double Brain_HiExonProp = (Double)grabGeneProportion_Brain.get(geneName);
						double Lung_HiExonProp = (Double)grabGeneProportion_Lung.get(geneName);
						double Liver_HiExonProp = (Double)grabGeneProportion_Liver.get(geneName);
						double Pituitary_HiExonProp = (Double)grabGeneProportion_Pituitary.get(geneName);
						if (Brain_HiExonProp >= rna_cutoff || Lung_HiExonProp >= rna_cutoff || Liver_HiExonProp >= rna_cutoff || Pituitary_HiExonProp >= rna_cutoff) {
							highNormalFlag = true;
						}
					}
					line_written += "\t" + highNormalFlag;
					out_TieredCategory.write("\t" + highNormalFlag);
					
					boolean highRiskTumorNormalMatch = false;
					if (grab_high_risk_tumornormal_pairs.containsKey(geneName)) {
						highRiskTumorNormalMatch = true;
					}
					line_written += "\t" + highRiskTumorNormalMatch;
					out_TieredCategory.write("\t" + highRiskTumorNormalMatch);
					
					boolean checkForHighIHCNormalFlag = false;
					if (geneWithAtLeastOneHighIHCinKeyTissue.containsKey(geneName)) {
						checkForHighIHCNormalFlag = true;
					}
					line_written += "\t" + checkForHighIHCNormalFlag;
					out_TieredCategory.write("\t" + checkForHighIHCNormalFlag);
					
					
					boolean checkForHighMediumInBrain = false;
					if (ihc_medium_high_brain.containsKey(geneName)) {
						checkForHighMediumInBrain = true;
					}
					line_written += "\t" + checkForHighMediumInBrain;
					out_TieredCategory.write("\t" + checkForHighMediumInBrain);
					
					/*
					boolean ihc_AboveHighFlag = false;
					if (geneWithAtLeastOneMedHighTissue.containsKey(geneName)) {
						ihc_AboveHighFlag = true;
					}
					out_TieredCategory.write("\t" + ihc_AboveHighFlag);
					*/
					
					// check if exon coverage is okay
					boolean check_for_exon_coverage_bias_in_gtex_samples_flag = false;
					if (check_for_exon_coverage_bias_in_gtex_samples.containsKey(geneName) && !grab_alternatively_spliced_gene.containsKey(geneName)) {
						check_for_exon_coverage_bias_in_gtex_samples_flag = true;
					}
					line_written += "\t" + check_for_exon_coverage_bias_in_gtex_samples_flag;
					out_TieredCategory.write("\t" + check_for_exon_coverage_bias_in_gtex_samples_flag);
					// Finished Hard Filtering Check
					// Next Filter for Tier 2 Candidates
					
					boolean ihc_AboveIntermediateHighFlag = false;
					if (geneWithAtLeastOneMedHighTissue.containsKey(geneName)) {
						ihc_AboveIntermediateHighFlag = true;
					}
					line_written += "\t" + ihc_AboveIntermediateHighFlag;
					out_TieredCategory.write("\t" + ihc_AboveIntermediateHighFlag);
					
					
					boolean boneMarrowHighFlag = false;
					if (grab_bone_marrow.containsKey(geneName)) {
						boneMarrowHighFlag = true;
					}
					line_written += "\t" + boneMarrowHighFlag;
					out_TieredCategory.write("\t" + boneMarrowHighFlag);
					
					boolean proteomicsHighGTExFlag = false;
					if (grab_overall_protein_abundance_GTEx.containsKey(geneName)) {
						double protein_gtex_norm_psm = (Double)grab_overall_protein_abundance_GTEx.get(geneName);
						if (protein_gtex_norm_psm > protein_cutoff) {
							proteomicsHighGTExFlag = true;
						}
					}
					line_written += "\t" + proteomicsHighGTExFlag;
					out_TieredCategory.write("\t" + proteomicsHighGTExFlag);
					
					
					boolean lowRiskTumorNormalPairFlag = false; 
					if (grab_low_risk_tumornormal_pairs.containsKey(geneName)) {
						lowRiskTumorNormalPairFlag = true;
					}
					line_written += "\t" + lowRiskTumorNormalPairFlag;
					out_TieredCategory.write("\t" + lowRiskTumorNormalPairFlag);
										
					boolean numNormalTissueAboveMedian = false; 
					if (grabGeneProportion_num_normal_tissue_above_median.containsKey(geneName)) {
						double num_normal_tissue = (Double)grabGeneProportion_num_normal_tissue_above_median.get(geneName);
						if (num_normal_tissue > 0) {
							numNormalTissueAboveMedian = true;
						}
					}
					line_written += "\t" + numNormalTissueAboveMedian;
					out_TieredCategory.write("\t" + numNormalTissueAboveMedian);
					
					boolean grab_car_target_flag = false;
					if (grab_gene_list.containsKey(geneName)) {
						grab_car_target_flag = true;
					}
					
					boolean surfaceome_flag = false;
					if (surfaceome_gene.containsKey(geneName)) {
						surfaceome_flag = true;
					}
					boolean matrisome_flag = false;
					if (matrisome_gene.containsKey(geneName)) {
						matrisome_flag = true;
					}
					
					boolean okay2keep = false;
					boolean okay2filter = false;
					if (okay2keeporfilter.containsKey(geneName)) {
						if ((Boolean)okay2keeporfilter.get(geneName)) {
							okay2keep = true;
						} else {
							okay2filter = true;
						}						
					}
					//if ((highNormalFlag || highRiskTumorNormalMatch || checkForHighIHCNormalFlag || checkForHighMediumInBrain) && !spliced_exon_status.equals("AS")) {
					//if (((check_for_exon_coverage_bias_in_gtex_samples_flag && !grab_car_target_flag) || highRiskTumorNormalMatch || checkForHighIHCNormalFlag || checkForHighMediumInBrain) && !spliced_exon_status.equals("AS")) {
					if ((((check_for_exon_coverage_bias_in_gtex_samples_flag && !grab_car_target_flag) || highRiskTumorNormalMatch) && !spliced_exon_status.equals("AS") && !okay2keep) || okay2filter) {
					//if ((((check_for_exon_coverage_bias_in_gtex_samples_flag && !grab_car_target_flag) || highRiskTumorNormalMatch) && !spliced_exon_status.equals("AS")) && !okay2keep) {						
						all_exon_candidates_tier.put(exon, "Not Prioritized");
						exon_prioritization = "Not Prioritized";
						not_prioritized_count++;
						not_prioritized_genes.put(geneName, geneName);
					//} else if (((checkForHighIHCNormalFlag || checkForHighMediumInBrain || ihc_AboveIntermediateHighFlag || boneMarrowHighFlag || proteomicsHighGTExFlag || lowRiskTumorNormalPairFlag || numNormalTissueAboveMedian) || highNormalFlag) && !spliced_exon_status.equals("AS")) {
						// checkForHighIHCNormalFlag || checkForHighMediumInBrain || proteomicsHighGTExFlag || ihc_AboveIntermediateHighFlag || 
					//} else if (((boneMarrowHighFlag || lowRiskTumorNormalPairFlag || numNormalTissueAboveMedian) || highNormalFlag) && !spliced_exon_status.equals("AS")) {
					} else if (((boneMarrowHighFlag || lowRiskTumorNormalPairFlag || numNormalTissueAboveMedian) || highNormalFlag) && !spliced_exon_status.equals("AS")) {
						if ((((check_for_exon_coverage_bias_in_gtex_samples_flag && !grab_car_target_flag) || highRiskTumorNormalMatch) && !spliced_exon_status.equals("AS"))) {
							
							//System.out.println("Not Removed after manual check: " + exon);
						}
						all_exon_candidates_tier.put(exon, "Tier2");
						exon_prioritization = "Tier 2";
						tier2_count++;
						tier2_genes.put(geneName, geneName);
						
						
						// Tier 2 counting
						if (spliced_exon_status.equals("AS")) {
							
							tier2_AS_genes.put(geneName, geneName);
							
							// shared tier2 surfaceome matrisome
							if (matrisome_flag && surfaceome_flag) {
								tier2_AS_gene_shared_surfmat_map.put(geneName, geneName);
								tier2_AS_exon_shared_surfmat++;
							}
							
							// tier2 matrisome
							if (matrisome_flag) {
								tier2_AS_gene_matrisome_map.put(geneName, geneName);
								tier2_AS_exon_matrisome++;
							}
							
							// tier2 surfaceome
							if (surfaceome_flag) {
								tier2_AS_gene_surfaceome_map.put(geneName, geneName);
								tier2_AS_exon_surfaceome++;
							}
						}
						
						//regardless of AS status
						// shared tier2 surfaceome matrisome
						if (matrisome_flag && surfaceome_flag) {
							tier2_gene_shared_surfmat_map.put(geneName, geneName);
							tier2_exon_shared_surfmat++;
						}
						
						// tier2 matrisome
						if (matrisome_flag) {
							tier2_gene_matrisome_map.put(geneName, geneName);
							tier2_exon_matrisome++;
						}
						
						// tier2 surfaceome
						if (surfaceome_flag) {
							tier2_gene_surfaceome_map.put(geneName, geneName);
							tier2_exon_surfaceome++;
						}
					
					} else {
						tier1_count++;
						tier1_genes.put(geneName, geneName);
						all_exon_candidates_tier.put(exon, "Tier1");
						
						// Tier 1 counting
						if (spliced_exon_status.equals("AS")) {
							
							tier1_AS_genes.put(geneName, geneName);
							// shared tier1 surfaceome matrisome
							if (matrisome_flag && surfaceome_flag) {
								tier1_AS_gene_shared_surfmat_map.put(geneName, geneName);
								tier1_AS_exon_shared_surfmat++;
							}
							
							// tier1 matrisome
							if (matrisome_flag) {
								tier1_AS_gene_matrisome_map.put(geneName, geneName);
								tier1_AS_exon_matrisome++;
							}
							
							// tier1 surfaceome
							if (surfaceome_flag) {
								tier1_AS_gene_surfaceome_map.put(geneName, geneName);
								tier1_AS_exon_surfaceome++;
							}
						} 
					
						// regardless of AS status
						// shared tier1 surfaceome matrisome
						if (matrisome_flag && surfaceome_flag) {
							tier1_gene_shared_surfmat_map.put(geneName, geneName);
							tier1_exon_shared_surfmat++;
						}
						
						// tier1 matrisome
						if (matrisome_flag) {
							tier1_gene_matrisome_map.put(geneName, geneName);
							tier1_exon_matrisome++;
						}
						
						// tier1 surfaceome
						if (surfaceome_flag) {
							tier1_gene_surfaceome_map.put(geneName, geneName);
							tier1_exon_surfaceome++;
						}
						
					}
					
					
					// final exon prioritization
					line_written += "\t" + exon_prioritization;
					out_TieredCategory.write("\t" + exon_prioritization);
					line_written += "\t" + surfaceome_flag + "\t" + matrisome_flag + "\t" + (surfaceome_flag && matrisome_flag);
					
					out_TieredCategory.write("\t" + surfaceome_flag + "\t" + matrisome_flag + "\t" + (surfaceome_flag && matrisome_flag));
					out_TieredCategory.write("\n");
					// if ((((check_for_exon_coverage_bias_in_gtex_samples_flag && !grab_car_target_flag) || highRiskTumorNormalMatch) && !spliced_exon_status.equals("AS")) && !okay2keep) {
					
					if ((((check_for_exon_coverage_bias_in_gtex_samples_flag && !grab_car_target_flag) || highRiskTumorNormalMatch) && !spliced_exon_status.equals("AS")) && okay2keep) {
						candidates_not_removed_after_manual_check_status.put(exon.split("_")[0], check_for_exon_coverage_bias_in_gtex_samples_flag + "\t" + grab_car_target_flag + "\t" + highRiskTumorNormalMatch + "\t" + spliced_exon_status + "\n");
						candidates_not_removed_after_manual_check.put(exon.split("_")[0], line_written);
					}
				}
				
				
			}
			
			
			HashMap found = new HashMap();
			in_filtered_candidate_file.close();
			out_TieredCategory.close();
			HashMap grab_candidate_summary = grab_candidate_summary();
			Iterator itr3 = tier1_genes.keySet().iterator();
			while (itr3.hasNext()) {
				String geneName = (String)itr3.next();
				if (grab_candidate_summary.containsKey(geneName)) {
					String type = (String)grab_candidate_summary.get(geneName);
					System.out.println("Tier1 Hit on " + geneName + "\t" + type);
					
					found.put(geneName, geneName);
				}
			}
			itr3 = tier2_genes.keySet().iterator();
			while (itr3.hasNext()) {
				String geneName = (String)itr3.next();
				if (grab_candidate_summary.containsKey(geneName)) {
					String type = (String)grab_candidate_summary.get(geneName);
					System.out.println("Tier2 Hit on " + geneName + "\t" + type);
					if (geneName.contains("COL20A1")) {
						System.out.println(" ###### Found COL20A1");
					}
					found.put(geneName, geneName);
				}
			}
			itr3 = not_prioritized_genes.keySet().iterator();
			while (itr3.hasNext()) {
				String geneName = (String)itr3.next();
				if (grab_candidate_summary.containsKey(geneName)) {
					String type = (String)grab_candidate_summary.get(geneName);
					System.out.println("Not Prioritized genes include: " + geneName + "\t" + type);
					found.put(geneName, geneName);
				}
			}
			
			System.out.println("A total of " + (tier1_count + tier2_count + not_prioritized_count) + " exons in " + (tier1_genes.size() + tier2_genes.size() + not_prioritized_genes.size()) + " genes were analyzed.");
			System.out.println("");
			
			System.out.println("Combine Tier1 and Tier2 exons: " + (tier1_count + tier2_count) + " in genes: " + (tier1_genes.size() + tier2_genes.size()));
									
			System.out.println("");
			System.out.println("Total Tier1 genes: " + tier1_genes.size() + " in exons " + tier1_count);
			System.out.println("Tier1 Total Gene level: " + (tier1_genes.size() - tier1_AS_genes.size()));			
			System.out.println("Tier1 Total AS level: " + tier1_AS_genes.size());
						
			System.out.println("");
			System.out.println("Tier1 Shared MatrisomeSurfaceome Gene: " + tier1_gene_shared_surfmat_map.size());			
			System.out.println("Tier1 Surfaceome Gene: " + (tier1_gene_surfaceome_map.size() - tier1_gene_shared_surfmat_map.size()));			
			System.out.println("Tier1 Matrisome Gene: " + (tier1_gene_matrisome_map.size() - tier1_gene_shared_surfmat_map.size()));
			System.out.println("");
			System.out.println("Tier1 Surfaceome exon: " + tier1_exon_surfaceome);
			System.out.println("Tier1 Shared MatrisomeSurfaceome exon: " + tier1_exon_shared_surfmat);
			System.out.println("Tier1 Matrisome exon: " + tier1_exon_matrisome);
			
			System.out.println("");
			System.out.println("Total Tier2 genes: " + tier2_genes.size() + " in exons: " + tier2_count);
			System.out.println("Tier2 Total Gene level: " + (tier2_genes.size() - tier2_AS_genes.size()));
			System.out.println("Tier2 Total AS level: " + tier2_AS_genes.size());
			
			System.out.println("");
			
				
			System.out.println("Tier2 Shared MatrisomeSurfaceome Gene: " + tier2_gene_shared_surfmat_map.size());			
			System.out.println("Tier2 Surfaceome Gene: " + (tier2_gene_surfaceome_map.size() - tier2_gene_shared_surfmat_map.size()));			
			System.out.println("Tier2 Matrisome Gene: " + (tier2_gene_matrisome_map.size() - tier2_gene_shared_surfmat_map.size()));
			System.out.println("");
			System.out.println("Tier2 Surfaceome exon: " + tier2_exon_surfaceome);
			System.out.println("Tier2 Shared MatrisomeSurfaceome exon: " + tier2_exon_shared_surfmat);
			System.out.println("Tier2 Matrisome exon: " + tier2_exon_matrisome);
			System.out.println("");
			System.out.println("Total Not Prioritized Exons: " + not_prioritized_count + "; genes: " + not_prioritized_genes.size());
			
			itr3 = grab_candidate_summary.keySet().iterator();
			while (itr3.hasNext()) {
				String geneName = (String)itr3.next();
				String type = (String)grab_candidate_summary.get(geneName);
				if (!found.containsKey(geneName)) {
					System.out.println("BlackListed: " + geneName + "\t" + type);
				}
			}
			
			int double_check_count = 0;
			int blacklist_check_count = 0;
			int exon_tier1_count = 0;
			int exon_tier2_count = 0;
			int exon_notprioiritized_count = 0;
			int exon_na_count = 0;
			HashMap not_found = new HashMap();
			// final check on the exon compared to previous lists.
			Iterator itr4 = double_check_exon_candidates.keySet().iterator();
			while (itr4.hasNext()) {
				String exon = (String)itr4.next();		
				boolean exon_found = false;
				if (all_exon_candidates_tier.containsKey(exon)) {
					String category = (String)all_exon_candidates_tier.get(exon);
					if (category.equals("Tier1")) {
						exon_tier1_count++;
					}
					if (category.equals("Tier2")) {
						exon_tier2_count++;
					}
					if (category.equals("Not Prioritized")) {
						exon_notprioiritized_count++;
					}
					exon_found = true;
				} else {
					exon_na_count++;
					
				}
				boolean found_flag = false;
				if (all_exon_candidates.containsKey(exon)) {
					double_check_count++;
					found_flag = true;
					if (!exon_found) {
						System.out.println("Contradiction: " + exon);
					}
				}
				String gene = exon.split("_")[0];
				//System.out.println(gene);
				if (blacklist_gene_list.containsKey(gene)) {
					blacklist_check_count++;
					found_flag = true;
				}
				
				if (!found_flag) {
					System.out.println("Missing: " + exon);
				}
			}						
			
			System.out.println(double_check_count + "\t" + (double_check_count + blacklist_check_count) + "\t" + double_check_exon_candidates.size());
			System.out.println("Tier1 Exons: " + exon_tier1_count);
			System.out.println("Tier2 Exons: " + exon_tier2_count);
			System.out.println("Not Prioritized Exons: " + exon_notprioiritized_count);
			System.out.println("NA Exons: " + exon_na_count);
			System.out.println("Total: " + (exon_tier1_count + exon_tier2_count + exon_notprioiritized_count));
			
			System.out.println("Exon\tcheck_for_exon_coverage_bias_in_gtex_samples_flag\tgrab_car_target_flag\thighRiskTumorNormalMatch\tspliced_exon_status");
			Iterator itr6 = candidates_not_removed_after_manual_check_status.keySet().iterator();
			while (itr6.hasNext()) {
				String exon = (String)itr6.next();
				String status = (String)candidates_not_removed_after_manual_check_status.get(exon);
				System.out.println(exon + "\t" + status);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap grab_all_exon_list(String inputFile) {
		HashMap grab_all_exon = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				grab_all_exon.put(split[0], split[0]);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grab_all_exon;
	}
	public static HashMap grab_ihc_medium_high_brain(String inputFile_IHC_staining_med_high) {
		HashMap gene_with_medium_ihc_brain = new HashMap();
		try {
			
			
			FileInputStream fstream = new FileInputStream(inputFile_IHC_staining_med_high);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				if (split[2].equals("true")) {
					gene_with_medium_ihc_brain.put(geneName, geneName);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return gene_with_medium_ihc_brain;
	}
	/**
	 * we excluded testis and ovary
	 * @param inputFile
	 * @return
	 */
	public static HashMap grab_TissueHighInAtLeastOneNormal(String inputFile_IHC_staining_med_high) {
		HashMap gene_TissueHighInAtLeastOneNormal = new HashMap();
		try {
			
			
			FileInputStream fstream = new FileInputStream(inputFile_IHC_staining_med_high);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				if (split[8].equals("true")) {
					gene_TissueHighInAtLeastOneNormal.put(geneName, geneName);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return gene_TissueHighInAtLeastOneNormal;
	}
	

	
	/**
	 * grabbed all medium high tissues
	 * @param inputFile
	 * @return
	 */
	public static HashMap grabIHCMedHighTissues(String inputFile_IHC_staining_med_high) {
		HashMap grabIHCMedHighTissues = new HashMap();
		try {
			
			
			FileInputStream fstream = new FileInputStream(inputFile_IHC_staining_med_high);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				String tissues = split[7];				
				grabIHCMedHighTissues.put(geneName, tissues);				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return grabIHCMedHighTissues;
	}
	
	
	/**
	 * grabbed all high tissues
	 * @param inputFile
	 * @return
	 */
	public static HashMap grabIHCHighTissues(String inputFile_IHC_staining_high_only) {
		HashMap grabICHighTissues = new HashMap();
		try {
						
			FileInputStream fstream = new FileInputStream(inputFile_IHC_staining_high_only);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				String tissues = split[7];				
				grabICHighTissues.put(geneName, tissues);				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return grabICHighTissues;
	}
	
	/**
	 * grab gene with at least one high ihc in key tissues including
	 * BrainStatus	LungStatus	PancreasStatus	PituitaryStatus	LiverStatus	
	 * @param inputFile
	 * @return
	 */
	public static HashMap grabGeneWithAtLeastOneHighIHCinKeyTissue(String inputFile_IHC_staining_high_only) {
		HashMap grabGeneWithAtLeastOneHighTissues = new HashMap();
		try {
						
			FileInputStream fstream = new FileInputStream(inputFile_IHC_staining_high_only);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				String atleastonehightissue_flag = split[1];		
				if (atleastonehightissue_flag.equals("true")) {
					grabGeneWithAtLeastOneHighTissues.put(geneName, geneName);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return grabGeneWithAtLeastOneHighTissues;
	}
	
	/**
	 * Check whether 
	 * Brain_HiExonProp	Lung_HiExonProp	Liver_HiExonProp	Pancreas_HiExonProp
	 * are at least min expressed
	 */
	public static HashMap grabGeneProportion(String gene_expression_level_tumornormal_pairing_file, String type) {
		HashMap grabGeneProportion = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(gene_expression_level_tumornormal_pairing_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] split_header = in.readLine().split("\t");
			int index = 0;
			for (int i = 0; i < split_header.length; i++) {
				if (split_header[i].equals(type)) {
					index = i;
					break;
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				double proportion = new Double(split[index]);		
				grabGeneProportion.put(geneName, proportion);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grabGeneProportion;
		
	}
	
	/**
	 * Check tumor normal pair for each exon
	 * If exon is significant for one, we will remove the gene
	 * Only high risk tissue including 
	 * 			pairs.put("HGG", "Brain");			
			pairs.put("LGG", "Brain");
			pairs.put("RB", "Nerve");
			pairs.put("NBL", "Nerve");					
			pairs.put("BT", "Brain");
			pairs.put("MB", "Brain");			
			pairs.put("CPC", "Brain");
			pairs.put("EPD", "Brain");
			pairs.put("WLM", "Kidney");					
	 */
	public static HashMap grab_tumor_highrisknormaltissue_paired_exon(String exon_expression_level_tumornormal_pairing_file, HashMap candidate_exon_list) {
		HashMap tumor_normal_gene_list = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(exon_expression_level_tumornormal_pairing_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String gene = split[0].split("_")[0];
				if (candidate_exon_list.containsKey(exon)) {
					if (split[9].equals("true")) {
						tumor_normal_gene_list.put(gene, gene);
					}
				}
				
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tumor_normal_gene_list;
	}
	
	/**
	 * Check tumor normal pair for each exon
	 * If exon is significant for one, we will remove the gene
	 * Only high risk tissue including 		
	low_risk_pairs.put("MEL", "Skin");
	low_risk_pairs.put("ACT", "Adrenal");	
	low_risk_pairs.put("RHB", "Muscle");			
	 */
	public static HashMap grab_tumor_lowrisknormaltissue_paired_exon(String exon_expression_level_tumornormal_pairing_file, HashMap candidate_exon_list) {
		HashMap tumor_normal_gene_list = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(exon_expression_level_tumornormal_pairing_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String gene = split[0].split("_")[0];
				if (candidate_exon_list.containsKey(exon)) {
					if (split[10].equals("true")) {
						tumor_normal_gene_list.put(gene, gene);
					}
				}
				
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tumor_normal_gene_list;
	}
	

	/**
	 * Check BoneMarrow Expression For Gene
	 * Bone marrow High is determined based on 
	 * Note this is an incomplete list based on putative targets			
	 */
	public static HashMap grab_High_BoneMarrow_Expressed_Genes(String bone_marrow_expression_file) {
		HashMap gene_with_high_bone_marrow_expression = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(bone_marrow_expression_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].equals("High")) {
					gene_with_high_bone_marrow_expression.put(split[0], split[0]);
				}
				
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gene_with_high_bone_marrow_expression;
	}
	

	/**
	 * Check Global Protein Abundance of GTEx tissue		
	 */
	public static HashMap grab_overall_protein_abundance_GTEx(String gtex_proteomics_gene_expression_file) {
		HashMap grab_overall_protein_abundance_GTEx = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(gtex_proteomics_gene_expression_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				grab_overall_protein_abundance_GTEx.put(split[0], new Double(split[1]));								
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grab_overall_protein_abundance_GTEx;
	}
	
	

	/**
	 * Load Alternatively Spliced Exon		
	 */
	public static HashMap grab_alternatively_spliced_exon(String inputFile_alternatively_spliced_exon) {
		HashMap grab_alternatively_spliced_exon = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile_alternatively_spliced_exon);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				grab_alternatively_spliced_exon.put(split[1], split[2]);								
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grab_alternatively_spliced_exon;
	}

	/**
	 * Load Alternatively Spliced Genes		
	 */
	public static HashMap grab_alternatively_spliced_gene(String inputFile_alternatively_spliced_exon) {
		HashMap grab_alternatively_spliced_gene = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile_alternatively_spliced_exon);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");	
				String geneName = split[1].split("_")[0];
				grab_alternatively_spliced_gene.put(geneName, geneName);								
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grab_alternatively_spliced_gene;
	}
	public static HashMap grab_candidate_summary() {
		HashMap candidate_summary = new HashMap();
		candidate_summary.put("VCAN", "AS");
		candidate_summary.put("FN1", "Manuscript CAR Example");
		candidate_summary.put("TNC", "Manuscript CAR Example");
		candidate_summary.put("COL11A1", "Manuscript CAR Example");
		candidate_summary.put("NRCAM", "AS");
		candidate_summary.put("PICALM", "AS");
		candidate_summary.put("FYN", "AS");
		
		candidate_summary.put("KDR", "Preclinical and clinical Exploration");
		candidate_summary.put("FAP", "Preclinical and clinical Exploration");
		candidate_summary.put("CD276", "Preclinical and clinical Exploration");
		candidate_summary.put("GPC3", "Preclinical and clinical Exploration");
		candidate_summary.put("CD83", "Preclinical Exploration");
		candidate_summary.put("IL1RAP", "Preclinical Exploration");
		candidate_summary.put("KIT", "Preclinical Exploration");
		candidate_summary.put("MET", "Preclinical Exploration");		
		candidate_summary.put("PROM1", "Preclinical Exploration");
		candidate_summary.put("GPC2", "Preclinical Exploration");
						
		candidate_summary.put("TGFB2", "Oncofetal");
		candidate_summary.put("WNT5A", "Oncofetal");
		candidate_summary.put("IGF2", "Oncofetal");
		candidate_summary.put("SPA17", "Testis Restricted Target");
		candidate_summary.put("TEX14", "Testis Restricted Target");
		candidate_summary.put("LAMA1", "Testis Restricted Target");
		candidate_summary.put("SMOC1", "Testis Restricted Target");
		candidate_summary.put("TNFAIP6", "Testis Restricted Target");
		candidate_summary.put("GPC2", "Testis Restricted Target");
		candidate_summary.put("COL20A1", "Testis Restricted Target");
		
		return candidate_summary;
	}
	
	public static HashMap get_geneName2transcript(String gene_transcript_exon_file) {
		HashMap geneName2transcript = new HashMap();
		try {
			

			FileInputStream fstream = new FileInputStream(gene_transcript_exon_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String geneName = split[1];
				String geneID = split[2];
				String transcriptID = split[3];
				if ( geneName2transcript.containsKey(geneName)) {
					LinkedList list = (LinkedList) geneName2transcript.get(geneName);
					list.add(transcriptID);
					 geneName2transcript.put(geneName, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(transcriptID);
					 geneName2transcript.put(geneName, list);
				}
							
			}
			in.close();			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return geneName2transcript;
	}
	
	public static HashMap get_transcript2geneName(String gene_transcript_exon_file) {
		HashMap transcript2geneName = new HashMap();
		try {
			

			FileInputStream fstream = new FileInputStream(gene_transcript_exon_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String geneName = split[1];
				String geneID = split[2];
				String transcriptID = split[3];
				transcript2geneName.put(transcriptID, geneName);				
							
			}
			in.close();			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transcript2geneName;
	}
	public static HashMap get_transcript2exon(String gene_transcript_exon_file) {
		HashMap transcript2exon = new HashMap();
		try {
			

			FileInputStream fstream = new FileInputStream(gene_transcript_exon_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String geneName = split[1];
				String geneID = split[2];
				String transcriptID = split[3];
				if (transcript2exon.containsKey(transcriptID)) {
					LinkedList list = (LinkedList)transcript2exon.get(transcriptID);
					list.add(exon);
					transcript2exon.put(transcriptID, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(exon);
					transcript2exon.put(transcriptID, list);
				}
							
			}
			in.close();			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transcript2exon;
	}
	
	public static HashMap get_exon2transcript(String gene_transcript_exon_file) {
		HashMap exon2transcript = new HashMap();
		try {
			

			FileInputStream fstream = new FileInputStream(gene_transcript_exon_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String geneName = split[1];
				String geneID = split[2];
				String transcriptID = split[3];
				if (exon2transcript.containsKey(transcriptID)) {
					LinkedList list = (LinkedList)exon2transcript.get(transcriptID);
					list.add(transcriptID);
					exon2transcript.put(exon, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(transcriptID);
					exon2transcript.put(exon, list);
				}
							
			}
			in.close();			
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exon2transcript;
	}
	public static HashMap okay2keeporfilter(String okaytokeeporfilter_file) {
		HashMap okay2keeporfilter = new HashMap();
		try {
			
			
			FileInputStream fstream = new FileInputStream(okaytokeeporfilter_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].equals("OkayToKeep")) {
					okay2keeporfilter.put(split[0], true);
				}
				if (split[1].equals("OkayToRemove")) {
					okay2keeporfilter.put(split[0], false);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return okay2keeporfilter;
	}
	public static HashMap check_for_exon_coverage_bias_in_gtex_samples(String exon_folder) {		
		
		HashMap gene_with_problem = new HashMap();
		
		try {
			File folder_with_exon = new File(exon_folder);
			for (File f: folder_with_exon.listFiles()) {				
				String inputFile = f.getPath();
				
				//if (inputFile.contains("ADAM9")) {
					double exon_num = 0.0;
					FileInputStream fstream = new FileInputStream(inputFile);
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					String header = in.readLine();
					String[] split_header = header.split("\t");
					LinkedList[] tissue = new LinkedList[split_header.length];
					double[] min = new double[split_header.length];
					double[] max = new double[split_header.length];
					for (int i = 0; i < split_header.length; i++) {
						tissue[i] = new LinkedList();
						min[i] = Double.MAX_VALUE;
						max[i] = Double.MIN_VALUE;
					}
					while (in.ready()) {
						
						String str = in.readLine();
						String[] split = str.split("\t");
						if (!split[0].contains("_ECM")) {
							exon_num++;
							tissue[0].add(exon_num);
							for (int i = 1; i < split.length; i++) {
								tissue[i].add(new Double(split[i]));
								if (min[i] > new Double(split[i])) {
									min[i] = new Double(split[i]);
								}
								if (max[i] < new Double(split[i])) {
									max[i] = new Double(split[i]);
								}
							}								
						}
					}
					in.close();
					
					int num_of_exons = tissue[0].size();
					//System.out.println(f.getName());
					double[] exon_numbers = MathTools.convertListDouble2Double(tissue[0]);
					for (int i = 1; i <= 32; i++) {
					//int[] indexes = {2, 3, 7, 9, 10, 13, 14, 15, 16, 20, 21, 25, 27, 29};
					//for (int i: indexes) {
					    
						double[] sample_values = MathTools.convertListDouble2Double(tissue[i]);
						
						if (sample_values.length > 1) {
							
							try {
								double r = MathTools.PearsonCorrel(exon_numbers, sample_values);
								double pval = MathTools.PearsonCorrelPvalue(exon_numbers, sample_values);
								double q1 = MathTools.quartile(sample_values, 25);
								double q3 = MathTools.quartile(sample_values, 75);
								if (pval < 0.05 && q3 >= 0.5) {
									String geneName = f.getName().replaceAll(".txt", "");
									String line = f.getName().replaceAll(".txt", "") + "\t" + split_header[i] + "\t" + r + "\t" + pval + "\t" + q1 + "\t" + q3 + "\t" + num_of_exons;
									//System.out.println(line);
									gene_with_problem.put(geneName, geneName);
								}
							} catch (Exception ex) {
								
							}
						}
					
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// additional genes with known issues in GTEx;
		// gene_with_problem.put("FCGBP", "FCGBP");
		// gene_with_problem.put("LAMA4", "LAMA4");
		return gene_with_problem;
	}
	
	public static HashMap grab_gene_list(String inputFile) {
		HashMap gene_list = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gene_list.put(split[0], split[0]);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gene_list;
	}
	
	public static HashMap grab_differentially_expressed_exons(String inputFile) {
		HashMap gene_list = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//gene_list.put(split[3], split[3]);
				//
				gene_list.put(split[0], split[0]);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gene_list;
	}
	
	public static HashMap double_check_exon_candidates(String inputFile) {
		HashMap double_check_exon_candidates = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//gene_list.put(split[3], split[3]);
				//
				double_check_exon_candidates.put(split[0], split[0]);
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return double_check_exon_candidates;
	}
}
