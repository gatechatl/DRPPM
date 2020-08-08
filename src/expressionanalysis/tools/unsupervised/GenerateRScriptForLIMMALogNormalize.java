package expressionanalysis.tools.unsupervised;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * R script for lognormalization
 * Example: /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/misc/AlexGoutStJudeCloudAnalysis/UnsupervisedClustering
 * @author tshaw
 *
 */
public class GenerateRScriptForLIMMALogNormalize {

	public static String type() {
		return "R";
	}
	public static String description() {
		return "Perform quantile based lognormalization ";
	}
	public static String parameter_info() {
		return "[inputFile] [buffer value before taking log: maybe set to 0.1] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			double buffer = new Double(args[1]);
			String outputFile = args[2];
			System.out.println(generate_R_script(inputFile, buffer, outputFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_R_script(String inputFile, double buffer, String outputFile) {

		String script = "library(limma)\n";

		script += "infile  <- \"" + inputFile + "\"\n";
		script += "outfile <- \"" + outputFile + "\"\n";

		script += "dat.raw <- read.table(infile,sep=\"\\t\",header=T,row.names=1,quote=\"\",stringsAsFactors=F)\n";
		script += "dat.raw <- as.matrix(dat.raw)\n";
		script += "dat.norm <- normalizeBetweenArrays(dat.raw,method=\"quantile\")\n";
		
		script += "dat.norm.lg2 <- log2(dat.norm + " + buffer + ")\n";
		script += "dat.norm.lg2.out <- cbind(rownames(dat.norm),dat.norm.lg2)\n";
		script += "colnames(dat.norm.lg2.out) <- c(\"Gene\",colnames(dat.norm))\n";

		script += "write.table(dat.norm.lg2.out,file=outfile,sep=\"\\t\", quote = F,row.names = F)\n";

		return script;

	}
}
