package moffitt.shawlab.aml.checkskippedexons;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class TestForEnrichedExons {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap boneMarrow_map = new HashMap();
			

			HashMap all_candidates = new HashMap();
			//String inputPSIPSOOutput = "/Users/4472414/Projects/AML_Cohort/ExonSplicing/PutativeASCandidates.txt";
			String inputPSIPSOOutput = "/Users/4472414/Projects/AML_Cohort/ExonSplicing/PutativeAlternativeSplicedCandidates.txt";
			FileInputStream fstream = new FileInputStream(inputPSIPSOOutput);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");

				all_candidates.put(split[0], split[0]);
			}
			in.close();
			int step = 1000;
			int index = 0;
			int max_index = index + step;
			if (max_index > all_candidates.size()) {
				max_index = all_candidates.size() - 1;
			}
			while (index < all_candidates.size()) {
				HashMap candidates_aml = new HashMap();
				HashMap candidates_bm = new HashMap();
				for (int i = index; i < max_index; i++) {
					
					String exon = (String)all_candidates.get(i);
					candidates_aml.put(exon, new LinkedList());
					candidates_bm.put(exon, new LinkedList());
					
				}
				
				String folderPath = "/Users/4472414/Projects/AML_Cohort/ExonSplicing/psi_clean";
				File files = new File(folderPath);
				for (File f: files.listFiles()) {
					boolean firstLine = true;
					String inputPSIPSOFile = f.getPath();
					fstream = new FileInputStream(inputPSIPSOFile);
					din = new DataInputStream(fstream);
					in = new BufferedReader(new InputStreamReader(din));			
					while (in.ready()) {
						String str = in.readLine();
						if (!firstLine) {
							String[] split = str.split("\t");
							if (candidates_aml.containsKey(split[0])) {
								boolean bm_flag = false;
								
								if (inputPSIPSOFile.contains("-BM") || inputPSIPSOFile.contains("-R00") || inputPSIPSOFile.contains("TARGET-00-")) {
									bm_flag = true;
								}
								double psi = new Double(split[1]);
								if (Double.isFinite(psi)) {
									if (bm_flag) {
										LinkedList list = (LinkedList)candidates_bm.get(split[0]);
										list.add(psi);
										candidates_bm.put(split[0], list);
									} else {
										LinkedList list = (LinkedList)candidates_aml.get(split[0]);
										list.add(psi);
										candidates_aml.put(split[0], list);
									}
								}
							}
							
						}
						firstLine = false;
					}
					in.close();
				}
				
	
				String outputFile = "/Users/4472414/Projects/AML_Cohort/ExonSplicing/Exon_WilcoxTest_Result.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);			
				out.write("Exon\tpval\taml_median\tbm_median\texonid\n");;
				Iterator itr = candidates_aml.keySet().iterator();
				while (itr.hasNext()) {
					String exon = (String)itr.next();
					String[] split_exon = exon.split("\\.");
					if (split_exon.length >= 3) {
						String end = split_exon[split_exon.length - 1];
						String start = split_exon[split_exon.length - 2];
						String chr = split_exon[split_exon.length - 3];
						String exon_id = chr + ":" + start + "-" + end;
						LinkedList aml_list = (LinkedList)candidates_aml.get(exon);
						LinkedList bm_list = (LinkedList)candidates_bm.get(exon);
						double[] aml_double = MathTools.convertListDouble2Double(aml_list);
						double[] bm_double = MathTools.convertListDouble2Double(bm_list);
						double aml_median = Double.NaN;
						if (aml_double.length > 0) {
							aml_median = MathTools.median(aml_double);
						}				
						double bm_median = Double.NaN;
						if (bm_double.length > 0) {
							bm_median = MathTools.median(bm_double);
						}
						double pval = 1;
						if (aml_double.length >= 3 && bm_double.length >= 3) {
							pval = MathTools.WilcoxRankSumTest(aml_double, bm_double);
						}
						out.write(exon + "\t" + pval + "\t" + aml_median + "\t" + bm_median + "\t" + exon_id + "\n");
						out.flush();
					}
				}
				out.close();
				index = max_index + 1;
				max_index = index + step;
				if (max_index > all_candidates.size()) {
					max_index = all_candidates.size() - 1;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
