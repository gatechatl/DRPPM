package RNAseqTools.SingleCell.Bootstrap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Generate a true false matrix from the variant matrix bootstrapping
 * @author tshaw
 *
 */
public class GenerateTrueFalseMatrix {
	public static String description() {
		return "Generate bootstrapped variant matrix";		
	}
	
	public static String type() {
		return "SingleCell";		
	}
	
	public static String parameter_info() {
		return "[inputVariantMatrix] [outputShuffledVariantMatrix]";		
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
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				for (int i = 1; i < split.length; i++) {
					double mutallele = new Integer(split[i].split("\\|")[0]);
					double allele = new Integer(split[i].split("\\|")[1]);
					double total = mutallele + allele;
					if (mutallele >= 3 && mutallele / total >= 0.1) {
						out.write("\t1");
					} else {
						out.write("\t0");
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
