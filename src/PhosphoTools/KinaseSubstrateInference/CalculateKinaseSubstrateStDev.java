package PhosphoTools.KinaseSubstrateInference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

import statistics.general.MathTools;

/**
 * After IKAP, performs post processing to determine the standard deviation. Based on our prior experience, a cutoff of 0.2 should be applied.
 * @author tshaw
 *
 */
public class CalculateKinaseSubstrateStDev {
	public static String description() {
		return "After IKAP, performs post processing to determine the standard deviation. Based on our prior experience, a cutoff of 0.2 should be applied";
	}
	public static String type() {
		return "KinaseSubstrate";
	}
	public static String parameter_info() {
		return "[inputFile] [numberOfSamples] [numberOfIterations] [sd_filter: recommend 0.2] [outputFile] [unfileredOutputFile]";
	}
	
	public static void execute(String[] args) {
		
		
		try {
			String inputFile = args[0];
			int numberOfSamples = new Integer(args[1]);
			int numberOfIterations = new Integer(args[2]);
			double sd_filter = new Double(args[3]);
			String outputFile = args[4];
			String unfileredOutputFile = args[5];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_unfiltered = new FileWriter(unfileredOutputFile);
			BufferedWriter out_unfiltered = new BufferedWriter(fwriter_unfiltered);
			out.write("Kinase");
			out_unfiltered.write("Kinase");
			//for (int j = 0; j < numberOfIterations; j++) {
			for (int i = 0; i < numberOfSamples; i++) {
				out.write("\t" + "AvgValueSample" + (i + 1));
				out_unfiltered.write("\t" + "AvgValueSample" + (i + 1)); 
			}
			for (int i = 0; i < numberOfSamples; i++) {				
				out_unfiltered.write("\t" + "StDevSample" + (i + 1)); 
			}
			out.write("\tAvgStDev");
			out_unfiltered.write("\tAvgStDev");
			//}
			out.write("\n");
			out_unfiltered.write("\n");
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length != (numberOfSamples * numberOfIterations + 1)) {
					System.out.println("The number of samples or iteration doesn't match the number of column in the input file");
					System.exit(0);
				}
				int index = 0;
				HashMap sampleVariation = new HashMap();
				
				for (int j = 0; j < numberOfIterations; j++) {
					for (int i = 0; i < numberOfSamples; i++) {
						index++;
						double value = new Double(split[index]); // value of that cell
						if (sampleVariation.containsKey(i)) {
							double[] values = (double[])sampleVariation.get(i);
							values[j] = value;
							sampleVariation.put(i, values);
						} else {
							double[] values = new double[numberOfIterations];
							values[j] = value;
							sampleVariation.put(i, values);
						}
					}
				}
				
				out_unfiltered.write(split[0]);
				String line = "";
				String stdev_line = "";
				double[] standardDevs = new double[numberOfSamples];
				for (int i = 0; i < numberOfSamples; i++) {
					double[] values = (double[])sampleVariation.get(i);
					double average = MathTools.mean(values);
					double standardDev = MathTools.standardDeviation(values);
					standardDevs[i] = standardDev;
					line += "\t" + average;
					stdev_line += "\t" + standardDev;
				}
				double avg_standardDev = MathTools.mean(standardDevs);
				if (avg_standardDev <= sd_filter) {
					out.write(split[0]);
					out.write(line);
					out.write("\t" + avg_standardDev + "\n");
				} 
				out_unfiltered.write(line + stdev_line + "\t" + avg_standardDev + "\n");
				
			}
			in.close();									
			out.close();
			out_unfiltered.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
