package proteomics.phospho.kinaseactivity.pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import statistics.general.MathTools;


/**
 * Take the activity measure and generate result
 * @author tshaw
 *
 */
public class SummarizeIKAPMatrix {

	public static String description() {
		return "Summarize the IKAP result";
	}
	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String parameter_info() {
		return "[IKAPResultFile] [origInputFile] [numIter] [numSamples] [stdev_cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String IKAPFile = args[0];
			String origInputFile = args[1];
			int numIter = new Integer(args[2]);
			int numSamples = new Integer(args[3]);
			double stdev_cutoff = new Double(args[4]);
			String outputFile = args[5];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap kinase_count = new HashMap();
			FileInputStream fstream = new FileInputStream(origInputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] kinases = split[1].split(",");
				for (String kinase: kinases) {
					if (kinase_count.containsKey(kinase)) {
						int count = (Integer)kinase_count.get(kinase);
						count = count + 1;
						kinase_count.put(kinase, count);
					} else {
						kinase_count.put(kinase, 1);
					}
				}
			}
			in.close();
			System.out.println(kinase_count.size());
			
			out.write("Kinase");
			for (int i = 1; i < numSamples + 1; i++) {
				out.write("\tData" + i);
			}
			out.write("\tAvgStdev\tNumSubstrate\n");
			
			fstream = new FileInputStream(IKAPFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			//out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double[] stdevs = new double[numSamples];
								
				for (int i = 1; i < numSamples + 1; i++) {
					double val = 0;					
					int n = 0;
					double[] values = new double[numIter];
					for (int k = 0; k < numIter; k++) {
						values[k] = new Double(split[i + (k * numSamples)]);
						n++;
					}										
					double stdev = MathTools.standardDeviation(values); 
					double avg = MathTools.mean(values);
					stdevs[i - 1] = stdev;
					//out.write("\t" + avg);
				}
				double avg_stdev = MathTools.mean(stdevs);
				
				if (avg_stdev < stdev_cutoff) {
					out.write(split[0]);
					for (int i = 1; i < numSamples + 1; i++) {
						double val = 0;					
						int n = 0;
						double[] values = new double[numIter];
						for (int k = 0; k < numIter; k++) {						
							values[k] = new Double(split[i + (k * numSamples)]);							
						}										 
						double avg = MathTools.mean(values);
						double stdev = MathTools.standardDeviation(values);
						out.write("\t" + avg);
					}
					out.write("\t" + avg_stdev + "\t" + kinase_count.get(split[0]));
					out.write("\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
