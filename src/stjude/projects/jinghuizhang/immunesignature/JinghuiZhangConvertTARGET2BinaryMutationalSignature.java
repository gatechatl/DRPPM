package stjude.projects.jinghuizhang.immunesignature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * After discussing with Sam, he suggested that I generate two level of binary mutational signature.
 * For each disease, cutoff at median or 0 for each mutational signature.
 * Output will be two binary value for each sample.
 * @author tshaw
 *
 */
public class JinghuiZhangConvertTARGET2BinaryMutationalSignature {

	public static void main(String[] args) {
		
		try {
			String outputPresentAbsent = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PAN_PCGP_TARGET_Genomic_Lesion_DB\\mutation_signature\\Header_TARGET_SingSampleOutput_Cosmic30cutPlusNovel.numMut.cosine_present_absent.txt";
			FileWriter fwriter_pa = new FileWriter(outputPresentAbsent);
			BufferedWriter out_pa = new BufferedWriter(fwriter_pa);

			String outputMediumCutoff = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PAN_PCGP_TARGET_Genomic_Lesion_DB\\mutation_signature\\Header_TARGET_SingSampleOutput_Cosmic30cutPlusNovel.numMut.cosine_cutoff.txt";
			FileWriter fwriter_cutoff = new FileWriter(outputMediumCutoff);
			BufferedWriter out_cutoff = new BufferedWriter(fwriter_cutoff);

			String outputIDConvert= "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PAN_PCGP_TARGET_Genomic_Lesion_DB\\mutation_signature\\Header_TARGET_SingSampleOutput_Cosmic30cutPlusNovel.numMut.cosine_id_conversion.txt";
			FileWriter fwriter_id = new FileWriter(outputIDConvert);
			BufferedWriter out_id = new BufferedWriter(fwriter_id);

			HashMap patient2sjid = new HashMap();
			String patient_id_conversion = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PAN_PCGP_TARGET_Genomic_Lesion_DB\\PanTARGET\\temp_patient_id_conversion.txt"; 		
			FileInputStream fstream = new FileInputStream(patient_id_conversion);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[2].equals("nosjid")) {
					patient2sjid.put(split[0], split[2]);
				} else if (!split[3].equals("nosjid")) {
					patient2sjid.put(split[0], split[3]);
				} else if (!split[4].equals("nosjid")) {
					patient2sjid.put(split[0], split[4]);
				}
				
			}
			in.close();
						
			HashMap type2sampleName = new HashMap();
			HashMap type2sampleName_values = new HashMap();
			String inputMutationalSignature = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PAN_PCGP_TARGET_Genomic_Lesion_DB\\mutation_signature\\Header_TARGET_SingSampleOutput_Cosmic30cutPlusNovel.numMut.cosine.txt"; 		
			fstream = new FileInputStream(inputMutationalSignature);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
						
			String[] split_header = header.split("\t");
			String replace_header = split_header[0];
			for (int i = 1; i < split_header.length; i++) {
				String patientid = split_header[i].split("-")[2];
				if (patient2sjid.containsKey(patientid)) {
					replace_header += "\t" + patient2sjid.get(patientid);
				} else {
					replace_header += "\t" + patientid; 
				}
			}
			split_header = replace_header.split("\t");
			out_pa.write(split_header[0]);
			out_cutoff.write(split_header[0]);
			out_id.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				String type = split_header[i].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0].split("0")[0];
				
				if (type2sampleName.containsKey(type)) {
					LinkedList list = (LinkedList)type2sampleName.get(type);
					list.add(i);
					type2sampleName.put(type, list);					
				} else {
					LinkedList list = new LinkedList();
					list.add(i);
					type2sampleName.put(type, list);
				}
			}
			
			Iterator itr = type2sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				LinkedList list = (LinkedList)type2sampleName.get(type);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					int sampleName_index = (Integer)itr2.next();
					out_pa.write("\t" + split_header[sampleName_index]);
					out_cutoff.write("\t" + split_header[sampleName_index]);
					out_id.write("\t" + split_header[sampleName_index]);
				}
			}
			
			out_pa.write("\n");
			out_cutoff.write("\n");
			out_id.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out_pa.write(split[0]);
				out_cutoff.write(split[0]);				
				out_id.write(split[0]);
				HashMap median_map = new HashMap();
				itr = type2sampleName.keySet().iterator();
				while (itr.hasNext()) {
					String type = (String)itr.next();
					LinkedList values_list = new LinkedList();
					LinkedList list = (LinkedList)type2sampleName.get(type);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						int sampleName_index = (Integer)itr2.next();
						values_list.add(new Double(split[sampleName_index]));					
					}
					double[] values = MathTools.convertListDouble2Double(values_list);
					double median_values = MathTools.median(values);
					median_map.put(type, median_values);
					System.out.println(split[0] + "\t" + type + "\t" + median_values);
				}												
				
				double[] values = new double[split.length - 1];
				
			
				
				itr = type2sampleName.keySet().iterator();
				while (itr.hasNext()) {
					String type = (String)itr.next();
					double median_values = (Double)median_map.get(type);
					LinkedList values_list = new LinkedList();
					LinkedList list = (LinkedList)type2sampleName.get(type);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						int sampleName_index = (Integer)itr2.next();	
						out_id.write("\t" + split[sampleName_index]);
						if (new Double(split[sampleName_index]) > median_values) {
							out_cutoff.write("\t" + "true");
						} else {
							out_cutoff.write("\t" + "false");
						}
						if (new Double(split[sampleName_index]) > 0) {
							out_pa.write("\t" + "true");
						} else {
							out_pa.write("\t" + "false");
						}
					}
				}				
				out_pa.write("\n");
				out_cutoff.write("\n");
				out_id.write("\n");
			}			
			in.close();
			out_pa.close();
			out_cutoff.close();
			out_id.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
