package functional.pathway.enrichment;

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
public class ORASummaryTable {

	public static String description() {
		return "Generate a comprehensive table for the ORA result";
	}
	public static String type() {
		return "ENRICHMENT";
	}
	public static String parameter_info() {
		return "[inputFileList] [sampleList] [fdr_cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileList = args[0];
			String sampleList = args[1];
			double fdr_cutoff = new Double(args[2]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
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
						split[1] = type + "_" + split[1];
						double enrichment = new Double(split[3]);
						double fdr = new Double(split[9]);
						if (enrichment > 1 && fdr <= fdr_cutoff) {
							Iterator itr3 = list.iterator();
							while (itr3.hasNext()) {
								String clusterName = (String)itr3.next();
								if (split[0].equals(clusterName)) {
									if (map.containsKey(split[1])) {
										LinkedList samples = (LinkedList)map.get(split[1]);
										samples.add(split[0]);
										map.put(split[1], samples);
									} else {
										LinkedList samples = new LinkedList();
										samples.add(split[0]);
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
				LinkedList samples = (LinkedList)map.get(pathway);
				Iterator itr3 = list.iterator();
				while (itr3.hasNext()) {
					String sample = (String)itr3.next();
					if (samples.contains(sample)) {
						out.write("\t1");
					} else {
						out.write("\t0");
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
