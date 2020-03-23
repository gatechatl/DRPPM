package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * In order to filer out the exon 
 * @author tshaw
 *
 */
public class JinghuiZhangFilteringThePrioritizedExonListOld {

	
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\PCGP_TARGET_GTEx_Candidates.txt";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\PCGP_TARGET_GTEx_Candidates_FilterForOverlapNovelCandidates.txt";
			
			HashMap novel_map = new HashMap();
			HashMap known_map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = split[19];
				int start = new Integer(split[20]);
				int end = new Integer(split[21]);
				String annotation = split[23];  
				if (annotation.equals("KnownECM")) {
					String geneList = split[0].split("\\|")[0];
					for (String gene: geneList.split("\\+")) {
						if (known_map.containsKey(gene)) {
							HashMap exon_list = (HashMap)known_map.get(gene);
							exon_list.put(split[0], chr + "," + start + "," + end);
							known_map.put(gene, exon_list);
						} else {
							HashMap exon_list = new HashMap();
							exon_list.put(split[0], chr + "," + start + "," + end);
							known_map.put(gene, exon_list);
						}
					}
				} else {
					novel_map.put(split[0], chr + "," + start + "," + end);
					//known_map.put(split[0].split("_")[0], novel_map);
				}
				
			}
			in.close();
						
			System.out.println(novel_map.size());
			int count = 0;
			HashMap good_exons = new HashMap();
			HashMap bad_exons = new HashMap();
			HashMap check_max_overlap_percentage = new HashMap();
			// check for duplicates
			Iterator itr = novel_map.keySet().iterator();
			while (itr.hasNext()) {
				String novel_exon = (String)itr.next();
				String novel_location = (String)novel_map.get(novel_exon);
				String novel_chr = novel_location.split(",")[0];
				int novel_start = new Integer(novel_location.split(",")[1]);
				int novel_end = new Integer(novel_location.split(",")[2]);
				count++;
				//System.out.println(count);
				boolean overlap = false;
				String novel_gene = novel_exon.split("_")[0];
				if (known_map.containsKey(novel_gene)) {
					HashMap exon_list = (HashMap)known_map.get(novel_gene);
					Iterator itr2 = exon_list.keySet().iterator();
					while (itr2.hasNext()) {
						String known_exon = (String)itr2.next();
						String known_location = (String)exon_list.get(known_exon);
						String known_chr = known_location.split(",")[0];
						int known_start = new Integer(known_location.split(",")[1]);
						int known_end = new Integer(known_location.split(",")[2]);
						if (known_chr.equals(novel_chr)) {
							if (check_overlap(known_start, known_end, novel_start, novel_end)) {
								double len = get_overlapping_length(known_start, known_end, novel_start, novel_end);
								double total = known_end - known_start;
								if (total > 0) {
									if (check_max_overlap_percentage.containsKey(novel_exon)) {
										double prev = (Double)check_max_overlap_percentage.get(novel_exon);
										if ((len / total) > prev) {
											check_max_overlap_percentage.put(novel_exon, len / total);
										}
									} else {
										check_max_overlap_percentage.put(novel_exon, len / total);
									}
								}
							}
							if ((known_start <= novel_start && novel_start <= known_end) && (known_start <= novel_end && novel_end <= known_end)) {
								overlap = true;
								bad_exons.put(novel_exon, novel_exon);
								System.out.println("bad: " + novel_exon);
								System.out.println("hit on: " + known_exon);
								break;
							}

						}
						
					}
					good_exons.put(novel_exon, novel_exon);
					
				} else {
					//System.out.println("Absent: " + novel_gene);
				}
			}
			


			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\tMaxPercentageOverlap\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double percentage = 0.0;
				if (check_max_overlap_percentage.containsKey(split[0])) {
					percentage = (Double)check_max_overlap_percentage.get(split[0]);
				}
				if (good_exons.containsKey(split[0])) {
					out.write(str + "\t" + percentage + "\n");
				}
			}
			in.close();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean check_overlap(int a1, int a2, int b1, int b2) {
		if (a1 <= b1 && b1 <= a2) {
			return true;
		}
		if (a1 <= b2 && b2 <= a2) {
			return true;
		}
		if (b1 <= a1 && a1 <= b2) {
			return true;
		}
		if (b1 <= a2 && a2 <= b2) {
			return true;
		}
		return false;
	}
	public static int get_overlapping_length(int a1, int a2, int b1, int b2) {
		
		if (check_overlap(a1, a2, b1, b2) && a2 >= a1 && b2 >= b1) {
			if (a1 <= b1 && b1 <= a2 && a1 <= b2 && b2 <= a2) {
				return b2 - b1;
			}
			if (b1 <= a1 && a1 <= b2 && b1 <= a2 && a2 <= b2) {
				return a2 - a1;
			}
			if (a1 <= b1 && b1 <= a2) {
				return a2 - b1;
			}
			if (b1 <= a1 && a1 <= b2) {
				return b2 - a1;
			}
			if (a1 <= b2 && b2 <= a2) {
				return b2 - a1;
			}
			if (b1 <= a2 && a2 <= b2) {
				return a2 - b1;
			}
		}
		return 0;
	}
}
