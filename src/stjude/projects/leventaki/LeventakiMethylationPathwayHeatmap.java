package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class LeventakiMethylationPathwayHeatmap {

	
	public static void main(String[] args) {
		
		try {
			HashMap groups = new HashMap();
			String[] group_split = {"UTR5_Group1_Hyper","UTR5_Group1_Hypo","UTR5_Group2_Hypo","UTR5_Group2_Hyper","Body_Group1_Hypo","Body_Group1_Hyper","Body_Group2_Hyper","Body_Group2_Hypo","TSS_Group1_Hypo","TSS_Group1_Hyper","TSS_Group2_Hypo","UTR3_Group1_Hyper","UTR3_Group1_Hypo","UTR3_Group2_Hypo"};
			for (String g: group_split) {
				groups.put(g, g);
			}
			String[] tags = {"TF", "Histone", "KEGG", "GOBP"};
			for (String tag: tags) {
				HashMap pathway = new HashMap();
				
				HashMap adj_pval = new HashMap();
				
				String outputFile = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\PathwayHeatmap\\" + tag + "_output.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				
				String inputFile = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\PathwayHeatmap\\Diff_GeneLevel_" + tag + ".txt";
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String type = split[0];
					String term = split[1];
					//groups.put(type, type);
					pathway.put(term, term);
					adj_pval.put(type + "\t" + term, split[4]);
				}
				in.close();
				out.write("Group");
				Iterator itr2 = pathway.keySet().iterator();
				while (itr2.hasNext()) {
					String term = (String)itr2.next();
					out.write("\t" + term);
				}
				out.write("\n");
				Iterator itr = groups.keySet().iterator();
				while (itr.hasNext()) {
					String type = (String)itr.next();
					out.write(type);
					itr2 = pathway.keySet().iterator();
					while (itr2.hasNext()) {
						String term = (String)itr2.next();
						if (adj_pval.containsKey(type + "\t" + term)) {
							String val = (String)adj_pval.get(type + "\t" + term);
							out.write("\t" + val);
						} else {
							out.write("\t1.0");
						}
					}
					out.write("\n");
				}
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
