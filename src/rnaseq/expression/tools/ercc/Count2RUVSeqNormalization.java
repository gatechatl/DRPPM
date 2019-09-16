package rnaseq.expression.tools.ercc;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Perform normalization using RUVSeq
 * @author tshaw
 *
 */
public class Count2RUVSeqNormalization {


	public static String type() {
		return "RUVSeq";
	}
	public static String description() {
		return "Convert the group txt to a gmt.";
	}
	public static String parameter_info() {
		return "[pathFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputRawCount = args[0];
			String outputNormalizedCountFile = args[1];
			String outputScript = args[2];

			FileWriter fwriter = new FileWriter(outputScript);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(generateRUVSeqScript(inputRawCount, outputNormalizedCountFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generateRUVSeqScript(String inputRawCountEnsembl, String outputNormalizedCountFile) {
		String script = "library(RUVSeq);\n";
		script += "data = read.table(\"" + inputRawCountEnsembl + "\", sep= \"\\t\", header=T, row.names = 1);\n";
		script += "\n";
		script += "genes <- rownames(data)[grep(\"^ENS\", rownames(data))]\n";
		script += "spikes <- rownames(data)[grep(\"^ERCC\", rownames(data))]\n";
		script += "\n";
		script += "x <- as.factor(c(rep(c(\"cntrl\"), length(colnames(data)))))\n";
		script += "\n";
		script += "set <- newSeqExpressionSet(as.matrix(data), phenoData = data.frame(x, row.names=colnames(data)));\n";
		script += "\n";
		script += "library(RColorBrewer)\n";
		script += "colors <- brewer.pal(3, \"Set2\")\n";
		script += "pdf(\"" + outputNormalizedCountFile + ".RLE.pdf\");\n";
		script += "plotRLE(set, outline=FALSE, ylim=c(-4, 4), col=colors[x])\n";
		script += "dev.off();\n";
		script += "pdf(\"" + outputNormalizedCountFile + ".PCA.pdf\");\n";
		script += "plotPCA(set, col=colors[x], cex=1.2)\n";
		script += "dev.off();\n";
		script += "\n";
		script += "set <- betweenLaneNormalization(set, which=\"upper\")\n";
		script += "pdf(\"" + outputNormalizedCountFile + ".RLE_after_normalization.pdf\");\n";
		script += "plotRLE(set, outline=FALSE, ylim=c(-4, 4), col=colors[x])\n";
		script += "dev.off();\n";
		script += "pdf(\"" + outputNormalizedCountFile + ".PCA_after_normalization.pdf\");\n";
		script += "plotPCA(set, col=colors[x], cex=1.2)\n";
		script += "dev.off();\n";
		script += "\n";
		script += "set1 <- RUVg(set, spikes, k=1)\n";
		script += "pData(set1)\n";
		script += "pdf(\"" + outputNormalizedCountFile + ".RLE_after_normalization_spikeinnorm.pdf\");\n";
		script += "plotRLE(set1, outline=FALSE, ylim=c(-4, 4), col=colors[x])\n";
		script += "dev.off();\n";
		script += "pdf(\"" + outputNormalizedCountFile + ".PCA_after_normalization_spikeinnorm.pdf\");\n";
		script += "plotPCA(set1, col=colors[x], cex=1.2)\n";
		script += "dev.off();\n";
		script += "\n";
		script += "write.table(normCounts(set1), file = \"" + outputNormalizedCountFile + "\", sep = \"\\t\", quote= FALSE);\n";
		script += "\n";
		/*script += "design <- model.matrix(~x + W_1, data=pData(set1))\n";
		script += "y <- DGEList(counts=counts(set1), group=x)\n";
		script += "y <- calcNormFactors(y, method=\"upperquartile\")\n";
		script += "y <- estimateGLMCommonDisp(y, design)\n";
		script += "y <- estimateGLMTagwiseDisp(y, design)\n";
		script += "fit <- glmFit(y, design)\n";
		script += "lrt <- glmLRT(fit, coef=2)\n";
		script += "topTags(lrt)\n";*/
		
		script += "\n";


		return script;
	}
}
