package rnaseq.quantification.kallisto;

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

public class EvaluateExonExpressionKallisto {


	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Generate exon expression based on kallisto";
	}
	public static String parameter_info() {
		return "[gtf_file] [matrixFile] [geneName] [chr] [start] [end] [outputFileMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String gtf_file = args[0];
			String matrixFile = args[1];
			String geneName = args[2];
			String chr = args[3];
			String start = args[4];
			String end = args[5];
			String outputFileMatrix = args[6];
			
			HashMap gene_transcripts_with_exon = new HashMap();
			HashMap gene_transcripts_without_exon = new HashMap();
			HashMap all_transcript = new HashMap();
			
			FileWriter fwriter = new FileWriter(outputFileMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtf_file);
			LinkedList geneIDs = (LinkedList)gtf.geneName2geneID.get(geneName);
			Iterator itr = geneIDs.iterator();					
			while (itr.hasNext()) {
				String geneID = (String)itr.next();
				System.out.println(geneID);
				if (gtf.gene2transcript.containsKey(geneID)) {
					String transcripts = (String)gtf.gene2transcript.get(geneID);	
					//String new_exon = "CHR" + chr + ":" + start + "-" + end + ":" + direct + ":" + exon_number;
					for (String transcript: transcripts.split(",")) {
						System.out.println(transcript);
						all_transcript.put(transcript, transcript);
						if (gtf.transcript2exon.containsKey(transcript)) {
							String exons = (String)gtf.transcript2exon.get(transcript);
							for (String exon: exons.split(",")) {
								String[] exon_split = exon.split(":");
								String exon_chr = exon_split[0];
								//System.out.println(exon);
								exon_chr = exon_chr.replaceAll("CHR", "chr");
								exon_chr = exon_chr.replaceAll("chrchr", "chr");
								String exon_start = exon_split[1].split("-")[0];
								String exon_end = exon_split[1].split("-")[1];
								
								if (exon_chr.equals(chr) && exon_start.equals(start) && exon_end.equals(end)) {
									gene_transcripts_with_exon.put(transcript, transcript);
								} else {
									gene_transcripts_without_exon.put(transcript, transcript);
								}
							}
						}
					}
				}
			}
			
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\t" + "ContainsExon\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (all_transcript.containsKey(split[0].split("\\.")[0])) {
					boolean present = false;
					if (gene_transcripts_with_exon.containsKey(split[0].split("\\.")[0])) {
						present = true;
					}
					out.write(str + "\t" + present + "\n");
				}

			}
			in.close();
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
