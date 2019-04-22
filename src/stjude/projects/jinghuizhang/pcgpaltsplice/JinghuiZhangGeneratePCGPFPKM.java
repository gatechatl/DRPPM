package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangGeneratePCGPFPKM {

	
	public static void main(String[] args) {
		
		try {
			
			
			HashMap map = new HashMap();
			HashMap updated = new HashMap();
			int count = 0;
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\pediatric.fpkm.hg19";
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\pcgp_fpkm.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split(" ");
			for (int i = 1; i < split_header.length; i++) {
				map.put(split_header[i], 0.0);
				updated.put(split_header[i], false);
			}
			out.write("GeneName");
			Iterator itr = updated.keySet().iterator();
			while (itr.hasNext()) {
				String sample_name = (String)itr.next();
				out.write("\t" + sample_name);
			}
			out.write("\n");
			
			System.out.println(updated.size());
			String final_gene = "";
			String prev_gene = "";
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = get_meta(split[3].replaceAll("\\{", "").replaceAll("\\}", ""), "sample").replaceAll(" ", "");
				String value = get_meta(split[3].replaceAll("\\{", "").replaceAll("\\}", ""), "value").replaceAll(" ", "");
				String gene = get_meta(split[3].replaceAll("\\{", "").replaceAll("\\}", ""), "gene").replaceAll(" ", "");
				if (prev_gene.equals("")) {
					prev_gene = gene;
				}
				final_gene = gene;
				map.put(sample, value);
				updated.put(sample, true);
				
				if (!prev_gene.equals(gene)) {
					out.write(gene);
					
					itr = updated.keySet().iterator();
					while (itr.hasNext()) {
						String sample_name = (String)itr.next();
						if (map.containsKey(sample_name)) {
							out.write("\t" + map.get(sample_name));
							updated.put(sample_name, false);							
						} else {
							out.write("\tNA");
						}
					}
					out.write("\n");
					map.clear();
				}
				prev_gene = gene;
			}

			out.write(final_gene);
			itr = updated.keySet().iterator();
			while (itr.hasNext()) {
				String sample_name = (String)itr.next();
				if (map.containsKey(sample_name)) {
					out.write("\t" + map.get(sample_name));
					updated.put(sample_name, false);							
				} else {
					out.write("\t0.0");
				}
			}
			out.write("\n");
			
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String get_meta(String line, String type) {
		String[] split = line.split(",");
		for (String stuff: split) {
			//System.out.println(stuff.split(":")[0].replaceAll("\"",  ""));
			if (stuff.split(":")[0].replaceAll("\"",  "").replaceAll(" ",  "").equals(type)) {
				return stuff.split(":")[1].replaceAll("\"", ""); 
			}
		}
		return "NA";
	}
}
