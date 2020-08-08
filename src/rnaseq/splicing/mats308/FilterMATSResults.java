package rnaseq.splicing.mats308;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FilterMATSResults {
	
	public static String type() {
		return "MATS";
	}
	public static String description() {
		return "Filter MATS Result.";
	}
	public static String parameter_info() {
		return "[inputFilePath] [pvalCutoff] [fdrCutoff] [incRatioCutoff]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFilePath = args[0];
			double pvalCutoff = new Double(args[1]);
			double fdrCutoff = new Double(args[2]);
			double incRatioCutoff = new Double(args[3]);
			boolean fdr = false;
			/*if (args.length > 3) {
				if (args[3].toUpperCase().equals("TRUE")) {
					fdr = true;
				}
			}*/
			
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
					
					double p_value = new Double(split[split.length - 5]);
					//if (fdr) {
						
					double fdr_value = new Double(split[split.length - 4]);
					//}
					double incLevel = new Double(split[split.length - 1]);
					if (p_value <= pvalCutoff && fdr_value <= fdrCutoff && Math.abs(incLevel) >= incRatioCutoff) {
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
