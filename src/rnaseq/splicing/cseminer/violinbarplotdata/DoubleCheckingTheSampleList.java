package rnaseq.splicing.cseminer.violinbarplotdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class DoubleCheckingTheSampleList {

	public static void main(String[] args) {
		
		try {
			
			HashMap ped_sample_map = new HashMap();
			String inputPediatricCancerFile = "/Users/4472414/Projects/CSIMiner/ViolinPlotBarPlot/EnsureGoodSampleName/Header_Pediatric_Cancer_FPKM_final.txt";
			FileInputStream fstream = new FileInputStream(inputPediatricCancerFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_ped_header = header.split("\t");
			for (String name: split_ped_header) {
				ped_sample_map.put(name, name);
			}
			in.close();
			
			HashMap normal_sample_map = new HashMap();
			String inputNormalCancerFile = "/Users/4472414/Projects/CSIMiner/ViolinPlotBarPlot/EnsureGoodSampleName/Header_GTEx_7526_FPKM_final.txt";
			fstream = new FileInputStream(inputNormalCancerFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			String[] split_normal_header = header.split("\t");
			for (String name: split_normal_header) {
				normal_sample_map.put(name, name);
			}
			in.close();
			
			String output_GTEx_SampleID2Histology_clean = "/Users/4472414/Projects/CSIMiner/ViolinPlotBarPlot/EnsureGoodSampleName/GTEx_SampleID2Histology_updated_clean.txt";
			FileWriter fwriter_gtex_sample = new FileWriter(output_GTEx_SampleID2Histology_clean);
			BufferedWriter out_gtex_sample = new BufferedWriter(fwriter_gtex_sample);
			
			
			HashMap map_normal_type = new HashMap();
			String inputGTExSampleListFile = "/Users/4472414/Projects/CSIMiner/ViolinPlotBarPlot/EnsureGoodSampleName/GTEx_SampleID2Histology_updated.txt";
			fstream = new FileInputStream(inputGTExSampleListFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (normal_sample_map.containsKey(split[0])) {
					out_gtex_sample.write(str + "\n");
					if (map_normal_type.containsKey(split[1])) {
						int prev_count = (Integer)map_normal_type.get(split[1]);
						prev_count++;
						map_normal_type.put(split[1], prev_count);
					} else {
						map_normal_type.put(split[1], 1);
					}
				}
			}
			in.close();
			
			Iterator itr = map_normal_type.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				int count = (Integer)map_normal_type.get(name);
				System.out.println(name + "\t" + count);
			}
			
			String output_Diagnosis_Solid_Clinical_PCGP_TARGET_Sample2DiseaseType_clean = "/Users/4472414/Projects/CSIMiner/ViolinPlotBarPlot/EnsureGoodSampleName/Diagnosis_Solid_Clinical_PCGP_TARGET_Sample2DiseaseType_Updated_clean.txt";
			FileWriter fwriter_solid_sample = new FileWriter(output_Diagnosis_Solid_Clinical_PCGP_TARGET_Sample2DiseaseType_clean);
			BufferedWriter out_solid_sample = new BufferedWriter(fwriter_solid_sample);
			
			String inputSolidSampleListFile = "/Users/4472414/Projects/CSIMiner/ViolinPlotBarPlot/EnsureGoodSampleName/Diagnosis_Solid_Clinical_PCGP_TARGET_Sample2DiseaseType_Updated.txt";
			fstream = new FileInputStream(inputSolidSampleListFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (ped_sample_map.containsKey(split[0])) {
					out_solid_sample.write(str + "\n");
				}
			}
			in.close();
			out_solid_sample.close();
			

			String output_Diagnosis_Brain_Clinical_PCGP_TARGET_Sample2DiseaseType_clean = "/Users/4472414/Projects/CSIMiner/ViolinPlotBarPlot/EnsureGoodSampleName/Diagnosis_Brain_Clinical_PCGP_TARGET_Sample2DiseaseType_Updated_clean.txt";
			FileWriter fwriter_brain_sample = new FileWriter(output_Diagnosis_Brain_Clinical_PCGP_TARGET_Sample2DiseaseType_clean);
			BufferedWriter out_brain_sample = new BufferedWriter(fwriter_brain_sample);
			
			String inputBrainSampleListFile = "/Users/4472414/Projects/CSIMiner/ViolinPlotBarPlot/EnsureGoodSampleName/Diagnosis_Brain_Clinical_PCGP_TARGET_Sample2DiseaseType_Updated.txt";
			fstream = new FileInputStream(inputBrainSampleListFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (ped_sample_map.containsKey(split[0])) {
					out_brain_sample.write(str + "\n");
				}
			}
			in.close();
			out_brain_sample.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
