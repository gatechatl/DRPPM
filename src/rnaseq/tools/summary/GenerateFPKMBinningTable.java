package rnaseq.tools.summary;

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

public class GenerateFPKMBinningTable {

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "Generate a binning table from FPKM matrix table.";
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
			out.write("bin");
			LinkedList sampleList = new LinkedList();
			
			HashMap id_bin = new HashMap();
			HashMap map = new HashMap();
			int max = 0;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] headers = header.split("\t");
			for (int l = 1; l < headers.length; l++) {
				sampleList.add(headers[l]);
				out.write("\t" + headers[l]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				String geneName = split[0];
				
				
				for (int i = 1; i < headers.length; i++) {
					double value = new Double(split[i]) + 0.00001;
					double log2value = MathTools.log2(value);
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
					
					
				}
			}
			in.close();
			
			for (int i = 0; i <= max; i++) {
				//out.write(i + "");
				double lower_bin = (Double)id_bin.get(i);
				double upper_bin = lower_bin + bin_size;
				out.write(lower_bin + ":" + upper_bin + "");
				Iterator itr = sampleList.iterator();
				while (itr.hasNext()) {
					String sampleName = (String)itr.next();
					HashMap bin = (HashMap)map.get(sampleName);
					if (bin.containsKey(i)) {
						int count = (Integer)bin.get(i);
						out.write("\t" + count);
					} else {
						out.write("\t" + 0);
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
