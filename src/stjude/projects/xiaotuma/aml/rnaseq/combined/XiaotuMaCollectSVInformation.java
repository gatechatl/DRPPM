package stjude.projects.xiaotuma.aml.rnaseq.combined;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class XiaotuMaCollectSVInformation {

	
	public static void main(String[] args) {
		
		try {
			HashMap key = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\Klco\\ComprehensiveAMLTranscriptome\\Combined\\sample_annotation.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 
			String header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				key.put(split[1], split[1]);
			}
			in.close();
			
			HashMap type = new HashMap();
			HashMap map = new HashMap();
			String typeFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\Klco\\ComprehensiveAMLTranscriptome\\Combined\\Xma_fusion_annotation_type.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				type.put(split[0], split[2]);
				map.put(split[0], split[1]);
			}
			in.close();
			
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_References\\PanCancer230_SV_20191108.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 
			header = in.readLine();			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[11].contains("Fusion")) {
					if (split[0].contains("SJAML") || split[0].contains("SJCBF")  || split[0].contains("SJMDS")) {
						
						if (key.containsKey(split[1])) {
							map.put(split[0].split("-")[0].split("\\.")[0], split[1]);
						}
						if (key.containsKey(split[2])) {
							map.put(split[0].split("-")[0].split("\\.")[0], split[2]);
						}
					}
				}
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\xmagrp\\AMLRelapse\\common\\Klco\\ComprehensiveAMLTranscriptome\\Combined\\TCGA_PCGP_sample_annotation.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tFusion\tType\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				sampleName = sampleName.split("\\.")[0];
				String mutgene = "NA";
				String type_str = "NA";
				if (map.containsKey(sampleName)) {
					mutgene = (String)map.get(sampleName);
				}
				if (type.containsKey(sampleName)) {
					type_str = (String)type.get(sampleName);
				}
				out.write(sampleName + "\t" + mutgene + "\t" + type_str + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
