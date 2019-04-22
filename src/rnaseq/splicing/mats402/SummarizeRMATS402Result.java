package rnaseq.splicing.mats402;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SummarizeRMATS402Result {

	public static String type() {
		return "rMATS";
	}
	public static String description() {
		return "rMATS 4.0.2 result summary.";
	}
	public static String parameter_info() {
		return "[rMATS Output Folder] [fdr_cutoff] [psi_cutoff]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String outputPath = args[0];
			double fdr_cutoff = new Double(args[1]);
			double psi_cutoff = new Double(args[2]);
			
			String SE = "SE.MATS.JC.txt";
			String RI = "RI.MATS.JC.txt";
			String MXE = "MXE.MATS.JC.txt";
			String A3SS = "A3SS.MATS.JC.txt";
			String A5SS = "A5SS.MATS.JC.txt";
			int SE_count = 0;
			int SE_count_gene = 0;
			int exon_skipping_count = 0;
			int exon_inclusion_count = 0;
			int exon_skipping_count_gene = 0;
			
			HashMap exon_skipping_count_gene_map = new HashMap();
			int exon_inclusion_count_gene = 0;
			
			int RI_count = 0;
			int RI_count_gene = 0;
			int increased_retained_intron_count = 0;
			int decreased_retained_intron_count = 0;
			int increased_retained_intron_count_gene = 0;
			int decreased_retained_intron_count_gene = 0;
			
			int MXE_count = 0;
			int MXE_count_gene = 0;
			int A3SS_count = 0;
			int A3SS_count_gene = 0;
			int A5SS_count = 0;
			int A5SS_count_gene = 0;			
			
			HashMap all_genes_tested = new HashMap();
			HashMap all_gene = new HashMap();
			HashMap SE_map = new HashMap();
			HashMap skipping_map = new HashMap();
			HashMap inclusion_map = new HashMap();
			FileInputStream fstream = new FileInputStream(outputPath + "/" + SE);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2];
				all_genes_tested.put(gene, gene);
				double fdr = new Double(split[19]);
				double incLevel = new Double(split[22]);
				if (fdr <= fdr_cutoff && Math.abs(incLevel) >= psi_cutoff) {
					if (!SE_map.containsKey(gene)) {
						SE_count_gene++;
						SE_map.put(gene, gene);
					}
					SE_count++;
					all_gene.put(gene, gene);
					
					// exon skipping
					if (incLevel < 0) {
						exon_skipping_count++;
						if (!skipping_map.containsKey(gene)) {
							exon_skipping_count_gene++;
							exon_skipping_count_gene_map.put(gene, gene);
							skipping_map.put(gene, gene);
						}
					// exon kept
					} else {
						exon_inclusion_count++;
						if (!inclusion_map.containsKey(gene)) {
							exon_inclusion_count_gene++;
							inclusion_map.put(gene, gene);
						}
					}
				}
			}
			in.close();
			
			HashMap RI_map = new HashMap();
			HashMap increased_RI_map = new HashMap();
			HashMap decreased_RI_map = new HashMap();
			
			fstream = new FileInputStream(outputPath + "/" + RI);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2];
				all_genes_tested.put(gene, gene);
				double fdr = new Double(split[19]);
				double incLevel = new Double(split[22]);
				if (fdr <= fdr_cutoff && Math.abs(incLevel) >= psi_cutoff) {
					if (!RI_map.containsKey(gene)) {
						RI_count_gene++;
						RI_map.put(gene, gene);
					} 
					RI_count++;
					all_gene.put(gene, gene);
					
					if (incLevel > 0) {
						increased_retained_intron_count++;
						if (!increased_RI_map.containsKey(gene)) {
							increased_retained_intron_count_gene++;
							increased_RI_map.put(gene, gene);
						}
					} else {
						decreased_retained_intron_count++;
						if (!decreased_RI_map.containsKey(gene)) {
							decreased_retained_intron_count_gene++;
							decreased_RI_map.put(gene, gene);
						}
					}
				}
			}
			in.close();
			
			HashMap MXE_map = new HashMap();
			fstream = new FileInputStream(outputPath + "/" + MXE);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2];
				all_genes_tested.put(gene, gene);
				double fdr = new Double(split[21]);
				double incLevel = new Double(split[24]);
				if (fdr <= fdr_cutoff && Math.abs(incLevel) >= psi_cutoff) {
					if (!MXE_map.containsKey(gene)) {
						MXE_count_gene++;
						MXE_map.put(gene, gene);
					}
					MXE_count++;
					all_gene.put(gene, gene);
				}
			}
			in.close();

			HashMap A3SS_map = new HashMap();
			fstream = new FileInputStream(outputPath + "/" + A3SS);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2];
				all_genes_tested.put(gene, gene);
				double fdr = new Double(split[19]);
				double incLevel = new Double(split[22]);
				if (fdr <= fdr_cutoff && Math.abs(incLevel) >= psi_cutoff) {
					if (!A3SS_map.containsKey(gene)) {
						A3SS_count_gene++;
						A3SS_map.put(gene, gene);
					} 
					A3SS_count++;
					all_gene.put(gene, gene);
				}
			}
			in.close();

			HashMap A5SS_map = new HashMap();
			fstream = new FileInputStream(outputPath + "/" + A5SS);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[2];
				all_genes_tested.put(gene, gene);
				double fdr = new Double(split[19]);
				double incLevel = new Double(split[22]);
				if (fdr <= fdr_cutoff && Math.abs(incLevel) >= psi_cutoff) {
					if (!A5SS_map.containsKey(gene)) {
						A5SS_count_gene++;
						A5SS_map.put(gene, gene);
					} 
					A5SS_count++;
					all_gene.put(gene, gene);
				}
			}
			in.close();
			
			System.out.println(outputPath);
			
			System.out.println("Exon Skipping" + "\t" + exon_skipping_count + "\t" + exon_skipping_count_gene);
			System.out.println("Exon Inclusion" + "\t" + exon_inclusion_count + "\t" + exon_inclusion_count_gene);
			System.out.println("Total SE" + "\t" + SE_count + "\t" + SE_count_gene);
			System.out.println("MXE" + "\t" + MXE_count + "\t" + MXE_count_gene);
			
			System.out.println("Increased Retained Intron" + "\t" + increased_retained_intron_count + "\t" + increased_retained_intron_count_gene);
			System.out.println("Decreased Retained Intron" + "\t" + decreased_retained_intron_count + "\t" + decreased_retained_intron_count_gene);
			System.out.println("Total RI" + "\t" + RI_count + "\t" + RI_count_gene);
			System.out.println("A3SS" + "\t" + A3SS_count + "\t" + A3SS_count_gene);
			System.out.println("A5SS" + "\t" + A5SS_count + "\t" + A5SS_count_gene);
			System.out.println("Percent of altered gene: " + new Double(new Double(new Double(all_gene.size()) / all_genes_tested.size() * 1000000).intValue()) / 10000 + "%");
			System.out.println("Total affected gene: " + all_gene.size());
			System.out.println("Total tested gene: " + all_genes_tested.size());
			
			/*
			StringBuffer str_buf = new StringBuffer();
			
			Iterator itr = all_gene.keySet().iterator();
			while (itr.hasNext()) {
				String gene_name = (String)itr.next();
				str_buf.append(gene_name + ",");
			}*/
			
			System.out.println();
			System.out.println("Affected_Gene List:\t" + map2string(all_gene));//str_buf.toString());			
			System.out.println("Skipped_Gene List:\t" + map2string(skipping_map));
			System.out.println("inclusion_Gene List:\t" + map2string(inclusion_map));
			System.out.println("increased_RI_map List:\t" + map2string(increased_RI_map));
			System.out.println("decreased_RI_map List:\t" + map2string(decreased_RI_map));
			System.out.println("MXE_map List:\t" + map2string(MXE_map));
			System.out.println("A3SS_map List:\t" + map2string(A3SS_map));
			System.out.println("A5SS_map List:\t" + map2string(A5SS_map));						
			System.out.println("Tested_Gene List:\t" + map2string(all_genes_tested));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String map2string(HashMap map) {
		StringBuffer str_buf = new StringBuffer();		
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String gene_name = (String)itr.next();
			str_buf.append(gene_name + ",");
		}
		return str_buf.toString();
	}
}
