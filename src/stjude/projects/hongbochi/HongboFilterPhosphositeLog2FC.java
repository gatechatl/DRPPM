package stjude.projects.hongbochi;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class HongboFilterPhosphositeLog2FC {

	public static String parameter_info() {
		return "[inputFilelog2FC two columns]";
	}
	public static String type() {
		return "HONGBO";
	}
	public static String description() {
		return "Filter log2FC list";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			System.out.println(header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (map.containsKey(split[0])) {
					LinkedList list = (LinkedList)map.get(split[0]);
					list.add(str);
					map.put(split[0], list);
				} else {
					LinkedList list = new LinkedList();
					list.add(str);
					map.put(split[0], list);
				}
			}
			in.close();			
			
			HashMap final_map = new HashMap();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				LinkedList list = (LinkedList)map.get(key);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String line = (String)itr2.next();
					String[] split = line.split("\t");
					double log2_torin = new Double(split[1]);
					double log2_raptor = new Double(split[2]);
					if (log2_torin > 0.2 && log2_raptor > 0.4) {
						if (final_map.containsKey(split[0])) {
							String prev_line = (String)final_map.get(split[0]);
							String[] prev_split = prev_line.split("\t");
							double prev_log2_torin = new Double(prev_split[1]);
							double prev_log2_raptor = new Double(prev_split[2]);
							if (log2_torin + log2_raptor > prev_log2_torin + prev_log2_raptor) {
								final_map.put(split[0], line);
							}
						} else {
							final_map.put(split[0], line);
						}
						
					} 
				}
				list = (LinkedList)map.get(key);
				itr2 = list.iterator();
				while (itr2.hasNext()) {
					String line = (String)itr2.next();
					String[] split = line.split("\t");
					double log2_torin = new Double(split[1]);
					double log2_raptor = new Double(split[2]);
					if (!final_map.containsKey(split[0])) {
						if (log2_raptor > 0.4) {
							if (final_map.containsKey(split[0])) {
								String prev_line = (String)final_map.get(split[0]);
								String[] prev_split = prev_line.split("\t");
								double prev_log2_torin = new Double(prev_split[1]);
								double prev_log2_raptor = new Double(prev_split[2]);
								if ((log2_torin + log2_raptor) > (prev_log2_torin + prev_log2_raptor)) {
									final_map.put(split[0], line);
								}
							} else {
								final_map.put(split[0], line);
							}
							
						} 
					}
				}
				list = (LinkedList)map.get(key);
				itr2 = list.iterator();
				while (itr2.hasNext()) {
					String line = (String)itr2.next();
					String[] split = line.split("\t");
					double log2_torin = new Double(split[1]);
					double log2_raptor = new Double(split[2]);				
					if (!final_map.containsKey(split[0])) {
						if (log2_torin > 0.2) {
							if (final_map.containsKey(split[0])) {
								String prev_line = (String)final_map.get(split[0]);
								String[] prev_split = prev_line.split("\t");
								double prev_log2_torin = new Double(prev_split[1]);
								double prev_log2_raptor = new Double(prev_split[2]);
								if ((log2_torin + log2_raptor) > (prev_log2_torin + prev_log2_raptor)) {
									final_map.put(split[0], line);
								}
							} else {
								final_map.put(split[0], line);
							}
							
						} 
					}
				}
				list = (LinkedList)map.get(key);
				itr2 = list.iterator();
				while (itr2.hasNext()) {
					String line = (String)itr2.next();
					String[] split = line.split("\t");
					double log2_torin = new Double(split[1]);
					double log2_raptor = new Double(split[2]);
					
					if (!final_map.containsKey(split[0])) {
						if (final_map.containsKey(split[0])) {
							String prev_line = (String)final_map.get(split[0]);
							String[] prev_split = prev_line.split("\t");
							double prev_log2_torin = new Double(prev_split[1]);
							double prev_log2_raptor = new Double(prev_split[2]);
							if ((log2_torin + log2_raptor) > (prev_log2_torin + prev_log2_raptor)) {
								final_map.put(split[0], line);
							}
						} else {
							final_map.put(split[0], line);
						}
						
					}
				}
			}
			
			
			Iterator itr3 = final_map.keySet().iterator();
			while (itr3.hasNext()) {
				String key = (String)itr3.next();
				System.out.println(final_map.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
