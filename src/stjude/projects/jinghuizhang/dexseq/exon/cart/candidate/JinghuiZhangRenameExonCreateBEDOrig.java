package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangRenameExonCreateBEDOrig {

	public static void main(String[] args) {
		
		try {
			
			
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\Orig_Annot_PCGP_TARGET_GTEx_Candidates.bed";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			String inputNewAnnotation = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\Orig_Annot_PCGP_TARGET_GTEx_Candidates.txt"; // args[0]; //Z:\ResearchHome\ProjectSpace\zhanggrp\AltSpliceAtlas\common\analysis\Comprehensive_CAR-T_Analysis\hg38_analysis\AfterLiqingsExonCounting\Summary\PCGP_TARGET_GTEx_Candidates.bed
			FileInputStream fstream = new FileInputStream(inputNewAnnotation);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0];
				String start = split[1];
				String end = split[2];
				String direction = split[9];
				String gene_name = GTFFile.grabMeta(split[3], "gene_name");
				String exon_number = GTFFile.grabMeta(split[3], "exon_number");
				String tag = chr + "\t" + start + "\t" + end + "\t" + gene_name + "_" + exon_number + "\t1\t" + direction;				
				if (!map.containsKey(tag)) {					
					map.put(tag, split[7]);
					out.write(tag + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
