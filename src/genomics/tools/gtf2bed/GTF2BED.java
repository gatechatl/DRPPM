package genomics.tools.gtf2bed;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import misc.CommandLine;

/**
 * Convert the gtf file to bed similar to the ucsc
 * Generates exon, intron, utr, cds
 * Use the gene_id feature
 * 
 * @author tshaw
 *
 */
public class GTF2BED {


	public static String type() {
		return "GTF";
	}
	public static String description() {
		return "Generate bed files from gtf";
	}
	public static String parameter_info() {
		return "[inputFile] [outputPrefix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputPrefix = args[1];
			
			FileWriter fwriter_gene = new FileWriter(outputPrefix + ".gene_extracted.bed");
			BufferedWriter out_gene = new BufferedWriter(fwriter_gene);
			
			FileWriter fwriter_gene_version2 = new FileWriter(outputPrefix + ".gene.bed");
			BufferedWriter out_gene_version2 = new BufferedWriter(fwriter_gene_version2);
			
			FileWriter fwriter_cds = new FileWriter(outputPrefix + ".cds.bed");
			BufferedWriter out_cds = new BufferedWriter(fwriter_cds);

			FileWriter fwriter_exon = new FileWriter(outputPrefix + ".exon.bed");
			BufferedWriter out_exon = new BufferedWriter(fwriter_exon);
			
			FileWriter fwriter_transcript = new FileWriter(outputPrefix + ".transcript_extracted.bed");
			BufferedWriter out_transcript = new BufferedWriter(fwriter_transcript);
			

			FileWriter fwriter_transcript_version2 = new FileWriter(outputPrefix + ".transcript.bed");
			BufferedWriter out_transcript_version2 = new BufferedWriter(fwriter_transcript_version2);
			
			
			FileWriter fwriter_first_exon = new FileWriter(outputPrefix + ".firstexon.bed");
			BufferedWriter out_first_exon = new BufferedWriter(fwriter_first_exon);
			
			FileWriter fwriter_utr = new FileWriter(outputPrefix + ".utr.bed");
			BufferedWriter out_utr = new BufferedWriter(fwriter_utr);
			
			FileWriter fwriter_ref = new FileWriter(outputPrefix + ".reference.txt");
			BufferedWriter out_ref = new BufferedWriter(fwriter_ref);
			
			HashMap map_transcript = new HashMap();
			HashMap map_biotype = new HashMap();
			HashMap map_transcript_name = new HashMap();
			HashMap map_geneName = new HashMap();
			HashMap map_transcript2geneid = new HashMap();
			HashMap unique_transcript_entries = new HashMap();
			String prev_gene_id = "";
			int prev_start = 999999999;
			int prev_end = -1;
			String prev_chr = "";
			String prev_direction = "";
			
			String prev_transcript_id = "";
			int prev_start_transcript_id = 999999999;
			int prev_end_transcript_id = -1;
			String prev_chr_transcript_id = "";
			String prev_direction_transcript_id = "";
			
			String intron_file_preraw = outputPrefix + ".intron_preraw.bed";
			String intron_file = outputPrefix + ".intron.bed";
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].substring(0, 1).equals("#")) {
					String chr = split[0];
					String start = split[3];
					String end = split[4];
					String direction = split[6];
					String gene_id = GTFFile.grabMeta(split[8], "gene_id");
					String transcript = GTFFile.grabMeta(split[8], "transcript_id");
					String transcript_id = transcript;
					
					map_transcript2geneid.put(transcript_id, gene_id);
					
					String biotype = GTFFile.grabMeta(split[8], "gene_biotype");
					String transcript_name = GTFFile.grabMeta(split[8], "transcript_name");
					String gene_name = GTFFile.grabMeta(split[8], "gene_name");
					if (map_transcript.containsKey(gene_id)) {
						String prev_transcript = (String)map_transcript.get(gene_id);
						if (!prev_transcript.contains(transcript)) {
							prev_transcript += "," + transcript;
							map_transcript.put(gene_id, prev_transcript);
						}
					} else {
						map_transcript.put(gene_id, transcript);
					}
					
					if (map_transcript_name.containsKey(gene_id)) {
						String prev_transcript_name = (String)map_transcript_name.get(gene_id);
						if (!prev_transcript_name.contains(transcript)) {
							prev_transcript_name += "," + transcript;
							map_transcript_name.put(gene_id, prev_transcript_name);
						}
					} else {
						map_transcript_name.put(gene_id, transcript);
					}
					
					map_geneName.put(gene_id, gene_name);
					map_biotype.put(gene_id, biotype);
					
					String exon_num = "exon_" + GTFFile.grabMeta(split[8], "exon_number");
					if (split[2].equals("gene")) {
						out_gene.write(chr + "\t" + start + "\t" + end + "\t" + gene_id + "\t0\t" + direction + "\n");  
					}
					if (split[2].equals("transcript")) {
						out_transcript.write(chr + "\t" + start + "\t" + end + "\t" + gene_id + "\t0\t" + direction + "\n");  
					}
					if (split[2].equals("CDS")) {
						out_cds.write(chr + "\t" + start + "\t" + end + "\t" + gene_id + "\t0\t" + direction + "\n");  
					}
					if (split[2].equals("exon")) {
						out_exon.write(chr + "\t" + start + "\t" + end + "\t" + gene_id + "_" + exon_num + "\t0\t" + direction + "\n");  
					}
					if (split[2].equals("UTR")) {
						out_utr.write(chr + "\t" + start + "\t" + end + "\t" + gene_id + "_" + exon_num + "\t0\t" + direction + "\n");  
					}
					if (split[2].equals("exon")) {
						if (exon_num.equals("exon_1")) {
							out_first_exon.write(chr + "\t" + start + "\t" + end + "\t" + gene_id + "_" + exon_num + "\t0\t" + direction + "\n");
						}
					}
					
					if (!prev_gene_id.equals("") && !prev_gene_id.equals(gene_id)) {
						String entry = prev_chr + "\t" + prev_start + "\t" + prev_end + "\t" + prev_gene_id + "\t" + 0 + "\t" + prev_direction + "\n";
						out_gene_version2.write(entry);										
						prev_start = 999999999;
						prev_end = -1;
					}
					if (prev_start > new Integer(start)) {
						prev_start = new Integer(start);
					}				
					if (prev_end < new Integer(end)) {
						prev_end = new Integer(end);
					}
					prev_gene_id = gene_id;
					prev_chr = chr;
					prev_direction = direction;
					
					if (!prev_transcript_id.equals("") && !prev_transcript_id.equals(transcript_id)) {
						String convert_geneid = (String)map_transcript2geneid.get(prev_transcript_id);
						String entry = prev_chr_transcript_id + "\t" + prev_start_transcript_id + "\t" + prev_end_transcript_id + "\t" + convert_geneid + "\t" + 0 + "\t" + prev_direction_transcript_id + "\n";
						if (!unique_transcript_entries.containsKey(entry)) {
							out_transcript_version2.write(entry);
							unique_transcript_entries.put(entry, entry);
						}
						
						prev_start_transcript_id = 999999999;
						prev_end_transcript_id = -1;
					}
					if (prev_start_transcript_id > new Integer(start)) {
						prev_start_transcript_id = new Integer(start);
					}				
					if (prev_end_transcript_id < new Integer(end)) {
						prev_end_transcript_id = new Integer(end);
					}
					prev_transcript_id = transcript_id;
					prev_chr_transcript_id = chr;
					prev_direction_transcript_id = direction;
				}
			}
			in.close();
			
			
			out_gene_version2.write(prev_chr + "\t" + prev_start + "\t" + prev_end + "\t" + prev_gene_id + "\t" + 0 + "\t" + prev_direction + "\n");
			
			String convert_geneid = (String)map_transcript2geneid.get(prev_transcript_id);
			String entry = prev_chr_transcript_id + "\t" + prev_start_transcript_id + "\t" + prev_end_transcript_id + "\t" + convert_geneid + "\t" + 0 + "\t" + prev_direction_transcript_id + "\n";
			if (!unique_transcript_entries.containsKey(entry)) {
				out_transcript_version2.write(entry);
				unique_transcript_entries.put(entry, entry);
			}
			
			prev_start = 999999999;
			prev_end = -1;
		
			
			out_gene.close();
			out_cds.close();
			out_exon.close();
			out_first_exon.close();
			out_transcript.close();
			out_utr.close();
			out_gene_version2.close();
			out_transcript_version2.close();
			
			Iterator itr = map_geneName.keySet().iterator();
			while (itr.hasNext()) {
				String gene_id = (String)itr.next();
				String transcript_id = (String)map_transcript.get(gene_id);
				String transcript_name = (String)map_transcript_name.get(gene_id);
				String biotype = (String)map_biotype.get(gene_id);
				String gene_name = (String)map_geneName.get(gene_id);
				out_ref.write(gene_id + "\t" + transcript_id + "\t" + transcript_name + "\t" + biotype + "\t" + gene_name + "\n");
			}
			out_ref.close();
	        File f = new File(outputPrefix + ".transcript.bed");
	        long transcript_fileSize = f.length();
	        if (transcript_fileSize > 1000) {
				String bedtools_subtract = "bedtools subtract -a " + outputPrefix + ".transcript.bed" + " -b " + outputPrefix + ".exon.bed -s > " + intron_file_preraw;
				CommandLine.executeCommand(bedtools_subtract);
	        } else {
				String bedtools_subtract = "bedtools subtract -a " + outputPrefix + ".gene.bed" + " -b " + outputPrefix + ".exon.bed -s > " + intron_file_preraw;
				CommandLine.executeCommand(bedtools_subtract);	        	
	        }
			String fix_intron_index = "awk -v s=1 '{if (($3 - 1) > ($2 + 1)) {print $1, ($2+1), ($3-1), $4, $5, $6}}' OFS='\t' " + intron_file_preraw + " > " + intron_file;
	        CommandLine.executeCommand(fix_intron_index);
	        File f2 = new File(intron_file_preraw);
	        if (f2.exists()) {
	        	f2.delete();
	        }
	        
	        String sort_exon_bed_script = "sort -k 1,1 -k2,2n " + outputPrefix + ".exon.bed > " + outputPrefix + ".exon.sorted.bed";
	        CommandLine.executeCommand(sort_exon_bed_script);
	        String sort_intron_bed_script = "sort -k 1,1 -k2,2n " + outputPrefix + ".gene.bed > " + outputPrefix + ".gene.sorted.bed";
	        CommandLine.executeCommand(sort_intron_bed_script);
	        String sort_gene_bed_script = "sort -k 1,1 -k2,2n " + outputPrefix + ".intron.bed > " + outputPrefix + ".intron.sorted.bed";
	        CommandLine.executeCommand(sort_gene_bed_script);
	        

	        clean_files(outputPrefix + ".exon.sorted.bed" , outputPrefix + ".exon.bed");
	        String mv_sort_exon_bed_script = "cp " + outputPrefix + ".exon.bed " + outputPrefix + ".exon.sorted.bed";
	        CommandLine.executeCommand(mv_sort_exon_bed_script);
	        
	        // remove ERCC here
	        clean_files(outputPrefix + ".intron.sorted.bed" , outputPrefix + ".intron.bed");
	        String mv_sort_intron_bed_script = "cp " + outputPrefix + ".intron.bed " + outputPrefix + ".intron.sorted.bed";
	        CommandLine.executeCommand(mv_sort_intron_bed_script);
	        
	        clean_files(outputPrefix + ".gene.sorted.bed" , outputPrefix + ".gene.bed");	        
	        //String mv_sort_gene_bed_script = "cp " + outputPrefix + ".gene.sorted.bed " + outputPrefix + ".gene.bed";
	        String mv_sort_gene_bed_script = "cp " + outputPrefix + ".gene.bed " + outputPrefix + ".gene.sorted.bed";
	        CommandLine.executeCommand(mv_sort_gene_bed_script);

	        
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void clean_files(String input, String output) {

		try {
			FileWriter fwriter_intron_bed = new FileWriter(output);
			BufferedWriter out_intron_bed = new BufferedWriter(fwriter_intron_bed);
			
			FileInputStream fstream = new FileInputStream(input);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));	
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length == 6) {
					out_intron_bed.write(str + "\n");
				}
			}
			in.close();
			out_intron_bed.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
