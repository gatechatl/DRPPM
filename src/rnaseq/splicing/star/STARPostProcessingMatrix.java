package rnaseq.splicing.star;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import rnaseq.splicing.rnapeg.EXON;

/**
 * Generate a folder containing the matrix of reference
 * /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/PCGP_RNAseq/processed_from_old_bam/RNApeg/Summarize
 * @author tshaw
 *
 */
public class STARPostProcessingMatrix {

	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Takes in the sj.out.tab and output\n";
	}
	public static String parameter_info() {
		return "[STAR SJ file] [min_reads_for_novel] [gtf_file] [geneName] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String sj_out_tab_file = args[0];
			double min_reads_for_novel = new Double(args[1]); // for novel
			String gtf_file = args[2];
			String geneName = args[3];
			String outputFolder = args[4];
			
			File outputFolder_file = new File(outputFolder);
			if (!outputFolder_file.isDirectory()) {
				outputFolder_file.mkdir();				
			}
			
			/*File outputFolder_gene = new File(outputFolder + "/" + geneName);
			if (!outputFolder_gene.isDirectory()) {
				outputFolder_gene.mkdir();				
			}*/
		
			
			HashMap transcript_id2line = new HashMap();
			String direction = "";
			String temp_chr = "";
			int min = 999999999;
			int max = -1;
			String prev_line = "";
			LinkedList list = new LinkedList();
			HashMap exon_structure = new HashMap();
			FileInputStream fstream = new FileInputStream(gtf_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 8) {
					String chr = split[0];
					
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					String type = split[2];
					if (type.equals("exon")) {
						String position = split[6];
						
						String name = GTFFile.grabMeta(split[8], "gene_name");
						String transcript_id = GTFFile.grabMeta(split[8], "transcript_id");
						if (geneName.equals(name)) {
							temp_chr = chr;
							//System.out.println(geneName);
							direction = position;
							if (min > start) {
								min = start;
							}
							if (min > end) {
								min = end;
							}
							if (max < start) {
								max = start;
							}
							if (max < end) {
								max = end;
							}		
							String line = chr + "\t" + start + "\t" + end + "\t" + position + "\t" + transcript_id;
							//System.out.println("Load: " + line);
							if (!transcript_id2line.containsKey(transcript_id)) {
								transcript_id2line.put(transcript_id,  line);					
							}
							String[] prev_line_split = prev_line.split("\t");
							list.add(line);
	
							if (prev_line_split.length >= 4) {
								if (transcript_id.equals(prev_line_split[4])) {
									
								
									EXON exon = new EXON();
									exon.transcript_id = transcript_id;
									exon.exon_chr = prev_line_split[0];
									exon.exon_start = new Integer(prev_line_split[1]);
									exon.exon_end = new Integer(prev_line_split[2]);
									exon.exon_direction = prev_line_split[3];
									
									exon.next_serial_exon_chr = chr;
									exon.next_serial_exon_start = start;
									exon.next_serial_exon_end = end;
									exon.next_serial_exon_position = position;
								
									//System.out.println("get line: " + exon.get_line());
									//System.out.println("next line: " + exon.get_next_serial_line());
									if (exon_structure.containsKey(transcript_id)) {
										HashMap exon_map = (HashMap)exon_structure.get(transcript_id);
										exon_map.put(prev_line, exon);
										exon_structure.put(transcript_id, exon_map);
									} else {
										HashMap exon_map = new HashMap();								
										exon_map.put(prev_line, exon);									
										exon_structure.put(transcript_id, exon_map);
									}
								}
							}
							prev_line = line;
						} // geneName match
					} // exon
				} // must have at least 8 columns
			}
			in.close();
			int total_good_junctions = 0;
			
			System.out.println(geneName + "\t" + temp_chr + ":" + (min - 1000) + "-" + (max + 1000));
			//System.out.println("exon size: " + exon_structure.size());
			HashMap novel_exon = new HashMap();
			HashMap orphan_exon = new HashMap();			
			HashMap junction_with_hit = new HashMap();
			HashMap missed_known_junction = new HashMap();
			HashMap one_sided_exon = new HashMap();
			HashMap exon = new HashMap();
			HashMap ref_map = new HashMap();
			HashMap ref_novel_map = new HashMap();
			
			// first step is to generate a serial exon structure based on the existing GTF transcript annotation.
			// we test whether it contains novel or known existing junctions.
			
			// details from the sj.out.tab
			// col 1: chromosome
			// col 2: first base of the intron
			// col 3: last base of the intron
			// col 4: strand (0: undefined, 1 for +, 2 for -)
			// col 5 intron motif 0: non-canonical; 1: GT/AG; 2: CT/AC, 3: GC/AG, 4: CT/GC, 5: AT/AC, 6: GT/AT
			// col 6: 0: unannotated, 1: annotated (only if splice junctions database is used)
			// col 7: number of uniquely mapped reads
			// col 8: number of multi-mapped reads
			// col 9: maximum spliced alignment overhang
			HashMap all_junction = new HashMap();
			FileInputStream fstream2 = new FileInputStream(sj_out_tab_file);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str2 = in2.readLine();
				String[] split = str2.split("\t");
				//String name = split[3];
				String chr = split[0];
				int junction1_position = (new Integer(split[1]) - 1);
				int junction2_position = (new Integer(split[2]) + 1);
				String junction1_direction = "+";
				String junction2_direction = "+";
				if (split[3].equals("2")) {
					junction1_direction = "-";
					junction2_direction = "-";
				}
				String junction1 = chr + ":" + junction1_position + ":" + junction1_direction;
				String junction2 = chr + ":" + junction2_position + ":" + junction2_direction;
				String junction1_chr = chr;
				String junction2_chr = chr;
				String annotation = split[5];
				
				String junction = junction1 + "," + junction2;
				if (temp_chr.equals(chr) && ((min - 10000) <= new Integer(junction1_position) && new Integer(junction1_position) <= (max + 10000)) || ((min - 10000) <= new Integer(junction2_position) && new Integer(junction2_position) <= (max + 10000))) {
				//if (geneName.equals(name)) {
				
					
					//String junction1 = junction.split(",")[0];
					//String junction1_chr = junction1.split(":")[0];
					//int junction1_position = new Integer(junction1.split(":")[1]);
					
					//String junction2 = junction.split(",")[1];
					//String junction2_chr = junction2.split(":")[0];
					//int junction2_position = new Integer(junction2.split(":")[1]);
											
					double read = new Double(split[7]);
					boolean found_hit = false;
					
					
					//String type = split[2];
					//if (type.equals("known")) {
					if (annotation.equals("1")) { // if it is part of the SJ db
						
						/*if (junction2_position == 48907153) {
							System.out.println("Found in known section");
							System.out.println(junction);
						}*/
						all_junction.put(junction, "known");
						if (ref_map.containsKey(junction)) {
							int count = (Integer)ref_map.get(junction);
							count = count + 1;
							ref_map.put(junction, count);
						} else {
							ref_map.put(junction, 1.0);
						}							
						
						// assign the exon_junction between exons
						Iterator itr = exon_structure.keySet().iterator();
						while (itr.hasNext()) {
							String transcript_id = (String)itr.next();
							HashMap exon_map = (HashMap)exon_structure.get(transcript_id);
							String line = (String)transcript_id2line.get(transcript_id);
							while (exon_map.containsKey(line)) {
								//System.out.println(line);
								EXON current_exon = (EXON)exon_map.get(line);								
								if (current_exon.exon_chr.equals(junction1_chr)) {
									//System.out.println("chr are matching");
									if (direction.equals("+")) {
										//System.out.println("positive_test: " + current_exon.exon_start + "\t" + current_exon.exon_end + "\t" + current_exon.next_serial_exon_start + "\t" + current_exon.next_serial_exon_end + "\t" + junction1_position + "\t" + junction2_position);
										if ((current_exon.exon_end - 5 < junction1_position && junction1_position < current_exon.exon_end + 5) && (current_exon.next_serial_exon_start - 5 < junction2_position && junction2_position < current_exon.next_serial_exon_start + 5)) {
											found_hit = true;
											
											//System.out.println(junction1 + "\t" + junction2);
											current_exon.exon_junction.put(junction1, junction2);
											exon_map.put(line, current_exon);
											exon_structure.put(transcript_id, exon_map);
										}
									} else {
										/*if (current_exon.exon_start == 39817327 && junction2_position == 39817327 ) {
											System.out.println("current_exon.next_serial_exon_end: " + current_exon.next_serial_exon_end);
											System.out.println("current_exon.exon_start: " + current_exon.exon_start);
											System.out.println("junction1_position, junction2_position: " + junction1_position + "\t" + junction2_position);																							
										}*/
										if ((current_exon.next_serial_exon_end - 5 < junction1_position && junction1_position < current_exon.next_serial_exon_end + 5) && (current_exon.exon_start - 5 < junction2_position && junction2_position < current_exon.exon_start + 5)) {
											//System.out.println(junction1 + "\t" + junction2);
											
											found_hit = true;
											current_exon.exon_junction.put(junction1, junction2);
											exon_map.put(line, current_exon);
											exon_structure.put(transcript_id, exon_map);
										}
									}
								}
								line = current_exon.get_next_serial_line();									
							}								
						}
						
						if (!found_hit) {
							missed_known_junction.put(junction, junction);			
							
							// copied the code from the novel section
							if (read >= min_reads_for_novel) {
								all_junction.put(junction, "novel");
								if (ref_novel_map.containsKey(junction)) {
									int count = (Integer)ref_novel_map.get(junction);
								} else {
									ref_novel_map.put(junction, 1.0);
								}
								
								if (direction.equals("+")) {
									if (min < junction1_position) {
										exon.put(junction1_chr + "\t" + junction1_position + "\t" + (junction1_position - 1), junction1_chr + "\t" + junction1_position + "\t" + (junction1_position - 1));
									}
								} else {
									if (max > junction2_position) {
										exon.put(junction2_chr + "\t" + junction2_position + "\t" + (junction2_position + 1), junction2_chr + "\t" + junction2_position + "\t" + (junction2_position + 1));
									}
								}
																			

								// assign the exon_junction between exons
								itr = exon_structure.keySet().iterator();
								while (itr.hasNext()) {
									String transcript_id = (String)itr.next();
									HashMap exon_map = (HashMap)exon_structure.get(transcript_id);
									String line = (String)transcript_id2line.get(transcript_id);
									while (exon_map.containsKey(line)) {										
										EXON current_exon = (EXON)exon_map.get(line);
										if (current_exon.exon_chr.equals(junction1_chr)) {
											if (direction.equals("+")) {
												if ((current_exon.exon_end - 5 < junction1_position && junction1_position < current_exon.exon_end + 5)) {
													junction_with_hit.put(junction1, current_exon.get_line());													
													current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
													exon_map.put(line, current_exon);
													exon_structure.put(transcript_id, exon_map);
												}
												if ((current_exon.exon_start - 5 < junction2_position && junction2_position < current_exon.exon_start + 5)) {
													junction_with_hit.put(junction2, current_exon.get_next_serial_line());													
													current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
													exon_map.put(line, current_exon);
													exon_structure.put(transcript_id, exon_map);
												}
												if ((current_exon.next_serial_exon_start - 5 < junction2_position && junction2_position < current_exon.next_serial_exon_start + 5)) {
													junction_with_hit.put(junction2, current_exon.get_next_serial_line());
													current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
													exon_map.put(line, current_exon);
													exon_structure.put(transcript_id, exon_map);												
												}
												if ((current_exon.next_serial_exon_end - 5 < junction1_position && junction1_position < current_exon.next_serial_exon_end + 5)) {
													junction_with_hit.put(junction1, current_exon.get_line());	
													current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
													exon_map.put(line, current_exon);
													exon_structure.put(transcript_id, exon_map);												
												}
											} else {
												if ((current_exon.next_serial_exon_start - 5 < junction2_position && junction2_position < current_exon.next_serial_exon_start + 5)) {
													junction_with_hit.put(junction2, current_exon.get_next_serial_line());
													current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
													exon_map.put(line, current_exon);
													exon_structure.put(transcript_id, exon_map);
												}
												if ((current_exon.next_serial_exon_end - 5 < junction1_position && junction1_position < current_exon.next_serial_exon_end + 5)) {
													junction_with_hit.put(junction1, current_exon.get_line());	
													current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
													exon_map.put(line, current_exon);
													exon_structure.put(transcript_id, exon_map);
												}
												if ((current_exon.exon_end - 5 < junction1_position && junction1_position < current_exon.exon_end + 5)) {
													junction_with_hit.put(junction1, current_exon.get_line());
													current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
													exon_map.put(line, current_exon);
													exon_structure.put(transcript_id, exon_map);
												}
												if ((current_exon.exon_start - 5 < junction2_position && junction2_position < current_exon.exon_start + 5)) {
													junction_with_hit.put(junction2, current_exon.get_next_serial_line());
													current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
													exon_map.put(line, current_exon);
													exon_structure.put(transcript_id, exon_map);
												}
											}
										}
										line = current_exon.get_next_serial_line();
									}								
								}
								
							}
						}
						
					} // end if known
					else {
						total_good_junctions++;
						if (junction2_position == 48907153) {
							System.out.println("Found in novel section");
							System.out.println(junction);
						}
						if (read >= min_reads_for_novel) {
							all_junction.put(junction, "novel");
							if (ref_novel_map.containsKey(junction)) {
								int count = (Integer)ref_novel_map.get(junction);
							} else {
								ref_novel_map.put(junction, 1.0);
							}
							
							if (direction.equals("+")) {
								if (min < junction1_position) {
									exon.put(junction1_chr + "\t" + junction1_position + "\t" + (junction1_position - 1), junction1_chr + "\t" + junction1_position + "\t" + (junction1_position - 1));
								}
							} else {
								if (max > junction2_position) {
									exon.put(junction2_chr + "\t" + junction2_position + "\t" + (junction2_position + 1), junction2_chr + "\t" + junction2_position + "\t" + (junction2_position + 1));
								}
							}
																		

							// assign the exon_junction between exons
							Iterator itr = exon_structure.keySet().iterator();
							while (itr.hasNext()) {
								String transcript_id = (String)itr.next();
								HashMap exon_map = (HashMap)exon_structure.get(transcript_id);
								String line = (String)transcript_id2line.get(transcript_id);
								while (exon_map.containsKey(line)) {										
									EXON current_exon = (EXON)exon_map.get(line);
									if (current_exon.exon_chr.equals(junction1_chr)) {
										if (direction.equals("+")) {
											if ((current_exon.exon_end - 5 < junction1_position && junction1_position < current_exon.exon_end + 5)) {
												junction_with_hit.put(junction1, current_exon.get_line());													
												current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
												exon_map.put(line, current_exon);
												exon_structure.put(transcript_id, exon_map);
											}
											if ((current_exon.exon_start - 5 < junction2_position && junction2_position < current_exon.exon_start + 5)) {
												junction_with_hit.put(junction2, current_exon.get_next_serial_line());													
												current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
												exon_map.put(line, current_exon);
												exon_structure.put(transcript_id, exon_map);
											}
											if ((current_exon.next_serial_exon_start - 5 < junction2_position && junction2_position < current_exon.next_serial_exon_start + 5)) {
												junction_with_hit.put(junction2, current_exon.get_next_serial_line());
												current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
												exon_map.put(line, current_exon);
												exon_structure.put(transcript_id, exon_map);												
											}
											if ((current_exon.next_serial_exon_end - 5 < junction1_position && junction1_position < current_exon.next_serial_exon_end + 5)) {
												junction_with_hit.put(junction1, current_exon.get_line());	
												current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
												exon_map.put(line, current_exon);
												exon_structure.put(transcript_id, exon_map);												
											}
										} else {
											if ((current_exon.next_serial_exon_start - 5 < junction2_position && junction2_position < current_exon.next_serial_exon_start + 5)) {
												junction_with_hit.put(junction2, current_exon.get_next_serial_line());
												current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
												exon_map.put(line, current_exon);
												exon_structure.put(transcript_id, exon_map);
											}
											if ((current_exon.next_serial_exon_end - 5 < junction1_position && junction1_position < current_exon.next_serial_exon_end + 5)) {
												junction_with_hit.put(junction1, current_exon.get_line());	
												current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
												exon_map.put(line, current_exon);
												exon_structure.put(transcript_id, exon_map);
											}
											if ((current_exon.exon_end - 5 < junction1_position && junction1_position < current_exon.exon_end + 5)) {
												junction_with_hit.put(junction1, current_exon.get_line());
												current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
												exon_map.put(line, current_exon);
												exon_structure.put(transcript_id, exon_map);
											}
											if ((current_exon.exon_start - 5 < junction2_position && junction2_position < current_exon.exon_start + 5)) {
												junction_with_hit.put(junction2, current_exon.get_next_serial_line());
												current_exon.novel_exon_junction.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
												exon_map.put(line, current_exon);
												exon_structure.put(transcript_id, exon_map);
											}
										}
									}
									line = current_exon.get_next_serial_line();
								}								
							}
							
						}
					} // end else
					
				}
			}
			in2.close();

			Iterator itr = all_junction.keySet().iterator();
			while (itr.hasNext()) {
				String junction = (String)itr.next();
				String type = (String)all_junction.get(junction);
				String junction1 = junction.split(",")[0];
				String junction1_chr = junction1.split(":")[0];
				int junction1_position = new Integer(junction1.split(":")[1]);
				
				String junction2 = junction.split(",")[1];
				String junction2_chr = junction2.split(":")[0];
				int junction2_position = new Integer(junction2.split(":")[1]);
				
				//System.out.println("Junction1: " + junction1_chr + "\t" + junction1_position);
				//System.out.println("Junction2: " + junction2_chr + "\t" + junction2_position);
				
				boolean found_exon = false;
				if (type.equals("novel")) {
					//System.out.println("NoveL");
					boolean one_side = false;
					
					Iterator itr3 = list.iterator();
					while (itr3.hasNext()) {
						String line = (String)itr3.next();
						String[] split_line = line.split("\t");
						int start = new Integer(split_line[1]);
						int end = new Integer(split_line[2]);
						String transcript_id = split_line[4];
						
						HashMap exon_map = (HashMap)exon_structure.get(transcript_id);
						
						if (exon_map.containsKey(line)) {
							EXON current_exon = (EXON)exon_map.get(line);
							//System.out.println("line: " + line);
							//String line = chr + "\t" + start + "\t" + end + "\t" + direction + "\t" + transcript_id;
							String next_exon_line = current_exon.get_next_serial_line();
							if (exon_map.containsKey(next_exon_line)) {
								EXON next_exon = (EXON)exon_map.get(next_exon_line);
								
								//list.add(chr + "\t" + start + "\t" + end + "\t" + position + "\n");
								if (direction.equals("+")) {
									if (end - 5 < junction1_position && junction1_position < end + 5) {
										
										// grab the next exon
										Iterator itr2 = next_exon.novel_exon_junction.keySet().iterator();
										while (itr2.hasNext()) {
											String novel_junction = (String)itr2.next();
											String novel_junction1 = novel_junction.split("\t")[0];
											String novel_junction2 = novel_junction.split("\t")[1];
											//System.out.println(novel_junction1);
											//System.out.println(novel_junction2);
											String novel_junction1_chr = novel_junction1.split(":")[0];
											int novel_junction1_position = new Integer(novel_junction1.split(":")[1]);
																					
											String novel_junction2_chr = novel_junction2.split(":")[0];
											int novel_junction2_position = new Integer(novel_junction2.split(":")[1]);
											if (junction2_position < novel_junction1_position) {
												novel_exon.put(junction2 + "\t" + novel_junction1, junction2 + "\t" + novel_junction1);
												found_exon = true;
											}
										}
										
										one_side = true;
									}
								} else {
									if (start - 5 < junction2_position && junction2_position < start + 5) {
										
										Iterator itr2 = next_exon.novel_exon_junction.keySet().iterator();
										while (itr2.hasNext()) {
											String novel_junction = (String)itr2.next();
											String novel_junction1 = novel_junction.split("\t")[0];
											String novel_junction2 = novel_junction.split("\t")[1];
											
											String novel_junction1_chr = novel_junction1.split(":")[0];
											int novel_junction1_position = new Integer(novel_junction1.split(":")[1]);
																					
											String novel_junction2_chr = novel_junction2.split(":")[0];
											int novel_junction2_position = new Integer(novel_junction2.split(":")[1]);
											if (novel_junction2_position < junction1_position) {
												novel_exon.put(novel_junction2 + "\t" + junction1, novel_junction2 + "\t" + junction1);	
												found_exon = true;
											}
										}
										
										one_side = true;
									}
								}
							}
						}
					} // end while
					
					if (!one_side) {
						orphan_exon.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
					}
					
					if (!found_exon) {
						one_sided_exon.put(junction1 + "\t" + junction2, junction1 + "\t" + junction2);
						/*if (junction1.contains("39764564")) {
							System.out.println("Junction1: " + junction1);
							
						}
						if (junction2.contains("39764564")) {
							System.out.println("Junction2: " + junction2);					
						}*/
					}
					/*Iterator itr2 = all_junction.keySet().iterator();
					while (itr2.hasNext()) {
						String junction_str = (String)itr2.next();
						String type2 = (String)all_junction.get(junction_str);
						if (type2.equals("novel")) {
															
						}
					}*/
				}
				
				
				
			}
			in.close();
			
			System.out.println("total_good_junctions: " + total_good_junctions);
			
			FileWriter fwriter = new FileWriter(outputFolder + "/ExonList.txt");
			BufferedWriter out = new BufferedWriter(fwriter);			

			HashMap prevent_duplicate = new HashMap();
			itr = exon_structure.keySet().iterator();
			while (itr.hasNext()) {
				String transcript_id = (String)itr.next();
				//System.out.println(transcript_id);
				HashMap exon_map = (HashMap)exon_structure.get(transcript_id);
				//System.out.println("exon_map.size(): " + exon_map.size());
				String line = (String)transcript_id2line.get(transcript_id);
				
				while (exon_map.containsKey(line)) {
					//System.out.println(line);
					EXON current_exon = (EXON)exon_map.get(line);
					String current_line = current_exon.get_line();
					if (!prevent_duplicate.containsKey(current_line)) {
						out.write(current_line + "\tCanonical\n");
						prevent_duplicate.put(current_line, current_line);
					}
					//String next_exon_line = current_exon.next_serial_exon_chr + "\t" + current_exon.next_serial_exon_start + "\t" + direction + "\t" + current_exon.transcript_id;
					String next_exon_line = current_exon.get_next_serial_line();
					EXON next_exon = (EXON)exon_map.get(next_exon_line);
					String next_line = current_exon.get_next_serial_line();
					if (!prevent_duplicate.containsKey(next_line)) {
						out.write(next_line + "\tCanonical\n");
						prevent_duplicate.put(next_line, next_line);
					}
					line = current_exon.get_next_serial_line();
				}								
			}
						
			Iterator itr4 = novel_exon.keySet().iterator();
			while (itr4.hasNext()) {
				String junction12 = (String)itr4.next();
				String junction1 = junction12.split("\t")[0];
				String junction2 = junction12.split("\t")[1];
				String junction1_chr = junction1.split(":")[0];
				int junction1_position = new Integer(junction1.split(":")[1]);				
				String junction2_chr = junction2.split(":")[0];
				int junction2_position = new Integer(junction2.split(":")[1]);
				
				out.write(junction1_chr + "\t" + junction1_position + "\t" + junction2_position + "\t" + direction + "\tNovel_Exon\tPutative\n");
			}
			
			System.out.println("junction_with_hit: " + junction_with_hit.size());
			itr4 = one_sided_exon.keySet().iterator();
			while (itr4.hasNext()) {
				String junction12 = (String)itr4.next();
				String junction1 = junction12.split("\t")[0];
				String junction2 = junction12.split("\t")[1];
				String junction1_chr = junction1.split(":")[0];
				
				int junction1_position = new Integer(junction1.split(":")[1]);
				
				String junction2_chr = junction2.split(":")[0];
				int junction2_position = new Integer(junction2.split(":")[1]);
				if (!junction_with_hit.containsKey(junction1)) {
					out.write(junction1_chr + "\t" + (junction1_position) + "\t" + junction1_position + "\t" + direction + "\tPutative_Alt_Start_End_Exon\tleft_junction\n");
				}
				if (!junction_with_hit.containsKey(junction2)) {
					out.write(junction2_chr + "\t" + (junction2_position) + "\t" + (junction2_position) + "\t" + direction + "\tPutative_Alt_Start_End_Exon\tright_junction\n");
				}
			}
			
			itr4 = orphan_exon.keySet().iterator();
			while (itr4.hasNext()) {
				String junction12 = (String)itr4.next();
				String junction1 = junction12.split("\t")[0];
				String junction2 = junction12.split("\t")[1];
				
				String junction1_chr = junction1.split(":")[0];
				int junction1_position = new Integer(junction1.split(":")[1]);
				
				String junction2_chr = junction2.split(":")[0];
				int junction2_position = new Integer(junction2.split(":")[1]);
				out.write(junction1_chr + "\t" + (junction1_position) + "\t" + junction1_position + "\t" + direction + "\tNovel_Orphan_Exon\tleft_junction\n");
				out.write(junction2_chr + "\t" + (junction2_position) + "\t" + (junction2_position) + "\t" + direction + "\tNovel_Orphan_Exon\tright_junction\n");
			}
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
