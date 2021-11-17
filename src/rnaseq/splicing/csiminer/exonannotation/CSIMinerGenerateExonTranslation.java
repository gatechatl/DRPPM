package rnaseq.splicing.csiminer.exonannotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CSIMinerGenerateExonTranslation {

	
	
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "/Users/4472414/References/genome/Exon_Annotation/exon_annotation.txt"; //args[0];
			
			String inputFile_protein = "/Users/4472414/References/genome/Exon_Annotation/exon_coordinates_short_protein.txt"; //args[0];
						
			String outputFile = "/Users/4472414/References/genome/Exon_Annotation/exon_annotation_updated.txt"; //args[0]; 
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			HashMap map = new HashMap();
			HashMap map2 = new HashMap();

			HashMap hit = new HashMap();
			FileInputStream fstream2 = new FileInputStream(inputFile_protein);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));			
			while (in2.ready()) {
				String str2 = in2.readLine();
				
				String[] split2 = str2.split(", ");
				String chr = "chr" + split2[0].split(":")[0];
				String start = split2[0].split(":")[1].split("-")[0];
				String end = split2[0].split(":")[1].split("-")[1];
				if (!split2[1].equals("NA")) {
					hit.put("chr" + split2[0], split2[1]);
				}
			}
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[7].equals("NA")) {
					String chr = split[0].split("_")[1];
					String start = split[0].split("_")[2];
					String end = split[0].split("_")[3];
					String coord = chr + ":" + start + "-" + end;
					if (hit.containsKey(coord)) {
						String seq = (String)hit.get(coord);
						System.out.println(coord + "\t" + seq);
						out.write(split[0]);
						for (int i = 1; i < 7; i++) {
							out.write("\t" + split[i]);
						}
						out.write("\t" + seq);
						for (int i = 8; i < split.length; i++) {
							out.write("\t" + split[i]);
						}
						out.write("\n");
					} else {
						out.write(str + "\n");
					}
				} else {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
