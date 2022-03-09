package rnaseq.splicing.star;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import misc.CommandLine;

public class STARPostProteinPaintSplicingTrack {


	public static String type() {
		return "Splicing";
	}
	public static String description() {
		return "Takes in the sj.out.tab and output\n";
	}
	public static String parameter_info() {
		return "[STAR SJ file] [output6coltab] [optional: add_chr_flag true/false]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String sj_out_tab_file = args[0];
			String output6coltab = args[1];
			boolean add_chr_flag = false;
			if (args.length > 2) {
				add_chr_flag = new Boolean(args[2]);
			}
			FileWriter fwriter_uniq = new FileWriter(output6coltab + "uniq");
			BufferedWriter out_uniq = new BufferedWriter(fwriter_uniq);
			
			FileWriter fwriter = new FileWriter(output6coltab);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_shell = new FileWriter(output6coltab + ".sh");
			BufferedWriter out_shell = new BufferedWriter(fwriter_shell);
			
			FileInputStream fstream2 = new FileInputStream(sj_out_tab_file);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			while (in2.ready()) {
				String str2 = in2.readLine();
				String[] split = str2.split("\t");
				//String name = split[3];
				String chr = split[0];
				int junction1_position = (new Integer(split[1]) - 1);
				int junction2_position = (new Integer(split[2]) + 1);
				String junction1_direction = "+";
				String junction2_direction = "+";
				if (split[3].equals("2")) {
					junction1_direction = "-";
					junction2_direction = "-";
				}
				String type = "canonical";
				String known = "known";
				if (split[4].equals("0")) {
					type = "non-canonical";
				} else if (split[4].equals("1")) {
					type = "GT-AG";
				} else if (split[4].equals("2")) {
					type = "CT-AC";
				} else if (split[4].equals("3")) {
					type = "GC-AG";
				} else if (split[4].equals("4")) {
					type = "CT-GC";
				} else if (split[4].equals("5")) {
					type = "AT-AC";
 				} else if (split[4].equals("6")) {
					type = "GT-AT";
				}
				if (split[5].equals("0")) {
					known = "novel";
				} else if (split[5].equals("1")) {
					known = "known";
				}
				
				String count_uniq = split[7];
				String count_multi = split[8];
				int total_reads = new Integer(count_uniq) + new Integer(count_multi);
				if (!chr.contains("chr") && add_chr_flag) {
					chr = "chr" + chr;
				}
				if (new Double(count_uniq) > 0) {
					out_uniq.write(chr + "\t" + junction1_position + "\t" + junction2_position + "\t" + junction1_direction + "\t" + type + "_" + known + "\t" + count_uniq + "\n");
				}
				if (total_reads > 0) {
					out.write(chr + "\t" + junction1_position + "\t" + junction2_position + "\t" + junction1_direction + "\t" + type + "_" + known + "\t" + total_reads + "\n");
				}
				
			}
			in2.close();
			
			out.close();
			out_uniq.close();
			
			out_shell.write("sort -k1,1 -k2,2n " + output6coltab + "uniq" + " > " + output6coltab + "uniq" + ".sorted" + "\n");
			out_shell.write("bgzip -f " + output6coltab + "uniq" + ".sorted" + "\n");
			out_shell.write("tabix -f -p bed " + output6coltab + "uniq" + ".sorted.gz" + "\n");
			out_shell.write("sort -k1,1 -k2,2n " + output6coltab + " > " + output6coltab + ".sorted" + "\n");
			out_shell.write("bgzip -f " + output6coltab + ".sorted" + "\n");
			out_shell.write("tabix -f -p bed " + output6coltab + ".sorted.gz" + "\n");
			out_shell.close();
			CommandLine.executeCommand("sh " + output6coltab + ".sh");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
