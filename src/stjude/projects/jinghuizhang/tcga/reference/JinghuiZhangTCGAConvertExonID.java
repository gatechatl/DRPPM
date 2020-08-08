package stjude.projects.jinghuizhang.tcga.reference;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangTCGAConvertExonID {

	public static void main(String[] args) {
		
		
		try {
			HashMap map = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\gene\\download\\legacy_sample_meta_informaion_table.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].equals("NA")) {
					map.put(split[0], split[3] + "_" + split[4]);
				}
				if (!split[1].equals("NA")) {
					map.put(split[1], split[3] + "_" + split[4]);
				}
			}
			in.close();
			
			//String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM_sampleName.20190928.txt";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM.sampleName.20191010.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			int count = 0;
			int count_total = 0;
			//String exon_matrix_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM.20190201.txt";
			String exon_matrix_file = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TCGA_Reference\\RPKM\\legacy\\TCGA_FN1_ED-B_RPKM.20191009.txt";
			fstream = new FileInputStream(exon_matrix_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			String[] split_header = header.split("\t");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String name = split[0];
				if (map.containsKey(name)) {
					count++;
					String updated_name = (String)map.get(name);
					out.write(updated_name + "\t" + split[1] + "\t" + split[2] + "\n");
				}
				count_total++;
				
			}
			in.close();
			out.close();
			System.out.println(count + "\t" + count_total);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
