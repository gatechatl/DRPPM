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
public class GenerateNodeMetaData {

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
				map.put(split[0], split[0]);
				map.put(split[2], split[2]);
			}
			in.close();
			
			System.out.println("Node\tWeight\tOutColor\tBackColor\tX-axis\tY-axis\tShape\tValue");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String node = (String)itr.next();
				System.out.println(node + "\t" + 10 + "\t" + "black" + "\t" + "white" + "\t" + 100 + "\t" + 100 + "\t" + "ellipse" + "\t" + "0.0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
