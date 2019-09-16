package rnaseq.splicing.rnapeg;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;


/**
 * 1. Sort the junctions based on direction and genomic coordinates
 * 2. Check whether the junction is associated with dramatic increase or decrease of reads. Filter junctions that don't contribute to the change in expression value.
 * 3. Calculate the background distribution of intron region.
 * 4. Check whether the reads between junction is continuously above the background noise.
 * 5. For junction without background noise, Check for regions with gradual decrease of reads at the 5' end.
 * 
 * Output list of exons and their support strength.
 * 
 * @author tshaw
 *
 */
public class RNApegDefineExonBasedoOnBW {

	public static String type() {
		return "RNApeg";
	}
	public static String description() {
		String description = "\n";
		description += "* 1. Sort the junctions based on direction and genomic coordinates\n";
		description += "* 2. Check whether the junction is associated with dramatic increase or decrease of reads. Filter junctions that don't contribute to the change in expression value.\n";
		description += "* 3. Calculate the background distribution of intron region.\n";
		description += "* 4. Check whether the reads between junction is continuously above the background noise.\n";
		description += "* 5. For junction without background noise, Check for regions with gradual decrease of reads at the 5' end.RNApeg result: junctions.tab.shifted.tab.annotated.tab.cross_sample_corrected.tab\n";
		
		return description;
	}
	public static String parameter_info() {
		return "[inputRNApegExonResult] [bedGraph] [gtfFile] [geneName] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			LinkedList list = new LinkedList();
			HashMap map_novel = new HashMap();
			HashMap map_known = new HashMap();
			String direction = "";
			String inputRNApegExonResult = args[0];
			String bedGraphFile = args[1];
			
			String gtfFile = args[2];
			String geneName = args[3];
			int break_point_len = new Integer(args[4]); // the length of the breakpoint upstream/downstream of the junction to check
			double threshold_sig = new Double(args[5]);
			String outputFolder = args[6];			
			
			FileWriter fwriter_alt_start = new FileWriter(outputFolder + "/AlternativeStartSite.txt");
			BufferedWriter out_alt_start = new BufferedWriter(fwriter_alt_start);
			out_alt_start.write("gene_name\tchr\tstart\tend\tdirection\ttype\n");
			FileWriter fwriter_novel_exons = new FileWriter(outputFolder + "/NovelExons.txt");
			BufferedWriter out_novel_exons = new BufferedWriter(fwriter_novel_exons);			
			out_novel_exons.write("gene_name\tchr\tstart\tend\tdirection\ttype\n");
			
			FileInputStream fstream = new FileInputStream(inputRNApegExonResult);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				direction = split[3];
				String type = split[4];
				String junction = split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[5];
				
				if (!list.contains(junction)) {						
					list.add(junction);						
				}
				if (type.equals("Novel_Orphan_Exon") || type.equals("Putative_Alt_Start_Exon") || type.equals("Putative_Alt_Start_End_Exon")) {
					map_novel.put(junction, junction);
				} else if (type.equals("Novel_Exon")) {
					
					String right_junction = split[0] + "\t" + split[1] + "\t" + split[1] + "\t" + direction + "\t" + "right_junction";
					String left_junction = split[0] + "\t" + split[2] + "\t" + split[2] + "\t" + direction + "\t" + "left_junction";
					map_novel.put(right_junction, right_junction);
					map_novel.put(left_junction, left_junction);
					if (!list.contains(right_junction)) {
						list.add(right_junction);
					}
					if (!list.contains(left_junction)) {
						list.add(left_junction);
					}
				} else {
				
					String right_junction = split[0] + "\t" + split[1] + "\t" + split[1] + "\t" + direction + "\t" + "right_junction";
					String left_junction = split[0] + "\t" + split[2] + "\t" + split[2] + "\t" + direction + "\t" + "left_junction";
					map_known.put(right_junction, right_junction);
					map_known.put(left_junction, left_junction);
					
				}
			}
			in.close();
			
			LinkedList bed_list = new LinkedList();
			fstream = new FileInputStream(bedGraphFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				bed_list.add(str);
			}
			in.close();			
			
