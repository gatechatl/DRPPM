package stjude.projects.mondirakundu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class MondiraKunduGenerateULK1SupplementaryTable {

	
	public static void main(String[] args) {
		try {
			
			HashMap gene = new HashMap();
			HashMap map = new HashMap();
			// get the list of genes applied in the 
			String inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\ULK1_edge_meta_cleaned_mod_updated.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));						
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0] + "\t" + split[2], "");
				gene.put(split[0], split[0]);
				gene.put(split[2], split[2]);
			}
			in.close();
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\ULK1_Autophagy_FromDB.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put("ULK1" + "\t" + split[0], "");
				gene.put(split[0], split[0]);
			}
			in.close();
			
			System.out.println(map.size());
			System.out.println(gene.size());
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\BioPlex_interactionList_v4.sjgraph";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String tag1 = split[0] + "\t" + split[1];
					String tag2 = split[1] + "\t" + split[0];
					if (map.containsKey(tag1)) {
						String prev = (String)map.get(tag1);
						prev += "BIOPLEX,";
						map.put(tag1, prev);
					} else if (map.containsKey(tag2)) {
						String prev = (String)map.get(tag2);
						prev += "BIOPLEX,";
						map.put(tag2, prev);
					}
				}
			}
			in.close();
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\inweb_core.score0.15.sjgraph";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String tag1 = split[0] + "\t" + split[1];
					String tag2 = split[1] + "\t" + split[0];
					String type = split[4];
					
					if (map.containsKey(tag1)) {
						String prev = (String)map.get(tag1);
						prev += "INWEB,";
						map.put(tag1, prev);
					} else if (map.containsKey(tag2)) {
						String prev = (String)map.get(tag2);
						prev += "INWEB,";
						map.put(tag2, prev);
					}
				
				}
			}
			in.close();
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\string_9606.protein.actions.v10.GN.ORrm.score400.sjgraph";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String tag1 = split[0] + "\t" + split[1];
					String tag2 = split[1] + "\t" + split[0];
					String type = split[4];
					//if (type.equals("binding")) {
						if (map.containsKey(tag1)) {
							String prev = (String)map.get(tag1);
							prev += "STRING,";
							map.put(tag1, prev);
						} else if (map.containsKey(tag2)) {
							String prev = (String)map.get(tag2);
							prev += "STRING,";
							map.put(tag2, prev);
						}
					//}
				}
			}
			in.close();
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\ULK1_BIOGRID-GENE-113996-3.4.157.tab2.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String tag1 = split[7] + "\t" + split[8];
					String tag2 = split[8] + "\t" + split[7];										
					if (map.containsKey(tag1)) {
						String prev = (String)map.get(tag1);
						prev += "BIOGRID,";
						map.put(tag1, prev);
					} else if (map.containsKey(tag2)) {
						String prev = (String)map.get(tag2);
						prev += "BIOGRID,";
						map.put(tag2, prev);
					}
				
				}
			}
			in.close();
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\BIOGRID-ALL-3.4.157.mitab.physical.sif";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String tag1 = split[0] + "\t" + split[2];
					String tag2 = split[2] + "\t" + split[0];										
					if (map.containsKey(tag1)) {
						String prev = (String)map.get(tag1);
						prev += "BIOGRIDPHYSICAL,";
						map.put(tag1, prev);
					} else if (map.containsKey(tag2)) {
						String prev = (String)map.get(tag2);
						prev += "BIOGRIDPHYSICAL,";
						map.put(tag2, prev);
					}
				
				}
			}
			in.close();
			

			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\BIOGRID-ALL-3.4.157.mitab.colocalization.sif";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String tag1 = split[0] + "\t" + split[2];
					String tag2 = split[2] + "\t" + split[0];										
					if (map.containsKey(tag1)) {
						String prev = (String)map.get(tag1);
						prev += "BIOGRIDCOLOCAL,";
						map.put(tag1, prev);
					} else if (map.containsKey(tag2)) {
						String prev = (String)map.get(tag2);
						prev += "BIOGRIDCOLOCAL,";
						map.put(tag2, prev);
					}
				
				}
			}
			in.close();
			
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\Mondira_ULK1_Target.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				if (!str.substring(0, 1).equals("#")) {
					String[] split = str.split("\t");
					String tag1 = "ULK1" + "\t" + split[0];
					String tag2 = split[0] + "\t" + "ULK1";										
					if (map.containsKey(tag1)) {
						String prev = (String)map.get(tag1);
						prev += "DETECTED_IN_OUR_MS,";
						map.put(tag1, prev);
					} else if (map.containsKey(tag2)) {
						String prev = (String)map.get(tag2);
						prev += "DETECTED_IN_OUR_MS,";
						map.put(tag2, prev);
					}
				
				}
			}
			in.close();

			HashMap stressGranule = new HashMap();
			HashMap autophagy = new HashMap();
			String a_inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\COMBINED_AUTOPHAGY\\autophagy.txt";
			fstream = new FileInputStream(a_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 	
			while (in.ready()) {
				String str = in.readLine();
				autophagy.put(str.trim(), str);
			}
			
			String sg_inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\COMBINED_STRESSGRANULE\\StressGranule.txt";
			fstream = new FileInputStream(sg_inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 	
			while (in.ready()) {
				String str = in.readLine();
				stressGranule.put(str.trim(), str);
			}
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\ULK1_Network_Reference_Bioplex_Inweb_String_Biogrid.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("GeneA\tGeneB\tGeneA_Annotation\tGeneBAnnotation\tBioplexDB\tInwebDB\tStringDB\tBiogridDB_Physical\tBiogridDB_Colocalization\tCoIPExpr\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String tag = (String)itr.next();
				String[] split = tag.split("\t");
				String geneA = split[0];
				String geneB = split[1];
				
				String ref = (String)map.get(tag);
				boolean bioplexDB = false;
				boolean inwebDB = false;
				boolean stringDB = false;
				boolean biogridDB_physical = false;
				boolean biogridDB_colocalization = false;
				boolean in_our_ms = false;
				
				String geneA_annotation = "";
				String geneB_annotation = "";
				if (autophagy.containsKey(geneA) && stressGranule.containsKey(geneA)) {
					geneA_annotation = "AUTOPHAGY,STRESSGRANULE";
				} else if (autophagy.containsKey(geneA)) {
					geneA_annotation = "AUTOPHAGY";
				} else if (stressGranule.containsKey(geneA)) {
					geneA_annotation = "STRESSGRANULE";
				}
				
				if (autophagy.containsKey(geneB) && stressGranule.containsKey(geneB)) {
					geneB_annotation = "AUTOPHAGY,STRESSGRANULE";
				} else if (autophagy.containsKey(geneB)) {
					geneB_annotation = "AUTOPHAGY";
				} else if (stressGranule.containsKey(geneB)) {
					geneB_annotation = "STRESSGRANULE";
				}
				
				if (ref.contains("BIOPLEX")) {
					bioplexDB = true;
				}
				if (ref.contains("INWEB")) {
					inwebDB = true;
				}
				if (ref.contains("STRING")) {
					stringDB = true;
				}
				if (ref.contains("BIOGRIDPHYSICAL")) {
					biogridDB_physical = true;
				}
				if (ref.contains("BIOGRIDCOLOCAL")) {
					biogridDB_colocalization = true;
				}
				if (ref.contains("DETECTED_IN_OUR_MS")) {
					in_our_ms = true;
				}
				
				
				
				out.write(tag + "\t" + geneA_annotation + "\t" + geneB_annotation + "\t" + bioplexDB + "\t" + inwebDB + "\t" + stringDB + "\t" + biogridDB_physical + "\t" + biogridDB_colocalization + "\t" + in_our_ms + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
