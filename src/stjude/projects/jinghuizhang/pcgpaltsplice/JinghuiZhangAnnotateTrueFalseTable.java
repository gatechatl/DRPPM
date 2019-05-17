package stjude.projects.jinghuizhang.pcgpaltsplice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangAnnotateTrueFalseTable {

	
	public static void main(String[] args) {
		
		
		try {
			
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\interactive_plots\\Candidate_Surface_annotation.txt";
			String geneInfoFile = "\\\\gsc.stjude.org\\resgen\\dev\\wc\\tshaw\\REFERENCE\\IDCONVERSION\\Homo_sapiens.gene_info";
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\interactive_plots\\Candidate_Surface_annotation_output.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(geneInfoFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[2], split[8]);
			}
			in.close();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				String description = "";
				if (map.containsKey(geneName)) {
					description = (String)map.get(geneName);
				}
				boolean External_side_of_plasma_membrane = false;
				boolean Extracellular_region = false;
				boolean Extrinsic_component_membrane = false;
				boolean Extra_cellular_matrix = false;
				boolean Apical_genes = false;
				String tag = "";
				if (split[1].equals("TRUE")) {
					External_side_of_plasma_membrane = true;
					tag += "External_side_of_plasma_membrane;";
				}
				if (split[2].equals("TRUE")) {
					Extracellular_region = true;
					tag += "Extracellular_region;";
				}
				if (split[3].equals("TRUE")) {
					Extrinsic_component_membrane = true;
					tag += "Extrinsic_component_membrane;";
				}
				if (split[4].equals("TRUE")) {
					Extra_cellular_matrix = true;
					tag += "Extra_cellular_matrix;";
				}
				if (split[5].equals("TRUE")) {
					Apical_genes = true;
					tag += "Apical_genes;";
				}
				out.write(geneName + "\t" + description + "\t" + tag + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
