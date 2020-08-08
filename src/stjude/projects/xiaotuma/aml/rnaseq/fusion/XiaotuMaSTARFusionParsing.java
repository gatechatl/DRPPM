package stjude.projects.xiaotuma.aml.rnaseq.fusion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Check whether STAR-fusion identified possible break points between 
 * @author tshaw
 *
 */
public class XiaotuMaSTARFusionParsing {

	
	public static void main(String[] args) {
		
		try {
			
			// chr11:118436490-118523917
			int kmt2a_start = 118436490;
			int kmt2a_end = 118523917;
			String kmt2a_chr = "chr11";
			
			// chr10:21534232-21743630
			int mllt10_start = 21534232;
			int mllt10_end = 21743630;
			String mllt10_chr = "chr10";
			
			//chr9:20341669-20622499 
			int mllt3_start = 20341669;
			int mllt3_end = 20622499;
			String mllt3_chr = "chr9";
			
			// chr19:18444516-18445399
			String ell_chr = "chr19";
			int ell_start = 18444516;
			int ell_end = 18445399;
			
			// chr12:76419227-76425556
			String phlda1_chr = "chr12";
			int phlda1_start = 76419227;
			int phlda1_end = 76425556;
			
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\star_mapping_rescue\\SJAML064723_D1-PAULWG-09A-01R.Chimeric.out.junction";
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\star_mapping_rescue\\SJAML064797_D1-PAUVAT-09A-01R.Chimeric.out.junction";
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\star_mapping_rescue\\SJAML064771_D1-PAUYSN-09A-01R.Chimeric.out.junction"; // KMT2A-MLLT10
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\star_mapping_rescue\\SJAML064843_D1-PAUYSN-09A-01R.Chimeric.out.junction"; // KMT2A-MLLT10
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\star_mapping_rescue\\SJAML064858_R1-PAUZTH-09A-01R.Chimeric.out.junction"; // KMT2A-ELL
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\star_mapping_rescue\\SJAML064896_D1-PAVCCX-03A-01R.Chimeric.out.junction"; // KMT2A-MLLT3
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 4) {
					String chr_1 = split[0];
					int bk_1 = new Integer(split[1]);
					String chr_2 = split[3];
					int bk_2 = new Integer(split[4]);
					
					if ((check_overlap(chr_1, bk_1, kmt2a_chr, kmt2a_start, kmt2a_end)) && (check_overlap(chr_2, bk_2, mllt10_chr, mllt10_start, mllt10_end))) {					
						System.out.println(chr_1 + "\t" + bk_1 + "\tKMT2A\t" + chr_2 + "\t" + bk_2 + "\tMLLT10");
					}
					
					if ((check_overlap(chr_2, bk_2, kmt2a_chr, kmt2a_start, kmt2a_end)) && (check_overlap(chr_1, bk_1, mllt10_chr, mllt10_start, mllt10_end))) {					
						System.out.println(chr_2 + "\t" + bk_2 + "\tKMT2A\t" + chr_1 + "\t" + bk_1 + "\tMLLT10");
					}
					
					if ((check_overlap(chr_1, bk_1, kmt2a_chr, kmt2a_start, kmt2a_end)) && (check_overlap(chr_2, bk_2, ell_chr, ell_start, ell_end))) {					
						System.out.println(chr_1 + "\t" + bk_1 + "\tKMT2A\t" + chr_2 + "\t" + bk_2 + "\tELL");
					}
					
					if ((check_overlap(chr_2, bk_2, kmt2a_chr, kmt2a_start, kmt2a_end)) && (check_overlap(chr_1, bk_1, ell_chr, ell_start, ell_end))) {					
						System.out.println(chr_2 + "\t" + bk_2 + "\tKMT2A\t" + chr_1 + "\t" + bk_1 + "\tELL");
					}
					

					if ((check_overlap(chr_1, bk_1, kmt2a_chr, kmt2a_start, kmt2a_end)) && (check_overlap(chr_2, bk_2, mllt3_chr, mllt3_start, mllt3_end))) {					
						System.out.println(chr_1 + "\t" + bk_1 + "\tKMT2A\t" + chr_2 + "\t" + bk_2 + "\tMLLT3");
					}
					
					if ((check_overlap(chr_2, bk_2, kmt2a_chr, kmt2a_start, kmt2a_end)) && (check_overlap(chr_1, bk_1, mllt3_chr, mllt3_start, mllt3_end))) {					
						System.out.println(chr_2 + "\t" + bk_2 + "\tKMT2A\t" + chr_1 + "\t" + bk_1 + "\tMLLT3");
					}
					
					
					if ((check_overlap(chr_1, bk_1, phlda1_chr, phlda1_start, phlda1_end)) && (check_overlap(chr_2, bk_2, mllt10_chr, mllt10_start, mllt10_end))) {					
						System.out.println(chr_1 + "\t" + bk_1 + "\tPHLDA1\t" + chr_2 + "\t" + bk_2 + "\tMLLT10");
					}
					
					if ((check_overlap(chr_2, bk_2, phlda1_chr, phlda1_start, phlda1_end)) && (check_overlap(chr_1, bk_1, mllt10_chr, mllt10_start, mllt10_end))) {					
						System.out.println(chr_2 + "\t" + bk_2 + "\tPHLDA1\t" + chr_1 + "\t" + bk_1 + "\tMLLT10");
					}
										
					/*
					if (check_overlap(chr_1, bk_1, mllt10_chr, mllt10_start, mllt10_end)) {
						System.out.println(chr_1 + "\t" + bk_1 + "\tMLLT10");
					}
					if (check_overlap(chr_1, bk_1, mllt3_chr, mllt3_start, mllt3_end)) {
						System.out.println(chr_1 + "\t" + bk_1 + "\tMLLT3");
					}
					
					if (check_overlap(chr_2, bk_2, kmt2a_chr, kmt2a_start, kmt2a_end)) {					
						System.out.println(chr_2 + "\t" + bk_2 + "\tKMT2A");
					}
					if (check_overlap(chr_2, bk_2, mllt10_chr, mllt10_start, mllt10_end)) {
						System.out.println(chr_2 + "\t" + bk_2 + "\tMLLT10");
					}
					if (check_overlap(chr_2, bk_2, mllt3_chr, mllt3_start, mllt3_end)) {
						System.out.println(chr_2 + "\t" + bk_2 + "\tMLLT3");
					}		*/	
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean check_overlap(String chr, int bk, String gene_chr, int gene_start, int gene_end) {
		if (chr.equals(gene_chr) && gene_start <= bk && bk <= gene_end) {
			return true;
		}
		return false;
	}
}
