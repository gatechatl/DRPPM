package rnaseq.splicing.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import misc.CommandLine;

/**
 * Based on the GTF file calculate the exon distribution
 * Generate the exon length table and create plot comparing the length of the skipped exon compared to global
 * 
 * @author tshaw
 *
 */
public class CalculateExonDistribution {

	public static String parameter_info() {
		return "[skipped_fasta] [reference_file] [outputFile]";
	}
	public static void execute(String[] args) {		
		try {
			
			String skipped_fasta = args[0];
			String reference_file = args[1];
			String outputFile = args[2];
			HashMap map = new HashMap();
			String name = "";
			FileInputStream fstream = new FileInputStream(skipped_fasta);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					name = str;
				} else {
					if (map.containsKey(name)) {
						String seq = (String)map.get(name);
						seq += str;
						map.put(name, seq);
					} else {
						map.put(name, str);
					}
				}
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write("Type\tGeneName\tLength\n");
			fstream = new FileInputStream(reference_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				out.write("Ref\t" + str + "\n");
			}
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String seq = (String)map.get(key);
				out.write("Skipped\t" + key + "\t" + seq.length() + "\n");
			}
			out.close();
			
			String script = generateHistogram("./", outputFile, "Length");
			CommandLine.writeFile("hist_script.r", script);
			CommandLine.executeCommand("R --vanilla < hist_script.r");		
			
			script = generateViolinPlot("./", outputFile, "Length");
			CommandLine.writeFile("violin_script.r", script);
			CommandLine.executeCommand("R --vanilla < violin_script.r");		
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}
	public static String generateViolinPlot(String outputFolder, String all_prot_file, String term) {
		String script = "library(ggplot2);\n";
		script += "data = read.table(\"" + all_prot_file + "\", sep=\"\\t\",header=T);\n";
		script += "SKIPPED = data[which(data$Type==\"Skipped\"),\"" + term + "\"]\n";
		script += "REF = data[which(data$Type==\"Ref\"),\"" + term + "\"]\n";
		script += "Skipped_vs_Ref = wilcox.test(SKIPPED, REF)\n";
		script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"SkippedvsRef:\", signif(Skipped_vs_Ref$p.value, digits=2));\n";
		//script += "p1 = ggplot(data, aes(" + term + ", fill=Type)) + geom_density(alpha=0.5) + ggtitle(text) + theme(plot.title=element_text(size=11));\n";
		//script += "p1 = ggplot2.violinplot(data=data, aes(\" + term + \", fill=Type), xName='dose',yName='len', addDot=TRUE, dotSize=1, dotPosition=\"center\")\n";
		script += "p1 = ggplot(data, aes(factor(Type), " + term + ", fill=Type)) + geom_violin() + geom_boxplot(width=0.1) + ggtitle(text) + theme(plot.title=element_text(size=11));\n";
		script += "png(file = \"" + outputFolder + "/" + term + "_Violin.png" + "\", width=700,height=500)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
	
	public static String generateHistogram(String outputFolder, String all_prot_file, String term) {
		String script = "library(ggplot2);\n";
		//script += "require(gridExtra);\n";
		script += "data = read.table(\"" + all_prot_file + "\", sep=\"\\t\",header=T);\n";
		script += "SKIPPED = data[which(data$Type==\"Skipped\"),\"" + term + "\"]\n";
		script += "REF = data[which(data$Type==\"Ref\"),\"" + term + "\"]\n";
		script += "Skipped_vs_Ref = wilcox.test(SKIPPED, REF)\n";
		script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"SkippedvsRef:\", signif(Skipped_vs_Ref$p.value, digits=2));\n";
		//script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"GRvsPR:\", G_vs_P$p.value, \"GRvsGRPR:\", G_vs_GP$p.value, \"\\n\", \"GRvsHuman_Proteome:\", G_vs_O$p.value, \"PRvsGRPR:\", P_vs_GP$p.value, \"\\n\", \"PRvsHuman_Proteome:\", P_vs_O$p.value, \"GRPRvsHuman_Proteome:\", GP_vs_O$p.value);\n";
		script += "p1 = ggplot(data, aes(" + term + ", fill=Type)) + geom_density(alpha=0.5) + ggtitle(text) + theme(plot.title=element_text(size=11));\n";				
		script += "png(file = \"" + outputFolder + "/" + term + "_Histogram.png" + "\", width=700,height=500)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
}
