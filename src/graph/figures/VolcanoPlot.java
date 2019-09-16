package graph.figures;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Take input from limma
 * @author tshaw
 *
 */
public class VolcanoPlot {

	public static String parameter_info() {
		return "[InputLIMMAoutputFile] [OutputPNG] [pvalue] [logFC] [annotation tag can leave blank] [optional fontsize]";
	}
	/**
	 * The label produced by the limma is offset by 1
	 * @param inputFile
	 * @param tag
	 * @return
	 */
	public static String hideLabelList(String inputFile, String tag) {
		String result = "c(";
		boolean first = true;
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");
			
			int index = -1;
			for (int i = 0; i < header.length; i++) {
				if (header[i].toUpperCase().equals(tag.toUpperCase())) {
					index = i;
				}
			}
			
			while (in.ready()) {					
				String str = in.readLine();				
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll("\"", "");
				if (index == -1) {
					if (first) {
						result += "'" + split[0] + "'";
					} else {
						result += ",'" + split[0] + "'";
					}
				} else {
					if (split[index + 1].equals("FALSE")) {
						if (first) {
							result += "''";
						} else {
							result += ",''";
						}
					} else {
						if (first) {
							result += "'" + split[0] + "'";
						} else {
							result += ",'" + split[0] + "'";
						}
					}
				}
				first = false;
			}
			in.close();
			result += ")";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void execute(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		if (args.length == 2) {
			System.out.println(createLIMMAVolcanoPlot(inputFile, outputFile));	
		} else if (args.length == 4) {
			String pvalue = args[2];
			String logfold = args[3];			
			System.out.println(createLIMMAVolcanoPlotTag(inputFile, outputFile, pvalue, logfold, "NOTAG"));
		} else if (args.length == 5) {
			String pvalue = args[2];
			String logfold = args[3];
			String tag = args[4];
			System.out.println(createLIMMAVolcanoPlotTag(inputFile, outputFile, pvalue, logfold, tag));
		}
		
	}
	public static String createLIMMAVolcanoPlotTag(String inputFile, String outputFile, String pvalue, String logfold, String tag) {
		String rownames = hideLabelList(inputFile, tag);
		String script = "";
		script += "library(ggplot2);\n";
		script += "options(bitmapType='cairo')\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, row.names=1);\n";

		script += "logFold = abs(data[,1]);\n";
		script += "pvalue = data[,4];\n";

		script += "PassPval = data$P.Value < 0.05;\n";
		script += "PassLG = data$logFold > 0.5;\n";

		//script += "rownames = rownames(data);\n";
		script += "rownames = " + rownames + ";\n";
		script += "rownames[data$P.Value > " + pvalue + "] = \"\";\n";
		script += "rownames[logFold < " + logfold + "] = \"\";\n";

		script += "png(file = \"" + outputFile + "\", width=2000,height=1400)\n";
		
		if (tag.equals("NOTAG")) {
			tag = "";
		}
		script += "ggplot(data=data, aes(x=logFC, y=-log10(P.Value), label=rownames)) + geom_point(alpha=0.4, size=2.0) + geom_text(size=12,aes(colour=8), alpha=0.9) + theme(axis.text = element_text(size=rel(4)),legend.position=\"none\") + theme(axis.title.y = element_text(size = rel(4))) + theme(axis.title.x = element_text(size = rel(4))) + theme(plot.title = element_text(size = rel(4))) + ggtitle(\"" + tag + "\");\n";

		script += "dev.off()";
		return script;
	}
	
	public static String createLIMMAVolcanoPlot(String inputFile, String outputFile) {
		String script = "";
		script += "library(ggplot2);\n";
		script += "options(bitmapType='cairo')\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, row.names=1);\n";

		//script += "logFold = data[,1];\n";
		//script += "pvalue = data[,4];\n";

		//script += "PassPval = data$P.Value < 0.05;\n";
		//script += "PassLG = data$logFold > 0.5;\n";

		/*script += "rownames = rownames(data);\n";
		script += "rownames[data$P.Value > 0.001] = \"\";\n";
		script += "rownames[data$logFold < 1] = \"\";\n";*/

		script += "png(file = \"" + outputFile + "\", width=2000,height=1300)\n";
		
		script += "ggplot(data=data, aes(x=logFC, y=-log10(P.Value))) + geom_point(alpha=0.4, size=2.0) + theme(axis.text = element_text(size=rel(5)),legend.position=\"none\")+ theme(axis.title.y = element_text(size = rel(5))) + theme(axis.title.x = element_text(size = rel(5)));\n";

		script += "dev.off()";
		return script;
	}
}
