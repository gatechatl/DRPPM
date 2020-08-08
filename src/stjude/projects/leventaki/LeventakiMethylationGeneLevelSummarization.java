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

public class LeventakiMethylationGeneLevelSummarization {

	
	public static void main(String[] args) {
		
		try {
			String inputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_108399_methylation_table_appendGeneInfo_normalized.txt";
			String outputFile_TSS = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_Methylation_normalized_GeneSummary_Mvalue_TSS_20180807.txt";
			String outputFile_Body = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_Methylation_normalized_GeneSummary_Mvalue_Body_20180807.txt";
			String outputFile_UTR5 = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_Methylation_normalized_GeneSummary_Mvalue_UTR5_20180807.txt";
			String outputFile_UTR3 = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_Methylation_normalized_GeneSummary_Mvalue_UTR3_20180807.txt";
			
			
			HashMap tss = new HashMap();
			HashMap body = new HashMap();
			HashMap utr5 = new HashMap();
			HashMap utr3 = new HashMap();
			HashMap geneName = new HashMap();
			HashMap sampleNames = new HashMap();
			FileWriter fwriter_tss = new FileWriter(outputFile_TSS);
			BufferedWriter out_tss = new BufferedWriter(fwriter_tss);
			
			FileWriter fwriter_body = new FileWriter(outputFile_Body);
			BufferedWriter out_body = new BufferedWriter(fwriter_body);
						
			FileWriter fwriter_utr5 = new FileWriter(outputFile_UTR5);
			BufferedWriter out_utr5 = new BufferedWriter(fwriter_utr5);
						
			FileWriter fwriter_utr3 = new FileWriter(outputFile_UTR3);
			BufferedWriter out_utr3 = new BufferedWriter(fwriter_utr3);
						
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
					geneName.put(refseq_geneName, refseq_geneName);
					if (!fail) {
						for (int i = 1; i < split.length; i++) {							
							if (header_split[i].contains("AVG_Beta")) {
								String newName = header_split[i].split("-")[0].split("_")[0] + "_" + header_split[i].split("-")[0].split("_")[1];
								if (header_split[i].split("-")[0].split("_")[1].contains("D")) {
									String key = newName + "\t" + refseq_geneName;
									
									sampleNames.put(newName, newName);
									if (!split[i].equals("NA") && !split[i].equals("")) {
										double beta = new Double(split[i]);
										double M = Beta2M(beta);
										if (bodyinfo.contains("TSS")) {
											if (tss.containsKey(key)) {
												LinkedList list = (LinkedList)tss.get(key);
												list.add("" + M);
												tss.put(key, list);										
											} else {
												LinkedList list = new LinkedList();
												list.add("" + M);
												tss.put(key, list);
											}
										}
										if (bodyinfo.contains("Body")) {
											if (body.containsKey(key)) {
												LinkedList list = (LinkedList)body.get(key);
												list.add("" + M);
												body.put(key, list);										
											} else {
												LinkedList list = new LinkedList();
												list.add("" + M);
												body.put(key, list);
											}
										} 
										if (bodyinfo.contains("5'UTR")) {
											if (utr5.containsKey(key)) {
												LinkedList list = (LinkedList)utr5.get(key);
												list.add("" + M);
												utr5.put(key, list);										
											} else {
												LinkedList list = new LinkedList();
												list.add("" + M);
												utr5.put(key, list);
											}
										}
										if (bodyinfo.contains("3'UTR")) {
											if (utr3.containsKey(key)) {
												LinkedList list = (LinkedList)utr3.get(key);
												list.add("" + M);
												utr3.put(key, list);										
											} else {
												LinkedList list = new LinkedList();
												list.add("" + M);
												utr3.put(key, list);
											}
										} 						
									}
								}
							}
						}
					}
				}
			}
			in.close();
			out_tss.write("GN");
			out_body.write("GN");
			out_utr5.write("GN");
			out_utr3.write("GN");
			
