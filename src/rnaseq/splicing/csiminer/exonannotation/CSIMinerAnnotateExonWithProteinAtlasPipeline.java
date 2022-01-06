package rnaseq.splicing.csiminer.exonannotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Grab the features to add to the exon feature list.
 * @author 4472414
 *
 */
public class CSIMinerAnnotateExonWithProteinAtlasPipeline {


	
	public static String type() {
		return "CSI-Miner";
	}
	public static String description() {
		return "Append the Protein Atlas annotation.";
	}
	public static String parameter_info() {
		return "[inputExonAnnotation] [inputProteinAtlas] [protein_atlas_feature_file] [outputExonAnnotationFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputExonAnnotation = args[0]; 					
			String inputProteinAtlas = args[1];
			String protein_atlas_feature_file = args[2];
			String outputExonAnnotationFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputExonAnnotationFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap map_features = new HashMap();
			FileInputStream fstream = new FileInputStream(protein_atlas_feature_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				map_features.put(str.trim(), str.trim());
			}
			in.close();
			
			fstream = new FileInputStream(inputProteinAtlas);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			LinkedList list_index = new LinkedList();
			String feature_list_str = "";
			String feature_list_na = "";
			for (int i = 1; i < split_header.length; i++) {
				if (map_features.containsKey(split_header[i])) {
					System.out.println(split_header[i]);
					list_index.add(i);
					feature_list_str += "\t" + split_header[i];
					feature_list_na += "\t" + "NA";
				}
			}			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String feature_value_str = "";
				for (int i = 1; i < split_header.length; i++) {
					if (map_features.containsKey(split_header[i])) {						
						feature_value_str += "\t" + split[i];
					}
				}			
				map.put(split[0], feature_value_str);
			}
			in.close();
			
			fstream = new FileInputStream(inputExonAnnotation);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + feature_list_str + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[0].split("_")[0];
				if (map.containsKey(gene)) {
					String value = (String)map.get(gene);
					out.write(str + value + "\n");
				} else {
					out.write(str + feature_list_na + "\n");
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
}
