package stjude.projects.leventaki;

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

public class LeventakiMethylationBetaDistributionGroup1Group2 {

	
	public static void main(String[] args) {
		
		try {
			String inputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_108399_methylation_table_appendGeneInfo_normalized.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_108399_methylation_table_appendGeneInfo_normalized_meanBeta.txt";
			String outputFile2 = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_108399_methylation_table_appendGeneInfo_normalized_diff.txt";
			String[] group1 = {"SJALCL014725_D1","SJALCL014728_D1","SJALCL017851_D1","SJALCL017852_D1","SJALCL017855_D1","SJALCL017856_D1","SJALCL017857_D1","SJALCL017858_D1","SJALCL017862_D1","SJALCL017863_D1","SJALCL017864_D1","SJALCL045610_D1","SJALCL045611_D1","SJALCL045615_D1","SJALCL045616_D1","SJALCL045620_D1","SJALCL045622_D1","SJALCL045625_D1","SJALCL045634_D1","SJALCL014724_D1"};
			String[] group2 = {"SJALCL014727_D1","SJALCL017846_D1","SJALCL017847_D1","SJALCL017853_D1","SJALCL017860_D1","SJALCL017861_D1","SJALCL045612_D1","SJALCL045613_D1","SJALCL045614_D1","SJALCL045627_D1","SJALCL045629_D1","SJALCL045631_D1"};

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			HashMap group1_map = new HashMap();
			for (String g: group1) {
				group1_map.put(g, g);
			}
			HashMap group2_map = new HashMap();
			for (String g: group2) {
				group2_map.put(g, g);
			}
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String[] header_split = in.readLine().split("\t");
			int index_refseq_geneName = -1;
			int index_bodyinfo = -1;
			int index_SNP_minorallelefrequency = -1;
			for (int i = 0; i < header_split.length; i++) {
				if (header_split[i].equals("refseq_geneName")) {
					index_refseq_geneName = i;
				}
				if (header_split[i].equals("bodyinformation")) {
					index_bodyinfo = i;
				}
				if (header_split[i].equals("SNP_minorallelefrequency")) {
					index_SNP_minorallelefrequency = i;
				}
			}
			 
			out.write("ProbeName\tType\tBeta\n");
			out2.write("ProbeName\tDiff\tGroup1_mean\tGroup2_mean\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length >= 73) {
					String refseq_geneName = split[index_refseq_geneName];
					String bodyinfo = split[index_bodyinfo];
					
					String SNP_minorallelefrequency = split[index_SNP_minorallelefrequency];
					String[] split_SNP_minorallelefrequency = SNP_minorallelefrequency.split(";");
					boolean fail = false;
					for (String freq: split_SNP_minorallelefrequency) {
						if (new Double(freq) > 0.01) {
							fail = true;
						}
					}

					if (!fail) {
						LinkedList group1_str = new LinkedList();
						LinkedList group2_str = new LinkedList();
						for (int i = 1; i < split.length; i++) {

							if (header_split[i].contains("AVG_Beta")) {
								for (String g1: group1) {
									if (header_split[i].contains(g1)) {
										if (isNumeric(split[i])) {
											group1_str.add(split[i]);
										}
									}
								}
								for (String g2: group2) {
									if (header_split[i].contains(g2)) {
										if (isNumeric(split[i])) {
											group2_str.add(split[i]);
										}
									}
								}
							} // end if AVG_beta							
						} // for loop
						
						if (group1_str.size() > 0 && group2_str.size() > 0) {
							double group1_mean = MathTools.mean(MathTools.convertListStr2Double(group1_str));
							double group2_mean = MathTools.mean(MathTools.convertListStr2Double(group2_str));
							out.write(split[0] + "\tGroup1\t" + group1_mean + "\n");
							out.write(split[0] + "\tGroup2\t" + group2_mean + "\n");
							out2.write(split[0] + "\t" + (group1_mean - group2_mean) + "\t" + group1_mean + "\t" + group2_mean + "\n");
						}
					} // if pass filter
				}
			}
			in.close();
			out.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double Beta2M(double beta) {
		return MathTools.log2((beta)/(1 - beta) + 0.0000000001);
	}
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
