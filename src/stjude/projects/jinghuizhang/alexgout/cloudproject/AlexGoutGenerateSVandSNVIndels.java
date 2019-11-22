package stjude.projects.jinghuizhang.alexgout.cloudproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class AlexGoutGenerateSVandSNVIndels {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap sampleName = new HashMap();
			HashMap snv_map = new HashMap();
			HashMap fusion_map = new HashMap();
			HashMap snv_indel_map = new HashMap();
			HashMap cnv_map = new HashMap();
			HashMap loh_map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SV_20191108.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sampleName.put(split[0], split[0]);
				if (split[12].equals("2")) {
					if (fusion_map.containsKey(split[0])) {
						String fusion = (String)fusion_map.get(split[0]);
						if (!fusion.contains(split[1] + "-" + split[2] + ":RNAseqFusion")) {
							fusion += ";" + split[1] + "-" + split[2] + ":RNAseqFusion";
						}
						fusion_map.put(split[0], fusion);
					} else {
						String fusion = split[1] + "-" + split[2] + ":RNAseqFusion";
						fusion_map.put(split[0], fusion);
					}
				}
				if (split[12].equals("5")) {
					if (fusion_map.containsKey(split[0])) {
						String fusion = (String)fusion_map.get(split[0]);
						if (!fusion.contains(split[1] + "-" + split[2] + ":DNAFusion")) {
							fusion += ";" + split[1] + "-" + split[2] + ":DNAFusion";
						}
						fusion_map.put(split[0], fusion);
					} else {
						String fusion = split[1] + "-" + split[2] + ":DNAFusion";
						fusion_map.put(split[0], fusion);
					}
				}
				if (split[12].equals("6")) {
					if (snv_indel_map.containsKey(split[0])) {
						String fusion = (String)snv_indel_map.get(split[0]);
						if (!fusion.contains(split[1] + ":internalDup")) {
							fusion += ";" + split[1] + ":internalDup";
						}
						fusion_map.put(split[0], fusion);
						
					} else {
						String fusion = split[1] + ":internalDup";
						fusion_map.put(split[0], fusion);
					}
				}
				if (split[12].equals("4")) {
					if (cnv_map.containsKey(split[0])) {
						String cnv_gene_list = (String)cnv_map.get(split[0]);
						if (!cnv_gene_list.contains(split[1] + ":CNV_" + split[3])) {
							cnv_gene_list += ";" + split[1] + ":CNV_" + split[3];
						}
						cnv_map.put(split[0], cnv_gene_list);
					} else {
						String cnv_gene_list = split[1] + ":CNV_" + split[3];
						cnv_map.put(split[0], cnv_gene_list);
					}
				}
				if (split[12].equals("10")) {
					if (loh_map.containsKey(split[0])) {
						String cnv_gene_list = (String)loh_map.get(split[0]);
						if (!cnv_gene_list.contains(split[1] + ":LOH_" + split[3])) {
							cnv_gene_list += ";" + split[1] + ":LOH_" + split[3];
						}
						loh_map.put(split[0], cnv_gene_list);
					} else {
						String cnv_gene_list = split[1] + ":LOH_" + split[3];
						loh_map.put(split[0], cnv_gene_list);
					}
				}
			}
			in.close();			
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SNVIndel_Simplified.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sampleName.put(split[0], split[0]);
				if (snv_indel_map.containsKey(split[0])) {
					String snv_indel = (String)snv_indel_map.get(split[0]);
					if (!snv_indel.contains(split[1] + ":" + split[2])) {
						snv_indel += ";" + split[1] + ":" + split[2];
					}
					snv_indel_map.put(split[0], snv_indel);
				} else {
					String snv_indel = split[1] + ":" + split[2];
					snv_indel_map.put(split[0], snv_indel);
				}
			}
			in.close();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\AlexGout_PCGP_TARGET_SNVIndel_Fusion_CNV_LOH_Combined.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Sample\tSNVINDEL\tFusion\tCNV(logFC>0.3)\tLOH(segmean>0.16)\n");
			Iterator itr = sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String sampleID = (String)itr.next();
				String snv_indel_line = "NA";
				String fusion_line = "NA";
				String cnv_line = "NA";
				String loh_line = "NA";
				if (snv_indel_map.containsKey(sampleID)) {
					snv_indel_line = (String)snv_indel_map.get(sampleID);
				}
				if (fusion_map.containsKey(sampleID)) {
					fusion_line = (String)fusion_map.get(sampleID);
				}
				if (cnv_map.containsKey(sampleID)) {
					cnv_line = (String)cnv_map.get(sampleID);
				}
				if (loh_map.containsKey(sampleID)) {
					loh_line = (String)loh_map.get(sampleID);
				}
				out.write(sampleID + "\t" + snv_indel_line + "\t" + fusion_line + "\t" + cnv_line + "\t" + loh_line + "\n");
			}
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
