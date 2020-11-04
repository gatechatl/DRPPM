package rnaseq.splicing.alternativejuncpipeline.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangGenerateTARGETFileListForJuncSalvage {

	
	public static void main(String[] args) {
		
		try {
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TARGET_RNAseq\\from_strongarm_mapping\\JuncSalvager\\sample_file.lst";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			String rnaPEGPath = "/rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/TARGET_RNAseq/from_strongarm_mapping/RNApeg/"; 
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TARGET_RNAseq\\from_strongarm_mapping\\RNApeg\\bam.lst";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				File f = new File(split[0]);
				System.out.println(rnaPEGPath + f.getName() + ".junctions.tab.shifted.tab.annotated.tab.cross_sample_corrected.tab");
				out.write(f.getName() + "\t" + split[0] + "\t" + rnaPEGPath + f.getName() + ".junctions.tab.shifted.tab.annotated.tab.cross_sample_corrected.tab" + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
