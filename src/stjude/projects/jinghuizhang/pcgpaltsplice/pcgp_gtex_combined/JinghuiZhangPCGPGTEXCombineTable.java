package stjude.projects.jinghuizhang.pcgpaltsplice.pcgp_gtex_combined;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * 
 * @author tshaw
 *
 */
public class JinghuiZhangPCGPGTEXCombineTable {

	
	public static void main(String[] args) {
		
		try {
			
			String inputGTExFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\GTEx_Tissue_2016-01-15_v7_RNASeQCv1.1.8_exon_median_reads_filter_cutoff.txt";
			String inputPCGPFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\PCGP_disease_median_exon_fpkm_filter_cutoff_20190303.txt";
			String inputCutoffFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\final_candidate.txt";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Combined_RNAseQC_Processed_Exon\\Combined_PCGP_GTEx_Exon_Quantification_Quartile_complete.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write("ID\tGTEx\tPCGP\n");
			HashMap gtex_avg_values = new HashMap();
			HashMap pcgp_avg_values = new HashMap();
			FileInputStream fstream = new FileInputStream(inputGTExFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].split("_")[0] + "_" + (new Integer(split[0].split("_")[1]) - 1); 
				double total = 0.0;
				for (int i = 1; i < split.length; i++) {
					double val = new Double(split[i]);
					total += val;
				}
				double avg = total / split.length;
				gtex_avg_values.put(name, avg);
			}
			in.close();			

			HashMap candidate = new HashMap();
			fstream = new FileInputStream(inputCutoffFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				candidate.put(split[0], split[0]);
			}
			in.close();
			
			
			fstream = new FileInputStream(inputPCGPFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double total = 0.0;
				for (int i = 1; i < split.length; i++) {
					double val = new Double(split[i]);
					total += val;
				}
				double avg = total / split.length;
				//pcgp_avg_values.put(split[0], avg);
				
				
				if (gtex_avg_values.containsKey(split[0])) {
					if (candidate.containsKey(split[0])) {
						out.write(split[0] + "\t" + gtex_avg_values.get(split[0]) + "\t" + avg + "\n");
					} else {
						//if (Math.random() < 0.2) {
							out.write(split[0] + "\t" + gtex_avg_values.get(split[0]) + "\t" + avg + "\n");
						//}
					}
				}
			}
			in.close();			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
