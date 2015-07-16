package Metagenomic.Postprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CombineOTUCounts {

	public static void execute(String[] args) {
		
		try {
			
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);							
			
			String inputFile = args[0];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			String[] split4 = in.readLine().split("\t");
			out.write("OTU\t" + split4[1]);
			for (int i = 2; i < split4.length; i++) {
				out.write("\t" + split4[i]);
			}
			out.write("\n");
			//out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String line1 = (String)map.get(split[0]);
					String line2 = str;
					map.put(split[0], combineLines(line1, line2));
				} else {
					map.put(split[0], str);
				}
				
			}
			in.close();
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String line = (String)map.get(key);
				String[] split3 = line.split("\t");
				out.write("OTU" + split3[1]);
				for (int i = 2; i < split3.length; i++) {
					out.write("\t" + split3[i]);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String combineLines(String line1, String line2) {
		String[] split1 = line1.split("\t");
		String[] split2 = line2.split("\t");
		String result = split1[0] + "\t" + split1[1];		
		for (int i = 2; i < split1.length; i++) {
			result += "\t" + (new Double(split1[i]) + new Double(split2[i]));
		}
		return result;
	}
}
