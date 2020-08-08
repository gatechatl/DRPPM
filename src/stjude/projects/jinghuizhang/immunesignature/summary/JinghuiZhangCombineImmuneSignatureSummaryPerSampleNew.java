package stjude.projects.jinghuizhang.immunesignature.summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

public class JinghuiZhangCombineImmuneSignatureSummaryPerSampleNew {

	
	
	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			//String inputSubtype = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_target_annotation_immunesubtype.txt";
			String inputSubtype = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\Solid_Brain_pcgp_target_annotation_immunesubtype.txt";
			FileInputStream fstream = new FileInputStream(inputSubtype );
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			in.close();
			

			//String outputCibersortFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\PCGP_Comprehensive_Cibersort_Result_immunesubtype_20200315.txt";
			String outputCibersortFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\Solid_Brain_Comprehensive_Cibersort_Result_immunesubtype_20200606.txt";
			FileWriter fwriter = new FileWriter(outputCibersortFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap frequency_count_disease = new HashMap();
			HashMap disease_type_map = new HashMap();
			HashMap immune_subtype_map = new HashMap();
			String cibersortFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\PCGP_Comprehensive_Cibersort_Result_20190530.txt";
			fstream = new FileInputStream(cibersortFile );
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(header + "\tLymphocyte\tMacrophage\tDendritic_cells\tMast\tNeutrophils\tEosinophils\tCancerType\tImmuneSubtype\n");
			String[] lymphocytes = {"Ciber B cells naive", "Ciber B cells memory", "Ciber Plasma cells", "Ciber T cells CD8", "Ciber T cells CD4 naive", "Ciber T cells CD4 memory resting", "Ciber T cells CD4 memory activated", "Ciber T cells follicular helper", "Ciber T cells regulatory (Tregs)", "Ciber T cells gamma delta", "Ciber NK cells resting", "Ciber NK cells activated"};
			String[] macrophages = {"Ciber Monocytes", "Ciber Macrophages M0", "Ciber Macrophages M1", "Ciber Macrophages M2"};
			String[] dendritic = {"Ciber Dendritic cells resting", "Ciber Dendritic cells activated"};
			String[] neutrophiles = {"Ciber Neutrophils"};
			String[] mast = {"Ciber Mast cells resting", "Ciber Mast cells activated"};
			String[] eosinophils = {"Ciber Eosinophils"};

			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String type = split[0].split("-")[0].replaceAll("0", "").replaceAll("1", "").replaceAll("2", "").replaceAll("3", "").replaceAll("4", "").replaceAll("5", "").replaceAll("6", "").replaceAll("7", "").replaceAll("8", "").replaceAll("9", "");
				//split[0] = split[0].replaceAll("-", ".");
				split[0] = split[0].split("-")[0];
				if (map.containsKey(split[0])) {
					String immune_subtype = (String)map.get(split[0]); 
					double[] lymphocytes_values = new double[lymphocytes.length];
					int lymphocytes_index = 0;
					double[] macrophages_values = new double[macrophages.length];
					int macrophages_index = 0;
					double[] dendritic_values = new double[dendritic.length];
					int dendritic_index = 0;
					double[] neutrophiles_values = new double[neutrophiles.length];
					int neutrophiles_index = 0;
					double[] mast_values = new double[mast.length];
					int mast_index = 0;
					double[] eosinophils_values = new double[eosinophils.length];
					int eosinophils_index = 0;
					for (int i = 1; i < split.length; i++) {
						for (int j = 0; j < lymphocytes.length; j++) {
							if (lymphocytes[j].equals(split_header[i])) {
								lymphocytes_values[j] = new Double(split[i]);
							}
						}
						for (int j = 0; j < dendritic.length; j++) {
							if (dendritic[j].equals(split_header[i])) {
								dendritic_values[j] = new Double(split[i]);
							}
						}
						for (int j = 0; j < macrophages.length; j++) {
							if (macrophages[j].equals(split_header[i])) {
								macrophages_values[j] = new Double(split[i]);
							}
						}
						for (int j = 0; j < neutrophiles.length; j++) {
							if (neutrophiles[j].equals(split_header[i])) {
								neutrophiles_values[j] = new Double(split[i]);
							}
						}
						for (int j = 0; j < eosinophils.length; j++) {
							if (eosinophils[j].equals(split_header[i])) {
								eosinophils_values[j] = new Double(split[i]);
							}
						}
						for (int j = 0; j < mast.length; j++) {
							if (mast[j].equals(split_header[i])) {
								mast_values[j] = new Double(split[i]);
							}
						}
					}
					double lymphocytes_sum = MathTools.sum(lymphocytes_values);
					double macrophages_sum = MathTools.sum(macrophages_values);
					double dendritic_sum = MathTools.sum(dendritic_values);
					double mast_sum = MathTools.sum(mast_values);
					double neutrophiles_sum = MathTools.sum(neutrophiles_values);
					double eosinophils_sum = MathTools.sum(eosinophils_values);
					out.write(str + "\t" + lymphocytes_sum + "\t" + macrophages_sum + "\t" + dendritic_sum + "\t" + mast_sum + "\t" + neutrophiles_sum + "\t" + eosinophils_sum  + "\t" + type + "\t" + immune_subtype + "\n");
					disease_type_map.put(type, type);
					immune_subtype_map.put(immune_subtype, immune_subtype);
					if (frequency_count_disease.containsKey(type + "\t" + immune_subtype)) {
						int count = (Integer)frequency_count_disease.get(type + "\t" + immune_subtype);
						count = count + 1;
						frequency_count_disease.put(type + "\t" + immune_subtype, count);
					} else {
						frequency_count_disease.put(type + "\t" + immune_subtype, 1);
					}
				}
			}
			in.close();
			out.close();
			
