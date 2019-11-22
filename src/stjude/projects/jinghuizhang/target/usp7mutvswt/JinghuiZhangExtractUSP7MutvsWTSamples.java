package stjude.projects.jinghuizhang.target.usp7mutvswt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangExtractUSP7MutvsWTSamples {

	
	public static void main(String[] args) {
		try {
			
			HashMap usp7_wt = new HashMap();
			HashMap usp7_mut = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\USP7mut_vs_USP7wt_TTEST\\USP7wt.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String sampleName = str.split("_")[0].replaceAll("\\.", "_");
				usp7_wt.put(sampleName, sampleName);
				System.out.println(sampleName);
			}
			in.close();
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\USP7mut_vs_USP7wt_TTEST\\USP7mut.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String sampleName = str.split("_")[0].replaceAll("\\.", "_");
				usp7_mut.put(sampleName, sampleName);
				System.out.println(sampleName);
			}
			in.close();
			
			//String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\NetBIDactivity_mean_all_tissues\\NetBID_activity_mean_all_target_TAL1.txt";					
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\NetBIDactivity_mean_all_tissues\\NetBID_activity_mean_all_target_zscore_TAL1.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			HashMap index = new HashMap();
			//inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\NetBIDactivity_mean_all_tissues\\NetBID_activity_mean_all_target.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\RNAseq_SupplementaryTable_FPKM\\7_Tcell_Dev_Network\\NetBIDactivity_mean_all_tissues\\NetBID_activity_mean_all_target_zscore.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				String sampleName = split_header[i].split("\\.TARGET")[0];
				if (usp7_wt.containsKey(sampleName)) {
					index.put(i, i);
					out.write("\t" + "USP7wt" + "_" + sampleName);
				}
				if (usp7_mut.containsKey(sampleName)) {
					index.put(i, i);
					out.write("\t" + "USP7mut" + "_" + sampleName);
				}				
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					if (index.containsKey(i)) {
						out.write("\t" + split[i]);
					}
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
