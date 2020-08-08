package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import statistics.general.MathTools;

public class JinghuiZhangAnnotateMembranePositions {

	
	public static void main(String[] args) {
		
		try {
			
			String accession = "";
			HashMap accession2fasta = new HashMap();
			String fasta_file = "\\\\gsc.stjude.org\\project_space\\gottsgrp\\CART\\common\\AltSpliceProject\\LiqingProcessedExpressionMatrix\\ECM_candidates.fasta";
			FileInputStream fstream = new FileInputStream(fasta_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					accession = str.split(" ")[0].replaceAll(">", "").split("\\|")[1];
					//System.out.println(accession);
				} else {
					if (accession2fasta.containsKey(accession)) {
						String prev = (String)accession2fasta.get(accession);
						prev += str;
						accession2fasta.put(accession, prev);
					} else {
						accession2fasta.put(accession, str);
					}
				}
			}
			in.close();
			
			HashMap membrane_annotation = new HashMap();
			String membrane_annotation_file = "\\\\gsc.stjude.org\\project_space\\gottsgrp\\CART\\common\\AltSpliceProject\\LiqingProcessedExpressionMatrix\\membrane_annotation.txt";
			fstream = new FileInputStream(membrane_annotation_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				accession = split[0].split("\\|")[1];
				if (!split[5].equals("Topology=o")) {
					membrane_annotation.put(accession, "1" + split[5].replaceAll("Topology=", "") + split[1].replaceAll("len=", ""));
				} else {
					membrane_annotation.put(accession, split[5].replaceAll("Topology=", ""));
				}
				
			}
			in.close();
	

			String outputFile = "\\\\gsc.stjude.org\\project_space\\gottsgrp\\CART\\common\\AltSpliceProject\\LiqingProcessedExpressionMatrix\\Candidate_ECM_fasta_membrane_annotation.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			String candidate_region_file = "\\\\gsc.stjude.org\\project_space\\gottsgrp\\CART\\common\\AltSpliceProject\\LiqingProcessedExpressionMatrix\\Candidate_ECM_fasta.txt";
			fstream = new FileInputStream(candidate_region_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\tOuterMembrane\tPartialOuterMembrane\tMembraneAnnotation\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("NA")) {
					accession = split[0].split("_")[0];
					String seq = split[1];
					int start = new Integer(split[0].split("_")[1]);
					int end = new Integer(split[0].split("_")[2]);
					if (membrane_annotation.containsKey(accession)) {
						String annotation = (String)membrane_annotation.get(accession);
						if (annotation.equals("o")) {
							out.write(str + "\tNA\tNA\tNA\n");
						} else {
							boolean embedded = false;
							boolean partial_intersect = false;
							String[] split_out = annotation.split("-");
							//System.out.println(annotation);
							for (String region: split_out) {
								if (region.contains("o")) {
									if (region.split("o").length == 2) {
										int start_out = new Integer(region.split("o")[0]) - 1;
										int start_end = new Integer(region.split("o")[1]) - 1;
										if (MathTools.embedded(start_out, start_end, start, end)) {
											embedded = true;
										}
										if (MathTools.intersect(start_out, start_end, start, end)) {
											partial_intersect = true;									
										}
									} else {
										System.out.println("Problem: " + region);
									}
								}
							}
							out.write(str + "\t" + embedded + "\t" + partial_intersect + "\t" + annotation + "\n");
						}
					} else {
						out.write(str + "\tNA\tNA\tNA\n");
					}
				} else {
					out.write(str + "\tNA\tNA\tNA\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
