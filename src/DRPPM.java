import java.util.Iterator;
import java.util.LinkedList;

import DifferentialExpression.AddAnnotation2DiffFisher;
import DifferentialExpression.AddAnnotation2DiffResult;
import DifferentialExpression.AddAnnotationGeneral;
import DifferentialExpression.CalculateLIMMA;
import DifferentialExpression.CheckIfDifferentiallyExpressed;
import DifferentialExpression.GrabSampleNameWithKeyword;
import DifferentialExpression.SampleFilter;
import GSEATools.CalculateRank;
import GSEATools.ConvertGSEAHuman2Mouse;
import GSEATools.ConvertGSEAList2AnnotationFile;
import GSEATools.GSEAHeatmap;
import GeneNetworkDisplay.CreateNetworkDisplay;
import GeneNetworkDisplay.CreateNetworkDisplayComplex;
import GeneNetworkDisplay.GenerateEdgeMetaData;
import GeneNetworkDisplay.GenerateNodeMetaData;
import GenerateNUC2PROT.Extract100PercentMatch;
import GenerateNUC2PROT.ExtractSequenceFromAlignment;
import GenerateNUC2PROT.MakeFastaSingleLine;
import GenerateNUC2PROT.RescueFragments;
import RNAseqTools.Cufflinks.ExtractFPKM;
import GraphsFigures.HeatmapGeneration;
import GraphsFigures.PCAPlot;
import GraphsFigures.SampleExprHistogram;
import GraphsFigures.SingleScatterPlot;
import GraphsFigures.VolcanoPlot;
import IDConversion.GenerateConversionTable;
import MISC.CollapseGeneExpression;
import MISC.ExpandGeneNames;
import MISC.FilterColumnNames;
import MISC.FilterColumns;
import MISC.GenerateFastaFileFromTrypticTxt;
import MISC.GrabColConvert2Fasta;
import MISC.GrabColumnName;
import MISC.HumanMouseGeneNameConversion;
import MISC.RemoveNoncodingRNA;
import MISC.ReorderSamples;
import MISC.RunRScript;
import Metabolomic.StructureClustering.StructureFractionClustering;
import Metagenomic.Postprocessing.CombineOTUCounts;
import Metagenomic.Postprocessing.GenerateJasonMatrixTable;
import Metagenomic.Postprocessing.GenerateQIIMEMatrixTable;
import Metagenomic.Postprocessing.NormalizePerHundredKilo;
import Metagenomic.Preprocessing.ChemoProjectGenerateMetaFile;
import Metagenomic.Preprocessing.GrabEachFileInsertIDFasta;
import MouseModelQC.FPKMBoxPlotOfGeneKO;
import MouseModelQC.FPKMBoxPlotOfGeneKOSampleSpecific;
import MouseModelQC.RenameSampleForBoxPlot;
import PhosphoGPS.CreatePhosphoGPSFastaFile;
import PhosphoPainter.KinaseSubstratePainter;
import PhosphoTools.BasicStats.GenerateBarPlot;
import PhosphoTools.BasicStats.PhosphoBasicStats;
import PhosphoTools.CoordConversion.Kin2SubConvert2Coordinate;
import PhosphoTools.CoordConversion.HumanCentricProteinAlignment;
import PhosphoTools.CoordConversion.MouseCentricProteinAlignment;
import PhosphoTools.Evaluation.GenerateROCCurvePerKinase;
import PhosphoTools.Evaluation.GenerateROCCurveRandomRandom;
import PhosphoTools.MISC.AddScanCountInfo;
import PhosphoTools.MISC.AppendMoreInformationTogether;
import PhosphoTools.MISC.AppendOriginalPeptideInformation;
import PhosphoTools.MISC.AppendOriginalPeptideInformation2Table;
import PhosphoTools.MISC.CalculateKinase2KinaseCorrelation;
import PhosphoTools.MISC.OrganismConversion2PhosphositeFile;
import PhosphoTools.MISC.FilterPutativeKinase;
import PhosphoTools.MotifTools.AddKinaseBasedOnPhosphosite;
import PhosphoTools.MotifTools.AddRelativeQuantification;
import PhosphoTools.MotifTools.AddRelativeQuantificationForPredicted;
import PhosphoTools.MotifTools.AddRelativeQuantificationForPredictedAll;
import PhosphoTools.MotifTools.AddRelativeQuantificationForPredictedAllJUMP;
import PhosphoTools.MotifTools.AddRelativeQuantificationJUMP;
import PhosphoTools.MotifTools.Ascore2FastaFile;
import PhosphoTools.MotifTools.Ascore2FastaFileJUMP;
import PhosphoTools.MotifTools.CalcMotifEnrichment;
import PhosphoTools.MotifTools.CalculateAllMotifPValueFastaFile;
import PhosphoTools.MotifTools.GeneratePredictedHistogramDistribution;
import PhosphoTools.MotifTools.PhosphoMotifMatching;
import PhosphoTools.MotifTools.MotifX.ExtendPeptide2Fasta;
import PhosphoTools.MotifTools.MotifX.ExtendPeptide2Table;
import PhosphoTools.MotifTools.MotifX.MotifXMatchMotif;
import PhosphoTools.MotifTools.MotifX.MotifXSummaryTable;
import PhosphoTools.MotifTools.MotifX.ParseMotifXOutput;
import PhosphoTools.MotifTools.Stats.GenerateKSScatterPlots;
import PhosphoTools.MotifTools.Stats.PhosphoKinaseBackgroundRandom;
import PhosphoTools.MotifTools.Stats.PhosphoKinaseCorrelationDistribution;
import PhosphoTools.MotifTools.Stats.PhosphoKinaseCorrelationDistributionAll;
import PhosphoTools.Network.AddNetworkNeighborEvidence;
import PhosphoTools.Network.GenerateComplexNetwork;
import PhosphoTools.PSSM.AppendPSSMResult2HPRD;
import PhosphoTools.PeptideCoverage.PeptideCategoriesSharedOrUniqIDmod;
import PhosphoTools.PeptideCoverage.PeptideCategoriesSharedOrUnique;
import PhosphoTools.PeptideCoverage.PeptideCoveragePlot;
import PhosphoTools.PeptideCoverage.PeptideCoverageSingleGeneComparison;
import PhosphoTools.Summary.PhosphoSummarizeResults;
import PhosphositePlusTools.DownloadAllPossibleSiteInfo;
import PreprocessForPCA.AddGeneKO2Sample;
import RNATools.PCPA.CalculatePolyADistribution;
import RNATools.PCPA.CalculatePolyADistributionMouse;
import RNATools.PCPA.ExtractPolyAReadsUsePolyALibrary;
import RNATools.PCPA.ExtractPolyAReadsYuxinScript;
import RNATools.PCPA.GeneratePCPAHumanScript;
import RNATools.PCPA.GeneratePCPAMouseScript;
import RNATools.PCPA.GeneratePolyAHistogramOutput;
import RNATools.PCPA.KeepPolyA;
import RNATools.PCPA.PLA2BEDFile;
import RNAseqTools.CombineFPKMFiles;
import RNAseqTools.AlternativeSplicing.AddGeneName2MATS;
import RNAseqTools.AlternativeSplicing.GenerateOverlappingResults;
import RNAseqTools.AlternativeSplicing.MATSGenerateResultTable;
import RNAseqTools.AlternativeSplicing.MATSScriptGenerator;
import RNAseqTools.CICERO.AppendCICEROHTMLLink;
import RNAseqTools.CICERO.GenerateBamSoftLink;
import RNAseqTools.IntronRetention.Bam2BedConversion;
import RNAseqTools.IntronRetention.CalculateCoverageBed;
import RNAseqTools.IntronRetention.CalculateSplicingDeficiency;
import RNAseqTools.IntronRetention.CalculateSplicingDeficiencyScript;
import RNAseqTools.IntronRetention.CountNumberOfUniqReads;
import RNAseqTools.IntronRetention.CountNumberOfUniqReadsScript;
import RNAseqTools.IntronRetention.DetectIntronRetention;
import RNAseqTools.IntronRetention.FilterBEDReads;
import RNAseqTools.IntronRetention.FilterBEDReadsScript;
import RNAseqTools.IntronRetention.IntersectBed;
import RNAseqTools.IntronRetention.IntronMappingPercentageSummary;
import RNAseqTools.IntronRetention.IntronRetentionHistogramData;
import RNAseqTools.IntronRetention.Graphs.GenerateIntronRetentionBarPlot;
import SNVTools.AddRecurrenceAnnotation;
import SNVTools.RecurrentGeneMutFreq;
import Statistics.General.RNASEQStatsReport;
import TranscriptionFactorTools.TFGeneEnrichmentFilter;
import TranscriptionFactorTools.TFRegulatedGenes;
import WholeExonTools.AppendBamReviewFile;
import WholeExonTools.MISC.AppendGermlineAlternativeAlleleCount;
import WholeExonTools.PostProcessing.SnpDetectPostProcessingScript;


