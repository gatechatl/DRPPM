package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class High20ToTHETA {


	public static String description() {
		return "Convert High20 File to THETA snp friendly files";
	}
	public static String type() {
		return "THETA";
	}
	public static String parameter_info() {
		return "[inputFolder] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			String outputFolder = args[1]; //"\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\high20s\\THETA";
			String inputFolder = args[0]; //"\\\\gsc.stjude.org\\project_space\\leventgrp\\LeventakiSpliceCell\\common\\CMPB\\BioinfoCore\\Exome\\high20s\\copy\\";
			File f = new File(inputFolder);
			for (File file: f.listFiles()) {
				
				String fileName = file.getName();
				System.out.println(file.getPath());
				String[] split_fileName = fileName.split("_");
				String outputTumorFile = outputFolder + "/" + split_fileName[0] + "_" + split_fileName[1] + ".tumor.snp_formatted.txt";				
				FileWriter fwriter_tumor = new FileWriter(outputTumorFile);
				BufferedWriter out_tumor = new BufferedWriter(fwriter_tumor);	
				
				String outputNormalFile = outputFolder + "/" + split_fileName[0] + "_" + split_fileName[1] + ".normal.snp_formatted.txt";
				FileWriter fwriter_normal = new FileWriter(outputNormalFile);
				BufferedWriter out_normal = new BufferedWriter(fwriter_normal);	
				
				out_tumor.write("#Chrm\tPos\tRef_Allele\tMut_Allele\n");
				out_normal.write("#Chrm\tPos\tRef_Allele\tMut_Allele\n");
				
				FileInputStream fstream = new FileInputStream(file.getPath());
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();								
				String[] header_split = header.split("\t");
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					//System.out.println(split[34]);
					if (!split[34].trim().equals("")) {
						out_tumor.write(split[3] + "\t" + split[4] + "\t" + split[15] + "\t" + split[17] + "\n");
						out_normal.write(split[3] + "\t" + split[4] + "\t" + split[14] + "\t" + split[16] + "\n");
					}
				}
				in.close();
				out_tumor.close();
				out_normal.close();
				System.out.println("Written File: " + outputTumorFile);
				System.out.println("Written File: " + outputNormalFile);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
