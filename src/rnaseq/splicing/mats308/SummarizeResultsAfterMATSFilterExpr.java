package rnaseq.splicing.mats308;

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
 * Summarize MATS and for the check if any of the gene passes the fpkm cutoff in any sample.
 * @author tshaw
 *
 */
public class SummarizeResultsAfterMATSFilterExpr {
	public static String type() {
		return "MATS";
	}
	public static String description() {
		return "Summarize MATS results with expression filter.";
	}
	
	public static String parameter_info() {
		return "[inputFilePath] [SampleName] [GENE/EVENT] [fpkm table] [value cutoff for fpkm table] [gtf_file] [num_exon_cutoff]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap total_gene = new HashMap();
			String inputFilePaths = args[0];
			String[] split_inputFilePaths = inputFilePaths.split(",");
			String sampleName = args[1];
			String flag = args[2].toUpperCase();
			String matrixFile = args[3];
			double fpkm_cutoff = new Double(args[4]);
			String gtf_file = args[5];
			int num_exon_cutoff = new Integer(args[6]);
			
			HashMap exon_count = new HashMap();
			HashMap max_count = new HashMap();
			HashMap transcript2geneName = new HashMap();
			HashMap geneName2transcript = new HashMap();

			HashMap transcript2gene_id = new HashMap();
			HashMap gene_id2transcript = new HashMap();
			
			FileInputStream fstream = new FileInputStream(gtf_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String entry_type = split[2];
				String gene_name = GTFFile.grabMeta(split[8].trim(), "gene_name").replaceAll("\"", "");
				String gene_id = GTFFile.grabMeta(split[8].trim(), "gene_id").replaceAll("\"", "");
				String transcript_id = GTFFile.grabMeta(split[8].trim(), "transcript_id").replaceAll("\"", "");
				if (entry_type.equals("exon")) {
					transcript2geneName.put(transcript_id, gene_name);
					if (geneName2transcript.containsKey(gene_name)) {
						LinkedList list = (LinkedList)geneName2transcript.get(gene_name);
						if (!list.contains(transcript_id)) {
							list.add(transcript_id);
							geneName2transcript.put(gene_name, list);
						}
					} else {
						LinkedList list = new LinkedList();
						list.add(transcript_id);
						geneName2transcript.put(gene_name, list);
					}
					
					if (exon_count.containsKey(transcript_id)) {
						int count = (Integer)exon_count.get(transcript_id);
						count = count + 1;
						exon_count.put(transcript_id, count);
					} else {
						exon_count.put(transcript_id, 1);
					}
				}
			}
			in.close();
			
			HashMap gene_name_exon_count = new HashMap();
			
			Iterator itr = geneName2transcript.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				int max = 0;
				
				LinkedList transcripts = (LinkedList)geneName2transcript.get(geneName);
				Iterator itr2 = transcripts.iterator();
				while (itr2.hasNext()) {
					String transcript = (String)itr2.next();
					int count = (Integer)exon_count.get(transcript);
					if (count > max ) {
						max = count;
					}
				}
				gene_name_exon_count.put(geneName, max);
			}
			
			HashMap gene_pass_cutoff = new HashMap();
			HashMap total_count_splicing_gene_pass_fpkm_cutoff = new HashMap();
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[0];
				for (int i = 1; i < split.length; i++) {
					boolean pass_exon_cutoff = false;
					if (gene_name_exon_count.containsKey(gene)) {
						int total_exon = (Integer)gene_name_exon_count.get(gene);
						if (total_exon >= num_exon_cutoff) {
							pass_exon_cutoff = true;
						}						
					}
					if (new Double(split[i]) >= fpkm_cutoff && pass_exon_cutoff) {
						gene_pass_cutoff.put(split[0], split[0]);
						
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
			String[] tags = {"Alternative 3' splice site", "Alternative 3' splice site (Pass FPKM)", "Alternative 5' splice site", "Alternative 5' splice site (Pass FPKM)", "Retained intron", "Retained intron (Pass FPKM)", "Mutually exclusive exon", "Mutually exclusive exon (Pass FPKM)", "Exon included", "Exon included (Pass FPKM)", "Exon skipped", "Exon skipped (Pass FPKM)", "Total Gene Count"};
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
					HashMap count_splicing_gene_pass_fpkm_cutoff = new HashMap();
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
						String[] split = str.replaceAll("\"", "").split("\t");
						
						double delta_psi_val = new Double(split[split.length - 1]);
						
						// If exon skipping then check if it is skipped or kept by looking at the delta psi value
						if (isSE) {
							if (delta_psi_val >= 0 && first_positive) {
								if (!geneMap.containsKey(split[1]) || !gene_level) {
									count++;
									if (gene_pass_cutoff.containsKey(split[2])) {
										count_splicing_gene_pass_fpkm_cutoff.put(split[2], split[2]);
										total_count_splicing_gene_pass_fpkm_cutoff.put(split[2], split[2]);
									}
									total_gene_count_per_comparison.put(split[1], split[1]);
									geneMap.put(split[1], split[1]);
								}
							} else if (delta_psi_val <= 0 && !first_positive) {
								if (!geneMap.containsKey(split[1]) || !gene_level) {
									count++;
									if (gene_pass_cutoff.containsKey(split[2])) {
										count_splicing_gene_pass_fpkm_cutoff.put(split[2], split[2]);
										total_count_splicing_gene_pass_fpkm_cutoff.put(split[2], split[2]);
									}
									total_gene_count_per_comparison.put(split[1], split[1]);
									geneMap.put(split[1], split[1]);
								}
							}
						} else {
							if (!geneMap.containsKey(split[1]) || !gene_level) {
								count++;
								if (gene_pass_cutoff.containsKey(split[2])) {
									count_splicing_gene_pass_fpkm_cutoff.put(split[2], split[2]);
									total_count_splicing_gene_pass_fpkm_cutoff.put(split[2], split[2]);
								}
								total_gene_count_per_comparison.put(split[1], split[1]);
								geneMap.put(split[1], split[1]);
							}
						}
						
						total_gene.put(split[1], split[1]);
					}
					in.close();
					
					System.out.print("\t" + count + "\t" + count_splicing_gene_pass_fpkm_cutoff.size());
					if (file.equals("SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt")) {
						first_positive = false;
					}
				}
				
				String genes = "";
				itr = total_gene_count_per_comparison.keySet().iterator();
				while (itr.hasNext()) {						
					String gene = (String)itr.next();
					genes += gene + ",";
				}
				System.out.println("\t" + total_gene_count_per_comparison.size() + "\t" + genes);
				
			}
			System.out.println("Total Genes: " + total_gene.size());
			System.out.println("Total FPKM Gene that passes cutoff with splicing: " + total_count_splicing_gene_pass_fpkm_cutoff.size());
			System.out.println("Total FPKM Gene that passes cutoff: " + gene_pass_cutoff.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
