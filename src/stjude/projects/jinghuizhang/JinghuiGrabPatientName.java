package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiGrabPatientName {

	
	public static void main(String[] args) {
		
		try {
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\tumormap\\sample_info\\processed\\ts_processed_synonyms.txt";
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\tumormap\\sample_info\\processed\\synonyms.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String tbid = "";
				if (str.contains("'TBID', ")) {
					tbid = str.split("'TBID', '")[1].split("'")[0];
				}
				String SJHBB = "";
				String id = str.split("', '")[0].replaceAll("\\[\\('", "");
				if (str.contains("'SJUID', '")) {
					SJHBB = str.split("'SJUID', '")[1].split("'\\)")[0];
					
										
					System.out.println(str + "\t" + id + "\t" + SJHBB + "\t" + tbid);
					
				}
				out.write(str + "\t" + id + "\t" + SJHBB + "\t" + tbid + "\t" + "end" + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
