package network.db.compass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CompassGenerateSifFile {

	public static String description() {
		return "COMPASS process data to generate sif file..";
	}
	public static String type() {
		return "NETWORK";
	}
	public static String parameter_info() {
		return "[path] [outputFile]";
	}
	
	public static void execute(String[] args) {
		
		try {
			String path = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			File folder = new File(path);
			for (File f: folder.listFiles()) {
				if (f.getName().contains("tsv")) {
					String name = f.getName().split("_")[2];
					FileInputStream fstream = new FileInputStream(f.getPath());
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					in.readLine();
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						double score = new Double(split[8]);
						String geneName = split[12];
						if (score >= 1.0) {
							out.write(name + "\tconnection\t" + geneName + "\n");
						}
					}
					in.close();
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
