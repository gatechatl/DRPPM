package integrate.OverRepresentationAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GeneratePathwayMap {

	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			
			String fileName = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\Metabolomic_PathwayMap\\PathwayInformation.txt";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String genes = str.split("\t")[1];
				String[] split = genes.split(",");
				for (int i = 0; i < split.length; i++) {
					for (int j = 0; j < split.length; j++) {
						if (!split[i].equals(split[j])) {
							String forward = split[i] + "\tpathway\t" + split[j];
							String reverse = split[j] + "\tpathway\t" + split[i];
							if (!(map.containsKey(forward) || map.containsKey(reverse))) {
								map.put(forward, forward);
							}							
						}
					}
				}
			}
			in.close();
			
			String outputFile = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\Metabolomic_PathwayMap\\Graph.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				out.write(key + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
