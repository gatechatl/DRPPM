package stjude.projects.xiaotuma.aml.rnaseq.qc.checknaming;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class XiaotuMaAMLCheckNaming {

	
	public static void main(String[] args) {
		try {
	
			
			int count_str_yes = 0;
			int count_str_no = 0;
			int count_09A_yes = 0;
			int count_09A_no = 0;
			int count_01R_yes = 0;
			int count_01R_no = 0;
			int count_TARGET_yes = 0;
			int count_TARGET_no = 0;
			
			int total_count = 0;
			HashMap map = new HashMap();
			int number_matching = 0;
			HashMap sample_name = new HashMap();
			String inputFileSize = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\file_size.txt";
			FileInputStream fstream = new FileInputStream(inputFileSize);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			System.out.println("Header: " + header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String fileName = split[1];
				if (fileName.contains("TARGET")) {
					count_TARGET_yes++;
				} else {
					count_TARGET_no++;
				}
				String new_name = fileName.replaceAll("withJunctionsOnGenome_dupsFlagged", "").replaceAll("_withJunctionsOnGenome_dupsFlagged", "").replaceAll("__", "_").replaceAll("_r1.fq.gz", "").replaceAll("_r2.fq.gz", "").replaceAll("_merged", "").replaceAll("_.srt", ".srt");
				if (fileName.split("-")[0].contains("P")) {
					
					System.out.println(fileName.split("-")[0] + "\t" + new_name);
				}
				if (fileName.split("-")[0].contains("RO")) {
					
					System.out.println(fileName.split("-")[0] + "\t" + new_name);
				}
				if (fileName.split("-")[0].contains("BM")) {
					
					System.out.println(fileName.split("-")[0] + "\t" + new_name);
				}
				if (fileName.split("-")[0].contains("TARGET")) {
					//System.out.println(fileName.split("-")[2] + "\t" + fileName.split("-")[2] + "-" + fileName.split("-")[3] + "-" + fileName.split("-")[4]);
					System.out.println(fileName.split("-")[2] + "\t" + new_name);
				}
				
				//System.out.println(fileName);
				String filter_fileName = fileName.replaceAll("r1.fq.gz",  "").replaceAll("r2.fq.gz",  "");
				if (sample_name.containsKey(filter_fileName)) {
					number_matching++;
				} else {
					sample_name.put(filter_fileName, filter_fileName);
				}
				if (!filter_fileName.contains("merged_withJunctionsOnGenome_")) {
					//System.out.println(filter_fileName);
				}
				if (filter_fileName.contains("str_")) {
					count_str_yes++;
				} else {
					count_str_no++;
				}
				total_count++;
			}
			in.close();
			
			System.out.println("Total file number: " + total_count);
			System.out.println("Number of duplicated sample names: " + number_matching);
			System.out.println("Number of unique sample names: " + sample_name.size());
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}
}
