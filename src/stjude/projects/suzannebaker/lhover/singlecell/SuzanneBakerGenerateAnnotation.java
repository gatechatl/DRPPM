package stjude.projects.suzannebaker.lhover.singlecell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Based on the LIMMA result will generate a custom annotation program for the differential results.
 * @author tshaw
 *
 */
public class SuzanneBakerGenerateAnnotation {

	public static void main(String[] args) {
		try {
			
			HashMap[] gfap_up_diff = new HashMap[16];
			HashMap[] nes_up_diff = new HashMap[16];
			HashMap[] young_age = new HashMap[16];
			HashMap[] old_age = new HashMap[16];
			HashMap genes = new HashMap();
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\combined_Suerat\\LIMMA\\Summary_Gene_Clusters.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Gene");
			for (int i = 0; i < 16; i++) {
				out.write("\tGFAP_UP_Cluster" + i + "\tNES_UP_Cluster" + i + "\tYoung_UP_Cluster" + i + "\tOld_UP_Cluster" + i);
				gfap_up_diff[i] = new HashMap();
				nes_up_diff[i] = new HashMap();
				young_age[i] = new HashMap();
				old_age[i] = new HashMap();
				String inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\combined_Suerat\\LIMMA\\Cluster" + i + "\\" + "Combined.cpm.median.txt_" + i + ".txt_limma.txt";
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					
					
					double cntrl_old = new Double(split[1]);
					double cntrl_young = new Double(split[2]);
					double gfap_avg = (new Double(split[3]) + new Double(split[4]) + new Double(split[5]) + new Double(split[6])) / 4;
					double nes_avg = (new Double(split[7]) + new Double(split[8]) + new Double(split[9]) + new Double(split[10])) / 4;
					double log2FC = new Double(split[11]);
					double fdr = new Double(split[13]);
					//System.out.println(fdr);
					if (fdr < 0.05) {
						if (log2FC > 0) {
							gfap_up_diff[i].put(split[0], split[0]);
						} else {
							nes_up_diff[i].put(split[0], split[0]);
						}
						if (cntrl_old > cntrl_young) {
							old_age[i].put(split[0], split[0]);
						} else {
							young_age[i].put(split[0], split[0]);
						}
						genes.put(split[0], split[0]);
						
					}
					
				}
				in.close();
				

			}
			out.write("\tGfap_Up\tNes_Up\tAgeMatchFreq" + "\n");
			
			Iterator itr = genes.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out.write(gene);
				int count_age_match = 0;
				int count_gfap_up = 0;
				int count_nes_up = 0;
				for (int i = 0; i < 16; i++) {
					boolean gfap_up = gfap_up_diff[i].containsKey(gene);
					boolean nes_up = nes_up_diff[i].containsKey(gene);
					boolean old_up = old_age[i].containsKey(gene);
					boolean young_up = young_age[i].containsKey(gene);
					if (gfap_up) {
						count_gfap_up++;
					}
					if (nes_up) {
						count_nes_up++;
					}
					if (nes_up && young_up) {
						count_age_match++;
					}
					if (gfap_up && old_up) {
						count_age_match++;
					}
					out.write("\t" + gfap_up + "\t" + nes_up + "\t" + young_up + "\t" + old_up);
				}
				out.write("\t" + count_gfap_up + "\t" + count_nes_up + "\t" + count_age_match + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
