package stjude.projects.jinghuizhang.customcollagen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Custom script for ranking the exons of COL11A1
 * @author tshaw
 *
 */
public class JinghuiZhangRankExonsCOL11A1 {

	public static void main(String[] args) {
		
		try {
			
			HashMap map_peptide = new HashMap();
			
			String inputCandidateFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\PCGP_RNAseq\\processed_from_old_bam\\QC\\RNAseQC\\candidates\\candidate_peptide.txt";
			FileInputStream fstream = new FileInputStream(inputCandidateFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[3].contains("-")) {
					map_peptide.put(split[0].replaceAll(">", ""), split[3] + ":" + split[6]);
				}
				
			}
			in.close();

			
			fstream = new FileInputStream(inputCandidateFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!map_peptide.containsKey(split[0].replaceAll(">", ""))) {
					map_peptide.put(split[0].replaceAll(">", ""), split[3] + ":" + split[6]);
				}
				
			}
			in.close();

			HashMap sjos_score = new HashMap();
			String folderPath = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\presentations\\Collagen_Target\\COL11A1\\SJOS\\";
			File folder = new File(folderPath);
			for (File f: folder.listFiles()) {
				if (!f.getName().contains(".html")) {
					String[] split_name = f.getName().split("_");
					String coord = split_name[1] + ":" + split_name[2] + "-" + split_name[3];
					
					fstream = new FileInputStream(f.getPath());
					din = new DataInputStream(fstream);
					in = new BufferedReader(new InputStreamReader(din));
					while (in.ready()) {
						String str = in.readLine();
						String[] split = str.split("\t");
						if (split[0].equals("SJOS")) {
							double score = new Double(split[1]) / (new Double(split[1]) + new Double(split[2]) + new Double(split[3]) + new Double(split[4]));
							sjos_score.put(coord,  score);
						}
					}
					in.close();					
				}
			}
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\presentations\\Collagen_Target\\COL11A1\\COL11A1_MetaInformation_Updated.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);	
			
			String inputMetaFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\presentations\\Collagen_Target\\COL11A1\\COL11A1_MetaInformation.txt";
			fstream = new FileInputStream(inputMetaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tSJOS_Score\tPeptide\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String coord = split[4];
				double score = -1;
				if (sjos_score.containsKey(coord)) {
					score = (Double)sjos_score.get(coord);
				}
				String peptide = "NA";
				if (map_peptide.containsKey(coord)) {
					peptide = (String)map_peptide.get(coord);
				}
				out.write(str + "\t" + score + "\t" + peptide + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
