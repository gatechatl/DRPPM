package rnaseq.splicing.cseminer.annotation;

import idconversion.tools.GTFFile;
import idconversion.tools.GTFFileExon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Generates the json graph for the exon
 * @author gatechatl
 *
 */
public class CSIMinerGTF2JsonExonGraph {

	public static void main(String[] args) {
		
		
		try {
			
			String inputGTFFile = "/home/gatechatl/References/genomic/hg38_release35_GRCh38.p13/GTF/gencode.v35.primary_assembly.annotation.gtf"; //args[0]; // assumes the gtf is in order
			//String inputGTFFile = "/home/gatechatl/CSI-Miner/Graph/FN1.gtf"; //args[0]; // assumes the gtf is in order
			String appris_transcript_annotation = "/home/gatechatl/References/genomic/APPRIS/Gencode36_Ensembl102_appris_data.principal.txt";
			//String geneName = "FN1"; //args[2];
			//String outputSIF = "/home/gatechatl/CSI-Miner/Graph/graph.sif"; //args[3];
			String outputFolder = "/home/gatechatl/CSI-Miner/Graph/Gene";
			//String outputJSON = "/home/gatechatl/CSI-Miner/Graph/graph.json"; //args[4];
			
			String outputExonAnnotation = "/home/gatechatl/CSI-Miner/Gencode36_Ensembl102_ApprisExonAnnotation.txt";
			
			FileWriter fwriter_exon_annotation = new FileWriter(outputExonAnnotation);
			BufferedWriter out_exon_annotation = new BufferedWriter(fwriter_exon_annotation);	
			
			HashMap principal_transcript = new HashMap();
			HashMap alternative_transcript = new HashMap();
			FileInputStream fstream = new FileInputStream(appris_transcript_annotation);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[4].contains("PRINCIPAL")) {
					principal_transcript.put(split[2], split[0]);					
				}
				if (split[4].contains("ALTERNATIVE")) {
					alternative_transcript.put(split[2], split[0]);					
				}
			}
			in.close();
			
			System.out.println("Loading GTF file...");
			
			GTFFile gtf = new GTFFile();
			gtf.initialize(inputGTFFile);
			System.out.println(gtf.geneName2geneID.size());
			
