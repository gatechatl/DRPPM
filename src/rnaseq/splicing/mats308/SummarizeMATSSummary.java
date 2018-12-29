package rnaseq.splicing.mats308;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SummarizeMATSSummary {

	public static String type() {
		return "MATS";
	}
	public static String description() {
		return "Summarize MATS results with expression filter.";
	}
	
	public static String parameter_info() {
		return "[inputFilePath] [SampleName] [GENE/EVENT] [limma table] [fdr cutoff]";
	}
	public static void execute(String[] args) {				
		try {						
			String inputFile = args[0];
			int[] index = {2, 4, 6, 8, 10, 12, 14};
			
			for (int i: index) {
				
				HashMap everything = new HashMap();
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();
				String[] split_header = header.split("\t");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					if (split.length > 14) {						
						for (String gene: split[i].split(",")) {
							everything.put(gene, gene);
						}
					}
				}
				in.close();
				System.out.println(split_header[i] + "\t" + everything.size());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
