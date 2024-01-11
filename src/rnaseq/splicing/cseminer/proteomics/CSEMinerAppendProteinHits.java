package rnaseq.splicing.cseminer.proteomics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * Read in the peptide PSM file and append hits that satisfy the xcorr and deltascore cutoff.
 * 1. Read in the reference file and get the coordinate region from the fasta file.
 * 2. Check the report file for the coordinate to see if the protein overlaps.
 * @author gatechatl
 *
 */
public class CSEMinerAppendProteinHits {
	
	public static String type() {
		return "CSI-Miner";
	}
	public static String description() {
		return "Append peptide hit to the CSIMiner Report.";
	}
	public static String parameter_info() {
		return "[inputCSI Report] [protein_col_name] [inputPeptidePSM] [Fasta DB File] [outputFile]";
	}
	/*public static void main(String[] args) {
		String peptide = "D.W!@#$%^&*E$R@^#WE.F";
		String query_seq = peptide.replaceAll("\\*", "").replaceAll("\\!",  "").replaceAll("\\.",  "").replaceAll("\\&",  "").replaceAll("\\^",  "").replaceAll("\\$",  "").replaceAll("\\#",  "").replaceAll("\\@",  "").replaceAll("\\%",  "");
		System.out.println(query_seq);
	}*/
	public static void execute(String[] args) {
		
		try {
			
			String inputCSIreport = args[0];
			String protein_col_name = args[1];
			String inputPeptidePSMFile = args[2];
			String inputFastaFile = args[3];
			String outputFile = args[4];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			HashMap ref = loadFastaFile(inputFastaFile);
			HashMap peptidePSM = new HashMap();
			FileInputStream fstream = new FileInputStream(inputPeptidePSMFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String peptide_psm_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				if (split.length > 13) {
					String target_decoy = split[11];
					if (target_decoy.equals("target")) {
						String outfilename = split[0];
						String charge = split[1];
						String trypic = split[2];
						String miscleavage = split[3];
						double score = new Double(split[4]);
						double deltaScore = new Double(split[5]);
						String precursor_neutral_mass = split[6];
						String neutral_pep_mass = split[7];
						String peptide = split[8];
						String peptide_length = split[9];
						String proteins = split[10] + "," + split[13];
						
						for (String protein: proteins.split(",")) {
							if (!protein.trim().equals("") && !protein.contains("decoy")) {
								String result = outfilename + "\t" + charge + "\t" + peptide + "\t" + protein + "\t" + score + "\t" + deltaScore; 
								if (target_decoy.equals("target") && score > 2 && deltaScore > 0.1) {
									if (protein.split("\\|").length >= 2) {
										String accession = protein.split("\\|")[1];
										String query_seq = peptide.replaceAll("\\*", "").replaceAll("\\!",  "").replaceAll("\\.",  "").replaceAll("\\&",  "").replaceAll("\\^",  "").replaceAll("\\$",  "").replaceAll("\\#",  "").replaceAll("\\@",  "").replaceAll("\\%",  "");
										boolean found = false;
										int start = -1;
										int end = -1;
										if (ref.containsKey(accession)) {
											String ref_seq = (String)ref.get(accession);
											//System.out.println(query_seq + "\t" + ref_seq);
											for (int i = 0; i < ref_seq.length() - query_seq.length() + 1; i++) {
												String subseq = ref_seq.substring(i, i + query_seq.length() - 1);
												if (query_seq.substring(0, query_seq.length() - 1).equals(subseq)) {
													found = true;
													start = i + 1;
													end = i + query_seq.length() - 1;
													break;
												}
											}
										}
										if (start > -1 && end > -1) {
											if (peptidePSM.containsKey(accession)) {
												HashMap position2peptide = (HashMap)peptidePSM.get(accession);		
												if (position2peptide.containsKey(accession + "\t" + start + "\t" + end)) {
													String prev_result = (String)position2peptide.get(accession + "\t" + start + "\t" + end);
													String[] split_prev_result = prev_result.split("\t");
													double prev_score = new Double(split_prev_result[4]);													
													if (score > prev_score) {
														position2peptide.put(accession + "\t" + start + "\t" + end, result);
														peptidePSM.put(accession, position2peptide);
													}
												} else {													
													position2peptide.put(accession + "\t" + start + "\t" + end, result);
													peptidePSM.put(accession, position2peptide);
												}
											} else {
												HashMap position2peptide = new HashMap();
												position2peptide.put(accession + "\t" + start + "\t" + end, result);
												peptidePSM.put(accession, position2peptide);
											}
										}
										//System.out.println(peptide + "\t" + accession + "\t" + start + "\t" + end);
									}
								}
							} // check if equals ""
						}
					}
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputCSIreport);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String csi_report_header = in.readLine();
			String[] split_csi_report_header = csi_report_header.split("\t");
			int index = -1;
			for (int i = 0; i < split_csi_report_header.length; i++) {
				if (split_csi_report_header[i].equals(protein_col_name)) {
					index = i;
				}
			}
			out.write(csi_report_header + "\t" + "ProteomicsHit" + "\t" + "PSMhit_outfilename" + "\t" + "PSMhit_charge" + "\t" + "PSMhit_peptide" + "\t" + "PSMhit_protein" + "\t" + "PSMhit_score" + "\t" + "PSMhit_deltaScore\n");
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
				if (split[index].contains("_")) {
					String[] split_peptide_info = split[index].split("_");
					String accession = split_peptide_info[0];
					int start = new Integer(split_peptide_info[1]) + 1; // I noticed the index is off by 1 adding 1 to compensate
					int end = new Integer(split_peptide_info[2]);
					double score = 0;
					String hit_result = "false\tNA\tNA\tNA\tNA\tNA\tNA";
					
					//if (peptidePSM.containsKey(accession) && accession.equals("P24821")) {
					if (peptidePSM.containsKey(accession)) {
						HashMap position2peptide = (HashMap)peptidePSM.get(accession);
						Iterator itr = position2peptide.keySet().iterator();
						while (itr.hasNext()) {
							String ref_accession_start_end = (String)itr.next();
							String ref_accession = ref_accession_start_end.split("\t")[0];
							String result = (String)position2peptide.get(ref_accession_start_end);
							String[] split_result = result.split("\t");
							String peptide = split_result[2];
							int ref_start = new Integer(ref_accession_start_end.split("\t")[1]);
							int ref_end = new Integer(ref_accession_start_end.split("\t")[2]);

							if (MathTools.overlap(start,  end, ref_start, ref_end)) {
								
								if (new Double(split_result[4]) >= score) {
									score = new Double(split_result[4]);
									hit_result = "true\t" + result;
								}
								//System.out.println("PSM Fasta: " + ref_start + "\t" + ref_end + "\t" + split_result[4] + "\t" + peptide);
								//System.out.println("CSI peptide: " + split[index] + "\t" + score + "\t" + split[index + 1]);
								//System.out.println(hit_result);
							}
						}
					}
					out.write(str + "\t" + hit_result + "\n");
				} else {
					String hit_result = "false\tNA\tNA\tNA\tNA\tNA\tNA";
					out.write(str + "\t" + hit_result + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap loadFastaFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			String name = "";			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					name = str.split(" ")[0].split("\\|")[1];	
					
				} else {					
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						seq += str;
						map.put(name, seq);
					} else {
						map.put(name, str);
					}
				}
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
