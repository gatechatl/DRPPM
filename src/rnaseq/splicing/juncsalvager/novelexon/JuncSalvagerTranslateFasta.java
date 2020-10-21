package rnaseq.splicing.juncsalvager.novelexon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import general.sequence.analysis.TranslationTool;

public class JuncSalvagerTranslateFasta {


	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Convert nucleotide fasta to peptide fasta.";
	}
	public static String parameter_info() {
		return "[inputBed] [inputFasta] [outputPeptideFasta]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap direction = new HashMap();
			String inputBed = args[0];
			String fasta = args[1];
			String outputFile = args[2];
			FileInputStream fstream = new FileInputStream(inputBed);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0] + ":" + split[1] + "-" + split[2];
				direction.put(name,  split[5]);
			}
			in.close();
	
	
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(fasta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String tag = in.readLine();
				String seq = in.readLine().toUpperCase();
				boolean forward = false;
				if (direction.containsKey(tag.replaceAll(">", ""))) {
					if (((String)direction.get(tag.replaceAll(">",  ""))).equals("+")) {
						forward = true;
					}
					
					if (forward) {
						String frame1 = TranslationTool.translateDNA(seq, 0);
						out.write(tag + ":frame1:forward\n" + frame1 + "\n");
						String frame2 = TranslationTool.translateDNA(seq, 1);
						out.write(tag + ":frame2:forward\n" + frame2 + "\n");
						String frame3 = TranslationTool.translateDNA(seq, 2);
						out.write(tag + ":frame3:forward\n" + frame3 + "\n");
					} else {
						String reverse_complement_seq = TranslationTool.reverse_complement(seq);
						String frame1 = TranslationTool.translateDNA(reverse_complement_seq, 0);
						out.write(tag + ":frame1:reverse\n" + frame1 + "\n");
						String frame2 = TranslationTool.translateDNA(reverse_complement_seq, 1);
						out.write(tag + ":frame2:reverse\n" + frame2 + "\n");
						String frame3 = TranslationTool.translateDNA(reverse_complement_seq, 2);
						out.write(tag + ":frame3:reverse\n" + frame3 + "\n");
					}
				}
				
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
