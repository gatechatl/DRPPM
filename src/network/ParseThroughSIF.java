package network;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ParseThroughSIF {

	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "Count Edges and Nodes in sif file";
	}
	public static String parameter_info() {
		return "[sifFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap mapEdges = new HashMap();
			HashMap mapNodes = new HashMap();
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String node1 = split[0];
				String node2 = split[2];
				String comb1 = node1 + "\t" + node2;
				String comb2 = node2 + "\t" + node1;
				if (!(mapEdges.containsKey(comb1) || mapEdges.containsKey(comb2))) {
					mapEdges.put(comb1, comb1);
				}
				mapNodes.put(node1, node1);
				mapNodes.put(node2, node2);
			}
			in.close();
			System.out.println("Edges: " + mapEdges.size());
			System.out.println("Nodes: " + mapNodes.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
