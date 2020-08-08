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

public class SpearmanRankCorrelation {

	public static String type() {
		return "SSRNASEQ";
	}
	public static String description() {
		return "Calculate Spearman Rank Correlation between single sample matrix and bulk reference";
	}
	public static String parameter_info() {
		return "[inputFile] [referenceFile] [referenceSampleName] [compare2referenceFile] [correlationMatrix] [cutoff]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String referenceFile = args[1];
			String referenceSampleName = args[2];
			String compare2referenceFile = args[3];
			String correlationMatrix = args[4];
			double cutoff = new Double(args[5]);
			
			HashMap map = new HashMap();
			HashMap geneNameMap = new HashMap();
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
					if (map.containsKey(sampleName)) {
						HashMap values = (HashMap)map.get(sampleName);
						values.put(geneName, new Double(split[i]));
						map.put(sampleName, values);
					} else {
						HashMap values = new HashMap();
						values.put(geneName, new Double(split[i]));
						map.put(sampleName, values);
					}
				}			
			}
			in.close();

			HashMap reference = new HashMap();
			fstream = new FileInputStream(referenceFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				for (int i = 1; i < split.length; i++) {
					if (referenceSampleName.equals(split_header[i])) {
						reference.put(split[0], new Double(split[i]));
						if (new Double(split[i]) >= cutoff) {
							geneNameMap.put(geneName, geneName);
						}
					}
				}
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(compare2referenceFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(correlationMatrix);
			BufferedWriter out2 = new BufferedWriter(fwriter2);			
			
			int n = 0;
			double[] ref = new double[geneNameMap.size()];
			Iterator itr = geneNameMap.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				double value = (Double)reference.get(geneName);
				
				ref[n] = value;
				n++;
			}
			out2.write("SampleName");
			Iterator itr2 = map.keySet().iterator();
			while (itr2.hasNext()) {
				String sampleName = (String)itr2.next();
				out2.write("\t" + sampleName);
				//out.write(sampleName);
				double[] sample = new double[geneNameMap.size()];
				n = 0;
				HashMap values = (HashMap)map.get(sampleName);
				Iterator itr3 = geneNameMap.keySet().iterator();
				while (itr3.hasNext()) {
					String geneName = (String)itr3.next();
					double value = (Double)values.get(geneName);
					sample[n] = value;
					n++;
					//System.out.println(sampleName + "\t" + MathTools.SpearmanRank(ref, sample));
					
				}
				out.write(sampleName + "\t" + MathTools.SpearmanRank(ref, sample) + "\n");
			}
			out.close();
			out2.write("\n");
			
			itr2 = map.keySet().iterator();
			while (itr2.hasNext()) {
				String sampleName = (String)itr2.next();
				
				double[] sample1 = new double[geneNameMap.size()];
				n = 0;
				HashMap values = (HashMap)map.get(sampleName);
				Iterator itr3 = geneNameMap.keySet().iterator();
				while (itr3.hasNext()) {
					String geneName = (String)itr3.next();
					double value = (Double)values.get(geneName);
					sample1[n] = value;
					n++;					
					//System.out.println(sampleName + "\t" + MathTools.SpearmanRank(ref, sample));					
				}
				out2.write(sampleName);
				Iterator itr4 = map.keySet().iterator();
				while (itr4.hasNext()) {
					String sampleName2 = (String)itr4.next();
					double[] sample2 = new double[geneNameMap.size()];
					n = 0;
					HashMap values2 = (HashMap)map.get(sampleName2);
					Iterator itr5 = geneNameMap.keySet().iterator();
					while (itr5.hasNext()) {
						String geneName = (String)itr5.next();
						double value = (Double)values2.get(geneName);
						sample2[n] = value;
						n++;
						//System.out.println(sampleName + "\t" + MathTools.SpearmanRank(ref, sample));
						
					}
					out2.write("\t" + MathTools.SpearmanRank(sample1, sample2));
				}
				out2.write("\n");
				//out.write(sampleName + "\t" + MathTools.SpearmanRank(ref, sample) + "\n");
			}
			
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
