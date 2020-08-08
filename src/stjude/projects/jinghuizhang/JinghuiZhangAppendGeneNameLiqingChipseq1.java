package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import idconversion.tools.GTFFile;

public class JinghuiZhangAppendGeneNameLiqingChipseq1 {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap transcript2geneName = new HashMap();
			String gtfFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_Chipseq\\ucsc_download_GENCODEV28lift3_basic.txt";			
			FileInputStream fstream = new FileInputStream(gtfFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				transcript2geneName.put(split[1].split("_")[0], split[12]);
			}
			in.close();
			
			String gmtFile = "\\\\gsc.stjude.org\\resgen\\dev\\wc\\tshaw\\REFERENCE\\GMT\\custom\\Sanda_ThomasLooke_E_Protein.gmt";
			fstream = new FileInputStream(gmtFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			HashMap e2a_heb = new HashMap();
			String str1 = in.readLine();
			String[] split_str1 = str1.split("\t");
			for (String gene: split_str1) {
				e2a_heb.put(gene, gene);
			}
			String str2 = in.readLine();
			String[] split_str2 = str2.split("\t");
			for (String gene: split_str2) {
				e2a_heb.put(gene, gene);
			}			
			in.close();
			
			HashMap tal1 = new HashMap();
			gmtFile = "\\\\gsc.stjude.org\\resgen\\dev\\wc\\tshaw\\REFERENCE\\GMT\\custom\\Sanada_ThomasLooke_TAL1_HEB_E2A_RUNX1_MYB_GATA3.gmt";
			fstream = new FileInputStream(gmtFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			str1 = in.readLine();
			split_str1 = str1.split("\t");
			for (String gene: split_str1) {
				tal1.put(gene, gene);
			}
			str2 = in.readLine();
			split_str2 = str2.split("\t");
			for (String gene: split_str2) {
				tal1.put(gene, gene);
			}			
			in.close();
			
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_Chipseq\\geneName_liqing_merged_peaks_promoter.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_Chipseq\\liqing_merged_peaks_promoter.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("GeneName\t" + header + "\tE2A_HEB_flag\tTAL1_flag\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String transcript = split[7].split("_")[0];
				String geneID = "NA";
				String geneSymbol = "NA";
				if (transcript2geneName.containsKey(transcript)) {
					geneSymbol = (String)transcript2geneName.get(transcript);					
				}
				boolean e2aheb_bool = false;
				boolean tal1_bool = false;
				if (e2a_heb.containsKey(geneSymbol)) {
					e2aheb_bool = true;
				}
				if (tal1.containsKey(geneSymbol)) {
					tal1_bool = true;
				}
				out.write(geneSymbol + "\t" + str + "\t" + e2aheb_bool + "\t" + tal1_bool + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
