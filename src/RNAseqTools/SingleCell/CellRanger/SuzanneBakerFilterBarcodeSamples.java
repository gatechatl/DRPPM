package RNAseqTools.SingleCell.CellRanger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Filter barcode Samples
 * @author tshaw
 *
 */
public class SuzanneBakerFilterBarcodeSamples {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Filter the matrix for bad barcodes.";
	}
	public static String parameter_info() {
		return "[inputClusterFile] [bad_clusters] [inputMatrix] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap good_samples = new HashMap();
			String inputClusterFile = args[0];
			String bad_clusters = args[1];
			String[] bad_cluster = bad_clusters.split(",");
			String inputMatrix = args[2];
			String outputFile = args[3];
			FileInputStream fstream = new FileInputStream(inputClusterFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = split[0].replaceAll("\"", "");
				String cluster = split[1].replaceAll("\"", "");
				boolean skip = false;
				for (String bad: bad_cluster) {
					if (cluster.equals(bad)) {
						skip = true;
					}
				}
				if (!skip) {
					good_samples.put(sample, cluster);
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream(inputMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0].replaceAll(".1", "");
				String line = split[0];
				for (int i = 1; i < split.length; i++) {
					line += "\t" + split[i];
				}
				if (good_samples.containsKey(split[0])) {
					out.write(line + "\n");					
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
