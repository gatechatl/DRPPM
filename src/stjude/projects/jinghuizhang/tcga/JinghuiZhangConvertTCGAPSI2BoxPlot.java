package stjude.projects.jinghuizhang.tcga;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Convert the TCGA PSI values to boxplot friendly plots.
 * @author tshaw
 *
 */
public class JinghuiZhangConvertTCGAPSI2BoxPlot {

	public static void main(String[] args) {
		
		try {
			
			HashMap tcga2type = new HashMap();
			HashMap tcga2disease = new HashMap();
			String inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\combined_meta_TCGA.txt";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String tcga_id = split[4];
				String type = split[2];
				String disease = split[1].replaceAll(" ",  "_").replaceAll(",", "");
				tcga2type.put(tcga_id, type);
				tcga2disease.put(tcga_id, disease);
			}
			in.close();
			
			String outputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\PSI_boxplot\\tcga_psi_boxplot.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tPSI\tType\tDisease\n");
			inputFile = "\\\\gsc.stjude.org\\project_space\\zhanggrp\\AltSpliceAtlas\\common\\TCGA_Reference\\TCGA_ENSG00000115414_25_FN1_header.txt";
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 6; i < split_header.length; i++) {
					String[] name = split_header[i].split("-");
					String tcga_id = name[0] + "-" + name[1] + "-" + name[2];
					String type = "NA";
					String disease = "NA";
					double psi = -1;
					if (tcga2type.containsKey(tcga_id)) {
						type = (String)tcga2type.get(tcga_id);
						disease = (String)tcga2disease.get(tcga_id);
						if (!split[i].equals("nan")) {
							out.write(tcga_id + "\t" + split[i] + "\t" + type + "\t" + disease + "\n");
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
