package stjude.projects.jinghuizhang.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

public class JinghuiZhangAppendCancerTypeColor {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			LinkedList color_list = new LinkedList();
			color_list.add("#9c5f70");
			color_list.add("#ffbbb3");
			color_list.add("#7214ec");
			color_list.add("#ce0000");
			color_list.add("#191919");
			color_list.add("#004000");
			color_list.add("#008000");
			color_list.add("#4ac7c2");
			color_list.add("#0f687e");
			color_list.add("#a59e39");
			color_list.add("#922932");
			color_list.add("#161fc4");
			color_list.add("#229f72");
			color_list.add("#b0cf0e");
			color_list.add("#aabf74");
			color_list.add("#7a3ca2");
			color_list.add("#ffce00");
			color_list.add("#ff4000");
			color_list.add("#5c9432");
			
			int color_list_id = 0;
			HashMap map_color = new HashMap();
			String sampleTypeFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM_renamed.20190716.txt";
			FileInputStream fstream = new FileInputStream(sampleTypeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[2]);
				
				if (!map_color.containsKey(split[2])) {
					String current_color = (String)color_list.get(color_list_id);
					map_color.put(split[2], current_color);
					color_list_id++;
					if (color_list_id >= color_list.size()) {
						color_list_id = 0;
					}
				}
			}
			in.close();
			
			HashMap cluster_map = new HashMap();
			String clusterFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\TCGA_Suerat\\TCGA_CellsIdentity.txt";
			fstream = new FileInputStream(clusterFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				cluster_map.put(split[0], split[1]);
				System.out.println(split[1]);
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\TCGA_Suerat\\TCGA_TSNE_appended.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String tsneCoordFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\TCGA_Suerat\\TCGA_TSNE.txt";
			fstream = new FileInputStream(tsneCoordFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine().replaceAll("\"", "");
			out.write("SampleName\t" + header + "\tType\tClusterNum\tColor\n");
			while (in.ready()) {
				String str = in.readLine().replaceAll("\"", "");
				String[] split = str.split("\t");
				
				if (map.containsKey(split[0])) {
					out.write(str);
					String annotation = (String)map.get(split[0]);
					String cluster_num = (String)cluster_map.get(split[0]);
					String color = (String)map_color.get(annotation);
					
					out.write("\t" + annotation + "\t" + cluster_num + "\t" + color);
					out.write("\n");
				}
				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
