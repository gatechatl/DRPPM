package expressionanalysis.tools.gsea;

/**
 * Generate gsea heatmap
 * need to install pheatmap
 * @author tshaw
 *
 */
public class GSEAHeatmap {

	public static void execute(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		System.out.println(generateGSEAHeatmap(inputFile, outputFile));
	}
	public static String generateGSEAHeatmap(String inputFile, String outputFile) {
		String script = "";

		script += "allDat = read.table(\"" + inputFile + "\", header=TRUE, row.names=1 );\n";
		script += "scaled = apply(allDat, 2, scale);\n";
		script += "all = apply(scaled, 1, rev)\n";
		//script += "newall = apply(all, 2, function(x) ifelse(x > 3, 3, x))\n";
		//script += "newall = apply(newall, 2, function(x) ifelse(x < -3, -3, x))\n";
		//script += "all = newall;\n";
		script += "colnames(all) = rownames(allDat)\n";
		script += "library(pheatmap)\n";
		script += "minimum = min(all);\n";
		script += "maximum = max(all);\n";
		script += "bk = c(seq(minimum,minimum/2, length=100), seq(minimum/2,maximum/2,length=100),seq(maximum/2,maximum,length=100))\n";
		script += "len = 50\n";
		script += "myBreaks <- c(seq(min(all), 0, length.out=ceiling(len/2) + 1),seq(max(all)/len, max(all), length.out=floor(len/2)))\n";		
		script += "hmcols<- colorRampPalette(c(\"dark blue\",\"blue\",\"white\",\"red\", \"dark red\"))(length(myBreaks)-1)\n";
		
		script += "png(file = \"" + outputFile + "\", width=1000,height=800)\n";
		script += "pheatmap(all, cluster_col = F, cluster_row = T, fontsize_row = 9, color=hmcols,breaks=myBreaks)\n";
		script += "dev.off();\n";
		return script;
	}
}
