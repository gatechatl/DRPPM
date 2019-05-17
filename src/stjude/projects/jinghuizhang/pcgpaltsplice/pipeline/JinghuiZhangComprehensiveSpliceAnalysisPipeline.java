package stjude.projects.jinghuizhang.pcgpaltsplice.pipeline;

public class JinghuiZhangComprehensiveSpliceAnalysisPipeline {

	public static String description() {
		String description = "Provide a comprehensive wrapper for the entire splicing analysis pipeline.\n";
		description += "1) bam2fastq conversion\n";
		description += "2) mapping to hg19 and hg38\n";
		description += "3) intron retention analysis\n";
		description += "4) psi score calculation\n";
		description += "5) denovo cufflinks transcript assembly\n";
		return description;
	}
	public static void execute(String[] args) {
		
	}
}
