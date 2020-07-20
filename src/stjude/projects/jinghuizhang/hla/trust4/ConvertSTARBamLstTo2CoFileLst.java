package stjude.projects.jinghuizhang.hla.trust4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ConvertSTARBamLstTo2CoFileLst {


	public static String description() {
		return "Generate two column file list from bam";
	}
	public static String type() {
		return "MISC";
	}
	public static String parameter_info() {
		return "[inputBamLstFile] [outputTwoColFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBamFilelst = args[0];
			String outputTwoColumnFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputTwoColumnFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputBamFilelst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				File file = new File(str);
				String name = file.getName().replaceAll(".STAR.Aligned.sortedByCoord.out.bam", "");
				out.write(name + "\t" + str + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
