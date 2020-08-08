package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class LeventakiCombineCNSResult {

	public static String type() {
		return "LEVENTAKI";
	}
	public static String description() {
		return "Combine CNS result";
	}
	public static String parameter_info() {
		return "[exome_sampleName_inputFile] [cytoband_inputFile] [cutoff 0.34?] [cns_path] [outputFile_group1] [outputFile_group2] [outputFile_group12] [outputFile_group21]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String exome_sampleName_inputFile = args[0];
			String cytoband_inputFile = args[1];
			double cutoff = new Double(args[2]);
			String cns_path = args[3];
			String outputFile_type1 = args[4]; //"\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\cnvkit_summary\\Type1.txt";
			FileWriter fwriter_type1 = new FileWriter(outputFile_type1);
			BufferedWriter out_type1 = new BufferedWriter(fwriter_type1);

			//String outputFile_type2 = "\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\cnvkit_summary\\Type2.txt";
			String outputFile_type2 = args[5];
			FileWriter fwriter_type2 = new FileWriter(outputFile_type2);
			BufferedWriter out_type2 = new BufferedWriter(fwriter_type2);
			
			String outputFile_type3 = args[6];
			FileWriter fwriter_type3 = new FileWriter(outputFile_type3);
			BufferedWriter out_type3 = new BufferedWriter(fwriter_type3);
			

			String outputFile_type4 = args[7];
			FileWriter fwriter_type4 = new FileWriter(outputFile_type4);
			BufferedWriter out_type4 = new BufferedWriter(fwriter_type4);
			
			HashMap map = new HashMap();
			// read a list of available files ordered by group1 and group2
			//String inputFile = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\cnvkit_summary\\exome_sampleName.txt";

			FileInputStream fstream = new FileInputStream(exome_sampleName_inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			in.close();
			
			HashMap cytoband = new HashMap();
			//inputFile = "Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\cnvkit_summary\\cytoBand.txt";
			
			fstream = new FileInputStream(cytoband_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				cytoband.put(split[0] + "\t" + split[1] + "\t" + split[2], split[0] + split[3]);
			}
			in.close();
			
			
			
			out_type1.write("SampleName\tchromosome\tstart\tend\tgene\tlog2\tdepth\tprobes\tweight\tcytoband\tnum_gene\n");
			
			
			out_type2.write("SampleName\tchromosome\tstart\tend\tgene\tlog2\tdepth\tprobes\tweight\tcytoband\tnum_gene\n");
			out_type3.write("SampleName\tchromosome\tstart\tend\tgene\tlog2\tdepth\tprobes\tweight\tcytoband\tnum_gene\n");
			out_type4.write("SampleName\tchromosome\tstart\tend\tgene\tlog2\tdepth\tprobes\tweight\tcytoband\tnum_gene\n");
			//File files = new File("\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\cnvkit_summary\\cns\\");
			File files = new File(cns_path);
			for (File file: files.listFiles()) {
				String name = file.getName();
				String cleaned_name = name.replaceAll(".cns", "");
				if (map.containsKey(cleaned_name)) {
					String type = (String)map.get(cleaned_name);
					
					if (type.equals("1")) {
						System.out.println("Type1: " + cleaned_name);
						FileInputStream fstream_cns = new FileInputStream(file.getPath());
						DataInputStream din_cns = new DataInputStream(fstream_cns);
						BufferedReader in_cns = new BufferedReader(new InputStreamReader(din_cns));
						in_cns.readLine();
						while (in_cns.ready()) {
							String str = in_cns.readLine();
							String[] split = str.split("\t");
							String chr = split[0];
							int start = new Integer(split[1]);
							int end = new Integer(split[2]);
							int gene_num = split[3].split(",").length;
							String cytoband_str = "";
							Iterator itr = cytoband.keySet().iterator();
							while (itr.hasNext()) {
								String pos = (String)itr.next();
								String cytoband_ucsc = (String)cytoband.get(pos);
								String[] split_cytoband = pos.split("\t");
								String chr_cytoband = split_cytoband[0];
								int start_cytoband = new Integer(split_cytoband[1]);
								int end_cytoband = new Integer(split_cytoband[2]);
								if (chr.equals(chr_cytoband) && overlap(start, end, start_cytoband, end_cytoband)) {
									cytoband_str += cytoband_ucsc + ",";
								}
							}
							double log2FC = new Double(split[4]);
							if (end - start > 10000000 && Math.abs(log2FC) > cutoff) {
								out_type1.write(cleaned_name + "\t" + str + "\t" + cytoband_str + "\t" + gene_num + "\n");
							}
						}
						in.close();
					} else if (type.equals("2")) {
						System.out.println("Type2: " + cleaned_name);
						FileInputStream fstream_cns = new FileInputStream(file.getPath());
						DataInputStream din_cns = new DataInputStream(fstream_cns);
						BufferedReader in_cns = new BufferedReader(new InputStreamReader(din_cns));
						in_cns.readLine();
						while (in_cns.ready()) {
							String str = in_cns.readLine();
							String[] split = str.split("\t");
							String chr = split[0];
							int start = new Integer(split[1]);
							int end = new Integer(split[2]);
							int gene_num = split[3].split(",").length;
							String cytoband_str = "";
							Iterator itr = cytoband.keySet().iterator();
							while (itr.hasNext()) {
								String pos = (String)itr.next();
								String cytoband_ucsc = (String)cytoband.get(pos);
								String[] split_cytoband = pos.split("\t");
								String chr_cytoband = split_cytoband[0];
								int start_cytoband = new Integer(split_cytoband[1]);
								int end_cytoband = new Integer(split_cytoband[2]);
								
								if (chr.equals(chr_cytoband) && overlap(start, end, start_cytoband, end_cytoband)) {
									cytoband_str += cytoband_ucsc + ",";
								}
							}
							double log2FC = new Double(split[4]);
							if (end - start > 10000000 && Math.abs(log2FC) > cutoff) {
								out_type2.write(cleaned_name + "\t" + str + "\t" + cytoband_str + "\t" + gene_num + "\n");
							}
						}
						in.close();
					} else if (type.equals("12")) {
						System.out.println("Other Type: " + cleaned_name);
						FileInputStream fstream_cns = new FileInputStream(file.getPath());
						DataInputStream din_cns = new DataInputStream(fstream_cns);
						BufferedReader in_cns = new BufferedReader(new InputStreamReader(din_cns));
						in_cns.readLine();
						while (in_cns.ready()) {
							String str = in_cns.readLine();
							String[] split = str.split("\t");
							String chr = split[0];
							int start = new Integer(split[1]);
							int end = new Integer(split[2]);
							int gene_num = split[3].split(",").length;
							String cytoband_str = "";
							Iterator itr = cytoband.keySet().iterator();
							while (itr.hasNext()) {
								String pos = (String)itr.next();
								String cytoband_ucsc = (String)cytoband.get(pos);
								String[] split_cytoband = pos.split("\t");
								String chr_cytoband = split_cytoband[0];
								int start_cytoband = new Integer(split_cytoband[1]);
								int end_cytoband = new Integer(split_cytoband[2]);
								
								if (chr.equals(chr_cytoband) && overlap(start, end, start_cytoband, end_cytoband)) {
									cytoband_str += cytoband_ucsc + ",";
								}
							}
							double log2FC = new Double(split[4]);
							if (end - start > 10000000 && Math.abs(log2FC) > cutoff) {
								out_type3.write(cleaned_name + "\t" + str + "\t" + cytoband_str + "\t" + gene_num + "\n");
							}
						}
						in.close();
					} else if (type.equals("21")) {
						System.out.println("Other Type: " + cleaned_name);
						FileInputStream fstream_cns = new FileInputStream(file.getPath());
						DataInputStream din_cns = new DataInputStream(fstream_cns);
						BufferedReader in_cns = new BufferedReader(new InputStreamReader(din_cns));
						in_cns.readLine();
						while (in_cns.ready()) {
							String str = in_cns.readLine();
							String[] split = str.split("\t");
							String chr = split[0];
							int start = new Integer(split[1]);
							int end = new Integer(split[2]);
							int gene_num = split[3].split(",").length;
							String cytoband_str = "";
							Iterator itr = cytoband.keySet().iterator();
							while (itr.hasNext()) {
								String pos = (String)itr.next();
								String cytoband_ucsc = (String)cytoband.get(pos);
								String[] split_cytoband = pos.split("\t");
								String chr_cytoband = split_cytoband[0];
								int start_cytoband = new Integer(split_cytoband[1]);
								int end_cytoband = new Integer(split_cytoband[2]);
								
								if (chr.equals(chr_cytoband) && overlap(start, end, start_cytoband, end_cytoband)) {
									cytoband_str += cytoband_ucsc + ",";
								}
							}
							double log2FC = new Double(split[4]);
							if (end - start > 10000000 && Math.abs(log2FC) > cutoff) {
								out_type4.write(cleaned_name + "\t" + str + "\t" + cytoband_str + "\t" + gene_num + "\n");
							}
						}
						in.close();
					}
				}
			}
			out_type1.close();
			out_type2.close();
			out_type3.close();
			out_type4.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean overlap(int start1, int end1, int start2, int end2) {
		if (start1 <= start2 && start2 <= end1) {
			return true;
		}
		if (start2 <= start1 && start1 <= end2) {
			return true;
		}
		if (start1 <= end2 && end2 <= end1) {
			return true;
		}
		if (start2 <= end1 && end1 <= end2) {
			return true;
		}
		return false;
	}
}
