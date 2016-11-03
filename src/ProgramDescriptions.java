import customscript.AppendChromosomeNumber;
import customscript.ElenaConvertRefSeq2GeneName;
import gene_network_display.DisplayJsonFileNetwork;
import gene_network_display.GenerateNodeMetaDataSize;
import genomics.gctools.GCScanner;
import genomics.openreadingframe.GenerateLowComplexityDomainInfo;
import genomics.openreadingframe.OpenReadingFrameFinder;
import gtf_manupulation.Filter3PrimeGTFExon;
import jump.pipeline.tools.ExtractUniqPeptides;
import jump.pipeline.tools.FilterPSMInformationPeptide;
import jump.pipeline.tools.FilterPSMInformationProteinName;
import jump.pipeline.tools.GeneratePhosphoPeptideMatrix;
import jump.pipeline.tools.GenerateProteomeGeneMatrix;
import jump.pipeline.tools.MergeRowsMaximizePSM;
import pathway.tools.PathwayKappaScore;
import proteomics.SimulatedPeptideDigestion;
import stjude.projects.leventaki.SummarizeLeventakiProject;
import stjude.tools.rnaseq.RNASEQConfig2MappingScriptGenerator;
import BlastTool.GenerateBlastFile;
import DifferentialExpression.AppendLIMMAResult2Matrix;
import DifferentialExpression.CombineDEGeneSet;
import DifferentialExpression.CombineDEGeneSetLimitOverlap;
import DifferentialExpression.DEAddAnnotation;
import DifferentialExpression.DEAddAnnotationRelaxed;
import DifferentialExpression.DEGFilteredGeneSet;
import DifferentialExpression.ExtractDEGenes;
import DifferentialExpression.OverlapDEGeneSet;
import EnrichmentTool.ORASummaryTable;
import EnrichmentTool.ORASummaryTableHeatmap;
import EnrichmentTool.OverRepresentationAnalysis;
import EnrichmentTool.OverRepresentationAnalysisFDR;
import EnrichmentTool.OverRepresentationAnalysisWithoutFilter;
import EnrichmentTool.DAVID.GenerateGODatabaseDAVID;
import EnrichmentTool.DAVID.StandardizeGeneName;
import EnrichmentTool.GO.ParseGeneOntology;
import ExpressionAnalysis.CalculateCorrelationMatrix;
import ExpressionAnalysis.FilterMatrixExpression;
import ExpressionAnalysis.FilterMatrixFile;
import ExpressionAnalysis.GeneListMatrix;
import ExpressionAnalysis.GeneListMatrix2;
import ExpressionAnalysis.GenerateTrendPlot;
import ExpressionAnalysis.MergeSamples;
import ExpressionAnalysis.RemoveColumnsFromMatrix;
import GraphsFigures.BarPlotGenerator;
import GraphsFigures.BoxPlotGeneratorThreeGroup;
import GraphsFigures.BoxPlotGeneratorTwoColumn;
import GraphsFigures.BoxPlotGeneratorTwoGroup;
import GraphsFigures.MultipleBarPlotGenerator;
import GraphsFigures.SampleExprHistogram;
import IDConversion.ConvertUniprot2GeneAndAppend;
import IDConversion.EnsemblGeneID2GeneName;
import IDConversion.GeneName2EnsemblID;
import IDConversion.RefSeq2GeneName;
import IDConversion.SubGeneFromConversionTable;
import IDConversion.kgXrefConversion;
import IDConversion.kgXrefConversionProtein2GeneName;
import Integration.Visualization.ExpressionIntegrationDrawer;
import Integration.Visualization.ExpressionIntegrationDrawerFilter;
import Integration.Visualization.ExpressionIntegrationDrawerWhlPho;
import Integration.Visualization.IntegrationDrawerFilterGeneList;
import Integration.summarytable.ComprehensiveSummaryTableSampleTypeSNVFusion;
import Integration.summarytable.ComprehensiveSummaryTableSampleTypeSNVFusionFilter;
import Integration.summarytable.FilterSNVSamples;
import Integration.summarytable.IntegratedSummaryTable;
import Integration.summarytable.IntegratedSummaryTableFrequencyCount;
import Integration.summarytable.IntegrationAddGeneAnnotation;
import MISC.GrabGeneName;
import MISC.MISCConvertPeptideID;
import MISC.MergeGeneName;
import MatrixManipulation.AppendMatrixTogether;
import Metagenomic.Assembly.MergeFastQ;
import NetworkTools.ParseThroughSIF;
import NetworkTools.Layout.GenerateLayoutForEachHub;
import NetworkTools.Layout.GenerateMultipleCircles;
import NetworkTools.Layout.GenerateMultipleCirclesEdge;
import NetworkTools.Layout.GenerateMultipleCirclesLabels;
import NetworkTools.MISC.GenerateGraphStatistics;
import NetworkTools.MISC.GenerateSubgraph;
import NetworkTools.STRINGdbParsing.CleanBioplexTSVFile;
import NetworkTools.STRINGdbParsing.Convert2SJGraphFormat;
import NetworkTools.STRINGdbParsing.StringDBFilter;
import NetworkTools.jung.CalculateCentrality;
import NetworkTools.jung.CalculateCentralityModifyDistance;
import PhosphoTools.ARMSERMSProject.AssignKnownKinaseSubstrateRelationshipARMSERMS;
import PhosphoTools.ARMSERMSProject.NormalizeMatrix2IKAPARMSERMS;
import PhosphoTools.ARMSERMSProject.NormalizePhosphoAgainstWholeARMSERMS;
import PhosphoTools.ARMSERMSProject.NormalizeWholeMatrixARMSERMS;
import PhosphoTools.DegenerativeMotif.ExtendJUMPqSite;
import PhosphoTools.DegenerativeMotif.GenerateFastaFileFromJUMPqPeptide;
import PhosphoTools.DegenerativeMotif.GenerateFastaFileFromJUMPqSite;
import PhosphoTools.GSEA.ConvertKinaseGroupTxt2Gmt;
import PhosphoTools.Heatmap.GrabPhosphositeExpressionAll;
import PhosphoTools.Heatmap.GrabPhosphositeExpressionGeneCentric;
import PhosphoTools.HongBoProject.AssignKnownKinaseSubstrateRelationshipHongbo;
import PhosphoTools.HongBoProject.HongboAnnotateMotifInformation;
import PhosphoTools.HongBoProject.HongboAnnotateMotifInformationYuxinFile;
import PhosphoTools.HongBoProject.KinaseFamilyCluster;
import PhosphoTools.HongBoProject.PhosphoMotifEnrichment;
import PhosphoTools.KinaseActivity.AssignKnownKinaseSubstrateRelationship;
import PhosphoTools.KinaseActivity.AssignKnownKinaseSubstrateRelationshipFlex;
import PhosphoTools.KinaseActivity.CleanWhlProteome;
import PhosphoTools.KinaseActivity.NormalizeMatrix2IKAP;
import PhosphoTools.KinaseActivity.NormalizeMatrix2IKAPFlex;
import PhosphoTools.KinaseActivity.NormalizePhosphoAgainstWhole;
import PhosphoTools.KinaseActivity.NormalizePhosphoAgainstWholeFlex;
import PhosphoTools.KinaseActivity.NormalizeWholeGenome;
import PhosphoTools.KinaseActivity.NormalizeWholeGenomeFlex;
import PhosphoTools.MISC.FilterBackground2CoreProtein;
import PhosphoTools.MISC.GrabFastaFile;
import PhosphoTools.MISC.KunduLab.CalculatePercentConservation;
import PhosphoTools.MISC.KunduLab.ExtractUCSCMultipleSeqAlign;
import PhosphoTools.MISC.KunduLab.PSSMMotifFinder;
import PhosphoTools.Network.KinaseSubstrate2KinaseOnly;
import PhosphoTools.Network.KinaseSubstrateAll;
import PhosphoTools.PSSM.GenerateBackgroundFrequencyTable;
import PhosphoTools.PSSM.GeneratePSSMUniprotDatabase;
import PhosphoTools.PSSM.GenerateReferencePSSMTable;
import PhosphoTools.PSSM.NormalizePWMWithBackground;
import PhosphoTools.PSSM.ScoreDistribution.AppendPSSMScore2Matrix;
import PhosphoTools.PSSM.ScoreDistribution.AppendPSSMScore2PhosphoSiteMatrix;
import PhosphoTools.PSSM.ScoreDistribution.AssignKnownKinaseSubstrateSupplementary;
import PhosphoTools.PSSM.ScoreDistribution.PSSMCreateSupplementaryTable;
import PhosphoTools.PSSM.ScoreDistribution.PSSMScoreDistribution;
import PhosphoTools.PSSM.ScoreDistribution.PSSMScoreDistributionKinaseMotif;
import PhosphoTools.PSSM.ScoreDistribution.RandomSelectionPSSM;
import ProteinFeature.AminoAcidResidue.CalculateResidueFrequencyFastaFile;
import ProteinFeature.AminoAcidResidue.CalculateResidueMotif;
import ProteinFeature.AminoAcidResidue.CalculateResidueMotifBootstrap;
import ProteinFeature.AminoAcidResidue.CalculateResidueMotifBootstrap3;
import ProteinFeature.AminoAcidResidue.CalculateResidueMotifBootstrap4;
import ProteinFeature.AminoAcidResidue.CalculateResidueMotifBootstrapDE;
import ProteinFeature.AminoAcidResidue.CountGeneWithResidueRegionPlot;
import ProteinFeature.Charge.CalculateChargeFastaFile;
import ProteinFeature.Charge.ConvertGene2Uniprot;
import ProteinFeature.Charge.GenerateChargeGraph;
import ProteinFeature.Charge.MatchFasta2Coordinate;
import ProteinFeature.Hydrophobicity.CalculateHydrophobicityFastaFile;
import ProteinFeature.MEMEMotif.GenerateUniqFastaFile;
import ProteinFeature.SequenceConservation.AlignSEGSequence;
import ProteinFeature.SequenceConservation.ConservationSurvey;
import ProteinFeature.SequenceConservation.GenerateFastaSequenceForEachProtein;
import RNATools.PCPA.AddChr;
import RNATools.PCPA.CombinePCPAResults;
import RNATools.PCPA.ExtractPolyAReadsUsePolyALibrarySingleCell;
import RNATools.PCPA.GeneratePCPAHumanScriptComplete;
import RNATools.PCPA.MatchFq2Bam;
import RNATools.PCPA.PCPAAppendMetaDeta;
import RNAseqTools.CICERO.ChromosomeBarPlot;
import RNAseqTools.CICERO.ExtractFusionGenes;
import RNAseqTools.GTF.Mouse2GTF;
import RNAseqTools.GeneLengthAnalysis.TranscriptLengthSlidingWindow;
import RNAseqTools.GeneLengthAnalysis.TranscriptLengthSlidingWindowInhibitedGenes;
import RNAseqTools.IntronRetention.CombineSplicingDeficiencyName;
import RNAseqTools.IntronRetention.OverlapAllMouseHuman;
import RNAseqTools.IntronRetention.OverlapMouseHumanGeneName;
import RNAseqTools.Mapping.CombineHTSEQResult;
import RNAseqTools.Mapping.CombineHTSEQResultRaw;
import RNAseqTools.Mapping.CuffLinksScriptGenerator;
import RNAseqTools.Mapping.Fastq2FileList;
import RNAseqTools.Mapping.MergeBamFiles;
import RNAseqTools.Mapping.RPM2RPKMExon;
import RNAseqTools.Mapping.RPM2RPKMTranscript;
import RNAseqTools.Mapping.STARMappingScriptGenerator;
import RNAseqTools.Mapping.STARMappingScriptGeneratorForTrimFastq;
import RNAseqTools.Mapping.SummarizeStarMapping;
import RNAseqTools.Mapping.TrimmomaticScriptGenerator;
import RNAseqTools.SingleCell.Bootstrap.Filter0PSamples;
import RNAseqTools.SingleCell.Bootstrap.GenerateTrueFalseMatrix;
import RNAseqTools.SingleCell.Bootstrap.VariantMatrixBootstrap;
import RNAseqTools.SingleCell.CellOfOrigin.FisherExactTest2groupcomparison;
import RNAseqTools.SingleCell.CellOfOrigin.GenerateMatrixForTwoGroups;
import RNAseqTools.SingleCell.CellOfOrigin.GenerateNodeMetaBasedOnGroups;
import RNAseqTools.SingleCell.CellOfOrigin.GenerateSIFfromMinimumSpanningTree;
import RNAseqTools.SingleCell.CellOfOrigin.PostProcessingOfVariantMatrix;
import RNAseqTools.SingleCell.CellRanger.SpecialClassForDougGreen;
import RNAseqTools.SingleCell.Correlation.SpearmanRankCorrelation;
import RNAseqTools.SingleCell.Correlation.SpearmanRankCorrelationMatrix;
import RNAseqTools.SingleCell.MappingPipeline.CombineFastqFiles;
import RNAseqTools.SingleCell.MappingPipeline.GenerateFqFileList;
import RNAseqTools.SingleCell.MappingPipeline.GenerateFqFileListParallel;
import RNAseqTools.SingleCell.MappingPipeline.RemoveNAGenes;
import RNAseqTools.SingleCell.MappingPipeline.SingleCellRNAseqMapAndQuan;
import RNAseqTools.SingleCell.MappingPipeline.SingleCellRNAseqMapAndQuanReg;
import RNAseqTools.SingleCell.MappingPipeline.ValidateSTARMapping;
import RNAseqTools.SingleCell.RibosomeDepletion.CombineSingleCellSampleIntoOne;
import RNAseqTools.SingleCell.RibosomeDepletion.SeparateGeneMatrixIntoTwo;
import RNAseqTools.SingleCell.ZeroAnalysis.CompileDataForViolinPlot;
import RNAseqTools.SingleCell.ZeroAnalysis.GrabGeneLessThanValue;
import RNAseqTools.SingleCell.ZeroAnalysis.GrabGeneOverValue;
import RNAseqTools.SingleCell.ZeroAnalysis.GenerateZeroAnalysisBinningTable;
import RNAseqTools.Summary.CalculateIntersectingGenes;
import RNAseqTools.Summary.CombineEnrichmentPathwayPvalues;
import RNAseqTools.Summary.GenerateFPKMBinningTable;
import RNAseqTools.Summary.GenerateRNASEQCoverageStatistics;
import RNAseqTools.Summary.IntronExonCoverageBED;
import RNAseqTools.Summary.PlotBinningTable;
import RNAseqTools.circos.GenerateCircosCoverageBed;
import RNAseqTools.pipeline.GenerateLIMMAComparisonScript;
import TextMiningSoftwareAnnotation.WebTextMining;
import UniprotAnnotation.GenerateIDConversionMasterTable;
import WholeExonTool.Indel.FilterDuplicatedHits;
import WholeExonTool.SJSNVIndelPipeline.GenerateGRCh37liteSNVIndelScript;
import WholeExonTool.SJSNVIndelPipeline.GenerateHg19SNVIndelScript;
import WholeExonTool.SJSNVIndelPipeline.GenerateMm9SNVIndelScript;
import WholeExonTool.SNPPopulationDistribution.SNPrsPopulation;
import WholeExonTool.Summarize.EXCAPSummary;
import WholeExonTool.Summarize.EXONCAPHumanBasicStats;
import WholeExonTool.circos.FromSV2CircosInput;
import WholeExonTool.circos.Indel2CircosInput;
import WholeExonTool.circos.SNV2CircosInput;
import WholeExonTool.circos.SV2CircosInput;
import WholeExonTools.GenerateSNVTableFromMutationTable;
import WholeExonTools.Special.MouseGermlineAnalysis.SummarizeMouseIndelAnalysis;
import WholeExonTools.UnpairedPipeline.GenerateSNVUnpairedScriptSimple;
import Xenograph.CustomFastaCombiner;

