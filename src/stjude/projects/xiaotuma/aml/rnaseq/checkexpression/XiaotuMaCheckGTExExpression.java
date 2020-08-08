package stjude.projects.xiaotuma.aml.rnaseq.checkexpression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaCheckGTExExpression {


	public static String type() {
		return "XIAOTUMA";
	}
	public static String parameter_info() {
		return "[sam files] [gtex_annotation] [outputFile]";
	}
	public static String description() {
		return "Convert the gtex sam files and";
	}
	public static void execute(String[] args) {
		
		try {
			
			String check_expression_list = args[0];
			String gtex_annotation_file = args[1];
			String outputFile = args[2];
			
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("sample\tBodySite\tHistology\tread_count\n");
			HashMap gtex_annotation = new HashMap();
			FileInputStream fstream = new FileInputStream(gtex_annotation_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String body_site = split[9];
				String histology = split[17];
				body_site = body_site.replaceAll("-", "_").replaceAll(" ", "_").replaceAll("\\(", "_").replaceAll("\\)", "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");
				body_site = body_site.replaceAll("__",  "_");		
				
				gtex_annotation.put(split[0], body_site + "\t" + histology);
			}
			in.close();
			
			fstream = new FileInputStream(check_expression_list);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			while (in.ready()) {
				String str = in.readLine();
				
				int lines = 0;
				File f = new File(str);
				String sampleName = f.getName().split("\\.")[0];
				FileInputStream fstream2 = new FileInputStream(str);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2)); 
				while (in2.ready()) {
					String str2 = in2.readLine();
					lines++;
				}
				System.out.println(sampleName + "\t" + gtex_annotation.get(sampleName) + "\t" + lines);
				out.write(sampleName + "\t" + gtex_annotation.get(sampleName) + "\t" + lines + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