			LinkedList gene_exons = new LinkedList();
			fstream = new FileInputStream(gtfFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 8) {
					String chr = split[0];
					String type = split[2];
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					String name = GTFFile.grabMeta(split[8], "gene_name");
					if (type.equals("exon") && name.equals(geneName)) {
						
						gene_exons.add(chr + "\t" + start + "\t" + end);
					}
				}
			}
			in.close();
			
			System.out.println("Finished reading gtfFile: " + gtfFile);
			HashMap position_value = new HashMap();
			int min = 999999999;
			int max = 0;
			int bases = 0;
			LinkedList intron_mapping_counts = new LinkedList();
			LinkedList exon_mapping_counts = new LinkedList();
			Iterator itr = bed_list.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				String[] split = line.split("\t");
				double reads = new Integer(split[3]);
				for (int position = new Integer(split[1]); position < new Integer(split[2]); position++) {
					
					if (min > position) {
						min = position;
					}
					if (max < position) {
						max = position;
					}
					position_value.put(position, reads);
					
					boolean hit_exon = false;
					Iterator itr2 = gene_exons.iterator();
					while (itr2.hasNext()) {
						String exon_line = (String)itr2.next();
						String[] exon_line_split = exon_line.split("\t");
						int exon_start = new Integer(exon_line_split[1]);
						int exon_end = new Integer(exon_line_split[2]);
						if (exon_start <= position && position <= exon_end) {
							hit_exon = true;
							
						} 
					}
					if (hit_exon) {
						exon_mapping_counts.add(reads);
					} else {
						intron_mapping_counts.add(reads);
					}
				}				
			}
			
			System.out.println("Finished calculate exon and intron reads");
					
			double[] exon_counts = MathTools.convertListDouble2Double(exon_mapping_counts);
			double[] intron_counts = MathTools.convertListDouble2Double(intron_mapping_counts);
			
			double exon_avg = MathTools.mean(exon_counts);
			double exon_stdev = MathTools.standardDeviation(exon_counts);
			double intron_avg =  MathTools.mean(intron_counts);
			double intron_stdev = MathTools.standardDeviation(intron_counts);
			System.out.println("Avg Exon read count: " + exon_avg + "\tStdev: " + exon_stdev);
			System.out.println("Avg Intron read count: " + intron_avg + "\tStdev: " + intron_stdev);
			double cutoff = (MathTools.mean(exon_counts) + MathTools.mean(intron_counts)) / 2; // this is based on heuristics
			System.out.println("Assigning the cutoff at: " + cutoff);
			
			// need to define the min and max
						
			LinkedList annotated_list = new LinkedList();
			Collections.sort(list);
			int count_known = 0;
			int count_novel = 0;
			itr = list.iterator();			
			while (itr.hasNext()) {
				String exon_line = (String)itr.next();				
				String[] exon_line_split = exon_line.split("\t");
				int exon_start = new Integer(exon_line_split[1]);
				int exon_end = new Integer(exon_line_split[2]);
				String left_right_info = exon_line_split[4];
				int i = 0;
				double[] test_start = new double[break_point_len * 2];
				//System.out.println("Left: " + exon_line);
				for (int index = exon_start - break_point_len; index < exon_start + break_point_len; index++) {					
					if (position_value.containsKey(index)) {
						test_start[i] = (Double)position_value.get(index);
					} else {
						test_start[i] = 0.0;
					}
					//System.out.println(test_start[i]);
					i++;
				}
				
				i = 0;
				double[] test_end = new double[break_point_len * 2];
				//System.out.println("right: " + exon_line);
				for (int index = exon_end - break_point_len; index < exon_end + break_point_len; index++) {					
					if (position_value.containsKey(index)) {
						test_end[i] = (Double)position_value.get(index);
						
					} else {
						test_end[i] = 0.0;
					}
					//System.out.println(test_end[i]);
					i++;
					
				}
				String type = "Canonical";
				if (map_novel.containsKey(exon_line)) {
					type = "Novel";
					count_novel++;
				}
				if (map_novel.containsKey(exon_line) && map_known.containsKey(exon_line)) {
					type = "Known";
					count_known++;
				}
				System.out.println(exon_line + "\t" + type + "\t" + test_left_border(test_start, exon_avg, threshold_sig) + "\t" + test_right_border(test_end, exon_avg, threshold_sig) + "\t" + left_right_info);
				annotated_list.add(exon_line + "\t" + type + "\t" + test_left_border(test_start, exon_avg, threshold_sig) + "\t" + test_right_border(test_end, exon_avg, threshold_sig) + "\t" + left_right_info);
			}
			System.out.println("count_novel: " + count_novel);
			System.out.println("count_known: " + count_known);
			System.out.println("Finished redefining annotation list");
			
			HashMap confirmed_exons = new HashMap();
			HashMap confirmed_exons_junctions = new HashMap();
			
			// check the putative exons for their coverage
			itr = annotated_list.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				String[] split = line.split("\t");
				if (split[4].equals("Putative") && split[6].equals("true") && split[7].equals("true")) {
					int start = new Integer(line.split("\t")[1]);
					int end = new Integer(line.split("\t")[2]);
					LinkedList value_list = new LinkedList();
					for (int i = start; i < end; i++) {
						double value = 0.0;
						if (position_value.containsKey(i)) {
							value = (Double)position_value.get(i);
						}
						value_list.add(value);
					}					
					double[] values = MathTools.convertListDouble2Double(value_list);
					if (check_relatively_sustained_coverage(values, 0.0 + (intron_avg + intron_stdev))) {
						String novel_exon_info = split[0] + "\t" + start + "\t" + end + "\t" + direction + "\tNovel_Exon\tNovel_Exon\ttrue\ttrue\tNovelExon";
						confirmed_exons.put(novel_exon_info, novel_exon_info);
						confirmed_exons_junctions.put(split[0] + "\t" + start + "\t" + start + "\t" + direction + "\t" + "right_junction", "");
						confirmed_exons_junctions.put(split[0] + "\t" + end + "\t" + end + "\t" + direction + "\t" + "left_junction", "");
					}
				}
			}
			
			// now check for novel exons
			// the requirement is need to have a junction that contains a gain of coverage with a matched with a junction with loss of coverage.
			// the region between the junction needs to be sustained above the noise cutoff which is (noise_avg + noise_stdev). need additional work to justify this cutoff.
			String last_right_junction = "";
			itr = annotated_list.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				//System.out.println(line);
				String[] split = line.split("\t");
				
				if (split[4].equals("right_junction") && split[5].equals("Novel") && split[6].equals("true")) {
					last_right_junction = line;
				} else if (!last_right_junction.equals("") && split[4].equals("left_junction") && split[5].equals("Novel") && split[7].equals("true")) {
					
					int start = new Integer(last_right_junction.split("\t")[1]);
					int end = new Integer(line.split("\t")[1]);
					LinkedList value_list = new LinkedList();
					for (int i = start; i < end; i++) {
						double value = 0.0;
						if (position_value.containsKey(i)) {
							value = (Double)position_value.get(i);
						}
						value_list.add(value);
					}					
					
					double[] values = MathTools.convertListDouble2Double(value_list);
					if (check_relatively_sustained_coverage(values, 0.0 + (intron_avg + intron_stdev))) {
						String novel_exon_info = split[0] + "\t" + start + "\t" + end + "\t" + direction + "\tNovel_Exon\tNovel_Exon\ttrue\ttrue\tNovelExon";
						confirmed_exons.put(novel_exon_info, novel_exon_info);
						confirmed_exons_junctions.put(split[0] + "\t" + start + "\t" + start + "\t" + direction + "\t" + "right_junction", "");
						confirmed_exons_junctions.put(split[0] + "\t" + end + "\t" + end + "\t" + direction + "\t" + "left_junction", "");
						//System.out.println("Confirmed_exon: " + novel_exon_info);
						//System.out.println(split[0] + "\t" + start + "\t" + start + "\t" + direction + "\t" + "right_junction");
						//System.out.println(split[0] + "\t" + end + "\t" + end + "\t" + direction + "\t" + "left_junction");
					}
				} else {
					
					String[] split_last_right_junction = last_right_junction.split("\t");
					if (!last_right_junction.equals("")) {
						if (!(split[1].equals(split_last_right_junction[1]) || split[2].equals(split_last_right_junction[1]))) {
							last_right_junction = "";
						}
					}					
				}
			}
			
			// look in the descending direction to capture all possible local combination
			String last_left_junction = "";
			itr = annotated_list.descendingIterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				//System.out.println(line);
				String[] split = line.split("\t");
				if (split[4].equals("left_junction") && split[5].equals("Novel") && split[7].equals("true")) {
					last_left_junction = line;
				} else if (!last_left_junction.equals("") && split[4].equals("right_junction") && split[5].equals("Novel") && split[6].equals("true")) {
					
					//int start = new Integer(last_right_junction.split("\t")[1]);
					//int end = new Integer(line.split("\t")[1]);
					int start = new Integer(line.split("\t")[1]);
					int end = new Integer(last_left_junction.split("\t")[1]);
					LinkedList value_list = new LinkedList();
					for (int i = start; i < end; i++) {
						double value = 0.0;
						if (position_value.containsKey(i)) {
							value = (Double)position_value.get(i);
						}
						value_list.add(value);
					}					
					
					double[] values = MathTools.convertListDouble2Double(value_list);
					if (check_relatively_sustained_coverage(values, 0.0 + (intron_avg + intron_stdev))) {
						String novel_exon_info = split[0] + "\t" + start + "\t" + end + "\t" + direction + "\tNovel_Exon\tNovel_Exon\ttrue\ttrue\tNovelExon";
						confirmed_exons.put(novel_exon_info, novel_exon_info);
						confirmed_exons_junctions.put(split[0] + "\t" + start + "\t" + start + "\t" + direction + "\t" + "right_junction", "");
						confirmed_exons_junctions.put(split[0] + "\t" + end + "\t" + end + "\t" + direction + "\t" + "left_junction", "");
						//System.out.println("Confirmed_exon: " + novel_exon_info);
						//System.out.println(split[0] + "\t" + start + "\t" + start + "\t" + direction + "\t" + "right_junction");
						//System.out.println(split[0] + "\t" + end + "\t" + end + "\t" + direction + "\t" + "left_junction");
					}
				} else {
					String[] split_last_left_junction = last_left_junction.split("\t");
					if (!last_left_junction.equals("")) {
						if (!(split[1].equals(split_last_left_junction[1]) || split[2].equals(split_last_left_junction[1]))) {
							last_left_junction = "";
						}
					}
				
				}
			}
			
			itr = confirmed_exons.keySet().iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				System.out.println("Confirmed_exon:\t" + line);
				String[] split = line.split("\t");
				out_novel_exons.write(geneName + "\t" + split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4] + "\n");
			}
			itr = confirmed_exons_junctions.keySet().iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				System.out.println("confirmed exon_line: " + line);
			}
									
			// now we will try to test for alternative start site
			// criteria, the change of coverage should be less than 10% of the relative change per nucleotide, no dramatic change.
			// grab the region from the exon start to the avg + stdev noise. 
			itr = annotated_list.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				String[] split = line.split("\t");
				String exon_line = split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4];
				
				if (direction.equals("-") && split[4].equals("right_junction") && split[5].equals("Novel") && split[6].equals("true")) {
					//System.out.println();
					//System.out.println("Testing this junction for alt start: " + line);
					//System.out.println("exon_line: " + exon_line);
					if (!confirmed_exons_junctions.containsKey(exon_line)) {
						
						
						int position = new Integer(split[1]);
						int start_position = position;
						int max_position = start_position;
						
						boolean above_noise = true;
						boolean no_dramatic_change = true;
						double value = (Double)position_value.get(position);
						double original_value = value;
						double prev_value = value;
						double max_value = value;
						
						LinkedList alt_start_counts = new LinkedList();
						alt_start_counts.add(value);
						//System.out.println("original_value: " + original_value);
						//System.out.println("Cutoff point for noise: " + (intron_avg + intron_stdev * 1.5));
						if (value < intron_avg + intron_stdev * 1.5) {
							above_noise = false;
						}
						int current_position = position + 1;
						while (above_noise && no_dramatic_change) {
							
							if (position_value.containsKey(current_position)) {
								value = (Double)position_value.get(current_position);
								if (max_value < value) {
									max_value = value;
									max_position = current_position;
								}
								alt_start_counts.add(value);
								if (value < intron_avg + intron_stdev * 1.5) {
									above_noise = false;
								}
								
							} else {
								no_dramatic_change = false;
								//System.out.println("Found Zero");
								value = 0.0;
							}
							if ((prev_value - value) / exon_avg > 0.5) {
								//System.out.println("found dramatic change" + "\t" + (prev_value - value) / exon_avg + "\t" + (prev_value - value) + "\t" + exon_avg);
								no_dramatic_change = false;
							}
							prev_value = value;
							current_position = current_position + 1;
							//System.out.println(current_position + "\t" + value);
						}
						double avg_alt_start_value = MathTools.mean(MathTools.convertListDouble2Double(alt_start_counts));
						double upstream_max_value = grab_avg_value(position_value, start_position, max_position);
						double dnstream_max_value = grab_avg_value(position_value, max_position, current_position);
						//System.out.println("final_position: " + current_position);
						//System.out.println("avg_alt_start_value: " + avg_alt_start_value);
						if (no_dramatic_change && upstream_max_value > dnstream_max_value) {
							int alt_start_left_position = new Integer(split[1]);
							int alt_start_right_position = new Integer(current_position);
							boolean other_junction = false;
							Iterator itr5 = list.iterator();
							while (itr5.hasNext()) {
								String line5 = (String)itr5.next();
								String[] split_line5 = line5.split("\t");
								int junction_position = new Integer(split_line5[1]);
								String junction_type = split_line5[4];
								if (junction_type.equals("left_junction") && alt_start_left_position < junction_position && junction_position < alt_start_right_position) {
									other_junction = true;
								}								
							}
							if (!other_junction) {
								System.out.println("Putative Alternative Start Site: " + split[0] + "\t" + split[1] + "\t" + current_position + "\t" + direction + "\tNovel_Alt_Start\tNovel_Alt_Start\ttrue\ttrue\tNovel_Alt_Start");
								out_alt_start.write(geneName + "\t" + split[0] + "\t" + split[1] + "\t" + current_position + "\t" + direction + "\t" + "Novel_Alt_Start\n");
							}
						}
					} // end if
				} // end if negative direction 
				else if (direction.equals("+") && split[4].equals("left_junction") && split[5].equals("Novel") && split[7].equals("true")) {
					//System.out.println();
					//System.out.println("Testing this junction for alt start: " + line);
					//System.out.println("exon_line: " + exon_line);
					if (!confirmed_exons_junctions.containsKey(exon_line)) {
						
						
						int position = new Integer(split[1]);
						int start_position = position;
						int max_position = start_position;
						
						boolean above_noise = true;
						boolean no_dramatic_change = true;
						double value = (Double)position_value.get(position);
						double original_value = value;
						double prev_value = value;
						double max_value = value;
						
						LinkedList alt_start_counts = new LinkedList();
						alt_start_counts.add(value);
						//System.out.println("original_value: " + original_value);
						//System.out.println("Cutoff point for noise: " + (intron_avg + intron_stdev * 1.5));
						if (value < intron_avg + intron_stdev * 1.5) {
							above_noise = false;
						}
						int current_position = position - 1;
						while (above_noise && no_dramatic_change) {
							
							if (position_value.containsKey(current_position)) {
								value = (Double)position_value.get(current_position);
								if (max_value < value) {
									max_value = value;
									max_position = current_position;
								}
								alt_start_counts.add(value);
								if (value < intron_avg + intron_stdev * 1.5) {
									above_noise = false;
								}
								
							} else {
								no_dramatic_change = false;
								//System.out.println("Found Zero");
								value = 0.0;
							}
							if ((prev_value - value) / exon_avg > 0.5) {
								//System.out.println("found dramatic change" + "\t" + (prev_value - value) / exon_avg + "\t" + (prev_value - value) + "\t" + exon_avg);
								no_dramatic_change = false;
							}
							prev_value = value;
							current_position = current_position - 1;
							//System.out.println(current_position + "\t" + value);
						}
						double avg_alt_start_value = MathTools.mean(MathTools.convertListDouble2Double(alt_start_counts));
						double upstream_max_value = grab_avg_value(position_value, max_position, start_position);
						double dnstream_max_value = grab_avg_value(position_value, current_position, max_position);
						//System.out.println("final_position: " + current_position);
						//System.out.println("avg_alt_start_value: " + avg_alt_start_value);
						if (no_dramatic_change && upstream_max_value > dnstream_max_value) {
							int alt_start_right_position = new Integer(split[1]);
							int alt_start_left_position = new Integer(current_position);
							boolean other_junction = false;
							
							// check for other junctions
							Iterator itr5 = list.iterator();
							while (itr5.hasNext()) {
								String line5 = (String)itr5.next();
								String[] split_line5 = line5.split("\t");
								int junction_position = new Integer(split_line5[1]);
								String junction_type = split_line5[4];
								if (junction_type.equals("right_junction") && alt_start_left_position < junction_position && junction_position < alt_start_right_position) {
									other_junction = true;
								}								
							}
							if (!other_junction) {
								System.out.println("Putative Alternative Start Site: " + split[0] + "\t" + split[1] + "\t" + current_position + "\t" + direction + "\tNovel_Alt_Start\tNovel_Alt_Start\ttrue\ttrue\tNovel_Alt_Start");
								out_alt_start.write(geneName + "\t" + split[0] + "\t" + split[1] + "\t" + current_position + "\t" + direction + "\t" + "Novel_Alt_Start\n");
							}
						}
					} // end if
				} // end if negative direction
			}
			out_alt_start.close();
			out_novel_exons.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double grab_avg_value(HashMap map, int start, int end) {
		LinkedList list = new LinkedList();
		for (int i = start; i < end; i++) {
			if (map.containsKey(i)) {
				double value = (Double)map.get(i);
				list.add(value);
			}
		}
		return MathTools.mean(MathTools.convertListDouble2Double(list));
	}
	public static boolean test_left_border(double[] values, double norm_value, double threshold) {
		//double prev = values[0] / norm_value;
		LinkedList left = new LinkedList();
		for (int i = 0; i < (values.length / 2) + 1; i++) {
			left.add(values[i]);
		}
		LinkedList right = new LinkedList();
		for (int i = values.length / 2 + 1; i < values.length; i++) {
			right.add(values[i]);
		}
		double left_avg = MathTools.mean(MathTools.convertListDouble2Double(left));
		double right_avg = MathTools.mean(MathTools.convertListDouble2Double(right));
		
		if ((right_avg / norm_value) - (left_avg / norm_value) > threshold) {
			return true;
		}
		
		
		return false;
	}
	public static boolean test_right_border(double[] values, double norm_value, double threshold) {
		LinkedList left = new LinkedList();
		for (int i = 0; i < (values.length / 2) + 1; i++) {
			left.add(values[i]);
		}
		LinkedList right = new LinkedList();
		for (int i = values.length / 2 + 1; i < values.length; i++) {
			right.add(values[i]);
		}
		double left_avg = MathTools.mean(MathTools.convertListDouble2Double(left));
		double right_avg = MathTools.mean(MathTools.convertListDouble2Double(right));
		
		if ((left_avg / norm_value) - (right_avg / norm_value) > threshold) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether 90% of the values satisfy the cutoff
	 * @param values
	 * @param cutoff
	 * @return
	 */
	public static boolean check_relatively_sustained_coverage(double[] values, double cutoff) {
		double satisfy_count = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i] > cutoff) {
				satisfy_count++;
			}
		}
		if (satisfy_count / values.length > 0.9) {
			return true;
		}
		return false;
	}
}
