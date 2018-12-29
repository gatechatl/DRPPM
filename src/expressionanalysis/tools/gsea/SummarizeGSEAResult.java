package expressionanalysis.tools.gsea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Read through the GSEA result table and generate a matrix
 * @author tshaw
 *
 */
public class SummarizeGSEAResult {

	public static String description() {
		return "Summarize the GSEA result";
	}
	public static String type() {
		return "GSEA";
	}
	public static String parameter_info() {
		return "[tabFile: pathwayName\tpath2excel] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
        	FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
            HashMap sample_pathway = new HashMap();
            // Compile a list of xls gsea result files
            LinkedList sampleName_list = new LinkedList();
            LinkedList pathwayName_list = new LinkedList();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				if (!sampleName_list.contains(split[0])) {
					sampleName_list.add(split[0]);
				}

				FileInputStream fstream2 = new FileInputStream(split[1]);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				in2.readLine();
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					String pathwayName = split2[0];
					//System.out.println(split[1] + "\t" + pathwayName);
					if (!split2[6].equals("")) {
						double pval = new Double(split2[6]);
						double fdr = new Double(split2[7]);
						
						String type = "0.0";
						if (pval < 0.2) {
							//type = "leaning";
							type = "0.2";
						}
						if (pval < 0.05) {
							//type = "weak_yes";
							type = "0.4";
						}
						if (pval < 0.05 && fdr < 0.2) {
							//type = "weak_yes";
							type = "0.6";
						}
						if (pval < 0.05 && fdr < 0.05) {
							//type = "yes";
							type = "0.8";
						}
						if (pval < 0.01 && fdr < 0.05) {
							//type = "yes";
							type = "1.0"; 
						}
						System.out.println(sampleName + "\t" + pathwayName + "\t" + pval + "\t" + fdr + "\t" + type);
						if (!type.equals("0.0")) {
							if (!pathwayName_list.contains(pathwayName)) {
								pathwayName_list.add(pathwayName);
							}
							sample_pathway.put(sampleName + "\t" + pathwayName, type);
						}
					}
				}
				in2.close();								
			}
			in.close();
			out.write("Pathway");
			Iterator itr2 = sampleName_list.iterator();
			while (itr2.hasNext()) {
				String sampleName = (String)itr2.next();
				out.write("\t" + sampleName);
			}
			out.write("\n");
			Iterator itr = pathwayName_list.iterator();
			while (itr.hasNext()) {
				String pathway = (String)itr.next();
				out.write(pathway);
				itr2 = sampleName_list.iterator();
				while (itr2.hasNext()) {
					String sampleName = (String)itr2.next();
					String type = "0.0";
					if (sample_pathway.containsKey(sampleName + "\t" + pathway)) {
						type = (String)sample_pathway.get(sampleName + "\t" + pathway);
						out.write("\t" + type);
					} else {
						out.write("\t0.0");
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
