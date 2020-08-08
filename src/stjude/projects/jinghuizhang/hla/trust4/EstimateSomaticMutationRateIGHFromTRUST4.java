package stjude.projects.jinghuizhang.hla.trust4;

import jaligner.Alignment;
import jaligner.SmithWatermanGotoh;
import jaligner.matrix.MatrixLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;




import java.util.Iterator;

import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.formats.Pair;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import jaligner.util.Commons;
import jaligner.util.SequenceParser;

/**
 * Summarize the BCR and TCR result from TRUST4
 * Count the number of somatic mutation from the assembled transcript
 * @author tshaw
 *
 */
public class EstimateSomaticMutationRateIGHFromTRUST4 {


	public static String description() {
		return "Count the number of somatic mutation for each clonotype from the assembled transcript";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputBamLstFile] [outputSomaticMutationSummary]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputBamFilelst = args[0];	
			String outputSomaticMutationSummary = args[1];
			
			FileWriter fwriter = new FileWriter(outputSomaticMutationSummary);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write("SampleName\tClonotypeName\tMutRate\tNumMismatch\tSeqLength\n");
			
			FileInputStream fstream = new FileInputStream(inputBamFilelst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String report_tsv = split[1];
				
				String cdr3_file = report_tsv.replaceAll("_report.tsv", "_cdr3.out");
				String final_out_file = report_tsv.replaceAll("_report.tsv", "_final.out");
				HashMap seqs = new HashMap();
				HashMap names = new HashMap();
				HashMap igg_shm = new HashMap();
				HashMap igg_shm_stat = new HashMap();
				String assemble = "";
				String name = "";
				FileInputStream fstream2 = new FileInputStream(cdr3_file);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					if ((split2[5].contains("IGHG") || split2[5].contains("IGHA") || split2[5].contains("IGHM") || split2[5].contains("IGHD")) && new Integer(split2[1]) == 0) {						
						igg_shm.put(split2[0], split2[6] + "\t" + split2[7] + "\t" + split2[8]);
					}					
					if ((split2[5].contains("IGHG") || split2[5].contains("IGHA") || split2[5].contains("IGHM") || split2[5].contains("IGHD")) && new Integer(split2[1]) > 0) {
						String ref = (String)igg_shm.get(split2[0]);
						String seq1 = ref.split("\t")[0];
						String seq2 = ref.split("\t")[1];
						String seq3 = ref.split("\t")[2];
						if (!split2[6].equals("*")) {
							int mismatch = count_mismatch(seq1, split2[6]);
							if (igg_shm_stat.containsKey(split2[5])) {
								String orig_result = (String)igg_shm_stat.get(split2[5]);
								int orig_mismatch = new Integer(orig_result.split("\t")[0]);
								int orig_length = new Integer(orig_result.split("\t")[1]);
								orig_mismatch += mismatch;
								orig_length += seq1.length();
								String new_result = orig_mismatch + "\t" + orig_length;
								igg_shm_stat.put(split2[5], new_result);
							} else {
								String result = mismatch + "\t" + seq1.length();
								igg_shm_stat.put(split2[5], result);
							}
						}
						if (!split2[7].equals("*")) {
							int mismatch = count_mismatch(seq2, split2[7]);
							if (igg_shm_stat.containsKey(split2[5])) {
								String orig_result = (String)igg_shm_stat.get(split2[5]);
								int orig_mismatch = new Integer(orig_result.split("\t")[0]);
								int orig_length = new Integer(orig_result.split("\t")[1]);
								orig_mismatch += mismatch;
								orig_length += seq2.length();
								String new_result = orig_mismatch + "\t" + orig_length;
								igg_shm_stat.put(split2[5], new_result);
							} else {
								String result = mismatch + "\t" + seq2.length();
								igg_shm_stat.put(split2[5], result);
							}
						}
						if (!split2[8].equals("*")) {
							int mismatch = count_mismatch(seq3, split2[8]);
							if (igg_shm_stat.containsKey(split2[5])) {
								String orig_result = (String)igg_shm_stat.get(split2[5]);
								int orig_mismatch = new Integer(orig_result.split("\t")[0]);
								int orig_length = new Integer(orig_result.split("\t")[1]);
								orig_mismatch += mismatch;
								orig_length += seq3.length();
								String new_result = orig_mismatch + "\t" + orig_length;
								igg_shm_stat.put(split2[5], new_result);
							} else {
								String result = mismatch + "\t" + seq3.length();
								igg_shm_stat.put(split2[5], result);
							}
						}
					}
				}
				in2.close();
				/*
				fstream2 = new FileInputStream(cdr3_file);
				din2 = new DataInputStream(fstream2);
				in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					if (split2[5].contains("IGHG") && new Integer(split2[1]) > 0) {
						String ref = (String)igg_shm.get(split2[5]);
						String seq1 = ref.split("\t")[0];
						String seq2 = ref.split("\t")[0];
						String seq3 = ref.split("\t")[0];
						if (!split2[8].equals("*")) {
							int mismatch = count_mismatch(seq3, split2[8]);
							if (igg_shm_stat.containsKey(split2[5])) {
								String orig_result = (String)igg_shm_stat.get(split2[5]);
								int orig_mismatch = new Integer(orig_result.split("\t")[0]);
								int orig_length = new Integer(orig_result.split("\t")[1]);
								orig_mismatch += mismatch;
								orig_length += seq3.length();
								String new_result = orig_mismatch + "\t" + orig_length;
								igg_shm_stat.put(split2[5], new_result);
							} else {
								String result = mismatch + "\t" + seq3.length();
								igg_shm_stat.put(split2[5], result);
							}
						}
						
						
					}					
				}
				in2.close();	
				*/
				Iterator itr = igg_shm_stat.keySet().iterator();
				while (itr.hasNext()) {
					String ighg_name = (String)itr.next();
					String result = (String)igg_shm_stat.get(ighg_name);
					double mut_rate = new Double(result.split("\t")[0]) / new Double(result.split("\t")[1]);
					//if (mut_rate >= 0) {
					//if (new Double(result.split("\t")[1]) > 0) {
					out.write(sampleName + "\t" + ighg_name + "\t" + mut_rate + "\t" + result + "\n");
					//}

				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int count_mismatch(String seq1, String seq2) {
		int count_mismatch = 0;
	
		try {
			Sequence s1 = SequenceParser.parse(seq1);
			Sequence s2 = SequenceParser.parse(seq2);
			Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("BLOSUM62"), 10f, 0.5f);
			//System.out.println ( alignment.getSummary() );
			String aln1 = new String(alignment.getSequence1());
			String line = new String(alignment.getMarkupLine());
			String aln2 = new String(alignment.getSequence2());
			System.out.println(aln1);
			System.out.println(line);
			System.out.println(aln2);
			
			for (int i = 0; i < line.length(); i++) {				
				if (line.substring(i, i + 1).equals(".") || line.substring(i, i + 1).equals(" ")) {					
					count_mismatch++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count_mismatch;
	}
	/*
	public static void main(String[] args) {
		try {
			String seq1 = "TGCCTCCCAGGTTCAAGTGATTCTCTGGCCTCAGCCTCCCCAGTAGCTGGGATTACAGGCGTGAGCCACTGCACCTGG";
			String seq2 = "TGCCTCCCAGTTTCAAGTGATTCTTTCTAGCCTCAGCCTCCCCAGTAGCTGGGATTACAGGCGTGAGCCACTGCACCTGG";
			Sequence s1 = SequenceParser.parse(seq1);
			Sequence s2 = SequenceParser.parse(seq2);
			Alignment alignment = SmithWatermanGotoh.align(s1, s2, MatrixLoader.load("BLOSUM62"), 10f, 0.5f);
			//System.out.println ( alignment.getSummary() );
			String aln1 = new String(alignment.getSequence1());
			String line = new String(alignment.getMarkupLine());
			String aln2 = new String(alignment.getSequence2());
			System.out.println(aln1);
			System.out.println(line);
			System.out.println(aln2);
			int count_mismatch = 0;
			for (int i = 0; i < line.length(); i++) {				
				if (line.substring(i, i + 1).equals(".") || line.substring(i, i + 1).equals(" ")) {					
					count_mismatch++;
				}
			}
			System.out.println(count_mismatch);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
}
