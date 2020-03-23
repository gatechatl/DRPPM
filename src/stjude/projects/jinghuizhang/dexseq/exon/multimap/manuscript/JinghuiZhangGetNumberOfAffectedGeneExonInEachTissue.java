package stjude.projects.jinghuizhang.dexseq.exon.multimap.manuscript;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangGetNumberOfAffectedGeneExonInEachTissue {

	
	public static void main(String[] args) {
		
		try {
			String folder = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\MappingComparison\\Impacted_Targets\\";
			HashMap gtex_gene_map = new HashMap();
			String[] tissues = {"Adipose.Tissue", "Adrenal.Gland", "Bladder", "Blood", "Blood.Vessel", "Bone.Marrow", "Brain", "Breast", "Cervix.Uteri", "Colon", "Esophagus", "Fallopian.Tube", "Heart", "Kidney", "Liver", "Lung", "Muscle", "Nerve", "Ovary", "Pancreas", "Pituitary", "Prostate", "Salivary.Gland", "Skin", "Small.Intestine", "Spleen", "Stomach", "Testis", "Thyroid", "Uterus", "Vagina"};
			
			
			for (String tissue: tissues) {
				int count = 0;
				FileInputStream fstream = new FileInputStream(folder + "\\GTEx_7526_FPKM_" + tissue + "_Gene.txt");
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					gtex_gene_map.put(split[0], split[0]);
					count++;
				}
				in.close();
				System.out.println(tissue + "\t" + count);
			}
			System.out.println("All GTEx tissue genes with exon with discrepant mapping " + gtex_gene_map.size());

			
			HashMap gtex_exon_map = new HashMap();
			for (String tissue: tissues) {
				int count = 0;
				FileInputStream fstream = new FileInputStream(folder + "\\GTEx_7526_FPKM_" + tissue + "_GTF_Exon.txt");
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					gtex_exon_map.put(split[0], split[0]);
					count++;
				}
				in.close();
				System.out.println(tissue + "\t" + count);
			}
			System.out.println("Exons with discrepant mapping in GTEx tissues: " + gtex_exon_map.size());
			

			
			
			
			
			HashMap pcgp_gene_map = new HashMap();
			String[] tumors = {"ACT", "AML", "BALL", "CPC", "EPD", "HGG", "LGG", "MB", "MEL", "MLL", "OS", "RB", "RHB", "TALL", "WLM"};
			
			
			for (String tumor: tumors) {
				int count = 0;
				FileInputStream fstream = new FileInputStream(folder + "\\PCGP_905_FPKM_" + tumor + "_Gene.txt");
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					pcgp_gene_map.put(split[0], split[0]);
					count++;
				}
				in.close();
				System.out.println(tumor + "\t" + count);
			}
			System.out.println("Genes with exon with discrepant mapping in PCGP: " + pcgp_gene_map.size());

			
			HashMap pcgp_exon_map = new HashMap();
			for (String tumor: tumors) {
				int count = 0;
				FileInputStream fstream = new FileInputStream(folder + "\\PCGP_905_FPKM_" + tumor + "_GTF_Exon.txt");
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					pcgp_exon_map.put(split[0], split[0]);
					count++;
				}
				in.close();
				System.out.println(tumor + "\t" + count);
			}
			System.out.println("Exons with discrepant mapping in PCGP: " + pcgp_exon_map.size());
			

			HashMap target_gene_map = new HashMap();
			String[] target_tumors = {"AML", "BALL", "NBL", "OS", "TALL", "WLM"};
			
			
			for (String tumor: target_tumors) {
				int count = 0;
				FileInputStream fstream = new FileInputStream(folder + "\\TARGET_1054_FPKM_" + tumor + "_Gene.txt");
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					target_gene_map.put(split[0], split[0]);
					count++;
				}
				in.close();
				System.out.println(tumor + "\t" + count);
			}
			System.out.println("Genes with exon with discrepant mapping in TARGET: " + target_gene_map.size());

			
			HashMap target_exon_map = new HashMap();
			for (String tumor: target_tumors) {
				int count = 0;
				FileInputStream fstream = new FileInputStream(folder + "\\TARGET_1054_FPKM_" + tumor + "_GTF_Exon.txt");
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					target_exon_map.put(split[0], split[0]);
					count++;
				}
				in.close();
				System.out.println(tumor + "\t" + count);
			}
			System.out.println("Exons with discrepant mapping in TARGET: " + target_exon_map.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
