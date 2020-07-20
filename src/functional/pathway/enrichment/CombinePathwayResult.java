package functional.pathway.enrichment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

public class CombinePathwayResult {

	
	public static String dependencies() {
		return "Combine the pathway result into a single heatmap matrix.";
	}
	public static String type() {
		return "ENRICHMENT";
	}
	public static String description() {
		return "Combine the pathway result into a single heatmap matrix.";
	}
	public static String parameter_info() {
		return "[inputFileWithPathwayNames] [pathwayResultFiles] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputPathwayNames = args[0];
			String pathwayResultFiles = args[1];
			String outputFile = args[2];
			String outputPathway = args[3];

			FileWriter fwriter_pathway = new FileWriter(outputPathway);
			BufferedWriter out_pathway = new BufferedWriter(fwriter_pathway);
			
			HashMap pathways = new HashMap();
			FileInputStream fstream = new FileInputStream(inputPathwayNames);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				pathways.put(str.toUpperCase(), str.toUpperCase());
			}
			in.close();
			
			HashMap comparisons = new HashMap();
			HashMap pathway_pval_up = new HashMap();
			HashMap pathway_pval_dn = new HashMap();
			
			fstream = new FileInputStream(pathwayResultFiles);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String comparison_name = split[0];
				comparisons.put(comparison_name, comparison_name);
				String pathway_file = split[1];
				FileInputStream fstream2 = new FileInputStream(pathway_file);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				String header = in2.readLine();
				out_pathway.write("ComparisonName\t" + header + "\n");
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					if (split2[0].equals("UpRegList")) {
						if (pathways.containsKey(split2[1].toUpperCase())) {
							pathway_pval_up.put(comparison_name + "\t" + split2[1].toUpperCase(), new Double(split2[2]));
							out_pathway.write(comparison_name + "\t" + str2 + "\n");
							//pathway_fdr.put(comparison_name + "\t" + split2[1].toUpperCase(), new Double(split2[9]));
						}
					}
					if (split2[0].equals("DnRegList")) {
						if (pathways.containsKey(split2[1].toUpperCase())) {
							pathway_pval_dn.put(comparison_name + "\t" + split2[1].toUpperCase(), new Double(split2[2]));
							out_pathway.write(comparison_name + "\t" + str2 + "\n");
							//pathway_fdr.put(comparison_name + "\t" + split2[1].toUpperCase(), new Double(split2[9]));
						}
					}
				}
				in2.close();
			}
			in.close();
			out_pathway.close();
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Pathways");
			Iterator itr = comparisons.keySet().iterator();
			while (itr.hasNext()) {
				String comparison_name = (String)itr.next();
				out.write("\t" + comparison_name);
			}
			out.write("\n");
			
			itr = pathways.keySet().iterator();
			while (itr.hasNext()) {
				String pathway = (String)itr.next();
				out.write(pathway);
				Iterator itr2 = comparisons.keySet().iterator();
				while (itr2.hasNext()) {
					String comparison_name = (String)itr2.next();
					double pval = 1.0;
					boolean up_reg = true;
					if (pathway_pval_up.containsKey(comparison_name + "\t" + pathway)) {
						pval = (Double)pathway_pval_up.get(comparison_name + "\t" + pathway);
						up_reg = true;
					}
					if (pathway_pval_dn.containsKey(comparison_name + "\t" + pathway)) {					
						double new_pval = (Double)pathway_pval_dn.get(comparison_name + "\t" + pathway);
						if (new_pval < pval) {
							pval = new_pval;
							up_reg = false;
						}
					}
					
					double score = MathTools.log2(pval) / MathTools.log2(20);
					if (up_reg) {
						score = score * -1;
					}
					out.write("\t" + score);					
				}
				out.write("\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
