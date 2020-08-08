package rnaseq.mapping.tools.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Generate mapping's input file
 * @author tshaw
 *
 */
public class Fastq2FileList {

	public static String description() {
		return "Generate mapping's input file. Assumes that the second ";
	}
	public static String type() {
		return "RNASEQ";
	}
	public static String parameter_info() {
		return "[inputFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			//String split_str = args[1];
			//int index = new Integer(args[1]);
			String outputFile = args[1];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap mapR1 = new HashMap();
			HashMap mapR2 = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("_");
				String name = "";
				for (int i = 0; i < split.length; i++) {
					if (i != (split.length - 2)) {
						if (name.equals("")) {
							name += split[i];
						} else {
							name += "_" + split[i];
						}
					}
				}
				if (str.contains("R1")) {
					mapR1.put(name, str);
				} else if (str.contains("R2")) {
					mapR2.put(name, str);
				}
			}
			in.close();
			
			Iterator itr = mapR1.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (mapR2.containsKey(sampleName)) {
					String R1_path = (String)mapR1.get(sampleName);
					String R2_path = (String)mapR2.get(sampleName);
					out.write(R1_path + "\t" + R2_path + "\t" + sampleName.replaceAll(".fastq.gz", "").replaceAll(".fastq", "") + "\n");
				}
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
