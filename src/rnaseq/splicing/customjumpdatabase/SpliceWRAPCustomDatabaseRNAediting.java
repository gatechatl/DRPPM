package rnaseq.splicing.customjumpdatabase;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SpliceWRAPCustomDatabaseRNAediting {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap candidates = new HashMap();
			HashMap genes = new HashMap();
			String inputCandidates = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseRNAediting/RNAediting_Candidates.txt";
			FileInputStream fstream = new FileInputStream(inputCandidates);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");				
				candidates.put(str, str);
				for (String transcripts: split) {
					String[] transcript_split = transcripts.split(":");
					if (transcript_split.length > 1) {
						//System.out.println(split[1]);
						
						genes.put(transcript_split[1], transcript_split[1]);
					}
				}
			}
			in.close();

			//HashMap geneName2proteinname = new HashMap();
			//HashMap proteinName2geneName = new HashMap();
			HashMap transcript2protein = new HashMap();
			HashMap protein2transcript = new HashMap();
			String conversion_table = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseRNAediting/GRCh38/GRCh38_latest_genomic.gtf";
			fstream = new FileInputStream(conversion_table);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length >= 8) {
					String meta = split[8];
					if (meta.contains("transcript_id") && meta.contains("protein_id")) {
						
						String transcript_id = GTFFile.grabMeta(meta, "transcript_id").split("\\.")[0];
						String protein_id = GTFFile.grabMeta(meta, "protein_id");
						if (genes.containsKey(transcript_id)) {
							protein2transcript.put(protein_id, transcript_id);
							transcript2protein.put(transcript_id, protein_id);
							//System.out.println(transcript_id + "\t" + protein_id);
						}
						/*String geneName = split[4];
						String protein_names = split[2].split("\\.")[0];*/
						/*geneName2proteinname.put(geneName, protein_names);
						for (String names: protein_names.split(",")) {
							proteinName2geneName.put(names, geneName);
						}*/
					}
				}
			}
			in.close();


			String outputFile = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseRNAediting/RNAeditDatabase.fasta";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			

			String outputFile_18mer = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseRNAediting/RNAedit_18mer.fasta";
			FileWriter fwriter_18mer = new FileWriter(outputFile_18mer);
			BufferedWriter out_18mer = new BufferedWriter(fwriter_18mer);
			

			String outputFile_pit_lookup = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseRNAediting/RNAeditDatabase.lookup.txt";
			FileWriter fwriter_pit_lookup = new FileWriter(outputFile_pit_lookup);
			BufferedWriter out_pit_lookup = new BufferedWriter(fwriter_pit_lookup);
			
			String outputFile_pit = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseRNAediting/RNAeditDatabase.pit";
			FileWriter fwriter_pit = new FileWriter(outputFile_pit);
			BufferedWriter out_pit = new BufferedWriter(fwriter_pit);
			
			String outputFile_peptide = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseRNAediting/RNAeditPeptideList.txt";
			FileWriter fwriter_peptide = new FileWriter(outputFile_peptide);
			BufferedWriter out_peptide = new BufferedWriter(fwriter_peptide);
			
			out_pit.write("UniprotAC\tSJPGnumber\tGroupName\tAbundance\tResidueLength\tProteinName\tFullDescription\tAnnotation\n");
			
			boolean found = false;
			String name = "";
			String protein_id = "";
			String seq = "";
			String transcript_id = "";
			HashMap sequence = new HashMap();
			String inputFasta = "/Users/4472414/Projects/CustomProteinDatabase/JUMPdatabaseRNAediting/GRCh38/GRCh38_latest_protein.faa";
			fstream = new FileInputStream(inputFasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str;
					
					if (found) {
						if (genes.containsKey(transcript_id)) {
							//System.out.println(transcript_id);
							sequence.put(transcript_id, seq);
						}
					}
					//System.out.println(proteinName2geneName.get(geneName));
					//System.out.println(geneName);
					
					protein_id = str.split(" ")[0].replaceAll(">", "");
					if (protein2transcript.containsKey(protein_id)) {
						//System.out.println(geneName + "\t" + proteinName2geneName.get(geneName));
						transcript_id = (String)protein2transcript.get(protein_id);
						found = true;
					} else {
						found = false;
					}
					String symbol = "NA";
				
					seq = "";
				
				} else {					
					seq += str;
				
					
					//map.put(name, seq);
				}
			}
			in.close();
			
			if (found) {
				sequence.put(transcript_id, seq);
			
			}
			int count_hits = 0;
			int index = 100001;
			HashMap convert_name = new HashMap();
			
			HashMap list_of_sequences = new HashMap();
			HashMap sequence_output = new HashMap();
			Iterator itr = candidates.keySet().iterator();
			while (itr.hasNext()) {
				String candidate = (String)itr.next();
				String[] split_candidate = candidate.split(",");
				boolean found_hit = false;
				for (String candidate_str: split_candidate) {
					if (candidate_str.split(":").length > 1) {
						transcript_id = candidate_str.split(":")[1];
						String[] split = candidate_str.split(":");
						String loc = split[4];
						System.out.println(loc + "\t" + transcript_id + "\t" + split[1]);
						if (sequence.containsKey(transcript_id)) {
							seq = (String)sequence.get(transcript_id);
							if (loc.contains("p")) {
								int pos = new Integer(loc.replaceAll("p\\.",  "").replaceAll("A",  "").replaceAll("B",  "").replaceAll("C",  "").replaceAll("D",  "").replaceAll("E",  "").replaceAll("F",  "").replaceAll("G",  "").replaceAll("H",  "").replaceAll("I",  "").replaceAll("J",  "").replaceAll("K",  "").replaceAll("L",  "").replaceAll("M",  "").replaceAll("N",  "").replaceAll("O",  "").replaceAll("P",  "").replaceAll("Q",  "").replaceAll("R",  "").replaceAll("S",  "").replaceAll("T",  "").replaceAll("U",  "").replaceAll("V",  "").replaceAll("W",  "").replaceAll("X",  "").replaceAll("Y",  "").replaceAll("Z",  "").replaceAll(",", ""));
								String original_aa = loc.substring(2, 3);
								String replacement_aa = loc.substring(loc.length() - 1, loc.length());
								System.out.println(candidate_str);
								System.out.println(pos + "\t" + original_aa + "\t" + replacement_aa);
								//System.out.println(pos);
								System.out.println(seq);
								if (seq.length() >= pos) {
									String identified_aa = seq.substring(pos - 1, pos);
									if (original_aa.equals(identified_aa)) {
										System.out.println("Hit: " + identified_aa + "\tReplace: " + replacement_aa);							
										found_hit = true;
										String new_seq = seq.substring(0, pos - 1) + replacement_aa + seq.substring(pos, seq.length());
										
										int index_KR_start = 0;
										for (int i = 0; i < pos - 1; i++) {
											if (seq.substring(i, i + 1).equals("K") || seq.substring(i, i + 1).equals("R")) {
												index_KR_start = i;
											}
										}
										int index_KR_end = seq.length() - 1;
										for (int i = pos - 1; i < seq.length(); i++) {
											if (seq.substring(i, i + 1).equals("K") || seq.substring(i, i + 1).equals("R")) {
												index_KR_end = i + 1;
												break;
											}
										}
										
										
										if (!sequence_output.containsKey(seq)) {
											sequence_output.put(seq,  "");

											list_of_sequences.put(">" + transcript_id  + "_" + transcript2protein.get(transcript_id), seq);
											String tag_name = transcript_id  + "_" + transcript2protein.get(transcript_id);
											convert_name.put("SJPG" + index + ".001", tag_name);
											
											
											out.write(">" + "SJPG" + index + ".001" + "\n");																				
											out.write(seq + "\n");
											out_pit.write("SJPG" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "-" + "\t" + seq.length() + "\t" + tag_name + "\t" + tag_name + "\t-\n");
											index++;
											
											out_peptide.write(">ORIG_" + tag_name + "\n");
											out_peptide.write(seq.substring(index_KR_start, index_KR_end) + "\n");
										}
										
										if (!sequence_output.containsKey(new_seq)) {
											sequence_output.put(new_seq,  "");
											//out.write(">" + candidate_str + "_" + transcript2protein.get(transcript_id) + "\n");
											
											
											list_of_sequences.put(">" + candidate_str + "_" + transcript2protein.get(transcript_id), new_seq);
											String tag_name = candidate_str + "_" + transcript2protein.get(transcript_id);
											int max = pos + 8;
											int min = pos - 9;
											if (pos + 10 > new_seq.length()) {
												max = new_seq.length();
											}
											if (min < 0) {
												min = 0;
											}
											out_18mer.write(">" + tag_name + "\n" + new_seq.substring(min, max) + "\n");
											convert_name.put("SJPG" + index + ".001", tag_name);
											out.write(">" + "SJPG" + index + ".001" + "\n");
											out.write(new_seq + "\n");
											out_pit.write("SJPG" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "-" + "\t" + seq.length() + "\t" + tag_name + "\t" + tag_name + "\t-\n");
											index++;
											out_peptide.write(">RNAEDIT_" + tag_name + "\n");
											out_peptide.write(new_seq.substring(index_KR_start, index_KR_end) + "\n");
										}
									}
								}
								
							}
						} else {
							System.out.println(candidate + "\t" + "missing");
						}
					}
				}
				if (found_hit) {
					count_hits++;
				}
			}
			
			
			
			
			itr = list_of_sequences.keySet().iterator();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				String aa_seq = (String)list_of_sequences.get(tag);
		        StringBuilder aa_seq_reverse = new StringBuilder(); 
		        
		        // append a string into StringBuilder input1 
		        aa_seq_reverse.append(aa_seq); 
		  
		        // reverse StringBuilder input1 
		        aa_seq_reverse = aa_seq_reverse.reverse(); 
		        aa_seq = aa_seq_reverse.toString();
		        String new_tag = ">" + "##Decoy__" + tag.replaceAll(">", "");

		        
				String tag_name = new_tag;
				convert_name.put("##Decoy__" + index + ".001", tag_name);
				
		        out.write(">" + "##Decoy__" + index + ".001" + "\n");
		        out.write(aa_seq.toString() + "\n");
				out_pit.write("##Decoy__" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "SJPG" + index + ".001" + "\t" + "-" + "\t" + seq.length() + "\t" + tag_name + "\t" + tag_name + "\t-\n");
				index++;
			}
			out.close();
			out_pit.close();
			System.out.println(count_hits);
			
			itr = convert_name.keySet().iterator();
			while (itr.hasNext()) {
				String sjpg_name = (String)itr.next();
				String tag = (String)convert_name.get(sjpg_name);
				out_pit_lookup.write(sjpg_name + "\t" + tag + "\n");
			}
			out_pit_lookup.close();
			out_peptide.close();
			out_18mer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

