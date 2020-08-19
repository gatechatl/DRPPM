package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JuncSalvagerWilcoxTestPostProcessing {

	
	public static String description() {
		return "Prioritize the candidates.";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[inputComprehensiveZScoreFile] [inputPCGPFolder] [inputPCGPAnnotationFile] [inputGTExFolder] [inputGTExAnnotationFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputComprehensiveZScoreFile = args[0];
			String inputPCGPFolder = args[1]; // PCGP_903_FPKM_ECM_filtcol.txt
			String inputPCGPAnnotationFile = args[2]; // /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/PCGP_TARGET_RNAseq_Analysis/PCGP_TARGET_Sample2DiseaseType.txt
			String inputGTExFolder = args[3]; // GTEx_7526_FPKM_ECM_filtcol.txt
			String inputGTExAnnotationFile = args[4]; // /research/rgs01/project_space/zhanggrp/MethodDevelopment/common/ExonLevelQuantificationPipeline/Reference/ExonLevelGTF/GTEx_Annotation/GTEx_SampleID2Histology.txt
			String outputFile = args[5];
			String outputFile_moreNormalization = args[6];
			
			HashMap pcgp_annotation = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputPCGPAnnotationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[1] = split[1].replaceAll(" ", "_");
				if (pcgp_annotation.containsKey(split[1])) {
					LinkedList list = (LinkedList)pcgp_annotation.get(split[1]);
					list.add(split[0]);
					pcgp_annotation.put(split[1], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[0]);
					pcgp_annotation.put(split[1], list);
				}
			}
			in.close();
			
			
			HashMap gtex_annotation = new HashMap();
			fstream = new FileInputStream(inputGTExAnnotationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[1] = split[1].replaceAll(" ", "_");
				if (gtex_annotation.containsKey(split[1])) {
					LinkedList list = (LinkedList)gtex_annotation.get(split[1]);
					list.add(split[0]);
					gtex_annotation.put(split[1], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(split[0]);
					gtex_annotation.put(split[1], list);
				}
			}
			in.close();
			
			
			int total_exons = 0;
			boolean run_once = true;
			HashMap disease_expression_map = new HashMap();
			HashMap gtex_expression_map = new HashMap();
			HashMap meta_analysis_map = new HashMap();
			
			
			HashMap disease_median_map = new HashMap();
			HashMap gtex_median_map = new HashMap();
			Iterator itr = pcgp_annotation.keySet().iterator();
			while (itr.hasNext()) {
				String pcgp_disease = (String)itr.next();

				String pcgp_file = inputPCGPFolder + "/" + pcgp_disease + "_rank_1FPKM_median.txt";
				FileInputStream fstreamDisease = new FileInputStream(pcgp_file);
				DataInputStream dinDisease = new DataInputStream(fstreamDisease);
				BufferedReader inDisease = new BufferedReader(new InputStreamReader(dinDisease));
				inDisease.readLine();
				while (inDisease.ready()) {
					String line = inDisease.readLine();
					String[] split_disease = line.split("\t");
					disease_median_map.put(split_disease[0] + "\t" + pcgp_disease, split_disease[1]);
				}	
				inDisease.close();
			}

			
			Iterator itr2 = gtex_annotation.keySet().iterator();				
			while (itr2.hasNext()) {
				String histology = (String)itr2.next();
				

				String gtex_file = inputGTExFolder + "/" + histology + "_rank_1FPKM_median.txt";
				FileInputStream fstream_gtex = new FileInputStream(gtex_file);
				DataInputStream din_gtex = new DataInputStream(fstream_gtex);
				BufferedReader in_gtex = new BufferedReader(new InputStreamReader(din_gtex));
				in_gtex.readLine();
				while (in_gtex.ready()) {
					String line = in_gtex.readLine();
					String[] split_disease = line.split("\t");
					gtex_median_map.put(split_disease[0] + "\t" + histology, split_disease[1]);
				}	
				in_gtex.close();
			}
			
			HashMap scores = new HashMap(); // exon
			HashMap composite_pow2_weights = new HashMap();
			//HashMap scores_count = new HashMap();
			HashMap disease_exon_1st_quartile = new HashMap();
			HashMap disease_exon_2nd_quartile = new HashMap();
			HashMap disease_exon_3rd_quartile = new HashMap();
			HashMap disease_exon_4th_quartile = new HashMap();
			HashMap normal_exon_1st_quartile = new HashMap();
			HashMap normal_exon_2nd_quartile = new HashMap();
			HashMap normal_exon_3rd_quartile = new HashMap();
			HashMap normal_exon_4th_quartile = new HashMap();
			
			FileInputStream fstream_comp = new FileInputStream(inputComprehensiveZScoreFile);
			DataInputStream din_comp = new DataInputStream(fstream_comp);
			BufferedReader in_comp = new BufferedReader(new InputStreamReader(din_comp));
			in_comp.readLine();
			while (in_comp.ready()) {
				String str = in_comp.readLine();
				String[] split = str.split("\t");
				String exon = split[0];
				String disease = split[1];
				String histology = split[2];
				double gtex_weight = 0.0;
				if (pcgp_annotation.containsKey(disease)) {
					if (gtex_median_map.containsKey(exon + "\t" + histology)) {
						gtex_weight = new Double((String)gtex_median_map.get(exon + "\t" + histology));
					}
					if (gtex_weight < 0.25) {
						if (normal_exon_1st_quartile.containsKey(exon)) {
							HashMap gtex_tissue = (HashMap)normal_exon_1st_quartile.get(exon);
							gtex_tissue.put(histology, histology);
							normal_exon_1st_quartile.put(exon, gtex_tissue);
						} else {
							HashMap gtex_tissue = new HashMap();
							gtex_tissue.put(histology, histology);
							normal_exon_1st_quartile.put(exon, gtex_tissue);
						}
					} else if (gtex_weight < 0.5) {
						if (normal_exon_2nd_quartile.containsKey(exon)) {
							HashMap gtex_tissue = (HashMap)normal_exon_2nd_quartile.get(exon);
							gtex_tissue.put(histology, histology);
							normal_exon_2nd_quartile.put(exon, gtex_tissue);
						} else {
							HashMap gtex_tissue = new HashMap();
							gtex_tissue.put(histology, histology);
							normal_exon_2nd_quartile.put(exon, gtex_tissue);
						}
					} else if (gtex_weight < 0.75) {
						if (normal_exon_3rd_quartile.containsKey(exon)) {
							HashMap gtex_tissue = (HashMap)normal_exon_3rd_quartile.get(exon);
							gtex_tissue.put(histology, histology);
							normal_exon_3rd_quartile.put(exon, gtex_tissue);
						} else {
							HashMap gtex_tissue = new HashMap();
							gtex_tissue.put(histology, histology);
							normal_exon_3rd_quartile.put(exon, gtex_tissue);
						}
					} else if (gtex_weight <= 1.0){
						if (normal_exon_4th_quartile.containsKey(exon)) {
							HashMap gtex_tissue = (HashMap)normal_exon_4th_quartile.get(exon);
							gtex_tissue.put(histology, histology);
							normal_exon_4th_quartile.put(exon, gtex_tissue);
						} else {
							HashMap gtex_tissue = new HashMap();
							gtex_tissue.put(histology, histology);
							normal_exon_4th_quartile.put(exon, gtex_tissue);
						}
					}
					
					
					double disease_weight = 0.0;
					if (disease_median_map.containsKey(exon + "\t" + disease)) {
						disease_weight = new Double((String)disease_median_map.get(exon + "\t" + disease));
					}
					
					if (disease_weight < 0.25) {
						if (disease_exon_1st_quartile.containsKey(exon)) {
							HashMap pcgp_target_disase = (HashMap)disease_exon_1st_quartile.get(exon);
							pcgp_target_disase.put(disease, disease);
							disease_exon_1st_quartile.put(exon, pcgp_target_disase);
						} else {
							HashMap pcgp_target_disase = new HashMap();
							pcgp_target_disase.put(disease, disease);
							disease_exon_1st_quartile.put(exon, pcgp_target_disase);
						}
					} else if (disease_weight < 0.5) {
						if (disease_exon_2nd_quartile.containsKey(exon)) {
							HashMap pcgp_target_disase = (HashMap)disease_exon_2nd_quartile.get(exon);
							pcgp_target_disase.put(disease, disease);
							disease_exon_2nd_quartile.put(exon, pcgp_target_disase);
						} else {
							HashMap pcgp_target_disase = new HashMap();
							pcgp_target_disase.put(disease, disease);
							disease_exon_2nd_quartile.put(exon, pcgp_target_disase);
						}
					} else if (disease_weight < 0.75) {
						if (disease_exon_3rd_quartile.containsKey(exon)) {
							HashMap pcgp_target_disase = (HashMap)disease_exon_3rd_quartile.get(exon);
							pcgp_target_disase.put(disease, disease);
							disease_exon_3rd_quartile.put(exon, pcgp_target_disase);
						} else {
							HashMap pcgp_target_disase = new HashMap();
							pcgp_target_disase.put(disease, disease);
							disease_exon_3rd_quartile.put(exon, pcgp_target_disase);
						}
					} else if (disease_weight <= 1.0){
						if (disease_exon_4th_quartile.containsKey(exon)) {
							HashMap pcgp_target_disase = (HashMap)disease_exon_4th_quartile.get(exon);
							pcgp_target_disase.put(disease, disease);
							disease_exon_4th_quartile.put(exon, pcgp_target_disase);
						} else {
							HashMap pcgp_target_disase = new HashMap();
							pcgp_target_disase.put(disease, disease);
							disease_exon_4th_quartile.put(exon, pcgp_target_disase);
						}
					}					
					
					double score = new Double(split[4]) * (disease_weight - gtex_weight);
					double normalized_weight = Math.pow(disease_weight - gtex_weight, 2);
					
					if (scores.containsKey(split[0])) {
						double prev_score = (Double)scores.get(split[0]);
						scores.put(split[0], (prev_score + score));
						//int count = (Integer)scores_count.get(split[0] + "\t" + disease);
						//count++;
						//scores_count.put(split[0] + "\t" + disease, count);
					} else {
						scores.put(split[0], score);
						//scores_count.put(split[0] + "\t" + disease, 1);
					}
					// composite_pow2_weight
					if (composite_pow2_weights.containsKey(split[0])) {
						double prev_normalized_weight = (Double)composite_pow2_weights.get(split[0]);
						composite_pow2_weights.put(split[0], (prev_normalized_weight + normalized_weight));
						//int count = (Integer)scores_count.get(split[0] + "\t" + disease);
						//count++;
						//scores_count.put(split[0] + "\t" + disease, count);
					} else {
						composite_pow2_weights.put(split[0], normalized_weight);
						//scores_count.put(split[0] + "\t" + disease, 1);
					}
					// composite_pow2_weight
					if (composite_pow2_weights.containsKey(split[0])) {
						double prev_normalized_weight = (Double)composite_pow2_weights.get(split[0]);
						composite_pow2_weights.put(split[0], (prev_normalized_weight + normalized_weight));
						//int count = (Integer)scores_count.get(split[0] + "\t" + disease);
						//count++;
						//scores_count.put(split[0] + "\t" + disease, count);
					} else {
						composite_pow2_weights.put(split[0], normalized_weight);
						//scores_count.put(split[0] + "\t" + disease, 1);
					}
				}
			}
			in_comp.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Exon\tWeightedScore\tHighNormal\tNumGTEx1stQuartile\tNumGTEx2ndQuartile\tNumGTEx3rdQuartile\tNumGTEx4thQuartile\tNumDisease1stQuartile\tNumDisease2ndQuartile\tNumDisease3rdQuartile\tNumDisease4thQuartile\tGTEx1stQuartile\tGTEx2ndQuartile\tGTEx3rdQuartile\tGTEx4thQuartile\tDisease1stQuartile\tDisease2ndQuartile\tDisease3rdQuartile\tDisease4thQuartile\n");
			
			FileWriter fwriter2 = new FileWriter(outputFile_moreNormalization);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			out2.write("Exon\tWeightedScore\tHighNormal\tNumGTEx1stQuartile\tNumGTEx2ndQuartile\tNumGTEx3rdQuartile\tNumGTEx4thQuartile\tNumDisease1stQuartile\tNumDisease2ndQuartile\tNumDisease3rdQuartile\tNumDisease4thQuartile\tGTEx1stQuartile\tGTEx2ndQuartile\tGTEx3rdQuartile\tGTEx4thQuartile\tDisease1stQuartile\tDisease2ndQuartile\tDisease3rdQuartile\tDisease4thQuartile\n");
			
			itr = scores.keySet().iterator();
			while (itr.hasNext()) {
				String exon = (String)itr.next();
				double score = (Double)scores.get(exon);
				double composite_pow2_weight = (Double)composite_pow2_weights.get(exon);
				
				HashMap disease_1st_quartile_map = new HashMap();
				if (disease_exon_1st_quartile.containsKey(exon)) {
					disease_1st_quartile_map = (HashMap)disease_exon_1st_quartile.get(exon);
				}				
				HashMap disease_2nd_quartile_map = new HashMap();
				if (disease_exon_2nd_quartile.containsKey(exon)) {
					disease_2nd_quartile_map = (HashMap)disease_exon_2nd_quartile.get(exon);
				}				
				HashMap disease_3rd_quartile_map = new HashMap(); 
				if (disease_exon_3rd_quartile.containsKey(exon)) {
					disease_3rd_quartile_map = (HashMap)disease_exon_3rd_quartile.get(exon);
				}		
				
				HashMap disease_4th_quartile_map = new HashMap();
				if (disease_exon_4th_quartile.containsKey(exon)) {
					disease_4th_quartile_map = (HashMap)disease_exon_4th_quartile.get(exon);
				}		
				
				HashMap normal_exon_1st_quartile_map = new HashMap();
				if (normal_exon_1st_quartile.containsKey(exon)) {
					normal_exon_1st_quartile_map = (HashMap)normal_exon_1st_quartile.get(exon);
				}
				HashMap normal_exon_2nd_quartile_map = new HashMap(); 
				if (normal_exon_2nd_quartile.containsKey(exon)) {
					normal_exon_2nd_quartile_map = (HashMap)normal_exon_2nd_quartile.get(exon);
				}
				HashMap normal_exon_3rd_quartile_map = new HashMap(); 
				if (normal_exon_3rd_quartile.containsKey(exon)) {
					normal_exon_3rd_quartile_map = (HashMap)normal_exon_3rd_quartile.get(exon);
				}
				HashMap normal_exon_4th_quartile_map = new HashMap(); 
				if (normal_exon_4th_quartile.containsKey(exon)) {
					normal_exon_4th_quartile_map = (HashMap)normal_exon_4th_quartile.get(exon);
				}
				String normal_1 = grab_hashmap_key(normal_exon_1st_quartile_map);
				String normal_2 = grab_hashmap_key(normal_exon_2nd_quartile_map);
				String normal_3 = grab_hashmap_key(normal_exon_3rd_quartile_map);
				String normal_4 = grab_hashmap_key(normal_exon_4th_quartile_map);
				
				String disease_1 = grab_hashmap_key(disease_1st_quartile_map);
				String disease_2 = grab_hashmap_key(disease_2nd_quartile_map);
				String disease_3 = grab_hashmap_key(disease_3rd_quartile_map);
				String disease_4 = grab_hashmap_key(disease_4th_quartile_map);
				
				out.write(exon + "\t" + (score) + "\t" + (normal_exon_3rd_quartile_map.size() + normal_exon_4th_quartile_map.size()) + "\t" + normal_exon_1st_quartile_map.size() + "\t" + normal_exon_2nd_quartile_map.size() + "\t" + normal_exon_3rd_quartile_map.size() + "\t" + normal_exon_4th_quartile_map.size() + "\t" + disease_1st_quartile_map.size() + "\t" + disease_2nd_quartile_map.size() + "\t" + disease_3rd_quartile_map.size() + "\t" + disease_4th_quartile_map.size() + "\t" + normal_1 + "\t" + normal_2 + "\t" + normal_3 + "\t" + normal_4 + "\t" + disease_1 + "\t" + disease_2 + "\t" + disease_3 + "\t" + disease_4 + "\n");
				out2.write(exon + "\t" + (score / Math.sqrt(composite_pow2_weight)) + "\t" + (normal_exon_3rd_quartile_map.size() + normal_exon_4th_quartile_map.size()) + "\t" + normal_exon_1st_quartile_map.size() + "\t" + normal_exon_2nd_quartile_map.size() + "\t" + normal_exon_3rd_quartile_map.size() + "\t" + normal_exon_4th_quartile_map.size() + "\t" + disease_1st_quartile_map.size() + "\t" + disease_2nd_quartile_map.size() + "\t" + disease_3rd_quartile_map.size() + "\t" + disease_4th_quartile_map.size() + "\t" + normal_1 + "\t" + normal_2 + "\t" + normal_3 + "\t" + normal_4 + "\t" + disease_1 + "\t" + disease_2 + "\t" + disease_3 + "\t" + disease_4 + "\n");
			}
			out.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String grab_hashmap_key(HashMap map) {
		String result = "";
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			result += (String)itr.next() + ",";
		}
		return result;
	}
}