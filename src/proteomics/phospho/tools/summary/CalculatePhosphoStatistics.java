package proteomics.phospho.tools.summary;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import proteomics.phospho.tools.motifs.MotifTools;

/**
 * Calculate the statistics based on what is given
 * For each phosphopeptide how many are in each network
 * @author tshaw
 *
 */
public class CalculatePhosphoStatistics {
	
	public static void execute(String[] args) {
		
		try {
			
			String orig_phosphojumpq = args[0];			
			String inputFileName = args[1];
			String hprd_motifFile = args[2];
			HashMap peptide_geneName = new HashMap();
			HashMap uniq_peptide = new HashMap();
			FileInputStream fstream = new FileInputStream(orig_phosphojumpq);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String jumpq_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = split[0];
				LinkedList list = addPossiblePeptide(peptide);
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					String key = (String)itr.next();
					peptide_geneName.put(key + "\t" + split[2], "");
					uniq_peptide.put(key, "");
				}
			}
			in.close();
			
			// present in phosphosite
			HashMap phosphosite_uniq_peptide = new HashMap();
			HashMap phosphosite_peptide_geneName = new HashMap();
			
			HashMap neighbor_evidence_peptide = new HashMap();
			HashMap neighbor_evidence_peptide_geneName = new HashMap();
			
			HashMap predicted_neighbor_peptide = new HashMap();
			HashMap predicted_neighbor_peptide_geneName = new HashMap();
			
			HashMap motif_found_peptide = new HashMap();
			HashMap motif_found_peptide_geneName = new HashMap();
			
			// utilize phosphosite to estimate correctness
			HashMap phosphosite_match_peptide = new HashMap();
			HashMap phosphosite_match_peptide_geneName = new HashMap();
			
			HashMap phosphosite_incorrect_match_peptide = new HashMap();
			HashMap phosphosite_incorrect_match_peptide_geneName = new HashMap();
			
			// utilize phosphosite to estimate correctness
			HashMap hprd_phosphosite_match_peptide = new HashMap();
			HashMap hprd_phosphosite_match_peptide_geneName = new HashMap();
			
			HashMap hprd_phosphosite_incorrect_match_peptide = new HashMap();
			HashMap hprd_phosphosite_incorrect_match_peptide_geneName = new HashMap();
			
			String hprd_gene = "";
			fstream = new FileInputStream(hprd_motifFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String hprd_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				hprd_gene += split[2] + ",";
			}
			// present in 
			fstream = new FileInputStream(inputFileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String inputFile_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = split[4];
				String geneName = split[1];
				String motif_genes = split[7];
				String phosphosite_genes = split[11];
				String phosphosite_genes2 = split[12];
				String key = split[4] + "\t" + split[1];
				if (!split[11].equals("NA")) {
					phosphosite_uniq_peptide.put(split[4], split[4]);
					phosphosite_peptide_geneName.put(key, key);
				}
				if (split[19].contains("found_predicted_neighbor")) {
					predicted_neighbor_peptide.put(split[4], split[4]);
					predicted_neighbor_peptide_geneName.put(key, key);
				}
				if (split[19].contains("found_neighbor")) {
					neighbor_evidence_peptide.put(split[4], split[4]);
					neighbor_evidence_peptide_geneName.put(key, key);
				}
				if (!split[6].equals("NA")) {
					motif_found_peptide.put(split[4], split[4]);
					motif_found_peptide_geneName.put(key, key);
				}
				if (!phosphosite_genes.equals("NA")) {
					if (overlapGeneStr(motif_genes, phosphosite_genes) || overlapGeneStr(motif_genes, phosphosite_genes2)) {
						phosphosite_match_peptide.put(split[4],  split[4]);
						phosphosite_match_peptide_geneName.put(key,  key);
						if (phosphosite_incorrect_match_peptide.containsKey(split[4])) {
							phosphosite_incorrect_match_peptide.remove(split[4]);
						}
						if (phosphosite_incorrect_match_peptide_geneName.containsKey(key)) {
							phosphosite_incorrect_match_peptide_geneName.remove(key);
						}
					} else {
						if (!phosphosite_match_peptide.containsKey(split[4])) {
							phosphosite_incorrect_match_peptide.put(split[4], split[4]);							
						}
						if (!phosphosite_match_peptide_geneName.containsKey(key)) {
							phosphosite_incorrect_match_peptide_geneName.put(key,  key);
						}
					}
					if (overlapGeneStr(phosphosite_genes, hprd_gene)) {
						if (overlapGeneStr(motif_genes, phosphosite_genes) || overlapGeneStr(motif_genes, phosphosite_genes2)) {
							hprd_phosphosite_match_peptide.put(split[4],  split[4]);
							hprd_phosphosite_match_peptide_geneName.put(key,  key);
							if (hprd_phosphosite_incorrect_match_peptide.containsKey(split[4])) {
								hprd_phosphosite_incorrect_match_peptide.remove(split[4]);
							}
							if (hprd_phosphosite_incorrect_match_peptide_geneName.containsKey(key)) {
								hprd_phosphosite_incorrect_match_peptide_geneName.remove(key);
							}
						} else {
							if (!hprd_phosphosite_match_peptide.containsKey(split[4])) {
								hprd_phosphosite_incorrect_match_peptide.put(split[4], split[4]);							
							}
							if (!hprd_phosphosite_match_peptide_geneName.containsKey(key)) {
								hprd_phosphosite_incorrect_match_peptide_geneName.put(key,  key);
							}
						}						
					}
				}
			}
			in.close();
			
