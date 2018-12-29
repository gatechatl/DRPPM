package rnaseq.expression.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CombineFPKMFiles {

	public static void execute(String[] args) {
		
		try {
			String fileName = args[0];
			String fileName2 = args[1];
			String outputFile = args[2];
			
			HashMap map1 = new HashMap();			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map1.put(split[0], str);
			}
			in.close();
			
			HashMap map2 = new HashMap();
			
			fstream = new FileInputStream(fileName2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String file2Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map2.put(split[0], str);
			}
			in.close();

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			out.write(file1Header + "\t" + removeFirstCol(file2Header + "\n"));
			Iterator itr = map1.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (map2.containsKey(geneName)) {
					String file1_str = (String)map1.get(geneName);
					String file2_str = (String)map2.get(geneName);
					out.write(geneName + "\t" + removeFirstCol(file1_str) + "\t" + removeFirstCol(file2_str) + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String removeFirstCol(String str) {
		String result = "";
		String[] split = str.split("\t");
		
		if (split.length > 1) {
			result = split[1];
			for (int i = 2; i < split.length; i++) {
				result += "\t" + split[i];
			}
		}
		return result;
	}
}
