package PhosphoTools.Network;

/**
 * Generate Subnetwork
 * @author tshaw
 *
 */
public class SubNetworkBioNet {

	public static String parameter_info() {
		return "[refNetworkFile] [pvalFile] [fdr cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String refFile = args[0];
			String pvalFile = args[1];
			double fdr = new Double(args[2]);
			String outputFile = args[3];
			System.out.println(generate_script(refFile, pvalFile, outputFile, fdr));
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generate_script(String refFile, String pvalFile, String outputFile, double fdr) {
		String script = "";
		script += "library(BioNet);\n";
		script += "graph = loadNetwork.sif(\"" + refFile + "\", format=\"graphNEL\", directed=TRUE)\n";

		script += "pval = read.table(\"" + pvalFile + "\", header = TRUE);\n";
		script += "fb = fitBumModel(pval, plot = FALSE);\n";
		script += "nodes = names(pval)\n";
		script += "subnet = subNetwork(nodes, graph);\n";
		script += "subnet <- rmSelfLoops(subnet)\n";
		script += "scores <- scoreNodes(subnet, fb, fdr = " + fdr + ")\n";
		script += "scores = unlist(scores)\n";
		script += "module <- runFastHeinz(subnet, scores)\n";
		script += "writeHeinzEdges(network = module, file = \"" + outputFile + "\", use.score = scores)\n";
		return script;
	}
}
