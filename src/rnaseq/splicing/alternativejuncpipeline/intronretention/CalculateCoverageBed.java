package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.util.Iterator;
import java.util.LinkedList;

import misc.FileTools;

/**
 * After Filter of BED Reads run Recalculation of Coverage Bed
 * @author tshaw
 *
 */
public class CalculateCoverageBed {

	public static void execute(String[] args) {
		
		String inputFile = args[0];
		String intron_bed = args[1];
		String exon_bed = args[2];
		System.out.println(generateScript(inputFile, intron_bed, exon_bed));
		
	}
	
	public static String generateScript(String inputFile1, String intron_bed, String exon_bed) {
		 
		LinkedList listFile = FileTools.readFileList(inputFile1);
		String script = "";
		Iterator itr = listFile.iterator();		
		
		while (itr.hasNext()) {
			String file = (String)itr.next();
			String bedFile = file.split("/")[file.split("/").length - 1] + ".bed";
			script += "coverageBed -b " + bedFile + "_exon_filter.bed -a " + exon_bed + " -sorted -counts -F 0.1 > " + bedFile + "_exon.coverageBed.txt \n";
			script += "coverageBed -b " + bedFile + "_intron_filter.bed -a " + intron_bed + " -sorted -counts -F 0.1 > " + bedFile + "_intron.coverageBed.txt \n";
		}
		return script;
	}
	
}
