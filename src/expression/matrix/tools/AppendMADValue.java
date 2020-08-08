package expression.matrix.tools;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import misc.CommandLine;
import misc.FileTools;

import org.apache.commons.math3.stat.inference.MannWhitneyUTest;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import statistics.general.MathTools;

public class AppendMADValue {


	public static String description() {
		return "Grab the most variable probes.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [sampleName_file] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			//String[] group1 = {"SJALCL045615", "SJALCL017856", "SJALCL045611", "SJALCL045620", "SJALCL045610", "SJALCL017851", "SJALCL045616", "SJALCL045627", "SJALCL017861", "SJALCL017858", "SJALCL017846", "SJALCL045629", "SJALCL017847", "SJALCL045613"};
			//String[] group2 = {"SJNORM016314_G1-Thy5_34", "SJNORM016314_G3-Thy5_1a_3_48DP", "SJNORM016314_G4-Thy5_1a_3_48SP", "SJNORM016314_G2-Thy5_1a_48DP", "SJNORM016314_G5-Thy5_3_48SP", "SJNORM018231_G1-Thy9_34", "SJNORM018231_G3-Thy9_1a_3_48DP", "SJNORM018231_G4-Thy9_1a_3_48SP", "SJNORM018231_G2-Thy9_1a_48DP"};

			String inputFile = args[0]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\Leventaki_850K_108399_methylation_table.txt";
			String group1Name_file = args[1];
			//String group2Name_file = args[2];			
			String outputFile = args[2]; //"T:\\Computational Biology\\Timothy Shaw\\HPC_Migration_Temporary\\Leventaki\\common\\Report\\Biostats\\Methylation\\TumorVsNormalLeventaki_850K_methylation_table_WilcoxResult.txt";
			
			WilcoxonSignedRankTest wilcoxTest = new WilcoxonSignedRankTest();
			MannWhitneyUTest mannWhitney = new MannWhitneyUTest();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			//double[] list1 = {6, 8, 2, 4, 4, 5};
			//double[] list2 = {7, 10, 4, 3, 5, 6};
			//System.out.println(mannWhitney.mannWhitneyU(list1, list2));
			//System.out.println(mannWhitney.mannWhitneyUTest(list1, list2));
			//System.out.println(wilcoxTest.wilcoxonSignedRankTest(list1, list2, false));
			//System.exit(0);;
			
			LinkedList group1_name_list = FileTools.readFileList(group1Name_file);
			
			String[] group1 = new String[group1_name_list.size()];
			
			
			Iterator itr_group1 = group1_name_list.iterator();
			int k = 0;
			while (itr_group1.hasNext()) {
				String str = (String)itr_group1.next();
				group1[k] = str.trim();
				k++;
			}
			
			LinkedList group1_index_list = new LinkedList();
			

			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] header_split = header.split("\t");
			for (int i = 0; i < header_split.length; i++) {
				int hit = -1;
				for (int j = 0; j < group1.length; j++) {
					if (header_split[i].contains(group1[j])) {
						group1_index_list.add(i);
					}
				}
				
			}
		
			//out.write(header_split[0]);
			System.out.println(group1_name_list.size());
			System.out.println("Number of samples with matching names: " + group1_index_list.size());
			
			Iterator itr1 = group1_index_list.iterator();
			while (itr1.hasNext()) {
				int index = (Integer)itr1.next();
				//out.write("\t" + header_split[index]);
			
			}
			LinkedList mad_score_list = new LinkedList();
			
			//LinkedList line_list = new LinkedList();
			out.write(header + "\tMAD\n");
			
			//out_topVar.write("\tMAD\n");
			while (in.ready()) {
				String str = in.readLine();
				//System.out.println(str);
				String[] split = (str + "\tExtra").split("\t");
				String line = "";
				//out.write(split[1]);
				line += split[0];
				
				LinkedList group1_val = new LinkedList();
				itr1 = group1_index_list.iterator();
				while (itr1.hasNext()) {
					int index = (Integer)itr1.next();
					if (!split[index].trim().equals("") && !split[index].trim().equals("NA")) {
						
						line += "\t" + new Double(split[index]);
						//out.write("\t" + M);
						group1_val.add(new Double(split[index]));
					} else {
						line += "\tNaN";
						
					}
				}
				//System.out.println(group1_val.size());
				//double[] group1_vals = MathTools.convertListStr2Double(group1_val);
				if (group1_val.size() == group1_index_list.size()) {
					//System.out.println(split.length);
											
					double mad_score = MathTools.mad(group1_val);
					mad_score_list.add(mad_score);
					out.write(str + "\t" + mad_score + "\n");
					//line_list.add(line + "\t" + mad_score);			
					
				}
			}
			in.close();
			
			out.close();			
			
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
	
}
