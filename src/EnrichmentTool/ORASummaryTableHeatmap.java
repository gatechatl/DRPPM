package EnrichmentTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate a comprehensive table for the ORA result
 * @author tshaw
 *
 */
public class ORASummaryTableHeatmap {

	public static String description() {
		return "Generate a comprehensive table for the ORA result in heatmap version";
	}
	public static String type() {
		return "ENRICHMENT";
	}
	public static String parameter_info() {
		return "[inputFileList] [sampleList] [fdr_cutoff PVALUE:0.05/BONFERONNI:0.05/BH:0.05/HOCHBERG:0.05] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileList = args[0];
			String sampleList = args[1];
			String flag = args[2].split(":")[0];
			double cutoff = new Double(args[2].split(":")[1]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			boolean pvalue_flag = false;
			boolean bh_flag = false;
			boolean bon_flag = false;
			boolean hoch_flag = false;
			if (flag.equals("PVALUE")) {
				pvalue_flag = true;
			}
			if (flag.equals("BH")) {
				bh_flag = true;
			}
			if (flag.equals("BONFERRONI")) {
				bon_flag = true;
			}
			if (flag.equals("HOCHBERG")) {
				hoch_flag = true;
			}
			
			LinkedList list = new LinkedList();
			for (int i = 0; i < sampleList.split(",").length; i++) {
				list.add(sampleList.split(",")[i]);
			}
			
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String type = "";
				if (str.contains("GOBP")) {
					type = "GO:BP";
				}
				if (str.contains("GOCC")) {
					type = "GO:CC";
				}
				if (str.contains("GOMF")) {
					type = "GO:MF";
				}
				
				
				File f = new File(str);
				if (f.exists()) {
					FileInputStream fstream2 = new FileInputStream(str);
					DataInputStream din2 = new DataInputStream(fstream2);
					BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
					in2.readLine();
					while (in2.ready()) {
						String str2 = in2.readLine();						
						String[] split = str2.split("\t");
						if (type.equals("")) {
							split[1] = split[1];
						} else {
							split[1] = type + "_" + split[1];
						}
						double pvalue = new Double(split[2]);
						double enrichment = new Double(split[3]);
						double bh = new Double(split[9]);
						double bon = new Double(split[10]);
						double hoch = new Double(split[11]);
						if (enrichment > 1 && ((bh <= cutoff && bh_flag) || (bon <= cutoff && bon_flag) || (hoch <= cutoff && hoch_flag) || (pvalue < cutoff && pvalue_flag))) {
							Iterator itr3 = list.iterator();
							while (itr3.hasNext()) {
								String clusterName = (String)itr3.next();
								if (split[0].equals(clusterName)) {
									if (map.containsKey(split[1])) {
										HashMap samples = (HashMap)map.get(split[1]);
										samples.put(split[0], pvalue);
										map.put(split[1], samples);
									} else {
										HashMap samples = new HashMap();
										samples.put(split[0], pvalue);
										map.put(split[1], samples);
									}
								}
							}
						}
					}
					in2.close();
				}
			}
			in.close();			
			
			out.write("Pathway");
			Iterator itr2 = list.iterator();
			while (itr2.hasNext()) {
				String sample = (String)itr2.next();
				out.write("\t" + sample);
			}
			out.write("\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String pathway = (String)itr.next();
				out.write(pathway);
				HashMap samples = (HashMap)map.get(pathway);
				Iterator itr3 = list.iterator();
				while (itr3.hasNext()) {
					String sample = (String)itr3.next();
					if (samples.containsKey(sample)) {
						double pvalue = (Double)samples.get(sample);
						
						out.write("\t" + pvalue);
					} else {
						out.write("\t1.0");
					}
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
