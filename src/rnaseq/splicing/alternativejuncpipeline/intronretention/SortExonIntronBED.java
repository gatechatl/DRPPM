package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.util.Iterator;
import java.util.LinkedList;

import misc.FileTools;

public class SortExonIntronBED {
	
	
	public static String type() {
		return "INTRONRETENTION";
	}
	public static String description() {
		return "Sort the BED file";
	}
	public static String parameter_info() {
		return "[BamFiles]";
	}
	public static void execute(String[] args) {
		
		String inputFile = args[0];
		System.out.println(generateScript(inputFile));
		
	}
	
	public static String generateScript(String inputFile1) {
		 
		LinkedList listFile = FileTools.readFileList(inputFile1);
		String script = "";
		Iterator itr = listFile.iterator();		
		
		while (itr.hasNext()) {
			String file = (String)itr.next();
			String bedFile = file.split("/")[file.split("/").length - 1] + ".bed";
			script += "sort -k 1,1 -k2,2n " + bedFile + "_exon_filter.bed > " + bedFile + "_exon_filter.sorted.bed" + "\n";
			script += "sort -k 1,1 -k2,2n " + bedFile + "_intron_filter.bed > " + bedFile + "_intron_filter.sorted.bed" + "\n";
			script += "mv " + bedFile + "_exon_filter.sorted.bed " + bedFile + "_exon_filter.bed\n";
			script += "mv " + bedFile + "_intron_filter.sorted.bed " + bedFile + "_intron_filter.bed\n";
		}
		return script;
	}
}
