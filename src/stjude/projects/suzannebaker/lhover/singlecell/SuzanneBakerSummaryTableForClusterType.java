package stjude.projects.suzannebaker.lhover.singlecell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class SuzanneBakerSummaryTableForClusterType {

	
	public static void main(String[] args) {
		
		
		try {
			
			HashMap map = new HashMap();
			HashMap sampleList = new HashMap();
			HashMap cluster = new HashMap();
			String outputFile = "\\\\gsc.stjude.org\\project_space\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\Res0.4_Per50\\Cluster_Summary_20190829.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
		
			String inputFile = "\\\\gsc.stjude.org\\project_space\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\Res0.4_Per50\\NTRK_NesCre_GfapCreER_TSNE_CellsIdentity_Color_WithOldClusters_genotypeColor.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = split[8] + "-" + split[0].split("-")[1];				
				cluster.put(split[3], split[3]);
				sampleList.put(sample, sample);
				
				String name = sample + "\t" + split[3];
				if (map.containsKey(name)) {
					int count = (Integer)map.get(name);
					count = count + 1;
					map.put(name, count);
				} else {
					map.put(name, 1);
				}
			}
			in.close();
			out.write("Sample");
			Iterator itr2 = cluster.keySet().iterator();
			while (itr2.hasNext()) {
				String c = (String)itr2.next();
				out.write("\tCluster" + c);
			}
			out.write("\n");
			
			Iterator itr = sampleList.keySet().iterator();
			while (itr.hasNext()) {
				String sample = (String)itr.next();
				out.write(sample);
				itr2 = cluster.keySet().iterator();
				while (itr2.hasNext()) {
					String c = (String)itr2.next();
					String name = sample + "\t" + c;
					int count = 0;
					if (map.containsKey(name)) {
						count = (Integer)map.get(name);						
					}
					out.write("\t" + count);
				}
				out.write("\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
