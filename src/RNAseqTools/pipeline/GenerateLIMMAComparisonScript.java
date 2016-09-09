package RNAseqTools.pipeline;

import MISC.CommandLine;

public class GenerateLIMMAComparisonScript {

	public static String type() {
		return "LIMMA";
	}
	public static String description() {
		return "Generate LIMMA script";
	}
	public static String parameter_info() {
		return "[inputFile] [keywords] [pvalue] [logFC] [lengthPath] [fpkmCutoff] [geneLength]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String keywords = args[1];
			String pvalue = args[2];
			String logFC = args[3];
			String lengthPath = args[4];
			String fpkmcutoff = args[5];
			String geneLength = args[6];
			String[] split_keywords = keywords.split(",");
			if (split_keywords.length < 2) {
				System.out.println("Need to split keywords with ',' i.e. NTRK,PDGFRA");
				System.exit(0);
			}
			String keyword1 = split_keywords[0];
			String keyword2 = split_keywords[1];
			String fileName = keyword1 + "_vs_" + keyword2;
			String step1_grabKeyWord = createGrabKeywordScript(inputFile, keywords);
			CommandLine.writeFile("step1_" + fileName + "_grabKeyWord.sh", step1_grabKeyWord);
			String step2_limma = createLIMMAscript(inputFile, split_keywords[0], split_keywords[1], pvalue, logFC);
			CommandLine.writeFile("step2_" + fileName + "_limma.sh", step2_limma);		
			String step3_length = createLengthScript(fileName, lengthPath, fpkmcutoff, geneLength);
			CommandLine.writeFile("step3_" + fileName + "_length.sh", step3_length);
			String step4_slidingwindow = transcriptLengthSlidingWindowScript(fileName);
			CommandLine.writeFile("step4_" + fileName + "_slidingwindow.sh", step4_slidingwindow);
			String step5_inhibitedGenes = transcriptLengthSlidingWindowInhibitedGenes(fileName);
			CommandLine.writeFile("step5_" + fileName + "_inhibitedGenes.sh", step5_inhibitedGenes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String createGrabKeywordScript(String inputFile, String keywords) {
		String script = "";
		String[] split = keywords.split(",");
		
		for (String keyword: split) {
			String name = keyword.replaceAll(",", "_");
			script += "drppm -GrabKeyword " + inputFile + " " + keyword + " > " + name + ".txt\n";
		}
		return script;
	}
	public static String createLIMMAscript(String inputFile, String keyword1, String keyword2, String pvalue, String logFC) {
		String fileName = keyword1 + "_vs_" + keyword2;
		String script = "";
		script += "drppm -LIMMA2Flex " + inputFile + " " + keyword1 + ".txt " + keyword2 + ".txt " + fileName + "_up.txt " + fileName + "_dn.txt " + fileName + "_all.txt " + pvalue + " " + logFC + " true false > " + fileName + "_limma.r" + "\n";
		script += "R --vanilla < " + fileName + "_limma.r";
		return script;
	}
	public static String createLengthScript(String fileName, String lengthPath, String fpkmcutoff, String geneLength) {
		String script = "";
		script += "drppm -CompareGeneLengthDistribution " + fileName + "_up.txt" + " " + fileName + "_dn.txt" + " " + fileName + "_all.txt" + " " + lengthPath + " TranscriptLength " + fpkmcutoff + " " + geneLength + " " + fileName + "_Length.txt" + " " + fileName + "_FoldChangeScatter.txt";
		return script;
	}
	public static String transcriptLengthSlidingWindowScript(String fileName) {
		String script = "";
		script += "drppm -TranscriptLengthSlidingWindow " + fileName + "_FoldChangeScatter.txt 200 " + fileName + "_FoldChangeScatter_TranscriptLength_win200.txt -1.5 1.5";
		return script;
	}
	public static String transcriptLengthSlidingWindowInhibitedGenes(String fileName) {
		String script = "";
		script += "drppm -TranscriptLengthSlidingWindowInhibitedGenes " + fileName + "_FoldChangeScatter.txt 100 " + fileName + "_FoldChangeScatter_TranscriptLength_InhibitedGene_win100.txt 0 1";
		return script;
	}
}
