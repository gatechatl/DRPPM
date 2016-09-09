package RNAseqTools.SpliceFactorAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


/**
 * Based on the MATS result
 * @author tshaw
 *
 */
public class ReadMATSExtractNeighboringSequencing {

	public static String parameter_info() {
		return "[inputFile] [organism] [psiCol] [cutoff] [skippingTag (SKIP/KEEP)] [outputFileUpstream] [outputFileExon] [outputFileDnstream]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String organism = args[1];
			//int psicol = new Integer(args[2]);
			double fdr_cutoff = new Double(args[2]);
			double psi_cutoff = new Double(args[3]);
			
			String skippedTag = args[4];
			String outputFileUpstream = args[5];
			String outputFileExon = args[6];
			String outputFileDnstream = args[7];
			
			String outputFileUpstreamFasta = outputFileUpstream + ".fasta";
			String outputFileExonFasta = outputFileExon + ".fasta";
			String outputFileDnstreamFasta = outputFileDnstream + ".fasta";
			FileWriter fwriter1 = new FileWriter(outputFileUpstream);
			BufferedWriter outUp = new BufferedWriter(fwriter1);

			FileWriter fwriter2 = new FileWriter(outputFileExon);
			BufferedWriter outExon = new BufferedWriter(fwriter2);

			FileWriter fwriter3 = new FileWriter(outputFileDnstream);
			BufferedWriter outDn = new BufferedWriter(fwriter3);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");
			
			int chrIndex = findIndex(header, "chr");
			int psicol = findIndex(header, "IncLevelDifference");
			int strandIndex = findIndex(header, "strand");
			int startIndex = findIndex(header, "exonStart_0base");
			int endIndex = findIndex(header, "exonEnd");
			int fdrIndex = findIndex(header, "FDR");
			int pvalueIndex = findIndex(header, "PValue");
			
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				
				if (!split[0].equals("GeneID")) {
					double psi = new Double(split[psicol]);
					String chr = split[chrIndex];
					String strand = split[strandIndex];
					double fdr = new Double(split[fdrIndex]);
					double pvalue = new Double(split[pvalueIndex]);
					
					int start = new Integer(split[startIndex]);
					int end = new Integer(split[endIndex]);
					if (strand.equals("+")) {
						
						
						start = start - 14;
						end = end + 9;
						String upstream = chr + "\t+\t" + (start - 100) + "\t" + (start - 1);
						String exon = chr + "\t+\t" + (start + 15) + "\t" + (end - 12);
						String dnstream= chr + "\t+\t" + (end + 1) + "\t" + (end + 100);
						if (fdr <= fdr_cutoff) {
							if (psi < psi_cutoff * -1 && skippedTag.equals("SKIP")) {
								outUp.write(upstream + "\n");
								outExon.write(exon + "\n");
								outDn.write(dnstream + "\n");
							} else if (psi > psi_cutoff && skippedTag.equals("KEEP")) {
								outUp.write(upstream + "\n");
								outExon.write(exon + "\n");
								outDn.write(dnstream + "\n");							
							}
						}
						
						
					} else {
						start = start - 8;
						end = end + 15;
						
						String dnstream = chr + "\t-\t" + (start - 100) + "\t" + (start - 1);
						String exon = chr + "\t-\t" + (start + 11) + "\t" + (end - 16);
						String upstream= chr + "\t-\t" + (end + 1) + "\t" + (end + 100);
						if (fdr <= fdr_cutoff) {
							if (psi < psi_cutoff * -1 && skippedTag.equals("SKIP")) {
								outUp.write(upstream + "\n");
								outExon.write(exon + "\n");
								outDn.write(dnstream + "\n");
							} else if (psi > psi_cutoff && skippedTag.equals("KEEP")) {
								outUp.write(upstream + "\n");
								outExon.write(exon + "\n");
								outDn.write(dnstream + "\n");							
							}
						}
						
					}
				}
			}
			in.close();
			outUp.close();
			outExon.close();
			outDn.close();
			
			GrabExonInformation.grabExon(outputFileUpstream, outputFileUpstreamFasta, organism);
			GrabExonInformation.grabExon(outputFileExon, outputFileExonFasta, organism);
			GrabExonInformation.grabExon(outputFileDnstream, outputFileDnstreamFasta, organism);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int findIndex(String[] split, String key) {
		for (int i = 0; i < split.length; i++) {
			if (split[i].equals(key)) {
				return i;
			}
		}
		return -1;
	}
}
