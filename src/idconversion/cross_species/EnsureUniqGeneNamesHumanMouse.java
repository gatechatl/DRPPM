package idconversion.cross_species;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Double check the hs_mm_homo_updated_preraw_*.txt file is uniq 
 * @author tshaw
 *
 */
public class EnsureUniqGeneNamesHumanMouse {

	public static String type() {
		return "Uniq gene name";
	}
	public static String description() {
		return "Double check the hs_mm_homo_updated_preraw_*.txt file is uniq";
	}
	public static String parameter_info() {
		return "[inputFile] [goodFile] [controversialFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			HashMap conflict = new HashMap();
			HashMap bad = new HashMap();
			String inputFile = args[0];
			String goodFile = args[1];
			String controversialFile = args[2];			

			FileWriter fwriter = new FileWriter(goodFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(controversialFile);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
						
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String pair = (String)map.get(split[0]);

					if (split[0].toUpperCase().equals(split[1].toUpperCase())) {
						map.put(split[0], split[1]);
					} else if (split[0].toUpperCase().equals(pair.toUpperCase())) {
						map.put(split[0], pair);
					} else {
						bad.put(split[0], split[0]);
						conflict.put(split[0] + "\t" + split[1], split[0] + "\t" + split[1]);
						conflict.put(split[0] + "\t" + pair, split[0] + "\t" + pair);
					}
				} else {
					map.put(split[0], split[1]);
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String human = (String)itr.next();
				if (!bad.containsKey(human)) {
					String mouse = (String)map.get(human);
					out.write(human + "\t" + mouse + "\n");
				}
			}
			out.close();
			
			Iterator itr2 = bad.keySet().iterator();
			while (itr2.hasNext()) {
				String badGene = (String)itr2.next();
				Iterator itr3 = conflict.keySet().iterator();
				while (itr3.hasNext()) {
					String str = (String)itr3.next();
					String[] split = str.split("\t");
					if (badGene.equals(split[0])) {
						out2.write(str + "\n");
					}
				}				
			}
			out.close();
			out2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
