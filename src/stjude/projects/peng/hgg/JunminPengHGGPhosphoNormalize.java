package stjude.projects.peng.hgg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JunminPengHGGPhosphoNormalize {

	
	public static void main(String[] args) {
		
		try {
			
			String whole_proteome_file = "\\\\gsc.stjude.org\\project_space\\penggrp\\ProteomicsEnzymeActivity\\common\\HGG_Paper\\who.txt";			
			String phos_proteome_file = "\\\\gsc.stjude.org\\project_space\\penggrp\\ProteomicsEnzymeActivity\\common\\HGG_Paper\\phos.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\penggrp\\ProteomicsEnzymeActivity\\common\\HGG_Paper\\phos_who_combined.txt";

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap whole_proteome_map = new HashMap();
			FileInputStream fstream = new FileInputStream(whole_proteome_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header1 = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				whole_proteome_map.put(split[1], str);
			}
			in.close();
			

			HashMap phos_proteome_map = new HashMap();
			fstream = new FileInputStream(phos_proteome_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header2 = in.readLine();
			out.write(header2 + "\t" + header1 + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (whole_proteome_map.containsKey(split[2])) {
					out.write(str + "\t" + whole_proteome_map.get(split[2]) + "\n");
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
