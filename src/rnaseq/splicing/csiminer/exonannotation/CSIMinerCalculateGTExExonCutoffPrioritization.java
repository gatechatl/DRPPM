package rnaseq.splicing.csiminer.exonannotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CSIMinerCalculateGTExExonCutoffPrioritization {

	
	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			HashMap map_total = new HashMap();
			String inputFile = "/Users/4472414/References/genome/Exon_Annotation/Exon_annotation_with_cutoff_20211109.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("_")[0];
				if (!split[15].equals("NA")) {
					if (map_total.containsKey(geneName)) {
						double count = (Double)map_total.get(geneName);
						count++;						
						map_total.put(geneName, count);
					} else {
						map_total.put(geneName, 1.0);
					}
				}
				if (split[20].equals("TRUE")) {
					if (map.containsKey(geneName)) {
						double count = (Double)map.get(geneName);
						count++;						
						map.put(geneName, count);
					} else {
						map.put(geneName, 1.0);
					}
				}
			}
			in.close();
			

			String outputFile = "/Users/4472414/References/genome/Exon_Annotation/gene_with_percent_of_sig_exon.txt"; //args[0]; 
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			Iterator itr = map_total.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				double total_hits = (Double)map_total.get(geneName);
				double sig_hits = 0.0;
				if (map.containsKey(geneName)) {
					sig_hits = (Double)map.get(geneName);
				}
				out.write(geneName + "\t" + (sig_hits / total_hits) + "\t" + sig_hits + "\t" + total_hits + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
