package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CombineSplicingDeficiencyName {

	
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
			HashMap id2geneName = new HashMap();
			HashMap filteredGene = new HashMap();
			HashMap[] maps = new HashMap[args.length];
			LinkedList[] sampleLists = new LinkedList[args.length];
			LinkedList sampleList = new LinkedList();
			for (int i = 0; i < args.length; i++) {
				maps[i] = new HashMap();
				sampleLists[i] = new LinkedList();
			}
			int i = 0;
			System.out.print("GeneID\tGeneName");
			for (String inputFile: args) {
				System.out.print("\t" + inputFile);
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));				
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					if (!split[0].equals("GeneID")) {
						geneList.put(split[0], split[0]);
						id2geneName.put(split[0], split[1]);
						int num_intron_read = new Integer(split[3]);
						int num_exon_read = new Integer(split[5]);
						if (num_intron_read < 5) {
							filteredGene.put(split[0], split[0]);						
						}
						if (num_exon_read < 5) {
							filteredGene.put(split[0], split[0]);						
						}
						//String values = split[1];
						/*for (int j = 2; j < split.length; j++) {
							values += "\t" + split[j];
						}*/
						maps[i].put(split[0], split[2]);
					}
				}
				in.close();
				i++;
			}
			String final_header = "GeneName";
			System.out.println();
			/*
			Iterator itr = sampleList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				final_header += "\t" + sampleName; 
			}
			*/
			//System.out.println(final_header);
			Iterator itr = geneList.keySet().iterator();
			while (itr.hasNext()) {
				String geneID = (String)itr.next();
				if (!filteredGene.containsKey(geneID)) {
					String geneName = (String)id2geneName.get(geneID);
					System.out.print(geneID + "\t" + geneName);
					//String values = geneID;
					String values = "";
					for (int j = 0; j < maps.length; j++) {
						if (maps[j].containsKey(geneID)) {
							String value = (String)maps[j].get(geneID);
							//String value = "";
							values += "\t" + value;
						} else {
							for (int k = 0; k < sampleLists[j].size(); k++) {
								values += "\t" + "NA";
							}
						}										
					}	
					System.out.println(values);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
