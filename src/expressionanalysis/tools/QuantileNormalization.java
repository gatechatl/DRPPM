package expressionanalysis.tools;

public class QuantileNormalization {

	public static String description() {
		return "Generate the quantile normalization script";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputFile] [take log flag as output true/false] [outputFile]";
	}
	public static void execute(String[] args) {
		
		String inputFile = args[0];
		boolean takeLog = new Boolean(args[1]);
		String outputFile = args[2];
		
		String script = "";
		script += "library(limma);\n";
		script += "#library(edgeR)\n";
		script += "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, as.is=T);\n";
		script += "gene=data[,1]\n";
		script += "allDat = data;\n";
		script += "selection = allDat;\n";
		script += "genenames = selection[,1];\n";
		script += "col_labels = colnames(allDat[1,]);\n";
		script += "sampleNames = col_labels[2:length(col_labels)];\n";
		script += "colnames(selection) = col_labels;\n";
		script += "rownames(selection) = genenames;\n";
		script += "mat = selection[, sampleNames];\n";
		//script += "numTop = 50;\n";
		script += "rownames(mat)=genenames\n";

		// need to make this a parameter
		script += "isexpr <- rowSums(mat>1) >= 1\n";
		script += "mat <- mat[isexpr,]\n";		
		
		script += "mat = log2(mat + 0.1);\n";
		
		script += "quantile_normalisation <- function(df){\n";
		script += "df_rank <- apply(df,2,rank,ties.method=\"min\")\n";
		script += "df_sorted <- data.frame(apply(df, 2, sort))\n";
		script += "df_mean <- apply(df_sorted, 1, mean)\n";			   
		script += "index_to_mean <- function(my_index, my_mean){\n";
		script += "return(my_mean[my_index])\n";
		script += "}\n";			   
		script += "df_final <- apply(df_rank, 2, index_to_mean, my_mean=df_mean)\n";
		script += "rownames(df_final) <- rownames(df)\n";
		script += "return(df_final)\n";
		script += "}\n";
		script += "mat = quantile_normalisation(mat);\n";
		if (!takeLog) {
			script += "mat = 2 ^ mat;\n";
		}
		script += "write.table(mat, file=\"" + outputFile + "\", sep=\"\t\");\n";
		System.out.println(script);
	}
}
