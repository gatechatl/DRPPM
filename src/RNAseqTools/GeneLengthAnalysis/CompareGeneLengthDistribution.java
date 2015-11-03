package RNAseqTools.GeneLengthAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import MISC.CommandLine;

public class CompareGeneLengthDistribution {

	public static String parameter_info() {
		return "[upRegInputFile] [dnRegInputFile] [reference] [outputFile] [outputFoldChange]";
	}
	public static void execute(String[] args) {
		
		try {
			String upRegInputFile = args[0];
			String dnRegInputFile = args[1];
			String allRegInputFile = args[2];
			String reference = args[3];
			String x_lab = args[4]; //TranscriptLength
			String outputFile = args[5];
			String outputFoldChange = args[6];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriterFC = new FileWriter(outputFoldChange);
			BufferedWriter outFC = new BufferedWriter(fwriterFC);
			
			HashMap map = new HashMap();
			
			HashMap upList = new HashMap();
			HashMap dnList = new HashMap();
			HashMap allList = new HashMap();
			
			FileInputStream fstream = new FileInputStream(reference);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			
			outFC.write("GeneName\t" + x_lab + "\tFoldChange\n");
			
			fstream = new FileInputStream(allRegInputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double aveExpr = new Double(split[2]);
				String foldChange = split[1];
				String geneName = split[0].replaceAll("\"", "");
				
				if (map.containsKey(geneName) && aveExpr > 2.0 && new Integer((String)map.get(geneName)) > 900) {
					allList.put(geneName, map.get(geneName));
					outFC.write(geneName + "\t" + map.get(geneName) + "\t" + foldChange + "\n");
				}
			}
			in.close();
			
			
			
			outFC.close();
			
			fstream = new FileInputStream(dnRegInputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].replaceAll("\"", "");
				if (map.containsKey(geneName)) {
					dnList.put(geneName, map.get(geneName));
				}
			}
			in.close();
			
			fstream = new FileInputStream(upRegInputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].replaceAll("\"", "");
				if (map.containsKey(geneName)) {
					upList.put(geneName, map.get(geneName));
				}
			}
			in.close();
			
			
			out.write("GeneName\t" + x_lab + "\tType\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (upList.containsKey(geneName)) {
					out.write(geneName + "\t" + map.get(geneName) + "\t" + "UpReg\n");
				} else if (dnList.containsKey(geneName)) {
					out.write(geneName + "\t" + map.get(geneName) + "\t" + "DnReg\n");
				} else if (allList.containsKey(geneName)) {
					out.write(geneName + "\t" + map.get(geneName) + "\t" + "Other\n");
				}				
			}
			out.close();
			
			String script = generateHistogram(outputFile + ".png", outputFile, x_lab);
			CommandLine.writeFile("script.r", script);
			CommandLine.executeCommand("R --vanilla < script.r");
			
			script = generateXYPlot(outputFoldChange, outputFoldChange + ".png", x_lab, "FoldChange");
			CommandLine.writeFile("scatter_script.r", script);
			CommandLine.executeCommand("R --vanilla < scatter_script.r");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateXYPlot(String inputFile, String outputFile, String xlab, String ylab) {
		String script = "library(ggplot2);\n";
		script += "data1 = read.table(\"" + inputFile + "\", sep=\"\\t\",header=T);\n";
		script += "png(file = \"" + outputFile + "\", width=1000,height=800)\n";
		script += "d = ggplot(data1, aes(x=" + xlab + ", y=" + ylab + "))\n";
		script += "d = d + geom_point() + geom_smooth(method=\"loess\") + scale_x_log10(breaks=c(10, 100, 1000, 10000, 100000, 1000000, 10000000)) + theme(text=element_text(size=30)) \n";
		//script += "d = d + geom_point() + geom_smooth(method=lm, se=TRUE);\n";
		script += "d\n";
		script += "dev.off()";
		return script;
	}
	public static String generateHistogram(String outputPng, String inputFile, String term) {
		String script = "library(ggplot2);\n";
		//script += "require(gridExtra);\n";
		script += "data = read.table(\"" + inputFile + "\", sep=\"\\t\",header=T);\n";
		script += "UpReg = data[which(data$Type==\"UpReg\"),\"" + term + "\"]\n";
		script += "DnReg = data[which(data$Type==\"DnReg\"),\"" + term + "\"]\n";
		script += "Other = data[which(data$Type==\"Other\"),\"" + term + "\"]\n";
		//script += "Human_Proteome = data[which(data$Type==\"Human_Proteome\"),\"" + term + "\"]\n";;
		script += "G_vs_P = wilcox.test(UpReg, DnReg)\n";
		script += "G_vs_GP = wilcox.test(UpReg, Other)\n";
		script += "P_vs_GP = wilcox.test(DnReg, Other)\n";
		//script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"GRvsPR:\", G_vs_P$p.value, \"GRvsOther:\", G_vs_GP$p.value, \"\\n\", \"GRvsHuman_Proteome:\", G_vs_O$p.value, \"PRvsOther:\", P_vs_GP$p.value, \"\\n\", \"PRvsHuman_Proteome:\", P_vs_O$p.value, \"OthervsHuman_Proteome:\", GP_vs_O$p.value);\n";
		script += "p1 = ggplot(data, aes(" + term + ", fill=Type)) + geom_density(alpha=0.5) + theme(plot.title=element_text(size=11));\n";				
		script += "png(file = \"" + outputPng + "\", width=700,height=500)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
}
