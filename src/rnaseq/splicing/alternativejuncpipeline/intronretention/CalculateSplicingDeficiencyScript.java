package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.util.Iterator;
import java.util.LinkedList;

import misc.FileTools;

public class CalculateSplicingDeficiencyScript {
	public static void execute(String[] args) {
		
		String inputFile = args[0]; //bamlist
		String intronBed = args[1];
		String exonBed = args[2];
	
		
		System.out.println(generateScript(inputFile, intronBed, exonBed));
		
	}
	
	public static String generateScript(String inputFile1, String intronBed, String exonBed) {
		 
		LinkedList listFile = FileTools.readFileList(inputFile1);
		String script = "";
		Iterator itr = listFile.iterator();		
		
		while (itr.hasNext()) {
			String file = (String)itr.next();
			String bedFile = file.split("/")[file.split("/").length - 1] + ".bed";
			script += "drppm -CalculateSplicingDeficiency " + intronBed + " " + exonBed + " " + bedFile + "_intron.coverageBed.txt " + bedFile + "_exon.coverageBed.txt " + bedFile + "_SD.txt\n";
			
		}
		return script;
	}
}
