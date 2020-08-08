package stjude.projects.jinghuizhang.hla.trust4.annotatematrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangAnnotateTCRBCRMatrix {

	
	public static void main(String[] args) {
		
		try {
			

			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TRUST4\\combined_TCR_BCR_TRUST4.output_merge_annotated.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap map = new HashMap();
			String annotationMatrix = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\pcgp_target_annotation_immune_combined.txt";
			FileInputStream fstream = new FileInputStream(annotationMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], split[2] + "\t" + split[3]);
			}
			in.close();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\TRUST4\\combined_TCR_BCR_TRUST4.output_merge.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\tDisease\tType\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String disease_type = (String)map.get(split[0]);
					out.write(str + "\t" + disease_type + "\n");
				}
				
			}
			in.close();			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
