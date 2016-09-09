package GraphsFigures;

/**
 * Given a matrix of values, generate the R boxplot script comparing two groups.
 * @author tshaw
 *
 */
public class BoxPlotGeneratorTwoGroup {

	public static String type() {
		return "GRAPH";
	}
	public static String description() {
		return "Given a matrix of values, generate the R boxplot script comparing two groups";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile] [type] [value] [group1] [group2]";
	}
	public static void execute(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		String type = args[2];
		String value = args[3];
		String group1 = args[4];
		String group2 = args[5];
		String script = generateViolinPlot(inputFile, outputFile, type, value, group1, group2);
		System.out.println(script);
	}
	public static String generateViolinPlot(String inputFile, String outputFile, String type, String value, String group1, String group2) {
		String script = " \n";
		script += "data = read.table(\"" + inputFile + "\", sep=\"\\t\",header=T);\n";
		script += group1 + " = data[which(data$Type==\"" + group1 + "\"),\"" + value + "\"]\n";
		script += group2 + " = data[which(data$Type==\"" + group2 + "\"),\"" + value + "\"]\n";
		script += group1 + "_vs_" + group2 + " = wilcox.test(" + group1 + ", " + group2 + ")\n";

		script += "text = paste(\"Wilcox Pvalue\", \"\\n\", \"" + group1 + "vs" + group2 + ":\", signif(" + group1 + "_vs_" + group2 + "$p.value, digits=2));\n";
		//script += "p1 = ggplot(data, aes(" + term + ", fill=Type)) + geom_density(alpha=0.5) + ggtitle(text) + theme(plot.title=element_text(size=11));\n";
		//script += "p1 = ggplot2.violinplot(data=data, aes(\" + term + \", fill=Type), xName='dose',yName='len', addDot=TRUE, dotSize=1, dotPosition=\"center\")\n";
		script += "p1 = ggplot(data, aes(factor(" + type + "), " + value + ", fill=" + type + ")) + geom_violin() + geom_boxplot(width=0.1) + ggtitle(text) + theme(plot.title=element_text(size=15), axis.text.x = element_text(size=15), axis.text.y = element_text(size=15), axis.title = element_text(size=15), legend.text = element_text(size=15), legend.title = element_text(size=15));\n";
		script += "png(file = \"" + outputFile + "\", width=700,height=500)\n";
		script += "p1\n";
		script += "dev.off();\n";
		return script;
	}
	/*
	public static String generatePvalueText(LinkedList list) {
		String result = "text = paste(\"Wilcox Pvalue\", \"\\n\", ";
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			
		}
	}*/
}
