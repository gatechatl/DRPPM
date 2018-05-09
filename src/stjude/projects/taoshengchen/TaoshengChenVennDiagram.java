package stjude.projects.taoshengchen;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class TaoshengChenVennDiagram {


	public static String description() {
		return "Create venn diagram.";
	}
	public static String type() {
		return "TaoshengCehn";
	}
	public static String parameter_info() {
		return "[inputFile] [log2FCs: 1,2,3] [pvals: 2,4,5]";
	}
	public static void execute(String[] args) {
		try {
			
			String inputFile = args[0];
			String[] log2FCs = args[1].split(",");
			String[] pvals = args[2].split(",");
			
			if (log2FCs.length != pvals.length) {
				System.out.println("log2FCs is not the same as pvals");
				System.exit(0);
			}
			
			int j = 1;
			HashMap map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] split_header = in.readLine().replaceAll("-", "").split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 0; i < log2FCs.length; i++) {
					if (new Double(split[new Integer(log2FCs[i])]) > 0 && new Double(split[new Integer(pvals[i])]) < 0.01) {
						if (map.containsKey("A" + i)) {
							String prev = (String)map.get("A" + i) + "," + j;
							map.put("A" + i, prev);
						} else {
							map.put("A" + i, j + "");
						}
					}
				}
				j++;
			}
			in.close();
			
			System.out.println("library(gplots);");
			String val = "venn(list(";
			boolean first = false;
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				String value = (String)map.get(key);
				if (!first) {
					val += key + " = c(" + value + ")";
				} else {
					val += "," + key + " = c(" + value + ")";
				}
				first = true;
			}
			val += "))";
			System.out.println(val);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
