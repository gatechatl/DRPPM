package moffitt.projects.paulorodriguez.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Program to format the TCGA files and extract melanoma samples with the BRAF V600E mutation. 
 * @author gatechatl
 *
 */
public class PauloRodriguezGenerateTCGAFormattedFiles {

	
	public static void main(String[] args) {
		
		try {
			// parsing the runtime config file
			HashMap sample_list = new HashMap();
			//String melanoma_type = "UVM";
			String melanoma_type = "SKCM";
			
			String inputClinical = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/clinical_PANCAN_patient_with_followup.tsv";
			String outputClinical = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/" + melanoma_type + "_clinical_PANCAN_patient_with_followup.tsv";
			
			FileWriter fwriter = new FileWriter(outputClinical);
			BufferedWriter out = new BufferedWriter(fwriter);			
	
			FileInputStream fstream = new FileInputStream(inputClinical);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String patient_id = split[1];
				String type = split[2];
				if (type.equals(melanoma_type)) {
					out.write(str + "\n");
					sample_list.put(patient_id, patient_id);
				}
			}
			in.close();
			out.close();
			
			HashMap braf_cases = new HashMap();
			//String braf_cases_file = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/patient_with_BRAF_V600E.txt";
			String braf_cases_file = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/added_cbioportal_BRAF_annotation.txt";
			fstream = new FileInputStream(braf_cases_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				braf_cases.put(split[0], split[0]);
			}
			in.close();

			String outputSampleNames = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/" + melanoma_type + "_sampleList.txt";
			
			FileWriter fwriter_sampleName = new FileWriter(outputSampleNames);
			BufferedWriter out_sampleName = new BufferedWriter(fwriter_sampleName);			

			/*
			String outputSampleNamesBraf = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/" + melanoma_type + "_sampleList_with_BRAFV700E.txt";		
			FileWriter fwriter_sampleNameBraf = new FileWriter(outputSampleNamesBraf);
			BufferedWriter out_sampleNameBraf = new BufferedWriter(fwriter_sampleNameBraf);			
			*/

			String outputSampleNamesBraf = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/" + melanoma_type + "_sampleList_with_BRAFevents.txt";		
			FileWriter fwriter_sampleNameBraf = new FileWriter(outputSampleNamesBraf);
			BufferedWriter out_sampleNameBraf = new BufferedWriter(fwriter_sampleNameBraf);			
			
			String expression_matrix = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.tsv";
			fstream = new FileInputStream(expression_matrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String sampleheader = in.readLine();
			String[] split_sampleheader = sampleheader.split("\t");
			for (int i = 0; i < split_sampleheader.length; i++) {
				boolean found = false;
				Iterator itr = sample_list.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					if (split_sampleheader[i].contains(sampleName)) {
						out_sampleName.write(split_sampleheader[i] + "\n");
						System.out.println(split_sampleheader[i]);
						found = true;
					}
				}
				
				if (found) {
					itr = braf_cases.keySet().iterator();
					while (itr.hasNext()) {
						String sampleName = (String)itr.next();
						if (split_sampleheader[i].contains(sampleName)) {
							out_sampleNameBraf.write(split_sampleheader[i] + "\n");
							System.out.println(split_sampleheader[i]);
							
						}
					}
				}
					
			}
			out_sampleName.close();
			out_sampleNameBraf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
