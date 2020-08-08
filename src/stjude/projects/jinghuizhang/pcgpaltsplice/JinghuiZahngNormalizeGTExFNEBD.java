package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZahngNormalizeGTExFNEBD {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap type_count = new HashMap();
			HashMap pass_cutoff = new HashMap();
			//String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\FN1_ED-B_Domain\\GTEx_FN1_ED-B_fpkm_norm_boxplot.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\GTEx_FN1_ED-B_fpkm_norm_boxplot.txt_back";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FN1_ED-B_Domain\\GTEx_FN1_ED-B_fpkm_boxplot.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double FN1_EBD_fpkm = new Double(split[1]);
				
				double p0_val = 1.001;
				double p1_val = 1.577;
				double p2_val = 2.583;
				double p3_val = 4.437;
				double p4_val = 7.443;
				double p5_val = 11.95;
				double p6_val = 18.22;
				double p7_val = 27.7;
				double p8_val = 43.18;
				double p9_val = 81.05;
				double p10_val = 19660.0;				

				double norm_quantile = 0.0;
				if (FN1_EBD_fpkm > p0_val && FN1_EBD_fpkm < p1_val) {
					norm_quantile = 0.05;
				}
				if (FN1_EBD_fpkm > p1_val && FN1_EBD_fpkm < p2_val) {
					norm_quantile = 0.15;
				}
				if (FN1_EBD_fpkm > p2_val && FN1_EBD_fpkm < p3_val) {
					norm_quantile = 0.25;
				}
				if (FN1_EBD_fpkm > p3_val && FN1_EBD_fpkm < p4_val) {
					norm_quantile = 0.35;
				}
				if (FN1_EBD_fpkm > p4_val && FN1_EBD_fpkm < p5_val) {
					norm_quantile = 0.45;
				}
				if (FN1_EBD_fpkm > p5_val && FN1_EBD_fpkm < p6_val) {
					norm_quantile = 0.55;
				}
				if (FN1_EBD_fpkm > p6_val && FN1_EBD_fpkm < p7_val) {
					norm_quantile = 0.65;
				}
				if (FN1_EBD_fpkm > p7_val && FN1_EBD_fpkm < p8_val) {
					norm_quantile = 0.75;
				}
				if (FN1_EBD_fpkm > p8_val && FN1_EBD_fpkm < p9_val) {
					norm_quantile = 0.85;
				}
				if (FN1_EBD_fpkm > p9_val) {
					norm_quantile = 0.95;
				}
				if (FN1_EBD_fpkm == p10_val) {
					norm_quantile = 1.0;
				}
				out.write(split[0] + "\t" + norm_quantile + "\t" + split[2] + "\n");
				if (type_count.containsKey(split[2])) {
					double count = (Double)type_count.get(split[2]);
					count = count + 1;
					type_count.put(split[2], count);
				} else {
					type_count.put(split[2], 1.0);
				}
				if (FN1_EBD_fpkm > 20) {
					if (pass_cutoff.containsKey(split[2])) {
						double count = (Double)pass_cutoff.get(split[2]);
						count = count + 1;
						pass_cutoff.put(split[2], count);
					} else {
						pass_cutoff.put(split[2], 1.0);
					}
				}
				
			}
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
