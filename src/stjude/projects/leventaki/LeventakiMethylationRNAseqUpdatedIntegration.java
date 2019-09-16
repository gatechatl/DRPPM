package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class LeventakiMethylationRNAseqUpdatedIntegration {

	
	public static void main(String[] args) {
		
		try {

			HashMap upreg_genes = new HashMap();
			HashMap dnreg_genes = new HashMap();
			
			HashMap expr_genes = new HashMap();
			String inputFile_geneExpr = "Z:\\ResearchHome\\ProjectSpace\\mulligrp\\LeventakiALCLProject\\common\\CMPB\\BioinfoCore\\Biostats\\RNAseq\\Purity\\LIMMA\\Group2_vs_Group1_all.txt_removequotation.txt";
			
			//String inputFile_geneExpr = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\cmpb\\BioinfoCore\\ALCL_RNASEQ_ANALYSIS\\FinalAnalysisPipeline\\3_LIMMA\\HIGH_ALKMYCvsLOW_ALKMYC_ALL_REMOVEQUOTE.txt";
			FileInputStream fstream = new FileInputStream(inputFile_geneExpr);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double avg_expr = new Double(split[2]);
				expr_genes.put(split[0], avg_expr);
				
				double fdr = new Double(split[5]);
				double log2FC = new Double(split[1]);
				if (fdr < 0.05) {
					if (log2FC > 0.8) {
						upreg_genes.put(split[0], split[0]);
					}
					if (log2FC < -0.8) {
						dnreg_genes.put(split[0], split[0]);
					}
				}
			}
			in.close();
			
			HashMap up_reg_list = new HashMap();
			HashMap dn_reg_list = new HashMap();

			String output = "Z:\\ResearchHome\\ProjectSpace\\mulligrp\\LeventakiALCLProject\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Integration\\ALCL_RNAseq_Methylation_TSS_Integration_20190819_updated.txt";
			//String output = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Integration\\ALCL_RNAseq_Methylation_TSS_Integration.txt";			
			FileWriter fwriter = new FileWriter(output);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			LinkedList list = new LinkedList();
			HashMap final_expr = new HashMap();
			String inputFile_TSS = "Z:\\ResearchHome\\ProjectSpace\\mulligrp\\LeventakiALCLProject\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Integration\\ALCL_Methylation_TSS_MAplot_Raw.txt";
			//String inputFile_TSS = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Integration\\ALCL_Methylation_TSS_MAplot_Raw.txt";
			fstream = new FileInputStream(inputFile_TSS);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\tRNAseq_Expr\n");
			while (in.ready()) {
				String str = in.readLine();
				
				list.add(str);
				String[] split = str.split("\t");
				double log2FC_methyl = new Double(split[2]);
				String final_name = "";
				HashMap uniq_name = new HashMap();
				for (String methyl_gene: split[0].split(";")) {
					uniq_name.put(methyl_gene, methyl_gene);
				}
				Iterator itr = uniq_name.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String)itr.next();
					if (final_name.equals("")) {
						final_name = key;
					} else {
						final_name += ";" + key;
					}
				}
				final_name = split[0];
				
				double max_expr = -999;
				
				for (String methyl_gene: split[0].split(";")) {
					uniq_name.put(methyl_gene, methyl_gene);
					if (expr_genes.containsKey(methyl_gene)) {
						//System.out.println("hit");
						double expr = (Double)expr_genes.get(methyl_gene);
						if (expr > max_expr) {
							max_expr = expr;
						}
					}
					
					if (upreg_genes.containsKey(methyl_gene)) {
						//if (log2FC_methyl > 0.8 || log2FC_methyl < -0.8) {
						if (log2FC_methyl > 0.8) {
							up_reg_list.put(final_name, split[0]);
						}
					}
					/*if (dnreg_genes.containsKey(methyl_gene)) {
						if (log2FC_methyl > 0.8 || log2FC_methyl < -0.8) {
							dn_reg_list.put(final_name, split[0]);
						}
					}*/
				}								
				final_expr.put(final_name, max_expr);
				
				
				if (max_expr > -999) {
					out.write(final_name + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + max_expr + "\n");
				}
			}
			in.close();
			out.close();
			String output_upreg_file = "Z:\\ResearchHome\\ProjectSpace\\mulligrp\\LeventakiALCLProject\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Integration\\UpRegMethyl_20190819_updated.txt";
			//String output_upreg_file = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Integration\\UpRegMethyl.txt";			
			FileWriter fwriter_upreg = new FileWriter(output_upreg_file);
			BufferedWriter out_upreg = new BufferedWriter(fwriter_upreg);
			Iterator itr = up_reg_list.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out_upreg.write(key + " red\n");
			}
			out_upreg.close();
			
			String output_dnreg_file = "Z:\\ResearchHome\\ProjectSpace\\mulligrp\\LeventakiALCLProject\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Integration\\DnRegMethyl_20190819_updated.txt";
			//String output_dnreg_file = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Integration\\DnRegMethyl.txt";			
			FileWriter fwriter_dnreg = new FileWriter(output_dnreg_file);
			BufferedWriter out_dnreg = new BufferedWriter(fwriter_dnreg);
			itr = dn_reg_list.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out_dnreg.write(key + " blue\n");
			}
			out_dnreg.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
