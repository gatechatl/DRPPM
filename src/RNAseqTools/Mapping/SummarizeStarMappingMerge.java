package RNAseqTools.Mapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SummarizeStarMappingMerge {

	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Summarize the reads statistics";
	}
	public static String parameter_info() {
		return "[inputFileList] [split _ index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			LinkedList list = new LinkedList();
			String inputFile = args[0];
			int buffer = new Integer(args[1]);
			String outputFile = args[2];
			
			HashMap entry = new HashMap();
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("SampleName\tTOTAL_READS\tMAPPED\tNONDUPS_MAPPED\tPERCENT_MAPPED\tPERCENT_DUPS\n");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String logFileName = split[2] + "Log.final.out";

				int totalReads = 0;
				int uniqMapReads = 0;
				String percent_uniq_map_reads = "";
				int multiple_loci = 0;
				int too_many_loci = 0;
				
				FileInputStream fstream2 = new FileInputStream(logFileName);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					str2 = str2.replaceAll(" ", "").replaceAll("\\|", "");
					String[] split2 = str2.split("\t");
					//System.out.println(str2);
					//System.out.println(split2[0]);
					if (split2[0].equals("Numberofinputreads")) {
						totalReads = new Integer(split2[1]);
					}
					if (split2[0].equals("Uniquelymappedreadsnumber")) {
						uniqMapReads = new Integer(split2[1]);
					}
					if (split2[0].equals("Uniquelymappedreads%")) {
						percent_uniq_map_reads = split2[1];
					}
					if (split2[0].equals("Numberofreadsmappedtomultipleloci")) {
						multiple_loci = new Integer(split2[1]);
					}
					if (split2[0].equals("Numberofreadsmappedtotoomanyloci")) {
						too_many_loci = new Integer(split2[1]);
					}
				}
				in2.close();
				
				String name = "";
				String[] split2 = split[2].split("_");
				for (int i = 0; i < split2.length - buffer; i++) {
					if (i == 0) {
						name = split2[i];
					} else {
						name += "_" + split2[i];
					}
				}
				
				double total_multiple = too_many_loci + multiple_loci;
				double total_mapped = uniqMapReads + too_many_loci + multiple_loci; 
				double percent_multiple = total_multiple / totalReads;
				double percent_mapped = total_mapped / totalReads;
				String text = totalReads + "\t" + total_mapped + "\t" + (total_mapped - total_multiple) + "\t" + percent_mapped + "\t" + percent_multiple;
				if (entry.containsKey(name)) {
					String result = (String)entry.get(name);
					String[] split3 = result.split("\t");
					double prev_totalReads = new Double(split3[0]);
					double prev_total_mapped = new Double(split3[1]);
					double prev_duplicate = new Double(split3[2]);
					double prev_percent_mapped = new Double(split3[3]);
					double prev_percent_multiple = new Double(split3[4]);
					double new_totalReads = prev_totalReads + totalReads;
					double new_total_mapped = prev_total_mapped + total_mapped;
					double new_duplicate = prev_duplicate + (total_mapped - total_multiple);
					double new_percent_mapped = prev_percent_mapped * (prev_totalReads / (prev_totalReads + totalReads)) + percent_mapped * (totalReads / (prev_totalReads + totalReads));
					double new_percent_multiple = prev_percent_multiple * (prev_totalReads / (prev_totalReads + totalReads)) + percent_multiple * (totalReads / (prev_totalReads + totalReads));
					String new_text = new_totalReads + "\t" + new_total_mapped + "\t" + new_duplicate + "\t" + new_percent_mapped + "\t" + new_percent_multiple;
					entry.put(name, new_text);
				} else {
					entry.put(name, text);
				}
				//out.write(split[2] + "\t" + totalReads + "\t" + total_mapped + "\t" + (total_mapped - total_multiple) + "\t" + percent_mapped + "\t" + percent_multiple + "\n");
				
			}
			in.close();
			
			Iterator itr = entry.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String)itr.next();
				String line = (String)entry.get(name);
				out.write(name + "\t" + line + "\n");
				
			}
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

