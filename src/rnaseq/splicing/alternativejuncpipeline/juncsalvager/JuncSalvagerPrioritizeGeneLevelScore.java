package rnaseq.splicing.alternativejuncpipeline.juncsalvager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JuncSalvagerPrioritizeGeneLevelScore {

	
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			
			String inputFile = args[0]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_full_exon_analysis\\solid_brain_exon_meta_analysis_matrixDB_proteomics_20200726.txt";
			String cutoff_gtex = args[1]; // 999,3,0,0
			String cutoff_disease = args[1]; // 0,0,0,1
			String outputFile = args[2]; //"\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_full_exon_analysis\\GeneLevel_solid_brain_exon_meta_analysis_matrixDB_proteomics_20200726.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write("GeneName\t" + header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("_")[0];
				double score = new Double(split[1]);
				if (new Integer(split[9]) > 0 || new Integer(split[10]) > 0) {
					if (map.containsKey(geneName)) {
						String line = (String)map.get(geneName);
						String[] split_line = line.split("\t");
						 
						if (score > new Double(split_line[1])) {
							map.put(geneName, str);
						}
					
					} else {
						map.put(geneName, str);
					}
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String line = (String)map.get(geneName);
				out.write(geneName + "\t" + line + "\n");
			}
			out.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
