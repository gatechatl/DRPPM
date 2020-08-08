package stjude.projects.leventaki;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LeventakiGenerateCNVPecanInput {

	
	public static void main(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String outputFile = "U:\\LagginServerTempFolder\\ALCL_Leventaki\\ALCL_GenomeLandscape\\cnv_input_pecan.txt";
			
			String header = "disease\tsampletype\tgene\tcnv\tTARGET_CASE_ID\n";
			//disease	sampletype	gene	cnv	TARGET_CASE_ID
			//ALL	diagnosis	CDKN2A	deletion	10-CAAABD

			HashMap group = new HashMap();
			String sample_group = "U:\\LagginServerTempFolder\\ALCL_Leventaki\\ALCL_GenomeLandscape\\PecanFiles\\SampleGroup_ExprMethylCluster_poster_v2_20180307.txt";
			FileInputStream fstream = new FileInputStream(sample_group);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din)); 			
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				group.put(split[1].split("_")[0], split[1]);
			}
			in.close();
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write(header + "\n");
			
			String refFile = "U:\\LagginServerTempFolder\\ALCL_Leventaki\\ALCL_GenomeLandscape\\cnv_input.txt";
			fstream = new FileInputStream(refFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din)); 			
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				
				String type = "amplification";
				String genes = split[4];
				
				double log2FC = log2FC = new Double(split[5]); 
				
				if (log2FC < 0) {
					type = "deletion";
				}
				for (String gene: genes.split(",")) {
					if (group.containsKey(split[0].replaceAll("_", ""))) {
						String value = "ALCL\tdiagnosis\t" + gene + "\t" + type + "\t" + group.get(split[0].replaceAll("_", "")) + "\n";
						if (!map.containsKey(value)) {
							out.write(value + "\n");
							map.put(value, "");
						}
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
