package stjude.projects.jinghuizhang.dexseq.exon.annotation.pcgptarget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangGenerateSampleInfoForTARGET {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap diag_relapse_map = new HashMap();
			HashMap diagnosis_short_map = new HashMap();
			HashMap disease_group_short_map = new HashMap();
			
			HashMap meta_info_map = new HashMap();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_TARGET_RNAseq_Analysis\\TARGET_processing_summary.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String sampleMetaInformation = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_TARGET_RNAseq_Analysis\\sample_type_metadata_appended.txt";
			FileInputStream fstream = new FileInputStream(sampleMetaInformation);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String diagnosis_full = split[0];
				String disease_group_full = split[1];
				String disease_group_short = split[2];
				String diagnosis_short = split[3];
				String diag_relapse = split[5];
				String sample_name = split[4];
				
				disease_group_short_map.put(disease_group_short, 0);
				diagnosis_short_map.put(diagnosis_short, 0);
				diag_relapse_map.put(diag_relapse, 0);
				
				meta_info_map.put(sample_name.split("-")[0], sample_name + "\t" + diagnosis_short + "\t" + disease_group_short + "\t" + diag_relapse + "\t" + disease_group_full + "\t" + diagnosis_full);				
			}
			in.close();
			int count_processed_samples = 0;
			
			String dexseq_folder_name = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\ExonLevelQuantificationPipeline\\Reference\\ExonLevelGTF\\TARGET_dexseq_count\\";
			File folder = new File(dexseq_folder_name);
			for (File file: folder.listFiles()) {
				if (file.getName().contains("_dexseq_count.txt")) {
					String sample_name = file.getName().replaceAll("_dexseq_count.txt", "");
					if (meta_info_map.containsKey(sample_name)) {
						String line = (String)meta_info_map.get(sample_name);
						String[] split_line = line.split("\t");
						
						String sample_name_full = split_line[0];
						String diagnosis_short = split_line[1]; 
						String disease_group_short = split_line[2]; 
						String diag_relapse = split_line[3];
						String disease_group_full = split_line[4];
						String diagnosis_full = split_line[5];
						count_processed_samples++;
						
						Iterator itr = disease_group_short_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(disease_group_short)) {
								int count = (Integer)disease_group_short_map.get(key);
								count++;
								disease_group_short_map.put(key, count);
							}
						}
						

						itr = diagnosis_short_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(diagnosis_short)) {
								int count = (Integer)diagnosis_short_map.get(key);
								count++;
								diagnosis_short_map.put(key, count);
							}
						}
						

						itr = diag_relapse_map.keySet().iterator();
						while (itr.hasNext()) {
							String key = (String)itr.next();
							if (key.equals(diag_relapse)) {
								int count = (Integer)diag_relapse_map.get(key);
								count++;
								diag_relapse_map.put(key, count);
							}
						}
						
						
						
					} else {
						System.out.println(sample_name);
					}
				}
			}
			
			System.out.println("Global_Summary\tNum samples processed\t" + count_processed_samples);
			out.write("Global_Summary\tNum samples processed\t" + count_processed_samples + "\n");
			
			
			Iterator itr = disease_group_short_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)disease_group_short_map.get(key);
				System.out.println(key + ": " + count);
				out.write("disease_group_short\t" + key + "\t" + count + "\n");
			}
			

			itr = diagnosis_short_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)diagnosis_short_map.get(key);
				System.out.println(key + ": " + count);
				out.write("diagnosis_short\t" + key + "\t" + count + "\n");
			}

			itr = diag_relapse_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();				
				int count = (Integer)diag_relapse_map.get(key);
				System.out.println(key + ": " + count);
				out.write("diag_relapse\t" + key + "\t" + count + "\n");
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
