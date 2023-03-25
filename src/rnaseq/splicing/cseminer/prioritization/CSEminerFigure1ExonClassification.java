package rnaseq.splicing.cseminer.prioritization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

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
public class CSEminerFigure1ExonClassification {

	public static void main(String[] args) {
		
		try {
			
			String inputFile_exon_candidate_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/CSEMinerExonList/67502_Exon_list.txt";
			
			String outputFirstFilter = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/OutputFilteredCandidates/CSEMiner_Significant_Exons.txt";
			FileWriter fwriter_FirstFilter = new FileWriter(outputFirstFilter);
			BufferedWriter out_FirstFilter = new BufferedWriter(fwriter_FirstFilter);
									
			String outputTieredCategoryFile = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/OutputFilteredCandidates/CSEMiner_Tiered_Exons.txt";
			FileWriter fwriter_TieredCategory = new FileWriter(outputTieredCategoryFile);
			BufferedWriter out_TieredCategory = new BufferedWriter(fwriter_TieredCategory);
			
			
			String gene_expression_level_tumornormal_pairing_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/GTEx_Exon_Annotation/Gene_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt";
			String exon_expression_level_tumornormal_pairing_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/GTEx_Exon_Annotation/Exon_Pediatric_GTEx_TissueEnrichment_TissuePairFlag_20230314.txt";
			String inputFile_alternatively_spliced_exon = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/AfterManualReview/CSEminer_alternatively_spliced_exon_candidates_updated_20230325.txt";
			String blacklist_genes_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/AfterManualReview/blacklist_targetlist.txt";
			String inputFile_IHC_staining_high_only = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/IHC_Protein_Annotation/tim_summary_ihc_normal_tissue_high_only.txt";
			String inputFile_IHC_staining_med_high = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/IHC_Protein_Annotation/tim_summary_ihc_normal_tissue_medium_or_high.txt";			
			String bone_marrow_expression_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/MicroarrayDerivedBoneMarrowExpression/GeneAnnotationOfBoneMarrowExpression.txt";
			String gtex_proteomics_gene_expression_file = "/Users/4472414/Documents/Current_Manuscripts/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/ProteomicsAnnotation/gene_abundance_normPSM_median.txt"; // distribution indicate cutoff should be around 0.1323528
			
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
			
			FileInputStream fstream_exon_candidate_file = new FileInputStream(inputFile_exon_candidate_file);
			DataInputStream din_exon_candidate_file = new DataInputStream(fstream_exon_candidate_file);
			BufferedReader in_exon_candidate_file = new BufferedReader(new InputStreamReader(din_exon_candidate_file));
			String header_exon_candidate = in_exon_candidate_file.readLine();
			out_FirstFilter.write(header_exon_candidate + "\n");
			while (in_exon_candidate_file.ready()) {
				String str = in_exon_candidate_file.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("_")[0];				
				// restrict to significant candidates
				// significant candidates were filetered based on Restricted to <= 5 normal samples and at least >= 1 Tumor expression 
				// Tumor score must be higher than normal score.
				// Meta Score > 1
				// filter black list genes
				if (!blacklist_gene_list.containsKey(geneName)) {
					if (split[7].contains("MATRISOME") || split[7].contains("SURFACEOME")) {
						if (split[17].equals("Significant")) {
							out_FirstFilter.write(str + "\n");
						}
					}
				}
				
			}
			in_exon_candidate_file.close();
			out_FirstFilter.close();
			System.out.println("Performed initial exon candidate filtering...");
			
			
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
			
			
			// Grab Manually Annotated Alternatively Spliced Exon
			HashMap grab_alternatively_spliced_exon = grab_alternatively_spliced_exon(inputFile_alternatively_spliced_exon);
			HashMap grab_alternatively_spliced_gene = grab_alternatively_spliced_gene(inputFile_alternatively_spliced_exon);
			
			int tier1_count = 0;
			HashMap tier1_genes = new HashMap();
			int tier2_count = 0;
			HashMap tier2_genes = new HashMap();
			int not_prioritized_count = 0;
			HashMap not_prioritized_genes = new HashMap();
			
			double rna_cutoff = 0.3;
			double protein_cutoff = 0.1323528;
			FileInputStream fstream_filtered_candidate_file = new FileInputStream(outputFirstFilter);
			DataInputStream din_filtered_candidate_file = new DataInputStream(fstream_filtered_candidate_file);
			BufferedReader in_filtered_candidate_file = new BufferedReader(new InputStreamReader(din_filtered_candidate_file));
			String header_str = in_filtered_candidate_file.readLine();
			out_TieredCategory.write(header_str + "\tGene\tAS_Flag\tHighNormalExonProp>0.3_Flag\tHighRiskTumorNormalMatch\tCheckForHighIHCNormalFlag\tCheckForHighMediumInBrain");
			out_TieredCategory.write("\tIHC_AboveIntermediate(Exclude Testis Ovary)\tBoneMarrowHigh\tHighProteomicsHitInGTExForGene\tLowRiskTumorNormalPair\tExcludeTestisOvaryRNAExpr");
			// IHC_AboveIntermediateHigh(Exclude Testis Ovary)	BoneMarrowHigh	HighProteomicsHitInGTExForExon	LowRiskTumorNormalPair	ExcludeTestisOvaryRNAExpr
			out_TieredCategory.write("\tFinalPrioritization");
			out_TieredCategory.write("\n");
			while (in_filtered_candidate_file.ready()) {
				String str = in_filtered_candidate_file.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String geneName = split[0].split("_")[0];
				String exon_prioritization = "Tier1";
				if (!(grab_alternatively_spliced_gene.containsKey(geneName) && !grab_alternatively_spliced_exon.containsKey(exon))) {
					String spliced_exon_status = "Gene";
					if (grab_alternatively_spliced_exon.containsKey(exon)) {
						spliced_exon_status = "AS";
					}
					out_TieredCategory.write(str + "\t" + geneName + "\t" + spliced_exon_status);
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
					out_TieredCategory.write("\t" + highNormalFlag);
					
					boolean highRiskTumorNormalMatch = false;
					if (grab_high_risk_tumornormal_pairs.containsKey(geneName)) {
						highRiskTumorNormalMatch = true;
					}
					out_TieredCategory.write("\t" + highRiskTumorNormalMatch);
					
					boolean checkForHighIHCNormalFlag = false;
					if (geneWithAtLeastOneHighIHCinKeyTissue.containsKey(geneName)) {
						checkForHighIHCNormalFlag = true;
					}
					out_TieredCategory.write("\t" + checkForHighIHCNormalFlag);
					
					
					boolean checkForHighMediumInBrain = false;
					if (ihc_medium_high_brain.containsKey(geneName)) {
						checkForHighMediumInBrain = true;
					}
					out_TieredCategory.write("\t" + checkForHighMediumInBrain);
					
					boolean ihc_AboveHighFlag = false;
					if (geneWithAtLeastOneMedHighTissue.containsKey(geneName)) {
						ihc_AboveHighFlag = true;
					}
					out_TieredCategory.write("\t" + ihc_AboveHighFlag);
					
					
					// Finished Hard Filtering Check
					// Next Filter for Tier 2 Candidates
					
					boolean ihc_AboveIntermediateHighFlag = false;
					if (geneWithAtLeastOneMedHighTissue.containsKey(geneName)) {
						ihc_AboveIntermediateHighFlag = true;
					}
					out_TieredCategory.write("\t" + ihc_AboveIntermediateHighFlag);
					
					
					boolean boneMarrowHighFlag = false;
					if (grab_bone_marrow.containsKey(geneName)) {
						boneMarrowHighFlag = true;
					}
					out_TieredCategory.write("\t" + boneMarrowHighFlag);
					
					boolean proteomicsHighGTExFlag = false;
					if (grab_overall_protein_abundance_GTEx.containsKey(geneName)) {
						double protein_gtex_norm_psm = (Double)grab_overall_protein_abundance_GTEx.get(geneName);
						if (protein_gtex_norm_psm > protein_cutoff) {
							proteomicsHighGTExFlag = true;
						}
					}
					out_TieredCategory.write("\t" + proteomicsHighGTExFlag);
					
					
					boolean lowRiskTumorNormalPairFlag = false; 
					if (grab_low_risk_tumornormal_pairs.containsKey(geneName)) {
						lowRiskTumorNormalPairFlag = true;
					}
					out_TieredCategory.write("\t" + lowRiskTumorNormalPairFlag);
										
					boolean numNormalTissueAboveMedian = false; 
					if (grabGeneProportion_num_normal_tissue_above_median.containsKey(geneName)) {
						double num_normal_tissue = (Double)grabGeneProportion_num_normal_tissue_above_median.get(geneName);
						if (num_normal_tissue > 0) {
							numNormalTissueAboveMedian = true;
						}
					}
					out_TieredCategory.write("\t" + numNormalTissueAboveMedian);
					
					//if ((highNormalFlag || highRiskTumorNormalMatch || checkForHighIHCNormalFlag || checkForHighMediumInBrain) && !spliced_exon_status.equals("AS")) {
					if ((highRiskTumorNormalMatch || checkForHighIHCNormalFlag || checkForHighMediumInBrain) && !spliced_exon_status.equals("AS")) {
						exon_prioritization = "Not Priorized";
						not_prioritized_count++;
						not_prioritized_genes.put(geneName, geneName);
					} else if ((highNormalFlag || ihc_AboveIntermediateHighFlag || boneMarrowHighFlag || proteomicsHighGTExFlag || lowRiskTumorNormalPairFlag || numNormalTissueAboveMedian) && !spliced_exon_status.equals("AS")) {
						exon_prioritization = "Tier 2";
						tier2_count++;
						tier2_genes.put(geneName, geneName);
					} else {
						tier1_count++;
						tier1_genes.put(geneName, geneName);
					}
					
					
					// final exon prioritization
					out_TieredCategory.write("\t" + exon_prioritization);
					out_TieredCategory.write("\n");
				}
				
				
			}
			in_filtered_candidate_file.close();
			out_TieredCategory.close();
			System.out.println("Total Tier1: " + tier1_count);
			System.out.println("Total Tier2: " + tier2_count);
			System.out.println("Total Not Prioritized Exons: " + not_prioritized_count);
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
			pairs.put("DSRCT", "Intestine");					
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
	
}
