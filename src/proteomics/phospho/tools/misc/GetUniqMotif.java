package proteomics.phospho.tools.misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class GetUniqMotif {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String fileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\JPengHGG\\MotifInfo\\kinase_motif.txt";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split(",");
				String result = split[1]; 
				for (int i = 2; i < split.length; i++) {
					result += "," + split[i];
				}
				map.put(result, result);
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
