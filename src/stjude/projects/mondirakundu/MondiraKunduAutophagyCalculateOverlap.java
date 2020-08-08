package stjude.projects.mondirakundu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class MondiraKunduAutophagyCalculateOverlap {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap background = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\background_geneName.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 	
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				String[] split = str.split("\t");
				background.put(split[0], split[0]);
			}
			in.close();
			
			HashMap autophagy = new HashMap();
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\COMBINED_AUTOPHAGY\\Autophagy.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 	
			while (in.ready()) {
				String str = in.readLine().toUpperCase();
				String[] split = str.split("\t");
				if (background.containsKey(split[0])) {
					autophagy.put(split[0], split[0]);
				}
			}
			in.close();
			
			HashMap hit = new HashMap();
			HashMap target = new HashMap();
			inputFile = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\RawInteractome\\Mondira_ULK1_Target.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));						
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (background.containsKey(split[0])) {
					target.put(split[0], split[0]);
				}
			}
			in.close();
			
			String outputFile_ULK1_interactome = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\COMBINED_AUTOPHAGY\\ULK1_Overlap_Autophagy_Target.txt";
			FileWriter fwriter_ULK1_interactome = new FileWriter(outputFile_ULK1_interactome);
			BufferedWriter out_ULK1_interactome = new BufferedWriter(fwriter_ULK1_interactome);
			
			String outputFile_autophagy = "\\\\gsc.stjude.org\\project_space\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\COMBINED_AUTOPHAGY\\ULK1_Overlap_Autophagy_DB.txt";
			FileWriter fwriter_autophagy = new FileWriter(outputFile_autophagy);
			BufferedWriter out_autophagy = new BufferedWriter(fwriter_autophagy);
			
			
			Iterator itr = target.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out_ULK1_interactome.write(gene);
				if (autophagy.containsKey(gene)) {
					hit.put(gene, gene);
					out_ULK1_interactome.write("\ttrue\n");
				} else {
					out_ULK1_interactome.write("\tfalse\n");
				}
			}
			
			itr = autophagy.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out_autophagy.write(gene);
				if (target.containsKey(gene)) {
					hit.put(gene, gene);
					out_autophagy.write("\ttrue\n");
				} else {
					out_autophagy.write("\tfalse\n");
				}
			}
			System.out.println("Total Background: " + background.size());
			System.out.println("Total Stress Granule: " + autophagy.size());
			System.out.println("Total Target: " + target.size());			
			System.out.println("Total Hit: " + hit.size());
			
			out_ULK1_interactome.close();
			out_autophagy.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
