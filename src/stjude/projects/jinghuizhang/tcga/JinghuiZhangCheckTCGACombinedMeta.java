package stjude.projects.jinghuizhang.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangCheckTCGACombinedMeta {

	
	public static void main(String[] args) {
		
		try {
			int count = 0;
			int count_tcga = 0;
			int primary_site_index = -1;
			int disease_type_index = -1;
			int project_project_id_index = -1;
			int id_index = -1;
			int submitter_id_index = -1;
			boolean header = false;
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\meta_information\\combined_meta_TCGA.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\meta_information\\combined_meta.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				// encountered header
				if (str.contains("primary_site")) {
					for (int i = 0; i < split.length; i++) {
						if (split[i].equals("primary_site")) {
							primary_site_index = i;
						}
						if (split[i].equals("disease_type")) {
							disease_type_index = i;
						}
						if (split[i].equals("project.project_id")) {
							project_project_id_index = i;
						}
						if (split[i].equals("id")) {
							id_index = i;
						}
						if (split[i].equals("submitter_id")) {
							submitter_id_index = i;
						}
					}
					if (!header) {
						out.write(split[primary_site_index] + "\t" + split[disease_type_index] + "\t" + split[project_project_id_index] + "\t" + split[id_index] + "\t" + split[submitter_id_index] + "\n");
						header = true;
					}
				}
				
				if (str.contains("TCGA")) {
					count_tcga++;
					out.write(split[primary_site_index] + "\t" + split[disease_type_index] + "\t" + split[project_project_id_index] + "\t" + split[id_index] + "\t" + split[submitter_id_index] + "\n");
				}
				if (split[0].contains("TCGA") || split[0].contains("AD")) {
					count++;
				}
			}
			in.close();
			out.close();
			System.out.println(count);
			System.out.println(count_tcga);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
