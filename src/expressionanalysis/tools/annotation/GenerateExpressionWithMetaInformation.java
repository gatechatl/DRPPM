package expressionanalysis.tools.annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class GenerateExpressionWithMetaInformation {

	
	public static void main(String[] args) {
		
		
		try {
			

			String output_sample_meta_info = "/home/gatechatl/ExpressionAnalysisTools/PediatricCancer/CombinedPediatricGTExCases/sample_metainfo.txt";
			FileWriter fwriter = new FileWriter(output_sample_meta_info);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			HashMap map_biotype = new HashMap();
			String inputFile = "/home/gatechatl/ExpressionAnalysisTools/GTEx/CAB/gtex_annotation.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0], "GTEx_" + split[17].replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "").replaceAll("-", "_"));
				map_biotype.put(split[0], "GTEx_" + split[9].replaceAll("\\(", "").replaceAll("\\)", "").replaceAll(" ", "").replaceAll("-", "_"));
			}
			in.close();

			HashMap stjude_map = new HashMap();
			inputFile = "/home/gatechatl/CSI-Miner/Reference/StJudeMetaInformation/sample_type_metadata_appended.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				stjude_map.put(split[4].split("-")[0], split[2] + "_" + "SJ" + split[3] + "_" + split[5]);
				
			}
			in.close();
			
			
			String metaInfoFile = "/home/gatechatl/ExpressionAnalysisTools/PediatricCancer/CombinedPediatricGTExCases/sample_information.txt";
			fstream = new FileInputStream(metaInfoFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (map.containsKey(split[0])) {
					String meta = (String)map.get(split[0]);
					String tissue_info = (String)map_biotype.get(split[0]);
					out.write(split[0] + "\t" + meta + "\t" + tissue_info + "\tGTEx" + "\n");
				} else if (stjude_map.containsKey(split[0])) { 
					String meta = (String)stjude_map.get(split[0]);
					out.write(split[0] + "\tSJ" + meta + "\tSJ" + meta + "\t" + meta.split("_")[0] + "\n");
				} else {
					String type = split[0].split("_")[0].replaceAll("0", "").replaceAll("1", "").replaceAll("2", "").replaceAll("3", "").replaceAll("4", "").replaceAll("5", "").replaceAll("6", "").replaceAll("7", "").replaceAll("8", "").replaceAll("9", "");
					if (split[0].split("_")[1].substring(0, 1).equals("D") || split[0].split("_")[1].substring(0, 1).equals("E") || split[0].split("_")[1].substring(0, 1).equals("S")  || split[0].split("_")[1].substring(0, 1).equals("F")) {
						//out.write(split[0] + "\t" + "X_" + type + "_Diagnosis\n");
					} else if (split[0].split("_")[1].substring(0, 1).equals("R") || split[0].split("_")[1].substring(0, 1).equals("M")) {
						//out.write(split[0] + "\t" + "X_" + type + "_Relapse\n");
					} else if (split[0].split("_")[1].substring(0, 1).equals("C")) {
						//out.write(split[0] + "\t" + "X_" + type + "_CellLine\n");
					} else if (split[0].split("_")[1].substring(0, 1).equals("X") || split[0].split("_")[1].substring(0, 1).equals("Y") || split[0].split("_")[1].substring(0, 1).equals("Z")) {
						out.write(split[0] + "\t" + "SJXeno_" + type + "_Xenograft\t" + "SJXeno_" + type + "_Xenograft" + "\tXeno\n");
					} else if (split[0].split("_")[1].substring(0, 1).equals("A")) {
						//out.write(split[0] + "\t" + "X_" + type + "_Autopsy\n");
					} else if (split[0].split("_")[1].substring(0, 1).equals("G")) {
						//out.write(split[0] + "\t" + "X_" + type + "_Germline\n");
					} else {
						//out.write(split[0] + "\t" + "X_" + type + "_Other\n");
					}
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
