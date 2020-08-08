package stjude.projects.potter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class PublicPotterIdentifyExonBeingSkippedThroughCufflinks {

	
	public static void main(String[] args) {
		
		
		try {
			
			HashMap skipped = new HashMap();
			//HashMap five_prime_pos = new HashMap();
			//HashMap three_prime_pos = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_SSA_public\\junction\\SSA_vs_CTRL_diff_novel_junctions_up_classified.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));									
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String skipped_chr = split[0].split(":")[0];	
				String start_pos = split[0].split(":")[1];
				String end_pos = split[0].split(":")[3];
				if (skipped.containsKey(skipped_chr)) {
					HashMap skipped_junction = (HashMap)skipped.get(skipped_chr);
					skipped_junction.put(str, str);
					skipped.put(skipped_chr, skipped_junction);					
				} else {
					HashMap skipped_junction = new HashMap();
					skipped_junction.put(str, str);
					skipped.put(skipped_chr, skipped_junction);
				}
			}
			in.close();						

			
			String[] files = {"SRR2159403", "SRR2159404", "SRR2159405", "SRR2159406", "SRR2159407", "SRR2159408"};
			for (String file: files) {
				
				HashMap keep_track_start_hits = new HashMap();
				HashMap keep_track_end_hits = new HashMap();
				HashMap keep_track_hit_on_both = new HashMap();
				HashMap exons = new HashMap();
				//inputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_Rh18\\Cufflinks2\\" + file + "\\transcripts.gtf";
				inputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_SSA_public\\cufflinks2\\" + file + "\\transcripts.gtf";
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));									
				while (in.ready()) {
					String str = in.readLine();
					//System.out.println(str);;
					String[] split = str.split("\t");
					String meta = split[8];
					String transcript_id = meta.split(";")[1].replaceAll("transcript_id", "");
					transcript_id = transcript_id.trim();
					transcript_id = transcript_id.replaceAll("\"", "");
					if (split[2].equals("exon")) {
						String exon_chr = split[0]; 
						int exon_start = new Integer(split[3]);
						int exon_end = new Integer(split[4]);
						exons.put(exon_chr + ":" + exon_start + ":" + exon_end + ":" + transcript_id, exon_chr + ":" + exon_start + ":" + exon_end + ":" + transcript_id);
						
						// iterate through skipped junction and look for matching start and end exons
						Iterator itr = skipped.keySet().iterator();
						while (itr.hasNext()) {
							String chr = (String)itr.next();
							if (exon_chr.equals(chr)) {
								HashMap junction = (HashMap)skipped.get(chr);
								// iterate through the junctions
								Iterator itr2 = junction.keySet().iterator();
								while (itr2.hasNext()) {
									String junction_str = (String)itr2.next();
									String[] split_junction_str = junction_str.split("\t");
									String skipped_chr = split_junction_str[0].split(":")[0];	
									int start_pos = new Integer(split_junction_str[0].split(":")[1]);
									int end_pos = new Integer(split_junction_str[0].split(":")[3]);
									if (exon_end == start_pos) {
										if (keep_track_start_hits.containsKey(split_junction_str[0])) {
											HashMap hit = (HashMap)keep_track_start_hits.get(split_junction_str[0]);
											if (!hit.containsKey(transcript_id)) {
												hit.put(transcript_id,  transcript_id);
												keep_track_start_hits.put(split_junction_str[0], hit);
											}
										} else {
											HashMap hits = new HashMap();
											hits.put(transcript_id, transcript_id);
											keep_track_start_hits.put(split_junction_str[0], hits);
										}
										
										// track hits on both
										if (keep_track_end_hits.containsKey(split_junction_str[0])) {
											HashMap hit = (HashMap)keep_track_end_hits.get(split_junction_str[0]);
											if (hit.containsKey(transcript_id)) {
												if (keep_track_hit_on_both.containsKey(transcript_id)) {
													HashMap both_hit_line = (HashMap)keep_track_hit_on_both.get(transcript_id);
													both_hit_line.put(split_junction_str[0], split_junction_str[0]);
													keep_track_hit_on_both.put(transcript_id, both_hit_line);
												} else {
													HashMap both_hit_line = new HashMap();
													both_hit_line.put(split_junction_str[0], split_junction_str[0]);
													keep_track_hit_on_both.put(transcript_id, both_hit_line);
												}
											}
										}
									}
									if (exon_start == end_pos) {
										if (keep_track_end_hits.containsKey(split_junction_str[0])) {
											HashMap hit = (HashMap)keep_track_end_hits.get(split_junction_str[0]);
											if (!hit.containsKey(transcript_id)) {
												hit.put(transcript_id,  transcript_id);
												keep_track_end_hits.put(split_junction_str[0], hit);
											}
										} else {
											HashMap hits = new HashMap();
											hits.put(transcript_id, transcript_id);
											keep_track_end_hits.put(split_junction_str[0], hits);
										}
										
										// track hits on both
										if (keep_track_start_hits.containsKey(split_junction_str[0])) {
											HashMap hit = (HashMap)keep_track_start_hits.get(split_junction_str[0]);
											if (hit.containsKey(transcript_id)) {
												if (keep_track_hit_on_both.containsKey(transcript_id)) {
													HashMap both_hit_line = (HashMap)keep_track_hit_on_both.get(transcript_id);
													both_hit_line.put(split_junction_str[0], split_junction_str[0]);
													keep_track_hit_on_both.put(transcript_id, both_hit_line);
												} else {
													HashMap both_hit_line = new HashMap();
													both_hit_line.put(split_junction_str[0], split_junction_str[0]);
													keep_track_hit_on_both.put(transcript_id, both_hit_line);
												}
											}
										}
									}
								}
							}
						}
					}
				}
				in.close();
				
				HashMap compilation_exon_between_junction = new HashMap();
				
				int count = 0;
				
				Iterator itr_exons = exons.keySet().iterator();
				while (itr_exons.hasNext()) {
					String pos = (String)itr_exons.next();
					String[] split = pos.split(":");
					String exon_chr = split[0]; 
					int exon_start = new Integer(split[1]);
					int exon_end = new Integer(split[2]);
					String transcript_id = split[3];
					if (keep_track_hit_on_both.containsKey(transcript_id)) {
						HashMap both_hit_line_map = (HashMap)keep_track_hit_on_both.get(transcript_id);
						String exon_str = exon_chr + "," + exon_start + "," + exon_end + "," + transcript_id;
						Iterator itr = both_hit_line_map.keySet().iterator();
						while (itr.hasNext()) {
							String line = (String)itr.next();								
							String[] split_line = line.split("\t");
							String skipped_chr = split_line[0].split(":")[0];
							int skipped_exon1_end = new Integer(split_line[0].split(":")[1]);
							int skipped_exon2_start = new Integer(split_line[0].split(":")[3]);
							if (skipped_exon1_end < exon_start && exon_end < skipped_exon2_start) {
								if (compilation_exon_between_junction.containsKey(line)) {
									HashMap entry = (HashMap)compilation_exon_between_junction.get(line);
									entry.put(exon_str, exon_str);
									compilation_exon_between_junction.put(line, entry);
								} else {
									HashMap entry = new HashMap();
									entry.put(exon_str, exon_str);
									compilation_exon_between_junction.put(line, entry);
								}
							}
						}
						
						
						
						
						/*
						if (skipped.containsKey(exon_chr)) {
							HashMap skipped_exon = (HashMap)skipped.get(exon_chr);
							Iterator itr = skipped_exon.keySet().iterator();
							while (itr.hasNext()) {
								String line = (String)itr.next();								
								String[] split_line = line.split("\t");
								String skipped_chr = split_line[0].split(":")[0];
								int skipped_exon1_end = new Integer(split_line[0].split(":")[1]);
								int skipped_exon2_start = new Integer(split_line[0].split(":")[3]);
								if (skipped_exon1_end < exon_start && exon_end < skipped_exon2_start) {
									if (compilation_exon_between_junction.containsKey(line)) {
										HashMap entry = (HashMap)compilation_exon_between_junction.get(line);
										entry.put(exon_str, exon_str);
										compilation_exon_between_junction.put(line, entry);
									} else {
										HashMap entry = new HashMap();
										entry.put(exon_str, exon_str);
										compilation_exon_between_junction.put(line, entry);
									}
								}
							}				
						}*/
					}
				
				}
					
	
				//String outputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_Rh18\\Cufflinks2\\" + file + "_output_result.txt";
				String outputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_SSA_public\\cufflinks2\\" + file + "_output_result.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);			
								
				System.out.println(compilation_exon_between_junction.size());
				Iterator itr = compilation_exon_between_junction.keySet().iterator();
				while (itr.hasNext()) {
					String line = (String)itr.next(); // junction information
					out.write(">" + line + "\n");
					HashMap total_length = new HashMap();
					
					HashMap exon_entry = (HashMap)compilation_exon_between_junction.get(line);
					Iterator itr_exon = exon_entry.keySet().iterator();
					while (itr_exon.hasNext()) {
						String exon_str = (String)itr_exon.next();
						int start = new Integer(exon_str.split(",")[1]);
						int end = new Integer(exon_str.split(",")[2]);
						String transcript_id = exon_str.split(",")[3];
						int dist = end - start + 1;
						boolean divisible_3 = false;
						if (dist % 3 == 0) {
							divisible_3 = true;
						}
						if (total_length.containsKey(transcript_id)) {
							int new_dist = (Integer)total_length.get(transcript_id);
							new_dist += dist;
							total_length.put(transcript_id, new_dist);
						} else {
							total_length.put(transcript_id, dist);
						}
						//total_length += dist;
						out.write(transcript_id + "\t" + exon_str + "\t" + (end - start + 1) + "\t" + divisible_3 + "\n");
					}
					Iterator itr2 = total_length.keySet().iterator();
					while (itr2.hasNext()) {
						String transcript_id = (String)itr2.next();
						int length = (Integer)total_length.get(transcript_id);
						boolean total_length_divisible_3 = false;
						if (length % 3 == 0) {
							total_length_divisible_3 = true;
						}
						out.write("TotalLength\t" + transcript_id + "\t" + length + "\t" + total_length_divisible_3 + "\n");
					}
					
					out.write("\n");
				}
				out.close();
				
				//String outputFile2 = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_Rh18\\Cufflinks2\\" + file + "_tally_of_junctions.txt";				
				String outputFile2 = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_SSA_public\\cufflinks2\\" + file + "_tally_of_junctions.txt";
				FileWriter fwriter2 = new FileWriter(outputFile2);
				BufferedWriter out2 = new BufferedWriter(fwriter2);
				out2.write("Junction\tStartPoint\tStartHitListOfTranscript\tEndPoint\tEndHitListOfTranscript\n");
				inputFile = "\\\\gsc.stjude.org\\project_space\\pottegrp\\SF3B1\\common\\CMPB\\BioinfoCore\\Hela_SSA_public\\junction\\SSA_vs_CTRL_diff_novel_junctions_up_classified.txt";
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));									
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					out2.write(split[0]);
					if (keep_track_start_hits.containsKey(split[0])) {
						HashMap hit = (HashMap)keep_track_start_hits.get(split[0]);
						String transcript = "";
						itr = hit.keySet().iterator();
						while (itr.hasNext()) {
							String transcript_id = (String)itr.next();
							transcript += transcript_id + ",";
						}
						out2.write("\ttrue\t" + transcript);
					} else {
						out2.write("\tfalse\tNA");
					}
					if (keep_track_end_hits.containsKey(split[0])) {
						HashMap hit = (HashMap)keep_track_end_hits.get(split[0]);
						String transcript = "";
						itr = hit.keySet().iterator();
						while (itr.hasNext()) {
							String transcript_id = (String)itr.next();
							transcript += transcript_id + ",";
						}
						out2.write("\ttrue\t" + transcript);
					} else {
						out2.write("\tfalse\tNA");
					}
					out2.write("\n");
				}
				in.close();
				out2.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
