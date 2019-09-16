package network.db.biogrid.annotation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class RefineList {

	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\kundugrp\\ULK\\common\\ULK1_PROTEOMICS\\ULK1_PPI\\BIOGRID\\BIOGRID-ALL-3.4.157.mitab.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[11], split[11]);
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				System.out.println(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