			System.out.println("######################################################");
			System.out.println("General Stats: ");
			System.out.println(uniq_peptide.size() + " Total Phosphopeptide");
			System.out.println(phosphosite_uniq_peptide.size() + " (" + calcRatio(phosphosite_uniq_peptide.size(), uniq_peptide.size()) + "%)" + " Phosphopeptides with known phosphosite annotation (PhosphositeDB)");
			System.out.println(motif_found_peptide.size() + " (" + calcRatio(motif_found_peptide.size(), uniq_peptide.size()) + "%)" + " Phosphopeptides with a predicted motif annotation");
			System.out.println("Network Stats: ");
			System.out.println(neighbor_evidence_peptide.size() + " (" + calcRatio(neighbor_evidence_peptide.size(), uniq_peptide.size()) + "%)" + " Phosphopeptides with a predicted motif annotation that has a known neighboring substrate from the same motif kinase");
			System.out.println(predicted_neighbor_peptide.size() + " (" + calcRatio(predicted_neighbor_peptide.size(), uniq_peptide.size()) + "%)" + " Phosphopeptides with a predicted motif annotation that has a predicted neighboring substrate from the same motif kinase");
			System.out.println("HPRD Motif Stats: ");
			System.out.println(hprd_phosphosite_match_peptide.size() + " (" + calcRatio(hprd_phosphosite_match_peptide.size(), (hprd_phosphosite_match_peptide.size() + hprd_phosphosite_incorrect_match_peptide.size())) + "%)" + " Phosphopeptides with matching predicted kinase with phosphosite annotation (Restricted to known HPRD genes)");
			System.out.println(hprd_phosphosite_incorrect_match_peptide.size() + " (" + calcRatio(hprd_phosphosite_incorrect_match_peptide.size(), (hprd_phosphosite_match_peptide.size() + hprd_phosphosite_incorrect_match_peptide.size())) + "%)" + " Phosphopeptides without matching predicted kinase with phosphosite annotation (Restricted to known HPRD genes)");			
			//System.out.println(phosphosite_match_peptide.size() + " (" + calcRatio(phosphosite_match_peptide.size(), (phosphosite_match_peptide.size() + phosphosite_incorrect_match_peptide.size())) + "%)" + " Phosphopeptides with matching predicted kinase with phosphosite annotation");
			//System.out.println(phosphosite_incorrect_match_peptide.size() + " (" + calcRatio(phosphosite_incorrect_match_peptide.size(), (phosphosite_match_peptide.size() + phosphosite_incorrect_match_peptide.size())) + "%)" + " Phosphopeptides without matching predicted kinase with phosphosite annotation");
			System.out.println();
			System.out.println("######################################################");
			System.out.println("General Stats: ");
			System.out.println(peptide_geneName.size() + " Total Peptide-Genes Combination");			
			System.out.println(phosphosite_peptide_geneName.size() + " (" + calcRatio(phosphosite_peptide_geneName.size(), peptide_geneName.size()) + "%)" + " Peptide-Genes with known phosphosite annotation (PhosphositeDB)");
			System.out.println(motif_found_peptide_geneName.size() + " (" + calcRatio(motif_found_peptide_geneName.size(), peptide_geneName.size()) + "%)" +  " Peptide-Genes with a predicted motif annotation");
			System.out.println("Network Stats: ");
			System.out.println(neighbor_evidence_peptide_geneName.size() + " (" + calcRatio(neighbor_evidence_peptide_geneName.size(), peptide_geneName.size()) + "%)" +  " Phosphopeptides with a predicted motif annotation that has a known neighboring substrate of the same kinase");
			System.out.println(predicted_neighbor_peptide_geneName.size() + " (" + calcRatio(predicted_neighbor_peptide_geneName.size(), peptide_geneName.size()) + "%)" +  " Phosphopeptides with a predicted motif annotation that has a predicted neighboring substrate the same kinase");			
			System.out.println("HPRD Motif Stats: ");
			System.out.println(hprd_phosphosite_match_peptide_geneName.size() + " (" + calcRatio(hprd_phosphosite_match_peptide_geneName.size(), (hprd_phosphosite_match_peptide_geneName.size() + hprd_phosphosite_incorrect_match_peptide_geneName.size())) + "%)" +  " Peptide-Genes with matching predicted kinase with phosphosite annotation (Restricted to known HPRD genes)");
			System.out.println(hprd_phosphosite_incorrect_match_peptide_geneName.size() + " (" + calcRatio(hprd_phosphosite_incorrect_match_peptide_geneName.size(), (hprd_phosphosite_match_peptide_geneName.size() + hprd_phosphosite_incorrect_match_peptide_geneName.size())) + "%)" +  " Peptide-Genes without matching predicted kinase with phosphosite annotation (Restricted to known HPRD genes)");
			
