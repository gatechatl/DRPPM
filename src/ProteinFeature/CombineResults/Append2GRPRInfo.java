package ProteinFeature.CombineResults;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Append2GRPRInfo {

	public static String parameter_info() {
		return "[GR_only] [PR_only] [GRPR] [GRPR_Annot] [ProteinFeature]";
	}
	public static void execute(String[] args) {
		
		try {
			String GR_only = args[0];
			String PR_only = args[1];
			String GRPR = args[2];			
			
			HashMap GR_map = getList(GR_only);
			HashMap PR_map = getList(PR_only);
			HashMap GRPR_map = getList(GRPR);
			
			String existing_annotation = args[3];			
			String proteinFeatureInputFile = args[4];
			HashMap protein_feature = new HashMap();
			FileInputStream fstream = new FileInputStream(proteinFeatureInputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String feature_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				protein_feature.put(split[split.length - 1], str);
			}
			in.close();
			
			fstream = new FileInputStream(existing_annotation);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String existing_header = in.readLine();
			System.out.println("Type" + "\t" + existing_header + "\t" + feature_header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String type = "NA";
				if (GR_map.containsKey(split[0])) {
					type = "GR_ONLY";
				}
				if (PR_map.containsKey(split[0])) {
					type = "PR_ONLY";
				}
				if (GRPR_map.containsKey(split[0])) {
					type = "GRPR";
				}
				if (protein_feature.containsKey(split[0])) {
					System.out.println(type + "\t" + str + "\t" + protein_feature.get(split[0]));
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	public static HashMap getList(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
