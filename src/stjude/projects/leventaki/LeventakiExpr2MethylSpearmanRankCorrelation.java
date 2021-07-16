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

import statistics.general.MathTools;


public class LeventakiExpr2MethylSpearmanRankCorrelation {


	public static String description() {
		return "Calculate spearman rank rho comparing query gene (e.g. ALK) to the methylation profile";
	}
	public static String type() {
		return "LEVENTAKI";
	}
	public static String parameter_info() {
		return "[inputSampleFile] [inputRNAseqFile] [inputMethylFile] [query_gene] [rho_cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String inputSampleFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\cmpb\\ALCL_RNASEQ_ANALYSIS\\FinalAnalysisPipeline\\13_MethylSpearmanRank\\sampleName.txt";			
			String inputRNAseqFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\cmpb\\ALCL_RNASEQ_ANALYSIS\\FinalAnalysisPipeline\\1_FPKM\\SJALCL_RNAseq_Exon_Read_Count_gene_unique_fpkm_final_totalstranded.txt";
			String inputMethylFile = args[2]; //"Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_850K_108399_methylation_table_appendGeneInfo_normalized.txt";
			String query_gene = args[3]; // insert the gene symbol to correlate with.
			double rho_cutoff = new Double(args[4]);
			String outputFile = args[5]; //"Z:\\ResearchHome\\ProjectSpace\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Biostats\\Methylation\\ProbeCorrection\\Leventaki_methylation_rnaseq_spearman_rho_20180211.txt";			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap sampleName_map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputSampleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sampleName_map.put(str.trim(), str.trim());								
			}
			in.close();
					
			HashMap sampleGene2rnaseq = new HashMap(); 
			HashMap name2index = new HashMap();
			fstream = new FileInputStream(inputRNAseqFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String[] headers = in.readLine().split("\t");
			for (int i = 0; i < headers.length; i++) {
				if (sampleName_map.containsKey(headers[i])) {
					name2index.put(headers[i], i);
				}
			}
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				Iterator itr = name2index.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					int i = (Integer)name2index.get(sampleName);
					if (split[0].equals(query_gene)) {
						sampleGene2rnaseq.put(sampleName + "\t" + split[0], new Double(split[i]));
					}
				}
			}
			in.close();
			
			//System.out.println(sampleGene2rnaseq.size());
			
			HashMap methyl_name2index = new HashMap();
			fstream = new FileInputStream(inputMethylFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header_line = in.readLine();
			
			headers = header_line.split("\t");
			Iterator itr = name2index.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				int i = 0;
				for (String header: headers) {
					if (header.contains(name)) {
						methyl_name2index.put(name,  i);
					}
					i++;
				}
			}
			
			System.out.println(methyl_name2index.size());
			out.write("Gene\tRho");
			Iterator name_itr = methyl_name2index.keySet().iterator();
			while (name_itr.hasNext()) {
				String name = (String)name_itr.next();
				out.write("\tRNA_" + name);
			}
			out.write("\t" + header_line + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				// grab the methylation values
				LinkedList methyl_values = new LinkedList();
				
				boolean good = true;
				Iterator itr2 = methyl_name2index.keySet().iterator();
				while (itr2.hasNext()) {
					String name = (String)itr2.next();
					int index = (Integer)methyl_name2index.get(name);
					if (!(split[index].equals("") || split[index].equals("NA") || split[index].equals("NaN"))) {
						double value = new Double(split[index]);
						methyl_values.add(value + "");
					} else {
						good = false;
					}
					
										
				}
				
				LinkedList rna_values = new LinkedList();
				String rna_values_str = "";

				itr2 = methyl_name2index.keySet().iterator();
				while (itr2.hasNext()) {
					String name = (String)itr2.next();																				
					//String tag = name + "\t" + gene_name;
					String tag = name + "\t" + query_gene;
					
					if (sampleGene2rnaseq.containsKey(tag)) {
						double value = (Double)sampleGene2rnaseq.get(tag);
						rna_values.add(value + "");
						rna_values_str += "\t" + value;
					}
				}
				if (good) {
					HashMap genes = new HashMap();
					//System.out.println(split[61]);
					if (split.length > 61) {
						for (String gene_name: split[61].split(";")) {										
							genes.put(gene_name, gene_name); // 										
						}
						double rho = Double.NaN;
						Iterator itr3 = genes.keySet().iterator();
						while (itr3.hasNext()) {
							String gene_name = (String)itr3.next();

							//System.out.println(methyl_values.size());
							//System.out.println(rna_values.size());
							if (methyl_values.size() == rna_values.size()) {
								
								double[] methyl_array = MathTools.convertListStr2Double(methyl_values);
								double[] rna_array = MathTools.convertListStr2Double(rna_values);
								rho = MathTools.SpearmanRank(methyl_array, rna_array);
								if (Math.abs(rho) >= rho_cutoff) {
									out.write(gene_name + "\t" + rho + rna_values_str + "\t" + str + "\n");
									//System.out.println(gene_name);
								}
							}
						}
					}
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

