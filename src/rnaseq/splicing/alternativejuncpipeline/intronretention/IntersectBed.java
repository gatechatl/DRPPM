package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class IntersectBed {
	public static void execute(String[] args) {
		
		String inputFile = args[0];
		String intronBed = args[1];
		String exonBed = args[2];
		String geneBed = args[3];
		System.out.println(generateScript(inputFile, intronBed, exonBed, geneBed));
		
	}
	
	public static String generateScript(String inputFile1, String intronBed, String exonBed, String geneBed) {
		 
		LinkedList listFile = readFileList(inputFile1);
		String script = "";
		Iterator itr = listFile.iterator();		
		
		while (itr.hasNext()) {
			String file = (String)itr.next();
			String bedFile = file.split("/")[file.split("/").length - 1] + ".bed";
			script += "intersectBed -b " +  bedFile + " -a " + intronBed + " -sorted > " + bedFile + "_intron.bed \n";
			script += "intersectBed -b " +  bedFile + " -a " + exonBed + " -sorted > " + bedFile + "_exon.bed \n";
			script += "intersectBed -b " +  bedFile + " -a " + geneBed + " -sorted > " + bedFile + "_gene.bed \n";
		}
		return script;
	}
	public static LinkedList readFileList(String inputFile) {
		
		LinkedList listFile = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					listFile.add(str.trim());
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFile;
	}
}
