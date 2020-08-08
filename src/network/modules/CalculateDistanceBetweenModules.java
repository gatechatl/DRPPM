package network.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CalculateDistanceBetweenModules {

	public static String type() {
		return "NETWORK";
	}
	public static String description() {
		return "Calculate the distance between module from JUMPn";
	}
	public static String parameter_info() {
		return "[moduleInfoFile] [sifFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String moduleInfoFile = args[0];
			String sifFile = args[1];
			String outputFile = args[2];

			HashMap module = new HashMap();
			HashMap gene_map = new HashMap();
			FileInputStream fstream = new FileInputStream(moduleInfoFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] genes = split[1].split(",");
				LinkedList list = new LinkedList();
				for (String gene: genes) {
					list.add(gene);						
					gene_map.put(gene, gene);
				}
				module.put(split[0], list);
			}
			in.close();

			HashMap network = new HashMap();
			fstream = new FileInputStream(sifFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (gene_map.containsKey(split[2]) || gene_map.containsKey(split[0])) {
					if (network.containsKey(split[0])) {
						LinkedList list = (LinkedList)network.get(split[0]);
						list.add(split[2]);
						network.put(split[0], list);
					} else {
						LinkedList list = new LinkedList();
						list.add(split[2]);
						network.put(split[0], list);
					}
					if (network.containsKey(split[2])) {
						LinkedList list = (LinkedList)network.get(split[2]);
						list.add(split[0]);
						network.put(split[2], list);
					} else {
						LinkedList list = new LinkedList();
						list.add(split[0]);
						network.put(split[2], list);
					}
				}								
			}
			in.close();
			HashMap result = new HashMap();
			
			Iterator itr = module.keySet().iterator();
			while (itr.hasNext()) {
				String module_name_1 = (String)itr.next();
				Iterator itr2 = module.keySet().iterator();
				while (itr2.hasNext()) {
					String module_name_2 = (String)itr2.next();
					if (!module_name_1.equals(module_name_2)) {
						LinkedList module1_genes = (LinkedList)module.get(module_name_1);
						Iterator itr3 = module1_genes.iterator();
						while (itr3.hasNext()) {
							String gene1 = (String)itr3.next();
							if (network.containsKey(gene1)) {
								LinkedList network_gene_list = (LinkedList)network.get(gene1);
								LinkedList module2_genes = (LinkedList)module.get(module_name_2);
								Iterator itr4 = module2_genes.iterator();
								while (itr4.hasNext()) {
									String gene2 = (String)itr4.next();
									if (network_gene_list.contains(gene2)) {
										if (result.containsKey(module_name_1 + "\t" + module_name_2)) {
											int count = (Integer)result.get(module_name_1 + "\t" + module_name_2);
											count++;
											result.put(module_name_1 + "\t" + module_name_2, count);
										} else {
											if (result.containsKey(module_name_2 + "\t" + module_name_1)) {
												int count = (Integer)result.get(module_name_2 + "\t" + module_name_1);
												count++;
												result.put(module_name_2 + "\t" + module_name_1, count);
											} else {
												result.put(module_name_1 + "\t" + module_name_2, 1);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			itr = module.keySet().iterator();
			while (itr.hasNext()) {
				String module_name_1 = (String)itr.next();
				Iterator itr2 = module.keySet().iterator();
				while (itr2.hasNext()) {
					String module_name_2 = (String)itr2.next();
					String key = module_name_1 + "\t" + module_name_2;
					if (result.containsKey(key)) {						
						int count = (Integer)result.get(key);
						
						out.write(module_name_1 + "\t" + count + "\t" + module_name_2 + "\n");
					} else {
						out.write(module_name_1 + "\t0\t" + module_name_2 + "\n");
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
