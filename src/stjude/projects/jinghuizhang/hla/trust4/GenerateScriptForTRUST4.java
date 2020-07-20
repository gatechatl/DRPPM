package stjude.projects.jinghuizhang.hla.trust4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GenerateScriptForTRUST4 {


	public static String description() {
		return "Generate Script for TRUST4.";
	}
	public static String type() {
		return "HLA";
	}
	public static String parameter_info() {
		return "[inputFile] [trust4_path] [bcrtcr_path] [ref_path] [outputShellScript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputBamFilelst = args[0];
			String trust4_path = args[1];
			String bcrtcr_path = args[2];
			String ref_path = args[3];			
			String outputShellScript = args[4];
			
			FileWriter fwriter = new FileWriter(outputShellScript);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputBamFilelst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				File folder = new File(split[0]);
				if (!folder.exists()) {
					folder.mkdir();									
				}				
				out.write("cd " + split[0] + ";" + trust4_path + " -b " + split[1] + " -f " + bcrtcr_path + " --ref " + ref_path + " -o " + split[0] + "  --abnormalUnmapFlag \n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
