package rnaseq.pcpa;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Iterator;

public class GeneratePolyAHistogramOutput {

	public static void execute(String[] args) {
		try {
			String fileName = args[0];
			String outputFile = args[1];
			
			int total = 220;
			int bin = 10;
			//double[] binning = new double[total/bin + 1];
			LinkedList[] binning = new LinkedList[total/bin + 1];
			for (int i = 0; i < binning.length; i++) {
				binning[i] = new LinkedList();
			}
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");	
				int val = new Double(split[1]).intValue();
				double t = new Double(split[3]);
				int i = val / bin;
				if (t > 0) {
					binning[i].add(new Double(1) / t) ;
				}
				//System.out.println(i + "\t" + new Double(1) / t * 1e6);
				
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			for (int i = 0; i < binning.length; i++) {
				out.write((i * bin) + "\t" + sum(binning[i]) + "\t" + sum(binning[i]) + "\t" + binning[i].size() + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double sum(LinkedList list) {
		double sum = 0;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			double val = (Double)itr.next();
			sum += val;
		}
		return sum;
	}
	public static double median(LinkedList list) {
		Object[] array = list.toArray();
		double[] values = new double[array.length];
		if (values.length == 0) {
			return 0;
		}
		for (int i = 0; i < array.length; i++) {
			values[i] = (Double)array[i];
		}
		Arrays.sort(values);
		double median;
		int middle = ((values.length) / 2);
		if(values.length % 2 == 0){
			double medianA = values[middle];
			double medianB = values[middle-1];
			median = (medianA + medianB) / 2;
		} else{
			median = values[middle];
		}
		return median;
	}
}

