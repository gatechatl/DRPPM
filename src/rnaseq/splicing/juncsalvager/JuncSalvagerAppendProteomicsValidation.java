package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JuncSalvagerAppendProteomicsValidation {

	public static String description() {
		return "Prioritize the exon candidate list";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputCandidateFile] [inputProteomicHitFile] [inputTranslationFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputMatrixFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_Filtered_Candidates_20200212.txt";
			String inputProteomicHitFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_Filtered_Candidates_20200212.proteomics_hit.txt";
			String inputTranslationFile = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_Filtered_Candidates_20200212.translation.txt";
			String proteomics_data_prefix = args[3];
			String outputFile = args[4]; ///"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_For_HTML.txt";			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			HashMap rms_validation = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputProteomicHitFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].replaceAll(">", "").replaceAll("_Other", "").replaceAll("_Novel_PanCan", "").replaceAll("_Novel_ECM", "").replaceAll("_KnownECM", "").replaceAll("_PanCan", "");
				rms_validation.put(name, "");
			}
			in.close();
			
			HashMap exon_accession = new HashMap();
			HashMap exon_peptide = new HashMap();
			
			fstream = new FileInputStream(inputTranslationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			//header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].replaceAll(">", "").replaceAll("_Other", "").replaceAll("_Novel_PanCan", "").replaceAll("_Novel_ECM", "").replaceAll("_KnownECM", "").replaceAll("_PanCan", "");
				if (exon_accession.containsKey(name)) {
					String prev = (String)exon_accession.get(name);
					prev += "," + split[2] + "_" + split[3] + "_" + split[4];
				} else {
					exon_accession.put(name, split[2] + "_" + split[3] + "_" + split[4]);
				}
				exon_peptide.put(name, split[5]);
			}
			in.close();
			
			
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\t" + proteomics_data_prefix + "_protein_accession\t" + proteomics_data_prefix + "_peptide\t" + proteomics_data_prefix + "_validation_flag\n");
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon_name = split[0];
				exon_name = exon_name.replaceAll(">", "").replaceAll("_Other", "").replaceAll("_Novel_PanCan", "").replaceAll("_Novel_ECM", "").replaceAll("_KnownECM", "").replaceAll("_PanCan", "");
				String peptide = "NA";
				if (exon_peptide.containsKey(exon_name)) {
					peptide = (String)exon_peptide.get(exon_name);
				}
				String protein_accession = "NA";
				if (exon_accession.containsKey(exon_name)) {
					protein_accession = (String)exon_accession.get(exon_name);
				}
				boolean rms_validation_flag = false;
				if (rms_validation.containsKey(exon_name)) {
					rms_validation_flag = true;
				}				
				out.write(str + "\t" + protein_accession + "\t" + peptide + "\t" + rms_validation_flag + "\n");

			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
