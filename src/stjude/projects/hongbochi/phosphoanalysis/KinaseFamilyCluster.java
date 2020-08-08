package stjude.projects.hongbochi.phosphoanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Define the cluster for each kinase
 * @author tshaw
 *
 */
public class KinaseFamilyCluster {

	public static String description() {
		return "Define the cluster for each kinase";
	}
	public static String type() {
		return "HONGBO";
	}
	public static String parameter_info() {
		return "[inputFile] [kinaseFamilyFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String kinaseFamilyFile = args[1];
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap kinase_family_substrate = new HashMap();
			HashMap cluster_map = new HashMap();
			FileInputStream fstream = new FileInputStream(kinaseFamilyFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				cluster_map.put(split[0], new HashMap());
				for (int i = 2; i < split.length; i++) {
					if (kinase_family_substrate.containsKey(split[i])) {
						LinkedList list = (LinkedList)kinase_family_substrate.get(split[i]);
						list.add(split[0]);
						kinase_family_substrate.put(split[i], list);						
					} else {
						LinkedList list = new LinkedList();
						list.add(split[0]);
						kinase_family_substrate.put(split[i], list);
					}
				}
				
			}
			in.close();
			
			HashMap cluster_name = new HashMap();
			
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[3].toUpperCase();
				String accession = split[2].split("\\|")[1];
				String site = split[13];
				String tag = geneName + "_" + accession + "_" + site;
				String cluster = split[split.length - 1];
				cluster_name.put(cluster, cluster);
				if (kinase_family_substrate.containsKey(tag)) {
					LinkedList kinase_family = (LinkedList)kinase_family_substrate.get(tag);
					Iterator itr = kinase_family.iterator();
					while (itr.hasNext()) {
						String kinaseFamily = (String)itr.next();
						if (cluster_map.containsKey(kinaseFamily)) {
							HashMap map = (HashMap)cluster_map.get(kinaseFamily);
							if (map.containsKey(cluster)) {
								int count = (Integer)map.get(cluster);
								count++;
								map.put(cluster, count);
							} else {
								map.put(cluster, 1);
							}
						}
						
					}
					
				}				
			}
			in.close();
			out.write("KinaseGroup");
			Iterator itr2 = cluster_name.keySet().iterator();
			while (itr2.hasNext()) {
				String cluster = (String)itr2.next();
				out.write("\t" + cluster);
			}
			out.write("\n");
			Iterator itr = cluster_map.keySet().iterator();
			while (itr.hasNext()) {
				String kinaseFamily = (String)itr.next();
				out.write(kinaseFamily);
				HashMap map = (HashMap)cluster_map.get(kinaseFamily);
				itr2 = cluster_name.keySet().iterator();
				while (itr2.hasNext()) {
					String cluster = (String)itr2.next();
					int count = 0;
					if (map.containsKey(cluster)) {
						count = (Integer)map.get(cluster);						
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
