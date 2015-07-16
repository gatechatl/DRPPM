package RNAseqTools.IntronRetention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import MISC.FileTools;

public class IntronRetentionHistogramData {

	public static void execute(String[] args) {
		
		try {
			
			int count = 130;
			
			double total = 0.3;
			
			
			String listSDFiles = args[0];
			String groupingFile = args[1];
			String summary_outputFile = args[2];
			String summary2_outputFile = args[3];
			
			if (args.length > 4) {
				count = new Integer(args[4]);
				total = new Double(args[5]);
			}
			double[] bins = new double[count];
			
			// This file contains an excel plotable format
			FileWriter fwriter_summary = new FileWriter(summary_outputFile);
			BufferedWriter out_summary = new BufferedWriter(fwriter_summary);
			
			// This file is in a different format
			FileWriter fwriter_summary2 = new FileWriter(summary2_outputFile);
			BufferedWriter out_summary2 = new BufferedWriter(fwriter_summary2);
			out_summary2.write("Group\tSampleName\tSpliceDef\tGeneNum\n");
			
			for (int i = 0; i < count; i = i + 1) {
				bins[i] = (total / count * i) * (new Double(i) / count);
			}
			
			HashMap final_result = new HashMap();
			LinkedList listFile = FileTools.readFileList(listSDFiles);
			String script = "";
			Iterator itr = listFile.iterator();		
			out_summary.write("Bins");
			String header = "Bins";
			while (itr.hasNext()) {
				String fileName = (String)itr.next();
				out_summary.write("\t" + fileName.split("_")[0]);
				header += "\t" + fileName.split("_")[0];
				HashMap map = new HashMap();
				FileInputStream fstream = new FileInputStream(fileName);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					if (!split[1].equals("NaN") && !split[1].equals("Infinity")) {
						
						double sd = new Double(split[1]);
						if (sd > 0) {
							for (int i = 0; i < bins.length - 1; i++) {
								if (bins[i] <= sd && sd <= bins[i + 1]) {
									if (map.containsKey(i)) {
										int num = (Integer)map.get(i);
										num++;
										map.put(i, num);
									} else {
										map.put(i, 1);
									}
								}
							}
						}
					}
				}
				map.put(0, 0);
				in.close();
				
				
				String outputFile = fileName + ".output";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				for (int i = 0; i < bins.length - 1; i++) {
					if (map.containsKey(i)) {
						out.write(bins[i] + "\t" + map.get(i) + "\n");
						if (final_result.containsKey(bins[i])) {
							String result = (String)final_result.get(bins[i]);
							final_result.put(bins[i], result + "\t" + map.get(i));
						} else {
							final_result.put(bins[i], "" + map.get(i));
						}
					} else {
						out.write(bins[i] + "\t0\n");
						if (final_result.containsKey(bins[i])) {
							String result = (String)final_result.get(bins[i]);
							final_result.put(bins[i], result + "\t0");
						} else {
							final_result.put(bins[i], "0");
						}
					}
				}
				out.close();
			}
			HashMap groupInfo = getGroupInfo(groupingFile);			
			HashMap sampleIndex = getSampleIndex(header);
			HashMap groupInfoIndex = convertGroupSample2Index(groupInfo, sampleIndex);
			HashMap index2Group = convertIndex2Group(groupInfo, sampleIndex);
			
			out_summary.write("\n");
			LinkedList list = new LinkedList();
			Iterator itr2 = final_result.keySet().iterator();
			while (itr2.hasNext()) {
				double key = (Double)itr2.next();
				list.add(key);
			}
			Collections.sort(list);
			itr2 = list.iterator();
			while (itr2.hasNext()) {
				double key = (Double)itr2.next();
				
				out_summary.write(key + "\t" + final_result.get(key) + "\n");
				
				// go through each group and calculate the mean and standard deviation
				
				String valuestr = (String)final_result.get(key);
				String[] values = valuestr.split("\t");
				for (int i = 0; i < values.length; i++) {
					if (index2Group.containsKey(i + 1)) {
						String group = (String)index2Group.get((i + 1));
						if (!group.equals("SKIP")) {
							String sampleName = header.split("\t")[i + 1];
							out_summary2.write(group + "\t" + sampleName + "\t" + (key + total / count) + "\t" + values[i] + "\n");
						}
					}
				}
			}
			out_summary.close();
			out_summary2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap convertGroupSample2Index(HashMap groupInfo, HashMap sampleIndex) {
		HashMap newGroupInfo = new HashMap();
		Iterator itr = groupInfo.keySet().iterator();
		while (itr.hasNext()) {
			String group = (String)itr.next();
			String stuff = (String)groupInfo.get(group);
			String[] split = stuff.split("\t");
			String newStuff = "";
			for (String sample: split) {
				//System.out.println(sample);
				sample = sample.split("_")[0];
				int index = (Integer)sampleIndex.get(sample);
				if (newStuff.equals("")) {
					newStuff = index + "";
				} else {
					newStuff += "\t" + index;
				}
			}
			newGroupInfo.put(group, newStuff);
		}
		return newGroupInfo;
	}
	public static HashMap convertIndex2Group(HashMap groupInfo, HashMap sampleIndex) {
		HashMap newGroupInfo = new HashMap();
		Iterator itr = groupInfo.keySet().iterator();
		while (itr.hasNext()) {
			String group = (String)itr.next();
			String stuff = (String)groupInfo.get(group);
			String[] split = stuff.split("\t");
			String newStuff = "";
			for (String sample: split) {
				sample = sample.split("_")[0];
				int index = (Integer)sampleIndex.get(sample);
				newGroupInfo.put(index, group);
			}
			//newGroupInfo.put(group, newStuff);
		}
		return newGroupInfo;
	}
	public static HashMap getSampleIndex(String header) {
		HashMap map = new HashMap();
		String[] split = header.split("\t");
		for (int i = 1; i < split.length; i++) {
			map.put(split[i].split("_")[0], i);
		}
		return map;
	}
	public static HashMap getGroupInfo(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[1])) {
					String other = (String)map.get(split[1]);
					other += "\t" + split[0];
					map.put(split[1], other);
				} else {
					map.put(split[1], split[0]);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
