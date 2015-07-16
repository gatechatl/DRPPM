package RNAseqTools.IntronRetention;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import MISC.FileTools;

public class FilterBEDReadsScript {
	public static void execute(String[] args) {
		
		String inputFile = args[0];
		int length = new Integer(args[1]);
		System.out.println(generateScript(inputFile, length));
		
	}
	
	public static String generateScript(String inputFile1, int length) {
		 
		LinkedList listFile = FileTools.readFileList(inputFile1);
		String script = "";
		Iterator itr = listFile.iterator();		
		
		while (itr.hasNext()) {
			String file = (String)itr.next();
			String bedFile = file.split("/")[file.split("/").length - 1] + ".bed";
			script += "drppm -FilterBEDReads " + bedFile + "_exon.bed " + length + " " + bedFile + "_exon_filter.bed" + "\n";
			script += "drppm -FilterBEDReads " + bedFile + "_intron.bed " + length + " " + bedFile + "_intron_filter.bed" + "\n";
		}
		return script;
	}
	
}
