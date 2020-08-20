package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Multiply a matrix with a particular factor with the option of taking its int value. 
 * Example: /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/BALL_Mullighan/tpm
 * @author tshaw
 *
 */
public class MultiplyMatrixValuesWithFactor {
	public static String description() {
		return "Multiply a matrix with a particular factor with the option of taking its int value.";
	}
	public static String type() {
		return "EXPRESSION";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [col_index_value_start] [factor] [toIntValue flag: true/false] [outputMatrixFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			int start_col_value = new Integer(args[1]);
			double factor = new Double(args[2]);
			boolean toIntValue = new Boolean(args[3]);
			String outputMatrixFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputMatrixFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				out.write(split[0]);
				for (int i = 1; i < start_col_value; i++) {
					out.write("\t" + split[i]);
				}
				for (int i = start_col_value; i < split.length; i++) {
					double value = new Double(split[i]);
					value = value * factor;
					if (toIntValue) {
						out.write("\t" + new Double(value).intValue());
					} else {
						out.write("\t" + value);
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
