package graph.figures;

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

public class BoxplotExpressionForEachSample {

	public static String type() {
		return "GRAPH";
	}
	public static String description() {
		return "Create boxplot violinplot for each sample based on their expression value.";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [sampleNameFile] [outputMatrixFile] [outputRScript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String sampleNameFile = args[1];
			String outputMatrixFile = args[2];
			String outputRScript = args[3];
			
			LinkedList sampleName = new LinkedList();

			FileWriter fwriter = new FileWriter(outputMatrixFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tValue\n");
			FileInputStream fstream = new FileInputStream(sampleNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				sampleName.add(str);
			}
			in.close();

			HashMap index2sampleName = new HashMap();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 0; i < split.length; i++) {
					if (sampleName.contains(split_header[i])) {
						if (!split[i].replaceAll("\"", "").equals("NA")) {
							out.write(split_header[i] + "\t" + split[i].replaceAll("\"",  "") + "\n");
						}
					}
				}
			}
			in.close();
			out.close();
			
			FileWriter fwriter2 = new FileWriter(outputRScript);
		    BufferedWriter out2 = new BufferedWriter(fwriter2);
		    out2.write(generateViolinPlot(outputMatrixFile, outputMatrixFile + ".png"));
		    out2.close();			    
		    CommandLine.executeCommand("R --vanilla < " + outputRScript);			    
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateViolinPlot(String inputFile, String outputPNGFile) {
		String script = " \n";
		script += "library(ggplot2)\n";
		script += "data = read.table(\"" + inputFile + "\", sep=\"\\t\",header=T);\n";
		//script += "p1 = ggplot(data, aes(SampleName, Value)) + geom_violin() + geom_boxplot(width=0.1) + theme(plot.title=element_text(size=12), axis.text.x = element_text(size=12), axis.text.y = element_text(size=12), axis.title = element_text(size=12), legend.text = element_text(size=12), legend.title = element_text(size=12)) + geom_jitter(width = 0.2) + theme(axis.text.x = element_text(angle = 90, hjust = 1));\n";
		script += "p1 = ggplot(data, aes(SampleName, Value)) + geom_violin() + geom_boxplot(width=0.1) + theme(plot.title=element_text(size=35), axis.text.x = element_text(size=10), axis.text.y = element_text(size=10), axis.title = element_text(size=35), legend.text = element_text(size=35), legend.title = element_text(size=35)) + theme(axis.text.x = element_text(angle = 90, hjust = 1));\n";
		script += "png(file = \"" + outputPNGFile + "\", width=1600,height=1000)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
}
