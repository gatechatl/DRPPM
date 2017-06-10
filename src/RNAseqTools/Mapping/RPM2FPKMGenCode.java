package RNAseqTools.Mapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Using the gencode file to normalize the gene length for RPKM
 * @author tshaw
 *
 */
public class RPM2FPKMGenCode {

	
	public static String type() {
		return "RNASEQ";
	}
	public static String description() {
		return "RPM conversion to RPKM";
	}
	public static String parameter_info() {
		return "[inputYiping's GenCode File] [matrixFile] [outputFile] [geneNameIndex] [lengthIndex]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputGenCodeFile = args[0];
			String matrixFile = args[1];
			String outputFile = args[2];
			int geneNameIndex = new Integer(args[3]);
			int lengthIndex = new Integer(args[4]);
			File f = new File(outputFile);
			if (f.exists()) {
				System.out.println(outputFile + " exists already. Please remove the file before running.");
				System.exit(0);
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			int maxIndex = 0;
			if (maxIndex < geneNameIndex)
				maxIndex = geneNameIndex;
			
			if (maxIndex < lengthIndex)
				maxIndex = lengthIndex;
			
			
			HashMap geneName2length = new HashMap();
			int total = 0;
			FileInputStream fstream = new FileInputStream(inputGenCodeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				total++;
				String[] split = str.split("\t");
				//if (split.length > 8) {
					//String type = split[3];
				if (split.length > maxIndex) {
					String geneName = split[geneNameIndex];
					//String ensembl_gene_id = split[2];
					//String direction = split[8];
					int length = new Integer(split[lengthIndex]);
					geneName2length.put(geneName, length);
				}
				//}
			}
			in.close();
					
			fstream = new FileInputStream(matrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneID = split[0];
				double length = 0;
				
				
				if (geneName2length.containsKey(geneID)) {
					out.write(geneID);
					length = (Integer)geneName2length.get(geneID);
				
					for (int i = 1; i < split.length; i++) {
						double value = new Double(split[i]) * 1000 / length;
						out.write("\t" + value);
					}
					out.write("\n");
				} else {
					System.out.println("Missing: " + geneID);
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
