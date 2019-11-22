package expressionanalysis.tools.unsupervised;

/**
 * Generate and perform pvclust clustering with bootstrapping
 * Example: /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/misc/AlexGoutStJudeCloudAnalysis/UnsupervisedClustering
 * @author tshaw
 *
 */
public class GenerateRScriptForPVClust {
	public static String type() {
		return "R";
	}
	public static String description() {
		return "Perform pvclust.";
	}
	public static String parameter_info() {
		return "[inputFile] [method_hclust: average, ward.D, ward.D2, single, complete, mcquitty, median or centroid] [method_dist: euclidean, maximum, manhattan, canberra, binary or minkowski] [num_boot: maybe 100 or 1000] [outputPrefixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String method_hclust = args[1]; // suggest using ward.D
			String method_dist = args[2]; // suggest using euclidean
			int num_boot = new Integer(args[3]);
			String outputFile = args[4];
			System.out.println(generate_pvclust_script(inputFile, method_hclust, method_dist, num_boot, outputFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String generate_pvclust_script(String inputFile, String method_hclust, String method_dist, int num_boot, String outputFile) {
		String script = "options(bitmapType='cairo')\n";
		script += "library(pvclust)\n";
		script += "exp.in <- \"" + inputFile + "\"\n";
		script += "exp <- read.table(exp.in,sep=\"\\t\",header=T,row.names=1,quote=\"\")\n";
		script += "zexp = apply(exp, 1, scale);\n";
		script += "zexp = apply(zexp, 1, rev)\n";
		script += "colnames(zexp) = colnames(exp)\n";
		script += "pv_result = pvclust(zexp, method.hclust=\"" + method_hclust + "\", method.dist= \"" + method_dist + "\", use.cor=\"pairwise.complete.obs\", nboot=" + num_boot + ")\n";
		script += "png(file = \"" + outputFile + ".png\", width=1000,height=800)\n";
		script += "plot(pv_result)\n";
		script += "pvrect(pv_result, alpha = 0.8)\n";
		script += "dev.off()\n";
		script += "# manually examine and select the K that were called as significance\n";
		
		script += "clusters60 <- pvpick(pv_result, alpha=0.60)\n";
		script += "clusters65 <- pvpick(pv_result, alpha=0.65)\n";
		script += "clusters70 <- pvpick(pv_result, alpha=0.7)\n";
		script += "clusters75 <- pvpick(pv_result, alpha=0.75)\n";
		script += "clusters80 <- pvpick(pv_result, alpha=0.8)\n";
		script += "clusters85 <- pvpick(pv_result, alpha=0.85)\n";
		script += "clusters90 <- pvpick(pv_result, alpha=0.9)\n";
		script += "clusters95 <- pvpick(pv_result, alpha=0.95)\n";
				
		script += "if (file.exists(\"" + outputFile + "_Cluster60.txt\")) {\n";
		script += "    file.remove(\"" + outputFile + "_Cluster60.txt\");\n";
		script += "}\n";
		script += "if (file.exists(\"" + outputFile + "_Cluster65.txt\")) {\n";
		script += "    file.remove(\"" + outputFile + "_Cluster65.txt\");\n";
		script += "}\n";
		script += "if (file.exists(\"" + outputFile + "_Cluster70.txt\")) {\n";
		script += "    file.remove(\"" + outputFile + "_Cluster70.txt\");\n";
		script += "}\n";
		script += "if (file.exists(\"" + outputFile + "_Cluster75.txt\")) {\n";
		script += "    file.remove(\"" + outputFile + "_Cluster75.txt\");\n";
		script += "}\n";
		script += "if (file.exists(\"" + outputFile + "_Cluster80.txt\")) {\n";
		script += "    file.remove(\"" + outputFile + "_Cluster80.txt\");\n";
		script += "}\n";
		script += "if (file.exists(\"" + outputFile + "_Cluster85.txt\")) {\n";
		script += "    file.remove(\"" + outputFile + "_Cluster85.txt\");\n";
		script += "}\n";
		script += "if (file.exists(\"" + outputFile + "_Cluster90.txt\")) {\n";
		script += "    file.remove(\"" + outputFile + "_Cluster90.txt\");\n";
		script += "}\n";
		script += "if (file.exists(\"" + outputFile + "_Cluster95.txt\")) {\n";
		script += "    file.remove(\"" + outputFile + "_Cluster95.txt\");\n";
		script += "}\n";

		script += "for (i in 1:length(clusters60$clusters)) {\n";
		script += "    write(paste(\"Cluster\",i), file = \"" + outputFile + "_Cluster60.txt\", append=T)\n";
		script += "    write(clusters60$clusters[[i]], file = \"" + outputFile + "_Cluster60.txt\", append=T)\n";
		script += "}\n";
		
		script += "for (i in 1:length(clusters65$clusters)) {\n";
		script += "    write(paste(\"Cluster\",i), file = \"" + outputFile + "_Cluster65.txt\", append=T)\n";
		script += "    write(clusters65$clusters[[i]], file = \"" + outputFile + "_Cluster65.txt\", append=T)\n";
		script += "}\n";
		
		script += "for (i in 1:length(clusters70$clusters)) {\n";
		script += "    write(paste(\"Cluster\",i), file = \"" + outputFile + "_Cluster70.txt\", append=T)\n";
		script += "    write(clusters70$clusters[[i]], file = \"" + outputFile + "_Cluste70.txt\", append=T)\n";
		script += "}\n";
		
		script += "for (i in 1:length(clusters75$clusters)) {\n";
		script += "    write(paste(\"Cluster\",i), file = \"" + outputFile + "_Cluster75.txt\", append=T)\n";
		script += "    write(clusters75$clusters[[i]], file = \"" + outputFile + "_Cluster75.txt\", append=T)\n";
		script += "}\n";
		
		script += "for (i in 1:length(clusters80$clusters)) {\n";
		script += "    write(paste(\"Cluster\",i), file = \"" + outputFile + "_Cluster80.txt\", append=T)\n";
		script += "    write(clusters80$clusters[[i]], file = \"" + outputFile + "_Cluster80.txt\", append=T)\n";
		script += "}\n";
		
		script += "for (i in 1:length(clusters85$clusters)) {\n";
		script += "    write(paste(\"Cluster\",i), file = \"" + outputFile + "_Cluster85.txt\", append=T)\n";
		script += "    write(clusters85$clusters[[i]], file = \"" + outputFile + "_Cluster85.txt\", append=T)\n";
		script += "}\n";
		
		script += "for (i in 1:length(clusters90$clusters)) {\n";
		script += "    write(paste(\"Cluster\",i), file = \"" + outputFile + "_Cluster90.txt\", append=T)\n";
		script += "    write(clusters90$clusters[[i]], file = \"" + outputFile + "_Cluster90.txt\", append=T)\n";
		script += "}\n";
		
		script += "for (i in 1:length(clusters95$clusters)) {\n";
		script += "    write(paste(\"Cluster\",i), file = \"" + outputFile + "_Cluster95.txt\", append=T)\n";
		script += "    write(clusters95$clusters[[i]], file = \"" + outputFile + "_Cluster95.txt\", append=T)\n";
		script += "}\n";
		
		script += "";
		return script;
	}
}
