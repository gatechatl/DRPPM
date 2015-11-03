package RNAseqTools.EXONJUNCTION;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Compare the LIMMA for exon junction vs gene expression
 * @author tshaw
 *
 */
public class CompareDifferentialAnalysis {

	public static String parameter_info() {
		return "[geneDiffFile] [exonDiffFile] [direction] [qvalue cutoff] [foldchange cutoff] [outputFileConflict]";
	}
	public static void execute(String[] args) {
		
		try {
						
			String geneDiffFile = args[0];
			String exonAllFile = args[1];
			String direction = args[2];
			double pvalue_cutoff = new Double(args[3]);
			double foldchange_cutoff = new Double(args[4]);
			String outputFileConflict = args[5];			
			HashMap gene_map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(geneDiffFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].replaceAll("\"", "");
				double pvalue = new Double(split[4]);
				double foldChange = new Double(split[1]);
				double expression = new Double(split[2]);
				if (direction.equals("+") && foldChange > 0) {				
					gene_map.put(geneName, split[1]);					
				} else if (direction.equals("-") && foldChange < 0) {
					gene_map.put(geneName, split[1]);
				} else if (direction.equals("?") && pvalue > 0.05) {
					gene_map.put(geneName, split[1]);
				}				
			}
			in.close();

			int num_sat_criteria = 0;
			System.out.println("Number of gene satisfy the criteria: " + gene_map.size());

			FileWriter fwriter = new FileWriter(outputFileConflict);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap pass_criteria_map = new HashMap();
			fstream = new FileInputStream(exonAllFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("JunctionName\t" + header + "\tGeneName\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double pvalue = new Double(split[4]);
				double foldChange = new Double(split[1]);
				String exonJuncName = split[0].replaceAll("\"", "");
				String geneName = exonJuncName.split("_")[exonJuncName.split("_").length - 1];
				if (pvalue < pvalue_cutoff && foldChange > foldchange_cutoff) {
					num_sat_criteria++;
				}
				if (gene_map.containsKey(geneName) && pvalue < pvalue_cutoff && foldChange > foldchange_cutoff) {
					out.write(str + "\t" + geneName + "\n");
				}
				
			}
			
			System.out.println("JunctionFile Number satisfy the qvalue and foldchange criteria: " + num_sat_criteria);
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
