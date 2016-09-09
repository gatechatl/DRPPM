package DifferentialExpression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Filter gene set based on differentially expressed genes
 * @author tshaw
 *
 */
public class DEGFilteredGeneSet {

	public static String type() {
		return "DEG";
	}
	public static String description() {
		return "Filter gene set based on differentially expressed genes";
	}
	public static String parameter_info() {
		return "[geneSetFile] [limmaFileList: file1,file2] [pvalueCutoff] [logFoldCutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String[] limmaFiles = args[1].split(",");
			double pval_cutoff = new Double(args[2]);
			double logFold_cutoff = new Double(args[3]);
			String outputFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap limma = new HashMap();
			int i = 0;
			for (String limmaFile: limmaFiles) {
				
				FileInputStream fstream = new FileInputStream(limmaFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String geneName = split[0].replaceAll("\"",  "");
					double logFC = new Double(split[1]);
					double pvalue = new Double(split[4]);
					if (pvalue <= pval_cutoff && Math.abs(logFC) >= logFold_cutoff) {
						limma.put(geneName, logFC + "\t" + pvalue);
					}
				}
				in.close();				
			}
			
			out.write(">Reduced: " + inputFile + "\n");
			//System.out.println("Reduced: " + inputFile);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String geneName = in.readLine();
				if (limma.containsKey(geneName)) {
					//System.out.println(geneName);
					out.write(geneName + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
