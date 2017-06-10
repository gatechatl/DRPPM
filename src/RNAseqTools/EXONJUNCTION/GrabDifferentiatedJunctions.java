package RNAseqTools.EXONJUNCTION;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * After processing grab junction that is different
 * @author tshaw
 *
 */
public class GrabDifferentiatedJunctions {
	public static String description() {
		return "CalculateDifferentiatedJunctions";
	}
	public static String type() {
		return "EXONJUNCTION";
	}
	public static String parameter_info() {
		return "[exonJunctionFile] [sampleList1] [sampleList2] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			double cutoff = new Double(args[1]);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double geneJC = new Double(split[2]);
				double junction = new Double(split[3]);
				if (geneJC - junction >= cutoff) {
					System.out.println("GeneMedianJC_Greater\t" + str);
				} else if (junction - geneJC >= cutoff) {
					System.out.println("Junction_Greater\t" + str);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
