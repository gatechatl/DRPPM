package rnaseq.splicing.mats402;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class RMATS402CompareSplicingResultsSDWithBlackList {
	public static String type() {
		return "rMATS";
	}
	public static String description() {
		return "rMATS 4.0.2 result compare splicing result.";
	}
	public static String parameter_info() {
		return "[summary_file_1] [summary_file_2] [limma_file_1] [limma_file_2] [blacklist.txt] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String summary_file_1 = args[0];
			String summary_file_2 = args[1];			
			String limma_file_1 = args[2];
			String limma_file_2 = args[3];
			String black_list_file = args[4];
			String outputMatrixFile = args[5];

			FileWriter fwriter = new FileWriter(outputMatrixFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			out.write("Gene\tFile1Gene\tFile1_Skipped\tFile1_Inclusion\tFile1_IncreaseRI\tFile1_DecreaseRI\tFile1_SDIncreaseRI\tFile1SDDecreaseRI\tFile1_MXE\tFile1_A3SS\tFile1_A5SS\tFile1_UpReg\tFile1_DnReg\tFile2Gene\tFile2_Skipped\tFile2_Inclusion\tFile2_IncreaseRI\tFile2_DecreaseRI\tFile2_SDIncreaseRI\tFile2SDDecreaseRI\tFile2_MXE\tFile2_A3SS\tFile2_A5SS\tFile2_UpReg\tFile2_DnReg\tBlackList\n");
			HashMap all_genes = new HashMap();
			HashMap alternative_spliced_gene_list1 = new HashMap();			
			HashMap skipped_gene_list1 = new HashMap();
			HashMap included_gene_list1 = new HashMap();
			HashMap increased_RI_gene_list1 = new HashMap();
			HashMap decreased_RI_gene_list1 = new HashMap();
			HashMap SD_increased_IR_gene_list1 = new HashMap();
			HashMap SD_decreased_IR_gene_list1 = new HashMap();
			HashMap mxe_gene_list1 = new HashMap();
			HashMap a3ss_gene_list1 = new HashMap();
			HashMap a5ss_gene_list1 = new HashMap();
			
			HashMap alternative_spliced_gene_list2 = new HashMap();
			HashMap skipped_gene_list2 = new HashMap();
			HashMap included_gene_list2 = new HashMap();
			HashMap increased_RI_gene_list2 = new HashMap();
			HashMap decreased_RI_gene_list2 = new HashMap();
			HashMap SD_increased_IR_gene_list2 = new HashMap();
			HashMap SD_decreased_IR_gene_list2 = new HashMap();
			HashMap mxe_gene_list2 = new HashMap();
			HashMap a3ss_gene_list2 = new HashMap();
			HashMap a5ss_gene_list2 = new HashMap();
			
			HashMap black_list = new HashMap();
			
			FileInputStream fstream = new FileInputStream(summary_file_1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("Affected_Gene List:")) {
					for (String gene: split[1].split(",")) {
						alternative_spliced_gene_list1.put(gene, gene);
						all_genes.put(gene, gene);
					}
				}
				if (split[0].equals("Skipped_Gene List:")) {
					for (String gene: split[1].split(",")) {
						skipped_gene_list1.put(gene, gene);
					}
				}
				if (split[0].equals("inclusion_Gene List:")) {
					for (String gene: split[1].split(",")) {
						included_gene_list1.put(gene, gene);
					}
				}
				if (split[0].equals("increased_RI_map List:")) {
					for (String gene: split[1].split(",")) {
						increased_RI_gene_list1.put(gene, gene);
					}
				}
				if (split[0].equals("decreased_RI_map List:")) {
					for (String gene: split[1].split(",")) {
						decreased_RI_gene_list1.put(gene, gene);
					}
				}
				if (split[0].equals("SD_increased_IR_map List:")) {
					for (String gene: split[1].split(",")) {
						SD_increased_IR_gene_list1.put(gene, gene);
					}
				}
				if (split[0].equals("SD_decreased_IR_map List:")) {
					for (String gene: split[1].split(",")) {
						SD_decreased_IR_gene_list1.put(gene, gene);
					}
				}
				if (split[0].equals("MXE_map List:")) {
					for (String gene: split[1].split(",")) {
						mxe_gene_list1.put(gene, gene);
					}
				}
				if (split[0].equals("A3SS_map List:")) {
					for (String gene: split[1].split(",")) {
						a3ss_gene_list1.put(gene, gene);
					}
				}
				if (split[0].equals("A5SS_map List:")) {
					for (String gene: split[1].split(",")) {
						a5ss_gene_list1.put(gene, gene);
					}
				}
				
			}
			in.close();
			
			fstream = new FileInputStream(summary_file_2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("Affected_Gene List:")) {
					for (String gene: split[1].split(",")) {
						alternative_spliced_gene_list2.put(gene, gene);
						all_genes.put(gene, gene);
					}
				}
				if (split[0].equals("Skipped_Gene List:")) {
					for (String gene: split[1].split(",")) {
						skipped_gene_list2.put(gene, gene);
					}
				}
				if (split[0].equals("inclusion_Gene List:")) {
					for (String gene: split[1].split(",")) {
						included_gene_list2.put(gene, gene);
					}
				}
				if (split[0].equals("increased_RI_map List:")) {
					for (String gene: split[1].split(",")) {
						increased_RI_gene_list2.put(gene, gene);
					}
				}
				if (split[0].equals("decreased_RI_map List:")) {
					for (String gene: split[1].split(",")) {
						decreased_RI_gene_list2.put(gene, gene);
					}
				}
				if (split[0].equals("SD_increased_IR_map List:")) {
					for (String gene: split[1].split(",")) {
						SD_increased_IR_gene_list2.put(gene, gene);
					}
				}
				if (split[0].equals("SD_decreased_IR_map List:")) {
					for (String gene: split[1].split(",")) {
						SD_decreased_IR_gene_list2.put(gene, gene);
					}
				}
				if (split[0].equals("MXE_map List:")) {
					for (String gene: split[1].split(",")) {
						mxe_gene_list2.put(gene, gene);
					}
				}
				if (split[0].equals("A3SS_map List:")) {
					for (String gene: split[1].split(",")) {
						a3ss_gene_list2.put(gene, gene);
					}
				}
				if (split[0].equals("A5SS_map List:")) {
					for (String gene: split[1].split(",")) {
						a5ss_gene_list2.put(gene, gene);
					}
				}				
			}
			in.close();

			fstream = new FileInputStream(black_list_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				all_genes.put(split[0], split[0]);
				black_list.put(split[0], split[0]);
			}
			in.close();
			
			HashMap up_reg_genes_list1 = new HashMap();
			HashMap dn_reg_genes_list1 = new HashMap();			
			fstream = new FileInputStream(limma_file_1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double log2FC = new Double(split[1]);
				double fdr = new Double(split[5]);
				if (log2FC > 0 && fdr < 0.05) {
					all_genes.put(split[0], split[0]);
					up_reg_genes_list1.put(split[0], split[0]);
				}
				if (log2FC < 0 && fdr < 0.05) {
					all_genes.put(split[0], split[0]);
					dn_reg_genes_list1.put(split[0], split[0]);
				}
			}
			in.close();
			HashMap up_reg_genes_list2 = new HashMap();
			HashMap dn_reg_genes_list2 = new HashMap();
			fstream = new FileInputStream(limma_file_2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double log2FC = new Double(split[1]);
				double fdr = new Double(split[5]);
				if (log2FC > 0 && fdr < 0.05) {
					all_genes.put(split[0], split[0]);
					up_reg_genes_list2.put(split[0], split[0]);
				}
				if (log2FC < 0 && fdr < 0.05) {
					all_genes.put(split[0], split[0]);
					dn_reg_genes_list2.put(split[0], split[0]);
				}				
			}
			in.close();
			
			Iterator itr = all_genes.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				boolean alt_splice_1 = alternative_spliced_gene_list1.containsKey(gene);
				boolean skipped_1 = skipped_gene_list1.containsKey(gene);
				boolean included_1 = included_gene_list1.containsKey(gene);
				boolean increaseRI_1 = increased_RI_gene_list1.containsKey(gene);				
				boolean decreaseRI_1 = decreased_RI_gene_list1.containsKey(gene);
				boolean SD_increaseRI_1 = SD_increased_IR_gene_list1.containsKey(gene);
				boolean SD_decreaseRI_1 = SD_decreased_IR_gene_list1.containsKey(gene);			
				boolean mxe_1 = mxe_gene_list1.containsKey(gene);
				boolean a3ss_1 = a3ss_gene_list1.containsKey(gene);
				boolean a5ss_1 = a5ss_gene_list1.containsKey(gene);
				boolean up_reg_genes_1 = up_reg_genes_list1.containsKey(gene);
				boolean dn_reg_genes_1 = dn_reg_genes_list1.containsKey(gene);
				boolean alt_splice_2 = alternative_spliced_gene_list2.containsKey(gene);
				boolean skipped_2 = skipped_gene_list2.containsKey(gene);
				boolean included_2 = included_gene_list2.containsKey(gene);
				boolean increaseRI_2 = increased_RI_gene_list2.containsKey(gene);
				boolean decreaseRI_2 = decreased_RI_gene_list2.containsKey(gene);
				boolean SD_increaseRI_2 = SD_increased_IR_gene_list2.containsKey(gene);
				boolean SD_decreaseRI_2 = SD_decreased_IR_gene_list2.containsKey(gene);
				boolean mxe_2 = mxe_gene_list2.containsKey(gene);
				boolean a3ss_2 = a3ss_gene_list2.containsKey(gene);
				boolean a5ss_2 = a5ss_gene_list2.containsKey(gene);
				boolean up_reg_genes_2 = up_reg_genes_list2.containsKey(gene);
				boolean dn_reg_genes_2 = dn_reg_genes_list2.containsKey(gene);				
				boolean black_list_flag = black_list.containsKey(gene);
				out.write(gene + "\t" + alt_splice_1 + "\t" + skipped_1 + "\t" + included_1 + "\t" + increaseRI_1 
						+ "\t" + decreaseRI_1 + "\t" + SD_increaseRI_1 + "\t" + SD_decreaseRI_1 + "\t" + mxe_1 + "\t" + a3ss_1 + "\t" + a5ss_1 + "\t" + up_reg_genes_1 + "\t" + dn_reg_genes_1 + "\t" + alt_splice_2  
						+ "\t" + skipped_2 + "\t" + included_2 + "\t" + increaseRI_2 + "\t" + decreaseRI_2 + "\t" + SD_increaseRI_2 + "\t" + SD_decreaseRI_2 + "\t" 
						+ mxe_2 + "\t" + a3ss_2 + "\t" + a5ss_2 + "\t" + up_reg_genes_2 + "\t" + dn_reg_genes_2 + "\t" + black_list_flag + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
