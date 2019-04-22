package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class JinghuiZhangCalculatePCGPFPKMTarget {
	public static String description() {
		return "Generate FPKM table";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[sampleFile] [path] [keyword] [exon_length] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String sampleFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\PCGP_sample.txt";
			String path = args[1]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\Comprehensive\\";
			String keyword = args[2]; //"ENSG00000115414.14_22";
			double exon_length = new Double(args[3]);//273.0;
			String outputFile = args[4]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\PCGP_RNAseq\\QC\\RNAseQC\\FN1_ENSG00000115414.14_22_FPKM.txt";
			
			HashMap pass_cutoff = new HashMap();
			HashMap type_count = new HashMap();
			
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write("Name\tFPKM_Norm\tcount\tTotal\tExonLength\tFPKM\tType\n");
			
			FileInputStream fstream = new FileInputStream(sampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0].trim();
				String type = sampleName.split("0")[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
				
				String exonFile = path + "/" + sampleName + "/" + sampleName + "/" + sampleName + ".metrics.tmp.txt.intronReport.txt_exonOnly.txt";
				String rpkmFile = path + "/" + sampleName + "/" + "genes.rpkm.gct";
				System.out.println(exonFile);
				double total = 0.0;
				double fn1_splice_count = 0.0;
				File f = new File(exonFile);
				if (f.exists()) {
					
					LinkedList list = new LinkedList();
					FileInputStream fstream2 = new FileInputStream(exonFile);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					header = in2.readLine();
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split2 = str2.split("\t");
						double count = new Double(split2[3]);
						total += count;
						if (split2[0].equals(keyword)) {
							fn1_splice_count = count;
						}
						double norm = (count + 0.0001) * 1000 / new Double(split2[4]);
						//list.add(norm);
					}
					in2.close();
					
					FileInputStream fstream3 = new FileInputStream(rpkmFile);
					DataInputStream din3 = new DataInputStream(fstream3);
					BufferedReader in3 = new BufferedReader(new InputStreamReader(din3));
					header = in3.readLine();
					header = in3.readLine();
					header = in3.readLine();
					while (in3.ready()) {
						String str2 = in3.readLine();
						String[] split2 = str2.split("\t");
						
						double count = new Double(split2[2]);
						if (count > 1) {
							list.add(count);
						}
					}
					in3.close();
					double FN1_EBD_fpkm = (((fn1_splice_count + 0.0001) * 1000 * 1000000) / (exon_length * total));
					double[] values = MathTools.convertListDouble2Double(list);
					java.util.Arrays.sort(values);
					int p1 = values.length / 10 - 1;
					int p2 = values.length * 2 / 10 - 1;
					int p3 = values.length * 3 / 10 - 1;
					int p4 = values.length * 4 / 10 - 1;
					int p5 = values.length * 5 / 10 - 1;
					int p6 = values.length * 6 / 10 - 1;
					int p7 = values.length * 7 / 10 - 1;
					int p8 = values.length * 8 / 10 - 1;
					int p9 = values.length * 9 / 10 - 1;
					int p10 = values.length * 10 / 10 - 1;

					double p0_val = values[0];
					double p1_val = values[p1];
					double norm_quantile = 0.0;
					if (FN1_EBD_fpkm > p0_val && FN1_EBD_fpkm < p1_val) {
						norm_quantile = 0.05;
					}
					double p2_val = values[p2];
					if (FN1_EBD_fpkm > p1_val && FN1_EBD_fpkm < p2_val) {
						norm_quantile = 0.15;
					}
					double p3_val = values[p3];
					if (FN1_EBD_fpkm > p2_val && FN1_EBD_fpkm < p3_val) {
						norm_quantile = 0.25;
					}
					double p4_val = values[p4];
					if (FN1_EBD_fpkm > p3_val && FN1_EBD_fpkm < p4_val) {
						norm_quantile = 0.35;
					}
					double p5_val = values[p5];
					if (FN1_EBD_fpkm > p4_val && FN1_EBD_fpkm < p5_val) {
						norm_quantile = 0.45;
					}
					double p6_val = values[p6];
					if (FN1_EBD_fpkm > p5_val && FN1_EBD_fpkm < p6_val) {
						norm_quantile = 0.55;
					}
					double p7_val = values[p7];
					if (FN1_EBD_fpkm > p6_val && FN1_EBD_fpkm < p7_val) {
						norm_quantile = 0.65;
					}
					double p8_val = values[p8];
					if (FN1_EBD_fpkm > p7_val && FN1_EBD_fpkm < p8_val) {
						norm_quantile = 0.75;
					}
					double p9_val = values[p9];
					if (FN1_EBD_fpkm > p8_val && FN1_EBD_fpkm < p9_val) {
						norm_quantile = 0.85;
					}
					double p10_val = values[p10];
					if (FN1_EBD_fpkm > p9_val) {
						norm_quantile = 0.95;
					}
					if (FN1_EBD_fpkm == p10_val) {
						norm_quantile = 1.0;
					}
					System.out.println("FN1_EDB FPKM: " + norm_quantile + "\t" + FN1_EBD_fpkm);
					System.out.println(p0_val + "\t" + p1_val + "\t" + p2_val + "\t" + p3_val + "\t" + p4_val + "\t" + p5_val + "\t" + p6_val + "\t" + p7_val + "\t" + p8_val + "\t" + p9_val + "\t" + p10_val);										
					
					out.write(sampleName + "\t" + norm_quantile + "\t" + fn1_splice_count + "\t" + total + "\t" + exon_length + "\t" + (((fn1_splice_count + 0.0001) * 1000 * 1000000) / (exon_length * total)) + "\t" + type + "\n");
					out.flush();
					
					if (type_count.containsKey(type)) {
						double count = (Double)type_count.get(type);
						count = count + 1;
						type_count.put(type, count);
					} else {
						type_count.put(type, 1.0);
					}						
					if (FN1_EBD_fpkm > 20) {
						if (pass_cutoff.containsKey(type)) {
							double count = (Double)pass_cutoff.get(type);
							count = count + 1;
							pass_cutoff.put(type, count);
						} else {
							pass_cutoff.put(type, 1.0);
						}						
					}
				}
			}
			in.close();
			out.close();
			
			Iterator itr = type_count.keySet().iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				double all_count = (Double)type_count.get(type);
				double hit_count = 0.0;
				if (pass_cutoff.containsKey(type)) {
					hit_count = (Double)pass_cutoff.get(type);
				}
				System.out.println(type + "\t" + hit_count / all_count + "\t" + hit_count + "\t" + all_count);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
