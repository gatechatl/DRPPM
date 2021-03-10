package rnaseq.splicing.csiminer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Comprehensive pipeline for performing the cancer specific isoform prioritization
 * @author gatechatl
 *
 */
public class CSIMinerPipeline {
	
	public static String type() {
		return "CSI-Miner";
	}
	public static String description() {
		return "Generates the script for the CSI Miner prioritization. The analysis performs a pairwise comparison based on meta analysis and wilcoxon testing.";
	}
	public static String parameter_info() {
		return "[parameter file] [outputFolder] [outputShellScript]";
	}
	
	private static String CANCER_EXON_MATRIX = "NA";
	private static String NORM_EXON_MATRIX = "NA";
	private static String QUERY_GENELIST = "NA"; // for CAR-T ECM and surfaceome gene list will be applied
	private static String CANCER_SAMPLE2DISEASETYPE = "NA";
	private static String NORM_SAMPLE2TISSUETYPE = "NA";
	
	private static String CANCER_PREFIX = "NA";
	private static String NORM_PREFIX = "NA";
	public static void execute(String[] args) {
		
		try {

			String runtime_config_file = args[0];			
			String outputFolder_prefix = args[1];
			String outputShellScript = args[2];
			String outputIntermediateFolder_prefix = "Intermediate";
			
			String current_working_dir = System.getProperty("user.dir");
			
			String outputFolder = current_working_dir + "/" + outputFolder_prefix;
			String outputIntermediateFolder = current_working_dir + "/" + outputIntermediateFolder_prefix;
			File outputFolder_f = new File(outputFolder);
			if (!outputFolder_f.exists()) {
				outputFolder_f.mkdir();
			}
			File outputIntermediateFolder_f = new File(outputIntermediateFolder);
			if (!outputIntermediateFolder_f.exists()) {
				outputIntermediateFolder_f.mkdir();
			}
			

			FileWriter fwriter = new FileWriter(outputShellScript);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			
			
			// parsing the runtime config file
			FileInputStream fstream = new FileInputStream(runtime_config_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.length() > 0) {
					if (!str.substring(0, 1).equals("#")) {
						String[] split = str.split("=");
												
						if (str.contains("=")) {
							
							split[0] = split[0].trim();
							split[1] = split[1].trim();
							
							if (split[0].equalsIgnoreCase("CANCER_EXON_MATRIX")) {
								CANCER_EXON_MATRIX = split[1];
							}
							if (split[0].equalsIgnoreCase("NORM_EXON_MATRIX")) {
								NORM_EXON_MATRIX = split[1];
							}
							if (split[0].equalsIgnoreCase("QUERY_GENELIST")) {
								QUERY_GENELIST = split[1];
							}
							if (split[0].equalsIgnoreCase("CANCER_SAMPLE2DISEASETYPE")) {
								CANCER_SAMPLE2DISEASETYPE = split[1];
							}
							if (split[0].equalsIgnoreCase("NORM_SAMPLE2TISSUETYPE")) {
								NORM_SAMPLE2TISSUETYPE = split[1];
							}
							
							if (split[0].equalsIgnoreCase("CANCER_PREFIX")) {
								CANCER_PREFIX = split[1];
							}
							if (split[0].equalsIgnoreCase("NORM_PREFIX")) {
								NORM_PREFIX = split[1];
							}
							/*
							if (split[0].equalsIgnoreCase("RSEQC_REFSEQ_BED")) {
								RSEQC_REFSEQ_BED = split[1];
							}
							if (split[0].equalsIgnoreCase("RSEQC_RIBOSOME_BED")) {
								RSEQC_RIBOSOME_BED = split[1];
							}
							if (split[0].equalsIgnoreCase("SPLICING_DEFICIENCY_CONFIG")) {
								SPLICING_DEFICIENCY_CONFIG = split[1];
							}
							if (split[0].equalsIgnoreCase("PRIMARY_GTF_REF")) {
								PRIMARY_GTF_REF = split[1];
							}
							if (split[0].equalsIgnoreCase("PRIMARY_GTF_EXON_REF")) {
								PRIMARY_GTF_EXON_REF = split[1];
							}
							if (split[0].equalsIgnoreCase("JUNCSALVAGER_GENELIST")) {
								JUNCSALVAGER_GENELIST = split[1];
							}
							if (split[0].equalsIgnoreCase("PRIMARY_GTF_EXON_LENGTH")) {
								PRIMARY_GTF_EXON_LENGTH = split[1];
							}
							
							
							if (split[0].equalsIgnoreCase("SKIP_BAM2FASTQ")) {
								SKIP_BAM2FASTQ = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_STAR")) {
								SKIP_STAR = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_FASTQC")) {
								SKIP_FASTQC = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_RSEQC")) {
								SKIP_RSEQC = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_PSI_PSO_CALC")) {
								SKIP_PSI_PSO_CALC = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_SPLICING_DEFICIENCY")) {
								SKIP_SPLICING_DEFICIENCY = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_HTSEQ_EXON_QUANT")) {
								SKIP_HTSEQ_EXON_QUANT = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_HTSEQ_GENE")) {
								SKIP_HTSEQ_GENE = new Boolean(split[1]);
							}
							if (split[0].equalsIgnoreCase("SKIP_JUNCSALVAGER")) {
								SKIP_JUNCSALVAGER = new Boolean(split[1]);
							}
							*/
						}
					}
				}
			}
			in.close();
			
			String cancer_exon_matrix_gene_filter = outputIntermediateFolder + "/" + CANCER_PREFIX + ".exon.matrix.genefilter.txt";
			String cancer_exon_matrix_gene_filter_precleaned = outputIntermediateFolder + "/" + CANCER_PREFIX + ".exon.matrix.genefilter.precleaned.txt";
			
			String cancer_exon_matrix_gene_filter_cleaned = outputIntermediateFolder + "/" + CANCER_PREFIX + ".exon.matrix.genefilter.cleaned.txt";
			
			String cancer_exon_matrix_gene_filter_cleaned2 = outputIntermediateFolder + "/" + CANCER_PREFIX + ".exon.matrix.genefilter.cleaned2.txt";
			String cancer_exon_matrix_percentile = outputIntermediateFolder + "/" + CANCER_PREFIX + ".exon.matrix.percentile.txt";
			String cancer_exon_matrix_binned = outputIntermediateFolder + "/" + CANCER_PREFIX + ".exon.matrix.binned.txt";


			String norm_exon_matrix_gene_filter = outputIntermediateFolder + "/" + NORM_PREFIX + ".exon.matrix.genefilter.txt";
			String norm_exon_matrix_gene_filter_precleaned = outputIntermediateFolder + "/" + NORM_PREFIX + ".exon.matrix.genefilter.precleaned.txt";
			
			String norm_exon_matrix_gene_filter_cleaned = outputIntermediateFolder + "/" + NORM_PREFIX + ".exon.matrix.genefilter.cleaned.txt";
			String norm_exon_matrix_gene_filter_cleaned2 = outputIntermediateFolder + "/" + NORM_PREFIX + ".exon.matrix.genefilter.cleaned2.txt";
			String norm_exon_matrix_percentile = outputIntermediateFolder + "/" + NORM_PREFIX + ".exon.matrix.percentile.txt";
			String norm_exon_matrix_binned = outputIntermediateFolder + "/" + NORM_PREFIX + ".exon.matrix.binned.txt";

			String wilcoxon_result = CANCER_PREFIX + "_" + NORM_PREFIX + "_WILCOX_RESULT.txt";
			String meta_analysis = CANCER_PREFIX + "_" + NORM_PREFIX + "_RAW_METAANALYSIS_RESULT.txt";

			String summarize_meta_analysis_result = CANCER_PREFIX + "_" + NORM_PREFIX + "_SUMMARIZED_METAANALYSIS_RESULT.txt";
			String summarize_weighted_meta_analysis_result = CANCER_PREFIX + "_" + NORM_PREFIX + "_SUMMARIZED_METAANALYSIS_RESULT.txt";

			StringBuffer string_buffer = new StringBuffer();
			
			//out.write("## bam2fastq conversion ##\n");
			

			
			string_buffer.append("## CSI-miner processing of cancer samples ##\n");
			//out.write("cd " + outputIntermediateFolder + "/" + sampleName + "/fastq/" + "\n");
			string_buffer.append("drppm -CSIMinerConsolidateInputs " + CANCER_EXON_MATRIX + " " + NORM_EXON_MATRIX + " " + CANCER_EXON_MATRIX + ".consolidate " + NORM_EXON_MATRIX + ".consolidate");
			
			string_buffer.append("drppm -CSIMinerFilterExonMatrixByGeneSymbol " + CANCER_EXON_MATRIX + ".consolidate " + QUERY_GENELIST + " " + cancer_exon_matrix_gene_filter + "\n");
			string_buffer.append("drppm -RemoveColumnsFromMatrix " + cancer_exon_matrix_gene_filter + " 1,2,3,4,5,6 " + cancer_exon_matrix_gene_filter_precleaned + "\n");
			string_buffer.append("drppm -SampleFilter " + cancer_exon_matrix_gene_filter_precleaned + " Annotation " + cancer_exon_matrix_gene_filter_cleaned + " no\n");
			
			string_buffer.append("drppm -CSIMinerCalculatePercentileCutoff " + cancer_exon_matrix_gene_filter_cleaned + " " + cancer_exon_matrix_percentile + " " + cancer_exon_matrix_binned + "\n");
			
			string_buffer.append("drppm -CSIMinerSplitMatrixCandidates " + cancer_exon_matrix_gene_filter_cleaned + " " + CANCER_SAMPLE2DISEASETYPE + " " + CANCER_PREFIX + "\n");
			
			string_buffer.append("drppm -CSIMinerFilterExonMatrixByGeneSymbol " + NORM_EXON_MATRIX + ".consolidate " + QUERY_GENELIST + " " + norm_exon_matrix_gene_filter + "\n");
			string_buffer.append("drppm -RemoveColumnsFromMatrix " + norm_exon_matrix_gene_filter + " 1,2,3,4,5,6 " + norm_exon_matrix_gene_filter_precleaned + "\n");
			string_buffer.append("drppm -SampleFilter " + norm_exon_matrix_gene_filter_precleaned + " Annotation " + norm_exon_matrix_gene_filter_cleaned + " no\n");
			
			
			string_buffer.append("drppm -CSIMinerCalculatePercentileCutoff " + norm_exon_matrix_gene_filter_cleaned + " " + norm_exon_matrix_percentile + " " + norm_exon_matrix_binned + "\n");
			string_buffer.append("drppm -CSIMinerSplitMatrixCandidates " + norm_exon_matrix_gene_filter_cleaned + " " + NORM_SAMPLE2TISSUETYPE + " " + NORM_PREFIX + "\n");
			string_buffer.append("drppm -JuncSalvagerWilcoxonTestRank " + CANCER_PREFIX + " " + CANCER_SAMPLE2DISEASETYPE + " " + NORM_PREFIX + " " + NORM_SAMPLE2TISSUETYPE + " " + wilcoxon_result + " " + meta_analysis + "\n");
			string_buffer.append("drppm -JuncSalvagerWilcoxTestPostProcessing " + wilcoxon_result + " " + CANCER_PREFIX + " " + CANCER_SAMPLE2DISEASETYPE + " " + NORM_PREFIX + " " + NORM_SAMPLE2TISSUETYPE + " " + summarize_meta_analysis_result + " " + summarize_weighted_meta_analysis_result + "\n");
			
			// need to generate plots and other stuff
			
			
			//out.write("drppm -Bam2Fastq " + sampleName + "_bam_file.lst bam2fastq.sh" + "\n");
			
			string_buffer.append("## end CSI-miner processing ##\n\n");								

			// finally write out the shell script
			out.write(string_buffer.toString());
			out.close();
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
