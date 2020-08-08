package proteomics.phospho.tools.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * This is a specialized class for matching mtor reference with phosphorylation_site_dataset.txt
 * @author tshaw
 *
 */
public class OverlapMtorReference {

	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\REFERENCE\\Phosphorylation_site_dataset.txt");
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 5) {
					String geneSymbol = split[2].toUpperCase();
					String acc_id = split[1];
					String type = split[5] + "-p";
					String org = split[7];
					if (org.equals("mouse")) {
						map.put(geneSymbol + "_" + type, acc_id);
						//System.out.println(geneSymbol + "_" + type);
					}
				}
			}
			in.close();
			
			int count = 0;
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\REFERENCE\\mtor_reference_updated.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			fstream = new FileInputStream("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Phosphorylation\\REFERENCE\\mtor_reference.txt");
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[1].equals("bad") || split[1].equals("")) {
					String key = split[0] + "_" + split[4];
					//System.out.println(key);
					if (map.containsKey(key)) {
						System.out.println("find\t" + count++);
						out.write(split[0] + "\tgood\t" + map.get(key) + "\tmouse\t" + split[4] + "\n");
					} else {
						out.write(str + "\n");
					}
				} else {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
