package rnaseq.splicing.cseminer.violinbarplotdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Automatically generate the meta table path from the CSEMiner Run
 * The file will be used for the violin and barplot matrix generation
 * @author 4472414
 *
 */
public class CSEMinerViolinAndBarPlotGenerateMetaTable {

	public static String type() {
		return "CSE-Miner";
	}
	public static String description() {
		return "Generates the meta table for the barplot and violin plot.";
	}
	public static String parameter_info() {
		return "[folderPath] [outputMetaTableFile]";
	}
	public static void execute(String[] args) {
						
		try {
			
			HashMap ped_sample_map = new HashMap();
			String folderPath = args[0];
			String outputMetaTableFile = args[1];
			

			FileWriter fwriter = new FileWriter(outputMetaTableFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			File folder = new File(folderPath);
			HashMap sampleName_map = new HashMap();
			HashMap quartile_map = new HashMap();
			HashMap fpkm_matrix_map = new HashMap();
			for (File file: folder.listFiles()) {				
				if (file.getName().contains("_rank_1FPKM_quartile.txt")) {					
					String sampleName = file.getName().replace("_rank_1FPKM_quartile.txt", "");					
					quartile_map.put(sampleName, file.getPath());
					sampleName_map.put(sampleName, "");
				}
				if (file.getName().contains("_rank_1FPKM.txt")) {					
					String sampleName = file.getName().replace("_rank_1FPKM.txt", "");
					fpkm_matrix_map.put(sampleName, file.getPath());
					sampleName_map.put(sampleName, "");
				}
			}
			
			out.write("SampleName\tQuartile\tFPKM_Matrix\n");
			Iterator itr = sampleName_map.keySet().iterator();
			while (itr.hasNext()) {
				String sampleName = (String)itr.next();
				String quartile = (String)quartile_map.get(sampleName);
				String fpkm_matrix = (String)fpkm_matrix_map.get(sampleName);		
				out.write(sampleName + "\t" + quartile + "\t" + fpkm_matrix + "\n");
			}
			out.close();
																		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