/**
 * Using specific keyword to query the program information.
 * @author tshaw
 *
 */
public class ProgramDescriptions {

	
	public static String generateProgramInfo(String type) {
		String result = "#### List of programs in " + type + " ####\n";
		
		if (CalculateIntersectingGenes.type().equals(type)) {
			result += " -CalculateIntersectingGenes: " + CalculateIntersectingGenes.description() + "\n";
		}
		if (CombineEnrichmentPathwayPvalues.type().equals(type)) {
			result += " -CombineEnrichmentPathwayPvalues: " + CombineEnrichmentPathwayPvalues.description() + "\n";
		}
		if (Convert2SJGraphFormat.type().equals(type)) {
			result += " -Convert2SJGraphFormat: " + Convert2SJGraphFormat.description() + "\n";
		}
		if (GenerateSubgraph.type().equals(type)) {
			result += " -GenerateSubgraph: " + GenerateSubgraph.description() + "\n";
		}
		if (GrabGeneName.type().equals(type)) {
			result += " -GrabGeneName: " + GrabGeneName.description() + "\n";
		}
		if (GenerateGraphStatistics.type().equals(type)) {
			result += " -GenerateGraphStatistics: " + GenerateGraphStatistics.description() + "\n";
		}
		if (SampleExprHistogram.type().equals(type)) {
			result += " -GenerateHistogram: " + SampleExprHistogram.description() + "\n"; 
		}
		if (StringDBFilter.type().equals(type)) {
			result += " -StringDBFilter: " + StringDBFilter.description() + "\n";
		}
		if (GenerateUniqFastaFile.type().equals(type)) {
			result += " -GenerateUniqFastaFile: " + GenerateUniqFastaFile.description() + "\n";
		}
		if (CalculateChargeFastaFile.type().equals(type)) {
			result += " -CalculateChargeFastaFile: " + CalculateChargeFastaFile.description() + "\n";
		}
		if (BoxPlotGeneratorTwoGroup.type().equals(type)) {
			result += " -BoxPlotGeneratorTwoGroup: " + BoxPlotGeneratorTwoGroup.description() + "\n";
		}
		if (BoxPlotGeneratorThreeGroup.type().equals(type)) {
			result += " -BoxPlotGeneratorThreeGroup: " + BoxPlotGeneratorThreeGroup.description() + "\n";
		}
		if (MatchFasta2Coordinate.type().equals(type)) {
			result += " -MatchFasta2Coordinate: " + MatchFasta2Coordinate.description() + "\n";
		}
		if (GenerateChargeGraph.type().equals(type)) {
			result += " -GenerateChargeGraph: " + GenerateChargeGraph.description() + "\n";
		}
		if (CalculateHydrophobicityFastaFile.type().equals(type)) {
			result += " -CalculateHydrophobicityFastaFile: " + CalculateHydrophobicityFastaFile.description() + "\n";
		}
		if (ConvertGene2Uniprot.type().equals(type)) {
			result += " -ConvertGene2Uniprot: " + ConvertGene2Uniprot.description() + "\n";
		}
		if (WebTextMining.type().equals(type)) {
			result += " -WebTextMining: " + WebTextMining.description() + "\n";
		}
		if (ConvertUniprot2GeneAndAppend.type().equals(type)) {
			result += " -ConvertUniprot2GeneAndAppend: " + ConvertUniprot2GeneAndAppend.description() + "\n";
		}
		if (ParseGeneOntology.type().equals(type)) {
			result += " -ParseGeneOntology: " + ParseGeneOntology.description() + "\n";
		}
		if (GrabPhosphositeExpressionAll.type().equals(type)) {
			result += " -GrabPhosphositeExpressionAll: " + GrabPhosphositeExpressionAll.description() + "\n";
		}
		if (GrabPhosphositeExpressionGeneCentric.type().equals(type)) {
			result += " -GrabPhosphositeExpressionGeneCentric: " + GrabPhosphositeExpressionGeneCentric.description() + "\n";
		}
		if (DEAddAnnotation.type().equals(type)) {
			result += " -DEAddAnnotation: " + DEAddAnnotation.description() + "\n";
		}
		if (DEAddAnnotationRelaxed.type().equals(type)) {
			result += " -DEAddAnnotationRelaxed: " + DEAddAnnotationRelaxed.description() + "\n";
		}
		if (MergeGeneName.type().equals(type)) {
			result += " -MergeGeneName: " + MergeGeneName.description() + "\n";
		}
		if (ConvertKinaseGroupTxt2Gmt.type().equals(type)) {
			result += " -ConvertKinaseGroupTxt2Gmt: " + ConvertKinaseGroupTxt2Gmt.description() + "\n";
		}
		if (OverRepresentationAnalysisFDR.type().equals(type)) {
			result += " -OverRepresentationAnalysisFDR: " + OverRepresentationAnalysisFDR.description() + "\n";
		}
		if (GenerateMultipleCircles.type().equals(type)) {
			result += " -GenerateMultipleCircles: " + GenerateMultipleCircles.description() + "\n";
		}
		if (GenerateMultipleCirclesEdge.type().equals(type)) {
			result += " -GenerateMultipleCirclesEdge: " + GenerateMultipleCirclesEdge.description() + "\n";
		}
		if (GenerateMultipleCirclesLabels.type().equals(type)) {
			result += " GenerateMultipleCirclesLabels: " + GenerateMultipleCirclesLabels.description() + "\n";
		}
		if (OverRepresentationAnalysis.type().equals(type)) {
			result += " -OverRepresentationAnalysis: " + OverRepresentationAnalysis.description() + "\n";
		}
		if (OverRepresentationAnalysisWithoutFilter.type().equals(type)) {
			result += " -OverRepresentationAnalysisWithoutFilter: " + OverRepresentationAnalysisWithoutFilter.description() + "\n";
		}
		if (GenerateGODatabaseDAVID.type().equals(type)) {
			result += " -GenerateGODatabaseDAVID: " + GenerateGODatabaseDAVID.description() + "\n";
		}
		if (StandardizeGeneName.type().equals(type)) {
			result += " -StandardizeGeneName: " + StandardizeGeneName.description() + "\n";
		}
		if (AppendMatrixTogether.type().equals(type)) {
			result += " -AppendMatrixTogether: " + AppendMatrixTogether.description() + "\n";
		}
		if (CalculateResidueMotif.type().equals(type)) {
			result += " -CalculateResidueMotif: " + CalculateResidueMotif.description() + "\n";
		}
		if (CountGeneWithResidueRegionPlot.type().equals(type)) {
			result += " -CountGeneWithResidueRegionPlot: " + CountGeneWithResidueRegionPlot.description() + "\n";
		}
		if (GenerateTrendPlot.type().equals(type)) {
			result += " -GenerateTrendPlot: " + GenerateTrendPlot.description() + "\n";
		}
		if (GenerateIDConversionMasterTable.type().equals(type)) {
			result += " -GenerateIDConversionMasterTable: " + GenerateIDConversionMasterTable.description() + "\n";
		}
		if (CalculateCorrelationMatrix.type().equals(type)) {
			result += " -CalculateCorrelationMatrix: " + CalculateCorrelationMatrix.description() + "\n";
		}
		if (CalculateResidueFrequencyFastaFile.type().equals(type)) {
			result += " -CalculateResidueFrequencyFastaFile: " + CalculateResidueFrequencyFastaFile.description() + "\n";
		}
		if (TranscriptLengthSlidingWindow.type().equals(type)) {
			result += " -TranscriptLengthSlidingWindow: " + TranscriptLengthSlidingWindow.description() + "\n";
		}
		if (TranscriptLengthSlidingWindowInhibitedGenes.type().equals(type)) {
			result += " -TranscriptLengthSlidingWindowInhibitedGenes: " + TranscriptLengthSlidingWindowInhibitedGenes.description() + "\n";
		}
		if (SummarizeMouseIndelAnalysis.type().equals(type)) {
			result += " -SummarizeMouseIndelAnalysis: " + SummarizeMouseIndelAnalysis.description() + "\n";
		}
		if (AssignKnownKinaseSubstrateRelationship.type().equals(type)) {
			result += " -AssignKnownKinaseSubstrateRelationship: " + AssignKnownKinaseSubstrateRelationship.description() + "\n";
		}
		if (NormalizeMatrix2IKAP.type().equals(type)) {
			result += " -NormalizeMatrix2IKAP: " + NormalizeMatrix2IKAP.description() + "\n";
		}
		if (NormalizeWholeGenome.type().equals(type)) {
			result += " -NormalizeWholeGenome: " + NormalizeWholeGenome.description() + "\n";
		}
		if (NormalizePhosphoAgainstWhole.type().equals(type)) {
			result += " -NormalizePhosphoAgainstWhole: " + NormalizePhosphoAgainstWhole.description() + "\n";
		}
		if (ORASummaryTable.type().equals(type)) {
			result += " -ORASummaryTable: " + ORASummaryTable.description() + "\n";
		}
		if (ORASummaryTableHeatmap.type().equals(type)) {
			result += " -ORASummaryTableHeatmap: " + ORASummaryTableHeatmap.description() + "\n";
		}
		if (KinaseFamilyCluster.type().equals(type)) {
			result += " -KinaseFamilyCluster: " + KinaseFamilyCluster.description() + "\n";
		}
		if (PhosphoMotifEnrichment.type().equals(type)) {
			result += " -PhosphoMotifEnrichment: " + PhosphoMotifEnrichment.description() + "\n";
		}
		if (MISCConvertPeptideID.type().equals(type)) {
			result += " -MISCConvertPeptideID: " + MISCConvertPeptideID.description() + "\n";
		}
		if (GenerateNodeMetaDataSize.type().equals(type)) {
			result += " -GenerateNodeMetaDataSize: " + GenerateNodeMetaDataSize.description() + "\n";
		}
		if (GenerateFastaSequenceForEachProtein.type().equals(type)) {
			result += " -GenerateFastaSequenceForEachProtein: " + GenerateFastaSequenceForEachProtein.description() + "\n";
		}
		if (ConservationSurvey.type().equals(type)) {
			result += " -ConservationSurvey: " + ConservationSurvey.description() + "\n";
		}
		if (AlignSEGSequence.type().equals(type)) {
			result += " -AlignSEGSequence: " + AlignSEGSequence.description() + "\n";
		}
		if (AppendLIMMAResult2Matrix.type().equals(type)) {
			result += " -AppendLIMMAResult2Matrix: " + AppendLIMMAResult2Matrix.description() + "\n";
		}
		if (DEGFilteredGeneSet.type().equals(type)) {
			result += " -DEGFilteredGeneSet: " + DEGFilteredGeneSet.description() + "\n";
		}
		if (MergeSamples.type().equals(type)) {
			result += " -MergeSamples: " + MergeSamples.description() + "\n";
		}
		if (GeneListMatrix.type().equals(type)) {
			result += " -GeneListMatrix: " + GeneListMatrix.description() + "\n";
		}
		if (GeneListMatrix2.type().equals(type)) {
			result += " -GeneListMatrix2: " + GeneListMatrix2.description() + "\n";
		}		
		if (CleanWhlProteome.type().equals(type)) {
			result += " -CleanWhlProteome: " + CleanWhlProteome.description() + "\n";
		}
		if (ExpressionIntegrationDrawer.type().equals(type)) {
			result += " -ExpressionIntegrationDrawer: " + ExpressionIntegrationDrawer.description() + "\n";
		}
		if (KinaseSubstrate2KinaseOnly.type().equals(type)) {
			result += " -KinaseSubstrate2KinaseOnly: " + KinaseSubstrate2KinaseOnly.description() + "\n";
		}
		if (KinaseSubstrateAll.type().equals(type)) {
			result += " -KinaseSubstrateAll: " + KinaseSubstrateAll.description() + "\n";
		}
		if (ExpressionIntegrationDrawerWhlPho.type().equals(type)) {
			result += " -ExpressionIntegrationDrawerWhlPho: " + ExpressionIntegrationDrawerWhlPho.description() + "\n";
		}
		if (FilterMatrixFile.type().equals(type)) {
			result += " -FilterMatrixFile: " + FilterMatrixFile.description() + "\n";
		}
		if (ExtractUCSCMultipleSeqAlign.type().equals(type)) {
			result += " -ExtractUCSCMultipleSeqAlign: " + ExtractUCSCMultipleSeqAlign.description() + "\n";
		}
		if (CalculatePercentConservation.type().equals(type)) {
			result += " -CalculatePercentConservation: " + CalculatePercentConservation.description() + "\n";
		}
		if (PSSMMotifFinder.type().equals(type)) {
			result += " -PSSMMotifFinder: " + PSSMMotifFinder.description() + "\n";
		}
		if (SimulatedPeptideDigestion.type().equals(type)) {
			result += " -SimulatedPeptideDigestion: " + SimulatedPeptideDigestion.description() + "\n";
		}
		if (GrabFastaFile.type().equals(type)) {
			result += " -GrabFastaFile: " + GrabFastaFile.description() + "\n";
		}
		if (ChromosomeBarPlot.type().equals(type)) {
			result += " -ChromosomeBarPlot: " + ChromosomeBarPlot.description() + "\n";
		}
		if (ExtractFusionGenes.type().equals(type)) {
			result += " -ExtractFusionGenes: " + ExtractFusionGenes.description() + "\n";
		}
		if (EXCAPSummary.type().equals(type)) {
			result += " -EXCAPSummary: " + EXCAPSummary.description() + "\n";
		}
		if (EXONCAPHumanBasicStats.type().equals(type)) {
			result += " -EXONCAPHumanBasicStats: " + EXONCAPHumanBasicStats.description() + "\n";
		}
		if (STARMappingScriptGenerator.type().equals(type)) {
			result += " -STARMappingScriptGenerator: " + STARMappingScriptGenerator.description() + "\n";
		}
		if (SummarizeStarMapping.type().equals(type)) {
			result += " -SummarizeStarMapping: " + SummarizeStarMapping.description() + "\n";
		}
		if (CuffLinksScriptGenerator.type().equals(type)) {
			result += " -CuffLinksScriptGenerator: " + CuffLinksScriptGenerator.description() + "\n";
		}
		if (CombineHTSEQResult.type().equals(type)) {
			result += " -CombineHTSEQResult: " + CombineHTSEQResult.description() + "\n";
		}
		if (EnsemblGeneID2GeneName.type().equals(type)) {
			result += " -EnsemblGeneID2GeneName: " + EnsemblGeneID2GeneName.description() + "\n";
		}
		if (GenerateProteomeGeneMatrix.type().equals(type)) {
			result += " -GenerateProteomeGeneMatrix: " + GenerateProteomeGeneMatrix.description() + "\n";
		}
		if (GeneratePhosphoPeptideMatrix.type().equals(type)) {
			result += " -GeneratePhosphoPeptideMatrix: " + GeneratePhosphoPeptideMatrix.description() + "\n";
		}
		if (ExpressionIntegrationDrawerFilter.type().equals(type)) {
			result += " -ExpressionIntegrationDrawerFilter: " + ExpressionIntegrationDrawerFilter.description() + "\n";
		}
		if (GenerateFPKMBinningTable.type().equals(type)) {
			result += " -GenerateFPKMBinningTable: " + GenerateFPKMBinningTable.description() + "\n";
		}
		if (CalculateCentrality.type().equals(type)) {
			result += " -CalculateCentrality: " + CalculateCentrality.description() + "\n";
		}
		if (kgXrefConversion.type().equals(type)) {
			result += " -kgXrefConversion: " + kgXrefConversion.description() + "\n";
		}
		if (CombineSplicingDeficiencyName.type().equals(type)) {
			result += " -CombineSplicingDeficiencyName: " + CombineSplicingDeficiencyName.description() + "\n";
		}
		if (CalculateResidueMotifBootstrap.type().equals(type)) {
			result += " -CalculateResidueMotifBootstrap: " + CalculateResidueMotifBootstrap.description() + "\n";
		}
		if (CalculateResidueMotifBootstrapDE.type().equals(type)) {
			result += " -CalculateResidueMotifBootstrapDE: " + CalculateResidueMotifBootstrapDE.description() + "\n";
		}
		if (CalculateResidueMotifBootstrap3.type().equals(type)) {
			result += " -CalculateResidueMotifBootstrap3: " + CalculateResidueMotifBootstrap3.description() + "\n";
		}
		if (CalculateResidueMotifBootstrap4.type().equals(type)) {
			result += " -CalculateResidueMotifBootstrap4: " + CalculateResidueMotifBootstrap4.description() + "\n";
		}
		if (CalculateCentralityModifyDistance.type().equals(type)) {
			result += " -CalculateCentralityModifyDistance: " + CalculateCentralityModifyDistance.description() + "\n";
		}
		if (GenerateBackgroundFrequencyTable.type().equals(type)) {
			result += " -GenerateBackgroundFrequencyTable: " + GenerateBackgroundFrequencyTable.description() + "\n";
		}
		if (MergeFastQ.type().equals(type)) {
			result += " -MergeFastQ: " + MergeFastQ.description() + "\n";
		}
		if (NormalizePWMWithBackground.type().equals(type)) {
			result += " -NormalizePWMWithBackground: " + NormalizePWMWithBackground.description() + "\n";
		}
		if (GeneratePSSMUniprotDatabase.type().equals(type)) {
			result += " -GeneratePSSMUniprotDatabase: " + GeneratePSSMUniprotDatabase.description() + "\n";
		}
		if (GenerateReferencePSSMTable.type().equals(type)) {
			result += " -GenerateReferencePSSMTable: " + GenerateReferencePSSMTable.description() + "\n";
		}
		if (FilterBackground2CoreProtein.type().equals(type)) {
			result += " -FilterBackground2CoreProtein: " + FilterBackground2CoreProtein.description() + "\n";
		}
		if (PlotBinningTable.type().equals(type)) {
			result += " -PlotBinningTable: " + PlotBinningTable.description() + "\n";
		}
		if (IntronExonCoverageBED.type().equals(type)) {
			result += " -IntronExonCoverageBED: " + IntronExonCoverageBED.description() + "\n";
		}
		if (GenerateRNASEQCoverageStatistics.type().equals(type)) {
			result += " -GenerateRNASEQCoverageStatistics: " + GenerateRNASEQCoverageStatistics.description() + "\n";
		}
		if (CompileDataForViolinPlot.type().equals(type)) {
			result += " -CompileDataForViolinPlot: " + CompileDataForViolinPlot.description() + "\n";
		}
		if (GrabGeneOverValue.type().equals(type)) {
			result += " -GrabGeneOverValue: " + GrabGeneOverValue.description() + "\n";
		}
		if (GenerateZeroAnalysisBinningTable.type().equals(type)) {
			result += " -GenerateZeroAnalysisBinningTable: " + GenerateZeroAnalysisBinningTable.description() + "\n";
		}
		if (BoxPlotGeneratorTwoColumn.type().equals(type)) {
			result += " -BoxPlotGeneratorTwoColumn: " + BoxPlotGeneratorTwoColumn.description() + "\n";
		}
		if (SpearmanRankCorrelation.type().equals(type)) {
			result += " -SpearmanRankCorrelation: " + SpearmanRankCorrelation.description() + "\n";
		}
		if (GrabGeneLessThanValue.type().equals(type)) {
			result += " -GrabGeneLessThanValue: " + GrabGeneLessThanValue.description() + "\n";
		}
		if (OverlapMouseHumanGeneName.type().equals(type)) {
			result += " -OverlapMouseHumanGeneName: " + OverlapMouseHumanGeneName.description() + "\n";
		}
		if (OverlapAllMouseHuman.type().equals(type)) {
			result += " -OverlapAllMouseHuman: " + OverlapAllMouseHuman.description() + "\n";
		}
		if (FilterMatrixExpression.type().equals(type)) {
			result += " -FilterMatrixExpression " + FilterMatrixExpression.description() + "\n";
		}
		if (PSSMScoreDistributionKinaseMotif.type().equals(type)) {
			result += " -PSSMScoreDistributionKinaseMotif: " + PSSMScoreDistributionKinaseMotif.description() + "\n";
		}
		if (PSSMScoreDistribution.type().equals(type)) {
			result += " -PSSMScoreDistribution: " + PSSMScoreDistribution.description() + "\n";
		}
		if (RandomSelectionPSSM.type().equals(type)) {
			result += " -RandomSelectionPSSM: " + RandomSelectionPSSM.description() + "\n";
		}
		if (AppendPSSMScore2Matrix.type().equals(type)) {
			result += " -AppendPSSMScore2Matrix: " + AppendPSSMScore2Matrix.description() + "\n";
		}
		if (PSSMCreateSupplementaryTable.type().equals(type)) {
			result += " -PSSMCreateSupplementaryTable: " + PSSMCreateSupplementaryTable.description() + "\n";
		}
		if (AssignKnownKinaseSubstrateSupplementary.type().equals(type)) {
			result += " -AssignKnownKinaseSubstrateSupplementary: " + AssignKnownKinaseSubstrateSupplementary.description() + "\n";
		}
		if (AppendPSSMScore2PhosphoSiteMatrix.type().equals(type)) {
			result += " -AppendPSSMScore2PhosphoSiteMatrix: " + AppendPSSMScore2PhosphoSiteMatrix.description() + "\n";
		}
		if (RemoveColumnsFromMatrix.type().equals(type)) {
			result += " -RemoveColumnsFromMatrix: " + RemoveColumnsFromMatrix.description() + "\n";
		}
		if (MergeRowsMaximizePSM.type().equals(type)) {
			result += " -MergeRowsMaximizePSM: " + MergeRowsMaximizePSM.description() + "\n";
		}
		if (AssignKnownKinaseSubstrateRelationshipHongbo.type().equals(type)) {
			result += " -AssignKnownKinaseSubstrateRelationshipHongbo: " + AssignKnownKinaseSubstrateRelationshipHongbo.description() + "\n";
		}
		if (GenerateMm9SNVIndelScript.type().equals(type)) {
			result += " -GenerateMm9SNVIndelScript: " + GenerateMm9SNVIndelScript.description() + "\n";
		}
		if (ComprehensiveSummaryTableSampleTypeSNVFusion.type().equals(type)) {
			result += " -ComprehensiveSummaryTableSampleTypeSNVFusion: " + ComprehensiveSummaryTableSampleTypeSNVFusion.description() + "\n";
		}
		if (FilterSNVSamples.type().equals(type)) {
			result += " -FilterSNVSamples: " + FilterSNVSamples.description() + "\n";
		}
		if (RefSeq2GeneName.type().equals(type)) {
			result += " -RefSeq2GeneName: " + RefSeq2GeneName.description() + "\n";
		}
		if (ComprehensiveSummaryTableSampleTypeSNVFusionFilter.type().equals(type)) {
			result += " -ComprehensiveSummaryTableSampleTypeSNVFusionFilter: " + ComprehensiveSummaryTableSampleTypeSNVFusionFilter.description() + "\n";
		}
		if (FilterPSMInformationPeptide.type().equals(type)) {
			result += " -FilterPSMInformationPeptide: " + FilterPSMInformationPeptide.description() + "\n";
		}
		if (FilterPSMInformationProteinName.type().equals(type)) {
			result += " -FilterPSMInformationProteinName: " + FilterPSMInformationProteinName.description() + "\n";
		}
		
		if (ExtractUniqPeptides.type().equals(type)) {
			result += " -ExtractUniqPeptides: " + ExtractUniqPeptides.description() + "\n";
		}
		if (OverlapDEGeneSet.type().equals(type)) {
			result += " -OverlapDEGeneSet: " + OverlapDEGeneSet.description() + "\n";
		}
		if (CombineDEGeneSet.type().equals(type)) {
			result += " -CombineDEGeneSet: " + CombineDEGeneSet.description() + "\n";
		}
		if (IntegrationAddGeneAnnotation.type().equals(type)) {
			result += " -IntegrationAddGeneAnnotation: " + IntegrationAddGeneAnnotation.description() + "\n";
		}
		if (IntegratedSummaryTable.type().equals(type)) {
			result += " -IntegratedSummaryTable: " + IntegratedSummaryTable.description() + "\n";
		}
		if (ExtractDEGenes.type().equals(type)) {
			result += " -ExtractDEGenes: " + ExtractDEGenes.description() + "\n";
		}
		if (IntegrationDrawerFilterGeneList.type().equals(type)) {
			result += " -IntegrationDrawerFilterGeneList: " + IntegrationDrawerFilterGeneList.description() + "\n";
		}
		if (Filter3PrimeGTFExon.type().equals(type)) {
			result += " -Filter3PrimeGTFExon: " + Filter3PrimeGTFExon.description() + "\n";
		}
		if (RPM2RPKMTranscript.type().equals(type)) {
			result += " -RPM2RPKMTranscript: " + RPM2RPKMTranscript.description() + "\n";
		}
		if (RPM2RPKMExon.type().equals(type)) {
			result += " -RPM2RPKMExon: " + RPM2RPKMExon.description() + "\n";
		}
		if (BarPlotGenerator.type().equals(type)) {
			result += " -BarPlotGenerator: " + BarPlotGenerator.description() + "\n";
		}
		if (MultipleBarPlotGenerator.type().equals(type)) {
			result += " -MultipleBarPlotGenerator: " + MultipleBarPlotGenerator.description() + "\n";
		}
		if (CombineDEGeneSetLimitOverlap.type().equals(type)) {
			result += " -CombineDEGeneSetLimitOverlap: " + CombineDEGeneSetLimitOverlap.description() + "\n";
		}
		if (GenerateBlastFile.type().equals(type)) {
			result += " -GenerateBlastFile: " + GenerateBlastFile.description() + "\n";
		}
		if (FromSV2CircosInput.type().equals(type)) {
			result += " -FromSV2CircosInput: " + FromSV2CircosInput.description() + "\n";
		}
		if (SNV2CircosInput.type().equals(type)) {
			result += " SNV2CircosInput: " + SNV2CircosInput.description() + "\n";
		}
		if (SV2CircosInput.type().equals(type)) {
			result += " SV2CircosInput: " + SV2CircosInput.description() + "\n";
		}
		if (Indel2CircosInput.type().equals(type)) {
			result += " Indel2CircosInput: " + Indel2CircosInput.description() + "\n";
		}
		if (GCScanner.type().equals(type)) {
			result += " GCScanner: " + GCScanner.description() + "\n";
		}
		if (SingleCellRNAseqMapAndQuan.type().equals(type)) {
			result += " SingleCellRNAseqMapAndQuan: " + SingleCellRNAseqMapAndQuan.description() + "\n";
		}
		if (RemoveNAGenes.type().equals(type)) {
			result += " RemoveNAGenes: " + RemoveNAGenes.description() + "\n";
		}
		if (CombineHTSEQResultRaw.type().equals(type)) {
			result += " CombineHTSEQResultRaw: " + CombineHTSEQResultRaw.description() + "\n";
		}
		if (ExtractPolyAReadsUsePolyALibrarySingleCell.type().equals(type)) {
			result += " ExtractPolyAReadsUsePolyALibrarySingleCell: " + ExtractPolyAReadsUsePolyALibrarySingleCell.description() + "\n";
		}
		if (GeneratePCPAHumanScriptComplete.type().equals(type)) {
			result += " GeneratePCPAHumanScriptComplete: " + GeneratePCPAHumanScriptComplete.description() + "\n";
		}
		if (Fastq2FileList.type().equals(type)) {
			result += " Fastq2FileList: " + Fastq2FileList.description() + "\n";
		}
		if (MergeBamFiles.type().equals(type)) {
			result += " MergeBamFiles: " + MergeBamFiles.description() + "\n";
		}
		if (RNASEQConfig2MappingScriptGenerator.type().equals(type)) {
			result += " RNASEQConfig2MappingScriptGenerator: " + RNASEQConfig2MappingScriptGenerator.description() + "\n";
		}
		if (GenerateCircosCoverageBed.type().equals(type)) {
			result += " GenerateCircosCoverageBed: " + GenerateCircosCoverageBed.description() + "\n";
		}
		if (GenerateLIMMAComparisonScript.type().equals(type)) {
			result += " GenerateLIMMAComparisonScript: " + GenerateLIMMAComparisonScript.description() + "\n";
		}
		if (GenerateFqFileList.type().equals(type)) {
			result += " GenerateFqFileList: " + GenerateFqFileList.description() + "\n";
		}
		if (GenerateFqFileListParallel.type().equals(type)) {
			result += " GenerateFqFileListParallel: " + GenerateFqFileListParallel.description() + "\n";
		}
		if (MatchFq2Bam.type().equals(type)) {
			result += " MatchFq2Bam: " + MatchFq2Bam.description() + "\n";
		}
		if (AddChr.type().equals(type)) {
			result += " AddChr: " + AddChr.description() + "\n";
		}
		if (TrimmomaticScriptGenerator.type().equals(type)) {
			result += " TrimmomaticScriptGenerator: " + TrimmomaticScriptGenerator.description() + "\n";
		}
		if (STARMappingScriptGeneratorForTrimFastq.type().equals(type)) {
			result += " STARMappingScriptGeneratorForTrimFastq: " + STARMappingScriptGeneratorForTrimFastq.description() + "\n";
		}
		if (ValidateSTARMapping.type().equals(type)) {
			result += " ValidateSTARMapping: " + ValidateSTARMapping.description() + "\n";
		}
		if (CombineFastqFiles.type().equals(type)) {
			result += " CombineFastqFiles: " + CombineFastqFiles.description() + "\n";
		}
		if (SingleCellRNAseqMapAndQuanReg.type().equals(type)) {
			result += " SingleCellRNAseqMapAndQuanReg: " + SingleCellRNAseqMapAndQuanReg.description() + "\n";
		}
		if (IntegratedSummaryTableFrequencyCount.type().equals(type)) {
			result += " IntegratedSummaryTableFrequencyCount: " + IntegratedSummaryTableFrequencyCount.description() + "\n";
		}
		if (FilterDuplicatedHits.type().equals(type)) {
			result += " FilterDuplicatedHits: " + FilterDuplicatedHits.description() + "\n";
		}
		if (CombinePCPAResults.type().equals(type)) {
			result += " CombinePCPAResults: " + CombinePCPAResults.description() + "\n";
		}
		if (PCPAAppendMetaDeta.type().equals(type)) {
			result += " PCPAAppendMetaDeta: " + PCPAAppendMetaDeta.description() + "\n";
		}
		if (ParseThroughSIF.type().equals(type)) {
			result += " ParseThroughSIF: " + ParseThroughSIF.description() + "\n";
		}
		if (AssignKnownKinaseSubstrateRelationshipARMSERMS.type().equals(type)) {
			result += " AssignKnownKinaseSubstrateRelationshipARMSERMS: " + AssignKnownKinaseSubstrateRelationshipARMSERMS.description() + "\n";
		}
		if (NormalizeMatrix2IKAPARMSERMS.type().equals(type)) {
			result += " NormalizeMatrix2IKAPARMSERMS: " + NormalizeMatrix2IKAPARMSERMS.description() + "\n";
		}
		if (NormalizeWholeMatrixARMSERMS.type().equals(type)) {
			result += " NormalizeWholeMatrixARMSERMS: " + NormalizeWholeMatrixARMSERMS.description() + "\n";
		}
		if (NormalizePhosphoAgainstWholeARMSERMS.type().equals(type)) {
			result += " NormalizePhosphoAgainstWholeARMSERMS: " + NormalizePhosphoAgainstWholeARMSERMS.description() + "\n";
		}
		if (CustomFastaCombiner.type().equals(type)) {
			result += " CustomFastaCombiner: " + CustomFastaCombiner.description() + "\n";
		}
		if (Mouse2GTF.type().equals(type)) {
			result += " Mouse2GTF: " + Mouse2GTF.description() + "\n";
		}
		if (GenerateSNVTableFromMutationTable.type().equals(type)) {
			result += " GenerateSNVTableFromMutationTable: " + GenerateSNVTableFromMutationTable.description() + "\n";
		}
		if (PostProcessingOfVariantMatrix.type().equals(type)) {
			result += " PostProcessingOfVariantMatrix: " + PostProcessingOfVariantMatrix.description() + "\n";
		}
		if (kgXrefConversionProtein2GeneName.type().equals(type)) {
			result += " kgXrefConversionProtein2GeneName: " + kgXrefConversionProtein2GeneName.description() + "\n";
		}
		if (SpearmanRankCorrelationMatrix.type().equals(type)) {
			result += " SpearmanRankCorrelationMatrix: " + SpearmanRankCorrelationMatrix.description() + "\n";
		}
		if (OpenReadingFrameFinder.type().equals(type)) {
			result += " OpenReadingFrameFinder: " + OpenReadingFrameFinder.description() + "\n";
		}
		if (FisherExactTest2groupcomparison.type().equals(type)) {
			result += " FisherExactTest2groupcomparison: " + FisherExactTest2groupcomparison.description() + "\n";
		}
		if (GenerateMatrixForTwoGroups.type().equals(type)) {
			result += " GenerateMatrixForTwoGroups: " + GenerateMatrixForTwoGroups.description() + "\n";
		}
		if (GenerateSIFfromMinimumSpanningTree.type().equals(type)) {
			result += " GenerateSIFfromMinimumSpanningTree: " + GenerateSIFfromMinimumSpanningTree.description() + "\n";
		}
		if (GenerateNodeMetaBasedOnGroups.type().equals(type)) {
			result += " GenerateNodeMetaBasedOnGroups: " + GenerateNodeMetaBasedOnGroups.description() + "\n";
		}
		if (SpecialClassForDougGreen.type().equals(type)) {
			result += " SpecialClassForDougGreen: " + SpecialClassForDougGreen.description() + "\n";
		}
		if (SeparateGeneMatrixIntoTwo.type().equals(type)) {
			result += " SeparateGeneMatrixIntoTwo: " + SeparateGeneMatrixIntoTwo.description() + "\n";
		}
		if (CombineSingleCellSampleIntoOne.type().equals(type)) {
			result += " CombineSingleCellSampleIntoOne: " + CombineSingleCellSampleIntoOne.description() + "\n";
		}
		if (SubGeneFromConversionTable.type().equals(type)) {
			result += " SubGeneFromConversionTable: " + SubGeneFromConversionTable.description() + "\n";
		}
		if (GeneName2EnsemblID.type().equals(type)) {
			result += " GeneName2EnsemblID: " + GeneName2EnsemblID.description() + "\n";
		}
		if (AppendChromosomeNumber.type().equals(type)) {
			result += " AppendChromosomeNumber: " + AppendChromosomeNumber.description() + "\n";
		}
		if (GenerateLowComplexityDomainInfo.type().equals(type)) {
			result += " GenerateLowComplexityDomainInfo: " + GenerateLowComplexityDomainInfo.description() + "\n";
		}
		if (ElenaConvertRefSeq2GeneName.type().equals(type)) {
			result += " ElenaConvertRefSeq2GeneName: " + ElenaConvertRefSeq2GeneName.description() + "\n";
		}
		if (GenerateHg19SNVIndelScript.type().equals(type)) {
			result += " GenerateHg19SNVIndelScript: " + GenerateHg19SNVIndelScript.description() + "\n";
		}
		if (GenerateGRCh37liteSNVIndelScript.type().equals(type)) {
			result += " GenerateGRCh37liteSNVIndelScript: " + GenerateGRCh37liteSNVIndelScript.description() + "\n";
		}
		if (GenerateSNVUnpairedScriptSimple.type().equals(type)) {
			result += " GenerateSNVUnpairedScriptSimple: " + GenerateSNVUnpairedScriptSimple.description() + "\n";
		}
		
		if (VariantMatrixBootstrap.type().equals(type)) {
			result += " VariantMatrixBootstrap: " + VariantMatrixBootstrap.description() + "\n";
		}
		if (Filter0PSamples.type().equals(type)) {
			result += " Filter0PSamples: " + Filter0PSamples.description() + "\n";
		}
		if (GenerateTrueFalseMatrix.type().equals(type)) {
			result += " GenerateTrueFalseMatrix: " + GenerateTrueFalseMatrix.description() + "\n";
		}
		if (DisplayJsonFileNetwork.type().equals(type)) {
			result += " DisplayJsonFileNetwork: " + DisplayJsonFileNetwork.description() + "\n";
		}
		if (GenerateLayoutForEachHub.type().equals(type)) {
			result += " GenerateLayoutForEachHub: " + GenerateLayoutForEachHub.description() + "\n";
		}
		if (NormalizeMatrix2IKAPFlex.type().equals(type)) {
			result += " NormalizeMatrix2IKAPFlex: " + NormalizeMatrix2IKAPFlex.description() + "\n";
		}
		if (NormalizeWholeGenomeFlex.type().equals(type)) {
			result += " NormalizeWholeGenomeFlex: " + NormalizeWholeGenomeFlex.description() + "\n";
		}
		if (NormalizePhosphoAgainstWholeFlex.type().equals(type)) {
			result += " NormalizePhosphoAgainstWholeFlex: " + NormalizePhosphoAgainstWholeFlex.description() + "\n";
		}
		if (AssignKnownKinaseSubstrateRelationshipFlex.type().equals(type)) {
			result += " AssignKnownKinaseSubstrateRelationshipFlex: " + AssignKnownKinaseSubstrateRelationshipFlex.description() + "\n";
		}
		if (SNPrsPopulation.type().equals(type)) {
			result += " SNPrsPopulation: " + SNPrsPopulation.description() + "\n";
		}
		if (GenerateFastaFileFromJUMPqSite.type().equals(type)) {
			result += " GenerateFastaFileFromJUMPqSite: " + GenerateFastaFileFromJUMPqSite.description() + "\n";
		}
		if (ExtendJUMPqSite.type().equals(type)) {
			result += " ExtendJUMPqSite: " + ExtendJUMPqSite.description() + "\n";
		}
		if (GenerateFastaFileFromJUMPqPeptide.type().equals(type)) {
			result += " GenerateFastaFileFromJUMPqPeptide: " + GenerateFastaFileFromJUMPqPeptide.description() + "\n";
		}
		if (HongboAnnotateMotifInformation.type().equals(type)) {
			result += " HongboAnnotateMotifInformation: " + HongboAnnotateMotifInformation.description() + "\n";
		}
		if (HongboAnnotateMotifInformationYuxinFile.type().equals(type)) {
			result += " HongboAnnotateMotifInformationYuxinFile: " + HongboAnnotateMotifInformationYuxinFile.description() + "\n";
		}
		if (SummarizeLeventakiProject.type().equals(type)) {
			result += " SummarizeLeventakiProject: " + SummarizeLeventakiProject.description() + "\n";
		}
		if (CleanBioplexTSVFile.type().equals(type)) {
			result += " CleanBioplexTSVFile: " + CleanBioplexTSVFile.description() + "\n";
		}
		if (PathwayKappaScore.type().equals(type)) {
			result += " PathwayKappaScore: " + PathwayKappaScore.description() + "\n";
		}
		return result;
	}
}
