package expressionanalysis.tools.unsupervised;

/**
 * Original MAD score calculation by Yu Liu
 * Example: /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/misc/AlexGoutStJudeCloudAnalysis/UnsupervisedClustering
 * @author tshaw
 *
 */
public class GenerateRScriptForCalculatingMADScores {

	public static String type() {
		return "R";
	}
	public static String description() {
		return "Perform MAD score calculation.";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			System.out.println(generate_MAD_script(inputFile, outputFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_MAD_script(String inputFile, String outputFile) {

		String script = "exp.in <- \"" + inputFile + "\"\n";

		script += "exp <- read.table(exp.in,sep=\"\\t\",header=T,row.names=1,quote=\"\")\n";

		script += "mad  <- NULL\n";
		script += "var <- NULL\n";

		script += "mad <- function(y) {\n";
		script += "    x <- as.numeric(y)\n";
		script += "    m <- median(x)\n";
		script += "    l <- length(x)\n";
		script += "    m.s <- abs(x - m)\n";
		script += "    m.o <- sum(m.s) / l\n";
		script += "    return(m.o)\n";
		script += "}\n";

		script += "### not used here.\n";
		script += "mad.lg <- function(y) {\n";
		script += "    x <- as.numeric(y)\n";
		script += "    x <- log10(x+0.01)\n";
		script += "    m <- median(x)\n";
		script += "    l <- length(x)\n";
		script += "    m.s <- abs(x - m)\n";
		script += "    m.o <- sum(m.s) / l\n";
		script += "    return(m.o)\n";
		script += "}\n";

		script += "mad <- apply(exp,1,mad)\n";
		script += "#mad.lg <- apply(exp,1,mad.lg)\n";
		script += "var <- apply(exp,1,function(x){var(as.numeric(x))})\n";

		script += "mad <- sort(mad,decreasing=T)\n";
		script += "out <- cbind(names(mad),mad[names(mad)],exp[names(mad),])\n";
		//script += "out <- cbind(names(mad),mad[names(mad)],var[names(mad)],exp[names(mad),])\n";
		//script += "colnames(out) <- c(\"Gene\",\"MAD\",\"VAR\",colnames(exp))\n";
		script += "colnames(out) <- c(\"Gene\",\"MAD\",colnames(exp))\n";
		script += "write.table(out,file=\"" + outputFile + "\",sep=\"\\t\",row.names=F,quote=F)\n";
		return script;
	}
}
