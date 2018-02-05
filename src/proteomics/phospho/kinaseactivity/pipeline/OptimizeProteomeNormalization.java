package proteomics.phospho.kinaseactivity.pipeline;

import misc.CommandLine;

public class OptimizeProteomeNormalization {

	public static String type() {
		return "PIPELINE";
	}
	public static String parameter_info() {
		return "[id_all_site_norm] [whole_proteome_norm] [kinase_activity_norm] [offset_skip: 0.1] [prefix] [kinase_substrate] [kinase_geneList]";
	}
	public static String description() {
		return "Optimize proteome normalization and generate boxplot";
	}
	public static void execute(String[] args) {
		try {
			
			// normalize phospho against whole
			String id_all_site_norm = args[0];
			String whole_proteome_norm = args[1];
			String kinase_activity_norm = args[2];
			double offset_skip = new Double(args[3]);
			String prefix = args[4];
			String kinase_substrate = args[5];
			String kinase_geneList = args[6];
			
			
			String step1_normalizePhosphoAgainstWholeWithOffset = normalizePhosphoAgainstWholeWithOffset(id_all_site_norm, whole_proteome_norm, offset_skip, prefix);
			CommandLine.writeFile("step1_normalizePhosphoWhole.sh", step1_normalizePhosphoAgainstWholeWithOffset);
			String step2_whlPhoSpearmanRankCorrelation = whlPhoSpearmanRankCorrelation(prefix, kinase_activity_norm, kinase_geneList, offset_skip);
			CommandLine.writeFile("step2_whlPhoSpearmanRankCorrelation.sh", step2_whlPhoSpearmanRankCorrelation);
			String step3_combinePhosphositeCorrelationResult = combinePhosphositeCorrelationResult(prefix, offset_skip);
			CommandLine.writeFile("step3_combinePhosphositeCorrelationResult.sh", step3_combinePhosphositeCorrelationResult);
			String step4_appendKinaseTargetInformation2Matrix = appendKinaseTargetInformation2Matrix(prefix, kinase_substrate, offset_skip);
			CommandLine.writeFile("step4_appendKinaseTargetInformation2Matrix.sh", step4_appendKinaseTargetInformation2Matrix);
			String step5_phosphositeMetaScoreSensitivitySpecificity = phosphositeMetaScoreSensitivitySpecificity(prefix, offset_skip);
			CommandLine.writeFile("step5_phosphositeMetaScoreSensitivitySpecificity.sh", step5_phosphositeMetaScoreSensitivitySpecificity);
			String step6_draw_boxplot_script = draw_boxplot(prefix);
			CommandLine.writeFile(prefix + "_boxplot.r", step6_draw_boxplot_script);
			String step6_draw_boxplot = "R --vanilla < " + prefix + "_boxplot.r\n";
			CommandLine.writeFile("step6_draw_boxplot.sh", step6_draw_boxplot);
			
			System.out.println("sh step1_normalizePhosphoWhole.sh");
			System.out.println("sh step2_whlPhoSpearmanRankCorrelation.sh");
			System.out.println("sh step3_combinePhosphositeCorrelationResult.sh");
			System.out.println("sh step4_appendKinaseTargetInformation2Matrix.sh");
			System.out.println("sh step5_phosphositeMetaScoreSensitivitySpecificity.sh");
			System.out.println("sh step6_draw_boxplot.sh");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String normalizePhosphoAgainstWholeWithOffset(String id_all_site_norm, String whole_proteome_norm, double offset_skip, String prefix) {
		String script = "";
		for (double cutoff = 0; cutoff <= 1; cutoff += offset_skip) {
			cutoff = new Double(Math.round(cutoff * 100)) / 100;
			script += "drppm -NormalizePhosphoAgainstWholeWithOffset " + id_all_site_norm + " " + whole_proteome_norm + " " + cutoff + " " + prefix + "_normalized_" + cutoff + "cutoff.txt false\n";
			
		}
		return script;
	}
	public static String whlPhoSpearmanRankCorrelation(String prefix, String whole_proteome_norm, String kinase_list, double offset_skip) {
		String script = "";
		for (double cutoff = 0; cutoff <= 1; cutoff += offset_skip) {
			cutoff = new Double(Math.round(cutoff * 100)) / 100;
			script += "drppm -WhlPhoSpearmanRankCorrelation " + prefix + "_normalized_" + cutoff + "cutoff.txt " + whole_proteome_norm + " 10 " + kinase_list + " " + prefix + "_normalized_" + cutoff + "cutoff_spearmanRank.txt\n";
			String fileText = prefix + cutoff + "\t" + prefix + "_normalized_" + cutoff + "cutoff_spearmanRank.txt";
			CommandLine.writeFile("fileList_" + prefix + cutoff + ".txt", fileText);
		}
		return script;
	}
	public static String combinePhosphositeCorrelationResult(String prefix, double offset_skip) {
		String script = "";
		for (double cutoff = 0; cutoff <= 1; cutoff += offset_skip) {
			cutoff = new Double(Math.round(cutoff * 100)) / 100;
			script += "drppm -CombinePhosphositeCorrelationResult " + "fileList_" + prefix + cutoff + ".txt" + " output_" + cutoff + "DifferentCutoff_" + prefix + ".txt\n";
		}
		return script;
	}
	public static String appendKinaseTargetInformation2Matrix(String prefix, String kinase_substrate, double offset_skip) {
		String script = "";
		for (double cutoff = 0; cutoff <= 1; cutoff += offset_skip) {
			cutoff = new Double(Math.round(cutoff * 100)) / 100;
			script += "drppm -AppendKinaseTargetInformation2Matrix output_" + cutoff + "DifferentCutoff_" + prefix + ".txt " + kinase_substrate + " no output_" + cutoff + "DifferentCutoff_" + prefix + "_result_all.txt\n";
		}
		return script;
	}
	
	public static String phosphositeMetaScoreSensitivitySpecificity(String prefix, double offset_skip) {
		String script = "";
		String fileList = "Title\tPath\n";
		for (double cutoff = 0; cutoff <= 1; cutoff += offset_skip) {
			cutoff = new Double(Math.round(cutoff * 100)) / 100;
			script += "mkdir " + prefix + " + cutoff\n";
			script += "drppm -PhosphositeMetaScoreSensitivitySpecificity output_" + cutoff + "DifferentCutoff_" + prefix + "_result_all.txt " + prefix + cutoff + " output_" + cutoff + "DifferentCutoff_" + prefix + "_result_all_ROC.txt\n";
			fileList += prefix + cutoff + "\toutput_" + cutoff + "DifferentCutoff_" + prefix + "_result_all_ROC.txt\n"; 
		}
		
		CommandLine.writeFile("fileList_" + prefix + "_ROC_values.lst", fileList);
		script += "drppm -KinaseSubstrateMergeROCResult " + "fileList_" + prefix + "_ROC_values.lst" + " 5 " + "Summarize_" + prefix + "_ROC_values.txt\n";
		return script;
		
	}

	
	public static String draw_boxplot(String prefix) {
		String script = "library(ggplot2);\n";
		script += "data = read.table(\"Summarize_" + prefix + "_ROC_values.txt\", sep=\"\\t\",header=T);\n";
		script += "p1 = ggplot(data, aes(factor(Type), ROC, fill=Type)) + geom_violin() + geom_boxplot(width=0.2) + theme(plot.title=element_text(size=15), axis.text.x = element_text(size=15, angle = 90), axis.text.y = element_text(size=15), axis.title = element_text(size=15), legend.text = element_text(size=15), legend.title = element_text(size=15))\n";
		script += "png(file = \"Summarize_" + prefix + "_ROC_values.png\", width=1300,height=600)\n";
		script += "p1;\n";
		script += "dev.off();\n";
		return script;
	}
	
}
