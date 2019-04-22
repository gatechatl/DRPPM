package rnaseq.tools.EXONJUNCTION;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * For count based method, this will normalize the result
 * @author tshaw
 *
 */
public class NormalizeJunctionCount {

	public static String parameter_info() {
		return "[inputFile] [baselineNumeric] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			double baseline = new Double(args[1]);
			String outputFile = args[2];
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] samples = header.split("\t");
			double[] totalcount = new double[samples.length - 1];
			for (int i = 0; i < totalcount.length; i++) {
				totalcount[i] = 0;
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 0; i < split.length - 1; i++) {
					totalcount[i] += new Double(split[i + 1]);
				}								
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine());
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 0; i < split.length; i++) {
					if (i > 0) {
						double newval = new Double(split[i]) * baseline / totalcount[i - 1];
						out.write("\t" + newval);
					} else {
						out.write(split[i]);
					}
				}								
				out.write("\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
