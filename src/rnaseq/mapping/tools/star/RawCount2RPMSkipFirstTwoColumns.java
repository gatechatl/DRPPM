package rnaseq.mapping.tools.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Input of raw count matrix. Calculate the RPM.
 * @author tshaw
 *
 */
public class RawCount2RPMSkipFirstTwoColumns {


	public static String description() {
		return "Input of ensembl id based raw count matrix. Calculate the RPM for targetseparately.";
	}
	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			double[] human_total = new double[split_header.length];
			for (int i = 0; i < human_total.length; i++) {
				human_total[i] = 0;
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String species = "NA";				
				
				for (int i = 1; i < human_total.length; i++) {
					double read = new Double(split[i]);
					human_total[i] += read;
	
				}
				
			}
			in.close();						
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String species = "NA";
								
				out.write(split[0] + "\t" + split[1]);
				for (int i = 2; i < human_total.length; i++) {
					double read = new Double(split[i]);
					double human_rpm = read / human_total[i] * 1000000;
					out.write("\t" + human_rpm);							
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
