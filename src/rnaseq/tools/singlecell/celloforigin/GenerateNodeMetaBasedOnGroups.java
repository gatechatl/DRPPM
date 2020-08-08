package rnaseq.tools.singlecell.celloforigin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Generates a meta data file user to fill in
 * @author tshaw
 *
 */
public class GenerateNodeMetaBasedOnGroups {

	public static String description() {
		return "Generate a node metadata file with color meta information";
	}
	public static String type() {
		return "NETWORK";
	}
	public static String parameter_info() {
		return "[sifFile] [colorInfoSameAsPCA]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String fileName = args[0];
			String groupColor = args[1];
			HashMap color = new HashMap();
			FileInputStream fstream = new FileInputStream(groupColor);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				color.put(split[0], split[1]);
			}
			in.close();
			
			
			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[0]);
				map.put(split[2], split[2]);
			}
			in.close();
			
			System.out.println("Node\tWeight\tOutColor\tBackColor\tX-axis\tY-axis\tShape\tValue\tSize");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String node = (String)itr.next();
				String colour = "#d3d3d3";
				if (color.containsKey(node)) {
					colour = (String)color.get(node);
				}
				System.out.println(node + "\t" + 10 + "\t" + "black" + "\t" + colour + "\t" + 100 + "\t" + 100 + "\t" + "ellipse" + "\t" + "0.0" + "\t" + "20");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
