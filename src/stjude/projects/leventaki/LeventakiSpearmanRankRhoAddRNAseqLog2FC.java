package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LeventakiSpearmanRankRhoAddRNAseqLog2FC {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\cmpb\\ALCL_RNASEQ_ANALYSIS\\FinalAnalysisPipeline\\3_LIMMA\\HIGH_ALKMYCvsLOW_ALKMYC_ALL.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0].replaceAll("\"", ""), split[1]);
			}
			in.close();

			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_methylation_rnaseq_spearman_rho_addLog2FC_20180308.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			inputFile = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_methylation_rnaseq_spearman_rho_20180211.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0] + "\tlog2FC");
			for (int i = 1; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String log2FC = "NA";
				if (map.containsKey(split[0])) {
					log2FC = (String)map.get(split[0]);
				}
				out.write(split[0] + "\t" + log2FC);
				for (int i = 1; i < split.length; i++) {
					out.write("\t" + split[i]);
				}
				out.write("\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
