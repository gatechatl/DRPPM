package rnaseq.mapping.tools.star;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Input of ensembl id based exon raw count matrix and use total protein gene count for normalization. Calculate the CPM for the exon.
 * @author tshaw
 *
 */
public class RawExonCount2CPMProteinFeatures {


	public static String description() {
		return "Input of ensembl id based raw count matrix. Calculate the RPM for targetseparately.";
	}
	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String parameter_info() {
		return "[gtfFile] [inputExonFile] [inputGeneFile] [outputFile] [outputTotalFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String gtfFile = args[0];
			String inputExonFile = args[1];
			String InputGeneFile = args[2];
			String outputFile = args[3];
			String outputTotalFile = args[4];

			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			

			FileWriter fwriter_total = new FileWriter(outputTotalFile);
			BufferedWriter out_total = new BufferedWriter(fwriter_total);			

			FileInputStream fstream = new FileInputStream(InputGeneFile);
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
				if (gtf.geneid2biotype.containsKey(split[0])) {
					String type = (String)gtf.geneid2biotype.get(split[0]);
					if (type.equals("protein_coding")) {		
						for (int i = 1; i < human_total.length; i++) {
							double read = new Double(split[i]);
							human_total[i] += read;
			
						}
					}
				}
				
			}
			in.close();			
			
			for (int i = 1; i < human_total.length; i++) {
				out_total.write(split_header[i] + "\t" + human_total[i] + "\n");
			}
			fstream = new FileInputStream(inputExonFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String species = "NA";
								
				out.write(split[0]);
				for (int i = 1; i < human_total.length; i++) {
					double read = new Double(split[i]);
					double human_rpm = read / human_total[i] * 1000000;
					out.write("\t" + human_rpm);							
				}
				out.write("\n");
			}
			in.close();
			out.close();
			out_total.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
