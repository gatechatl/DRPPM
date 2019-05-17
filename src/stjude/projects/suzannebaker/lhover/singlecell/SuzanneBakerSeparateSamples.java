package stjude.projects.suzannebaker.lhover.singlecell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class SuzanneBakerSeparateSamples {

	
	public static void main(String[] args) {
		
		try {
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\combined_Suerat\\Combined.cpm.median.txt";
			
			for (int i = 0; i < 16; i++) {
				String outputFile = inputFile + "_" + i + ".txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
			
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();
				String[] split_header = header.split("\t");
				out.write(split_header[0]);
				for (int j = 1; j < split_header.length; j++) {
					if (split_header[j].split("_")[2].equals(i + "")) {
						out.write("\t" + split_header[j]);
					}
				}
				out.write("\n");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					out.write(split[0]);
					for (int j = 1; j < split_header.length; j++) {
						if (split_header[j].split("_")[2].equals(i + "")) {
							out.write("\t" + split[j]);
						}
					}
					out.write("\n");
				}
				in.close();
				out.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
