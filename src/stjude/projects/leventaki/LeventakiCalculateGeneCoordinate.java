package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class LeventakiCalculateGeneCoordinate {


	public static String description() {
		return "Calculate gene coordinates";
	}
	public static String type() {
		return "LEVENTAKI";
	}
	public static String parameter_info() {
		return "[inputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String chr = split[2];
				int start = new Integer(split[3]);
				int end = new Integer(split[4]);
				if (map.containsKey(split[0])) {
					String prev = (String)map.get(split[0]);
					int prev_start = new Integer(prev.split(":")[1]);
					int prev_end = new Integer(prev.split(":")[2]);
					int length = end - start;
					int prev_length = prev_end - prev_start;
					if (length > prev_length) {
						map.put(split[0], chr + ":" + start + ":" + end);
					}
				} else {
					map.put(split[0], chr + ":" + start + ":" + end);
				}
			
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				String coord = (String)map.get(gene);
				System.out.println(gene + "\t" + coord);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
