package TranscriptionFactorTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateTFNetwork {

	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			String outputFile = "C:\\Users\\tshaw\\Desktop\\INTEGRATION\\TranscriptionFactorNetwork\\TFNetwork_v1_20160417.sif";

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			File files = new File("C:\\Users\\tshaw\\Desktop\\INTEGRATION\\TranscriptionFactorNetwork\\");
			for (File f: files.listFiles()) {
				if (f.isDirectory()) {
					
					FileInputStream fstream = new FileInputStream(f.getPath() + "\\" + "genes-regulate-genes.txt");
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));			
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (map.containsKey(split[0])) {
							LinkedList list = (LinkedList)map.get(split[0]);
							list.add(split[1]);
							map.put(split[0], list);
						} else {
							LinkedList list = new LinkedList();
							list.add(split[1]);
							map.put(split[0], list);
						}
					}
					in.close();
				}
			}
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				LinkedList list = (LinkedList)map.get(geneName);
				Iterator itr2 = list.iterator();
				while (itr2.hasNext()) {
					String target = (String)itr2.next();
					out.write(geneName + "\t" + target + "\n");
				}				
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
