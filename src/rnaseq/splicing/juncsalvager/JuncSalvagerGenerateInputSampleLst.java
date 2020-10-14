package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Load St Jude bam files, output the input file 
 * @author gatechatl
 *
 */
public class JuncSalvagerGenerateInputSampleLst {

	public static String description() {
		return "Generate a three column sample lst containing sample name, bam file, and STAR sj.tab.";
	}
	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String parameter_info() {
		return "[inputBamLst] [outputJuncSalvagerBamLst]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBamFilelst = args[0];
			String outputJuncSalvagerBamLst = args[1];
			
			FileWriter fwriter = new FileWriter(outputJuncSalvagerBamLst);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputBamFilelst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				File file = new File(str);
				String name = file.getName().replaceAll(".STAR.Aligned.sortedByCoord.out.bam", "");
				name = name.replaceAll(".Aligned.sortedByCoord.out.bam", "");
				String bam = str;
				String sj_out_tab = file.getName().replaceAll(".STAR.Aligned.sortedByCoord.out.bam", ".SJ.out.tab");
				sj_out_tab = sj_out_tab.replaceAll(".Aligned.sortedByCoord.out.bam", ".SJ.out.tab");
				out.write(name + "\t" + str + "\t" + sj_out_tab + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
