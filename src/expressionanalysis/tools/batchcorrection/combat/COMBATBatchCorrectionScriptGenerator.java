package expressionanalysis.tools.batchcorrection.combat;

/**
 * Define the sampleInput File which contains the sample, path of htseq or Kallisto count, batch, and cancer type
 * Perform COMBAT batch correction
 * Script originally from Zhaohui Gu and Chunxu Qu modified by Timothy Shaw
 * @author tshaw
 *
 */
public class COMBATBatchCorrectionScriptGenerator {

	public static String description() {
		return "Generate COMBAT batch correction script assuming no more than one covariate.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [sampleColName] [pathColName] [batchColName] [covariateColName] [outputMatrix]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String sampleMetaFile = args[0];
			String sampleColName = args[1];
			String pathColName = args[2];
			String batchColName = args[3];
			String covariateColName = args[4];
			String outputMatrix = args[5];
			System.out.println(generate_combat_script(sampleMetaFile, sampleColName, pathColName, batchColName, covariateColName, outputMatrix));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_combat_script(String metaFile, String sampleColName, String pathColName, String batchColName, String covariateColName, String outputMatrix) {
		String script = "";
		script += "rm(list=ls())\n";
		//library(rstudioapi)
		script += "library(DESeq2)\n";
		script += "library(dplyr)\n";
		script += "library(magrittr)\n";
		script += "library(readr)\n";
		script += "library(Rtsne)\n";
		script += "library(ggpubr)\n";
		script += "library(sva)\n";
		// #setwd(dirname(getActiveDocumentContext()$path))

		//script += "sampleInfor=read.table(file.path(dir,inputFile), header = T, as.is = T, sep = \"\\t\", na.strings = \"notUsedString\")\n";

		script += "#convert HTSeq to count and rld\n";
		script += "HTSeqConvert <- function(sampleInfor, N=1){\n";
		script += "  sampleTable=data.frame(sampleName=sampleInfor$" + sampleColName + ",\n";
		script += "                         fileName=sampleInfor$" + pathColName + ")\n";
		script += "  ddsHTSeq_0=DESeqDataSetFromHTSeqCount(sampleTable = sampleTable, directory = \"\", design= ~ 1)\n";
		script += "  print(\"Loading HTSeq files is done!\")\n";
		  
		script += "  geneNames=rownames(ddsHTSeq_0)\n";
		script += " #ddsHTSeq_1=ddsHTSeq_0[which(geneNames %in% finalGenes$ID), ]\n";
		script += "  ddsHTSeq_1 = ddsHTSeq_0\n";
		script += "  ddsCount <- counts(ddsHTSeq_1)\n";
		script += "  #keep the genes with at least N samples covered by >= 10 reads\n";
		script += "  ddsHTSeq <- ddsHTSeq_1[ apply(X = counts(ddsHTSeq_1)\n";
		script += "                                , MARGIN = 1\n";
		script += "                                , FUN = function(x) sum(x>=10) ) >=N, ]\n";
		script += "  rldHTSeq <- varianceStabilizingTransformation(ddsHTSeq, blind=T)\n";
		script += "  print(\"VST transformation is done!\")\n";
		script += "  return(list(count=ddsCount, rld=rldHTSeq, dds=ddsHTSeq))\n";
		script += "}\n";

		//script += "prj=\"" + outputFolder + "\";\n";
		//script += "inputFile=\"test_meta.txt\"\n";

		//dir=file.path("/rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/Comprehensive_CAR-T_Analysis/Comprehensive_Clustering_Analysis/CombatNormalization/", prj)
		
		//sampleInfor=read.table(file.path(dir,inputFile), header = T, as.is = T, sep = "\t", na.strings = "notUsedString")
		script += "sampleInfor=read.table(\"" + metaFile + "\", header = T, as.is = T, sep = \"\\t\", na.strings = \"notUsedString\")\n";
		script += "#read in read count data\n";
		script += "HTSeqList=HTSeqConvert(sampleInfor)\n";
		script += "rld=HTSeqList$rld\n";

		script += "#renew rlogDF\n";
		script += "rlogDF=round(data.frame(assay(rld)), digits = 2 )\n";
		script += "write.table(rlogDF, \"" + outputMatrix + "_VST_normalization.txt\", sep=\"\\t\", row.names = TRUE, col.names=NA, quote = FALSE);\n";
		script += "#batch effect correction\n";
		if (covariateColName.equals("NA") || covariateColName.equals("-1")) {
			script += "modcombat = model.matrix(~1, data=sampleInfor) # this is the same as\n";
		} else {
			script += "modcombat = model.matrix(~as.factor(" + covariateColName + "), data=sampleInfor) # this is the same as\n";
		}
		script += "# reference-batch version, with covariates\n";
		script += "rlogDF_0 = ComBat(dat=as.matrix(rlogDF), batch=sampleInfor$" + batchColName + ", mod=modcombat, par.prior=TRUE)\n";
		script += "write.table(rlogDF_0, \"" + outputMatrix + "_ouputFile_with_covariates.txt\", sep=\"\\t\", row.names = TRUE, col.names=NA, quote = FALSE);\n";
		script += "# parametric adjustment\n";
		script += "rlogDF_1 = ComBat(dat=as.matrix(rlogDF), batch=sampleInfor$" + batchColName + ", mod=NULL, par.prior=TRUE)\n";
		script += "write.table(rlogDF_1, \"" + outputMatrix + "ouputFile_parametric_adjustment.txt\", sep=\"\\t\", row.names = TRUE, col.names=NA, quote = FALSE);\n";
		

		return script;
	}
}
