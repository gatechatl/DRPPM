package stjude.projects.jinghuizhang.cibersort;

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

public class JinghuiZhangSummarizeCibersortResult {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			HashMap uniq_id = new HashMap();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\Cibersort\\PCGP_TumorType_Result_20190606.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String ciber_file = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\Cibersort\\PCGP_Comprehensive_Cibersort_Result_20190530.txt";
			FileInputStream fstream = new FileInputStream(ciber_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0].split("0")[0];
				uniq_id.put(type, type);
				for (int i = 1; i < split.length; i++) {
					String tag = type + "\t" + split_header[i].replaceAll(" ", "_");
					if (map.containsKey(tag)) {
						LinkedList list = (LinkedList)map.get(tag);
						list.add(split[i]);
						map.put(tag,  list);
					} else {
						LinkedList list = new LinkedList();
						list.add(split[i]);
						map.put(tag,  list);
					}
				}
			}
			in.close();
			Iterator itr = uniq_id.keySet().iterator();
			while (itr.hasNext()) {
				String tumorType = (String)itr.next();
				out.write(tumorType);
				for (int i = 1; i < split_header.length; i++) {				
					String tag = tumorType + "\t" + split_header[i].replaceAll(" ", "_");
					LinkedList list = (LinkedList)map.get(tag);
					double[] values = MathTools.convertListStr2Double(list);
					double avg = MathTools.mean(values);
					out.write("\t" + avg);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
