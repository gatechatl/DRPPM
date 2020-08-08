package stjude.projects.xiaotuma.aml.rnaseq.combined;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuReplaceSampleName {
	
	public static void main(String[] args) {
				
		try {
			
			HashMap map = new HashMap();

			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\SJID2TARGETID_20200117.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				map.put(split[1].replaceAll("_srt", ""), split[0]);
				if (split[0].contains("SJNORM")) {
					map.put(split[1].split("-")[2], split[0]);
				}
				if (split[1].contains("PAVZBM")) {
					map.put("PAVZBM", split[0]);
				}
				if (split[1].contains("PAVBMM")) {
					map.put("PAVBMM", split[0]);
				}
				System.out.println(split[1] + "\t" + split[0]);
			}
			in.close();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\Klco\\ComprehensiveAMLTranscriptome\\Combined\\FredHutch_TPM_filter_transpose_clean_short_rename.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("Transcript");
            String matrixFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\Klco\\ComprehensiveAMLTranscriptome\\Combined\\FredHutch_TPM_filter_transpose_clean_short.txt";
			//String matrixFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\Klco\\ComprehensiveAMLTranscriptome\\Combined\\FredHutch_TPM_filter_transpose_clean_short_log2_1000mad.txt";
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				String sample = split_header[i];				
				sample = sample.replaceAll("_merged", "");
				
				if (sample.contains("_withJunctionsOnGenome")) {
					sample = sample.replaceAll("_withJunctionsOnGenome", "");
				}
				if (sample.contains("RO") || sample.contains("BM")) {
					sample = sample.replaceAll("TARGET-00-", "");
					//System.out.println(sample);
					sample = sample.split("-")[0];
				} else if (!sample.contains("TARGET")) {
					sample = "TARGET-20-" + sample;
				}
				if (map.containsKey(sample)) {
					out.write("\t" + map.get(sample));
					//System.out.println(sample + "\t" + map.get(sample));
				} else {
					out.write("\t" + sample);
				}
				
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(str + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
