package stjude.projects.xiaotuma.aml.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaConvertSampleName2SJID {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\SJID2TARGETID_20200117.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1], split[0]);
				String name = split[1].replaceAll("TARGET-00-", "");
				name = name.replaceAll("TARGET-20-", "");
				name = name.replaceAll("_RBS", "");
				name = name.replaceAll("_srt", "");
				map.put(name, split[0]);
				
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_analysis\\TRUST4\\FredHutch_AML\\AML_TCR_BCR_TRUST4.output_mod_SJID.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			// 
			inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_analysis\\TRUST4\\FredHutch_AML\\AML_TCR_BCR_TRUST4.output_mod.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].substring(0, 2).equals("BM")) {
					split[0] = split[0].replaceAll("09A", "14A");
				}
				if (split[0].substring(0, 3).equals("RO0")) {
					split[0] = split[0].replaceAll("09A", "14A");
				}
				if (!map.containsKey(split[0])) {
					
					System.out.println(split[0]);
				}
				out.write((String)map.get(split[0]));
				for (int i = 1; i < split.length; i++) {
					out.write("\t" + split[i]);
				}
				out.write("\n");
			}
			out.close();
			
			
		} catch (Exception e ){
			e.printStackTrace();
		}
	}
}
