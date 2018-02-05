package microarray.tools.methylation.EPIC850K;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import misc.CommandLine;
import misc.FileTools;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import statistics.general.MathTools;

public class EPIC850KWilcoxonTestMethylation {


	public static String description() {
		return "Perform Mann Whitney U test between two population.";
	}
	public static String type() {
		return "METHYLATION";
	}
	public static String parameter_info() {
		return "[inputFileAfterBMIQNormalization] [group1Name_file] [group2Name_file] [fdr_cutoff] [outputFile] [outputFile_fdr_limit]";
	}
	public static void execute(String[] args) {
		
		try {
			//String[] group1 = {"SJALCL045615", "SJALCL017856", "SJALCL045611", "SJALCL045620", "SJALCL045610", "SJALCL017851", "SJALCL045616", "SJALCL045627", "SJALCL017861", "SJALCL017858", "SJALCL017846", "SJALCL045629", "SJALCL017847", "SJALCL045613"};
			//String[] group2 = {"SJNORM016314_G1-Thy5_34", "SJNORM016314_G3-Thy5_1a_3_48DP", "SJNORM016314_G4-Thy5_1a_3_48SP", "SJNORM016314_G2-Thy5_1a_48DP", "SJNORM016314_G5-Thy5_3_48SP", "SJNORM018231_G1-Thy9_34", "SJNORM018231_G3-Thy9_1a_3_48DP", "SJNORM018231_G4-Thy9_1a_3_48SP", "SJNORM018231_G2-Thy9_1a_48DP"};

			String inputFile = args[0]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_108399_methylation_table.txt";
			String group1Name_file = args[1];
			String group2Name_file = args[2];
			double fdr_cutoff = new Double(args[3]);			
			String outputFile = args[4]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\TumorVsNormalLeventaki_850K_methylation_table_WilcoxResult.txt";
			String outputFile_limit = args[5]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\TumorVsNormalLeventaki_850K_methylation_table_WilcoxResult_0.01_cutoff.txt";
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
			
			LinkedList group1_name_list = FileTools.readFileList(group1Name_file);
			LinkedList group2_name_list = FileTools.readFileList(group2Name_file);
			String[] group1 = new String[group1_name_list.size()];
			String[] group2 = new String[group2_name_list.size()];
			
			Iterator itr_group1 = group1_name_list.iterator();
			int k = 0;
			while (itr_group1.hasNext()) {
				String str = (String)itr_group1.next();
				group1[k] = str.trim();
				k++;
			}
			Iterator itr_group2 = group2_name_list.iterator();
			k = 0;
			while (itr_group2.hasNext()) {
				String str = (String)itr_group2.next();
				group2[k] = str.trim();
				k++;
			}
			
			LinkedList group1_index_list = new LinkedList();
			LinkedList group2_index_list = new LinkedList();
			

			String buffer = UUID.randomUUID().toString();
			String buffer_output = buffer + "_output";
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String[] header_split = in.readLine().split("\t");
			for (int i = 0; i < header_split.length; i++) {
				int hit = -1;
				for (int j = 0; j < group1.length; j++) {
					if (header_split[i].contains(group1[j]) && header_split[i].contains("AVG_Beta")) {
						group1_index_list.add(i);
					}
				}
				for (int j = 0; j < group2.length; j++) {
					if (header_split[i].contains(group2[j]) && header_split[i].contains("AVG_Beta")) {
						group2_index_list.add(i);
					}
				}
			}
			out_limit.write(header_split[0]);
			out.write(header_split[0]);
			
			System.out.println("Group1: " + group1_index_list.size());
			System.out.println("Group2: " + group2_index_list.size());
			FileWriter fwriter_buffer_pval = new FileWriter(buffer);
			BufferedWriter out_buffer_pval = new BufferedWriter(fwriter_buffer_pval);
			
			Iterator itr1 = group1_index_list.iterator();
			while (itr1.hasNext()) {
				int index = (Integer)itr1.next();
				out.write("\t" + header_split[index]);
				out_limit.write("\t" + header_split[index]);
			}
			Iterator itr2 = group2_index_list.iterator();
			while (itr2.hasNext()) {
				int index = (Integer)itr2.next();
				out.write("\t" + header_split[index]);
				out_limit.write("\t" + header_split[index]);
			}
			
			LinkedList line_list = new LinkedList();
			out.write("\tGroup1Avg\tGroup2Avg\tGroup2-Group1\tMannWhitneyUPval\tBH\tBonferroni\tHochberg\n");
			out_limit.write("\tGroup1Avg\tGroup2Avg\tGroup2-Group1\tMannWhitneyUPval\tBH\tBonferroni\tHochberg\n");
			while (in.ready()) {
				String str = in.readLine();
				//System.out.println(str);
				String[] split = str.split("\t");
				String line = "";
				//out.write(split[1]);
				line += split[0];
				LinkedList group1_val = new LinkedList();
				itr1 = group1_index_list.iterator();
				while (itr1.hasNext()) {
					int index = (Integer)itr1.next();
					if (!split[index].trim().equals("") && !split[index].trim().equals("NA")) {
						double M = Beta2M(new Double(split[index]));
						line += "\t" + M;
						//out.write("\t" + M);
						group1_val.add(M + "");
					} else {
						line += "\tNaN";
						
					}
				}
				LinkedList group2_val = new LinkedList();
				itr2 = group2_index_list.iterator();
				while (itr2.hasNext()) {
					int index = (Integer)itr2.next();
					if (!split[index].trim().equals("") && !split[index].trim().equals("NA")) {
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
					double mean_group1 = MathTools.mean(group1_vals);
					double mean_group2 = MathTools.mean(group2_vals);
					double diff_group2_group1 = mean_group2 - mean_group1;
					double pval = mannWhitney.mannWhitneyUTest(group1_vals, group2_vals);
					
					line += "\t" + mean_group1 + "\t" + mean_group2 + "\t" + diff_group2_group1 + "\t" + pval;
					out_buffer_pval.write(pval + "\n");
					//out.write(line + "\t" + pval + "\n");
				} else {
					line += "\tNA\tNA\tNA\t1.0";
					out_buffer_pval.write("1.0\n");
					//out.write(line + "\t1.0\n");
				}
				//
				line_list.add(line);
			}
			in.close();

			out_buffer_pval.close();
			
			String script = generateFDRScript(buffer, buffer_output);
			CommandLine.writeFile(buffer + "_fdr.r", script);
			CommandLine.executeCommand("R --vanilla < " + buffer + "_fdr.r");
			
			LinkedList pvals = new LinkedList();
			fstream = new FileInputStream(buffer_output);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				pvals.add(str);
			}
			in.close();
			
			Iterator itr = line_list.iterator();
			Iterator itr3 = pvals.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				String pval = (String)itr3.next();
				String[] split = pval.split("\t");
				line += "\t" + split[1] + "\t" + split[2] + "\t" + split[3];
				out.write(line + "\n");
				if (new Double(split[1]) < fdr_cutoff) {
					out_limit.write(line + "\n");
				}
			}
			out.close();
			out_limit.close();
			
			FileTools.deleteFile(buffer);
			FileTools.deleteFile(buffer_output);
			FileTools.deleteFile(buffer + "pvalue.r");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateFDRScript(String inputFile, String outputFile) {
		String script = "";
		script += "pval = read.table(\"" + inputFile + "\",colClasses='numeric');\n";
		script += "pval = as.array(pval[,1])\n";
		script += "BH = p.adjust(pval, method = \"BH\")\n";
		script += "bonferroni = p.adjust(pval, method = \"bonferroni\");\n";
		script += "hochberg = p.adjust(pval, method = \"hochberg\");\n";
		script += "data = cbind(BH, bonferroni, hochberg)\n";
		script += "write.table(data, file = \"" + outputFile + "\", sep = \"\t\");\n";

		return script;
	}
	public static double Beta2M(double beta) {
		return MathTools.log2((beta)/(1 - beta) + 0.0000000001);
	}
}
