package rnaseq.splicing.juncsalvager.psi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * /rgs01/project_space/zhanggrp/AltSpliceAtlas/common/analysis/Comprehensive_CAR-T_Analysis/hg38_full_exon_analysis/PSI_Exon_Skipping
 * @author tshaw
 *
 */
public class JuncSalvagerGeneratePSIScript {


	public static String type() {
		return "JUNCSALVAGER";
	}
	public static String description() {
		return "Generate script to calculate the psi value for each exon in the gtf file using STAR SJ file.\n";
	}
	public static String parameter_info() {
		return "[STARSJfilelst] [gtfFile] [outputFolder] [outputScript]";
	}
	public static void execute(String[] args) {
		
		try {
			
			
			String fileLst = args[0];
			String gtfFile = args[1];
			String outputFolder = args[2];
			String outputScript = args[3];
			
			
			FileWriter fwriter = new FileWriter(outputScript);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(fileLst);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				File f = new File(str);
				if (f.exists()) {
					out.write("drppm -JuncSalvagerExonSkippingPSI " + str + " " + gtfFile + " 5 " + outputFolder + "/" + f.getName() + ".psi.txt" + "\n");
					//System.out.println("drppm -JuncSalvagerExonSkippingPSI " + fileLst + " " + gtfFile + " " + outputFolder + "/" + f.getName() + ".psi.txt");
				} else {
					System.out.println("Warning! File doesn't exist: " + f.getPath());
				}
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
