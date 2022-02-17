package rnaseq.splicing.cseminer.prioritization;

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
 * Given the exon annotation, we will look at the percentage of exons significantly expressed.
 * @author 4472414
 *
 */
public class CSEminerPrioritization {
	
	public static void main(String[] args) {
				
		try {
			

			FileWriter fwriter = new FileWriter("/Users/4472414/Projects/CSIMiner/FacilitatedManualChecking/Output_Exon_Annotations_20220217.txt");
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap gene2transcripts = new HashMap();
			HashMap transcript_total_exon_count = new HashMap();
			HashMap transcript_sig_exon_count = new HashMap();
			
			int count_lines = 0;
			String inputFile = "/Users/4472414/Projects/CSIMiner/FacilitatedManualChecking/Comprehensive_Exon_Annotations_20220217.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				count_lines++;
				String[] split = str.split("\t");
				
				String exon = split[0];
				String gene = exon.split("_")[0];
				String sig = split[5];
				
				boolean protein = new Boolean(split[4]);
				String[] transcripts = split[20].split(",");
				LinkedList transcript_list = new LinkedList();
				//System.out.println(sig);
				if (gene2transcripts.containsKey(gene)) {
					transcript_list = (LinkedList)gene2transcripts.get(gene);
				}
				for (String transcript: transcripts) {
					
					if (sig.equals("Significant")) {
						if (!transcript.equals("NA")) {
							if (transcript_sig_exon_count.containsKey(transcript)) {
								double count_sig = (Double)transcript_sig_exon_count.get(transcript);						
								count_sig++;							
								transcript_sig_exon_count.put(transcript, count_sig);
								System.out.println(transcript + "\t" + count_sig);
							} else {								
								transcript_sig_exon_count.put(transcript, 1.0);		
								
							}
						}
					}
					
					if (!transcript.equals("NA")) {
						if (transcript_total_exon_count.containsKey(transcript)) {
							double total_sig = (Double)transcript_total_exon_count.get(transcript);
							total_sig++;
							transcript_total_exon_count.put(transcript, total_sig);
						} else {
							transcript_total_exon_count.put(transcript, 1.0);
						}
						if (!transcript_list.contains(transcript)) {
							transcript_list.add(transcript);
						}
					}
					
				}
				System.out.println(gene + "\t" + transcript_list.size());
				
				gene2transcripts.put(gene, transcript_list);
				
			}
			in.close();
			
			Iterator itr = gene2transcripts.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				
				double min = Double.MAX_VALUE;
				double max = Double.MIN_VALUE;
				String meta = "";
				LinkedList transcript_list = (LinkedList)gene2transcripts.get(gene);
				Iterator itr_transcripts = transcript_list.iterator();
				while (itr_transcripts.hasNext()) {
					String transcript = (String)itr_transcripts.next();
					
					
					if (transcript_total_exon_count.containsKey(transcript)) {
						double count_sig = 0.0;
						if (transcript_sig_exon_count.containsKey(transcript)) {
							count_sig = (Double)transcript_sig_exon_count.get(transcript);	
						}
						double count_total = (Double)transcript_total_exon_count.get(transcript);
						double proportion = count_sig / count_total;
						System.out.println(gene + "\t" + transcript + "\t" + count_sig + "\t" + count_total);
						meta += transcript + ":" + count_sig + ":" + count_total + ":" + proportion + ",";
						if (min > proportion) {
							if (proportion > 0) {
								min = proportion;
							}
						}
						if (max < proportion) {
							if (proportion < 1) {
								max = proportion;
							}
						}
					}
				}
				out.write(gene);
				out.write("\t" + min + "\t" + max + "\t" + meta);				
				out.write("\n");
			}
			out.close();
			
			System.out.println(count_lines);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
