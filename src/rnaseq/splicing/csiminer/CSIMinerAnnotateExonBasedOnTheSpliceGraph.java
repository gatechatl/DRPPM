package rnaseq.splicing.csiminer;

import java.util.HashMap;
import java.util.LinkedList;

import idconversion.tools.GTFFile;

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

	
	public static void execute(String[] args) {
		
		try {
			
			String inputGTFFile = args[0]; // assumes the gtf is in order
			String query_exon = args[1];
			GTFFile gtf = new GTFFile();
			gtf.initialize(inputGTFFile);
	
			HashMap exon_terminal = new HashMap();
			HashMap exon2upstream = new HashMap();
			HashMap exon2downstream = new HashMap();
			
			String direction = query_exon.split(":")[2];
			// find the transcript for the query exon
			String transcript_id = (String)gtf.coord_exon2transcript.get(query_exon);
			String gene_id = (String)gtf.transcript2gene.get(transcript_id);
			String transcript_ids = (String)gtf.gene2transcript.get(gene_id);
			
			
			
			for (String transcript: transcript_ids.split(",")) {
				
				if (direction.equals("+")) {
					String coord_exons = (String)gtf.transcript2coord_exon.get(transcript);
					String[] split_coord_exons = coord_exons.split(",");
					for (int i = 0; i < split_coord_exons.length; i++) {
						String current_exon = split_coord_exons[i];
						
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
						
					}
					
				} else if (direction.equals("-")) {
					
					
					String coord_exons = (String)gtf.transcript2coord_exon.get(transcript);
					String[] split_coord_exons = coord_exons.split(",");
					for (int i = split_coord_exons.length - 1; i >= 0; i--) {
						String current_exon = split_coord_exons[i];
						
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
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
