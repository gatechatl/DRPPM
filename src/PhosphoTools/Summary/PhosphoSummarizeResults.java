package PhosphoTools.Summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.StatisticsConversion;


/**
 * Generate output files for the publication table
 * https://en.wikipedia.org/wiki/Fisher's_method
 * -2 sum of all log (pvalue)
 * 
 * Add additional p value from gene enrichment result
 * @author tshaw
 *
 */
public class PhosphoSummarizeResults {
	
	public static void execute(String[] args) {
		
		try {
			HashMap upreg_motif = new HashMap();
			
			HashMap dnreg_motif = new HashMap();
			String inputFile = args[0];
			String upregFile = args[1];
			String dnregFile = args[2];			
			String phospho_input = args[3];
			String outputFile = args[4]; // contains all
			String outputFile_Phosphosite = args[5]; // contains only Phosphosite
			String outputFile_Network = args[6]; // contains only Network Support
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_phosphosite = new FileWriter(outputFile_Phosphosite);
			BufferedWriter out_phosphosite = new BufferedWriter(fwriter_phosphosite);
			
			FileWriter fwriter_network = new FileWriter(outputFile_Network);
			BufferedWriter out_network = new BufferedWriter(fwriter_network);			
			
			
			HashMap uniprot2geneName = new HashMap();
			FileInputStream fstream = new FileInputStream(phospho_input);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String uniprot = split[2];
				String geneName = split[4];
				uniprot2geneName.put(uniprot, geneName);
			}
			in.close();
			
			fstream = new FileInputStream(upregFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
				double hit = new Double(split[4]);				
				double noDiffFound = new Double(split[6]);
				
				double noHit = new Double(split[5]);
				double noDiffNotFound = new Double(split[7]);
				
				double ratio = hit / noDiffFound;
				double back_ratio = noHit / noDiffNotFound;
				if (ratio > back_ratio) {
					upreg_motif.put(split[0] + "\t" + split[1], split[2]);
				} else {
					upreg_motif.put(split[0] + "\t" + split[1], "" + (new Double(split[2]) * -1));
				}
			}
			in.close();
			
			
			fstream = new FileInputStream(dnregFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
				dnreg_motif.put(split[0], split[1]);
				double hit = new Double(split[4]);				
				double noDiffFound = new Double(split[6]);
				
				double noHit = new Double(split[5]);
				double noDiffNotFound = new Double(split[7]);
				
				double ratio = hit / noDiffFound;
				double back_ratio = noHit / noDiffNotFound;
				if (ratio > back_ratio) {
					dnreg_motif.put(split[0] + "\t" + split[1], split[2]);
				} else {
					dnreg_motif.put(split[0] + "\t" + split[1], "" + (new Double(split[2]) * -1));
				}
			}
			in.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("GeneName" + "\t" + header + "\t" + "UpMotifEnrichPval" + "\t" + "DnMotifEnrichPval" + "\t" + "FinalScore" + "\n");
			out_phosphosite.write("GeneName" + "\t" + header + "\t" + "UpMotifEnrichPval" + "\t" + "DnMotifEnrichPval" + "\t" + "FinalScore" + "\n");
			out_network.write("GeneName" + "\t" + header + "\t" + "UpMotifEnrichPval" + "\t" + "DnMotifEnrichPval" + "\t" + "FinalScore" + "\n");
			
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
				String uniprot = split[0];
				String converted_geneName = (String)uniprot2geneName.get(uniprot);
				if (split.length >= 10) {
					String key = split[4] + "\t" + split[5];
					
					LinkedList zscore_list = new LinkedList();
					LinkedList weight_list = new LinkedList();
					String upreg_motif_pval = (String)upreg_motif.get(key);
					double upreg_motif_pval_double = new Double(upreg_motif_pval);
					if (upreg_motif_pval_double > 1) {
						upreg_motif_pval_double = 1;
					}
					
					if (upreg_motif_pval_double < 0) {
						upreg_motif_pval_double = 1;
					}
					if (upreg_motif_pval_double < 1E-15) {
						upreg_motif_pval_double = 1E-15;
					}
					double upreg_motif_zscore = StatisticsConversion.inverseCumulativeProbability(upreg_motif_pval_double / 2) * -1;
					String dnreg_motif_pval = (String)dnreg_motif.get(key);
					//double dnreg_motif_pval_double = Math.abs(new Double(dnreg_motif_pval));
					double dnreg_motif_pval_double = new Double(dnreg_motif_pval);
					if (dnreg_motif_pval_double > 1) {
						dnreg_motif_pval_double = 1;
					}
					
					if (dnreg_motif_pval_double < 0) {
						dnreg_motif_pval_double = 1;
					}
					if (dnreg_motif_pval_double < 1E-15) {
						dnreg_motif_pval_double = 1E-15;
					}
					double dnreg_motif_zscore = StatisticsConversion.inverseCumulativeProbability(dnreg_motif_pval_double / 2) * -1;
					
					zscore_list.add(upreg_motif_zscore);
					weight_list.add(0.5);
					zscore_list.add(dnreg_motif_zscore);
					weight_list.add(0.5);
					
					String geneName = split[16];
					double correl_pval = 1;
					if (!split[14].equals("NA")) {
						correl_pval = new Double(split[14]);
						if (correl_pval > 1) {
							correl_pval = 1;
						}
						if (correl_pval < 1E-15) {
							correl_pval = 1E-15;
						}
					}
					
					double correl_pval_zscore = StatisticsConversion.inverseCumulativeProbability(correl_pval / 2) * -1;
					zscore_list.add(correl_pval_zscore);
					weight_list.add(1.0);
					String pssm_hit = split[8].split(",")[0];
					double pssm_hit_pval = 1;
					String[] pssm_geneNames = split[6].split(",");
					String[] pssm_pvals = split[7].split(",");
					boolean find = false;
					double pssm_pval = 1;
					for (int i = 0; i < pssm_geneNames.length; i++) {
						if (pssm_geneNames[i].equals(geneName) && pssm_geneNames.length == pssm_pvals.length) {
							find = true;
							if (pssm_pvals[i].equals("NA") || pssm_pvals[i].equals("NAX")) {
								pssm_pval = 1;
							} else {
								pssm_pval = new Double(pssm_pvals[i]);
							}
							
							if (pssm_pval > 1) {
								pssm_pval = 1;
							}
							if (pssm_pval < 1E-15) {
								pssm_pval = 1E-15;
							}
						}
						if (pssm_geneNames[i].equals(pssm_hit) && !pssm_hit.equals("NA")) {
							if (!split[9].equals("NAX")) {
								pssm_hit_pval = new Double(split[9].split(",")[0]);								
								if (pssm_hit_pval > 1) {
									pssm_hit_pval = 1;
								}
								if (pssm_hit_pval < 1E-8) {
									pssm_hit_pval = 1E-8;
								}
							} else {
								pssm_hit_pval = 1;
							}
						}
					}
					double pssm_pval_zscore = StatisticsConversion.inverseCumulativeProbability(pssm_pval / 2) * -1;
					zscore_list.add(pssm_pval_zscore);
					weight_list.add(1.0);
					
					double pssm_hit_pval_zscore = StatisticsConversion.inverseCumulativeProbability(pssm_hit_pval / 2) * -1;					
					zscore_list.add(pssm_hit_pval_zscore);
					weight_list.add(1.0);	
					
					String neighbor = split[18];
					double neighbor_zscore = StatisticsConversion.inverseCumulativeProbability(new Double(1E-15) / 2) * -1;
					zscore_list.add(neighbor_zscore);
					if (neighbor.contains("present_found_neighbor")) {
						weight_list.add(4.0);
					} else if (neighbor.contains("present_found_predicted_neighbor")) {
						weight_list.add(2.0);
					} else {
						weight_list.add(0.0);
					}
					
					double score = calculate_score(zscore_list, weight_list);
					out.write(converted_geneName + "\t" + str + "\t" + upreg_motif_pval_double + "\t" + dnreg_motif_pval_double + "\t" + score + "\n");
					
					if (neighbor.contains("neighbor")) {
						out_network.write(converted_geneName + "\t" + str + "\t" + upreg_motif_pval_double + "\t" + dnreg_motif_pval_double + "\t" + score + "\n");
					}
					if (!split[11].equals("NA")) {
						out_phosphosite.write(converted_geneName + "\t" + str + "\t" + upreg_motif_pval_double + "\t" + dnreg_motif_pval_double + "\t" + score + "\n");
					}
				}
			}
			in.close();
			
			out.close();
			out_network.close();
			out_phosphosite.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double calculate_score(LinkedList Zis, LinkedList weights) {
		
		double score = 0;
		Iterator itr = Zis.iterator();
		Iterator itr_weight = weights.iterator();
		while (itr.hasNext()) {
			double Zi = (Double)itr.next();
			double weight = (Double)itr_weight.next();
			score += weight * Zi;
		}		
		double den = 0;
		
		itr = Zis.iterator();
		itr_weight = weights.iterator();
		while (itr.hasNext()) {
			double Zi = (Double)itr.next();
			double weight = (Double)itr_weight.next();
			den += weight * weight;
		}
		return score / Math.sqrt(den);
	}
}
