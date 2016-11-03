package genomics.openreadingframe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GenerateLowComplexityDomainInfo {

	
	public static String type() {
		return "LCR";
	}
	public static String description() {
		return "Generate the low complexity information based on Xin Zhou's requirement";
	}
	public static String parameter_info() {
		return "[inputLowComplexityPredictedFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			/*
refseq varchar(30) not null,
name text null,
description text null,
start integer not null,
stop integer not null,
key1 varchar(100) null,
reference text null*/
			String inputLowComplexityFile = args[0];
			String outputFile = args[1];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			FileInputStream fstream = new FileInputStream(inputLowComplexityFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					str = str.replaceAll(">", "");
					String refseq = str.split(":")[0];
					String refseq_rename = refseq.split("_")[0] + "_" + refseq.split("_")[1];
					String name = refseq_rename + " " + refseq.split("_")[2]; 
					String coord = str.split(":")[1].split(" ")[0];
					int startCoordOrf = new Integer(coord.split("\\(")[0]);
					int startCoordLCR = (new Integer(coord.split("\\(")[1].split("-")[0]) - 1) * 3;
					int endCoordLCR = new Integer(coord.split("\\(")[1].split("-")[1].replaceAll("\\)", "")) * 3;
					String final_str = refseq_rename + "\tLCR\tseg predicted Low Complexity Region\t" + (startCoordOrf + startCoordLCR) + "\t" + (startCoordOrf + endCoordLCR) + "\tseg prediction\tseg prediction";
					//System.out.println(final_str);
					out.write(final_str + "\n");
				} else {
					
				}
			}
			in.close();
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
