package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class JinghuiZhangAnthonyHoProteomicsMatrix {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap sampleNames = new HashMap();
			HashMap geneNames = new HashMap();
			

			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ProteomicsAMLAnthonyHo\\proteomics_matrix.txt";;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ProteomicsAMLAnthonyHo\\systemage-peptides_HSC.tsv";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = "Donor" + split[0] + "-" + split[1] + "-" + split[3] + "-" + split[4] + "-" + split[6] + "-" + split[12];
				sampleNames.put("Donor" + split[0] + "-" + split[1] + "-" + split[3] + "-" + split[4] + "-" + split[6], "");
				geneNames.put(split[12], "");
				if (map.containsKey(name)) {
					LinkedList list = (LinkedList)map.get(name);
					String med_ratio = split[9];
					list.add(med_ratio);
					map.put(name, list);
				} else {
					LinkedList list = new LinkedList();
					String med_ratio = split[9];
					list.add(med_ratio);
					map.put(name, list);
				}
				
			}
			in.close();
			
			out.write("GN");
			Iterator itr = sampleNames.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				out.write("\t" + name);
			}
			out.write("\n");
			
			Iterator itr2 = geneNames.keySet().iterator();
			while (itr2.hasNext()) {
				String geneName = (String)itr2.next();
				out.write(geneName);
				itr = sampleNames.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					if (map.containsKey(sampleName + "-" + geneName)) {
						LinkedList list = (LinkedList)map.get(sampleName + "-" + geneName);
						double median = MathTools.median(MathTools.convertListStr2Double(list));
						out.write("\t" + median);
					} else {
						out.write("\tNA");
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
