package Metabolomic.StructureClustering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class StructureFractionClustering {

	public static void execute(String[] args) {
		
		try {
			String path = args[0];
			String outputFile = args[1];
			HashMap map = new HashMap();
			
			LinkedList id_list = new LinkedList();
			File[] files = new File(path).listFiles();
			for (File file: files) {
				String name = file.getName();
				
				String id = name.split("_")[name.split("_").length - 1];
				id_list.add(id);
				String fileName = file.getPath();
				FileInputStream fstream = new FileInputStream(fileName);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					if (str.contains("Fragment count")) {
						while (in.ready()) {
							str = in.readLine();
							String[] split = str.split(" ");
							String formula = split[2];
							String smile = split[0];
							if (map.containsKey(formula)) {
								LinkedList ids = (LinkedList)map.get(formula);
								if (!ids.contains(id)) {
									ids.add(id);
									map.put(formula, ids);
								}
							} else {
								LinkedList list = new LinkedList();
								list.add(id);
								map.put(formula, list);
							}
						}
					}
				}
				in.close();
			}
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("IDs");
			/*Iterator itr2 = id_list.iterator();
			while (itr2.hasNext()) {
				String id = (String)itr2.next();
				out.write("\t" + id);
			}*/
			
			Iterator itr2 = map.keySet().iterator();
			while (itr2.hasNext()) {
				String formula = (String)itr2.next();
				out.write("\t" + formula);
			}
			out.write("\n");
			itr2 = id_list.iterator();
			while (itr2.hasNext()) {
				String id = (String)itr2.next();
				out.write(id);
				Iterator itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String formula = (String)itr.next();					
					LinkedList ids = (LinkedList)map.get(formula);										
					if (ids.contains(id)) {
						out.write("\t1");
					} else {
						out.write("\t0");
					}					
					
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
