package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/** 
 * After plotting the global distribution, we wanted to examine what probes are enriched in Group1 that is also in the promoter
 * @author tshaw
 *
 */
public class LeventakiMethylationExtractEnrichedProbes {

	
	public static void main(String[] args) {
		
		try {
			HashMap id_pass = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_108399_methylation_table_appendGeneInfo_normalized_diff.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double group1_mean = new Double(split[2]);
				double group2_mean = new Double(split[3]);
				if (new Double(split[1]) > 0.2 && group1_mean > 0.75) {
					id_pass.put(split[0], split[1] + "\t" + split[2] + "\t" + split[3]);
				}
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_108399_methylation_table_appendGeneInfo_normalized_diff_cutoff0.2.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile2 = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_108399_methylation_table_appendGeneInfo_normalized.txt";
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\tG1-G2\tG1_Mean\tG2_Mean\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (id_pass.containsKey(split[0])) {
					String val = (String)id_pass.get(split[0]);
					out.write(str + "\t" + val + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
