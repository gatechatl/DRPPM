package stjude.projects.peng;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PengIRFinderOverlapFCcutoff {

	public static void main(String[] args) {
		
		try {
			
			
			String inputFile1 = "\\\\gsc.stjude.org\\project_space\\penggrp\\Alzheimer\\common\\CompareTotalvsStranded\\IRFinderCutoff\\6MmRNA_IRFinder_Filtered.txt";
			String inputFile2 = "\\\\gsc.stjude.org\\project_space\\penggrp\\Alzheimer\\common\\CompareTotalvsStranded\\IRFinderCutoff\\6MTotal_IRFinder_Filtered.txt";
			for (double cutoff = 0; cutoff < 13; cutoff++) {
				HashMap map1 = new HashMap();
				FileInputStream fstream = new FileInputStream(inputFile1);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header1 = in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					double log2FC = new Double(split[2]);
					if (log2FC < -cutoff) {
						map1.put(split[0], split[2]);
					}
				}
				in.close();
				
				int count = 0;
				//HashMap map1 = new HashMap();
				fstream = new FileInputStream(inputFile2);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header1 = in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					double log2FC = new Double(split[2]);
					if (log2FC < -cutoff) {
						if (map1.containsKey(split[0])) {
							count++;
						}
					}
				}
				in.close();
				
				System.out.println(cutoff + "\t" + count);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
