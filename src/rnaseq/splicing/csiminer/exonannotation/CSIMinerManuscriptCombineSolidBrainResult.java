package rnaseq.splicing.csiminer.exonannotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Custom program to combine the solid and brain tumor result
 * Calculate the meta-analysis score
 * Calculate the average expression score
 * @author 4472414
 *
 */
public class CSIMinerManuscriptCombineSolidBrainResult {

	
	public static String type() {
		return "CSI-Miner";
	}
	public static String description() {
		return "Combine the CSI-miner result from brain and solid tumor.";
	}
	public static String parameter_info() {
		return "[solidonly_tumor_csi_result] [brainonly_tumor_csi_result] [solid_brain_tumor_csi_result] [output_result]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String solidonly_tumor_csi_result = args[0];
			String brainonly_tumor_csi_result = args[1];
			String solid_brain_tumor_csi_result = args[2];
			String output_result = args[3];
			
			FileWriter fwriter = new FileWriter(output_result);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap solidonly_value_map = new HashMap();
			HashMap brainonly_value_map = new HashMap();
			
			FileInputStream fstream = new FileInputStream(solidonly_tumor_csi_result);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon_name = split[0].replaceAll("_ECM_", "_");
				exon_name = exon_name.replaceAll("_PanCan_", "_");
				solidonly_value_map.put(exon_name, new Double(split[1]));
			}
			in.close();
			
			fstream = new FileInputStream(brainonly_tumor_csi_result);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon_name = split[0].replaceAll("_ECM_", "_");
				exon_name = exon_name.replaceAll("_PanCan_", "_");
				
				brainonly_value_map.put(exon_name, new Double(split[1]));
			}
			in.close();
			
			
			fstream = new FileInputStream(solid_brain_tumor_csi_result);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			out.write(split_header[0]);
			out.write("\tMaxWeightedScore\tTumorScore\tNormalScore\tTumorGreaterThansNormalFlag\tHighTumorCount");
			for (int i = 2; i < split_header.length; i++) {
				out.write("\t" + split_header[i]);
			}
			out.write("\tGeneSymbol\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String exon_name = split[0].replaceAll("_ECM_", "_");
				exon_name = exon_name.replaceAll("_PanCan_", "_");
				
				double solid_value = (Double)solidonly_value_map.get(exon_name);
				double brain_value = (Double)brainonly_value_map.get(exon_name);
				double max_value = -999;
				if (solid_value > brain_value) {
					max_value = solid_value;
				} else {
					max_value = brain_value;
				}
				double normal_score = (new Double(split[3]) * 0 + new Double(split[4]) * 1 + new Double(split[5]) * 2 + new Double(split[6]) * 3) / (new Double(split[3]) + new Double(split[4]) + new Double(split[5]) + new Double(split[6]));
				double tumor_score = (new Double(split[7]) * 0 + new Double(split[8]) * 1 + new Double(split[9]) * 2 + new Double(split[10]) * 3) / (new Double(split[7]) + new Double(split[8]) + new Double(split[9]) + new Double(split[10]));
				int total_tumor_high = new Integer(split[9]) + new Integer(split[10]);
				boolean tumor_greater_normal = tumor_score > normal_score;
				out.write(exon_name + "\t" + max_value + "\t" + tumor_score + "\t" + normal_score + "\t" + tumor_greater_normal + "\t" + total_tumor_high);
				for (int i = 2; i < split_header.length; i++) {
					if (i >= split.length) {
						out.write("\t" + "NA");
					} else {
						out.write("\t" + split[i]);
					}
				}
				out.write("\t" + exon_name.split("_")[0] + "\n");
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
