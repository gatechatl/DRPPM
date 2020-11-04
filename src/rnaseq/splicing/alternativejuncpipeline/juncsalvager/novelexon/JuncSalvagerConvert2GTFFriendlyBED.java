package rnaseq.splicing.alternativejuncpipeline.juncsalvager.novelexon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JuncSalvagerConvert2GTFFriendlyBED {


	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Convert bed file from JuncSalvager with a full name.";
	}
	public static String parameter_info() {
		return "[inputBEDFile] [outputBEDFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBEDFile = args[0];
			String outputBEDFile = args[1];			

			FileWriter fwriter = new FileWriter(outputBEDFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputBEDFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				out.write(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + ":" + split[0] + ":" + split[1] + ":" + split[2] + "\t" + split[4] + "\t" + split[5] + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
