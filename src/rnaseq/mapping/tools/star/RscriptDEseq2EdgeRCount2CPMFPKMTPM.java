package rnaseq.mapping.tools.star;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import misc.RunRScript;

/**
 * Take 
 * @author gatechatl
 *
 */
public class RscriptDEseq2EdgeRCount2CPMFPKMTPM {

	public static String type() {
		return "RSCRIPT";
	}
	public static String description() {
		String description = "Generate Rscript for converting count to CPM, FPKM, and TPM\n";
		return description;
	}
	public static String parameter_info() {
		return "[inputCountFile] [geneLengthFile] [outputPrefix]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputCountFile = args[0];
			String geneLengthFile = args[1]; //
			String outputPrefix = args[2];
			String rscript = genereate_cpm_fpkm_tpm(inputCountFile, geneLengthFile, outputPrefix);
			RunRScript.writeFile(outputPrefix + "_cpm_fpkm_tpm_rscript.r", rscript);
			RunRScript.runRScript(outputPrefix + "_cpm_fpkm_tpm_rscript.r");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String genereate_cpm_fpkm_tpm(String inputCountFile, String geneLengthFile, String outputPrefix) {
		
		String script = "";
		script += "library(edgeR)\n";
		script += "library(limma)\n";

		script += "inputFile = \"" + inputCountFile + "\"\n";
		script += "geneLengthFile = \"" + geneLengthFile + "\"\n";
		script += "counts <- read.table(inputFile, sep =\"\\t\", header=TRUE, row.names = 1)\n";
		script += "geneLength <- read.table(geneLengthFile, sep =\"\\t\", header=TRUE, row.names = 1)\n";
		script += "updated_counts = counts[rownames(geneLength),]\n";
		script += "dge <- DGEList(counts=updated_counts, genes=geneLength)\n";
		script += "test <- DGEList(counts=updated_counts)\n";
		script += "dge <- calcNormFactors(dge, method=\"TMM\")\n";
		script += "cpm_result = cpm(dge)\n";
		script += "write.table(cbind(names = rownames(cpm_result), cpm_result), file=\"" + outputPrefix + "_CPM.txt\", sep=\"\\t\", row.names=FALSE, quote = FALSE)\n";

		script += "rpkm_result = rpkm(dge)\n";

		script += "write.table(cbind(names = rownames(rpkm_result), rpkm_result), file=\"" + outputPrefix + "_FPKM.txt\", sep=\"\\t\", row.names=FALSE, quote = FALSE)\n";

		script += "total_rpkm = apply (rpkm_result, 2, sum)\n";

		script += "tpm_result =  1E6 * rpkm_result / total_rpkm\n";

		script += "write.table(cbind(names = rownames(tpm_result), tpm_result), file=\"" + outputPrefix + "_TPM.txt\", sep=\"\\t\", row.names=FALSE, quote = FALSE)\n";
		return script;
	}
}
