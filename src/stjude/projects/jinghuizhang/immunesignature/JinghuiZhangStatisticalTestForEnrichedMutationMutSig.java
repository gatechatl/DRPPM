package stjude.projects.jinghuizhang.immunesignature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import statistics.general.MathTools;

/**
 * Perform Fishers-Exact Test to examine whether specific immune signatures are enriched in specific mutations in each disease type.
 * See example at /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/ImmuneSignatureAnalysis 
 * @author tshaw
 *
 */
public class JinghuiZhangStatisticalTestForEnrichedMutationMutSig {


	public static String type() {
		return "Immune";
	}
	public static String description() {
		return "Perform Fishers exact test to examine whether specific immune signatures are enriched for specific mutational signatures in a particular cancer type.";
	}
	public static String parameter_info() {
		return "[input_matrix_expr_file] [mutSigFile] [outputFile]";
	}
	public static void execute(String[] args) {				
		try {
			
			HashMap geneName2sampleName = new HashMap();
			HashMap sampleName2geneName = new HashMap();

			String input_matrix_expr_file = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_immune_ssGSEA_transpose.txt";
			String mutSigFile= args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SV.txt";
			String outputFile = args[2]; // "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\immune_signature_enriched_in_particular_mutations\\PanCancerImmuneSignature_Enrichment.txt";
			
			String outputMatrixFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("ImmuneSignature\tDiseaseType\tGeneWithMut\tImmuneType\tEnrichPval\tA\tB\tC\tD\n");


			FileWriter fwriter2 = new FileWriter(outputMatrixFile);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			out2.write("ImmuneSignature\tDiseaseType_GeneWithMut\tWilcox_pval\tTissueType\n");
			
			HashMap samples2test = new HashMap();
			FileInputStream fstream = new FileInputStream(mutSigFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				samples2test.put(split_header[i], split_header[i]);
			}
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split_header.length; i++) {
					samples2test.put(split_header[i], split_header[i]);
					if (split[i].equals("true")) {
						if (geneName2sampleName.containsKey(split[0])) {
							LinkedList list = (LinkedList)geneName2sampleName.get(split[0]);
							if (!list.contains(split_header[i])) {
								list.add(split_header[i]);
							}
							geneName2sampleName.put(split[0], list);
						} else {
							LinkedList list = new LinkedList();
							if (!list.contains(split_header[i])) {
								list.add(split_header[i]);
							}
							geneName2sampleName.put(split[0], list);
						}
						
						
						if (sampleName2geneName.containsKey(split_header[i])) {					
							LinkedList list = (LinkedList)sampleName2geneName.get(split_header[i]);
							if (!split[0].equals("NA")) {
								if (!list.contains(split[0])) {
									list.add(split[0]);
								}
							}
							
							sampleName2geneName.put(split_header[i], list);
						} else {
							LinkedList list = new LinkedList();
							if (!split[0].equals("NA")) {
								if (!list.contains(split[0])) {
									list.add(split[0]);
								}
							}							
							sampleName2geneName.put(split_header[i], list);
						}
					}
					
					
				}

				
			}
			in.close();
			
			
			
			
			HashMap sampleType = new HashMap();
			HashMap type2samples = new HashMap();
			HashMap type2samples_index = new HashMap();
			HashMap samples_with_mutation = new HashMap();
			int count = 0;
			
