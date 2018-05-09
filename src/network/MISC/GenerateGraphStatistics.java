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
 * Generate the statistics about the graph
 * @author tshaw
 *
 */
public class GenerateGraphStatistics {
	public static String dependencies() {
		//return "R library BioNet installation";
		return "None";
	}
	
	public static String description() {
		return "Generate the basic statistics for a graph network"; 
	}
	
	public static String type() {
		return "NETWORK"; 
	}
	public static String parameter_info() {
		return "[sifInputFile] [header] [referenceFile] [outputHistogram] [outputStatistics]";
	}
	public static void execute(String[] args) {
		
		try {
			String sifInputFile = args[0];
			boolean header = new Boolean(args[1]);
			String referenceFile = args[2];						
			String outputHistogramFile = args[3];
			String outputStatistics = args[4];
			
			HashMap reference = grab_reference(referenceFile);
			FileWriter fwriter2 = new FileWriter(outputStatistics);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			HashMap found_reference = new HashMap();
			FileWriter fwriter = new FileWriter(outputHistogramFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Gene\tDegrees\n");
			HashMap connections = new HashMap();
			HashMap clusters = grabSIFGraph(sifInputFile, header);
			double degree = 0;
			int nodes = 0;
			Iterator itr = clusters.keySet().iterator();
			while (itr.hasNext()) {
				String genes = (String)itr.next();
				nodes++;
				if (reference.containsKey(genes)) {
					found_reference.put(genes, genes);
				}
				LinkedList conn = (LinkedList)clusters.get(genes);
				out.write(genes + "\t" + conn.size() + "\n");
				degree += conn.size();
				Iterator itr2 = conn.iterator();
				while (itr2.hasNext()) {
					String gene2 = (String)itr2.next();
					if (reference.containsKey(gene2)) {
						found_reference.put(gene2, gene2);
					}
					if (!(connections.containsKey(genes + "\t" + gene2) || connections.containsKey(gene2 + "\t" + genes))) {
						connections.put(genes + "\t" + gene2, "");
					}
				}
			}
			
			out2.write("Total Nodes: " + nodes + "\n");
			out2.write("Total Edges: " + connections.size() + "\n");
			out2.write("Average degree: " + floor2decimal(degree / nodes) + "\n");
			out2.write("% of genes covered by the reference file: " + floor2decimal((new Double(found_reference.size()) / reference.size()) * 100) + "%\n");
			out2.write("Total genes found in the reference: " +  found_reference.size());
			out2.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double floor2decimal(double val) {
		return new Double((new Double(val * 100).intValue())) / 100;
	}
	public static HashMap grab_reference(String inputFile) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
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
				split[0] = split[0].toUpperCase();
				split[2] = split[2].toUpperCase();
				
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
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subgraph;
	}
	public static String gen_subgraph_script(String inputFile, String nodeFile) {
		String result = "library(BioNet);\n";
		result += "graph = loadNetwork.sif(\"" + inputFile + "\", format=\"graphNEL\");\n";
		result += "";
		return result;
	}
}
