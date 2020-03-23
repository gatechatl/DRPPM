package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Overlap the IDs against the exon list
 * @author tshaw
 *
 */
public class JinghuiZhangOverlapCandidateWithProteomicsIDOrig {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map_hits = new HashMap();
			int candidates = 0;
			String inputCandidateFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\candidate_peptide.txt"; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\PCGP_TARGET_GTEx_Candidates.bed"; // args[0]; 
			FileInputStream fstream = new FileInputStream(inputCandidateFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 6) {
					String protein_id = split[3];
					String peptide = split[6];
					if (peptide.length() > 7) {
						candidates++;
						if (map_hits.containsKey(protein_id)) {
							HashMap peptides = (HashMap)map_hits.get(protein_id);
							peptides.put(protein_id + "\t" + split[4] + "\t" + split[5], str);
							map_hits.put(protein_id, peptides);
						} else {
							HashMap peptides = new HashMap();
							peptides.put(protein_id + "\t" + split[4] + "\t" + split[5], str);
							map_hits.put(protein_id, peptides);
							//System.out.println(protein_id);
						}
					}
				}
			}
			in.close();
			HashMap found = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\ProteomicsRMS\\common\\HongWangProteomics\\Combined_ID\\Combined_ID.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.contains("Database") && !str.contains("Peptide")) {
					String[] split = str.split(";");
					String peptide = split[0];
					String uniprot_name = split[1];
					String protein_accession = uniprot_name;
					double xscore = new Double(split[6]);
					if (uniprot_name.contains("|")) {
						protein_accession = uniprot_name.split("\\|")[1];
					}
					//System.out.println("Hits: " + protein_accession);
					int start = new Integer(split[14].replaceAll("AA", "").split("to")[0]);
					int end = new Integer(split[14].replaceAll("AA", "").split("to")[1]);
					if (map_hits.containsKey(protein_accession)) {
						
						HashMap peptides = (HashMap)map_hits.get(protein_accession);
						Iterator itr = peptides.keySet().iterator();
						while (itr.hasNext()) {
							String line = (String)itr.next();
							String info = (String)peptides.get(line);
							String protein_id = line.split("\t")[0];
							int entry_start = new Integer(line.split("\t")[1]);
							int entry_end = new Integer(line.split("\t")[2]);
							if (check_overlap(start, end, entry_start, entry_end)) {
								if (found.containsKey(info)) {
									String prev_id = (String)found.get(info);
									double prev_id_xscore = new Double(prev_id.split(";")[6]);
									int count = new Integer(prev_id.split("\t")[1]);
									count++;
									if (xscore > prev_id_xscore) {
										found.put(info, str + "\t" + count);
									} else {
										found.put(info,  prev_id.split("\t")[0] + "\t" + count);
									}
								} else {
									found.put(info, str + "\t" + 1);
								}
							}
						}
					}
							
				}
			}
			in.close();
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\candidate_peptide_with_peptide_support.txt"; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\AfterLiqingsExonCounting\\Summary\\PCGP_TARGET_GTEx_Candidates.bed"; // args[0];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			Iterator itr = found.keySet().iterator();
			while (itr.hasNext()) {
				String info = (String)itr.next();
				String peptide_info = (String)found.get(info);
				out.write(info + "\t" + peptide_info + "\n");
			}
			out.close();
			System.out.println(found.size() + "\t" + candidates);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean check_overlap(int a1, int a2, int b1, int b2) {
		if (a1 <= b1 && b1 <= a2) {
			return true;
		}
		if (a1 <= b2 && b2 <= a2) {
			return true;
		}		
		if (a2 <= b1 && b1 <= a1) {
			return true;
		}
		if (a2 <= b2 && b2 <= a1) {
			return true;
		}
		if (b1 <= a1 && a1 <= b2) {
			return true;
		}
		if (b1 <= a2 && a2 <= b2) {
			return true;
		}
		if (b2 <= a1 && a1 <= b1) {
			return true;
		}
		if (b2 <= a2 && a2 <= b1) {
			return true;
		}
		return false;
	}
}
