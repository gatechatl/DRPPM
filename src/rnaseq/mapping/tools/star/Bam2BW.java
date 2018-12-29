package rnaseq.mapping.tools.star;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Bam2BW {


	public static String type() {
		return "RNASEQMAPPING";
	}
	public static String description() {
		return "Convert bam to bw";
	}
	public static String parameter_info() {
		return "[inputBamFileList]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String bamfileList = args[0];
			FileInputStream fstream = new FileInputStream(bamfileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				System.out.println("bamCoverage -b " + str + " -o " + str + ".bw");
				
			}
			in.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
