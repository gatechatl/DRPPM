package MouseModelQC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class FPKMBoxPlotOfGeneKOSampleSpecific {
	
	public static void execute(String[] args) {

		HashMap[][] map = null;
		try {

			String exon_coverage_file = args[0];
			String path_exon_total_file = args[1];
			String gene_names_str = args[2];
			String sampleType_str = args[3];
			String sampleFilter_str = args[4];
			String boxplot_script = args[5];
			
			String[] gene_names = gene_names_str.split(",");
			
			
			String[] sampleType = sampleType_str.split(",");
			
			String[] sampleFilter = sampleFilter_str.split(",");
			
			map = new HashMap[gene_names.length][sampleType.length + 1];
			for (int i = 0; i < sampleType.length + 1; i++) {
				for (int j = 0; j < gene_names.length; j++) {
					map[j][i] = new HashMap();
				}
			}
			
			HashMap direction = new HashMap();
			//String fileName = "\\\\sonas01\\clusterhome\\tshaw\\RNASEQ\\Mckinnon\\FPKM_07282014\\SJMMATTEL_RNAseq_Exon_Coverage.txt";
			//String fileName = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\Mckinnon\\QC_KO_05272014\\SJMMATTEL_RNAseq_Exon_Coverage.txt";
			String fileName = exon_coverage_file;
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String coord = split[0].replaceAll("chr", "");
				String dir = split[1];
				String gene = split[4];
				String refgene = split[6];
				for (int j = 0; j < gene_names.length; j++) {
					
					String geneName = gene_names[j];
					if (gene.equals(geneName)) {
						//direction.put("Lig4", dir);
						direction.put(geneName, dir);					
						map[j][0].put(map[j][0].size() + 1, coord);
						for (int i = 1; i < sampleType.length + 1; i++) {
							map[j][i].put(coord, "");
						}
					}
					/*Lig4.put(Lig4.size() + 1, coord);
					System.out.println(coord + "\t" + gene + "\t" + refgene);
					Lig4_Lig4.put(coord, "");
					Lig4_POT.put(coord, "");
					Lig4_AT.put(coord, "");
					Lig4_BRCA2.put(coord, "");*/
				}
				/*if (gene.equals("Lig4")) {
					direction.put("Lig4", dir);
					Lig4.put(Lig4.size() + 1, coord);
					System.out.println(coord + "\t" + gene + "\t" + refgene);
					Lig4_Lig4.put(coord, "");
					Lig4_POT.put(coord, "");
					Lig4_AT.put(coord, "");
					Lig4_BRCA2.put(coord, "");
				}
				
				if (gene.equals("Tdp1")) {
					direction.put("Tdp1", dir);
					Tdp1.put(Tdp1.size() + 1, coord);
					System.out.println(coord + "\t" + gene + "\t" + refgene);
					Tdp1_Lig4.put(coord, "");
					Tdp1_POT.put(coord, "");
					Tdp1_AT.put(coord, "");
					Tdp1_BRCA2.put(coord, "");
				}
				if (gene.equals("Atm")) {
					ATM.put(ATM.size() + 1, coord);
					System.out.println(coord + "\t" + gene + "\t" + refgene);
					ATM_Lig4.put(coord, "");
					ATM_POT.put(coord, "");
					ATM_AT.put(coord, "");
					ATM_BRCA2.put(coord, "");
				}
				if (gene.equals("Pot1a")) {
					POT1a.put(POT1a.size() + 1, coord);
					System.out.println(coord + "\t" + gene + "\t" + refgene);
					POT1a_Lig4.put(coord, "");
					POT1a_POT.put(coord, "");
					POT1a_AT.put(coord, "");
					POT1a_BRCA2.put(coord, "");
				}
				if (gene.equals("Pot1b")) {
					POT1b.put(POT1b.size() + 1, coord);
					System.out.println(coord + "\t" + gene + "\t" + refgene);
					POT1b_Lig4.put(coord, "");
					POT1b_POT.put(coord, "");
					POT1b_AT.put(coord, "");
					POT1b_BRCA2.put(coord, "");
				}
				if (gene.equals("Trp53")) {
					P53.put(P53.size() + 1, coord);
					System.out.println(coord + "\t" + gene + "\t" + refgene);
					P53_Lig4.put(coord, "");
					P53_POT.put(coord, "");
					P53_AT.put(coord, "");
					P53_BRCA2.put(coord, "");
				}
				if (gene.equals("Brca2")) {
					BRCA2.put(BRCA2.size() + 1, coord);
					System.out.println(coord + "\t" + gene + "\t" + refgene);
					BRCA2_Lig4.put(coord, "");
					BRCA2_POT.put(coord, "");
					BRCA2_AT.put(coord, "");
					BRCA2_BRCA2.put(coord, "");
				}*/
			}
			in.close();
			
			System.out.println("Load coordinates into sample/gene combo");
			
			for (int j = 0; j < gene_names.length; j++) {	
				map[j][0] = flip(map[j][0]);
			}
			
			System.out.println("Flip map");
			
			
			/**
			 * Might need to change this strategy later on
			 * This will only work if the KO gene is present in the filename
			 */
			File[] files = new File(path_exon_total_file).listFiles();
			for (File file: files) {
				for (String sampleTerm: sampleFilter) {
					if (!file.getPath().contains("_total.txt") && file.getName().contains(sampleTerm)) {
						
						boolean[] boo = new boolean[sampleType.length];
						for (int i = 0; i < sampleType.length; i++) {
							String geneNameOnFile = sampleType[i];
							boo[i] = false;
							if (file.getName().contains(geneNameOnFile)) {
								boo[i] = true;
							}
							
						}
						addDataPoint(file.getPath(), boo, map, gene_names, sampleType);
						System.out.println("Loaded data for: " + file.getPath());				
					}
				}
				
			}
			System.out.println("Writing exon info to outputfile");
			for (int j = 0; j < gene_names.length; j++) {
				writeFile(gene_names[j] + "_EXON.txt", map[j][0], map, sampleType, j);
			}
			
			
        	FileWriter fwriter = new FileWriter(boxplot_script);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write(boxplot(gene_names, sampleType) + "\n");
            out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String boxplot(String[] gene_names, String[] sampleType) {
		String script = "";
		for (String geneKO: gene_names) {
			script += "allDat = read.table(\"" + geneKO + "_EXON.txt\", header=FALSE, row.names=1 );\n";
			
			String lastTag = "";
			for (int i = 0; i < sampleType.length; i++) {
				script += sampleType[i] + "_length = allDat[1," + (i + 1) + "];\n";
				//pot_length = allDat[1,2];
				//atm_length = allDat[1,3];
				
				if (i == 0) {
					script += sampleType[i] + " = t(allDat[," + (1 + sampleType.length) + ":(" + (1 + sampleType.length) + "+" + sampleType[i] + "_length-1)]) * 1e6;\n";
					lastTag = "(" + (1 + sampleType.length) + "+" + sampleType[i] + "_length";
				} else {
					script += sampleType[i] + " = t(allDat[," + lastTag + "):" + lastTag + "+" + sampleType[i] + "_length-1)]) * 1e6;\n";
					lastTag = lastTag + "+" + sampleType[i] + "_length";
				}
				//pot = t(allDat[,(4+lig4_length):(4+lig4_length+pot_length-1)]) * 1e6
				//atm = t(allDat[,(4+lig4_length+pot_length):(4+lig4_length+pot_length+atm_length-1)]) * 1e6
			}
			
	
			script += "png(file = \"" + geneKO + "_Exon.png\", width=1000,height=1000);\n";
			
			
			script += "par(mfrow=c(" + (sampleType.length) + ",1));\n";
	
			for (int i = 0; i < sampleType.length; i++) {
				script += "boxplot(" + sampleType[i] + ", ylim=c(0,7),main=\"" + sampleType[i] + "\",xlab=\"EXON Number\",cex.main=3)\n";
			}
			//boxplot(pot, ylim=c(0,7),main="POT",xlab="EXON Number",cex.main=3)
			//boxplot(atm, ylim=c(0,7),main="ATMTDP1",xlab="EXON Number",cex.main=3)
			script += "dev.off();\n";

		}
		return script;
	}
	public static HashMap flip(HashMap map) {
		HashMap newMap = new HashMap();
		int j = 1;
		for (int i = map.size(); i >= 1; i--) {
			String stuff = (String)map.get(i);
			newMap.put(j, stuff);
			j++;
		}
		return newMap;
	}
	public static void writeFile(String fileName, HashMap exon_num, HashMap[][] map, String[] sampleType, int geneNameIndex) { //HashMap lig4, HashMap pot, HashMap AT, HashMap brca2) {
		try {
			
        	FileWriter fwriter = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fwriter);
            for (int k = 1; k <= exon_num.size(); k++) {
				String coord = (String)exon_num.get(k);
				
				int j = geneNameIndex;
				out.write(k + ""); // + "\t" + lig4_values.split("\t").length + "\t" + pot_values.split("\t").length + "\t" + AT_values.split("\t").length + "\t" + brca2_values.split("\t").length + "\t" + lig4_values + "\t" + pot_values + "\t" + AT_values + "\t" + brca2_values + "\n");
				String values = "";
				for (int i = 1; i < sampleType.length + 1; i++) {
					values = (String)map[j][i].get(coord);
					out.write("\t" + values.split("\t").length);
					
				}
				
				for (int i = 1; i < sampleType.length + 1; i++) {
					values = (String)map[j][i].get(coord);
					out.write("\t" + values);
					
				}
				out.write("\n");
			}
			
            out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*public static void writeFile(String fileName, HashMap exon_num, HashMap lig4, HashMap pot, HashMap AT, HashMap brca2) {
		try {
			
        	FileWriter fwriter = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fwriter);
            for (int i = 1; i <= exon_num.size(); i++) {
				String coord = (String)exon_num.get(i);
				String lig4_values = (String)lig4.get(coord);
				String pot_values = (String)pot.get(coord);
				String AT_values = (String)AT.get(coord);
				String brca2_values = (String)brca2.get(coord);
				out.write(i + "\t" + lig4_values.split("\t").length + "\t" + pot_values.split("\t").length + "\t" + AT_values.split("\t").length + "\t" + brca2_values.split("\t").length + "\t" + lig4_values + "\t" + pot_values + "\t" + AT_values + "\t" + brca2_values + "\n");
				
			}
            out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	public static void addDataPoint(String fileName, boolean[] boo, HashMap[][] map, String[] gene_names, String[] sampleType) {
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			double total = getTotal(fileName.replaceAll("_Exon.txt", "_total.txt"));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");

				for (int j = 0; j < gene_names.length; j++) {
					String geneName = gene_names[j];
					//map[0][j].put(map[0][j].size() + 1, coord);
					for (int i = 1; i < sampleType.length + 1; i++) {
						if (map[j][i].containsKey(split[0]) && boo[i - 1]) {
							String val = (String)map[j][i].get(split[0]);
							if (val.equals("")) {
								map[j][i].put(split[0], "" + new Double(split[1]) / total);
							} else {
								map[j][i].put(split[0],  val + "\t" + "" + new Double(split[1]) / total);
							}
						}
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double getTotal(String fileName) {
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].equals("Total")) {
					return new Double(split[1]);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Double.NaN;
	}
}

