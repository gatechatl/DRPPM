package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Find a specific gene from the exon matrix to extract.
 * @author gatechatl
 *
 */
public class JuncSalvagerExtractGeneMatrix {


	public static String description() {
		return "Extract the gene from the exon matrix.";
	}
	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [inputGeneName] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String inputMatrixFile = args[0];
			String inputGeneName = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneSymbol = split[0].split("_")[0];
				if (geneSymbol.equals(inputGeneName)) {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
			
			
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
}
