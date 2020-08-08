package stjude.projects.jinghuizhang.immunesignature;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class JinghuiZhangPCGPTARGETAppendImmuneClusterInformation {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			
			HashMap map2 = new HashMap();
			//String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\Predict_Pediatric_Cancer_ImmuneSubtype\\SJ_predicted_6Type_15features_batch_corrected.txt";
			//
			//String inputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\Predict_Pediatric_Cancer_ImmuneSubtype\\SJ_predicted_6Type_6features_batch_corrected.txt";
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\Immune_Score_TCGAGenes\\SJ_predicted_6Type_15features_batch_corrected.txt";
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
				map2.put(split[0].split("\\.")[0], type);
			}
			in.close();
			
			//String outputFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_target_annotation_immunesubtype.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\Solid_Brain_pcgp_target_annotation_immunesubtype.txt";
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
				split[0] = split[0].split("-")[0].split("\\.")[0];
				String type = "NotAvail";
				if (map.containsKey(split[0])) {
					type = (String)map.get(split[0]);
				}
				//out.write(str + "\t" + type + "\n");
				if (map.containsKey(split[0])) {
					out.write(split[0] + "\t" + type + "\n");
				}
			}
			in.close();
			out.close();
			
			
			//String outputFileAnnotation = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_target_simplified_meta_appended_20200508.txt";
			String outputFileAnnotation = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis_Updated_Matrix_20200518\\Uncorrected\\solid_brain_pcgp_target_simplified_meta_appended_20200528.txt";
			FileWriter fwriterAnnotation = new FileWriter(outputFileAnnotation);
			BufferedWriter outAnnotation = new BufferedWriter(fwriterAnnotation);

			
			HashMap disease_type = new HashMap();
			HashMap immune_subtypes = new HashMap();
			HashMap map_disease_count = new HashMap();
			String inputMetaFile = "Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\ImmuneSignatureAnalysis\\pcgp_target_simplified_meta_20200508.txt";
			fstream = new FileInputStream(inputMetaFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			//out.write(in.readLine() + "\tImmuneCluster\n");
			String header = in.readLine();
			//out.write( + "\tImmuneCluster\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String disease = split[1];
				split[0] = split[0].split("-")[0];
				System.out.println(split[0]);
				if (map2.containsKey(split[0])) {
					String immune_subtype = (String)map2.get(split[0]);
					immune_subtypes.put(immune_subtype, immune_subtype);
					disease_type.put(disease, disease);
					if (map_disease_count.containsKey(disease + "\t" + immune_subtype)) {
						int count = (Integer)map_disease_count.get(disease + "\t" + immune_subtype);
						count++;
						map_disease_count.put(disease + "\t" + immune_subtype, count);
					} else {
						map_disease_count.put(disease + "\t" + immune_subtype, 1);
					}
				}
			}
			in.close();
			
			outAnnotation.write("Disease");
			Iterator itr2 = immune_subtypes.keySet().iterator();
			while (itr2.hasNext()) {
				String immune = (String)itr2.next();
				outAnnotation.write("\t" + immune);
			}
			outAnnotation.write("\n");
			Iterator itr = disease_type.keySet().iterator();
			while (itr.hasNext()) {
				String disease = (String)itr.next();
				outAnnotation.write(disease);
				itr2 = immune_subtypes.keySet().iterator();
				while (itr2.hasNext()) {
					String immune = (String)itr2.next();
					if (map_disease_count.containsKey(disease + "\t" + immune)) {
						outAnnotation.write("\t" + map_disease_count.get(disease + "\t" + immune));
					} else {
						outAnnotation.write("\t0.0");
					}					
				}
				outAnnotation.write("\n");
			}
			//System.out.println(count);
			outAnnotation.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
