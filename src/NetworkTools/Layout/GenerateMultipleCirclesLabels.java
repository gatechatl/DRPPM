package NetworkTools.Layout;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate customized location for the nodes grouped based on module ID
 * The network will draw multiple circles based on each individual modules
 * @author tshaw
 *
 */
public class GenerateMultipleCirclesLabels {

	public static String type() {
		return "NETWORK";
	}
	public static String description() {
		return "Generate customized location for the nodes grouped based on module ID\nThe network will draw multiple circles based on each individual modules";
	}
	public static String parameter_info() {
		return "[moduleInformation] [moduleLayout]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String moduleInformationFile = args[0];
			String moduleLayoutFile = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(moduleInformationFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] genes = split[1].split(",");
				LinkedList list = new LinkedList();
				for (String gene: genes) {
					if (!list.contains(gene)) {
						list.add(gene);
					}
				}
				map.put(split[0], list);
			}
			in.close();

			// load module layout information 
			// col1: module name; col2: xaxis; col3: yaxis
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
				mod_layout.put(split[0], split[1] + "\t" + split[2] + "\t" + colour);
			}
			
			
			// generate the node meta data file
			System.out.println("Node\tWeight\tOutColor\tBackColor\tX-axis\tY-axis\tShape\tValue\tSize");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String moduleName = (String)itr.next();
				String layout_coord_str = (String)mod_layout.get(moduleName);
				int layout_xaxis = new Integer(layout_coord_str.split("\t")[0]);
				int layout_yaxis = new Integer(layout_coord_str.split("\t")[1]);
				String color_text = layout_coord_str.split("\t")[2];
				LinkedList list = (LinkedList)map.get(moduleName);
				HashMap coord = generateCoordinates(list);
				Iterator itr2 = coord.keySet().iterator();
				while (itr2.hasNext()) {
					String geneName = (String)itr2.next();
					String coord_str = (String)coord.get(geneName);
					int cir_xaxis = new Integer(coord_str.split("\t")[0]);
					int cir_yaxis = new Integer(coord_str.split("\t")[1]);					
					int xaxis = cir_xaxis + layout_xaxis;
					int yaxis = cir_yaxis + layout_yaxis;
					
					System.out.println(geneName + "\t" + 10 + "\t" + "black" + "\t" + color_text + "\t" + xaxis + "\t" + yaxis + "\t" + "ellipse" + "\t" + "0.0" + "\t" + "0");
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap generateCoordinates(LinkedList nodes) {
		HashMap map = new HashMap();
		double seperation_angle = (2.0 * Math.PI)/(nodes.size()); // calculation of the angle between each node
		int x_start = 0; // x-coordinate of initial node
		int y_start = 0; // y-coordinate of initial node
		int x_next, y_next;
		double radius = nodes.size() * 30 + 300;
		// FOR loop to draw rest of the nodes around the circle outline spaced out equally
		double angle = 0;
		Iterator itr = nodes.iterator();
		while (itr.hasNext()) {
			String geneName = (String)itr.next();
			x_next = (new Double((radius * (double) Math.sin(angle)) + x_start)).intValue(); 
			y_next = (new Double((radius * (double) Math.cos(angle)) + y_start)).intValue();
			map.put(geneName, x_next + "\t" + y_next);
			angle += seperation_angle;
			//for (double angle=0; angle<(2*Math.PI); angle=angle+seperation_angle) { 
			

				//System.out.println("x_next = " + x_next);
				//System.out.println("y_next = " + y_next);
				//System.out.println();
			//}
		}
		return map;
	}
}
