package proteomics.phospho.tools.summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class PhosphoSummarizeKeepTopHit {
	
	public static void execute(String[] args) {
		
		try {
			
			String fileName = args[0];
			String outputFile = args[1];
			
			HashMap map = new HashMap();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			// assumes the last column is the score
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double score = new Double(split[split.length - 1]);
				String key = split[1] + "\t" + split[4];
				if (map.containsKey(key)) {
					String str2 = (String)map.get(key);
					String[] split2 = str2.split("\t");
					double orig_score = new Double(split2[split2.length - 1]);
					if (orig_score < score) {
						map.put(key, str);
					}
				} else {
					map.put(key, str);				
				}
			}
			in.close();
			
			out.write(header + "\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String str = (String)map.get(key);
				out.write(str + "\n");
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
