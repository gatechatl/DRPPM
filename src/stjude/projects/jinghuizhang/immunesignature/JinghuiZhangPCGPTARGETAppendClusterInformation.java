package stjude.projects.jinghuizhang.immunesignature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangPCGPTARGETAppendClusterInformation {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			//String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\Predict_Pediatric_Cancer_ImmuneSubtype\\SJ_predicted_6Type_15features_batch_corrected.txt";
			//
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\Predict_Pediatric_Cancer_ImmuneSubtype\\SJ_predicted_6Type_6features_batch_corrected.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				double immune_clust1 = new Double(split[1]);
				double immune_clust2 = new Double(split[2]);
				double immune_clust3 = new Double(split[3]);
				double immune_clust4 = new Double(split[4]);
				double immune_clust5 = new Double(split[5]);
				double immune_clust6 = new Double(split[6]);
				String type = "NA";
				if (immune_clust1 > immune_clust2 && immune_clust1 > immune_clust3 && immune_clust1 > immune_clust4 && immune_clust1 > immune_clust5 && immune_clust1 > immune_clust6) {
					type = "ImmClust1";
				} else if (immune_clust2 > immune_clust1 && immune_clust2 > immune_clust3 && immune_clust2 > immune_clust4 && immune_clust2 > immune_clust5 && immune_clust2 > immune_clust6) {
					type = "ImmClust2";
				} else if (immune_clust3 > immune_clust1 && immune_clust3 > immune_clust2 && immune_clust3 > immune_clust4 && immune_clust3 > immune_clust5 && immune_clust3 > immune_clust6) {
					type = "ImmClust3";
				} else if (immune_clust4 > immune_clust1 && immune_clust4 > immune_clust2 && immune_clust4 > immune_clust3 && immune_clust4 > immune_clust5 && immune_clust4 > immune_clust6) {
					type = "ImmClust4";
				} else if (immune_clust5 > immune_clust1 && immune_clust5 > immune_clust2 && immune_clust5 > immune_clust3 && immune_clust5 > immune_clust4 && immune_clust5 > immune_clust6) {
					type = "ImmClust5";
				} else if (immune_clust6 > immune_clust1 && immune_clust6 > immune_clust2 && immune_clust6 > immune_clust3 && immune_clust6 > immune_clust4 && immune_clust6 > immune_clust5) {
					type = "ImmClust6";
				} else {
					type = "Ambig";
				}
				map.put(split[0], type);
			}
			in.close();
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_target_annotation_immunesubtype.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputAnnotationFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_target_annotation.txt";
			fstream = new FileInputStream(inputAnnotationFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			//out.write(in.readLine() + "\tImmuneCluster\n");
			out.write("SampleName\tImmuneCluster\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = "NotAvail";
				if (map.containsKey(split[0])) {
					type = (String)map.get(split[0]);
				}
				//out.write(str + "\t" + type + "\n");
				out.write(split[0] + "\t" + type + "\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
