package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangGeneratePCGPFileListForJuncSalvage {

	
	public static void main(String[] args) {
		
		try {
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\JuncSalvager\\sample_file.lst";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			String rnaPEGPath = "/rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/PCGP_RNAseq/processed_from_old_bam/RNApeg/"; 
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\RNApeg\\bam.lst";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				File f = new File(split[0]);
				System.out.println(rnaPEGPath + f.getName() + ".junctions.tab.shifted.tab.annotated.tab.cross_sample_corrected.tab");
				out.write(f.getName().replaceAll(".bam", "") + "\t" + split[0] + "\t" + rnaPEGPath + f.getName() + ".junctions.tab.shifted.tab.annotated.tab.cross_sample_corrected.tab" + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
