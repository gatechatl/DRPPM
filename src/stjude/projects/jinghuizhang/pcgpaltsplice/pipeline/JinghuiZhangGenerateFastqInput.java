package stjude.projects.jinghuizhang.pcgpaltsplice.pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangGenerateFastqInput {

	
	public static void main(String[] args) {
		
		try {
		
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\star_2pass_bam\\intermediate\\bam.lst";
			String outputFolder = "/rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/PCGP_RNAseq/star_2pass_bam/intermediate";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\star_2pass_bam\\STAR_2.5.3a\\star_file.lst";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				System.out.println(outputFolder + "/" + split[0] + ".R1.fastq\t" + outputFolder + "/" + split[0] + ".R2.fastq\t" + split[0]);
				out.write(outputFolder + "/" + split[0] + ".R1.fastq\t" + outputFolder + "/" + split[0] + ".R2.fastq\t" + split[0] + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