			String[] manual_sampleName = {"SJALCL014725_D1","SJALCL014728_D1","SJALCL017851_D1","SJALCL017852_D1","SJALCL017855_D1","SJALCL017856_D1","SJALCL017857_D1","SJALCL017858_D1","SJALCL017862_D1","SJALCL017863_D1","SJALCL017864_D1","SJALCL045610_D1","SJALCL045611_D1","SJALCL045615_D1","SJALCL045616_D1","SJALCL045620_D1","SJALCL045622_D1","SJALCL045625_D1","SJALCL045634_D1","SJALCL014724_D1","SJALCL017844_D1","SJALCL017845_D1","SJALCL017859_D1","SJALCL045621_D1","SJALCL045635_D1","SJALCL014727_D1","SJALCL017846_D1","SJALCL017847_D1","SJALCL017853_D1","SJALCL017860_D1","SJALCL017861_D1","SJALCL045612_D1","SJALCL045613_D1","SJALCL045614_D1","SJALCL045627_D1","SJALCL045629_D1","SJALCL045631_D1"};
			//Iterator itr1 = sampleNames.keySet().iterator();
			//while (itr1.hasNext()) {
			for (String sampleName: manual_sampleName) {
				//String sampleName = (String)itr1.next();
				out_tss.write("\t" + sampleName);
				out_body.write("\t" + sampleName);
				out_utr5.write("\t" + sampleName);
				out_utr3.write("\t" + sampleName);
			}
			out_tss.write("\tGene\tBodyInfo\n");
			out_body.write("\tGene\tBodyInfo\n");
			out_utr5.write("\tGene\tBodyInfo\n");
			out_utr3.write("\tGene\tBodyInfo\n");
			
				
			Iterator itr2 = geneName.keySet().iterator();
			while (itr2.hasNext()) {
				String gene = (String)itr2.next();
				
				out_tss.write(gene);
				out_body.write(gene);
				out_utr5.write(gene);
				out_utr3.write(gene);
				
				//itr1 = sampleNames.keySet().iterator();
				//while (itr1.hasNext()) {
				//	String sampleName = (String)itr1.next();
				for (String sampleName: manual_sampleName) {
					String key = sampleName + "\t" + gene;
					if (tss.containsKey(key)) {
						LinkedList list_tss = (LinkedList)tss.get(key);
						double[] tss_double = MathTools.convertListStr2Double(list_tss);
						double tss_mean = MathTools.mean(tss_double);
						out_tss.write("\t" + tss_mean);
					} else {
						out_tss.write("\tNA");
					}
				}
				out_tss.write("\t" + gene + "\tTSS\n");
				
				out_body.write(gene);				
				//itr1 = sampleNames.keySet().iterator();
				//while (itr1.hasNext()) {
				//	String sampleName = (String)itr1.next();
				for (String sampleName: manual_sampleName) {
					String key = sampleName + "\t" + gene;
					if (body.containsKey(key)) {
						LinkedList list_body = (LinkedList)body.get(key);
						double[] body_double = MathTools.convertListStr2Double(list_body);
						double body_mean = MathTools.mean(body_double);
						out_body.write("\t" + body_mean);			
					} else {
						out_body.write("\tNA");
					}
				}
				out_body.write("\t" + gene + "\tBody\n");
				
				out_utr5.write(gene);				
				//itr1 = sampleNames.keySet().iterator();
				//while (itr1.hasNext()) {
				//	String sampleName = (String)itr1.next();
				for (String sampleName: manual_sampleName) {
					String key = sampleName + "\t" + gene;			
					if (utr5.containsKey(key)) {
						LinkedList list_utr5 = (LinkedList)utr5.get(key);
						double[] utr5_double = MathTools.convertListStr2Double(list_utr5);
						double utr5_mean = MathTools.mean(utr5_double);
						out_utr5.write("\t" + utr5_mean);
					} else {
						out_utr5.write("\tNA");
					}
				}
				out_utr5.write("\t" + gene + "\t5'UTR\n");
				
				out_utr3.write(gene);				
				//itr1 = sampleNames.keySet().iterator();
				//while (itr1.hasNext()) {
				//	String sampleName = (String)itr1.next();
				for (String sampleName: manual_sampleName) {
					String key = sampleName + "\t" + gene;		
					if (utr3.containsKey(key)) {
						LinkedList list_utr3 = (LinkedList)utr3.get(key);
						double[] utr3_double = MathTools.convertListStr2Double(list_utr3);
						double utr3_mean = MathTools.mean(utr3_double);
						out_utr3.write("\t" + utr3_mean);
					} else {
						out_utr3.write("\tNA");
					}
				}
				out_utr3.write("\t" + gene + "\t3'UTR\n");
				
				/*
				 * 
					
					
					
				 */
			}
			out_tss.close();
			out_body.close();
			out_utr5.close();
			out_utr3.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double Beta2M(double beta) {
		return MathTools.log2((beta)/(1 - beta) + 0.0000000001);
	}
}
