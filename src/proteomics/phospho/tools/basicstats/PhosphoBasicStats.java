package proteomics.phospho.tools.basicstats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Calculate the S, T, Y modification proportion
 * @author tshaw
 *
 */
public class PhosphoBasicStats {

	
	public static void execute(String[] args) {
		try {
			
			
			String fileName = args[0];
			//String fileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\JPengHGG\\total\\preliminary_ascore\\ascore_hong_merged_20150123.txt";
			//String fileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\JPengHGG\\total\\preliminary_ascore\\ascore_20150123.txt";
			HashMap map = new HashMap();
			HashMap geneList = new HashMap();
			
			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//if (split[1].toUpperCase().contains("NTRK")) {
					//System.out.println(str);
					map.put(split[1], split[0]);
					
				//}
				geneList.put(split[0].split("\\.")[0], split[0]);
			}
			in.close();
			int serine = 0;
			int tyrosine = 0;
			int threonine = 0;
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String geneName = (String)map.get(key);
				if (key.contains("S#")) {
					serine++;
				}
				if (key.contains("T%")) {
					threonine++;
				}
				if (key.contains("Y*")) {
					tyrosine++;
				}
				out.write(">" + geneName + "\n" + key.replaceAll("#", "#").replaceAll("%", "#").replaceAll("\\*", "#").replaceAll("@", "") + "\n");
			}
			out.close();
			System.out.println("Number of Uniq Genes: " + geneList.size());
			System.out.println("Modified Serine peptides: " + serine);
			System.out.println("Modified Threonine peptides: " + threonine);
			System.out.println("Modified Tyrosine peptides: " + tyrosine);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
