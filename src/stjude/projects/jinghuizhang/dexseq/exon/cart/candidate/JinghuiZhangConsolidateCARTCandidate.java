package stjude.projects.jinghuizhang.dexseq.exon.cart.candidate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class JinghuiZhangConsolidateCARTCandidate {

	
	public static void execute(String[] args) {
		
		try {
			
			String target_input_file = args[0]; // this should be the TARGET_1054_FPKM_filtcol_truefalse_MedianDiseaseType_filterNAs.txt
			String pcgp_input_file = args[1]; // this should be the PCGP_905_FPKM_filtcol_truefalse_MedianDiseaseType_filterNAs.txt
			String gtex_input_file = args[2]; // this should be GTEx_7526_FPKM_truefalse_MedianHistology_filterNAs.txt_*
			
			// read through each exon and identify disease with high expression
			
			HashMap exon_list = new HashMap();
			HashMap target_disease_list = new HashMap();
			HashMap pcgp_disease_list = new HashMap();
			/*
			HashMap expr_level = new HashMap();
			String target_input_file = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\BALL_Mullighan\\tpm\\Combat\\BALL_meta.txt";
			FileInputStream fstream = new FileInputStream(target_input_file);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
