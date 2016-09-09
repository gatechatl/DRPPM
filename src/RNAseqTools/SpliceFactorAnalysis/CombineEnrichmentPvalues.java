package RNAseqTools.SpliceFactorAnalysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CombineEnrichmentPvalues {

	public static String parameter_info() {
		return "[file1] [file2] [file...]";
	}
	public static void execute(String[] args) {
		
		System.out.print("GeneName");
		HashMap[] map = new HashMap[args.length];
		HashMap gene = new HashMap();
		for (int i = 0; i < args.length; i++) {
			map[i] = new HashMap();
			String inputFile = args[i];
			map[i] = grabPvalue(inputFile);
			Iterator itr = map[i].keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				gene.put(geneName, geneName);
			}
			System.out.print("\t" + args[i]);
		}
		System.out.println();
		Iterator itr2 = gene.keySet().iterator();
		while (itr2.hasNext()) {
			String geneName = (String)itr2.next();
			System.out.print(geneName);
			for (int i = 0; i < args.length; i++) {
				String pvalue = (String)map[i].get(geneName);
				System.out.print("\t" + pvalue);
			}
			System.out.println();
		}
		
		
	}
	
	public static HashMap grabPvalue(String inputFile) {
		
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				if (new Double(split[2]) > 1) {
					map.put(split[0], split[1]);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
