package graph.interactive.javascript.scatterplot;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import statistics.general.MathTools;

/**
 * Customize the color based on their expression value
 * See example at /rgs01/project_space/bakergrp/NTRK/common/10X_fastq_files/processed/10xSingleCell/10xSingleCell/Combined_NesCre_GfapCreER_Additional_Analysis/Default_Res0.6_Per30/CustomExpression
 * @author tshaw
 *
 */
public class AppendExpressionColorAsMetaData {

	public static String description() {
		return "Regenerate the html with a new coloring scheme";
	}
	public static String type() {
		return "JAVASCRIPT";
	}
	public static String parameter_info() {
		return "[matrixFile] [geneExpressionMatrix] [geneName] [normalization_factor 0 to 100; recommend 75 or 90] [takeLog boolean] [outputMatrixWithColor]";
	}
	public static void execute(String[] args) {
		
		try {
			String matrixFile = args[0];
			String geneExpressionMatrix = args[1];
			String geneName = args[2];
			double normalization_factor = new Double(args[3]); // from 1 to 100 previously I used 75
			boolean take_log = new Boolean(args[4]);
			String outputFile = args[5];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap sample2expr = new HashMap();
			HashMap sample2unnorm_expr = new HashMap();
			HashMap sample2color = new HashMap();
			HashMap sample2norm = new HashMap();
			
			LinkedList values_list = new LinkedList();
			double max = -9999999999.0;
			double min = 9999999999.0;
			double unnorm_max = 0.0;
			double unnorm_min = 0.0;
			
			FileInputStream fstream = new FileInputStream(geneExpressionMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals(geneName)) {
					for (int i = 1; i < split.length; i++) {
						if (unnorm_max < new Double(split[i])) {
							unnorm_max = new Double(split[i]);
						}
						if (unnorm_min > new Double(split[i])) {
							unnorm_min = new Double(split[i]);
						}
						
						sample2unnorm_expr.put(split_header[i], new Double(split[i]));
						
						if (take_log) {
							split[i] = MathTools.log2(new Double(split[i]) + 0.001) + "";
						}
						values_list.add(new Double(split[i]));
						if (max < new Double(split[i])) {
							max = new Double(split[i]);
						}
						if (min > new Double(split[i])) {
							min = new Double(split[i]);
						}
						
						sample2expr.put(split_header[i], new Double(split[i]));
					}
				}
			}
			in.close();
			
			double[] values = MathTools.convertListDouble2Double(values_list);
			Percentile percentile = new Percentile();			
			double upper_quartile = percentile.evaluate(values, normalization_factor);			
			System.out.println(upper_quartile);
			
			Iterator itr = sample2expr.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				double expr = (Double)sample2expr.get(sampleName);
				double norm_score = ((expr - min) / (upper_quartile - min)) * 0.9 + 0.1;
				if (norm_score > 1.0) {
					norm_score = 1.0;
				}
				if (norm_score < 0.1) {
					norm_score = 0.1;
				}
				sample2norm.put(sampleName, norm_score);
				
				if (min == max) {
					String hex = String.format("#%02x%02x%02x", 0, 0, 0);
					sample2color.put(sampleName, hex);
				} else {
					
					int val = 255 - new Double((expr - min) / (upper_quartile - min) * 255).intValue();
					if (val < 0) {
						val = 0;
					}
					if (val > 255) {
						val = 255;
					}
					String hex = String.format("#%02x%02x%02x", 255, val, val);
					sample2color.put(sampleName, hex);
				}
				/*double expr = (Double)sample2expr.get(sampleName); 
				if (min == max) {
					String hex = String.format("#%02x%02x%02x", 0, 0, 0);
					sample2color.put(sampleName, hex);
				} else {
					int val = 255 - new Double((expr - min) / (max - min) * 255).intValue();					
					String hex = String.format("#%02x%02x%02x", 255, val, val);
					sample2color.put(sampleName, hex);
				}*/
			}
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\t" + geneName + "_Opacity\t" + geneName + "_Expr\t" + geneName + "_Color" + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (sample2color.containsKey(split[0])) {
					double value = (Double)sample2unnorm_expr.get(split[0]);
					String hex = (String)sample2color.get(split[0]);
					double norm_score = (Double)sample2norm.get(split[0]);
					
					out.write(str + "\t" + norm_score + "\t" + value + "\t" + hex + "\n");
				} else {
					out.write(str + "\t0.1\tNA\twhite\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