			String outputImmuneDistributionFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\Disease_immunesubtype_distribution_20200606.txt";
			//String outputImmuneDistributionFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\Disease_immunesubtype_distribution_20200315.txt";
			fwriter = new FileWriter(outputImmuneDistributionFile);
			out = new BufferedWriter(fwriter);
			out.write("Disease");
			Iterator itr2 = immune_subtype_map.keySet().iterator();
			while (itr2.hasNext()) {
				String immune_subtype = (String)itr2.next();
				out.write("\t" + immune_subtype);
			}
			out.write("\n");
			Iterator itr = disease_type_map.keySet().iterator();
			while (itr.hasNext()) {
				String disease = (String)itr.next();
				out.write(disease);
				itr2 = immune_subtype_map.keySet().iterator();
				while (itr2.hasNext()) {
					String immune_subtype = (String)itr2.next();
					int count = 0;
					if (frequency_count_disease.containsKey(disease + "\t" + immune_subtype)) {
						count = (Integer)frequency_count_disease.get(disease + "\t" + immune_subtype);
					}
					out.write("\t" + count);
				}
				out.write("\n");
			}
			out.close();
			
/*
			String outputImmuneFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\solid_brain_pcgp_immune_ssGSEA_immune_subgroup_annotation.txt";
			FileWriter fwriter_immune = new FileWriter(outputImmuneFile);
			BufferedWriter out_immune = new BufferedWriter(fwriter_immune);

			String immuneFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_immune_ssGSEA_20191016.txt";
			fstream = new FileInputStream(immuneFile );
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out_immune.write(header + "\tImmuneSubtype\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll("-", ".");
				if (map.containsKey(split[0])) {
					String subgroup = (String)map.get(split[0]);
					out_immune.write(str + "\t" + subgroup + "\n");
				}
			}
			in.close();
			out_immune.close();
			
			// add T cell annotation
			String outputTcellFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_tcell_immune_ssGSEA_subgroup_annotation.txt";
			FileWriter fwriter_Tcell = new FileWriter(outputTcellFile);
			BufferedWriter out_Tcell = new BufferedWriter(fwriter_Tcell);

			String t_cell_immuneFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_tcell_immune_ssGSEA.txt";
			fstream = new FileInputStream(t_cell_immuneFile );
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out_Tcell.write(header + "\tImmuneSubtype\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll("-", ".");
				if (map.containsKey(split[0])) {
					String subgroup = (String)map.get(split[0]);
					out_Tcell.write(str + "\t" + subgroup + "\n");
				}
			}
			in.close();
			out_Tcell.close();
			
			

			// add estimate annotation
			String outputEstimateFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\Estimate\\updated_pcgp_fpkm_zero.common.affy.estimate.cleanDesc.T.annot.txt";
			FileWriter fwriter_Estimate = new FileWriter(outputEstimateFile);
			BufferedWriter out_Estimate = new BufferedWriter(fwriter_Estimate);

			String estimate_immuneFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\Estimate\\updated_pcgp_fpkm_zero.common.affy.estimate.cleanDesc.T.txt";
			fstream = new FileInputStream(estimate_immuneFile );
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out_Estimate.write(header + "\tImmuneSubtype\tDisease\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String disease = split[0].split("\\.")[0].replaceAll("0", "").replaceAll("1", "").replaceAll("2", "").replaceAll("3", "").replaceAll("4", "").replaceAll("5", "").replaceAll("6", "").replaceAll("7", "").replaceAll("8", "").replaceAll("9", "");
				split[0] = split[0].replaceAll("-", ".");
				if (map.containsKey(split[0])) {
					String subgroup = (String)map.get(split[0]);					
					out_Estimate.write(str + "\t" + subgroup + "\t" + disease + "\n");
				}
			}
			in.close();
			out_Estimate.close();
			
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
