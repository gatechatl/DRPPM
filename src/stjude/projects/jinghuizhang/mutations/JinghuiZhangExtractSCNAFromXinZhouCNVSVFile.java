package stjude.projects.jinghuizhang.mutations;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Previously Xin Zhou gave me the SNV file 
 * Z:\ResearchHome\ProjectSpace\zhanggrp\AltSpliceAtlas\common\analysis\PCGP_References\pediatric.hg19.vcf
 * @author tshaw
 *
 */
public class JinghuiZhangExtractSCNAFromXinZhouCNVSVFile {

	public static String description() {
		return "Extract mutations from Xin Zhou's SNV file";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[Xin's snv sv file] [length_cutoff] [log2FC_cutoff] [outputRaw] [outputResult] [outputFileSEG] [outputFileMarker] [outputTotalCountTable] [outputTotalCountTableCutoff] [outputComprehensiveTable]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String[] arms_pq = {"chr1:1:125000000", "chr10:1:40200000", "chr11:1:53700000", "chr12:1:35800000", "chr13:1:17900000", "chr14:1:17600000", "chr15:1:19000000", "chr16:1:36600000", "chr17:1:24000000", "chr18:1:17200000", "chr19:1:26500000", "chr2:1:93300000", "chr20:1:27500000", "chr21:1:13200000", "chr22:1:14700000", "chr3:1:91000000", "chr4:1:50400000", "chr5:1:48400000", "chr6:1:61000000", "chr7:1:59900000", "chr8:1:45600000", "chr9:1:49000000", "chrX:1:60600000", "chrY:1:12500000", "chr1:125000001:249250621", "chr10:40200001:135534747", "chr11:53700001:135006516", "chr12:35800001:133851895", "chr13:17900001:115169878", "chr14:17600001:107349540", "chr15:19000001:102531392", "chr16:36600001:90354753", "chr17:24000001:81195210", "chr18:17200001:78077248", "chr19:26500001:59128983", "chr2:93300001:243199373", "chr20:27500001:63025520", "chr21:13200001:48129895", "chr22:14700001:51304566", "chr3:91000001:198022430", "chr4:50400001:191154276", "chr5:48400001:180915260", "chr6:61000001:171115067", "chr7:59900001:159138663", "chr8:45600001:146364022", "chr9:49000001:141213431", "chrX:60600001:155270560", "chrY:12500001:59373566"};	
			String[] chr_len = {"chr1:249250621","chr10:135534747","chr11:135006516","chr12:133851895","chr13:115169878","chr14:107349540","chr15:102531392","chr16:90354753","chr17:81195210","chr18:78077248","chr19:59128983","chr2:243199373","chr20:63025520","chr21:48129895","chr22:51304566","chr3:198022430","chr4:191154276","chr5:180915260","chr6:171115067","chr7:159138663","chr8:146364022","chr9:141213431","chrX:155270560","chrY:59373566"};
			
			boolean start_reading = false;
			String[] sample_info = {};
			double length_cutoff = new Double(args[1]);
			double log2FC_cutoff = new Double(args[2]);
			String outputFile = args[4]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String outputFileSEG = args[5]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample.txt";						
			
			FileWriter fwriterGeneSEG = new FileWriter(outputFileSEG);
			BufferedWriter outGeneSEG = new BufferedWriter(fwriterGeneSEG);
			outGeneSEG.write("SampleName\tChromosome\tStart_Position\tEnd_Position\tNum_Markers\tSeg.CN\n");
			
			String outputFileMarker = args[6];
			FileWriter fwriterGeneMK = new FileWriter(outputFileMarker);
			BufferedWriter outGeneMK = new BufferedWriter(fwriterGeneMK);
			outGeneMK.write("SampleName\tChromosome\tPosition\n");
			
			HashMap comprehensive_sample_name = new HashMap();
			// do these later
			HashMap disease_type_arm_gain = new HashMap();
			HashMap disease_type_arm_loss = new HashMap();
			
			HashMap disease_type_arm_loh_gain = new HashMap();
			HashMap disease_type_arm_loh_loss = new HashMap();
			
			HashMap total_count = new HashMap();
			HashMap total_count_cutoff = new HashMap();
						
			HashMap focal_event_cutoff = new HashMap();
			HashMap arm_level_count_cutoff = new HashMap();
			HashMap chr_level_count_cutoff = new HashMap();
			HashMap less_than_1million_cutoff = new HashMap();
			
			HashMap total_count_loh = new HashMap();
			HashMap total_count_cutoff_loh = new HashMap();
						
			HashMap focal_event_cutoff_loh = new HashMap();
			HashMap arm_level_count_cutoff_loh = new HashMap();
			HashMap chr_level_count_cutoff_loh = new HashMap();
			HashMap less_than_1million_cutoff_loh = new HashMap();
			
			// contains the table with the comprehensive SV event. Prioritize WGS then SNPs then Exon. If have WGS then don't use SNP and Exon
			String outputTotalCountTable = args[7];
			FileWriter fwriterCountTable = new FileWriter(outputTotalCountTable);
			BufferedWriter outCountTable = new BufferedWriter(fwriterCountTable);
			outCountTable.write("SampleName\tSeqType\tCount\n");
			
			String outputTotalCountTableCutoff = args[8];
			FileWriter fwriterCountTableCutoff = new FileWriter(outputTotalCountTableCutoff);
			BufferedWriter outCountTableCutoff = new BufferedWriter(fwriterCountTableCutoff);
			outCountTableCutoff.write("SampleName\tSeqType\tCount\n");
			
			String outputComprehensiveTable = args[9];
			FileWriter fwriterComprehensiveTable = new FileWriter(outputComprehensiveTable);
			BufferedWriter outComprehensiveTable = new BufferedWriter(fwriterComprehensiveTable);
			//outCountTableCutoff.write("SampleName\tSeqType\tCount\n");
			
			String outputFileGeneRaw = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_Sample_Raw.txt";
			FileWriter fwriterGeneRaw = new FileWriter(outputFileGeneRaw);
			BufferedWriter outGeneRaw = new BufferedWriter(fwriterGeneRaw);
			
			HashMap map = new HashMap();
			HashMap uniq_marker = new HashMap();
			HashMap seq_type_map = new HashMap();
			int marker_id = 1;
			String input_cnvsv_file = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\pediatric.hg19.vcf";
			FileInputStream fstream = new FileInputStream(input_cnvsv_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine(); // header
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[0];
				int start = new Integer(split[1]);
				int end = new Integer(split[2]);
						
				String text = split[3].replaceAll(" ", "").replaceAll("\"", "").replaceAll("\\{", "").replaceAll("\\}", "");
				String sampleName = "NA";
				String type = "NA";
				String vorigin = "NA";
				String pmid = "NA";
				String dna_assay = "NA";
				String project = "NA";
				String value = "NA";
				String segmean = "NA";
				for (String split_text: text.split(",")) {					
					if (split_text.contains("sample:")) {
						sampleName = split_text.replaceAll("sample:", "").trim();
					}
					if (split_text.contains("dt:")) {
						type = split_text.replaceAll("dt:", "").trim();						
					}

					if (split_text.contains("vorigin:")) {
						vorigin = split_text.replaceAll("vorigin:", "").trim();						
					}
					if (split_text.contains("dna_assay:")) {
						dna_assay = split_text.replaceAll("dna_assay:", "").trim();						
					}
					if (split_text.contains("value:")) {
						value = split_text.replaceAll("value:", "").trim();						
					}
					if (split_text.contains("segmean:")) {
						segmean = split_text.replaceAll("segmean:", "").trim();						
					}
				}
				// only copy number gain loss
				if (type.equals("4")) {
					seq_type_map.put(dna_assay, dna_assay);
					
					
					outGeneRaw.write(str + "\n");
					if ((!chr.contains("X") && !chr.contains("Y")) && (end - start) >= length_cutoff && ((new Double(value) > log2FC_cutoff || new Double(value) < -log2FC_cutoff))) {
						out.write(sampleName + "\t" + chr + "\t" + start + "\t" + end + "\t" + (end - start) + "\t" + value + "\t" + dna_assay + "\t" + vorigin + "\t" + pmid + "\n");												
					}
					
					// satisfy the fold change for gain
					if (Math.abs(new Double(value)) > log2FC_cutoff) {
						comprehensive_sample_name.put(sampleName, sampleName);
						
						if (total_count.containsKey(sampleName)) {
							HashMap seq_type = (HashMap)total_count.get(sampleName);
							if (seq_type.containsKey(dna_assay)) {
								int count = (Integer)seq_type.get(dna_assay);
								count++;
								seq_type.put(dna_assay, count);
								total_count.put(sampleName, seq_type);
							} else {
								seq_type.put(dna_assay, 1);
								total_count.put(sampleName, seq_type);
							}
						} else {
							HashMap seq_type = new HashMap();
							seq_type.put(dna_assay, 1);
							total_count.put(sampleName, seq_type);
						}
						// testing the arms
						boolean hit_arm = false;
						for (String arms_coord: arms_pq) {
							String[] arms_coord_split = arms_coord.split(":");
							String arm_chr = arms_coord_split[0];
							int arm_start = new Integer(arms_coord_split[1]);
							int arm_end = new Integer(arms_coord_split[2]);
							// if the same chr
							
						
							if (chr.equals(arm_chr)) {								
								if (MathTools.embedded(arm_start, arm_end, start, end)) {
									
									// if it is basically arm level change
									if (new Double(end - start) / new Double(arm_end - arm_start) > 0.8) {
										if (arm_level_count_cutoff.containsKey(sampleName)) {
											HashMap seq_type = (HashMap)arm_level_count_cutoff.get(sampleName);
											if (seq_type.containsKey(dna_assay)) {
												int count = (Integer)seq_type.get(dna_assay);
												count++;
												seq_type.put(dna_assay, count);
												arm_level_count_cutoff.put(sampleName, seq_type);
											} else {
												seq_type.put(dna_assay, 1);
												arm_level_count_cutoff.put(sampleName, seq_type);
											}
										} else {
											HashMap seq_type = new HashMap();
											seq_type.put(dna_assay, 1);
											arm_level_count_cutoff.put(sampleName, seq_type);
										}
										hit_arm = true;
									} else {
										
									}
									
								} // if embedded inside
								
								
							}
						} // for loop iteration of the arms
						if (!hit_arm) {
							if (focal_event_cutoff.containsKey(sampleName)) {
								HashMap seq_type = (HashMap)focal_event_cutoff.get(sampleName);
								if (seq_type.containsKey(dna_assay)) {
									int count = (Integer)seq_type.get(dna_assay);
									count++;
									seq_type.put(dna_assay, count);
									focal_event_cutoff.put(sampleName, seq_type);
								} else {
									seq_type.put(dna_assay, 1);
									focal_event_cutoff.put(sampleName, seq_type);
								}
							} else {
								HashMap seq_type = new HashMap();
								seq_type.put(dna_assay, 1);
								focal_event_cutoff.put(sampleName, seq_type);
							}
						}
						// smaller than 1 million
						if (new Double(end - start) < 1000000) {
							if (less_than_1million_cutoff.containsKey(sampleName)) {
								HashMap seq_type = (HashMap)less_than_1million_cutoff.get(sampleName);
								if (seq_type.containsKey(dna_assay)) {
									int count = (Integer)seq_type.get(dna_assay);
									count++;
									seq_type.put(dna_assay, count);
									less_than_1million_cutoff.put(sampleName, seq_type);
								} else {
									seq_type.put(dna_assay, 1);
									less_than_1million_cutoff.put(sampleName, seq_type);
								}
							} else {
								HashMap seq_type = new HashMap();
								seq_type.put(dna_assay, 1);
								less_than_1million_cutoff.put(sampleName, seq_type);
							}
						}
						// testing the chr
						for (String chr_length: chr_len) {
							String[] chr_split = chr_length.split(":");
							String test_chr = chr_split[0];
							int test_chr_start = 1;
							int test_chr_end = new Integer(chr_split[1]);
							// if the same chr
							if (chr.equals(test_chr)) {								
								if (new Double(end - start) / new Double(test_chr_end - test_chr_start) > 0.8) {																		
									if (chr_level_count_cutoff.containsKey(sampleName)) {
										HashMap seq_type = (HashMap)chr_level_count_cutoff.get(sampleName);
										if (seq_type.containsKey(dna_assay)) {
											int count = (Integer)seq_type.get(dna_assay);
											count++;
											seq_type.put(dna_assay, count);
											chr_level_count_cutoff.put(sampleName, seq_type);
										} else {
											seq_type.put(dna_assay, 1);
											chr_level_count_cutoff.put(sampleName, seq_type);
										}
									} else {
										HashMap seq_type = new HashMap();
										seq_type.put(dna_assay, 1);
										chr_level_count_cutoff.put(sampleName, seq_type);
									}
								}
							}
							
							
						} // end for each chr
						
					} // end if log passes cutoff
					
					// length cutoff
					if ((end - start) >= length_cutoff && Math.abs(new Double(value)) > log2FC_cutoff) {
						
						if (total_count_cutoff.containsKey(sampleName)) {
							HashMap seq_type = (HashMap)total_count_cutoff.get(sampleName);
							if (seq_type.containsKey(dna_assay)) {
								int count = (Integer)seq_type.get(dna_assay);
								count++;
								seq_type.put(dna_assay, count);
								total_count_cutoff.put(sampleName, seq_type);
							} else {
								seq_type.put(dna_assay, 1);
								total_count_cutoff.put(sampleName, seq_type);
							}
						} else {
							HashMap seq_type = new HashMap();
							seq_type.put(dna_assay, 1);
							total_count_cutoff.put(sampleName, seq_type);
						}
						boolean write = true;
						if (map.containsKey(sampleName)) {
							LinkedList list = (LinkedList)map.get(sampleName);
							Iterator itr = list.iterator();
							while (itr.hasNext()) {
								String prevCNV = (String)itr.next();
								String[] split_cnv = prevCNV.split("\t");
								String prev_chr = split_cnv[0];
								int prev_start = new Integer(split_cnv[1]);
								int prev_end = new Integer(split_cnv[2]);
								if (prev_chr.equals(chr)) {
									if (MathTools.overlap(prev_start, prev_end, start, end)) {
										write = false;
									}
								}
							}
							if (write) {
								list.add(chr + "\t" + start + "\t" + end);
								map.put(sampleName, list);
							}
						} else {
							write = true;
							LinkedList list = new LinkedList();
							list.add(chr + "\t" + start + "\t" + end);
							map.put(sampleName, list);
						}
						
						if (write) {
							if (!uniq_marker.containsKey(chr + "\t" + (start + 1))) {
								uniq_marker.put(chr + "\t" + (start + 1), chr + "\t" + (start + 1));
							}
							if (!uniq_marker.containsKey(chr + "\t" + (end + 1))) {
								uniq_marker.put(chr + "\t" + (end + 1), chr + "\t" + (end + 1));
							}
							//outGeneMK.write(sampleName + "\t" + chr + "\t" + (start + 1) + "\n");
							
							outGeneSEG.write(sampleName + "\t" + chr + "\t" + (start + 1) + "\t" + (end + 1) + "\t" + (end - start) + "\t" + value + "\n");
						}
					}
				} // dt type = 4
				
				
				// only copy number gain loss
				if (type.equals("10")) {
					seq_type_map.put(dna_assay, dna_assay);
					
					
					//outGeneRaw.write(str + "\n");
					//if ((!chr.contains("X") && !chr.contains("Y")) && (end - start) >= length_cutoff && ((new Double(value) > log2FC_cutoff || new Double(value) < -log2FC_cutoff))) {
					//	out.write(sampleName + "\t" + chr + "\t" + start + "\t" + end + "\t" + (end - start) + "\t" + value + "\t" + dna_assay + "\t" + vorigin + "\t" + pmid + "\n");												
					//}
					
					// satisfy the fold change for gain
					//if (!segmean.equals("NA")) {
						if (Math.abs(new Double(segmean)) > log2FC_cutoff) {
							comprehensive_sample_name.put(sampleName, sampleName);
							
							boolean hit_arm = false;
							if (total_count_loh.containsKey(sampleName)) {
								HashMap seq_type = (HashMap)total_count_loh.get(sampleName);
								if (seq_type.containsKey(dna_assay)) {
									int count = (Integer)seq_type.get(dna_assay);
									count++;
									seq_type.put(dna_assay, count);
									total_count_loh.put(sampleName, seq_type);
								} else {
									seq_type.put(dna_assay, 1);
									total_count_loh.put(sampleName, seq_type);
								}
							} else {
								HashMap seq_type = new HashMap();
								seq_type.put(dna_assay, 1);
								total_count_loh.put(sampleName, seq_type);
							}
							// testing the arms
							for (String arms_coord: arms_pq) {
								String[] arms_coord_split = arms_coord.split(":");
								String arm_chr = arms_coord_split[0];
								int arm_start = new Integer(arms_coord_split[1]);
								int arm_end = new Integer(arms_coord_split[2]);
								// if the same chr
								if (chr.equals(arm_chr)) {								
									if (MathTools.embedded(arm_start, arm_end, start, end)) {
										
										// if it is basically arm level change
										if (new Double(end - start) / new Double(arm_end - arm_start) > 0.8) {
											if (arm_level_count_cutoff_loh.containsKey(sampleName)) {
												HashMap seq_type = (HashMap)arm_level_count_cutoff_loh.get(sampleName);
												if (seq_type.containsKey(dna_assay)) {
													int count = (Integer)seq_type.get(dna_assay);
													count++;
													seq_type.put(dna_assay, count);
													arm_level_count_cutoff_loh.put(sampleName, seq_type);
												} else {
													seq_type.put(dna_assay, 1);
													arm_level_count_cutoff_loh.put(sampleName, seq_type);
												}
											} else {
												HashMap seq_type = new HashMap();
												seq_type.put(dna_assay, 1);
												arm_level_count_cutoff_loh.put(sampleName, seq_type);
											} 
											hit_arm = true;
										} else {
											
										}
										
									} 
								}
							} // for loop iteration of the arms
							
							if (!hit_arm) {
								if (focal_event_cutoff_loh.containsKey(sampleName)) {
									HashMap seq_type = (HashMap)focal_event_cutoff_loh.get(sampleName);
									if (seq_type.containsKey(dna_assay)) {
										int count = (Integer)seq_type.get(dna_assay);
										count++;
										seq_type.put(dna_assay, count);
										focal_event_cutoff_loh.put(sampleName, seq_type);
									} else {
										seq_type.put(dna_assay, 1);
										focal_event_cutoff_loh.put(sampleName, seq_type);
									}
								} else {
									HashMap seq_type = new HashMap();
									seq_type.put(dna_assay, 1);
									focal_event_cutoff_loh.put(sampleName, seq_type);
								}
							}
							// smaller than 1 million
							if (new Double(end - start) < 1000000) {
								if (less_than_1million_cutoff_loh.containsKey(sampleName)) {
									HashMap seq_type = (HashMap)less_than_1million_cutoff_loh.get(sampleName);
									if (seq_type.containsKey(dna_assay)) {
										int count = (Integer)seq_type.get(dna_assay);
										count++;
										seq_type.put(dna_assay, count);
										less_than_1million_cutoff_loh.put(sampleName, seq_type);
									} else {
										seq_type.put(dna_assay, 1);
										less_than_1million_cutoff_loh.put(sampleName, seq_type);
									}
								} else {
									HashMap seq_type = new HashMap();
									seq_type.put(dna_assay, 1);
									less_than_1million_cutoff_loh.put(sampleName, seq_type);
								}
							}
							
							
							// testing the chr
							for (String chr_length: chr_len) {
								String[] chr_split = chr_length.split(":");
								String test_chr = chr_split[0];
								int test_chr_start = 1;
								int test_chr_end = new Integer(chr_split[1]);
								// if the same chr
								if (chr.equals(test_chr)) {								
									if (new Double(end - start) / new Double(test_chr_end - test_chr_start) > 0.8) {																		
										if (chr_level_count_cutoff_loh.containsKey(sampleName)) {
											HashMap seq_type = (HashMap)chr_level_count_cutoff_loh.get(sampleName);
											if (seq_type.containsKey(dna_assay)) {
												int count = (Integer)seq_type.get(dna_assay);
												count++;
												seq_type.put(dna_assay, count);
												chr_level_count_cutoff_loh.put(sampleName, seq_type);
											} else {
												seq_type.put(dna_assay, 1);
												chr_level_count_cutoff_loh.put(sampleName, seq_type);
											}
										} else {
											HashMap seq_type = new HashMap();
											seq_type.put(dna_assay, 1);
											chr_level_count_cutoff_loh.put(sampleName, seq_type);
										}
									}
								}
								
								
							} // end for each chr
							
						} // end if log passes cutoff
					//} // check if segmean exist
					
					
				} // dt type = 10
				
			}
			in.close();
			out.close();
			outGeneRaw.close();
			outGeneSEG.close();
			
			Iterator itr = uniq_marker.keySet().iterator();
			while (itr.hasNext()) {
				String marker = (String)itr.next();
				outGeneMK.write("Mark" + marker_id + "\t" + marker + "\n");
				marker_id++;
			}
			outGeneMK.close();
			
			itr = total_count.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				HashMap seq_type = (HashMap)total_count.get(sampleName);
				Iterator itr2 = seq_type.keySet().iterator();
				int wes_count = 0;
				int cgi_count = 0;
				int wgs_count = 0;
				int snp6_count = 0;
				int unknown_count = 0;
				int final_count = 0;
				while (itr2.hasNext()) {
					String seq_type_str = (String)itr2.next();
					int event_count = (Integer)seq_type.get(seq_type_str);
					if (seq_type_str.equals("mattr:wes")) {
						wes_count = event_count;
					}
					if (seq_type_str.equals("mattr:cgi")) {
						cgi_count = event_count;
					}
					if (seq_type_str.equals("mattr:wgs")) {
						wgs_count = event_count;
					}
					if (seq_type_str.equals("mattr:snp6")) {
						snp6_count = event_count;
					}
					unknown_count = event_count;
					outCountTable.write(sampleName + "\t" + seq_type_str + "\t" + event_count + "\n");
				}
				if (wgs_count > 0) {
					final_count = wgs_count;
				} else if (cgi_count > 0) {
					final_count = cgi_count;
				} else if (wes_count > 0) {
					final_count = wes_count;
				} else if (snp6_count > 0) {
					final_count = snp6_count;
				} else if (unknown_count > 0) {
					final_count = unknown_count;
				}
				outCountTable.write(sampleName + "\tPrioritized\t" + final_count + "\n");
			}
			outCountTable.close();

			outComprehensiveTable.write("SampleName\tTotalCNVCount\twhole_chr_change\tchr_arm_level_change\tfocal_change\tsmall_change\tTotalLOHCount\twhole_chr_loh_change\tchr_arm_level_loh_change\tfocal_loh_change\tsmall_loh_change\n");
			
			outComprehensiveTable.write("\n");
			itr = comprehensive_sample_name.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				outComprehensiveTable.write(sampleName.split("-")[0]);	
				if (total_count.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)total_count.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tTotalCount\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tTotalCount\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
			
				// chr_level_count_cutoff
				if (chr_level_count_cutoff.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)chr_level_count_cutoff.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tchr_level\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tchr_level\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
				

				// arm_level_count_cutoff
				if (arm_level_count_cutoff.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)arm_level_count_cutoff.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tarm_level\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tArmLevel\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
				
				// focal_event_cutoff
				if (focal_event_cutoff.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)focal_event_cutoff.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int final_count = 0;
					int unknown_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						} 
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tFocal_Level\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tFocalLevel\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
				
