package stjude.projects.xiaotuma.aml.rnaseq.qc.checknaming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class XiaotuMaAMLGenerateIntakeSampleFile {

	
	public static void main(String[] args) {
		
		try {
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\patient_and_sample_names_meta.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFileSize = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\patient_and_sample_names.txt";
			FileInputStream fstream = new FileInputStream(inputFileSize);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[1];
				String clean_sampleName = sampleName.replaceAll("TARGET-20-", "").replaceAll("TARGET-00-", "");
				String[] split_name = clean_sampleName.split("-");
				String tissue_id = split_name[1];
				String nuc_acid_id = split_name[2];
				String tissue_name = check_tissue_type(tissue_id);
				String nuc_acid_name = check_nucleic_acid_code(nuc_acid_id);
				System.out.println(sampleName + "\t" + clean_sampleName + "\t" + tissue_id + "\t" + tissue_name + "\t" + nuc_acid_id + "\t" + nuc_acid_name);
				out.write(sampleName + "\t" + clean_sampleName + "\t" + tissue_id + "\t" + tissue_name + "\t" + nuc_acid_id + "\t" + nuc_acid_name + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String check_nucleic_acid_code(String nuc_acid_id) {
		if (nuc_acid_id.contains("D")) {
			return "unamplified_from_the_first_isolationof_a_tissue";
		}
		if (nuc_acid_id.contains("W")) {
			return "WGA_by_Qiagen_1_whole_genome?";
		}
		if (nuc_acid_id.contains("X")) {
			return "WGA_by_Qiagen_2_exome?";
		}
		if (nuc_acid_id.contains("R")) {
			return "RNA";
		}
		return nuc_acid_id;
	}
	public static String check_tissue_type(String id) {
		if (id.contains("01")) {
			return "Primary_Tumor";
		}
		if (id.contains("02")) {
			return "Recurrent_Tumor";
		}
		if (id.contains("03")) {
			return "Primary_Blood_Cancer_Peripheral_Blood";
		}
		if (id.contains("04")) {
			return "Recurrent_Blood_Cancer_Bone_Marrow";
		}
		if (id.contains("05")) {
			return "Additional_New_Primary";
		}
		if (id.contains("06")) {
			return "Metastatic";
		}
		if (id.contains("07")) {
			return "Additional_Metastatic";
		}
		if (id.contains("08")) {
			return "Post_neo-adjuvant_therapy";
		}
		if (id.contains("09")) {
			return "Primary_Blood_Cancer_BM";
		}
		if (id.contains("10")) {
			return "Blood_Derived_Normal";
		}
		if (id.contains("11")) {
			return "Solid_Tissue_Normal";
		}
		if (id.contains("12")) {
			return "Buccal_Cell_Normal";
		}
		if (id.contains("13")) {
			return "EBV_Normal";
		}
		if (id.contains("14")) {
			return "BM_Normal";
		}
		if (id.contains("15")) {
			return "Fibroblast_Normal";
		}
		if (id.contains("16")) {
			return "Mononuclear_Cell_Normal";
		}
		if (id.contains("20")) {
			return "Cell_Line_Control";
		}
		if (id.contains("40")) {
			return "Recurrent_Blood_Cancer_Peripheral_Blood";
		}
		if (id.contains("41")) {
			return "Post_Treatment_Blood_Cancer_Bone_Marrow";
		}
		if (id.contains("42")) {
			return "Post_Treatment_Blood_Cancer_Blood";
		}
		if (id.contains("50")) {
			return "Cancer_Cell_Line";
		}
		if (id.contains("60")) {
			return "Xenograft_Primary";
		}
		if (id.contains("61")) {
			return "Xenograft_Cell-line_Derived";
		}
		if (id.contains("99")) {
			return "Granulocytes";
		}
		return id;
	}
}
