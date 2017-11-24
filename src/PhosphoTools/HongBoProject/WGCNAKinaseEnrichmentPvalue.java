package PhosphoTools.HongBoProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Based on Yuxin's WGCNA Result calculate the Kinase Enrichment
 * @author tshaw
 *
 */
public class WGCNAKinaseEnrichmentPvalue {

	public static String parameter_info() {
		return "[inputFile] [fastaFile] [clusterFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String fastaFile = args[1];
			String clusterFile = args[2];
			String enrichmentOutputFile = args[3];
			HashMap motif_map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0].split("\\|")[1] + "_" + split[1].split(",")[0].replaceAll("\\*", "");
				String motif = split[5];
				if (motif_map.containsKey(motif)) {
					LinkedList list = (LinkedList)motif_map.get(motif);
					if (!list.contains(name)) {
						list.add(name);
					}
					motif_map.put(motif, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(name);
					motif_map.put(motif, list);
				}
			}
			in.close();
			
			HashMap total_phosphosite = new HashMap();
			HashMap peptide2accession = new HashMap();
			
			fstream = new FileInputStream(fastaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\t");
					String accession = split[0].split("\\|")[1];
					String positions = split[1];
					String seq = in.readLine();
					String[] split2 = seq.split("\t");
					String peptide = split2[1];
					
					if (peptide2accession.containsKey(peptide)) {
						LinkedList list = (LinkedList)peptide2accession.get(peptide);
						for (String position: positions.split(",")) {
							position = position.replaceAll("\\*", "");
							total_phosphosite.put(accession + "_" + position, accession + "_" + position);
							list.add(accession + "_" + position);
							//System.out.println(accession + "\t" + position);
						}
						peptide2accession.put(peptide, list);		
						
					} else {
						LinkedList list = new LinkedList();
						for (String position: positions.split(",")) {
							position = position.replaceAll("\\*", "");
							total_phosphosite.put(accession + "_" + position, accession + "_" + position);
							list.add(accession + "_" + positions);
							//System.out.println(accession + "\t" + position);
						}
						peptide2accession.put(peptide, list);
						
					}
				}
			}
			in.close();
			
			HashMap phosphosite = new HashMap();
			HashMap clusterList = new HashMap();									
			fstream = new FileInputStream(clusterFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String peptide = split[0].replaceAll("M@", "M");
				peptide = peptide.replaceAll("\\@", "*");
				peptide = peptide.replaceAll("\\#", "*");
				peptide = peptide.replaceAll("\\%", "*");
				
				LinkedList combination = generateCombination(peptide);				
				Iterator itr2 = combination.iterator();
				while (itr2.hasNext()) {						
					String new_peptide = (String)itr2.next();
					//System.out.println(new_peptide);
					if (peptide2accession.containsKey(new_peptide)) {						
						//System.out.println("Hit?");
						LinkedList list = (LinkedList)peptide2accession.get(new_peptide);
						Iterator itr = list.iterator();
						while (itr.hasNext()) {
							String accession = (String)itr.next();
							phosphosite.put(accession, accession);
							
							/*String stuff = accession2geneName.get(accession.split("\t")[0]) + "\t" + accession + "\n";
							if (!outputGene.containsKey(stuff)) {
								outputGene.put(stuff, "");
								out.write(clusterFile + "\t" + accession2geneName.get(accession.split("\t")[0]) + "\t" + accession + "\n");
								count++;
							}*/							
						
						}										
					}
				}				
			}
			in.close();
			
			int total = total_phosphosite.size();
			int diff = phosphosite.size();
			
			FileWriter fwriter = new FileWriter(enrichmentOutputFile);
			BufferedWriter out = new BufferedWriter(fwriter);							
			out.write("Motif\tFE_pval\tCorrected_pval\tEnrichmentValue\tDiffMotif\tNodiffMotif\tDiffNoMotif\tNoDiffNoMotif\n");
			//System.out.println(total_phosphosite.size());
			//System.out.println(phosphosite.size());
			Iterator itr = motif_map.keySet().iterator();
			while (itr.hasNext()) {
				String motifName = (String)itr.next();
				int hit = 0;				
				LinkedList list = (LinkedList)motif_map.get(motifName);
				int totalMotif = list.size();
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String accession = (String)itr2.next();					
					if (phosphosite.containsKey(accession)) {
						hit++;
					} 
				}
				int a = hit;
				int b = totalMotif - hit;
				int c = diff - hit;
				int d = total - b - c;
				
				double enrichment_value = (hit / diff) / (totalMotif / total);
				double fisher_pvalue = MathTools.fisherTest(a, b, c, d);
				double corrected_fisher_pvalue = fisher_pvalue * motif_map.size();
				if (corrected_fisher_pvalue > 1) {
					corrected_fisher_pvalue = 1;
				}
				if (new Double(a) / b < (new Double(c) / d)) {
					fisher_pvalue = 1;
					corrected_fisher_pvalue = 1;
				}
				out.write(motifName + "\t" + fisher_pvalue + "\t" + corrected_fisher_pvalue + "\t" + enrichment_value + "\t" + a + "\t" + b + "\t" + c + "\t" + d + "\n");
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static LinkedList generateCombination(String peptide) {
		
		peptide = peptide.replaceAll("M@", "M");
		peptide = peptide.replaceAll("\\@", "*");
		peptide = peptide.replaceAll("\\#", "*");
		peptide = peptide.replaceAll("\\%", "*");
		
		LinkedList list = new LinkedList();
		String[] split = peptide.split("\\*");
		
		for (int i = 0; i < split.length - 1; i++) {
			String result = "";
			for (int j = 0; j <= i; j++) {
				result += split[j];
			}
			result += "*";
			for (int k = i + 1; k < split.length; k++) {
				result += split[k];
			}
			list.add(result);
		}
		return list;
	}
}
