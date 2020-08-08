package functional.pathway.enrichr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ConvertEnrichR2GMTPathwayFolder {

	public static String dependencies() {
		return "Require input file.";
	}
	public static String type() {
		return "ENRICHMENT.ENRICHR";
	}
	public static String description() {
		return "Convert the GMT files from EnrichR to a pathway format";
	}
	public static String parameter_info() {
		return "[enrichRfolderPath] [outputFolder: full path] [outputIndexFolder]";
	}
	public static void execute(String[] args) {
		
		try {

			String folderPath = args[0];
			String outputFolder = args[1];
			String outputIndexFolder = args[2];
			File folder = new File(folderPath);
			
			
			for (File file: folder.listFiles()) {
				
				
				String pathway_name = file.getName().replaceAll(".txt", "");
				

				FileWriter fwriter_index = new FileWriter(outputIndexFolder + "/" + pathway_name + ".index.txt");
				BufferedWriter out_index = new BufferedWriter(fwriter_index);				
					
				FileInputStream fstream = new FileInputStream(file.getPath());
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					if (split.length > 1) {
						String name = split[0].replaceAll("-", "_").replaceAll(" ", "_").replaceAll("\\\\", "_").replaceAll("/", "_").replaceAll("~", "_").replaceAll(",", "_").replaceAll("=", "_").replaceAll("\\+", "_").replaceAll("\\(", "_").replaceAll("\\)", "_").replaceAll(":", "_");
						while (name.contains("__")) {
							name = name.replaceAll("__", "_");
						}
						File outputFolderName = new File(outputFolder + "/" + pathway_name);
						if (!outputFolderName.exists()) {
							outputFolderName.mkdir();
						}
						String outputFile = outputFolder + "/" + pathway_name + "/" + name + ".txt";
						
						out_index.write(name + "\t" + outputFile + "\n");
						FileWriter fwriter = new FileWriter(outputFile);
						BufferedWriter out = new BufferedWriter(fwriter);
						out.write(">" + name  + "\n");
						for (int i = 2; i < split.length; i++) {
							out.write(split[i].split(",")[0] + "\n");
						}
						out.close();
					}
				}
				in.close();
				out_index.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
