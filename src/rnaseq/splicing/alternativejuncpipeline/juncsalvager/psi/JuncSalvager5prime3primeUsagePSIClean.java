package rnaseq.splicing.alternativejuncpipeline.juncsalvager.psi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JuncSalvager5prime3primeUsagePSIClean {
	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String description() {
		return "Calculate the psi value for 3' and 5' alternative splice site STAR SJ file.\n";
	}
	public static String parameter_info() {
		return "[inputSTARSJ] [gtfFile] [outputFile_SpliceIn/Out] [default uniq read; set true if multi-mapped-read]";
	}
	public static void main(String[] args) {
		int start = 53019384;
		int index = start / 1000000;
		System.out.println(index * 1000000);
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputSTARSJ = args[0];
			String GTFFile = args[1];
			int buffer = new Integer(args[2]);
			String outputFile = args[3];
			String outputFile2 = args[4];
			boolean include_multi_mapped_false = false;
			if (args.length > 5) {
				include_multi_mapped_false = new Boolean(args[5]);
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("ExonID\tpsi\n");
			
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			out2.write("ExonID\tpsi\n");
			
			HashMap exon_left = new HashMap();
			HashMap exon_right = new HashMap();
			HashMap exon_matches = new HashMap();
			HashMap exon_direction = new HashMap();
			
			HashMap geneSymbol = new HashMap();
			HashMap geneTranscript = new HashMap();
			
			FileInputStream fstream = new FileInputStream(GTFFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!str.substring(0, 1).equals("#")) {
					if (split[2].equals("exon")) {
						String chr = split[0];
						int start = new Integer(split[3]);
						int end = new Integer(split[4]);
						String direction = split[6];
					
						String geneID = grabMeta(split[8], "gene_id");
						String transcriptID = grabMeta(split[8], "transcript_id");
						String gene_name = grabMeta(split[8], "gene_name");
						
						exon_direction.put(chr + "\t" + start + "\t" + end + "\t" + direction, "");						
						
						geneSymbol.put(chr + "\t" + start + "\t" + end, gene_name);
						geneTranscript.put(chr + "\t" + start + "\t" + end, transcriptID);
						
						exon_left.put(chr + "\t" + start, 0);
						exon_right.put(chr + "\t" + end, 0);
						
						if (exon_matches.containsKey(chr + "\t" + start)) {
							LinkedList list = (LinkedList)exon_matches.get(chr + "\t" + start);
							if (!list.contains(chr + "\t" + end)) {
								list.add(chr + "\t" + end);
							}
							exon_matches.put(chr + "\t" + start, list);							
						} else {
							LinkedList list = new LinkedList();
							list.add(chr + "\t" + end);
							exon_matches.put(chr + "\t" + start, list);
						}
						
						if (exon_matches.containsKey(chr + "\t" + end)) {
							LinkedList list = (LinkedList)exon_matches.get(chr + "\t" + end);
							if (!list.contains(chr + "\t" + start)) {
								list.add(chr + "\t" + start);
							}
							exon_matches.put(chr + "\t" + end, list);							
						} else {
							LinkedList list = new LinkedList();
							list.add(chr + "\t" + start);
							exon_matches.put(chr + "\t" + end, list);
						}
						
					}			
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputSTARSJ);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");

				String chr = split[0];
				int intron_left = new Integer(split[1]) - 1;
				int intron_right = new Integer(split[2]) + 1;
				int uniq_read = new Integer(split[6]);
				int multi_read = new Integer(split[7]);
				int total_reads = uniq_read + multi_read;
				
				for (int i = 0; i <= buffer; i++) {
					if (exon_left.containsKey(chr + "\t" + (intron_right + i))) {
						int prev_read = (Integer)exon_left.get(chr + "\t" + (intron_right + i));
						if (include_multi_mapped_false) {
							prev_read += total_reads;
						} else {
							prev_read += uniq_read;
						}
						exon_left.put(chr + "\t" + (intron_right + i), prev_read);
					}
				}
				for (int i = 0; i <= buffer; i++) {
					if (exon_right.containsKey(chr + "\t" + (intron_left - i))) {
						int prev_read = (Integer)exon_right.get(chr + "\t" + (intron_left - i));
						if (include_multi_mapped_false) {
							prev_read += total_reads;
						} else {
							prev_read += uniq_read;
						}
						exon_right.put(chr + "\t" + (intron_left - i), prev_read);
					}
				}				
			}
			in.close();
			
			Iterator itr = exon_direction.keySet().iterator();
			while (itr.hasNext()) {
				String coord = (String)itr.next();				
				
				String[] split_coord = coord.split("\t");
				String chr = split_coord[0];
				String start = split_coord[1];
				String end = split_coord[2];
				String direction = split_coord[3];
			
				String gene = (String)geneSymbol.get(chr + "\t" + start + "\t" + end);
				int orig_read_5prime = 0;
				int alt_read_5prime = 0;
				
				int orig_read_3prime = 0;
				int alt_read_3prime = 0;
				
				if (direction.equals("+")) {

					
					if (exon_matches.containsKey(chr + "\t" + end)) {
						LinkedList list = (LinkedList)exon_matches.get(chr + "\t" + end);
						Iterator itr2 = list.iterator();
						while (itr2.hasNext()) {
							String chr_start = (String)itr2.next();
							String left_chr = chr_start.split("\t")[0];
							String left_start = chr_start.split("\t")[1];
							int right_read = 0;
							if (exon_left.containsKey(left_chr + "\t" + left_start)) {
								right_read = (Integer)exon_left.get(left_chr + "\t" + left_start);
								if (left_chr.equals(chr) && left_start.equals(start)) {
									orig_read_5prime = right_read;
								} else {
									alt_read_5prime += right_read;
								}
							}
						}
					}
					
					if (exon_matches.containsKey(chr + "\t" + start)) {
						LinkedList list = (LinkedList)exon_matches.get(chr + "\t" + start);
						Iterator itr2 = list.iterator();
						while (itr2.hasNext()) {
							String chr_start = (String)itr2.next();
							String left_chr = chr_start.split("\t")[0];
							String left_end = chr_start.split("\t")[1];
							int left_read = 0;
							if (exon_right.containsKey(left_chr + "\t" + left_end)) {
								left_read = (Integer)exon_right.get(left_chr + "\t" + left_end);
								if (left_chr.equals(chr) && left_end.equals(end)) {
									orig_read_3prime = left_read;
								} else {
									alt_read_3prime += left_read;
								}
							}
						}
					}										
					
				} else {
					
					if (exon_matches.containsKey(chr + "\t" + end)) {
						LinkedList list = (LinkedList)exon_matches.get(chr + "\t" + end);
						Iterator itr2 = list.iterator();
						while (itr2.hasNext()) {
							String chr_start = (String)itr2.next();
							String left_chr = chr_start.split("\t")[0];
							String left_start = chr_start.split("\t")[1];
							int right_read = 0;
							if (exon_left.containsKey(left_chr + "\t" + left_start)) {
								right_read = (Integer)exon_left.get(left_chr + "\t" + left_start);
								if (left_chr.equals(chr) && left_start.equals(start)) {
									orig_read_3prime = right_read;
								} else {
									alt_read_3prime += right_read;
								}
							}
						}
					}
					
					if (exon_matches.containsKey(chr + "\t" + start)) {
						LinkedList list = (LinkedList)exon_matches.get(chr + "\t" + start);
						Iterator itr2 = list.iterator();
						while (itr2.hasNext()) {
							String chr_start = (String)itr2.next();
							String left_chr = chr_start.split("\t")[0];
							String left_end = chr_start.split("\t")[1];
							int left_read = 0;
							if (exon_right.containsKey(left_chr + "\t" + left_end)) {
								left_read = (Integer)exon_right.get(left_chr + "\t" + left_end);
								if (left_chr.equals(chr) && left_end.equals(end)) {
									orig_read_5prime = left_read;
								} else {
									alt_read_5prime += left_read;
								}
							}
						}
					}	
				}
				
				double PSI_3_prime_alt_spice = new Double(orig_read_3prime) / new Double(alt_read_3prime + orig_read_3prime);
				double PSI_5_prime_alt_spice = new Double(orig_read_5prime) / new Double(alt_read_5prime + orig_read_5prime);
				
				//out.write(chr + "\t" + start + "\t" + end + "\t" + direction + "\t" + PSI_5_prime_alt_spice + "\t" + PSI_3_prime_alt_spice + "\t" + orig_read_5prime + "\t" + alt_read_5prime + "\t" + orig_read_3prime + "\t" + alt_read_3prime + "\n");
				
				out.write(gene + "." + chr + "." + start + "." + end + "\t" + PSI_5_prime_alt_spice + "\n");
				out2.write(gene + "." + chr + "." + start + "." + end + "\t" + PSI_3_prime_alt_spice + "\n");
				
				//out.write("chr\tstart\tend\tdirection\tPSI_5'AltSplice\tPSI_3'AltSplice\tFound_Read_5'End\tAlt_Read_5'End\tFound_Read_3'End\tAlt_Read_3'End\n");
				
			}

			out.close();
			out2.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function for grabbing the meta information from the 
	 * @param text
	 * @param id
	 * @return
	 */
	public static String grabMeta(String text, String id) {
		String returnval = "";
		if (text.contains(id)) {
			if (text.split(id).length > 1) {
				String val = text.split(id)[1].split(";")[0].trim();
				val = val.replaceAll("\"", "");
				val.trim();
				returnval = val;
			}
		}
		return returnval;
	}
}
