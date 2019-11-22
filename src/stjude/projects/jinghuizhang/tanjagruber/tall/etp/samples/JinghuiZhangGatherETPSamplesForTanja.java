package stjude.projects.jinghuizhang.tanjagruber.tall.etp.samples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangGatherETPSamplesForTanja {

	
	public static void main(String[] args) {
		
		try {
			String[] samples = {"PATMYZ","PATBGC","PATGZA","PATKYI","PATYJK","PASWFN","PATHJF","PATMAF","PATHBG","PATFWF","PATGYH","PATCKV","PARNJB","PASFLK","PAUCDY","PATFRM","PATXKW","PARTLY","PASJJR"};
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\TARGET_Clinical_Info_ForTanja.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			int count = 0;
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\USP7\\common\\TARGET_TALL\\metadata\\TARGET_Clinical_Info.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			//out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].contains("TARGET")) {
					boolean found = false;
					for (String sample: samples) {
						if (split[0].split("-")[2].equals(sample)) {
							found = true;
						}
					}
					if (found) {
						out.write(str + "\n");
						count++;
					}
				}
			}
			in.close();
			out.close();
			System.out.println(samples.length + "\t" + count);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
