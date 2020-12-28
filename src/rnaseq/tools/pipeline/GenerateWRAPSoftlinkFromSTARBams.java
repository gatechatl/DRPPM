package rnaseq.tools.pipeline;


/**
 * Generate WRAP compatible folder structure using existing STAR bams
 * 
 * @author gatechatl
 *
 */
public class GenerateWRAPSoftlinkFromSTARBams {
	
	
	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Generates the entire script for the RNAseq analysis pipeline.";
	}
	public static String parameter_info() {
		return "[inputSTARBamFileLst] [Output FolderPath2Setup]";
	}

}
