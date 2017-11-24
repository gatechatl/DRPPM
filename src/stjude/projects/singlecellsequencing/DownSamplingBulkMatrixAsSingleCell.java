package stjude.projects.singlecellsequencing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeMap;

import specialized.algorithm.SpecializedAlgorithms;
import statistics.general.MathTools;


/**
 * Based on the single cell data's rnaseq 0 values, calculate the probability for 0 to occur at each ranking. Based on that probability then generate bootstrap of samples that follow the same distribution.
 * 
 * @author tshaw
 *
 */
public class DownSamplingBulkMatrixAsSingleCell {


	public static String type() {
		return "SINGLE CELL";
	}
	public static String description() {
		return "Based on the single cell data's rnaseq 0 values, calculate the probability for 0 to occur at each ranking. Based on that probability then generate bootstrap of samples that follow the same distribution.";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [scRNAseqMatrixFile] [replicates] [outputFile]";
	}

	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String scRNAseqMatrixFile = args[1];
			int replicates = new Integer(args[2]);
			String outputFile = args[3];			
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			HashMap bulk_expr_full = new HashMap();
			HashMap bulk_expr_rank = new HashMap();
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				for (int j = 0; j < replicates; j++) {
					out.write("\t" + split_header[i] + "_" + (j + 1));
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double[] values = new double[split.length - 1];
				int count_zeroes = 0;
				for (int i = 1; i < split.length; i++) {
					values[i - 1] = new Double(split[i]);
					
				}
				double mean = MathTools.mean(values);
				bulk_expr_full.put(split[0], str);
				bulk_expr_rank.put(split[0], mean);
			}
			in.close();
									
			HashMap prob_zeroes = new HashMap();
			HashMap expression = new HashMap();
			
			fstream = new FileInputStream(scRNAseqMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double[] values = new double[split.length - 1];
				int count_zeroes = 0;
				for (int i = 1; i < split.length; i++) {
					values[i - 1] = new Double(split[i]);
					if (new Double(split[i]) == 0) {
						count_zeroes++;
					}
				}
				double prob = new Double(count_zeroes) / (split.length - 1);
				double mean = MathTools.mean(values);
				expression.put(split[0], mean);
				prob_zeroes.put(split[0], prob);
			}
			in.close();
			LinkedList prob = new LinkedList();
			
			TreeMap treeMap = SpecializedAlgorithms.sortMapByValue(expression);
			Iterator itr3 = treeMap.keySet().iterator();
			while (itr3.hasNext()) {
				String key = (String)itr3.next();		
				//double expr = (Double)expression.get(key);
				//String line = (String)lines_map.get(geneName);							
				prob.add(prob_zeroes.get(key));
				
			}
			
			Iterator itr4 = prob.iterator();			
			Random rand = new Random();			
			treeMap = SpecializedAlgorithms.sortMapByValue(bulk_expr_rank);
			itr3 = treeMap.keySet().iterator();
			while (itr3.hasNext()) {
				String key = (String)itr3.next();
				double probabilty_zero = (Double)itr4.next();
				//double expr = (Double)expression.get(key);
				//String line = (String)lines_map.get(geneName);							
				String str = (String)bulk_expr_full.get(key);
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					for (int j = 0; j < replicates; j++) {
						double random = rand.nextDouble();
						if (random > probabilty_zero) {
							out.write("\t" + split[i]);
						} else {
							out.write("\t0.0");
						}
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
