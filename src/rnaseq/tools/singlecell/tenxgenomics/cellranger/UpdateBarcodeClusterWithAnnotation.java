package rnaseq.tools.singlecell.tenxgenomics.cellranger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Annotate the barcode depending on whether it has evidence of expression for the plasmid
 * @author tshaw
 *
 */
public class UpdateBarcodeClusterWithAnnotation {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "nnotate the barcode depending on whether it has evidence of expression for the plasmid";
	}
	public static String parameter_info() {
		return "[cell_identity_file] [marker_hit_file] [tag: YFP] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap barcode2cluster = new HashMap();
			HashMap hit = new HashMap();
			String cell_identity_file = args[0];
			String marker_hit_file = args[1];
			String tag = args[2];
			String outputFile = args[3];
			FileInputStream fstream = new FileInputStream(cell_identity_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String barcode = split[0].replaceAll("\"", "");
				String cluster = split[1].replaceAll("\"", "");
				barcode2cluster.put(barcode, cluster);
			}
			in.close();
			
			fstream = new FileInputStream(marker_hit_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (barcode2cluster.containsKey(split[0])) {
					if (split[1].equals(tag)) {
						hit.put(split[0], split[0]);
					}
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Barcode\tCluster\t" + tag + "\n");
			Iterator itr = barcode2cluster.keySet().iterator();
			while (itr.hasNext()) {
				String barcode = (String)itr.next();
				String cluster = (String)barcode2cluster.get(barcode);
				boolean found = false;
				if (hit.containsKey(barcode)) {
					found = true;
				}
				out.write(barcode + "\t" + cluster + "\t" + found + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
