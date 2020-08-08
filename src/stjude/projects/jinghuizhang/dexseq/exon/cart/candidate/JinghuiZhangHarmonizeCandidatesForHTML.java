package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangHarmonizeCandidatesForHTML {

	
	public static void main(String[] args) {
		
		try {
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_For_HTML.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("ExonID\tGTEx\tPediatric_Cancer\tgeneSymbol\thg38_chr\thg38_start\thg38_end\tProteinAccession\tPeptide\tRMS_Proteomics_Validation\tAnnotation\tExpressedDisease\tNormal_Some_Expression\tNormal_Above_Medium_Expr\tBlank\n");

			
			String outputFile_PanCan = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_For_HTML_PanCan.txt";
			FileWriter fwriter_PanCan = new FileWriter(outputFile_PanCan);
			BufferedWriter out_PanCan = new BufferedWriter(fwriter_PanCan);
			out_PanCan.write("ExonID\tGTEx\tPediatric_Cancer\tgeneSymbol\thg38_chr\thg38_start\thg38_end\tProteinAccession\tPeptide\tRMS_Proteomics_Validation\tAnnotation\tExpressedDisease\tNormal_Some_Expression\tNormal_Above_Medium_Expr\tBlank\n");

			HashMap rms_validation = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_Filtered_Candidates_20200212.proteomics_hit.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].replaceAll(">", "").replaceAll("_Novel_PanCan", "").replaceAll("_Novel_ECM", "").replaceAll("_KnownECM", "");
				rms_validation.put(name, "");
			}
			in.close();
			
			HashMap exon_accession = new HashMap();
			HashMap exon_peptide = new HashMap();
			inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_Filtered_Candidates_20200212.translation.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].replaceAll(">", "").replaceAll("_Novel_PanCan", "").replaceAll("_Novel_ECM", "").replaceAll("_KnownECM", "");
				if (exon_accession.containsKey(name)) {
					String prev = (String)exon_accession.get(name);
					prev += "," + split[2] + "_" + split[3] + "_" + split[4];
				} else {
					exon_accession.put(name, split[2] + "_" + split[3] + "_" + split[4]);
				}
				exon_peptide.put(name, split[5]);
			}
			in.close();
			
			inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\PCGP_TARGET_GTEx_Filtered_Candidates_20200212.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon_name = split[0];
				String geneName = exon_name.split("\\|")[0].split("_")[0];
				String chr = split[19];
				String start = split[20];
				String end = split[21];
				if (new Integer(end) - new Integer(start) >= 5) {
					String annotation = split[23];
					
					String highly_expressed_disease = "";
					String normal_tissue_median = "";
					String normal_tissue_some = "";
					String peptide = "NA";
					if (exon_peptide.containsKey(exon_name)) {
						peptide = (String)exon_peptide.get(exon_name);
					}
					String protein_accession = "NA";
					if (exon_accession.containsKey(exon_name)) {
						protein_accession = (String)exon_accession.get(exon_name);
					}
					double pcgp_target_score = 0;
					double gtex_score = 0;
					for (int i = 1; i <= 15; i++) {
						if (new Double(split[i]) >= 3) {
							highly_expressed_disease += "PCGP_" + split_header[i] + ",";
						} 
						pcgp_target_score += new Double(split[i]);
					}
					for (int i = 26; i <= 31; i++) {
						if (new Double(split[i]) >= 3) {
							highly_expressed_disease += "TARGET_" + split_header[i] + ",";
						}
						pcgp_target_score += new Double(split[i]);
					}
					pcgp_target_score = pcgp_target_score / (15 + 6);
					for (int i = 47; i <= 77; i++) {
						if (new Double(split[i]) >= 2) {
							normal_tissue_median += split_header[i] + ",";
						} else if (new Double(split[i]) >= 1) {
							normal_tissue_some += split_header[i] + ",";
						} 
						gtex_score += new Double(split[i]);
					}
					gtex_score = gtex_score / (77 - 47 + 1);
					boolean rms_validation_flag = false;
					if (rms_validation.containsKey(exon_name)) {
						rms_validation_flag = true;
					}
					if (annotation.equals("Novel_PanCan")) {
						out_PanCan.write(exon_name + "\t" + gtex_score + "\t" + pcgp_target_score + "\t" + geneName + "\t" + chr + "\t" + start + "\t" + end + "\t" + protein_accession + "\t" + peptide + "\t" + rms_validation_flag + "\t" + annotation + "\t" + highly_expressed_disease + "\t" + normal_tissue_some + "\t" + normal_tissue_median + "\tblank\n");
					} else {
						out.write(exon_name + "\t" + gtex_score + "\t" + pcgp_target_score + "\t" + geneName + "\t" + chr + "\t" + start + "\t" + end + "\t" + protein_accession + "\t" + peptide + "\t" + rms_validation_flag + "\t" + annotation + "\t" + highly_expressed_disease + "\t" + normal_tissue_some + "\t" + normal_tissue_median + "\tblank\n");
					}
					//out.write("ExonID\tGTEx\tPediatric_Cancer\tgeneSymbol\tProteinAccession\tPeptide\tExpressedDisease\tNormal_Some_Expression\tNormal_Above_Medium_Expr\n");
				}
			}
			in.close();
			out.close();
			out_PanCan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