			//System.out.println(phosphosite_match_peptide_geneName.size() + " (" + calcRatio(phosphosite_match_peptide_geneName.size(), (phosphosite_match_peptide_geneName.size() + phosphosite_incorrect_match_peptide_geneName.size())) + "%)" +  " Peptide-Genes with matching predicted kinase with phosphosite annotation");
			//System.out.println(phosphosite_incorrect_match_peptide_geneName.size() + " (" + calcRatio(phosphosite_incorrect_match_peptide_geneName.size(), (phosphosite_match_peptide_geneName.size() + phosphosite_incorrect_match_peptide_geneName.size())) + "%)" +  " Peptide-Genes without matching predicted kinase with phosphosite annotation");			
			System.out.println();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double calcRatio(int num1, int num2) {
		double ratio = new Double(num1) / new Double(num2);
		return new Double(new Double(ratio * 10000).intValue()) / 100;
	}
	public static boolean overlapGeneStr(String str1, String str2) {
		String[] split1 = str1.split(",");
		String[] split2 = str2.split(",");
		for (String stuff1: split1) {
			for (String stuff2: split2) {
				if (!stuff1.equals("")) {
					if (stuff1.toUpperCase().equals(stuff2.toUpperCase())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		
		String peptide = "R.S#YELPDGQVITIGNER.F";
		String examine_peptide = peptide.split("\\.")[1];
		String new_peptide = MotifTools.replaceTag(examine_peptide);
		System.out.println(MotifTools.convert(new_peptide));
		
	}
	public static LinkedList addPossiblePeptide(String peptide) {
		String examine_peptide = peptide.split("\\.")[1];
		String new_peptide = MotifTools.replaceTag(examine_peptide);
		return MotifTools.convert(new_peptide);

	}
}
