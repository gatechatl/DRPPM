package proteomics.phospho.kinaseactivity.pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Clean up the whole proteome id_all_prot_quan.txt
 * @author tshaw
 *
 */
public class JUMPqWhlProteome2Matrix {

	public static String type() {
		return "KINASEACTIVITY";
	}
	public static String description() {
		return "Clean up the whole proteome id_all_prot_quan.txt";
	}
	public static String parameter_info() {
		return "[inputFile] [numberOfSamples] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			int numSamples = new Integer(args[1]);
			String outputFile = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			//out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String geneName = split[3];
				String line = geneName + "\t" + accession;
				for (int i = split.length - numSamples; i < split.length; i++) {
					line += "\t" + split[i];
				}
				out.write(line + "\n");
			}
			in.close();
			out.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
