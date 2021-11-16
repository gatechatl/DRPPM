package rnaseq.splicing.csiminer.exontranslation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Use BLAT to annotate
 * @author 4472414
 *
 */
public class CSIMinerAnnotationBasedOnBLATOutut {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputBLATSearchResult = "/Users/4472414/References/genome/Exon_Annotation/output.txt";
			String inputExonAnnotation = "/Users/4472414/References/genome/Exon_Annotation/exon_annotation_updated.txt"; 
			
			String outputFile = "/Users/4472414/References/genome/Exon_Annotation/exon_annotation_updated_withblat.txt"; //args[0]; 
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputBLATSearchResult);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				int mismatch_score = new Integer(split[1]);
				int match_score = new Integer(split[0]);
				String protein_name = split[9];
				String exon_name_raw = split[13];
				String chr = exon_name_raw.split(":")[0];
				int start = new Integer(exon_name_raw.split(":")[1].split("-")[0]) + 1;
				int end = new Integer(exon_name_raw.split(":")[1].split("-")[1]);
				String protein_coord = protein_name.split("_")[0] + ":" + split[11] + ":" + split[12];
				String peptide = "";
				for (int i = 21; i < split.length; i++) {
					if (!peptide.contains(split[i])) {
						peptide += split[i]; 
					}
				}
				String newname = chr + ":" + start + "-" + end;
				if (mismatch_score == 0) {
					if (map.containsKey(newname)) {						
						String content = (String)map.get(newname);
						int prev_match_score = new Integer(content.split("\t")[2]);
						if (match_score > prev_match_score) {
							map.put(newname, protein_name + "\t" + protein_coord + "\t" + match_score + "\t" + peptide);
						}
					} else {
						map.put(newname, protein_name + "\t" + protein_coord + "\t" + match_score + "\t" + peptide);
					}
				}
			}
			in.close();
			
			FileInputStream fstream2 = new FileInputStream(inputExonAnnotation);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			String header = in2.readLine();
			out.write(header + "\t" + "BLAT_Protein\tProtein_Coord\tMatchScore\tPeptide\tPutativeMultiIsoform\n");
			while (in2.ready()) {
				String str2 = in2.readLine();
				String[] split2 = str2.split("\t");
				
				String chr = split2[0].split("_")[1];
				String start = split2[0].split("_")[2];
				String end = split2[0].split("_")[3];
				String coord = chr + ":" + start + "-" + end;
				if (map.containsKey(coord)) {
					String content = (String)map.get(coord);
					String[] split_content = content.split("\t");
					out.write(str2 + "\t" + content + "\t" + split_content[2].split(",").length + "\n");
				} else {
					out.write(str2 + "\tNA\tNA\tNA\tNA\tNA\n");
				}
			}
			in2.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
