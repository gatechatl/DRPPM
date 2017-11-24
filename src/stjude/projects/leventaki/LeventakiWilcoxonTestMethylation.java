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

public class LeventakiWilcoxonTestMethylation {

	
	public static void main(String[] args) {
		
		try {
			//String[] group1 = {"SJALCL045615", "SJALCL017856", "SJALCL045611", "SJALCL045620", "SJALCL045610", "SJALCL017851", "SJALCL045616"};
			//String[] group2 = {"SJALCL045627", "SJALCL017861", "SJALCL017858", "SJALCL017846", "SJALCL045629", "SJALCL017847", "SJALCL045613"};
			
			String[] group1 = {"SJALCL045616", "SJALCL017851", "SJALCL014725", "SJALCL045620", "SJALCL045611", "SJALCL017856", "SJALCL014724", "SJALCL045615"};
			String[] group2 = {"SJALCL045614", "SJALCL014727", "SJALCL045613", "SJALCL017847", "SJALCL045629", "SJALCL017846", "SJALCL017858", "SJALCL045627"};
			//String[] group1 = {"SJALCL014724", "SJALCL014725", "SJALCL017844", "SJALCL017851", "SJALCL017856", "SJALCL017859", "SJALCL017863", "SJALCL045611", "SJALCL045615", "SJALCL045616", "SJALCL045620", "SJALCL045621"};
			//String[] group2 = {"SJALCL017846", "SJALCL017847", "SJALCL017858", "SJALCL045613", "SJALCL045614", "SJALCL045627", "SJALCL045629", "SJALCL045631"};
			String outputFile = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_methylation_table_WilcoxResult_20170926.txt";
			String outputFile_limit = "T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_methylation_table_WilcoxResult_0.01_cutoff_20170926.txt";
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
					if (header_split[i].contains(group1[j]) && header_split[i].contains("AVG_Beta") && !header_split[i].contains("_R")) {
						group1_list.add(i);
					}
				}
				for (int j = 0; j < group2.length; j++) {
					if (header_split[i].contains(group2[j]) && header_split[i].contains("AVG_Beta") && !header_split[i].contains("_R")) {
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
					if (pval < 0.01) {
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
