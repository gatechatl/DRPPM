package RNAseqTools.SingleCell.MappingPipeline;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import misc.CommandLine;

/**
 * Generate the script for Single Cell Sequencing Mapping and Quantification
 * 
 * @author tshaw
 * 
 */
public class SingleCellRNAseqMapAndQuan {

	public static String description() {
		return "Generate the script for Single Cell Sequencing Mapping and Quantification";
	}

	public static String type() {
		return "SINGLECELL";
	}

	public static String parameter_info() {
		return "[configFile] [fileNameLstFile] [organism hg19/mm9] [prefix]";
	}

	public static void execute(String[] args) {

		try {
			String configFile = args[0]; // file to use as default
			String fileNameListFile = args[1];
			String organism = args[2]; // hg19
			String prefix = args[3];
			// String StarSummaryFile = args[2];
			// String StarCombinedExpression = args[3];

			ReadConfigFile.readFile(configFile);
			if (!organism.equals("hg19")) {
				System.out
						.println("Your input: "
								+ organism
								+ " is an invalid organism name. Please use the following (hg19/mm9)");
				System.exit(0);
				;
			}
			String step0_script = generateTrimmomaticScript(fileNameListFile,
					ReadConfigFile.TRIMMOMATICPATH, ReadConfigFile.ADAPTORPATH, prefix
					+ "_" + ReadConfigFile.TRIMMOMATICSCRIPTSHELL)
			+ ";\necho \"Generated Trimming Script...\"\n echo \"Step0 Executed...\" > SingleCellSequencing.log\n";
			
			step0_script += executeSCRIPT(ReadConfigFile.BSUBCMDFILE,
					prefix + "_" + ReadConfigFile.TRIMMOMATICSCRIPTSHELL, 8000, false, 1)
					+ ";\necho \"Please wait for jobs to finish...\"\necho \"Step0 Job Submitted...\" >> SingleCellSequencing.log";
			
			CommandLine.writeFile("step0_generateTrimmomaticScript.sh", step0_script);
			
			//String step1_script = generateStarMappingScript(fileNameListFile,
			//		ReadConfigFile.STARPATH, ReadConfigFile.STARINDEX, prefix
			//				+ "_" + ReadConfigFile.STARSCRIPTSHELL)
			//		+ ";\necho \"Generated Mapping Script...\"\n echo \"Step1 Executed...\" > SingleCellSequencing.log";
			
			String step1_script = generateStarMappingScriptForTrimFastq(fileNameListFile,
					ReadConfigFile.STARPATH, ReadConfigFile.STARINDEX, prefix
							+ "_" + ReadConfigFile.STARSCRIPTSHELL)
					+ ";\necho \"Generated Mapping Script...\"\n echo \"Step1 Executed...\" > SingleCellSequencing.log";
			
			CommandLine.writeFile("step1_generateStarMapping.sh", step1_script);
			String step2_script = executeSCRIPT(ReadConfigFile.BSUBCMDFILE,
					prefix + "_" + ReadConfigFile.STARSCRIPTSHELL, 32000, true, 8)
					+ ";\necho \"Please wait for jobs to finish...\"\necho \"Step2 Executed...\" >> SingleCellSequencing.log";
			CommandLine
					.writeFile("step2_StarMappingSubmitJob.sh", step2_script);
			String step3_script = SummarizeStarMapping(fileNameListFile, prefix
					+ "_" + ReadConfigFile.STARSUMMARYFILE)
					+ "\necho \"Summarized STAR Mapping Statistics\"\necho \"Step3 Executed... \" >> SingleCellSequencing.log";
			CommandLine
					.writeFile("step3_SummarizeStarMapping.sh", step3_script);
			String step4_script = combineBamFiles(ReadConfigFile.BAMLST)
					+ "\necho \"Generated a bamlst file for the HTSEQ...\"\necho \"Step4 Executed... \" >> SingleCellSequencing.log";
			CommandLine.writeFile("step4_combineBamFile.sh", step4_script);
			String step5_script = createHTSEQScript(ReadConfigFile.HTSEQPERL,
					ReadConfigFile.BAMLST, organism)
					+ "\necho \"Finished generating HTSEQ Script...\"\necho \"Step5 Executed... \" >> SingleCellSequencing.log";
			CommandLine.writeFile("step5_createHTSEQScript.sh", step5_script);
			String step6_script = executeSCRIPT(ReadConfigFile.BSUBCMDFILE,
					"complete_execution.sh", 16000, true, 8)
					+ "\necho \"Please wait for jobs to finish...\"\necho \"Step6 Executed... \" >> SingleCellSequencing.log";
			CommandLine.writeFile("step6_HTSEQSubmitJob.sh", step6_script);
			String step7_script = combineHTSEQFiles(ReadConfigFile.BAMLST,
					prefix + "_" + ReadConfigFile.HTSEQRPM)
					+ "\necho \"Combined HTSEQ result...\"\necho \"Step7 Executed... \" >> SingleCellSequencing.log";
			CommandLine.writeFile("step7_combineHTSEQResult.sh", step7_script);
			String step8_script = normalizeHTSEQ(ReadConfigFile.GTFFILE, prefix
					+ "_" + ReadConfigFile.HTSEQRPM, prefix + "_"
					+ ReadConfigFile.HTSEQFPKM)
					+ "\necho \"Normalized expression information...\"\necho \"Step8 Executed... \" >> SingleCellSequencing.log";
			CommandLine.writeFile("step8_FPKMNormalization.sh", step8_script);
			String step9_script = ensemblID2geneName(prefix + "_"
					+ ReadConfigFile.HTSEQFPKM, ReadConfigFile.GTFFILE, prefix
					+ "_" + ReadConfigFile.HTSEQFPKMGENENAME)
					+ "\necho \"Convert EnsemblID to GeneName...\"\necho \"Step9 Executed... \" >> SingleCellSequencing.log";
			CommandLine.writeFile("step9_ensemblID2geneName.sh", step9_script);
			String step10_script = plotZeroPlot(prefix + "_"
					+ ReadConfigFile.HTSEQFPKMGENENAME, prefix + "_"
					+ ReadConfigFile.HTSEQZEROBINNING, prefix + "_"
					+ ReadConfigFile.HTSEQZEROBINNINGPNG)
					+ "\necho \"Generated HTSEQ Zero Binning Plot...\"\necho \"Step10 Executed... \" >> SingleCellSequencing.log";
			CommandLine.writeFile("step10_plotZeroHistogram.sh", step10_script);
			String step11_script = generateFPKMHistogram(prefix + "_"
					+ ReadConfigFile.HTSEQFPKMGENENAME, prefix + "_"
					+ ReadConfigFile.HTSEQFPKMBINNING)
					+ "\necho \"Generated FPKM Histogram...\"\necho \"Step11 Executed... \" >> SingleCellSequencing.log";
			;
			CommandLine.writeFile("step11_plotFPKMHistogram.sh", step11_script);
			
			String step12_script = plotHeatmap(ReadConfigFile.HTSEQFPKMGENENAME, ReadConfigFile.HTSEQFPKMGENENAMECLEANED, ReadConfigFile.HTSEQFPKMGENENAMECLEAENDMEDIAN, ReadConfigFile.HTSEQFPKMGENENAMECLEAENDMEDIANPNG, ReadConfigFile.HEATMAPR)
					+ "\necho \"Generated Heatmap...\"\necho \"Step12 Executed... \" >> SingleCellSequencing.log";
			CommandLine.writeFile("step12_plotHeatmap.sh", step12_script);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String generateTrimmomaticScript(String fastaFileList,
			String TRIMPATH, String ADAPTORPATH, String TRIMSCRIPTSHELL) {		
		return "drppm -TrimmomaticScriptGenerator " + fastaFileList + " " 
				+ TRIMPATH + " " + ADAPTORPATH + " > " + TRIMSCRIPTSHELL;
	}
	public static String generateStarMappingScript(String fastaFileList,
			String STARPATH, String STARINDEX, String STARSCRIPTSHELL) {
		return "drppm -STARMappingScriptGenerator " + fastaFileList + " "
				+ STARPATH + " " + STARINDEX + " > " + STARSCRIPTSHELL;
	}
	public static String generateStarMappingScriptForTrimFastq(String fastaFileList,
			String STARPATH, String STARINDEX, String STARSCRIPTSHELL) {
		return "drppm -STARMappingScriptGeneratorForTrimFastq " + fastaFileList + " "
				+ STARPATH + " " + STARINDEX + " > " + STARSCRIPTSHELL;
	}
	public static String executeSCRIPT(String BSUBCMDFILE, String SHELLSCRIPT,
			int memory, boolean limitHost, int cpuNumber) {
		String hostStr = "";
		if (limitHost) {
			hostStr = "-R span[hosts=1]";
		}
		return BSUBCMDFILE + " " + SHELLSCRIPT + " -M " + memory + " " + hostStr + " -n " + cpuNumber;
	}

	public static String SummarizeStarMapping(String fastaFileList,
			String StarSummaryFile) {
		return "drppm -SummarizeStarMapping " + fastaFileList + " "
				+ StarSummaryFile;
	}

	public static String combineBamFiles(String bamLst) {
		return "ls *Aligned.sortedByCoord.out.bam > " + bamLst;
	}

	public static String createHTSEQScript(String HTSEQPERL, String BAMLST,
			String organism) {
		return "perl " + HTSEQPERL + " " + BAMLST + " no " + organism;
	}

	public static String combineHTSEQFiles(String BAMLST, String HTSEQRPM) {
		return "drppm -CombineHTSEQResult " + BAMLST + " " + HTSEQRPM;
	}

	public static String normalizeHTSEQ(String gtf, String rpmFile,
			String fpkmFile) {
		return "drppm -RPM2RPKMExon " + gtf + " " + rpmFile + " " + fpkmFile;
	}

	public static String ensemblID2geneName(String fpkmFile, String gtfFile,
			String fpkmGeneNameFile) {
		return "drppm -EnsemblGeneID2GeneName " + fpkmFile + " " + gtfFile
				+ " " + fpkmGeneNameFile;
	}

	public static String plotZeroPlot(String HTSEQFPKMGENENAME,
			String HTSEQZEROBINNING, String HTSEQZEROBINNINGPNG) {
		String script = "drppm -GenerateZeroAnalysisBinningTable "
				+ HTSEQFPKMGENENAME + " " + HTSEQZEROBINNING + " 1.0\n";
		script += "drppm -BoxPlotGeneratorTwoColumn " + HTSEQZEROBINNING + " "
				+ HTSEQZEROBINNINGPNG + " > zero_boxplot.r\n";
		script += "R --vanilla < zero_boxplot.r\n";
		return script;
	}

	public static String generateFPKMHistogram(String HTSEQFPKMGENENAME,
			String HTSEQFPKMBINNING) {
		return "drppm -GenerateFPKMBinningTable " + HTSEQFPKMGENENAME + " "
				+ HTSEQFPKMBINNING + " " + "0.5";
	}
	public static String plotHeatmap(String HTSEQFPKMGENENAME, String HTSEQFPKMGENENAMECLEANED, String HTSEQFPKMGENENAMECLEAENDMEDIAN, String HTSEQFPKMGENENAMECLEAENDMEDIANPNG, String HEATMAPR) {
		String script = "drppm -RemoveNAGenes " + HTSEQFPKMGENENAME + " " + HTSEQFPKMGENENAMECLEANED + "\n";
		script += "drppm -MergeGeneName " + HTSEQFPKMGENENAMECLEANED + " MEDIAN " + HTSEQFPKMGENENAMECLEAENDMEDIAN + "\n";
		script += "drppm -GrabColumnName " + HTSEQFPKMGENENAMECLEAENDMEDIAN + " sampleName.txt" + "\n";
		script += "drppm -plotMADHeatMap " + HTSEQFPKMGENENAMECLEAENDMEDIAN + " 1000 sampleName.txt " + HTSEQFPKMGENENAMECLEAENDMEDIANPNG + " > " + HEATMAPR + "\n";
		script += "R --vanilla < " + HEATMAPR + "\n";
		
		return script;
	}
}
