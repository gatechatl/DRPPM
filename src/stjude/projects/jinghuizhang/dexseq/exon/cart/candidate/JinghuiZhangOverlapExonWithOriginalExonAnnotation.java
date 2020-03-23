package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;


public class JinghuiZhangOverlapExonWithOriginalExonAnnotation {

	public static String description() {
		return "Overlap the predicted exon with annotation.";
	}
	public static String type() {
		return "CAR-T";
	}
	public static String parameter_info() {
		return "[inputCandidateFile] [gtfFile] [outputFile;]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap chr_candidates = new HashMap();
			String inputCandidateFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\PCGP_TARGET_GTEx_Candidates.bed"; // args[0]; //Z:\ResearchHome\ProjectSpace\zhanggrp\AltSpliceAtlas\common\analysis\Comprehensive_CAR-T_Analysis\hg38_analysis\AfterLiqingsExonCounting\Summary\PCGP_TARGET_GTEx_Candidates.bed
			String gtfFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\gencode.v31.primary_assembly.annotation.gtf"; // args[1]; //
			String outputFile = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\Orig_Annot_PCGP_TARGET_GTEx_Candidates.txt";
			FileInputStream fstream = new FileInputStream(inputCandidateFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (chr_candidates.containsKey(split[0])) {
					HashMap map = (HashMap)chr_candidates.get(split[0]);
					map.put(str, "");
					chr_candidates.put(split[0], map);
				} else {
					HashMap map = new HashMap();
					map.put(str, "");
					chr_candidates.put(split[0], map);
				}
			}
			in.close();				
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			int count = 0;
			
			fstream = new FileInputStream(gtfFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				String[] split = str.split("\t");
				if (!split[0].contains("#")) {
					String orig_chr = split[0];
					int orig_start = new Integer(split[3]);
					int orig_end = new Integer(split[4]);
					
					if (split[2].equals("exon")) {
						if (chr_candidates.containsKey(orig_chr)) {
							HashMap map = (HashMap)chr_candidates.get(orig_chr);
							Iterator itr = map.keySet().iterator();
							while (itr.hasNext()) {
								String candidates = (String)itr.next();
								String[] split_candidates = candidates.split("\t");
								String chr = split_candidates[0];
								int start = new Integer(split_candidates[1]);
								int end = new Integer(split_candidates[2]);
								if (check_overlap(orig_start, orig_end, start, end)) {
									out.write(orig_chr + "\t" + orig_start + "\t" + orig_end + "\t" + split[8] + "\t" + candidates + "\n");
								}
							}
						}
					}
				}
				count++;
				if (count % 1000 == 0) {
					System.out.println(count);
				}
			}
			in.close();				
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean check_overlap(int a1, int a2, int b1, int b2) {
		if (a1 <= b1 && b1 <= a2) {
			return true;
		}
		if (a1 <= b2 && b2 <= a2) {
			return true;
		}
		
		if (a2 <= b1 && b1 <= a1) {
			return true;
		}
		if (a2 <= b2 && b2 <= a1) {
			return true;
		}
		if (b1 <= a1 && a1 <= b2) {
			return true;
		}
		if (b1 <= a2 && a2 <= b2) {
			return true;
		}
		if (b2 <= a1 && a1 <= b1) {
			return true;
		}
		if (b2 <= a2 && a2 <= b1) {
			return true;
		}
		return false;
	}
}
