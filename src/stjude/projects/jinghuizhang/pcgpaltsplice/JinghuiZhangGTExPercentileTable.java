package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class JinghuiZhangGTExPercentileTable {

	public static void main(String[] args) {
		
		try {
			
			LinkedList list = new LinkedList();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\GTEx_Analysis_2016-01-15_v7_RNASeQCv1.1.8_gene_tpm.gct";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			in.readLine();
			while (in.ready()) {				
				String str = in.readLine();
				String[] split = str.split("\t");
				double count = new Double(split[2]);
				if (count > 1) {
					list.add(count);
				}
			}
			in.close();
			double FN1_EBD_fpkm = 20;
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
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
