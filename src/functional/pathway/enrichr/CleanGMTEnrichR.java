package functional.pathway.enrichr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CleanGMTEnrichR {

	public static String dependencies() {
		return "Require input file.";
	}
	public static String type() {
		return "ENRICHMENT.ENRICHR";
	}
	public static String description() {
		return "Clean up the extra score that ENRICHR added";
	}
	public static String parameter_info() {
		return "[enrichRfolderPath] [outputFolder: full path]";
	}
	public static void execute(String[] args) {
		
		try {

			String folderPath = args[0];
			String outputFolder = args[1];
			File folder = new File(folderPath);
			
			
			for (File file: folder.listFiles()) {
								
				String pathway_name = file.getName().replaceAll(".txt", "");
				FileWriter fwriter = new FileWriter(outputFolder + "/" + file.getName() + ".gmt");
				BufferedWriter out = new BufferedWriter(fwriter);
				
				System.out.println(file.getPath());
				FileInputStream fstream = new FileInputStream(file.getPath());
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine().trim();
					String[] split = str.split("\t");
					
					
					if (split.length > 1) {
						out.write(split[0] + "\t" + split[0]);
						for (int i = 1; i < split.length; i++) {
							if (!split[i].equals("")) {
								out.write("\t" + split[i].split(",")[0].trim());
							}
						}
						out.write("\n");
					}
					
				
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
