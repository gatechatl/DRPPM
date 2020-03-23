package stjude.projects.jinghuizhang.rnaediting.proteomics;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Identify whether a particular RNA editing event peptide exist in the ID.txt
 * @author tshaw
 *
 */
public class JinghuiZhangProteomicsValidationRNAeditingTALL {

	public static void main(String[] args) {
		
		try {
			
			HashMap names = new HashMap();
			HashMap original_peptide = new HashMap();
			HashMap rnaedited_peptide = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\RNAediting\\OpenReadingFrame\\RNAeditPeptideList.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();				
				if (str.contains(">") && str.contains("ORIG_")) {					
					String peptide = in.readLine();					
					original_peptide.put(peptide, 0);
					names.put(str.replaceAll(">ORIG_", ""), peptide);
				}
				if (str.contains(">") && str.contains("RNAEDIT_")) {
					String peptide = in.readLine();
					rnaedited_peptide.put(peptide, 0);
					names.put(str.replaceAll(">RNAEDIT_", ""), peptide);					
				}
			}
			in.close();
			
			/*
			//inputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\ProteomicsRMS\\common\\HongWangProteomics\\COMET_SEARCH_RMS_whl2\\comet\\Combined_ID.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\ProteomicsRMS\\common\\HongWangProteomics\\Combined_ID_Relaxed\\Fraction_Combined_ID.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");
				String peptide = split[0].replaceAll("\\.", "").replaceAll("\\*", "");
				
				peptide = peptide.substring(0, peptide.length() - 1);
				
				if (original_peptide.containsKey(peptide)) {
					int count = (Integer)original_peptide.get(peptide);
					count++;
					original_peptide.put(peptide, count);
				}
				if (rnaedited_peptide.containsKey(peptide)) {
					int count = (Integer)rnaedited_peptide.get(peptide);
					count++;
					rnaedited_peptide.put(peptide, count);
				}
				
				String shorter_peptide = peptide.substring(1, peptide.length());
				
				if (original_peptide.containsKey(shorter_peptide)) {
					int count = (Integer)original_peptide.get(shorter_peptide);
					count++;
					original_peptide.put(shorter_peptide, count);
				}
				if (rnaedited_peptide.containsKey(shorter_peptide)) {
					int count = (Integer)rnaedited_peptide.get(shorter_peptide);
					count++;
					rnaedited_peptide.put(shorter_peptide, count);
				}
			}
			in.close();
			
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\ProteomicsRMS\\common\\HongWangProteomics\\Combined_ID\\Combined_ID.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");
				String peptide = split[0].replaceAll("\\.", "").replaceAll("\\*", "");
				
				peptide = peptide.substring(0, peptide.length() - 1);
				
				if (original_peptide.containsKey(peptide)) {
					int count = (Integer)original_peptide.get(peptide);
					count++;
					original_peptide.put(peptide, count);
				}
				if (rnaedited_peptide.containsKey(peptide)) {
					int count = (Integer)rnaedited_peptide.get(peptide);
					count++;
					rnaedited_peptide.put(peptide, count);
				}
				
				String shorter_peptide = peptide.substring(1, peptide.length());
				
				if (original_peptide.containsKey(shorter_peptide)) {
					int count = (Integer)original_peptide.get(shorter_peptide);
					count++;
					original_peptide.put(shorter_peptide, count);
				}
				if (rnaedited_peptide.containsKey(shorter_peptide)) {
					int count = (Integer)rnaedited_peptide.get(shorter_peptide);
					count++;
					rnaedited_peptide.put(shorter_peptide, count);
				}
			}
			in.close();
			*/
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\ProteomicsRMS\\common\\AnthonyHigh\\COMET_zhanggrp_121117_usp7_TMT\\sum_TALL_JUMPF_OUTPUT_20200212\\ID.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(";");
				String peptide = split[0].replaceAll("\\.", "").replaceAll("\\*", "");
				
				peptide = peptide.substring(0, peptide.length() - 1);
				
				if (original_peptide.containsKey(peptide)) {
					int count = (Integer)original_peptide.get(peptide);
					count++;
					original_peptide.put(peptide, count);
				}
				if (rnaedited_peptide.containsKey(peptide)) {
					int count = (Integer)rnaedited_peptide.get(peptide);
					count++;
					rnaedited_peptide.put(peptide, count);
				}
				
				String shorter_peptide = peptide.substring(1, peptide.length());
				
				if (original_peptide.containsKey(shorter_peptide)) {
					int count = (Integer)original_peptide.get(shorter_peptide);
					count++;
					original_peptide.put(shorter_peptide, count);
				}
				if (rnaedited_peptide.containsKey(shorter_peptide)) {
					int count = (Integer)rnaedited_peptide.get(shorter_peptide);
					count++;
					rnaedited_peptide.put(shorter_peptide, count);
				}
			}
			in.close();
			
			HashMap id_conversion = new HashMap();
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\RNAediting\\OpenReadingFrame\\RNAeditDatabase.lookup.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				id_conversion.put(split[0], split[1]);
			}
			in.close();
			
			Iterator itr = original_peptide.keySet().iterator();
			while (itr.hasNext()) {
				String peptide = (String)itr.next();
				int count = (Integer)original_peptide.get(peptide);
				if (count > 0) {
					String associated_name = "";
					Iterator itr2 = names.keySet().iterator();
					while (itr2.hasNext()) {
						String name = (String)itr2.next();
						
						String associated_peptide = (String)names.get(name);
						if (associated_peptide.equals(peptide)) {
							associated_name += name + ",";
						}
					}
					
					System.out.println("Original: " + associated_name + "\t" + peptide + "\t" + count);
				}
			}
			
			itr = rnaedited_peptide.keySet().iterator();
			while (itr.hasNext()) {
				String peptide = (String)itr.next();
				int count = (Integer)rnaedited_peptide.get(peptide);
				if (count > 0) {
					String associated_name = "";
					Iterator itr2 = names.keySet().iterator();
					while (itr2.hasNext()) {
						String name = (String)itr2.next();
						
						String associated_peptide = (String)names.get(name);
						if (associated_peptide.equals(peptide)) {
							associated_name += name + ",";
						}
					}
					System.out.println("RNAedited: " + associated_name + "\t" + peptide + "\t" + count);
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
