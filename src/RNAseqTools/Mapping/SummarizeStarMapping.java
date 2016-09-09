package RNAseqTools.Mapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class SummarizeStarMapping {

	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Summarize the reads statistics";
	}
	public static String parameter_info() {
		return "[inputFileList] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			LinkedList list = new LinkedList();
			String inputFile = args[0];
			String outputFile = args[1];
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
				 
				double total_multiple = too_many_loci + multiple_loci;
				double total_mapped = uniqMapReads + too_many_loci + multiple_loci; 
				double percent_multiple = total_multiple / totalReads;
				double percent_mapped = total_mapped / totalReads;
				out.write(split[2] + "\t" + totalReads + "\t" + total_mapped + "\t" + (total_mapped - total_multiple) + "\t" + percent_mapped + "\t" + percent_multiple + "\n");
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
