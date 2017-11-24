package RNAseqTools.AlternativeSplicing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SummarizeResultsAfterMATSFilter {
	
	public static String parameter_info() {
		return "[inputFilePath] [SampleName] [GENE/EVENT]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap total_gene = new HashMap();
			String inputFilePaths = args[0];
			String[] split_inputFilePaths = inputFilePaths.split(",");
			String sampleName = args[1];
			String flag = args[2].toUpperCase();
			boolean gene_level = false;
			if (flag.equals("GENE")) {
				gene_level = true;
			}
			String[] split_sampleName = sampleName.split(",");
			String[] files = {"A3SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "A5SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "RI.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "MXE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt"};
			
			System.out.print("SampleNames");
			String[] tags = {"Alternative 3' splice site", "Alternative 5' splice site", "Retained intron", "Mutually exclusive exon", "Exon included", "Exon skipped"};
			for (String tag: tags) {
				System.out.print("\t" + tag);
			}
			System.out.println();
			for (int i = 0; i < split_inputFilePaths.length; i++) {
				System.out.print(split_sampleName[i]);
				boolean first_positive = true;
				boolean isSE = false;
				for (String file: files) {				
					
					HashMap map = new HashMap();
					int count = 0;
					if (file.equals("SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt")) {
						isSE = true;
					}
					HashMap geneMap = new HashMap();
					FileInputStream fstream = new FileInputStream(split_inputFilePaths[i] + "/" + file);
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					in.readLine();
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						
						double val = new Double(split[split.length - 1]);
						if (isSE) {
							if (val >= 0 && first_positive) {
								if (!geneMap.containsKey(split[1]) || !gene_level) {
									count++;
								}
							} else if (val <= 0 && !first_positive) {
								if (!geneMap.containsKey(split[1]) || !gene_level) {
									count++;
								}
							}
						} else {
							if (!geneMap.containsKey(split[1]) || !gene_level) {
								count++;
							}
						}
						total_gene.put(split[1], split[1]);
						geneMap.put(split[1], split[1]);
					}
					in.close();
					System.out.print("\t" + count);
					if (file.equals("SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt.filter.txt")) {
						first_positive = false;
					}
				}
				System.out.println();
				
			}
			System.out.println("Total Genes: " + total_gene.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
