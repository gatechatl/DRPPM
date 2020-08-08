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

import statistics.general.MathTools;
import statistics.general.StatisticsConversion;


/**
 * Input should include each disease compared to a reference group.
 * For each reference group, grab the sample and the exon's values.
 * Generate the rank for each 
 * Perform differential analysis using Wilcoxon Rank Sum Test
 * @author tshaw
 *
 */
public class JuncSalvagerWilcoxonTestRank {

	
	public static String description() {
		return "Split the matrix based on their annotation";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[inputPCGPFolder] [inputPCGPAnnotationFile] [inputGTExFolder] [inputGTExAnnotationFile] [outputComparison] [outputMetaAnalysis]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputPCGPFolder = args[0]; // PCGP_903_FPKM_ECM_filtcol.txt
			String inputPCGPAnnotationFile = args[1]; // /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/PCGP_TARGET_RNAseq_Analysis/PCGP_TARGET_Sample2DiseaseType.txt
			String inputGTExFolder = args[2]; // GTEx_7526_FPKM_ECM_filtcol.txt
			String inputGTExAnnotationFile = args[3]; // /research/rgs01/project_space/zhanggrp/MethodDevelopment/common/ExonLevelQuantificationPipeline/Reference/ExonLevelGTF/GTEx_Annotation/GTEx_SampleID2Histology.txt			
			String outputComparison = args[4];
			String outputMetaAnalysis = args[5];
			FileWriter fwriter = new FileWriter(outputComparison);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Exon\tDisease\tGTExHistology\tpvalue\tzscore\t\tweighted_zscore\tDiseaseMedian\tGTExMedian\n");
			
			//String outputFolderGTEx = args[5];
			//String outputFile = args[6]; // write Disease \t GTEx_Tissue \t wilcox_pvalue \t 
			
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
			Iterator itr = pcgp_annotation.keySet().iterator();
			while (itr.hasNext()) {
				String pcgp_disease = (String)itr.next();
				if (run_once) {
					String pcgp_file_run_once = inputPCGPFolder + "/" + pcgp_disease + "_rank.txt";
					FileInputStream fstreamDisease_run_once = new FileInputStream(pcgp_file_run_once);
					DataInputStream dinDisease_run_once = new DataInputStream(fstreamDisease_run_once);
					BufferedReader inDisease_run_once = new BufferedReader(new InputStreamReader(dinDisease_run_once));
					while (inDisease_run_once.ready()) {
						inDisease_run_once.readLine();
						total_exons = total_exons + 1;
					}
					run_once = false;
				}
				
				Iterator itr2 = gtex_annotation.keySet().iterator();				
				while (itr2.hasNext()) {
					String histology = (String)itr2.next();
					
					
					String pcgp_file = inputPCGPFolder + "/" + pcgp_disease + "_rank.txt";
					FileInputStream fstreamDisease = new FileInputStream(pcgp_file);
					DataInputStream dinDisease = new DataInputStream(fstreamDisease);
					BufferedReader inDisease = new BufferedReader(new InputStreamReader(dinDisease));
					String header_disase = inDisease.readLine();
					
					String gtex_file = inputGTExFolder + "/" + histology + "_rank.txt";
					FileInputStream fstream_gtex = new FileInputStream(gtex_file);
					DataInputStream din_gtex = new DataInputStream(fstream_gtex);
					BufferedReader in_gtex = new BufferedReader(new InputStreamReader(din_gtex));
					
					
					String header_gtex = in_gtex.readLine();
					while (inDisease.ready()) {
						String str_disease = inDisease.readLine();
						String[] split_disease = str_disease.split("\t");
						String str_gtex = in_gtex.readLine();
						String[] split_gtex = str_gtex.split("\t");
						
						String exon = split_disease[0];
						
						double[] disease_values = new double[split_disease.length - 1];
						double[] gtex_values = new double[split_gtex.length - 1];
						
						for (int i = 1; i < split_disease.length; i++) {
							disease_values[i - 1] = new Double(split_disease[i]);
						}
						for (int i = 1; i < split_gtex.length; i++) {
							gtex_values[i - 1] = new Double(split_gtex[i]);
						}
						
						double disease_median = MathTools.median(disease_values);
						double gtex_median = MathTools.median(gtex_values);
						
						if (disease_expression_map.containsKey(exon)) {					
							HashMap disease2median = (HashMap)disease_expression_map.get(exon);
							disease2median.put(pcgp_disease, disease_median);
							disease_expression_map.put(exon, disease2median);
						} else {
							HashMap disease2median = new HashMap();
							disease2median.put(pcgp_disease, disease_median);
							disease_expression_map.put(exon, disease2median);
						}
						//disease_expression_map.put(exon + "\t" + pcgp_disease, disease_median);
						
						if (gtex_expression_map.containsKey(exon)) {					
							HashMap histology2median = (HashMap)gtex_expression_map.get(exon);
							histology2median.put(histology, gtex_median);
							gtex_expression_map.put(exon, histology2median);
						} else {
							HashMap histology2median = new HashMap();
							histology2median.put(histology, gtex_median);
							gtex_expression_map.put(exon, histology2median);
						}
						double weight = 0.0;
						
						double percentile_disease = disease_median / new Double(total_exons);
						double percentile_gtex = gtex_median / new Double(total_exons);
						//System.out.println(exon + "\t" + histology + "\t" + rank + "\t" + meta_analysis_map.size() + "\t" + percentile);
						if (percentile_gtex > 0.5) {
							if (percentile_disease < 0.25) {
								weight = 3.0;
							} else if (percentile_disease < 0.5) {
								weight = 2.0;
							} else if (percentile_disease < 0.75) {
								weight = 1.0;
							}							
						}						
						
						//gtex_expression_map.put(exon + "\t" + histology, gtex_median);
						double pvalue = 1.0;
						if (disease_median < gtex_median) { // smaller the higher expressed it is
							pvalue = MathTools.WilcoxRankSumTest(disease_values, gtex_values);
							if (pvalue < 2.2E-16) {
								pvalue = 2.2E-16;
							}
						}
						double zscore = 0.0;
						
						if (pvalue < 1.0) {
							zscore = -1 * StatisticsConversion.inverseCumulativeProbability(pvalue);							
						}						
						
						double weighted_zscore = zscore * weight;
						out.write(split_disease[0] + "\t" + pcgp_disease + "\t" + histology + "\t" + pvalue + "\t" + zscore + "\t" + weighted_zscore + "\t" + disease_median + "\t" + gtex_median + "\n");
						if (meta_analysis_map.containsKey(exon)) {
							LinkedList list = (LinkedList)meta_analysis_map.get(exon);
							list.add(weighted_zscore);						
							meta_analysis_map.put(exon, list);
						} else {
							LinkedList list = new LinkedList();
							list.add(weighted_zscore);						
							meta_analysis_map.put(exon, list);
						}
					}
					inDisease.close();
					in_gtex.close();
				} // exit itr2
				
			} // exit itr
			out.close();
			
			FileWriter fwriter_meta_analysis = new FileWriter(outputMetaAnalysis);
			BufferedWriter out_meta_analysis = new BufferedWriter(fwriter_meta_analysis);
			out_meta_analysis.write("Exon\tDisease\tNumGTExHighExpr\tHistologyGTExHighExpr\tDiseaseHighExpr\tDiseaseMedExpr\n");
			itr = meta_analysis_map.keySet().iterator();
			while (itr.hasNext()) {
				String exon = (String)itr.next();
				LinkedList list = (LinkedList)meta_analysis_map.get(exon);
				double[] values = MathTools.convertListDouble2Double(list);
				double stouffer_zscore = 0.0;
				if (values.length > 0) {
					stouffer_zscore = MathTools.sum(values) / Math.sqrt(new Double(values.length));
				}
				HashMap histology2median = (HashMap)gtex_expression_map.get(exon);
				int num_medhigh_expr_gtex = 0;
				String gtex_med_high_histology = "";
				String disease_med_expr = "";
				String disease_high_expr = "";
				Iterator itr2 = histology2median.keySet().iterator();
				while (itr2.hasNext()) {
					String histology = (String)itr2.next();
					double rank = (Double)histology2median.get(histology);
					
					double percentile = rank / new Double(meta_analysis_map.size());
					//System.out.println(exon + "\t" + histology + "\t" + rank + "\t" + meta_analysis_map.size() + "\t" + percentile);
					if (percentile <= 0.5) { 
						num_medhigh_expr_gtex++;
						gtex_med_high_histology += histology + ",";
						
					}										
				}
				HashMap disease2median = (HashMap)disease_expression_map.get(exon);
				itr2 = disease2median.keySet().iterator();
				while (itr2.hasNext()) {
					String pcgp_disease = (String)itr2.next();
					double rank = (Double)disease2median.get(pcgp_disease);
					double percentile = rank / new Double(meta_analysis_map.size());
					if (percentile <= 0.5 && percentile > 0.25) { 
<<<<<<< HEAD
						disease_med_expr += pcgp_disease + ":" + rank + ",";						
					}
					if (percentile <= 0.25) { 
						disease_high_expr += pcgp_disease + ":" + rank + ",";						
=======
						disease_med_expr += pcgp_disease + ",";						
					}
					if (percentile <= 0.25) { 
						disease_high_expr += pcgp_disease + ",";						
>>>>>>> dae22fb134ef93dd3a4b55fe3f588cbbe3c83712
					}				
				}
				out_meta_analysis.write(exon + "\t" + stouffer_zscore + "\t" + num_medhigh_expr_gtex + "\t" + gtex_med_high_histology + "\t" + disease_high_expr + "\t" + disease_med_expr + "\n");
			}
			out_meta_analysis.close();
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
}
