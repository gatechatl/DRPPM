package RNAseqTools.SingleCell.CellRanger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SeuratCalculateClusterDistribution {

	public static String type() {
		return "SINGLECELL";
	}
	public static String description() {
		return "Examine the cluster distribution";
	}
	public static String parameter_info() {
		return "[inputFolderMeta] [final_cluster_file] [outputDistributionFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileMeta = args[0];
			String final_cluster = args[1];
			String outputDistribution = args[2];
			
			HashMap barcode2experiment = new HashMap();
			HashMap experiment_count = new HashMap();
			HashMap experiment_count_with_annotation = new HashMap();
			HashMap barcode2cluster = new HashMap();
			HashMap barcode2annotation = new HashMap();
			HashMap uniq_cluster_map = new HashMap();
			FileInputStream fstream = new FileInputStream(final_cluster);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String barcode = split[0].replaceAll("\"",  "");
				String cluster = split[1].replaceAll("\"",  "");
				String annotation = split[2].replaceAll("\"",  "");
				barcode2cluster.put(barcode, cluster);
				barcode2annotation.put(barcode, annotation);
			}
			in.close();
			
			fstream = new FileInputStream(inputFileMeta);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String folderPath = split[0];
				String prefix = split[1];
				String barcodes = folderPath;
				FileInputStream fstream2 = new FileInputStream(barcodes);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String barcode = str2.split("-")[0];
					if (!barcode2experiment.containsKey(barcode)) {
						barcode2experiment.put(barcode, prefix);
					} else {
						System.out.println("Duplicate: " + barcode + "\t" + prefix);
						barcode2experiment.put(barcode, prefix);
					}
					if (barcode2cluster.containsKey(barcode)) {
						String cluster = (String)barcode2cluster.get(barcode); 
						String annotation = (String)barcode2annotation.get(barcode);
						uniq_cluster_map.put(cluster, cluster);
						if (experiment_count.containsKey(prefix)) {
							HashMap cluster_map = (HashMap)experiment_count.get(prefix);
							if (cluster_map.containsKey(cluster)) {
								int count = (Integer)cluster_map.get(cluster);
								count++;
								cluster_map.put(cluster, count);
								experiment_count.put(prefix, cluster_map);
							} else {
								cluster_map.put(cluster, 1);
								experiment_count.put(prefix, cluster_map);
							}
						} else {
							HashMap cluster_map = new HashMap();							
							cluster_map.put(cluster, 1);
							experiment_count.put(prefix, cluster_map);
						}
						
						if (annotation.equals("true")) {
							if (experiment_count_with_annotation.containsKey(prefix)) {
								HashMap cluster_map = (HashMap)experiment_count_with_annotation.get(prefix);
								if (cluster_map.containsKey(cluster)) {
									int count = (Integer)cluster_map.get(cluster);
									count++;
									cluster_map.put(cluster, count);
									experiment_count_with_annotation.put(prefix, cluster_map);
								} else {
									cluster_map.put(cluster, 1);
									experiment_count_with_annotation.put(prefix, cluster_map);
								}
							} else {
								HashMap cluster_map = new HashMap();							
								cluster_map.put(cluster, 1);
								experiment_count_with_annotation.put(prefix, cluster_map);
							}
						}
					}
				}
			}
			
			FileWriter fwriter = new FileWriter(outputDistribution);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Cluster");
			Iterator itr = experiment_count.keySet().iterator();
			while (itr.hasNext()) {
				String experiment_name = (String)itr.next();
				out.write("\t" + experiment_name);
			}
			
			itr = experiment_count.keySet().iterator();
			while (itr.hasNext()) {
				String experiment_name = (String)itr.next();
				out.write("\t" + experiment_name + "_annotation");
			}
			
			out.write("\n");
			
			
			itr = uniq_cluster_map.keySet().iterator();
			while (itr.hasNext()) {
				String cluster = (String)itr.next();
				out.write(cluster);
				Iterator itr2 = experiment_count.keySet().iterator();
				while (itr2.hasNext()) {
					String experiment_name = (String)itr2.next();
					HashMap cluster_count = (HashMap)experiment_count.get(experiment_name);
					int count = 0;
					if (cluster_count.containsKey(cluster)) {
						count = (Integer)cluster_count.get(cluster);
					}
					out.write("\t" + count);
				}
				itr2 = experiment_count_with_annotation.keySet().iterator();
				while (itr2.hasNext()) {
					String experiment_name = (String)itr2.next();
					HashMap cluster_count = (HashMap)experiment_count_with_annotation.get(experiment_name);
					int count = 0;
					if (cluster_count.containsKey(cluster)) {
						count = (Integer)cluster_count.get(cluster);
					}
					out.write("\t" + count);
				}
				
				out.write("\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
