package stjude.projects.mondirakundu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Spliterator;

/**
 * For the network graph, we will need to correct the edge and node values.
 * @author tshaw
 *
 */
public class MondiraKunduCorrectEdgeValues {

	
	public static void main(String[] args) {
		
		try {
			
			
			HashMap Detected_ALS_SG_A_OVERLAP = new HashMap();
			HashMap ALS_SG_A_OVERLAP = new HashMap();	
			HashMap SG_A_OVERLSP = new HashMap();
			HashMap ULK1_INTERACTOME_OVERLAP = new HashMap();			
			HashMap AUTOPHAGY = new HashMap();
			HashMap STRESSGRANULE = new HashMap();
			HashMap ULK1_INTERACTOME_STRESSGRANULE = new HashMap();
			HashMap ALS_STRESSGRANULE = new HashMap();
			HashMap ALS_AUTOPHAGY = new HashMap();
			//String inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_modules.txt";
			String inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_modules2.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));									
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("M1_DETECTED_ALS_OVERLAP")) {
					for (String gene: split[1].split(",")) {
						Detected_ALS_SG_A_OVERLAP.put(gene, "#C8707E");
					}
				}
				if (split[0].equals("M2_ALS_OVERLAP")) {
					for (String gene: split[1].split(",")) {
						ALS_SG_A_OVERLAP.put(gene, "#FFA500");
					}
				}
				if (split[0].equals("M3_OVERLAP")) {
					for (String gene: split[1].split(",")) {
						SG_A_OVERLSP.put(gene, "#E48E58");
					}
				}
				if (split[0].equals("M4_AUTOPHAGY")) {
					for (String gene: split[1].split(",")) {
						AUTOPHAGY.put(gene, "#5AA08D");
					}
				}
				if (split[0].equals("M5_STRESSGRANULE")) {
					for (String gene: split[1].split(",")) {
						STRESSGRANULE.put(gene, "#4C92B1");
					}
				}
				if (split[0].equals("M8_ULK1_INTERACTOME_OVERLAP")) {
					for (String gene: split[1].split(",")) {
						ULK1_INTERACTOME_OVERLAP.put(gene, "#A9C879");
					}
				}
				if (split[0].equals("M9_ULK1_INTERACTOME_STRESSGRANULE")) {
					for (String gene: split[1].split(",")) {
						ULK1_INTERACTOME_STRESSGRANULE.put(gene, "#AC99C1");
					}
				}
				if (split[0].equals("M6_ALS_STRESSGRANULE")) {
					for (String gene: split[1].split(",")) {
						ALS_STRESSGRANULE.put(gene,  "#6666ff");
					}
				}
				if (split[0].equals("M7_ALS_AUTOPHAGY")) {
					for (String gene: split[1].split(",")) {
						ALS_AUTOPHAGY.put(gene,  "#69c0bc");
					}
				}
			}
			in.close();
			

			//String outputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_edge_meta_cleaned_mod_updated.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_edge_meta_cleaned_mod_updated2.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			
			//inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_edge_meta_cleaned_mod.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_edge_meta_cleaned_mod2.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));									
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//if (split[3].equals("20")) {
					split[3] = "1";
					split[7] = "0.5";
					split[6] = "dashed";
					if (split[0].equals("ULK1") || split[2].equals("ULK1")) {
						split[3] = "10";
						split[7] = "1.0";
						split[6] = "solid";
					}
				//}
				if (Detected_ALS_SG_A_OVERLAP.containsKey(split[0]) || ALS_SG_A_OVERLAP.containsKey(split[0]) || SG_A_OVERLSP.containsKey(split[0]) || Detected_ALS_SG_A_OVERLAP.containsKey(split[2]) || ALS_SG_A_OVERLAP.containsKey(split[2]) || SG_A_OVERLSP.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					
				}
				if (ALS_SG_A_OVERLAP.containsKey(split[0]) && ALS_SG_A_OVERLAP.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (ALS_SG_A_OVERLAP.containsKey(split[0])) {
						split[4] = (String)ALS_SG_A_OVERLAP.get(split[0]);
					}
					if (ALS_SG_A_OVERLAP.containsKey(split[2])) {
						split[4] = (String)ALS_SG_A_OVERLAP.get(split[2]);
					}
				}
				if (SG_A_OVERLSP.containsKey(split[0]) && SG_A_OVERLSP.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (SG_A_OVERLSP.containsKey(split[0])) {
						split[4] = (String)SG_A_OVERLSP.get(split[0]);
					}
					if (SG_A_OVERLSP.containsKey(split[2])) {
						split[4] = (String)SG_A_OVERLSP.get(split[2]);
					}
				}
				if (STRESSGRANULE.containsKey(split[0]) && STRESSGRANULE.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (STRESSGRANULE.containsKey(split[0])) {
						split[4] = (String)STRESSGRANULE.get(split[0]);
					}
					if (STRESSGRANULE.containsKey(split[2])) {
						split[4] = (String)STRESSGRANULE.get(split[2]);
					}
				}
				if (AUTOPHAGY.containsKey(split[0]) && AUTOPHAGY.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (AUTOPHAGY.containsKey(split[0])) {
						split[4] = (String)AUTOPHAGY.get(split[0]);
					}
					if (AUTOPHAGY.containsKey(split[2])) {
						split[4] = (String)AUTOPHAGY.get(split[2]);
					}
				}
				if (ULK1_INTERACTOME_OVERLAP.containsKey(split[0]) && ULK1_INTERACTOME_OVERLAP.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (ULK1_INTERACTOME_OVERLAP.containsKey(split[0])) {
						split[4] = (String)ULK1_INTERACTOME_OVERLAP.get(split[0]);
					}
					if (ULK1_INTERACTOME_OVERLAP.containsKey(split[2])) {
						split[4] = (String)ULK1_INTERACTOME_OVERLAP.get(split[2]);
					}
				}
				if (ULK1_INTERACTOME_STRESSGRANULE.containsKey(split[0]) && ULK1_INTERACTOME_STRESSGRANULE.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (ULK1_INTERACTOME_STRESSGRANULE.containsKey(split[0])) {
						split[4] = (String)ULK1_INTERACTOME_STRESSGRANULE.get(split[0]);
					}
					if (ULK1_INTERACTOME_STRESSGRANULE.containsKey(split[2])) {
						split[4] = (String)ULK1_INTERACTOME_STRESSGRANULE.get(split[2]);
					}
				}
				if (ALS_STRESSGRANULE.containsKey(split[0]) && ALS_STRESSGRANULE.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (ALS_STRESSGRANULE.containsKey(split[0])) {
						split[4] = (String)ALS_STRESSGRANULE.get(split[0]);
					}
					if (ALS_STRESSGRANULE.containsKey(split[2])) {
						split[4] = (String)ALS_STRESSGRANULE.get(split[2]);
					}
				} //
				if (ALS_AUTOPHAGY.containsKey(split[0]) && ALS_AUTOPHAGY.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (ALS_AUTOPHAGY.containsKey(split[0])) {
						split[4] = (String)ALS_AUTOPHAGY.get(split[0]);
					}
					if (ALS_AUTOPHAGY.containsKey(split[2])) {
						split[4] = (String)ALS_AUTOPHAGY.get(split[2]);
					}
				} //ALS_AUTOPHAGY
				if (ALS_STRESSGRANULE.containsKey(split[0]) && ALS_STRESSGRANULE.containsKey(split[2])) {
					split[3] = "10";
					split[7] = "1.0";
					split[6] = "solid";
					if (ALS_STRESSGRANULE.containsKey(split[0])) {
						split[4] = (String)ALS_STRESSGRANULE.get(split[0]);
					}
					if (ALS_STRESSGRANULE.containsKey(split[2])) {
						split[4] = (String)ALS_STRESSGRANULE.get(split[2]);
					}
				}
				/*if ((ALS_STRESSGRANULE.containsKey(split[0]) || ALS_STRESSGRANULE.containsKey(split[2])) && (STRESSGRANULE.containsKey(split[0]) || STRESSGRANULE.containsKey(split[2]))) {
					split[3] = "20";
					split[7] = "0.5";
					split[6] = "dashed";
				}
				if ((ALS_AUTOPHAGY.containsKey(split[0]) || ALS_AUTOPHAGY.containsKey(split[2])) && (AUTOPHAGY.containsKey(split[0]) || AUTOPHAGY.containsKey(split[2]))) {
					split[3] = "20";
					split[7] = "0.5";
					split[6] = "dashed";
				}*/
				if ((ALS_STRESSGRANULE.containsKey(split[0]) || ALS_STRESSGRANULE.containsKey(split[2])) && !(AUTOPHAGY.containsKey(split[0]) || AUTOPHAGY.containsKey(split[2]))) {
					split[3] = "5";
					split[7] = "0.5";
					split[6] = "dashed";
				}
				if ((ALS_AUTOPHAGY.containsKey(split[0]) || ALS_AUTOPHAGY.containsKey(split[2])) && !(STRESSGRANULE.containsKey(split[0]) || STRESSGRANULE.containsKey(split[2]) || ULK1_INTERACTOME_STRESSGRANULE.containsKey(split[0]) || ULK1_INTERACTOME_STRESSGRANULE.containsKey(split[2]))) {
					split[3] = "5";
					split[7] = "0.5";
					split[6] = "dashed";
				}
				out.write(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4] + "\t" + split[5] + "\t" + split[6] + "\t" + split[7] + "\n");
			}
			in.close();
			out.close();
			
			
			
			
			//String outputFile2 = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_node_meta_labels_mod_updated.txt";
			String outputFile2 = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_node_meta_labels_mod_updated2.txt";
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);			
			
			//inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_node_meta_labels_mod.txt";
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\ULK1_node_meta_labels_mod2.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));									
			out2.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				split[8] = "0";
				/*if (split[3].equals("20")) {
					split[3] = "2";
					split[7] = "0.5";
					if (split[0].equals("ULK1") || split[2].equals("ULK1")) {
						split[3] = "10";
					}
				}*/
				if (Detected_ALS_SG_A_OVERLAP.containsKey(split[0])) {
					if (Detected_ALS_SG_A_OVERLAP.containsKey(split[0])) {
						split[3] = (String)Detected_ALS_SG_A_OVERLAP.get(split[0]);
					}
				}
				if (ALS_SG_A_OVERLAP.containsKey(split[0])) {
					if (ALS_SG_A_OVERLAP.containsKey(split[0])) {
						split[3] = (String)ALS_SG_A_OVERLAP.get(split[0]);
					}					
				}
				if (SG_A_OVERLSP.containsKey(split[0])) {

					if (SG_A_OVERLSP.containsKey(split[0])) {
						split[3] = (String)SG_A_OVERLSP.get(split[0]);
					}
					
				}
				if (STRESSGRANULE.containsKey(split[0])) {
					if (STRESSGRANULE.containsKey(split[0])) {
						split[3] = (String)STRESSGRANULE.get(split[0]);
					}
					
				}
				if (AUTOPHAGY.containsKey(split[0])) {

					if (AUTOPHAGY.containsKey(split[0])) {
						split[3] = (String)AUTOPHAGY.get(split[0]);
					}
					
				}
				if (ULK1_INTERACTOME_OVERLAP.containsKey(split[0])) {
					if (ULK1_INTERACTOME_OVERLAP.containsKey(split[0])) {
						split[3] = (String)ULK1_INTERACTOME_OVERLAP.get(split[0]);
					}
					
				}
				if (ALS_AUTOPHAGY.containsKey(split[0])) {
					if (ALS_AUTOPHAGY.containsKey(split[0])) {
						split[3] = (String)ALS_AUTOPHAGY.get(split[0]);
					}					
				}
				if (ALS_STRESSGRANULE.containsKey(split[0])) {
					if (ALS_STRESSGRANULE.containsKey(split[0])) {
						split[3] = (String)ALS_STRESSGRANULE.get(split[0]);
					}					
				}
				//
				out2.write(split[0] + "\t" + split[1] + "\t" + split[2] + "\t" + split[3] + "\t" + split[4] + "\t" + split[5] + "\t" + split[6] + "\t" + split[7] + "\t" + split[8] + "\n");
			}
			in.close();
						
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
