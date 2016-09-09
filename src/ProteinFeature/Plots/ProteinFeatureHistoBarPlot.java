package ProteinFeature.Plots;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import MISC.CommandLine;

public class ProteinFeatureHistoBarPlot {

	
	public static String parameter_info() {
		return "[all protein file] [GRPR File] [Histogram or BarPlot MetaFile] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			String file1 = args[0]; // all protein features
			String file2 = args[1]; // only GRPR sequences
			String metaFile = args[2];
			String outputFolder = args[3];
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(metaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String feature_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			in.close();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String type = (String)map.get(key);
				if (type.equals("BAR_PLOT")) {
					String script = generateBarPlot(outputFolder, file1, file2, key);
					CommandLine.writeFile("bar_script.r", script);
					CommandLine.executeCommand("R --vanilla < bar_script.r");
					
				} else if (type.equals("HISTOGRAM")){
					String script = generateHistogram(outputFolder, file1, key);
					CommandLine.writeFile("hist_script.r", script);
					CommandLine.executeCommand("R --vanilla < hist_script.r");		
					
					script = generateViolinPlot(outputFolder, file1, key);
					CommandLine.writeFile("violin_script.r", script);
					CommandLine.executeCommand("R --vanilla < violin_script.r");		
					
				}
			}
			
			//File f = new File("script.r");
			//f.delete();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateViolinPlot(String outputFolder, String all_prot_file, String term) {
		String script = "library(ggplot2);\n";
		script += "data = read.table(\"" + all_prot_file + "\", sep=\"\\t\",header=T);\n";
		script += "GR_ONLY = data[which(data$Type==\"GR_ONLY\"),\"" + term + "\"]\n";
		script += "PR_ONLY = data[which(data$Type==\"PR_ONLY\"),\"" + term + "\"]\n";
		script += "GRPR = data[which(data$Type==\"GRPR\"),\"" + term + "\"]\n";
		script += "Human_Proteome = data[which(data$Type==\"Human_Proteome\"),\"" + term + "\"]\n";;
		script += "G_vs_P = wilcox.test(GR_ONLY, PR_ONLY)\n";
		script += "G_vs_GP = wilcox.test(GR_ONLY, GRPR)\n";
		script += "P_vs_GP = wilcox.test(PR_ONLY, GRPR)\n";
		script += "G_vs_O = wilcox.test(GR_ONLY, Human_Proteome)\n";
		script += "P_vs_O = wilcox.test(PR_ONLY, Human_Proteome)\n";
		script += "GP_vs_O = wilcox.test(GRPR, Human_Proteome)\n";
		script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"GRvsPR:\", signif(G_vs_P$p.value, digits=2), \"\\nGRvsGRPR:\", signif(G_vs_GP$p.value, digits=2), \"\\n\", \"GRvsHuman_Proteome:\", signif(G_vs_O$p.value, digits=2), \"\\nPRvsGRPR:\", signif(P_vs_GP$p.value, digits=2), \"\\n\", \"PRvsHuman_Proteome:\", signif(P_vs_O$p.value, digits=2), \"\\nGRPRvsHuman_Proteome:\", signif(GP_vs_O$p.value, digits=2));\n";
		//script += "p1 = ggplot(data, aes(" + term + ", fill=Type)) + geom_density(alpha=0.5) + ggtitle(text) + theme(plot.title=element_text(size=11));\n";
		//script += "p1 = ggplot2.violinplot(data=data, aes(\" + term + \", fill=Type), xName='dose',yName='len', addDot=TRUE, dotSize=1, dotPosition=\"center\")\n";
		script += "p1 = ggplot(data, aes(factor(Type), " + term + ", fill=Type)) + geom_violin() + geom_boxplot(width=0.1) + ggtitle(text) + theme(plot.title=element_text(size=11));\n";
		script += "png(file = \"" + outputFolder + "/" + term + "_Violin.png" + "\", width=700,height=500)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
	
	public static String generateBarPlot(String outputFolder, String all_prot_file, String grpr_only_file, String term) {
		String script = "library(ggplot2);\n";
		script += "require(gridExtra);\n";
		script += "data = read.table(\"" + all_prot_file + "\", sep=\"\\t\",header=T);\n";
		script += "data2 = read.table(\"" + grpr_only_file + "\", sep=\"\\t\",header=T);\n";
		script += "p1 = ggplot(data, aes(" + term + ", fill=Type)) + geom_bar(position=\"dodge\", alpha=0.5)\n";
		script += "p2 = ggplot(data2, aes(" + term + ", fill=Type)) + geom_bar(position=\"dodge\",alpha=0.5)\n";
		script += "png(file = \"" + outputFolder + "/" + term + "_BarPlot.png" + "\", width=700,height=500)\n";
		script += "grid.arrange(p1, p2, ncol=2);\n";
		script += "dev.off();\n";
		return script;
	}
	public static String generateHistogram(String outputFolder, String all_prot_file, String term) {
		String script = "library(ggplot2);\n";
		//script += "require(gridExtra);\n";
		script += "data = read.table(\"" + all_prot_file + "\", sep=\"\\t\",header=T);\n";
		script += "GR_ONLY = data[which(data$Type==\"GR_ONLY\"),\"" + term + "\"]\n";
		script += "PR_ONLY = data[which(data$Type==\"PR_ONLY\"),\"" + term + "\"]\n";
		script += "GRPR = data[which(data$Type==\"GRPR\"),\"" + term + "\"]\n";
		script += "Human_Proteome = data[which(data$Type==\"Human_Proteome\"),\"" + term + "\"]\n";;
		script += "G_vs_P = wilcox.test(GR_ONLY, PR_ONLY)\n";
		script += "G_vs_GP = wilcox.test(GR_ONLY, GRPR)\n";
		script += "P_vs_GP = wilcox.test(PR_ONLY, GRPR)\n";
		script += "G_vs_O = wilcox.test(GR_ONLY, Human_Proteome)\n";
		script += "P_vs_O = wilcox.test(PR_ONLY, Human_Proteome)\n";
		script += "GP_vs_O = wilcox.test(GRPR, Human_Proteome)\n";
		//script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"GRvsPR:\", signif(G_vs_P$p.value, digits=2), \"GRvsGRPR:\", signif(G_vs_GP$p.value, digits=2), \"\\n\", \"GRvsHuman_Proteome:\", signif(G_vs_O$p.value, digits=2), \"PRvsGRPR:\", signif(P_vs_GP$p.value, digits=2), \"\\n\", \"PRvsHuman_Proteome:\", signif(P_vs_O$p.value, digits=2), \"GRPRvsHuman_Proteome:\", signif(GP_vs_O$p.value, digits=2));\n";
		script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"GRvsPR:\", signif(G_vs_P$p.value, digits=2), \"\\nGRvsGRPR:\", signif(G_vs_GP$p.value, digits=2), \"\\n\", \"GRvsHuman_Proteome:\", signif(G_vs_O$p.value, digits=2), \"\\nPRvsGRPR:\", signif(P_vs_GP$p.value, digits=2), \"\\n\", \"PRvsHuman_Proteome:\", signif(P_vs_O$p.value, digits=2), \"\\nGRPRvsHuman_Proteome:\", signif(GP_vs_O$p.value, digits=2));\n";
		//script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"GRvsPR:\", G_vs_P$p.value, \"GRvsGRPR:\", G_vs_GP$p.value, \"\\n\", \"GRvsHuman_Proteome:\", G_vs_O$p.value, \"PRvsGRPR:\", P_vs_GP$p.value, \"\\n\", \"PRvsHuman_Proteome:\", P_vs_O$p.value, \"GRPRvsHuman_Proteome:\", GP_vs_O$p.value);\n";
		script += "p1 = ggplot(data, aes(" + term + ", fill=Type)) + geom_density(alpha=0.5) + ggtitle(text) + theme(plot.title=element_text(size=11));\n";				
		script += "png(file = \"" + outputFolder + "/" + term + "_Histogram.png" + "\", width=700,height=500)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
}
