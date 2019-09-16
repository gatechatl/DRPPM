package network.layout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class NetworkNodeReplaceColor {

	public static String description() {
		return "Replace node color.";
	}
	public static String type() {
		return "NETWORK";
	}
	public static String parameter_info() {
		return "[nodeFile] [color_meta_file] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			
			String node_file = args[0];
			String color_meta_file = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(color_meta_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			in.close();
			
			fstream = new FileInputStream(node_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String new_str = split[0];
				if (map.containsKey(split[0])) {
					for (int i = 1; i < split.length; i++) {
						if (i == 3) {
							String color = (String)map.get(split[0]);
							new_str += "\t" + color;
						} else {
							new_str += "\t" + split[i];
						}
					}
				} else {
					new_str = str;
				}
				out.write(new_str + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
