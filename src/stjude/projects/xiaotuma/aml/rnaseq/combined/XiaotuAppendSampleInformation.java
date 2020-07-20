package stjude.projects.xiaotuma.aml.rnaseq.combined;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuAppendSampleInformation {

	
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
				
				map.put(split[0], split[1].replaceAll("_srt", ""));
				System.out.println(split[0] + "\t" + split[1]);
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\Klco\\ComprehensiveAMLTranscriptome\\Combined\\Xma_fusion_annotation_type.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            
			String annotationFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\Klco\\ComprehensiveAMLTranscriptome\\Combined\\Xma_fusion_annotation.txt";
			fstream = new FileInputStream(annotationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tType\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = "NA";
				if (map.containsKey(split[0])) {
					String target_name = (String)map.get(split[0]);
					if (target_name.split("-")[3].equals("09A") || target_name.split("-")[3].equals("40A")) {
						type = "Bone_Marrow";
					}
					
					if (target_name.split("-")[3].equals("03A") || target_name.split("-")[3].equals("04A")) {
						type = "Peripheral_Blood";
					}
					
					if (split[0].contains("SJNORM")) {
						type = "Normal";
					}
				} else {
					System.out.println(split[0]);
				}
				out.write(str + "\t" + type + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e ) { 
			e.printStackTrace();
		}
	}
}
