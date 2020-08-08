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
 * Perform TTest to examine whether specific immune signatures are enriched in specific mutations in each disease type.
 * See example at /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/ImmuneSignatureAnalysis 
 * @author tshaw
 *
 */
public class JinghuiZhangStatisticalTestForEnrichedMutationImmuneSignature {


	public static String type() {
		return "Immune";
	}
	public static String description() {
		return "Perform TTest to examine whether specific immune signatures are enriched in specific mutations in each disease type.";
	}
	public static String parameter_info() {
		return "[input_matrix_expr_file] [fusionFile] [snvindelFile] [cnvFile] [outputFile] [outputMatrix]";
	}
	public static void execute(String[] args) {				
		try {
			
			HashMap geneName2sampleName = new HashMap();
			HashMap sampleName2geneName = new HashMap();

			String input_matrix_expr_file = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_immune_ssGSEA_transpose.txt";
			String fusionFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SV.txt";
			String snvindelFile = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SNVIndel_Simplified.txt";
			String cnvFile = args[3];
			String outputFile = args[4]; // "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\immune_signature_enriched_in_particular_mutations\\PanCancerImmuneSignature_Enrichment.txt";
			
			//String outputMatrix = args[5]; // generate a matrix of -log2(pvalues) for all the comparisons
			
			String outputMatrixFile = args[5];
			String outputAll = args[6];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("ImmuneSignature\tDiseaseType\tGeneWithMut\tImmuneType\tEnrichPval\tA\tB\tC\tD\n");

			FileWriter fwriter3 = new FileWriter(outputAll);
			BufferedWriter out3 = new BufferedWriter(fwriter3);
			out3.write("ImmuneSignature\tDiseaseType_GeneWithMut\tImmuneType\tEnrichPval\tProportion\tCount\tTissueType\n");

			FileWriter fwriter2 = new FileWriter(outputMatrixFile);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			out2.write("ImmuneSignature\tDiseaseType_GeneWithMut\tWilcox_pval\tTissueType\n");
			
			FileInputStream fstream = new FileInputStream(fusionFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("\\.")[0].split("-")[0];
				if (!split[1].equals("NA")) {
					if (geneName2sampleName.containsKey(split[1])) {
						LinkedList list = (LinkedList)geneName2sampleName.get(split[1]);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[1], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[1], list);
					}
				}
				
				if (!split[2].equals("NA")) {
					if (geneName2sampleName.containsKey(split[2])) {
						LinkedList list = (LinkedList)geneName2sampleName.get(split[2]);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[2], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[2], list);
					}
				}
				
				if (sampleName2geneName.containsKey(split[0])) {					
					LinkedList list = (LinkedList)sampleName2geneName.get(split[0]);
					if (!split[1].equals("NA")) {
						if (!list.contains(split[1])) {
							list.add(split[1]);
						}
					}
					if (!split[2].equals("NA")) {
						if (!list.contains(split[2])) {
							list.add(split[2]);
						}
					}
					sampleName2geneName.put(split[0], list);
				} else {
					LinkedList list = new LinkedList();
					if (!split[1].equals("NA")) {
						if (!list.contains(split[1])) {
							list.add(split[1]);
						}
					}
					if (!split[2].equals("NA")) {
						if (!list.contains(split[2])) {
							list.add(split[2]);
						}
					}
					sampleName2geneName.put(split[0], list);
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(snvindelFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("\\.")[0].split("-")[0];
				if (!split[1].equals("NA")) {
					if (geneName2sampleName.containsKey(split[1])) {
						LinkedList list = (LinkedList)geneName2sampleName.get(split[1]);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[1], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(split[1], list);
					}
				

					if (sampleName2geneName.containsKey(split[0])) {					
						LinkedList list = (LinkedList)sampleName2geneName.get(split[0]);
						if (!list.contains(split[1])) {
							list.add(split[1]);
						}
						sampleName2geneName.put(split[0], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[1])) {
							list.add(split[1]);
						}
						sampleName2geneName.put(split[0], list);
					}
				}
			}
			in.close();
			
			System.out.println(sampleName2geneName.size());
			
			fstream = new FileInputStream(cnvFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].split("\\.")[0].split("-")[0];
				double length = new Double(split[3]) - new Double(split[2]);
				LinkedList tags = new LinkedList();
				String type = split[6].replaceAll("mattr:", "");
				if (length > 1000000) {
					//tags.add(type + "_CNV_1000000");
					tags.add("CNV_1000000");
				}
				if (length > 5000000) {
					// tags.add(type + "_CNV_5000000");
					tags.add("CNV_5000000");
				}
				if (length > 10000000) {
					// tags.add(type + "_CNV_10000000");
					tags.add("CNV_10000000");
				}
				if (length > 20000000) {
					// tags.add(type + "_CNV_20000000");
					tags.add("CNV_20000000");
				}
				if (length > 30000000) {
					// tags.add(type + "_CNV_30000000");
					tags.add("CNV_30000000");
				}

				Iterator itr_tag = tags.iterator();
				while (itr_tag.hasNext()) {
					String tag = (String)itr_tag.next();
					if (geneName2sampleName.containsKey(tag)) {
						LinkedList list = (LinkedList)geneName2sampleName.get(tag);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(tag, list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						geneName2sampleName.put(tag, list);
					}
				
	
					if (sampleName2geneName.containsKey(split[0])) {					
						LinkedList list = (LinkedList)sampleName2geneName.get(split[0]);
						if (!list.contains(tag)) {
							list.add(tag);
						}
						sampleName2geneName.put(split[0], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(tag)) {
							list.add(tag);
						}
						sampleName2geneName.put(split[0], list);
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
			String header = in.readLine().replaceAll("\\.", "-");
			String[] split_header = header.split("\t");
			
			for (int i = 1; i < split_header.length; i++) {
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
					System.out.println(split_header[i]);
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
								if (type1_enrichment_pval > 1.0) {
									type1_enrichment_pval = 1.0;
								}
								if (type2_enrichment_pval > 1.0) {
									type2_enrichment_pval = 1.0;
								}
								if (type3_enrichment_pval > 1.0) {
									type3_enrichment_pval = 1.0;
								}
								if (type4_enrichment_pval > 1.0) {
									type4_enrichment_pval = 1.0;
								}
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
								out3.write(feature + "\t" + type + "_" + geneName + "\tImmuneType1\t" + type1_enrichment_pval + "\t" + new Double(type1_hit) / (type1_hit + type2_hit + type3_hit + type4_hit) + "\t" + type1_hit + "\t" + test_tissue_type(type) + "\n");
								out3.write(feature + "\t" + type + "_" + geneName + "\tImmuneType2\t" + type2_enrichment_pval + "\t" + new Double(type2_hit) / (type1_hit + type2_hit + type3_hit + type4_hit) + "\t" + type2_hit + "\t" +  test_tissue_type(type) + "\n");
								out3.write(feature + "\t" + type + "_" + geneName + "\tImmuneType3\t" + type3_enrichment_pval + "\t" + new Double(type3_hit) / (type1_hit + type2_hit + type3_hit + type4_hit) + "\t" + type3_hit + "\t" +  test_tissue_type(type) + "\n");
								out3.write(feature + "\t" + type + "_" + geneName + "\tImmuneType4\t" + type4_enrichment_pval + "\t" + new Double(type4_hit) / (type1_hit + type2_hit + type3_hit + type4_hit) + "\t" + type4_hit + "\t" +  test_tissue_type(type) + "\n");
								
								double score1 = -1 * MathTools.log2(type1_enrichment_pval) / MathTools.log2(10);
								double score2 = -1 * MathTools.log2(type2_enrichment_pval) / MathTools.log2(10);
								double score3 = -1 * MathTools.log2(type3_enrichment_pval) / MathTools.log2(10);
								double score4 = -1 * MathTools.log2(type4_enrichment_pval) / MathTools.log2(10);
								out2.write(feature + "\t" + type + "_" + geneName + "\tImmuneType1\t" + score1 + "\t" +  test_tissue_type(type) + "\n");
								out2.write(feature + "\t" + type + "_" + geneName + "\tImmuneType2\t" + score2 + "\t" +  test_tissue_type(type) + "\n");
								out2.write(feature + "\t" + type + "_" + geneName + "\tImmuneType3\t" + score3 + "\t" +  test_tissue_type(type) + "\n");
								out2.write(feature + "\t" + type + "_" + geneName + "\tImmuneType4\t" + score4 + "\t" +  test_tissue_type(type) + "\n");
								
							}
						}
					}
				}
			}
			in.close();
			out.close();
			out2.close();
			out3.close();
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
