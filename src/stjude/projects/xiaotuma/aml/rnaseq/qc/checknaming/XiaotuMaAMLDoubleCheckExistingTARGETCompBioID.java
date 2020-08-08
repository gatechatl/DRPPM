package stjude.projects.xiaotuma.aml.rnaseq.qc.checknaming;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaAMLDoubleCheckExistingTARGETCompBioID {

	
	public static void main(String[] args) {
		
		try {
			
			
			
			HashMap map_wgs = new HashMap();
			HashMap map_wxs = new HashMap();
			HashMap map_rnaseq = new HashMap();
			String inputTARGETFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\pan_target_sample_info.update_Nov212016_OSbadAdded_update04062018_forPatee.txt";
			FileInputStream fstream = new FileInputStream(inputTARGETFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[6].equals("")) {
					if (map_wgs.containsKey(split[2])) {
						System.out.println(split[2] + "\t" + split[6]);
					} else {
						map_wgs.put(split[2], split[6]);
					}
				}
				if (split.length > 9) {
					if (!split[9].equals("")) {
						
						if (map_wxs.containsKey(split[2])) {
							System.out.println(split[2] + "\t" + split[9]);
						} else {
							map_wxs.put(split[2], split[9]);
						}
					}
				}
				if (split.length > 11) {
					if (!split[11].equals("")) {
						if (map_rnaseq.containsKey(split[2])) {
							System.out.println(split[2] + "\t" + split[11]);
						} else {
							map_rnaseq.put(split[2], split[11]);
						}
					}
				}
				
			}
			in.close();
			String inputFileSize = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\patient_and_sample_names_meta.txt";
			fstream = new FileInputStream(inputFileSize);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String patient_id = split[1].split("-")[0];
				System.out.println(patient_id);
				if (map_rnaseq.containsKey(patient_id)) {
					System.out.println("Found RNAseq: " + patient_id);
				}
				if (map_wxs.containsKey(patient_id)) {
					System.out.println("Found WXS: " + patient_id);
				}
				if (map_wgs.containsKey(patient_id)) {
					System.out.println("Found WGS: " + patient_id);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
