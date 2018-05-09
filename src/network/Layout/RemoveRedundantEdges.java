package network.Layout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class RemoveRedundantEdges {


	public static String type() {
		return "NETWORK";
	}
	public static String description() {
		return "Remove the redundant edges";
	}
	public static String parameter_info() {
		return "[edgeInput] [RedundantEdgesRemovedOutput]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0];
			String outputFile = args[1];
			
			HashMap map = new HashMap();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));		
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String tag1 = split[0].toUpperCase() + "\t" + split[2].toUpperCase();
				String tag2 = split[2].toUpperCase() + "\t" + split[0].toUpperCase();
				if (!map.containsKey(tag1) && !map.containsKey(tag2)) {
					out.write(str + "\n");
				}
				map.put(tag1, tag1);
				map.put(tag2, tag2);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
