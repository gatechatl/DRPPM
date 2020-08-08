package stjude.projects.suzannebaker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SuzanneBakerOverlapGeneSets {

	
	public static void main(String[] args) {
		try {
			
			HashMap map = new HashMap();
			String pri_opc_file = "\\\\gsc.stjude.org\\project_space\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\ssGSEA\\Weng_Cancer_Stem_Cell_2019_human_short.gmt";
			FileInputStream fstream = new FileInputStream(pri_opc_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 2; i < split.length; i++) {
					map.put(split[i], split[i]);
				}
			}
			
			String cluster_file = "\\\\gsc.stjude.org\\project_space\\bakergrp\\NTRK\\common\\10X_fastq_files\\processed\\10xSingleCell\\10xSingleCell\\Combined_NesCre_GfapCreER_Additional_Analysis\\ssGSEA\\SeuratClusters_Res04Per50_Human.gmt";
			fstream = new FileInputStream(cluster_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double count = 0;
				String genes = "";
				for (int i = 2; i < split.length; i++) {
					if (map.containsKey(split[i])) {
						count++;
						genes += split[i] + ",";
					}
				}
				double hit_ratio = count / (split.length - 2);
				System.out.println(split[0] + "\t" + hit_ratio + "\t" + count + "\t" + (split.length - 2) + "\t" + genes);
			}
			System.out.println(map.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
