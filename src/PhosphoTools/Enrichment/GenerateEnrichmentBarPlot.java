package PhosphoTools.Enrichment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import misc.CommandLine;

public class GenerateEnrichmentBarPlot {

	public static String parameter_info() {
		return "[enrichmentFile] [pval_cutoff] [enrich_cutoff] [outputFileUp] [outputFileDn] [rscript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String enrichmentFile = args[0];
			double pval_cutoff = new Double(args[1]);
			double enrich_cutoff = new Double(args[2]);
			String outputFileUp = args[3];
			String outputFileDn = args[4];
			String outputRscript = args[5];
			
			
			FileWriter fwriterUp = new FileWriter(outputFileUp);
			BufferedWriter outUp = new BufferedWriter(fwriterUp);
			FileWriter fwriterDn = new FileWriter(outputFileDn);
			BufferedWriter outDn = new BufferedWriter(fwriterDn);
			outUp.write("Kinase\tPvalue\tEnrichment\n");
			outDn.write("Kinase\tPvalue\tEnrichment\n");
			FileInputStream fstream = new FileInputStream(enrichmentFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[1];
				double pval = new Double(split[2]);
				double foldenrichment = new Double(split[3]);				
				if (pval < pval_cutoff && foldenrichment > enrich_cutoff && !kinase.equals("")) {
					if (split[0].equals("UpRegList")) {
						outUp.write(kinase + "\t" + pval + "\t" + foldenrichment + "\n");
					} else if (split[0].equals("DnRegList") && !kinase.equals("")) {
						outDn.write(kinase + "\t" + pval + "\t" + foldenrichment + "\n");
					}
				}
			}
			in.close();
			outUp.close();
			outDn.close();
			
			String script = generateBarPlot(outputFileUp + ".png", outputFileUp, "Upregulated Kinase") + "\n";
			script += "\nrm(list=ls(all=TRUE))\n";
			script += generateBarPlot(outputFileDn + ".png", outputFileDn, "Dnregulated Kinase") + "\n";
			CommandLine.writeFile(outputRscript, script);
			CommandLine.executeCommand("R --vanilla < " + outputRscript);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateBarPlot(String outputFile, String UpRegFile, String title) {
		String script = "library(ggplot2);\n";
		script += "require(gridExtra);\n";
		script += "data = read.table(\"" + UpRegFile + "\", sep=\"\\t\",header=T);\n";
		script += "data <- transform(data, Kinase = reorder(Kinase,Enrichment))\n";
		script += "text = paste(\"" + title + "\");\n";
		script += "p1 = ggplot(data, aes(x=Kinase, y=Enrichment)) + coord_flip() + geom_bar(position=\"dodge\", alpha=0.5) + ggtitle(text) + theme(axis.text.x = element_text(angle = 90, hjust = 1))\n";
		script += "png(file = \"" + outputFile + "_BarPlot.png" + "\", width=500,height=800)\n";
		script += "p1;\n";
		script += "dev.off();\n";
		return script;
	}
}
