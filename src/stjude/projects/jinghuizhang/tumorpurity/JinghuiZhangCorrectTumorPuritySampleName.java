package stjude.projects.jinghuizhang.tumorpurity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangCorrectTumorPuritySampleName {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PAN_PCGP_TARGET_Genomic_Lesion_DB\\meta_table\\Sample_Sheet_inMS.SJID.txt"; 		
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 4) {
					//System.out.println(str);
					if (map.containsKey(split[2])) {
						System.out.println(str);
					}
					map.put(split[2], split[4]);
				}
			}
			in.close();
			
			HashMap all_samples = new HashMap();
			String inputSampleFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\updated_pcgp_fpkm_zero_max.txt"; 		
			fstream = new FileInputStream(inputSampleFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				all_samples.put(split_header[i].split("_")[0], split_header[i]);
			}
			in.close();
			
			HashMap pcgp = new HashMap();
			
			HashMap hit = new HashMap();
			String inputPCGP_Purity = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_TARGET_Tumor_Purity\\Xiang_Daniel_Tumor_Purity.txt"; 		
			fstream = new FileInputStream(inputPCGP_Purity);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				if (all_samples.containsKey(split[0])) {
					System.out.println("Hit:\t" + all_samples.get(split[0]));
					hit.put(all_samples.get(split[0]), split[1]);
				} else {
					System.out.println("Missing:\t" + split[0]);
				}
			}
			in.close();
			
			String inputTARGET_Purity = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_TARGET_Tumor_Purity\\tumor_purity_from_xma_20200512.txt"; 		
			fstream = new FileInputStream(inputTARGET_Purity);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[2])) {
					hit.put(map.get(split[2]), split[3]);
					System.out.println(split[2] + "\t" + map.get(split[2]));
				}
			}
			in.close();
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_TARGET_Tumor_Purity\\Harmonized_20200606.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tPurity\n");
			Iterator itr = hit.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String purity = (String)hit.get(sampleName);
				out.write(sampleName + "\t" + purity + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
