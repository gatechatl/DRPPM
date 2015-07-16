package Metagenomic.Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GrabEachFileInsertIDFasta {

	public static void execute(String[] args) {
		
		try {
			
			String inputFolder = args[0];
			String tag = args[1];
			String outputFile = args[2];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			File[] files = new File(inputFolder).listFiles();
			for (File file: files) {
				String fileName = file.getPath();
				int len = fileName.length();
				String name = file.getName();
				String prefix = name.split("_")[0];
				String tail = fileName.substring(len - tag.length(), len);
				//System.out.println(tail);
				if (tail.equals(tag)) {
					int index = 1;
					FileInputStream fstream = new FileInputStream(fileName);
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						if (str.contains(">")) {
							str = str.replaceAll(">", ">ID" + prefix + "_" + index + " " + index + " ");
							out.write(str + "\n");
							index++;
						} else {
							out.write(str + "\n");
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
