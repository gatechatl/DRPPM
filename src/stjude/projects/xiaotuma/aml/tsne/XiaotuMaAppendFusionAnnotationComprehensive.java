package stjude.projects.xiaotuma.aml.tsne;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaAppendFusionAnnotationComprehensive {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\fusion\\Tim_Manual_Checking\\AML_CICERO_Manual_Check_List_Clean_Fusion_TARGETID_20200302.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[1], str);
			}
			in.close();
				
			String[] tags = {"per20", "per30", "per40", "per50"};
			//String[] tags = {"per20"};
			for (String tag: tags) {
				String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_analysis\\tsne\\FredHutch_AML_TSNE_output_" + tag + "_annotated.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				
				// TARGET_ID	SampleName	Major_Gene	FLT3	Major_Event	Secondary_Event	Fusion	Indel
	
				out.write("SampleName\tTSNE_1\tTSNE_2\tTARGET_ID\tSJID\tMajor_Gene\tFLT3_ITD\tMajor_Event\t_Secondary_Event\tIndel\tAllEvent\n");
				// 
				inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_analysis\\tsne\\FredHutch_AML_TSNE_output_" + tag + ".txt";
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din)); 
				header = in.readLine();			
				while (in.ready()) {
					String str = in.readLine().replaceAll("\"", "");
					String[] split = str.split("\t");
					if (map.containsKey(split[1])) {
						out.write(split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + map.get(split[1]) + "\n");
					} else {
					
						out.write(split[1] + "\t" + split[2] + "\t" + split[3] + "\tNA\tNA\tNA\tNA\tNA\tNA\tNA\tNA\n");
					}
				}
				in.close();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
