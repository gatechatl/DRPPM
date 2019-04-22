package stjude.projects.jinghuizhang.usp7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * For TARGET T-ALL cohort, merge MRD Survival information with USP7 mutation status subgroup and gene activities
 * @author tshaw
 *
 */
public class JinghuiZhangMergeTARGETPatientMRDInfo {

	
	public static void main(String[] args) {
		
		try {

			
			HashMap TALL_samples = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\annotation.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				TALL_samples.put(split[0], str);
				
			}
			in.close();
			
			HashMap sample2patientID = new HashMap();
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\harmonized_pantarget_diagnosis_snvindel_redundant_07202017.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));	
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample2patientID.put(split[83], split[4]);
				sample2patientID.put(split[87], split[4]);
				sample2patientID.put(split[89], split[4]);
			}
			/*
			HashMap ssGSEA_samples = new HashMap();
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\ssGSEA_activity.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String activity_header = in.readLine();
			String[] split_activity_header = activity_header.split("\t");
			System.out.println(split_activity_header.length);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				ssGSEA_samples.put(split[0], str);
			}*/
			
			HashMap sample2activity = new HashMap();
			//inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\NetBID_TcellNetwork_TALL_GeneActivity.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\ssGSEA_activity.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String activity_header = in.readLine();
			String[] split_activity_header = activity_header.split("\t");
			System.out.println(split_activity_header.length);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sample2activity.put(split[0], str);
			}
			
			HashMap final_sample2patientID = new HashMap();
			HashMap final_patientID2sample = new HashMap();
			Iterator itr = TALL_samples.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (sample2patientID.containsKey(sampleName)) {
					String patientID = (String)sample2patientID.get(sampleName);
					final_sample2patientID.put(sampleName, patientID);
					final_patientID2sample.put(patientID, sampleName);
				}
			}
			
			HashMap meta_sampleName = new HashMap();
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\TARGET_Clinical_Info.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String clinical_header =  in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].split("-").length > 2) {
					String patientID = split[0].split("-")[2];
					if (final_patientID2sample.containsKey(patientID)) {						
						String sampleName = (String)final_patientID2sample.get(patientID);
						if (sample2activity.containsKey(sampleName)) {
							String activity_str = (String)sample2activity.get(sampleName);
							String orig_info = (String)TALL_samples.get(sampleName);
							meta_sampleName.put(sampleName, orig_info + "\t" + str + "\t" + activity_str);
						}
					}
				}
			}
			System.out.println(meta_sampleName.size());
			System.out.println(final_patientID2sample.size());
			
			
			//String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\Comprehensive_Clinical_Information_GeneActivity_20190219.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\Comprehensive_Clinical_Information_GeneActivity_20190314.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			//out.write(header + "\t" + clinical_header);
			for (String geneName: split_activity_header) {
			//	out.write("\t" + geneName);
			}
			//out.write("\n");
			itr = meta_sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName= (String)itr.next();
				String line = (String)meta_sampleName.get(sampleName);
				out.write(line + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
