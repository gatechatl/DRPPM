package rnaseq.splicing.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class STARPostProteinPaintSplicingTrackGenerateScript {


	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Takes in a list of sj.out.tab and generate a shell script for execution\n";
	}
	public static String parameter_info() {
		return "[STAR SJ file Lst] [shell script] [boolean: add chr]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileLst = args[0];
			String shell_script = args[1];
			String add_chr_flag = args[2];
			String add_chr_flag_str = "false";
			if (add_chr_flag.equalsIgnoreCase("TRUE") || add_chr_flag.equalsIgnoreCase("YES")) {
				add_chr_flag_str = "true";
			}

			FileWriter fwriter = new FileWriter(shell_script);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().trim();
				String[] split = str.split("\t");
				out.write("drppm -STARPostProteinPaintSplicingTrack " + split[0] + " " + split[0] + ".pptab " + add_chr_flag_str + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
