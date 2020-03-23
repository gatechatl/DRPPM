package stjude.projects.jinghuizhang.dexseq.exon.multimap.manuscript;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Compare multimap vs. uniqmap expression difference. Report exons with 10x fold change.
 * 
 * @author tshaw
 *
 */
public class JinghuiZhangSummarizeNumberOfExonWithMultimapping {

	public static String description() {
		return "Custom script for removing tag associated with PanCan and ECM.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputFile] [sampleName_multi] [sampleName_uniq] [gtfFile] [outputRawExon] [outputGTFExon] [outputGene]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap gene_exons = new HashMap();
			String inputFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\MappingComparison\\PCGP_905_FPKM_filtcol_MedianDiseaseType_Combined.txt";
			String sampleName_multi = args[1];
			String sampleName_uniq = args[2];
			String gtfFile = args[3];
			String outputRawExon = args[4];
			String outputGTFExon = args[5];
			String outputGene = args[6];
			FileWriter fwriter_raw_exon = new FileWriter(outputRawExon);
			BufferedWriter out_raw_exon = new BufferedWriter(fwriter_raw_exon);
			
			FileWriter fwriter_gtf_exon = new FileWriter(outputGTFExon);
			BufferedWriter out_gtf_exon = new BufferedWriter(fwriter_gtf_exon);
			
			FileWriter fwriter_gene = new FileWriter(outputGene);
			BufferedWriter out_gene = new BufferedWriter(fwriter_gene);
			
			FileInputStream fstream = new FileInputStream(gtfFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].contains("##")) {
					String chr = split[0];
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					String meta = split[8];
					String gene_name = GTFFile.grabMeta(meta, "gene_name");
					String transcript_id = GTFFile.grabMeta(meta, "transcript_id");
					if (split[2].equals("exon")) {
						String id = gene_name + ";" + chr + ";" + start + ";" + end + ";" + transcript_id;
						if (gene_exons.containsKey(gene_name)) {
							LinkedList list = (LinkedList)gene_exons.get(gene_name);
							list.add(id);
							gene_exons.put(gene_name, list);
						} else {
							LinkedList list = new LinkedList();
							list.add(id);
							gene_exons.put(gene_name, list);
						}
					}
				}
			}
			in.close();
			
			HashMap gene_with_hit = new HashMap();
			int index_multimap = -1;
			int index_uniqmap = -1;
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 0; i < split_header.length; i++) {
				if (split_header[i].equals(sampleName_multi)) {
					index_multimap = i;
				}
				if (split_header[i].equals(sampleName_uniq)) {
					index_uniqmap = i;
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[index_multimap].equals("NA") && !split[index_uniqmap].equals("NA")) {
					double diff = Math.log10(new Double(split[index_multimap]) + 1.0) - Math.log10(new Double(split[index_uniqmap]) + 1.0);
					if (diff > 1.0) {
						out_raw_exon.write(split[0] + "\t" + split[index_multimap] + "\t" + split[index_uniqmap] + "\t" + diff + "\n");
						String[] split_exon_info = split[0].split("\\|");
						String[] genes = split_exon_info[0].split("\\+");
						String chr = split_exon_info[1];
						int start = new Integer(split_exon_info[2]);
						int end = new Integer(split_exon_info[3]);
						for (String gene: genes) {
							gene_with_hit.put(gene, gene);
							if (gene_exons.containsKey(gene)) {
								LinkedList list = (LinkedList)gene_exons.get(gene);
								Iterator itr = list.iterator();
								while (itr.hasNext()) {
									String line = (String)itr.next();
									String[] split_line = line.split(";");
									String gtf_gene_name = split_line[0];
									String gtf_chr = split_line[1];
									int gtf_start = new Integer(split_line[2]);
									int gtf_end = new Integer(split_line[3]);
									if (chr.equals(gtf_chr)) {
										boolean hit = false;
										if (start <= gtf_start && gtf_start <= end) {
											hit = true;
										}
										if (start <= gtf_end && gtf_end <= end) {
											hit = true;
										}
										if (gtf_start <= start && start <= gtf_end) {
											hit = true;
										}
										if (gtf_start <= end && end <= gtf_end) {
											hit = true;
										}
										if (hit) {
											out_gtf_exon.write(line + "\t" + split[index_multimap] + "\t" + split[index_uniqmap] + "\t" + diff + "\n");
										}
									}
								}
							}
						}
					}
				}
			}
			in.close();
			out_raw_exon.close();
			out_gtf_exon.close();
			
			Iterator itr = gene_with_hit.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out_gene.write(gene + "\n");
			}
			out_gene.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
