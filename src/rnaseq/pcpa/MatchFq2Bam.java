package rnaseq.pcpa;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * A MISC program for matching bam to fastq files for PCPA 
 * @author tshaw
 *
 */
public class MatchFq2Bam {

	public static String description() {
		return "A program for matching bam to fastq files for PCPA";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[fqFileList] [filePathOfBam]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String filePath = args[1];
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str.replaceAll(".fq", ""), "");
			}
			
			File files = new File(filePath);
			for (File file: files.listFiles()) {
				if (file.getName().contains(".bam") && !file.getName().contains(".bai") && !file.getName().contains(".md5")) {
					String[] split = file.getName().split("_");
					if (map.containsKey(split[0])) {
						System.out.println(split[0]+ ".fq\t" + filePath + "/" + file.getName());
						//map.put(split[0], file.getName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
