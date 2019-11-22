package stjude.projects.xiaotuma.aml.rnaseq.fastqsoftlink;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class XiaotuMaAMLGenerateIntakeLabName {

	
	public static void main(String[] args) {
		
		try {
			
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\intake_information\\updated_ids.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap sjids = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\intake_information\\all_SJID.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				sjids.put(str, str);		
				
			}
			in.close();
			
			boolean found = false;
			int count = 0;
			int uniq_count = 0;
			inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\intake_information\\old_id2newid.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String new_name = split[1].replaceAll("_r1.fq.gz",  "").replaceAll("_r2.fq.gz",  "");
				//System.out.println(new_name);
				String hit = "";
				Iterator itr = sjids.keySet().iterator();
				while (itr.hasNext()) {
					String sjid = (String)itr.next();
					boolean sjid_rbs = false;
					boolean new_name_rbs = false;
					boolean sjid_srt = false;
					boolean new_name_srt = false;
					if (sjid.contains("_RBS")) {
						sjid_rbs = true;
					}
					if (new_name.contains("_RBS")) {
						new_name_rbs = true;						
					}
					if (sjid.contains("_srt")) {
						sjid_srt = true;
					}
					if (new_name.contains("_srt")) {
						new_name_srt = true;						
					}					
					if (sjid.contains(new_name) && sjid_rbs == new_name_rbs && sjid_srt == new_name_srt) {
						count++;
						found = true;
						hit = sjid;
						out.write(split[0] + "\t" + split[1] + "\t" + hit + "\n");
					}
				}
				if (found) {
					uniq_count++;
					
				}
			}
			in.close();
			out.close();
			System.out.println(count + "\t" + uniq_count + "\t" + sjids.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
