import functional.annotation.genecard.GeneCardKeyWords;
import functional.pathway.enrichment.FilterORAResults;
import functional.pathway.enrichment.FilterORAResultsFlex;
import functional.pathway.enrichment.GenerateGeneListDatabase;
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
import functional.pathway.network.database.CompareNetworkDatabase;
import functional.pathway.visualization.webcytoscape.CreateNetworkDisplay;
import functional.pathway.visualization.webcytoscape.CreateNetworkDisplayComplex;
import functional.pathway.visualization.webcytoscape.CreateNetworkDisplayExpression;
import functional.pathway.visualization.webcytoscape.DisplayJsonFileNetwork;
import functional.pathway.visualization.webcytoscape.GenerateEdgeMetaData;
import functional.pathway.visualization.webcytoscape.GenerateNodeMetaData;
import functional.pathway.visualization.webcytoscape.GenerateNodeMetaDataSize;
import functional.pathway.visualization.webcytoscape.NetworkNodeHighlight;
import general.sequence.analysis.GCScanner;
import general.sequence.blast.parse.nucleotide2protein.Extract100PercentMatch;
import general.sequence.blast.parse.nucleotide2protein.ExtractSequenceFromAlignment;
import general.sequence.blast.parse.nucleotide2protein.MakeFastaSingleLine;
import general.sequence.blast.parse.nucleotide2protein.RescueFragments;
import genomics.exome.AppendBamReviewFile;
import genomics.exome.GenerateSNVTableFromMutationTable;
import genomics.exome.circos.FromSV2CircosInput;
import genomics.exome.circos.Indel2CircosInput;
import genomics.exome.circos.SNV2CircosInput;
import genomics.exome.circos.SV2CircosInput;
import genomics.exome.indel.FilterDuplicatedHits;
import genomics.exome.misc.AppendGermlineAlternativeAlleleCount;
import genomics.exome.misc.GenerateSNV4File;
import genomics.exome.overlapexternaldb.CosmicParsingAndOverlap;
import genomics.exome.postprocessing.SnpDetectPostProcessingScript;
import genomics.exome.postprocessing.snvtools.AddRecurrenceAnnotation;
import genomics.exome.postprocessing.snvtools.RecurrentGeneMutFreq;
import genomics.exome.probedesign.GenerateExomeProbeDataHuman;
import genomics.exome.probedesign.GenerateExomeProbeDataMouse;
import genomics.exome.sjsnvindelpipeline.GenerateGRCh37liteSNVIndelScript;
import genomics.exome.sjsnvindelpipeline.GenerateHg19SNVIndelScript;
import genomics.exome.sjsnvindelpipeline.GenerateMm9SNVIndelScript;
import genomics.exome.snppopulationdistribution.SNPrsPopulation;
import genomics.exome.special.mousegermlineanalysis.ExtractSNPBasedOnSampleChrCoord;
import genomics.exome.special.mousegermlineanalysis.ExtractSingletonAndRecurrent;
import genomics.exome.special.mousegermlineanalysis.Mouse2HumanProteinPaintInput;
import genomics.exome.special.mousegermlineanalysis.SummarizeMouseIndelAnalysis;
import genomics.exome.summarize.AddSiftPrediction;
import genomics.exome.summarize.EXCAPGenerateSampleType;
import genomics.exome.summarize.EXCAPSummary;
import genomics.exome.summarize.EXONCAPBasicStats;
import genomics.exome.summarize.EXONCAPBasicStatsIndelPairedFile;
import genomics.exome.summarize.EXONCAPBasicStatsPairedFile;
import genomics.exome.summarize.EXONCAPHumanBasicStats;
import genomics.exome.summarize.ExcapRNAseqMAFColumn;
import genomics.exome.unpairedpipeline.GenerateSNVPseudoUnpairedScript;
import genomics.exome.unpairedpipeline.GenerateSNVUnpairedScript;
import genomics.exome.unpairedpipeline.GenerateSNVUnpairedScriptSimple;
import genomics.rnaseq.coverage.bw.NormalizeBedGraph;
import genomics.rnaseq.expression.transcriptionfactornetwork.ConvertAracneOutput2GMT;
import genomics.rnaseq.expression.transcriptionfactornetwork.GenerateAracneInputFile;
import genomics.rnaseq.fusion.cicero.AppendCICEROHTMLLink;
import genomics.rnaseq.fusion.cicero.ChromosomeBarPlot;
import genomics.rnaseq.fusion.cicero.ExtractFusionGenes;
import genomics.rnaseq.fusion.cicero.GenerateBamSoftLink;
import graph.figures.BarPlotGenerator;
import graph.figures.BoxPlotGeneratorThreeGroup;
import graph.figures.BoxPlotGeneratorTwoColumn;
import graph.figures.BoxPlotGeneratorTwoGroup;
import graph.figures.BoxplotExpressionForEachSample;
import graph.figures.HeatmapGeneration;
import graph.figures.MultipleBarPlotGenerator;
import graph.figures.SampleExprHistogram;
import graph.figures.ScatterPlotWithNameResidual;
import graph.figures.SingleScatterPlot;
import graph.figures.VolcanoPlot;
import graph.interactive.javascript.GenerateFoldchangeGeneLengthPlot;
import graph.interactive.javascript.GenerateScatterPlotJavaScript;
import graph.interactive.javascript.GenerateScatterPlotJavaScriptUserInput;
import graph.interactive.javascript.barplot.GenerateBatchBarPlotHtmls;
import graph.interactive.javascript.barplot.GenerateHorizontalBarPlotJavaScript;
import graph.interactive.javascript.barplot.GenerateVerticalBarPlotJavaScript;
import graph.interactive.javascript.heatmap.GenerateHeatmapJavaScript;
import graph.interactive.javascript.heatmap.GenerateHeatmapZscoreSSGSEAJavaScript;
import graph.interactive.javascript.heatmap.GenerateHeatmapZscoreWithOriginalValuesJavaScript;
import graph.interactive.javascript.maplot.GenerateMAPlotJavaScript;
import graph.interactive.javascript.maplot.GenerateMAPlotJavaScriptUserInput;
import graph.interactive.javascript.volcanoplot.GenerateVolcanoPlotJavaScript;
import graph.interactive.javascript.volcanoplot.GenerateVolcanoPlotJavaScriptUserInput;
import graph.interactive.javascript.volcanoplot.GenerateVolcanoPlotJavaScriptUserInputPathways;
import idconversion.cross_species.AppendHuman2Mouse;
import idconversion.cross_species.AppendMouse2Human;
import idconversion.cross_species.EnsureUniqGeneNamesHumanMouse;
import idconversion.cross_species.GMTHuman2Mouse;
import idconversion.cross_species.GMTMouse2Human;
import idconversion.cross_species.HumanMouseGeneNameConversion;
import idconversion.ensembl.GenerateEnsembl2GeneNameTable;
import idconversion.ensembl.MicroarrayEnsembl2GeneName;
import idconversion.protein2genome.FastaRefSeq2Ensembl;
import idconversion.tools.CleanEnsemblGeneID2GeneName;
import idconversion.tools.ConvertUniprot2GeneAndAppend;
import idconversion.tools.EnsemblGeneID2GeneName;
import idconversion.tools.EnsemblGeneID2GeneNameXenograft;
import idconversion.tools.EnsemblGeneIDAppendAnnotation;
import idconversion.tools.GeneName2EnsemblID;
import idconversion.tools.GenerateConversionTable;
import idconversion.tools.MicroarrayAddGeneName;
import idconversion.tools.RefSeq2GeneName;
import idconversion.tools.SubGeneFromConversionTable;
import idconversion.tools.kgXrefAppendOfficialGeneSymbol;
import idconversion.tools.kgXrefConversion;
import idconversion.tools.kgXrefConversionProtein2GeneName;
import integrate.DNARNAseq.CalculateRNAseqMAF;
import integrate.DNARNAseq.OverlapGenotypeMatrix;
import integrate.Visualization.ExpressionIntegrationDrawer;
import integrate.Visualization.ExpressionIntegrationDrawerFilter;
import integrate.Visualization.ExpressionIntegrationDrawerWhlPho;
import integrate.Visualization.IntegrationDrawerFilterGeneList;
import integrate.genematrix.GenerateGeneWeightFile;
import integrate.genematrix.IntegrateExpressionMatrix;
import integrate.summarytable.ComprehensiveSummaryTableSampleTypeSNVFusion;
import integrate.summarytable.ComprehensiveSummaryTableSampleTypeSNVFusionFilter;
import integrate.summarytable.FilterSNVSamples;
import integrate.summarytable.IntegratedSummaryTable;
import integrate.summarytable.IntegratedSummaryTableFrequencyCount;
import integrate.summarytable.IntegrationAddGeneAnnotation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import network.ParseThroughSIF;
import network.MISC.BioPlex2HumanInteractome;
import network.MISC.GenerateGraphStatistics;
import network.MISC.GenerateSubgraph;
import network.db.biogrid.annotation.GenerateBiogrid2SIF;
import network.db.compass.CompassGenerateSifFile;
import network.db.string.annotation.AppendGeneName2StringNetwork;
import network.db.string.annotation.CleanBioplexTSVFile;
import network.db.string.annotation.Convert2SIFFile;
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
import network.protein.complex.annotation.AppendProteinComplexInfo;
import network.statistics.CalculateGraphStatistics;
import network.transcription.tf.tools.TFGeneEnrichmentFilter;
import network.transcription.tf.tools.TFRegulatedGenes;
import nextgenerationsequencing.fastq.SplitFastqForwardReverse;
import bedtools.BedAddRemoveChr;
import mappingtools.Bam2Fastq;
import mappingtools.MappingInsertSizeEstimation;
import mappingtools.samtools.v0_1_17.SummarizeFlagStats;
import mathtools.expressionanalysis.differentialexpression.AddAnnotation2DiffFisher;
import mathtools.expressionanalysis.differentialexpression.AddAnnotationGeneral;
import mathtools.expressionanalysis.differentialexpression.AppendLIMMAResult2Matrix;
import mathtools.expressionanalysis.differentialexpression.CalculateCumulativeProb;
import mathtools.expressionanalysis.differentialexpression.CalculateLIMMA;
import mathtools.expressionanalysis.differentialexpression.CalculateTTest;
import mathtools.expressionanalysis.differentialexpression.CheckIfDifferentiallyExpressed;
import mathtools.expressionanalysis.differentialexpression.CombineDEGeneSet;
import mathtools.expressionanalysis.differentialexpression.CombineDEGeneSetLimitOverlap;
import mathtools.expressionanalysis.differentialexpression.DEAddAnnotation;
import mathtools.expressionanalysis.differentialexpression.DEAddAnnotationRelaxed;
import mathtools.expressionanalysis.differentialexpression.DEGFilteredGeneSet;
import mathtools.expressionanalysis.differentialexpression.ExtractDEGenes;
import mathtools.expressionanalysis.differentialexpression.GrabSampleNameWithKeyword;
import mathtools.expressionanalysis.differentialexpression.GrabSampleNameWithoutKeyword;
import mathtools.expressionanalysis.differentialexpression.OverlapDEGeneSet;
import mathtools.expressionanalysis.differentialexpression.SampleFilter;
import metabolomics.structure.clustering.StructureFractionClustering;
import metagenomics.assembly.MergeFastQ;
import metagenomics.qiime.postprocessing.CombineOTUCounts;
import metagenomics.qiime.postprocessing.GenerateJasonMatrixTable;
import metagenomics.qiime.postprocessing.GenerateQIIMEMatrixTable;
import metagenomics.qiime.postprocessing.NormalizePerHundredKilo;
import metagenomics.qiime.preprocessing.ChemoProjectGenerateMetaFile;
import metagenomics.qiime.preprocessing.GrabEachFileInsertIDFasta;
import microarray.tools.idconversion.MicroArrayIDConversionAnnotation;
import microarray.tools.idconversion.MicroArrayIDConversionFlex;
import microarray.tools.methylation.EPIC850K.BMIQNormalizationSingleSample;
import microarray.tools.methylation.EPIC850K.CombineBMIQFiles;
import microarray.tools.methylation.EPIC850K.EPIC850KAveragedBEDFile;
import microarray.tools.methylation.EPIC850K.EPIC850KMostMADVariableProbe;
import microarray.tools.methylation.EPIC850K.EPIC850KWilcoxonTestMethylation;
import microarray.tools.methylation.EPIC850K.Epic850KAppendMetaInformation;
import microarray.tools.methylation.EPIC850K.Epic850KHyperHypoMethylationFilter;
import microarray.tools.methylation.EPIC850K.Methylation850KAppendGeneInfo;
import microarray.tools.methylation.EPIC850K.Methylation850KWilcoxonTestAppendGeneInfo;
import microsoft.document.word.generator.GenerateDisplayIonReport;
import microsoft.document.word.generator.Txt2Excel;
import misc.CommandLine;
import misc.CustomFastaCombiner;
import misc.ExpandGeneNames;
import misc.ExtractRandomFastaSequence;
import misc.FilterColumnName;
import misc.FilterColumns;
import misc.FilterDuplicate;
import misc.GenerateFastaFileFromTrypticTxt;
import misc.GenerateGSEADataset;
import misc.GrabColConvert2Fasta;
import misc.GrabColumnName;
import misc.GrabGeneName;
import misc.GrabRowName;
import misc.GrabUniqValuesFromColumn;
import misc.KeepProteinCodingGenes;
import misc.MISCConvertPeptideID;
import misc.Matrix2Addition;
import misc.Matrix2Exponent;
import misc.Matrix2Log2;
import misc.MergeGeneName;
import misc.MergeGeneNameClean;
import misc.OverlapTwoFiles;
import misc.RemoveNoncodingRNA;
import misc.RemoveQuotations;
import misc.ReorderSampleFast;
import misc.ReorderSamples;
import misc.RunRScript;
import misc.test.ReadEnsemblGTFFile;
import misc.test.Test;
import misc.textmining.software.annotation.WebTextMining;
import customScript.AppendChromosomeNumber;
import customScript.ElenaConvertRefSeq2GeneName;
import enrichment.tool.go.ParseGeneOntology;
import expression.matrix.tools.AddGeneKO2Sample;
import expression.matrix.tools.AppendMADValue;
import expression.matrix.tools.AppendMatrixTogether;
import expression.matrix.tools.CalculateCorrelationMatrix;
import expression.matrix.tools.CombineTwoMatrixWithMismatch;
import expression.matrix.tools.FilterBasedOnAnnotation;
import expression.matrix.tools.FilterMatrixColumnValue;
import expression.matrix.tools.FilterMatrixExpression;
import expression.matrix.tools.FilterMatrixFile;
import expression.matrix.tools.FilterMatrixFileFlex;
import expression.matrix.tools.GeneListMatrix;
import expression.matrix.tools.GeneListMatrix2;
import expression.matrix.tools.GenerateSpearmanRankMatrix;
import expression.matrix.tools.GenerateTrendPlot;
import expression.matrix.tools.HumanMouseSpearmanRankCorrel;
import expression.matrix.tools.KeepColumnsFromMatrix;
import expression.matrix.tools.MatrixLog2ZscoreNormalization;
import expression.matrix.tools.MatrixZscoreNormalization;
import expression.matrix.tools.MatrixZscoreNormalizationWithOriginalValues;
import expression.matrix.tools.MergeSamples;
import expression.matrix.tools.OrderGeneMatrixBasedOnTTestDist;
import expression.matrix.tools.QuantileNormalization;
import expression.matrix.tools.RemoveColumnsFromMatrix;
import expression.matrix.tools.RemoveRowsWithNAs;
import expression.matrix.tools.RemoveZeroCountGenes;
import expression.matrix.tools.SummarizeMATSGenes;
import expression.matrix.tools.TransposeMatrix;
import expressionanalysis.tools.batchcorrection.TwoGroupMeanCentering;
import expressionanalysis.tools.batchcorrection.TwoGroupMeanCenteringFlex;
import expressionanalysis.tools.genename.GeneSymbol2UCSCIDAppend;
import expressionanalysis.tools.gsea.CalculateRank;
import expressionanalysis.tools.gsea.ConvertGSEAHuman2Mouse;
import expressionanalysis.tools.gsea.ConvertGSEAList2AnnotationFile;
import expressionanalysis.tools.gsea.GSEAHeatmap;
import expressionanalysis.tools.gsea.GenerateGSEAInputCLSFile;
import expressionanalysis.tools.gsea.GenerateGSEAInputGCTFile;
import expressionanalysis.tools.gsea.SummarizeGSEAResult;
import expressionanalysis.tools.unsupervised.GeneratePCAScatterPlotPython;
import expressionanalysis.tools.unsupervised.PCAPlot;
import jump.pipeline.tools.ExtractUniqPeptides;
import jump.pipeline.tools.FilterPSMInformationPeptide;
import jump.pipeline.tools.FilterPSMInformationProteinName;
import jump.pipeline.tools.GeneratePhosphoPeptideMatrix;
import jump.pipeline.tools.GenerateProteomeGeneMatrix;
import jump.pipeline.tools.MergeRowsMaximizePSM;
import pathway.tools.PathwayKappaScore;
import pipeline.sequence.analysis.blasttool.GenerateBlastFile;
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
import protein.features.charge.GenerateChargeGraphForEachProtein;
import protein.features.charge.MatchFasta2Coordinate;
import protein.features.combineresults.Append2GRPRInfo;
import protein.features.combineresults.CombineAAFreqProteinFeature;
import protein.features.combineresults.CombineProteinFeatures;
import protein.features.combineresults.ProteinFeatureWithGRPRInfo;
import protein.features.embosstools.ReadPepInfo;
import protein.features.hydrophobicity.CalculateHydrophobicityFastaFile;
import protein.features.lowcomplexitydomain.AppendUbiquitome;
import protein.features.lowcomplexitydomain.GRPRReplaceAnnotationInformation;
import protein.features.lowcomplexitydomain.GenerateSEGSampleGroup;
import protein.features.lowcomplexitydomain.GrabGRPRFasta;
import protein.features.lowcomplexitydomain.SEGPostProcessing;
import protein.features.lowcomplexitydomain.UniprotSEGPostProcessing;
import protein.features.motif.meme.GenerateUniqFastaFile;
import protein.features.plots.ProteinFeatureHistoBarPlot;
import protein.features.plots.ProteinFeatureHistoBarPlotGRPR;
import protein.features.plots.ProteinFeaturePlots;
import protein.features.sequenceconservation.AlignSEGSequence;
import protein.features.sequenceconservation.ConservationSurvey;
import protein.features.sequenceconservation.GenerateFastaSequenceForEachProtein;
import protein.features.sspa_tools.ExtractSpeciesEMBOSFile;
import protein.features.sspa_tools.GenerateSAPSOutput;
import protein.features.sspa_tools.GenerateSSPAMatrix;
import protein.structure.domain.disorder.CalcDisorderRegionDistribution;
import protein.structure.domain.disorder.CalculateAminoAcidContent;
import protein.structure.domain.disorder.CalculateAminoAcidContentSummary;
import protein.structure.domain.disorder.CountGeneWithDisorderRegion;
import protein.structure.domain.disorder.CountGeneWithDisorderRegionPlot;
import protein.structure.domain.disorder.GenerateD2P2Input;
import protein.structure.domain.disorder.ProteinFeatureCombineResults;
import protein.structure.domain.disorder.ReadD2P2Database;
import proteomics.SimulatedPeptideDigestion;
import proteomics.annotation.uniprot.ExportNucleotideBindingMotifInfo;
import proteomics.annotation.uniprot.ExtractUniprotInfo;
import proteomics.annotation.uniprot.GenerateIDConversionMasterTable;
import proteomics.apms.saint.CalculateGeneLengthSaintInputFile;
import proteomics.apms.saint.GenerateInteractionFileForSaint;
import proteomics.apms.saint.GeneratePreyGeneLength;
import proteomics.phospho.kinaseactivity.pipeline.AssignKnownKinaseSubstrateRelationship;
import proteomics.phospho.kinaseactivity.pipeline.AssignKnownKinaseSubstrateRelationshipFlex;
import proteomics.phospho.kinaseactivity.pipeline.CleanWhlProteome;
import proteomics.phospho.kinaseactivity.pipeline.ConvertMatrix2IKAPInput;
import proteomics.phospho.kinaseactivity.pipeline.ConvertMatrix2IKAPInputNormalize;
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
import proteomics.phospho.motifs.tools.stats.GenerateKSScatterPlots;
import proteomics.phospho.motifs.tools.stats.PhosphoKinaseBackgroundRandom;
import proteomics.phospho.motifs.tools.stats.PhosphoKinaseCorrelationDistribution;
import proteomics.phospho.motifs.tools.stats.PhosphoKinaseCorrelationDistributionAll;
import proteomics.phospho.tools.annotation.ProteinActivityAnnotation;
import proteomics.phospho.tools.basicstats.GenerateBarPlot;
import proteomics.phospho.tools.basicstats.PhosphoBasicStats;
import proteomics.phospho.tools.coordinate.converter.ConvertProteinCoord;
import proteomics.phospho.tools.coordinate.converter.HumanCentricProteinAlignment;
import proteomics.phospho.tools.coordinate.converter.Kin2SubConvert2Coordinate;
import proteomics.phospho.tools.coordinate.converter.MouseCentricProteinAlignment;
import proteomics.phospho.tools.enrichment.GenerateEnrichmentBarPlot;
import proteomics.phospho.tools.enrichment.GenerateEnrichmentFileInput;
import proteomics.phospho.tools.enrichment.GenerateKinaseSubstrateGMT;
import proteomics.phospho.tools.enrichment.GenerateKinaseSubstrateGMTFamily;
import proteomics.phospho.tools.enrichment.GenerateKinaseSubstrateGMTGroup;
import proteomics.phospho.tools.evaluation.GenerateROCCurvePerKinase;
import proteomics.phospho.tools.evaluation.GenerateROCCurveRandomRandom;
import proteomics.phospho.tools.generatenetwork.AddNetworkNeighborEvidence;
import proteomics.phospho.tools.generatenetwork.CalculateSubnetBioNetInput;
import proteomics.phospho.tools.generatenetwork.FilterKinaseSubstrate2KinaseOnly;
import proteomics.phospho.tools.generatenetwork.GenerateComplexNetwork;
import proteomics.phospho.tools.generatenetwork.KinaseSubstrate2KinaseOnly;
import proteomics.phospho.tools.generatenetwork.KinaseSubstrateAll;
import proteomics.phospho.tools.generatenetwork.SubNetworkBioNet;
import proteomics.phospho.tools.gsea.ConvertKinaseGroupTxt2Gmt;
import proteomics.phospho.tools.heatmap.GrabPhosphositeExpression;
import proteomics.phospho.tools.heatmap.GrabPhosphositeExpressionAll;
import proteomics.phospho.tools.heatmap.GrabPhosphositeExpressionGeneCentric;
import proteomics.phospho.tools.heatmap.JUMPqDataMatrixGeneration;
import proteomics.phospho.tools.heatmap.JUMPqDataMatrixGenerationAll;
import proteomics.phospho.tools.heatmap.PhosphoDataMatrixAndHeatmap;
import proteomics.phospho.tools.heatmap.PhosphoExpr2HeatmapFriendly;
import proteomics.phospho.tools.kinase.report.ActivityPhosphositeForAll;
import proteomics.phospho.tools.kinase.report.ActivityPhosphositeForKinase;
import proteomics.phospho.tools.kinase.report.DegradationPhosphositeRegForAll;
import proteomics.phospho.tools.kinase.report.SummarizeKinaseInformation;
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
import proteomics.phospho.tools.misc.AddScanCountInfo;
import proteomics.phospho.tools.misc.AppendMoreInformationTogether;
import proteomics.phospho.tools.misc.AppendOriginalPeptideInformation;
import proteomics.phospho.tools.misc.AppendOriginalPeptideInformation2Table;
import proteomics.phospho.tools.misc.CalculateKinase2KinaseCorrelation;
import proteomics.phospho.tools.misc.FilterBackground2CoreProtein;
import proteomics.phospho.tools.misc.FilterPutativeKinase;
import proteomics.phospho.tools.misc.GrabFastaFile;
import proteomics.phospho.tools.misc.OrganismConversion2PhosphositeFile;
import proteomics.phospho.tools.motifs.AddKinaseBasedOnPhosphosite;
import proteomics.phospho.tools.motifs.AddRelativeQuantification;
import proteomics.phospho.tools.motifs.AddRelativeQuantificationForPredicted;
import proteomics.phospho.tools.motifs.AddRelativeQuantificationForPredictedAll;
import proteomics.phospho.tools.motifs.AddRelativeQuantificationForPredictedAllJUMP;
import proteomics.phospho.tools.motifs.AddRelativeQuantificationJUMP;
import proteomics.phospho.tools.motifs.Ascore2FastaFile;
import proteomics.phospho.tools.motifs.Ascore2FastaFileJUMP;
import proteomics.phospho.tools.motifs.CalcMotifEnrichment;
import proteomics.phospho.tools.motifs.CalculateAllMotifPValueFastaFile;
import proteomics.phospho.tools.motifs.GeneratePredictedHistogramDistribution;
import proteomics.phospho.tools.motifs.PhosphoMotifMatching;
import proteomics.phospho.tools.motifs.degenerative.ExtendJUMPqSite;
import proteomics.phospho.tools.motifs.degenerative.GenerateFastaFileFromJUMPqPeptide;
import proteomics.phospho.tools.motifs.degenerative.GenerateFastaFileFromJUMPqSite;
import proteomics.phospho.tools.motifs.motifx.ExtendPeptide2Fasta;
import proteomics.phospho.tools.motifs.motifx.ExtendPeptide2Table;
import proteomics.phospho.tools.motifs.motifx.MotifXMatchMotif;
import proteomics.phospho.tools.motifs.motifx.MotifXSummaryTable;
import proteomics.phospho.tools.motifs.motifx.ParseMotifXOutput;
import proteomics.phospho.tools.painter.KinaseSubstratePainter;
import proteomics.phospho.tools.peptide.coverage.PeptideCategoriesSharedOrUniqIDmod;
import proteomics.phospho.tools.peptide.coverage.PeptideCategoriesSharedOrUnique;
import proteomics.phospho.tools.peptide.coverage.PeptideCoveragePlot;
import proteomics.phospho.tools.peptide.coverage.PeptideCoverageSingleGeneComparison;
import proteomics.phospho.tools.phophositeplus.DownloadAllPossibleSiteInfo;
import proteomics.phospho.tools.phosphogps.CreatePhosphoGPSFastaFile;
import proteomics.phospho.tools.pssm.AppendPSSMResult2HPRD;
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
import proteomics.phospho.tools.summary.CalculatePhosphoStatistics;
import proteomics.phospho.tools.summary.ExtractLineBasedOnList;
import proteomics.phospho.tools.summary.PhosphoSummarizeKeepTopHit;
import proteomics.phospho.tools.summary.PhosphoSummarizeResults;
import references.gtf.manipulation.Filter3PrimeGTFExon;
import references.gtf.manipulation.xenograft.Mouse2GTF;
import references.gtf.statistics.GTFSummaryStatistics;
import rnaseq.bed.coverage.circos.GenerateCircosCoverageBed;
import rnaseq.expression.tools.CombineFPKMFiles;
import rnaseq.expression.tools.ExpressionNormalization;
import rnaseq.mapping.tools.bw.Bam2BW;
import rnaseq.mapping.tools.cufflinks.ExtractDifferentiatedTranscriptOnly;
import rnaseq.mapping.tools.cufflinks.ExtractFPKM;
import rnaseq.mapping.tools.cufflinks.GenerateCuffDiffScript;
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
import rnaseq.mapping.tools.star.RPM2RPKMTranscript;
import rnaseq.mapping.tools.star.RawCount2RPM;
import rnaseq.mapping.tools.star.RawCount2RPMSkipFirstTwoColumns;
import rnaseq.mapping.tools.star.STARMappingScriptGenerator;
import rnaseq.mapping.tools.star.STARMappingScriptGeneratorForTrimFastq;
import rnaseq.mapping.tools.star.SummarizeStarMapping;
import rnaseq.mapping.tools.star.SummarizeStarMappingMerge;
import rnaseq.mapping.tools.star.TrimmomaticScriptGenerator;
import rnaseq.mapping.tools.star.UBam2FQ;
import rnaseq.pcpa.AddChr;
import rnaseq.pcpa.CalculatePolyADistribution;
import rnaseq.pcpa.CalculatePolyADistributionMouse;
import rnaseq.pcpa.CombinePCPAResults;
import rnaseq.pcpa.ExtractPolyAReadsUsePolyALibrary;
import rnaseq.pcpa.ExtractPolyAReadsUsePolyALibrarySingleCell;
import rnaseq.pcpa.ExtractPolyAReadsYuxinScript;
import rnaseq.pcpa.GeneratePCPAHumanScript;
import rnaseq.pcpa.GeneratePCPAHumanScriptComplete;
import rnaseq.pcpa.GeneratePCPAMouseScript;
import rnaseq.pcpa.GeneratePolyAHistogramOutput;
import rnaseq.pcpa.KeepPolyA;
import rnaseq.pcpa.MatchFq2Bam;
import rnaseq.pcpa.PCPAAppendMetaDeta;
import rnaseq.pcpa.PLA2BEDFile;
import rnaseq.splicing.intronretention.Bam2BedConversion;
import rnaseq.splicing.intronretention.CalculateCoverageBed;
import rnaseq.splicing.intronretention.CalculateSplicingDeficiency;
import rnaseq.splicing.intronretention.CalculateSplicingDeficiencyScript;
import rnaseq.splicing.intronretention.CombineSplicingDeficiencyName;
import rnaseq.splicing.intronretention.CountNumberOfUniqReads;
import rnaseq.splicing.intronretention.CountNumberOfUniqReadsScript;
import rnaseq.splicing.intronretention.DetectIntronRetention;
import rnaseq.splicing.intronretention.FilterBEDReads;
import rnaseq.splicing.intronretention.FilterBEDReadsScript;
import rnaseq.splicing.intronretention.FilterReadsForSDScore;
import rnaseq.splicing.intronretention.IntersectBed;
import rnaseq.splicing.intronretention.IntronMappingPercentageSummary;
import rnaseq.splicing.intronretention.IntronRetentionHistogramData;
import rnaseq.splicing.intronretention.OverlapAllMouseHuman;
import rnaseq.splicing.intronretention.OverlapMouseHumanGeneName;
import rnaseq.splicing.intronretention.graphs.GenerateIntronRetentionBarPlot;
import rnaseq.splicing.mats308.AddGeneName2MATS;
import rnaseq.splicing.mats308.AddGeneName2rMATS401;
import rnaseq.splicing.mats308.FilterMATSResults;
import rnaseq.splicing.mats308.GenerateOverlappingResults;
import rnaseq.splicing.mats308.MATSGenerateResultTable;
import rnaseq.splicing.mats308.MATSScriptGenerator;
import rnaseq.splicing.mats308.OverlapAlternativeSplicingGeneList;
import rnaseq.splicing.mats308.SummarizeMATSSummary;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilter;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilterDiffExpr;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilterDisplayGeneList;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilterExpr;
import rnaseq.splicing.mats308.SummarizeResultsAfterMATSFilterGeneMatrix;
import rnaseq.splicing.misc.CalculateExonDistribution;
import rnaseq.splicing.misc.GenerateGCContentMatrix;
import rnaseq.splicing.splicefactor.enrichment.CombineEnrichmentPvalues;
import rnaseq.splicing.splicefactor.enrichment.ExtractRandomExonFromGTF;
import rnaseq.splicing.splicefactor.enrichment.GrabExonInformation;
import rnaseq.splicing.splicefactor.enrichment.PositionWeightMatrixScanner;
import rnaseq.splicing.splicefactor.enrichment.ReadMATSExtractNeighboringSequencing;
import rnaseq.splicing.splicefactor.enrichment.SpliceFactorMotifFisherExact;
import rnaseq.splicing.splicefactor.enrichment.SpliceFactorMotifScanner;
import rnaseq.splicing.summary.AppendExpressionToMATSOutput;
import rnaseq.tools.ercc.GenerateERCCgtffile;
import rnaseq.tools.exonjunction.CompareDifferentialAnalysis;
import rnaseq.tools.exonjunction.ExonJunctionMatrix;
import rnaseq.tools.exonjunction.GeneVsJunctionFC;
import rnaseq.tools.exonjunction.GrabDifferentiatedJunctions;
import rnaseq.tools.exonjunction.JunctionVsGeneJunc;
import rnaseq.tools.exonjunction.NormalizeJunctionCount;
import rnaseq.tools.exonjunction.OverlapLIMMAAndExonJunctionCount;
import rnaseq.tools.genelengthanalysis.AppendGeneLength;
import rnaseq.tools.genelengthanalysis.CompareExonCountDistribution;
import rnaseq.tools.genelengthanalysis.CompareGeneLengthDistribution;
import rnaseq.tools.genelengthanalysis.GTFAnnotateExonLength;
import rnaseq.tools.genelengthanalysis.GTFAnnotateGeneLength;
import rnaseq.tools.genelengthanalysis.GTFAnnotateNumExon;
import rnaseq.tools.genelengthanalysis.GTFAnnotationSimple;
import rnaseq.tools.genelengthanalysis.TranscriptLengthSlidingWindow;
import rnaseq.tools.genelengthanalysis.TranscriptLengthSlidingWindowInhibitedGenes;
import rnaseq.tools.metadata.AppendMetadataTag2RNAseqMatrixSampleName;
import rnaseq.tools.mousemodel.qc.FPKMBoxPlotOfGeneKO;
import rnaseq.tools.mousemodel.qc.FPKMBoxPlotOfGeneKOSampleSpecific;
import rnaseq.tools.mousemodel.qc.RenameSampleForBoxPlot;
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
import rnaseq.tools.singlecell.tenxgenomics.cellranger.SpecialClassForDougGreen;
import rnaseq.tools.singlecell.tools.census.normalization.CensusNormalization;
import rnaseq.tools.singlecell.zeroanalysis.CompileDataForViolinPlot;
import rnaseq.tools.singlecell.zeroanalysis.GenerateZeroAnalysisBinningTable;
import rnaseq.tools.singlecell.zeroanalysis.GrabGeneLessThanValue;
import rnaseq.tools.singlecell.zeroanalysis.GrabGeneOverValue;
import rnaseq.tools.summary.CalculateIntersectingGenes;
import rnaseq.tools.summary.CombineEnrichmentPathwayPvalues;
import rnaseq.tools.summary.CombineLIMMAResultExpression;
import rnaseq.tools.summary.GenerateFPKMBinningTable;
import rnaseq.tools.summary.GenerateRNASEQCoverageStatistics;
import rnaseq.tools.summary.IntronExonCoverageBED;
import rnaseq.tools.summary.PlotBinningTable;
import sequencing.tools.bedmanupulation.BedGraphFilterChromosomeName;
import statistics.general.EXONCAPStatsReport;
import statistics.general.MathTools;
import statistics.general.RNASEQStatsReport;
import stjude.StJudeSoftLinks.CoveragePostGenSoftLink;
import stjude.StJudeSoftLinks.FlagStatSoftLink;
import stjude.projects.hongbochi.AppendMTORC1Motif2Table;
import stjude.projects.hongbochi.AppendMetaInformation;
import stjude.projects.hongbochi.CalculateAUC;
import stjude.projects.hongbochi.CalculateROCforMTORC1Motif;
import stjude.projects.hongbochi.HongboAppendSensitivitySpecificity;
import stjude.projects.hongbochi.HongboAppendSensitivitySpecificityFlex;
import stjude.projects.hongbochi.phosphoanalysis.AssignKnownKinaseSubstrateRelationshipHongbo;
import stjude.projects.hongbochi.phosphoanalysis.ConvertWGCNAPeptide2Site;
import stjude.projects.hongbochi.phosphoanalysis.GenerateComprehensiveGeneList;
import stjude.projects.hongbochi.phosphoanalysis.GenerateMotifXFasta;
import stjude.projects.hongbochi.phosphoanalysis.GenerateMotifXFastaAll;
import stjude.projects.hongbochi.phosphoanalysis.GenerateNetworkBasedOnClusters;
import stjude.projects.hongbochi.phosphoanalysis.HongboAnnotateMotifInformation;
import stjude.projects.hongbochi.phosphoanalysis.HongboAnnotateMotifInformationYuxinFile;
import stjude.projects.hongbochi.phosphoanalysis.KinaseFamilyCluster;
import stjude.projects.hongbochi.phosphoanalysis.OverlapPeptide2Phosphosite;
import stjude.projects.hongbochi.phosphoanalysis.PhosphoMotifEnrichment;
import stjude.projects.hongbochi.phosphoanalysis.WGCNAKinaseEnrichmentPhosphosite;
import stjude.projects.hongbochi.phosphoanalysis.WGCNAKinaseEnrichmentPvalue;
import stjude.projects.hongbochi.phosphoanalysis.WGCNAModifyShape;
import stjude.projects.hongbochi.phosphoanalysis.WGCNANetwork;
import stjude.projects.jinghuizhang.GenerateMIXCR;
import stjude.projects.jinghuizhang.SummarizeMIXCRresult;
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
import stjude.projects.jpaultaylor.MatchUniprotGeneName2GeneLCDLength;
import stjude.projects.jpaultaylor.SplitFastaFile;
import stjude.projects.junminpeng.hgg.FilterKinaseBasedOnFrequency;
import stjude.projects.junminpeng.proteomics.peptide.peak.iondrawer.GenerateDTAFilesScript;
import stjude.projects.junminpeng.proteomics.peptide.peak.iondrawer.GenerateDTARawFilesScript;
import stjude.projects.junminpeng.proteomics.peptide.peak.iondrawer.GenerateDisplayIonHTMLImgSimple;
import stjude.projects.junminpeng.proteomics.peptide.peak.iondrawer.GenerateDisplayIonHtmlImg;
import stjude.projects.leventaki.FilterCNVkitcnrfiles;
import stjude.projects.leventaki.High20ToTHETA;
import stjude.projects.leventaki.LeventakiCombineCNSResult;
import stjude.projects.leventaki.LeventakiExtractProbeCoordinate;
import stjude.projects.leventaki.LeventakiGenerateVCFPlot;
import stjude.projects.leventaki.SummarizeLeventakiProject;
import stjude.projects.leventaki.SummarizeVDJclones;
import stjude.projects.mckinnon.GenerateMatrixForMutationalSignature;
import stjude.projects.mckinnon.McKinnonCalculateGCSkew;
import stjude.projects.mckinnon.McKinnonGCScanner;
import stjude.projects.mckinnon.McKinnonGCScatterPlot;
import stjude.projects.mckinnon.McKinnonGCScatterPlotTTS;
import stjude.projects.mckinnon.McKinnonGenerateRandomBEDFile;
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
import UniprotTool.ExtractFastaOrganism;
import WordDocumentGenerator.KinaseSummary.GenerateWordKinaseSummary;

