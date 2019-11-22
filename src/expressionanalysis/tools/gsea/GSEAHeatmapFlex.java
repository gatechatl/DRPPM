package expressionanalysis.tools.gsea;

/**
 * Generate gsea heatmap
 * need to install pheatmap
 * @author tshaw
 *
 */
public class GSEAHeatmapFlex {

	public static String description() {
		return "Generate ssGSEA Heatmap.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [width] [height] [clust_col] [clust_row] [outputFile] [annotation]";
	}
	public static void execute(String[] args) {
		String inputFile = args[0];
		
		int width = new Integer(args[1]);
		int height = new Integer(args[2]);
		boolean clust_col = new Boolean(args[3]);
		boolean clust_row = new Boolean(args[4]);
		String outputFile = args[5];
		String annotation = "";
		if (args.length > 5) {
			annotation = args[6];
		}
		System.out.println(generateGSEAHeatmap(inputFile, width, height, clust_col, clust_row, outputFile, annotation));
	}
	public static String generateGSEAHeatmap(String inputFile, int width, int height, boolean clust_col, boolean clust_row, String outputFile, String annotation_file) {
		String script = "";
		script += "options(bitmapType='cairo')\n";
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
		
		if (!annotation_file.equals("")) {
			script += "annotation = read.csv(file=\"" + annotation_file + "\",head=TRUE,sep=\"\\t\",stringsAsFactors=F,row.names = 1)\n";
		}

		script += "png(file = \"" + outputFile + "\", width=" + width + ",height=" + height + ")\n";
		String cluster_col_str = "F";
		if (clust_col) {
			cluster_col_str = "T";
		}
		String cluster_row_str = "F";
		if (clust_row) {
			cluster_row_str = "T";
		}
		if (!annotation_file.equals("")) {
			
			script += "pheatmap(all, cluster_col = " + cluster_col_str + ", cluster_row = " + cluster_row_str + ", fontsize_row = 9, color=hmcols,breaks=myBreaks, annotation_col=annotation)\n";
		} else {
			script += "pheatmap(all, cluster_col = " + cluster_col_str + ", cluster_row = " + cluster_row_str + ", fontsize_row = 9, color=hmcols,breaks=myBreaks)\n";
		}
		script += "dev.off();\n";
		return script;
	}
}
