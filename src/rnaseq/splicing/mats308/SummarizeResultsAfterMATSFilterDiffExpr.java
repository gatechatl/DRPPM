package rnaseq.splicing.mats308;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Summarize MATS and for the check if any of the gene passes the fpkm cutoff in any sample.
 * @author tshaw
 *
 */
public class SummarizeResultsAfterMATSFilterDiffExpr {
	public static String type() {
		return "MATS";
	}
	public static String description() {
		return "Summarize MATS results with expression filter.";
	}
	
	public static String parameter_info() {
		return "[inputFilePath] [SampleName] [GENE/EVENT] [limma table] [fdr cutoff]";
	}
	public static void execute(String[] args) {
		//System.out.println("Running");
		try {
			
			HashMap total_gene = new HashMap();
			String inputFilePaths = args[0];
			String[] split_inputFilePaths = inputFilePaths.split(",");
			String sampleName = args[1];
			String flag = args[2].toUpperCase();
			String matrixFile = args[3];
			//System.out.println(args[0]);
			//System.out.println(args[4]);
			//System.out.println("######################");
			
			//System.exit(0);
			
			double fdr = new Double(args[4]);
			 
			
			HashMap gene_pass_cutoff_up = new HashMap();
			HashMap gene_pass_cutoff_dn = new HashMap();
			HashMap total_count_splicing_gene_pass_fpkm_cutoff_up = new HashMap();
			HashMap total_count_splicing_gene_pass_fpkm_cutoff_dn = new HashMap();
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[0].replaceAll("\"",  "");
				if (new Double(split[5]) < fdr) {
					if (new Double(split[1]) > 0) {
						gene_pass_cutoff_up.put(gene, str);
					} else if (new Double(split[1]) < 0) {
						gene_pass_cutoff_dn.put(gene, str);
					}
					
				}
				
			}
			in.close();
			boolean gene_level = false;
			if (flag.equals("GENE")) {
				gene_level = true;
			}
			String[] split_sampleName = sampleName.split(",");
			String[] files = {"A3SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "A5SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "RI.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "MXE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt"};
			
			System.out.print("SampleNames");
			String[] tags = {"Alternative 3' splice site", "Alternative 3' splice site (DE Up)", "Alternative 3' splice site (DE Down)", "Alternative 5' splice site", "Alternative 5' splice site (DE Up)", "Alternative 5' splice site (DE Down)", "Retained intron", "Retained intron (DE Up)", "Retained intron (DE Down)", "Mutually exclusive exon", "Mutually exclusive exon (DE Up)", "Mutually exclusive exon (DE Down)", "Exon included", "Exon included (DE Up)", "Exon included (DE Down)", "Exon skipped", "Exon skipped (DE Up)", "Exon skipped (DE Down)", "Total Gene Count"};
			for (String tag: tags) {
				System.out.print("\t" + tag);
			}
			System.out.println();
			for (int i = 0; i < split_inputFilePaths.length; i++) {
				System.out.print(split_sampleName[i]);
				
				HashMap total_gene_count_per_comparison = new HashMap();
				
				boolean first_positive = true;
				boolean isSE = false;
				for (String file: files) {				
					
					HashMap map = new HashMap();
					HashMap count_splicing_gene_pass_fpkm_cutoff_up = new HashMap();
					HashMap count_splicing_gene_pass_fpkm_cutoff_dn = new HashMap();
					int count = 0;
					if (file.equals("SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt")) {
						isSE = true;
					}
					HashMap geneMap = new HashMap();
					fstream = new FileInputStream(split_inputFilePaths[i] + "/" + file);
					din = new DataInputStream(fstream);
					in = new BufferedReader(new InputStreamReader(din));
					in.readLine();
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						
						double delta_psi_val = new Double(split[split.length - 1]);
						
						// If exon skipping then check if it is skipped or kept by looking at the delta psi value
						if (isSE) {
							if (delta_psi_val >= 0 && first_positive) {
								if (!geneMap.containsKey(split[1]) || !gene_level) {
									count++;
									if (gene_pass_cutoff_up.containsKey(split[2])) {
										
										count_splicing_gene_pass_fpkm_cutoff_up.put(split[2], split[2]);
										total_count_splicing_gene_pass_fpkm_cutoff_up.put(split[2], gene_pass_cutoff_up.get(split[2]));
									}
									if (gene_pass_cutoff_dn.containsKey(split[2])) {										
										count_splicing_gene_pass_fpkm_cutoff_dn.put(split[2], split[2]);
										total_count_splicing_gene_pass_fpkm_cutoff_dn.put(split[2], gene_pass_cutoff_dn.get(split[2]));
									}
									total_gene_count_per_comparison.put(split[1], split[1]);
									geneMap.put(split[1], split[1]);
								}
							} else if (delta_psi_val <= 0 && !first_positive) {
								if (!geneMap.containsKey(split[1]) || !gene_level) {
									count++;
									if (gene_pass_cutoff_up.containsKey(split[2])) {
										count_splicing_gene_pass_fpkm_cutoff_up.put(split[2], split[2]);
										total_count_splicing_gene_pass_fpkm_cutoff_up.put(split[2], gene_pass_cutoff_up.get(split[2]));
									}
									if (gene_pass_cutoff_dn.containsKey(split[2])) {
										count_splicing_gene_pass_fpkm_cutoff_dn.put(split[2], split[2]);
										total_count_splicing_gene_pass_fpkm_cutoff_dn.put(split[2], gene_pass_cutoff_dn.get(split[2]));
									}
									total_gene_count_per_comparison.put(split[1], split[1]);
									geneMap.put(split[1], split[1]);
								}
							}
						} else {
							if (!geneMap.containsKey(split[1]) || !gene_level) {
								count++;
								if (gene_pass_cutoff_up.containsKey(split[2])) {
									count_splicing_gene_pass_fpkm_cutoff_up.put(split[2], split[2]);
									total_count_splicing_gene_pass_fpkm_cutoff_up.put(split[2], gene_pass_cutoff_up.get(split[2]));
								}
								if (gene_pass_cutoff_dn.containsKey(split[2])) {
									count_splicing_gene_pass_fpkm_cutoff_dn.put(split[2], split[2]);
									total_count_splicing_gene_pass_fpkm_cutoff_dn.put(split[2], gene_pass_cutoff_dn.get(split[2]));
								}
								total_gene_count_per_comparison.put(split[1], split[1]);
								geneMap.put(split[1], split[1]);
							}
						}
						total_gene.put(split[1], split[1]);
						
						
					}
					in.close();
					System.out.print("\t" + count + "\t" + count_splicing_gene_pass_fpkm_cutoff_up.size() + "\t" + count_splicing_gene_pass_fpkm_cutoff_dn.size());
					if (file.equals("SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt")) {
						first_positive = false;
					}
				}
				System.out.println("\t" + total_gene_count_per_comparison.size());
				
			}
			System.out.println("Total Genes: " + total_gene.size());
			System.out.println("Total FPKM Gene with up reg differential expression (fdr < " + fdr + ") with splicing change: " + total_count_splicing_gene_pass_fpkm_cutoff_up.size());
			System.out.println("Total FPKM Gene with up reg differential expression (fdr < " + fdr + "): " + gene_pass_cutoff_up.size());
			Iterator itr = total_count_splicing_gene_pass_fpkm_cutoff_up.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				System.out.println(total_count_splicing_gene_pass_fpkm_cutoff_up.get(gene));
			}
			System.out.println("Total FPKM Gene with down reg differential expression (fdr < " + fdr + ") with splicing change: " + total_count_splicing_gene_pass_fpkm_cutoff_dn.size());
			System.out.println("Total FPKM Gene with down reg differential expression (fdr < " + fdr + "): " + gene_pass_cutoff_dn.size());
			itr = total_count_splicing_gene_pass_fpkm_cutoff_dn.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				System.out.println(total_count_splicing_gene_pass_fpkm_cutoff_dn.get(gene));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
