package stjude.projects.jinghuizhang.mutations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import statistics.general.MathTools;
import misc.CommandLine;

/**
 * Custom script to generate boxplot to examine the immune score for each mutation and cancer type.
 * Example: /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/ImmuneSignatureAnalysis
 * @author tshaw
 *
 */
public class JinghuiZhangCustomBoxplotForImmuneSignaturesCleaner {

	public static String type() {
		return "Immune";
	}
	public static String description() {
		return "Custom script to generate boxplot to examine the immune score for each mutation and cancer type.";
	}
	public static String parameter_info() {
		return "[InputEnrichmentResult] [outputFolder]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0]; //
			String outputFolder = args[1];
			File f = new File(outputFolder);
			f.mkdir();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String immune_type = split[0].replaceAll(" ", "_");
				String disease_type = split[1];
				String gene = split[2];
				String name = immune_type + "_" + disease_type + "_" + gene;
				name = name.replaceAll("\\(", "_").replaceAll("\\)", "_");
				double ttest = new Double(split[3]);
				double wilcox = new Double(split[4]);
				if (wilcox < 0.05) {
					
					String outputData = outputFolder + "/" + name + ".txt";
					FileWriter fwriter = new FileWriter(outputData);
					BufferedWriter out = new BufferedWriter(fwriter);
					out.write("name\tvalue\t" + gene + "_Mut\n");
					int id = 1;
					for (String value: split[10].split(",")) {
						out.write("S" + id + "\t" + value + "\tyes(n=" + split[8] + ")\n");
						id++;
					}
					for (String value: split[11].split(",")) {
						out.write("S" + id + "\t" + value + "\tno(n=" + split[9] + ")\n");
						id++;
					}
					out.close();
					
					String script = "";
					script += "library(ggplot2)\n";
					script += "options(bitmapType='cairo')\n";
					script += "data = read.table(\"" + outputFolder + "/" + name + ".txt" + "\", sep=\"\\t\",header=T);\n";					
					script += "p1 = ggplot(data, aes(factor(" + gene + "_Mut), value, fill=" + gene + "_Mut)) + geom_boxplot(width=0.5) + geom_point(pch = 21, position = position_jitterdodge()) + ggtitle(\"" + name + "_wilcox_pval:" + MathTools.thousandth_dec(wilcox) + "\") + theme(plot.title=element_text(size=15), axis.text.x = element_text(size=15), axis.text.y = element_text(size=15), axis.title = element_text(size=15), legend.text = element_text(size=15), legend.title = element_text(size=15));\n";
					script += "png(file = \"" + outputFolder + "/" + name + ".png" + "\", width=700,height=500)\n";
					script += "p1\n";
					script += "dev.off();\n";
					CommandLine.writeFile(outputFolder + "/" + name + ".r", script);
					CommandLine.executeCommand("R --vanilla < " + outputFolder + "/" + name + ".r");
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
			
}
