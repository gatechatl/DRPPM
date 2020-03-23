package stjude.projects.xiaotuma.aml.tsne;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

import statistics.general.MathTools;

public class XiaotuMaCalculateFPKMAverage {

	
	public static void main(String[] args) {
		
		try {
			

			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_analysis\\htseq\\AMLRelapse.AVG.protein_coding.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_analysis\\htseq\\AMLRelapse.1377.combined.fpkm.geneName.protein_coding.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0] + "\t" + split_header[1] + "\t" + split_header[2] + "\tFPKM\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				LinkedList list = new LinkedList();
				for (int i = 3; i < split.length; i++) {
					list.add(new Double(split[i]));
				}
				double[] values = MathTools.convertListDouble2Double(list);
				double avg = MathTools.average(values);
				out.write(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + avg + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
