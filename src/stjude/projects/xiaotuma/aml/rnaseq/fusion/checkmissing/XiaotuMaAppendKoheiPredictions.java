package stjude.projects.xiaotuma.aml.rnaseq.fusion.checkmissing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaAppendKoheiPredictions {

	
	public static void main(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Kohei_Result_Combined\\Koehi_RNAindel_Summary.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String prev = (String)map.get(split[0]);
					if (!prev.contains(split[1])) {
						map.put(split[0], prev + split[1] + ",");
					}
				} else {
					map.put(split[0], split[1] + ",");
				}
			}
			in.close();
			


			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Kohei_Result_Combined\\AML_Wit_RNAindel.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Kohei_Result_Combined\\AML_Without_RNAindel.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			out.write(header + "\tRNAindel\n");
			while (in.ready()) {
				String str = in.readLine();
				out.write(str);
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String result = (String)map.get(split[0]);
					out.write("\t" + result + "\n");
				} else {
					out.write("\tNA\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
