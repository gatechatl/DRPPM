package stjude.projects.jinghuizhang.target.usp7mutvswt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangExtractAnnotatedBoxplotSamples {

	public static void main(String[] args) {
		
		try {
			HashMap meta = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\TracyWeightedActivity\\boxplot\\nonETP\\selective_subtype_e2a_heb_activity.txt";
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String meta_header = in.readLine();
			String[] split_meta_header = meta_header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				meta.put(split[1].replaceAll("\\.", "_"), split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4] + "\t" + split[5] + "\t" + split[6] + "\t" + split[7]);
			}
			in.close();
			
			
			//String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\NetBIDactivity_mean_all_tissues\\NetBID_activity_mean_all_target_Meta.txt";					
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\NetBIDactivity_mean_all_tissues\\NetBID_activity_mean_all_target_zscore_Meta.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write(split_meta_header[1] + "\t" + split_meta_header[2] + "\t" + split_meta_header[3] + "\t" + split_meta_header[4] + "\t" + split_meta_header[5] + "\t" + split_meta_header[6] + "\t" + split_meta_header[7]);
			//inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\NetBIDactivity_mean_all_tissues\\NetBID_activity_mean_all_target_transpose.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\NetBIDactivity_mean_all_tissues\\NetBID_activity_mean_all_target_zscore_transpose.txt";
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String sample_header = in.readLine();
			out.write("\t" + sample_header + "\n");
			String[] split_sample_header = sample_header.split("\t");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0].split("\\.TARGET")[0];
				if (meta.containsKey(sampleName)) {
					out.write(meta.get(sampleName) + "\t" + str + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
