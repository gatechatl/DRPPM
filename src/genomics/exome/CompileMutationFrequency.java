package genomics.exome;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CompileMutationFrequency {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap sample_name = new HashMap();
			String inputFile = args[0];
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					String inputFile2 = str;
					FileInputStream fstream2 = new FileInputStream(inputFile2);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split = str2.split("\t");
						String gene = split[0];
						
						if (sample_name.containsKey(gene)) {
							HashMap everything = (HashMap)sample_name.get(gene);
							everything.put(inputFile2,  "");
							sample_name.put(gene, everything);
						} else {
							HashMap everything = new HashMap();
							everything.put(inputFile2, "");
							sample_name.put(gene, everything);
						}
						
						if (map.containsKey(gene)) {
							int count = (Integer)map.get(gene);
							map.put(gene, count + 1);
						} else {
							map.put(gene, 1);
						}
					}
					in2.close();
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				int count = (Integer)map.get(gene);
				HashMap everything = (HashMap)sample_name.get(gene);
				System.out.println(gene + "\t" + everything.size() + "\t" + convert(everything));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String convert(HashMap everything) {
		String result = "";
		Iterator itr = everything.keySet().iterator();
		while (itr.hasNext()) {
			result += (String)itr.next() + ",";
		}
		return result;
	}
}
