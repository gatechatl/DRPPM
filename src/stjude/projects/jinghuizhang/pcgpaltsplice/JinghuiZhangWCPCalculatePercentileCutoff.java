package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Calculate the percentiles for each sample. These will be used to gauge the cutoffs for low medium high. Min is > 0
 * @author tshaw
 *
 */
public class JinghuiZhangWCPCalculatePercentileCutoff {

	public static String description() {
		return "Generate percentile table for expression matrix";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[input matrix file] [outputPercentileFile] [outputTrueFalse]";
	}
	public static void execute(String[] args) {
		
		try {
						
			String inputFile = args[0];
			String outputFile = args[1];
			String outputCutoffMatrix = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write("SampleName");
			for (int i = 0; i < 100; i++) {
				out.write("\t" + i);
			}
			out.write("\n");
			
			FileWriter fwriter_cutoff = new FileWriter(outputCutoffMatrix);
			BufferedWriter out_cutoff = new BufferedWriter(fwriter_cutoff);	
			

			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			LinkedList[] list = new LinkedList[split_header.length - 1];
			for (int i = 0; i < list.length; i++) {				
				list[i] = new LinkedList();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					if (new Double(split[i]) > 0.0) {
						list[i - 1].add(new Double(split[i]));
					}
				}
			}
			in.close();

			double[] first_quantile = new double[split_header.length - 1];
			double[] median = new double[split_header.length - 1];
			double[] third_quantile = new double[split_header.length - 1];
			for (int i = 1; i < split_header.length ; i++) {
				out.write(split_header[i]);
				double[] value_list = MathTools.convertListDouble2Double(list[i - 1]);
				Arrays.sort(value_list);
				for (int j = 0; j < 100; j++) {
					int total_size = value_list.length;
					int index = new Double(new Double(total_size) * j / 100).intValue();
					if (index > value_list.length) {
						index = value_list.length - 1;
					}
					out.write("\t" + value_list[index]);
					if (j == 25) {
						first_quantile[i - 1] = value_list[index];
					}
					if (j == 50) {
						median[i - 1] = value_list[index];
					}
					if (j == 75) {
						third_quantile[i - 1] = value_list[index];
					}
				}
				out.write("\n");
			}
			out.close();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out_cutoff.write("SampleName");
			split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				out_cutoff.write("\t" + split_header[i]);
			}
			out_cutoff.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out_cutoff.write(split[0]);
				for (int i = 1; i < split_header.length ; i++) {
					double val = new Double(split[i]);
					if (val < first_quantile[i - 1]) {
						out_cutoff.write("\t0");
					} else if (val >= first_quantile[i - 1] && val < median[i - 1]) {
						out_cutoff.write("\t1");
					} else if (val >= median[i - 1] && val < third_quantile[i - 1]) {
						out_cutoff.write("\t2");
					} else if (val >= third_quantile[i - 1]) {
						out_cutoff.write("\t3");
					} else {
						out_cutoff.write("\tNA");
					}
				}
				out_cutoff.write("\n");
			}
			in.close();
			out_cutoff.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

