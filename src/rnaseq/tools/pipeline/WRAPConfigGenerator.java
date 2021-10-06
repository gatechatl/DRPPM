package rnaseq.tools.pipeline;

/**
 * Generate a shell script to download all the references and sets up a config file for the RNAseq analysis
 * @author gatechatl
 *
 */
public class WRAPConfigGenerator {


	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Generates the entire script for the RNAseq analysis pipeline.";
	}
	public static String parameter_info() {
		return "[Location: STJUDE or MOFFITT or DEBUG] [Analysis Type: ALL, Mapping_Only, QC_Only, Splicing_Only, Expression_Only, MappingSplicing, MappingExpression]";
	}
	public static void execute(String[] args) {
		String config_type = args[0];
		String analysis_type = args[1];
		StringBuffer string_buffer = new StringBuffer();
		if (config_type.equalsIgnoreCase("MOFFITT")) {
			
			string_buffer.append("# config file for the WRAP: wrapper for my RNA analysis pipeline");
			string_buffer.append("# STAR mapping");
			string_buffer.append("STAR_INDEX_DIR = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/hg38_release35_GRCh38.p13/FASTA/star/");
			string_buffer.append("CHR_NAME_LENGTH_FILE = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/hg38_release35_GRCh38.p13/FASTA/star/chrNameLength.txt");
			string_buffer.append("# RSEQC");
			string_buffer.append("RSEQC_REFSEQ_BED = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/rseqc/hg38_RefSeq.bed");
			string_buffer.append("RSEQC_HOUSE_KEEPING_GENE_BED = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/rseqc/hg38.HouseKeepingGenes.bed");
			string_buffer.append("RSEQC_RIBOSOME_BED = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/rseqc/hg38_rRNA.bed");
			string_buffer.append("RSEQC_NOWIG = true");
			string_buffer.append("RSEQC_GTF = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/rseqc/gencode.v22.annotation.gtf");
			string_buffer.append("RSEQC_GENEINFO_TSV = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/rseqc/gencode.gene.info.v22.tsv");
			string_buffer.append("# PSIPSO");
			string_buffer.append("PRIMARY_GTF_REF = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/hg38_release35_GRCh38.p13/GTF/gencode.v35.primary_assembly.annotation.gtf");
			string_buffer.append("PRIMARY_GTF_EXON_REF = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/hg38_release35_GRCh38.p13/pipeline/ExonReference/gencode.v35.primary_assembly.annotation.exon.gtf");
			string_buffer.append("SPLICING_DEFICIENCY_CONFIG = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/hg38_release35_GRCh38.p13/pipeline/splicing_deficiency/splicing_deficiency.config");
			string_buffer.append("# JUNCSALVAGER");
			string_buffer.append("JUNCSALVAGER_GENELIST = /rgs01/resgen/dev/wc/tshaw/REFERENCE/WRAP/hg38/GTF/JUNCSALVAGER/short_gene_list.txt");
			string_buffer.append("# EXON PIPELINE");
			string_buffer.append("PRIMARY_GTF_EXON_LENGTH = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/hg38_release35_GRCh38.p13/pipeline/ExonReference/gencode.v35.primary_assembly.annotation.exon.length.txt");
			string_buffer.append("# OUTPUT Files");
			string_buffer.append("OUTPUT_HTSEQGENE_FILELST = htseq_gene_files.lst");
			string_buffer.append("OUTPUT_RSEQC_FILELST = rseqc_files.lst");
			string_buffer.append("OUTPUT_HTSEQEXON_FILELST = htseq_exon_files.lst");
			string_buffer.append("OUTPUT_STARfinalout_FILELST = star_finalout_files.lst");
			string_buffer.append("OUTPUT_SPLICING_DEFICIENCY_FILELST = splicing_deficiency_files.lst");
			string_buffer.append("OUTPUT_PSI_PSO_CALC_FILELST = psi_pso_files.lst");
			string_buffer.append("# KALLISTO PIPELINE");
			string_buffer.append("# RSEM PIPELINE");
			string_buffer.append("# GENESET ssGSEA analysis ");
			string_buffer.append("# RNAEDITING PIPELINE");
			string_buffer.append(" PRIMARY_FASTA = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/hg38_release35_GRCh38.p13/FASTA/GRCh38.primary_assembly.genome.fa");
			string_buffer.append(" RNAEDITING_VARIANTS = /share/dept_bbsr/Projects/Shaw_Timothy/3352_Splicing_Pipeline_2021/references/hg38_release35_GRCh38.p13/pipeline/rnaediting_references/pcgp_rnaediting_hg38_sites_updated.txt");
			string_buffer.append("#");
			string_buffer.append("# # Run Optityping");
			string_buffer.append(" OPTITYPE_PROGRAM = /home/gatechatl/anaconda3/bin/OptiTypePipeline.py");
			string_buffer.append("# ");
			string_buffer.append("# Check expression of mitochondria");
			string_buffer.append("# Check expression of Ribosomes");
			string_buffer.append("# Check expression of representative lincRNA");
			string_buffer.append("# Check expression of HERV RNAs");
			string_buffer.append("## Advance mode ##");
			
		}
		
		if (analysis_type.equalsIgnoreCase("ALL")) {
			string_buffer.append("SKIP_BAM2FASTQ = false");
			string_buffer.append("SKIP_STAR = false");
			string_buffer.append("SKIP_FASTQC = false");
			string_buffer.append("SKIP_RSEQC = false");
			string_buffer.append("SKIP_PSI_PSO_CALC = false");
			string_buffer.append("SKIP_SPLICING_DEFICIENCY = false");
			string_buffer.append("SKIP_HTSEQ_EXON_QUANT = false");
			string_buffer.append("SKIP_HTSEQ_GENE = false");
			string_buffer.append("SKIP_JUNCSALVAGER = false");
			string_buffer.append("SKIP_RNAEDIT = true"); // skipping for now
			string_buffer.append("SKIP_OPTITYPE = true");  // skipping for now
			string_buffer.append("SKIP_KNOWNVARIANTS = true"); // skipping for now
			string_buffer.append("SKIP_RNAINDEL = true"); // skipping for now
		} else {
			if (analysis_type.contains("Mapping") || analysis_type.contains("mapping") || analysis_type.contains("MAPPING")) {
				string_buffer.append("SKIP_BAM2FASTQ = false");
				string_buffer.append("SKIP_STAR = false");	
			} else {
				string_buffer.append("SKIP_BAM2FASTQ = true");
				string_buffer.append("SKIP_STAR = true");
			}
			
			if (analysis_type.contains("QC") || analysis_type.contains("qc") || analysis_type.contains("Qc")) {
				string_buffer.append("SKIP_FASTQC = false");
				string_buffer.append("SKIP_RSEQC = false");
			} else {
				string_buffer.append("SKIP_FASTQC = true");
				string_buffer.append("SKIP_RSEQC = true");
			}
			
			if (analysis_type.contains("Splicing") || analysis_type.contains("SPLICING") || analysis_type.contains("splicing")) {
				string_buffer.append("SKIP_PSI_PSO_CALC = false");
				string_buffer.append("SKIP_SPLICING_DEFICIENCY = false");
				string_buffer.append("SKIP_HTSEQ_EXON_QUANT = false");
				string_buffer.append("SKIP_HTSEQ_GENE = false");
			} else {
				string_buffer.append("SKIP_PSI_PSO_CALC = true");
				string_buffer.append("SKIP_SPLICING_DEFICIENCY = true");
				string_buffer.append("SKIP_HTSEQ_EXON_QUANT = true");				
				if (analysis_type.contains("Expression") || analysis_type.contains("EXPRESSION") || analysis_type.contains("expression")) {
					string_buffer.append("SKIP_HTSEQ_GENE = false");
				} else {
					string_buffer.append("SKIP_HTSEQ_GENE = true");
				}
			}
			
			if (analysis_type.contains("NovelJunc") || analysis_type.contains("NOVELJUNC") || analysis_type.contains("noveljunc")  || analysis_type.contains("NovelJunc")) {
				string_buffer.append("SKIP_JUNCSALVAGER = false");
			} else {
				string_buffer.append("SKIP_JUNCSALVAGER = true");
			}						

			string_buffer.append("SKIP_RNAEDIT = true"); // skipping for now
			string_buffer.append("SKIP_OPTITYPE = true");  // skipping for now
			string_buffer.append("SKIP_KNOWNVARIANTS = true"); // skipping for now
			string_buffer.append("SKIP_RNAINDEL = true"); // skipping for now
		}
	}
}
