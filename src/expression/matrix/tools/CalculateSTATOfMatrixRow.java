package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import statistics.general.MathTools;

public class CalculateSTATOfMatrixRow {

	public static String description() {
		return "Calculate basic statistics of the rows of the matrix";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [outputFile]";
	}
	public static void execute(String[] args) {
		try {
			
			String matrixFile = args[0];
			String outputFile = args[1];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Gene\tMin\tMax\tAverage\tGeometricMean\tMedian\tMAD\tVariance\tPopVariance\tStDev\n");
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double[] values = new double[split.length - 1];
				for (int i = 1; i < split.length; i++) {
					values[i - 1] = new Double(split[i]);
				}
				double median = MathTools.median(values);
				double min = MathTools.min(values);
				double max = MathTools.max(values);
				double avg = MathTools.mean(values);
				double geom_mean = MathTools.getGeometricMean(values);
				double mad = MathTools.mad(values);
				double variance = MathTools.SampleVariance(values);
				double pop_variance = MathTools.PopulationVariance(values);
				double standard_dev = MathTools.standardDeviation(values);
				out.write(split[0] + "\t" + min + "\t" + max + "\t" + avg + "\t" + geom_mean + "\t" + median + "\t" + mad + "\t" + variance + "\t" + pop_variance + "\t" + standard_dev + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	public static double median(double[] m) {
		double[] temp = m;
		Arrays.sort(temp);
	    int middle = temp.length/2;
	    if (temp.length%2 == 1) {
	        return temp[middle];
	    } else {
	        return (temp[middle-1] + temp[middle]) / 2.0;
	    }
	}
	public static double mean(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	public static double average(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	public static double sum(double[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum;
	}
	public static double max(double[] m) {
	    double max = m[0];
	    for (int i = 0; i < m.length; i++) {
	        if (m[i] > max) {
	        	max = m[i];
	        }
	    }
	    return max;
	}
	public static double PopulationVariance(double[] set) {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (double val: set) {
			stats.addValue(val);
		}
		return stats.getPopulationVariance();
	}
	 */
}
