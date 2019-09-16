package stjude.projects.jinghuizhang.fusion2expression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Append the matrix that Karol provided
 * @author tshaw
 *
 */
public class JinghuiZhangCorrelateFusion2ImmuneMatrix {

	public static void main(String[] args) {
		
		try {

			HashMap cd247_gene_expr = new HashMap();
			HashMap cd2_gene_expr = new HashMap();
			HashMap cd3e_gene_expr = new HashMap();
			HashMap gzmh_gene_expr = new HashMap();
			HashMap nkg7_gene_expr = new HashMap();
			HashMap prf1_gene_expr = new HashMap();
			HashMap gzmk_gene_expr = new HashMap();
			HashMap pd_l1_gene_expr = new HashMap();
			String inputFPKMFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\pcgp_fpkm.txt";
			FileInputStream fstream = new FileInputStream(inputFPKMFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("CD274")) {
					for (int i = 1; i < split.length; i++) {
						pd_l1_gene_expr.put(split_header[i], split[i]);
						pd_l1_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("CD2")) {
					for (int i = 1; i < split.length; i++) {
						cd2_gene_expr.put(split_header[i], split[i]);
						cd2_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("CD3E")) {
					for (int i = 1; i < split.length; i++) {
						cd3e_gene_expr.put(split_header[i], split[i]);
						cd3e_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("GZMH")) {
					for (int i = 1; i < split.length; i++) {
						gzmh_gene_expr.put(split_header[i], split[i]);
						gzmh_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("NKG7")) {
					for (int i = 1; i < split.length; i++) {
						nkg7_gene_expr.put(split_header[i], split[i]);
						nkg7_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("PRF1")) {
					for (int i = 1; i < split.length; i++) {
						prf1_gene_expr.put(split_header[i], split[i]);
						prf1_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("GZMK")) {
					for (int i = 1; i < split.length; i++) {
						gzmk_gene_expr.put(split_header[i], split[i]);
						gzmk_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
						
					}
				}
			}
			in.close();
			
			String inputTARGETFPKMFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanTARGET\\TARGET.combined.fpkm.geneNameOnly.targetid.txt";
			fstream = new FileInputStream(inputTARGETFPKMFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("CD274")) {
					for (int i = 1; i < split.length; i++) {
						pd_l1_gene_expr.put(split_header[i], split[i]);
						pd_l1_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("CD2")) {
					for (int i = 1; i < split.length; i++) {
						cd2_gene_expr.put(split_header[i], split[i]);
						cd2_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("CD3E")) {
					for (int i = 1; i < split.length; i++) {
						cd3e_gene_expr.put(split_header[i], split[i]);
						System.out.println(split_header[i] + "\t" +  split[i]);
						cd3e_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("GZMH")) {
					for (int i = 1; i < split.length; i++) {
						gzmh_gene_expr.put(split_header[i], split[i]);
						gzmh_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("NKG7")) {
					for (int i = 1; i < split.length; i++) {
						nkg7_gene_expr.put(split_header[i], split[i]);
						nkg7_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("PRF1")) {
					for (int i = 1; i < split.length; i++) {
						prf1_gene_expr.put(split_header[i], split[i]);
						prf1_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
					}
				}
				if (split[0].equals("GZMK")) {
					for (int i = 1; i < split.length; i++) {
						gzmk_gene_expr.put(split_header[i], split[i]);
						
						gzmk_gene_expr.put(split_header[i].split("_")[0] + "_D", split[i]);
						
					}
				}				
			}
			in.close();
			
			HashMap target_scna = new HashMap();
			String inputTARGET_SCNA = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ComprehensiveCopyNumberChange\\TARGET\\Comprehensive_Summary_20190821.txt";			
			fstream = new FileInputStream(inputTARGET_SCNA);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				target_scna.put(split[0], str);
			}
			in.close();

			HashMap count_gain = new HashMap();
			String inputTARGET_CNV_GAIN = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanTARGET\\CopyNumberGain_40milbp_Alteration_20190822.txt";			
			fstream = new FileInputStream(inputTARGET_CNV_GAIN);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (count_gain.containsKey(split[0])) {
					int count = (Integer)count_gain.get(split[0]);
					count++;
					count_gain.put(split[0], count);
				} else {
					count_gain.put(split[0], 1);
				}
			}
			in.close();
			
			HashMap count_loss = new HashMap();
			String inputTARGET_CNV_LOSS = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanTARGET\\CopyNumberLoss_40milbp_Alteration_20190822.txt";			
			fstream = new FileInputStream(inputTARGET_CNV_LOSS);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (count_loss.containsKey(split[0])) {
					int count = (Integer)count_loss.get(split[0]);
					count++;
					count_loss.put(split[0], count);
				} else {
					count_loss.put(split[0], 1);
				}
			}
			in.close();
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\final_integrated_table\\PCGP_TARGET_Integrated_table_20190820.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			/*
			 * CD247
CD2
CD3E
GZMH
NKG7
PRF1
GZMK

			 */

			HashMap ploidy = new HashMap();
			String inputPloidy = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanPCGP\\PCGP_Ploidy.txt";
			fstream = new FileInputStream(inputPloidy);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String ploidy_header = in.readLine();
			String[] ploidy_split_header = ploidy_header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				ploidy.put(split[0] + "_D", split[1]);
				ploidy.put(split[0] + "_D1", split[1]);
			}
			in.close();
			
			HashMap chromothripsis = new HashMap();
			String inputChromothripsis = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanPCGP\\PCGP_Chromothripsis.txt";
			fstream = new FileInputStream(inputChromothripsis);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String chromothripsis_header = in.readLine();
			String[] chromothripsis_split_header = chromothripsis_header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				chromothripsis.put(split[0] + "_D", split[1]);
				chromothripsis.put(split[0] + "_D1", split[1]);
			}
			in.close();
			
			HashMap fusion = new HashMap();
			String inputFusion = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\PanPCGP\\PCGP_fusion.txt";
			fstream = new FileInputStream(inputFusion);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String fusion_header = in.readLine();
			String[] fusion_split_header = fusion_header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				fusion.put(split[0] + "_D", split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4]);
				fusion.put(split[0] + "_D1", split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4]);
			}
			in.close();
			
			String fusion_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\FeatureIntegration\\fusion_info\\Karol_CTX_per_ID_20190820.txt";
			fstream = new FileInputStream(fusion_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\tPD-L1\tCD2\tCD3E\tGZMH\tNKG7\tPRF1\tGZMK\tPloidy\tChromothripsis\tDeletions\tDuplications\tInversions\tTranslocations\tpatient_name\tcancer_type\tLogRatio>2.0\tLogRatio<-2.0\tCNV30milGain\tCNV30milLoss\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String pd_l1_value = "NA";
				String cd2_value = "NA";
				String cd3e_value = "NA";
				String gzmh_value = "NA";
				String nkg7_value = "NA";
				String prf1_value = "NA";
				String gzmk_value = "NA";
				String ploidy_value = "NA";
				String chromothripsis_value = "NA";				
				String fusion_value = "NA\tNA\tNA\tNA";
				String scna_value = "NA\tNA\tNA\tNA";
				String cnv_gain_value = "NA";
				String cnv_loss_value = "NA";
				if (pd_l1_gene_expr.containsKey(split[0])) {
					pd_l1_value = (String)pd_l1_gene_expr.get(split[0]);
				}
				if (cd2_gene_expr.containsKey(split[0])) {
					cd2_value = (String)cd2_gene_expr.get(split[0]);
				}
				if (cd3e_gene_expr.containsKey(split[0])) {
					cd3e_value = (String)cd3e_gene_expr.get(split[0]);
				}
				if (gzmh_gene_expr.containsKey(split[0])) {
					gzmh_value = (String)gzmh_gene_expr.get(split[0]);
				}
				if (nkg7_gene_expr.containsKey(split[0])) {
					nkg7_value = (String)nkg7_gene_expr.get(split[0]);
				}
				if (prf1_gene_expr.containsKey(split[0])) {
					prf1_value = (String)prf1_gene_expr.get(split[0]);
				}
				if (gzmk_gene_expr.containsKey(split[0])) {
					gzmk_value = (String)gzmk_gene_expr.get(split[0]);
				}
				
				if (ploidy.containsKey(split[0])) {
					ploidy_value = (String)ploidy.get(split[0]);
				}
				if (chromothripsis.containsKey(split[0])) {
					chromothripsis_value = (String)chromothripsis.get(split[0]);
				}
				if (fusion.containsKey(split[0])) {
					fusion_value = (String)fusion.get(split[0]);
				}
				if (target_scna.containsKey(split[0])) {
					scna_value = (String)target_scna.get(split[0]);
				}
				if (count_gain.containsKey(split[0])) {
					int count = (Integer)count_gain.get(split[0]);
					cnv_gain_value = count + "";
				}
				if (count_loss.containsKey(split[0])) {
					int count = (Integer)count_loss.get(split[0]);
					cnv_loss_value = count + "";
				}
				out.write(str + "\t" + pd_l1_value + "\t" + cd2_value + "\t" + cd3e_value + "\t" + gzmh_value + "\t" + nkg7_value + "\t" + prf1_value + "\t" + gzmk_value + "\t" + ploidy_value + "\t" + chromothripsis_value + "\t" + fusion_value + "\t" + scna_value + "\t" + cnv_gain_value + "\t" + cnv_loss_value + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
