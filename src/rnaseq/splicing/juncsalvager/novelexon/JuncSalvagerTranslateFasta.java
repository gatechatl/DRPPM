package rnaseq.splicing.juncsalvager.novelexon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import general.sequence.analysis.TranslationTool;

public class JuncSalvagerTranslateFasta {


	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Convert nucleotide fasta to peptide fasta.";
	}
	public static String parameter_info() {
		return "[inputBed] [inputFasta] [six digit index] [outputPeptideFasta] [outputIndexFile] [outputJUMPDatabase]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap direction = new HashMap();
			HashMap idx_map = new HashMap();
			String inputBed = args[0];
			String fasta = args[1];
			int index = new Integer(args[2]); // recommend six digit starting with 1 or 2
			String outputFile = args[3];
			String outputIndexFile = args[4];			
			String outputJUMPdbFile = args[5];
			String outputPITtable = args[6];
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
			

			FileWriter fwriterIdxFile = new FileWriter(outputIndexFile);
			BufferedWriter outIdxFile = new BufferedWriter(fwriterIdxFile);
			
			FileWriter fwriterJUMPdb = new FileWriter(outputJUMPdbFile);
			BufferedWriter outJUMPdb = new BufferedWriter(fwriterJUMPdb);
			
			FileWriter fwriterPITtable = new FileWriter(outputPITtable);
			BufferedWriter outPITtable = new BufferedWriter(fwriterPITtable);
			outPITtable.write("UniprotAC\tSJPGnumber\tGroupName\tAbundance\tResidueLength\tProteinName\tFullDescription\tAnnotation\n");

			
			HashMap uniq_seq = new HashMap();
			HashMap decoy = new HashMap();
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
						index++;
						String name1 = ">SJPG" + index + ".001";
						String name1_clean = name1.replaceAll(">", "");
						String frame1 = clean_seq(TranslationTool.translateDNA(seq, 0));
						if (frame1.length() > 6) {
							if (!uniq_seq.containsKey(tag + ":frame1:forward")) {
								out.write(tag + ":frame1:forward\n" + frame1 + "\n");						
								outIdxFile.write(name1 + "\t" + tag + ":frame1:forward\n");
								outJUMPdb.write(name1 + "\n" + frame1 + "\n");
								decoy.put(reverseUsingStringBuilder(frame1), "##Decoy__" + tag + ":frame1:forward");
								outPITtable.write(name1_clean + "\t" + name1_clean + "\t" + name1_clean + "\t" + "-" + "\t" + seq.length() + "\t" + tag + "_frame1_forward" + "\t" + tag + "_frame1_forward" + "\t-\n");
							}
							uniq_seq.put(tag + ":frame1:forward", frame1);
						}
						index++;						
						String frame2 = clean_seq(TranslationTool.translateDNA(seq, 1));
						if (frame2.length() > 6) {
							String name2 = ">SJPG" + index + ".001";
							String name2_clean = name2.replaceAll(">", "");
							if (!uniq_seq.containsKey(tag + ":frame2:forward")) {
								out.write(tag + ":frame2:forward\n" + frame2 + "\n");
								outIdxFile.write(name2 + "\t" + tag + ":frame2:forward\n");
								outJUMPdb.write(name2 + "\n" + frame2 + "\n");
								decoy.put(reverseUsingStringBuilder(frame2), "##Decoy__" + tag + ":frame2:forward");
								outPITtable.write(name2_clean + "\t" + name2_clean + "\t" + name2_clean + "\t" + "-" + "\t" + seq.length() + "\t" + tag + "_frame2_forward" + "\t" + tag + "_frame2_forward" + "\t-\n");
							}
							uniq_seq.put(tag + ":frame2:forward", frame2);
						}
						index++;
						String frame3 = clean_seq(TranslationTool.translateDNA(seq, 2));		
						if (frame3.length() > 6) {
							String name3 = ">SJPG" + index + ".001";
							String name3_clean = name3.replaceAll(">", "");
							if (!uniq_seq.containsKey(tag + ":frame3:forward")) {
								out.write(tag + ":frame3:forward\n" + frame3 + "\n");
								outIdxFile.write(name3 + "\t" + tag + ":frame3:forward\n");
								outJUMPdb.write(name3 + "\n" + frame3 + "\n");
								decoy.put(reverseUsingStringBuilder(frame3), "##Decoy__" + tag + ":frame3:forward");
								outPITtable.write(name3_clean + "\t" + name3_clean + "\t" + name3_clean + "\t" + "-" + "\t" + seq.length() + "\t" + tag + "_frame3_forward" + "\t" + tag + "_frame3_forward" + "\t-\n");
							}
							uniq_seq.put(tag + ":frame3:forward", frame3);
						}
					} else {
						index++;
						String name1 = ">SJPG" + index + ".001";
						String name1_clean = name1.replaceAll(">", "");
						String reverse_complement_seq = TranslationTool.reverse_complement(seq);
						String frame1 = clean_seq(TranslationTool.translateDNA(reverse_complement_seq, 0));
						if (frame1.length() > 6) {
							if (!uniq_seq.containsKey(tag + ":frame1:reverse")) {
								out.write(tag + ":frame1:reverse\n" + frame1 + "\n");
								outIdxFile.write(name1 + "\t" + tag + ":frame1:reverse\n");
								outJUMPdb.write(name1 + "\n" + frame1 + "\n");
								decoy.put(reverseUsingStringBuilder(frame1), tag + ":frame1:reverse");
								outPITtable.write(name1_clean + "\t" + name1_clean + "\t" + name1_clean + "\t" + "-" + "\t" + seq.length() + "\t" + tag + "_frame1_reverse" + "\t" + tag + "_frame1_reverse" + "\t-\n");
							}
							uniq_seq.put(tag + ":frame1:reverse", frame1);
						}
						index++;
						String name2 = ">SJPG" + index + ".001";
						String name2_clean = name2.replaceAll(">", "");
						String frame2 = clean_seq(TranslationTool.translateDNA(reverse_complement_seq, 1));
						if (frame2.length() > 6) {
							if (!uniq_seq.containsKey(tag + ":frame2:reverse")) {
								out.write(tag + ":frame2:reverse\n" + frame2 + "\n");
								outIdxFile.write(name2 + "\t" + tag + ":frame2:reverse\n");
								outJUMPdb.write(name2 + "\n" + frame2 + "\n");
								decoy.put(reverseUsingStringBuilder(frame2), tag + ":frame2:reverse");
								outPITtable.write(name2_clean + "\t" + name2_clean + "\t" + name2_clean + "\t" + "-" + "\t" + seq.length() + "\t" + tag + "_frame2_reverse" + "\t" + tag + "_frame2_reverse" + "\t-\n");
							}
							uniq_seq.put(tag + ":frame2:reverse", frame2);
						}
						index++;
						String name3 = ">SJPG" + index + ".001";
						String name3_clean = name3.replaceAll(">", "");
						String frame3 = clean_seq(TranslationTool.translateDNA(reverse_complement_seq, 2));
						if (frame3.length() > 6) {
							if (!uniq_seq.containsKey(tag + ":frame3:reverse")) {
								out.write(tag + ":frame3:reverse\n" + frame3 + "\n");
								decoy.put(reverseUsingStringBuilder(frame3), tag + ":frame3:reverse");
								outIdxFile.write(name3 + "\t" + tag + ":frame3:reverse\n");
								outJUMPdb.write(name3 + "\n" + frame3 + "\n");
								outPITtable.write(name3_clean + "\t" + name3_clean + "\t" + name3_clean + "\t" + "-" + "\t" + seq.length() + "\t" + tag + "_frame3_reverse" + "\t" + tag + "_frame3_reverse" + "\t-\n");
							}
							uniq_seq.put(tag + ":frame3:reverse", frame3);
						}
					}
				}								
			}
			in.close();
			
			Iterator itr = decoy.keySet().iterator();
			while (itr.hasNext()) {
				String seq = (String)itr.next();
				String decoy_tag_name = (String)decoy.get(seq);
				String pitid = "SJPG" + index + ".001";
				outPITtable.write("##Decoy__" + index + ".001" + "\t" + pitid + "\t" + pitid + "\t" + "-" + "\t" + seq.length() + "\t" + decoy_tag_name + "\t" + decoy_tag_name + "\t-\n");
				outJUMPdb.write(">##Decoy__" + index + ".001" + "\n" + seq + "\n");
				index++;
			}
			
			out.close();
			outIdxFile.close();
			outJUMPdb.close();
			outPITtable.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String reverseUsingStringBuilder(String input) {
	    if (input == null) {
	        return null;
	    }	 
	    StringBuilder output = new StringBuilder(input).reverse();
	    return output.toString();
	}
	
	public static void main(String[] args) {
		
		System.out.println(clean_seq("AWERWXSDFXASDMASDFWOLERJWOEMRJWFIEJ"));
	}
	public static String clean_seq(String input) {				
		String split[] = input.split("\\*");		
		if (split.length > 1) {
			String remaining = split[split.length - 1];
			if (remaining.contains("M")) {
				String return_str = "";
				boolean foundM = false;
				for (int i = 0; i < remaining.length(); i++) {
					if (remaining.substring(i, i + 1).equals("M")) {
						foundM = true;
					}
					if (foundM) {
						return_str += remaining.substring(i, i + 1);
					}					
				}
				return return_str;
			} else {
				return "";
			}						
		} else {
			return input;
		}
						
	}
}
