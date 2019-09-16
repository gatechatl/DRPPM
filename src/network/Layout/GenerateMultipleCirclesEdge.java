package network.layout;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate multiple circles with edge information
 * @author tshaw
 *
 */
public class GenerateMultipleCirclesEdge {
	public static String type() {
		return "NETWORK";
	}
	public static String description() {
		return "Generate multiple circles with edge information.";
	}
	public static String parameter_info() {
		return "[fileName] [moduleLayoutFile] [moduleFile] [size] [opaque_module] [opaque_outside_module]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String fileName = args[0];
			String moduleLayoutFile = args[1];
			String moduleFile = args[2];
			int size = new Integer(args[3]);
			double opaque = new Double(args[4]);
			double opaque2 = new Double(args[5]);
			HashMap genes = new HashMap();
			LinkedList allnode = new LinkedList();
			HashMap mod = new HashMap();
			FileInputStream fstream = new FileInputStream(moduleFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] nodes = split[1].split(",");
				LinkedList list = new LinkedList();
				for (String node: nodes) {
					list.add(node);
					allnode.add(node);
					genes.put(node,  node);
				}
				mod.put(split[0], list);
				

			}
			in.close();
			
			HashMap mod_layout = new HashMap();
			fstream = new FileInputStream(moduleLayoutFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String colour = "white";
				if (split.length > 3) {
					colour = split[3];
					
				}
				mod_layout.put(split[0], colour);

			}
			in.close();
			
			
			fstream = new FileInputStream(fileName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals(split[2])) {
					if (genes.containsKey(split[0]) || genes.containsKey(split[2])) {
						map.put(split[0] + "\t" + split[1] + "\t" + split[2], str);
					}
				}				
			}
			in.close();
			
			System.out.println("Node1\tConnection\tNode2\tWidth\tEdgeColor\tArrowShape\tLineStyle\tOpacity");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String nodes = (String)itr.next();
				String colour = "black";
				boolean found = false;
				boolean found_outside_edge = false;
				String[] node = nodes.split("\t");
				Iterator itr2 = mod_layout.keySet().iterator();
				while (itr2.hasNext()) {
					String module_name = (String)itr2.next();
					LinkedList list = (LinkedList)mod.get(module_name);
					if (list.contains(node[0]) && list.contains(node[2])) {
						colour = (String)mod_layout.get(module_name);						
						found = true;
					}
					if (allnode.contains(node[0]) && list.contains(node[2])) {
						found_outside_edge = true;
					}
					if (allnode.contains(node[2]) && list.contains(node[0])) {
						found_outside_edge = true;
					}
				}
				if (found) {					
					System.out.println(nodes + "\t" + size + "\t" + colour + "\t" + "none" + "\t" + "solid" + "\t" + opaque);
				} else if (found_outside_edge) {
					System.out.println(nodes + "\t" + 2 + "\t" + colour + "\t" + "none" + "\t" + "solid" + "\t" + opaque2);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
