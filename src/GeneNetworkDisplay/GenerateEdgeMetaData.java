package GeneNetworkDisplay;

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
public class GenerateEdgeMetaData {

	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String fileName = args[0];
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0] + "\t" + split[1] + "\t" + split[2], str);
				
			}
			in.close();
			
			System.out.println("Node1\tConnection\tNode2\tWidth\tEdgeColor\tArrowShape\tLineStyle\tOpacity");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String nodes = (String)itr.next();
				System.out.println(nodes + "\t" + 1 + "\t" + "black" + "\t" + "triangle" + "\t" + "solid" + "\t" + "0.5");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
