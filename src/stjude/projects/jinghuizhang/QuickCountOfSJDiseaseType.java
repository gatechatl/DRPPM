package stjude.projects.jinghuizhang;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class QuickCountOfSJDiseaseType {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\MethodDevelopment\\common\\tumormap\\list_of_samples.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				String sjid = str.split("-")[0];
				sjid = sjid.split("0")[0];
				sjid = sjid.split("1")[0];
				sjid = sjid.split("2")[0];
				sjid = sjid.split("3")[0];
				sjid = sjid.split("4")[0];
				sjid = sjid.split("5")[0];
				sjid = sjid.split("6")[0];
				sjid = sjid.split("7")[0];
				sjid = sjid.split("8")[0];
				sjid = sjid.split("9")[0];
				
				if (map.containsKey(sjid)) {
					int count = (Integer)map.get(sjid) + 1;
					map.put(sjid, count);
				} else {
					map.put(sjid, 1);
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String id = (String)itr.next();
				int count = (Integer)map.get(id);
				System.out.println(id + "\t" + count);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
