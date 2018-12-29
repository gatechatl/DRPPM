package stjude.projects.mondirakundu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class MondiraKunduCheckOnlineOverlapULK1 {

	public static void main(String[] args) {	
		
		try {			
			HashMap online_ulk1_network = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\BIOGRID\\BioPlex_String400_Inweb150_v1.0.1_BIOGRID-3.4.157.sif";
			//inputFile = "C:\\Users\\tshaw\\project\\Pathways\\Network\\BioPlex_String400_Inweb150_v1.0.1.sif";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 	
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				String[] split = str.split("\t");
				if (split[0].equals("ULK1") || split[2].equals("ULK1")) {
					online_ulk1_network.put(split[0], split[0]);
					online_ulk1_network.put(split[2], split[2]);
				}
			}
			in.close();

			String outputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\BIOGRID\\Human_PPI_genelist_append_ULK1_interactome.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

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
			
			String inputFile2 = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\BIOGRID\\Human_PPI_genelist.txt";
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 	
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (online_ulk1_network.containsKey(split[0])) {
					out.write(str + "\ttrue");
				} else {
					out.write(str + "\tfalse");
				}
				if (autophagy.containsKey(split[0])) {
					out.write("\ttrue");
				} else {
					out.write("\tfalse");
				}
				if (stressGranule.containsKey(split[0])) {
					out.write("\ttrue\n");
				} else {
					out.write("\tfalse\n");
				}
			}
			
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