			fstream = new FileInputStream(input_matrix_expr_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine().replaceAll("\\.", "-");
			split_header = header.split("\t");
			
			for (int i = 1; i < split_header.length; i++) {
				if (samples2test.containsKey(split_header[i].split("-")[0])) {
					System.out.println(split_header[i].split("-")[0]);
					split_header[i] = split_header[i].split("\\.")[0].split("-")[0];
					String type = split_header[i].replaceAll("0", "").replaceAll("1", "").replaceAll("2", "").replaceAll("3", "").replaceAll("4", "").replaceAll("5", "").replaceAll("6", "").replaceAll("7", "").replaceAll("8", "").replaceAll("9", "");
					sampleType.put(split_header[i], type);
					if (type2samples.containsKey(type)) {
						LinkedList list = (LinkedList)type2samples.get(type);
						list.add(split_header[i]);
						type2samples.put(type, list);
						
						LinkedList list_index = (LinkedList)type2samples_index.get(type);
						list_index.add(i);
						type2samples_index.put(type, list_index);
					} else {
						LinkedList list = new LinkedList();
						list.add(split_header[i]);
						type2samples.put(type, list);
						
						LinkedList list_index = new LinkedList();
						list_index.add(i);
						type2samples_index.put(type, list_index);
					}
					if (sampleName2geneName.containsKey(split_header[i])) {
						count++;
						samples_with_mutation.put(split_header[i], split_header[i]);
					} else {
						//System.out.println(split_header[i]);
					}
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String feature = split[0];
				
				// iterate through all the mutations
				Iterator itr_mutations = geneName2sampleName.keySet().iterator();
				while (itr_mutations.hasNext()) {
					String geneName = (String)itr_mutations.next();
					LinkedList samples_with_mutation_list = (LinkedList)geneName2sampleName.get(geneName);
					if (samples_with_mutation_list.size() >= 5) {
					
						// iterate through all the type of samples
						Iterator itr = type2samples_index.keySet().iterator();
						while (itr.hasNext()) {
							String type = (String)itr.next();
							
							LinkedList mutation_hit = new LinkedList(); // samples that contains the mutation
							LinkedList mutation_miss = new LinkedList(); // samples without the mutation
							LinkedList list_index = (LinkedList)type2samples_index.get(type);
							Iterator itr_list_index = list_index.iterator();
							while (itr_list_index.hasNext()) {
								int i = (Integer)itr_list_index.next();						
								String value = split[i];
								if (samples_with_mutation_list.contains(split_header[i])) {
									mutation_hit.add(value);
								} else {
									mutation_miss.add(value);
								}
							}
							
							// if there are sufficient samples that contains the mutation
							if (mutation_hit.size() >= 3 && mutation_miss.size() >= 3) {
								
								// perform fishers exact test
								int type1_hit = 0;
								int type2_hit = 0;
								int type3_hit = 0;
								int type4_hit = 0;
								
								int type1_miss = 0;
								int type2_miss = 0;
								int type3_miss = 0;
								int type4_miss = 0;
								Iterator itr_hit = mutation_hit.iterator();
								while (itr_hit.hasNext()) {
									String immune_type = (String)itr_hit.next();
									if (immune_type.equals("1")) {
										type1_hit++;
									}
									if (immune_type.equals("2")) {
										type2_hit++;
									}
									if (immune_type.equals("3")) {
										type3_hit++;
									}
									if (immune_type.equals("4")) {
										type4_hit++;
									}
								}
								
								Iterator itr_miss = mutation_miss.iterator();
								while (itr_miss.hasNext()) {
									String immune_type = (String)itr_miss.next();
									if (immune_type.equals("1")) {
										type1_miss++;
									}
									if (immune_type.equals("2")) {
										type2_miss++;
									}
									if (immune_type.equals("3")) {
										type3_miss++;
									}
									if (immune_type.equals("4")) {
										type4_miss++;
									}
								}
								int a = type1_hit;
								int b = type1_miss;
								int c = type2_hit + type3_hit + type4_hit;
								int d = type2_miss + type3_miss + type4_miss;
								double type1_enrichment_pval = MathTools.fisherTest(a, b, c, d);
								double hit_odds_ratio = new Double(a) / new Double(c);
								double miss_odds_ratio = new Double(b) / new Double(d);
								if (hit_odds_ratio < miss_odds_ratio) {
									type1_enrichment_pval = 1.0;
								}
								
								a = type2_hit;
								b = type2_miss;
								c = type1_hit + type3_hit + type4_hit;
								d = type1_miss + type3_miss + type4_miss;
								double type2_enrichment_pval = MathTools.fisherTest(a, b, c, d);
								hit_odds_ratio = new Double(a) / new Double(c);
								miss_odds_ratio = new Double(b) / new Double(d);
								if (hit_odds_ratio < miss_odds_ratio) {
									type2_enrichment_pval = 1.0;
								}
								
								a = type3_hit;
								b = type3_miss;
								c = type1_hit + type2_hit + type4_hit;
								d = type1_miss + type2_miss + type4_miss;
								double type3_enrichment_pval = MathTools.fisherTest(a, b, c, d);
								hit_odds_ratio = new Double(a) / new Double(c);
								miss_odds_ratio = new Double(b) / new Double(d);
								if (hit_odds_ratio < miss_odds_ratio) {
									type3_enrichment_pval = 1.0;
								}
								
								a = type4_hit;
								b = type4_miss;
								c = type1_hit + type2_hit + type3_hit;
								d = type1_miss + type2_miss + type3_miss;
								double type4_enrichment_pval = MathTools.fisherTest(a, b, c, d);
								hit_odds_ratio = new Double(a) / new Double(c);
								miss_odds_ratio = new Double(b) / new Double(d);
								if (hit_odds_ratio < miss_odds_ratio) {
									type4_enrichment_pval = 1.0;
								}
								
								/*
								double[] mutation_hit_values = MathTools.convertListDouble2Double(mutation_hit);
								double[] mutation_miss_values = MathTools.convertListDouble2Double(mutation_miss);
								
								String mutation_hit_values_str = "";
								for (double values: mutation_hit_values) {
									mutation_hit_values_str += values + ",";
								}
								if (mutation_hit_values_str.length() > 0) {
									mutation_hit_values_str = mutation_hit_values_str.substring(0, mutation_hit_values_str.length() - 1);
								}
								
								String mutation_absent_values_str = "";
								for (double values: mutation_miss_values) {
									mutation_absent_values_str += values + ",";
								}
								if (mutation_absent_values_str.length() > 0) {
									mutation_absent_values_str = mutation_absent_values_str.substring(0, mutation_absent_values_str.length() - 1);
								}
								TTest test = new TTest();
								double pval = test.tTest(mutation_hit_values, mutation_miss_values);
								
								WilcoxonSignedRankTest wilcoxTest = new WilcoxonSignedRankTest();
								MannWhitneyUTest mannWhitney = new MannWhitneyUTest();
								double wilcox_pval = mannWhitney.mannWhitneyUTest(mutation_hit_values, mutation_miss_values);
								*/
								if (type1_enrichment_pval < 0.05) {									
									out.write(feature + "\t" + type + "\t" + geneName + "\tImmuneType1\t" + type1_enrichment_pval + "\t" + type1_hit + "\t" + type1_miss + "\t" + (type2_hit + type3_hit + type4_hit) + "\t" + (type2_miss + type3_miss + type4_miss) + "\n");
								}
								if (type2_enrichment_pval < 0.05) {									
									out.write(feature + "\t" + type + "\t" + geneName + "\tImmuneType2\t" + type2_enrichment_pval + "\t" + type2_hit + "\t" + type2_miss + "\t" + (type1_hit + type3_hit + type4_hit) + "\t" + (type1_miss + type3_miss + type4_miss) + "\n");
								}
								if (type3_enrichment_pval < 0.05) {									
									out.write(feature + "\t" + type + "\t" + geneName + "\tImmuneType3\t" + type3_enrichment_pval + "\t" + type3_hit + "\t" + type3_miss + "\t" + (type1_hit + type2_hit + type4_hit) + "\t" + (type1_miss + type2_miss + type4_miss) + "\n");
								}
								if (type4_enrichment_pval < 0.05) {									
									out.write(feature + "\t" + type + "\t" + geneName + "\tImmuneType4\t" + type4_enrichment_pval + "\t" + type4_hit + "\t" + type4_miss + "\t" + (type1_hit + type2_hit + type3_hit) + "\t" + (type1_miss + type2_miss + type3_miss) + "\n");
								}
								double score1 = -1 * MathTools.log2(type1_enrichment_pval) / MathTools.log2(10);
								double score2 = -1 * MathTools.log2(type2_enrichment_pval) / MathTools.log2(10);
								double score3 = -1 * MathTools.log2(type3_enrichment_pval) / MathTools.log2(10);
								double score4 = -1 * MathTools.log2(type4_enrichment_pval) / MathTools.log2(10);
								out2.write(feature + "\t" + type + "_" + geneName + "\tImmuneType1\t" + score1 + "\t" +  test_tissue_type(type) + "\n");
								out2.write(feature + "\t" + type + "_" + geneName + "\tImmuneType2\t" + score2 + "\t" +  test_tissue_type(type) + "\n");
								out2.write(feature + "\t" + type + "_" + geneName + "\tImmuneType3\t" + score3 + "\t" +  test_tissue_type(type) + "\n");
								out2.write(feature + "\t" + type + "_" + geneName + "\tImmuneType4\t" + score4 + "\t" +  test_tissue_type(type) + "\n");
								
								/*if (MathTools.mean(mutation_hit_values) > MathTools.mean(mutation_miss_values)) {
									double score = -1 * MathTools.log2(wilcox_pval);
									out2.write(feature + "\t" + type + "_" + geneName + "\t" + score + "\t" +  test_tissue_type(type) + "\n");
								} else {
									double score = MathTools.log2(wilcox_pval);
									out2.write(feature + "\t" + type + "_" + geneName + "\t" + score + "\t" +  test_tissue_type(type) + "\n");
								}*/
							}
						}
					}
				}
			}
			in.close();
			out.close();
			out2.close();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String test_tissue_type(String type) {
		String tissue_type = "OTHER";
		if (type.contains("SJACT")) {
			tissue_type = "SOLID";
		}
		if (type.contains("SJOS")) {
			tissue_type = "SOLID";
		}
		if (type.contains("SJWLM")) {
			tissue_type = "SOLID";
		}
		if (type.contains("SJNBL")) {
			tissue_type = "SOLID";
		}
		if (type.contains("SJRHB")) {
			tissue_type = "SOLID";
		}
		if (type.contains("SJEPD")) {
			tissue_type = "BRAIN";
		}
		
		if (type.contains("SJMB")) {
			tissue_type = "BRAIN";
		}
		if (type.contains("SJHGG")) {
			tissue_type = "BRAIN";
		}
		if (type.contains("SJLGG")) {
			tissue_type = "BRAIN";
		}
		
		if (type.contains("SJBALL")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJALL")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJTALL")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJAML")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJAMLM")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJCBF")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJCOGALL")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJCOGALL")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJERG")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJERG")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJMLL")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJHYPO")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJETV")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJINF")) {
			tissue_type = "BLOOD";
		}
		if (type.contains("SJPHALL")) {
			tissue_type = "BLOOD";
		}
		return tissue_type;
	}
}
