package rnaseq.mapping.tools.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
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
public class Fastq2FileListFlex {

	public static String description() {
		return "Generate mapping's input file";
	}
	public static String type() {
		return "RNASEQ";
	}
	public static String parameter_info() {
		return "[inputFile] [index of R1 or R2 split by \"_\"] [outputFile]";
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
				File f = new File(str);
				if (f.isFile()) {
					String[] split = f.getName().split("_");
					String name = "";
					if (split.length >= 2) {
						name = split[0] + "_" + split[1].split("\\.")[0];						
						if (str.contains("R1.fastq") || str.contains("R1.fq") || str.contains("R1.fastq.gz") || str.contains("R1.fq.gz") || str.contains("r1.fastq") || str.contains("r1.fq") || str.contains("r1.fastq.gz") || str.contains("r1.fq.gz")) {
							mapR1.put(name, str);
						} else if (str.contains("R2.fastq") || str.contains("R2.fq") || str.contains("R2.fastq.gz") || str.contains("R2.fq.gz") || str.contains("r2.fastq") || str.contains("r2.fq") || str.contains("r2.fastq.gz") || str.contains("r2.fq.gz")) {
							mapR2.put(name, str);
						}
					}
				}
			}
			in.close();
			
			Iterator itr = mapR1.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				if (mapR2.containsKey(sampleName)) {
					String R1_path = (String)mapR1.get(sampleName);
					String R2_path = (String)mapR2.get(sampleName);
					out.write(R1_path + "\t" + R2_path + "\t" + sampleName + "\n");
				}
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
