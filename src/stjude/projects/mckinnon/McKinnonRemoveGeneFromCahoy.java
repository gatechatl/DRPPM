package stjude.projects.mckinnon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class McKinnonRemoveGeneFromCahoy {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap purkinje = new HashMap();
			String inputFile = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\mRNAseq_RNASEQ\\intron_retention\\gsea\\Kratz_et_al_purkinje.gmt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (String stuff: split) {
					purkinje.put(stuff, stuff);
				}
			}
			in.close();
			
			String outputFile = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\mRNAseq_RNASEQ\\intron_retention\\gsea\\Neuron_Short.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			inputFile = "Z:\\ResearchHome\\ProjectSpace\\mckingrp\\ATTEL\\common\\mRNAseq_RNASEQ\\intron_retention\\gsea\\Cahoy_Neuron.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				if (!purkinje.containsKey(str)) {
					out.write(str + "\n");
				} else {
					System.out.println(str);
				}
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
			
			
}
