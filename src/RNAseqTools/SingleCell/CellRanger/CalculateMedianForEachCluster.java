package RNAseqTools.SingleCell.CellRanger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

/**
 * Based on the cluster, generate a collapsed matrix of median expression for each cluster
 * Do not show anything if cluster is absent from sample.
 * @author tshaw
 *
 */
public class CalculateMedianForEachCluster {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Based on the cluster, generate a collapsed matrix of median expression for each cluster";
	}
	public static String parameter_info() {
		return "[inputBarcodeClusterFile] [blackListFile] [inputMatrixFile] [prefix] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBarcodeClusterFile = args[0];
			String blackListFile = args[1];
			String inputMatrixFile = args[2];
			String prefix = args[3];
			String outputFile = args[4];
			
			HashMap black_list_map = new HashMap();
			HashMap cluster2barcode = new HashMap();
			HashMap barcode2annotation = new HashMap();
			FileInputStream fstream = new FileInputStream(blackListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String black_list_barcode = split[1].replaceAll("Duplicate: ", "");
				black_list_map.put(black_list_barcode, black_list_barcode);
			}
			in.close();
			
			fstream = new FileInputStream(inputBarcodeClusterFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String barcode = split[0];
				String cluster = split[1];
				String annotation = split[2]; //true false
				if (!black_list_map.containsKey(barcode)) {
					if (cluster2barcode.containsKey(cluster)) {
						LinkedList list = (LinkedList)cluster2barcode.get(cluster);
						list.add(barcode);
						cluster2barcode.put(cluster, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(barcode);
						cluster2barcode.put(cluster, list);
					}
					barcode2annotation.put(barcode, annotation);					
				}
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneName");
			Iterator itr = cluster2barcode.keySet().iterator();
			while (itr.hasNext()) {
				String cluster = (String)itr.next();
				out.write("\t" + prefix + "_" + cluster);
			}
			out.write("\n");
			
			HashMap barcode2index = new HashMap();
			fstream = new FileInputStream(inputMatrixFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			for (int i = 1; i < split_header.length; i++) {
				barcode2index.put(split_header[i].split("-")[0], i);
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0]);
				itr = cluster2barcode.keySet().iterator();
				while (itr.hasNext()) {
					String cluster = (String)itr.next();
					LinkedList values_list = new LinkedList();
					LinkedList list = (LinkedList)cluster2barcode.get(cluster);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						String barcode = (String)itr2.next();
						if (barcode2index.containsKey(barcode)) {
							int index = (Integer)barcode2index.get(barcode);
							values_list.add(new Double(split[index]));
						}
					}
					double[] values = MathTools.convertListDouble2Double(values_list);
					double median = 0.0;
					if (values.length > 0) {						
						median = MathTools.median(values);
					}
					out.write("\t" + median);
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
