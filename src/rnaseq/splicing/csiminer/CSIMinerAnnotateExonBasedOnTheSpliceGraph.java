package rnaseq.splicing.csiminer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import idconversion.tools.GTFFile;
import idconversion.tools.GTFFileExon;

/**
 * Examine whether there are known differentiated isoforms capturing the exon of interest. Output a true false table with annotation with other differentiated exons.
 * 1. Read through the GTF file and define the gene -> exon map
 * 2. Generate an exon graph which contains a map of exon and downstream linkedlist targets. 
 * 3. Generate an exon graph which contains a map of exon and upstream linkedlist targets.
 * 4. The exon naming is chr_start_stop_direction -> new_coord_exon = "CHR" + chr + ":" + start + "-" + end + ":" + direct;		
 * 5. Identify the exon with multiple downstream and upstream. Check for exons downstream of them.
 * 6. Grab exons that are between the two paths
 * 
 * @author gatechatl
 *
 */
public class CSIMinerAnnotateExonBasedOnTheSpliceGraph {

	
	public static String type() {
		return "CSI-Miner";
	}
	public static String description() {
		return "Grab the surrounding exons that contains a different isoform for the same gene.";
	}
	public static String parameter_info() {
		return "[inputGTFFile] [query_exon] [outputSplicingHotspot]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputGTFFile = args[0]; // assumes the gtf is in order
			String query_exon = args[1];
			String outputSplicingHotspot = args[2];
			
			FileWriter fwriter = new FileWriter(outputSplicingHotspot);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			GTFFileExon gtf = new GTFFileExon();
			gtf.initialize(inputGTFFile);
	
			System.out.println("Finish loading GTF file");
			
			HashMap exon_terminal = new HashMap();
			HashMap exon2upstream = new HashMap();
			HashMap exon2downstream = new HashMap();
			
			String direction = query_exon.split(":")[2];
			// find the transcript for the query exon

			int query_exon_start = new Integer(query_exon.split(":")[1].split("-")[0]);
			int query_exon_end = new Integer(query_exon.split(":")[1].split("-")[1]);
			
			System.out.println("query_exon: " + query_exon);
			String transcript_ids = "NA";
			if (gtf.coord_exon2transcript.containsKey(query_exon)) {
				transcript_ids = (String)gtf.coord_exon2transcript.get(query_exon);
			} 
			System.out.println("transcript_id: " + transcript_ids);
			String gene_id = "NA";
			if (gtf.transcript2gene.containsKey(transcript_ids.split(",")[0])) {
				gene_id = (String)gtf.transcript2gene.get(transcript_ids.split(",")[0]);
			}
			System.out.println("gene_id: " + gene_id);
			if (gtf.gene2transcript.containsKey(gene_id)) {
				transcript_ids = (String)gtf.gene2transcript.get(gene_id);
			}
			System.out.println("All transcript in this gene: " + transcript_ids);
						
			HashMap check_valid_transcript = new HashMap();
			HashMap check_partial_transcript = new HashMap();
			for (String transcript: transcript_ids.split(",")) {
				
				//if (direction.equals("+")) {
					if (gtf.transcript2coord_exon.containsKey(transcript)) {
						
						int transcript_start = 999999999;
						int transcript_end = 0;
						
						String coord_exons = (String)gtf.transcript2coord_exon.get(transcript);
						String[] split_coord_exons = coord_exons.split(",");
						for (int i = 0; i < split_coord_exons.length; i++) {
							String current_exon = split_coord_exons[i];
							
							int current_exon_start = new Integer(current_exon.split(":")[1].split("-")[0]);
							int current_exon_end = new Integer(current_exon.split(":")[1].split("-")[1]);								
							
							if (transcript_start > current_exon_start) {
								transcript_start = current_exon_start;
							}
							if (transcript_end < current_exon_end) {
								transcript_end = current_exon_end;
							}
							
							System.out.println("iterating through exon check current_exon in " + transcript + ": " + current_exon);
							// downstream exon
							if (i + 1 < split_coord_exons.length) {
								String next_exon = split_coord_exons[i + 1];
								
								
								if (exon2downstream.containsKey(current_exon)) {
									LinkedList next_exon_list = (LinkedList)exon2downstream.get(current_exon);
									if (!next_exon_list.contains(next_exon)) {
										next_exon_list.add(next_exon);
									}
									exon2downstream.put(current_exon, next_exon_list);
								} else {
									LinkedList next_exon_list = new LinkedList();
									next_exon_list.add(next_exon);
									exon2downstream.put(current_exon, next_exon_list);
								}
							}
							
							// upstream exon
							if (i - 1 >= 0) {
								String prev_exon = split_coord_exons[i - 1];
								if (exon2upstream.containsKey(current_exon)) {
									LinkedList prev_exon_list = (LinkedList)exon2upstream.get(current_exon);
									if (!prev_exon_list.contains(prev_exon)) {
										prev_exon_list.add(prev_exon);
									}
									exon2upstream.put(current_exon, prev_exon_list);
								} else {
									LinkedList prev_exon_list = new LinkedList();
									prev_exon_list.add(prev_exon);
									exon2upstream.put(current_exon, prev_exon_list);
								}							
							}
							
						} // end iteration of all the exons
						
						if (transcript_start <= query_exon_start && query_exon_end <= transcript_end) {							
							check_valid_transcript.put(transcript, false);
							check_partial_transcript.put(transcript, false);
							for (int i = 0; i < split_coord_exons.length; i++) {
								if (split_coord_exons[i].equals(query_exon)) {
									check_valid_transcript.put(transcript, true);
								}
								
								if (split_coord_exons[i].split(":")[1].split("-")[0].equals(query_exon.split(":")[1].split("-")[0])) {
									check_partial_transcript.put(transcript, true);
								}
								if (split_coord_exons[i].split(":")[1].split("-")[1].equals(query_exon.split(":")[1].split("-")[1])) {
									check_partial_transcript.put(transcript, true);
								}
							}
						}
					}
					
					
				/*} else if (direction.equals("-")) {
					
					
					
					if (gtf.transcript2coord_exon.containsKey(transcript)) {
						String coord_exons = (String)gtf.transcript2coord_exon.get(transcript);
						String[] split_coord_exons = coord_exons.split(",");
						System.out.println(transcript + ": " + coord_exons);
						for (int i = split_coord_exons.length - 1; i >= 0; i--) {
							String current_exon = split_coord_exons[i];
							System.out.println("iterating through exon check current_exonin " + transcript + ": " + current_exon);
							// upstream exon
							if (i + 1 < split_coord_exons.length) {
								String prev_exon = split_coord_exons[i + 1];
								if (exon2upstream.containsKey(current_exon)) {
									LinkedList prev_exon_list = (LinkedList)exon2upstream.get(current_exon);
									if (!prev_exon_list.contains(prev_exon)) {
										prev_exon_list.add(prev_exon);
									}
									exon2upstream.put(current_exon, prev_exon_list);
								} else {
									LinkedList prev_exon_list = new LinkedList();
									prev_exon_list.add(prev_exon);
									exon2upstream.put(current_exon, prev_exon_list);
								}
							}
							
							// downstream exon
							if (i - 1 >= 0) {
								String next_exon = split_coord_exons[i - 1];
								if (exon2downstream.containsKey(current_exon)) {
									LinkedList next_exon_list = (LinkedList)exon2downstream.get(current_exon);
									if (!next_exon_list.contains(next_exon)) {
										next_exon_list.add(next_exon);
									}
									exon2downstream.put(current_exon, next_exon_list);
								} else {
									LinkedList prev_exon_list = new LinkedList();
									prev_exon_list.add(next_exon);
									exon2downstream.put(current_exon, prev_exon_list);
								}							
							}
							
						}
					}
				} */			
			}
			
			// look for upstream exon
			boolean check_splicing_status = false;
			
			
			
			boolean search_upstream = true; 
			String up_stream_exon = find_up_stream_exons(exon2upstream, exon2downstream, query_exon);	
			String down_stream_exon = find_down_stream_exons(exon2upstream, exon2downstream, query_exon);
			System.out.println("Final up_stream_exon: " + up_stream_exon);
			System.out.println("Final down_stream_exon: " + down_stream_exon);
			String split_upstream_exon = up_stream_exon.split(":")[1];
			String split_downstream_exon = down_stream_exon.split(":")[1];
			int exon_upstream_start = new Integer(split_upstream_exon.split("-")[0]);
			int exon_upstream_end = new Integer(split_upstream_exon.split("-")[1]);
			int exon_downstream_start = new Integer(split_downstream_exon.split("-")[0]);
			int exon_downstream_end = new Integer(split_downstream_exon.split("-")[1]);
			LinkedList exon_candidates = new LinkedList();
			
			for (String transcript: transcript_ids.split(",")) {
				if (gtf.transcript2coord_exon.containsKey(transcript)) {
					String coord_exons = (String)gtf.transcript2coord_exon.get(transcript);
					String[] split_coord_exons = coord_exons.split(",");
					for (String exon: split_coord_exons) {
						String split_exon = exon.split(":")[1];					
						int exon_start = new Integer(split_exon.split("-")[0]);
						int exon_end = new Integer(split_exon.split("-")[1]);
						if (direction.equals("+")) {
							if (exon_upstream_start <= exon_start && exon_end <= exon_downstream_end) {
								if (!exon_candidates.contains(exon)) {
									exon_candidates.add(exon);
								}
							}
						} else if (direction.equals("-")) {
							if (exon_downstream_start <= exon_start && exon_end <= exon_upstream_end) {
								if (!exon_candidates.contains(exon)) {
									exon_candidates.add(exon);
								}
							}
						}				
					}
				}
			}
			
			int count = exon_candidates.size();
			
			String exons = "";
			Iterator itr = exon_candidates.iterator();
			while (itr.hasNext()) {
				String exon = (String)itr.next();
				exons += exon + ",";
			}
			
			String position = "NA";
			if (direction.equals("+")) {
				position = query_exon.split(":")[0] + ":" + exon_upstream_start + "-" + exon_downstream_end;
			} else {
				position = query_exon.split(":")[0] + ":" + exon_downstream_start + "-" + exon_upstream_end;
			}
			
			String transcript_hit = "";
			String transcript_nohit = "";
			double count_nohit = 0;
			double count_hit = 0;
			itr = check_valid_transcript.keySet().iterator();
			while (itr.hasNext()) {
				String transcript = (String)itr.next();
				boolean hit = (Boolean)check_valid_transcript.get(transcript);
				if (hit) {
					transcript_hit += transcript + ",";
					count_hit++;
				} else {
					transcript_nohit += transcript + ",";
					count_nohit++;
				}
			}
			boolean alternative_splicing_flag = false;
			if ((count_hit / (count_hit + count_nohit)) < 1) {
				alternative_splicing_flag = true;
			}
			
			String partial_transcript_hit = "";
			String partial_transcript_nohit = "";
			double partial_count_nohit = 0;
			double partial_count_hit = 0;
			itr = check_partial_transcript.keySet().iterator();
			while (itr.hasNext()) {
				String transcript = (String)itr.next();
				boolean hit = (Boolean)check_partial_transcript.get(transcript);
				if (hit) {
					partial_transcript_hit += transcript + ",";
					partial_count_hit++;
				} else {
					partial_transcript_nohit += transcript + ",";
					partial_count_nohit++;
				}
			}
			boolean partial_alternative_splicing_flag = false;
			if ((partial_count_hit / (partial_count_hit + partial_count_nohit)) < 1) {
				partial_alternative_splicing_flag = true;
			}
			
			String annotation = "NoSplicingChange(Recommend Checking)";
			if (partial_alternative_splicing_flag && alternative_splicing_flag) {
				annotation = "AlternativeExonUsage";
			}
			if (!partial_alternative_splicing_flag && alternative_splicing_flag) {
				annotation = "PartialSpliceWithSharedExonCoordinate";
			}
			
			if (transcript_hit.equals("")) {
				transcript_hit = "NA";
			}
			if (transcript_nohit.equals("")) {
				transcript_nohit = "NA";
			}
			
			out.write(query_exon + "\t" + annotation + "\t" + alternative_splicing_flag + "\t" + partial_alternative_splicing_flag + "\t" + position + "\t" + count + "\t" + up_stream_exon + "\t" + down_stream_exon + "\t" + exons + "\t" + transcript_hit + "\t" + transcript_nohit + "\n");
			
			out.close();
			System.out.println(query_exon + "\t" + annotation + "\t" + alternative_splicing_flag + "\t" + partial_alternative_splicing_flag + "\t" + position + "\t" + count + "\t" + up_stream_exon + "\t" + down_stream_exon + "\t" + exons + "\t" + transcript_hit + "\t" + transcript_nohit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String find_up_stream_exons(HashMap exon2upstream, HashMap exon2downstream, String query_exon) {
		System.out.println("up_stream_exon: " + query_exon);
		if (exon2upstream.containsKey(query_exon)) {
			LinkedList upstream_exon_list = (LinkedList)exon2upstream.get(query_exon);
			String direction = query_exon.split(":")[2];
			LinkedList up_stream_candidates = new LinkedList();
			Iterator itr = upstream_exon_list.iterator();		
			while (itr.hasNext()) {
				String up_stream_exon = (String)itr.next();
				System.out.println("up_stream_exon list: " + up_stream_exon);
				LinkedList downstream_exon_list = (LinkedList)exon2downstream.get(up_stream_exon);
				if (downstream_exon_list.size() == 1) {
					//	return up_stream_exon;
					
					// need to look upstream for more.
					String candidate = find_up_stream_exons(exon2upstream, exon2downstream, up_stream_exon);
					if (!up_stream_candidates.contains(candidate)) {
						up_stream_candidates.add(candidate);
					}
					
				} else if (downstream_exon_list.size() > 1){		
					// if this is the one then we return this one.
					if (upstream_exon_list.size() == 1) {
						return up_stream_exon;
					} else {
						// if there are more then we need to compare
						up_stream_candidates.add(up_stream_exon);						
					}					
				} 
			}		
			if (up_stream_candidates.size() == 1) {
				return (String)up_stream_candidates.get(0);
			} else if (up_stream_candidates.size() > 1) {	
				
				if (direction.equals("+")) {
					String return_exon = (String)up_stream_candidates.get(0);
					for (int i = 1; i < up_stream_candidates.size() - 1; i++) {
						String test_exon = (String)up_stream_candidates.get(i);
						if (isExon1LowerThanExon2(test_exon, return_exon)) {
							return_exon = test_exon;
						}
					}			
					return return_exon;
				} else if (direction.equals("-")) {
					String return_exon = (String)up_stream_candidates.get(0);
					for (int i = 1; i < up_stream_candidates.size() - 1; i++) {
						String test_exon = (String)up_stream_candidates.get(i);
						if (isExon2HigherThanExon1(return_exon, test_exon)) {
							return_exon = test_exon;
						}
					}			
					return return_exon;
				}
			}
		}
		return "NA";
	}
	
	public static String find_down_stream_exons(HashMap exon2upstream, HashMap exon2downstream, String query_exon) {
		System.out.println("down_stream_exon: " + query_exon);
		if (exon2downstream.containsKey(query_exon)) {
			LinkedList downstream_exon_list = (LinkedList)exon2downstream.get(query_exon);
			String direction = query_exon.split(":")[2];
			LinkedList down_stream_candidates = new LinkedList();
			Iterator itr = downstream_exon_list.iterator();		
			while (itr.hasNext()) {
				String down_stream_exon = (String)itr.next();
				System.out.println("down_stream_exon list: " + down_stream_exon);
				LinkedList upstream_exon_list = (LinkedList)exon2upstream.get(down_stream_exon);
				if (upstream_exon_list.size() == 1) {
					//	
					
					String candidate = find_down_stream_exons(exon2upstream, exon2downstream, down_stream_exon);
					if (!down_stream_candidates.contains(candidate)) {
						down_stream_candidates.add(candidate);
					}
					
				} else if (upstream_exon_list.size() > 1){		
					// if this is the only one then this is the answer.
					if (downstream_exon_list.size() == 1) {
						return down_stream_exon;
					} else {											
						// if there are more then we need to compare
						down_stream_candidates.add(down_stream_exon);						
					}					
				} 
			}		
			if (down_stream_candidates.size() == 1) {
				return (String)down_stream_candidates.get(0);
			} else if (down_stream_candidates.size() > 1) {		
				if (direction.equals("-")) {
					String return_exon = (String)down_stream_candidates.get(0);
					for (int i = 1; i < down_stream_candidates.size() - 1; i++) {
						String test_exon = (String)down_stream_candidates.get(i);
						if (isExon1LowerThanExon2(test_exon, return_exon)) {
							return_exon = test_exon;
						}
					}			
					return return_exon;
				} else if (direction.equals("+")) {
					String return_exon = (String)down_stream_candidates.get(0);
					for (int i = 1; i < down_stream_candidates.size() - 1; i++) {
						String test_exon = (String)down_stream_candidates.get(i);
						if (isExon2HigherThanExon1(return_exon, test_exon)) {
							return_exon = test_exon;
						}
					}			
					return return_exon;
				}
			}
		}
		return "NA";
	}
	/**
	 * Is exon2 higher than exon1 based on coordinate
	 * @param query_exon1
	 * @param query_exon2
	 * @return
	 */
	public static boolean isExon2HigherThanExon1(String query_exon1, String query_exon2) {
		if (query_exon1.equals("NA")) {
			return true;
		}
		
		String split_query_exon1 = query_exon1.split(":")[1];
		String split_query_exon2 = query_exon2.split(":")[1];
		int exon1_start = new Integer(split_query_exon1.split("-")[0]);
		int exon1_end = new Integer(split_query_exon1.split("-")[1]);
		int exon2_start = new Integer(split_query_exon2.split("-")[0]);
		int exon2_end = new Integer(split_query_exon2.split("-")[1]);
		if (exon2_end > exon1_end) {
			return true;
		} else {
			if (exon2_start > exon1_start) {
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Is exon1 lower than exon2 based on coordinate
	 * @param query_exon1
	 * @param query_exon2
	 * @return
	 */
	public static boolean isExon1LowerThanExon2(String query_exon1, String query_exon2) {
		if (query_exon1.equals("NA")) {
			return false;
		}
		String split_query_exon1 = query_exon1.split(":")[1];
		String split_query_exon2 = query_exon2.split(":")[1];
		int exon1_start = new Integer(split_query_exon1.split("-")[0]);
		int exon1_end = new Integer(split_query_exon1.split("-")[1]);
		int exon2_start = new Integer(split_query_exon2.split("-")[0]);
		int exon2_end = new Integer(split_query_exon2.split("-")[1]);
		if (exon1_start < exon2_start) {
			return true;
		} else {
			if (exon1_end < exon2_end) {
				return true;
			}
			return false;
		}
	}
}

