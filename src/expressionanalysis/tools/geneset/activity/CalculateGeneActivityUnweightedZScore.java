package expressionanalysis.tools.geneset.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Calculate an unweighted Z score activity
 * @author tshaw
 *
 */
public class CalculateGeneActivityUnweightedZScore {

	
	public static String description() {
		return "Calculate an unweighted Z score activity given a gmt files.";
	}
	public static String type() {
		return "ACTIVITY";
	}
	public static String parameter_info() {
		return "[matrix_table_file] [gmtFile] [min_gene_expr_cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		
		try {
						
			String matrix_table_file = args[0];
			String gmtFile = args[1];
			double min_gene_expr_cutoff = new Double(args[2]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			FileInputStream fstream = new FileInputStream(matrix_table_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");
			LinkedList[] sample_values = new LinkedList[split_header.length - 1];			
			for (int i = 1; i < split_header.length; i++) {
				sample_values[i - 1] = new LinkedList();
			}
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				for (int i = 1; i < split_header.length; i++) {
					if (new Double(split[i]) > min_gene_expr_cutoff) {
						sample_values[i - 1].add(MathTools.log2(new Double(split[i]) + 1.0));
					}
				}
			}
			in.close();

			double[] mean_values = new double[split_header.length - 1];
			double[] stdev_values = new double[split_header.length - 1];
			for (int i = 1; i < split_header.length; i++) {
				double[] values = MathTools.convertListDouble2Double(sample_values[i - 1]);
				double mean = MathTools.mean(values);
				double stdev = MathTools.standardDeviation(values);
				mean_values[i - 1] = mean;
				stdev_values[i - 1] = stdev;
			}
			
			fstream = new FileInputStream(gmtFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				HashMap geneset = new HashMap();
				int geneset_hit = 0;
				for (int i = 2; i < split.length; i++) {
					geneset.put(split[i].toUpperCase(), split[i].toUpperCase());
				}
				out.write(split[0]);
				
				FileInputStream fstream2 = new FileInputStream(matrix_table_file);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				header = in2.readLine();
				split_header = header.split("\t");
				double[] z_scores = new double[split_header.length - 1];			
				for (int i = 1; i < split_header.length; i++) {
					z_scores[i - 1] = 0.0;
				}
				while (in2.ready()) {
					String str2 = in2.readLine().trim();
					String[] split2 = str2.split("\t");
					if (geneset.containsKey(split2[0].toUpperCase())) {
						geneset_hit++;
						for (int i = 1; i < split2.length; i++) {
							double log2 = MathTools.log2(new Double(split2[i]) + 1.0);
							double z_score = (log2 - mean_values[i - 1]) / stdev_values[i - 1];
							z_scores[i - 1] += z_score;
						}
					}
				}
				in2.close();
				for (int i = 1; i < split_header.length; i++) {
					if (geneset_hit > 0) {
						out.write("\t" + z_scores[i - 1] / Math.sqrt(geneset_hit));
					} else {
						out.write("\t" + Double.NaN);
					}
				}
				out.write("\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
