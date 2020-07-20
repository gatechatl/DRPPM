package stjude.projects.jinghuizhang.immunesignature.activeorsuppressed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Tally a list of genesets enrichment for gene_mutations and categorize them as active or suppressed immune 
 * @author tshaw
 *
 */
public class JinghuiZhangTestActiveOrSuppressedImmune {

	
	public static void main(String[] args) {
		
		try {
			// first read the gene set categories
			HashMap geneset_up = new HashMap();
			HashMap geneset_down = new HashMap();
			String geneset_category_file = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\ImmuneActiveVsSuppressed\\ImmuneScoreFeatures_20200607.txt";
			FileInputStream fstream = new FileInputStream(geneset_category_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				geneset_up.put(split[0], split[4]);
				geneset_down.put(split[0], split[5]);
			}
			in.close();
			
			HashMap categories = new HashMap();
			HashMap categories_totalcount = new HashMap();
			HashMap categories_active = new HashMap();
			HashMap categories_inactive = new HashMap();
			
			HashMap result = new HashMap();
			String enrichment_file = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\immune_signature_enriched_in_particular_mutations\\PanCancerImmuneSignature_Enrichment.txt";
			fstream = new FileInputStream(enrichment_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (new Double(split[4]) < 0.05 && new Double(split[7]) > 0) {
					if (geneset_up.containsKey(split[0])) {
						String category = (String)geneset_up.get(split[0]);
						if (category.equals("Active")) {
							result.put(split[1] + "\t" + split[2] + "\t" + split[0] + "\t" + split[7] + "\tActive", str);
							categories.put(split[1] + "\t" + split[2], "");
							if (categories_totalcount.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_totalcount.get(split[1] + "\t" + split[2]);
								count++;
								categories_totalcount.put(split[1] + "\t" + split[2], count);
							} else {
								categories_totalcount.put(split[1] + "\t" + split[2], 1);
							}
							if (categories_active.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_active.get(split[1] + "\t" + split[2]);
								count++;
								categories_active.put(split[1] + "\t" + split[2], count);
							} else {
								categories_active.put(split[1] + "\t" + split[2], 1);
							}
						} else if (category.equals("Suppressed")) {
							result.put(split[1] + "\t" + split[2] + "\t" + split[0] + "\t" + split[7] + "\tSuppressed", str);
							categories.put(split[1] + "\t" + split[2], "");
							if (categories_totalcount.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_totalcount.get(split[1] + "\t" + split[2]);
								count++;
								categories_totalcount.put(split[1] + "\t" + split[2], count);
							} else {
								categories_totalcount.put(split[1] + "\t" + split[2], 1);
							}
							if (categories_inactive.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_inactive.get(split[1] + "\t" + split[2]);
								count++;
								categories_inactive.put(split[1] + "\t" + split[2], count);
							} else {
								categories_inactive.put(split[1] + "\t" + split[2], 1);
							}
						} else if (category.equals("Low_Immune")) {
							result.put(split[1] + "\t" + split[2] + "\t" + split[0] + "\t" + split[7] + "\tAbsent", str);
							categories.put(split[1] + "\t" + split[2], "");
							if (categories_totalcount.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_totalcount.get(split[1] + "\t" + split[2]);
								count++;
								categories_totalcount.put(split[1] + "\t" + split[2], count);
							} else {
								categories_totalcount.put(split[1] + "\t" + split[2], 1);
							}
							if (categories_inactive.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_inactive.get(split[1] + "\t" + split[2]);
								count++;
								categories_inactive.put(split[1] + "\t" + split[2], count);
							} else {
								categories_inactive.put(split[1] + "\t" + split[2], 1);
							}
						}
					}
				} else if (new Double(split[4]) < 0.05 && new Double(split[7]) < 0) {
					if (geneset_down.containsKey(split[0])) {
						String category = (String)geneset_down.get(split[0]);
						if (category.equals("Active")) {
							result.put(split[1] + "\t" + split[2] + "\t" + split[0] + "\t" + split[7] + "\tActive", str);
							categories.put(split[1] + "\t" + split[2], "");
							if (categories_totalcount.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_totalcount.get(split[1] + "\t" + split[2]);
								count++;
								categories_totalcount.put(split[1] + "\t" + split[2], count);
							} else {
								categories_totalcount.put(split[1] + "\t" + split[2], 1);
							}
							if (categories_active.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_active.get(split[1] + "\t" + split[2]);
								count++;
								categories_active.put(split[1] + "\t" + split[2], count);
							} else {
								categories_active.put(split[1] + "\t" + split[2], 1);
							}
						} else if (category.equals("Suppressed")) {
							result.put(split[1] + "\t" + split[2] + "\t" + split[0] + "\t" + split[7] + "\tSuppressed", str);
							categories.put(split[1] + "\t" + split[2], "");
							if (categories_totalcount.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_totalcount.get(split[1] + "\t" + split[2]);
								count++;
								categories_totalcount.put(split[1] + "\t" + split[2], count);
							} else {
								categories_totalcount.put(split[1] + "\t" + split[2], 1);
							}
							if (categories_inactive.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_inactive.get(split[1] + "\t" + split[2]);
								count++;
								categories_inactive.put(split[1] + "\t" + split[2], count);
							} else {
								categories_inactive.put(split[1] + "\t" + split[2], 1);
							}
						} else if (category.equals("Low_Immune")) {
							result.put(split[1] + "\t" + split[2] + "\t" + split[0] + "\t" + split[7] + "\tAbsent", str);
							categories.put(split[1] + "\t" + split[2], "");
							if (categories_totalcount.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_totalcount.get(split[1] + "\t" + split[2]);
								count++;
								categories_totalcount.put(split[1] + "\t" + split[2], count);
							} else {
								categories_totalcount.put(split[1] + "\t" + split[2], 1);
							}
							if (categories_inactive.containsKey(split[1] + "\t" + split[2])) {
								int count = (Integer)categories_inactive.get(split[1] + "\t" + split[2]);
								count++;
								categories_inactive.put(split[1] + "\t" + split[2], count);
							} else {
								categories_inactive.put(split[1] + "\t" + split[2], 1);
							}
						}
					}
				}				
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\ImmuneActiveVsSuppressed\\Active_vs_Suppressed_20200607.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Disease\tGene\tGeneset\tScore\tType\n");
			Iterator itr = result.keySet().iterator();
			while (itr.hasNext()) {
				String result_enrichment = (String)itr.next();
				
				out.write(result_enrichment + "\n");
			}
			out.close();
			
			String outputFileSummary = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\ImmuneActiveVsSuppressed\\Active_vs_Suppressed_Summary_20200607.txt";
			fwriter = new FileWriter(outputFileSummary);
			out = new BufferedWriter(fwriter);
			out.write("Disease\tGeneMut\tActive\tInactive\n");
			itr = categories.keySet().iterator();
			while (itr.hasNext()) {
				String type = (String)itr.next();
				double total = (Integer)categories_totalcount.get(type);
				double active = 0.0;
				double inactive = 0.0;
				if (categories_active.containsKey(type)) {
					active = (Integer)categories_active.get(type);
				}
				if (categories_inactive.containsKey(type)) {
					inactive = (Integer)categories_inactive.get(type);
				}
				out.write(type + "\t" + (active/total) + "\t" + (inactive/total) + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
