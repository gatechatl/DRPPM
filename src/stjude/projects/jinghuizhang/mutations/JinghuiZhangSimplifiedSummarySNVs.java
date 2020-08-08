package stjude.projects.jinghuizhang.mutations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangSimplifiedSummarySNVs {

	public static void main(String[] args) {
		
		try {
			
			String geneInfo = "NA";
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SNVIndel_Simplified.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (str.contains(">")) {
					String geneName = split[0].replaceAll(">",  "");
					String type = split[1];
					if (!(type.equals("intron_variant") || type.equals("synonymous_variant"))) {
						geneInfo = geneName + "\t" + type;
					} else {
						geneInfo = "NA";
					}
				} else {
					if (!geneInfo.equals("NA")) {
						out.write(split[0] + "\t" + geneInfo + "\n");
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
