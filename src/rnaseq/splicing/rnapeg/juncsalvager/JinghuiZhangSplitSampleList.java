package rnaseq.splicing.rnapeg.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JinghuiZhangSplitSampleList {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap types = new HashMap();
			String inputFilePath = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\JuncSalvager_ExtraCellular\\";
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\JuncSalvager_ExtraCellular\\sample_file.lst";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = split[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0].split("0")[0];
				map.put(split[0], str);
				
				
				if (types.containsKey(type)) {
					LinkedList list = (LinkedList)types.get(type);
					list.add(str);
					types.put(type, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(str);
					types.put(type, list);
				}
			}
			in.close();
					
			Iterator itr = types.keySet().iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				LinkedList list = (LinkedList)types.get(type);
				
				String outputFile = inputFilePath + "\\" + type + "_sample_file.lst";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String str = (String)itr2.next();
					out.write(str + "\n");	
				}
				out.close();				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
