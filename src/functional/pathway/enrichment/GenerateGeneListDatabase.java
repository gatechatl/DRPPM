package functional.pathway.enrichment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenerateGeneListDatabase {

	public static String parameter_info() {
		return "[directoryPath]";
	}
	public static void execute(String[] args) {
		
		try {
			String directory = args[0];			
			File f = new File(directory);
			File[] files = f.listFiles();
			for (File file: files) {
				
				
				String name = file.getName().replaceAll(".txt",  ""); 
				String path = file.getPath();
				
				int count = 0;
				FileInputStream fstream = new FileInputStream(path);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					count++;
				}
				in.close();
				if (count >= 8) {
					System.out.println(name + "\t" + path);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
