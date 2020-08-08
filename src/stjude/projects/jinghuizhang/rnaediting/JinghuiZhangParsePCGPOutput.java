package stjude.projects.jinghuizhang.rnaediting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class JinghuiZhangParsePCGPOutput {

	
	public static void main(String[] args) {
		
		try {
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\RNAediting\\PCGP\\Identify_PCGP_RNAediting.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("RNAeditingEvent\tvaf_max\tvaf_avg\tvaf_stdev\tmax_edit\tavg_edit\tstdev_edit\tmax_reference\tavg_reference\tstdev_reference\n");

			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\RNAediting\\PCGP\\matrix_combined_matrix_sample_x_cell_variant_count_first.tab";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();			
				String[] split = str.split("\t");
				LinkedList variant_allele_frequency = new LinkedList();
				LinkedList count_edit = new LinkedList();
				LinkedList count_reference = new LinkedList();
				for (int i = 1; i < split.length; i++) {
					double edit = new Double(split[i].split("\\|")[0]);
					double reference = new Double(split[i].split("\\|")[1]);
					count_edit.add(edit);
					count_reference.add(reference);
					
					if (edit + reference <= 10) {
						if (edit <= 4) {
							edit = 0.0;
						}
					}
					double vaf = edit / (edit + reference);
					if (new Double(vaf).isNaN()) {
						vaf = 0.0;
					}
					
					variant_allele_frequency.add(vaf);
				}
				double[] count_edit_array = MathTools.convertListDouble2Double(count_edit);
				double average_edit = MathTools.mean(count_edit_array);
				double stdev_edit = MathTools.standardDeviation(count_edit_array);
				double max_edit = MathTools.max(count_edit_array);
				double[] count_reference_array = MathTools.convertListDouble2Double(count_reference);
				double average_reference = MathTools.mean(count_reference_array);
				double stdev_reference = MathTools.standardDeviation(count_reference_array);
				double max_reference = MathTools.max(count_reference_array);
				/*
				Iterator itr = variant_allele_frequency.iterator();
				while (itr.hasNext()) {
					double value = (Double)itr.next();
					System.out.print("\t" + value);
				}*/
				double[] count_vaf_array = MathTools.convertListDouble2Double(variant_allele_frequency);
				double average_vaf = MathTools.mean(count_vaf_array);
				double stdev_vaf = MathTools.standardDeviation(count_vaf_array);
				double max_vaf = MathTools.max(count_vaf_array);
				System.out.println(split[0] + "\t" + max_vaf + "\t" + average_vaf + "\t" + stdev_vaf + "\t" + max_edit + "\t" + average_edit + "\t" + stdev_edit + "\t" + max_reference + "\t" + average_reference + "\t" + stdev_reference);
				out.write(split[0] + "\t" + max_vaf + "\t" + average_vaf + "\t" + stdev_vaf + "\t" + max_edit + "\t" + average_edit + "\t" + stdev_edit + "\t" + max_reference + "\t" + average_reference + "\t" + stdev_reference + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
