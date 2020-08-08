package network.statistics;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CalculateGraphStatistics {


	public static String type() {
		return "NETWORK";
	}
	public static String description() {
		return "Calculate the graph statistics";
	}
	public static String parameter_info() {
		return "[sifFile]";
	}
	/**
	 * Calculate basic graph statistics
	 * @param args
	 */
	public static void execute(String[] args) {
		
		try {
			
			HashMap edge = new HashMap();
			HashMap node = new HashMap();
			String sifFile = args[0];
			FileInputStream fstream = new FileInputStream(sifFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				String[] split = str.split("\t");
				String comboA = split[0] + "\t" + split[2];
				String comboB = split[2] + "\t" + split[0];
				if (edge.containsKey(comboA) || edge.containsKey(comboB)) {
					
				} else {
					edge.put(comboA, comboA);
				}
				node.put(split[0], split[0]);
				node.put(split[2], split[2]);
			}
			in.close();
			
			System.out.println("Total Nodes: " + node.size());
			System.out.println("Total Edges: " + edge.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
