package rnaseq.quantification.kallisto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Takes in a sorted list of transcripts and output unique geneSymbols
 * @author tshaw
 *
 */
public class FinalPrioritizedTranscripts {

	public static void main(String[] args) {
		
		try {
			
			String inputMatrixFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\Comprehensive_Clustering_Analysis\\AVG_Transcript.txt";
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\analysis\\Comprehensive_CAR-T_Analysis\\Comprehensive_Clustering_Analysis\\AVG_Transcript_Final.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			out.write(in.readLine() + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (!map.containsKey(split[3])) {
					out.write(str + "\n");
				}
				map.put(split[3], split[3]);
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
