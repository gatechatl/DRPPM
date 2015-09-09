package RNAseqTools.AlternativeSplicing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FilterMATSResults {
	public static void execute(String[] args) {
		
		try {
			
			String inputFilePath = args[0];
			double pvalCutoff = new Double(args[1]);
			double incRatioCutoff = new Double(args[2]);

			
			String[] files = {"A3SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt", "A5SS.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt", "MXE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt", "RI.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt.addGeneName.txt"};
			for (String file: files) {
				HashMap map = new HashMap();
				
				
				FileWriter fwriter = new FileWriter(inputFilePath + "/" + file + ".filter.txt");
				BufferedWriter out = new BufferedWriter(fwriter);
				
				FileInputStream fstream = new FileInputStream(inputFilePath + "/" + file);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				out.write(in.readLine() + "\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					double pvalue = new Double(split[split.length - 5]);
					double incLevel = new Double(split[split.length - 1]);
					if (pvalue < pvalCutoff && Math.abs(incLevel) > incRatioCutoff) {
						//System.out.println(str);
						//map.put(split[1], split[1]);
						out.write(str + "\n");
					}
				}
				in.close();
				out.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
