package RNAseqTools.SingleCell.Normalization;

/**
 * Normalize the TPM quantification to UMI like cDNA count
 * @author tshaw
 *
 */
public class CensusNormalization {
	public static String type() {
		return "SSRNASEQ";
	}
	public static String description() {
		return "Prints out the census script for normalization";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];
		System.out.println(CensusNormalizationScript(inputFile, outputFile));
	}
	
	/**
	 * require monocle library
	 * @param inputFile
	 * @param outputFile
	 * @return
	 */
	public static String CensusNormalizationScript(String inputFile, String outputFile) {
		String script = "";
		script += "library(monocle);\n";
		script += "matrix2MonocleObject = function(data, groups, covariates = NULL,\n";
        script += "lowerDetectionLimit = 1,\n";
        script += "expressionFamily = negbinomial()) {\n";
  
        script += "phenoData = NULL\n";
        script += "phenoDataRaw = NULL\n";
        script += "if (!is.null(covariates)) {\n";
        script += "covariates = as.data.frame(covariates)\n";
        script += "phenoDataRaw = data.frame(groups, covariates)\n";
        script += "} else {\n";
        script += "phenoDataRaw = data.frame(groups)\n";
        script += "}\n";
        script += "rownames(phenoDataRaw) = colnames(data)\n";
        script += "colnames(phenoDataRaw)[1] = \"groups\"\n";
        script += "if (!is.null(covariates)) {\n";
        script += "colnames(phenoDataRaw)[2:dim(phenoDataRaw)[2]] = colnames(covariates)\n";
        script += "}\n";
        script += "phenoData = new(\"AnnotatedDataFrame\", data = phenoDataRaw)\n";
        script += "cellData = newCellDataSet(cellData = data,\n"; 
        script += "phenoData = phenoData,\n";
        script += "lowerDetectionLimit = lowerDetectionLimit,\n";
        script += "expressionFamily = expressionFamily)\n";
  
        script += "return(cellData)\n";
        script += "}\n";

        script += "data=read.csv(\"" + inputFile + "\", sep=\"\\\t\", header=T, row.names=1);\n";
        script += "#TPM = t(t(data) / totalReads * 1e6)\n";
        script += "lowerDetectionLimit = 0.1\n";
        script += "groups = rep(1,length(data[1,]))\n";
        script += "object = matrix2MonocleObject(as.matrix(data), groups, covariates = NULL,\n";
        script += "lowerDetectionLimit = lowerDetectionLimit,\n";
        script += "expressionFamily = VGAM::tobit(Lower = log10(lowerDetectionLimit)))\n";
  
        script += "rpcMatrix = relative2abs(object)\n";
        /*
        script += "gene = apply(rpcMatrix,1,mean)\n";
        script += "hist(log(gene[which(gene>0)],2))\n";

        script += "datagene = apply(data,1,mean)\n";
        script += "hist(log(datagene[which(datagene>0)],2))\n";
		*/
        script += "write.table(rpcMatrix, file = \"" + outputFile + "\",sep=\"\\\t\")\n";
        //rpcCount = round(rpcMatrix)
        return script;
	}
}
