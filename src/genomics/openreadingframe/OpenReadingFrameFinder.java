package genomics.openreadingframe;

import genomics.gctools.TranslationTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

//import org.biojava3.core.sequence.transcription.DNAToRNATranslator;
//import org.biojava3.core.sequence.transcription.TranscriptionEngine;

/**
 * Identifying the open reading from for sequences
 * @author tshaw
 */
public class OpenReadingFrameFinder {

	
	public static String FindORF(String seq) {
		String orf = "";
		String frame1 = TranslationTool.translateDNA(seq, 0);
		String frame2 = TranslationTool.translateDNA(seq, 1);
		String frame3 = TranslationTool.translateDNA(seq, 2);
		String orf_frame1 = identifyLongestORF(frame1);
		String orf_frame2 = identifyLongestORF(frame2);
		String orf_frame3 = identifyLongestORF(frame3);
		if (orf_frame1.length() >= orf_frame2.length() && orf_frame1.length() >= orf_frame3.length()) {
			return orf_frame1;
		}
		if (orf_frame2.length() >= orf_frame1.length() && orf_frame2.length() >= orf_frame3.length()) {
			return orf_frame2;
		}
		if (orf_frame3.length() >= orf_frame1.length() && orf_frame3.length() >= orf_frame2.length()) {
			return orf_frame3;
		}
		return "NA";
	}
	public static String FindORF_StartSite(String seq) {
		String orf = "";
		String frame1 = TranslationTool.translateDNA(seq, 0);
		String frame2 = TranslationTool.translateDNA(seq, 1);
		String frame3 = TranslationTool.translateDNA(seq, 2);
		String orf_frame1 = identifyLongestORF(frame1);
		String orf_frame2 = identifyLongestORF(frame2);
		String orf_frame3 = identifyLongestORF(frame3);
		if (orf_frame1.length() >= orf_frame2.length() && orf_frame1.length() >= orf_frame3.length()) {
			return frame1.indexOf(orf_frame1) * 3 + 1 + "";
			//return orf_frame1;
		}
		if (orf_frame2.length() >= orf_frame1.length() && orf_frame2.length() >= orf_frame3.length()) {
			return 1 + frame2.indexOf(orf_frame2) * 3 + 1 + "";
		}
		if (orf_frame3.length() >= orf_frame1.length() && orf_frame3.length() >= orf_frame2.length()) {
			return 2 + frame3.indexOf(orf_frame3) * 3 + 1 + "";
		}
		return "NA";
	}
	public static String identifyLongestORF(String seq) {
		
		int longestM = -1;
		int longestStop = -1;
		LinkedList indexMList = new LinkedList();
		LinkedList indexStopList = new LinkedList();
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("M")) {
				
				indexMList.add(i);
			}
			if (seq.substring(i, i + 1).equals("*")) {
				//System.out.println(seq.substring(i, i + 1));
				indexStopList.add(i);
			}
			
		}
		Iterator itrM = indexMList.iterator();
		while (itrM.hasNext()) {
			int indexM = (Integer)itrM.next();
			Iterator itrStop = indexStopList.iterator();
			while (itrStop.hasNext()) {
				int indexStop = (Integer)itrStop.next();
				if (indexStop - indexM > longestStop - longestM && !seq.substring(indexM, indexStop).contains("*")) {
					longestStop = indexStop;
					longestM = indexM;
				}
			}
		}
		if (longestM == -1) {
			return "";
		}
		return seq.substring(longestM, longestStop + 1);
	}
	public static void main(String[] args) {
		String seq = "CTTCTCCCACCCTTCTCCCCTGAAGCAGGCCATAAAAGAATCCTCTGACGTTTCTATAAAGTAGGTCATAAGAACCTTCATTCCAGAAGTACCCTCAAAGACAGAGACACCAAGAAGAATCGGAACATACAGGCTTTGATATCAAAGGTTTATAAAGCCAATATCTGGGAAAGAGAAAACCGTGAGACTTCCAGATCTTCTCTGGTGAAGTGTGTTTCCTGCAACGATCACGAACATGAACATCAAAGGATCGCCATGGAAAGCAGGGTCCCTCCTGCTGCTGCTGGTGTCAAACCTGCTCCTGTGCCAGAGCGTGGCCCCCTTGCCCATCTGTCCCGGCGGGGCTGCCCGATGCCAGGTGACCCTTCGAGACCTGTTTGACCGCGCCGTCGTCCTGTCCCACTACATCCATAACCTCTCCTCAGAAATGTTCAGCGAATTCGATAAACGGTATACCCATGGCCGGGGGTTCATTACCAAGGCCATCAACAGCTGCCACACTTCTTCCCTTGCCACCCCCGAAGACAAGGAGCAAGCCCAACAGATGAATCAAAAAGACTTTCTGAGCCTGATAGTCAGCATATTGCGATCCTGGAATGAGCCTCTGTATCATCTGGTCACGGAAGTACGTGGTATGCAAGAAGCCCCGGAGGCTATCCTATCCAAAGCTGTAGAGATTGAGGAGCAAACCAAACGGCTTCTAGAGGGCATGGAGCTGATAGTCAGCCAGGTTCATCCTGAAACCAAAGAAAATGAGATCTACCCTGTCTGGTCGGGACTTCCATCCCTGCAGATGGCTGATGAAGAGTCTCGCCTTTCTGCTTATTATAACCTGCTCCACTGCCTACGCAGGGATTCACATAAAATCGACAATTATCTCAAGCTCCTGAAGTGCCGAATCATCCACAACAACAACTGCTAAGCCCACATCCATTTCATCTATTTCTGAGAAGGTCCTTAATGATCCGTTCCATTGCAAGCTTCTTTTAGTTGTATCTCTTTTGAATCCATGCTTGGGTGTAACAGGTCTCCTCTTAAAAAATAAAAACTGACTCCTTAGAGACATCAAAATCTAAAA";
		System.out.println(FindORF(seq));
	}
	public static String type() {
		return "ORF";
	}
	public static String description() {
		return "Extract the open reading frame for mRNA sequences";
	}
	public static String parameter_info() {
		return "[inputFastaFile] [outputORFFastaFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String name = "";
			String seq = "";
			String inputFastaFile = args[0];
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFastaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str;
					seq = "";					
				} else {
					seq += str.trim();
					map.put(name, seq);
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String fastaName = (String)itr.next();
				String sequence = (String)map.get(fastaName);
				String orf = FindORF(sequence);
				String orf_start = FindORF_StartSite(sequence);
				int start = new Integer(orf_start);				
				out.write(fastaName + ":" + orf_start + "\n");
				out.write(orf + "\n");
				out.flush();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
