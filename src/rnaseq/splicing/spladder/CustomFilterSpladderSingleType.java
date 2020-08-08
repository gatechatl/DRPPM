package rnaseq.splicing.spladder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Filter Spladder files
 * @author tshaw
 *
 */
public class CustomFilterSpladderSingleType {

	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Filter the outputfiles for spladder.";
	}
	public static String parameter_info() {
		return "[inputSpladderFile] [cutoff: At least one sample type with 0.5] [outputSpladderFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap index2cancerType = new HashMap();
			HashMap cancerTypeCount = new HashMap();
			String inputFile = args[0];
			double cutoff = new Double(args[1]);
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");
			for (int i = 3; i < split_header.length; i++) {
				String cancerType = split_header[i].split("1")[0].split("2")[0].split("3")[0].split("4")[0].split("5")[0].split("6")[0].split("7")[0].split("8")[0].split("9")[0];
				index2cancerType.put(i, cancerType);
				if (cancerTypeCount.containsKey(cancerType)) {
					int count = (Integer)cancerTypeCount.get(cancerType);
					count = count + 1;
					cancerTypeCount.put(cancerType, count);
				} else {
					cancerTypeCount.put(cancerType, 1);
				}
			}
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				HashMap cancerTypeNAs = new HashMap();
				for (int i = 3; i < split.length; i++) {
					String cancerType = (String)index2cancerType.get(i);
					if (!(split[i].equals("NA") || split[i].equals("nan"))) {
						if (cancerTypeNAs.containsKey(cancerType)) {
							int count = (Integer)cancerTypeNAs.get(cancerType);
							count = count + 1;
							cancerTypeNAs.put(cancerType,  count);
						} else {
							cancerTypeNAs.put(cancerType,  1);
						}
					}
				}
				boolean pass = false;
				Iterator itr = cancerTypeCount.keySet().iterator();
				while (itr.hasNext()) {
					String cancerType = (String)itr.next();
					
					double total_count = (Integer)cancerTypeCount.get(cancerType);
					double hit = 0.0;
					if (cancerTypeNAs.containsKey(cancerType)) {
						hit = (Integer)cancerTypeNAs.get(cancerType);
					}
					if (hit / total_count > cutoff) {
						pass = true;
					}
				}
				if (pass) {
					out.write(str + "\n");
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
