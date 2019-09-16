package rnaseq.tools.pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * If the geneName contains ";" then we will split them into separate geneName and duplicate the rows
 * @author tshaw
 *
 */
public class ExpandGeneListAfterLIMMA {

	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "If the geneName contains \";\" then we will split them into separate geneName and duplicate the rows";
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
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].contains(";")) {
					String[] geneNames = split[0].split(";");
					for (String geneName: geneNames) {
						out.write(geneName);
						for (int i = 1; i < split.length; i++) {
							out.write("\t" + split[i]);
						}
						out.write("\n");
					}
				} else {
					out.write(str + "\n");
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
