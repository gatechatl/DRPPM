package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class McKinnonOverlapIRFinderSD {

	
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\mRNAseq_RNASEQ\\intron_retention\\limma\\APTXATM_vs_WT_all.txt_removequotation.txt";			
			HashMap candidates = new HashMap();
			HashMap intron_retained = new HashMap();
			HashMap intron_excluded = new HashMap();
			HashMap intron_retention_log2FC = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				intron_retention_log2FC.put(split[0], split[1]);
				if (new Double(split[5]) < 0.05) {
					candidates.put(split[0], split[0]);
					if (new Double(split[1]) > 0) {
						intron_retained.put(split[0], split[0]);
					} else {
						intron_excluded.put(split[0], split[0]);
					}
				}
			}
			in.close();
			
			HashMap log2FC = new HashMap();
			HashMap fdr = new HashMap();
			String limma_result = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\mRNAseq_RNASEQ\\limma\\Cerebellum\\McKinnon.APTXATM.PARPATM.WT.combined.fpkm.renamed.cerebellum.txt_limma.geneName.txt";
			fstream = new FileInputStream(limma_result);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				log2FC.put(split[0], split[12]);
				fdr.put(split[0], split[14]);
			}
			in.close();
						
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\mRNAseq_RNASEQ\\Overlap_IRFinder_SD\\IRFinderResult_Updated.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String irfinder_inputFile = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\mRNAseq_RNASEQ\\Overlap_IRFinder_SD\\IRFinderResult.txt";
			fstream = new FileInputStream(irfinder_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\tSD_Candidate\tIntron_Retention_log2FC\tIntron_Status\tLIMMA_log2FC\tLIMMA_FDR\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(str);
				String intron_retention_log2FC_str = "NA";
				String tag = "NA";
				
				String log2FC_str = "NA";
				String fdr_str = "NA";
				if (intron_retention_log2FC.containsKey(split[7])) {
					intron_retention_log2FC_str = (String)intron_retention_log2FC.get(split[7]);
				}
				if (log2FC.containsKey(split[7])) {
					log2FC_str = (String)log2FC.get(split[7]);
				}
				if (fdr.containsKey(split[7])) {
					fdr_str = (String)fdr.get(split[7]);
				}
				if (intron_retained.containsKey(split[7])) {
					tag = "retained";
				}
				if (intron_excluded.containsKey(split[7])) {
					tag = "excluded";
				}
				if (candidates.containsKey(split[7])) {
					out.write("\ttrue\t" + intron_retention_log2FC_str + "\t" + tag + "\t" + log2FC_str + "\t" + fdr_str);
				} else {
					out.write("\tfalse\t" + intron_retention_log2FC_str + "\t" + tag + "\t" + log2FC_str + "\t" + fdr_str);
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
