package protein.features.combineresults;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ProteinFeatureWithGRPRInfo {

	public static String parameter_info() {
		return "[GR_only] [PR_only] [GRPR] [ProteinFeature]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String GR_only = args[0];
			String PR_only = args[1];
			String GRPR = args[2];			
			String proteinFeatureInputFile = args[3];
			
			HashMap GR_map = Append2GRPRInfo.getList(GR_only);
			HashMap PR_map = Append2GRPRInfo.getList(PR_only);
			HashMap GRPR_map = Append2GRPRInfo.getList(GRPR);
			
			
			HashMap protein_feature = new HashMap();
			FileInputStream fstream = new FileInputStream(proteinFeatureInputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String feature_header = in.readLine();
			System.out.println("Type" + "\t" + feature_header);
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				protein_feature.put(split[split.length - 1], str);
				String type = "Other";
				if (GR_map.containsKey(split[split.length - 1])) {
					type = "GR_ONLY";
				}
				if (PR_map.containsKey(split[split.length - 1])) {
					type = "PR_ONLY";
				}
				if (GRPR_map.containsKey(split[split.length - 1])) {
					type = "GRPR";
				}
				if (split.length >= 40) {
					System.out.println(type + "\t" + str);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
