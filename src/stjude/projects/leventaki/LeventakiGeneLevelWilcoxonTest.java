package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import statistics.general.MathTools;

public class LeventakiGeneLevelWilcoxonTest {

	
	public static void main(String[] args) {
		
		try {
			String[] tags = {"TSS", "Body", "UTR3", "UTR5"};
			for (String tag: tags) {
				String inputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_Methylation_normalized_GeneSummary_Mvalue_" + tag + "_20180807.txt";
				String outputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_Methylation_normalized_GeneSummary_Mvalue_" + tag + "_pval_20180807.txt";
				
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				
				String[] group1 = {"SJALCL014725_D1","SJALCL014728_D1","SJALCL017851_D1","SJALCL017852_D1","SJALCL017855_D1","SJALCL017856_D1","SJALCL017857_D1","SJALCL017858_D1","SJALCL017862_D1","SJALCL017863_D1","SJALCL017864_D1","SJALCL045610_D1","SJALCL045611_D1","SJALCL045615_D1","SJALCL045616_D1","SJALCL045620_D1","SJALCL045622_D1","SJALCL045625_D1","SJALCL045634_D1","SJALCL014724_D1"};
				String[] group2 = {"SJALCL014727_D1","SJALCL017846_D1","SJALCL017847_D1","SJALCL017853_D1","SJALCL017860_D1","SJALCL017861_D1","SJALCL045612_D1","SJALCL045613_D1","SJALCL045614_D1","SJALCL045627_D1","SJALCL045629_D1","SJALCL045631_D1"};
				HashMap group1_map = new HashMap();
				for (String g: group1) {
					group1_map.put(g, g);
				}
				HashMap group2_map = new HashMap();
				for (String g: group2) {
					group2_map.put(g, g);
				}
				WilcoxonSignedRankTest wilcoxTest = new WilcoxonSignedRankTest();
				MannWhitneyUTest mannWhitney = new MannWhitneyUTest();
				
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
				String header = in.readLine();
				String[] header_split = header.split("\t");
				out.write(header + "\tGroup1_Status\tGroup2_Status\tGroup1_Mean\tGroup2_Mean\tG1_minus_G2\tWilcox_Pval\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					LinkedList g1_list = new LinkedList();
					LinkedList g2_list = new LinkedList();
					for (int i = 1; i < split.length; i++) {
						
						if (group1_map.containsKey(header_split[i])) {
							if (!split[i].equals("NA")) {
								g1_list.add(split[i]);
							}
						}
						
						if (group2_map.containsKey(header_split[i])) {
							if (!split[i].equals("NA")) {
								g2_list.add(split[i]);
							}
						}
					}
					
					double[] group1_vals = MathTools.convertListStr2Double(g1_list);
					double[] group2_vals = MathTools.convertListStr2Double(g2_list);
					double group1_mean = MathTools.mean(group1_vals);
					double group2_mean = MathTools.mean(group2_vals);
					
					double g1_minus_g2 = group1_mean - group2_mean;
					if (group1_vals.length > 0 && group2_vals.length > 0) {
						String group1_status = "Medium_Range";
						String group2_status = "Medium_Range";
						if (group1_mean >= 2.0) {
							group1_status = "Hyper-methylated";
						}
						if (group1_mean <= -2.0) {
							group1_status = "Hypo-methylated";
						}
						
						if (group2_mean >= 2.0) {
							group2_status = "Hyper-methylated";
						}
						if (group2_mean <= -2.0) {
							group2_status = "Hypo-methylated";
						}
						
						double pval = mannWhitney.mannWhitneyUTest(group1_vals, group2_vals);
						out.write(str + "\t" + group1_status + "\t" + group2_status + "\t" + group1_mean + "\t" + group2_mean + "\t" + g1_minus_g2 + "\t" + pval + "\n");
					}
				}
				
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
