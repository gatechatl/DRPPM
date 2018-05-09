package graph.figures;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Given a matrix table generate bar plot for each samples
 * @author tshaw
 *
 */
public class BarPlotGenerator {

	public static String type() {
		return "GRAPH";
	}
	public static String description() {
		return "Given a matrix table generate bar plot for each gene";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [geneName] [outputPngFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String geneName = args[1];
			String outputPng = args[2];
			//String type = args[3]; // PROTEOMICS, FPKM, LOG2
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String sample_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals(geneName)) {
					double[] values = new double[split.length - 1];
					String[] samples = new String[split.length - 1];
					for (int i = 1; i < split.length; i++) {						
						values[i - 1] = new Double(split[i]);
						/*if (type.equals("PROTEOMICS")) {
							values[i - 1] = new Double(new Double(values[i - 1] * 100).intValue()) / 100;
						} else if (type.equals("RNASEQ")) {
							values[i - 1] = new Double(new Double(values[i - 1] * 1000).intValue()) / 1000;
						}*/
						samples[i - 1] = sample_header.split("\t")[i];
					}
					String script = generateBarPlot(values, samples, outputPng, geneName);
					System.out.println(script);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateBarPlot(double[] values, String[] samples, String outputFile, String title) {
		String script = "library(ggplot2)\n";
		script += "data = c(";
		for (int i = 0; i < values.length; i++) {
			if (i == 0) {
				script += values[i]; 
			} else {
				script += "," + values[i];
			}
		}
		script += ");\n";
		script += "samples = c(";
		for (int i = 0; i < samples.length; i++) {
			if (i == 0) {
				script += "\"" + samples[i] + "\""; 
			} else {
				script += ",\"" + samples[i] + "\"";
			}
		}
		script += ");\n";
		script += "a = data.frame(data)\n";
		script += "names(a) = c(\"Expression\");\n";
		script += "transform(a, Expression = as.numeric(Expression))\n";
		script += "a$Samples = samples\n";
		
		//script += "a = cbind(samples, data);\n";
		//script += "a = data.frame(a)\n";
		//script += "names(a) = c(\"Samples\", \"Expression\");\n";
		
		script += "png(file=\"" + outputFile + "\",width=700, height=500)\n";
		script += "ggplot(a, aes(x=Samples, y=Expression)) + geom_bar(width=0.7, stat=\"identity\", fill=\"lightgray\") + theme_bw() + theme(axis.text.x = element_text(angle = 90, hjust=1, vjust=0.2)) + scale_x_discrete(limits = samples) + ggtitle(\"" + title + "\");\n";
		script += "dev.off()\n";
		return script;
	}
}
