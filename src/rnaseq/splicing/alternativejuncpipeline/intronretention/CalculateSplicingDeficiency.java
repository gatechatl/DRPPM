package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Calculate the splicing deficiency based on bam file
 * @author tshaw
 *
 */
public class CalculateSplicingDeficiency {

	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap all_genes = new HashMap();
			HashMap intron_total = new HashMap();
			HashMap exon_total = new HashMap();
			
			String intronBedFile = args[0]; // input file is the intron bed file
			String exonBedFile = args[1]; // input file is the exon bed file
			String intron_CoverageBed = args[2];
			String exon_CoverageBedFile = args[3];
			String outputFile = args[4];
			
			FileInputStream fstream = new FileInputStream(intronBedFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String start = split[1];
				String end = split[2];
				int distance = Math.abs(new Integer(end) - new Integer(start)) + 1;
				String gene_name = split[3].split("\\.")[0].split("_exon")[0];
				all_genes.put(gene_name, gene_name);
				if (intron_total.containsKey(gene_name)) {
					int total = (Integer)intron_total.get(gene_name);
					total += distance;
					intron_total.put(gene_name, total);										     
				} else {
					intron_total.put(gene_name, distance);
				}
			}
			in.close();

			
			fstream = new FileInputStream(exonBedFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String start = split[1];
				String end = split[2];
				int distance = Math.abs(new Integer(end) - new Integer(start)) + 1;
				String gene_name = split[3].split("\\.")[0].split("_exon")[0];
				all_genes.put(gene_name, gene_name);
				
				if (exon_total.containsKey(gene_name)) {
					int total = (Integer)exon_total.get(gene_name);
					total += distance;
					exon_total.put(gene_name, total);										       
				} else {
					exon_total.put(gene_name, distance);
				}
				
			}
			in.close();
			
			HashMap num_intronic_reads = new HashMap();			
			fstream = new FileInputStream(intron_CoverageBed);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene_name = split[3].split("\\.")[0].split("_exon")[0];
				int reads = new Integer(split[6]);
				if (num_intronic_reads.containsKey(gene_name)) {
					int num = (Integer)num_intronic_reads.get(gene_name);
					reads += num;
					num_intronic_reads.put(gene_name, reads);
				} else {
					num_intronic_reads.put(gene_name, reads);
				}
			}
			in.close();
			
			HashMap num_exonic_reads = new HashMap();			
			fstream = new FileInputStream(exon_CoverageBedFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene_name = split[3].split("\\.")[0].split("_exon")[0];
				int reads = new Integer(split[6]);
				if (num_exonic_reads.containsKey(gene_name)) {
					int num = (Integer)num_exonic_reads.get(gene_name);
					reads += num;
					num_exonic_reads.put(gene_name, reads);
				} else {
					num_exonic_reads.put(gene_name, reads);
				}
			}
			in.close();
						
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneID\tSplicingDeficiency\tNumIntronReads\tIntronLength\tNumExonReads\tExonLength\n");
			Iterator itr = all_genes.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				
				if (num_exonic_reads.containsKey(key) && num_intronic_reads.containsKey(key) && exon_total.containsKey(key) && intron_total.containsKey(key)) {
					int no_intron_reads = (Integer)num_intronic_reads.get(key);
					int no_exon_reads = (Integer)num_exonic_reads.get(key);
					int intron_length = (Integer)intron_total.get(key);
					int exon_length = (Integer)exon_total.get(key);
					double sd = (new Double(no_intron_reads) / intron_length) / (new Double(no_exon_reads) / exon_length);
					if (no_intron_reads >= 5 && no_exon_reads >= 5) {
						out.write(key + "\t" + sd + "\t" + no_intron_reads + "\t" + intron_length + "\t" + no_exon_reads + "\t" + exon_length + "\n");
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
