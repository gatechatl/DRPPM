package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class JuncSalvagerWilcoxTestPostProcessing {

	
	public static void execute(String[] args) {
		
		try {
			
			String inputPCGPFolder = args[0]; // PCGP_903_FPKM_ECM_filtcol.txt
			String inputInteractome = args[1];
			String outputMetaSummary = args[2];
			FileInputStream fstream = new FileInputStream(inputPCGPFolder);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
