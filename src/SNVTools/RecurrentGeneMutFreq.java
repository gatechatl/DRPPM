package SNVTools;

/**
 * Compiles the list of samples present for recurrence,
 * Preferably Filter out samples that are non-silent.
 * @author tshaw
 *
 */

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class RecurrentGeneMutFreq {

	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap sample_name = new HashMap();
			String inputFile = args[0];
			int SNVClassIndex = new Integer(args[1]);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					String inputFile2 = str;
					String fileTag = str.split("/")[1].split("_")[0];
					FileInputStream fstream2 = new FileInputStream(inputFile2);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					while (in2.ready()) {
						String str2 = in2.readLine();
						String[] split = str2.split("\t");
						String gene = split[0];
						if (!split[SNVClassIndex].equals("silent")) {
							if (sample_name.containsKey(gene)) {
								HashMap everything = (HashMap)sample_name.get(gene);
								everything.put(fileTag,  "");
								sample_name.put(gene, everything);
							} else {
								HashMap everything = new HashMap();
								everything.put(fileTag, "");
								sample_name.put(gene, everything);
							}
							
							if (map.containsKey(gene)) {
								int count = (Integer)map.get(gene);
								map.put(gene, count + 1);
							} else {
								map.put(gene, 1);
							}
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

