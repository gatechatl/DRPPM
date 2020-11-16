package rnaseq.splicing.alternativejuncpipeline.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JuncSalvagerGenerateBarplotData {


	public static String description() {
		return "Takes the output from JuncSalvagerSplitMatrixCandidates and generate barplot data for the barplot html.";
	}
	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String parameter_info() {
		return "[inputFileLst: sampleName[tab]quartile.txt] [exon name] [outputDataMatrix]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFileList = args[0];
			String exonName = args[1];
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\tFourth_Quartile\tThird_Quartile\tSecond_Quartile\tFirst_Quartile\n");
			FileInputStream fstream = new FileInputStream(inputFileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				HashMap map = new HashMap();
				map.put("1st_Quartile", 0);
				map.put("2nd_Quartile", 0);
				map.put("3rd_Quartile", 0);
				map.put("4th_Quartile", 0);
				
				FileInputStream fstream2 = new FileInputStream(split[1]);
				DataInputStream din2 = new DataInputStream(fstream2);
				BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
				while (in2.ready()) {
					String str2 = in2.readLine();
					String[] split2 = str2.split("\t");
					if (split2[0].equals(exonName)) {
						for (int i = 1; i < split2.length; i++) {
							if (split2[i].equals("0")) {
								int count = (Integer)map.get("1st_Quartile");
								count++;
								map.put("1st_Quartile", count);
							} else if (split2[i].equals("1")) {
								int count = (Integer)map.get("2nd_Quartile");
								count++;
								map.put("2nd_Quartile", count);
							} else if (split2[i].equals("2")) {
								int count = (Integer)map.get("3rd_Quartile");
								count++;
								map.put("3rd_Quartile", count);
							} else if (split2[i].equals("3")) {
								int count = (Integer)map.get("4th_Quartile");
								count++;
								map.put("4th_Quartile", count);
							}
						}
					}
				}
				in2.close();
				int count_1st_quartile = (Integer)map.get("1st_Quartile");
				int count_2nd_quartile = (Integer)map.get("2nd_Quartile");
				int count_3rd_quartile = (Integer)map.get("3rd_Quartile");
				int count_4th_quartile = (Integer)map.get("4th_Quartile");
				out.write(split[0] + "\t" + count_4th_quartile + "\t" + count_3rd_quartile + "\t" + count_2nd_quartile + "\t" + count_1st_quartile + "\n");
				
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
