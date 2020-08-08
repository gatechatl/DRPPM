package rnaseq.splicing.spladder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class SpladderScriptGenerator {


	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Generate script for spladder.";
	}
	public static String parameter_info() {
		return "[bamFileList] [spladder_path] [gtfFile] [outputFileCluster]";
	}
	public static void execute(String[] args) {
		
		try {
			
			///home/tshaw/.local/bin/spladder build -a gencode.v19.annotation_for_cufflink.gtf -b /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/PCGP_RNAseq/star_2pass_bam/hg19_STAR_2.5.3a/SJWLM010706_G1-NOR004Aligned.sortedByCoord.out.bam -o SJWLM010706_G1-NOR004
			String bamFileList = args[0];
			String spladder_path = args[1];
			String gtfFile = args[2];
			String outputFileCluster = args[3];

        	FileWriter fwriter_cluster = new FileWriter(outputFileCluster);
            BufferedWriter out_cluster = new BufferedWriter(fwriter_cluster);

            
			FileInputStream fstream = new FileInputStream(bamFileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String bamFile = split[1];
				
				String outputFile = sampleName + ".sh";
				out_cluster.write("sh " + outputFile + "\n");
	        	FileWriter fwriter = new FileWriter(outputFile);
	            BufferedWriter out = new BufferedWriter(fwriter);
	            //out.write("samtools index " + bamFile + "\n");
	            //out.write("mkdir " + sampleName + "\n");
				String script = spladder_path + " build -a " + gtfFile + " -b " + bamFile + " -o " + sampleName;
				out.write(script + "\n");
				out.close();
				//System.out.println(script);
			}
			in.close();
		
			out_cluster.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
