package network.STRINGdbParsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A program for filtering the STRINGDB
 * @author tshaw
 *
 */
public class StringDBFilter {

	public static String dependencies() {
		return "NONE";
	}
	public static String description() {
		return "A program for filtering the STRING database";
	}
	public static String type() {
		return "NETWORK";
	}
	public static String parameter_info() {
		return "[inputFile] [score_cutoff] [connection_type e.g. binding,activation,ptmod,catalysis,ALL] [max_num_connection] [outputFile]\nConnection type include: binding,activation,inhibition,ptmod,reaction,catalysis\nUse ALL if want to include all connection";
		
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			
			double score_cutoff = new Double(args[1]);
			String connection_type = args[2];
			int max_connection = new Integer(args[3]);
			String outputFile = args[4];
			
			HashMap graph = grabSIFGraph(inputFile, true);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (graph.containsKey(split[0]) && graph.containsKey(split[1])) {
					LinkedList list1 = (LinkedList)graph.get(split[0]);
					LinkedList list2 = (LinkedList)graph.get(split[1]);
					if (list1.size() <= max_connection && list2.size() <= max_connection) {
						double score = new Double(split[5]);
						String mode = split[2];
						if (connection_type.equals("ALL")) {
							if (score >= score_cutoff) {
								out.write(str + "\n");
							}
						} else {
							String[] split_conn_type = connection_type.split(",");
							for (String type: split_conn_type) {
								if (type.equals(mode)) {
									if (score >= score_cutoff) {
										out.write(str + "\n");
									}
								}
							}
						}
					}
				} else {
					System.out.println("Shouldn't happen: " + split[0] + "\t" + split[1]);
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap grabSIFGraph(String inputFile, boolean header) {
		HashMap subgraph = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			if (header) {
				in.readLine();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[0] = split[0];
				split[1] = split[1];
				
				if (subgraph.containsKey(split[0])) {
					LinkedList list = (LinkedList)subgraph.get(split[0]);
					if (!list.contains(split[1])) {
						list.add(split[1]);
					}
					subgraph.put(split[0], list);
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(split[1])) {
						list.add(split[1]);
					}
					subgraph.put(split[0], list);
				}
				if (subgraph.containsKey(split[1])) {
					LinkedList list = (LinkedList)subgraph.get(split[1]);
					if (!list.contains(split[0])) {
						list.add(split[0]);
					}
					subgraph.put(split[1], list);
				} else {
					LinkedList list = new LinkedList();
					if (!list.contains(split[0])) {
						list.add(split[0]);
					}
					subgraph.put(split[1], list);
				}
				
			
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subgraph;
	}
}
