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

import idconversion.tools.GTFFile;

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
			
			GTFFile gtfFile = new GTFFile();
			gtfFile.initialize("/Users/4472414/References/genome/HG38_GTF/gencode.v35.primary_assembly.annotation.gtf");
			System.out.println(gtfFile.transcript_clean2biotype.size());
			System.out.println((String)gtfFile.transcript_clean2biotype.get("ENST00000389658"));
			
			FileWriter fwriter_exon = new FileWriter("/Users/4472414/Projects/CSIMiner/FacilitatedManualChecking/Output_Exon_AS_Annotations_20220218.txt");
			BufferedWriter out_exon = new BufferedWriter(fwriter_exon);
			out_exon.write("exon\ttranscript_type\n");
			HashMap gene2transcripts = new HashMap();
			HashMap transcript_total_exon_count = new HashMap();
			HashMap transcript_sig_exon_count = new HashMap();
			HashMap exon_transcripts = new HashMap();
			HashMap gene_transcript_max_exon_count = new HashMap();
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
				LinkedList exon_transcript_list = new LinkedList();
				//System.out.println(sig);
				if (gene2transcripts.containsKey(gene)) {
					transcript_list = (LinkedList)gene2transcripts.get(gene);
				}
				if (exon_transcripts.containsKey(exon)) {
					exon_transcript_list = (LinkedList)exon_transcripts.get(gene);
				}
				for (String transcript: transcripts) {
					
					String biotype = "NA";
					if (gtfFile.transcript_clean2biotype.containsKey(transcript.split("\\.")[0])) {
						biotype = (String)gtfFile.transcript_clean2biotype.get(transcript.split("\\.")[0]); 
					}
					if (biotype.equals("protein_coding")) {
						if (sig.equals("Significant")) {
							if (!transcript.equals("NA")) {
								if (transcript_sig_exon_count.containsKey(transcript)) {
									double count_sig = (Double)transcript_sig_exon_count.get(transcript);						
									count_sig++;							
									transcript_sig_exon_count.put(transcript, count_sig);
									//System.out.println(transcript + "\t" + count_sig);
								} else {								
									transcript_sig_exon_count.put(transcript, 1.0);		
									
								}
							}
						}
						
						if (!transcript.equals("NA")) {
							if (transcript_total_exon_count.containsKey(transcript)) {
								double total_count = (Double)transcript_total_exon_count.get(transcript);
								total_count++;
								transcript_total_exon_count.put(transcript, total_count);
								if (gene_transcript_max_exon_count.containsKey(gene)) {
									double prev_count = (Double)gene_transcript_max_exon_count.get(gene);
									if (total_count > prev_count) {
										gene_transcript_max_exon_count.put(gene, total_count);					
									}
								} else {
									gene_transcript_max_exon_count.put(gene, total_count);
								}
							} else {
								transcript_total_exon_count.put(transcript, 1.0);
								gene_transcript_max_exon_count.put(gene, 1.0);
							}
							if (!transcript_list.contains(transcript)) {
								transcript_list.add(transcript);
							}
							if (!exon_transcript_list.contains(transcripts)) {
								exon_transcript_list.add(transcript);
							}
						}
					}
					
				}
				//System.out.println(gene + "\t" + transcript_list.size());
				
				gene2transcripts.put(gene, transcript_list);
				exon_transcripts.put(exon, exon_transcript_list);
				
			}
			in.close();
			
			
			Iterator itr = exon_transcripts.keySet().iterator();
			while (itr.hasNext()) {
				String exon = (String)itr.next();
				String gene = exon.split("_")[0];
				
				if (gene_transcript_max_exon_count.containsKey(gene)) {
					double gene_max_exon_count = (Double)gene_transcript_max_exon_count.get(gene);
					double count_total_transcript_in_gene = 0;
					double count_total_transcript_in_exon = 0;
					LinkedList transcript_list = (LinkedList)gene2transcripts.get(gene);
					LinkedList exon_transcript_list = (LinkedList)exon_transcripts.get(exon);
					Iterator itr_transcripts = transcript_list.iterator();
					while (itr_transcripts.hasNext()) {
						String transcript = (String)itr_transcripts.next();
						double transcript_exon_count = (Double)transcript_total_exon_count.get(transcript);
						if (transcript_exon_count > 2.0 && transcript_exon_count / gene_max_exon_count > 0.4) {
							count_total_transcript_in_gene++;
							if (exon_transcript_list.contains(transcript)) {
								count_total_transcript_in_exon++;
							}
						}
					}
					//System.out.println(exon + "\t" + gene + "\t" + count_total_transcript_in_exon + "\t" + count_total_transcript_in_gene);
					if (count_total_transcript_in_exon == 0) {
						out_exon.write(exon + "\t" + "Missing_in_annotated_transcripts" + "\n");
					} else if (count_total_transcript_in_exon != count_total_transcript_in_gene) {
						out_exon.write(exon + "\t" + "Alt_Transcript" + "\n");
					} else {
						out_exon.write(exon + "\t" + "Canonical" + "\n");
					}
				} else {
					out_exon.write(exon + "\tNot_Evaluated\n");
				}
			}
			out_exon.close();
			System.out.println(count_lines);
			
			itr = gene2transcripts.keySet().iterator();
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
						//System.out.println(gene + "\t" + transcript + "\t" + count_sig + "\t" + count_total);
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
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
