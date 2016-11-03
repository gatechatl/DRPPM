package RNAseqTools.SingleCell.RibosomeDepletion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Combine single cell sample into a combined sample
 * @author tshaw
 *
 */
public class CombineSingleCellSampleIntoOne {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Combine single cell sample into a combined sample";
	}
	public static String parameter_info() {
		return "[inputFile] [countOutputFile] [rpmOutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String countOutputFile = args[1];
			String rpmOutputFile = args[2];
			FileWriter fwriter = new FileWriter(countOutputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			out.write("gene\t" + inputFile + "\n");
			
			FileWriter fwriter2 = new FileWriter(rpmOutputFile);
			BufferedWriter out2 = new BufferedWriter(fwriter2);	
			out2.write("gene\t" + inputFile + "\n");
			
			int total_reads = 0;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				double total = 0;
				for (int i = 1; i < split.length; i++) {
					total += new Double(split[i]);
				}
				total_reads += total;
			}
			in.close();	
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				double total = 0;
				for (int i = 1; i < split.length; i++) {
					total += new Double(split[i]);
				}
				out.write(split[0] + "\t" + total + "\n");
				out2.write(split[0] + "\t" + new Double(total) / total_reads * 1000000 + "\n");
			}
			in.close();			
			out.close();
			out2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
