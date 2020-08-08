package rnaseq.tools.genelengthanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import misc.CommandLine;
import functional.pathway.enrichment.MapUtilsBig2Small;
import functional.pathway.enrichment.MapUtilsSmall2Big;

/**
 * Generate a sliding window from the longest to shortest transcript
 * Calculate a list of average fold change
 * @author tshaw
 *
 */
public class TranscriptLengthSlidingWindow {

	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Generate a sliding window from the longest to shortest transcript and calculate the average fold change";
	}
	public static String parameter_info() {
		return "[inputFile] [window_size] [outputFile] [ymin] [ymax]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap gene_length = new HashMap();
			HashMap gene_foldchange = new HashMap();
			String inputFile = args[0];
			int window_size = new Integer(args[1]);
			String outputFile = args[2];
			double ymin = new Double(args[3]);
			double ymax = new Double(args[4]);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				gene_length.put(split[0], new Double(split[1]));
				gene_foldchange.put(split[0], new Double(split[2]));
			}
			in.close();
			
			int index = 0;
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tLength\tFoldChange\n");
			gene_length = (HashMap) MapUtilsBig2Small.sortByValue(gene_length);
			LinkedList fold_change = new LinkedList();
			LinkedList length_list = new LinkedList();
			Iterator itr = gene_length.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				double length = (Double)gene_length.get(geneName);
				double fc = (Double)gene_foldchange.get(geneName);
				length_list.add(length);
				fold_change.add(fc);
				if (fold_change.size() == window_size) {
					
					double avg_fold_change = 0;
					Iterator itr2 = fold_change.iterator();
					while (itr2.hasNext()) {
						double num = (Double)itr2.next();
						avg_fold_change += num;
					}
					avg_fold_change = avg_fold_change / fold_change.size();
					double max_length = 0;
					double avg_length = 0;
					itr2 = length_list.iterator();
					while (itr2.hasNext()) {
						double num = (Double)itr2.next();
						avg_length += num;
						if (max_length < num) {
							max_length = num;
						}
					}
					avg_length = avg_length / length_list.size();
					index++;
					out.write("AvgGene" + index + "\t" + avg_length + "\t" + avg_fold_change + "\n");
					fold_change.clear();
					length_list.clear();
				}

			}
			out.close();
			
			String script = generateXYPlot(outputFile, outputFile + ".png", "Length", "FoldChange", ymin, ymax);
			CommandLine.writeFile("scatter_script.r", script);
			CommandLine.executeCommand("R --vanilla < scatter_script.r");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateXYPlot(String inputFile, String outputFile, String xlab, String ylab, double ymin, double ymax) {
		String script = "library(ggplot2);\n";
		script += "data1 = read.table(\"" + inputFile + "\", sep=\"\\t\",header=T);\n";
		script += "png(file = \"" + outputFile + "\", width=500,height=400)\n";
		script += "d = ggplot(data1, aes(x=" + xlab + ", y=" + ylab + "))\n";
		script += "d = d + geom_point() + geom_smooth(method=\"loess\") + coord_cartesian(ylim=c(" + ymin + ", " + ymax + ")) + scale_x_log10(breaks=c(10, 100, 1000, 10000, 100000, 1000000, 10000000)) + theme(text=element_text(size=20)) + xlab(\"Mean Transcript Length\") + ylab(\"Mean Log2 fold change\")\n";
		//script += "d = d + geom_point() + geom_smooth(method=lm, se=TRUE);\n";
		script += "d\n";
		script += "dev.off()";
		return script;
	}
}
