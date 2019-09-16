package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SuzanneBakerAppendPreviousClusterInformation {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap map_tag = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\combined_Suerat\\Scombined_CellsIdentity_YFP.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], "Old_clust_" + split[1]);
				String tag = "";
				if (split[1].equals("0"))
					tag = "Cells that are Cycling";
				if (split[1].equals("1"))
					tag = "OPC-like stem-like";
				if (split[1].equals("2"))
					tag = "OPC-like stem-like";
				if (split[1].equals("3"))
					tag = "OPC-like stem-like";
				if (split[1].equals("4"))
					tag = "OPC-like stem-like";
				if (split[1].equals("5"))
					tag = "Microglia";
				if (split[1].equals("6"))
					tag = "Oligodendrocyte (resemble from tumor)";
				if (split[1].equals("7"))
					tag = "Neuronal (Polycomb expr)";
				if (split[1].equals("8"))
					tag = "Radial Glia (Stem-like)";
				if (split[1].equals("9"))
					tag = "Astro (Polycomb Expr)";
				if (split[1].equals("10"))
					tag = "OPC-like (Polycomb Expr)";
				if (split[1].equals("11"))
					tag = "OPC-like with (Polycomb expr)";
				if (split[1].equals("12"))
					tag = "Endothelial cells (Polycomb Expr)";
				if (split[1].equals("13"))
					tag = "Myelinated Oligo";
				if (split[1].equals("14"))
					tag = "Astro (Polycomb Expr)";
				if (split[1].equals("15"))
					tag = "Unknown";
				map_tag.put(split[0], tag);
			}
			in.close();

			
			//String outputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\Default_Res0.6_Per30\\NTRK_NesCre_GfapCreER_TSNE_CellsIdentity_Color_WithOldClusters.txt";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\Res0.3_Per50\\NTRK_NesCre_GfapCreER_TSNE_CellsIdentity_Color_WithOldClusters.txt";
			//String outputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\Res0.4_Per50\\NTRK_NesCre_GfapCreER_TSNE_CellsIdentity_Color_WithOldClusters.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\Res0.3_Per50\\NTRK_NesCre_GfapCreER_TSNE_CellsIdentity_Color.txt";
			//inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\Res0.4_Per50\\NTRK_NesCre_GfapCreER_TSNE_CellsIdentity_Color.txt";
			//inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\Default_Res0.6_Per30\\NTRK_NesCre_GfapCreER_TSNE_CellsIdentity_Color.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tPreviosCluster\tPreviousAnnotation\tGenotype\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String barcode = split[0].split("-")[0];
				String sample = split[0].split("-")[1];
				String sampleType = "NA";
				if (sample.equals("2942")) {
					sampleType = "NesCre";
				}
				if (sample.equals("2958")) {
					sampleType = "NesCre";
				}
				if (sample.equals("2992")) {
					sampleType = "NesCre";
				}
				if (sample.equals("2998")) {
					sampleType = "NesCre";
				}
				if (sample.equals("2827")) {
					sampleType = "Gfap";
				}
				if (sample.equals("2833")) {
					sampleType = "Gfap";
				}
				if (sample.equals("2866")) {
					sampleType = "Gfap";
				}
				if (sample.equals("2916")) {
					sampleType = "Gfap";
				}
				String prev_cluster_type = "NA";
				if (map_tag.containsKey(barcode)) {
					prev_cluster_type = (String)map_tag.get(barcode);
				}
				String prev_cluster = "NA";
				if (map.containsKey(barcode)) {
					prev_cluster = (String)map.get(barcode);
				}
				out.write(str + "\t" + prev_cluster + "\t" + prev_cluster_type + "\t" + sampleType + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
