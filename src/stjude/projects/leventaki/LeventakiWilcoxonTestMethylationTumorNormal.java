package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import statistics.general.MathTools;

public class LeventakiWilcoxonTestMethylationTumorNormal {

	
	public static void main(String[] args) {
		
		try {
			String[] group1 = {"SJALCL045615", "SJALCL017856", "SJALCL045611", "SJALCL045620", "SJALCL045610", "SJALCL017851", "SJALCL045616", "SJALCL045627", "SJALCL017861", "SJALCL017858", "SJALCL017846", "SJALCL045629", "SJALCL017847", "SJALCL045613"};
			String[] group2 = {"SJNORM016314_G1-Thy5_34", "SJNORM016314_G3-Thy5_1a_3_48DP", "SJNORM016314_G4-Thy5_1a_3_48SP", "SJNORM016314_G2-Thy5_1a_48DP", "SJNORM016314_G5-Thy5_3_48SP", "SJNORM018231_G1-Thy9_34", "SJNORM018231_G3-Thy9_1a_3_48DP", "SJNORM018231_G4-Thy9_1a_3_48SP", "SJNORM018231_G2-Thy9_1a_48DP"};
			String outputFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\TumorVsNormalLeventaki_850K_methylation_table_WilcoxResult.txt";
			String outputFile_limit = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\TumorVsNormalLeventaki_850K_methylation_table_WilcoxResult_0.01_cutoff.txt";
			String inputFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_108399_methylation_table.txt";
			
			WilcoxonSignedRankTest wilcoxTest = new WilcoxonSignedRankTest();
			MannWhitneyUTest mannWhitney = new MannWhitneyUTest();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_limit = new FileWriter(outputFile_limit);
			BufferedWriter out_limit = new BufferedWriter(fwriter_limit);
			
			//double[] list1 = {6, 8, 2, 4, 4, 5};
			//double[] list2 = {7, 10, 4, 3, 5, 6};
			//System.out.println(mannWhitney.mannWhitneyU(list1, list2));
			//System.out.println(mannWhitney.mannWhitneyUTest(list1, list2));
			//System.out.println(wilcoxTest.wilcoxonSignedRankTest(list1, list2, false));
			//System.exit(0);;
			
			LinkedList group1_list = new LinkedList();
			LinkedList group2_list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String[] header_split = in.readLine().split("\t");
			for (int i = 0; i < header_split.length; i++) {
				int hit = -1;
				for (int j = 0; j < group1.length; j++) {
					if (header_split[i].contains(group1[j]) && header_split[i].contains("AVG_Beta")) {
						group1_list.add(i);
					}
				}
				for (int j = 0; j < group2.length; j++) {
					if (header_split[i].contains(group2[j]) && header_split[i].contains("AVG_Beta")) {
						group2_list.add(i);
					}
				}
			}
			out_limit.write(header_split[1]);
			out.write(header_split[1]);
			Iterator itr1 = group1_list.iterator();
			while (itr1.hasNext()) {
				int index = (Integer)itr1.next();
				out.write("\t" + header_split[index]);
				out_limit.write("\t" + header_split[index]);
			}
			Iterator itr2 = group2_list.iterator();
			while (itr2.hasNext()) {
				int index = (Integer)itr2.next();
				out.write("\t" + header_split[index]);
				out_limit.write("\t" + header_split[index]);
			}
			out.write("\tMannWhitneyUPval\n");
			out_limit.write("\tMannWhitneyUPval\n");
			while (in.ready()) {
				String str = in.readLine();
				//System.out.println(str);
				String[] split = str.split("\t");
				String line = "";
				//out.write(split[1]);
				line += split[1];
				LinkedList group1_val = new LinkedList();
				itr1 = group1_list.iterator();
				while (itr1.hasNext()) {
					int index = (Integer)itr1.next();
					if (!split[index].trim().equals("")) {
						double M = Beta2M(new Double(split[index]));
						line += "\t" + M;
						//out.write("\t" + M);
						group1_val.add(M + "");
					} else {
						line += "\tNaN";
					}
				}
				LinkedList group2_val = new LinkedList();
				itr2 = group2_list.iterator();
				while (itr2.hasNext()) {
					int index = (Integer)itr2.next();
					if (!split[index].trim().equals("")) {
						double M = Beta2M(new Double(split[index]));
						line += "\t" + M;
						//out.write("\t" + M);
						group2_val.add(M + "");
					} else {
						line += "\tNaN";
					}
				}
				double[] group1_vals = MathTools.convertListStr2Double(group1_val);
				double[] group2_vals = MathTools.convertListStr2Double(group2_val);
				if (group1_vals.length > 0 && group2_vals.length > 0) {
					double pval = mannWhitney.mannWhitneyUTest(group1_vals, group2_vals);
					if (pval < 0.032) {
						out_limit.write(line + "\t" + pval + "\n");
					}
					out.write(line + "\t" + pval + "\n");
				} else {
					out.write(line + "\t1.0\n");
				}
				//
			}
			in.close();
			out.close();
			out_limit.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double Beta2M(double beta) {
		return MathTools.log2((beta)/(1 - beta) + 0.0000000001);
	}
}
