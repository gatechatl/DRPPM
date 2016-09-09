package GeneNetworkDisplay;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.codec.binary.StringUtils;

/**
 * Generates a meta data file user to fill in
 * different type of edges: tee,triangle,dotted,dashed,diamond,none
 * @author tshaw
 *
 */
public class GenerateEdgeMetaData {

	public static String parameter_info() {
		return "[sif inputFile] [edgeCutoff]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String fileName = args[0];
			int cutoff_min = 0;
			int cutoff_2 = 2;
			int cutoff_max = 3;
			if (args.length > 1) {
				cutoff_min = new Integer(args[1]);
				cutoff_2 = new Integer(args[2]);
				cutoff_max = new Integer(args[3]);
			}
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				
				String[] split = str.split("\t");
				if (isNumeric(split[1])) {
					if (new Integer(split[1]) >= 1) {
						map.put(split[0] + "\t" + split[1] + "\t" + split[2], str);
					}
				} else {
				map.put(split[0] + "\t" + split[1] + "\t" + split[2], str);
				}
				
			}
			in.close();
			
			System.out.println("Node1\tConnection\tNode2\tWidth\tEdgeColor\tArrowShape\tLineStyle\tOpacity");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String nodes = (String)itr.next();
				String[] split = nodes.split("\t");
				if (args.length > 1) {
					
					double val = new Double(split[1]);
					if (val >= cutoff_min) {
						int width = 1;
						if (val < cutoff_2) {
							width = 1;
						} else if (val >= cutoff_2 && val < cutoff_max){
							width = 2;
						} else if (val >= cutoff_max) {
							width = 3;
						} else {
							width = new Double(val).intValue();
						
						}
						double alpha = 0.1 * val;
						if (alpha > 0.5) {
							alpha = 0.5;
						}
						System.out.println(nodes + "\t" + width + "\t" + "black" + "\t" + "none" + "\t" + "solid" + "\t" + alpha);
						/*if (new Integer(split[1]) >= cutoff) {
							System.out.println(nodes + "\t" + 3 + "\t" + "black" + "\t" + "triangle" + "\t" + "solid" + "\t" + "0.5");
						} else {
							System.out.println(nodes + "\t" + 1 + "\t" + "black" + "\t" + "triangle" + "\t" + "solid" + "\t" + "0.1");
						}*/
					} else {

					}
				} else {
					System.out.println(nodes + "\t" + 1 + "\t" + "black" + "\t" + "triangle" + "\t" + "solid" + "\t" + "0.5");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean isNumeric(String str) {  
		try {  
			double d = Double.parseDouble(str);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}  
		return true;  
	}
}
