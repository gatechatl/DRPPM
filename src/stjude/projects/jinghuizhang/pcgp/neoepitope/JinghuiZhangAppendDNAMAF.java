package stjude.projects.jinghuizhang.pcgp.neoepitope;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangAppendDNAMAF {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap hit = new HashMap();
			HashMap mutation_count = new HashMap();
			HashMap mutation_pass_count = new HashMap();
			HashMap mutation_pass_RNAexpr_count = new HashMap();
			HashMap mutation_with_RNAexpr_count = new HashMap();
			//HashMap RNAmaf_map = new HashMap();
			//HashMap DNAmaf_map = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\PCGP_Subset_RNA_DNA_MAF_Neoepitope.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//System.out.println(split[29]);
				map.put(split[1] + "\t" + split[29], str);
				hit.put(split[29], split[29]);
				/*double RNAref = new Double(split[30]);
				double RNAvar = new Double(split[31]);
				double RNAmaf = RNAvar / (RNAref + RNAvar + 0.001);
				
				double DNAref = new Double(split[33]);
				double DNAvar = new Double(split[34]);
				double DNAmaf = RNAvar / (RNAref + RNAvar + 0.001);
				RNAmaf_map.put(split[1] + "\t" + split[29], RNAmaf);
				DNAmaf_map.put(split[1] + "\t" + split[29], DNAmaf);
				double ratio = RNAmaf / DNAmaf;*/
				if (mutation_count.containsKey(split[1])) {
					double count = (Double)mutation_count.get(split[1]);
					count++;
					mutation_count.put(split[1], count);
				} else {
					mutation_count.put(split[1], 1.0);
				}
				if (new Double(split[12]) < 500) {
					if (mutation_pass_count.containsKey(split[1])) {
						double count = (Double)mutation_pass_count.get(split[1]);
						count++;
						mutation_pass_count.put(split[1], count);
					} else {
						mutation_pass_count.put(split[1], 1.0);
					}
				}
				if (new Double(split[12]) < 500 && new Double(split[31]) > 50) {
					if (mutation_pass_RNAexpr_count.containsKey(split[1])) {
						double count = (Double)mutation_pass_RNAexpr_count.get(split[1]);
						count++;
						mutation_pass_RNAexpr_count.put(split[1], count);
					} else {
						mutation_pass_RNAexpr_count.put(split[1], 1.0);
					}
				}
				if (new Double(split[31]) > 50) {
					if (mutation_with_RNAexpr_count.containsKey(split[1])) {
						double count = (Double)mutation_with_RNAexpr_count.get(split[1]);
						count++;
						mutation_with_RNAexpr_count.put(split[1], count);
					} else {
						mutation_with_RNAexpr_count.put(split[1], 1.0);
					}
				}
			}
			in.close();
			System.out.println(map.size());
			
			HashMap cibersort = new HashMap();
			String input_cibersort_result_File = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\Cibersort\\PCGP_Comprehensive_Cibersort_Result_20190530.txt";
			fstream = new FileInputStream(input_cibersort_result_File);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String cibersort_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				System.out.println(split[0].split("-")[0]);
				cibersort.put(split[0].split("-")[0], str);
			}
			in.close();
			
			HashMap ssGSEA = new HashMap();
			String inputGSEAFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\FPKM\\ssGSEA\\pcgp_immune_ssGSEA_score.txt";
			fstream = new FileInputStream(inputGSEAFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String ssGSEA_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				System.out.println(split[0].split("\\.")[0]);
				ssGSEA.put(split[0].split("\\.")[0], str);
				
			}
			in.close();
			System.out.println(map.size());
			
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\variant2matrix\\PCGP_Subset_RNA_DNA_MAF_Neoepitope_appended_20190530.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(header + "\t" + "VCF_info" + "\t" + "DNA_Ref\tDNA_Alt\t" + ssGSEA_header + "\tSNVCount\tSNVPassCutoffCount\tRNAexprSNVPassCutoffCount\tRNAexprCount\tRNA_MAF\tDNA_MAF\tRNA2DNA_MAF_Ratio\tTumorType\t" + cibersort_header + "\n");
			String vcf_header = "";
			String[] split_vcf_header = vcf_header.split("\t");
			String vcf_inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\pediatric.hg19.vcf";
			fstream = new FileInputStream(vcf_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (str.substring(0, 1).equals("#")) {
					vcf_header = str;
					split_vcf_header = vcf_header.split("\t");
				} else {
					String name = "chr" + split[0] + "." + split[1] + "." + split[3] + "." + split[4];
					if (hit.containsKey(name)) {
						for (int i = 9; i < split_vcf_header.length; i++) {
							if (!split[i].contains(".:")) {
								if (map.containsKey(split_vcf_header[i] + "\t" + name)) {
									String line = (String)map.get(split_vcf_header[i] + "\t" + name);
									String[] split_line = line.split("\t");
									String ssGSEA_line = (String)ssGSEA.get(split_vcf_header[i]);
									String cibersort_line =(String)cibersort.get(split_vcf_header[i]);
									
									double RNAref = new Double(split_line[split_line.length - 2]);
									double RNAvar = new Double(split_line[split_line.length - 1]);
									double RNAmaf = RNAvar / (RNAref + RNAvar + 0.001);
									
									double DNAref = new Double(split[i].split(":")[0].split(",")[0]);
									double DNAvar = new Double(split[i].split(":")[0].split(",")[1]);
									double DNAmaf = DNAvar / (DNAref + DNAvar + 0.001);
									
									double rna2dna_maf_ratio = RNAmaf / (DNAmaf + 0.001);
									double mutation_count_value = 0.0;
									double mutation_pass_count_value = 0.0;
									double mutation_pass_RNAexpr_count_value = 0.0;
									double mutation_with_RNAexpr_count_value = 0.0;
									
									String tumorType = split_vcf_header[i].split("0")[0].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
									
									if (mutation_count.containsKey(split_vcf_header[i])) {
										mutation_count_value = (Double)mutation_count.get(split_vcf_header[i]);
									}
									if (mutation_pass_count.containsKey(split_vcf_header[i])) {
										mutation_pass_count_value = (Double)mutation_pass_count.get(split_vcf_header[i]);
									}
									if (mutation_pass_RNAexpr_count.containsKey(split_vcf_header[i])) {
										mutation_pass_RNAexpr_count_value = (Double)mutation_pass_RNAexpr_count.get(split_vcf_header[i]);
									}
									if (mutation_with_RNAexpr_count.containsKey(split_vcf_header[i])) {
										mutation_with_RNAexpr_count_value = (Double)mutation_with_RNAexpr_count.get(split_vcf_header[i]);
									}
									out.write(line + "\t" + split[i] + "\t" + split[i].split(":")[0].split(",")[0] + "\t" + split[i].split(":")[0].split(",")[1] + "\t" + ssGSEA_line + "\t" + mutation_count_value + "\t" + mutation_pass_count_value + "\t" + mutation_pass_RNAexpr_count_value + "\t" + mutation_with_RNAexpr_count_value + "\t" + RNAmaf + "\t" + DNAmaf + "\t" + rna2dna_maf_ratio + "\t" + tumorType + "\t" + cibersort_line + "\n");
									System.out.println(name + "\t" + split_vcf_header[i] + "\t" + split[i] + "\t" + ssGSEA_line);
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