/**
 * Tool is the cumulation of scripts and pipelines over time
 * This is the centralized jar class for combining all datatype
 * Last updated 2015-05-20
 * @author Timothy Shaw
 *
 */
public class DRPPM {
	public static String VERSION = "20150625";
	public static void main(String[] args) {
		try {
			
			if (args.length <= 0) {
				System.out.println("Not enough argument");
				printProgramInfo();
				System.exit(0);
			}
			
			String type = args[0];
			if (type.equals("-LIMMA1")) {
				//System.out.println("Single Group Differential Expression");
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -LIMMA1 [input] [groupFile] [OutputUpReg] [OutputDownReg] [OutputAll] [FilterType: ALL,PVALUE,FOLDCHANGE,BOTH] [TAKELOG]");
					System.exit(0);
				}
				CalculateLIMMA.CompareOneGroup(args_remain);
			} else if (type.equals("-LIMMA2")) {
				//System.out.println("Double Group Differential Expression");
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -LIMMA2 [input] [groupFile1] [groupFile2] [OutputUpReg] [OutputDownReg] [OutputAll] [FilterType: ALL,PVALUE,FOLDCHANGE,BOTH] [TAKELOG]");
					System.exit(0);
				}
				CalculateLIMMA.CompareTwoGroup(args_remain);
			} else if (type.equals("-LIMMA2Flex")) {
				//System.out.println("Double Group Differential Expression");
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -LIMMA2Flex [input] [groupFile1] [groupFile2] [OutputUpReg] [OutputDownReg] [OutputAll] [Pvalue] [FoldChange] [TAKELOG]");
					System.exit(0);
				}
				CalculateLIMMA.CompareTwoGroupFlex(args_remain);				
			} else if (type.equals("-plotKinase")) {
				//System.out.println("Running Isotope Calculator");
				String[] args_remain = getRemaining(args);
				KinaseSubstratePainter.execute(args_remain);
			} else if (type.equals("-plotHeatMap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
				
					System.out.println("drppm -plotHeatMap [InputFile] [sampleNameFile] [GeneSetFile] [OutputPngFile]");
					System.exit(0);
				}
				HeatmapGeneration.execute(args_remain);
			} else if (type.equals("-plotMADHeatMap")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -plotMADHeatMap [InputFile] [TopN] [SampleNameFile] [OutputPngFile]");
					System.exit(0);
				}
				HeatmapGeneration.executeMAD(args_remain);
			} else if (type.equals("-plotMADHeatMapCOL")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -plotMADHeatMapCOL [InputFile] [TopN] [SampleNameFile] [OutputPngFile]");
					System.exit(0);
				}
				HeatmapGeneration.executeMADCOL(args_remain);
			} else if (type.equals("-removeNonCoding")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -removeNonCoding [InputFile] [TopN] [SampleNameFile] [OutputPngFile]");
					System.exit(0);
				}
				RemoveNoncodingRNA.execute(args_remain);
			} else if (type.equals("-mouse2human")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -mouse2human [inputFile] [hs2mmFile]");
					System.exit(0);
				}
				HumanMouseGeneNameConversion.executeMouse2Human(args_remain);
			} else if (type.equals("-human2mouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -human2mouse [inputFile] [hs2mmFile]");
					System.exit(0);
				}
				HumanMouseGeneNameConversion.executeHuman2Mouse(args_remain);
			} else if (type.equals("-DEAddAnnotation")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -DEAddAnnotation [InputFile] [GeneSetInfo]");
					System.exit(0);
				}
				AddAnnotation2DiffResult.execute(args_remain);
			} else if (type.equals("-AddAnnotationGeneral")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -AddAnnotationGeneral [InputFile] [GeneSetInfo] [columnIndex]");
					System.exit(0);
				}
				AddAnnotationGeneral.execute(args_remain);
			} else if (type.equals("-GEFisher")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GEFisher [InputFile] [GeneSetInfo] > [FisherExactTestFileOutput]");
					System.exit(0);
				}
				AddAnnotation2DiffFisher.execute(args_remain);
			} else if (type.equals("-PCAScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PCAScript [InputFile] [OutputPCAInfo]");
					System.exit(0);
				}
				PCAPlot.executePCA(args_remain);
				
			} else if (type.equals("-PlotPCA")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PlotPCA [InputFile] [ColorFile]");
					System.exit(0);
				}				
				PCAPlot.executePlotPCA(args_remain);
				
			} else if (type.equals("-FilterColumn")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterColumn [InputFile] [FilterFile]");
					System.exit(0);
				}				
				FilterColumns.execute(args_remain);				
			} else if (type.equals("-FilterColumnName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterColumnName [InputFile] [FilterFile]");
					System.exit(0);
				}				
				FilterColumnNames.execute(args_remain);				
			} else if (type.equals("-CollapseExpr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabColumnName [InputFile] [OutputFile]");
					System.out.println("Example: drppm -GrabColumnName drppm -GrabColumnName 2014-12-04_mckinnon_shimada_expr_collapse.txt 2014-12-04_mckinnon_shimada_sampleNames.txt");
					System.exit(0);
				}
				CollapseGeneExpression.execute(args_remain);
			} else if (type.equals("-MergeGeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabColumnName [InputFile] [OutputFile]");
					System.out.println("Example: drppm -GrabColumnName drppm -GrabColumnName 2014-12-04_mckinnon_shimada_expr_collapse.txt 2014-12-04_mckinnon_shimada_sampleNames.txt");
					System.exit(0);
				}
				CollapseGeneExpression.execute(args_remain);				
			} else if (type.equals("-GrabColumnName")) {
				String[] args_remain = getRemaining(args);
				GrabColumnName.execute(args_remain);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabColumnName [InputFile] [OutputFile]");
					System.out.println("Example: drppm -GrabColumnName drppm -GrabColumnName 2014-12-04_mckinnon_shimada_expr_collapse.txt 2014-12-04_mckinnon_shimada_sampleNames.txt");
					System.exit(0);
				}
			} else if (type.equals("-ExpandGeneNames")) {
				String[] args_remain = getRemaining(args);
				ExpandGeneNames.execute(args_remain);
			} else if (type.equals("-GenerateHistogram")) {
				String[] args_remain = getRemaining(args);
				SampleExprHistogram.execute(args_remain);
			} else if (type.equals("-GenerateVolcano")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateVolcano [InputLIMMAoutputFile] [OutputPNG] [pvalue] [logFC] [annotation tag can leave blank]");
					System.out.println("Example: drppm -GenerateVolcano APCvsWTC_ALL_GENESET.txt APCvsWTC_ALL_Volcano_meta.png 0.05 0.5 METAINFOHIT > APCvsWTC_ALL_Volcano_META.r");
					System.exit(0);
				}
				VolcanoPlot.execute(args_remain);
				
			} else if (type.equals("-BoxplotFPKMOfGeneKO")) {
				String[] args_remain = getRemaining(args);
				FPKMBoxPlotOfGeneKO.execute(args_remain);
				//FPKMBoxPlotOfGeneKOSampleSpecific
			} else if (type.equals("-BoxplotFPKMOfGeneKOFilter")) {
				String[] args_remain = getRemaining(args);
				FPKMBoxPlotOfGeneKOSampleSpecific.execute(args_remain);
				//FPKMBoxPlotOfGeneKOSampleSpecific
			} else if (type.equals("-RenameFilesForBoxPlot")) {
				String[] args_remain = getRemaining(args);
				RenameSampleForBoxPlot.execute(args_remain);;
			} else if (type.equals("-ExtractFPKM")) {
				String[] args_remain = getRemaining(args);
				// grab cufflinks expression
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractFPKM /nfs_exports/genomes/1/projects/RNASEQ/Baker/BucketRaw/QC output.table.txt exon.txt intron.txt");
					System.exit(0);
				}
				ExtractFPKM.execute(args_remain);;
				
			} else if (type.equals("-ExtractCufflinksFPKM")) {
				String[] args_remain = getRemaining(args);
				// grab cufflinks expression
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractCufflinksFPKM [outputFile] [indexNumber] [file1] [file2] [fileN] ...");
					System.exit(0);
				}
				ExtractFPKM.execute(args_remain);;
				
			} else if (type.equals("-GenePeptideCoverage")) {
				String[] args_remain = getRemaining(args);
				PeptideCoverageSingleGeneComparison.execute(args_remain);;
				
			} else if (type.equals("-PeptideCategoryMouseHuman")) {
				String[] args_remain = getRemaining(args);
				PeptideCategoriesSharedOrUnique.execute(args_remain);;
				
			} else if (type.equals("-PeptideCategoryMouseHumanIDmod")) {
				String[] args_remain = getRemaining(args);
				PeptideCategoriesSharedOrUniqIDmod.execute(args_remain);;
				
			} else if (type.equals("-PeptideIntensityBarplot")) {
				String[] args_remain = getRemaining(args);
				GenerateBarPlot.execute(args_remain);;
				
			} else if (type.equals("-PeptideCoverage")) {
				String[] args_remain = getRemaining(args);
				PeptideCoveragePlot.execute(args_remain);;
				
			} else if (type.equals("-PhosphoBasicStats")) {
				String[] args_remain = getRemaining(args);
				PhosphoBasicStats.execute(args_remain);;
				
			} else if (type.equals("-PhosphoAllMotifMatch")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PhosphoAllMotifMatch [all_motif.txt] [fasta file] [flag for complete match yes/no] [outputFile]");
					System.exit(0);
				}
				PhosphoMotifMatching.execute(args_remain);;
				//AppendPSSMResult2HPRD
			} else if (type.equals("-AppendPSSMResult2HPRD")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendPSSMResult2HPRD [CeasarKinaseMotifFile] [HPRD File] [outputFile]");
					System.exit(0);
				}
				AppendPSSMResult2HPRD.execute(args_remain);;
				//
			} else if (type.equals("-RNASEQMappingStatistics")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -RNASEQMappingStatistics /nfs_exports/genomes/1/projects/RNASEQ/Baker/BucketRaw/QC output.table.txt exon.txt intron.txt");
					System.exit(0);
				}
				RNASEQStatsReport.execute(args_remain);
				
			} else if (type.equals("-MappingStqatistics")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -MappingStatistics /nfs_exports/genomes/1/projects/RNASEQ/Baker/BucketRaw/QC output.table.txt exon.txt intron.txt");
					System.exit(0);
				}
				RNASEQStatsReport.execute(args_remain);
				
			} else if (type.equals("-AddGeneKO2SampleName")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -AddGeneKO2SampleName [input] [metafile] [outputfile] [yes]");
					System.exit(0);
				}
				AddGeneKO2Sample.execute(args_remain);;
				
			} else if (type.equals("-GrabKeyword")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabKeyword [input] [terms separated by comma]");
					System.out.println("The list of terms");
					System.exit(0);
				}
				GrabSampleNameWithKeyword.execute(args_remain);;
			} else if (type.equals("-SampleFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SampleFilter [input] [terms separated by comma] [outputFile] [yes/no contains keyword?]");
					System.out.println("The list of terms");
					System.exit(0);
				}
				SampleFilter.execute(args_remain);;
				
			} else if (type.equals("-GSEAgmt2txt")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GSEAgmt2txt [input] [outputfolder] [outputLink]");
					System.out.println("This will automatically generate txt files for each line");
					System.exit(0);
				}
				ConvertGSEAList2AnnotationFile.execute(args_remain);;
				
			} else if (type.equals("-Ascore2Fasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Ascore2Fasta [PEPTIDE_MOD_FILE] [JUMPQ_NAME_INDEX] [JUMPQ_PEPTIDE_INDEX] [JUMPQ_PVALUE_INDEX] [JUMPQ_PVALUE_CUTOFF] [JUMPQ_LOGFOLDCHANGE_INDEX] [JUMPQ_LOGFOLDCHANGE_CUTOFF] [JUMPQ_FASTA_UP] [JUMPQ_FASTA_DN] [JUMPQ_FASTA_ALL]");
					System.out.println("");
					System.exit(0);
				}
				Ascore2FastaFile.execute(args_remain);;
				
			} else if (type.equals("-Ascore2FastaFileJUMP")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Ascore2FastaFileJUMP [PEPTIDE_MOD_FILE] [JUMPQ_NAME_INDEX] [JUMPQ_PEPTIDE_INDEX] [JUMPQ_GROUP_INDEX] [JUMPQ_PVALUE_CUTOFF] [JUMPQ_LOGFOLDCHANGE_CUTOFF] [JUMPQ_FASTA_UP] [JUMPQ_FASTA_DN] [JUMPQ_FASTA_ALL]");
					System.out.println("");
					System.exit(0);
				}
				Ascore2FastaFileJUMP.execute(args_remain);;
				
			} else if (type.equals("-CalcMotifEnrichment")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalcMotifEnrichment [AllPhosphoFasta] [AllPhosphoMotifFile] [diffFasta] [DiffMotifFile] [AllMotifFile] [Option: Group/Family] [outputFile]");
					System.out.println("");
					System.exit(0);
				}
				CalcMotifEnrichment.execute(args_remain);
				
			} else if (type.equals("-ParseMotifX")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ParseMotifX [inputFileList] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				ParseMotifXOutput.execute(args_remain);
				
			} else if (type.equals("-MatchMotifX")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MatchMotifX [inputMotifAll] [ParseMotifXOutputFile] [flag for complete match yes/no]");
					System.out.println("");
					System.exit(0);
				}
				MotifXMatchMotif.execute(args_remain);;
				//MotifXSummaryTable
			} else if (type.equals("-MotifXSummary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MotifXSummary [inputMotifXMatch]");
					System.out.println("");	
					System.exit(0);
				}
				MotifXSummaryTable.execute(args_remain);;
				//MotifXSummaryTable
			} else if (type.equals("-ExtendPeptideTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtendPeptideWithFasta [fastaDatabaseInput] [tableInput] [OutputFasta] [extension length(bp)]");
					System.out.println("");
					System.exit(0);
				}
				ExtendPeptide2Table.execute(args_remain);;				
			} else if (type.equals("-ExtendPeptideFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtendPeptideWithFasta [fastaDatabaseInput] [fastaInput] [OutputFasta] [extension length(bp)]");
					System.out.println("");
					System.exit(0);
				}
				ExtendPeptide2Fasta.execute(args_remain);;	
				//
			} else if (type.equals("-AllMotifPValueFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AllMotifPValueFastaFile [allmotifFile] [fastaFile] [OutputCount]");
					System.out.println("");
					System.exit(0);
				}
				CalculateAllMotifPValueFastaFile.execute(args_remain);;	
				
			} else if (type.equals("-AppendPhosphositeKinase")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendPhosphositeKinase [phosphosite] [AllMotifMappingResult] [organism] [OutputCount] [buffer length]");
					System.out.println("");
					System.exit(0);
				}
				AddKinaseBasedOnPhosphosite.execute(args_remain);;	
				//AddRelativeQuantification
			} else if (type.equals("-AppendQuantification")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -AppendQuantification [originalFile] [AScoreFile] [totalProteome] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantification.execute(args_remain);;	
				//AddRelativeQuantificationJUMP
				//PhosphoKinaseCorrelationDistribution
			} else if (type.equals("-AddRelativeQuantificationJUMP")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -AddRelativeQuantificationJUMP [originalFile] [AScoreFile] [totalProteome] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantificationJUMP.execute(args_remain);;	
				
				//PhosphoKinaseCorrelationDistribution
			} else if (type.equals("-AppendPredictedQuantification")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {					
					System.out.println("drppm -AppendPredictedQuantification [originalFile] [AScoreFile] [totalProteome] [kinase_name] [motifName] [groupInfo] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantificationForPredicted.execute(args_remain);;	
			} else if (type.equals("-AppendPredictedQuantificationAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {					
					System.out.println("drppm -AppendPredictedQuantificationAll [originalFile] [AScoreFile] [totalProteome] [MotifAllFile] [groupInfo] [OutputFolder]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantificationForPredictedAll.execute(args_remain);;
				//AddRelativeQuantificationForPredictedAllJUMP
			} else if (type.equals("-AddRelativeQuantificationForPredictedAllJUMP")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {					
					System.out.println("drppm -AddRelativeQuantificationForPredictedAllJUMP [originalFile] [AScoreFile] [totalProteome] [MotifAllFile] [groupInfo] [OutputFolder]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantificationForPredictedAllJUMP.execute(args_remain);;
				//
			} else if (type.equals("-PhoshKSCorrel")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PhoshKSCorrel [InputFile] [GeneName] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				PhosphoKinaseCorrelationDistribution.execute(args_remain);	
			} else if (type.equals("-PhoshKSCorrelAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PhoshKSCorrelAll [InputFile] [GeneNameFile] [Col of PeptideName] [Col of Peptide] [OutputFolder]");
					System.out.println("");
					System.exit(0);
				}
				PhosphoKinaseCorrelationDistributionAll.execute(args_remain);	
			} else if (type.equals("-PhoshKSRandomCorrel")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -PhoshKSRandomCorrel [AscoreFile] [TotalFile] [GeneName] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				PhosphoKinaseBackgroundRandom.execute(args_remain);;	

			} else if (type.equals("-GenerateTrypticFasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -GenerateTrypticFasta [Input] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				GenerateFastaFileFromTrypticTxt.execute(args_remain);;	
			} else if (type.equals("-Col2Fasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -Col2Fasta [Input] [Col_number] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				GrabColConvert2Fasta.execute(args_remain);;	
				
			} else if (type.equals("-IDMatching")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -IDMatching [uniprotIDMappingFile] [proteinFile] [rnaseqFile] [gtfFile] [outputFile]");
					System.out.println("");
					System.exit(0);
				}
				
				GenerateConversionTable.execute(args_remain);;	
				//GenerateConversionTable
			} else if (type.equals("-CombineFPKMExpression")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -CombineFPKMExpression [FPKMExpression1File] [FPKMExpression2File] [outputFile]");
					System.out.println("");
					System.exit(0);
				}
				
				CombineFPKMFiles.execute(args_remain);;	
				
			} else if (type.equals("-SingleScatterPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -SingleScatterPlot [InputFile] [OutputPNG]");
					System.out.println("");
					System.exit(0);
				}
				
				SingleScatterPlot.execute(args_remain);;	
				//GenerateKSScatterPlots
			} else if (type.equals("-GenerateKSScatterPlots")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -GenerateKSScatterPlots [InputFile] [KinaseName] [OutputPNG]");
					System.out.println("");
					System.exit(0);
				}
				
				GenerateKSScatterPlots.execute(args_remain);;	
				//GenerateKSScatterPlots
			} else if (type.equals("-GenerateGPSFasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {			
					System.out.println("drppm -GenerateGPSFasta [InputFastaFile] [OutputFastaFile]");
					System.out.println("");
					System.exit(0);
				}
				
				CreatePhosphoGPSFastaFile.execute(args_remain);;	
				//AppendOriginalPeptideInformation
			} else if (type.equals("-AppendOrigPeptideInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {		
					System.out.println("drppm -AppendOrigPeptideInfo [InputMotifFile] [AscoreFile] [IDsumFile] [OuputMotifFile]");
					System.out.println("");
					System.exit(0);
				}
				
				AppendOriginalPeptideInformation.execute(args_remain);;	
				//AppendOriginalPeptideInformation
			} else if (type.equals("-AppendOrigPeptideInfoPSTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {		
					System.out.println("drppm -AppendOrigPeptideInfoPSTable [InputPSTableFile] [AscoreFile] [IDsumFile] [OuputMotifFile]");
					System.out.println("");
					System.exit(0);
				}
				
				AppendOriginalPeptideInformation2Table.execute(args_remain);;	
				//AppendOriginalPeptideInformation
			} else if (type.equals("-AppendMoreInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {		
					System.out.println("drppm -AppendMoreInfo [InputPSTableFile] [AscoreFile] [IDsumFile] [fastaUp] [fastaDN] [motifInfo] [OuputMotifFile]");
					System.out.println("");
					System.exit(0);
				}
				
				AppendMoreInformationTogether.execute(args_remain);;	
				//AddScanCountInfo
			} else if (type.equals("-AddScanCountInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {		
					
					System.out.println("drppm -AddScanCountInfo [InputFile] [SubIDSumFile] [TotalFile] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				
				AddScanCountInfo.execute(args_remain);;	
				//CalculateKinase2KinaseCorrelation
			} else if (type.equals("-FilterPutativeKinase")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {		
					/*			String fileName = args[0]; //Hong_Final_List.txt
			String correlFile = args[1];
			String motifFile = args[2];
			double cutoff = new Double(args[3]);
			String outputFile = args[4];*/
					System.out.println("drppm -FilterPutativeKinase [InputFile] [correlFile] [motifFile] [cutoff 0.0-1.0] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				
				FilterPutativeKinase.execute(args_remain);;	
				//CalculateKinase2KinaseCorrelation
			} else if (type.equals("-CalculateKinase2KinaseCorrelation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -CalculateKinase2KinaseCorrelation [kinaseFile] [KinaseName] [TotalFile] [grouping] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				
				CalculateKinase2KinaseCorrelation.execute(args_remain);;	
				//CalculateKinase2KinaseCorrelation
			} else if (type.equals("-ReorderSamples")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -ReorderSamples [inputFPKMFile] [SampleOrder] [outputFPKMFile]");
					System.out.println("");
					System.exit(0);
				}				
				ReorderSamples.execute(args_remain);;	
				//CalculateKinase2KinaseCorrelation
			} else if (type.equals("-CalculateRank")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -CalculateRank [inputFPKMFile]");
					System.out.println("");
					System.exit(0);
				}				
				CalculateRank.execute(args_remain);;	
				//CalculateKinase2KinaseCorrelation
			} else if (type.equals("-GSEAHeatmap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -GSEAHeatmap [inputFile] [outputFile]");
					System.out.println("");
					System.exit(0);
				}				
				GSEAHeatmap.execute(args_remain);;	
				//CalculateKinase2KinaseCorrelation
				//
			} else if (type.equals("-mouse2human2col")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -mouse2human2col [inputFile] [inputHuman2Mouse]");
					System.out.println("");
					System.exit(0);
				}				
				HumanMouseGeneNameConversion.convertM2HTwoColumn(args_remain);;	
				//CalculateKinase2KinaseCorrelation
				//mouse2human
				//
			} else if (type.equals("-ExtractSeqFromAln")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -ExtractSeqFromAln [inputFile] [organism] [outputFile]");
					System.out.println("");
					System.exit(0);
				}				
				ExtractSequenceFromAlignment.execute(args_remain);;	
				//CalculateKinase2KinaseCorrelation
				//mouse2human
				//ExtractSequenceFromAlignment
			} else if (type.equals("-Extract100Match")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -ExtractSeqFromAln [inputUNIPROT] [inputUCSC] [outputFOUND] [outputNOTFOUND]");
					System.out.println("");
					System.exit(0);
				}				
				Extract100PercentMatch.execute(args_remain);;	
				//CalculateKinase2KinaseCorrelation
				//mouse2human
				//ExtractSequenceFromAlignment
				//RescueFragments
			} else if (type.equals("-RescueFragments")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -RescueFragments [inputUNIPROT] [outputMatches] [outputFound] [outputNotFound]");
					System.out.println("");
					System.exit(0);
				}				
				RescueFragments.execute(args_remain);;	
			
			} else if (type.equals("-MakeFastaSingleLine")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {							
					System.out.println("drppm -MakeFastaSingleLine [inputFasta] [outputFasta]");
					System.out.println("");
					System.exit(0);
				}				
				MakeFastaSingleLine.execute(args_remain);;	
				//GeneratePredictedHistogramDistribution
			} else if (type.equals("-GeneratePredictedHistogram")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneratePredictedHistogram [motifAllFile] [outputFolder] [pngFolder]");
					System.out.println("");
					System.exit(0);
				}				
				GeneratePredictedHistogramDistribution.execute(args_remain);;	
				
			} else if (type.equals("-CreateNetworkDisplay")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CreateNetworkDisplay [NetworkTXT] [outputFolder]");
					System.out.println("");
					System.exit(0);
				}				
				CreateNetworkDisplay.execute(args_remain);;	
				//RunRScript
			} else if (type.equals("-CreateNetworkDisplayComplex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CreateNetworkDisplayComplex [NetworkTXT] [NetworkMetaData] [NetworkName] [NetworkType: CONCENTRIC/BREADTHFIRST] [fontSize] [outputFolder]");					
					System.out.println("");
					System.exit(0);
				}				
				CreateNetworkDisplayComplex.execute(args_remain);;	
				//GenerateNodeMetaData
			} else if (type.equals("-GenerateNodeMetaData")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateNodeMetaData [inputFile]");
					System.out.println("");
					System.exit(0);
				}				
				GenerateNodeMetaData.execute(args_remain);;	
				//GenerateEdgeMetaData
			} else if (type.equals("-GenerateEdgeMetaData")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateEdgeMetaData [inputFile]");
					System.out.println("");
					System.exit(0);
				}				
				GenerateEdgeMetaData.execute(args_remain);;	
				//
			} else if (type.equals("-RunRScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RunRScript [RScript]");
					System.out.println("");
					System.exit(0);
				}				
				RunRScript.execute(args_remain);;	
				
			} else if (type.equals("-HumanCentricProteinAlignment")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -HumanCentricProteinAlignment [InputUniprotFasta] [outputHomolog] [alignment]");
					System.out.println("");
					System.exit(0);
				}				
				HumanCentricProteinAlignment.execute(args_remain);;	
				
			} else if (type.equals("-MouseCentricProteinAlignment")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MouseCentricProteinAlignment [InputUniprotFasta] [outputHomolog] [alignment]");
					System.out.println("");
					System.exit(0);
				}				
				MouseCentricProteinAlignment.execute(args_remain);;	
				
			} else if (type.equals("-Kin2SubConvert2Coordinate")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Kin2SubConvert2Coordinate [alignment_file] [kinase_substrate] [output_kinase_substrate] [missed_kinase_substrate]");
					System.out.println("");
					System.exit(0);
				}				
				Kin2SubConvert2Coordinate.execute(args_remain);;	
				
			} else if (type.equals("-GSEAHuman2Mouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GSEAHuman2Mouse [mouse/human homology] [human gsea_gmt] [mouse gsea_gmt]");
					System.out.println("");
					System.exit(0);
				}				
				ConvertGSEAHuman2Mouse.execute(args_remain);;	
				
			} else if (type.equals("-Gene2TF")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Gene2TF [TF file] [geneSet File] [outputFile]");
					System.out.println("");
					System.exit(0);
				}				
				TFRegulatedGenes.execute(args_remain);;	
				
			} else if (type.equals("-GEFisherFilter")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GEFisherFilter [InputFile] [GeneSetInfo] [FilterFile]> [FisherExactTestFileOutput]");
					System.exit(0);
				}
				TFGeneEnrichmentFilter.execute(args_remain);
				
			} else if (type.equals("-CheckIfDiffExpr")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -CheckIfDiffExpr [InputDEFile] [FilterFile] > [DEGeneList]");
					System.exit(0);
				}
				CheckIfDifferentiallyExpressed.execute(args_remain);
				
			} else if (type.equals("-ROCCurve")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					
					System.out.println("drppm -ROCCurve [geneName] [kinase_substrate_file] [phospho_fasta_extended] [predicted_substrate_site] [kinase_motif_name]");
					System.exit(0);
				}
				GenerateROCCurvePerKinase.execute(args_remain);
				//GenerateROCCurveRandomRandom
			} else if (type.equals("-ROCCurveRandom")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					
					System.out.println("drppm -ROCCurve [geneName] [kinase_substrate_file] [phospho_fasta_extended] [predicted_substrate_site] [kinase_motif_name]");
					System.exit(0);
				}
				GenerateROCCurveRandomRandom.execute(args_remain);
				
			} else if (type.equals("-MATSScriptGen")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -MATSScriptGen [inputFile1] [inputFile2] [gtfFile] [outputFile]");
					System.exit(0);
				}
				MATSScriptGenerator.execute(args_remain);	
				//
			} else if (type.equals("-AddGeneName2MATS")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -AddGeneName2MATS [mats_output_path] [gtfFile]");
					System.exit(0);
				}
				AddGeneName2MATS.execute(args_remain);	
				//AddGeneName2MATS
			} else if (type.equals("-DetectIntronRetention")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					/*			String inputFile1 = args[0];			
			String bedFile = args[1];
			String outputFile = args[2];
			String tag = args[3];*/
					System.out.println("drppm -DetectIntronRetention [inputFile1] [bedFile] [outputTag]");
					System.exit(0);
				}
				DetectIntronRetention.execute(args_remain);	
				//DetectIntronRetention
			} else if (type.equals("-Bam2Bed")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -Bam2Bed [bamlistFile]");
					System.exit(0);
				}
				Bam2BedConversion.execute(args_remain);					
			} else if (type.equals("-IntersectBed")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -IntersectBed [bamlistFile] [intronBed] [exonBed] [geneBed]");
					System.exit(0);
				}
				IntersectBed.execute(args_remain);					
			} else if (type.equals("-CountNumberOfUniqReads")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -CountNumberOfUniqReads [bedFile] [countAllFlag]");
					System.exit(0);
				}
				CountNumberOfUniqReads.execute(args_remain);	
				//CountNumberOfUniqReadsScript
			} else if (type.equals("-CountNumUniqReadsScript")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -CountNumUniqReadsScript [bedFiles]");
					System.exit(0);
				}
				CountNumberOfUniqReadsScript.execute(args_remain);	
				//CountNumberOfUniqReadsScript
			} else if (type.equals("-KeepPolyA")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -KeepPolyA [inputLst] [outputFq]");
					System.exit(0);
				}
				KeepPolyA.execute(args_remain);	
				//ExtractPolyAReadsUsePolyALibrary
			} else if (type.equals("-ExtractPolyAReadsUsePolyALibrary")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractPolyAReadsUsePolyALibrary [inputLst] [outputFq]");
					System.exit(0);
				}
				ExtractPolyAReadsUsePolyALibrary.execute(args_remain);	
				//ExtractPolyAReadsYuxinScript
			} else if (type.equals("-ExtractPolyAReadsYuxinScript")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractPolyAReadsYuxinScript [inputLst] [outputFq] [perlScriptPath]");
					System.exit(0);
				}
				ExtractPolyAReadsYuxinScript.execute(args_remain);	
				//
			} else if (type.equals("-CalculatePolyADistribution")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculatePolyADistribution [inputLst] [inputFile] [outputFq] [inputFile]");
					System.exit(0);
				}
				CalculatePolyADistribution.execute(args_remain);	
				//GeneratePolyAHistogramOutput
			} else if (type.equals("-CalculatePolyADistributionMouse")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculatePolyADistributionMouse [inputLst] [inputFile] [outputFq] [inputFile]");
					System.exit(0);
				}
				CalculatePolyADistributionMouse.execute(args_remain);	
				//GeneratePolyAHistogramOutput
			} else if (type.equals("-GeneratePolyAHistogramOutput")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneratePolyAHistogramOutput [inputDistFile] [outputFile]");
					System.exit(0);
				}
				GeneratePolyAHistogramOutput.execute(args_remain);	
				//
			} else if (type.equals("-GeneratePCPAMouseScript")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneratePCPAMouseScript [inputFile] [mouseFasta] [perlPath] [coverageBedPath] [mm9bowtieIndex]");
					System.exit(0);
				}
				
				GeneratePCPAMouseScript.execute(args_remain);	
				//GeneratePCPAHumanScript
			} else if (type.equals("-GeneratePCPAHumanScript")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneratePCPAHumanScript [inputFile] [humanFasta] [perlPath] [coverageBedPath] [hg19bowtieIndex]");
					System.exit(0);
				}
				
				GeneratePCPAHumanScript.execute(args_remain);	
				//
			} else if (type.equals("-PLA2BEDFile")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -PLA2BEDFile [plaFile]");
					System.exit(0);
				}
				
				PLA2BEDFile.execute(args_remain);	
				//
			} else if (type.equals("-IntronMappingPercentageSummary")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -IntronMappingPercentageSummary [inputLst] [outputSummaryTable]");
					System.exit(0);
				}
				IntronMappingPercentageSummary.execute(args_remain);	
				//IntronMappingPercentageSummary
			} else if (type.equals("-FilterBEDReads")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterBEDReads [inputLst] [lengthCutoff] [outputFile]");
					System.exit(0);
				}
				FilterBEDReads.execute(args_remain);	
				//FilterBEDReadsScript
			} else if (type.equals("-FilterBEDReadsScript")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterBEDReadsScript [inputLst] [lengthCutoff]");
					System.exit(0);
				}
				FilterBEDReadsScript.execute(args_remain);	
				//CalculateCoverageBed
			} else if (type.equals("-CalculateCoverageBed")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateCoverageBed [bamlistFile] [intronBed] [exonBed]");
					System.exit(0);
				}
				CalculateCoverageBed.execute(args_remain);					
			} else if (type.equals("-CalculateSplicingDeficiency")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {

					System.out.println("drppm -CalculateSplicingDeficiency [intronBedFile] [exonBedFile] [intronCoverageBed] [exonCoverageBed] [outputFile]");
					System.exit(0);
				}
				CalculateSplicingDeficiency.execute(args_remain);	
				//CalculateSplicingDeficiencyScript
			} else if (type.equals("-CalculateSplicingDeficiencyScript")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {

					System.out.println("drppm -CalculateSplicingDeficiencyScript [bamList] [intronBedFile] [exonBedFile]");
					System.exit(0);
				}
				CalculateSplicingDeficiencyScript.execute(args_remain);	
				//IntronRetentionHistogramData
			} else if (type.equals("-IntronRetentionHistogramData")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {

					System.out.println("drppm -IntronRetentionHistogramData [SD_txt_files] [binFreq] [RetentionPercentage]");
					System.exit(0);
				}
				IntronRetentionHistogramData.execute(args_remain);	
				//ConvertConversionAppend2PhosphositeFile
			} else if (type.equals("-ReplaceOrganismPhosphositeFile")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {

					System.out.println("drppm -ReplaceOrganismPhosphositeFile [orig_phosphosite_file] [additional phosphosite] [new phosphositeFile]");
					System.exit(0);
				}
				OrganismConversion2PhosphositeFile.execute(args_remain);	
				//DownloadAllPossibleSiteInfo
			} else if (type.equals("-DownloadAllPossibleSiteInfo")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {

					System.out.println("drppm -DownloadAllPossibleSiteInfo [Add something to run]");
					System.exit(0);
				}
				DownloadAllPossibleSiteInfo.execute(args_remain);	
				//AddNetworkNeighborEvidence
			} else if (type.equals("-AddNetworkNeighborEvidence")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -AddNetworkNeighborEvidence [Phosphosite_hrpd_motif_output_all_file] [humanInteractiveDBFile] [Phosphosite_kinase_substrate] [uniprot2geneIDFile] [motif_all_annotation_file] [outputFile]");
					System.exit(0);
				}
				AddNetworkNeighborEvidence.execute(args_remain);	
				//
			} else if (type.equals("-GenerateIntronRetentionBarPlot")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateIntronRetentionBarPlot [inputFile] [outputFile] [groupInfo]");
					System.exit(0);
				}
				GenerateIntronRetentionBarPlot.execute(args_remain);	
				//GenerateOverlappingResults
			} else if (type.equals("-GenerateOverlappingResults")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					
					System.out.println("drppm -GenerateOverlappingResults [path1] [path2] [path3] [tag1] [tag2] [tag3] [pvalue] [outputFolder]");
					System.exit(0);
				}
				GenerateOverlappingResults.execute(args_remain);	
				//RecurrentGeneMutFreq
			} else if (type.equals("-RecurrentGeneMutFreq")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					
					System.out.println("drppm -RecurrentGeneMutFreq [InputFileList] [SNVClassIndex]");
					System.exit(0);
				}
				RecurrentGeneMutFreq.execute(args_remain);	
				//AddRecurrenceAnnotation
			} else if (type.equals("-AddRecurrenceAnnotation")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					
					System.out.println("drppm -AddRecurrenceAnnotation [inputFile] [mutationFrequencyFile]");
					System.exit(0);
				}
				AddRecurrenceAnnotation.execute(args_remain);	
				//
			} else if (type.equals("-MATSGenerateResultTable")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					
					System.out.println("drppm -MATSGenerateResultTable [inputFile] [mutationFrequencyFile]");
					System.exit(0);
				}
				MATSGenerateResultTable.execute(args_remain);	
				
			} else if (type.equals("-GenerateComplexNetwork")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {

					System.out.println("drppm -GenerateComplexNetwork [complexFile] [uniprot2geneIDFile] [outputFile]");
					System.exit(0);
				}
				GenerateComplexNetwork.execute(args_remain);					
				//AppendBamReviewFile
				//SnpDetectPostProcessingScript
			} else if (type.equals("-SnpDetectPostProcessingScript")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {

					System.out.println("drppm -SnpDetectPostProcessingScript [inputFile] [bamFiles] [snpDetectPath] [bamPath]");
					System.exit(0);
				}
				SnpDetectPostProcessingScript.execute(args_remain);					
				//
				//
			} else if (type.equals("-AppendGermlineAlternativeAlleleCount")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {

					System.out.println("drppm -AppendGermlineAlternativeAlleleCount [Allele Matrix] [snvMutFile] [outputFile]");
					System.exit(0);
				}
				AppendGermlineAlternativeAlleleCount.execute(args_remain);					
				
				//
				
			} else if (type.equals("-AppendBamReviewFile")) { // for whole exome
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					
					System.out.println("drppm -AppendBamReviewFile [fileFile] [bamPathIDFile] [bamFile] [organism]");
					System.exit(0);
				}
				//
				AppendBamReviewFile.execute(args_remain);					
				//
			} else if (type.equals("-GenerateBamSoftLink")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					
					System.out.println("drppm -GenerateBamSoftLink [folderPath]");
					System.exit(0);
				}

				GenerateBamSoftLink.execute(args_remain);					
				//AppendCICEROHTMLLink
			} else if (type.equals("-AppendCICEROHTMLLink")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
			
					System.out.println("drppm -AppendCICEROHTMLLink [originalFile] [path] [organism]");
					System.exit(0);
				}

				AppendCICEROHTMLLink.execute(args_remain);					
				//StructureFractionClustering
			} else if (type.equals("-StructureFractionClustering")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -StructureFractionClustering [folderPath] [outputFile]");
					System.exit(0);
				}
				StructureFractionClustering.execute(args_remain);					
				//GrabEachFileInsertIDFasta
			} else if (type.equals("-GrabEachFileInsertIDFasta")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabEachFileInsertIDFasta [folderPath] [outputFile]");
					System.exit(0);
				}
				GrabEachFileInsertIDFasta.execute(args_remain);					
				//ChemoProjectGenerateMetaFile
			} else if (type.equals("-ChemoProjectGenerateMetaFile")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -ChemoProjectGenerateMetaFile [folderPath] [outputFile]");
					System.exit(0);
				}
				ChemoProjectGenerateMetaFile.execute(args_remain);					
				//GenerateQIIMEMatrixTable
			} else if (type.equals("-GenerateQIIMEMatrixTable")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateQIIMEMatrixTable [inputFile] [outputFile]");
					System.exit(0);
				}
				GenerateQIIMEMatrixTable.execute(args_remain);					
				//GenerateJasonMatrixTable
			} else if (type.equals("-GenerateJasonMatrixTable")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateJasonMatrixTable [inputFile] [outputFile]");
					System.exit(0);
				}
				GenerateJasonMatrixTable.execute(args_remain);					
				// CombineOTUCounts
			} else if (type.equals("-CombineOTUCounts")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineOTUCounts [inputFile] [outputFile]");
					System.exit(0);
				}
				CombineOTUCounts.execute(args_remain);					
				// NormalizePerHundredKilo
			} else if (type.equals("-NormalizePerHundredKilo")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizePerHundredKilo [inputFile] [outputFile]");
					System.exit(0);
				}
				NormalizePerHundredKilo.execute(args_remain);					
				// 
			} else if (type.equals("-PhosphoSummarizeResults")) {
				String[] args_remain = getRemaining(args);				
				if (args_remain.length == 0) {
					System.out.println("drppm -PhosphoSummarizeResults [inputFile] [upregFile] [dnregFile] [outputFile]");
					System.exit(0);
				}
				/*			String inputFile = args[0];
			String upregFile = args[1];
			String dnregFile = args[2];			
			String outputFile = args[3];*/
				PhosphoSummarizeResults.execute(args_remain);					
				// 
			} else {
				System.out.println("Here are the available programs");
				printProgramInfo();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void printProgramInfo() {
		System.out.println("DRPPM Version Number: " + VERSION);
		System.out.println("Main Categories of DRPPM could be accessed if you type");
		System.out.println("drppm -RNASEQ");
		System.out.println("	-LIMMA1 can estimate differential expression within one group vs other samples");
		System.out.println("	-LIMMA2 can estimate differential expression within two group of samples");
		System.out.println("	-GenerateVolcano plots a volcano plot based on LIMMA result");
		System.out.println("	-DEAddAnnotation add annotation to the differential expression");
		System.out.println("	-removeNonCoding remove genes tagged as noncoding RNA");
		
		System.out.println("drppm -PROTEOMICS");
		System.out.println("drppm -PHOSPHORYLATION");
		System.out.println("	-plotKinase can plot the kinase mapping");
		System.out.println("	-ParseMotifX generate motif file by grabbing the result from MotifX");
		System.out.println("	-MatchMotifX add the matching motif information");
		
		System.out.println("drppm -DNA");		
		System.out.println("    -SnpDetectPostProcessingScript can generate the script for SnpDetect post processing");
		
		System.out.println("drppm -GRAPH");
		System.out.println("	-plotHeatMap can plot heatmap");
				
		System.out.println("drppm -MISC");
		System.out.println("	-human2mouse convert gene list from human to mouse");
		System.out.println("	-mouse2human convert gene list from mouse to human");

		System.out.println("drppm -NETWORK");
		
		System.out.println("	-AddGeneKO2SampleName add gene KO information to gene expression matrix");
		System.out.println("	-GrabKeyword grab the keyword from header of a gene matrix file.  Useful as a limma input.");
		System.out.println("	-SampleFilter write out the samples containing the input keyword");

		System.out.println("	-CreateNetworkDisplay creates a network based on the input text");
		System.out.println("	-RunRScript run R script");
		System.out.println("	-GSEAgmt2txt convert GSEA gmt file into a txt file");
		System.out.println("	-HumanCentricProteinAlignment perform alignment of the uniprot proteins");
		System.out.println("	-MouseCentricProteinAlignment perform alignment of the uniprot proteins");
		System.out.println("	-GSEAHuman2Mouse human and mouse homology conversion for GSEA");
		System.out.println("	-Gene2TF check whether a TF list contains the gene set specified by the user");
		System.out.println("	-GEFisher performs Fisher Exact test to estimate whether a gene set is enriched in a differentially expressed gene set");
		System.out.println("	-GEFisherFilter performs Fisher Exact test to estimate whether a gene set is enriched in a differentially expressed gene set and filters the gene set");
	}
	public static void updateCONFIG(String inputFile) {
		try {
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String[] getRemaining(String[] args) {
		String[] args_remain = new String[args.length - 1];
		boolean startQuotation = false;
		LinkedList list = new LinkedList();
		String longStr = "";
		
		for (int i = 1; i < args.length; i++) {
			//System.out.println("args: " + args[i]);
			if (args[i].contains("\"") && !startQuotation) {
				startQuotation = true;
				longStr = args[i];
			} else if (args[i].contains("\"") && startQuotation) {
				longStr += " " + args[i];
				longStr.replaceAll("\"", "");
				list.add(longStr);
				startQuotation = false;
			} else {
				list.add(args[i]);
			}
		}
		
		int i = 0;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String str = (String)itr.next();
			args_remain[i] = str;
			//System.out.println(args_remain[i]);
			i++;
		}		
		return args_remain;
	}
}