			Iterator itr3 = gtf.geneName2geneID.keySet().iterator();
			while (itr3.hasNext()) {
				String geneName = (String)itr3.next();
				
				//GTFFile gtf = gtf2;
				//gtf.initialize_geneName(inputGTFFile, geneName);
				System.out.println(geneName);
				String outputJSON = outputFolder + "/" + geneName.replaceAll("/", "_") + ".json";
				FileWriter fwriter = new FileWriter(outputJSON);
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("{\"nodes\":[");
				//System.out.println("Finish loading GTF file");
				
				LinkedList gene_id_list = (LinkedList)gtf.geneName2geneID.get(geneName);
				
				Iterator itr2 = gene_id_list.iterator();
				while (itr2.hasNext()) {
					String gene_id = (String)itr2.next();
							
					HashMap exon_terminal = new HashMap();
					HashMap exon2upstream = new HashMap();
					HashMap exon2downstream = new HashMap();
					
					
					String transcript_ids = "NA";
					 
					System.out.println("transcript_id: " + transcript_ids);
					String gene_biotype = (String)gtf.geneid2biotype.get(gene_id);
					
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
					
					
					HashMap links = new HashMap();
					HashMap edge = new HashMap();
					HashMap edge_group = new HashMap();
					
					for (String transcript: transcript_ids.split(",")) {
						if (gtf.transcript2biotype.containsKey(transcript)) {
							String transcript_biotype = (String)gtf.transcript2biotype.get(transcript);
							//if (direction.equals("+")) {
							if (gtf.transcript2coord_exon.containsKey(transcript) && gene_biotype.equals("protein_coding") && transcript_biotype.equals("protein_coding")) {							
								int transcript_start = 999999999;
								int transcript_end = 0;
								
								String coord_exons = (String)gtf.transcript2coord_exon.get(transcript);
								System.out.println(transcript + "\t" + coord_exons);
								String[] split_coord_exons = coord_exons.split(",");
								String prev_exon_name = "";
								for (int i = 0; i < split_coord_exons.length; i++) {
									String current_exon = split_coord_exons[i];
									String are_the_exon_same =  (String)gtf.transcript2exon_num.get(transcript + "." + (i + 1));
									System.out.println(current_exon + "\t" + are_the_exon_same);
									int current_exon_start = new Integer(current_exon.split(":")[1].split("-")[0]);
									int current_exon_end = new Integer(current_exon.split(":")[1].split("-")[1]);								
									current_exon = current_exon.replaceAll("CHRchr", "chr");
									String current_exon_chr = current_exon.split(":")[0];
									String current_exon_direction = current_exon.split(":")[2];
									
									String current_exon_name = geneName + "_" + current_exon_chr + "_" + current_exon_start + "_" + current_exon_end + "_" + current_exon_direction;
									if (edge.containsKey(current_exon_name)) {
										int count_exon = (Integer)edge.get(current_exon_name);
										count_exon++;
										edge.put(current_exon_name, count_exon);
									} else {
										edge.put(current_exon_name, 1);
									}
									String transcript_type = "NA";
									if (principal_transcript.containsKey(transcript.split("\\.")[0])) {
										transcript_type = "PRINCIPAL";
									} else if (alternative_transcript.containsKey(transcript.split("\\.")[0])) {
										transcript_type = "ALTERNATIVE";
									} 
									if (edge_group.containsKey(current_exon_name)) {
										String prev_type = (String)edge_group.get(current_exon_name);
										if (transcript_type.equals("PRINCIPAL")) {
											edge_group.put(current_exon_name, transcript_type);
											prev_type = "PRINCIPAL";
										}
										if (!transcript_type.equals("NA")) {
											if (prev_type.equals("NA")) {
												edge_group.put(current_exon_name, transcript_type); 
											}										
										}
									} else {
										edge_group.put(current_exon_name, transcript_type);
										
									}
									
									if (!prev_exon_name.equals("")) {
										links.put(prev_exon_name + "\t" + current_exon_name, "");
										links.put(current_exon_name  + "\t" + prev_exon_name, "");
										System.out.println("Links: " + current_exon_name + "\t" + prev_exon_name);
									}
									if (transcript_start > current_exon_start) {
										transcript_start = current_exon_start;
									}
									if (transcript_end < current_exon_end) {
										transcript_end = current_exon_end;
									}
									
									//System.out.println("iterating through exon check current_exon in " + transcript + ": " + current_exon);
									prev_exon_name = current_exon_name;
									
								} // end iteration of all the exons
								
							}
						}
					}
						
					
					LinkedList sorted_keys = sortbykey(edge);
					
					boolean first = true;
					Iterator itr = sorted_keys.iterator();
					while (itr.hasNext()) {
						String exon_name = (String)itr.next();
						int count = (Integer)edge.get(exon_name);
						String transcript_type = (String)edge_group.get(exon_name);
						int grp_type = 4;
						if (transcript_type.equals("PRINCIPAL")) {
							grp_type = 1;
						} else if (transcript_type.equals("ALTERNATIVE")) {
							grp_type = 2;
						}
						
						//System.out.println(exon_name);
						if (count > 10) {
							count = 10;
						}
						String line = "{\"name\":\"" + exon_name + "\",\"n\":" + count + ",\"grp\":" + grp_type + ",\"id\":\"" + exon_name + "\"" + "}";
						if (first) {						
							out.write(line);
							first = false;
						} else {
							out.write("," + line);
						}
						out_exon_annotation.write(exon_name + "\t" + transcript_type + "\n");
					}
					
					HashMap write_once = new HashMap();
					first = true;
					out.write("],\"links\":[");
					itr = links.keySet().iterator();
					while (itr.hasNext()) {
						String link = (String)itr.next();
						String original_exon_name = link.split("\t")[0];
						String target_exon_name = link.split("\t")[1];
						if (!write_once.containsKey(original_exon_name + "\t" + target_exon_name)) {											
							String value = "1";
							if (edge_group.get(original_exon_name).equals("PRINCIPAL") && edge_group.get(target_exon_name).equals("PRINCIPAL")) {
								value = "3";
							}
							if (edge_group.get(original_exon_name).equals("ALTERNATIVE") && edge_group.get(target_exon_name).equals("ALTERNATIVE")) {
								value = "2";
							}
							String line = "{\"source\":\"" + original_exon_name + "\",\"target\":\"" + target_exon_name + "\",\"value\":" + value + "}";
							
							if (first) {						
								out.write(line);
								first = false;
							} else {
								out.write("," + line);
							}
						}
						write_once.put(original_exon_name + "\t" + target_exon_name, "");
						write_once.put(target_exon_name + "\t" + original_exon_name, "");
					}				
				}
				out.write("],\"attributes\":{}}");
				out.close();
			}
			
			out_exon_annotation.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static LinkedList sortbykey(HashMap map) 
    { 
        // TreeMap to store values of HashMap 
        TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(); 
  
        // Copy all data from hashMap into TreeMap 
        sorted.putAll(map); 
  
        LinkedList final_list = new LinkedList();
        // Display the TreeMap which is naturally sorted 
        for (Entry<String, Integer> entry : sorted.entrySet()) {
        	final_list.add(entry.getKey());
            //System.out.println("Key = " + entry.getKey() +  
            //             ", Value = " + entry.getValue());      
        }
        return final_list;
    } 

}
