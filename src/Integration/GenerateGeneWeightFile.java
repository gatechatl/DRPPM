package Integration;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateGeneWeightFile {

	public static String parameter_info() {
		return "[phosphoAnnotationFile]";
	}
	public static void execute(String[] args) {
		
		try {
			System.out.println("Accession\tSite\tWeight");
			String phosphoAnnotationFile = args[0];
			HashMap map = readPhosphoActivityFile(phosphoAnnotationFile);
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				double val = (Double)map.get(key);
				System.out.println(key + "\t" + val);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap readPhosphoActivityFile(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				if (split[4].contains("activity") && split[4].contains("induced") && !split[4].contains("inhibited")) {
					map.put(split[1] + "\t" + split[3].replaceAll("-p", ""), 1.0);
					//System.out.println(str);
				} else if (split[4].contains("activity") && !split[4].contains("induced") && split[4].contains("inhibited")) {
					//System.out.println(str);
					map.put(split[1] + "\t" + split[3].replaceAll("-p", ""), -1.0);
				} else {
					map.put(split[1] + "\t" + split[3].replaceAll("-p", ""), 0.5); // All functional sites are recorded
				}											
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
