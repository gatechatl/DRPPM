package rnaseq.splicing.alternativejuncpipeline.juncsalvager.novelexon;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import rnaseq.splicing.rnapeg.EXON;

/**
 * Input: GTF file and all the novel exon candidate. Perform six frame translation of the exon. 
 * @author gatechatl
 *
 */
public class JuncSalvagerValidateNovelExonAndGeneratePeptide {


	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Filter the novel exons that overlap with the GTF file.";
	}
	public static String parameter_info() {
		return "[inputGTFFile] [exonFolder] [exon_gtf] [outputBed] [outputNovelExonSummary]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String inputGTFFile = args[0];
			String exonFolder = args[1];
			String outputBed = args[2];
			String outputNovelExonSummary = args[3];
			
			HashMap all_novel_exons_summary = new HashMap();			
			HashMap exon_structure = new HashMap();
			
			HashMap exons = new HashMap();
			FileInputStream fstream = new FileInputStream(inputGTFFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 8) {
					String chr = split[0];					
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);					
					exons.put(chr + "\t" + start + "\t" + end, "");
					
				}
			}
			in.close();
			
			File outputFolder_file = new File(exonFolder);
			File[] sample_files = outputFolder_file.listFiles();
			for (File sampleFolder_file: sample_files) {
				if (sampleFolder_file.isDirectory()) {
					File[] gene_files = sampleFolder_file.listFiles();
					for (File geneFolder_file: gene_files) {
						String path = geneFolder_file.getPath();
						
						
						File f = new File(path + "/NovelExons.txt");
						if (f.exists()) {
							
							FileWriter fwriter = new FileWriter(path + "/NovelExons.Filtered.bed");
							BufferedWriter out = new BufferedWriter(fwriter);
							
							fstream = new FileInputStream(f.getPath());
							din = new DataInputStream(fstream);
							in = new BufferedReader(new InputStreamReader(din));
							String header = in.readLine();
							while (in.ready()) {
								String str = in.readLine();
								String[] split = str.split("\t");
								if (!exons.containsKey(split[1] + "\t" + split[2] + "\t" + split[3])) {
									out.write(split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[0] + ":" + split[5] + ":" + sampleFolder_file.getName() + "\t0\t" + split[4] + "\n");
									String exon_coord = split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[0] + ":" + split[5] + "\t0\t" + split[4];
									if (all_novel_exons_summary.containsKey(exon_coord)) {
										LinkedList sample_list = (LinkedList)all_novel_exons_summary.get(exon_coord);
										if (!sample_list.contains(sampleFolder_file.getName())) {
											sample_list.add(sampleFolder_file.getName());
										}
										all_novel_exons_summary.put(exon_coord, sample_list);
									} else {
										LinkedList sample_list = new LinkedList();
										sample_list.add(sampleFolder_file.getName());
										all_novel_exons_summary.put(exon_coord, sample_list);
									}
								}
							}
							in.close();
							
							out.close();
						}
						
					}
				}
			}
			

			FileWriter fwriter_bed = new FileWriter(outputBed);
			BufferedWriter out_bed = new BufferedWriter(fwriter_bed);
			
			FileWriter fwriter_exon_summary = new FileWriter(outputNovelExonSummary);
			BufferedWriter out_exon_summary = new BufferedWriter(fwriter_exon_summary);
			
			Iterator itr = all_novel_exons_summary.keySet().iterator();
			while (itr.hasNext()) {
				String bed_line = (String)itr.next();
				out_bed.write(bed_line + "\n");
				LinkedList sample_list = (LinkedList)all_novel_exons_summary.get(bed_line);
				String sample_txt = "";
				Iterator itr2 = sample_list.iterator();
				while (itr2.hasNext()) {
					String sample_name = (String)itr2.next();
					
					if (sample_txt.equals("")) {
						sample_txt = sample_name;
					} else {
						sample_txt += "," + sample_name;
					}
				}
				out_exon_summary.write(bed_line + "\t" + sample_list.size() + "\t" + sample_txt + "\n");
			}
			out_bed.close();
			out_exon_summary.close();
			
			// need to add pipeline for extracting fasta file
			
			// need to post-process and 3 frame translate the fasta
			
			// need to extract sequence for 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
