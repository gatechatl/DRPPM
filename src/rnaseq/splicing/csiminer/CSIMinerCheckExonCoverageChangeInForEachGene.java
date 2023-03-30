package rnaseq.splicing.csiminer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Checking coverage bias
 * @author 4472414
 *
 */
public class CSIMinerCheckExonCoverageChangeInForEachGene {

	
	public static void main(String[] args) {
		
		try {

			String outputFile = "/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/csiminer_heatmap/ExonCoverageSummary.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			File folder_with_exon = new File("/Users/4472414/Projects/CSIMiner/Current_Manuscript/NatureCommunication_Draft/CompleteAnnotationPipeline/pipeline_input_files/csiminer_heatmap/gene_exonexp");
			for (File f: folder_with_exon.listFiles()) {				
				String inputFile = f.getPath();
				
				//if (inputFile.contains("ADAM9")) {
					double exon_num = 0.0;
					FileInputStream fstream = new FileInputStream(inputFile);
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					String header = in.readLine();
					String[] split_header = header.split("\t");
					LinkedList[] tissue = new LinkedList[split_header.length];
					double[] min = new double[split_header.length];
					double[] max = new double[split_header.length];
					for (int i = 0; i < split_header.length; i++) {
						tissue[i] = new LinkedList();
						min[i] = Double.MAX_VALUE;
						max[i] = Double.MIN_VALUE;
					}
					while (in.ready()) {
						
						String str = in.readLine();
						String[] split = str.split("\t");
						if (!split[0].contains("_ECM")) {
							exon_num++;
							tissue[0].add(exon_num);
							for (int i = 1; i < split.length; i++) {
								tissue[i].add(new Double(split[i]));
								if (min[i] > new Double(split[i])) {
									min[i] = new Double(split[i]);
								}
								if (max[i] < new Double(split[i])) {
									max[i] = new Double(split[i]);
								}
							}								
						}
					}
					in.close();
					int num_of_exons = tissue[0].size();
					System.out.println(f.getName());
					double[] exon_numbers = MathTools.convertListDouble2Double(tissue[0]);
					for (int i = 1; i < 32; i++) {						
						double[] sample_values = MathTools.convertListDouble2Double(tissue[i]);
						
						if (sample_values.length > 1) {
							
							try {
								double r = MathTools.PearsonCorrel(exon_numbers, sample_values);
								double pval = MathTools.PearsonCorrelPvalue(exon_numbers, sample_values);
								double q1 = MathTools.quartile(sample_values, 25);
								double q3 = MathTools.quartile(sample_values, 75);
								if (pval < 0.05) {
									String line = f.getName().replaceAll(".txt", "") + "\t" + split_header[i] + "\t" + r + "\t" + pval + "\t" + q1 + "\t" + q3 + "\t" + num_of_exons;
									System.out.println(line);
									out.write(line + "\n");
								}
							} catch (Exception ex) {
								
							}
						}
					}
				//}
				
				
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
