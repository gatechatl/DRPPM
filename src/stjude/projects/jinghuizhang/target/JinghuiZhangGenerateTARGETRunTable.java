package stjude.projects.jinghuizhang.target;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 * Automated script to generate the run table for TARGET
 * @author tshaw
 *
 */
public class JinghuiZhangGenerateTARGETRunTable {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap uniq = new HashMap();
			HashMap sjid2targetid = new HashMap();
			HashMap targetid2sjid = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PAN_PCGP_TARGET_Genomic_Lesion_DB\\PanTARGET\\temp_patient_id_conversion.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length >= 1) {
					System.out.println(split[1]);
					if (!split[1].equals("nobam")) {
						sjid2targetid.put(split[2], split[1]);
						sjid2targetid.put(split[3], split[1]);
						sjid2targetid.put(split[4], split[1]);
						String sjids = "";
						boolean found = false;
						if (!split[2].equals("nosjid") && !split[2].equals("-1")) {
							targetid2sjid.put(split[1], split[2]);
							if (!sjids.contains(split[2])) {
								sjids += split[2] + ",";
							}
							found = true;
						}
						if (!split[3].equals("nosjid")  && !split[3].equals("-1")) {
							targetid2sjid.put(split[1], split[3]);
							if (!sjids.contains(split[3])) {
								sjids += split[3] + ",";
							}
							found = true;
						}
						if (!split[4].equals("nosjid")  && !split[4].equals("-1")) { 
							targetid2sjid.put(split[1], split[4]);
							if (!sjids.contains(split[4])) {
								sjids += split[4] + ",";
							}
							found = true;
						}
						if (found) {
							targetid2sjid.put(split[1], sjids);
						}
					}
				}
			}
			in.close();

			
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TARGET_Reference\\idconversion\\pan_target_sample_info.update_Nov212016_OSbadAdded_update04062018_forPatee.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length >= 11) {
					System.out.println(split[11]);
					sjid2targetid.put(split[11], split[7]);
					targetid2sjid.put(split[7], split[11]);
				}
			}
			in.close();

			System.out.println(targetid2sjid.size());
			System.out.println(targetid2sjid.size());
			
			HashMap total_sjids = new HashMap();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\references\\TARGET_RNAseq_Information\\TARGET_RNAseq_LibraryPrep_Info.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("TARGET\tAssociatedSJIDs\tStrandInfo\tRiboZeroStatus\n");
			HashMap found = new HashMap();
			String target_inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\references\\TARGET_RNAseq_Information\\combined_sdrf.txt";
			fstream = new FileInputStream(target_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String strand_annotation = "Unknown";
				String library_info = "Unknown";
				if (targetid2sjid.containsKey(split[19])) {
					String sjid = (String)targetid2sjid.get(split[19]);
					if (str.contains("Unstranded")) {
						strand_annotation = "Unstranded";
					} else if (str.contains("StrandSpecific")) {
						strand_annotation = "StrandSpecific";
					} else {
						strand_annotation = "Unknown";
					}
					if (str.contains("rRnaDepleted")) {
						library_info = "rRnaDepleted";
					} else {
						
					}
					found.put(split[19], split[19]);
					if (!uniq.containsKey(split[19] + "\t" + sjid + "\t" + strand_annotation + "\t" + library_info + "\n")) {
						out.write(split[19] + "\t" + sjid + "\t" + strand_annotation + "\t" + library_info + "\n");
						uniq.put(split[19] + "\t" + sjid + "\t" + strand_annotation + "\t" + library_info + "\n", split[19] + "\t" + sjid + "\t" + strand_annotation + "\t" + library_info + "\n");
						for (String id: sjid.split(",")) {
							if (!id.equals("")) {
								total_sjids.put(id, id);
							}
						}
					}
				}
			}
			in.close();
			out.close();
			System.out.println(found.size());
			
			System.out.println(total_sjids.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