/**
 * Collection of scripts and pipelines for DNA RNA Proteomics Phosphoproteomics
 * and Metabolomic and Metagenomics This is the centralized jar class for
 * combining all datatype Last updated 2017-06-29
 * 
 * @author Timothy Shaw
 * 
 */
public class DRPPM_Backup {

	public static void main(String[] args) {
		try {

			if (args.length <= 0) {
				System.out.println("Not enough argument");
				printProgramInfo();
				System.exit(0);
			}

			String type = args[0];
			if (type.equals("-LIMMA1")) {
				// System.out.println("Single Group Differential Expression");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(CalculateLIMMA
							.parameter_info_compare_two_group());
					// System.out
					// .println("drppm -LIMMA1 [input] [groupFile] [OutputUpReg] [OutputDownReg] [OutputAll] [FilterType: ALL,PVALUE,FOLDCHANGE,BOTH] [TAKELOG]");
					System.exit(0);
				}
				CalculateLIMMA.CompareOneGroup(args_remain);
			} else if (type.equals("-LIMMA2")) {
				// System.out.println("Double Group Differential Expression");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(CalculateLIMMA
							.parameter_info_compare_two_group());
					// System.out
					// .println("drppm -LIMMA2 [input] [groupFile1] [groupFile2] [OutputUpReg] [OutputDownReg] [OutputAll] [FilterType: ALL,PVALUE,FOLDCHANGE,BOTH] [TAKELOG]");
					System.exit(0);
				}
				CalculateLIMMA.CompareTwoGroup(args_remain);
			} else if (type.equals("-TTEST")) {
				// System.out.println("Double Group Differential Expression");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					// System.out
					// .println("drppm -TTEST [input] [groupFile1] [groupFile2] [OutputUpReg] [OutputDownReg] [OutputAll] [FilterType: ALL,PVALUE,FOLDCHANGE,BOTH] [TAKELOG]");
					System.out.println(CalculateTTest.parameter_info());
					System.exit(0);
				}
				CalculateTTest.execute(args_remain);

				// CalculateTTest
			} else if (type.equals("-CalculateTTest")) {
				// System.out.println("Double Group Differential Expression");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					// System.out
					// .println("drppm -TTEST [input] [groupFile1] [groupFile2] [OutputUpReg] [OutputDownReg] [OutputAll] [FilterType: ALL,PVALUE,FOLDCHANGE,BOTH] [TAKELOG]");
					System.out.println(CalculateTTest.parameter_info());
					System.exit(0);
				}
				CalculateTTest.execute(args_remain);

				//
			} else if (type.equals("-CalculateCumulativeProb")) {
				// System.out.println("Double Group Differential Expression");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					// System.out
					// .println("drppm -TTEST [input] [groupFile1] [groupFile2] [OutputUpReg] [OutputDownReg] [OutputAll] [FilterType: ALL,PVALUE,FOLDCHANGE,BOTH] [TAKELOG]");
					System.out
							.println(CalculateCumulativeProb.parameter_info());
					System.exit(0);
				}
				CalculateCumulativeProb.execute(args_remain);

				//
			} else if (type.equals("-LIMMA2Flex")) {
				// System.out.println("Double Group Differential Expression");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(CalculateLIMMA
							.parameter_info_compare_two_group_flex());
					// System.out
					// .println("drppm -LIMMA2Flex [input] [groupFile1] [groupFile2] [OutputUpReg] [OutputDownReg] [OutputAll] [Pvalue] [FoldChange] [TAKELOG]");
					System.exit(0);
				}
				CalculateLIMMA.CompareTwoGroupFlex(args_remain);
				//
			} else if (type.equals("-LIMMA3Flex")) {
				// System.out.println("Double Group Differential Expression");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(CalculateLIMMA
							.parameter_info_compare_three_group_flex());
					// System.out
					// .println("drppm -LIMMA2Flex [input] [groupFile1] [groupFile2] [OutputUpReg] [OutputDownReg] [OutputAll] [Pvalue] [FoldChange] [TAKELOG]");
					System.exit(0);
				}
				CalculateLIMMA.CompareThreeGroupFlex(args_remain);
				// CompareThreeGroupFlex
			} else if (type.equals("-plotKinase")) {
				// System.out.println("Running Isotope Calculator");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(KinaseSubstratePainter.parameter_info());
					System.exit(0);
				}
				KinaseSubstratePainter.execute(args_remain);
			} else if (type.equals("-KinaseSubstratePainter")) {
				// System.out.println("Running Isotope Calculator");
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(KinaseSubstratePainter.parameter_info());
					System.exit(0);
				}
				KinaseSubstratePainter.execute(args_remain);
			} else if (type.equals("-plotHeatMap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -plotHeatMap [InputFile] [sampleNameFile] [GeneSetFile] [OutputPngFile]");
					System.exit(0);
				}
				HeatmapGeneration.execute(args_remain);
				// executePHeat
			} else if (type.equals("-plotPHeatMap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -plotPHeatMap "
							+ HeatmapGeneration.PHeat_Parameter_Info());
					System.exit(0);
				}
				HeatmapGeneration.executePHeat(args_remain);
				//
			} else if (type.equals("-plotMADHeatMap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -plotMADHeatMap [InputFile] [TopN] [SampleNameFile] [OutputPngFile]");
					System.exit(0);
				}
				HeatmapGeneration.executeMAD(args_remain);
			} else if (type.equals("-plotMADHeatMapCOL")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -plotMADHeatMapCOL [InputFile] [TopN] [SampleNameFile] [metaInfo: ATMTDP1,red:Pot,blue:Lig4,green:BRCA2,green:NA,purple] [OutputPngFile]");
					System.exit(0);
				}
				HeatmapGeneration.executeMADCOL(args_remain);
				// executePHeatMAD
			} else if (type.equals("-plotMADPHeatMap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -plotMADPHeatMap [InputFile] [TopN] [SampleNameFile] [OutputPngFile]");
					System.exit(0);
				}
				HeatmapGeneration.executePHeatMAD(args_remain);
				//
			} else if (type.equals("-removeNonCoding")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -removeNonCoding "
							+ RemoveNoncodingRNA.parameter_info());
					System.exit(0);
				}
				RemoveNoncodingRNA.execute(args_remain);
				// KeepProteinCodingGenes
			} else if (type.equals("-KeepProteinCodingGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -KeepProteinCodingGenes "
							+ KeepProteinCodingGenes.parameter_info());
					System.exit(0);
				}
				KeepProteinCodingGenes.execute(args_remain);
				//
			} else if (type.equals("-ExpressionNormalization")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExpressionNormalization [InputFile] [outputFile]");
					System.exit(0);
				}
				ExpressionNormalization.execute(args_remain);
				//
			} else if (type.equals("-mouse2human")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -mouse2human [inputFile] [hs2mmFile]");
					System.exit(0);
				}
				HumanMouseGeneNameConversion.executeMouse2Human(args_remain);
				// executeMouse2HumanCapitalize
			} else if (type.equals("-mouse2humanCapitalizeIfNotFound")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -mouse2human [inputFile] [hs2mmFile]");
					System.exit(0);
				}
				HumanMouseGeneNameConversion
						.executeMouse2HumanCapitalize(args_remain);
				//
			} else if (type.equals("-mouse2humanMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -mouse2humanMatrix [inputFile] [hs2mmFile]");
					System.exit(0);
				}
				HumanMouseGeneNameConversion
						.executeMouse2HumanMatrix(args_remain);
			} else if (type.equals("-human2mouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -human2mouse [inputFile] [hs2mmFile]");
					System.exit(0);
				}
				HumanMouseGeneNameConversion.executeHuman2Mouse(args_remain);
			} else if (type.equals("-human2mouseMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -human2mouseMatrix [inputFile] [hs2mmFile]");
					System.exit(0);
				}
				HumanMouseGeneNameConversion
						.executeHuman2MouseMatrix(args_remain);
				// AppendHuman2Mouse
			} else if (type.equals("-AppendHuman2Mouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendHuman2Mouse "
							+ AppendHuman2Mouse.parameter_info());
					System.exit(0);
				}
				AppendHuman2Mouse.execute(args_remain);
				// AppendMouse2Human
			} else if (type.equals("-AppendMouse2Human")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendMouse2Human "
							+ AppendMouse2Human.parameter_info());
					System.exit(0);
				}
				AppendMouse2Human.execute(args_remain);
				//
			} else if (type.equals("-MergeIntronRetentionTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeIntronRetentionTable "
							+ MergeIntronRetentionTable.parameter_info());
					System.exit(0);
				}
				MergeIntronRetentionTable.execute(args_remain);
				//
			} else if (type.equals("-DEAddAnnotation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -DEAddAnnotation "
							+ DEAddAnnotation.parameter_info());
					System.exit(0);
				}
				DEAddAnnotation.execute(args_remain);
				// DEAddAnnotationRelaxed
			} else if (type.equals("-DEAddAnnotationRelaxed")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -DEAddAnnotationRelaxed "
							+ DEAddAnnotationRelaxed.parameter_info());
					System.exit(0);
				}
				DEAddAnnotationRelaxed.execute(args_remain);
				// 
			} else if (type.equals("-AddAnnotationGeneral")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AddAnnotationGeneral [InputFile] [GeneSetInfo] [columnIndex]");
					System.exit(0);
				}
				AddAnnotationGeneral.execute(args_remain);
				//
			} else if (type.equals("-ExtractDifferentiatedTranscriptOnly")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractDifferentiatedTranscriptOnly [geneInputFile] [transcriptInputFile] [outputFile]");
					System.exit(0);
				}
				ExtractDifferentiatedTranscriptOnly.execute(args_remain);
			} else if (type.equals("-GEFisher")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GEFisher [InputFile] [GeneSetInfo] > [FisherExactTestFileOutput]");
					System.exit(0);
				}
				AddAnnotation2DiffFisher.execute(args_remain);
			} else if (type.equals("-PCAScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PCAScript [InputFile] [OutputPCAInfo]");
					System.exit(0);
				}
				PCAPlot.executePCA(args_remain);

			} else if (type.equals("-PlotPCA")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PlotPCA [InputFile] [ColorFile]");
					System.exit(0);
				}
				PCAPlot.executePlotPCA(args_remain);

			} else if (type.equals("-FilterColumn")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FilterColumn [InputFile] [FilterFile]");
					System.exit(0);
				}
				FilterColumns.execute(args_remain);
			} else if (type.equals("-FilterColumnName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FilterColumnName [InputFile] [FilterFile] [outputFile]");
					System.exit(0);
				}
				FilterColumnName.execute(args_remain);
			} else if (type.equals("-CollapseExpr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GrabColumnName [InputFile] [OutputFile]");
					System.out
							.println("Example: drppm -GrabColumnName drppm -GrabColumnName 2014-12-04_mckinnon_shimada_expr_collapse.txt 2014-12-04_mckinnon_shimada_sampleNames.txt");
					System.exit(0);
				}
				MergeGeneName.execute(args_remain);
				// MergeGeneNameClean
			} else if (type.equals("-MergeGeneNameClean")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeGeneNameClean "
							+ MergeGeneNameClean.parameter_info()); // [InputFile]
					// [MEDIAN or
					// AVERAGE][OutputFile]");
					System.exit(0);
				}
				MergeGeneNameClean.execute(args_remain);
			} else if (type.equals("-MergeGeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeGeneName "
							+ MergeGeneName.parameter_info()); // [InputFile]
																// [MEDIAN or
																// AVERAGE][OutputFile]");
					System.exit(0);
				}
				MergeGeneName.execute(args_remain);
			} else if (type.equals("-GrabColumnName")) {
				String[] args_remain = getRemaining(args);

				if (args_remain.length == 0) {
					System.out
							.println("drppm -GrabColumnName [InputFile] [OutputFile]");
					System.out
							.println("Example: drppm -GrabColumnName 2014-12-04_mckinnon_shimada_expr_collapse.txt 2014-12-04_mckinnon_shimada_sampleNames.txt");
					System.exit(0);
				}
				GrabColumnName.execute(args_remain);
				//
			} else if (type.equals("-GrabRowName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabRowName "
							+ GrabRowName.parameter_info()); // /nfs_exports/genomes/1/projects/RNASEQ/Baker/BucketRaw/QC
																// output.table.txt
																// exon.txt
																// intron.txt");
					System.exit(0);
				}
				GrabRowName.execute(args_remain);
				// EXONCAPStatsReport
			} else if (type.equals("-ExpandGeneNames")) {
				String[] args_remain = getRemaining(args);
				ExpandGeneNames.execute(args_remain);
			} else if (type.equals("-GenerateHistogram")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(SampleExprHistogram.parameter_info());
					System.exit(0);
				}
				SampleExprHistogram.execute(args_remain);
			} else if (type.equals("-SampleExprHistogram")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(SampleExprHistogram.parameter_info());
					System.exit(0);
				}
				SampleExprHistogram.execute(args_remain);
			} else if (type.equals("-GenerateVolcano")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateVolcano [InputLIMMAoutputFile] [OutputPNG] [pvalue] [logFC] [annotation tag can leave blank]");
					System.out
							.println("Example: drppm -GenerateVolcano APCvsWTC_ALL_GENESET.txt APCvsWTC_ALL_Volcano_meta.png 0.05 0.5 METAINFOHIT > APCvsWTC_ALL_Volcano_META.r");
					System.exit(0);
				}
				VolcanoPlot.execute(args_remain);
				// ExonJunctionMatrix
			} else if (type.equals("-ExonJunctionMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExonJunctionMatrix [inputFile] [outputFile]");
					// System.out.println("Example: drppm -ExonJunctionMatrix APCvsWTC_ALL_GENESET.txt APCvsWTC_ALL_Volcano_meta.png 0.05 0.5 METAINFOHIT > APCvsWTC_ALL_Volcano_META.r");
					System.exit(0);
				}
				ExonJunctionMatrix.execute(args_remain);
				// NormalizeJunctionCount
			} else if (type.equals("-NormalizeJunctionCount")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizeJunctionCount "
							+ NormalizeJunctionCount.parameter_info());

					System.exit(0);
				}
				NormalizeJunctionCount.execute(args_remain);
				//
			} else if (type.equals("-BoxplotFPKMOfGeneKO")) {
				String[] args_remain = getRemaining(args);
				FPKMBoxPlotOfGeneKO.execute(args_remain);
				// FPKMBoxPlotOfGeneKOSampleSpecific
			} else if (type.equals("-BoxplotFPKMOfGeneKOFilter")) {
				String[] args_remain = getRemaining(args);
				FPKMBoxPlotOfGeneKOSampleSpecific.execute(args_remain);
				// FPKMBoxPlotOfGeneKOSampleSpecific
			} else if (type.equals("-RenameFilesForBoxPlot")) {
				String[] args_remain = getRemaining(args);
				RenameSampleForBoxPlot.execute(args_remain);
				;
			} else if (type.equals("-ExtractFPKM")) {
				String[] args_remain = getRemaining(args);
				// grab cufflinks expression
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractFPKM /nfs_exports/genomes/1/projects/RNASEQ/Baker/BucketRaw/QC output.table.txt exon.txt intron.txt");
					System.exit(0);
				}
				ExtractFPKM.execute(args_remain);
				;
				//
			} else if (type.equals("-ExtractCufflinksFPKM")) {
				String[] args_remain = getRemaining(args);
				// grab cufflinks expression
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractCufflinksFPKM [outputFile] [indexNumber] [file1] [file2] [fileN] ...");
					System.exit(0);
				}
				ExtractFPKM.execute(args_remain);
				;

			} else if (type.equals("-GenerateCuffDiffScript")) {
				String[] args_remain = getRemaining(args);
				// grab cufflinks expression
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateCuffDiffScript [labels] [outputFolder] [GTFReference] [num_threads] [bamLstfile1] [bamLstfile2]");
					System.exit(0);
				}
				GenerateCuffDiffScript.execute(args_remain);

			} else if (type.equals("-GenePeptideCoverage")) {
				String[] args_remain = getRemaining(args);
				PeptideCoverageSingleGeneComparison.execute(args_remain);
				;

			} else if (type.equals("-PeptideCategoryMouseHuman")) {
				String[] args_remain = getRemaining(args);
				PeptideCategoriesSharedOrUnique.execute(args_remain);
				;

			} else if (type.equals("-PeptideCategoryMouseHumanIDmod")) {
				String[] args_remain = getRemaining(args);
				PeptideCategoriesSharedOrUniqIDmod.execute(args_remain);
				;

			} else if (type.equals("-PeptideIntensityBarplot")) {
				String[] args_remain = getRemaining(args);
				GenerateBarPlot.execute(args_remain);
				;

			} else if (type.equals("-PeptideCoverage")) {
				String[] args_remain = getRemaining(args);
				PeptideCoveragePlot.execute(args_remain);
				;

			} else if (type.equals("-PhosphoBasicStats")) {
				String[] args_remain = getRemaining(args);
				PhosphoBasicStats.execute(args_remain);
				;

			} else if (type.equals("-PhosphoAllMotifMatch")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PhosphoAllMotifMatch [all_motif.txt] [fasta file] [flag for complete match yes/no] [outputFile]");
					System.exit(0);
				}
				PhosphoMotifMatching.execute(args_remain);
				;
				// AppendPSSMResult2HPRD
			} else if (type.equals("-AppendPSSMResult2HPRD")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendPSSMResult2HPRD [CeasarKinaseMotifFile] [HPRD File] [outputFile]");
					System.exit(0);
				}
				AppendPSSMResult2HPRD.execute(args_remain);
				;
				//
			} else if (type.equals("-RNASEQMappingStatistics")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RNASEQMappingStatistics "
							+ RNASEQStatsReport.parameter_info()); // /nfs_exports/genomes/1/projects/RNASEQ/Baker/BucketRaw/QC
																	// output.table.txt
																	// exon.txt
																	// intron.txt");
					System.exit(0);
				}
				RNASEQStatsReport.execute(args_remain);
				// SummarizeFlagStat
			} else if (type.equals("-SummarizeFlagStat")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeFlagStat "
							+ SummarizeFlagStat.parameter_info()); // /nfs_exports/genomes/1/projects/RNASEQ/Baker/BucketRaw/QC
																	// output.table.txt
																	// exon.txt
																	// intron.txt");
					System.exit(0);
				}
				SummarizeFlagStat.execute(args_remain);
				//
			} else if (type.equals("-EXONCAPStatsReport")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EXONCAPStatsReport "
							+ EXONCAPStatsReport.parameter_info());
					System.exit(0);
				}
				EXONCAPStatsReport.execute(args_remain);
				//
			} else if (type.equals("-MappingStatistics")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MappingStatistics /nfs_exports/genomes/1/projects/RNASEQ/Baker/BucketRaw/QC output.table.txt exon.txt intron.txt");
					System.exit(0);
				}
				RNASEQStatsReport.execute(args_remain);

			} else if (type.equals("-AddGeneKO2SampleName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AddGeneKO2SampleName [input] [metafile] [outputfile] [yes]");
					System.exit(0);
				}
				AddGeneKO2Sample.execute(args_remain);
				;

			} else if (type.equals("-GrabKeyword")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GrabKeyword [input] [terms separated by comma]");
					System.out.println("The list of terms");
					System.exit(0);
				}
				GrabSampleNameWithKeyword.execute(args_remain);
				; // GrabSampleNameWithKeyword
			} else if (type.equals("-GrabSampleNameWithKeyword")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabSampleNameWithKeyword "
							+ GrabSampleNameWithKeyword.parameter_info());
					// System.out.println("The list of terms");
					System.exit(0);
				}
				GrabSampleNameWithKeyword.execute(args_remain);
				; // GrabSampleNameWithoutKeyword
			} else if (type.equals("-GrabSampleNameWithoutKeyword")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabSampleNameWithoutKeyword "
							+ GrabSampleNameWithoutKeyword.parameter_info());
					// System.out.println("The list of terms");
					System.exit(0);
				}
				GrabSampleNameWithoutKeyword.execute(args_remain);
				; // GrabSampleNameWithoutKeyword
			} else if (type.equals("-SampleFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SampleFilter [input] [terms separated by comma] [outputFile] [yes/no contains keyword?]");
					System.out.println("The list of terms");
					System.exit(0);
				}
				SampleFilter.execute(args_remain);
				;

			} else if (type.equals("-FilterSample")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FilterSample [input] [terms separated by comma] [outputFile] [yes/no contains keyword?]");
					System.out.println("The list of terms");
					System.exit(0);
				}
				SampleFilter.execute(args_remain);
				;

			} else if (type.equals("-GSEAgmt2txt")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GSEAgmt2txt "
							+ ConvertGSEAList2AnnotationFile.parameter_info());
					System.out
							.println("This will automatically generate txt files for each line");
					System.exit(0);
				}
				ConvertGSEAList2AnnotationFile.execute(args_remain);
				;

			} else if (type.equals("-ConvertGSEAList2AnnotationFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ConvertGSEAList2AnnotationFile "
							+ ConvertGSEAList2AnnotationFile.parameter_info());
					System.exit(0);
				}
				ConvertGSEAList2AnnotationFile.execute(args_remain);
				//
			} else if (type.equals("-Ascore2Fasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -Ascore2Fasta [PEPTIDE_MOD_FILE] [JUMPQ_NAME_INDEX] [JUMPQ_PEPTIDE_INDEX] [JUMPQ_PVALUE_INDEX] [JUMPQ_PVALUE_CUTOFF] [JUMPQ_LOGFOLDCHANGE_INDEX] [JUMPQ_LOGFOLDCHANGE_CUTOFF] [JUMPQ_FASTA_UP] [JUMPQ_FASTA_DN] [JUMPQ_FASTA_ALL]");
					System.out.println("");
					System.exit(0);
				}
				Ascore2FastaFile.execute(args_remain);
				;

			} else if (type.equals("-Ascore2FastaFileJUMP")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -Ascore2FastaFileJUMP [PEPTIDE_MOD_FILE] [JUMPQ_NAME_INDEX] [JUMPQ_PEPTIDE_INDEX] [JUMPQ_GROUP_INDEX] [JUMPQ_PVALUE_CUTOFF] [JUMPQ_LOGFOLDCHANGE_CUTOFF] [JUMPQ_FASTA_UP] [JUMPQ_FASTA_DN] [JUMPQ_FASTA_ALL]");
					System.out.println("");
					System.exit(0);
				}
				Ascore2FastaFileJUMP.execute(args_remain);
				;

			} else if (type.equals("-PepMod2FastaFileJUMP")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PepMod2FastaFileJUMP [PEPTIDE_MOD_FILE] [JUMPQ_NAME_INDEX] [JUMPQ_PEPTIDE_INDEX] [JUMPQ_GROUP_INDEX] [JUMPQ_PVALUE_CUTOFF] [JUMPQ_LOGFOLDCHANGE_CUTOFF] [JUMPQ_FASTA_UP] [JUMPQ_FASTA_DN] [JUMPQ_FASTA_ALL]");
					System.out.println("");
					System.exit(0);
				}
				Ascore2FastaFileJUMP.execute(args_remain);
				;

			} else if (type.equals("-CalcMotifEnrichment")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalcMotifEnrichment [AllPhosphoFasta] [AllPhosphoMotifFile] [diffFasta] [DiffMotifFile] [AllMotifFile] [Option: Group/Family] [outputFile]");
					System.out.println("");
					System.exit(0);
				}
				CalcMotifEnrichment.execute(args_remain);

			} else if (type.equals("-ParseMotifX")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ParseMotifX [inputFileList] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				ParseMotifXOutput.execute(args_remain);

			} else if (type.equals("-MatchMotifX")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MatchMotifX [inputMotifAll] [ParseMotifXOutputFile] [flag for complete match yes/no]");
					System.out.println("");
					System.exit(0);
				}
				MotifXMatchMotif.execute(args_remain);
				;
				// MotifXSummaryTable
			} else if (type.equals("-MotifXSummary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MotifXSummary [inputMotifXMatch]");
					System.out.println("");
					System.exit(0);
				}
				MotifXSummaryTable.execute(args_remain);
				;
				// MotifXSummaryTable
			} else if (type.equals("-ExtendPeptideTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtendPeptideWithFasta [fastaDatabaseInput] [tableInput] [OutputFasta] [extension length(bp)]");
					System.out.println("");
					System.exit(0);
				}
				ExtendPeptide2Table.execute(args_remain);
				;
			} else if (type.equals("-ExtendPeptideFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtendPeptideWithFasta [fastaDatabaseInput] [fastaInput] [OutputFasta] [extension length(bp)]");
					System.out.println("");
					System.exit(0);
				}
				ExtendPeptide2Fasta.execute(args_remain);
				;
				//
			} else if (type.equals("-AllMotifPValueFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AllMotifPValueFastaFile [allmotifFile] [fastaFile] [OutputCount]");
					System.out.println("");
					System.exit(0);
				}
				CalculateAllMotifPValueFastaFile.execute(args_remain);
				;

			} else if (type.equals("-AppendPhosphositeKinase")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendPhosphositeKinase [phosphosite] [AllMotifMappingResult] [organism] [OutputCount] [buffer length]");
					System.out.println("");
					System.exit(0);
				}
				AddKinaseBasedOnPhosphosite.execute(args_remain);
				;
				// AddRelativeQuantification
			} else if (type.equals("-AppendQuantification")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendQuantification [originalFile] [AScoreFile] [totalProteome] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantification.execute(args_remain);
				;
				// AddRelativeQuantificationJUMP
				// PhosphoKinaseCorrelationDistribution
			} else if (type.equals("-AddRelativeQuantificationJUMP")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AddRelativeQuantificationJUMP [originalFile] [AScoreFile] [totalProteome] [OutputFile] [useTotal 1Proteome, 2Whole, 3,or 4] [special_case]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantificationJUMP.execute(args_remain);
				;

				// PhosphoKinaseCorrelationDistribution
			} else if (type.equals("-AppendPredictedQuantification")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendPredictedQuantification [originalFile] [AScoreFile] [totalProteome] [kinase_name] [motifName] [groupInfo] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantificationForPredicted.execute(args_remain);
				;
			} else if (type.equals("-AppendPredictedQuantificationAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendPredictedQuantificationAll [originalFile] [AScoreFile] [totalProteome] [MotifAllFile] [groupInfo] [OutputFolder]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantificationForPredictedAll.execute(args_remain);
				;
				// AddRelativeQuantificationForPredictedAllJUMP
			} else if (type
					.equals("-AddRelativeQuantificationForPredictedAllJUMP")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AddRelativeQuantificationForPredictedAllJUMP [originalFile] [AScoreFile] [totalProteome] [MotifAllFile] [groupInfo] [OutputFolder]");
					System.out.println("");
					System.exit(0);
				}
				AddRelativeQuantificationForPredictedAllJUMP
						.execute(args_remain);
				;
				//
			} else if (type.equals("-PhoshKSCorrel")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PhoshKSCorrel [InputFile] [GeneName] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				PhosphoKinaseCorrelationDistribution.execute(args_remain);
			} else if (type.equals("-PhoshKSCorrelAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PhoshKSCorrelAll [InputFile] [GeneNameFile] [Col of PeptideName] [Col of Peptide] [OutputFolder]");
					System.out.println("");
					System.exit(0);
				}
				PhosphoKinaseCorrelationDistributionAll.execute(args_remain);
			} else if (type.equals("-PhoshKSRandomCorrel")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PhoshKSRandomCorrel [AscoreFile] [TotalFile] [GeneName] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				PhosphoKinaseBackgroundRandom.execute(args_remain);
				;

			} else if (type.equals("-GenerateTrypticFasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateTrypticFasta [Input] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				GenerateFastaFileFromTrypticTxt.execute(args_remain);

			} else if (type.equals("-Col2Fasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -Col2Fasta [Input] [Col_number] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}
				GrabColConvert2Fasta.execute(args_remain);
				;

			} else if (type.equals("-IDMatching")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -IDMatching [uniprotIDMappingFile] [proteinFile] [rnaseqFile] [gtfFile] [outputFile]");
					System.out.println("");
					System.exit(0);
				}

				GenerateConversionTable.execute(args_remain);
				;
				// GenerateConversionTable
			} else if (type.equals("-CombineFPKMExpression")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CombineFPKMExpression [FPKMExpression1File] [FPKMExpression2File] [outputFile]");
					System.out.println("");
					System.exit(0);
				}

				CombineFPKMFiles.execute(args_remain);
				;

			} else if (type.equals("-CombineFPKMFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CombineFPKMFiles [FPKMExpression1File] [FPKMExpression2File] [outputFile]");
					System.out.println("");
					System.exit(0);
				}

				CombineFPKMFiles.execute(args_remain);

			} else if (type.equals("-SingleScatterPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SingleScatterPlot [InputFile] [OutputPNG]");
					System.out.println("");
					System.exit(0);
				}

				SingleScatterPlot.execute(args_remain);
				;
				// GenerateKSScatterPlots
			} else if (type.equals("-GenerateKSScatterPlots")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateKSScatterPlots [InputFile] [KinaseName] [OutputPNG]");
					System.out.println("");
					System.exit(0);
				}

				GenerateKSScatterPlots.execute(args_remain);
				;
				// GenerateKSScatterPlots
			} else if (type.equals("-GenerateGPSFasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateGPSFasta [InputFastaFile] [OutputFastaFile]");
					System.out.println("");
					System.exit(0);
				}

				CreatePhosphoGPSFastaFile.execute(args_remain);
				;
				// AppendOriginalPeptideInformation
			} else if (type.equals("-AppendOrigPeptideInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendOrigPeptideInfo [InputMotifFile] [AscoreFile] [IDsumFile] [OuputMotifFile]");
					System.out.println("");
					System.exit(0);
				}

				AppendOriginalPeptideInformation.execute(args_remain);
				;
				// AppendOriginalPeptideInformation
			} else if (type.equals("-AppendOrigPeptideInfoPSTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendOrigPeptideInfoPSTable [InputPSTableFile] [AscoreFile] [IDsumFile] [OuputMotifFile]");
					System.out.println("");
					System.exit(0);
				}

				AppendOriginalPeptideInformation2Table.execute(args_remain);
				;
				// AppendOriginalPeptideInformation
			} else if (type.equals("-AppendMoreInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendMoreInfo [InputPSTableFile] [AscoreFile] [IDsumFile] [fastaUp] [fastaDN] [motifInfo] [OuputMotifFile]");
					System.out.println("");
					System.exit(0);
				}

				AppendMoreInformationTogether.execute(args_remain);
				;
				// AddScanCountInfo
			} else if (type.equals("-AddScanCountInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -AddScanCountInfo [InputFile] [SubIDSumFile] [TotalFile] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}

				AddScanCountInfo.execute(args_remain);
				;
				// CalculateKinase2KinaseCorrelation
			} else if (type.equals("-FilterPutativeKinase")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					/*
					 * String fileName = args[0]; //Hong_Final_List.txt String
					 * correlFile = args[1]; String motifFile = args[2]; double
					 * cutoff = new Double(args[3]); String outputFile =
					 * args[4];
					 */
					System.out
							.println("drppm -FilterPutativeKinase [InputFile] [correlFile] [motifFile] [cutoff 0.0-1.0] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}

				FilterPutativeKinase.execute(args_remain);
				;
				// CalculateKinase2KinaseCorrelation
			} else if (type.equals("-CalculateKinase2KinaseCorrelation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateKinase2KinaseCorrelation [kinaseFile] [KinaseName] [TotalFile] [grouping] [OutputFile]");
					System.out.println("");
					System.exit(0);
				}

				CalculateKinase2KinaseCorrelation.execute(args_remain);
				;
				// CalculateKinase2KinaseCorrelation
			} else if (type.equals("-ReorderSamples")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ReorderSamples [inputFPKMFile] [SampleOrder] [outputFPKMFile]");
					System.out.println("");
					System.exit(0);
				}
				ReorderSamples.execute(args_remain);
				;
				// ReorderSampleFast
			} else if (type.equals("-ReorderSampleFast")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -ReorderSampleFast "
							+ ReorderSampleFast.parameter_info());
					System.out.println(ReorderSampleFast.description());
					System.out.println("");
					System.exit(0);
				}
				ReorderSampleFast.execute(args_remain);
				;
				//
			} else if (type.equals("-PhosphoDataMatrixAndHeatmap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -PhosphoDataMatrixAndHeatmap "
							+ PhosphoDataMatrixAndHeatmap.parameter_info());
					System.out.println("");
					System.exit(0);
				}
				PhosphoDataMatrixAndHeatmap.execute(args_remain);
				;
				// JUMPqDataMatrixGeneration
			} else if (type.equals("-JUMPqDataMatrixGeneration")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -JUMPqDataMatrixGeneration "
							+ JUMPqDataMatrixGeneration.parameter_info());
					System.out.println("");
					System.exit(0);
				}
				JUMPqDataMatrixGeneration.execute(args_remain);
				;
				// JUMPqDataMatrixGenerationAll
			} else if (type.equals("-JUMPqDataMatrixGenerationAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -JUMPqDataMatrixGenerationAll "
							+ JUMPqDataMatrixGenerationAll.parameter_info());
					System.out.println("");
					System.exit(0);
				}
				JUMPqDataMatrixGenerationAll.execute(args_remain);
				;
				//
			} else if (type.equals("-CalculateRank")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateRank [inputFPKMFile]");
					System.out.println("");
					System.exit(0);
				}
				CalculateRank.execute(args_remain);
				;
				//
			} else if (type.equals("-GSEAHeatmap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GSEAHeatmap [inputFile] [outputFile]");
					System.out.println("");
					System.exit(0);
				}
				GSEAHeatmap.execute(args_remain);
				;
				// CalculateKinase2KinaseCorrelation
				//
			} else if (type.equals("-mouse2human2col")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -mouse2human2col [inputFile] [inputHuman2Mouse]");
					System.out.println("");
					System.exit(0);
				}
				HumanMouseGeneNameConversion.convertM2HTwoColumn(args_remain);
				;
				// CalculateKinase2KinaseCorrelation
				// mouse2human
				//
			} else if (type.equals("-ExtractSeqFromAln")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractSeqFromAln [inputFile] [organism] [outputFile]");
					System.out.println("");
					System.exit(0);
				}
				ExtractSequenceFromAlignment.execute(args_remain);
				;
				// CalculateKinase2KinaseCorrelation
				// mouse2human
				// ExtractSequenceFromAlignment
			} else if (type.equals("-Extract100Match")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractSeqFromAln [inputUNIPROT] [inputUCSC] [outputFOUND] [outputNOTFOUND]");
					System.out.println("");
					System.exit(0);
				}
				Extract100PercentMatch.execute(args_remain);
				;
				// CalculateKinase2KinaseCorrelation
				// mouse2human
				// ExtractSequenceFromAlignment
				// RescueFragments
			} else if (type.equals("-RescueFragments")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -RescueFragments [inputUNIPROT] [outputMatches] [outputFound] [outputNotFound]");
					System.out.println("");
					System.exit(0);
				}
				RescueFragments.execute(args_remain);
				;

			} else if (type.equals("-MakeFastaSingleLine")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MakeFastaSingleLine [inputFasta] [outputFasta]");
					System.out.println("");
					System.exit(0);
				}
				MakeFastaSingleLine.execute(args_remain);
				;
				// GeneratePredictedHistogramDistribution
			} else if (type.equals("-GeneratePredictedHistogram")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GeneratePredictedHistogram [motifAllFile] [outputFolder] [pngFolder]");
					System.out.println("");
					System.exit(0);
				}
				GeneratePredictedHistogramDistribution.execute(args_remain);
				;

			} else if (type.equals("-CreateNetworkDisplay")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CreateNetworkDisplay "
							+ CreateNetworkDisplay.parameter_info());
					System.out.println("");
					System.exit(0);
				}
				CreateNetworkDisplay.execute(args_remain);
				;
				// RunRScript
			} else if (type.equals("-CreateNetworkDisplayComplex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					/*
					 * System.out .println(
					 * "drppm -CreateNetworkDisplayComplex [NetworkTXT] [NetworkMetaData] [NetworkName] [NetworkType: CONCENTRIC/BREADTHFIRST] [fontSize] [outputFolder]"
					 * );
					 */
					System.out.println(CreateNetworkDisplayComplex
							.parameter_info());
					System.out.println("");
					System.exit(0);
				}
				CreateNetworkDisplayComplex.execute(args_remain);
				;
				// GenerateNodeMetaData
			} else if (type.equals("-GenerateNodeMetaData")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateNodeMetaData [inputFile]");
					System.out.println("");
					System.exit(0);
				}
				GenerateNodeMetaData.execute(args_remain);
				;
				// GenerateNodeMetaDataSize
			} else if (type.equals("-GenerateNodeMetaDataSize")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateNodeMetaDataSize "
							+ GenerateNodeMetaDataSize.parameter_info());
					System.out.println("");
					System.exit(0);
				}
				GenerateNodeMetaDataSize.execute(args_remain);
				;
				//
			} else if (type.equals("-GenerateEdgeMetaData")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateEdgeMetaData [inputFile]");
					System.out.println("");
					System.exit(0);
				}
				GenerateEdgeMetaData.execute(args_remain);
				;
				// GenerateMultipleCircles
			} else if (type.equals("-GenerateMultipleCircles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMultipleCircles "
							+ GenerateMultipleCircles.parameter_info());

					System.exit(0);
				}
				GenerateMultipleCircles.execute(args_remain);
				;
				// GenerateMultipleCirclesFlex
			} else if (type.equals("-GenerateMultipleCirclesFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMultipleCirclesFlex "
							+ GenerateMultipleCirclesFlex.parameter_info());
					System.exit(0);
				}
				GenerateMultipleCirclesFlex.execute(args_remain);
				;
				//
			} else if (type.equals("-GenerateMultipleCirclesLabels")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMultipleCirclesLabels "
							+ GenerateMultipleCirclesLabels.parameter_info());

					System.exit(0);
				}
				GenerateMultipleCirclesLabels.execute(args_remain);
				;
				//
			} else if (type.equals("-RunRScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RunRScript [RScript]");
					System.out.println("");
					System.exit(0);
				}
				RunRScript.execute(args_remain);
				;

			} else if (type.equals("-HumanCentricProteinAlignment")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -HumanCentricProteinAlignment [InputUniprotFasta] [outputHomolog] [alignment]");
					System.out.println("");
					System.exit(0);
				}
				HumanCentricProteinAlignment.execute(args_remain);
				;

			} else if (type.equals("-MouseCentricProteinAlignment")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MouseCentricProteinAlignment [InputUniprotFasta] [outputHomolog] [alignment]");
					System.out.println("");
					System.exit(0);
				}
				MouseCentricProteinAlignment.execute(args_remain);
				;

			} else if (type.equals("-Kin2SubConvert2Coordinate")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -Kin2SubConvert2Coordinate [alignment_file] [kinase_substrate] [output_kinase_substrate] [missed_kinase_substrate]");
					System.out.println("");
					System.exit(0);
				}
				Kin2SubConvert2Coordinate.execute(args_remain);
				;

			} else if (type.equals("-ConvertProteinCoord")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ConvertProteinCoord [alignment_file] [kinase_substrate] [output_kinase_substrate] [missed_kinase_substrate] [organism_index] [accession_index] [location_index]");
					System.out.println("");
					System.exit(0);
				}
				ConvertProteinCoord.execute(args_remain);
				;

			} else if (type.equals("-GSEAHuman2Mouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GSEAHuman2Mouse [mouse/human homology] [human gsea_gmt] [mouse gsea_gmt]");
					System.out.println("");
					System.exit(0);
				}
				ConvertGSEAHuman2Mouse.execute(args_remain);
				;

			} else if (type.equals("-ConvertGSEAHuman2Mouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println(ConvertGSEAHuman2Mouse.description());
					System.out.println("drppm -ConvertGSEAHuman2Mouse "
							+ ConvertGSEAHuman2Mouse.parameter_info());

					// " [mouse/human homology] [human gsea_gmt] [mouse gsea_gmt]");
					System.out.println("");
					System.exit(0);
				}
				ConvertGSEAHuman2Mouse.execute(args_remain);
				;

			} else if (type.equals("-Gene2TF")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -Gene2TF [TF file] [geneSet File] [outputFile]");
					System.out.println("");
					System.exit(0);
				}
				TFRegulatedGenes.execute(args_remain);
				;

			} else if (type.equals("-GEFisherFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GEFisherFilter [InputFile] [GeneSetInfo] [FilterFile]> [FisherExactTestFileOutput]");
					System.exit(0);
				}
				TFGeneEnrichmentFilter.execute(args_remain);

			} else if (type.equals("-CheckIfDiffExpr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CheckIfDiffExpr [InputDEFile] [FilterFile] > [DEGeneList]");
					System.exit(0);
				}
				CheckIfDifferentiallyExpressed.execute(args_remain);

			} else if (type.equals("-ROCCurve")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -ROCCurve [geneName] [kinase_substrate_file] [phospho_fasta_extended] [predicted_substrate_site] [kinase_motif_name]");
					System.exit(0);
				}
				GenerateROCCurvePerKinase.execute(args_remain);
				// GenerateROCCurveRandomRandom
			} else if (type.equals("-ROCCurveRandom")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -ROCCurve [geneName] [kinase_substrate_file] [phospho_fasta_extended] [predicted_substrate_site] [kinase_motif_name]");
					System.exit(0);
				}
				GenerateROCCurveRandomRandom.execute(args_remain);

			} else if (type.equals("-MATSScriptGen")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MATSScriptGen [inputFile1] [inputFile2] [gtfFile] [outputFile] [MATS python path nad pgoram]");
					System.exit(0);
				}
				MATSScriptGenerator.execute(args_remain);
				//
			} else if (type.equals("-AddGeneName2MATS")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AddGeneName2MATS [mats_output_path] [gtfFile]");
					System.exit(0);
				}
				AddGeneName2MATS.execute(args_remain);
				// OverlapAlternativeSplicingGeneList
			} else if (type.equals("-OverlapAlternativeSplicingGeneList")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -OverlapAlternativeSplicingGeneList [inputFile1] [inputFile2] [fdrCutoff] [incLevelRatio]");
					System.exit(0);
				}
				OverlapAlternativeSplicingGeneList.execute(args_remain);
				// FilterMATSResults
			} else if (type.equals("-FilterMATSResults")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FilterMATSResults " + FilterMATSResults.parameter_info());
					System.exit(0);
				}
				FilterMATSResults.execute(args_remain);
				// SummarizeResultsAfterFilter
			} else if (type.equals("-SummarizeResultsAfterMATSFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SummarizeResultsAfterMATSFilter "
									+ SummarizeResultsAfterMATSFilter
											.parameter_info());
					System.exit(0);
				}
				SummarizeResultsAfterMATSFilter.execute(args_remain);
				// SummarizeResultsAfterMATSFilterExpr
			} else if (type.equals("-SummarizeResultsAfterMATSFilterExpr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SummarizeResultsAfterMATSFilterExpr "
									+ SummarizeResultsAfterMATSFilterExpr
											.parameter_info());
					System.exit(0);
				}
				SummarizeResultsAfterMATSFilterExpr.execute(args_remain);
				// SummarizeResultsAfterMATSFilterDiffExpr
			} else if (type.equals("-SummarizeResultsAfterMATSFilterDiffExpr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SummarizeResultsAfterMATSFilterDiffExpr "
									+ SummarizeResultsAfterMATSFilterDiffExpr
											.parameter_info());
					System.exit(0);
				}
				SummarizeResultsAfterMATSFilterDiffExpr.execute(args_remain);
				//
			} else if (type.equals("-DetectIntronRetention")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					/*
					 * String inputFile1 = args[0]; String bedFile = args[1];
					 * String outputFile = args[2]; String tag = args[3];
					 */
					System.out
							.println("drppm -DetectIntronRetention [inputFile1] [bedFile] [outputTag]");
					System.exit(0);
				}
				DetectIntronRetention.execute(args_remain);
				// Bam2BW
			} else if (type.equals("-Bam2BW")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Bam2BW "
							+ Bam2BW.parameter_info());
					System.exit(0);
				}
				Bam2BW.execute(args_remain);
				// Bam2BW
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
					System.out
							.println("drppm -IntersectBed [bamlistFile] [intronBed] [exonBed] [geneBed]");
					System.exit(0);
				}
				IntersectBed.execute(args_remain);
			} else if (type.equals("-CountNumberOfUniqReads")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CountNumberOfUniqReads [bedFile] [countAllFlag]");
					System.exit(0);
				}
				CountNumberOfUniqReads.execute(args_remain);
				// CountNumberOfUniqReadsScript
			} else if (type.equals("-CountNumUniqReadsScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CountNumUniqReadsScript [bedFiles]");
					System.exit(0);
				}
				CountNumberOfUniqReadsScript.execute(args_remain);
				// CountNumberOfUniqReadsScript
			} else if (type.equals("-KeepPolyA")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -KeepPolyA [inputLst] [outputFq]");
					System.exit(0);
				}
				KeepPolyA.execute(args_remain);
				// ExtractPolyAReadsUsePolyALibrary
			} else if (type.equals("-ExtractPolyAReadsUsePolyALibrary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractPolyAReadsUsePolyALibrary [inputLst] [outputFq]");
					System.exit(0);
				}
				ExtractPolyAReadsUsePolyALibrary.execute(args_remain);
				// ExtractPolyAReadsYuxinScript
			} else if (type.equals("-ExtractPolyAReadsYuxinScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractPolyAReadsYuxinScript [inputLst] [outputFq] [perlScriptPath]");
					System.exit(0);
				}
				ExtractPolyAReadsYuxinScript.execute(args_remain);
				// ExtractPolyAReadsUsePolyALibrarySingleCell
			} else if (type
					.equals("-ExtractPolyAReadsUsePolyALibrarySingleCell")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractPolyAReadsUsePolyALibrarySingleCell "
									+ ExtractPolyAReadsUsePolyALibrarySingleCell
											.parameter_info());
					System.exit(0);
				}
				ExtractPolyAReadsUsePolyALibrarySingleCell.execute(args_remain);
				//
			} else if (type.equals("-CalculatePolyADistribution")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculatePolyADistribution [inputLst] [inputFile] [outputFq] [inputFile]");
					System.exit(0);
				}
				CalculatePolyADistribution.execute(args_remain);
				// GeneratePolyAHistogramOutput
			} else if (type.equals("-CalculatePolyADistributionMouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculatePolyADistributionMouse [inputLst] [inputFile] [outputFq] [inputFile]");
					System.exit(0);
				}
				CalculatePolyADistributionMouse.execute(args_remain);
				// GeneratePolyAHistogramOutput
			} else if (type.equals("-GeneratePolyAHistogramOutput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GeneratePolyAHistogramOutput [inputDistFile] [outputFile]");
					System.exit(0);
				}
				GeneratePolyAHistogramOutput.execute(args_remain);
				//
			} else if (type.equals("-GeneratePCPAMouseScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GeneratePCPAMouseScript [inputFile] [mouseFasta] [perlPath] [coverageBedPath] [mm9bowtieIndex]");
					System.exit(0);
				}

				GeneratePCPAMouseScript.execute(args_remain);
				// GeneratePCPAHumanScript
			} else if (type.equals("-GeneratePCPAHumanScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GeneratePCPAHumanScript [inputFile] [humanFasta] [perlPath] [coverageBedPath] [hg19bowtieIndex]");
					System.exit(0);
				}

				GeneratePCPAHumanScript.execute(args_remain);
				// GeneratePCPAHumanScriptComplete
			} else if (type.equals("-GeneratePCPAHumanScriptComplete")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GeneratePCPAHumanScriptComplete "
									+ GeneratePCPAHumanScriptComplete
											.parameter_info() + "\n");
					System.exit(0);
				}

				GeneratePCPAHumanScriptComplete.execute(args_remain);
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
					System.out
							.println("drppm -IntronMappingPercentageSummary [inputLst] [outputSummaryTable]");
					System.exit(0);
				}
				IntronMappingPercentageSummary.execute(args_remain);
				// IntronMappingPercentageSummary
			} else if (type.equals("-FilterBEDReads")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FilterBEDReads [inputLst] [lengthCutoff] [outputFile]");
					System.exit(0);
				}
				FilterBEDReads.execute(args_remain);
				// FilterBEDReadsScript
			} else if (type.equals("-FilterBEDReadsScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FilterBEDReadsScript [inputLst] [lengthCutoff]");
					System.exit(0);
				}
				FilterBEDReadsScript.execute(args_remain);
				// CalculateCoverageBed
			} else if (type.equals("-CalculateCoverageBed")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateCoverageBed [bamlistFile] [intronBed] [exonBed]");
					System.exit(0);
				}
				CalculateCoverageBed.execute(args_remain);
			} else if (type.equals("-CalculateSplicingDeficiency")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -CalculateSplicingDeficiency [intronBedFile] [exonBedFile] [intronCoverageBed] [exonCoverageBed] [outputFile]");
					System.exit(0);
				}
				CalculateSplicingDeficiency.execute(args_remain);
				// CalculateSplicingDeficiencyScript
			} else if (type.equals("-CalculateSplicingDeficiencyScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -CalculateSplicingDeficiencyScript [bamList] [intronBedFile] [exonBedFile]");
					System.exit(0);
				}
				CalculateSplicingDeficiencyScript.execute(args_remain);
				// IntronRetentionHistogramData
			} else if (type.equals("-IntronRetentionHistogramData")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -IntronRetentionHistogramData "
							+ IntronRetentionHistogramData.description());
					System.exit(0);
				}
				IntronRetentionHistogramData.execute(args_remain);
				// ConvertConversionAppend2PhosphositeFile
			} else if (type.equals("-ReplaceOrganismPhosphositeFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -ReplaceOrganismPhosphositeFile [orig_phosphosite_file] [additional phosphosite] [new phosphositeFile]");
					System.exit(0);
				}
				OrganismConversion2PhosphositeFile.execute(args_remain);
				// DownloadAllPossibleSiteInfo
			} else if (type.equals("-DownloadAllPossibleSiteInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -DownloadAllPossibleSiteInfo [Add something to run]");
					System.exit(0);
				}
				DownloadAllPossibleSiteInfo.execute(args_remain);
				// AddNetworkNeighborEvidence
			} else if (type.equals("-AddNetworkNeighborEvidence")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AddNetworkNeighborEvidence [Phosphosite_hrpd_motif_output_all_file] [humanInteractiveDBFile] [Phosphosite_kinase_substrate] [uniprot2geneIDFile] [motif_all_annotation_file] [outputFile]");
					System.exit(0);
				}
				AddNetworkNeighborEvidence.execute(args_remain);
				//
			} else if (type.equals("-GenerateIntronRetentionBarPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateIntronRetentionBarPlot [inputFile] [outputFile] [groupInfo]");
					System.exit(0);
				}
				GenerateIntronRetentionBarPlot.execute(args_remain);
				// GenerateOverlappingResults
			} else if (type.equals("-GenerateOverlappingResults")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -GenerateOverlappingResults [path1] [path2] [path3] [tag1] [tag2] [tag3] [pvalue] [outputFolder]");
					System.exit(0);
				}
				GenerateOverlappingResults.execute(args_remain);
				// RecurrentGeneMutFreq
			} else if (type.equals("-RecurrentGeneMutFreq")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -RecurrentGeneMutFreq [InputFileList] [SNVClassIndex]");
					System.exit(0);
				}
				RecurrentGeneMutFreq.execute(args_remain);
				// AddRecurrenceAnnotation
			} else if (type.equals("-AddRecurrenceAnnotation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -AddRecurrenceAnnotation [inputFile] [mutationFrequencyFile]");
					System.exit(0);
				}
				AddRecurrenceAnnotation.execute(args_remain);
				//
			} else if (type.equals("-MATSGenerateResultTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -MATSGenerateResultTable [inputFile] [mutationFrequencyFile]");
					System.exit(0);
				}
				MATSGenerateResultTable.execute(args_remain);

			} else if (type.equals("-GenerateComplexNetwork")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -GenerateComplexNetwork [complexFile] [uniprot2geneIDFile] [outputFile]");
					System.exit(0);
				}
				GenerateComplexNetwork.execute(args_remain);
				// AppendBamReviewFile
				// SnpDetectPostProcessingScript
			} else if (type.equals("-SnpDetectPostProcessingScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -SnpDetectPostProcessingScript [inputFile] [bamFiles] [snpDetectPath] [bamPath]");
					System.exit(0);
				}
				SnpDetectPostProcessingScript.execute(args_remain);
				//
				//
			} else if (type.equals("-AppendGermlineAlternativeAlleleCount")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -AppendGermlineAlternativeAlleleCount [Allele Matrix] [snvMutFile] [outputFile]");
					System.exit(0);
				}
				AppendGermlineAlternativeAlleleCount.execute(args_remain);

				//

			} else if (type.equals("-AppendBamReviewFile")) { // for whole exome
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -AppendBamReviewFile [fileFile] [bamPathIDFile] [bamFile] [organism]");
					System.exit(0);
				}
				//
				AppendBamReviewFile.execute(args_remain);
				//
			} else if (type.equals("-GenerateBamSoftLink")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -GenerateBamSoftLink [folderPath]");
					System.exit(0);
				}

				GenerateBamSoftLink.execute(args_remain);
				// AppendCICEROHTMLLink
			} else if (type.equals("-AppendCICEROHTMLLink")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -AppendCICEROHTMLLink [originalFile] [path] [organism]");
					System.exit(0);
				}

				AppendCICEROHTMLLink.execute(args_remain);
				// StructureFractionClustering
			} else if (type.equals("-StructureFractionClustering")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -StructureFractionClustering [folderPath] [outputFile]");
					System.exit(0);
				}
				StructureFractionClustering.execute(args_remain);
				// GrabEachFileInsertIDFasta
			} else if (type.equals("-GrabEachFileInsertIDFasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GrabEachFileInsertIDFasta [folderPath] [outputFile]");
					System.exit(0);
				}
				GrabEachFileInsertIDFasta.execute(args_remain);
				// ChemoProjectGenerateMetaFile
			} else if (type.equals("-ChemoProjectGenerateMetaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ChemoProjectGenerateMetaFile [folderPath] [outputFile]");
					System.exit(0);
				}
				ChemoProjectGenerateMetaFile.execute(args_remain);
				// GenerateQIIMEMatrixTable
			} else if (type.equals("-GenerateQIIMEMatrixTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateQIIMEMatrixTable [inputFile] [outputFile]");
					System.exit(0);
				}
				GenerateQIIMEMatrixTable.execute(args_remain);
				// GenerateJasonMatrixTable
			} else if (type.equals("-GenerateJasonMatrixTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateJasonMatrixTable [inputFile] [outputFile]");
					System.exit(0);
				}
				GenerateJasonMatrixTable.execute(args_remain);
				// CombineOTUCounts
			} else if (type.equals("-CombineOTUCounts")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CombineOTUCounts [inputFile] [outputFile]");
					System.exit(0);
				}
				CombineOTUCounts.execute(args_remain);
				// NormalizePerHundredKilo
			} else if (type.equals("-NormalizePerHundredKilo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -NormalizePerHundredKilo [inputFile] [outputFile]");
					System.exit(0);
				}
				NormalizePerHundredKilo.execute(args_remain);
				//
			} else if (type.equals("-PhosphoSummarizeResults")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PhosphoSummarizeResults [inputFile] [upregFile] [dnregFile] [outputFile]");
					System.exit(0);
				}
				/* PhosphoSummarizeKeepTopHit */
				PhosphoSummarizeResults.execute(args_remain);
				//
			} else if (type.equals("-PhosphoSummarizeKeepTopHit")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PhosphoSummarizeKeepTopHit [inputFile] [outputFile]");
					System.exit(0);
				}
				PhosphoSummarizeKeepTopHit.execute(args_remain);
				// CalculatePhosphoStatistics
			} else if (type.equals("-CalculatePhosphoStatistics")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculatePhosphoStatistics [phosphoJUMPq] [inputSummaryFile]");
					System.exit(0);
				}
				CalculatePhosphoStatistics.execute(args_remain);
				// BioPlex2HumanInteractome
			} else if (type.equals("-BioPlex2HumanInteractome")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -BioPlex2HumanInteractome [bioplexFormatFile] [outputFile]");
					System.exit(0);
				}
				BioPlex2HumanInteractome.execute(args_remain);
				//
			} else if (type.equals("-Test")) {
				String[] args_remain = getRemaining(args);
				/*
				 * if (args_remain.length == 0) {
				 * System.out.println("drppm -Test"); System.exit(0); }
				 */
				Test.execute(args_remain);
				// GenerateDTAFilesScript
			} else if (type.equals("-GenerateDTAFilesScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateDTAFilesScript");
					System.exit(0);
				}
				GenerateDTAFilesScript.execute(args_remain);
				// GenerateDTARawFilesScript
			} else if (type.equals("-GenerateDTARawFilesScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateDTARawFilesScript");
					System.exit(0);
				}
				GenerateDTARawFilesScript.execute(args_remain);

			} else if (type.equals("-GenerateDisplayIonHtmlImg")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -GenerateDisplayIonHtmlImg [inputFile] [ipaddress] [name_idx] [scan_idx] [charge_idx] [peptide_idx] [uniprot_idx] [path] [outputFolderImg] [outputFolderCSV] [outputFolderHTML]");
					System.exit(0);
				}
				GenerateDisplayIonHtmlImg.execute(args_remain);
				// GenerateDisplayIonHTMLImgSimple
			} else if (type.equals("-GenerateDisplayIonHTMLImgSimple")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -GenerateDisplayIonHTMLImgSimple "
									+ GenerateDisplayIonHTMLImgSimple
											.parameter_info());
					System.exit(0);
				}
				GenerateDisplayIonHTMLImgSimple.execute(args_remain);
				// GenerateDisplayIonReport
			} else if (type.equals("-GenerateDisplayIonReport")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -GenerateDisplayIonReport "
							+ GenerateDisplayIonReport.parameter_info());
					System.exit(0);
				}
				GenerateDisplayIonReport.execute(args_remain);
				//
			} else if (type.equals("-GrabPhosphositeExpression")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -GrabPhosphositeExpression "
							+ GrabPhosphositeExpression.parameter_info());
					System.exit(0);
				}
				GrabPhosphositeExpression.execute(args_remain);
				// GrabPhosphositeExpressionAll
			} else if (type.equals("-GrabPhosphositeExpressionAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -GrabPhosphositeExpressionAll "
							+ GrabPhosphositeExpressionAll.parameter_info());
					System.exit(0);
				}
				GrabPhosphositeExpressionAll.execute(args_remain);
				//
			} else if (type.equals("-PhosphoExpr2HeatmapFriendly")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -PhosphoExpr2HeatmapFriendly "
							+ PhosphoExpr2HeatmapFriendly.parameter_info());
					System.exit(0);
				}
				PhosphoExpr2HeatmapFriendly.execute(args_remain);
				// ConvertUniprot2GeneAndAppend
			} else if (type.equals("-ConvertUniprot2GeneAndAppend")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -ConvertUniprot2GeneAndAppend "
							+ ConvertUniprot2GeneAndAppend.parameter_info());
					System.exit(0);
				}
				ConvertUniprot2GeneAndAppend.execute(args_remain);
				// AppendProteinComplexInfo
			} else if (type.equals("-AppendProteinComplexInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -AppendProteinComplexInfo "
							+ AppendProteinComplexInfo.parameter_info());
					System.exit(0);
				}
				AppendProteinComplexInfo.execute(args_remain);
				// ExtractUniprotInfo
			} else if (type.equals("-ExtractUniprotInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -ExtractUniprotInfo "
							+ ExtractUniprotInfo.parameter_info());
					System.exit(0);
				}
				ExtractUniprotInfo.execute(args_remain);
				// ExportNucleotideBindingMotifInfo
			} else if (type.equals("-ExportNucleotideBindingMotifInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out
							.println("drppm -ExportNucleotideBindingMotifInfo "
									+ ExportNucleotideBindingMotifInfo
											.parameter_info());
					System.exit(0);
				}
				ExportNucleotideBindingMotifInfo.execute(args_remain);
				// GeneCardKeyword
			} else if (type.equals("-GeneCardKeyWords")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -GeneCardKeyWords "
							+ GeneCardKeyWords.parameter_info());
					System.exit(0);
				}
				GeneCardKeyWords.execute(args_remain);
				//
			} else if (type.equals("-ExtractFastaOrganism")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -ExtractFastaOrganism "
							+ ExtractFastaOrganism.parameter_info());
					System.exit(0);
				}
				ExtractFastaOrganism.execute(args_remain);
				// ReadPepInfo
			} else if (type.equals("-ReadPepInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {

					System.out.println("drppm -ReadPepInfo "
							+ ReadPepInfo.parameter_info());
					System.exit(0);
				}
				ReadPepInfo.execute(args_remain);
				// ExtractSpeciesEMBOSFile
			} else if (type.equals("-ExtractSpeciesEMBOSFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractSpeciesEMBOSFile "
							+ ExtractSpeciesEMBOSFile.parameter_info());
					System.exit(0);
				}
				ExtractSpeciesEMBOSFile.execute(args_remain);
				// GenerateSAPSOutput
			} else if (type.equals("-GenerateSAPSOutput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateSAPSOutput "
							+ GenerateSAPSOutput.parameter_info());
					System.exit(0);
				}
				GenerateSAPSOutput.execute(args_remain);
				// GenerateSSPAMatrix
			} else if (type.equals("-GenerateSSPAMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateSSPAMatrix "
							+ GenerateSSPAMatrix.parameter_info());
					System.exit(0);
				}
				GenerateSSPAMatrix.execute(args_remain);
				// CombineProteinFeatures
			} else if (type.equals("-CombineProteinFeatures")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineProteinFeatures "
							+ CombineProteinFeatures.parameter_info());
					System.exit(0);
				}
				CombineProteinFeatures.execute(args_remain);
				// Append2GRPRInfo
			} else if (type.equals("-Append2GRPRInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Append2GRPRInfo "
							+ Append2GRPRInfo.parameter_info());
					System.exit(0);
				}
				Append2GRPRInfo.execute(args_remain);
				// ProteinFeatureWithGRPRInfo
			} else if (type.equals("-ProteinFeatureWithGRPRInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ProteinFeatureWithGRPRInfo "
							+ ProteinFeatureWithGRPRInfo.parameter_info());
					System.exit(0);
				}
				ProteinFeatureWithGRPRInfo.execute(args_remain);
				// ProteinFeatureHistoBarPlot
			} else if (type.equals("-ProteinFeatureHistoBarPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ProteinFeatureHistoBarPlot "
							+ ProteinFeatureHistoBarPlot.parameter_info());
					System.exit(0);
				}
				ProteinFeatureHistoBarPlot.execute(args_remain);
				// ActivityPhosphositeForKinase
			} else if (type.equals("-ActivityPhosphositeForKinase")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ActivityPhosphositeForKinase "
							+ ActivityPhosphositeForKinase.parameter_info());
					System.exit(0);
				}
				ActivityPhosphositeForKinase.execute(args_remain);
				// ProteinActivityAnnotation
			} else if (type.equals("-ProteinActivityAnnotation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ProteinActivityAnnotation "
							+ ProteinActivityAnnotation.parameter_info());
					System.exit(0);
				}
				ProteinActivityAnnotation.execute(args_remain);
				// ActivityPhosphositeForAll
			} else if (type.equals("-ActivityPhosphositeForAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ActivityPhosphositeForAll "
							+ ActivityPhosphositeForAll.parameter_info());
					System.exit(0);
				}
				ActivityPhosphositeForAll.execute(args_remain);
				//
			} else if (type.equals("-Txt2Excel")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Txt2Excel "
							+ Txt2Excel.parameter_info());
					System.exit(0);
				}
				Txt2Excel.execute(args_remain);
				// SummarizeKinaseInformation
			} else if (type.equals("-SummarizeKinaseInformation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeKinaseInformation "
							+ SummarizeKinaseInformation.parameter_info());
					System.exit(0);
				}
				SummarizeKinaseInformation.execute(args_remain);
				// GenerateWordKinaseSummary
			} else if (type.equals("-ReadEnsemblGTFFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ReadEnsemblGTFFile "
							+ ReadEnsemblGTFFile.parameter_info());
					System.exit(0);
				}
				ReadEnsemblGTFFile.execute(args_remain);
				// GTFAnnotationSimple
			} else if (type.equals("-GTFAnnotationSimple")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GTFAnnotationSimple "
							+ GTFAnnotationSimple.parameter_info());
					System.exit(0);
				}
				GTFAnnotationSimple.execute(args_remain);
				//
			} else if (type.equals("-CompareDifferentialAnalysis")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CompareDifferentialAnalysis "
							+ CompareDifferentialAnalysis.parameter_info());
					System.exit(0);
				}
				CompareDifferentialAnalysis.execute(args_remain);
				//
			} else if (type.equals("-GeneVsJunctionFC")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneVsJunctionFC "
							+ GeneVsJunctionFC.parameter_info());
					System.exit(0);
				}
				GeneVsJunctionFC.execute(args_remain);
				// MappingInsertSizeEstimation
			} else if (type.equals("-MappingInsertSizeEstimation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MappingInsertSizeEstimation "
							+ MappingInsertSizeEstimation.parameter_info());
					System.exit(0);
				}
				MappingInsertSizeEstimation.execute(args_remain);
				// GenerateGeneWeightFile
			} else if (type.equals("-GenerateGeneWeightFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateGeneWeightFile "
							+ GenerateGeneWeightFile.parameter_info());
					System.exit(0);
				}
				GenerateGeneWeightFile.execute(args_remain);
				// IntegrateExpressionMatrix
			} else if (type.equals("-IntegrateExpressionMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -IntegrateExpressionMatrix "
							+ IntegrateExpressionMatrix.parameter_info());
					System.exit(0);
				}
				IntegrateExpressionMatrix.execute(args_remain);
				// GenerateSNVUnpairedScript
			} else if (type.equals("-GenerateSNVUnpairedScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateSNVUnpairedScript "
							+ GenerateSNVUnpairedScript.parameter_info());
					System.exit(0);
				}
				GenerateSNVUnpairedScript.execute(args_remain);
				// GenerateSNVPseudoUnpairedScript
			} else if (type.equals("-GenerateSNVPseudoUnpairedScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateSNVPseudoUnpairedScript "
									+ GenerateSNVPseudoUnpairedScript
											.parameter_info());
					System.exit(0);
				}
				GenerateSNVPseudoUnpairedScript.execute(args_remain);
				//
			} else if (type.equals("-GTFAnnotateGeneLength")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GTFAnnotateGeneLength "
							+ GTFAnnotateGeneLength.parameter_info());
					System.exit(0);
				}
				GTFAnnotateGeneLength.execute(args_remain);
				// CompareGeneLengthDistribution
			} else if (type.equals("-GTFAnnotateExonLength")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GTFAnnotateExonLength "
							+ GTFAnnotateExonLength.parameter_info());
					System.exit(0);
				}
				GTFAnnotateExonLength.execute(args_remain);
				// GTFAnnotateNumExon
			} else if (type.equals("-GTFAnnotateNumExon")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GTFAnnotateNumExon "
							+ GTFAnnotateNumExon.parameter_info());
					System.exit(0);
				}
				GTFAnnotateNumExon.execute(args_remain);
				//
			} else if (type.equals("-CompareGeneLengthDistribution")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CompareGeneLengthDistribution "
							+ CompareGeneLengthDistribution.parameter_info());
					System.exit(0);
				}
				CompareGeneLengthDistribution.execute(args_remain);
				// CompareExonCountDistribution
			} else if (type.equals("-CompareExonCountDistribution")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CompareExonCountDistribution "
							+ CompareExonCountDistribution.parameter_info());
					System.exit(0);
				}
				CompareExonCountDistribution.execute(args_remain);
				//
			} else if (type.equals("-ExtractLineBasedOnList")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractLineBasedOnList "
							+ ExtractLineBasedOnList.parameter_info());
					System.exit(0);
				}
				ExtractLineBasedOnList.execute(args_remain);
				// CoveragePostGenSoftLink
			} else if (type.equals("-CoveragePostGenSoftLink")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CoveragePostGenSoftLink "
							+ CoveragePostGenSoftLink.parameter_info());
					System.exit(0);
				}
				CoveragePostGenSoftLink.execute(args_remain);
				// FlagStatSoftLink
			} else if (type.equals("-FlagStatSoftLink")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FlagStatSoftLink "
							+ FlagStatSoftLink.parameter_info());
					System.exit(0);
				}
				FlagStatSoftLink.execute(args_remain);
				// GenerateExomeProbeData
			} else if (type.equals("-GenerateExomeProbeDataMouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateExomeProbeDataMouse "
							+ GenerateExomeProbeDataMouse.parameter_info());
					System.exit(0);
				}
				GenerateExomeProbeDataMouse.execute(args_remain);
				// ReadD2P2Database
			} else if (type.equals("-GenerateExomeProbeDataHuman")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateExomeProbeDataHuman "
							+ GenerateExomeProbeDataHuman.parameter_info());
					System.exit(0);
				}
				GenerateExomeProbeDataHuman.execute(args_remain);
				// ReadD2P2Database
			} else if (type.equals("-ReadD2P2Database")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ReadD2P2Database "
							+ ReadD2P2Database.parameter_info());
					System.exit(0);
				}
				ReadD2P2Database.execute(args_remain);
				// CalculateAminoAcidContent
			} else if (type.equals("-CalculateAminoAcidContent")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateAminoAcidContent "
							+ CalculateAminoAcidContent.parameter_info());
					System.exit(0);
				}
				CalculateAminoAcidContent.execute(args_remain);
				// CalculateAminoAcidContentSummary
			} else if (type.equals("-CalculateAminoAcidContentSummary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateAminoAcidContentSummary "
									+ CalculateAminoAcidContentSummary
											.parameter_info());
					System.exit(0);
				}
				CalculateAminoAcidContentSummary.execute(args_remain);
				//
			} else if (type.equals("-ProteinFeatureCombineResults")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ProteinFeatureCombineResults "
							+ ProteinFeatureCombineResults.parameter_info());
					System.exit(0);
				}
				ProteinFeatureCombineResults.execute(args_remain);
				//
			} else if (type.equals("-ExecuteShellScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExecuteShellScript [shell script file]");
					System.exit(0);
				}
				FileInputStream fstream = new FileInputStream(args_remain[0]);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						din));
				while (in.ready()) {
					String str = in.readLine();
					CommandLine.executeCommand(str);
				}
				//
			} else if (type.equals("-EXCAPSummary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EXCAPSummary "
							+ EXCAPSummary.parameter_info());
					System.exit(0);
				}
				EXCAPSummary.execute(args_remain);
				// AddSiftPrediction
			} else if (type.equals("-AddSiftPrediction")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AddSiftPrediction "
							+ AddSiftPrediction.parameter_info());
					System.exit(0);
				}
				AddSiftPrediction.execute(args_remain);
				// OverlapTwoFiles
			} else if (type.equals("-OverlapTwoFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OverlapTwoFiles "
							+ OverlapTwoFiles.parameter_info());
					System.exit(0);
				}
				OverlapTwoFiles.execute(args_remain);
				// OverlapGenotypeMatrix
			} else if (type.equals("-OverlapGenotypeMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OverlapGenotypeMatrix "
							+ OverlapGenotypeMatrix.parameter_info());
					System.exit(0);
				}
				OverlapGenotypeMatrix.execute(args_remain);
				// ScatterPlotWithNameResidual
			} else if (type.equals("-ScatterPlotWithNameResidual")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ScatterPlotWithNameResidual "
							+ ScatterPlotWithNameResidual.parameter_info());
					System.exit(0);
				}
				ScatterPlotWithNameResidual.execute(args_remain);
				// CosmicParsingAndOverlap
			} else if (type.equals("-CosmicParsingAndOverlap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CosmicParsingAndOverlap "
							+ CosmicParsingAndOverlap.parameter_info());
					System.exit(0);
				}
				CosmicParsingAndOverlap.execute(args_remain);
				// CalcDisorderRegionDistribution
			} else if (type.equals("-CalcDisorderRegionDistribution")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalcDisorderRegionDistribution "
							+ CalcDisorderRegionDistribution.parameter_info());
					System.exit(0);
				}
				CalcDisorderRegionDistribution.execute(args_remain);
				// CountGeneWithDisorderRegion
			} else if (type.equals("-CountGeneWithDisorderRegion")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CountGeneWithDisorderRegion "
							+ CountGeneWithDisorderRegion.parameter_info());
					System.exit(0);
				}
				CountGeneWithDisorderRegion.execute(args_remain);
				// CountGeneWithDisorderRegionPlot
			} else if (type.equals("-CountGeneWithDisorderRegionPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CountGeneWithDisorderRegionPlot "
									+ CountGeneWithDisorderRegionPlot
											.parameter_info());
					System.exit(0);
				}
				CountGeneWithDisorderRegionPlot.execute(args_remain);
				// SEGPostProcessing
			} else if (type.equals("-SEGPostProcessing")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SEGPostProcessing "
							+ SEGPostProcessing.parameter_info());
					System.exit(0);
				}
				SEGPostProcessing.execute(args_remain);
				// GrabGRPRFasta
			} else if (type.equals("-GrabGRPRFasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabGRPRFasta "
							+ GrabGRPRFasta.parameter_info());
					System.exit(0);
				}
				GrabGRPRFasta.execute(args_remain);
				// ProteinFeatureHistoBarPlotGRPR
			} else if (type.equals("-ProteinFeatureHistoBarPlotGRPR")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ProteinFeatureHistoBarPlotGRPR "
							+ ProteinFeatureHistoBarPlotGRPR.parameter_info());
					System.exit(0);
				}
				ProteinFeatureHistoBarPlotGRPR.execute(args_remain);
				// OverlapPeptide2Phosphosite
			} else if (type.equals("-OverlapPeptide2Phosphosite")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OverlapPeptide2Phosphosite "
							+ OverlapPeptide2Phosphosite.parameter_info());
					System.exit(0);
				}
				OverlapPeptide2Phosphosite.execute(args_remain);
				// ExcapRNAseqMAFColumn
			} else if (type.equals("-ExcapRNAseqMAFColumn")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExcapRNAseqMAFColumn "
							+ ExcapRNAseqMAFColumn.parameter_info());
					System.exit(0);
				}
				ExcapRNAseqMAFColumn.execute(args_remain);
				// GenerateChargeGraphForEachProtein
			} else if (type.equals("-GenerateChargeGraphForEachProtein")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateChargeGraphForEachProtein "
									+ GenerateChargeGraphForEachProtein
											.parameter_info());
					System.exit(0);
				}
				GenerateChargeGraphForEachProtein.execute(args_remain);
				// CalculateChargeFastaFile
			} else if (type.equals("-CalculateChargeFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateChargeFastaFile "
							+ CalculateChargeFastaFile.parameter_info());
					System.exit(0);
				}
				CalculateChargeFastaFile.execute(args_remain);
				//
			} else if (type.equals("-GrabExonInformation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabExonInformation "
							+ GrabExonInformation.parameter_info());
					System.exit(0);
				}
				GrabExonInformation.execute(args_remain);
				// CalculateExonDistribution
			} else if (type.equals("-CalculateExonDistribution")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateExonDistribution "
							+ CalculateExonDistribution.parameter_info());
					System.exit(0);
				}
				CalculateExonDistribution.execute(args_remain);
				// WGCNAKinaseEnrichmentPvalue
			} else if (type.equals("-WGCNAKinaseEnrichmentPvalue")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -WGCNAKinaseEnrichmentPvalue "
							+ WGCNAKinaseEnrichmentPvalue.parameter_info());
					System.exit(0);
				}
				WGCNAKinaseEnrichmentPvalue.execute(args_remain);
				// WGCNAKinaseEnrichmentPhosphosite
			} else if (type.equals("-WGCNAKinaseEnrichmentPhosphosite")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -WGCNAKinaseEnrichmentPhosphosite "
									+ WGCNAKinaseEnrichmentPhosphosite
											.parameter_info());
					System.exit(0);
				}
				WGCNAKinaseEnrichmentPhosphosite.execute(args_remain);
				// GenerateSNV4File
			} else if (type.equals("-GenerateSNV4File")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateSNV4File "
							+ GenerateSNV4File.parameter_info());
					System.exit(0);
				}
				GenerateSNV4File.execute(args_remain);
				// ExtractSingletonAndRecurrent
			} else if (type.equals("-ExtractSingletonAndRecurrent")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractSingletonAndRecurrent "
							+ ExtractSingletonAndRecurrent.parameter_info());
					System.exit(0);
				}
				ExtractSingletonAndRecurrent.execute(args_remain);
				// Mouse2HumanProteinPaintInput
			} else if (type.equals("-Mouse2HumanProteinPaintInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Mouse2HumanProteinPaintInput "
							+ Mouse2HumanProteinPaintInput.parameter_info());
					System.exit(0);
				}
				Mouse2HumanProteinPaintInput.execute(args_remain);
				// ExtractSNPBasedOnSampleChrCoord
			} else if (type.equals("-ExtractSNPBasedOnSampleChrCoord")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExtractSNPBasedOnSampleChrCoord "
									+ ExtractSNPBasedOnSampleChrCoord
											.parameter_info());
					System.exit(0);
				}
				ExtractSNPBasedOnSampleChrCoord.execute(args_remain);
				// OverRepresentationAnalysis
			} else if (type.equals("-OverRepresentationAnalysis")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OverRepresentationAnalysis "
							+ OverRepresentationAnalysis.parameter_info());
					System.exit(0);
				}
				OverRepresentationAnalysis.execute(args_remain);
				// GenerateNetworkBasedOnClusters
			} else if (type.equals("-GenerateNetworkBasedOnClusters")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateNetworkBasedOnClusters "
							+ GenerateNetworkBasedOnClusters.parameter_info());
					System.exit(0);
				}
				GenerateNetworkBasedOnClusters.execute(args_remain);
				// WGCNANetwork
			} else if (type.equals("-WGCNANetwork")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -WGCNANetwork "
							+ WGCNANetwork.parameter_info());
					System.exit(0);
				}
				WGCNANetwork.execute(args_remain);
				// WGCNAModifyShape
			} else if (type.equals("-WGCNAModifyShape")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -WGCNAModifyShape "
							+ WGCNAModifyShape.parameter_info());
					System.exit(0);
				}
				WGCNAModifyShape.execute(args_remain);
				// GenerateKinaseSubstrateGMT
			} else if (type.equals("-GenerateKinaseSubstrateGMT")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateKinaseSubstrateGMT "
							+ GenerateKinaseSubstrateGMT.parameter_info());
					System.exit(0);
				}
				GenerateKinaseSubstrateGMT.execute(args_remain);
				// GenerateKinaseSubstrateGMTFamily
			} else if (type.equals("-GenerateKinaseSubstrateGMTFamily")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateKinaseSubstrateGMTFamily "
									+ GenerateKinaseSubstrateGMTFamily
											.parameter_info());
					System.exit(0);
				}
				GenerateKinaseSubstrateGMTFamily.execute(args_remain);
				// GenerateKinaseSubstrateGMTGroup
			} else if (type.equals("-GenerateKinaseSubstrateGMTGroup")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateKinaseSubstrateGMTGroup "
									+ GenerateKinaseSubstrateGMTGroup
											.parameter_info());
					System.exit(0);
				}
				GenerateKinaseSubstrateGMTGroup.execute(args_remain);
				//
			} else if (type.equals("-GenerateEnrichmentFileInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateEnrichmentFileInput "
							+ GenerateEnrichmentFileInput.parameter_info());
					System.exit(0);
				}
				GenerateEnrichmentFileInput.execute(args_remain);
				// FilterKinaseSubstrate2KinaseOnly
			} else if (type.equals("-FilterKinaseSubstrate2KinaseOnly")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FilterKinaseSubstrate2KinaseOnly "
									+ FilterKinaseSubstrate2KinaseOnly
											.parameter_info());
					System.exit(0);
				}
				FilterKinaseSubstrate2KinaseOnly.execute(args_remain);
				// KinaseSubstrate2KinaseOnly
			} else if (type.equals("-KinaseSubstrate2KinaseOnly")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -KinaseSubstrate2KinaseOnly "
							+ KinaseSubstrate2KinaseOnly.parameter_info());
					System.exit(0);
				}
				KinaseSubstrate2KinaseOnly.execute(args_remain);
				// KinaseSubstrateAll
			} else if (type.equals("-KinaseSubstrateAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -KinaseSubstrateAll "
							+ KinaseSubstrateAll.parameter_info());
					System.exit(0);
				}
				KinaseSubstrateAll.execute(args_remain);
				//
			} else if (type.equals("-GenerateEnrichmentBarPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateEnrichmentBarPlot "
							+ GenerateEnrichmentBarPlot.parameter_info());
					System.exit(0);
				}
				GenerateEnrichmentBarPlot.execute(args_remain);
				// GenerateGeneListDatabase
			} else if (type.equals("-GenerateGeneListDatabase")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateGeneListDatabase "
							+ GenerateGeneListDatabase.parameter_info());
					System.exit(0);
				}
				GenerateGeneListDatabase.execute(args_remain);
				// GenerateGCContentMatrix
			} else if (type.equals("-GenerateGCContentMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateGCContentMatrix "
							+ GenerateGCContentMatrix.parameter_info());
					System.exit(0);
				}
				GenerateGCContentMatrix.execute(args_remain);
				// AssignKnownKinaseSubstrateRelationship
			} else if (type.equals("-AssignKnownKinaseSubstrateRelationship")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AssignKnownKinaseSubstrateRelationship "
									+ AssignKnownKinaseSubstrateRelationship
											.parameter_info());
					System.exit(0);
				}
				AssignKnownKinaseSubstrateRelationship.execute(args_remain);
				// AssignKnownKinaseSubstrateRelationshipARMSERMS
			} else if (type
					.equals("-AssignKnownKinaseSubstrateRelationshipARMSERMS")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AssignKnownKinaseSubstrateRelationshipARMSERMS "
									+ AssignKnownKinaseSubstrateRelationshipARMSERMS
											.parameter_info());
					System.exit(0);
				}
				AssignKnownKinaseSubstrateRelationshipARMSERMS
						.execute(args_remain);
				//
			} else if (type.equals("-ConvertMatrix2IKAPInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ConvertMatrix2IKAPInput "
							+ ConvertMatrix2IKAPInput.parameter_info());
					System.exit(0);
				}
				ConvertMatrix2IKAPInput.execute(args_remain);
				// CreateNetworkDisplayExpression
			} else if (type.equals("-CreateNetworkDisplayExpression")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CreateNetworkDisplayExpression "
							+ CreateNetworkDisplayExpression.parameter_info());
					System.exit(0);
				}
				CreateNetworkDisplayExpression.execute(args_remain);
				// NetworkNodeHighlight
			} else if (type.equals("-NetworkNodeHighlight")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NetworkNodeHighlight "
							+ NetworkNodeHighlight.parameter_info());
					System.exit(0);
				}
				NetworkNodeHighlight.execute(args_remain);
				// ConvertWGCNAPeptide2Site
			} else if (type.equals("-ConvertWGCNAPeptide2Site")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ConvertWGCNAPeptide2Site "
							+ ConvertWGCNAPeptide2Site.parameter_info());
					System.exit(0);
				}
				ConvertWGCNAPeptide2Site.execute(args_remain);
				// GenerateComprehensiveGeneList
			} else if (type.equals("-GenerateComprehensiveGeneList")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateComprehensiveGeneList "
							+ GenerateComprehensiveGeneList.parameter_info());
					System.exit(0);
				}
				GenerateComprehensiveGeneList.execute(args_remain);
				// ConvertMatrix2IKAPInputNormalize
			} else if (type.equals("-ConvertMatrix2IKAPInputNormalize")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ConvertMatrix2IKAPInputNormalize "
									+ ConvertMatrix2IKAPInputNormalize
											.parameter_info());
					System.exit(0);
				}
				ConvertMatrix2IKAPInputNormalize.execute(args_remain);
				// PositionWeightMatrixScanner
			} else if (type.equals("-PositionWeightMatrixScanner")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PositionWeightMatrixScanner "
							+ PositionWeightMatrixScanner.parameter_info());
					System.exit(0);
				}
				PositionWeightMatrixScanner.execute(args_remain);
				// SpliceFactorMotifScanner
			} else if (type.equals("-SpliceFactorMotifScanner")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SpliceFactorMotifScanner "
							+ SpliceFactorMotifScanner.parameter_info());
					System.exit(0);
				}
				SpliceFactorMotifScanner.execute(args_remain);
				// SpliceFactorMotifFisherExact
			} else if (type.equals("-SpliceFactorMotifFisherExact")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SpliceFactorMotifFisherExact "
							+ SpliceFactorMotifFisherExact.parameter_info());
					System.exit(0);
				}
				SpliceFactorMotifFisherExact.execute(args_remain);
				// CombineLIMMAResultExpression
			} else if (type.equals("-CombineLIMMAResultExpression")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineLIMMAResultExpression "
							+ CombineLIMMAResultExpression.parameter_info());
					System.exit(0);
				}
				CombineLIMMAResultExpression.execute(args_remain);
				// MicroarrayAddGeneName
			} else if (type.equals("-MicroarrayAddGeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MicroarrayAddGeneName "
							+ MicroarrayAddGeneName.parameter_info());
					System.exit(0);
				}
				MicroarrayAddGeneName.execute(args_remain);
				// CalculateSubnetBioNet
			} else if (type.equals("-CalculateSubnetBioNetInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateSubnetBioNetInput "
							+ CalculateSubnetBioNetInput.parameter_info());
					System.exit(0);
				}
				CalculateSubnetBioNetInput.execute(args_remain);
				// SubNetworkBioNet
			} else if (type.equals("-SubNetworkBioNet")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SubNetworkBioNet "
							+ SubNetworkBioNet.parameter_info());
					System.exit(0);
				}
				SubNetworkBioNet.execute(args_remain);
				// GenerateD2P2Input
			} else if (type.equals("-GenerateD2P2Input")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateD2P2Input "
							+ GenerateD2P2Input.parameter_info());
					System.exit(0);
				}
				GenerateD2P2Input.execute(args_remain);
				// GenerateSEGSampleGroup
			} else if (type.equals("-GenerateSEGSampleGroup")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateSEGSampleGroup "
							+ GenerateSEGSampleGroup.parameter_info());
					System.exit(0);
				}
				GenerateSEGSampleGroup.execute(args_remain);
				// CalculateRNAseqMAF
			} else if (type.equals("-CalculateRNAseqMAF")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateRNAseqMAF "
							+ CalculateRNAseqMAF.parameter_info());
					System.exit(0);
				}
				CalculateRNAseqMAF.execute(args_remain);
				// EXONCAPBasicStats
			} else if (type.equals("-EXONCAPBasicStats")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EXONCAPBasicStats "
							+ EXONCAPBasicStats.parameter_info());
					System.exit(0);
				}
				EXONCAPBasicStats.execute(args_remain);
				// EXCAPGenerateSampleType
			} else if (type.equals("-EXCAPGenerateSampleType")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EXCAPGenerateSampleType "
							+ EXCAPGenerateSampleType.parameter_info());
					System.exit(0);
				}
				EXCAPGenerateSampleType.execute(args_remain);
				// ReadMATSExtractNeighboringSequencing
			} else if (type.equals("-ReadMATSExtractNeighboringSequencing")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ReadMATSExtractNeighboringSequencing "
									+ ReadMATSExtractNeighboringSequencing
											.parameter_info());
					System.exit(0);
				}
				ReadMATSExtractNeighboringSequencing.execute(args_remain);
				// SummarizeMATSGenes
			} else if (type.equals("-SummarizeMATSGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SummarizeMATSGenes "
									+ SummarizeMATSGenes
											.parameter_info());
					System.exit(0);
				}
				SummarizeMATSGenes.execute(args_remain);
				// 
			} else if (type.equals("-ExtractRandomExonFromGTF")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractRandomExonFromGTF "
							+ ExtractRandomExonFromGTF.parameter_info());
					System.exit(0);
				}
				ExtractRandomExonFromGTF.execute(args_remain);
				// CombineEnrichmentPvalues
			} else if (type.equals("-CombineEnrichmentPvalues")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineEnrichmentPvalues "
							+ CombineEnrichmentPvalues.parameter_info());
					System.exit(0);
				}
				CombineEnrichmentPvalues.execute(args_remain);
				// AppendGeneName2StringNetwork
			} else if (type.equals("-AppendGeneName2StringNetwork")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendGeneName2StringNetwork "
							+ AppendGeneName2StringNetwork.parameter_info());
					System.exit(0);
				}
				AppendGeneName2StringNetwork.execute(args_remain);
				// GenerateEnsembl2GeneNameTable
			} else if (type.equals("-GenerateEnsembl2GeneNameTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateEnsembl2GeneNameTable "
							+ GenerateEnsembl2GeneNameTable.parameter_info());
					System.exit(0);
				}
				GenerateEnsembl2GeneNameTable.execute(args_remain);
				// Convert2SIFFile
			} else if (type.equals("-Convert2SIFFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Convert2SIFFile "
							+ Convert2SIFFile.parameter_info());
					System.exit(0);
				}
				Convert2SIFFile.execute(args_remain);
				// GenerateGSEAInputFile
			} else if (type.equals("-GenerateGSEAInputGCTFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateGSEAInputGCTFile "
							+ GenerateGSEAInputGCTFile.parameter_info());
					System.exit(0);
				}
				GenerateGSEAInputGCTFile.execute(args_remain);
				// GenerateGSEAInputCLSFile
			} else if (type.equals("-GenerateGSEAInputCLSFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateGSEAInputCLSFile "
							+ GenerateGSEAInputCLSFile.parameter_info());
					System.exit(0);
				}
				GenerateGSEAInputCLSFile.execute(args_remain);
				// GRPRReplaceAnnotationInformation
			} else if (type.equals("-GRPRReplaceAnnotationInformation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GRPRReplaceAnnotationInformation "
									+ GRPRReplaceAnnotationInformation
											.parameter_info());
					System.exit(0);
				}
				GRPRReplaceAnnotationInformation.execute(args_remain);
				// CalculateIntersectingGenes
			} else if (type.equals("-CalculateIntersectingGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateIntersectingGenes "
							+ CalculateIntersectingGenes.parameter_info());
					System.exit(0);
				}
				CalculateIntersectingGenes.execute(args_remain);
				// CombineEnrichmentPathwayPvalues
			} else if (type.equals("-CombineEnrichmentPathwayPvalues")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CombineEnrichmentPathwayPvalues "
									+ CombineEnrichmentPathwayPvalues
											.parameter_info());
					System.exit(0);
				}
				CombineEnrichmentPathwayPvalues.execute(args_remain);
				// Convert2SJGraphFormat
			} else if (type.equals("-Convert2SJGraphFormat")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Convert2SJGraphFormat "
							+ Convert2SJGraphFormat.parameter_info());
					System.exit(0);
				}
				Convert2SJGraphFormat.execute(args_remain);
				// GenerateSubgraph
			} else if (type.equals("-GenerateSubgraph")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateSubgraph "
							+ GenerateSubgraph.parameter_info());
					System.exit(0);
				}
				GenerateSubgraph.execute(args_remain);
				// GrabGeneName
			} else if (type.equals("-GrabGeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabGeneName "
							+ GrabGeneName.parameter_info());
					System.exit(0);
				}
				GrabGeneName.execute(args_remain);
				// GenerateGraphStatistics
			} else if (type.equals("-GenerateGraphStatistics")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateGraphStatistics "
							+ GenerateGraphStatistics.parameter_info());
					System.exit(0);
				}
				GenerateGraphStatistics.execute(args_remain);
				// StringDBFilter
			} else if (type.equals("-StringDBFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -StringDBFilter "
							+ StringDBFilter.parameter_info());
					System.exit(0);
				}
				StringDBFilter.execute(args_remain);
				// GenerateUniqFastaFile
			} else if (type.equals("-GenerateUniqFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateUniqFastaFile "
							+ GenerateUniqFastaFile.parameter_info());
					System.exit(0);
				}
				GenerateUniqFastaFile.execute(args_remain);
				// BoxPlotGeneratorTwoGroup
			} else if (type.equals("-BoxPlotGeneratorTwoGroup")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BoxPlotGeneratorTwoGroup "
							+ BoxPlotGeneratorTwoGroup.parameter_info());
					System.exit(0);
				}
				BoxPlotGeneratorTwoGroup.execute(args_remain);
				// BoxPlotGeneratorThreeGroup
			} else if (type.equals("-BoxPlotGeneratorThreeGroup")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BoxPlotGeneratorThreeGroup "
							+ BoxPlotGeneratorThreeGroup.parameter_info());
					System.exit(0);
				}
				BoxPlotGeneratorThreeGroup.execute(args_remain);
				//
			} else if (type.equals("-MatchFasta2Coordinate")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MatchFasta2Coordinate "
							+ MatchFasta2Coordinate.parameter_info());
					System.exit(0);
				}
				MatchFasta2Coordinate.execute(args_remain);
				// GenerateChargeGraph
			} else if (type.equals("-GenerateChargeGraph")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateChargeGraph "
							+ GenerateChargeGraph.parameter_info());
					System.exit(0);
				}
				GenerateChargeGraph.execute(args_remain);
				// CalculateHydrophobicityFastaFile
			} else if (type.equals("-CalculateHydrophobicityFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateHydrophobicityFastaFile "
									+ CalculateHydrophobicityFastaFile
											.parameter_info());
					System.exit(0);
				}
				CalculateHydrophobicityFastaFile.execute(args_remain);
				// ConvertGene2Uniprot
			} else if (type.equals("-ConvertGene2Uniprot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ConvertGene2Uniprot "
							+ ConvertGene2Uniprot.parameter_info());
					System.exit(0);
				}
				ConvertGene2Uniprot.execute(args_remain);
				// WebTextMining
			} else if (type.equals("-WebTextMining")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -WebTextMining "
							+ WebTextMining.parameter_info());
					System.exit(0);
				}
				WebTextMining.execute(args_remain);
				// ParseGeneOntology
			} else if (type.equals("-ParseGeneOntology")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ParseGeneOntology "
							+ ParseGeneOntology.parameter_info());
					System.exit(0);
				}
				ParseGeneOntology.execute(args_remain);
				// GrabPhosphositeExpressionGeneCentric
			} else if (type.equals("-GrabPhosphositeExpressionGeneCentric")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GrabPhosphositeExpressionGeneCentric "
									+ GrabPhosphositeExpressionGeneCentric
											.parameter_info());
					System.exit(0);
				}
				GrabPhosphositeExpressionGeneCentric.execute(args_remain);
				// ConvertKinaseGroupTxt2Gmt
			} else if (type.equals("-ConvertKinaseGroupTxt2Gmt")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ConvertKinaseGroupTxt2Gmt "
							+ ConvertKinaseGroupTxt2Gmt.parameter_info());
					System.exit(0);
				}
				ConvertKinaseGroupTxt2Gmt.execute(args_remain);
				// OverRepresentationAnalysisFDR
			} else if (type.equals("-OverRepresentationAnalysisFDR")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OverRepresentationAnalysisFDR "
							+ OverRepresentationAnalysisFDR.parameter_info());
					System.exit(0);
				}
				OverRepresentationAnalysisFDR.execute(args_remain);
				// CompareNetworkDatabase
			} else if (type.equals("-OverRepresentationAnalysisWithoutFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -OverRepresentationAnalysisWithoutFilter "
									+ OverRepresentationAnalysisWithoutFilter
											.parameter_info());
					System.exit(0);
				}
				OverRepresentationAnalysisWithoutFilter.execute(args_remain);
				// CompareNetworkDatabase
			} else if (type.equals("-CompareNetworkDatabase")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CompareNetworkDatabase "
							+ CompareNetworkDatabase.parameter_info());
					System.exit(0);
				}
				CompareNetworkDatabase.execute(args_remain);
				// GenerateMultipleCirclesEdge
			} else if (type.equals("-GenerateMultipleCirclesEdge")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMultipleCirclesEdge "
							+ GenerateMultipleCirclesEdge.parameter_info());
					System.exit(0);
				}
				GenerateMultipleCirclesEdge.execute(args_remain);
				// GenerateGODatabaseDAVID
			} else if (type.equals("-GenerateGODatabaseDAVID")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateGODatabaseDAVID "
							+ GenerateGODatabaseDAVID.parameter_info());
					System.exit(0);
				}
				GenerateGODatabaseDAVID.execute(args_remain);
				// StandardizeGeneName
			} else if (type.equals("-StandardizeGeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -StandardizeGeneName "
							+ StandardizeGeneName.parameter_info());
					System.exit(0);
				}
				StandardizeGeneName.execute(args_remain);
				// AppendMatrixTogether
			} else if (type.equals("-AppendMatrixTogether")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendMatrixTogether "
							+ AppendMatrixTogether.parameter_info());
					System.exit(0);
				}
				AppendMatrixTogether.execute(args_remain);
				// AppendMatrixTogether
			} else if (type.equals("-CalculateResidueMotif")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateResidueMotif "
							+ CalculateResidueMotif.parameter_info());
					System.exit(0);
				}
				CalculateResidueMotif.execute(args_remain);
				// CountGeneWithResidueRegionPlot
			} else if (type.equals("-CountGeneWithResidueRegionPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CountGeneWithResidueRegionPlot "
							+ CountGeneWithResidueRegionPlot.parameter_info());
					System.exit(0);
				}
				CountGeneWithResidueRegionPlot.execute(args_remain);
				// GenerateTrendPlot
			} else if (type.equals("-GenerateTrendPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateTrendPlot "
							+ GenerateTrendPlot.parameter_info());
					System.exit(0);
				}
				GenerateTrendPlot.execute(args_remain);
				// GenerateIDConversionMasterTable
			} else if (type.equals("-GenerateIDConversionMasterTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateIDConversionMasterTable "
									+ GenerateIDConversionMasterTable
											.parameter_info());
					System.exit(0);
				}
				GenerateIDConversionMasterTable.execute(args_remain);
				// CalculateCorrelationMatrix
			} else if (type.equals("-CalculateCorrelationMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateCorrelationMatrix "
							+ CalculateCorrelationMatrix.parameter_info());
					System.exit(0);
				}
				CalculateCorrelationMatrix.execute(args_remain);
				// CalculateResidueFrequencyFastaFile
			} else if (type.equals("-CalculateResidueFrequencyFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateResidueFrequencyFastaFile "
									+ CalculateResidueFrequencyFastaFile
											.parameter_info());
					System.exit(0);
				}
				CalculateResidueFrequencyFastaFile.execute(args_remain);
				// TranscriptLengthSlidingWindow
			} else if (type.equals("-TranscriptLengthSlidingWindow")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -TranscriptLengthSlidingWindow "
							+ TranscriptLengthSlidingWindow.parameter_info());
					System.exit(0);
				}
				TranscriptLengthSlidingWindow.execute(args_remain);
				// TranscriptLengthSlidingWindowInhibitedGenes
			} else if (type
					.equals("-TranscriptLengthSlidingWindowInhibitedGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -TranscriptLengthSlidingWindowInhibitedGenes "
									+ TranscriptLengthSlidingWindowInhibitedGenes
											.parameter_info());
					System.exit(0);
				}
				TranscriptLengthSlidingWindowInhibitedGenes
						.execute(args_remain);
				// SummarizeMouseIndelAnalysis
			} else if (type.equals("-SummarizeMouseIndelAnalysis")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeMouseIndelAnalysis "
							+ SummarizeMouseIndelAnalysis.parameter_info());
					System.exit(0);
				}
				SummarizeMouseIndelAnalysis.execute(args_remain);
				// NormalizeMatrix2IKAP
			} else if (type.equals("-NormalizeMatrix2IKAP")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizeMatrix2IKAP "
							+ NormalizeMatrix2IKAP.parameter_info());
					System.exit(0);
				}
				NormalizeMatrix2IKAP.execute(args_remain);
				// NormalizeWholeGenome
			} else if (type.equals("-NormalizeWholeGenome")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizeWholeGenome "
							+ NormalizeWholeGenome.parameter_info());
					System.exit(0);
				}
				NormalizeWholeGenome.execute(args_remain);
				// NormalizePhosphoAgainstWhole
			} else if (type.equals("-NormalizePhosphoAgainstWhole")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizePhosphoAgainstWhole "
							+ NormalizePhosphoAgainstWhole.parameter_info());
					System.exit(0);
				}
				NormalizePhosphoAgainstWhole.execute(args_remain);
				// NormalizePhosphoAgainstWholeWithOffset
			} else if (type.equals("-NormalizePhosphoAgainstWholeWithOffset")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -NormalizePhosphoAgainstWholeWithOffset "
									+ NormalizePhosphoAgainstWholeWithOffset
											.parameter_info());
					System.exit(0);
				}
				NormalizePhosphoAgainstWholeWithOffset.execute(args_remain);
				//
			} else if (type.equals("-ORASummaryTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ORASummaryTable "
							+ ORASummaryTable.parameter_info());
					System.exit(0);
				}
				ORASummaryTable.execute(args_remain);
				// ORASummaryTableHeatmap
			} else if (type.equals("-ORASummaryTableHeatmap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ORASummaryTableHeatmap "
							+ ORASummaryTableHeatmap.parameter_info());
					System.exit(0);
				}
				ORASummaryTableHeatmap.execute(args_remain);
				//
			} else if (type.equals("-KinaseFamilyCluster")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -KinaseFamilyCluster "
							+ KinaseFamilyCluster.parameter_info());
					System.exit(0);
				}
				KinaseFamilyCluster.execute(args_remain);
				// GenerateMotifXFasta
			} else if (type.equals("-GenerateMotifXFasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMotifXFasta "
							+ GenerateMotifXFasta.parameter_info());
					System.exit(0);
				}
				GenerateMotifXFasta.execute(args_remain);
				// GenerateMotifXFastaAll
			} else if (type.equals("-GenerateMotifXFastaAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMotifXFastaAll "
							+ GenerateMotifXFastaAll.parameter_info());
					System.exit(0);
				}
				GenerateMotifXFastaAll.execute(args_remain);
				// PhosphoMotifEnrichment
			} else if (type.equals("-PhosphoMotifEnrichment")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PhosphoMotifEnrichment "
							+ PhosphoMotifEnrichment.parameter_info());
					System.exit(0);
				}
				PhosphoMotifEnrichment.execute(args_remain);
				// MISCConvertPeptideID
			} else if (type.equals("-MISCConvertPeptideID")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MISCConvertPeptideID "
							+ MISCConvertPeptideID.parameter_info());
					System.exit(0);
				}
				MISCConvertPeptideID.execute(args_remain);
				// GenerateFastaSequenceForEachProtein
			} else if (type.equals("-GenerateFastaSequenceForEachProtein")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateFastaSequenceForEachProtein "
									+ GenerateFastaSequenceForEachProtein
											.parameter_info());
					System.exit(0);
				}
				GenerateFastaSequenceForEachProtein.execute(args_remain);
				// ConservationSurvey
			} else if (type.equals("-ConservationSurvey")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ConservationSurvey "
							+ ConservationSurvey.parameter_info());
					System.exit(0);
				}
				ConservationSurvey.execute(args_remain);
				// AlignSEGSequence
			} else if (type.equals("-AlignSEGSequence")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AlignSEGSequence "
							+ AlignSEGSequence.parameter_info());
					System.exit(0);
				}
				AlignSEGSequence.execute(args_remain);
				// AppendLIMMAResult2Matrix
			} else if (type.equals("-AppendLIMMAResult2Matrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendLIMMAResult2Matrix "
							+ AppendLIMMAResult2Matrix.parameter_info());
					System.exit(0);
				}
				AppendLIMMAResult2Matrix.execute(args_remain);
				// DEGFilteredGeneSet
			} else if (type.equals("-DEGFilteredGeneSet")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -DEGFilteredGeneSet "
							+ DEGFilteredGeneSet.parameter_info());
					System.exit(0);
				}
				DEGFilteredGeneSet.execute(args_remain);
				// MergeSamples
			} else if (type.equals("-MergeSamples")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeSamples "
							+ MergeSamples.parameter_info());
					System.exit(0);
				}
				MergeSamples.execute(args_remain);
				// GeneListMatrix
			} else if (type.equals("-GeneListMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneListMatrix "
							+ GeneListMatrix.parameter_info());
					System.exit(0);
				}
				GeneListMatrix.execute(args_remain);
				// GeneListMatrix2
			} else if (type.equals("-GeneListMatrix2")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneListMatrix2 "
							+ GeneListMatrix2.parameter_info());
					System.exit(0);
				}
				GeneListMatrix2.execute(args_remain);
				// FilterMatrixExpression
			} else if (type.equals("-FilterMatrixExpression")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterMatrixExpression "
							+ FilterMatrixExpression.parameter_info());
					System.exit(0);
				}
				FilterMatrixExpression.execute(args_remain);
				//
			} else if (type.equals("-CleanWhlProteome")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CleanWhlProteome "
							+ CleanWhlProteome.parameter_info());
					System.exit(0);
				}
				CleanWhlProteome.execute(args_remain);
				// ExpressionIntegrationDrawer
			} else if (type.equals("-ExpressionIntegrationDrawer")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExpressionIntegrationDrawer "
							+ ExpressionIntegrationDrawer.parameter_info());
					System.exit(0);
				}
				ExpressionIntegrationDrawer.execute(args_remain);
				// ExpressionIntegrationDrawerFilter
			} else if (type.equals("-ExpressionIntegrationDrawerFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExpressionIntegrationDrawerFilter "
									+ ExpressionIntegrationDrawerFilter
											.parameter_info());
					System.exit(0);
				}
				ExpressionIntegrationDrawerFilter.execute(args_remain);
				//
			} else if (type.equals("-ExpressionIntegrationDrawerWhlPho")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ExpressionIntegrationDrawerWhlPho "
									+ ExpressionIntegrationDrawerWhlPho
											.parameter_info());
					System.exit(0);
				}
				ExpressionIntegrationDrawerWhlPho.execute(args_remain);
				// FilterMatrixFile
			} else if (type.equals("-FilterMatrixFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterMatrixFile "
							+ FilterMatrixFile.parameter_info());
					System.exit(0);
				}
				FilterMatrixFile.execute(args_remain);
				// FilterMatrixFileFlex
			} else if (type.equals("-FilterMatrixFileFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterMatrixFileFlex "
							+ FilterMatrixFileFlex.parameter_info());
					System.exit(0);
				}
				FilterMatrixFileFlex.execute(args_remain);
				//
			} else if (type.equals("-ExtractUCSCMultipleSeqAlign")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractUCSCMultipleSeqAlign "
							+ ExtractUCSCMultipleSeqAlign.parameter_info());
					System.exit(0);
				}
				ExtractUCSCMultipleSeqAlign.execute(args_remain);
				// CalculatePercentConservation
			} else if (type.equals("-CalculatePercentConservation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculatePercentConservation "
							+ CalculatePercentConservation.parameter_info());
					System.exit(0);
				}
				CalculatePercentConservation.execute(args_remain);
				// CalculatePercentConservationNameInput
			} else if (type.equals("-CalculatePercentConservationNameInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculatePercentConservationNameInput "
									+ CalculatePercentConservationNameInput
											.parameter_info());
					System.exit(0);
				}
				CalculatePercentConservationNameInput.execute(args_remain);
				//
			} else if (type.equals("-PSSMMotifFinder")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PSSMMotifFinder "
							+ PSSMMotifFinder.parameter_info());
					System.exit(0);
				}
				PSSMMotifFinder.execute(args_remain);
				// SimulatedPeptideDigestion
			} else if (type.equals("-SimulatedPeptideDigestion")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SimulatedPeptideDigestion "
							+ SimulatedPeptideDigestion.parameter_info());
					System.exit(0);
				}
				SimulatedPeptideDigestion.execute(args_remain);
				// GrabFastaFile
			} else if (type.equals("-GrabFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabFastaFile "
							+ GrabFastaFile.parameter_info());
					System.exit(0);
				}
				GrabFastaFile.execute(args_remain);
				// ChromosomeBarPlot
			} else if (type.equals("-ChromosomeBarPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ChromosomeBarPlot "
							+ ChromosomeBarPlot.parameter_info());
					System.exit(0);
				}
				ChromosomeBarPlot.execute(args_remain);
				// ExtractFusionGenes
			} else if (type.equals("-ExtractFusionGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractFusionGenes "
							+ ExtractFusionGenes.parameter_info());
					System.exit(0);
				}
				ExtractFusionGenes.execute(args_remain);
				// EXONCAPHumanBasicStats
			} else if (type.equals("-EXONCAPHumanBasicStats")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EXONCAPHumanBasicStats "
							+ EXONCAPHumanBasicStats.parameter_info());
					System.exit(0);
				}
				EXONCAPHumanBasicStats.execute(args_remain);
				// EXONCAPBasicStatsPairedFile
			} else if (type.equals("-EXONCAPBasicStatsPairedFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EXONCAPBasicStatsPairedFile "
							+ EXONCAPBasicStatsPairedFile.parameter_info());
					System.exit(0);
				}
				EXONCAPBasicStatsPairedFile.execute(args_remain);
				// EXONCAPBasicStatsIndelPairedFile
			} else if (type.equals("-EXONCAPBasicStatsIndelPairedFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -EXONCAPBasicStatsIndelPairedFile "
									+ EXONCAPBasicStatsIndelPairedFile
											.parameter_info());
					System.exit(0);
				}
				EXONCAPBasicStatsIndelPairedFile.execute(args_remain);
				//
			} else if (type.equals("-STARMappingScriptGenerator")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -STARMappingScriptGenerator "
							+ STARMappingScriptGenerator.parameter_info());
					System.exit(0);
				}
				STARMappingScriptGenerator.execute(args_remain);
				// STARMappingScriptGeneratorForTrimFastq
			} else if (type.equals("-STARMappingScriptGeneratorForTrimFastq")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -STARMappingScriptGeneratorForTrimFastq "
									+ STARMappingScriptGeneratorForTrimFastq
											.parameter_info());
					System.exit(0);
				}
				STARMappingScriptGeneratorForTrimFastq.execute(args_remain);
				//
			} else if (type.equals("-SummarizeStarMapping")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeStarMapping "
							+ SummarizeStarMapping.parameter_info());
					System.exit(0);
				}
				SummarizeStarMapping.execute(args_remain);
				// SummarizeStarMappingMerge
			} else if (type.equals("-SummarizeStarMappingMerge")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeStarMappingMerge "
							+ SummarizeStarMappingMerge.parameter_info());
					System.exit(0);
				}
				SummarizeStarMappingMerge.execute(args_remain);
				//
			} else if (type.equals("-CuffLinksScriptGenerator")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CuffLinksScriptGenerator "
							+ CuffLinksScriptGenerator.parameter_info());
					System.exit(0);
				}
				CuffLinksScriptGenerator.execute(args_remain);
				// CombineHTSEQResult
			} else if (type.equals("-CombineHTSEQResult")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineHTSEQResult "
							+ CombineHTSEQResult.parameter_info());
					System.exit(0);
				}
				CombineHTSEQResult.execute(args_remain);
				// EnsemblGeneID2GeneName
			} else if (type.equals("-EnsemblGeneID2GeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EnsemblGeneID2GeneName "
							+ EnsemblGeneID2GeneName.parameter_info());
					System.exit(0);
				}
				EnsemblGeneID2GeneName.execute(args_remain);
				// EnsemblGeneIDAppendAnnotation
			} else if (type.equals("-EnsemblGeneIDAppendAnnotation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EnsemblGeneIDAppendAnnotation "
							+ EnsemblGeneIDAppendAnnotation.parameter_info());
					System.exit(0);
				}
				EnsemblGeneIDAppendAnnotation.execute(args_remain);
				// EnsemblGeneIDAppendAnnotation
			} else if (type.equals("-CleanEnsemblGeneID2GeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CleanEnsemblGeneID2GeneName "
							+ CleanEnsemblGeneID2GeneName.parameter_info());
					System.exit(0);
				}
				CleanEnsemblGeneID2GeneName.execute(args_remain);
				//
			} else if (type.equals("-EnsemblGeneID2GeneNameXenograft")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -EnsemblGeneID2GeneNameXenograft "
									+ EnsemblGeneID2GeneNameXenograft
											.parameter_info());
					System.exit(0);
				}
				EnsemblGeneID2GeneNameXenograft.execute(args_remain);
				//
			} else if (type.equals("-GenerateProteomeGeneMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateProteomeGeneMatrix "
							+ GenerateProteomeGeneMatrix.parameter_info());
					System.exit(0);
				}
				GenerateProteomeGeneMatrix.execute(args_remain);
				// GeneratePhosphoPeptideMatrix
			} else if (type.equals("-GeneratePhosphoPeptideMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneratePhosphoPeptideMatrix "
							+ GeneratePhosphoPeptideMatrix.parameter_info());
					System.exit(0);
				}
				GeneratePhosphoPeptideMatrix.execute(args_remain);
				// GenerateFPKMBinningTable
			} else if (type.equals("-GenerateFPKMBinningTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateFPKMBinningTable "
							+ GenerateFPKMBinningTable.parameter_info());
					System.exit(0);
				}
				GenerateFPKMBinningTable.execute(args_remain);
				// CalculateCentrality
			} else if (type.equals("-CalculateCentrality")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateCentrality "
							+ CalculateCentrality.parameter_info());
					System.exit(0);
				}
				CalculateCentrality.execute(args_remain);
				// kgXrefConversion
			} else if (type.equals("-kgXrefConversion")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -kgXrefConversion "
							+ kgXrefConversion.parameter_info());
					System.exit(0);
				}
				kgXrefConversion.execute(args_remain);
				// CombineSplicingDeficiencyName
			} else if (type.equals("-CombineSplicingDeficiencyName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineSplicingDeficiencyName "
							+ CombineSplicingDeficiencyName.parameter_info());
					System.exit(0);
				}
				CombineSplicingDeficiencyName.execute(args_remain);
				// CalculateResidueMotifBootstrap
			} else if (type.equals("-CalculateResidueMotifBootstrap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateResidueMotifBootstrap "
							+ CalculateResidueMotifBootstrap.parameter_info());
					System.exit(0);
				}
				CalculateResidueMotifBootstrap.execute(args_remain);
				// CalculateResidueMotifBootstrapDE
			} else if (type.equals("-CalculateResidueMotifBootstrapDE")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateResidueMotifBootstrapDE "
									+ CalculateResidueMotifBootstrapDE
											.parameter_info());
					System.exit(0);
				}
				CalculateResidueMotifBootstrapDE.execute(args_remain);
				// CalculateResidueMotifBootstrap4
			} else if (type.equals("-CalculateResidueMotifBootstrap4")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateResidueMotifBootstrap4 "
									+ CalculateResidueMotifBootstrap4
											.parameter_info());
					System.exit(0);
				}
				CalculateResidueMotifBootstrap4.execute(args_remain);
				// CalculateResidueMotifBootstrap4
			} else if (type.equals("-CalculateResidueMotifBootstrap3")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateResidueMotifBootstrap3 "
									+ CalculateResidueMotifBootstrap3
											.parameter_info());
					System.exit(0);
				}
				CalculateResidueMotifBootstrap3.execute(args_remain);
				// CalculateCentralityModifyDistance
			} else if (type.equals("-CalculateCentralityModifyDistance")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateCentralityModifyDistance "
									+ CalculateCentralityModifyDistance
											.parameter_info());
					System.exit(0);
				}
				CalculateCentralityModifyDistance.execute(args_remain);
				// GenerateBackgroundFrequencyTable
			} else if (type.equals("-GenerateBackgroundFrequencyTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateBackgroundFrequencyTable "
									+ GenerateBackgroundFrequencyTable
											.parameter_info());
					System.exit(0);
				}
				GenerateBackgroundFrequencyTable.execute(args_remain);
				// MergeFastQ
			} else if (type.equals("-MergeFastQ")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeFastQ "
							+ MergeFastQ.parameter_info());
					System.exit(0);
				}
				MergeFastQ.execute(args_remain);
				// NormalizePWMWithBackground
			} else if (type.equals("-NormalizePWMWithBackground")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizePWMWithBackground "
							+ NormalizePWMWithBackground.parameter_info());
					System.exit(0);
				}
				NormalizePWMWithBackground.execute(args_remain);
				// GeneratePSSMUniprotDatabase
			} else if (type.equals("-GeneratePSSMUniprotDatabase")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneratePSSMUniprotDatabase "
							+ GeneratePSSMUniprotDatabase.parameter_info());
					System.exit(0);
				}
				GeneratePSSMUniprotDatabase.execute(args_remain);
				// GenerateReferencePSSMTable
			} else if (type.equals("-GenerateReferencePSSMTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateReferencePSSMTable "
							+ GenerateReferencePSSMTable.parameter_info());
					System.exit(0);
				}
				GenerateReferencePSSMTable.execute(args_remain);
				// FilterBackground2CoreProtein
			} else if (type.equals("-FilterBackground2CoreProtein")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterBackground2CoreProtein "
							+ FilterBackground2CoreProtein.parameter_info());
					System.exit(0);
				}
				FilterBackground2CoreProtein.execute(args_remain);
				// PlotBinningTable
			} else if (type.equals("-PlotBinningTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PlotBinningTable "
							+ PlotBinningTable.parameter_info());
					System.exit(0);
				}
				PlotBinningTable.execute(args_remain);
				// IntronExonCoverageBED
			} else if (type.equals("-IntronExonCoverageBED")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -IntronExonCoverageBED "
							+ IntronExonCoverageBED.parameter_info());
					System.exit(0);
				}
				IntronExonCoverageBED.execute(args_remain);
				// GenerateRNASEQCoverageStatistics
			} else if (type.equals("-GenerateRNASEQCoverageStatistics")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateRNASEQCoverageStatistics "
									+ GenerateRNASEQCoverageStatistics
											.parameter_info());
					System.exit(0);
				}
				GenerateRNASEQCoverageStatistics.execute(args_remain);
				// CompileDataForViolinPlot
			} else if (type.equals("-CompileDataForViolinPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CompileDataForViolinPlot "
							+ CompileDataForViolinPlot.parameter_info());
					System.exit(0);
				}
				CompileDataForViolinPlot.execute(args_remain);
				// GrabGeneOverValue
			} else if (type.equals("-GrabGeneOverValue")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabGeneOverValue "
							+ GrabGeneOverValue.parameter_info());
					System.exit(0);
				}
				GrabGeneOverValue.execute(args_remain);
				// ZeroAnalysis
			} else if (type.equals("-GenerateZeroAnalysisBinningTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateZeroAnalysisBinningTable "
									+ GenerateZeroAnalysisBinningTable
											.parameter_info());
					System.exit(0);
				}

				GenerateZeroAnalysisBinningTable.execute(args_remain);
				// BoxPlotGeneratorTwoColumn
			} else if (type.equals("-BoxPlotGeneratorTwoColumn")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BoxPlotGeneratorTwoColumn "
							+ BoxPlotGeneratorTwoColumn.parameter_info());
					System.exit(0);
				}
				BoxPlotGeneratorTwoColumn.execute(args_remain);
				// SpearmanRankCorrelation
			} else if (type.equals("-SpearmanRankCorrelation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SpearmanRankCorrelation "
							+ SpearmanRankCorrelation.parameter_info());
					System.exit(0);
				}
				SpearmanRankCorrelation.execute(args_remain);
				// GrabGeneLessThanValue
			} else if (type.equals("-GrabGeneLessThanValue")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabGeneLessThanValue "
							+ GrabGeneLessThanValue.parameter_info());
					System.exit(0);
				}
				GrabGeneLessThanValue.execute(args_remain);
				// OverlapMouseHumanGeneName
			} else if (type.equals("-OverlapMouseHumanGeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OverlapMouseHumanGeneName "
							+ OverlapMouseHumanGeneName.parameter_info());
					System.exit(0);
				}
				OverlapMouseHumanGeneName.execute(args_remain);
				// OverlapAllMouseHuman
			} else if (type.equals("-OverlapAllMouseHuman")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OverlapAllMouseHuman "
							+ OverlapAllMouseHuman.parameter_info());
					System.exit(0);
				}
				OverlapAllMouseHuman.execute(args_remain);
				// PSSMScoreDistributionKinaseMotif
			} else if (type.equals("-PSSMScoreDistributionKinaseMotif")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PSSMScoreDistributionKinaseMotif "
									+ PSSMScoreDistributionKinaseMotif
											.parameter_info());
					System.exit(0);
				}
				PSSMScoreDistributionKinaseMotif.execute(args_remain);
				//
			} else if (type.equals("-PSSMScoreDistribution")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PSSMScoreDistribution "
							+ PSSMScoreDistribution.parameter_info());
					System.exit(0);
				}
				PSSMScoreDistribution.execute(args_remain);
				// RandomSelectionPSSM
			} else if (type.equals("-RandomSelectionPSSM")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RandomSelectionPSSM "
							+ RandomSelectionPSSM.parameter_info());
					System.exit(0);
				}
				RandomSelectionPSSM.execute(args_remain);
				// AppendPSSMScore2Matrix
			} else if (type.equals("-AppendPSSMScore2Matrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendPSSMScore2Matrix "
							+ AppendPSSMScore2Matrix.parameter_info());
					System.exit(0);
				}
				AppendPSSMScore2Matrix.execute(args_remain);
				// AppendPSSMScore2PhosphoSiteMatrix
			} else if (type.equals("-AppendPSSMScore2PhosphoSiteMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendPSSMScore2PhosphoSiteMatrix "
									+ AppendPSSMScore2PhosphoSiteMatrix
											.parameter_info());
					System.exit(0);
				}
				AppendPSSMScore2PhosphoSiteMatrix.execute(args_remain);
				//
			} else if (type.equals("-PSSMCreateSupplementaryTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PSSMCreateSupplementaryTable "
							+ PSSMCreateSupplementaryTable.parameter_info());
					System.exit(0);
				}
				PSSMCreateSupplementaryTable.execute(args_remain);
				// AssignKnownKinaseSubstrateSupplementary
			} else if (type.equals("-AssignKnownKinaseSubstrateSupplementary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AssignKnownKinaseSubstrateSupplementary "
									+ AssignKnownKinaseSubstrateSupplementary
											.parameter_info());
					System.exit(0);
				}
				AssignKnownKinaseSubstrateSupplementary.execute(args_remain);
				// RemoveColumnsFromMatrix
			} else if (type.equals("-RemoveColumnsFromMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RemoveColumnsFromMatrix "
							+ RemoveColumnsFromMatrix.parameter_info());
					System.exit(0);
				}
				RemoveColumnsFromMatrix.execute(args_remain);
				// MergeRowsMaximizePSM
			} else if (type.equals("-MergeRowsMaximizePSM")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeRowsMaximizePSM "
							+ MergeRowsMaximizePSM.parameter_info());
					System.exit(0);
				}
				MergeRowsMaximizePSM.execute(args_remain);
				// AssignKnownKinaseSubstrateRelationshipHongbo
			} else if (type
					.equals("-AssignKnownKinaseSubstrateRelationshipHongbo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AssignKnownKinaseSubstrateRelationshipHongbo "
									+ AssignKnownKinaseSubstrateRelationshipHongbo
											.parameter_info());
					System.exit(0);
				}
				AssignKnownKinaseSubstrateRelationshipHongbo
						.execute(args_remain);
				// GenerateMm9SNVIndelScript
			} else if (type.equals("-GenerateMm9SNVIndelScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMm9SNVIndelScript "
							+ GenerateMm9SNVIndelScript.parameter_info());
					System.exit(0);
				}
				GenerateMm9SNVIndelScript.execute(args_remain);
				// ComprehensiveSummaryTableSampleTypeSNVFusion
			} else if (type
					.equals("-ComprehensiveSummaryTableSampleTypeSNVFusion")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ComprehensiveSummaryTableSampleTypeSNVFusion "
									+ ComprehensiveSummaryTableSampleTypeSNVFusion
											.parameter_info());
					System.exit(0);
				}
				ComprehensiveSummaryTableSampleTypeSNVFusion
						.execute(args_remain);
				// FilterSNVSamples
			} else if (type.equals("-FilterSNVSamples")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterSNVSamples "
							+ FilterSNVSamples.parameter_info());
					System.exit(0);
				}
				FilterSNVSamples.execute(args_remain);
				// RefSeq2GeneName
			} else if (type.equals("-RefSeq2GeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RefSeq2GeneName "
							+ RefSeq2GeneName.parameter_info());
					System.exit(0);
				}
				RefSeq2GeneName.execute(args_remain);
				// ComprehensiveSummaryTableSampleTypeSNVFusionFilter
			} else if (type
					.equals("-ComprehensiveSummaryTableSampleTypeSNVFusionFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ComprehensiveSummaryTableSampleTypeSNVFusionFilter "
									+ ComprehensiveSummaryTableSampleTypeSNVFusionFilter
											.parameter_info());
					System.exit(0);
				}
				ComprehensiveSummaryTableSampleTypeSNVFusionFilter
						.execute(args_remain);
				// FilterKinaseBasedOnFrequency
			} else if (type.equals("-FilterKinaseBasedOnFrequency")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterKinaseBasedOnFrequency "
							+ FilterKinaseBasedOnFrequency.parameter_info());
					System.exit(0);
				}
				FilterKinaseBasedOnFrequency.execute(args_remain);
				// FilterPSMInformationPeptide
			} else if (type.equals("-FilterPSMInformationPeptide")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterPSMInformationPeptide "
							+ FilterPSMInformationPeptide.parameter_info());
					System.exit(0);
				}
				FilterPSMInformationPeptide.execute(args_remain);
				// FilterPSMInformationProteinName
			} else if (type.equals("-FilterPSMInformationProteinName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FilterPSMInformationProteinName "
									+ FilterPSMInformationProteinName
											.parameter_info());
					System.exit(0);
				}
				FilterPSMInformationProteinName.execute(args_remain);
				// ExtractUniqPeptides
			} else if (type.equals("-ExtractUniqPeptides")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractUniqPeptides "
							+ ExtractUniqPeptides.parameter_info());
					System.exit(0);
				}
				ExtractUniqPeptides.execute(args_remain);
				// OverlapDEGeneSet
			} else if (type.equals("-OverlapDEGeneSet")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OverlapDEGeneSet "
							+ OverlapDEGeneSet.parameter_info());
					System.exit(0);
				}
				OverlapDEGeneSet.execute(args_remain);
				// CombineDEGeneSet
			} else if (type.equals("-CombineDEGeneSet")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineDEGeneSet "
							+ CombineDEGeneSet.parameter_info());
					System.exit(0);
				}
				CombineDEGeneSet.execute(args_remain);
				// CombineDEGeneSetLimitOverlap
			} else if (type.equals("-CombineDEGeneSetLimitOverlap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineDEGeneSetLimitOverlap "
							+ CombineDEGeneSetLimitOverlap.parameter_info());
					System.exit(0);
				}
				CombineDEGeneSetLimitOverlap.execute(args_remain);
				//
			} else if (type.equals("-IntegrationAddGeneAnnotation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -IntegrationAddGeneAnnotation "
							+ IntegrationAddGeneAnnotation.parameter_info());
					System.exit(0);
				}
				IntegrationAddGeneAnnotation.execute(args_remain);
				// IntegratedSummaryTable
			} else if (type.equals("-IntegratedSummaryTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -IntegratedSummaryTable "
							+ IntegratedSummaryTable.parameter_info());
					System.exit(0);
				}
				IntegratedSummaryTable.execute(args_remain);
				// ExtractDEGenes
			} else if (type.equals("-ExtractDEGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractDEGenes "
							+ ExtractDEGenes.parameter_info());
					System.exit(0);
				}
				ExtractDEGenes.execute(args_remain);
				// IntegrationDrawerFilterGeneList
			} else if (type.equals("-IntegrationDrawerFilterGeneList")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -IntegrationDrawerFilterGeneList "
									+ IntegrationDrawerFilterGeneList
											.parameter_info());
					System.exit(0);
				}
				IntegrationDrawerFilterGeneList.execute(args_remain);
				// Filter3PrimeGTFExon
			} else if (type.equals("-Filter3PrimeGTFExon")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Filter3PrimeGTFExon "
							+ Filter3PrimeGTFExon.parameter_info());
					System.exit(0);
				}
				Filter3PrimeGTFExon.execute(args_remain);
				// RPM2RPKMTranscript
			} else if (type.equals("-RPM2RPKMTranscript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RPM2RPKMTranscript "
							+ RPM2RPKMTranscript.parameter_info());
					System.exit(0);
				}
				RPM2RPKMTranscript.execute(args_remain);
				// RPM2RPKMExon
			} else if (type.equals("-RPM2RPKMExon")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RPM2RPKMExon "
							+ RPM2RPKMExon.parameter_info());
					System.exit(0);
				}
				RPM2RPKMExon.execute(args_remain);
				// RPM2FPKMGenCode
			} else if (type.equals("-RPM2FPKMGenCode")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RPM2FPKMGenCode "
							+ RPM2FPKMGenCode.parameter_info());
					System.exit(0);
				}
				RPM2FPKMGenCode.execute(args_remain);
				//
			} else if (type.equals("-BarPlotGenerator")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BarPlotGenerator "
							+ BarPlotGenerator.parameter_info());
					System.exit(0);
				}
				BarPlotGenerator.execute(args_remain);
				// MultipleBarPlotGenerator
			} else if (type.equals("-MultipleBarPlotGenerator")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MultipleBarPlotGenerator "
							+ MultipleBarPlotGenerator.parameter_info());
					System.exit(0);
				}
				MultipleBarPlotGenerator.execute(args_remain);
				// GenerateBlastFile
			} else if (type.equals("-GenerateBlastFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateBlastFile "
							+ GenerateBlastFile.parameter_info());
					System.exit(0);
				}
				GenerateBlastFile.execute(args_remain);
				// FromSV2CircosInput
			} else if (type.equals("-FromSV2CircosInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FromSV2CircosInput "
							+ FromSV2CircosInput.parameter_info());
					System.exit(0);
				}
				FromSV2CircosInput.execute(args_remain);
				// SNV2CircosInput
			} else if (type.equals("-SNV2CircosInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SNV2CircosInput "
							+ SNV2CircosInput.parameter_info());
					System.exit(0);
				}
				SNV2CircosInput.execute(args_remain);
				//
			} else if (type.equals("-SNV2CircosInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SNV2CircosInput "
							+ SNV2CircosInput.parameter_info());
					System.exit(0);
				}
				SNV2CircosInput.execute(args_remain);
				// SV2CircosInput
			} else if (type.equals("-SV2CircosInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SV2CircosInput "
							+ SV2CircosInput.parameter_info());
					System.exit(0);
				}
				SV2CircosInput.execute(args_remain);
				// Indel2CircosInput
			} else if (type.equals("-Indel2CircosInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Indel2CircosInput "
							+ Indel2CircosInput.parameter_info());
					System.exit(0);
				}
				Indel2CircosInput.execute(args_remain);
				// GCScanner
			} else if (type.equals("-GCScanner")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GCScanner "
							+ GCScanner.parameter_info());
					System.exit(0);
				}
				GCScanner.execute(args_remain);
				// SingleCellSequencingMapping
			} else if (type.equals("-SingleCellRNAseqMapAndQuan")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SingleCellRNAseqMapAndQuan "
							+ SingleCellRNAseqMapAndQuan.parameter_info());
					System.exit(0);
				}
				SingleCellRNAseqMapAndQuan.execute(args_remain);
				// RemoveNAGenes
			} else if (type.equals("-RemoveNAGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RemoveNAGenes "
							+ RemoveNAGenes.parameter_info());
					System.exit(0);
				}
				RemoveNAGenes.execute(args_remain);
				// CombineHTSEQResultRaw
			} else if (type.equals("-CombineHTSEQResultRaw")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineHTSEQResultRaw "
							+ CombineHTSEQResultRaw.parameter_info());
					System.exit(0);
				}
				CombineHTSEQResultRaw.execute(args_remain);
				// Fastq2FileList
			} else if (type.equals("-Fastq2FileList")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Fastq2FileList "
							+ Fastq2FileList.parameter_info());
					System.exit(0);
				}
				Fastq2FileList.execute(args_remain);
				// MergeBamFiles
			} else if (type.equals("-MergeBamFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeBamFiles "
							+ MergeBamFiles.parameter_info());
					System.exit(0);
				}
				MergeBamFiles.execute(args_remain);
				// RNASEQConfig2MappingScriptGenerator
			} else if (type.equals("-RNASEQConfig2MappingScriptGenerator")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -RNASEQConfig2MappingScriptGenerator "
									+ RNASEQConfig2MappingScriptGenerator
											.parameter_info());
					System.exit(0);
				}
				RNASEQConfig2MappingScriptGenerator.execute(args_remain);
				// GenerateCircosCoverageBed
			} else if (type.equals("-GenerateCircosCoverageBed")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateCircosCoverageBed "
							+ GenerateCircosCoverageBed.parameter_info());
					System.exit(0);
				}
				GenerateCircosCoverageBed.execute(args_remain);
				// GenerateLIMMAComparisonScript
			} else if (type.equals("-GenerateLIMMAComparisonScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateLIMMAComparisonScript "
							+ GenerateLIMMAComparisonScript.parameter_info());
					System.exit(0);
				}
				GenerateLIMMAComparisonScript.execute(args_remain);
				// GenerateFqFileList
			} else if (type.equals("-GenerateFqFileList")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateFqFileList "
							+ GenerateFqFileList.parameter_info());
					System.exit(0);
				}
				GenerateFqFileList.execute(args_remain);
				// GenerateFqFileListParallel
			} else if (type.equals("-GenerateFqFileListParallel")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateFqFileListParallel "
							+ GenerateFqFileListParallel.parameter_info());
					System.exit(0);
				}
				GenerateFqFileListParallel.execute(args_remain);
				// MatchFq2Bam
			} else if (type.equals("-MatchFq2Bam")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MatchFq2Bam "
							+ MatchFq2Bam.parameter_info());
					System.exit(0);
				}
				MatchFq2Bam.execute(args_remain);
				// AddChr
			} else if (type.equals("-AddChr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AddChr "
							+ AddChr.parameter_info());
					System.exit(0);
				}
				AddChr.execute(args_remain);
				// TrimmomaticScriptGenerator
			} else if (type.equals("-TrimmomaticScriptGenerator")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -TrimmomaticScriptGenerator "
							+ TrimmomaticScriptGenerator.parameter_info());
					System.exit(0);
				}
				TrimmomaticScriptGenerator.execute(args_remain);
				// ValidateSTARMapping
			} else if (type.equals("-ValidateSTARMapping")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ValidateSTARMapping "
							+ ValidateSTARMapping.parameter_info());
					System.exit(0);
				}
				ValidateSTARMapping.execute(args_remain);
				// CombineFastqFiles
			} else if (type.equals("-CombineFastqFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineFastqFiles "
							+ CombineFastqFiles.parameter_info());
					System.exit(0);
				}
				CombineFastqFiles.execute(args_remain);
				// SingleCellRNAseqMapAndQuanReg
			} else if (type.equals("-SingleCellRNAseqMapAndQuanReg")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SingleCellRNAseqMapAndQuanReg "
							+ SingleCellRNAseqMapAndQuanReg.parameter_info());
					System.exit(0);
				}
				SingleCellRNAseqMapAndQuanReg.execute(args_remain);
				// IntegratedSummaryTableFrequencyCount
			} else if (type.equals("-IntegratedSummaryTableFrequencyCount")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -IntegratedSummaryTableFrequencyCount "
									+ IntegratedSummaryTableFrequencyCount
											.parameter_info());
					System.exit(0);
				}
				IntegratedSummaryTableFrequencyCount.execute(args_remain);
				// FilterDuplicatedHits
			} else if (type.equals("-FilterDuplicatedHits")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterDuplicatedHits "
							+ FilterDuplicatedHits.parameter_info());
					System.exit(0);
				}
				FilterDuplicatedHits.execute(args_remain);
				// CombinePCPAResults
			} else if (type.equals("-CombinePCPAResults")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombinePCPAResults "
							+ CombinePCPAResults.parameter_info());
					System.exit(0);
				}
				CombinePCPAResults.execute(args_remain);
				// PCPAAppendMetaDeta
			} else if (type.equals("-PCPAAppendMetaDeta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PCPAAppendMetaDeta "
							+ PCPAAppendMetaDeta.parameter_info());
					System.exit(0);
				}
				PCPAAppendMetaDeta.execute(args_remain);
				// ParseThroughSIF
			} else if (type.equals("-ParseThroughSIF")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ParseThroughSIF "
							+ ParseThroughSIF.parameter_info());
					System.exit(0);
				}
				ParseThroughSIF.execute(args_remain);
				// NormalizeMatrix2IKAPARMERMS
			} else if (type.equals("-NormalizeMatrix2IKAPARMSERMS")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizeMatrix2IKAPARMSERMS "
							+ NormalizeMatrix2IKAPARMSERMS.parameter_info());
					System.exit(0);
				}
				NormalizeMatrix2IKAPARMSERMS.execute(args_remain);
				// NormalizeWholeMatrixARMSERMS
			} else if (type.equals("-NormalizeWholeMatrixARMSERMS")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizeWholeMatrixARMSERMS "
							+ NormalizeWholeMatrixARMSERMS.parameter_info());
					System.exit(0);
				}
				NormalizeWholeMatrixARMSERMS.execute(args_remain);
				// NormalizePhosphoAgainstWholeARMSERMS
			} else if (type.equals("-NormalizePhosphoAgainstWholeARMSERMS")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -NormalizePhosphoAgainstWholeARMSERMS "
									+ NormalizePhosphoAgainstWholeARMSERMS
											.parameter_info());
					System.exit(0);
				}
				NormalizePhosphoAgainstWholeARMSERMS.execute(args_remain);
				// CustomFastaCombiner
			} else if (type.equals("-CustomFastaCombiner")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CustomFastaCombiner "
							+ CustomFastaCombiner.parameter_info());
					System.exit(0);
				}
				CustomFastaCombiner.execute(args_remain);
				// Mouse2GTF
			} else if (type.equals("-Mouse2GTF")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Mouse2GTF "
							+ Mouse2GTF.parameter_info());
					System.exit(0);
				}
				Mouse2GTF.execute(args_remain);
				// GenerateSNVTableFromMutationTable
			} else if (type.equals("-GenerateSNVTableFromMutationTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateSNVTableFromMutationTable "
									+ GenerateSNVTableFromMutationTable
											.parameter_info());
					System.exit(0);
				}
				GenerateSNVTableFromMutationTable.execute(args_remain);
				// PostProcessingOfVariantMatrix
			} else if (type.equals("-PostProcessingOfVariantMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PostProcessingOfVariantMatrix "
							+ PostProcessingOfVariantMatrix.parameter_info());
					System.exit(0);
				}
				PostProcessingOfVariantMatrix.execute(args_remain);
				// kgXrefConversionProtein2GeneName
			} else if (type.equals("-kgXrefConversionProtein2GeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -kgXrefConversionProtein2GeneName "
									+ kgXrefConversionProtein2GeneName
											.parameter_info());
					System.exit(0);
				}
				kgXrefConversionProtein2GeneName.execute(args_remain);
				// SpearmanRankCorrelationMatrix
			} else if (type.equals("-SpearmanRankCorrelationMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SpearmanRankCorrelationMatrix "
							+ SpearmanRankCorrelationMatrix.parameter_info());
					System.exit(0);
				}
				SpearmanRankCorrelationMatrix.execute(args_remain);
				// OpenReadingFrameFinder
			} else if (type.equals("-OpenReadingFrameFinder")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OpenReadingFrameFinder "
							+ OpenReadingFrameFinder.parameter_info());
					System.exit(0);
				}
				OpenReadingFrameFinder.execute(args_remain);
				// FisherExactTest2groupcomparison
			} else if (type.equals("-FisherExactTest2groupcomparison")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -FisherExactTest2groupcomparison "
									+ FisherExactTest2groupcomparison
											.parameter_info());
					System.exit(0);
				}
				FisherExactTest2groupcomparison.execute(args_remain);
				// GenerateMatrixForTwoGroups
			} else if (type.equals("-GenerateMatrixForTwoGroups")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMatrixForTwoGroups "
							+ GenerateMatrixForTwoGroups.parameter_info());
					System.exit(0);
				}
				GenerateMatrixForTwoGroups.execute(args_remain);
				// GenerateSIFfromMinimumSpanningTree
			} else if (type.equals("-GenerateSIFfromMinimumSpanningTree")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateSIFfromMinimumSpanningTree "
									+ GenerateSIFfromMinimumSpanningTree
											.parameter_info());
					System.exit(0);
				}
				GenerateSIFfromMinimumSpanningTree.execute(args_remain);
				// GenerateNodeMetaBasedOnGroups
			} else if (type.equals("-GenerateNodeMetaBasedOnGroups")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateNodeMetaBasedOnGroups "
							+ GenerateNodeMetaBasedOnGroups.parameter_info());
					System.exit(0);
				}
				GenerateNodeMetaBasedOnGroups.execute(args_remain);
				// GeneratePCAScatterPlotPython
			} else if (type.equals("-GeneratePCAScatterPlotPython")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneratePCAScatterPlotPython "
							+ GeneratePCAScatterPlotPython.parameter_info());
					System.exit(0);
				}
				GeneratePCAScatterPlotPython.execute(args_remain);
				// SpecialClassForDougGreen
			} else if (type.equals("-SpecialClassForDougGreen")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SpecialClassForDougGreen "
							+ SpecialClassForDougGreen.parameter_info());
					System.exit(0);
				}
				SpecialClassForDougGreen.execute(args_remain);
				// SeparateGeneMatrixIntoTwo
			} else if (type.equals("-SeparateGeneMatrixIntoTwo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SeparateGeneMatrixIntoTwo "
							+ SeparateGeneMatrixIntoTwo.parameter_info());
					System.exit(0);
				}
				SeparateGeneMatrixIntoTwo.execute(args_remain);
				// CombineSingleCellSampleIntoOne
			} else if (type.equals("-CombineSingleCellSampleIntoOne")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineSingleCellSampleIntoOne "
							+ CombineSingleCellSampleIntoOne.parameter_info());
					System.exit(0);
				}
				CombineSingleCellSampleIntoOne.execute(args_remain);
				// SubGeneFromConversionTable
			} else if (type.equals("-SubGeneFromConversionTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SubGeneFromConversionTable "
							+ SubGeneFromConversionTable.parameter_info());
					System.exit(0);
				}
				SubGeneFromConversionTable.execute(args_remain);
				// GeneName2EnsemblID
			} else if (type.equals("-GeneName2EnsemblID")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneName2EnsemblID "
							+ GeneName2EnsemblID.parameter_info());
					System.exit(0);
				}
				GeneName2EnsemblID.execute(args_remain);
				// AppendChromosomeNumber
			} else if (type.equals("-AppendChromosomeNumber")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendChromosomeNumber "
							+ AppendChromosomeNumber.parameter_info());
					System.exit(0);
				}
				AppendChromosomeNumber.execute(args_remain);
				// GenerateLowComplexityDomainInfo
			} else if (type.equals("-GenerateLowComplexityDomainInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateLowComplexityDomainInfo "
									+ GenerateLowComplexityDomainInfo
											.parameter_info());
					System.exit(0);
				}
				GenerateLowComplexityDomainInfo.execute(args_remain);
				// ElenaConvertRefSeq2GeneName
			} else if (type.equals("-ElenaConvertRefSeq2GeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ElenaConvertRefSeq2GeneName "
							+ ElenaConvertRefSeq2GeneName.parameter_info());
					System.exit(0);
				}
				ElenaConvertRefSeq2GeneName.execute(args_remain);
				// GenerateHg19SNVIndelScript
			} else if (type.equals("-GenerateHg19SNVIndelScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateHg19SNVIndelScript "
							+ GenerateHg19SNVIndelScript.parameter_info());
					System.exit(0);
				}
				GenerateHg19SNVIndelScript.execute(args_remain);
				// GenerateGRCh37liteSNVIndelScript
			} else if (type.equals("-GenerateGRCh37liteSNVIndelScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateGRCh37liteSNVIndelScript "
									+ GenerateGRCh37liteSNVIndelScript
											.parameter_info());
					System.exit(0);
				}
				GenerateGRCh37liteSNVIndelScript.execute(args_remain);
				// GenerateSNVUnpairedScriptSimple
			} else if (type.equals("-GenerateSNVUnpairedScriptSimple")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateSNVUnpairedScriptSimple "
									+ GenerateSNVUnpairedScriptSimple
											.parameter_info());
					System.exit(0);
				}
				GenerateSNVUnpairedScriptSimple.execute(args_remain);
				// VariantMatrixBootstrap
			} else if (type.equals("-VariantMatrixBootstrap")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -VariantMatrixBootstrap "
							+ VariantMatrixBootstrap.parameter_info());
					System.exit(0);
				}
				VariantMatrixBootstrap.execute(args_remain);
				// Filter0PSamples
			} else if (type.equals("-Filter0PSamples")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Filter0PSamples "
							+ Filter0PSamples.parameter_info());
					System.exit(0);
				}
				Filter0PSamples.execute(args_remain);
				// GenerateTrueFalseMatrix
			} else if (type.equals("-GenerateTrueFalseMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateTrueFalseMatrix "
							+ GenerateTrueFalseMatrix.parameter_info());
					System.exit(0);
				}
				GenerateTrueFalseMatrix.execute(args_remain);
				// DisplayJsonFileNetwork
			} else if (type.equals("-DisplayJsonFileNetwork")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -DisplayJsonFileNetwork "
							+ DisplayJsonFileNetwork.parameter_info());
					System.exit(0);
				}
				DisplayJsonFileNetwork.execute(args_remain);
				// GenerateLayoutForEachHub
			} else if (type.equals("-GenerateLayoutForEachHub")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateLayoutForEachHub "
							+ GenerateLayoutForEachHub.parameter_info());
					System.exit(0);
				}
				GenerateLayoutForEachHub.execute(args_remain);
				// NormalizeMatrix2IKAPFlex
			} else if (type.equals("-NormalizeMatrix2IKAPFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizeMatrix2IKAPFlex "
							+ NormalizeMatrix2IKAPFlex.parameter_info());
					System.exit(0);
				}
				NormalizeMatrix2IKAPFlex.execute(args_remain);
				// NormalizeWholeGenomeFlex
			} else if (type.equals("-NormalizeWholeGenomeFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizeWholeGenomeFlex "
							+ NormalizeWholeGenomeFlex.parameter_info());
					System.exit(0);
				}
				NormalizeWholeGenomeFlex.execute(args_remain);
				// NormalizePhosphoAgainstWholeFlex
			} else if (type.equals("-NormalizePhosphoAgainstWholeFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -NormalizePhosphoAgainstWholeFlex "
									+ NormalizePhosphoAgainstWholeFlex
											.parameter_info());
					System.exit(0);
				}
				NormalizePhosphoAgainstWholeFlex.execute(args_remain);
				// AssignKnownKinaseSubstrateRelationshipFlex
			} else if (type
					.equals("-AssignKnownKinaseSubstrateRelationshipFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AssignKnownKinaseSubstrateRelationshipFlex "
									+ AssignKnownKinaseSubstrateRelationshipFlex
											.parameter_info());
					System.exit(0);
				}
				AssignKnownKinaseSubstrateRelationshipFlex.execute(args_remain);
				// SNPrsPopulation
			} else if (type.equals("-SNPrsPopulation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SNPrsPopulation "
							+ SNPrsPopulation.parameter_info());
					System.exit(0);
				}
				SNPrsPopulation.execute(args_remain);
				// GenerateFastaFileFromJUMPqSite
			} else if (type.equals("-GenerateFastaFileFromJUMPqSite")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateFastaFileFromJUMPqSite "
							+ GenerateFastaFileFromJUMPqSite.parameter_info());
					System.exit(0);
				}
				GenerateFastaFileFromJUMPqSite.execute(args_remain);
				// ExtendJUMPqSite
			} else if (type.equals("-ExtendJUMPqSite")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtendJUMPqSite "
							+ ExtendJUMPqSite.parameter_info());
					System.exit(0);
				}
				ExtendJUMPqSite.execute(args_remain);
				// GenerateFastaFileFromJUMPqPeptide
			} else if (type.equals("-GenerateFastaFileFromJUMPqPeptide")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateFastaFileFromJUMPqPeptide "
									+ GenerateFastaFileFromJUMPqPeptide
											.parameter_info());
					System.exit(0);
				}
				GenerateFastaFileFromJUMPqPeptide.execute(args_remain);
				// HongboAnnotateMotifInformation
			} else if (type.equals("-HongboAnnotateMotifInformation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -HongboAnnotateMotifInformation "
							+ HongboAnnotateMotifInformation.parameter_info());
					System.exit(0);
				}
				HongboAnnotateMotifInformation.execute(args_remain);
				// HongboAnnotateMotifInformationYuxinFile
			} else if (type.equals("-HongboAnnotateMotifInformationYuxinFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -HongboAnnotateMotifInformationYuxinFile "
									+ HongboAnnotateMotifInformationYuxinFile
											.parameter_info());
					System.exit(0);
				}
				HongboAnnotateMotifInformationYuxinFile.execute(args_remain);
				//
			} else if (type.equals("-SummarizeLeventakiProject")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeLeventakiProject "
							+ SummarizeLeventakiProject.parameter_info());
					System.exit(0);
				}
				SummarizeLeventakiProject.execute(args_remain);
				// CleanBioplexTSVFile
			} else if (type.equals("-CleanBioplexTSVFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CleanBioplexTSVFile "
							+ CleanBioplexTSVFile.parameter_info());
					System.exit(0);
				}
				CleanBioplexTSVFile.execute(args_remain);
				// PathwayKappaScore
			} else if (type.equals("-PathwayKappaScore")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PathwayKappaScore "
							+ PathwayKappaScore.parameter_info());
					System.exit(0);
				}
				PathwayKappaScore.execute(args_remain);
				// EstimatingTotalCoverage
			} else if (type.equals("-EstimatingTotalCoverage")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EstimatingTotalCoverage "
							+ EstimatingTotalCoverage.parameter_info());
					System.exit(0);
				}
				EstimatingTotalCoverage.execute(args_remain);
				// GTFFileAddRemoveChr
			} else if (type.equals("-GTFFileAddRemoveChr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GTFFileAddRemoveChr "
							+ GTFFileAddRemoveChr.parameter_info());
					System.exit(0);
				}
				GTFFileAddRemoveChr.execute(args_remain);
				// FastaAddRemoveChr
			} else if (type.equals("-FastaAddRemoveChr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FastaAddRemoveChr "
							+ FastaAddRemoveChr.parameter_info());
					System.exit(0);
				}
				FastaAddRemoveChr.execute(args_remain);
				// OverlapLIMMAAndExonJunctionCount
			} else if (type.equals("-OverlapLIMMAAndExonJunctionCount")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -OverlapLIMMAAndExonJunctionCount "
									+ OverlapLIMMAAndExonJunctionCount
											.parameter_info());
					System.exit(0);
				}
				OverlapLIMMAAndExonJunctionCount.execute(args_remain);
				// JunctionVsGeneJunc
			} else if (type.equals("-JunctionVsGeneJunc")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -JunctionVsGeneJunc "
							+ JunctionVsGeneJunc.parameter_info());
					System.exit(0);
				}
				JunctionVsGeneJunc.execute(args_remain);
				// GrabDifferentiatedJunctions
			} else if (type.equals("-GrabDifferentiatedJunctions")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabDifferentiatedJunctions "
							+ GrabDifferentiatedJunctions.parameter_info());
					System.exit(0);
				}
				GrabDifferentiatedJunctions.execute(args_remain);
				// GenerateMatrixForMutationalSignature
			} else if (type.equals("-GenerateMatrixForMutationalSignature")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateMatrixForMutationalSignature "
									+ GenerateMatrixForMutationalSignature
											.parameter_info());
					System.exit(0);
				}
				GenerateMatrixForMutationalSignature.execute(args_remain);
				// JUMPqWhlProteome2Matrix
			} else if (type.equals("-JUMPqWhlProteome2Matrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -JUMPqWhlProteome2Matrix "
							+ JUMPqWhlProteome2Matrix.parameter_info());
					System.exit(0);
				}
				JUMPqWhlProteome2Matrix.execute(args_remain);
				// JUMPqPhoProteome2Matrix
			} else if (type.equals("-JUMPqPhoProteome2Matrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -JUMPqPhoProteome2Matrix "
							+ JUMPqPhoProteome2Matrix.parameter_info());
					System.exit(0);
				}
				JUMPqPhoProteome2Matrix.execute(args_remain);
				// WhoPhoSpearmanRankCorrelation
			} else if (type.equals("-WhlPhoSpearmanRankCorrelation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -WhlPhoSpearmanRankCorrelation "
							+ WhlPhoSpearmanRankCorrelation.parameter_info());
					System.exit(0);
				}
				WhlPhoSpearmanRankCorrelation.execute(args_remain);
				// CombinePhosphositeCorrelationResult
			} else if (type.equals("-CombinePhosphositeCorrelationResult")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CombinePhosphositeCorrelationResult "
									+ CombinePhosphositeCorrelationResult
											.parameter_info());
					System.exit(0);
				}
				CombinePhosphositeCorrelationResult.execute(args_remain);
				// AppendKinaseTargetInformation2Matrix
			} else if (type.equals("-AppendKinaseTargetInformation2Matrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendKinaseTargetInformation2Matrix "
									+ AppendKinaseTargetInformation2Matrix
											.parameter_info());
					System.exit(0);
				}
				AppendKinaseTargetInformation2Matrix.execute(args_remain);
				// PhoFilterKinaseFunctionalRole
			} else if (type.equals("-PhoFilterKinaseFunctionalRole")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PhoFilterKinaseFunctionalRole "
							+ PhoFilterKinaseFunctionalRole.parameter_info());
					System.exit(0);
				}
				PhoFilterKinaseFunctionalRole.execute(args_remain);
				// DegradationPhosphositeRegForAll
			} else if (type.equals("-DegradationPhosphositeRegForAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -DegradationPhosphositeRegForAll "
									+ DegradationPhosphositeRegForAll
											.parameter_info());
					System.exit(0);
				}
				DegradationPhosphositeRegForAll.execute(args_remain);
				// AppendFunctionalInformation2Matrix
			} else if (type.equals("-AppendFunctionalInformation2Matrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendFunctionalInformation2Matrix "
									+ AppendFunctionalInformation2Matrix
											.parameter_info());
					System.exit(0);
				}
				AppendFunctionalInformation2Matrix.execute(args_remain);
				// SummarizeResultsAfterMATSFilterDisplayGeneList
			} else if (type.equals("-SummarizeResultsAfterMATSFilterDisplayGeneList")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SummarizeResultsAfterMATSFilterDisplayGeneList "
									+ SummarizeResultsAfterMATSFilterDisplayGeneList
											.parameter_info());
					System.exit(0);
				}
				SummarizeResultsAfterMATSFilterDisplayGeneList.execute(args_remain);
				// 
			} else if (type
					.equals("-SummarizeResultsAfterMATSFilterGeneMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SummarizeResultsAfterMATSFilterGeneMatrix "
									+ SummarizeResultsAfterMATSFilterGeneMatrix
											.parameter_info());
					System.exit(0);
				}
				SummarizeResultsAfterMATSFilterGeneMatrix.execute(args_remain);
				// SummarizeMATSSummary
			} else if (type
					.equals("-SummarizeMATSSummary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SummarizeMATSSummary "
									+ SummarizeMATSSummary
											.parameter_info());
					System.exit(0);
				}
				SummarizeMATSSummary.execute(args_remain);
				// 
			} else if (type.equals("-ExtractRandomFastaSequence")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractRandomFastaSequence "
							+ ExtractRandomFastaSequence.parameter_info());
					System.exit(0);
				}
				ExtractRandomFastaSequence.execute(args_remain);
				// GenerateGSEADataset
			} else if (type.equals("-GenerateGSEADataset")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateGSEADataset "
							+ GenerateGSEADataset.parameter_info());
					System.exit(0);
				}
				GenerateGSEADataset.execute(args_remain);
				// CalculateStemness
			} else if (type.equals("-CalculateStemness")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateStemness "
							+ CalculateStemness.parameter_info());
					System.exit(0);
				}
				CalculateStemness.execute(args_remain);
				// BedGraphFilterChromosomeName
			} else if (type.equals("-BedGraphFilterChromosomeName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BedGraphFilterChromosomeName "
							+ BedGraphFilterChromosomeName.parameter_info());
					System.exit(0);
				}
				BedGraphFilterChromosomeName.execute(args_remain);
				// Bam2FQ
			} else if (type.equals("-UBam2FQ")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -UBam2FQ "
							+ UBam2FQ.parameter_info());
					System.exit(0);
				}
				UBam2FQ.execute(args_remain);
				// Bam2FqMouseERCC
			} else if (type.equals("-Bam2FqMouseERCC")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Bam2FqMouseERCC "
							+ Bam2FqMouseERCC.parameter_info());
					System.exit(0);
				}
				Bam2FqMouseERCC.execute(args_remain);
				// GenerateMappingInputFile
			} else if (type.equals("-GenerateMappingInputFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMappingInputFile "
							+ GenerateMappingInputFile.parameter_info());
					System.exit(0);
				}
				GenerateMappingInputFile.execute(args_remain);
				// GenerateRNAseqCNVValues
			} else if (type.equals("-GenerateRNAseqCNVValues")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateRNAseqCNVValues "
							+ GenerateRNAseqCNVValues.parameter_info());
					System.exit(0);
				}
				GenerateRNAseqCNVValues.execute(args_remain);
				// GenerateERCCgtffile
			} else if (type.equals("-GenerateERCCgtffile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateERCCgtffile "
							+ GenerateERCCgtffile.parameter_info());
					System.exit(0);
				}
				GenerateERCCgtffile.execute(args_remain);
				// KeepColumnsFromMatrix
			} else if (type.equals("-KeepColumnsFromMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -KeepColumnsFromMatrix "
							+ KeepColumnsFromMatrix.parameter_info());
					System.exit(0);
				}
				KeepColumnsFromMatrix.execute(args_remain);
				// CombineRawCountSamplesTogether
			} else if (type.equals("-CombineRawCountSamplesTogether")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineRawCountSamplesTogether "
							+ CombineRawCountSamplesTogether.parameter_info());
					System.exit(0);
				}
				CombineRawCountSamplesTogether.execute(args_remain);
				// FastaRefSeq2Ensembl
			} else if (type.equals("-FastaRefSeq2Ensembl")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FastaRefSeq2Ensembl "
							+ FastaRefSeq2Ensembl.parameter_info());
					System.exit(0);
				}
				FastaRefSeq2Ensembl.execute(args_remain);
				// DivideByTotalMultiplyByX
			} else if (type.equals("-FastaRefSeq2EnsemblNew")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FastaRefSeq2EnsemblNew "
							+ FastaRefSeq2EnsemblNew.parameter_info());
					System.exit(0);
				}
				FastaRefSeq2EnsemblNew.execute(args_remain);
				// DivideByTotalMultiplyByX
			} else if (type.equals("-DivideByTotalMultiplyByX")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -DivideByTotalMultiplyByX "
							+ DivideByTotalMultiplyByX.parameter_info());
					System.exit(0);
				}
				DivideByTotalMultiplyByX.execute(args_remain);
				//
			} else if (type.equals("-Matrix2Addition")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Matrix2Addition "
							+ Matrix2Addition.parameter_info());
					System.exit(0);
				}
				Matrix2Addition.execute(args_remain);
				// Matrix2Log2
			} else if (type.equals("-Matrix2Log2")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Matrix2Log2 "
							+ Matrix2Log2.parameter_info());
					System.exit(0);
				}
				Matrix2Log2.execute(args_remain);
				//
			} else if (type.equals("-Matrix2Exponent")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Matrix2Exponent "
							+ Matrix2Exponent.parameter_info());
					System.exit(0);
				}
				Matrix2Exponent.execute(args_remain);
				// GenerateRNAHGGSampleK27MStatus
			} else if (type.equals("-GenerateRNAHGGSampleK27MStatus")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateRNAHGGSampleK27MStatus "
							+ GenerateRNAHGGSampleK27MStatus.parameter_info());
					System.exit(0);
				}
				GenerateRNAHGGSampleK27MStatus.execute(args_remain);
				// QuantileNormalization
			} else if (type.equals("-QuantileNormalization")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -QuantileNormalization "
							+ QuantileNormalization.parameter_info());
					System.exit(0);
				}
				QuantileNormalization.execute(args_remain);
				// CalculateKinaseSubstrateStDev
			} else if (type.equals("-CalculateKinaseSubstrateStDev")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateKinaseSubstrateStDev "
							+ CalculateKinaseSubstrateStDev.parameter_info());
					System.exit(0);
				}
				CalculateKinaseSubstrateStDev.execute(args_remain);
				// ExamineGeneCoverages
			} else if (type.equals("-ExamineGeneCoverages")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExamineGeneCoverages "
							+ ExamineGeneCoverages.parameter_info());
					System.exit(0);
				}
				ExamineGeneCoverages.execute(args_remain);
				// PlotGeneSetBoxPlot
			} else if (type.equals("-ExamineGeneCoverageFlexible")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExamineGeneCoverageFlexible "
							+ ExamineGeneCoverageFlexible.parameter_info());
					System.exit(0);
				}
				ExamineGeneCoverageFlexible.execute(args_remain);
				// ExamineGeneCoverageFlexible
			} else if (type.equals("-PlotGeneSetBoxPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PlotGeneSetBoxPlot "
							+ PlotGeneSetBoxPlot.parameter_info());
					System.exit(0);
				}
				PlotGeneSetBoxPlot.execute(args_remain);
				// PlotGeneSetBoxPlotAcrossSamples
			} else if (type.equals("-PlotGeneSetBoxPlotAcrossSamples")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PlotGeneSetBoxPlotAcrossSamples "
									+ PlotGeneSetBoxPlotAcrossSamples
											.parameter_info());
					System.exit(0);
				}
				PlotGeneSetBoxPlotAcrossSamples.execute(args_remain);
				// CalculateMutantAllelFrequencyMatrix
			} else if (type.equals("-CalculateMutantAllelFrequencyMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateMutantAllelFrequencyMatrix "
									+ CalculateMutantAllelFrequencyMatrix
											.parameter_info());
					System.exit(0);
				}
				CalculateMutantAllelFrequencyMatrix.execute(args_remain);
				// kgXrefAppendOfficialGeneSymbol
			} else if (type.equals("-kgXrefAppendOfficialGeneSymbol")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -kgXrefAppendOfficialGeneSymbol "
							+ kgXrefAppendOfficialGeneSymbol.parameter_info());
					System.exit(0);
				}
				kgXrefAppendOfficialGeneSymbol.execute(args_remain);
				// CalculateMutantExpressionMatrix
			} else if (type.equals("-CalculateMutantExpressionMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateMutantExpressionMatrix "
									+ CalculateMutantExpressionMatrix
											.parameter_info());
					System.exit(0);
				}
				CalculateMutantExpressionMatrix.execute(args_remain);
				// CalculateReferenceAlleleExpressionMatrix
			} else if (type.equals("-CalculateReferenceAlleleExpressionMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateReferenceAlleleExpressionMatrix "
									+ CalculateReferenceAlleleExpressionMatrix
											.parameter_info());
					System.exit(0);
				}
				CalculateReferenceAlleleExpressionMatrix.execute(args_remain);
				// AppendGeneLength
			} else if (type.equals("-AppendGeneLength")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendGeneLength "
							+ AppendGeneLength.parameter_info());
					System.exit(0);
				}
				AppendGeneLength.execute(args_remain);
				// GenerateCoreHomologTableMGISummary
			} else if (type.equals("-GenerateCoreHomologTableMGISummary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateCoreHomologTableMGISummary "
									+ GenerateCoreHomologTableMGISummary
											.parameter_info());
					System.exit(0);
				}
				GenerateCoreHomologTableMGISummary.execute(args_remain);
				// EnsureUniqGeneNamesHumanMouse
			} else if (type.equals("-EnsureUniqGeneNamesHumanMouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EnsureUniqGeneNamesHumanMouse "
							+ EnsureUniqGeneNamesHumanMouse.parameter_info());
					System.exit(0);
				}
				EnsureUniqGeneNamesHumanMouse.execute(args_remain);
				// RemoveQuotations
			} else if (type.equals("-RemoveQuotations")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RemoveQuotations "
							+ RemoveQuotations.parameter_info());
					System.exit(0);
				}
				RemoveQuotations.execute(args_remain);
				// CensusNormalization
			} else if (type.equals("-CensusNormalization")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CensusNormalization "
							+ CensusNormalization.parameter_info());
					System.exit(0);
				}
				CensusNormalization.execute(args_remain);
				// GenerateFoldchangeGeneLengthPlot
			} else if (type.equals("-GenerateFoldchangeGeneLengthPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateFoldchangeGeneLengthPlot "
									+ GenerateFoldchangeGeneLengthPlot
											.parameter_info());
					System.exit(0);
				}
				GenerateFoldchangeGeneLengthPlot.execute(args_remain);
				//
			} else if (type.equals("-GenerateScatterPlotJavaScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateScatterPlotJavaScript "
							+ GenerateScatterPlotJavaScript.parameter_info());
					System.exit(0);
				}
				GenerateScatterPlotJavaScript.execute(args_remain);
				// GenerateScatterPlotJavaScriptUserInput
			} else if (type.equals("-GenerateScatterPlotJavaScriptUserInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateScatterPlotJavaScriptUserInput "
									+ GenerateScatterPlotJavaScriptUserInput
											.parameter_info());
					System.exit(0);
				}
				GenerateScatterPlotJavaScriptUserInput.execute(args_remain);
				//
			} else if (type.equals("-GenerateHorizontalBarPlotJavaScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateHorizontalBarPlotJavaScript "
									+ GenerateHorizontalBarPlotJavaScript
											.parameter_info());
					System.exit(0);
				}
				GenerateHorizontalBarPlotJavaScript.execute(args_remain);
				// GenerateVolcanoPlotJavaScript
			} else if (type.equals("-GenerateVolcanoPlotJavaScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateVolcanoPlotJavaScript "
							+ GenerateVolcanoPlotJavaScript.parameter_info());
					System.exit(0);
				}
				GenerateVolcanoPlotJavaScript.execute(args_remain);
				// GenerateMAPlotJavaScriptUserInput
			} else if (type.equals("-GenerateMAPlotJavaScriptUserInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateMAPlotJavaScriptUserInput "
									+ GenerateMAPlotJavaScriptUserInput
											.parameter_info());
					System.exit(0);
				}
				GenerateMAPlotJavaScriptUserInput.execute(args_remain);
				//
			} else if (type.equals("-GenerateMAPlotJavaScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMAPlotJavaScript "
							+ GenerateMAPlotJavaScript.parameter_info());
					System.exit(0);
				}
				GenerateMAPlotJavaScript.execute(args_remain);
				// FilterORAResults
			} else if (type.equals("-FilterORAResults")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterORAResults "
							+ FilterORAResults.parameter_info());
					System.exit(0);
				}
				FilterORAResults.execute(args_remain);
				// FilterORAResultsFlex
			} else if (type.equals("-FilterORAResultsFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterORAResultsFlex "
							+ FilterORAResultsFlex.parameter_info());
					System.exit(0);
				}
				FilterORAResultsFlex.execute(args_remain);
				//
			} else if (type.equals("-CombineHTSEQResultRPMChunxuPipeline")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CombineHTSEQResultRPMChunxuPipeline "
									+ CombineHTSEQResultRPMChunxuPipeline
											.parameter_info());
					System.exit(0);
				}
				CombineHTSEQResultRPMChunxuPipeline.execute(args_remain);
				// CombineHTSEQResultRefGeneOnly
			} else if (type.equals("-CombineHTSEQResultRefGeneOnly")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineHTSEQResultRefGeneOnly "
							+ CombineHTSEQResultRefGeneOnly.parameter_info());
					System.exit(0);
				}
				CombineHTSEQResultRefGeneOnly.execute(args_remain);
				// CheckForMissingGenes
			} else if (type.equals("-CheckForMissingGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CheckForMissingGenes "
							+ CheckForMissingGenes.parameter_info());
					System.exit(0);
				}
				CheckForMissingGenes.execute(args_remain);
				// IncreaseCanonicalGeneIDs
			} else if (type.equals("-IncreaseCanonicalGeneIDs")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -IncreaseCanonicalGeneIDs "
							+ IncreaseCanonicalGeneIDs.parameter_info());
					System.exit(0);
				}
				IncreaseCanonicalGeneIDs.execute(args_remain);
				// AppendGeneNameBasedOnKnownCanonical
			} else if (type.equals("-AppendGeneNameBasedOnKnownCanonical")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendGeneNameBasedOnKnownCanonical "
									+ AppendGeneNameBasedOnKnownCanonical
											.parameter_info());
					System.exit(0);
				}
				AppendGeneNameBasedOnKnownCanonical.execute(args_remain);
				// FilterGenesBasedOnMaximumReads
			} else if (type.equals("-FilterGenesBasedOnMaximumReads")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterGenesBasedOnMaximumReads "
							+ FilterGenesBasedOnMaximumReads.parameter_info());
					System.exit(0);
				}
				FilterGenesBasedOnMaximumReads.execute(args_remain);
				// FilterMinimumOf5Reads
			} else if (type.equals("-FilterMinimumOf5Reads")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterMinimumOf5Reads "
							+ FilterMinimumOf5Reads.parameter_info());
					System.exit(0);
				}
				FilterMinimumOf5Reads.execute(args_remain);
				// GenerateVerticalBarPlotJavaScript
			} else if (type.equals("-GenerateVerticalBarPlotJavaScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateVerticalBarPlotJavaScript "
									+ GenerateVerticalBarPlotJavaScript
											.parameter_info());
					System.exit(0);
				}
				GenerateVerticalBarPlotJavaScript.execute(args_remain);
				// GenerateVolcanoPlotJavaScriptUserInput
			} else if (type.equals("-GenerateVolcanoPlotJavaScriptUserInput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateVolcanoPlotJavaScriptUserInput "
									+ GenerateVolcanoPlotJavaScriptUserInput
											.parameter_info());
					System.exit(0);
				}
				GenerateVolcanoPlotJavaScriptUserInput.execute(args_remain);
				// GenerateScriptForORA
			} else if (type.equals("-GenerateScriptForORA")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateScriptForORA "
							+ GenerateScriptForORA.parameter_info());
					System.exit(0);
				}
				GenerateScriptForORA.execute(args_remain);
				//
			} else if (type.equals("-GenerateScriptForORAFromInputFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateScriptForORAFromInputFile "
									+ GenerateScriptForORAFromInputFile
											.parameter_info());
					System.exit(0);
				}
				GenerateScriptForORAFromInputFile.execute(args_remain);
				//
			} else if (type.equals("-HumanMouseXenograftRawCount2RPM")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -HumanMouseXenograftRawCount2RPM "
									+ HumanMouseXenograftRawCount2RPM
											.parameter_info());
					System.exit(0);
				}
				HumanMouseXenograftRawCount2RPM.execute(args_remain);
				// AppendMetadataTag2RNAseqMatrixSampleName
			} else if (type.equals("-AppendMetadataTag2RNAseqMatrixSampleName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -AppendMetadataTag2RNAseqMatrixSampleName "
									+ AppendMetadataTag2RNAseqMatrixSampleName
											.parameter_info());
					System.exit(0);
				}
				AppendMetadataTag2RNAseqMatrixSampleName.execute(args_remain);
				// PhosphositeMetaScoreSensitivitySpecificity
			} else if (type
					.equals("-PhosphositeMetaScoreSensitivitySpecificity")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PhosphositeMetaScoreSensitivitySpecificity "
									+ PhosphositeMetaScoreSensitivitySpecificity
											.parameter_info());
					System.exit(0);
				}
				PhosphositeMetaScoreSensitivitySpecificity.execute(args_remain);
				// KinaseSubstrateMergeROCResult
			} else if (type.equals("-KinaseSubstrateMergeROCResult")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -KinaseSubstrateMergeROCResult "
							+ KinaseSubstrateMergeROCResult.parameter_info());
					System.exit(0);
				}
				KinaseSubstrateMergeROCResult.execute(args_remain);
				// GenerateMotifScoreTable
			} else if (type.equals("-GenerateMotifScoreTable")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMotifScoreTable "
							+ GenerateMotifScoreTable.parameter_info());
					System.exit(0);
				}
				GenerateMotifScoreTable.execute(args_remain);
				// GenerateMotifScoreTableAll
			} else if (type.equals("-GenerateMotifScoreTableAll")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMotifScoreTableAll "
							+ GenerateMotifScoreTableAll.parameter_info());
					System.exit(0);
				}
				GenerateMotifScoreTableAll.execute(args_remain);
				//
			} else if (type
					.equals("-CalculatePhosphositePlusKinaseEntrySummary")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculatePhosphositePlusKinaseEntrySummary "
									+ CalculatePhosphositePlusKinaseEntrySummary
											.parameter_info());
					System.exit(0);
				}
				CalculatePhosphositePlusKinaseEntrySummary.execute(args_remain);
				// MergeBamFilesAfterSTAR
			} else if (type.equals("-MergeBamFilesAfterSTAR")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeBamFilesAfterSTAR "
							+ MergeBamFilesAfterSTAR.parameter_info());
					System.exit(0);
				}
				MergeBamFilesAfterSTAR.execute(args_remain);
				// RawCount2RPMSkipFirstTwoColumns
			} else if (type.equals("-RawCount2RPMSkipFirstTwoColumns")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -RawCount2RPMSkipFirstTwoColumns "
									+ RawCount2RPMSkipFirstTwoColumns
											.parameter_info());
					System.exit(0);
				}
				RawCount2RPMSkipFirstTwoColumns.execute(args_remain);
				//
			} else if (type.equals("-RawCount2RPM")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RawCount2RPM "
							+ RawCount2RPM.parameter_info());
					System.exit(0);
				}
				RawCount2RPM.execute(args_remain);
				// GMTHuman2Mouse
			} else if (type.equals("-GMTHuman2Mouse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GMTHuman2Mouse "
							+ GMTHuman2Mouse.parameter_info());
					System.exit(0);
				}
				GMTHuman2Mouse.execute(args_remain);
				// RemoveZeroCountGenes
			} else if (type.equals("-GMTMouse2Human")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GMTMouse2Human "
							+ GMTMouse2Human.parameter_info());
					System.exit(0);
				}
				GMTMouse2Human.execute(args_remain);
				// RemoveZeroCountGenes
			} else if (type.equals("-RemoveZeroCountGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RemoveZeroCountGenes "
							+ RemoveZeroCountGenes.parameter_info());
					System.exit(0);
				}
				RemoveZeroCountGenes.execute(args_remain);
				// GenerateSolidBowtieMapping
			} else if (type.equals("-GenerateSolidBowtieMapping")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateSolidBowtieMapping "
							+ GenerateSolidBowtieMapping.parameter_info());
					System.exit(0);
				}
				GenerateSolidBowtieMapping.execute(args_remain);
				// ConvertSam2BamFile
			} else if (type.equals("-ConvertSam2BamFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ConvertSam2BamFile "
							+ ConvertSam2BamFile.parameter_info());
					System.exit(0);
				}
				ConvertSam2BamFile.execute(args_remain);
				// ConvertSam2BamFileWithReference
			} else if (type.equals("-ConvertSam2BamFileWithReference")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ConvertSam2BamFileWithReference "
									+ ConvertSam2BamFileWithReference
											.parameter_info());
					System.exit(0);
				}
				ConvertSam2BamFileWithReference.execute(args_remain);
				// SortBamFiles
			} else if (type.equals("-SortBamFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SortBamFiles "
							+ SortBamFiles.parameter_info());
					System.exit(0);
				}
				SortBamFiles.execute(args_remain);
				// MergeBamFilesAfterBowtie
			} else if (type.equals("-MergeBamFilesAfterBowtie")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeBamFilesAfterBowtie "
							+ MergeBamFilesAfterBowtie.parameter_info());
					System.exit(0);
				}
				MergeBamFilesAfterBowtie.execute(args_remain);
				// CreateBamIndex
			} else if (type.equals("-CreateBamIndex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CreateBamIndex "
							+ CreateBamIndex.parameter_info());
					System.exit(0);
				}
				CreateBamIndex.execute(args_remain);
				// CreateSingleSampleGSEAInputFiles
			} else if (type.equals("-CreateSingleSampleGSEAInputFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CreateSingleSampleGSEAInputFiles "
									+ CreateSingleSampleGSEAInputFiles
											.parameter_info());
					System.exit(0);
				}
				CreateSingleSampleGSEAInputFiles.execute(args_remain);
				// SummarizeSingleSampleGSEAResult
			} else if (type.equals("-SummarizeSingleSampleGSEAResult")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -SummarizeSingleSampleGSEAResult "
									+ SummarizeSingleSampleGSEAResult
											.parameter_info());
					System.exit(0);
				}
				SummarizeSingleSampleGSEAResult.execute(args_remain);
				// GenerateVolcanoPlotJavaScriptUserInputPathways
			} else if (type.equals("-GenerateHeatmapJavaScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateHeatmapJavaScript "
							+ GenerateHeatmapJavaScript.parameter_info());
					System.exit(0);
				}
				GenerateHeatmapJavaScript.execute(args_remain);
				// MatrixZscoreNormalization
			} else if (type.equals("-MatrixZscoreNormalization")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MatrixZscoreNormalization "
							+ MatrixZscoreNormalization.parameter_info());
					System.exit(0);
				}
				MatrixZscoreNormalization.execute(args_remain);
				// MatrixLog2ZscoreNormalization
			} else if (type.equals("-MatrixLog2ZscoreNormalization")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MatrixLog2ZscoreNormalization "
							+ MatrixLog2ZscoreNormalization.parameter_info());
					System.exit(0);
				}
				MatrixLog2ZscoreNormalization.execute(args_remain);
				//
			} else if (type.equals("-OrderGeneMatrixBasedOnTTestDist")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -OrderGeneMatrixBasedOnTTestDist "
									+ OrderGeneMatrixBasedOnTTestDist
											.parameter_info());
					System.exit(0);
				}
				OrderGeneMatrixBasedOnTTestDist.execute(args_remain);
				// TransposeMatrix
			} else if (type.equals("-TransposeMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -TransposeMatrix "
							+ TransposeMatrix.parameter_info());
					System.exit(0);
				}
				TransposeMatrix.execute(args_remain);
				// TwoGroupMeanCentering
			} else if (type.equals("-TwoGroupMeanCentering")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -TwoGroupMeanCentering "
							+ TwoGroupMeanCentering.parameter_info());
					System.exit(0);
				}
				TwoGroupMeanCentering.execute(args_remain);
				// TwoGroupMeanCenteringFlex
			} else if (type.equals("-TwoGroupMeanCenteringFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -TwoGroupMeanCenteringFlex "
							+ TwoGroupMeanCenteringFlex.parameter_info());
					System.exit(0);
				}
				TwoGroupMeanCenteringFlex.execute(args_remain);
				// MatrixZscoreNormalizationWithOriginalValues
			} else if (type
					.equals("-MatrixZscoreNormalizationWithOriginalValues")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MatrixZscoreNormalizationWithOriginalValues "
									+ MatrixZscoreNormalizationWithOriginalValues
											.parameter_info());
					System.exit(0);
				}
				MatrixZscoreNormalizationWithOriginalValues
						.execute(args_remain);
				// GenerateHeatmapZscoreWithOriginalValuesJavaScript
			} else if (type
					.equals("-GenerateHeatmapZscoreWithOriginalValuesJavaScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateHeatmapZscoreWithOriginalValuesJavaScript "
									+ GenerateHeatmapZscoreWithOriginalValuesJavaScript
											.parameter_info());
					System.exit(0);
				}
				GenerateHeatmapZscoreWithOriginalValuesJavaScript
						.execute(args_remain);
				// GenerateHeatmapFromGMTPipeline
			} else if (type.equals("-GenerateHeatmapFromGMTPipeline")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateHeatmapFromGMTPipeline "
							+ GenerateHeatmapFromGMTPipeline.parameter_info());
					System.exit(0);
				}
				GenerateHeatmapFromGMTPipeline.execute(args_remain);
				// CreatePythonGSEAInputFile
			} else if (type.equals("-CreatePythonGSEAInputFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CreatePythonGSEAInputFile "
							+ CreatePythonGSEAInputFile.parameter_info());
					System.exit(0);
				}
				CreatePythonGSEAInputFile.execute(args_remain);
				// SummarizeGSEAResultNESFDR
			} else if (type.equals("-SummarizeGSEAResultNESFDR")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeGSEAResultNESFDR "
							+ SummarizeGSEAResultNESFDR.parameter_info());
					System.exit(0);
				}
				SummarizeGSEAResultNESFDR.execute(args_remain);
				// GenerateHeatmapZscoreSSGSEAJavaScript
			} else if (type.equals("-GenerateHeatmapZscoreSSGSEAJavaScript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateHeatmapZscoreSSGSEAJavaScript "
									+ GenerateHeatmapZscoreSSGSEAJavaScript
											.parameter_info());
					System.exit(0);
				}
				GenerateHeatmapZscoreSSGSEAJavaScript.execute(args_remain);
				// GenerateBatchBarPlotHtmls
			} else if (type.equals("-GenerateBatchBarPlotHtmls")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateBatchBarPlotHtmls "
							+ GenerateBatchBarPlotHtmls.parameter_info());
					System.exit(0);
				}
				GenerateBatchBarPlotHtmls.execute(args_remain);
				// PlotIsotopicBarPlots
			} else if (type.equals("-PlotIsotopicBarPlots")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PlotIsotopicBarPlots "
							+ PlotIsotopicBarPlots.parameter_info());
					System.exit(0);
				}
				PlotIsotopicBarPlots.execute(args_remain);
				// GeneratePreyGeneLength
			} else if (type.equals("-GeneratePreyGeneLength")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneratePreyGeneLength "
							+ GeneratePreyGeneLength.parameter_info());
					System.exit(0);
				}
				GeneratePreyGeneLength.execute(args_remain);
				// GeneSymbol2UCSCIDAppend
			} else if (type.equals("-GeneSymbol2UCSCIDAppend")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GeneSymbol2UCSCIDAppend "
							+ GeneSymbol2UCSCIDAppend.parameter_info());
					System.exit(0);
				}
				GeneSymbol2UCSCIDAppend.execute(args_remain);
				// CalculateGeneLengthSaintInputFile
			} else if (type.equals("-CalculateGeneLengthSaintInputFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateGeneLengthSaintInputFile "
									+ CalculateGeneLengthSaintInputFile
											.parameter_info());
					System.exit(0);
				}
				CalculateGeneLengthSaintInputFile.execute(args_remain);
				// GenerateInteractionFileForSaint
			} else if (type.equals("-GenerateInteractionFileForSaint")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -GenerateInteractionFileForSaint "
									+ GenerateInteractionFileForSaint
											.parameter_info());
					System.exit(0);
				}
				GenerateInteractionFileForSaint.execute(args_remain);
				// CalculateROCforMTORC1Motif
			} else if (type.equals("-CalculateROCforMTORC1Motif")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateROCforMTORC1Motif "
							+ CalculateROCforMTORC1Motif.parameter_info());
					System.exit(0);
				}
				CalculateROCforMTORC1Motif.execute(args_remain);
				// OptimizeProteomeNormalization
			} else if (type.equals("-OptimizeProteomeNormalization")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -OptimizeProteomeNormalization "
							+ OptimizeProteomeNormalization.parameter_info());
					System.exit(0);
				}
				OptimizeProteomeNormalization.execute(args_remain);
				// AUCFilter
			} else if (type.equals("-AUCFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AUCFilter "
							+ AUCFilter.parameter_info());
					System.exit(0);
				}
				AUCFilter.execute(args_remain);
				// CalculateAUC
			} else if (type.equals("-CalculateAUC")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateAUC "
							+ CalculateAUC.parameter_info());
					System.exit(0);
				}
				CalculateAUC.execute(args_remain);
				// AppendMTORC1Motif2Table
			} else if (type.equals("-AppendMTORC1Motif2Table")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendMTORC1Motif2Table "
							+ AppendMTORC1Motif2Table.parameter_info());
					System.exit(0);
				}
				AppendMTORC1Motif2Table.execute(args_remain);
				// HongboAppendSensitivitySpecificity
			} else if (type.equals("-HongboAppendSensitivitySpecificity")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -HongboAppendSensitivitySpecificity "
									+ HongboAppendSensitivitySpecificity
											.parameter_info());
					System.exit(0);
				}
				HongboAppendSensitivitySpecificity.execute(args_remain);
				// MicroarrayEnsembl2GeneName
			} else if (type.equals("-MicroarrayEnsembl2GeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MicroarrayEnsembl2GeneName "
							+ MicroarrayEnsembl2GeneName.parameter_info());
					System.exit(0);
				}
				MicroarrayEnsembl2GeneName.execute(args_remain);
				// MergeGeneCountChunxuPipeline
			} else if (type.equals("-MergeGeneCountChunxuPipeline")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MergeGeneCountChunxuPipeline "
							+ MergeGeneCountChunxuPipeline.parameter_info());
					System.exit(0);
				}
				MergeGeneCountChunxuPipeline.execute(args_remain);
				// ExpandGeneListAfterLIMMA
			} else if (type.equals("-ExpandGeneListAfterLIMMA")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExpandGeneListAfterLIMMA "
							+ ExpandGeneListAfterLIMMA.parameter_info());
					System.exit(0);
				}
				ExpandGeneListAfterLIMMA.execute(args_remain);
				// NormalizeBedGraph
			} else if (type.equals("-NormalizeBedGraph")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NormalizeBedGraph "
							+ NormalizeBedGraph.parameter_info());
					System.exit(0);
				}
				NormalizeBedGraph.execute(args_remain);
				// AppendExpressionToMATSOutput
			} else if (type.equals("-AppendExpressionToMATSOutput")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendExpressionToMATSOutput "
							+ AppendExpressionToMATSOutput.parameter_info());
					System.exit(0);
				}
				AppendExpressionToMATSOutput.execute(args_remain);
				// CalculateDistanceBetweenModules
			} else if (type.equals("-CalculateDistanceBetweenModules")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CalculateDistanceBetweenModules "
									+ CalculateDistanceBetweenModules
											.parameter_info());
					System.exit(0);
				}
				CalculateDistanceBetweenModules.execute(args_remain);
				// SummarizeVDJclones
			} else if (type.equals("-SummarizeVDJclones")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeVDJclones "
							+ SummarizeVDJclones.parameter_info());
					System.exit(0);
				}
				SummarizeVDJclones.execute(args_remain);
				// JiyangYuConvertGeneNames
			} else if (type.equals("-JiyangYuConvertGeneNames")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -JiyangYuConvertGeneNames "
							+ JiyangYuConvertGeneNames.parameter_info());
					System.exit(0);
				}
				JiyangYuConvertGeneNames.execute(args_remain);
				// JiyangYuAppendOtherColumn
			} else if (type.equals("-JiyangYuAppendOtherColumn")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -JiyangYuAppendOtherColumn "
							+ JiyangYuAppendOtherColumn.parameter_info());
					System.exit(0);
				}
				JiyangYuAppendOtherColumn.execute(args_remain);
				// GenerateAracneInputFile
			} else if (type.equals("-GenerateAracneInputFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateAracneInputFile "
							+ GenerateAracneInputFile.parameter_info());
					System.exit(0);
				}
				GenerateAracneInputFile.execute(args_remain);
				// ConvertAracneOutput2GMT
			} else if (type.equals("-ConvertAracneOutput2GMT")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ConvertAracneOutput2GMT "
							+ ConvertAracneOutput2GMT.parameter_info());
					System.exit(0);
				}
				ConvertAracneOutput2GMT.execute(args_remain);
				// HongboAppendSensitivitySpecificityFlex
			} else if (type.equals("-HongboAppendSensitivitySpecificityFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -HongboAppendSensitivitySpecificityFlex "
									+ HongboAppendSensitivitySpecificityFlex
											.parameter_info());
					System.exit(0);
				}
				HongboAppendSensitivitySpecificityFlex.execute(args_remain);
				// SplitFastaFile
			} else if (type.equals("-SplitFastaFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SplitFastaFile "
							+ SplitFastaFile.parameter_info());
					System.exit(0);
				}
				SplitFastaFile.execute(args_remain);
				// ChangeFastaIDRefmRNA
			} else if (type.equals("-ChangeFastaIDRefmRNA")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ChangeFastaIDRefmRNA "
							+ ChangeFastaIDRefmRNA.parameter_info());
					System.exit(0);
				}
				ChangeFastaIDRefmRNA.execute(args_remain);
				// FilterDuplicate
			} else if (type.equals("-FilterDuplicate")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterDuplicate "
							+ FilterDuplicate.parameter_info());
					System.exit(0);
				}
				FilterDuplicate.execute(args_remain);
				// FilterDuplicateTranscriptSeq
			} else if (type.equals("-FilterDuplicateTranscriptSeq")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterDuplicateTranscriptSeq "
							+ FilterDuplicateTranscriptSeq.parameter_info());
					System.exit(0);
				}
				FilterDuplicateTranscriptSeq.execute(args_remain);
				// ExtractD2P2Sequences
			} else if (type.equals("-ExtractD2P2Sequences")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractD2P2Sequences "
							+ ExtractD2P2Sequences.parameter_info());
					System.exit(0);
				}
				ExtractD2P2Sequences.execute(args_remain);
				// ChangeFastaIDUniprot
			} else if (type.equals("-ChangeFastaIDUniprot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ChangeFastaIDUniprot "
							+ ChangeFastaIDUniprot.parameter_info());
					System.exit(0);
				}
				ChangeFastaIDUniprot.execute(args_remain);
				// Disorder2BEDFile
			} else if (type.equals("-Disorder2BEDFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Disorder2BEDFile "
							+ Disorder2BEDFile.parameter_info());
					System.exit(0);
				}
				Disorder2BEDFile.execute(args_remain);
				// ExtractD2P2SequenceRaw
			} else if (type.equals("-ExtractD2P2SequenceRaw")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ExtractD2P2SequenceRaw "
							+ ExtractD2P2SequenceRaw.parameter_info());
					System.exit(0);
				}
				ExtractD2P2SequenceRaw.execute(args_remain);
				// HTSEQMergeCountFiles
			} else if (type.equals("-HTSEQMergeCountFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -HTSEQMergeCountFiles "
							+ HTSEQMergeCountFiles.parameter_info());
					System.exit(0);
				}
				HTSEQMergeCountFiles.execute(args_remain);
				// MicroArrayIDConversionAnnotation
			} else if (type.equals("-MicroArrayIDConversionAnnotation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MicroArrayIDConversionAnnotation "
									+ MicroArrayIDConversionAnnotation
											.parameter_info());
					System.exit(0);
				}
				MicroArrayIDConversionAnnotation.execute(args_remain);
				// AppendMetaInformation
			} else if (type.equals("-AppendMetaInformation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendMetaInformation "
							+ AppendMetaInformation.parameter_info());
					System.exit(0);
				}
				AppendMetaInformation.execute(args_remain);
				// CheckGMTCoverage
			} else if (type.equals("-CheckGMTCoverage")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CheckGMTCoverage "
							+ CheckGMTCoverage.parameter_info());
					System.exit(0);
				}
				CheckGMTCoverage.execute(args_remain);
				// SummarizeIKAPMatrix
			} else if (type.equals("-SummarizeIKAPMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeIKAPMatrix "
							+ SummarizeIKAPMatrix.parameter_info());
					System.exit(0);
				}
				SummarizeIKAPMatrix.execute(args_remain);
				// DownSamplingBulkMatrixAsSingleCell
			} else if (type.equals("-DownSamplingBulkMatrixAsSingleCell")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -DownSamplingBulkMatrixAsSingleCell "
									+ DownSamplingBulkMatrixAsSingleCell
											.parameter_info());
					System.exit(0);
				}
				DownSamplingBulkMatrixAsSingleCell.execute(args_remain);
				// RemoveRedundantEdges
			} else if (type.equals("-RemoveRedundantEdges")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RemoveRedundantEdges "
							+ RemoveRedundantEdges.parameter_info());
					System.exit(0);
				}
				RemoveRedundantEdges.execute(args_remain);
				// BMIQNormalization
			} else if (type.equals("-BMIQNormalization")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BMIQNormalization "
							+ BMIQNormalization.parameter_info());
					System.exit(0);
				}
				BMIQNormalization.execute(args_remain);
				// BMIQNormalizationSingleSample
			} else if (type.equals("-BMIQNormalizationSingleSample")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BMIQNormalizationSingleSample "
							+ BMIQNormalizationSingleSample.parameter_info());
					System.exit(0);
				}
				BMIQNormalizationSingleSample.execute(args_remain);
				// CombineBMIQNormalizedFiles {
			} else if (type.equals("-CombineBMIQNormalizedFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineBMIQNormalizedFiles "
							+ CombineBMIQNormalizedFiles.parameter_info());
					System.exit(0);
				}
				CombineBMIQNormalizedFiles.execute(args_remain);
				// {
			} else if (type.equals("-CombineBMIQNormalizedFilesRscript")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -CombineBMIQNormalizedFilesRscript "
									+ CombineBMIQNormalizedFilesRscript
											.parameter_info());
					System.exit(0);
				}
				CombineBMIQNormalizedFilesRscript.execute(args_remain);
				// BoxplotExpressionForEachSample
			} else if (type.equals("-BoxplotExpressionForEachSample")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BoxplotExpressionForEachSample "
							+ BoxplotExpressionForEachSample.parameter_info());
					System.exit(0);
				}
				BoxplotExpressionForEachSample.execute(args_remain);
				// MatchUniprotGeneName2GeneLCDLength
			} else if (type.equals("-MatchUniprotGeneName2GeneLCDLength")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -MatchUniprotGeneName2GeneLCDLength "
									+ MatchUniprotGeneName2GeneLCDLength
											.parameter_info());
					System.exit(0);
				}
				MatchUniprotGeneName2GeneLCDLength.execute(args_remain);
				// UniprotSEGPostProcessing
			} else if (type.equals("-UniprotSEGPostProcessing")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -UniprotSEGPostProcessing "
							+ UniprotSEGPostProcessing.parameter_info());
					System.exit(0);
				}
				UniprotSEGPostProcessing.execute(args_remain);
				// JPaulTaylorConvertUniprot2UniprotGeneName
			} else if (type
					.equals("-JPaulTaylorConvertUniprot2UniprotGeneName")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -JPaulTaylorConvertUniprot2UniprotGeneName "
									+ JPaulTaylorConvertUniprot2UniprotGeneName
											.parameter_info());
					System.exit(0);
				}
				JPaulTaylorConvertUniprot2UniprotGeneName.execute(args_remain);
				// AppendUbiquitome
			} else if (type.equals("-AppendUbiquitome")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendUbiquitome "
							+ AppendUbiquitome.parameter_info());
					System.exit(0);
				}
				AppendUbiquitome.execute(args_remain);
				// Methylation850KAppendGeneInfo
			} else if (type.equals("-Methylation850KAppendGeneInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Methylation850KAppendGeneInfo "
							+ Methylation850KAppendGeneInfo.parameter_info());
					System.exit(0);
				}
				Methylation850KAppendGeneInfo.execute(args_remain);
				// CombineBMIQFiles
			} else if (type.equals("-CombineBMIQFiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineBMIQFiles "
							+ CombineBMIQFiles.parameter_info());
					System.exit(0);
				}
				CombineBMIQFiles.execute(args_remain);
				// EPIC850KWilcoxonTestMethylation
			} else if (type.equals("-EPIC850KWilcoxonTestMethylation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -EPIC850KWilcoxonTestMethylation "
									+ EPIC850KWilcoxonTestMethylation
											.parameter_info());
					System.exit(0);
				}
				EPIC850KWilcoxonTestMethylation.execute(args_remain);
				// Methylation850KWilcoxonTestAppendGeneInfo
			} else if (type
					.equals("-Methylation850KWilcoxonTestAppendGeneInfo")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -Methylation850KWilcoxonTestAppendGeneInfo "
									+ Methylation850KWilcoxonTestAppendGeneInfo
											.parameter_info());
					System.exit(0);
				}
				Methylation850KWilcoxonTestAppendGeneInfo.execute(args_remain);
				// EPIC850KMostMADVariableProbe
			} else if (type.equals("-EPIC850KMostMADVariableProbe")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EPIC850KMostMADVariableProbe "
							+ EPIC850KMostMADVariableProbe.parameter_info());
					System.exit(0);
				}
				EPIC850KMostMADVariableProbe.execute(args_remain);
				// EPIC850KAveragedBEDFile
			} else if (type.equals("-EPIC850KAveragedBEDFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -EPIC850KAveragedBEDFile "
							+ EPIC850KAveragedBEDFile.parameter_info());
					System.exit(0);
				}
				EPIC850KAveragedBEDFile.execute(args_remain);
				// BedAddRemoveChr
			} else if (type.equals("-BedAddRemoveChr")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -BedAddRemoveChr "
							+ BedAddRemoveChr.parameter_info());
					System.exit(0);
				}
				BedAddRemoveChr.execute(args_remain);
				// AppendMayoMetaData
			} else if (type.equals("-AppendMayoMetaData")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendMayoMetaData "
							+ AppendMayoMetaData.parameter_info());
					System.exit(0);
				}
				AppendMayoMetaData.execute(args_remain);
				// SplitFastqForwardReverse
			} else if (type.equals("-SplitFastqForwardReverse")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SplitFastqForwardReverse "
							+ SplitFastqForwardReverse.parameter_info());
					System.exit(0);
				}
				SplitFastqForwardReverse.execute(args_remain);
				// SummarizeFlagStats
			} else if (type.equals("-SummarizeFlagStats")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeFlagStats "
							+ SummarizeFlagStats.parameter_info());
					System.exit(0);
				}
				SummarizeFlagStats.execute(args_remain);
				// PengROSMAPAttachMetaInformation
			} else if (type.equals("-PengROSMAPAttachMetaInformation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PengROSMAPAttachMetaInformation "
									+ PengROSMAPAttachMetaInformation
											.parameter_info());
					System.exit(0);
				}
				PengROSMAPAttachMetaInformation.execute(args_remain);
				// GenerateFastqFromBAM
			} else if (type.equals("-GenerateFastqFromBAM")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateFastqFromBAM "
							+ GenerateFastqFromBAM.parameter_info());
					System.exit(0);
				}
				GenerateFastqFromBAM.execute(args_remain);
				// AddGeneName2rMATS401
			} else if (type.equals("-AddGeneName2rMATS401")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AddGeneName2rMATS401 "
							+ AddGeneName2rMATS401.parameter_info());
					System.exit(0);
				}
				AddGeneName2rMATS401.execute(args_remain);
				// FilterCNVkitcnrfiles
			} else if (type.equals("-FilterCNVkitcnrfiles")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterCNVkitcnrfiles "
							+ FilterCNVkitcnrfiles.parameter_info());
					System.exit(0);
				}
				FilterCNVkitcnrfiles.execute(args_remain);
				// High20ToTHETA
			} else if (type.equals("-High20ToTHETA")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -High20ToTHETA "
							+ High20ToTHETA.parameter_info());
					System.exit(0);
				}
				High20ToTHETA.execute(args_remain);
				// XiangChenGrabTopVariableGenes
			} else if (type.equals("-XiangChenGrabTopVariableGenes")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -XiangChenGrabTopVariableGenes "
							+ XiangChenGrabTopVariableGenes.parameter_info());
					System.exit(0);
				}
				XiangChenGrabTopVariableGenes.execute(args_remain);
				// XiangChenGrabTopVariableGenesFilterSNPXY
			} else if (type.equals("-XiangChenGrabTopVariableGenesFilterSNPXY")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -XiangChenGrabTopVariableGenesFilterSNPXY "
									+ XiangChenGrabTopVariableGenesFilterSNPXY
											.parameter_info());
					System.exit(0);
				}
				XiangChenGrabTopVariableGenesFilterSNPXY.execute(args_remain);
				//
			} else if (type.equals("-Bam2Fastq")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Bam2Fastq "
							+ Bam2Fastq.parameter_info());
					System.exit(0);
				}
				Bam2Fastq.execute(args_remain);
				// GrabUniqValuesFromColumn
			} else if (type.equals("-GrabUniqValuesFromColumn")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GrabUniqValuesFromColumn "
							+ GrabUniqValuesFromColumn.parameter_info());
					System.exit(0);
				}
				GrabUniqValuesFromColumn.execute(args_remain);
				// GenerateBiogrid2SIF
			} else if (type.equals("-GenerateBiogrid2SIF")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateBiogrid2SIF "
							+ GenerateBiogrid2SIF.parameter_info());
					System.exit(0);
				}
				GenerateBiogrid2SIF.execute(args_remain);
				// FilterMatrixColumnValue
			} else if (type.equals("-FilterMatrixColumnValue")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterMatrixColumnValue "
							+ FilterMatrixColumnValue.parameter_info());
					System.exit(0);
				}
				FilterMatrixColumnValue.execute(args_remain);
				// PotterIdentifyExonBeingSkippedThroughCufflinks
			} else if (type
					.equals("-PotterIdentifyExonBeingSkippedThroughCufflinks")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -PotterIdentifyExonBeingSkippedThroughCufflinks "
									+ PotterIdentifyExonBeingSkippedThroughCufflinks
											.parameter_info());
					System.exit(0);
				}
				PotterIdentifyExonBeingSkippedThroughCufflinks
						.execute(args_remain);
				// PotterGrabTranscriptExonFasta
			} else if (type.equals("-PotterGrabTranscriptExonFasta")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -PotterGrabTranscriptExonFasta "
							+ PotterGrabTranscriptExonFasta.parameter_info());
					System.exit(0);
				}
				PotterGrabTranscriptExonFasta.execute(args_remain);
				// GenerateMIXCR
			} else if (type.equals("-GenerateMIXCR")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateMIXCR "
							+ GenerateMIXCR.parameter_info());
					System.exit(0);
				}
				GenerateMIXCR.execute(args_remain);
				// SummarizeMIXCRresult
			} else if (type.equals("-SummarizeMIXCRresult")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeMIXCRresult "
							+ SummarizeMIXCRresult.parameter_info());
					System.exit(0);
				}
				SummarizeMIXCRresult.execute(args_remain);
				// SchwartzExtractFastqSeq
			} else if (type.equals("-SchwartzExtractFastqSeq")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SchwartzExtractFastqSeq "
							+ SchwartzExtractFastqSeq.parameter_info());
					System.exit(0);
				}
				SchwartzExtractFastqSeq.execute(args_remain);
				// SchwartzCountTomatoCre
			} else if (type.equals("-SchwartzCountTomatoCre")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SchwartzCountTomatoCre "
							+ SchwartzCountTomatoCre.parameter_info());
					System.exit(0);
				}
				SchwartzCountTomatoCre.execute(args_remain);
				// SchwartzCheckGeneExpression
			} else if (type.equals("-SchwartzCheckGeneExpression")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SchwartzCheckGeneExpression "
							+ SchwartzCheckGeneExpression.parameter_info());
					System.exit(0);
				}
				SchwartzCheckGeneExpression.execute(args_remain);
				// TaoshengChenVennDiagram
			} else if (type.equals("-TaoshengChenVennDiagram")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -TaoshengChenVennDiagram "
							+ TaoshengChenVennDiagram.parameter_info());
					System.exit(0);
				}
				TaoshengChenVennDiagram.execute(args_remain);
				// SummarizeRNAPEG
			} else if (type.equals("-SummarizeRNAPEG")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeRNAPEG "
							+ SummarizeRNAPEG.parameter_info());
					System.exit(0);
				}
				SummarizeRNAPEG.execute(args_remain);
				// CalculateIntronRPKM
			} else if (type.equals("-CalculateIntronRPKM")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateIntronRPKM "
							+ CalculateIntronRPKM.parameter_info());
					System.exit(0);
				}
				CalculateIntronRPKM.execute(args_remain);
				// CalculateExonRPKM
			} else if (type.equals("-CalculateExonRPKM")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateExonRPKM "
							+ CalculateExonRPKM.parameter_info());
					System.exit(0);
				}
				CalculateExonRPKM.execute(args_remain);
				//
			} else if (type.equals("-CombineTwoMatrixWithMismatch")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineTwoMatrixWithMismatch "
							+ CombineTwoMatrixWithMismatch.parameter_info());
					System.exit(0);
				}
				CombineTwoMatrixWithMismatch.execute(args_remain);
				// ConvertEnrichR2PathwayFolder
			} else if (type.equals("-ConvertEnrichRGMT2PathwayFolder")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -ConvertEnrichRGMT2PathwayFolder "
									+ ConvertEnrichR2GMTPathwayFolder
											.parameter_info());
					System.exit(0);
				}
				ConvertEnrichR2GMTPathwayFolder.execute(args_remain);
				// CleanGMTEnrichR
			} else if (type.equals("-CleanGMTEnrichR")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CleanGMTEnrichR "
							+ CleanGMTEnrichR.parameter_info());
					System.exit(0);
				}
				CleanGMTEnrichR.execute(args_remain);
				//
			} else if (type.equals("-JunminPengCombineSplicingAndExpression")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -JunminPengCombineSplicingAndExpression "
									+ JunminPengCombineSplicingAndExpression
											.parameter_info());
					System.exit(0);
				}
				JunminPengCombineSplicingAndExpression.execute(args_remain);
				// CompareModule0ToOthers
			} else if (type.equals("-CompareModule0ToOthers")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CompareModule0ToOthers "
							+ CompareModule0ToOthers.parameter_info());
					System.exit(0);
				}
				CompareModule0ToOthers.execute(args_remain);
				// JunminPengRemoveModuleHighlightiPSDConnections
			} else if (type
					.equals("-JunminPengRemoveModuleHighlightiPSDConnections")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -JunminPengRemoveModuleHighlightiPSDConnections "
									+ JunminPengRemoveModuleHighlightiPSDConnections
											.parameter_info());
					System.exit(0);
				}
				JunminPengRemoveModuleHighlightiPSDConnections
						.execute(args_remain);
				// JunminPengColoriPSDConnections
			} else if (type.equals("-JunminPengColoriPSDConnections")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -JunminPengColoriPSDConnections "
							+ JunminPengColoriPSDConnections.parameter_info());
					System.exit(0);
				}
				JunminPengColoriPSDConnections.execute(args_remain);
				// McKinnonGCScanner
			} else if (type.equals("-McKinnonGCScanner")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -McKinnonGCScanner "
							+ McKinnonGCScanner.parameter_info());
					System.exit(0);
				}
				McKinnonGCScanner.execute(args_remain);
				// McKinnonSummarizeGCScanning
			} else if (type.equals("-McKinnonSummarizeGCScanning")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -McKinnonSummarizeGCScanning "
							+ McKinnonSummarizeGCScanning.parameter_info());
					System.exit(0);
				}
				McKinnonSummarizeGCScanning.execute(args_remain);
				// McKinnonGCScatterPlot
			} else if (type.equals("-McKinnonGCScatterPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -McKinnonGCScatterPlot "
							+ McKinnonGCScatterPlot.parameter_info());
					System.exit(0);
				}
				McKinnonGCScatterPlot.execute(args_remain);
				// XiangChenExtractMetaData
			} else if (type.equals("-XiangChenExtractMetaData")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -XiangChenExtractMetaData "
							+ XiangChenExtractMetaData.parameter_info());
					System.exit(0);
				}
				XiangChenExtractMetaData.execute(args_remain);
				// Epic850KAppendMetaInformation
			} else if (type.equals("-Epic850KAppendMetaInformation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -Epic850KAppendMetaInformation "
							+ Epic850KAppendMetaInformation.parameter_info());
					System.exit(0);
				}
				Epic850KAppendMetaInformation.execute(args_remain);
				// Epic850KHyperHypoMethylationFilter
			} else if (type.equals("-Epic850KHyperHypoMethylationFilter")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -Epic850KHyperHypoMethylationFilter "
									+ Epic850KHyperHypoMethylationFilter
											.parameter_info());
					System.exit(0);
				}
				Epic850KHyperHypoMethylationFilter.execute(args_remain);
				//
			} else if (type.equals("-LeventakiExtractProbeCoordinate")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out
							.println("drppm -LeventakiExtractProbeCoordinate "
									+ LeventakiExtractProbeCoordinate
											.parameter_info());
					System.exit(0);
				}
				LeventakiExtractProbeCoordinate.execute(args_remain);
				// McKinnonGCScatterPlotTTS
			} else if (type.equals("-McKinnonGCScatterPlotTTS")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -McKinnonGCScatterPlotTTS "
							+ McKinnonGCScatterPlotTTS.parameter_info());
					System.exit(0);
				}
				McKinnonGCScatterPlotTTS.execute(args_remain);
				// McKinnonCalculateGCSkew
			} else if (type.equals("-McKinnonCalculateGCSkew")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -McKinnonCalculateGCSkew "
							+ McKinnonCalculateGCSkew.parameter_info());
					System.exit(0);
				}
				McKinnonCalculateGCSkew.execute(args_remain);
				// McKinnonGenerateRandomBEDFile
			} else if (type.equals("-McKinnonGenerateRandomBEDFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -McKinnonGenerateRandomBEDFile "
							+ McKinnonGenerateRandomBEDFile.parameter_info());
					System.exit(0);
				}
				McKinnonGenerateRandomBEDFile.execute(args_remain);
				// MicroArrayIDConversionFlex
			} else if (type.equals("-MicroArrayIDConversionFlex")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -MicroArrayIDConversionFlex "
							+ MicroArrayIDConversionFlex.parameter_info());
					System.exit(0);
				}
				MicroArrayIDConversionFlex.execute(args_remain);
				// HumanMouseSpearmanRankCorrel
			} else if (type.equals("-HumanMouseSpearmanRankCorrel")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -HumanMouseSpearmanRankCorrel "
							+ HumanMouseSpearmanRankCorrel.parameter_info());
					System.exit(0);
				}
				HumanMouseSpearmanRankCorrel.execute(args_remain);
				// GenerateSpearmanRankMatrix
			} else if (type.equals("-GenerateSpearmanRankMatrix")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GenerateSpearmanRankMatrix "
							+ GenerateSpearmanRankMatrix.parameter_info());
					System.exit(0);
				}
				GenerateSpearmanRankMatrix.execute(args_remain);
				// CalculateGraphStatistics
			} else if (type.equals("-CalculateGraphStatistics")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CalculateGraphStatistics "
							+ CalculateGraphStatistics.parameter_info());
					System.exit(0);
				}
				CalculateGraphStatistics.execute(args_remain);
				// LeventakiCombineCNSResult
			} else if (type.equals("-LeventakiCombineCNSResult")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -LeventakiCombineCNSResult "
							+ LeventakiCombineCNSResult.parameter_info());
					System.exit(0);
				}
				LeventakiCombineCNSResult.execute(args_remain);
				// LeventakiGenerateVCFPlot
			} else if (type.equals("-LeventakiGenerateVCFPlot")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -LeventakiGenerateVCFPlot "
							+ LeventakiGenerateVCFPlot.parameter_info());
					System.exit(0);
				}
				LeventakiGenerateVCFPlot.execute(args_remain);
				// SummarizeGSEAResult
			} else if (type.equals("-SummarizeGSEAResult")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SummarizeGSEAResult "
							+ SummarizeGSEAResult.parameter_info());
					System.exit(0);
				}
				SummarizeGSEAResult.execute(args_remain);
				// RemoveRowsWithNAs
			} else if (type.equals("-RemoveRowsWithNAs")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -RemoveRowsWithNAs "
							+ RemoveRowsWithNAs.parameter_info());
					System.exit(0);
				}
				RemoveRowsWithNAs.execute(args_remain);
				// FilterBasedOnAnnotation
			} else if (type.equals("-FilterBasedOnAnnotation")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterBasedOnAnnotation "
							+ FilterBasedOnAnnotation.parameter_info());
					System.exit(0);
				}
				FilterBasedOnAnnotation.execute(args_remain);
				// ReorderIkapColumn
			} else if (type.equals("-ReorderIkapColumn")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ReorderIkapColumn "
							+ ReorderIkapColumn.parameter_info());
					System.exit(0);
				}
				ReorderIkapColumn.execute(args_remain);
				// AppendMADValue
			} else if (type.equals("-AppendMADValue")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -AppendMADValue "
							+ AppendMADValue.parameter_info());
					System.exit(0);
				}
				AppendMADValue.execute(args_remain);
				// SIF2Geneset
			} else if (type.equals("-SIF2Geneset")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -SIF2Geneset "
							+ SIF2Geneset.parameter_info());
					System.exit(0);
				}
				SIF2Geneset.execute(args_remain);
				// CompassGenerateSifFile
			} else if (type.equals("-CompassGenerateSifFile")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CompassGenerateSifFile "
							+ CompassGenerateSifFile.parameter_info());
					System.exit(0);
				}
				CompassGenerateSifFile.execute(args_remain);
				// NetworkNodeReplaceColor
			} else if (type.equals("-NetworkNodeReplaceColor")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -NetworkNodeReplaceColor "
							+ NetworkNodeReplaceColor.parameter_info());
					System.exit(0);
				}
				NetworkNodeReplaceColor.execute(args_remain);
				// FilterSitePhosphoWithPeptidePhospho
			} else if (type.equals("-FilterSitePhosphoWithPeptidePhospho")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterSitePhosphoWithPeptidePhospho "
							+ FilterSitePhosphoWithPeptidePhospho.parameter_info());
					System.exit(0);
				}
				FilterSitePhosphoWithPeptidePhospho.execute(args_remain);
				// FilterReadsForSDScore
			} else if (type.equals("-FilterReadsForSDScore")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -FilterReadsForSDScore "
							+ FilterReadsForSDScore.parameter_info());
					System.exit(0);
				}
				FilterReadsForSDScore.execute(args_remain);
				// CombineAAFreqProteinFeature
			} else if (type.equals("-CombineAAFreqProteinFeature")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -CombineAAFreqProteinFeature "
							+ CombineAAFreqProteinFeature.parameter_info());
					System.exit(0);
				}
				CombineAAFreqProteinFeature.execute(args_remain);
				// JunminPengAnnotateProteinFeature
			} else if (type.equals("-JunminPengAnnotateProteinFeature")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -JunminPengAnnotateProteinFeature "
							+ JunminPengAnnotateProteinFeature.parameter_info());
					System.exit(0);
				}
				JunminPengAnnotateProteinFeature.execute(args_remain);
				// ProteinFeaturePlots
			} else if (type.equals("-ProteinFeaturePlots")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -ProteinFeaturePlots "
							+ ProteinFeaturePlots.parameter_info());
					System.exit(0);
				}
				ProteinFeaturePlots.execute(args_remain);
				// GTFSummaryStatistics 
			} else if (type.equals("-GTFSummaryStatistics")) {
				String[] args_remain = getRemaining(args);
				if (args_remain.length == 0) {
					System.out.println("drppm -GTFSummaryStatistics "
							+ GTFSummaryStatistics.parameter_info());
					System.exit(0);
				}
				GTFSummaryStatistics.execute(args_remain);
				//  
			} else {
				System.out.println("Here are the available programs");
				printProgramInfo();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Provide a list of programs that might match the user input
	 * 
	 * @param userInput
	 * @return
	 */
	public static String provideOptions(String userInput) {
		return userInput;
	}

	public static void printProgramInfo() {
		System.out.println("DRPPM Version Number: "
				+ ProgramDescriptions.VERSION);
		System.out
				.println("Main Categories of DRPPM could be accessed if you type");
		System.out.println("drppm -RNASEQ");
		System.out
				.println("	-LIMMA1 can estimate differential expression within one group vs other samples");
		System.out
				.println("	-LIMMA2 can estimate differential expression within two group of samples");
		System.out
				.println("	-GenerateVolcano plots a volcano plot based on LIMMA result");
		System.out
				.println("	-DEAddAnnotation add annotation to the differential expression");
		System.out
				.println("	-removeNonCoding remove genes tagged as noncoding RNA");
		System.out
				.println("  -CombineFPKMExpression combines two FPKM file into the same file");
		System.out.println("drppm -PROTEOMICS");
		System.out.println("drppm -PHOSPHORYLATION");
		System.out.println("	-plotKinase can plot the kinase mapping");
		System.out
				.println("	-ParseMotifX generate motif file by grabbing the result from MotifX");
		System.out.println("	-MatchMotifX add the matching motif information");

		System.out.println("drppm -DNA");
		System.out
				.println("    -SnpDetectPostProcessingScript can generate the script for SnpDetect post processing");

		System.out.println("drppm -GRAPH");
		System.out.println("	-plotHeatMap can plot heatmap");

		System.out.println("drppm -MISC");
		System.out
				.println("	-human2mouse convert gene list from human to mouse");
		System.out
				.println("	-mouse2human convert gene list from mouse to human");

		System.out.println("drppm -NETWORK");

		System.out
				.println("	-AddGeneKO2SampleName add gene KO information to gene expression matrix");
		System.out
				.println("	-GrabKeyword grab the keyword from header of a gene matrix file.  Specifically useful for limma.");
		System.out
				.println("	-SampleFilter write out the samples containing the input keyword");

		System.out
				.println("	-CreateNetworkDisplay creates a network based on the input text");
		System.out.println("	-RunRScript run R script");
		System.out
				.println("	-GSEAgmt2txt convert GSEA gmt file into a txt file");
		System.out
				.println("	-HumanCentricProteinAlignment perform alignment of the uniprot proteins");
		System.out
				.println("	-MouseCentricProteinAlignment perform alignment of the uniprot proteins");
		System.out
				.println("	-GSEAHuman2Mouse human and mouse homology conversion for GSEA");
		System.out
				.println("	-Gene2TF check whether a TF list contains the gene set specified by the user");
		System.out
				.println("	-GEFisher performs Fisher Exact test to estimate whether a gene set is enriched in a differentially expressed gene set");
		System.out
				.println("	-GEFisherFilter performs Fisher Exact test to estimate whether a gene set is enriched in a differentially expressed gene set and filters the gene set");
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
			// System.out.println("args: " + args[i]);
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
			String str = (String) itr.next();
			args_remain[i] = str;
			// System.out.println(args_remain[i]);
			i++;
		}
		return args_remain;
	}
}
