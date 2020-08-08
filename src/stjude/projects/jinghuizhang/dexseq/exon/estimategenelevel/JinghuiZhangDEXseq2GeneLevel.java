package stjude.projects.jinghuizhang.dexseq.exon.estimategenelevel;

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

import statistics.general.MathTools;

/**
 * Convert the FPKM matrix from DEXseq into a total count for each gene
 * For exon with multiple gene, reference the gff file to figure out which gene they correspond to.
 * @author tshaw
 *
 */
public class JinghuiZhangDEXseq2GeneLevel {


	public static String description() {
		return "Estimate gene level quantification using exon level quant.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[inputExonFPKMMatrix] [GenCode_GTF] [DEXSEQ_GTF] [GeneLevelOutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputExonFPKMMatrix = args[0];
			String gtfFile = args[1];
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			String dexseq_gff = args[2];
			GTFFile dexseq_gtf = new GTFFile();
			dexseq_gtf.initialize(dexseq_gff);
			
			String outputFile = args[3]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\updated_pcgp_fpkm_zero_chemokine_sjid.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String prev_geneID = "";
						
			FileInputStream fstream = new FileInputStream(inputExonFPKMMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			// count the number of samples
			LinkedList[] values = new LinkedList[split_header.length - 7];
			HashMap geneIDs = new HashMap();
			HashMap written_ids = new HashMap();
			out.write("GeneID");
			for (int i = 7; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
				values[i - 7] = new LinkedList();
			}
			out.write("\n");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[1].split("\\|")[0];	
				String geneID = split[0].split(":")[0];
				if (split[2].equals("protein_coding")) {
					// if first encounter
					if (!geneIDs.containsKey(geneID)) {
						// check if previous if not then get median
						if (!prev_geneID.equals("") && !prev_geneID.equals(geneID)) {
							out.write(prev_geneID);
							System.out.println(prev_geneID + "\t" + values[0].size());
							for (int i = 7; i < split_header.length; i++) {
								double median = MathTools.median(values[i - 7]);
								out.write("\t" + median);
							}
							out.write("\n");
							written_ids.put(prev_geneID, prev_geneID);
						}
						for (int i = 7; i < split_header.length; i++) {
							values[i - 7].clear();
						}
					}
					for (int i = 7; i < split_header.length; i++) {
						values[i - 7].add(new Double(split[i]));						
						
					}
					geneIDs.put(geneID, geneID);
					prev_geneID = geneID;
				}									
			}
			in.close();			
			if (!written_ids.containsKey(prev_geneID)) {
				out.write(prev_geneID);
				for (int i = 7; i < split_header.length; i++) {
					double median = MathTools.median(values[i - 7]);
					out.write("\t" + median);
				}
				out.write("\n");
			}
			

			// now iterate through to capture the overlapping genes
			fstream = new FileInputStream(inputExonFPKMMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			
			HashMap exons = new HashMap();
			HashMap overlapping_geneids = new HashMap();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[1].split("\\|")[0];	
				String geneID = split[0].split(":")[0];
				String coord = split[3] + "\t" + split[4] + "\t" + split[5];
				if (split[2].contains("+") && split[2].contains("protein_coding")) {
					if (!geneIDs.containsKey(geneID)) {
						// check if previous if not then get median
						Iterator itr = overlapping_geneids.keySet().iterator();
						while (itr.hasNext()) {
							String id = (String)itr.next();
							out.write(id);
							for (int i = 7; i < split_header.length; i++) {
								LinkedList value = (LinkedList)exons.get(split_header[i] + "\t" + id);
								double median = MathTools.median(value);
								out.write("\t" + median);					
							}
							out.write("\n");
							written_ids.put(id, id);
						}
						overlapping_geneids.clear();
					}
					
					
					// write previous
					if (dexseq_gtf.coord2transcriptid.containsKey(coord)) {
						LinkedList get_ids = (LinkedList)dexseq_gtf.coord2transcriptid.get(coord);
						Iterator itr = get_ids.iterator();
						while (itr.hasNext()) {
							String transcript_ids = (String)itr.next();
							String[] split_transcript_ids = transcript_ids.split("\\+");
							for (String transcript_id: split_transcript_ids) {
								String id = (String)gtf.transcript2gene.get(transcript_id);
								String biotype = (String)gtf.geneid2biotype.get(id);
								if (biotype.equals("protein_coding")) {
									for (int i = 7; i < split_header.length; i++) {
										if (exons.containsKey(split_header[i] + "\t" + id)) {
											LinkedList list = (LinkedList)exons.get(split_header[i] + "\t" + id);
											list.add(new Double(split[i]));
											exons.put(split_header[i] + "\t" + id, list);
										} else {
											LinkedList list = new LinkedList();
											list.add(new Double(split[i]));
											exons.put(split_header[i] + "\t" + id, list);								
										}
									}
									overlapping_geneids.put(id, id);
								}
							}
						}
						geneIDs.put(geneID, geneID);
						prev_geneID = geneID;
					}
				}
			}
			in.close();
			
			Iterator itr = overlapping_geneids.keySet().iterator();
			while (itr.hasNext()) {
				String id = (String)itr.next();
				if (!written_ids.containsKey(id)) {
					out.write(id);
					for (int i = 7; i < split_header.length; i++) {
						LinkedList value = (LinkedList)exons.get(split_header[i] + "\t" + id);
						double median = MathTools.median(value);
						out.write("\t" + median);					
					}
					out.write("\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
