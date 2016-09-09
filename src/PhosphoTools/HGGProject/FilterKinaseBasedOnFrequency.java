package PhosphoTools.HGGProject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FilterKinaseBasedOnFrequency {

	
	public static String parameter_info() {
		return "[kinase_inputFile] [kinaseList]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String queryFile = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] split2 = split[split.length - 1].split(",");
				for (String kinase: split2) {
					if (map.containsKey(kinase)) {
						int count = (Integer)map.get(kinase);
						count++;
						map.put(kinase, count);
					} else {
						map.put(kinase, 1);
					}
				}
			}
			in.close();			
			
			fstream = new FileInputStream(queryFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (map.containsKey(str)) {
					int count = (Integer)map.get(str);
					if (count >= 3) {
						System.out.println(str);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
