package network.MISC;

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
 * Generate a subgraph given a sif file and a list of nodes
 * require bionet installed
 * @author tshaw
 *
 */
public class GenerateSubgraph {

	public static String type() {
		return "NETWORK";
	}
	public static String description() {
		return "generate a subgraph given the node";
	}
	public static String parameter_info() {
		return "[graphFile] [graphFileHeader flag true/false] [nodeFile] [index] [nodeFileHeader flag true/false] [cutoff] [outputFile] [outputSubGraph]";
	}
	
	
	public static void execute(String[] args) {
		try {
			String graphFile = args[0];
			boolean graphFileHeader = new Boolean(args[1]); 
			String nodeFile = args[2];		
			int index = new Integer(args[3]);
			boolean nodeFileHeader = new Boolean(args[4]);	
			int cutoff = new Integer(args[5]);
			String outputFile = args[6];
			String outputSubGraph = args[7];
	
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(outputSubGraph);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			HashMap nodes = grabNodes(nodeFile, index, nodeFileHeader);
			HashMap subgraph = grabSIFGraph(graphFile, nodes, graphFileHeader);
			LinkedList clusters = grabClusters(subgraph);
			int id = 1;
			Iterator itr = clusters.iterator();
			while (itr.hasNext()) {
				HashMap map = (HashMap)itr.next();
				
				// check if the size of the cluster exceed a certain number of nodes
				if (map.size() >= cutoff) {
					String line = "Cluster" + id + "\t";
					String nodestr = "";
					Iterator itr2 = map.keySet().iterator();
					while (itr2.hasNext()) {
						String gene = (String)itr2.next();				
						line += gene + ",";
					}
					out.write(line + "\n");
					id++;
				}
			}
			out.close();
			
			HashMap uniq = new HashMap();
			itr = subgraph.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				LinkedList list = (LinkedList)subgraph.get(geneName);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String geneName2 = (String)itr2.next();
					
					if (!(uniq.containsKey(geneName + "\t" + geneName2) || uniq.containsKey(geneName2 + "\t" + geneName))) {
						out2.write(geneName + "\tconnection\t" + geneName2 + "\n");
						uniq.put(geneName + "\t" + geneName2, "");
						uniq.put(geneName2 + "\t" + geneName, "");
					}
				}
			}
			
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Graph all the nodes in the file
	 * @param inputFile
	 * @return
	 */
	public static HashMap grabNodes(String inputFile, int index, boolean header) {
		HashMap map = new HashMap();
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
				map.put(split[index].toUpperCase(), split[index].toUpperCase());
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static LinkedList grabClusters(HashMap graph) {
		
		
		// keep finding the cluster
		LinkedList smallergraphs = new LinkedList();
		Iterator itr = graph.keySet().iterator();
		while (itr.hasNext()) {
			String gene = (String)itr.next();			
			if (!checkGraphContainsNode(smallergraphs, gene)) {
				HashMap subgraph = new HashMap();
				boolean added = true;
				if (graph.containsKey(gene)) {
					subgraph.put(gene, gene);								
					while (added) {
						added = false;			
						HashMap new_subgraph = (HashMap)subgraph.clone();
						Iterator itr2 = subgraph.keySet().iterator();
						while (itr2.hasNext()) {
							String gene2 = (String)itr2.next();
							LinkedList genes = (LinkedList)graph.get(gene2);
							
							Iterator itr3 = genes.iterator();
							while (itr3.hasNext()) {
								String gene3 = (String)itr3.next();
								if (!new_subgraph.containsKey(gene3)) {
									new_subgraph.put(gene3, gene3);
									added = true;
								}
							}
						}				
						subgraph = new_subgraph;
					}
					smallergraphs.add(subgraph);
				}
			}
			
		}
		return smallergraphs;
	}
	public static boolean checkGraphContainsNode(LinkedList list, String node) {
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			HashMap map = (HashMap)itr.next();
			if (map.containsKey(node)) {
				return true;
			}
		}
		return false;
	}
	public static HashMap grabSIFGraph(String inputFile, HashMap map, boolean header) {
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
				split[0] = split[0].toUpperCase();
				split[2] = split[2].toUpperCase();
				if (map.containsKey(split[0]) && map.containsKey(split[2])) {
					if (subgraph.containsKey(split[0])) {
						LinkedList list = (LinkedList)subgraph.get(split[0]);
						if (!list.contains(split[2])) {
							list.add(split[2]);
						}
						subgraph.put(split[0], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[2])) {
							list.add(split[2]);
						}
						subgraph.put(split[0], list);
					}
					if (subgraph.containsKey(split[2])) {
						LinkedList list = (LinkedList)subgraph.get(split[2]);
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						subgraph.put(split[2], list);
					} else {
						LinkedList list = new LinkedList();
						if (!list.contains(split[0])) {
							list.add(split[0]);
						}
						subgraph.put(split[2], list);
					}
					
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subgraph;
	}
}
