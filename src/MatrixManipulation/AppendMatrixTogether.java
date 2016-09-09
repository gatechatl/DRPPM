package MatrixManipulation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class AppendMatrixTogether {

	
	public static String description() {
		return "Append different matrix into a single matrix. First row must be sample name. First column must be gene name.";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[file1] [file2] [file...]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap geneList = new HashMap();
			HashMap[] maps = new HashMap[args.length];
			LinkedList[] sampleLists = new LinkedList[args.length];
			LinkedList sampleList = new LinkedList();
			for (int i = 0; i < args.length; i++) {
				maps[i] = new HashMap();
				sampleLists[i] = new LinkedList();
			}
			int i = 0;
			for (String inputFile: args) {
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				String header = in.readLine();
				String[] header_split = header.split("\t");
				for (int j = 1; j < header_split.length; j++) {
					header_split[j] = header_split[j].replaceAll(" ",  "_");
					sampleLists[i].add(header_split[j]);
					if (sampleList.contains(header_split[j])) {
						String newName = header_split[j];
						int num = 2;
						while (sampleList.contains(newName)) {
							newName = header_split[j] + "_" + num;
							num++;
						}
						sampleList.add(newName);
					} else {
						sampleList.add(header_split[j]);
					}
				}
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					geneList.put(split[0], split[0]);
					String values = split[1];
					for (int j = 2; j < split.length; j++) {
						values += "\t" + split[j];
					}
					maps[i].put(split[0], values);
				}
				in.close();
				i++;
			}
			String final_header = "GeneName";
			
			Iterator itr = sampleList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				final_header += "\t" + sampleName; 
			}
		
			System.out.println(final_header);
			itr = geneList.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				String values = geneName;
				for (int j = 0; j < maps.length; j++) {
					if (maps[j].containsKey(geneName)) {
						String value = (String)maps[j].get(geneName);
						values += "\t" + value;
					} else {
						for (int k = 0; k < sampleLists[j].size(); k++) {
							values += "\t" + "0";
						}
					}										
				}	
				System.out.println(values);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
