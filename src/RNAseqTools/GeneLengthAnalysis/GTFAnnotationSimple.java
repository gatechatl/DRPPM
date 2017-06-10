package RNAseqTools.GeneLengthAnalysis;

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
 * Generate row for gene annotation
 * @author tshaw
 *
 */
public class GTFAnnotationSimple {

	public static String parameter_info() {
		return "[Ensembl gtfFile] [OutputFile]";
	}
	public static void execute(String[] args) {
		
		
		try {
			HashMap transcript_length = new HashMap();
			HashMap transcript2GeneName = new HashMap();
			HashMap transcript2GeneID = new HashMap();
			HashMap geneName2transcript = new HashMap();
			HashMap geneID2transcript = new HashMap();
			
			String inputFile = args[0];
			String transcript_output = args[1];
			
			
			HashMap transcript_start = new HashMap();
			HashMap transcript_end = new HashMap();
			HashMap chrLoc = new HashMap();
			HashMap geneStart = new HashMap();
			HashMap geneEnd = new HashMap();
			
			FileWriter fwriter_transcript = new FileWriter(transcript_output);
			BufferedWriter out_transcript = new BufferedWriter(fwriter_transcript);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				String geneType = split[2];				
				int length = new Integer(split[4]) - new Integer(split[3]);
				String meta = split[8];
				String gene_id = GTFFile.grabMeta(meta, "gene_id");
				String gene_name = GTFFile.grabMeta(meta, "gene_name");				
				chrLoc.put(gene_name, split[0]);
				if (geneStart.containsKey(gene_name)) {
					int start = (Integer)geneStart.get(gene_name);
					if (start > new Integer(split[3])) {
						start = new Integer(split[3]);
					}
					geneStart.put(gene_name,  start);					
				} else {
					geneStart.put(gene_name,  new Integer(split[3]));
				}
				if (geneEnd.containsKey(gene_name)) {
					int end = (Integer)geneEnd.get(gene_name);
					if (end < new Integer(split[4])) {
						end = new Integer(split[4]);
					}
					geneEnd.put(gene_name, end);					
				} else {
					geneEnd.put(gene_name, new Integer(split[4]));
				}
				
				String transcript_id = GTFFile.grabMeta(meta, "transcript_id");
								
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
				
								
				
			}
			in.close();
			
			out_transcript.write("GeneID\tChr\tStart\tEnd\n");
			Iterator itr = geneName2transcript.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();				
				String chr = (String)chrLoc.get(geneName);
				int start = (Integer)geneStart.get(geneName);
				int end = (Integer)geneEnd.get(geneName);
				out_transcript.write(geneName + "\t" + chr + "\t" + start + "\t" + end + "\n");
			}
			out_transcript.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
