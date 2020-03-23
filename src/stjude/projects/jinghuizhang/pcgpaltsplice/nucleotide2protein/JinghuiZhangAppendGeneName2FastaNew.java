package stjude.projects.jinghuizhang.pcgpaltsplice.nucleotide2protein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JinghuiZhangAppendGeneName2FastaNew {

	
	public static void main(String[] args) {
		
		try {
			
			LinkedList gene_list = new LinkedList();
			HashMap map = new HashMap();
			String bedFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\Orig_Annot_PCGP_TARGET_GTEx_Candidates.bed";
			FileInputStream fstream = new FileInputStream(bedFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = str.split("\t")[3].split("_")[0];
				gene_list.add(geneName);
				map.put(split[0] + ":" + split[1] + "-" + split[2], geneName);
			}
			in.close();

			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\Candidate.GeneName.fasta";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			Iterator itr = gene_list.iterator();
			
			String fastaFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\Orig_Annot_PCGP_TARGET_GTEx_Candidates.fasta";
			fstream = new FileInputStream(fastaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String geneName = "NA";
					String tag = str.replaceAll(">", "").trim();
					if (map.containsKey(tag)) {
						geneName = (String)map.get(tag);
					}
					
					out.write(str + "\t" + geneName + "\n");
				} else {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
