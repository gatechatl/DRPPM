package GraphsFigures;

public class SingleScatterPlot {

	public static void execute(String[] args) {
		try {
			if (args.length == 2) {
				String inputFile = args[0];
				String outputFile = args[1];
				System.out.println(createScript(inputFile, outputFile));
			} else {
				String inputFile = args[0];
				String outputFile = args[1];
				String xlab = args[2];
				String ylab = args[3];
				int xindex = new Integer(args[4]);
				int yindex = new Integer(args[5]);
				System.out.println(createScript(inputFile, outputFile, xlab, ylab, xindex, yindex));				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String createScript(String inputFile, String outputFile, String xlab, String ylab, int xindex, int yindex) {
		String script = "library(ggplot2);\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=F);\n";
		script += "data1 = cbind(data.frame(as.numeric(as.matrix(data[," + xindex + "]))), data.frame(as.numeric(as.matrix(data[," + yindex + "]))));\n";
		//script += "data1 = cbind(data.frame(data[,3]), data.frame(data[,6]));\n";
		script += "colnames(data1) = c(\"" + xlab + "\", \"" + ylab + "\")\n";
		script += "png(file = \"" + outputFile + "\", width=600,height=450)\n";
		script += "d = ggplot(data1, aes(x=" + xlab + ", y=" + ylab + "))\n";
		script += "d = d + geom_point() + geom_smooth(method=lm, se=TRUE) + labs(title = paste(\"Kinase(Total) vs Substrate(Phospho) R =\", round(cor(as.numeric(as.matrix(data[," + xindex + "])),as.numeric(as.matrix(data[," + yindex + "]))), digits=4)));\n";
		//script += "d = d + geom_point() + geom_smooth(method=lm, se=TRUE);\n";
		script += "d\n";
		script += "dev.off()";
		return script;
	}
	public static String createScript(String inputFile, String outputFile) {
		String script = "library(ggplot2);\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=F);\n";
		script += "data1 = cbind(data.frame(as.numeric(as.matrix(data[,3]))), data.frame(as.numeric(as.matrix(data[,6]))));\n";
		//script += "data1 = cbind(data.frame(data[,3]), data.frame(data[,6]));\n";
		script += "colnames(data1) = c(\"Kinase_log_SC\", \"Substrate_log_SC\")\n";
		script += "png(file = \"" + outputFile + "\", width=600,height=450)\n";
		script += "d = ggplot(data1, aes(x=Substrate_log_SC, y=Kinase_log_SC))\n";
		script += "d = d + geom_point() + geom_smooth(method=lm, se=TRUE) + labs(title = paste(\"Kinase(Total) vs Substrate(Phospho) R =\", round(cor(as.numeric(as.matrix(data[,3])),as.numeric(as.matrix(data[,6]))), digits=4)));\n";
		//script += "d = d + geom_point() + geom_smooth(method=lm, se=TRUE);\n";
		script += "d\n";
		script += "dev.off()";
		return script;
	}
}
