package stjude.projects.xiangchen;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;
import statistics.general.Methylation;

public class XiangChenGrabTopVariableGenes {

	public static String description() {
		return "Xiang Chen File Grab Top Variable Genes based on MAD.";
	}
	public static String type() {
		return "XIANGCHEN";
	}
	public static String parameter_info() {
		return "[inputFile] [topN] [metaInformationFile] [outputFileWithMAD] [outputFileTopNonly]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_108399_methylation_table.txt";
			int NTopGene = new Integer(args[1]); //2000;
			String metaInformationFile = args[2];
			HashMap filter_id2snp_sexchr = new HashMap();
			HashMap id2result = new HashMap();
			FileInputStream fstream = new FileInputStream(metaInformationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				id2result.put(split[0], str);
				if (split.length > 43) {
					String name = split[1];
					String snp_id = split[43];
					String chr = split[11];
					if (!snp_id.equals("") || chr.toUpperCase().equals("X") || chr.toUpperCase().equals("Y")) {
						filter_id2snp_sexchr.put(name, snp_id);
						//System.out.println(chr + "\t" + snp_id);
					}
				}
			}
			String outputFile = args[3]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Normal_Leventaki_850K_methylation_table_TopVariableProbes.txt";
			
			String outputFile_short = args[4]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Combined_Shorter_Normal_Leventaki_850K_methylation_table_Top" + NTopGene + "VariableProbes.txt";
			//double[] list1 = {6, 8, 2, 4, 4, 5};
			//double[] list2 = {7, 10, 4, 3, 5, 6};
			//System.out.println(mannWhitney.mannWhitneyU(list1, list2));
			//System.out.println(mannWhitney.mannWhitneyUTest(list1, list2));
			//System.out.println(wilcoxTest.wilcoxonSignedRankTest(list1, list2, false));
			//System.exit(0);;
			
			
			//String[] group2 = {"SJALCL045614", "SJALCL014727", "SJALCL045613", "SJALCL017847", "SJALCL045629", "SJALCL017846", "SJALCL017858", "SJALCL045627", "SJALCL045616", "SJALCL017851", "SJALCL014725", "SJALCL045620", "SJALCL045611", "SJALCL017856", "SJALCL014724", "SJALCL045615"};
			//String[] group1 = {"SJNORM016314_G1-Thy5_34", "SJNORM016314_G3-Thy5_1a_3_48DP", "SJNORM016314_G4-Thy5_1a_3_48SP", "SJNORM016314_G2-Thy5_1a_48DP", "SJNORM016314_G5-Thy5_3_48SP", "SJNORM018231_G1-Thy9_34", "SJNORM018231_G3-Thy9_1a_3_48DP", "SJNORM018231_G4-Thy9_1a_3_48SP", "SJNORM018231_G2-Thy9_1a_48DP"};
			//
			//String[] group1_thy9 = {"SJNORM018231_G1-Thy9_34", "SJNORM018231_G3-Thy9_1a_3_48DP", "SJNORM018231_G4-Thy9_1a_3_48SP", "SJNORM018231_G2-Thy9_1a_48DP"};
			
			//String[] group1_thy5 = {"SJNORM016314_G1-Thy5_34", "SJNORM016314_G3-Thy5_1a_3_48DP", "SJNORM016314_G4-Thy5_1a_3_48SP", "SJNORM016314_G2-Thy5_1a_48DP", "SJNORM016314_G5-Thy5_3_48SP"};
			
			LinkedList group1_thy9_list = new LinkedList();
			//LinkedList group1_thy5_list = new LinkedList();
			//LinkedList group2_list = new LinkedList();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_limit = new FileWriter(outputFile_short);
			BufferedWriter out_limit = new BufferedWriter(fwriter_limit);
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			String[] header_split = in.readLine().split("\t");
			for (int i = 1; i < header_split.length; i++) {
				int hit = -1;
				//for (int j = 0; j < group1_thy9.length; j++) {
				//	if (header_split[i].contains(group1_thy9[j]) && header_split[i].contains("AVG_Beta")) {
				group1_thy9_list.add(i);
				//	}
					
				//}
				/*for (int j = 0; j < group1_thy5.length; j++) {
					if (header_split[i].contains(group1_thy5[j]) && header_split[i].contains("AVG_Beta")) {
						group1_thy5_list.add(i);
					}
					
				}*/	
			}
			
			
			out_limit.write("Probe");
			out.write("Probe");
			Iterator itr1 = group1_thy9_list.iterator();
			while (itr1.hasNext()) {
				int index = (Integer)itr1.next();
				out.write("\t" + header_split[index]);
				out_limit.write("\t" + header_split[index]);
			}
			
			LinkedList mad_score_list = new LinkedList();
			out.write("\tMADScore\n");
			out_limit.write("\n");
			//out_limit.write("\tMADScore\n");
			while (in.ready()) {
				String str = in.readLine();
				//System.out.println(str);
				String[] split = str.split("\t");
				String line = "";
				//out.write(split[0]);
				line += split[0];
				LinkedList group1_thy9_val = new LinkedList();
				LinkedList group1_thy9_val_avg = new LinkedList();
				boolean skip = false;
				//if (!str.contains("NaN") && !filter_id2snp_sexchr.containsKey(split[0])) {
				if (!str.contains("NaN")) {
					itr1 = group1_thy9_list.iterator();
					while (itr1.hasNext()) {
						int index = (Integer)itr1.next();
						if (!split[index].trim().equals("") && !split[index].trim().equals("NA")) {
							double M = Methylation.Beta2M(new Double(split[index]));
							line += "\t" + M;						
							group1_thy9_val.add(M);
							group1_thy9_val_avg.add(M + "");
						} else {
							line += "\tNaN";
							skip = true;
						}
					}
					if (!skip) {
						if (group1_thy9_val.size() > 1) {
							double mad_thy9_score = MathTools.mad(group1_thy9_val);
							double[] group1_thy9_vals = MathTools.convertListStr2Double(group1_thy9_val_avg);
							double avg_thy9 = MathTools.mean(group1_thy9_vals);
																	
							double avg_mad_score = mad_thy9_score;
							//if (avg > 0) {
								mad_score_list.add(avg_mad_score);
								out.write(line);
								out.write("\t" + avg_mad_score + "\n");
							//}
						}
					}
				}
			}
			in.close();
			//LinkedList mad_score_list = new LinkedList();
			Object[] mad_score_array = mad_score_list.toArray();
			Arrays.sort(mad_score_array);
			
			LinkedList new_mad_score_array = new LinkedList();
			double topMAD = (Double)mad_score_array[mad_score_array.length - NTopGene];
			//for (int i = 0; i < mad_score_array.length - 1000; i++) {
				//System.out.println(mad_score_array[i]);
				//new_mad_score_array.addLast(mad_score_array[i]);
				// 
			//}
						
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				//System.out.println(str);
				String[] split = str.split("\t");
				boolean skip = false;
				//if (!str.contains("NaN") && !filter_id2snp_sexchr.containsKey(split[0])) {
				if (!str.contains("NaN")) {
					String line = "";
					//out.write(split[0]);
					line += split[0];
					
					LinkedList group1_thy9_val = new LinkedList();
					LinkedList group1_thy9_val_avg = new LinkedList();
					itr1 = group1_thy9_list.iterator();
					while (itr1.hasNext()) {
						int index = (Integer)itr1.next();
						if (!split[index].trim().equals("") && !split[index].trim().equals("NA")) {
							double M = Methylation.Beta2M(new Double(split[index]));
							line += "\t" + M;						
							group1_thy9_val.add(M);
							group1_thy9_val_avg.add(M + "");
						} else {
							line += "\tNaN";
							skip = true;
						}
					}
					if (!skip) {
						if (group1_thy9_val.size() > 1) {
							double mad_thy9_score = MathTools.mad(group1_thy9_val);
							
							double[] group1_thy9_vals = MathTools.convertListStr2Double(group1_thy9_val_avg);
		
							double group1_thy9_avg = MathTools.mean(group1_thy9_vals);
		
							
							itr1 = group1_thy9_list.iterator();
							while (itr1.hasNext()) {
								int index = (Integer)itr1.next();
								if (!split[index].trim().equals("") && !split[index].trim().equals("NA")) {
									double M = Methylation.Beta2M(new Double(split[index]));
									//line += "\t" + (M - diff_thy9);						
									//group1_thy9_val.add(M);
									//group1_thy9_val_avg.add(M + "");
								} else {
									//line += "\tNaN";
								}
							}					
							
							double avg_mad_score = mad_thy9_score;
							//if (group1_avg > 0 && group2_avg > 0) {
								if (avg_mad_score >= topMAD) {
									if (id2result.containsKey(split[0])) {
										//System.out.println(id2result.get(split[0]));
									}
									out_limit.write(line);
									out_limit.write("\n");
									//out_limit.write("\t" + mad_score + "\n");
								}
							//}
						}
					}
				}
			}
			out.close();
			out_limit.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
