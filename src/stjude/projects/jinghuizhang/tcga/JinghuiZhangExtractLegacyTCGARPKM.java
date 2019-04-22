package stjude.projects.jinghuizhang.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Iterate through the folder to find translated_to_genomic.exon.quantification.txt files generate boxplot format file
 * @author tshaw
 *
 */
public class JinghuiZhangExtractLegacyTCGARPKM {

	
	public static void main(String[] args) {
		
		try {
			
			String keyWord = "chr2:216257654-216257926:-";
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM.20190201.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tRPKM\tType\n");
			

			LinkedList prev_skip_list = new LinkedList();
			String outputSkipFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\RPKM\\legacy\\DontHave_FN1_ED-B_RPKM.20190201.txt";
			FileInputStream fstream = new FileInputStream(outputSkipFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				prev_skip_list.add(str.trim());
			}
			in.close();
			FileWriter fwriter_skip = new FileWriter(outputSkipFile);
			BufferedWriter out_skip = new BufferedWriter(fwriter_skip);
			Iterator itr = prev_skip_list.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				out_skip.write(line + "\n");
			}									
			
			String[] folders = {"TCGA_BRCA_200", "TCGA_GBM", "TCGA_HNSC_200", "TCGA_KIRC_200", "TCGA_LGG_200", 
					"TCGA_LUAD_200", "TCGA_THCA_200", "TCGA_THYM", "TCGA_UCEC_200", "TCGA_LUSC_200", "TCGA_PRAD_200", 
					"TCGA_SKCM_200", "TCGA_COAD_200", "TCGA_OV_200", "TCGA_LIHC_200", "TCGA_STAD_200", "TCGA_BLCA_200", 
					"TCGA_KIRP_200", "TCGA_CESC_200"};
			//String[] folders = {"TCGA_GBM"};
			String inputFolder = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\RPKM\\legacy\\";
			for (String folder: folders) {
				File f = new File(inputFolder + "\\" + folder);
				for (File possibleFolder: f.listFiles()) {
					if (possibleFolder.isDirectory()) {
						for (File exonFile: possibleFolder.listFiles()) {
							System.out.println(exonFile.getPath());
							if (!prev_skip_list.contains(exonFile.getName()) && (exonFile.getName().contains("exon"))) {
								
								boolean didnotfindEDB = true;
								fstream = new FileInputStream(exonFile.getPath());
								din = new DataInputStream(fstream);
								in = new BufferedReader(new InputStreamReader(din));
								while (in.ready()) {
									String str = in.readLine();
									//System.out.println(str);
									String[] split = str.split("\t");									
									if (split[0].equals(keyWord)) {
										out.write(exonFile.getName() + "\t" + split[3] + "\t" + folder + "\n");
										out.flush();
										didnotfindEDB = false;
									}
									
								}
								if (didnotfindEDB) {
									out_skip.write(exonFile.getName() + "\n");
									out_skip.flush();
								}
								in.close();
							}
						}
					}
				}
			}
			out.close();
			out_skip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
