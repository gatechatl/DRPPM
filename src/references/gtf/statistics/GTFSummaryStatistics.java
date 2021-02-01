package references.gtf.statistics;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GTFSummaryStatistics {

	public static String type() {
		return "MATS";
	}
	public static String description() {
		return "Print out basic stats like number of genes, transcripts, genes with exon.";
	}
	public static String parameter_info() {
		return "[gtfFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap total_gene = new HashMap();
			String gtf_file = args[0];
			
			
			HashMap exon_count = new HashMap();
			HashMap max_count = new HashMap();
			HashMap transcript2geneName = new HashMap();
			HashMap geneName2transcript = new HashMap();
			HashMap all_gene_name = new HashMap();
			HashMap transcript2gene_id = new HashMap();
			HashMap gene_id2transcript = new HashMap();
			
			HashMap total_transcript = new HashMap();
			FileInputStream fstream = new FileInputStream(gtf_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String entry_type = split[2];
					String gene_name = GTFFile.grabMeta(split[8].trim(), "gene_name").replaceAll("\"", "");
					String gene_id = GTFFile.grabMeta(split[8].trim(), "gene_id").replaceAll("\"", "");
					String transcript_id = GTFFile.grabMeta(split[8].trim(), "transcript_id").replaceAll("\"", "");
					all_gene_name.put(gene_name, gene_name);
					total_transcript.put(transcript_id, transcript_id);
					if (entry_type.equals("exon")) {
						transcript2geneName.put(transcript_id, gene_name);
						if (geneName2transcript.containsKey(gene_name)) {
							LinkedList list = (LinkedList)geneName2transcript.get(gene_name);
							if (!list.contains(transcript_id)) {
								list.add(transcript_id);
								geneName2transcript.put(gene_name, list);
							}
						} else {
							LinkedList list = new LinkedList();
							list.add(transcript_id);
							geneName2transcript.put(gene_name, list);
						}
						
						if (exon_count.containsKey(transcript_id)) {
							int count = (Integer)exon_count.get(transcript_id);
							count = count + 1;
							exon_count.put(transcript_id, count);
						} else {
							exon_count.put(transcript_id, 1);
						}
					}
				}
			}
			in.close();
			
			HashMap gene_name_exon_count = new HashMap();
			
			Iterator itr = geneName2transcript.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				int max = 0;
				
				LinkedList transcripts = (LinkedList)geneName2transcript.get(geneName);
				Iterator itr2 = transcripts.iterator();
				while (itr2.hasNext()) {
					String transcript = (String)itr2.next();
					int count = (Integer)exon_count.get(transcript);
					if (count > max ) {
						max = count;
					}
				}
				gene_name_exon_count.put(geneName, max);
			}
			
			System.out.println("total_transcript: " + total_transcript.size());
			System.out.println("transcript with exons: " + exon_count.size());
			System.out.println("all_gene_name: " + all_gene_name.size());
			System.out.println("Total genes with exon: " + gene_name_exon_count.size());			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
