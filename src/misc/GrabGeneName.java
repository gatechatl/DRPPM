package misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Grab the geneName column in a file
 * @author tshaw
 *
 */
public class GrabGeneName {

	public static String description() {
		return "Grab the gene name by specifying the column index of a file";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputFile] [colIndex] [skip header flag true/false]";
	}
	public static void execute(String[] args) {
		try {
			String inputFile = args[0];
			int colIndex = new Integer(args[1]);
			boolean header = new Boolean(args[2]);
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			if (header) {
				in.readLine();
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[colIndex].equals("NA")) {
					map.put(split[colIndex],  "");
				}
			}
			in.close();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				System.out.println(key);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
