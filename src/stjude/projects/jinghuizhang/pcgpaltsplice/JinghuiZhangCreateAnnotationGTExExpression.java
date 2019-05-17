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

public class JinghuiZhangCreateAnnotationGTExExpression {

	public static void main(String[] args) {
		
		try {
			
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\interactive_plots\\Candidates_GTEx_ID.txt";
			String inputFile2 = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\GTEx_Reference\\ExonLevelQuant\\GTEx_Tissue_2016-01-15_v7_RNASeQCv1.1.8_exon_median_reads_filter_cutoff.txt";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\interactive_plots\\Candidates_GTEx_ID_annotation.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			LinkedList list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				list.add(str);
			}
			in.close();
			
			HashMap low_map = new HashMap();
			HashMap above_avg_map = new HashMap();
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (list.contains(split[0])) {
					for (int i = 1; i < split.length; i++) {
						split_header[i] = convert_name(split_header[i]);
						if (new Double(split[i]) == 1.0) {
							if (low_map.containsKey(split[0])) {
								String value = (String)low_map.get(split[0]);
								if (!value.contains(split_header[i])) {
									value += split_header[i] + ",";
								}
								low_map.put(split[0], value);								
							} else {
								String value = split_header[i] + ",";
								low_map.put(split[0], value);
							}
						}
						if (new Double(split[i]) >= 2.0) {
							if (above_avg_map.containsKey(split[0])) {
								String value = (String)above_avg_map.get(split[0]);
								if (!value.contains(split_header[i])) {
									value += split_header[i] + ",";
								}
								above_avg_map.put(split[0], value);								
							} else {
								String value = split_header[i] + ",";
								above_avg_map.put(split[0], value);
							}
						}
					}
					
				}
			}
			in.close();
			
			
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				String id = (String)itr.next();
				String low = (String)low_map.get(id);
				String above_avg = (String)above_avg_map.get(id);
				out.write(id + "\t" + low + "\t" + above_avg + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String convert_name(String name) {
		if (name.contains("Adipose")) {
			return "Adipose";
		}
		if (name.contains("Artery")) {
			return "Artery";
		}
		if (name.contains("Brain")) {
			return "Brain";
		}
		if (name.contains("Cervix")) {
			return "Cervix";
		}
		if (name.contains("Colon")) {
			return "Colon";
		}
		if (name.contains("Esophagus")) {
			return "Esophagus";
		}
		if (name.contains("Skin")) {
			return "Skin";
		}
		if (name.contains("Skin")) {
			return "Skin";
		}
		return name;
	}
}
