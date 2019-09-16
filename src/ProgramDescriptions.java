import network.ParseThroughSIF;
import network.MISC.GenerateGraphStatistics;
import network.MISC.GenerateSubgraph;
import network.db.biogrid.annotation.GenerateBiogrid2SIF;
import network.db.biogrid.annotation.GenerateBiogrid2SIFColocalization;
import network.db.biogrid.annotation.GenerateBiogrid2SIFPhysical;
import network.db.compass.CompassGenerateSifFile;
import network.db.string.annotation.CleanBioplexTSVFile;
import network.db.string.annotation.Convert2SJGraphFormat;
import network.db.string.annotation.StringDBFilter;
import network.geneset.SIF2Geneset;
import network.jung.CalculateCentrality;
import network.jung.CalculateCentralityModifyDistance;
import network.layout.GenerateLayoutForEachHub;
import network.layout.GenerateMultipleCircles;
import network.layout.GenerateMultipleCirclesEdge;
import network.layout.GenerateMultipleCirclesFlex;
import network.layout.GenerateMultipleCirclesLabels;
import network.layout.NetworkNodeReplaceColor;
import network.layout.RemoveRedundantEdges;
import network.modules.CalculateDistanceBetweenModules;
import network.statistics.CalculateGraphStatistics;
import nextgenerationsequencing.fastq.SplitFastqForwardReverse;
import bedtools.BedAddRemoveChr;
import mappingtools.Bam2Fastq;
import mappingtools.samtools.v0_1_17.SummarizeFlagStats;
import mathtools.expressionanalysis.differentialexpression.AppendLIMMAResult2Matrix;
import mathtools.expressionanalysis.differentialexpression.CalculateWilcoxon;
import mathtools.expressionanalysis.differentialexpression.CombineDEGeneSet;
import mathtools.expressionanalysis.differentialexpression.CombineDEGeneSetLimitOverlap;
import mathtools.expressionanalysis.differentialexpression.DEAddAnnotation;
import mathtools.expressionanalysis.differentialexpression.DEAddAnnotationRelaxed;
import mathtools.expressionanalysis.differentialexpression.DEGFilteredGeneSet;
import mathtools.expressionanalysis.differentialexpression.ExtractDEGenes;
import mathtools.expressionanalysis.differentialexpression.GrabSampleNameWithKeyword;
import mathtools.expressionanalysis.differentialexpression.GrabSampleNameWithoutKeyword;
import mathtools.expressionanalysis.differentialexpression.OverlapDEGeneSet;
import metagenomics.assembly.MergeFastQ;
import microarray.tools.idconversion.MicroArrayIDConversionAnnotation;
import microarray.tools.idconversion.MicroArrayIDConversionFlex;
import microarray.tools.methylation.EPIC850K.BMIQNormalizationSingleSample;
import microarray.tools.methylation.EPIC850K.CombineBMIQFiles;
import microarray.tools.methylation.EPIC850K.EPIC850KAveragedBEDFile;
import microarray.tools.methylation.EPIC850K.EPIC850KBedGraph2BW;
import microarray.tools.methylation.EPIC850K.EPIC850KGenerateBEDFile;
import microarray.tools.methylation.EPIC850K.EPIC850KMostMADVariableProbe;
import microarray.tools.methylation.EPIC850K.EPIC850KWilcoxonTestMethylation;
import microarray.tools.methylation.EPIC850K.Epic850KHyperHypoMethylationFilter;
import microarray.tools.methylation.EPIC850K.Methylation850KAppendGeneInfo;
import microarray.tools.methylation.EPIC850K.Methylation850KWilcoxonTestAppendGeneInfo;
import misc.CustomFastaCombiner;
import misc.ExtractRandomFastaSequence;
import misc.FilterDuplicate;
import misc.GenerateGSEADataset;
import misc.GrabGeneName;
import misc.GrabUniqValuesFromColumn;
import misc.KeepProteinCodingGenes;
import misc.MISCConvertPeptideID;
import misc.Matrix2Addition;
import misc.Matrix2Exponent;
import misc.MergeGeneName;
import misc.MergeGeneNameClean;
import misc.OverlapTwoFiles;
import misc.RemoveQuotations;
import misc.SplitFilesCols;
import misc.SplitFilesRows;
import idconversion.cross_species.AppendHuman2Mouse;
import idconversion.cross_species.AppendMouse2Human;
import idconversion.cross_species.EnsureUniqGeneNamesHumanMouse;
import idconversion.cross_species.GMTHuman2Mouse;
import idconversion.cross_species.GMTMouse2Human;
import idconversion.ensembl.MicroarrayEnsembl2GeneName;
import idconversion.protein2genome.FastaRefSeq2Ensembl;
import idconversion.tools.CleanEnsemblGeneID2GeneName;
import idconversion.tools.ConvertUniprot2GeneAndAppend;
import idconversion.tools.EnsemblGeneID2GeneName;
import idconversion.tools.EnsemblGeneID2GeneNameXenograft;
import idconversion.tools.EnsemblGeneIDAppendAnnotation;
import idconversion.tools.EnsemblGeneIDAppendGeneName;
import idconversion.tools.GeneName2EnsemblID;
import idconversion.tools.GeneralIDConversion;
import idconversion.tools.RefSeq2GeneName;
import idconversion.tools.SubGeneFromConversionTable;
import idconversion.tools.kgXrefAppendOfficialGeneSymbol;
import idconversion.tools.kgXrefConversion;
import idconversion.tools.kgXrefConversionProtein2GeneName;
import integrate.Visualization.ExpressionIntegrationDrawer;
import integrate.Visualization.ExpressionIntegrationDrawerFilter;
import integrate.Visualization.ExpressionIntegrationDrawerWhlPho;
import integrate.Visualization.IntegrationDrawerFilterGeneList;
import integrate.summarytable.ComprehensiveSummaryTableSampleTypeSNVFusion;
import integrate.summarytable.ComprehensiveSummaryTableSampleTypeSNVFusionFilter;
import integrate.summarytable.FilterSNVSamples;
import integrate.summarytable.IntegratedSummaryTable;
import integrate.summarytable.IntegratedSummaryTableFrequencyCount;
import integrate.summarytable.IntegrationAddGeneAnnotation;
import customScript.AppendChromosomeNumber;
import customScript.ElenaConvertRefSeq2GeneName;
import enrichment.tool.go.ParseGeneOntology;
import expression.matrix.tools.AppendMatrixTogether;
import expressionanalysis.tools.AppendMADValue;
import expressionanalysis.tools.CalculateCorrelationMatrix;
import expressionanalysis.tools.CombineTwoMatrixWithMismatch;
import expressionanalysis.tools.CombineTwoMatrixWithMismatchDoubleGene;
import expressionanalysis.tools.CorrectMarSeptGeneName;
import expressionanalysis.tools.FilterBasedOnAnnotation;
import expressionanalysis.tools.FilterMatrixColumnValue;
import expressionanalysis.tools.FilterMatrixExpression;
import expressionanalysis.tools.FilterMatrixFile;
import expressionanalysis.tools.FilterMatrixFileFlex;
import expressionanalysis.tools.GeneListMatrix;
import expressionanalysis.tools.GeneListMatrix2;
import expressionanalysis.tools.GenerateSpearmanRankMatrix;
import expressionanalysis.tools.GenerateTrendPlot;
import expressionanalysis.tools.HumanMouseSpearmanRankCorrel;
import expressionanalysis.tools.KeepColumnsFromMatrix;
import expressionanalysis.tools.ListOfFiles2Matrix;
import expressionanalysis.tools.MatrixLog2ZscoreNormalization;
import expressionanalysis.tools.MatrixZscoreNormalization;
import expressionanalysis.tools.MatrixZscoreNormalizationWithOriginalValues;
import expressionanalysis.tools.MergeSamples;
import expressionanalysis.tools.MultiplyMatrixValuesWithFactor;
import expressionanalysis.tools.OrderGeneMatrixBasedOnTTestDist;
import expressionanalysis.tools.QuantileNormalization;
import expressionanalysis.tools.RemoveColumnsFromMatrix;
import expressionanalysis.tools.RemoveDuplicatedSampleName;
import expressionanalysis.tools.RemoveRowsWithNAs;
import expressionanalysis.tools.RemoveZeroCountGenes;
import expressionanalysis.tools.ReplaceNAwithZero;
import expressionanalysis.tools.ReplaceNegWithZero;
import expressionanalysis.tools.SummarizeMATSGenes;
import expressionanalysis.tools.TransposeMatrix;
import expressionanalysis.tools.batchcorrection.TwoGroupMeanCentering;
import expressionanalysis.tools.batchcorrection.TwoGroupMeanCenteringFlex;
import expressionanalysis.tools.boxplot.GenerateExpressionBoxPlot;
import expressionanalysis.tools.genename.GeneSymbol2UCSCIDAppend;
import expressionanalysis.tools.gsea.ConvertGSEAList2AnnotationFile;
import expressionanalysis.tools.gsea.SummarizeGSEAResult;
import functional.pathway.enrichment.FilterORAResults;
import functional.pathway.enrichment.FilterORAResultsFlex;
import functional.pathway.enrichment.GenerateScriptForORA;
import functional.pathway.enrichment.GenerateScriptForORAFromInputFile;
import functional.pathway.enrichment.ORASummaryTable;
import functional.pathway.enrichment.ORASummaryTableHeatmap;
import functional.pathway.enrichment.OverRepresentationAnalysis;
import functional.pathway.enrichment.OverRepresentationAnalysisFDR;
import functional.pathway.enrichment.OverRepresentationAnalysisWithoutFilter;
import functional.pathway.enrichment.david.GenerateGODatabaseDAVID;
import functional.pathway.enrichment.david.StandardizeGeneName;
import functional.pathway.enrichr.CleanGMTEnrichR;
import functional.pathway.enrichr.ConvertEnrichR2GMTPathwayFolder;
import functional.pathway.visualization.webcytoscape.DisplayJsonFileNetwork;
import functional.pathway.visualization.webcytoscape.GenerateNodeMetaDataSize;
import general.sequence.analysis.GCScanner;
import genomics.exome.GenerateSNVTableFromMutationTable;
import genomics.exome.circos.FromSV2CircosInput;
import genomics.exome.circos.Indel2CircosInput;
import genomics.exome.circos.SNV2CircosInput;
import genomics.exome.circos.SV2CircosInput;
import genomics.exome.indel.FilterDuplicatedHits;
import genomics.exome.sjsnvindelpipeline.GenerateGRCh37liteSNVIndelScript;
import genomics.exome.sjsnvindelpipeline.GenerateHg19SNVIndelScript;
import genomics.exome.sjsnvindelpipeline.GenerateMm9SNVIndelScript;
import genomics.exome.snppopulationdistribution.SNPrsPopulation;
import genomics.exome.special.mousegermlineanalysis.SummarizeMouseIndelAnalysis;
import genomics.exome.summarize.EXCAPSummary;
import genomics.exome.summarize.EXONCAPBasicStatsPairedFile;
import genomics.exome.summarize.EXONCAPHumanBasicStats;
import genomics.exome.unpairedpipeline.GenerateSNVUnpairedScriptSimple;
import genomics.rnaseq.coverage.bw.NormalizeBedGraph;
import genomics.rnaseq.expression.transcriptionfactornetwork.ConvertAracneOutput2GMT;
import genomics.rnaseq.expression.transcriptionfactornetwork.GenerateAracneInputFile;
import genomics.rnaseq.fusion.cicero.ChromosomeBarPlot;
import genomics.rnaseq.fusion.cicero.ExtractFusionGenes;
import graph.figures.BarPlotGenerator;
import graph.figures.BoxPlotGeneratorThreeGroup;
import graph.figures.BoxPlotGeneratorTwoColumn;
import graph.figures.BoxPlotGeneratorTwoGroup;
import graph.figures.BoxplotExpressionForEachSample;
import graph.figures.ConvertssGSEAMatrix2BoxplotMatrix;
import graph.figures.MultipleBarPlotGenerator;
import graph.figures.SampleExprHistogram;
import graph.interactive.javascript.GenerateFoldchangeGeneLengthPlot;
import graph.interactive.javascript.GenerateScatterPlotJavaScript;
import graph.interactive.javascript.GenerateScatterPlotJavaScriptUserInput;
import graph.interactive.javascript.barplot.GenerateBatchBarPlotHtmls;
import graph.interactive.javascript.barplot.GenerateHorizontalBarPlotJavaScript;
import graph.interactive.javascript.barplot.GenerateStackedBarPlotJavaScript;
import graph.interactive.javascript.barplot.GenerateVerticalBarPlotJavaScript;
import graph.interactive.javascript.heatmap.GenerateHeatmapJavaScript;
import graph.interactive.javascript.heatmap.GenerateHeatmapZscoreSSGSEAJavaScript;
import graph.interactive.javascript.heatmap.GenerateHeatmapZscoreWithOriginalValuesJavaScript;
import graph.interactive.javascript.maplot.GenerateMAPlotJavaScript;
import graph.interactive.javascript.maplot.GenerateMAPlotJavaScriptUserInput;
import graph.interactive.javascript.scatterplot.AppendColorAsMetaInfo;
import graph.interactive.javascript.scatterplot.AppendExpressionColorAsMetaData;
import graph.interactive.javascript.scatterplot.GenerateScatterPlotJavaScriptInputHTMLMeta;
import graph.interactive.javascript.scatterplot.GenerateScatterPlotJavaScriptUserInputCustomColor;
import graph.interactive.javascript.scatterplot.GenerateScatterPlotJavaScriptUserInputCustomColorMeta;
import graph.interactive.javascript.scatterplot.GenerateScatterPlotJavaScriptUserInputCustomColorMetaComplex;
import graph.interactive.javascript.scatterplot.GenerateScatterPlotJavaScriptUserInputInitializeColor;
import graph.interactive.javascript.scatterplot.UpdateScatterPlotColorBasedOnExpression;
import graph.interactive.javascript.volcanoplot.GenerateVolcanoPlotJavaScript;
import graph.interactive.javascript.volcanoplot.GenerateVolcanoPlotJavaScriptUserInput;
import jump.pipeline.tools.ExtractUniqPeptides;
import jump.pipeline.tools.FilterPSMInformationPeptide;
import jump.pipeline.tools.FilterPSMInformationProteinName;
import jump.pipeline.tools.GeneratePhosphoPeptideMatrix;
import jump.pipeline.tools.GenerateProteomeGeneMatrix;
import jump.pipeline.tools.MergeRowsMaximizePSM;
import jump.pipeline.tools.ReplaceUniprotGeneSymbol2NCBIGeneSymbol;
import pathway.tools.PathwayKappaScore;
import pipeline.sequence.analysis.blasttool.GenerateBlastFile;
import pipeline.tools.jump.jumpn.JUMPnProcessCluster2GMT;
import protein.features.aminoacidresidue.CalculateResidueFrequencyFastaFile;
import protein.features.aminoacidresidue.CalculateResidueMotif;
import protein.features.aminoacidresidue.CalculateResidueMotifBootstrap;
import protein.features.aminoacidresidue.CalculateResidueMotifBootstrap3;
import protein.features.aminoacidresidue.CalculateResidueMotifBootstrap4;
import protein.features.aminoacidresidue.CalculateResidueMotifBootstrapDE;
import protein.features.aminoacidresidue.CountGeneWithResidueRegionPlot;
import protein.features.charge.CalculateChargeFastaFile;
import protein.features.charge.ConvertGene2Uniprot;
import protein.features.charge.GenerateChargeGraph;
import protein.features.charge.MatchFasta2Coordinate;
import protein.features.combineresults.CombineAAFreqProteinFeature;
import protein.features.hydrophobicity.CalculateHydrophobicityFastaFile;
import protein.features.lowcomplexitydomain.AppendUbiquitome;
import protein.features.lowcomplexitydomain.UniprotSEGPostProcessing;
import protein.features.motif.meme.GenerateUniqFastaFile;
import protein.features.plots.ProteinFeaturePlots;
import protein.features.sequenceconservation.AlignSEGSequence;
import protein.features.sequenceconservation.ConservationSurvey;
import protein.features.sequenceconservation.GenerateFastaSequenceForEachProtein;
import proteomics.SimulatedPeptideDigestion;
import proteomics.annotation.uniprot.GenerateIDConversionMasterTable;
import proteomics.apms.saint.CalculateGeneLengthSaintInputFile;
import proteomics.apms.saint.GenerateInteractionFileForSaint;
import proteomics.apms.saint.GeneratePreyGeneLength;
import proteomics.phospho.kinaseactivity.pipeline.AssignKnownKinaseSubstrateRelationship;
import proteomics.phospho.kinaseactivity.pipeline.AssignKnownKinaseSubstrateRelationshipFlex;
import proteomics.phospho.kinaseactivity.pipeline.CleanWhlProteome;
import proteomics.phospho.kinaseactivity.pipeline.FilterSitePhosphoWithPeptidePhospho;
import proteomics.phospho.kinaseactivity.pipeline.JUMPqPhoProteome2Matrix;
import proteomics.phospho.kinaseactivity.pipeline.JUMPqWhlProteome2Matrix;
import proteomics.phospho.kinaseactivity.pipeline.NormalizeMatrix2IKAP;
import proteomics.phospho.kinaseactivity.pipeline.NormalizeMatrix2IKAPFlex;
import proteomics.phospho.kinaseactivity.pipeline.NormalizePhosphoAgainstWhole;
import proteomics.phospho.kinaseactivity.pipeline.NormalizePhosphoAgainstWholeFlex;
import proteomics.phospho.kinaseactivity.pipeline.NormalizePhosphoAgainstWholeWithOffset;
import proteomics.phospho.kinaseactivity.pipeline.NormalizeWholeGenome;
import proteomics.phospho.kinaseactivity.pipeline.NormalizeWholeGenomeFlex;
import proteomics.phospho.kinaseactivity.pipeline.OptimizeProteomeNormalization;
import proteomics.phospho.kinaseactivity.pipeline.ReorderIkapColumn;
import proteomics.phospho.kinaseactivity.pipeline.SummarizeIKAPMatrix;
import proteomics.phospho.kinaseactivity.sem.GenerateSEMScript;
import proteomics.phospho.tools.annotation.AppendKinaseMotif2PeptideTable;
import proteomics.phospho.tools.generatenetwork.KinaseSubstrate2KinaseOnly;
import proteomics.phospho.tools.generatenetwork.KinaseSubstrateAll;
import proteomics.phospho.tools.gsea.ConvertKinaseGroupTxt2Gmt;
import proteomics.phospho.tools.heatmap.GrabPhosphositeExpressionAll;
import proteomics.phospho.tools.heatmap.GrabPhosphositeExpressionGeneCentric;
import proteomics.phospho.tools.kinase.report.DegradationPhosphositeRegForAll;
import proteomics.phospho.tools.kinase.substrate.predictions.AUCFilter;
import proteomics.phospho.tools.kinase.substrate.predictions.AppendFunctionalInformation2Matrix;
import proteomics.phospho.tools.kinase.substrate.predictions.AppendKinaseTargetInformation2Matrix;
import proteomics.phospho.tools.kinase.substrate.predictions.CalculateKinaseSubstrateStDev;
import proteomics.phospho.tools.kinase.substrate.predictions.CalculatePhosphositePlusKinaseEntrySummary;
import proteomics.phospho.tools.kinase.substrate.predictions.CombinePhosphositeCorrelationResult;
import proteomics.phospho.tools.kinase.substrate.predictions.GenerateMotifScoreTable;
import proteomics.phospho.tools.kinase.substrate.predictions.GenerateMotifScoreTableAll;
import proteomics.phospho.tools.kinase.substrate.predictions.KinaseSubstrateMergeROCResult;
import proteomics.phospho.tools.kinase.substrate.predictions.PhoFilterKinaseFunctionalRole;
import proteomics.phospho.tools.kinase.substrate.predictions.PhosphositeMetaScoreSensitivitySpecificity;
import proteomics.phospho.tools.kinase.substrate.predictions.WhlPhoSpearmanRankCorrelation;
import proteomics.phospho.tools.misc.FilterBackground2CoreProtein;
import proteomics.phospho.tools.misc.GrabFastaFile;
import proteomics.phospho.tools.motifs.degenerative.ExtendJUMPqSite;
import proteomics.phospho.tools.motifs.degenerative.GenerateFastaFileFromJUMPqPeptide;
import proteomics.phospho.tools.motifs.degenerative.GenerateFastaFileFromJUMPqSite;
import proteomics.phospho.tools.pssm.GenerateBackgroundFrequencyTable;
import proteomics.phospho.tools.pssm.GeneratePSSMUniprotDatabase;
import proteomics.phospho.tools.pssm.GenerateReferencePSSMTable;
import proteomics.phospho.tools.pssm.NormalizePWMWithBackground;
import proteomics.phospho.tools.pssm.distribution.AppendPSSMScore2Matrix;
import proteomics.phospho.tools.pssm.distribution.AppendPSSMScore2PhosphoSiteMatrix;
import proteomics.phospho.tools.pssm.distribution.AssignKnownKinaseSubstrateSupplementary;
import proteomics.phospho.tools.pssm.distribution.PSSMCreateSupplementaryTable;
import proteomics.phospho.tools.pssm.distribution.PSSMScoreDistribution;
import proteomics.phospho.tools.pssm.distribution.PSSMScoreDistributionKinaseMotif;
import proteomics.phospho.tools.pssm.distribution.RandomSelectionPSSM;
import proteomics.phospho.tools.rarefractioncurve.EstimatingTotalCoverage;
import references.gtf.manipulation.Filter3PrimeGTFExon;
import references.gtf.manipulation.xenograft.Mouse2GTF;
import references.gtf.statistics.GTFSummaryStatistics;
import rnaseq.bed.coverage.circos.GenerateCircosCoverageBed;
import rnaseq.mapping.tools.bw.Bam2BW;
import rnaseq.mapping.tools.bw.Bam2StrandedBW;
import rnaseq.mapping.tools.flagstat.SummarizeFlagStat;
import rnaseq.mapping.tools.star.Bam2FqMouseERCC;
import rnaseq.mapping.tools.star.CombineHTSEQResult;
import rnaseq.mapping.tools.star.CombineHTSEQResultRPMChunxuPipeline;
import rnaseq.mapping.tools.star.CombineHTSEQResultRaw;
import rnaseq.mapping.tools.star.CombineHTSEQResultRefGeneOnly;
import rnaseq.mapping.tools.star.CreateBamIndex;
import rnaseq.mapping.tools.star.CuffLinksScriptGenerator;
import rnaseq.mapping.tools.star.FastaAddRemoveChr;
import rnaseq.mapping.tools.star.Fastq2FileList;
import rnaseq.mapping.tools.star.GTFFileAddRemoveChr;
import rnaseq.mapping.tools.star.HumanMouseXenograftRawCount2RPM;
import rnaseq.mapping.tools.star.MergeBamFiles;
import rnaseq.mapping.tools.star.RPM2FPKMGenCode;
import rnaseq.mapping.tools.star.RPM2RPKMExon;
import rnaseq.mapping.tools.star.RPM2RPKMExonRelaxedGeneID;
import rnaseq.mapping.tools.star.RPM2RPKMTranscript;
import rnaseq.mapping.tools.star.RawCount2RPM;
import rnaseq.mapping.tools.star.RawCount2RPMSkipFirstTwoColumns;
import rnaseq.mapping.tools.star.STARMappingScriptGenerator;
import rnaseq.mapping.tools.star.STARMappingScriptGeneratorForTrimFastq;
import rnaseq.mapping.tools.star.SummarizeStarMapping;
import rnaseq.mapping.tools.star.TrimmomaticScriptGenerator;
import rnaseq.mapping.tools.star.UBam2FQ;
import rnaseq.mapping.tools.star.ver2_5_3a.STARMappingScriptGeneratorV253a;
import rnaseq.pcpa.AddChr;
import rnaseq.pcpa.CombinePCPAResults;
import rnaseq.pcpa.ExtractPolyAReadsUsePolyALibrarySingleCell;
import rnaseq.pcpa.GeneratePCPAHumanScriptComplete;
import rnaseq.pcpa.MatchFq2Bam;
import rnaseq.pcpa.PCPAAppendMetaDeta;
import rnaseq.splicing.intronretention.CombineSplicingDeficiencyName;
import rnaseq.splicing.intronretention.CombineSplicingDeficiencyNameMeta;
import rnaseq.splicing.intronretention.FilterReadsForSDScore;
import rnaseq.splicing.intronretention.OverlapAllMouseHuman;
import rnaseq.splicing.intronretention.OverlapMouseHumanGeneName;
import rnaseq.splicing.mats308.AddGeneName2rMATS401;
import rnaseq.splicing.mats308.FilterMATSResults;
import rnaseq.splicing.mats308.SummarizeMATSSummary;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilterDiffExpr;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilterDisplayGeneList;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilterExpr;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilterGeneMatrix;
import rnaseq.splicing.mats402.RMATS402CompareSplicingResults;
import rnaseq.splicing.mats402.RMATS402CompareSplicingResultsSDWithBlackList;
import rnaseq.splicing.mats402.SummarizeRMATS402CountGene;
import rnaseq.splicing.mats402.SummarizeRMATS402Result;
import rnaseq.splicing.mats402.SummarizeRMATS402ResultBlackList;
import rnaseq.splicing.mats402.SummarizeRMATS402SDResultWithBlackList;
import rnaseq.splicing.rnapeg.GeneratePseudoReverseReferenceForRNAPeg;
import rnaseq.splicing.rnapeg.GenerateReverseReference;
import rnaseq.splicing.rnapeg.RNApegDefineExonBasedoOnBW;
import rnaseq.splicing.rnapeg.RNApegPostProcessingExons;
import rnaseq.splicing.rnapeg.SummarizeNovelExonAltStartSiteMatrix;
import rnaseq.splicing.rnapeg.juncsalvager.JuncSalvagerPipeline;
import rnaseq.splicing.spladder.CustomFilterSpladderHardFilter;
import rnaseq.splicing.spladder.CustomFilterSpladderSingleType;
import rnaseq.splicing.spladder.SpladderScriptGenerator;
import rnaseq.splicing.spladder.SpladderSummarizeOutput;
import rnaseq.splicing.summary.AppendExpressionToMATSOutput;
import rnaseq.tools.ercc.GenerateERCCgtffile;
import rnaseq.tools.exonjunction.JunctionVsGeneJunc;
import rnaseq.tools.exonjunction.OverlapLIMMAAndExonJunctionCount;
import rnaseq.tools.genelengthanalysis.AppendGeneLength;
import rnaseq.tools.genelengthanalysis.TranscriptLengthSlidingWindow;
import rnaseq.tools.genelengthanalysis.TranscriptLengthSlidingWindowInhibitedGenes;
import rnaseq.tools.metadata.AppendMetadataTag2RNAseqMatrixSampleName;
import rnaseq.tools.pipeline.ExpandGeneListAfterLIMMA;
import rnaseq.tools.pipeline.GenerateLIMMAComparisonScript;
import rnaseq.tools.quantification.CalculateExonRPKM;
import rnaseq.tools.quantification.CalculateIntronRPKM;
import rnaseq.tools.singlecell.bootstrap.Filter0PSamples;
import rnaseq.tools.singlecell.bootstrap.GenerateTrueFalseMatrix;
import rnaseq.tools.singlecell.bootstrap.VariantMatrixBootstrap;
import rnaseq.tools.singlecell.celloforigin.CalculateMutantAllelFrequencyMatrix;
import rnaseq.tools.singlecell.celloforigin.CalculateMutantExpressionMatrix;
import rnaseq.tools.singlecell.celloforigin.CalculateReferenceAlleleExpressionMatrix;
import rnaseq.tools.singlecell.celloforigin.FisherExactTest2groupcomparison;
import rnaseq.tools.singlecell.celloforigin.GenerateMatrixForTwoGroups;
import rnaseq.tools.singlecell.celloforigin.GenerateNodeMetaBasedOnGroups;
import rnaseq.tools.singlecell.celloforigin.GenerateSIFfromMinimumSpanningTree;
import rnaseq.tools.singlecell.celloforigin.PostProcessingOfVariantMatrix;
import rnaseq.tools.singlecell.cnv.GenerateRNAseqCNVValues;
import rnaseq.tools.singlecell.correlation.SpearmanRankCorrelation;
import rnaseq.tools.singlecell.correlation.SpearmanRankCorrelationMatrix;
import rnaseq.tools.singlecell.correlation.SpearmanRankCorrelationMatrixForTwo;
import rnaseq.tools.singlecell.htseq.HTSEQMergeCountFiles;
import rnaseq.tools.singlecell.mapping.pipeline.CombineFastqFiles;
import rnaseq.tools.singlecell.mapping.pipeline.GenerateFqFileList;
import rnaseq.tools.singlecell.mapping.pipeline.GenerateFqFileListParallel;
import rnaseq.tools.singlecell.mapping.pipeline.RemoveNAGenes;
import rnaseq.tools.singlecell.mapping.pipeline.SingleCellRNAseqMapAndQuan;
import rnaseq.tools.singlecell.mapping.pipeline.SingleCellRNAseqMapAndQuanReg;
import rnaseq.tools.singlecell.mapping.pipeline.ValidateSTARMapping;
import rnaseq.tools.singlecell.qc.ExamineGeneCoverageFlexible;
import rnaseq.tools.singlecell.qc.ExamineGeneCoverages;
import rnaseq.tools.singlecell.qc.PlotGeneSetBoxPlot;
import rnaseq.tools.singlecell.qc.PlotGeneSetBoxPlotAcrossSamples;
import rnaseq.tools.singlecell.qc.ribosomedepletion.CombineSingleCellSampleIntoOne;
import rnaseq.tools.singlecell.qc.ribosomedepletion.SeparateGeneMatrixIntoTwo;
import rnaseq.tools.singlecell.stemnesscalculator.CalculateStemness;
import rnaseq.tools.singlecell.tenxgenomics.TenXGenomics2Matrix;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.CalculateMedianForEachCluster;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.CalculateMedianForEachClusterSimple;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.CellRangerRenameSampleName;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.ConvertMatrix2CellRangerExpressionGeneIDCleanOutput;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.ConvertMatrix2CellRangerExpressionOutput;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.ConvertMatrix2CellRangerExpressionOutputGene2Ensembl;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.ConvertMatrix2CellRangerExpressionOutputNoGTF;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.RunSeuratAnalysisFromCellRanger;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.SamHeader2CellType;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.SeuratCalculateClusterDistribution;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.SpecialClassForDougGreen;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.SuzanneBakerFilterBarcodeSamples;
import rnaseq.tools.singlecell.tenxgenomics.cellranger.UpdateBarcodeClusterWithAnnotation;
import rnaseq.tools.singlecell.tools.census.normalization.CensusNormalization;
import rnaseq.tools.singlecell.zeroanalysis.CompileDataForViolinPlot;
import rnaseq.tools.singlecell.zeroanalysis.GenerateZeroAnalysisBinningTable;
import rnaseq.tools.singlecell.zeroanalysis.GrabGeneLessThanValue;
import rnaseq.tools.singlecell.zeroanalysis.GrabGeneOverValue;
import rnaseq.tools.summary.CalculateIntersectingGenes;
import rnaseq.tools.summary.CombineEnrichmentPathwayPvalues;
import rnaseq.tools.summary.GenerateFPKMBinningTable;
import rnaseq.tools.summary.GenerateRNASEQCoverageStatistics;
import rnaseq.tools.summary.IntronExonCoverageBED;
import rnaseq.tools.summary.PlotBinningTable;
import sequencing.tools.bedmanupulation.BedGraphFilterChromosomeName;
import stjude.projects.hongbochi.AppendMTORC1Motif2PeptideTable;
import stjude.projects.hongbochi.AppendMTORC1Motif2Table;
import stjude.projects.hongbochi.AppendMetaInformation;
import stjude.projects.hongbochi.CalculateAUC;
import stjude.projects.hongbochi.CalculateROCforMTORC1Motif;
import stjude.projects.hongbochi.HongboAppendSensitivitySpecificity;
import stjude.projects.hongbochi.HongboAppendSensitivitySpecificityFlex;
import stjude.projects.hongbochi.HongboFilterPhosphositeLog2FC;
import stjude.projects.hongbochi.phosphoanalysis.AssignKnownKinaseSubstrateRelationshipHongbo;
import stjude.projects.hongbochi.phosphoanalysis.HongboAnnotateMotifInformation;
import stjude.projects.hongbochi.phosphoanalysis.HongboAnnotateMotifInformationYuxinFile;
import stjude.projects.hongbochi.phosphoanalysis.KinaseFamilyCluster;
import stjude.projects.hongbochi.phosphoanalysis.PhosphoMotifEnrichment;
import stjude.projects.jinghuizhang.GenerateMIXCR;
import stjude.projects.jinghuizhang.GroupComparisonBoxPlot;
import stjude.projects.jinghuizhang.JinghuiZhangPatientSummary;
import stjude.projects.jinghuizhang.SummarizeMIXCRresult;
import stjude.projects.jinghuizhang.TwoGroupComparisonBoxPlot;
import stjude.projects.jinghuizhang.pcgpaltsplice.JinghuiZhangCalculateGTExTotalReads;
import stjude.projects.jinghuizhang.pcgpaltsplice.JinghuiZhangCalculatePCGPExonCount;
import stjude.projects.jinghuizhang.pcgpaltsplice.JinghuiZhangCalculatePCGPExonDiseaseType;
import stjude.projects.jinghuizhang.pcgpaltsplice.JinghuiZhangCalculatePCGPExonFPKM;
import stjude.projects.jinghuizhang.pcgpaltsplice.JinghuiZhangCalculatePCGPFPKMTarget;
import stjude.projects.jinghuizhang.pcgpaltsplice.JinghuiZhangCalculatePercentileCutoff;
import stjude.projects.jinghuizhang.pcgpaltsplice.JinghuiZhangWCPCalculatePercentileCutoff;
import stjude.projects.jinghuizhang.pcgpaltsplice.JinghuiZhangWeightedCumulativePercentile;
import stjude.projects.jinghuizhang.pcgpaltsplice.gtex.JinghuiZhangGTExExonMedianQuan;
import stjude.projects.jinghuizhang.pcgpaltsplice.gtex.JinghuiZhangGTExGenerateCategoryBarplot;
import stjude.projects.jinghuizhang.pcgpaltsplice.plots.JinghuiZhangExonBoxplotMatrix;
import stjude.projects.jinghuizhang.pcgpaltsplice.plots.JinghuiZhangGenerateCategoryBarplot;
import stjude.projects.jinghuizhang.tcga.JinghuiZhangGenerateTCGAMatrix;
import stjude.projects.jinghuizhang.tcga.JinghuiZhangRenameTCGAMatrix;
import stjude.projects.jiyangyu.JiyangYuAppendOtherColumn;
import stjude.projects.jiyangyu.JiyangYuConvertGeneNames;
import stjude.projects.jpaultaylor.ChangeFastaIDRefmRNA;
import stjude.projects.jpaultaylor.ChangeFastaIDUniprot;
import stjude.projects.jpaultaylor.Disorder2BEDFile;
import stjude.projects.jpaultaylor.ExtractD2P2SequenceRaw;
import stjude.projects.jpaultaylor.ExtractD2P2Sequences;
import stjude.projects.jpaultaylor.FastaRefSeq2EnsemblNew;
import stjude.projects.jpaultaylor.FilterDuplicateTranscriptSeq;
import stjude.projects.jpaultaylor.JPaulTaylorConvertUniprot2UniprotGeneName;
import stjude.projects.jpaultaylor.JPaulTaylorEstimateCoverage;
import stjude.projects.jpaultaylor.JPaulTaylorEstimateCoverageID;
import stjude.projects.jpaultaylor.JPaulTaylorEstimateCoverageSpecial;
import stjude.projects.jpaultaylor.MatchUniprotGeneName2GeneLCDLength;
import stjude.projects.jpaultaylor.SplitFastaFile;
import stjude.projects.leventaki.FilterCNVkitcnrfiles;
import stjude.projects.leventaki.High20ToTHETA;
import stjude.projects.leventaki.LeventakiAddChrBW;
import stjude.projects.leventaki.LeventakiCalculateGeneCoordinate;
import stjude.projects.leventaki.LeventakiCombineCNSResult;
import stjude.projects.leventaki.LeventakiExtractProbeCoordinate;
import stjude.projects.leventaki.LeventakiGenerateVCFPlot;
import stjude.projects.leventaki.SummarizeLeventakiProject;
import stjude.projects.leventaki.SummarizeVDJclones;
import stjude.projects.mckinnon.GenerateMatrixForMutationalSignature;
import stjude.projects.mckinnon.McKinnonCalculateGCSkew;
import stjude.projects.mckinnon.McKinnonEnsurePerfectMatch;
import stjude.projects.mckinnon.McKinnonGCScanner;
import stjude.projects.mckinnon.McKinnonGCScatterPlot;
import stjude.projects.mckinnon.McKinnonGCScatterPlotTTS;
import stjude.projects.mckinnon.McKinnonGenerateBlatBEDFile;
import stjude.projects.mckinnon.McKinnonGenerateRandomBEDFile;
import stjude.projects.mckinnon.McKinnonIntronRetentionQuantification;
import stjude.projects.mckinnon.McKinnonRemoveFastaHits;
import stjude.projects.mckinnon.McKinnonSummarizeGCScanning;
import stjude.projects.metabolomics.PlotIsotopicBarPlots;
import stjude.projects.michaeldyer.armserms.AssignKnownKinaseSubstrateRelationshipARMSERMS;
import stjude.projects.michaeldyer.armserms.NormalizeMatrix2IKAPARMSERMS;
import stjude.projects.michaeldyer.armserms.NormalizePhosphoAgainstWholeARMSERMS;
import stjude.projects.michaeldyer.armserms.NormalizeWholeMatrixARMSERMS;
import stjude.projects.mondirakundu.phosphoanalysis.CalculatePercentConservation;
import stjude.projects.mondirakundu.phosphoanalysis.CalculatePercentConservationNameInput;
import stjude.projects.mondirakundu.phosphoanalysis.ExtractUCSCMultipleSeqAlign;
import stjude.projects.mondirakundu.phosphoanalysis.PSSMMotifFinder;
import stjude.projects.peng.AppendGeneNameBasedOnKnownCanonical;
import stjude.projects.peng.AppendMayoMetaData;
import stjude.projects.peng.CheckForMissingGenes;
import stjude.projects.peng.CompareModule0ToOthers;
import stjude.projects.peng.ConvertSam2BamFile;
import stjude.projects.peng.ConvertSam2BamFileWithReference;
import stjude.projects.peng.FilterGenesBasedOnMaximumReads;
import stjude.projects.peng.FilterMinimumOf5Reads;
import stjude.projects.peng.GenerateCoreHomologTableMGISummary;
import stjude.projects.peng.GenerateSolidBowtieMapping;
import stjude.projects.peng.IncreaseCanonicalGeneIDs;
import stjude.projects.peng.JunminPengAnnotateProteinFeature;
import stjude.projects.peng.JunminPengColoriPSDConnections;
import stjude.projects.peng.JunminPengCombineSplicingAndExpression;
import stjude.projects.peng.JunminPengRemoveModuleHighlightiPSDConnections;
import stjude.projects.peng.MergeBamFilesAfterBowtie;
import stjude.projects.peng.MergeBamFilesAfterSTAR;
import stjude.projects.peng.MergeIntronRetentionTable;
import stjude.projects.peng.NormalizeJunctionBEDFile;
import stjude.projects.peng.PengROSMAPAttachMetaInformation;
import stjude.projects.peng.SortBamFiles;
import stjude.projects.potter.PotterGrabTranscriptExonFasta;
import stjude.projects.potter.PotterIdentifyExonBeingSkippedThroughCufflinks;
import stjude.projects.rnapeg.SummarizeRNAPEG;
import stjude.projects.schwartz.SchwartzCheckGeneExpression;
import stjude.projects.schwartz.SchwartzCountTomatoCre;
import stjude.projects.schwartz.SchwartzExtractFastqSeq;
import stjude.projects.singlecellsequencing.CombineRawCountSamplesTogether;
import stjude.projects.singlecellsequencing.DivideByTotalMultiplyByX;
import stjude.projects.singlecellsequencing.DownSamplingBulkMatrixAsSingleCell;
import stjude.projects.singlecellsequencing.GenerateMappingInputFile;
import stjude.projects.suzannebaker.CheckGMTCoverage;
import stjude.projects.suzannebaker.CreatePythonGSEAInputFile;
import stjude.projects.suzannebaker.CreateSingleSampleGSEAInputFiles;
import stjude.projects.suzannebaker.GenerateFastqFromBAM;
import stjude.projects.suzannebaker.GenerateHeatmapFromGMTPipeline;
import stjude.projects.suzannebaker.GenerateRNAHGGSampleK27MStatus;
import stjude.projects.suzannebaker.SummarizeGSEAResultNESFDR;
import stjude.projects.suzannebaker.SummarizeSingleSampleGSEAResult;
import stjude.projects.suzannebaker.stemness_lineage_ac_ol.SuzanneBakerConvertSingleSampleGSEA2LineageScore;
import stjude.projects.taoshengchen.TaoshengChenVennDiagram;
import stjude.projects.xiangchen.BMIQNormalization;
import stjude.projects.xiangchen.CombineBMIQNormalizedFiles;
import stjude.projects.xiangchen.CombineBMIQNormalizedFilesRscript;
import stjude.projects.xiangchen.XiangChenExtractMetaData;
import stjude.projects.xiangchen.XiangChenGrabTopVariableGenes;
import stjude.projects.xiangchen.XiangChenGrabTopVariableGenesFilterSNPXY;
import stjude.proteinpaint.tracks.GenerateLowComplexityDomainInfo;
import stjude.proteinpaint.tracks.OpenReadingFrameFinder;
import stjude.tools.rnaseq.MergeGeneCountChunxuPipeline;
import stjude.tools.rnaseq.RNASEQConfig2MappingScriptGenerator;
import TextMiningSoftwareAnnotation.WebTextMining;

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
		if (CalculatePercentConservationNameInput.type().equals(type)) {
			result += "CalculatePercentConservationNameInput: " + CalculatePercentConservationNameInput.description() + "\n";
		}
		if (EstimatingTotalCoverage.type().equals(type)) {
			result += "EstimatingTotalCoverage: " + EstimatingTotalCoverage.description() + "\n";
		}
		if (GTFFileAddRemoveChr.type().equals(type)) {
			result += "GTFFileAddRemoveChr: " + GTFFileAddRemoveChr.description() + "\n";
		}
		if (FastaAddRemoveChr.type().equals(type)) {
			result += "FastaAddRemoveChr: " + FastaAddRemoveChr.description() + "\n";
		}
		if (OverlapLIMMAAndExonJunctionCount.type().equals(type)) {
			result += "OverlapLIMMAAndExonJunctionCount: " + OverlapLIMMAAndExonJunctionCount.description() + "\n";
		}
		if (JunctionVsGeneJunc.type().equals(type)) {
			result += "JunctionVsGeneJunc: " + JunctionVsGeneJunc.description() + "\n";
		}
		if (GenerateMatrixForMutationalSignature.type().equals(type)) {
			result += "GenerateMatrixForMutationalSignature: " + GenerateMatrixForMutationalSignature.description() + "\n";
		}
		if (JUMPqWhlProteome2Matrix.type().equals(type)) {
			result += "JUMPqWhlProteome2Matrix: " + JUMPqWhlProteome2Matrix.description() + "\n";
		}
		if (JUMPqPhoProteome2Matrix.type().equals(type)) {
			result += "JUMPqPhoProteome2Matrix: " + JUMPqPhoProteome2Matrix.description() + "\n";
		}
		if (WhlPhoSpearmanRankCorrelation.type().equals(type)) {
			result += "WhlPhoSpearmanRankCorrelation: " + WhlPhoSpearmanRankCorrelation.description() + "\n";
		}
		if (CombinePhosphositeCorrelationResult.type().equals(type)) {
			result += "CombinePhosphositeCorrelationResult: " + CombinePhosphositeCorrelationResult.description() + "\n";
		}
		if (AppendKinaseTargetInformation2Matrix.type().equals(type)) {
			result += "AppendKinaseTargetInformation2Matrix: " + AppendKinaseTargetInformation2Matrix.description() + "\n";
		}
		if (PhoFilterKinaseFunctionalRole.type().equals(type)) {
			result += "PhoFilterKinaseFunctionalRole: " + PhoFilterKinaseFunctionalRole.description() + "\n";
		}
		if (MergeGeneNameClean.type().equals(type)) {
			result += "MergeGeneNameClean: " + MergeGeneNameClean.description() + "\n";
		}
		if (DegradationPhosphositeRegForAll.type().equals(type)) {
			result += "DegradationPhosphositeRegForAll: " + DegradationPhosphositeRegForAll.description() + "\n";
		}
		if (AppendFunctionalInformation2Matrix.type().equals(type)) {
			result += "AppendFunctionalInformation2Matrix: " + AppendFunctionalInformation2Matrix.description() + "\n";
		}
		if (EXONCAPBasicStatsPairedFile.type().equals(type)) {
			result += "EXONCAPBasicStatsPairedFile: " + EXONCAPBasicStatsPairedFile.description() + "\n";
		}
		if (SummarizeResultsAfterMATSFilterGeneMatrix.type().equals(type)) {
			result += "SummarizeResultsAfterMATSFilterGeneMatrix; " + SummarizeResultsAfterMATSFilterGeneMatrix.description() + "\n";
		}
		if (ExtractRandomFastaSequence.type().equals(type)) {
			result += "ExtractRandomFastaSequence: " + ExtractRandomFastaSequence.description() + "\n";
		}
		if (GenerateGSEADataset.type().equals(type)) {
			result += "GenerateGSEADataset: " + GenerateGSEADataset.description() + "\n";
		}
		if (CalculateStemness.type().equals(type)) {
			result += "CalculateStemness: " + CalculateStemness.description() + "\n";
		}
		if (BedGraphFilterChromosomeName.type().equals(type)) {
			result += "BedGraphFilterChromosomeName: " + BedGraphFilterChromosomeName.description() + "\n";
		}
		if (UBam2FQ.type().equals(type)) {
			result += "UBam2FQ: " + UBam2FQ.description() + "\n";
		}
		if (Bam2FqMouseERCC.type().equals(type)) {
			result += "Bam2FqMouseERCC: " + Bam2FqMouseERCC.description() + "\n";
		}
		if (GenerateMappingInputFile.type().equals(type)) {
			result += "GenerateMappingInputFile: " + GenerateMappingInputFile.description() + "\n";
		}
		if (GenerateRNAseqCNVValues.type().equals(type)) {
			result += "GenerateRNAseqCNVValues: " + GenerateRNAseqCNVValues.description() + "\n";
		}
		if (GenerateERCCgtffile.type().equals(type)) {
			result += "GenerateERCCgtffile: " + GenerateERCCgtffile.description() + "\n";
		}
		if (KeepColumnsFromMatrix.type().equals(type)) {
			result += "KeepColumnsFromMatrix: " + KeepColumnsFromMatrix.description() + "\n";
		}
		if (CombineRawCountSamplesTogether.type().equals(type)) {
			result += "CombineRawCountSamplesTogether: " + CombineRawCountSamplesTogether.description() + "\n";
		}
		if (FastaRefSeq2Ensembl.type().equals(type)) {
			result += "FastaRefSeq2Ensembl: " + FastaRefSeq2Ensembl.description() + "\n";
		}
		if (DivideByTotalMultiplyByX.type().equals(type)) {
			result += "DivideByTotalMultiplyByX: " + DivideByTotalMultiplyByX.description() + "\n";
		}
		if (KeepProteinCodingGenes.type().equals(type)) {
			result += "KeepProteinCodingGenes: " + KeepProteinCodingGenes.description() + "\n";
		}
		if (Matrix2Exponent.type().equals(type)) {
			result += "Matrix2Exponent: " + Matrix2Exponent.description() + "\n";
		}
		if (Matrix2Addition.type().equals(type)) {
			result += "Matrix2Addition: " + Matrix2Addition.description() + "\n";
		}
		if (GenerateRNAHGGSampleK27MStatus.type().equals(type)) {
			result += "GenerateRNAHGGSampleK27MStatus: " + GenerateRNAHGGSampleK27MStatus.description() + "\n";
		}
		if (QuantileNormalization.type().equals(type)) {
			result += "QuantileNormalization: " + QuantileNormalization.description() + "\n";
		}
		if (CalculateKinaseSubstrateStDev.type().equals(type)) {
			result += "CalculateKinaseSubstrateStDev: " + CalculateKinaseSubstrateStDev.description() + "\n";
		}
		if (ExamineGeneCoverages.type().equals(type)) {
			result += "ExamineGeneCoverages: " + ExamineGeneCoverages.description() + "\n";
		}
		if (PlotGeneSetBoxPlot.type().equals(type)) {
			result += "PlotGeneSetBoxPlot: " + PlotGeneSetBoxPlot.description() + "\n";
		}
		if (PlotGeneSetBoxPlotAcrossSamples.type().equals(type)) {
			result += "PlotGeneSetBoxPlotAcrossSamples: " + PlotGeneSetBoxPlotAcrossSamples.description() + "\n";
		}
		if (ExamineGeneCoverageFlexible.type().equals(type)) {
			result += "ExamineGeneCoverageFlexible: " + ExamineGeneCoverageFlexible.description() + "\n";
		}
		if (CalculateMutantAllelFrequencyMatrix.type().equals(type)) {
			result += "CalculateMutantAllelFrequencyMatrix: " + CalculateMutantAllelFrequencyMatrix.description() + "\n";
		}
		if (kgXrefAppendOfficialGeneSymbol.type().equals(type)) {
			result += "kgXrefAppendOfficialGeneSymbol: " + kgXrefAppendOfficialGeneSymbol.description() + "\n";
		}
		if (AppendHuman2Mouse.type().equals(type)) {
			result += "AppendHuman2Mouse: " + AppendHuman2Mouse.description() + "\n";
		}
		if (AppendMouse2Human.type().equals(type)) {
			result += "AppendMouse2Human: " + AppendMouse2Human.description() + "\n";
		}
		if (MergeIntronRetentionTable.type().equals(type)) {
			result += "MergeIntronRetentionTable: " + MergeIntronRetentionTable.description() + "\n";
		}
		if (CalculateMutantExpressionMatrix.type().equals(type)) {
			result += "CalculateMutantExpressionMatrix: " + CalculateMutantExpressionMatrix.description() + "\n";
		}
		if (CalculateReferenceAlleleExpressionMatrix.type().equals(type)) {
			result += "CalculateReferenceAlleleExpressionMatrix: " + CalculateReferenceAlleleExpressionMatrix.description() + "\n";
		}
		if (AppendGeneLength.type().equals(type)) {
			result += "AppendGeneLength: " + AppendGeneLength.description() + "\n";
		}
		if (GenerateCoreHomologTableMGISummary.type().equals(type)) {
			result += "GenerateCoreHomologTableMGISummary: " + GenerateCoreHomologTableMGISummary.description() + "\n";
		}
		
		if (EnsureUniqGeneNamesHumanMouse.type().equals(type)) {
			result += "EnsureUniqGeneNamesHumanMouse: " + EnsureUniqGeneNamesHumanMouse.description() + "\n";
		}
		if (OverlapTwoFiles.type().equals(type)) {
			result += "OverlapTwoFiles: " + OverlapTwoFiles.description() + "\n";
		} 
		if (RemoveQuotations.type().equals(type)) {
			result += "RemoveQuotations: " + RemoveQuotations.description() + "\n";
		}
		if (CensusNormalization.type().equals(type)) {
			result += "CensusNormalization: " + CensusNormalization.description() + "\n";
		}
		if (ConvertGSEAList2AnnotationFile.type().equals(type)) {
			result += "ConvertGSEAList2AnnotationFile: " + ConvertGSEAList2AnnotationFile.description() + "\n";
		}
		if (GenerateScatterPlotJavaScript.type().equals(type)) {
			result += "GenerateScatterPlotJavaScript: " + GenerateScatterPlotJavaScript.description() + "\n";
		}
		if (GenerateHorizontalBarPlotJavaScript.type().equals(type)) {
			result += "GenerateHorizontalBarPlotJavaScript: " + GenerateHorizontalBarPlotJavaScript.description() + "\n";
		}
		if (GenerateVolcanoPlotJavaScript.type().equals(type)) {
			result += "GenerateVolcanoPlotJavaScript: " + GenerateVolcanoPlotJavaScript.description() + "\n";
		}
		if (GenerateMAPlotJavaScript.type().equals(type)) {
			result += "GenerateMAPlotJavaScript: " + GenerateMAPlotJavaScript.description() + "\n";
		}
		if (GenerateMAPlotJavaScriptUserInput.type().equals(type)) {
			result += "GenerateMAPlotJavaScriptUserInput: " + GenerateMAPlotJavaScriptUserInput.description() + "\n";
		}
		if (FilterORAResults.type().equals(type)) {
			result += "FilterORAResults: " + FilterORAResults.description() + "\n";
		}
		if (RPM2FPKMGenCode.type().equals(type)) {
			result += "RPM2FPKMGenCode: " + RPM2FPKMGenCode.description() + "\n";
		}
		if (CombineHTSEQResultRPMChunxuPipeline.type().equals(type)) {
			result += "CombineHTSEQResultRPMChunxuPipeline: " + CombineHTSEQResultRPMChunxuPipeline.description() + "\n";
		}
		if (CombineHTSEQResultRefGeneOnly.type().equals(type)) {
			result += "CombineHTSEQResultRefGeneOnly: " + CombineHTSEQResultRefGeneOnly.description() + "\n";
		}
		if (CheckForMissingGenes.type().equals(type)) {
			result += "CheckForMissingGenes: " + CheckForMissingGenes.description() + "\n";
		}
		if (EnsemblGeneID2GeneNameXenograft.type().equals(type)) {
			result += "EnsemblGeneID2GeneNameXenograft: " + EnsemblGeneID2GeneNameXenograft.description() + "\n";
		}
		if (IncreaseCanonicalGeneIDs.type().equals(type)) {
			result += "IncreaseCanonicalGeneIDs: " + IncreaseCanonicalGeneIDs.description() + "\n";
		}
		if (FilterMatrixFileFlex.type().equals(type)) {
			result += "FilterMatrixFileFlex: " + FilterMatrixFileFlex.description() + "\n";
		}
		if (AppendGeneNameBasedOnKnownCanonical.type().equals(type)) {
			result += "AppendGeneNameBasedOnKnownCanonical: " + AppendGeneNameBasedOnKnownCanonical.description() + "\n";
		}
		if (FilterGenesBasedOnMaximumReads.type().equals(type)) {
			result += "FilterGenesBasedOnMaximumReads: " + FilterGenesBasedOnMaximumReads.description() + "\n";
		}
		if (FilterMinimumOf5Reads.type().equals(type)) {
			result += "FilterMinimumOf5Reads: " + FilterMinimumOf5Reads.description() + "\n";
		}
		if (GenerateVerticalBarPlotJavaScript.type().equals(type)) {
			result += "GenerateVerticalBarPlotJavaScript: " + GenerateVerticalBarPlotJavaScript.description() + "\n";
		}
		if (GenerateVolcanoPlotJavaScriptUserInput.type().equals(type)) {
			result += "GenerateVolcanoPlotJavaScriptUserInput: " + GenerateVolcanoPlotJavaScriptUserInput.description() + "\n";
		}
		if (GenerateScriptForORA.type().equals(type)) {
			result += "GenerateScriptForORA: " + GenerateScriptForORA.description() + "\n";
		}
		if (GenerateScriptForORAFromInputFile.type().equals(type)) {
			result += "GenerateScriptForORAFromInputFile: " + GenerateScriptForORAFromInputFile.description() + "\n";
		}
		if (HumanMouseXenograftRawCount2RPM.type().equals(type)) {
			result += "HumanMouseXenograftRawCount2RPM: " + HumanMouseXenograftRawCount2RPM.description() + "\n";
		}
		if (FilterORAResultsFlex.type().equals(type)) {
			result += "FilterORAResultsFlex: " + FilterORAResultsFlex.description() + "\n";
		}
		if (AppendMetadataTag2RNAseqMatrixSampleName.type().equals(type)) {
			result += "AppendMetadataTag2RNAseqMatrixSampleName: " + AppendMetadataTag2RNAseqMatrixSampleName.description() + "\n";
		}
		if (PhosphositeMetaScoreSensitivitySpecificity.type().equals(type)) {
			result += "PhosphositeMetaScoreSensitivitySpecificity: " + PhosphositeMetaScoreSensitivitySpecificity.description() + "\n";
		}
		if (KinaseSubstrateMergeROCResult.type().equals(type)) {
			result += "KinaseSubstrateMergeROCResult: " + KinaseSubstrateMergeROCResult.description() + "\n";
		}
		if (GenerateMotifScoreTable.type().equals(type)) {
			result += "GenerateMotifScoreTable: " + GenerateMotifScoreTable.description() + "\n";
		}
		if (CalculatePhosphositePlusKinaseEntrySummary.type().equals(type)) {
			result += "CalculatePhosphositePlusKinaseEntrySummary: " + CalculatePhosphositePlusKinaseEntrySummary.description() + "\n";
		}
		if (GenerateFoldchangeGeneLengthPlot.type().equals(type)) {
			result += "GenerateFoldchangeGeneLengthPlot: " + GenerateFoldchangeGeneLengthPlot.description() + "\n";
		}
		if (MergeBamFilesAfterSTAR.type().equals(type)) {
			result += "MergeBamFilesAfterSTAR: " + MergeBamFilesAfterSTAR.description() + "\n";
		}
		if (Bam2BW.type().equals(type)) {
			result += "Bam2BW: " + Bam2BW.description() + "\n";
		}
		if (RawCount2RPMSkipFirstTwoColumns.type().equals(type)) {
			result += "RawCount2RPMSkipFirstTwoColumns: " + RawCount2RPMSkipFirstTwoColumns.description() + "\n";
		}
		if (RawCount2RPM.type().equals(type)) {
			result += "RawCount2RPM: " + RawCount2RPM.description() + "\n";
		}
		if (GMTHuman2Mouse.type().equals(type)) {
			result += "GMTHuman2Mouse: " + GMTHuman2Mouse.description() + "\n";
		}
		if (GMTMouse2Human.type().equals(type)) {
			result += "GMTMouse2Human: " + GMTMouse2Human.description() + "\n";
		}
		if (RemoveZeroCountGenes.type().equals(type)) {
			result += "RemoveZeroCountGenes: " + RemoveZeroCountGenes.description() + "\n";
		}
		if (GenerateSolidBowtieMapping.type().equals(type)) {
			result += "GenerateSolidBowtieMapping: " + GenerateSolidBowtieMapping.description() + "\n";
		}
		if (ConvertSam2BamFile.type().equals(type)) {
			result += "ConvertSam2BamFile: " + ConvertSam2BamFile.description() + "\n";
		}
		if (ConvertSam2BamFileWithReference.type().equals(type)) {
			result += "ConvertSam2BamFileWithReference: " + ConvertSam2BamFileWithReference.description() + "\n";
		}
		if (SortBamFiles.type().equals(type)) {
			result += "SortBamFiles: " + SortBamFiles.description() + "\n";
		}
		if (MergeBamFilesAfterBowtie.type().equals(type)) {
			result += "MergeBamFilesAfterBowtie: " + MergeBamFilesAfterBowtie.description() + "\n";
		}
		if (CreateBamIndex.type().equals(type)) {
			result += "CreateBamIndex: " + CreateBamIndex.description() + "\n";
		}
		if (CreateSingleSampleGSEAInputFiles.type().equals(type)) {
			result += "CreateSingleSampleGSEAInputFiles: " + CreateSingleSampleGSEAInputFiles.description() + "\n";
		}
		if (SummarizeSingleSampleGSEAResult.type().equals(type)) {
			result += "SummarizeSingleSampleGSEAResult: " + SummarizeSingleSampleGSEAResult.description() + "\n";
		}
		if (GenerateHeatmapJavaScript.type().equals(type)) {
			result += "GenerateHeatmapJavaScript: " + GenerateHeatmapJavaScript.description() + "\n";
		}
		if (MatrixZscoreNormalization.type().equals(type)) {
			result += "MatrixZscoreNormalization: " + MatrixZscoreNormalization.description() + "\n";
		}
		if (OrderGeneMatrixBasedOnTTestDist.type().equals(type)) {
			result += "OrderGeneMatrixBasedOnTTestDist: " + OrderGeneMatrixBasedOnTTestDist.description() + "\n";
		}
		if (GrabSampleNameWithoutKeyword.type().equals(type)) {
			result += "GrabSampleNameWithoutKeyword: " + GrabSampleNameWithoutKeyword.description() + "\n";
		}
		if (GrabSampleNameWithKeyword.type().equals(type)) {
			result += "GrabSampleNameWithKeyword: " + GrabSampleNameWithKeyword.description() + "\n";
		}
		if (TransposeMatrix.type().equals(type)) {
			result += "TransposeMatrix: " + TransposeMatrix.description() + "\n";
		}
		if (MatrixLog2ZscoreNormalization.type().equals(type)) {
			result += "MatrixLog2ZscoreNormalization: " + MatrixLog2ZscoreNormalization.description() + "\n";
		}
		if (TwoGroupMeanCentering.type().equals(type)) {
			result += "TwoGroupMeanCentering: " + TwoGroupMeanCentering.description() + "\n";
		}
		if (TwoGroupMeanCenteringFlex.type().equals(type)) {
			result += "TwoGroupMeanCenteringFlex: " + TwoGroupMeanCenteringFlex.description() + "\n";
		}
		if (MatrixZscoreNormalizationWithOriginalValues.type().equals(type)) {
			result += "MatrixZscoreNormalizationWithOriginalValues: " + MatrixZscoreNormalizationWithOriginalValues.description() + "\n";
		}
		if (GenerateHeatmapZscoreWithOriginalValuesJavaScript.type().equals(type)) {
			result += "GenerateHeatmapZscoreWithOriginalValuesJavaScript: " + GenerateHeatmapZscoreWithOriginalValuesJavaScript.description() + "\n";
		}
		if (GenerateHeatmapFromGMTPipeline.type().equals(type)) {
			result += "GenerateHeatmapFromGMTPipeline: " + GenerateHeatmapFromGMTPipeline.description() + "\n";
		}
		if (CreatePythonGSEAInputFile.type().equals(type)) {
			result += "CreatePythonGSEAInputFile: " + CreatePythonGSEAInputFile.description() + "\n";
		}
		if (SummarizeGSEAResultNESFDR.type().equals(type)) {
			result += "SummarizeGSEAResultNESFDR: " + SummarizeGSEAResultNESFDR.description() + "\n";
		}
		if (GenerateHeatmapZscoreSSGSEAJavaScript.type().equals(type)) {
			result += "GenerateHeatmapZscoreSSGSEAJavaScript: " + GenerateHeatmapZscoreSSGSEAJavaScript.description() + "\n";
		}
		if (GenerateBatchBarPlotHtmls.type().equals(type)) {
			result += "GenerateBatchBarPlotHtmls: " + GenerateBatchBarPlotHtmls.description() + "\n";
		}
		if (PlotIsotopicBarPlots.type().equals(type)) {
			result += "PlotIsotopicBarPlots: " + PlotIsotopicBarPlots.description() + "\n";
		}
		if (GeneratePreyGeneLength.type().equals(type)) {
			result += "GeneratePreyGeneLength: " + GeneratePreyGeneLength.description() + "\n";
		}
		if (GeneSymbol2UCSCIDAppend.type().equals(type)) {
			result += "GeneSymbol2UCSCIDAppend: " + GeneSymbol2UCSCIDAppend.description() + "\n";
		}
		if (CalculateGeneLengthSaintInputFile.type().equals(type)) {
			result += "CalculateGeneLengthSaintInputFile: " + CalculateGeneLengthSaintInputFile.description() + "\n";
		}
		if (GenerateInteractionFileForSaint.type().equals(type)) {
			result += "GenerateInteractionFileForSaint: " + GenerateInteractionFileForSaint.description() + "\n";
		}
		if (CalculateROCforMTORC1Motif.type().equals(type)) {
			result += "CalculateROCforMTORC1Motif: " + CalculateROCforMTORC1Motif.description() + "\n";
		}
		if (NormalizePhosphoAgainstWholeWithOffset.type().equals(type)) {
			result += "NormalizePhosphoAgainstWholeWithOffset: " + NormalizePhosphoAgainstWholeWithOffset.description() + "\n";
		}
		if (OptimizeProteomeNormalization.type().equals(type)) {
			result += "OptimizeProteomeNormalization: " + OptimizeProteomeNormalization.description() + "\n";
		}
		if (AUCFilter.type().equals(type)) {
			result += "AUCFilter: " + AUCFilter.description() + "\n";
		}
		if (CalculateAUC.type().equals(type)) {
			result += "CalculateAUC: " + CalculateAUC.description() + "\n";
		}
		if (GenerateMotifScoreTableAll.type().equals(type)) {
			result += "GenerateMotifScoreTableAll: " + GenerateMotifScoreTableAll.description() + "\n";
		}
		if (AppendMTORC1Motif2Table.type().equals(type)) {
			result += "AppendMTORC1Motif2Table: " + AppendMTORC1Motif2Table.description() + "\n";
		}
		if (HongboAppendSensitivitySpecificity.type().equals(type)) {
			result += "HongboAppendSensitivitySpecificity: " + HongboAppendSensitivitySpecificity.description() + "\n";
		}
		if (MicroarrayEnsembl2GeneName.type().equals(type)) {
			result += "MicroarrayEnsembl2GeneName: " + MicroarrayEnsembl2GeneName.description() + "\n";
		}
		if (MergeGeneCountChunxuPipeline.type().equals(type)) {
			result += "MergeGeneCountChunxuPipeline: " + MergeGeneCountChunxuPipeline.description() + "\n";
		}
		if (ExpandGeneListAfterLIMMA.type().equals(type)) {
			result += "ExpandGeneListAfterLIMMA: " + ExpandGeneListAfterLIMMA.description() + "\n";
		}
		if (NormalizeBedGraph.type().equals(type)) {
			result += "NormalizeBedGraph: " + NormalizeBedGraph.description() + "\n";
		}
		if (AppendExpressionToMATSOutput.type().equals(type)) {
			result += "AppendExpressionToMATSOutput: " + AppendExpressionToMATSOutput.description() + "\n";
		}
		if (CalculateDistanceBetweenModules.type().equals(type)) {
			result += "CalculateDistanceBetweenModules: " + CalculateDistanceBetweenModules.description() + "\n";
		}
		if (SummarizeVDJclones.type().equals(type)) {
			result += "SummarizeVDJclones: " + SummarizeVDJclones.description() + "\n";
		}
		if (JiyangYuConvertGeneNames.type().equals(type)) {
			result += "JiyangYuConvertGeneNames: " + JiyangYuConvertGeneNames.description() + "\n";
		}
		if (JiyangYuAppendOtherColumn.type().equals(type)) {
			result += "JiyangYuAppendOtherColumn: " + JiyangYuAppendOtherColumn.description() + "\n";
		}
		if (GenerateAracneInputFile.type().equals(type)) {
			result += "GenerateAracneInputFile: " + GenerateAracneInputFile.description() + "\n";
		}
		if (ConvertAracneOutput2GMT.type().equals(type)) {
			result += "ConvertAracneOutput2GMT: " + ConvertAracneOutput2GMT.description() + "\n";
		}
		if (HongboAppendSensitivitySpecificityFlex.type().equals(type)) {
			result += "HongboAppendSensitivitySpecificityFlex: " + HongboAppendSensitivitySpecificityFlex.description() + "\n";
		}
		if (SplitFastaFile.type().equals(type)) {
			result += "SplitFastaFile: " + SplitFastaFile.description() + "\n";
		}
		if (ChangeFastaIDRefmRNA.type().equals(type)) {
			result += "ChangeFastaIDRefmRNA: " + ChangeFastaIDRefmRNA.description() + "\n";
		}
		if (FastaRefSeq2EnsemblNew.type().equals(type)) {
			result += "FastaRefSeq2EnsemblNew: " + FastaRefSeq2EnsemblNew.description() + "\n";
		}
		if (FilterDuplicate.type().equals(type)) {
			result += "FilterDuplicate: " + FilterDuplicate.description() + "\n";
		}
		if (FilterDuplicateTranscriptSeq.type().equals(type)) {
			result += "FilterDuplicateTranscriptSeq: " + FilterDuplicateTranscriptSeq.description() + "\n";
		}
		if (ExtractD2P2Sequences.type().equals(type)) {
			result += "ExtractD2P2Sequences: " + ExtractD2P2Sequences.description() + "\n";
		}
		if (ChangeFastaIDUniprot.type().equals(type)) {
			result += "ChangeFastaIDUniprot: " + ChangeFastaIDUniprot.description() + "\n";
		}
		if (Disorder2BEDFile.type().equals(type)) {
			result += "Disorder2BEDFile: " + Disorder2BEDFile.description() + "\n";
		}
		if (ExtractD2P2SequenceRaw.type().equals(type)) {
			result += "ExtractD2P2SequenceRaw: " + ExtractD2P2SequenceRaw.description() + "\n";
		}
		if (HTSEQMergeCountFiles.type().equals(type)) {
			result += "HTSEQMergeCountFiles: " + HTSEQMergeCountFiles.description() + "\n";
		}
		if (MicroArrayIDConversionAnnotation.type().equals(type)) {
			result += "MicroArrayIDConversionAnnotation: " + MicroArrayIDConversionAnnotation.description() + "\n";
		}
		if (AppendMetaInformation.type().equals(type)) {
			result += "AppendMetaInformation: " + AppendMetaInformation.description() + "\n";
		}
		if (CheckGMTCoverage.type().equals(type)) {
			result += "CheckGMTCoverage: " + CheckGMTCoverage.description() + "\n";
		}
		if (SummarizeIKAPMatrix.type().equals(type)) {
			result += "SummarizeIKAPMatrix: " + SummarizeIKAPMatrix.description() + "\n";
		}
		if (DownSamplingBulkMatrixAsSingleCell.type().equals(type)) {
			result += "DownSamplingBulkMatrixAsSingleCell: " + DownSamplingBulkMatrixAsSingleCell.description() + "\n";
		}
		if (RemoveRedundantEdges.type().equals(type)) {
			result += "RemoveRedundantEdges: " + RemoveRedundantEdges.description() + "\n";
		}
		if (BMIQNormalization.type().equals(type)) {
			result += "BMIQNormalization: " + BMIQNormalization.description() + "\n";
		}
		if (BMIQNormalizationSingleSample.type().equals(type)) {
			result += "BMIQNormalizationSingleSample: " + BMIQNormalizationSingleSample.description() + "\n";
		}
		if (CombineBMIQNormalizedFiles.type().equals(type)) {
			result += "CombineBMIQNormalizedFiles: " + CombineBMIQNormalizedFiles.description() + "\n";
		} //
		if (CombineBMIQNormalizedFilesRscript.type().equals(type)) {
			result += "CombineBMIQNormalizedFilesRscript: " + CombineBMIQNormalizedFilesRscript.description() + "\n";
		} 
		if (BoxplotExpressionForEachSample.type().equals(type)) {
			result += "BoxplotExpressionForEachSample: " + BoxplotExpressionForEachSample.description() + "\n";
		}
		if (MatchUniprotGeneName2GeneLCDLength.type().equals(type)) {
			result += "MatchUniprotGeneName2GeneLCDLength: " + MatchUniprotGeneName2GeneLCDLength.description() + "\n";
		}
		if (UniprotSEGPostProcessing.type().equals(type)) {
			result += "UniprotSEGPostProcessing: " + UniprotSEGPostProcessing.description() + "\n";
		}
		if (JPaulTaylorConvertUniprot2UniprotGeneName.type().equals(type)) {
			result += "JPaulTaylorConvertUniprot2UniprotGeneName: " + JPaulTaylorConvertUniprot2UniprotGeneName.description() + "\n";
		}
		if (AppendUbiquitome.type().equals(type)) {
			result += "AppendUbiquitome: " + AppendUbiquitome.description() + "\n";
		}
		if (Methylation850KAppendGeneInfo.type().equals(type)) {
			result += "Methylation850KAppendGeneInfo: " + Methylation850KAppendGeneInfo.description() + "\n";
		}
		if (CombineBMIQFiles.type().equals(type)) {
			result += "CombineBMIQFiles: " + CombineBMIQFiles.description() + "\n";
		}
		if (EPIC850KWilcoxonTestMethylation.type().equals(type)) {
			result += "EPIC850KWilcoxonTestMethylation: " + EPIC850KWilcoxonTestMethylation.description() + "\n";
		}
		if (Methylation850KWilcoxonTestAppendGeneInfo.type().equals(type)) {
			result += "Methylation850KWilcoxonTestAppendGeneInfo: " + Methylation850KWilcoxonTestAppendGeneInfo.description() + "\n";
		}
		if (EPIC850KMostMADVariableProbe.type().equals(type)) {
			result += "EPIC850KMostMADVariableProbe: " + EPIC850KMostMADVariableProbe.description() + "\n";
		}
		if (EPIC850KAveragedBEDFile.type().equals(type)) {
			result += "EPIC850KAveragedBEDFile: " + EPIC850KAveragedBEDFile.description() + "\n";
		} 
		if (BedAddRemoveChr.type().equals(type)) {
			result += "BedAddRemoveChr: " + BedAddRemoveChr.description() + "\n";
		}
		if (AppendMayoMetaData.type().equals(type)) {
			result += "AppendMayoMetaData: " + AppendMayoMetaData.description() + "\n";
		}
		if (SplitFastqForwardReverse.type().equals(type)) {
			result += "SplitFastqForwardReverse: " + SplitFastqForwardReverse.description() + "\n";
		}
		if (SummarizeFlagStats.type().equals(type)) {
			result += "SummarizeFlagStats: " + SummarizeFlagStats.description() + "\n";
		}
		if (PengROSMAPAttachMetaInformation.type().equals(type)) {
			result += "PengROSMAPAttachMetaInformation: " + PengROSMAPAttachMetaInformation.description() + "\n";
		}
		if (GenerateFastqFromBAM.type().equals(type)) {
			result += "GenerateFastqFromBAM: " + GenerateFastqFromBAM.description() + "\n";
		}
		if (AddGeneName2rMATS401.type().equals(type)) {
			result += "AddGeneName2rMATS401: " + AddGeneName2rMATS401.description() + "\n";
		}
		if (FilterCNVkitcnrfiles.type().equals(type)) {
			result += "FilterCNVkitcnrfiles: " + FilterCNVkitcnrfiles.description() + "\n";
		}
		if (High20ToTHETA.type().equals(type)) {
			result += "High20ToTHETA: " + High20ToTHETA.description() + "\n";
		}
		if (XiangChenGrabTopVariableGenes.type().equals(type)) {
			result += "XiangChenGrabTopVariableGenes: " + XiangChenGrabTopVariableGenes.description() + "\n";
		}
		if (XiangChenGrabTopVariableGenesFilterSNPXY.type().equals(type)) {
			result += "XiangChenGrabTopVariableGenesFilterSNPXY: " + XiangChenGrabTopVariableGenesFilterSNPXY.description() + "\n";
		}
		if (Bam2Fastq.type().equals(type)) {
			result += "Bam2Fastq: " + Bam2Fastq.description() + "\n";
		}
		if (GrabUniqValuesFromColumn.type().equals(type)) {
			result += "GrabUniqValuesFromColumn: " + GrabUniqValuesFromColumn.description() + "\n";
		}
		if (GenerateBiogrid2SIF.type().equals(type)) {
			result += "GenerateBiogrid2SIF: " + GenerateBiogrid2SIF.description() + "\n";
		}
		if (GenerateBiogrid2SIFPhysical.type().equals(type)) {
			result += "GenerateBiogrid2SIFPhysical: " + GenerateBiogrid2SIFPhysical.description() + "\n";
		}
		if (GenerateBiogrid2SIFColocalization.type().equals(type)) {
			result += "GenerateBiogrid2SIFColocalization: " + GenerateBiogrid2SIFColocalization.description() + "\n";
		}
		if (FilterMatrixColumnValue.type().equals(type)) {
			result += "FilterMatrixColumnValue: " + FilterMatrixColumnValue.description() + "\n";
		}
		if (PotterIdentifyExonBeingSkippedThroughCufflinks.type().equals(type)) {
			result += "PotterIdentifyExonBeingSkippedThroughCufflinks: " + PotterIdentifyExonBeingSkippedThroughCufflinks.description() + "\n";
		}
		if (PotterGrabTranscriptExonFasta.type().equals(type)) {
			result += "PotterGrabTranscriptExonFasta: " + PotterGrabTranscriptExonFasta.description() + "\n";
		}
		if (GenerateMIXCR.type().equals(type)) {
			result += "GenerateMIXCR: " + GenerateMIXCR.description() + "\n";
		}
		if (SummarizeMIXCRresult.type().equals(type)) {
			result += "SummarizeMIXCRresult: " + SummarizeMIXCRresult.description() + "\n";
		}
		if (SchwartzExtractFastqSeq.type().equals(type)) {
			result += "SchwartzExtractFastqSeq: " + SchwartzExtractFastqSeq.description() + "\n";
		}
		if (SchwartzCountTomatoCre.type().equals(type)) {
			result += "SchwartzCountTomatoCre: " + SchwartzCountTomatoCre.description() + "\n";
		}
		if (SchwartzCheckGeneExpression.type().equals(type)) {
			result += "SchwartzCheckGeneExpression: " + SchwartzCheckGeneExpression.description() + "\n";
		}
		if (TaoshengChenVennDiagram.type().equals(type)) {
			result += "TaoshengChenVennDiagram: " + TaoshengChenVennDiagram.description() + "\n";
		}
		if (SummarizeRNAPEG.type().equals(type)) {			
			result += "SummarizeRNAPEG: " + SummarizeRNAPEG.description() + "\n";
		}
		if (CalculateIntronRPKM.type().equals(type)) {
			result += "CalculateIntronRPKM: " + CalculateIntronRPKM.description() + "\n";
		}
		if (CombineTwoMatrixWithMismatch.type().equals(type)) {
			result += "CombineTwoMatrixWithMismatch: " + CombineTwoMatrixWithMismatch.description() + "\n";
		}
		if (CalculateExonRPKM.type().equals(type)) {
			result += "CalculateExonRPKM: " + CalculateExonRPKM.description() + "\n";
		}
		if (ConvertEnrichR2GMTPathwayFolder.type().equals(type)) {
			result += "ConvertEnrichRGMT2PathwayFolder: " + ConvertEnrichR2GMTPathwayFolder.description() + "\n";
		}
		if (JunminPengCombineSplicingAndExpression.type().equals(type)) {
			result += "JunminPengCombineSplicingAndExpression: " + JunminPengCombineSplicingAndExpression.description() + "\n";
		}
		if (CompareModule0ToOthers.type().equals(type)) {
			result += "CompareModule0ToOthers: " + CompareModule0ToOthers.description() + "\n";
		}
		if (JunminPengRemoveModuleHighlightiPSDConnections.type().equals(type)) {
			result += "JunminPengRemoveModuleHighlightiPSDConnections: " + JunminPengRemoveModuleHighlightiPSDConnections.description() + "\n";
		}
		if (JunminPengColoriPSDConnections.type().equals(type)) {
			result += "JunminPengColoriPSDConnections: " + JunminPengColoriPSDConnections.description() + "\n";
		}
		if (CleanEnsemblGeneID2GeneName.type().equals(type)) {
			result += "CleanEnsemblGeneID2GeneName: " + CleanEnsemblGeneID2GeneName.description() + "\n";
		}
		if (McKinnonGCScanner.type().equals(type)) {
			result += "McKinnonGCScanner: " + McKinnonGCScanner.description() + "\n";
		}
		if (McKinnonSummarizeGCScanning.type().equals(type)) {
			result += "McKinnonSummarizeGCScanning: " + McKinnonSummarizeGCScanning.description() + "\n";
		}
		if (McKinnonGCScatterPlot.type().equals(type)) {
			result += "McKinnonGCScatterPlot: " + McKinnonGCScatterPlot.description() + "\n";
		}
		if (XiangChenExtractMetaData.type().equals(type)) {
			result += "XiangChenExtractMetaData: " + XiangChenExtractMetaData.description() + "\n";
		}
		if (GenerateMultipleCirclesFlex.type().equals(type)) {
			result += "GenerateMultipleCirclesFlex: " + GenerateMultipleCirclesFlex.description() + "\n";
		}
		if (LeventakiExtractProbeCoordinate.type().equals(type)) {
			result += "LeventakiExtractProbeCoordinate: " + LeventakiExtractProbeCoordinate.description() + "\n";
		}
		if (McKinnonGCScatterPlotTTS.type().equals(type)) {
			result += "McKinnonGCScatterPlotTTS: " + McKinnonGCScatterPlotTTS.description() + "\n";
		}
		if (McKinnonCalculateGCSkew.type().equals(type)) {
			result += "McKinnonCalculateGCSkew: " + McKinnonCalculateGCSkew.description() + "\n";
		}
		if (McKinnonGenerateRandomBEDFile.type().equals(type)) {
			result += "McKinnonGenerateRandomBEDFile: " + McKinnonGenerateRandomBEDFile.description() + "\n";
		}
		if (MicroArrayIDConversionFlex.type().equals(type)) {
			result += "MicroArrayIDConversionFlex: " + MicroArrayIDConversionFlex.description() + "\n";
		}
		if (HumanMouseSpearmanRankCorrel.type().equals(type)) {
			result += "HumanMouseSpearmanRankCorrel: " + HumanMouseSpearmanRankCorrel.description() + "\n";
		}
		if (GenerateSpearmanRankMatrix.type().equals(type)) {
			result += "GenerateSpearmanRankMatrix: " + GenerateSpearmanRankMatrix.description() + "\n";
		}
		if (CleanGMTEnrichR.type().equals(type)) {
			result += "CleanGMTEnrichR: " + CleanGMTEnrichR.description() + "\n";
		}
		if (CalculateGraphStatistics.type().equals(type)) {
			result += "CalculateGraphStatistics: " + CalculateGraphStatistics.description() + "\n";
		}
		if (LeventakiCombineCNSResult.type().equals(type)) {
			result += "LeventakiCombineCNSResult: " + LeventakiCombineCNSResult.description() + "\n";
		}
		if (LeventakiGenerateVCFPlot.type().equals(type)) {
			result += "LeventakiGenerateVCFPlot: " + LeventakiGenerateVCFPlot.description() + "\n";
		}
		if (Epic850KHyperHypoMethylationFilter.type().equals(type)) {
			result += "Epic850KHyperHypoMethylationFilter: " + Epic850KHyperHypoMethylationFilter.description() + "\n";
		}
		if (SummarizeGSEAResult.type().equals(type)) {
			result += "SummarizeGSEAResult: " + SummarizeGSEAResult.description() + "\n";
		}
		if (SummarizeResultsAfterMATSFilterExpr.type().equals(type)) {
			result += "SummarizeResultsAfterMATSFilterExpr: " + SummarizeResultsAfterMATSFilterExpr.description() + "\n";
		}
		if (SummarizeResultsAfterMATSFilterDiffExpr.type().equals(type)) {
			result += "SummarizeResultsAfterMATSFilterDiffExpr: " + SummarizeResultsAfterMATSFilterDiffExpr.description() + "\n";
		}
		if (EnsemblGeneIDAppendAnnotation.type().equals(type)) {
			result += "EnsemblGeneIDAppendAnnotation: " + EnsemblGeneIDAppendAnnotation.description() + "\n";
		}
		if (RemoveRowsWithNAs.type().equals(type)) {
			result += "RemoveRowsWithNAs: " + RemoveRowsWithNAs.description() + "\n";
		}
		if (GenerateScatterPlotJavaScriptUserInput.type().equals(type)) {
			result += "GenerateScatterPlotJavaScriptUserInput: " + GenerateScatterPlotJavaScriptUserInput.description() + "\n";
		}
		if (SummarizeFlagStat.type().equals(type)) {
			result += "SummarizeFlagStat: " + SummarizeFlagStat.description() + "\n";
		}
		if (FilterBasedOnAnnotation.type().equals(type)) {
			result += "FilterBasedOnAnnotation: " + FilterBasedOnAnnotation.description() + "\n";
		}
		if (ReorderIkapColumn.type().equals(type)) {
			result += "ReorderIkapColumn: " + ReorderIkapColumn.description() + "\n";
		}
		if (AppendMADValue.type().equals(type)) {
			result += "AppendMADValue: " + AppendMADValue.description() + "\n";
		}
		if (SummarizeMATSGenes.type().equals(type)) {
			result += "SummarizeMATSGenes: " + SummarizeMATSGenes.description() + "\n";
		}
		if (FilterMATSResults.type().equals(type)) {
			result += "FilterMATSResults: " + FilterMATSResults.description() + "\n";
		}
		if (SIF2Geneset.type().equals(type)) {
			result += "SIF2Geneset: " + SIF2Geneset.description() + "\n";
		}
		if (CompassGenerateSifFile.type().equals(type)) {
			result += "CompassGenerateSifFile: " + CompassGenerateSifFile.description() + "\n";
		}
		if (NetworkNodeReplaceColor.type().equals(type)) {
			result += "NetworkNodeReplaceColor: " + NetworkNodeReplaceColor.description() + "\n";
		}
		if (FilterSitePhosphoWithPeptidePhospho.type().equals(type)) {
			result += "FilterSitePhosphoWithPeptidePhospho: " + FilterSitePhosphoWithPeptidePhospho.description() + "\n";
		}
		if (SummarizeResultsAfterMATSFilterDisplayGeneList.type().equals(type)) {
			result += "SummarizeResultsAfterMATSFilterDisplayGeneList: " + SummarizeResultsAfterMATSFilterDisplayGeneList.description() + "\n";
		}
		if (SummarizeMATSSummary.type().equals(type)) {
			result += "SummarizeMATSSummary: " + SummarizeMATSSummary.description() + "\n";
		}
		if (FilterReadsForSDScore.type().equals(type)) {
			result += "FilterReadsForSDScore: " + FilterReadsForSDScore.description() + "\n";
		}
		if (CombineAAFreqProteinFeature.type().equals(type)) {
			result += "CombineAAFreqProteinFeature: " + CombineAAFreqProteinFeature.description() + "\n";
		}
		if (JunminPengAnnotateProteinFeature.type().equals(type)) {
			result += "JunminPengAnnotateProteinFeature: " + JunminPengAnnotateProteinFeature.description() + "\n";
		}
		if (ProteinFeaturePlots.type().equals(type)) {
			result += "ProteinFeaturePlots: " + ProteinFeaturePlots.description() + "\n";
		}
		if (GTFSummaryStatistics.type().equals(type)) {
			result += "GTFSummaryStatistics: " + GTFSummaryStatistics.description() + "\n";
		}
		if (JinghuiZhangPatientSummary.type().equals(type)) {
			result += "JinghuiZhangPatientSummary: " + JinghuiZhangPatientSummary.description() + "\n";
		}
		if (ReplaceUniprotGeneSymbol2NCBIGeneSymbol.type().equals(type)) {
			result += "ReplaceUniprotGeneSymbol2NCBIGeneSymbol: " + ReplaceUniprotGeneSymbol2NCBIGeneSymbol.description() + "\n";
		}
		if (NormalizeJunctionBEDFile.type().equals(type)) {
			result += "NormalizeJunctionBEDFile: " + NormalizeJunctionBEDFile.description() + "\n";
		}
		if (EPIC850KGenerateBEDFile.type().equals(type)) {
			result += "EPIC850KGenerateBEDFile: " + EPIC850KGenerateBEDFile.description() + "\n";
		}
		if (EPIC850KBedGraph2BW.type().equals(type)) {
			result += "EPIC850KBedGraph2BW: " + EPIC850KBedGraph2BW.description() + "\n";
		}
		if (LeventakiCalculateGeneCoordinate.type().equals(type)) {
			result += "LeventakiCalculateGeneCoordinate: " + LeventakiCalculateGeneCoordinate.description() + "\n";
		}
		if (LeventakiAddChrBW.type().equals(type)) {
			result += "LeventakiAddChrBW: " + LeventakiAddChrBW.description() + "\n";
		}
		if (JPaulTaylorEstimateCoverage.type().equals(type)) {
			result += "JPaulTaylorEstimateCoverage: " + JPaulTaylorEstimateCoverage.description() + "\n";
		}
		if (JPaulTaylorEstimateCoverageID.type().equals(type)) {
			result += "JPaulTaylorEstimateCoverageID: " + JPaulTaylorEstimateCoverageID.description() + "\n";
		}
		if (JPaulTaylorEstimateCoverageSpecial.type().equals(type)) {
			result += "JPaulTaylorEstimateCoverageSpecial: " + JPaulTaylorEstimateCoverageSpecial.description() + "\n";
		}
		if (TwoGroupComparisonBoxPlot.type().equals(type)) {
			result += "TwoGroupComparisonBoxPlot: " + TwoGroupComparisonBoxPlot.description() + "\n";
		}
		if (GroupComparisonBoxPlot.type().equals(type)) {
			result += "GroupComparisonBoxPlot: " + GroupComparisonBoxPlot.description() + "\n";
		}
		if (AppendMTORC1Motif2PeptideTable.type().equals(type)) {
			result += "AppendMTORC1Motif2PeptideTable: " + AppendMTORC1Motif2PeptideTable.description() + "\n";
		}
		if (HongboFilterPhosphositeLog2FC.type().equals(type)) {
			result += "HongboFilterPhosphositeLog2FC: " + HongboFilterPhosphositeLog2FC.description() + "\n";
		}
		if (AppendKinaseMotif2PeptideTable.type().equals(type)) {
			result += "AppendKinaseMotif2PeptideTable: " + AppendKinaseMotif2PeptideTable.description() + "\n";
		}
		if (GenerateSEMScript.type().equals(type)) {
			result += "GenerateSEMScript: " + GenerateSEMScript.description() + "\n";
		}
		if (McKinnonEnsurePerfectMatch.type().equals(type)) {
			result += "McKinnonEnsurePerfectMatch: " + McKinnonEnsurePerfectMatch.description() + "\n";
		}
		if (McKinnonRemoveFastaHits.type().equals(type)) {
			result += "McKinnonRemoveFastaHits: " + McKinnonRemoveFastaHits.description() + "\n";
		}
		if (McKinnonGenerateBlatBEDFile.type().equals(type)) {
			result += "McKinnonGenerateBlatBEDFile: " + McKinnonGenerateBlatBEDFile.description() + "\n";
		}
		if (McKinnonIntronRetentionQuantification.type().equals(type)) {
			result += "McKinnonIntronRetentionQuantification: " + McKinnonIntronRetentionQuantification.description() + "\n";
		}
		if (GenerateScatterPlotJavaScriptUserInputCustomColor.type().equals(type)) {
			result += "GenerateScatterPlotJavaScriptUserInputCustomColor: " + GenerateScatterPlotJavaScriptUserInputCustomColor.description() + "\n";
		}
		if (SummarizeRMATS402Result.type().equals(type)) {
			result += "SummarizeRMATS402Result: " + SummarizeRMATS402Result.description() + "\n";
		}
		if (SummarizeRMATS402ResultBlackList.type().equals(type)) {
			result += "SummarizeRMATS402ResultBlackList: " + SummarizeRMATS402ResultBlackList.description() + "\n";
		}
		if (GenerateExpressionBoxPlot.type().equals(type)) {
			result += "GenerateExpressionBoxPlot: " + GenerateExpressionBoxPlot.description() + "\n";
		}
		if (JUMPnProcessCluster2GMT.type().equals(type)) {
			result += "JUMPnProcessCluster2GMT: " + JUMPnProcessCluster2GMT.description() + "\n";
		}
		if (RMATS402CompareSplicingResults.type().equals(type)) {
			result += "RMATS402CompareSplicingResults: " + RMATS402CompareSplicingResults.description() + "\n";
		}
		if (JinghuiZhangCalculateGTExTotalReads.type().equals(type)) {
			result += "JinghuiZhangCalculateGTExTotalReads: " + JinghuiZhangCalculateGTExTotalReads.description() + "\n";
		}
		if (JinghuiZhangCalculatePCGPFPKMTarget.type().equals(type)) {
			result += "JinghuiZhangCalculatePCGPFPKM: " + JinghuiZhangCalculatePCGPFPKMTarget.description() + "\n";
		}
		if (SummarizeRMATS402SDResultWithBlackList.type().equals(type)) {
			result += "SummarizeRMATS402SDResultWithBlackList: " + SummarizeRMATS402SDResultWithBlackList.description() + "\n";
		}
		if (RMATS402CompareSplicingResultsSDWithBlackList.type().equals(type)) {
			result += "RMATS402CompareSplicingResultsSDWithBlackList: " + RMATS402CompareSplicingResultsSDWithBlackList.description() + "\n";
		}
		if (SummarizeRMATS402CountGene.type().equals(type)) {
			result += "SummarizeRMATS402CountGene: " + SummarizeRMATS402CountGene.description() + "\n";
		}
		if (SpearmanRankCorrelationMatrixForTwo.type().equals(type)) {
			result += "SpearmanRankCorrelationMatrixForTwo: " + SpearmanRankCorrelationMatrixForTwo.description() + "\n";
		}
		if (CombineSplicingDeficiencyNameMeta.type().equals(type)) {
			result += "CombineSplicingDeficiencyNameMeta: " + CombineSplicingDeficiencyNameMeta.description() + "\n";
		}
		if (JinghuiZhangGTExExonMedianQuan.type().equals(type)) {
			result += "JinghuiZhangGTExExonMedianQuan: " + JinghuiZhangGTExExonMedianQuan.description() + "\n";
		}
		if (JinghuiZhangCalculatePCGPExonCount.type().equals(type)) {
			result += "JinghuiZhangCalculatePCGPExonCount: " + JinghuiZhangCalculatePCGPExonCount.description() + "\n";
		}
		if (JinghuiZhangCalculatePCGPExonFPKM.type().equals(type)) {
			result += "JinghuiZhangCalculatePCGPExonFPKM: " + JinghuiZhangCalculatePCGPExonFPKM.description() + "\n";
		}
		if (JinghuiZhangCalculatePCGPExonDiseaseType.type().equals(type)) {
			result += "JinghuiZhangCalculatePCGPExonDiseaseType: " + JinghuiZhangCalculatePCGPExonDiseaseType.description() + "\n";
		}
		if (JinghuiZhangCalculatePercentileCutoff.type().equals(type)) {
			result += "JinghuiZhangCalculatePercentileCutoff: " + JinghuiZhangCalculatePercentileCutoff.description() + "\n";
		}
		if (CorrectMarSeptGeneName.type().equals(type)) {
			result += "CorrectMarSeptGeneName: " + CorrectMarSeptGeneName.description() + "\n";
		}
		if (JinghuiZhangWeightedCumulativePercentile.type().equals(type)) {
			result += "JinghuiZhangWeightedCumulativePercentile: " + JinghuiZhangWeightedCumulativePercentile.description() + "\n";
		}
		if (JinghuiZhangWCPCalculatePercentileCutoff.type().equals(type)) {
			result += "JinghuiZhangWCPCalculatePercentileCutoff: " + JinghuiZhangWCPCalculatePercentileCutoff.description() + "\n";
		}
		if (JinghuiZhangExonBoxplotMatrix.type().equals(type)) {
			result += "JinghuiZhangExonBoxplotMatrix: " + JinghuiZhangExonBoxplotMatrix.description() + "\n";
		}
		if (JinghuiZhangGenerateCategoryBarplot.type().equals(type)) {
			result += "JinghuiZhangGenerateCategoryBarplot: " + JinghuiZhangGenerateCategoryBarplot.description() + "\n";
		}
		if (JinghuiZhangGTExGenerateCategoryBarplot.type().equals(type)) {
			result += "JinghuiZhangGTExGenerateCategoryBarplot: " + JinghuiZhangGTExGenerateCategoryBarplot.description() + "\n";
		}
		if (UpdateScatterPlotColorBasedOnExpression.type().equals(type)) {
			result += "UpdateScatterPlotColorBasedOnExpression: " + UpdateScatterPlotColorBasedOnExpression.description() + "\n";
		}
		if (TenXGenomics2Matrix.type().equals(type)) {
			result += "TenXGenomics2Matrix: " + TenXGenomics2Matrix.description() + "\n";
		}
		if (RunSeuratAnalysisFromCellRanger.type().equals(type)) {
			result += "RunSeuratAnalysisFromCellRanger: " + RunSeuratAnalysisFromCellRanger.description() + "\n";
		}
		if (SeuratCalculateClusterDistribution.type().equals(type)) {
			result += "SeuratCalculateClusterDistribution: " + SeuratCalculateClusterDistribution.description() + "\n";
		}
		if (SamHeader2CellType.type().equals(type)) {
			result += "SamHeader2CellType: " + SamHeader2CellType.description() + "\n";
		}
		if (UpdateBarcodeClusterWithAnnotation.type().equals(type)) {
			result += "UpdateBarcodeClusterWithAnnotation: " + UpdateBarcodeClusterWithAnnotation.description() + "\n";
		}
		if (CalculateMedianForEachCluster.type().equals(type)) {
			result += "CalculateMedianForEachCluster: " + CalculateMedianForEachCluster.description() + "\n";
		}
		if (SuzanneBakerFilterBarcodeSamples.type().equals(type)) {
			result += "SuzanneBakerFilterBarcodeSamples: " + SuzanneBakerFilterBarcodeSamples.description() + "\n";
		}
		if (GenerateScatterPlotJavaScriptUserInputCustomColorMeta.type().equals(type)) {
			result += "GenerateScatterPlotJavaScriptUserInputCustomColorMeta: " + GenerateScatterPlotJavaScriptUserInputCustomColorMeta.description() + "\n";
		}
		if (GenerateScatterPlotJavaScriptUserInputCustomColorMetaComplex.type().equals(type)) {
			result += "GenerateScatterPlotJavaScriptUserInputCustomColorMetaComplex: " + GenerateScatterPlotJavaScriptUserInputCustomColorMetaComplex.description() + "\n";
		}
		if (ConvertMatrix2CellRangerExpressionOutput.type().equals(type)) {
			result += "ConvertMatrix2CellRangerExpressionOutput: " + ConvertMatrix2CellRangerExpressionOutput.description() + "\n";
		}
		if (ConvertMatrix2CellRangerExpressionOutputGene2Ensembl.type().equals(type)) {
			result += "ConvertMatrix2CellRangerExpressionOutputGene2Ensembl: " + ConvertMatrix2CellRangerExpressionOutputGene2Ensembl.description() + "\n";
		}
		if (EnsemblGeneIDAppendGeneName.type().equals(type)) {
			result += "EnsemblGeneIDAppendGeneName: " + EnsemblGeneIDAppendGeneName.description() + "\n";
		}		
		if (GenerateScatterPlotJavaScriptInputHTMLMeta.type().equals(type)) {
			result += "GenerateScatterPlotJavaScriptInputHTMLMeta: " + GenerateScatterPlotJavaScriptInputHTMLMeta.description() + "\n";
		}
		if (SuzanneBakerConvertSingleSampleGSEA2LineageScore.type().equals(type)) {
			result += "SuzanneBakerConvertSingleSampleGSEA2LineageScore: " + SuzanneBakerConvertSingleSampleGSEA2LineageScore.description() + "\n";
		}
		if (GenerateStackedBarPlotJavaScript.type().equals(type)) {
			result += "GenerateStackedBarPlotJavaScript: " + GenerateStackedBarPlotJavaScript.description() + "\n";
		}
		if (STARMappingScriptGeneratorV253a.type().equals(type)) {
			result += "STARMappingScriptGeneratorV253a: " + STARMappingScriptGeneratorV253a.description() + "\n";
		}
		if (SplitFilesRows.type().equals(type)) {
			result += "SplitFilesRows: " + SplitFilesRows.description() + "\n";
		}
		if (ConvertssGSEAMatrix2BoxplotMatrix.type().equals(type)) {
			result += "ConvertssGSEAMatrix2BoxplotMatrix: " + ConvertssGSEAMatrix2BoxplotMatrix.description() + "\n";
		}
		if (SpladderScriptGenerator.type().equals(type)) {
			result += "SpladderScriptGenerator: " + SpladderScriptGenerator.description() + "\n";
		}
		if (MultiplyMatrixValuesWithFactor.type().equals(type)) {
			result += "MultiplyMatrixValuesWithFactor: " + MultiplyMatrixValuesWithFactor.description() + "\n";
		}
		if (CombineTwoMatrixWithMismatchDoubleGene.type().equals(type)) {
			result += "CombineTwoMatrixWithMismatchDoubleGene: " + CombineTwoMatrixWithMismatchDoubleGene.description() + "\n";
		}
		if (ReplaceNAwithZero.type().equals(type)) {
			result += "ReplaceNAwithZero: " + ReplaceNAwithZero.description()  + "\n";
		}
		if (SpladderSummarizeOutput.type().equals(type)) {
			result += "SpladderSummarizeOutput: " + SpladderSummarizeOutput.description() + "\n";
		}
		if (CustomFilterSpladderSingleType.type().equals(type)) {
			result += "CustomFilterSpladderSingleType: " + CustomFilterSpladderSingleType.description() + "\n";
		}
		if (CustomFilterSpladderHardFilter.type().equals(type)) {
			result += "CustomFilterSpladderHardFilter: " + CustomFilterSpladderHardFilter.description() + "\n";
		}
		if (SplitFilesCols.type().equals(type)) {
			result += "SplitFilesCols: " + SplitFilesCols.description() + "\n";
		}
		if (ListOfFiles2Matrix.type().equals(type)) {
			result += "ListOfFiles2Matrix: " + ListOfFiles2Matrix.description() + "\n";
		}
		if (ConvertMatrix2CellRangerExpressionGeneIDCleanOutput.type().equals(type)) {
			result += "ConvertMatrix2CellRangerExpressionGeneIDCleanOutput: " + ConvertMatrix2CellRangerExpressionGeneIDCleanOutput.description() + "\n";
		}
		if (RPM2RPKMExonRelaxedGeneID.type().equals(type)) {
			result += "RPM2RPKMExonRelaxedGeneID: " + RPM2RPKMExonRelaxedGeneID.description() + "\n";
		}
		if (ReplaceNegWithZero.type().equals(type)) {
			result += "ReplaceNegWithZero: " + ReplaceNegWithZero.description() + "\n";
		}
		if (ConvertMatrix2CellRangerExpressionOutputNoGTF.type().equals(type)) {
			result += "ConvertMatrix2CellRangerExpressionOutputNoGTF: " + ConvertMatrix2CellRangerExpressionOutputNoGTF.description() + "\n";
		}
		if (CalculateWilcoxon.type().equals(type)) {
			result += "CalculateWilcoxon: " + CalculateWilcoxon.description() + "\n";
		}
		if (RNApegPostProcessingExons.type().equals(type)) {
			result += "RNApegPostProcessingMatrix: " + RNApegPostProcessingExons.description() + "\n";
		}
		if (RNApegDefineExonBasedoOnBW.type().equals(type)) {
			result += "RNApegDefineExonBasedoOnBW: " + RNApegDefineExonBasedoOnBW.description() + "\n";
		}
		if (GeneratePseudoReverseReferenceForRNAPeg.type().equals(type)) {
			result += "GeneratePseudoReverseReferenceForRNAPeg: " + GeneratePseudoReverseReferenceForRNAPeg.description() + "\n";
		}
		if (GenerateReverseReference.type().equals(type)) {
			result += "GenerateReverseReference: " + GenerateReverseReference.description() + "\n";
		}
		if (JuncSalvagerPipeline.type().equals(type)) {
			result += "JuncSalvagerPipeline: " + JuncSalvagerPipeline.description() + "\n";
		}
		if (SummarizeNovelExonAltStartSiteMatrix.type().equals(type)) {
			result += "SummarizeNovelExonAltStartSiteMatrix: " + SummarizeNovelExonAltStartSiteMatrix.description() + "\n";
		}
		if (GeneralIDConversion.type().equals(type)) {
			result += "GeneralIDConversion: " + GeneralIDConversion.description() + "\n";
		}
		if (JinghuiZhangGenerateTCGAMatrix.type().equals(type)) {
			result += "JinghuiZhangGenerateTCGAMatrix: " + JinghuiZhangGenerateTCGAMatrix.description() + "\n";
		}
		if (JinghuiZhangRenameTCGAMatrix.type().equals(type)) {
			result += "JinghuiZhangRenameTCGAMatrix: " + JinghuiZhangRenameTCGAMatrix.description() + "\n";
		}
		if (RemoveDuplicatedSampleName.type().equals(type)) {
			result += "RemoveDuplicatedSampleName: " + RemoveDuplicatedSampleName.description() + "\n";
		}
		if (Bam2StrandedBW.type().equals(type)) {
			result += "Bam2StrandedBW: " + Bam2StrandedBW.description() + "\n";
		}
		if (AppendColorAsMetaInfo.type().equals(type)) {
			result += "AppendColorAsMetaInfo: " + AppendColorAsMetaInfo.description() + "\n";
		}
		if (CellRangerRenameSampleName.type().equals(type)) {
			result += "CellRangerRenameSampleName: " + CellRangerRenameSampleName.description() + "\n";
		}
		if (GenerateScatterPlotJavaScriptUserInputInitializeColor.type().equals(type)) {
			result += "GenerateScatterPlotJavaScriptUserInputInitializeColor: " + GenerateScatterPlotJavaScriptUserInputInitializeColor.description() + "\n";
		}
		if (AppendExpressionColorAsMetaData.type().equals(type)) {
			result += "AppendExpressionColorAsMetaData: " + AppendExpressionColorAsMetaData.description() + "\n";
		}
		if (CalculateMedianForEachClusterSimple.type().equals(type)) {
			result += "CalculateMedianForEachClusterSimple: " + CalculateMedianForEachClusterSimple.description() + "\n";
		}
		return result;
	}
	

	public static String VERSION = "20190826";
	
}
