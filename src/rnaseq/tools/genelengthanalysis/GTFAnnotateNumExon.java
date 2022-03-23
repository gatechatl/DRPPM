package rnaseq.tools.genelengthanalysis;


import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Summarize GeneLength
 * Calculate the gene length based on ensembl gtf file
 * @author tshaw
 *
 */
public class GTFAnnotateNumExon {

	public static String parameter_info() {
		return "[Ensembl gtfFile] [transcript Output] [geneID Output] [geneName Output] [gene_name_exon_output] [exon_length_output]";
	}
	public static void execute(String[] args) {
		
		
		try {
			HashMap transcript_length = new HashMap();
			HashMap transcript2GeneName = new HashMap();
			HashMap transcript2GeneID = new HashMap();
			HashMap geneName2transcript = new HashMap();
			HashMap geneID2transcript = new HashMap();
			HashMap exonInGene = new HashMap();
			HashMap exon_length = new HashMap();
			
			String inputFile = args[0];
			String transcript_output = args[1];
			String gene_id_output = args[2];
			String gene_name_output = args[3];
			String gene_name_exon_output = args[4];
			String exon_length_output = args[5];
			
			int exon_id = 1;
			FileWriter fwriter_transcript = new FileWriter(transcript_output);
			BufferedWriter out_transcript = new BufferedWriter(fwriter_transcript);
			
			FileWriter fwriter_geneID = new FileWriter(gene_id_output);
			BufferedWriter out_geneID = new BufferedWriter(fwriter_geneID);
			
			FileWriter fwriter_geneName = new FileWriter(gene_name_output);
			BufferedWriter out_geneName = new BufferedWriter(fwriter_geneName);
			
			FileWriter fwriter_geneName_exon = new FileWriter(gene_name_exon_output);
			BufferedWriter out_geneName_exon = new BufferedWriter(fwriter_geneName_exon);
			
			FileWriter fwriter_exon_len = new FileWriter(exon_length_output);
			BufferedWriter out_exon_len = new BufferedWriter(fwriter_exon_len);
			
			HashMap geneName2biotype = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				if (!str.substring(0, 2).equals("##") && !str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String geneType = split[2];
					int length = new Integer(split[4]) - new Integer(split[3]);
					String meta = split[8];
					String gene_id = GTFFile.grabMeta(meta, "gene_id");
					String gene_name = GTFFile.grabMeta(meta, "gene_name");
					String transcript_id = GTFFile.grabMeta(meta, "transcript_id");
					

					//String geneID = ((String)GTFFile.transcript_clean2gene.get(split[0].replaceAll("\"",  "").split("\\.")[0]));
					String biotype = GTFFile.grabMeta(meta, "gene_type");
					if (biotype.equals("")) {
						biotype = GTFFile.grabMeta(meta, "biotype");
					}
					geneName2biotype.put(gene_name, biotype);
					
					transcript2GeneName.put(transcript_id, gene_name);
					transcript2GeneID.put(transcript_id, gene_id);
					if (geneName2transcript.containsKey(gene_name)) {
						LinkedList list = (LinkedList)geneName2transcript.get(gene_name);
						list.add(transcript_id);
						geneName2transcript.put(gene_name, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(transcript_id);
						geneName2transcript.put(gene_name, list);
					}
					
					if (geneID2transcript.containsKey(gene_id)) {
						LinkedList list = (LinkedList)geneID2transcript.get(gene_id);
						list.add(transcript_id);
						geneID2transcript.put(gene_id, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(transcript_id);
						geneID2transcript.put(gene_id, list);
					}
					
					if (geneType.equals("exon")) {
						exon_length.put(transcript_id + "_" + exon_id, length);
						exon_id++;
						
						if (transcript_length.containsKey(transcript_id)) {
							int len = (Integer)transcript_length.get(transcript_id);
							len = len + length;
							transcript_length.put(transcript_id, len);
						} else {
							transcript_length.put(transcript_id, length);
						}
						
						if (exonInGene.containsKey(gene_name)) {
							int count = (Integer)exonInGene.get(gene_name);
							count = count + 1;
							exonInGene.put(gene_name, count);
						} else {
							exonInGene.put(gene_name, 1);
						}
						
						
					}				
				}
			}
			in.close();
			
			out_transcript.write("TranscriptID\tLength\n");
			Iterator itr = transcript_length.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				int len = (Integer)transcript_length.get(name);
				out_transcript.write(name + "\t" + len + "\n");
			}
			out_transcript.close();
			
			out_geneID.write("GeneID\tAvgLength\n");
			itr = geneID2transcript.keySet().iterator();
			while (itr.hasNext()) {
				String geneID = (String)itr.next();
				LinkedList list = (LinkedList)geneID2transcript.get(geneID);
				int total = 0;
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String transcript_id = (String)itr2.next();
					if (transcript_length.containsKey(transcript_id)) {
						total += (Integer)transcript_length.get(transcript_id);
					}
				}
				out_geneID.write(geneID + "\t" + (total / list.size()) + "\n");
			}
			out_geneID.close();
			
			out_geneName.write("GeneID\tAvgLength\n");
			itr = geneName2transcript.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				LinkedList list = (LinkedList)geneName2transcript.get(geneName);
				int total = 0;
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String transcript_id = (String)itr2.next();
					if (transcript_length.containsKey(transcript_id)) {
						total += (Integer)transcript_length.get(transcript_id);
					}
				}
				out_geneName.write(geneName + "\t" + (total / list.size()) + "\n");
			}
			out_geneName.close();
			
			
			out_geneName_exon.write("GeneName\tNumExon\n");
			itr = exonInGene.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				int total = (Integer)exonInGene.get(geneName);
				if (exonInGene.containsKey(geneName)) {
					out_geneName_exon.write(geneName + "\t" + total + "\t" + geneName2biotype.get(geneName) + "\n");
				}
			}
			out_geneName_exon.close();
			
			out_exon_len.write("ExonID\tLength\n");
			itr = exon_length.keySet().iterator();
			while (itr.hasNext()) {
				String exon_id_key = (String)itr.next();
				if (exon_length.containsKey(exon_id_key)) {
					int length = (Integer)exon_length.get(exon_id_key);					
					out_exon_len.write(exon_id_key + "\t" + length + "\n");
				}
			}
			out_exon_len.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
