package RNAseqTools.SingleCell.QC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import misc.CommandLine;

/**
 * Take the gene coverage file and generate boxplot
 * @author tshaw
 *
 */
public class PlotGeneSetBoxPlot {
	public static String type() {
		return "SingleCellQC";
	}
	public static String description() {
		return "Take the gene coverage file and generate boxplot";
	}
	public static String parameter_info() {
		return "[inputGeneCoverageFile] [outputRFormatMatrix] [outputRscript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFIle = args[0];
			String outputRFormatMatrix = args[1];
			String outputRscript = args[2];
			FileWriter fwriter = new FileWriter(outputRFormatMatrix);
		    BufferedWriter out = new BufferedWriter(fwriter);
		    out.write("GeneSetName\tValue\n");
		    
			FileInputStream fstream = new FileInputStream(inputFIle);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				for (int i = 1; i < split.length; i++) {
					double value = new Double(split[i]);
					out.write(geneName + "\t" + value + "\n");
				}
				
			}
			in.close();
			out.close();
			
			FileWriter fwriter2 = new FileWriter(outputRscript);
		    BufferedWriter out2 = new BufferedWriter(fwriter2);
		    out2.write(generateViolinPlot(outputRFormatMatrix, outputRFormatMatrix + ".png"));
		    out2.close();
		    
		    CommandLine.executeCommand("R --vanilla < " + outputRscript);
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateViolinPlot(String inputFile, String outputFile) {
		String script = " \n";
		script += "library(ggplot2)\n";
		script += "data = read.table(\"" + inputFile + "\", sep=\"\\t\",header=T);\n";

		script += "p1 = ggplot(data, aes(GeneSetName, Value)) + geom_violin() + geom_boxplot(width=0.1) + theme(plot.title=element_text(size=15), axis.text.x = element_text(size=15), axis.text.y = element_text(size=15), axis.title = element_text(size=15), legend.text = element_text(size=15), legend.title = element_text(size=15)) + geom_jitter(width = 0.2);\n";
		script += "png(file = \"" + outputFile + "\", width=700,height=500)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
}
