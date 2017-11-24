package RNAseqTools.SingleCell.ZeroAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class GenerateZeroAnalysisBinningTable {

	public static String type() {
		return "SCRNASEQ";
	}
	public static String description() {
		return "Generate a binning table for the Zero Count from matrix table.";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			double bin_size = new Double(args[2]);
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Log2FPKM_Binning\tNumberOfSamplesZeroCount\n");
			LinkedList sampleList = new LinkedList();
			
			HashMap id_bin = new HashMap();
			HashMap count_bin = new HashMap();
			HashMap map = new HashMap();
			HashMap count_zero_map = new HashMap();
			
			HashMap countFrequency = new HashMap();
			int max = 0;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] headers = header.split("\t");
			for (int l = 1; l < headers.length; l++) {
				sampleList.add(headers[l]);
				//out.write("\t" + headers[l]);
			}
			//out.write("\n");
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				String geneName = split[0];
				double avg_log2value = 0;
				int count_zero = 0;
				for (int i = 1; i < headers.length; i++) {
					double value = new Double(split[i]);
					if (value == 0) {
						count_zero++;
					}
					double log2value = MathTools.log2(value + 0.0001);
					avg_log2value += log2value / (headers.length - 1);
					String name = headers[i];
					if (map.containsKey(name)) {
						HashMap bin = (HashMap)map.get(name);
						int n = 0;
						if (log2value <= -10) {
							if (bin.containsKey(n)) {
								int count = (Integer)bin.get(n);
								count++;
								bin.put(n, count);
							} else {
								bin.put(n, 1);
							}
						}
						for (double j = -10; j < 10; j = j + bin_size) {			
							id_bin.put(n, j);
							if (log2value >= j && log2value <= (j + bin_size)) {
								if (bin.containsKey(n)) {
									int count = (Integer)bin.get(n);
									count++;
									bin.put(n, count);
								} else {
									bin.put(n, 1);
								}
							}
							n++;
						}
						if (log2value >= 10.0) {
							id_bin.put(n, 10.0);
							if (bin.containsKey(n)) {
								int count = (Integer)bin.get(n);
								count++;
								bin.put(n, count);
							} else {
								bin.put(n, 1);
							}
						}
						max = n;
						map.put(name, bin);
					} else {
						HashMap bin = new HashMap();						
						int n = 0;
						if (log2value <= -10) {
							bin.put(n, 1);
						}
						for (double j = -10; j < 10; j = j + bin_size) {								
							if (log2value >= j && log2value <= j + bin_size) {
								bin.put(n, 1);
							}
							n++;
						}
						if (log2value >= 10) {
							bin.put(n, 1);
						}
						max = n;
						map.put(name, bin);
					}
					
					
				} // for loop
				
				count_zero_map.put(geneName, count_zero);
				int bin_id = 0;
				int n = 0;
				if (avg_log2value <= -10) {
					bin_id = n;
				}
				for (double j = -10; j < 10; j = j + bin_size) {								
					if (avg_log2value >= j && avg_log2value <= j + bin_size) {
						bin_id = n;
					}
					n++;
				}
				if (avg_log2value >= 10) {
					bin_id = n;
				}
				String binID = bin_id + "";
				if (new Integer(bin_id).toString().length() == 1) {
					binID = "0" + bin_id;
				}
				if (new Integer(bin_id).toString().length() == 2) {
					binID = "" + bin_id;
				}
				
				
				
				out.write("Bin_" + binID + "_[" + (bin_id-10) + ":" + (bin_id - 10 + bin_size) + "]\t" + count_zero + "\n");
				if (countFrequency.containsKey("Bin_" + binID)) {
					int count = (Integer)(countFrequency.get("Bin_" + binID));
					count++;
					countFrequency.put("Bin_" + binID, count);					
				} else {					
					countFrequency.put("Bin_" + binID, 1);
				}
			}
			
			in.close();
			out.close();
			
			Iterator itr = countFrequency.keySet().iterator();
			while (itr.hasNext()) {
				String id = (String)itr.next();
				int count = (Integer)countFrequency.get(id);
				System.out.println(id + "\t" + count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

