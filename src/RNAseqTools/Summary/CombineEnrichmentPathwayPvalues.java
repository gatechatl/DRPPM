package RNAseqTools.Summary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CombineEnrichmentPathwayPvalues {

	public static String type() {
		return "PostDE";
	}
	public static String description() {
		return "Combine the enrichment pathways into a table";
	}
	public static String parameter_info() {
		return "[file1] [file2] [file...] [tag_up] [tag_dn] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			//System.out.print("GeneName");
			HashMap[] map_up = new HashMap[args.length];
			HashMap[] map_dn = new HashMap[args.length];
			String tag_up = args[args.length - 3];
			String tag_dn = args[args.length - 2];
			String outputFile = args[args.length - 1];
			int UserInputNum = 3; // change this based on number of user input above
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String header = "Name\tType";
			HashMap gene = new HashMap();
			
			for (int i = 0; i < args.length - UserInputNum; i++) {
				map_up[i] = new HashMap();
				map_dn[i] = new HashMap();
				String inputFile = args[i];
				map_up[i] = grabPvalue(inputFile, tag_up);
				map_dn[i] = grabPvalue(inputFile, tag_dn);
				Iterator itr = map_up[i].keySet().iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr.next();
					gene.put(geneName, geneName);
				}
				itr = map_dn[i].keySet().iterator();
				while (itr.hasNext()) {
					String geneName = (String)itr.next();
					gene.put(geneName, geneName);
				}
				//System.out.print("\t" + args[i]);
				header += "\t" + args[i];
			}
			
			out.write(header + "\tAvgPvalue\n");
			//System.out.println();
			Iterator itr2 = gene.keySet().iterator();
			while (itr2.hasNext()) {
				String geneName = (String)itr2.next();
				//System.out.print(geneName);
				out.write(geneName + "\t" + tag_up);
				double total = 0;
				for (int i = 0; i < args.length - UserInputNum; i++) {
					String pvalue = "1.0";
					if (map_up[i].containsKey(geneName)) {
						pvalue = (String)map_up[i].get(geneName);
					}
					total += new Double(pvalue);
					//System.out.print("\t" + pvalue);
					out.write("\t" + pvalue);
				}
				
				out.write("\t" + (total / (args.length - UserInputNum)) + "\n");
				//System.out.println();
			}
			
			itr2 = gene.keySet().iterator();
			while (itr2.hasNext()) {
				String geneName = (String)itr2.next();
				//System.out.print(geneName);
				out.write(geneName + "\t" + tag_dn);
				double total = 0;
				for (int i = 0; i < args.length - UserInputNum; i++) {
					String pvalue = "1.0";
					if (map_dn[i].containsKey(geneName)) {
						pvalue = (String)map_dn[i].get(geneName);
					}
					out.write("\t" + pvalue);
					total += new Double(pvalue);
				}
				out.write("\t" + (total / (args.length - UserInputNum)) + "\n");
				//System.out.println();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap grabPvalue(String inputFile, String tag) {
		
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (new Double(split[3]) > 1) {
					if (tag.equals(split[0])) {
						map.put(split[1], split[2]);
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
