package stjude.projects.xiaotuma.aml.rnaseq.qc.checknaming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XiaotuMaAttachTARGET2SJID {

	
	public static void main(String[] args) {
		
		try {
			
			
			HashMap sjid2targetid = new HashMap();
			String inputFileSize = "Z:\\ResearchHome\\ProjectSpace\\xmagrp\\AMLRelapse\\common\\FredHutch\\runtable\\tshaw_runtable\\from_xiaotu\\SJID2TARGETID_20200117.txt";
			FileInputStream fstream = new FileInputStream(inputFileSize);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				sjid2targetid.put(split[0], split[1]);
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_mapping_stats\\TARGETID_AMLFredHutch_RNAseq_Mapping_Stats.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String mapping_stats_file = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\FredHutch\\rnaseq_mapping_stats\\AMLFredHutch_RNAseq_Mapping_Stats.txt";
			fstream = new FileInputStream(mapping_stats_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();
			out.write("TARGET\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (sjid2targetid.containsKey(split[0])) {
					String target = (String)sjid2targetid.get(split[0]);
					out.write(target + "\t" + str + "\n");
				}
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
