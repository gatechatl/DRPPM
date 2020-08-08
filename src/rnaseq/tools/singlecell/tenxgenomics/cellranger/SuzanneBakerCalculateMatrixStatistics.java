package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class SuzanneBakerCalculateMatrixStatistics {

	
	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\NesCre_Tumors\\2942_082819\\Run 3\\filtered_gene_bc_matrices\\mm10\\matrix.mtx";
			//String inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\NesCre_Tumors\\2942_082819\\Top off\\filtered_gene_bc_matrices\\mm10\\matrix.mtx";
			//String inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\NesCre_Tumors\\2942_082819\\2942 Nes\\filtered_gene_bc_matrices\\mm10\\matrix.mtx";
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\GfapCreER_Tumors\\2827\\outs\\filtered_gene_bc_matrices\\mm10\\matrix.mtx";
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\NesCre_Tumors\\2942_082819\\Top off\\filtered_gene_bc_matrices\\mm10\\matrix.mtx";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(" ");
				if (split.length >= 3) {
					
					if (map.containsKey(split[1])) {
						int prev_count = (Integer)map.get(split[1]);
						prev_count += 1;
						map.put(split[1], prev_count);
					} else {
						map.put(split[1], 1);
					}
					//System.out.println(str);
					
					/*
					if (map.containsKey(split[1])) {
						int prev_count = (Integer)map.get(split[1]);
						prev_count += 1;
						map.put(split[1], prev_count);
					} else {
						map.put(split[1], new Integer(1);
					}*/
					//System.out.println(str);
				}
			}
			in.close();
			
			LinkedList list = new LinkedList();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				int count = (Integer)map.get(sample);
				list.add(count + "");
			}
			double[] values = MathTools.convertListStr2Double(list);
			System.out.println(MathTools.median(values));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
