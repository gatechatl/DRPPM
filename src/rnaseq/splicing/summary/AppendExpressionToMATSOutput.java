package rnaseq.splicing.summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class AppendExpressionToMATSOutput {

	public static String type() {
		return "SPLICING";
	}
	public static String description() { 
		return "Append the gene expression information to MATS output.";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [mats_file] [index] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputMatrixFile = args[0];
			String mats_file = args[1];
			int index = new Integer(args[2]);
			String outputFile = args[3];
			HashMap expr_map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header_split = in.readLine().split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				double average = 0;
				for (int i = 1; i < split.length; i++) {
					average += new Double(split[i]);
				}
				average = average / (header_split.length - 1);
				expr_map.put(geneName, average);
			}
			in.close();
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(mats_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header_split = in.readLine().split("\t");
			out.write(header_split[0]);
			for (int i = 1; i < index + 1; i++) {
				out.write("\t" + header_split[i]);
			}
			out.write("\tGene_Expr");
			for (int i = index + 1; i < header_split.length; i++) {
				out.write("\t" + header_split[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[index];
				out.write(split[0]);
				for (int i = 1; i < index + 1; i++) {
					out.write("\t" + split[i]);
				}
				out.write("\t" + expr_map.get(geneName));
				for (int i = index + 1; i < split.length; i++) {
					out.write("\t" + split[i]);
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
