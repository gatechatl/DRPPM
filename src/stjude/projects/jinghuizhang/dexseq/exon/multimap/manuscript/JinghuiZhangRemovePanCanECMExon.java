package stjude.projects.jinghuizhang.dexseq.exon.multimap.manuscript;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class JinghuiZhangRemovePanCanECMExon {

	public static String description() {
		return "Custom script for removing tag associated with PanCan and ECM.";
	}
	public static String type() {
		return "JinghuiZhang";
	}
	public static String parameter_info() {
		return "[Exon File from Custom DEXseq] [Filtered OutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			

			String outputFile = args[1]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\MappingComparison\\PCGP_905_FPKM_filtcol_MedianDiseaseType_Combined_RemoveECMPanCancer.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String inputFile = args[0]; //"Z:\\ResearchHome\\ProjectSpace\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\hg38_analysis\\MappingComparison\\PCGP_905_FPKM_filtcol_MedianDiseaseType_Combined.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].split("_").length > 1) {
					if (!split[0].split("_")[1].equals("ECM") && !split[0].split("_")[1].equals("PanCan")) {
						out.write(str + "\n");
					}
				} else {
					out.write(str + "\n");
				}
			}
			in.close();					
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
