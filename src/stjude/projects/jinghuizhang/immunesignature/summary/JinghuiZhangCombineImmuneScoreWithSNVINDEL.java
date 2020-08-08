package stjude.projects.jinghuizhang.immunesignature.summary;

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


/**
 * Quantify the number of mutation for each sample and annotate the immune scores
 * @author tshaw
 *
 */
public class JinghuiZhangCombineImmuneScoreWithSNVINDEL {

	
	public static void main(String[] args) {
		
		try {
			
			

			String outputFile_ssGSEA = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\SpearmanCorrelMutationBurden\\pcgp_immune_ssGSEA_filtered2SNVINDEL_ALL.txt";
			FileWriter fwriter = new FileWriter(outputFile_ssGSEA);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String outputFile_SNVINDEL_Count = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\SpearmanCorrelMutationBurden\\PCGP_TARGET_SNVINDEL_Table_Prioritized_Filtered_ALL.txt";
			FileWriter fwriter_SNVINDEL = new FileWriter(outputFile_SNVINDEL_Count);
			BufferedWriter out_SNVINDEL = new BufferedWriter(fwriter_SNVINDEL);
			
			String outputFile_Disease_Separman = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\SpearmanCorrelMutationBurden\\PCGP_TARGET_SNVINDEL_Table_Disease_Spearman_ALL.txt";
			FileWriter fwriter_disease_spearman = new FileWriter(outputFile_Disease_Separman);
			BufferedWriter out_disease_spearman = new BufferedWriter(fwriter_disease_spearman);
			
			HashMap map_immune_score = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\pcgp_immune_ssGSEA.txt"; 		
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String immune_score_header = in.readLine();
			out.write(immune_score_header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map_immune_score.put(split[0], str);
			}
			in.close();
			
			HashMap prioritized_SNVINDEL = new HashMap();
			inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\pediatric.hg19.protein_alt_mutcount_clean_name.txt"; 		
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String SNVINDEL_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].contains("WES") || split[1].contains("CGI") || split[1].contains("WGS")) {
					if (prioritized_SNVINDEL.containsKey(split[0])) {
						String prev = (String)prioritized_SNVINDEL.get(split[0]);
						String[] split_prev = prev.split("\t");
						if (split_prev[1].equals("CGI")) {
							if (split[1].equals("WGS") || split[1].equals("WES")) {
								prioritized_SNVINDEL.put(split[0], str);
							}
						}
						System.out.println("Prev: " + prev);
						System.out.println("Other: " + str);
					} else {
						prioritized_SNVINDEL.put(split[0], str);
					}
					
					
				}
			}
			in.close();
			
			int num_immune_scores = 0; 
			HashMap filtered_immune_score = new HashMap();
			Iterator itr = map_immune_score.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (prioritized_SNVINDEL.containsKey(sampleName)) {
					String line = (String)map_immune_score.get(sampleName);
					String[] split = line.split("\t");
					num_immune_scores = split.length - 1;
					filtered_immune_score.put(sampleName, line);
					out.write(line + "\n");
				}
			}
			out.close();
			
			
			out_SNVINDEL.write("SampleName\tSNVINDEL\n");
			itr = prioritized_SNVINDEL.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (map_immune_score.containsKey(sampleName)) {
					String line = (String)prioritized_SNVINDEL.get(sampleName);
					String[] split = line.split("\t");
					out_SNVINDEL.write(split[0] + "\t" + split[2] + "\n");
				}
			}
			out_SNVINDEL.close();
			
			out_disease_spearman.write("ImmuneName");
			String[] diseases = {"SJLGG", "SJEPD", "SJNBL", "SJWLM", "SJHGG", "SJRHB", "SJOS", "SJACT"};
			for (String disease: diseases) {
				out_disease_spearman.write("\t" + disease);
			}
			out_disease_spearman.write("\n");
			for (int i = 1; i < num_immune_scores + 1; i++) {
				out_disease_spearman.write(immune_score_header.split("\t")[i]);
				for (String disease: diseases) {

					LinkedList SNVINDEL = new LinkedList();
					LinkedList immune_score = new LinkedList();
					itr = filtered_immune_score.keySet().iterator();
					while (itr.hasNext()) {
						String sampleName = (String)itr.next();
						if (sampleName.contains(disease)) {
							String SNVINDEL_count = (String)prioritized_SNVINDEL.get(sampleName);
							SNVINDEL.add(new Double(SNVINDEL_count.split("\t")[2]));
							String line = (String)filtered_immune_score.get(sampleName);
							String[] split_line = line.split("\t");
							immune_score.add(new Double(split_line[i]));
						}						
					}
					double[] values1_array = MathTools.convertListDouble2Double(SNVINDEL);
					double[] values2_array = MathTools.convertListDouble2Double(immune_score);
					if (values1_array.length > 0 && values2_array.length > 0) {
						//System.out.println(values1_array.length);
						//System.out.println(values2_array.length);
						double spearmanRank = MathTools.SpearmanRank(values1_array, values2_array);
						
						out_disease_spearman.write("\t" + spearmanRank);
					} else {
						out_disease_spearman.write("\t0.0");
					}
				}
				out_disease_spearman.write("\n");
			}				
			out_disease_spearman.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