				// less_than_1million_cutoff
				if (less_than_1million_cutoff.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)less_than_1million_cutoff.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tLessThanOneMil_Level\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tLessThanOneMil\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
				
				// now calculate the LOH
				if (total_count_loh.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)total_count_loh.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tTotalCount_LOH\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tTotalCount_LOH\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
			
				// chr_level_count_cutoff_loh
				if (chr_level_count_cutoff_loh.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)chr_level_count_cutoff_loh.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tchr_level_loh\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tchr_level_loh\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
				

				// arm_level_count_cutoff
				if (arm_level_count_cutoff_loh.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)arm_level_count_cutoff_loh.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tarm_level_LOH\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tArmLevel_LOH\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
				
				// focal_event_cutoff
				if (focal_event_cutoff_loh.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)focal_event_cutoff_loh.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tFocal_Level_LOH\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tFocalLevel_LOH\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
				
				// less_than_1million_cutoff
				if (less_than_1million_cutoff_loh.containsKey(sampleName)) {
					HashMap seq_type = (HashMap)less_than_1million_cutoff_loh.get(sampleName);
					Iterator itr2 = seq_type.keySet().iterator();
					int wes_count = 0;
					int cgi_count = 0;
					int wgs_count = 0;
					int snp6_count = 0;
					int unknown_count = 0;
					int final_count = 0;
					while (itr2.hasNext()) {
						String seq_type_str = (String)itr2.next();
						int event_count = (Integer)seq_type.get(seq_type_str);
						if (seq_type_str.equals("mattr:wes")) {
							wes_count = event_count;
						}
						if (seq_type_str.equals("mattr:cgi")) {
							cgi_count = event_count;
						}
						if (seq_type_str.equals("mattr:wgs")) {
							wgs_count = event_count;
						}
						if (seq_type_str.equals("mattr:snp6")) {
							snp6_count = event_count;
						}
						unknown_count = event_count;
						outCountTableCutoff.write(sampleName + "\tLessThanOneMil_Level_LOH\t" + seq_type_str + "\t" + event_count + "\n");
					}
					if (wgs_count > 0) {
						final_count = wgs_count;
					} else if (cgi_count > 0) {
						final_count = cgi_count;
					} else if (wes_count > 0) {
						final_count = wes_count;
					} else if (snp6_count > 0) {
						final_count = snp6_count;
					} else if (unknown_count > 0) {
						final_count = unknown_count;
					}
					outComprehensiveTable.write("\t" + final_count);
					outCountTableCutoff.write(sampleName + "\tLessThanOneMil_LOH\tPrioritized\tCNV\t" + final_count + "\n");
				} else {
					outComprehensiveTable.write("\t0.0");
				}
				
				outComprehensiveTable.write("\n");
				
				
			} 
			
			outComprehensiveTable.close();
			outCountTableCutoff.close();
			
			Iterator itr5 = seq_type_map.keySet().iterator();
			while (itr5.hasNext()) {
				String type = (String)itr5.next();
				System.out.println(type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
