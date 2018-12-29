package rnaseq.expression.tools;

public class ExpressionNormalization {

	public static void execute(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		System.out.println(quantNormScriptGen(args[0], args[1]));
	}
	
	public static String quantNormScriptGen(String inputFile, String outputFile) {

		String script = "data=read.csv(\"" + inputFile + "\", sep=\"\\t\", header=T, as.is=T, row.names = 1);\n";
		script += "mat = data;\n";
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
		script += "write.table(file=\"" + outputFile + "\", mat, sep=\"\\t\");\n";
		return script;
	}
}
