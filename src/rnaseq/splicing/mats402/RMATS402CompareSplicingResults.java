package rnaseq.splicing.mats402;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class RMATS402CompareSplicingResults {
	public static String type() {
		return "rMATS";
	}
	public static String description() {
		return "rMATS 4.0.2 result compare splicing result.";
	}
	public static String parameter_info() {
		return "[summary_file_1] [summary_file_2] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String summary_file_1 = args[0];
			String summary_file_2 = args[1];			
			String outputMatrixFile = args[2];

			FileWriter fwriter = new FileWriter(outputMatrixFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			out.write("Gene\tFile1Gene\tFile1_Skipped\tFile1_Inclusion\tFile1_IncreaseRI\tFile1_DecreaseRI\tFile1_MXE\tFile1_A3SS\tFile1_A5SS\tFile2Gene\tFile2_Skipped\tFile2_Inclusion\tFile2_IncreaseRI\tFile2_DecreaseRI\tFile2_MXE\tFile2_A3SS\tFile2_A5SS\n");
			HashMap all_genes = new HashMap();
			HashMap alternative_spliced_gene_list1 = new HashMap();			
			HashMap skipped_gene_list1 = new HashMap();
			HashMap included_gene_list1 = new HashMap();
			HashMap increased_RI_gene_list1 = new HashMap();
			HashMap decreased_RI_gene_list1 = new HashMap();
			HashMap mxe_gene_list1 = new HashMap();
			HashMap a3ss_gene_list1 = new HashMap();
			HashMap a5ss_gene_list1 = new HashMap();
			
			HashMap alternative_spliced_gene_list2 = new HashMap();
			HashMap skipped_gene_list2 = new HashMap();
			HashMap included_gene_list2 = new HashMap();
			HashMap increased_RI_gene_list2 = new HashMap();
			HashMap decreased_RI_gene_list2 = new HashMap();
			HashMap mxe_gene_list2 = new HashMap();
			HashMap a3ss_gene_list2 = new HashMap();
			HashMap a5ss_gene_list2 = new HashMap();
			
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
			
			Iterator itr = all_genes.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				boolean alt_splice_1 = alternative_spliced_gene_list1.containsKey(gene);
				boolean skipped_1 = skipped_gene_list1.containsKey(gene);
				boolean included_1 = included_gene_list1.containsKey(gene);
				boolean increaseRI_1 = increased_RI_gene_list1.containsKey(gene);
				boolean decreaseRI_1 = decreased_RI_gene_list1.containsKey(gene);
				boolean mxe_1 = mxe_gene_list1.containsKey(gene);
				boolean a3ss_1 = a3ss_gene_list1.containsKey(gene);
				boolean a5ss_1 = a5ss_gene_list1.containsKey(gene);
				
				boolean alt_splice_2 = alternative_spliced_gene_list2.containsKey(gene);
				boolean skipped_2 = skipped_gene_list2.containsKey(gene);
				boolean included_2 = included_gene_list2.containsKey(gene);
				boolean increaseRI_2 = increased_RI_gene_list2.containsKey(gene);
				boolean decreaseRI_2 = decreased_RI_gene_list2.containsKey(gene);
				boolean mxe_2 = mxe_gene_list2.containsKey(gene);
				boolean a3ss_2 = a3ss_gene_list2.containsKey(gene);
				boolean a5ss_2 = a5ss_gene_list2.containsKey(gene);
				
				out.write(gene + "\t" + alt_splice_1 + "\t" + skipped_1 + "\t" + included_1 + "\t" + increaseRI_1 
						+ "\t" + decreaseRI_1 + "\t" + mxe_1 + "\t" + a3ss_1 + "\t" + a5ss_1 + "\t" + alt_splice_2  
						+ "\t" + skipped_2 + "\t" + included_2 + "\t" + increaseRI_2 + "\t" + decreaseRI_2 + "\t" 
						+ mxe_2 + "\t" + a3ss_2 + "\t" + a5ss_2 + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
