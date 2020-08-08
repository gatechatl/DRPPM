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
public class JinghuiZhangCombineImmuneScoresWithSCNASpearman {

	
	public static void main(String[] args) {
		
		try {
			
			
			
			/*
			String outputFile_ssGSEA = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\SpearmanCorrelMutationBurden\\pcgp_immune_ssGSEA_filtered2SCNA.txt";
			FileWriter fwriter = new FileWriter(outputFile_ssGSEA);
			BufferedWriter out = new BufferedWriter(fwriter);
			*/
			/*
			String outputFile_SCNA_Count = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\SpearmanCorrelMutationBurden\\PCGP_TARGET_SCNA_Table_Prioritized_Filtered.txt";
			FileWriter fwriter_scna = new FileWriter(outputFile_SCNA_Count);
			BufferedWriter out_scna = new BufferedWriter(fwriter_scna);
			*/
						
			String[] type = {"","TotalCNVCount","whole_chr_change","chr_arm_level_change","focal_change","small_change","TotalLOHCount","whole_chr_loh_change","chr_arm_level_loh_change","focal_loh_change","small_loh_change"};
			for (int index = 1; index < type.length; index++) {
				
				String outputFile_Disease_Separman = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\SpearmanCorrelMutationBurden\\PCGP_TARGET_SCNA_Table_Disease_Spearman_" + type[index] + ".txt";
				FileWriter fwriter_disease_spearman = new FileWriter(outputFile_Disease_Separman);
				BufferedWriter out_disease_spearman = new BufferedWriter(fwriter_disease_spearman);
				
				HashMap map_immune_score = new HashMap();
				String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\pcgp_immune_ssGSEA.txt"; 		
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String immune_score_header = in.readLine();
				//out.write(immune_score_header + "\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					map_immune_score.put(split[0], str);
				}
				in.close();
				
				HashMap prioritized_scna = new HashMap();
				inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PCGP_TARGET_ComprehensiveTable.txt"; 		
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				String scna_header = in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					prioritized_scna.put(split[0], str);
				}
				in.close();
				
				int num_immune_scores = 0; 
				HashMap filtered_immune_score = new HashMap();
				Iterator itr = map_immune_score.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					if (prioritized_scna.containsKey(sampleName)) {
						String line = (String)map_immune_score.get(sampleName);
						String[] split = line.split("\t");
						num_immune_scores = split.length - 1;
						filtered_immune_score.put(sampleName, line);
						//out.write(line + "\n");
					}
				}
				//out.close();
				
				
				//out_scna.write("SampleName\tSCNA\n");
				itr = prioritized_scna.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					if (map_immune_score.containsKey(sampleName)) {
						String line = (String)prioritized_scna.get(sampleName);
						String[] split = line.split("\t");
						//out_scna.write(split[0] + "\t" + split[2] + "\n");
					}
				}
				//out_scna.close();
				
				out_disease_spearman.write("ImmuneName");
				String[] diseases = {"SJLGG", "SJEPD", "SJNBL", "SJWLM", "SJHGG", "SJRHB", "SJOS", "SJACT"};
				for (String disease: diseases) {
					out_disease_spearman.write("\t" + disease);
				}
				out_disease_spearman.write("\n");
				for (int i = 1; i < num_immune_scores + 1; i++) {
					out_disease_spearman.write(immune_score_header.split("\t")[i]);
					for (String disease: diseases) {
	
						LinkedList scna = new LinkedList();
						LinkedList immune_score = new LinkedList();
						itr = filtered_immune_score.keySet().iterator();
						while (itr.hasNext()) {
							String sampleName = (String)itr.next();
							if (sampleName.contains(disease)) {
								String scna_count = (String)prioritized_scna.get(sampleName);
								scna.add(new Double(scna_count.split("\t")[index]));
								String line = (String)filtered_immune_score.get(sampleName);
								String[] split_line = line.split("\t");
								immune_score.add(new Double(split_line[i]));
							}						
						}
						double[] values1_array = MathTools.convertListDouble2Double(scna);
						double[] values2_array = MathTools.convertListDouble2Double(immune_score);
						double spearmanRank = MathTools.SpearmanRank(values1_array, values2_array);
						out_disease_spearman.write("\t" + spearmanRank);
					}
					out_disease_spearman.write("\n");
				}				
				out_disease_spearman.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
