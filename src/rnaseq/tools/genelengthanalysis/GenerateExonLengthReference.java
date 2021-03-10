package rnaseq.tools.genelengthanalysis;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Takes in liqing's gtf reference and generate an exon length reference. 
 * @author gatechatl
 *
 */
public class GenerateExonLengthReference {


	public static String type() {
		return "GTF";
	}
	public static String description() {
		return "Generate output with exon id and its length";
	}
	public static String parameter_info() {
		return "[inputExonGTFFile] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			
			String inputExonGTFfile = args[0];
			String outputLengthFile = args[1];

			FileWriter fwriter = new FileWriter(outputLengthFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write("ExonID\tLength\n");
			FileInputStream fstream = new FileInputStream(inputExonGTFfile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));					
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (split[2].equals("exon")) {
					int start = new Integer(split[3]);
					int end = new Integer(split[4]);
					int length = end - start + 1;					
					String gene_id = GTFFile.grabMeta(split[8], "gene_id");
					out.write(gene_id + "\t" + length + "\n");
				}
			}
			in.close();
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
