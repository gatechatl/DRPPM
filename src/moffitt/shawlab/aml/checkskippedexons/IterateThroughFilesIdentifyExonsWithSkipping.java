package moffitt.shawlab.aml.checkskippedexons;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class IterateThroughFilesIdentifyExonsWithSkipping {

	
	public static void main(String[] args) {
		
		
		try {
			
			HashMap map = new HashMap();
			HashMap boneMarrow_map = new HashMap();
			
			String outputFile = "/Users/4472414/Projects/AML_Cohort/ExonSplicing/PutativeAlternativeSplicedCandidates_0to0.9.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			

			
			String folderPath = "/Users/4472414/Projects/AML_Cohort/ExonSplicing/psi_clean";
			File files = new File(folderPath);
			for (File f: files.listFiles()) {
				boolean firstLine = true;
				String inputPSIPSOFile = f.getPath();
				FileInputStream fstream = new FileInputStream(inputPSIPSOFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				
				while (in.ready()) {
					String str = in.readLine();
					if (!firstLine) {
						String[] split = str.split("\t");
						double psi = new Double(split[1]);
						if (psi > 0.0 && psi < 0.9) {
							if (map.containsKey(split[0])) {
								int count = (Integer)map.get(split[0]);
								count = count + 1;
								map.put(split[0], count);
							} else {
								map.put(split[0], 1);
							}
							
							if (inputPSIPSOFile.contains("-BM") || inputPSIPSOFile.contains("-R00") || inputPSIPSOFile.contains("TARGET-00-")) {
								if (boneMarrow_map.containsKey(split[0])) {
									int count = (Integer)boneMarrow_map.get(split[0]);
									count = count + 1;
									boneMarrow_map.put(split[0], count);
								} else {
									boneMarrow_map.put(split[0], 1);
								}
							}
						}
					}
					firstLine = false;
				}
				in.close();
			}
			
			out.write("Exon\tAMLFrequency\tBM_Frequency\tExonID\n");
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String exon = (String)itr.next();
				int count = (Integer)map.get(exon);
				int bm_count = 0;
				if (boneMarrow_map.containsKey(exon)) {
					bm_count = (Integer)boneMarrow_map.get(exon);
				}
				out.write(exon + "\t" + (count - bm_count) + "\t" + bm_count + "\n");
				out.flush();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
