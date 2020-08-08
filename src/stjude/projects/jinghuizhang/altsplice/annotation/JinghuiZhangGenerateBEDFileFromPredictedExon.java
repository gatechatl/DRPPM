package stjude.projects.jinghuizhang.altsplice.annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangGenerateBEDFileFromPredictedExon {

	
	public static void main(String[] args) {
		
		try {
			
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\JuncSalvager_ExtraCellular\\PCGP_SJBALL_Novel_Exon_CancerType.bed";
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\JuncSalvager_ExtraCellular\\PCGP_SJBALL_Novel_Exon_CancerType.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String[] split_loc = split[0].split(":");
				String geneName = split_loc[0];
				String chr = split_loc[1];
				if (!chr.contains("chr")) {
					chr = "chr" + chr;
				}
				String start = split_loc[2];
				String end = split_loc[3];
				String direction = split_loc[4];
				if (direction.equals("for")) {
					direction = "+";
				} else if (direction.equals("rev")) {
					direction = "-";
				}
				out.write(chr + "\t" + start + "\t" + end + "\t" + geneName + "\t1.0\t" + direction + "\n");
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
