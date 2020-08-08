package expression.matrix.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Takes the output from AppendMADValue and extract the Top most variable rows.
 * Example: /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/TCGA_Reference/RPKM/gene/download
 * @author tshaw
 *
 */
public class FilterTopMADScores {

	public static String type() {
		return "EXPRESSION";
	}
	public static String description() {
		return "Takes the output from AppendMADValue and extract the Top most variable rows.";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [Top N ranked based on MAD] [outputMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			LinkedList mad_score_list = new LinkedList();
			String matrixFile = args[0];
			int top_n = new Integer(args[1]);
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(matrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			int mad_index = -1;
			for (int i = 0; i < split_header.length; i++) {
				if (split_header[i].equals("MAD")) {
					mad_index = i;
				}
			}
			if (mad_index == -1) {
				System.out.println("Program couldn't find the column with the calculated MAD score");			
				System.exit(0);
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				mad_score_list.add(new Double(split[mad_index]));
			}
			in.close();
			Object[] mad_score_array = mad_score_list.toArray();
			Arrays.sort(mad_score_array);
			double topMAD = (Double)mad_score_array[mad_score_array.length - top_n];
			
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			out.write(split_header[0]);
			for (int i = 1; i < split_header.length; i++) {
				if (!split_header[i].equals("MAD")) {
					out.write("\t" + split_header[i]);
				}
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (new Double(split[mad_index]) >= topMAD) {
					out.write(split[0]);
					for (int i = 1; i < split_header.length; i++) {
						if (i != mad_index) {
							out.write("\t" + split[i]);
						}
					}		
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
