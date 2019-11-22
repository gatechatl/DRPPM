package stjude.projects.jinghuizhang.dexseq.exon.annotation.pcgptarget;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangGenerateAppendedSampleInformation {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap sampleid2type = new HashMap();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_TARGET_RNAseq_Analysis\\sample_type_metadata_appended.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String target_info_file = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_TARGET_RNAseq_Analysis\\pan_target_sample_info.update_Nov212016_OSbadAdded_update04062018_forPatee_TSedited.txt";
			FileInputStream fstream = new FileInputStream(target_info_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 11) {
					if (!split[11].equals("")) {
						System.out.println(str);
						sampleid2type.put(split[11], split[0]);
					}				
				}
			}
			in.close();
			
			HashMap sample_info = new HashMap();
			HashMap existing_sample_name = new HashMap();
			String sampleMetaInformation = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_TARGET_RNAseq_Analysis\\sample_type_metadata.txt";
			fstream = new FileInputStream(sampleMetaInformation);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				out.write(str + "\n");
				String[] split = str.split("\t");
				
				existing_sample_name.put(split[4], split[4]);
				sample_info.put(split[3], split[0] + "\t" + split[1] + "\t" + split[2]);
			}
			in.close();
			
			Iterator itr = sampleid2type.keySet().iterator();
			while (itr.hasNext()) {
				String sampleid = (String)itr.next();
				String diagnosis_short = (String)sampleid2type.get(sampleid);
				String sample_type = "NA";
				if (sampleid.contains("_D")) {
					sample_type = "DIAGNOSIS";
				} else if (sampleid.contains("_R")) {
					sample_type = "RELAPSE";
				} else if (sampleid.contains("_M")) {
					sample_type = "METASTATIC";
				} else if (sampleid.contains("_E")) {
					sample_type = "DIAGNOSIS";
				}
				if (!existing_sample_name.containsKey(sampleid)) {
					if (sample_info.containsKey(diagnosis_short)) {
						String meta = (String)sample_info.get(diagnosis_short);
						out.write(meta + "\t" + diagnosis_short + "\t" + sampleid + "\t" + sample_type + "\n");
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
