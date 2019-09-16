package rnaseq.tools.singlecell.correlation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

public class SpearmanRankCorrelationMatrixForTwo {

	public static String type() {
		return "SSRNASEQ";
	}
	public static String description() {
		return "Calculate Spearman Rank Correlation between two sample matrix";
	}
	public static String parameter_info() {
		return "[matrixFile1] [matrixFile1] [correlationMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String matrixFile1 = args[0];
			String matrixFile2 = args[1];		
			String correlationMatrix = args[2];
			
			FileWriter fwriter = new FileWriter(correlationMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap geneNameMap = new HashMap();
			HashMap file1_genes = new HashMap();
			HashMap overlapping_genes = new HashMap();
			FileInputStream fstream = new FileInputStream(matrixFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				file1_genes.put(geneName, geneName);
			}
			in.close();
			
			fstream = new FileInputStream(matrixFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				if (file1_genes.containsKey(geneName)) {
					overlapping_genes.put(geneName, geneName);	
				}				
			}
			in.close();
			
			HashMap file1_map = generate_expression_map(matrixFile1);
			HashMap file2_map = generate_expression_map(matrixFile2);
			
			out.write("SampleName");
			Iterator itr2 = file1_map.keySet().iterator();
			while (itr2.hasNext()) {
				String sampleName = (String)itr2.next();
				out.write("\t" + sampleName);
			}
			out.write("\n");
			itr2 = file2_map.keySet().iterator();
			while (itr2.hasNext()) {
				String sampleName2 = (String)itr2.next();
							
				double[] sample2 = new double[overlapping_genes.size()];
				int n = 0;
				HashMap values2 = (HashMap)file2_map.get(sampleName2);
				Iterator itr3 = overlapping_genes.keySet().iterator();
				while (itr3.hasNext()) {
					String geneName = (String)itr3.next();
					double value = (Double)values2.get(geneName);
					sample2[n] = value;
					n++;					
				}
				out.write(sampleName2);
				Iterator itr4 = file1_map.keySet().iterator();
				while (itr4.hasNext()) {
					String sampleName1 = (String)itr4.next();
					
					double[] sample1 = new double[overlapping_genes.size()];
					n = 0;
					HashMap values1 = (HashMap)file1_map.get(sampleName1);
					itr3 = overlapping_genes.keySet().iterator();
					while (itr3.hasNext()) {
						String geneName = (String)itr3.next();
						double value1 = (Double)values1.get(geneName);
						sample1[n] = value1;
						n++;					
					}
					out.write("\t" + MathTools.SpearmanRank(sample1, sample2));
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap generate_expression_map(String inputFile) {
		HashMap file1_map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				
				for (int i = 1; i < split.length; i++) {
					String sampleName = split_header[i];
					if (file1_map.containsKey(sampleName)) {
						HashMap values = (HashMap)file1_map.get(sampleName);
						values.put(geneName, new Double(split[i]));
						file1_map.put(sampleName, values);
					} else {
						HashMap values = new HashMap();
						values.put(geneName, new Double(split[i]));
						file1_map.put(sampleName, values);
					}
				}			
			}
			in.close();
			return file1_map;
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file1_map;
	}
}	
