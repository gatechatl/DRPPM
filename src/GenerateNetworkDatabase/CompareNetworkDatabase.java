package GenerateNetworkDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CompareNetworkDatabase {

	public static String parameter_info() {
		return "";
	}
	public static void execute(String[] args) {
		try {
			String sifFile1 = args[0];
			String sifFile2 = args[1];
			String sifFile1Only = args[2];
			
			FileWriter fwriter_sifFile1Only = new FileWriter(sifFile1Only);
			BufferedWriter out_sifFile1Only = new BufferedWriter(fwriter_sifFile1Only);
			
			String overlapFile = args[3];
			
			FileWriter fwriter_overlap = new FileWriter(overlapFile);
			BufferedWriter out_overlapFile = new BufferedWriter(fwriter_overlap);
			
			String sifFile2Only = args[4];
			

			FileWriter fwriter_sifFile2Only = new FileWriter(sifFile2Only);
			BufferedWriter out_sifFile2Only = new BufferedWriter(fwriter_sifFile2Only);
			
			String sifFile1Nodes = args[5];
			
			FileWriter fwriter_sifFile1Nodes = new FileWriter(sifFile1Nodes);
			BufferedWriter out_sifFile1Nodes = new BufferedWriter(fwriter_sifFile1Nodes);
			
			String overlapNodes = args[6];
			
			FileWriter fwriter_overlapNodes = new FileWriter(overlapNodes);
			BufferedWriter out_overlapNodes = new BufferedWriter(fwriter_overlapNodes);
			
			String sifFile2Nodes = args[7];

			FileWriter fwriter_sifFile2Nodes = new FileWriter(sifFile2Nodes);
			BufferedWriter out_sifFile2Nodes = new BufferedWriter(fwriter_sifFile2Nodes);
			
			HashMap map1 = new HashMap();			
			HashMap map2 = new HashMap();
			
			HashMap map1_nodes = new HashMap();
			HashMap map2_nodes = new HashMap();
			HashMap overlap_nodes = new HashMap();
			
			HashMap overlap = new HashMap();
			FileInputStream fstream = new FileInputStream(sifFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!map1.containsKey(split[2] + "\t" + split[0])) {
					map1.put(split[0] + "\t" + split[2], split[2] + "\t" + split[0]);
				}
				map1_nodes.put(split[0], split[0]);
				map1_nodes.put(split[2], split[2]);
				
			}
			in.close();
			
			fstream = new FileInputStream(sifFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map2_nodes.put(split[0], split[0]);
				map2_nodes.put(split[2], split[2]);
				if (!map2.containsKey(split[2] + "\t" + split[0])) {
					map2.put(split[0] + "\t" + split[2], split[2] + "\t" + split[0]);
				}
				if (map1.containsKey(split[0] + "\t" + split[2]) || map1.containsKey(split[2] + "\t" + split[0])) {
					overlap.put(split[0] + "\t" + split[2], split[2] + "\t" + split[0]);
					overlap_nodes.put(split[0], split[0]);
					overlap_nodes.put(split[2], split[2]);
					
					out_overlapFile.write(str + "\n");
					
				} else {
					out_sifFile2Only.write(str + "\n");
				}
				
			}
			in.close();
			
			fstream = new FileInputStream(sifFile1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!(map2.containsKey(split[0] + "\t" + split[2]) || map2.containsKey(split[2] + "\t" + split[0]))) {					
					out_sifFile1Only.write(str + "\n");
					
				}
				
			}
			in.close();
			out_overlapFile.close();
			out_sifFile1Only.close();
			out_sifFile2Only.close();
			int countFile1 = 0;
			int countFile2 = 0;
			int combined = 0;
			Iterator itr = map1_nodes.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (map2_nodes.containsKey(gene)) {
					out_overlapNodes.write(gene + "\n");
					combined++;
				} else {
					out_sifFile1Nodes.write(gene + "\n");
					countFile1++;
				}
			}
			itr = map2_nodes.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				if (map1_nodes.containsKey(gene)) {
					
				} else {
					out_sifFile2Nodes.write(gene + "\n");
					countFile2++;
				}
			}
			out_overlapNodes.close();
			out_sifFile1Nodes.close();
			out_sifFile2Nodes.close();
			System.out.println(sifFile1 + ": " + map1.size());
			System.out.println(sifFile2 + ": " + map2.size());
			System.out.println("Overlap: " + overlap.size());
			System.out.println("Nodes only in sif1File: " + countFile1);
			System.out.println("Nodes only in sif2File: " + countFile2);
			System.out.println("Nodes in overlap: " + overlap_nodes.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
