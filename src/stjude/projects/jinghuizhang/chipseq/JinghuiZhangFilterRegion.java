package stjude.projects.jinghuizhang.chipseq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangFilterRegion {

	
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_Chipseq\\chromatin_interactions\\chr11_GSM2774001_Jurkat_YY1_hichip5kb-results.csv";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\USP7_Chipseq\\chromatin_interactions\\Highlight_GSM2774001_Jurkat_YY1_hichip5kb-results.csv";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap uniprot2fasta = new HashMap();
			HashMap geneName2uniprot = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				if (split[0].equals("chr11")) {
					if (35000000 < new Integer(split[1]) && new Integer(split[1]) < 37000000 && new Integer(split[6]) > 0) {
						out.write(split[0] + "," + split[1] + "," + split[2] + "\t" + split[3] + "," + split[4] + "," + split[5] + "\t" + split[6] + "\n");
					}
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
