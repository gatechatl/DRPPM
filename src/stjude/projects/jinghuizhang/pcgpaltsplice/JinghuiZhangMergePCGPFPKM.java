package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JinghuiZhangMergePCGPFPKM {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap geneName = new HashMap();
			int count = 0;
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\updated_pcgp_fpkm.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			LinkedList sampleName = new LinkedList();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\pcgp_fpkm.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split(" ");
			for (int i = 1; i < split_header.length; i++) {
				sampleName.add(split_header[i]);
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (geneName.containsKey(split[0])) {
					String prev_line = (String)geneName.get(split[0]);
					String merged_line = merge(prev_line, str);
					geneName.put(split[0], merged_line);
				} else {
					geneName.put(split[0],  str);
				}
				count++;
				if (count %1000 == 0) {
					System.out.println(count);
				}
			}
			in.close();
			
			Iterator itr = geneName.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String line = (String)geneName.get(gene);
				out.write(line + "\n");
				
			}
			out.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	public static String merge(String line1, String line2) {
		String[] split_line1 = line1.split("\t");
		String[] split_line2 = line2.split("\t");
		
		String[] merged_line = new String[split_line1.length];
		merged_line[0] = split_line1[0];				
		for (int i = 1; i < merged_line.length; i++) {
			if (!split_line1[i].equals("NA")) {
				merged_line[i] = split_line1[i];
			} else if (!split_line2[i].equals("NA")) {				
				merged_line[i] = split_line2[i];
			} else {
				merged_line[i] = "NA";
			}
		}
		String final_line = merged_line[0];
		for (int i = 1; i < merged_line.length; i++) {
			final_line += "\t" + merged_line[i];
		}
		return final_line;
	}
}
