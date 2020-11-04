package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class CombineSplicingDeficiencyNameMetaHG38 {

	
	public static String description() {
		return "Append different matrix into a single matrix. First row header contains sample name. First and second column contains geneID and geneSymbol. Third column contains intron to exon ratio. Genes with less than 5 intron or exon reads were removed";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[metaFile] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap geneList = new HashMap();
			HashMap id2geneName = new HashMap();
			HashMap filteredGene = new HashMap();
			
			HashMap filtered_intron_count = new HashMap();
			HashMap filtered_exon_count = new HashMap();
			LinkedList sampleList = new LinkedList();
			
			HashMap sampleFiles = new HashMap();
			String metaFile = args[0];
			String outputFile = args[1];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(metaFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));				
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sampleName = split[0];
				String filePath = split[1];
				sampleFiles.put(sampleName,  filePath);
				sampleList.add(sampleName);
			}
			in.close();
			LinkedList[] sampleLists = new LinkedList[sampleFiles.size()];
			HashMap[] maps = new HashMap[sampleFiles.size()];
			
			for (int i = 0; i < sampleFiles.size(); i++) {
				maps[i] = new HashMap();
				sampleLists[i] = new LinkedList();
			}
			int i = 0;
			//System.out.print("GeneID\tGeneName");
			out.write("GeneID\tGeneName");
			Iterator itr = sampleList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String inputFile = (String)sampleFiles.get(sampleName);
				//System.out.print("\t" + inputFile);
				out.write("\t" + sampleName);
				fstream = new FileInputStream(inputFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));	
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					geneList.put(split[0], split[0]);
					id2geneName.put(split[0], split[3]);
					int num_intron_read = new Integer(split[5]);
					int num_exon_read = new Integer(split[7]);
					if (filtered_intron_count.containsKey(split[0])) {
						int count = (Integer)filtered_intron_count.get(split[0]);
						count += num_intron_read;
						filtered_intron_count.put(split[0],  count);
					} else {
						filtered_intron_count.put(split[0],  num_intron_read);
					}
					
					if (num_intron_read < 5) {
						//filteredGene.put(split[1], split[1]);						
					}
					if (num_exon_read < 5) {
						//filteredGene.put(split[1], split[1]);						
					}
					
					if (filtered_exon_count.containsKey(split[0])) {
						int count = (Integer)filtered_exon_count.get(split[0]);
						count += num_exon_read;
						filtered_exon_count.put(split[0],  count);
					} else {
						filtered_exon_count.put(split[0],  num_exon_read);
					}
					
					//String values = split[1];
					/*for (int j = 2; j < split.length; j++) {
						values += "\t" + split[j];
					}*/
					maps[i].put(split[0], split[4]);
				}
				in.close();
				i++;
			}
			String final_header = "GeneName";
			//System.out.println();
			out.write("\n");
			/*
			Iterator itr = sampleList.iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				final_header += "\t" + sampleName; 
			}
			*/
			//System.out.println(final_header);
			itr = geneList.keySet().iterator();
			while (itr.hasNext()) {
				String geneID = (String)itr.next();
				int intron_count = (Integer)filtered_intron_count.get(geneID);
				int exon_count = (Integer)filtered_exon_count.get(geneID);
				
				//if (!filteredGene.containsKey(geneID)) {
				if (intron_count >= 5 && exon_count >= 5) {
					String geneName = (String)id2geneName.get(geneID);
					//System.out.print(geneID + "\t" + geneName);
					
					
					boolean skip = false;
					//String values = geneID;
					String values = "";
					for (int j = 0; j < maps.length; j++) {
						if (maps[j].containsKey(geneID)) {
							String value = (String)maps[j].get(geneID);
							//String value = "";
							values += "\t" + value;
						} else {
							//for (int k = 0; k < sampleLists[j].size(); k++) {
							//skip = true;
							values += "\t" + "0.0";
							//}
						}										
					}	
					//System.out.println(values);
					if (!skip) {
						out.write(geneID + "\t" + geneName);
						out.write(values + "\n");
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
