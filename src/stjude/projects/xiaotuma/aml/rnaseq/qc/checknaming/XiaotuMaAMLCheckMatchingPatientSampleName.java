package stjude.projects.xiaotuma.aml.rnaseq.qc.checknaming;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaAMLCheckMatchingPatientSampleName {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\meta_test.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String patient_id = "";
				if (split[0].contains("TARGET")) {
					patient_id = split[0].split("-")[2]; 
				} else {
					patient_id = split[0].split("-")[0];
				}
				
				map.put(split[1], patient_id);
			}
			in.close();
			
			int hits = 0;
			inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\test.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String patient_id = split[1].split("-")[2];
					if (!map.get(split[0]).equals(patient_id)) {
						System.out.println("Not Same");
					} else {
						hits++;
					}
				} else {
					System.out.println("Missing");
				}
			}
			System.out.println(hits);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
