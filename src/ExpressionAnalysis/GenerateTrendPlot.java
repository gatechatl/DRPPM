package ExpressionAnalysis;

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

import MISC.CommandLine;
import Statistics.General.MathTools;

/**
 * Take in a matrix of gene set
 * Generate the expression trend
 * Original idea from Yuxin Li.
 * 
 * @author tshaw
 *
 */
public class GenerateTrendPlot {

	public static String description() {
		return "Generate the trend of the expression profiles.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[matrixFile] [geneSetFile] [cntrlGroup] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String matrixFile = args[0];
			String geneSetFile = args[1];
			String sampleGroup = args[2];
			String sampleGroupTitle = args[3];
			String cntrlGroup = args[4];
			String outputFolder = args[5];
			
			HashMap geneSets = new HashMap();
			FileInputStream fstream = new FileInputStream(geneSetFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneSetName = split[0];
				String fileName = split[1];
				FileInputStream fstream2 = new FileInputStream(fileName);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));				
				while (in2.ready()) {
					String str2 = in2.readLine();
					if (!str2.contains(">")) {
						if (geneSets.containsKey(geneSetName)) {
							LinkedList list = (LinkedList)geneSets.get(geneSetName);
							list.add(str2.trim());
							geneSets.put(geneSetName, list);						
							
						} else {
							LinkedList list = new LinkedList();
							list.add(str2.trim());
							geneSets.put(geneSetName, list);
						}
					}
				}
				in2.close();
			}
			in.close();
			
			
			LinkedList data = new LinkedList();
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header_str = in.readLine();
			data.add(header_str);
			while (in.ready()) {
				String str = in.readLine();
				data.add(str);
				//String[] split = str.split("\t");				
			}
			in.close();
			
			LinkedList cntrl = new LinkedList();
			String[] sampleGroupSplit = sampleGroup.split(":");
			LinkedList[] groups = new LinkedList[sampleGroupSplit.length];
			for (int i = 0; i < sampleGroupSplit.length; i++) {
				groups[i] = new LinkedList();
				String[] sampleGroupNamesSplit = sampleGroupSplit[i].split(",");
				for (int j = 0; j < sampleGroupNamesSplit.length; j++) {
					groups[i].add(sampleGroupNamesSplit[j]);
				}
			}
			
			String[] header_split = header_str.split("\t");
			for (int i = 1; i < header_split.length; i++) {				
				String[] cntrl_split = cntrlGroup.split(",");
				for (int j = 0; j < cntrl_split.length; j++) {
					if (header_split[i].equals(cntrl_split[j])) {
						cntrl.add(i);		
					}
				}	
			}
			
			Iterator itr = geneSets.keySet().iterator();			
			while (itr.hasNext()) {
				String geneSetName = (String)itr.next();
				LinkedList geneSet = (LinkedList)geneSets.get(geneSetName);
				
				String outputFile = outputFolder + "/" + geneSetName.replaceAll(" ", "_") + "_matrix.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);	
				

				LinkedList value_list = new LinkedList();
				LinkedList geneName_list = new LinkedList();
				Iterator itr2 = data.iterator();
				header_str = (String)itr2.next();
				//out.write(header_str + "\n");
				String[] sampleGroupTitleSplit = sampleGroupTitle.split(",");
				out.write(header_str.split("\t")[0]);
				for (String title: sampleGroupTitleSplit) {
					out.write("\t" + title);
				}
				out.write("\n");
				header_split = header_str.split("\t");
				while (itr2.hasNext()) {
					String line = (String)itr2.next();
					double cntrl_norm = 0.0;
					String[] split = line.split("\t");
					
					if (geneSet.contains(split[0])) {
						geneName_list.add(split[0]);
						out.write(split[0]);
						Iterator itr3 = cntrl.iterator();
						while (itr3.hasNext()) {
							int cntrl_index = (Integer)itr3.next();
							cntrl_norm += new Double(split[cntrl_index]);
						}
						cntrl_norm = (cntrl_norm / cntrl.size()) + 0.0001;
						
						//double[] values = new double[split.length - 1];
						double[] values = new double[groups.length];
						
						for (int i = 0; i < groups.length; i++) {
							double total_avg = 0;
							for (int j = 1; j < header_split.length; j++) {
								if (groups[i].contains(header_split[j])) {
									total_avg += new Double(split[j]);
								}
							}
							total_avg = total_avg / groups[i].size();
							double log2_value = MathTools.log2(total_avg / cntrl_norm);
							out.write("\t" + log2_value);
							values[i] = log2_value;
						}
						//System.out.println(cntrl_norm);
						/*for (int i = 1; i < split.length; i++) {
							
							double value = new Double(split[i]);
							double log2_value = MathTools.log2(value / cntrl_norm);
							out.write("\t" + log2_value);
							values[i - 1] = log2_value;
						}*/
						out.write("\n");
						value_list.add(values);
					} // check if 
					
					
				}				
				out.close();
				
				int index1 = 0;
				int max_index = -1;
				double max_correl = -1;
				Iterator itr4 = value_list.iterator();
				while (itr4.hasNext()) {
					double[] values1 = (double[])itr4.next();
					index1++;
					int index2 = 0;
					double total_correl = 0; 
					Iterator itr5 = value_list.iterator();
					while (itr5.hasNext()) {
						double[] values2 = (double[])itr5.next();
						index2++;
						if (index1 != index2) {
							total_correl += MathTools.PearsonCorrel(values1,  values2);
						}
					}
					if (max_correl < total_correl) {
						max_index = index1 - 1;
						max_correl = total_correl;
					}
				}
				
				String outputFile2 = outputFolder + "/" + geneSetName.replaceAll(" ", "_") + "_correlation.txt";
				FileWriter fwriter2 = new FileWriter(outputFile2);
				BufferedWriter out2 = new BufferedWriter(fwriter2);	
				double[] values1 = (double[])value_list.get(max_index);
				for (double value: values1) {
					out2.write(value + "\n");
				}
				out2.close();

				String outputFile3 = outputFolder + "/" + geneSetName.replaceAll(" ", "_") + "_rdata.txt";
				FileWriter fwriter3 = new FileWriter(outputFile3);
				BufferedWriter out3 = new BufferedWriter(fwriter3);	
				out3.write("GeneName\tIntensity\tGroup\tCorrelation\n");
				Iterator itr5 = geneName_list.iterator();
				itr4 = value_list.iterator();
				while (itr4.hasNext()) {
					String geneName = (String)itr5.next();
					double[] values_each = (double[])itr4.next();
					double correl = MathTools.PearsonCorrel(values_each, values1);
					if (correl < 0) {
						correl = 0;
					}
					for (int i = 0; i < values1.length; i++) {
						String title = sampleGroupTitle.split(",")[i];
						out3.write(geneName + "\t" + values_each[i] + "\t" + title + "\t" + correl + "\n");
					}										
				}
				out3.close();

				// add trend generation R script here;
				String outputPng = outputFolder + "/" + geneSetName.replaceAll(" ", "_") + ".png";
				String script = generate_correlation(outputFile3, outputPng);
				CommandLine.writeFile("plot_trend.r", script);
				CommandLine.executeCommand("R --vanilla < plot_trend.r");
				//File f = new File("plot_trend.r");
				//f.delete();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_correlation(String inputFile, String outputPngFile) {

		String script = "library(ggplot2);\n";
		script += "data = read.table(\"" + inputFile + "\", header = TRUE);\n";
		script += "png(\"" + outputPngFile + "\", width = 500, height = 300);\n";
		script += "ggplot(data, aes(x = Group, y = Intensity, group = GeneName, colour = Correlation)) + geom_line(aes(alpha = Correlation ^ 20 + 0.1)) + scale_colour_gradient(limits=c(0, 1), low=\"blue\", high=\"red\") + theme_bw() + xlab(\"\") + ylab(\"Abundance Change\") + theme(text = element_text(size=16), axis.text.x = element_text(size = 14), axis.text.y = element_text(size = 14));\n";
		script += "dev.off();\n";
		return script;
	}
}
