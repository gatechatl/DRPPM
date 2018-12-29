package microsoft.document.word.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MergeOneHitWonderWithMertzList {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\Input\\one_hit_wonder.txt";
			String orderFileName = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\Input\\MertzSortedGeneList.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Mertz\\DEEP\\Input\\MertzBai_ReOrderedList.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String accession = split[1];
				String peptide = split[8];
				double jscore = new Double(split[14]);
				String key = accession + "\t" + peptide;
				if (map.containsKey(key)) {
					String old_str = (String)map.get(key);
					String[] split2 = old_str.split("\t");
					double jscore_old = new Double(split2[14]);
					if (jscore > jscore_old) {
						map.put(accession + "\t" + peptide, str);
					}
				} else {
					map.put(accession + "\t" + peptide, str);
				}
			}
			in.close();
			
			FileInputStream fstream2 = new FileInputStream(orderFileName);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			out.write(header + "\n");
			while (in2.ready()) {
				String str2 = in2.readLine();
				String[] split = str2.split("\t");
				String name = split[0] + "\t" + split[4];
				String stuff = (String)map.get(name);
				out.write(stuff + "\n");				
			}
			in2.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
