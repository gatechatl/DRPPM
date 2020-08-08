package jump.pipeline.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Takes input from peptide and generate a matrix
 * Assumes that sample is a 10plex
 * @author tshaw
 *
 */
public class GenerateProteomeGeneMatrix {

	public static String type() {
		return "JUMP";
	}
	public static String description() {
		return "Takes input from peptide and generate a matrix\nAssumes that sample is a 10plex";
	}
	public static String parameter_info() {
		return "[id_uni_prot_quan.txt] [sampleNames sample1,sample2,...,sample10] [minPSM] [outputMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String sampleNames = args[1];
			String outputFile = args[2];
			
			
			if (sampleNames.split(",").length != 10) {
				System.out.println("sampleNames must contains 10 different tags");
				System.exit(0);
			}
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write("Gene");
			String[] names = sampleNames.split(",");
			for (int i = 0; i < names.length; i++) {
				out.write("\t" + names[i]);
			}
			out.write("\n");
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[3];
				if (!geneName.equals("NA")) {
					out.write(geneName);
					for (int i = split.length - 10; i < split.length; i++) {
						out.write("\t" + split[i]);
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
