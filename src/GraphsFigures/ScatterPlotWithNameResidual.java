package GraphsFigures;

import misc.CommandLine;


/**
 * Show scatter plot with name and residuals
 * Should not be executed in parallel
 * @author tshaw
 *
 */
public class ScatterPlotWithNameResidual {

	public static String parameter_info() {
		return "[inputFile] [xlabel] [ylabel] [geneLabel] [outputResid] [outputImg]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String xlabel = args[1];
			String ylabel = args[2];
			String geneLabel = args[3];
			String outputResid = args[4];
			String outputImg = args[5];
			
			String script = print_residual_script(inputFile, xlabel, ylabel, outputResid);
			CommandLine.writeFile("printresidual.r", script);
			CommandLine.executeCommand("R --vanilla < printresidual.r");
			
			script = generate_scatterplot_script(inputFile, xlabel, ylabel, geneLabel, outputImg);
			CommandLine.writeFile("generate_scatterplot_script.r", script);
			CommandLine.executeCommand("R --vanilla < generate_scatterplot_script.r");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String print_residual_script(String inputFile, String xlabel, String ylabel, String outputFile) {
		String script = "";
		script += "library(ggplot2);\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T);\n";
		script += "m1 <- lm(data$" + ylabel + "~data$" + xlabel + ")\n";
		script += "sd = sd(resid(m1)) * 1.5\n";
		script += "data$resid = resid(m1)\n";
		script += "write.table(data, file=\"" + outputFile + "\", sep=\"\t\");\n";
		//script += "plot(density(resid(m1)))\n";		
		return script;
	}
	public static String generate_scatterplot_script(String inputFile, String xlabel, String ylabel, String geneLabel, String outputFile) {
		String script = "";
		script += "library(ggplot2);\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T);\n";
		script += "m1 <- lm(data$" + ylabel + "~data$" + xlabel + ")\n";
		script += "sd = sd(resid(m1)) * 1.5\n";
		script += "data$resid = abs(resid(m1))\n";
		script += "data[abs(data$resid) < sd,]$GeneName = \"\"\n";		
		script += "png(file = \"" + outputFile + "\", width=1000,height=900)\n";
		script += "ggplot(data=data,aes(x=" + xlabel + ",y=" + ylabel + ",label=" + geneLabel + ")) + geom_point(alpha=0.4, size=2.0) + geom_text(size=9,aes(colour=8), alpha=0.8) + xlim(c(0,1.0)) + ylim(c(0,1.0));\n";
		script += "dev.off()\n";
		
		return script;
	}
}
