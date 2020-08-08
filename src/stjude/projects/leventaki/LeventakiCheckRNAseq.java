package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LeventakiCheckRNAseq {

	
	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			HashMap listSamples = new HashMap();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\Variants2Matrix\\unpaired_snv_table_appended.txt"; 
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\Variants2Matrix\\paired_matrix_combined_matrix_sample_x_cell_variant_count_first.tab";
			String inputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\Variants2Matrix\\unpaired_matrix_combined_matrix_sample_x_cell_variant_count_first.tab";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			String[] header_split = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String samples = "";
				for (int i = 1; i < split.length; i++) {
					String sampleName = header_split[i].replaceAll(".bam", "").replaceAll("_", "");
					map.put(split[0] + "\t" + sampleName, split[i]);
					String value = split[i];
					String[] split_value = value.split("\\|");
					double mutRead = new Double(split_value[0]);
					double otherRead = new Double(split_value[1]);
					if (mutRead >= 5) {
						samples += sampleName + ",";
					}
					//System.out.println(split[0] + "\t" + header_split[i].replaceAll(".bam", "").replaceAll("_", ""));
				}
				listSamples.put(split[0], samples);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			//inputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\Variants2Matrix\\paired_snv_table.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\Variants2Matrix\\unpaired_snv_table.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			header = in.readLine();
			out.write(header + "\tRNAseq#Reads\tRNAseqPercentage\tRNAseq\tSamples\n");
			//String[] header_split = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String tag = split[4] + "." + split[5] + "." + split[15] + "." + split[16];
				String samples = (String)listSamples.get(tag);
				//System.out.println(tag + "\t" + sampleName);
				if (map.containsKey(tag + "\t" + sampleName)) {
					String value = (String)map.get(tag + "\t" + sampleName);
					String[] split_value = value.split("\\|");
					double mutRead = new Double(split_value[0]);
					double otherRead = new Double(split_value[1]);
					if (mutRead == 0) {
						out.write(str + "\t0.0\t0.0\tno\t" + samples + "\n");
					} else {
						if (mutRead >= 5) {
							out.write(str + "\t" + mutRead + "\t" + (mutRead / (mutRead + otherRead)) + "\tyes\t" + samples + "\n");
						} else {
							out.write(str + "\t" + mutRead + "\t" + (mutRead / (mutRead + otherRead)) + "\tno\t" + samples + "\n");
						}
					}
					
					//System.out.println(str + "\t" + value);
				} else {
					out.write(str + "\tNoRNAseq\tNoRNAseq\tNoRNAseq\t" + samples + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
