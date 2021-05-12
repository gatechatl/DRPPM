package moffitt.projects.paulorodriguez.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PauloRodriguezCleanGeneName {

	public static void main(String[] args) {
		
		try {
			// parsing the runtime config file
			HashMap sample_list = new HashMap();
			String melanoma_type = "UVM";
			
			//String inputMatrix = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/SKCM_EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.tsv";
			//String outputMatrix = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/SKCM_EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.clean.tsv";
			
			String inputMatrix = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.tsv";
			String outputMatrix = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.clean.tsv";
			
			FileWriter fwriter = new FileWriter(outputMatrix);
			BufferedWriter out = new BufferedWriter(fwriter);			
	
			FileInputStream fstream = new FileInputStream(inputMatrix);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].contains("?")) {
					out.write(split[0].split("\\|")[0]);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
						
					}
					out.write("\n");
				}
			}
			in.close();
			out.close();
			
			/*
			
			sample_list = new HashMap();
			
			inputMatrix = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/SKCM_BRAFV700E_EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.tsv";
			outputMatrix = "/home/gatechatl/Projects/PauloProjects/TCGAPanCanAtlas/SKCM_BRAFV700E_EBPlusPlusAdjustPANCAN_IlluminaHiSeq_RNASeqV2.geneExp.clean.tsv";
			
			
			fwriter = new FileWriter(outputMatrix);
			out = new BufferedWriter(fwriter);			
	
			fstream = new FileInputStream(inputMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!split[0].contains("?")) {
					out.write(split[0].split("\\|")[0]);
					for (int i = 1; i < split.length; i++) {
						out.write("\t" + split[i]);
						
					}
					out.write("\n");
				}
			}
			in.close();
			out.close();
			
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
