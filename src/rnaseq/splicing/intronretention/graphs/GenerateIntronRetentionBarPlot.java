package rnaseq.splicing.intronretention.graphs;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenerateIntronRetentionBarPlot {

	public static void execute(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		String groupInfoFile = args[2];
		System.out.println(generateBarPlot(inputFile, outputFile, readGroupFile(groupInfoFile)));
	}
	public static String readGroupFile(String inputFile) {
		String result = "";
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (result.equals("")) {
					result += "\"" + str + "\"";
				} else {
					result += ",\"" + str + "\""; 
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String generateBarPlot(String inputFile, String outputFile, String groupInfo) {
		String script = "library(ggplot2);\n";
		script += "library(plyr);\n";
		script += "library(reshape2);\n";

		script += "data=read.csv(\"" + inputFile + "\", sep=\"\t\", header=T);\n";
		script += "data1 = cbind(data.frame(data = as.numeric(as.matrix(data[,2])),group=factor(c(" + groupInfo + "))));\n";

		script += "data2 = ddply(data1, .(group), summarise, mean=mean(data), sem=sd(data)/sqrt(length(data)));\n";

		script += "png(file = \"" + outputFile + "\", width=600,height=450);\n";

		script += "ggplot(data2, aes(x=group, y=mean)) + geom_bar(posiiton=\"dodge\", stat=\"identity\", fill=\"lightblue\", colour=\"black\", size=1) + geom_errorbar(aes(ymin=mean, ymax=mean+sem), size=0.5, width=0.6, position=position_dodge(.9));\n";

		script += "dev.off();\n";

		return script;
	}
}
