package rnaseq.mapping.tools.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Input of ensembl id based raw count matrix. Calculate the RPM for human and mouse separately.
 * @author tshaw
 *
 */
public class HumanMouseXenograftRawCount2RPM {


	public static String description() {
		return "Input of ensembl id based raw count matrix. Calculate the RPM for human and mouse separately.";
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
			
			HashMap transcripts = new HashMap();
			HashMap transcriptID2geneID = new HashMap();
			HashMap geneID2transcriptID = new HashMap();

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			double[] mouse_total = new double[split_header.length];
			double[] human_total = new double[split_header.length];
			for (int i = 0; i < mouse_total.length; i++) {
				mouse_total[i] = 0;
				human_total[i] = 0;
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String species = "NA";				
				if (split[0].contains("ENSG")) {
					species = "HUMAN";
				} else if (split[0].contains("ENSMUSG")) {
					species = "MOUSE";
				}
				for (int i = 1; i < mouse_total.length; i++) {
					double read = new Double(split[i]);
					if (species.equals("HUMAN")) {
						human_total[i] += read;
					} else if (species.equals("MOUSE")) {
						mouse_total[i] += read;
					}					
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
				
				if (split[0].contains("ENSG")) {
					species = "HUMAN";
				} else if (split[0].contains("ENSMUSG")) {
					species = "MOUSE";
				}
				out.write(split[0]);
				for (int i = 1; i < mouse_total.length; i++) {
					double read = new Double(split[i]);
					if (species.equals("HUMAN")) {
						double human_rpm = read / human_total[i] * 1000000;
						out.write("\t" + human_rpm);
					} else if (species.equals("MOUSE")) {
						double mouse_rpm = read / mouse_total[i] * 1000000;
						out.write("\t" + mouse_rpm);
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
