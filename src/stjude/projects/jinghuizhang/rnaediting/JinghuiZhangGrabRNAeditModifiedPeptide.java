package stjude.projects.jinghuizhang.rnaediting;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangGrabRNAeditModifiedPeptide {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\RNAeditDatabase.lookup.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[1]);
			}
			in.close();
			

			/*
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\RNAeditDatabase.lookup.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				
				
			}
			in.close();*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
