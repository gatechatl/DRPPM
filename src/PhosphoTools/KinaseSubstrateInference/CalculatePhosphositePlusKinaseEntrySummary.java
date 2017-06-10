package PhosphoTools.KinaseSubstrateInference;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CalculatePhosphositePlusKinaseEntrySummary {

	public static String description() {
		return "Calculate the number of kinase within phosphositeplus.";
	}
	public static String type() {
		return "KinaseSubstrate";
	}
	public static String parameter_info() {
		return "[phosphositePlusKinaseSubstrateFile]";
	}
	public static void execute(String[] args) {
		try {
			
			HashMap map = new HashMap();
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[0].toUpperCase();
				if (map.containsKey(kinase)) {
					int count = (Integer)map.get(kinase);
					count = count + 1;
					map.put(kinase, count);
				} else {
					map.put(kinase, 1);
				}
			}
			in.close();
			
			HashMap max = new HashMap();
			for (int i = 0; i < map.size(); i++) {
				int max_count = 0;
				String max_kinase = "";
				Iterator itr = map.keySet().iterator();
				while (itr.hasNext()) {
					String kinase = (String)itr.next();
					int count = (Integer)map.get(kinase);
					if (count > max_count) {
						max_count = count;
						max_kinase = kinase;
					}
				}
				map.remove(max_kinase);
				System.out.println(max_kinase + "\t" + max_count);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
