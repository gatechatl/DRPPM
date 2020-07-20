package stjude.projects.jinghuizhang.hla.trust4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class JinghuiZhangHLAJiccardDistanceMatrix {

	public static String description() {
		return "Quantify the HLA matrix.";
	}
	public static String type() {
		return "HLA";
	}
	public static String parameter_info() {
		return "[inputBamFilelst] [outputFeatureMatrix] [outputJaccardMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputBamFilelst = args[0];	
			String outputFeatureMatrix = args[1];
			String outputJaccardMatrix = args[2];
			
			FileWriter fwriter = new FileWriter(outputFeatureMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_jaccard = new FileWriter(outputJaccardMatrix);
			BufferedWriter out_jaccard = new BufferedWriter(fwriter_jaccard);
			
			//out.write("SampleName\tClonotypeName\tMutRate\tNumMismatch\tSeqLength\n");
			HashMap count = new HashMap();
			HashMap features = new HashMap();
			HashMap sampleNames = new HashMap();
			
			HashMap sample_clones = new HashMap();
			FileInputStream fstream = new FileInputStream(inputBamFilelst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String report_tsv = split[1];				
				//sampleNames.put(sampleName, sampleName);
				FileInputStream fstream2 = new FileInputStream(split[1]);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				in2.readLine();
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					String feature = split2[4] + "_" + split2[5] + "_" + split2[6] + "_" + split2[7];
					features.put(feature, feature);			
					count.put(sampleName + "\t" + feature, new Double(split2[0]));
					if (sampleNames.containsKey(sampleName)) {
						double count2 = (Double)sampleNames.get(sampleName);
						count2 += new Double(split2[0]);
						sampleNames.put(sampleName, count2);
					} else {
						sampleNames.put(sampleName, new Double(split2[0]));
					}
					
					if (sample_clones.containsKey(sampleName)) {
						LinkedList list = (LinkedList)sample_clones.get(sampleName);
						list.add(feature);
						sample_clones.put(sampleName, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(feature);
						sample_clones.put(sampleName, list);
					}
				}
				in2.close();				
			}
			in.close();
			
			out.write("Feature");
			out_jaccard.write("SampleName");
			Iterator itr = sampleNames.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				out.write("\t" + sampleName);
				out_jaccard.write("\t" + sampleName);
			}
			out.write("\n");
			Iterator itr2 = features.keySet().iterator();
			while (itr2.hasNext()) {
				String feature = (String)itr2.next();
				out.write(feature);
				itr = sampleNames.keySet().iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					double value = 0.0;
					if (count.containsKey(sampleName + "\t" + feature)) {
						value = (Double)count.get(sampleName + "\t" + feature);
						
						value = value / (Double)sampleNames.get(sampleName) * 1000;
					}
					
					out.write("\t" + value);
				}
				out.write("\n");
			}
			out.close();
			
			itr = sampleNames.keySet().iterator();
			while (itr.hasNext()) {
				String sample1 = (String)itr.next();
				out_jaccard.write(sample1);
				
				LinkedList list1 = new LinkedList();
				if (sample_clones.containsKey(sample1)) {
					list1 = (LinkedList)sample_clones.get(sample1);
				}
				itr2 = sampleNames.keySet().iterator();
				while (itr2.hasNext()) {
					String sample2 = (String)itr2.next();
					
					LinkedList list2 = new LinkedList();
					if (sample_clones.containsKey(sample2)) {
						list2 = (LinkedList)sample_clones.get(sample2);
					}
					HashMap total_list = new HashMap();
					HashMap overlap_list = new HashMap();
					Iterator itr3 = list1.iterator();
					while (itr3.hasNext()) {
						String feature = (String)itr3.next();
						total_list.put(feature, feature);
					}
					itr3 = list2.iterator();
					while (itr3.hasNext()) {
						String feature = (String)itr3.next();
						total_list.put(feature, feature);
						if (list1.contains(feature)) {
							overlap_list.put(feature, feature);
						}
					}
					double jaccard = new Double(overlap_list.size()) / new Double(total_list.size());
					out_jaccard.write("\t" + jaccard);
				}
				out_jaccard.write("\n");
			}
			out_jaccard.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
