package MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import statistics.general.MathTools;

/**
 * For genes with multiple expression value, take the mean or median expression value
 * @author tshaw
 *
 */
public class MergeGeneName {

	public static void main(String[] args) {
		
		try {
			String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PCGP_MB\\David_MB_11_7_14_GeneSymbol.txt";
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName_Collapse.txt";
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PCGP_MB\\David_MB_11_7_14_GeneSymbol_Collapsed.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			
			captureMean(inputFile, outputFile, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String type() {
		return "MISC";
	}
	public static String description() {
		return "For genes with multiple expression value, take the mean or median expression value";
	}
	public static String parameter_info() {
		return "[InputFile] [MEDIAN/AVERAGE/MAX][OutputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName.txt";
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PCGP_MB\\David_MB_11_7_14_GeneSymbol.txt";
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName_Collapse.txt";
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PCGP_MB\\David_MB_11_7_14_GeneSymbol_Collapsed.txt";
			String type = args[1]; 
			//int id = new Integer(args[2]) - 1;
			String outputFile = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\GangGeneList\\phospho_peptide_raw_data_GBM_Name_Collapse.txt";
			
			int id = 0;
			if (type.toUpperCase().equals("MEDIAN")) {
				captureMedian(inputFile, outputFile, id);
			} else if (type.toUpperCase().equals("AVERAGE")) {
				captureMean(inputFile, outputFile, id);
			} else if (type.toUpperCase().equals("MAX")) {
				captureMax(inputFile, outputFile, id);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void DavidDataset(String[] args) {
		
		try {
			
			//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName.txt";
			String inputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PCGP_MB\\David_MB_11_7_14_GeneSymbol.txt";
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\COMPLETE_FINAL_ANALYSIS\\HGG_EXAMPLE\\raw\\total\\Total_Proteome_GeneName_Collapse.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\PCGP_MB\\David_MB_11_7_14_GeneSymbol_Collapsed.txt";
			captureMean(inputFile, outputFile, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap captureMean(String inputFile, String outputFile, int tagIndex) {
		HashMap result = new HashMap();
		try {
			
			HashMap map = new HashMap();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
								
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				if (!str.contains("Infinity") && !str.contains("null")) {
					String[] split = str.split("\t");
					String[] tags = split[tagIndex].split(" ");
					System.out.println(split[tagIndex] + "\t" + tags.length);
					
					for (int j = 0; j < tags.length; j = j + 2) {
						String tag = tags[j];
						System.out.println(tag);
						if (map.containsKey(tag)) {
							String[] split2 = ((String)map.get(tag)).split("\t");
							String newStr = tag;
							for (int i = tagIndex + 1; i < split.length; i++) {
								newStr += "\t" + split2[i] + "," + split[i];
								
							}
							
							map.put(tag, newStr);
						} else {
							
							map.put(tag, str);
						}
					}
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				if (!key.equals("")) {
					String str = (String)map.get(key);
					String[] split = str.split("\t");
					if (!str.contains("Infinity") && !str.contains("null")) {
						String newStr = "\"" + key + "\"";
						for (int i = 1; i < split.length; i++) {
							
							String[] split2 = split[i].split(",");
							double[] values = new double[split2.length];
							for (int j = 0; j < split2.length; j++) {
								values[j] = new Double(split2[j]);
							}
							
							newStr += "\t" + MathTools.mean(values);
						}
						result.put(key, newStr);
					//if (!newStr.contains("Infinity") && !newStr.contains("null") && !newStr.contains("\tNaN")) {
						out.write(newStr + "\n");
					}
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static HashMap captureMax(String inputFile, String outputFile, int tagIndex) {
		HashMap result = new HashMap();
		try {
			
			HashMap map = new HashMap();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
								
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				if (!str.contains("Infinity") && !str.contains("null")) {
					String[] split = str.split("\t");
					String[] tags = split[tagIndex].split(" ");
					System.out.println(split[tagIndex] + "\t" + tags.length);
					
					for (int j = 0; j < tags.length; j = j + 2) {
						String tag = tags[j];
						System.out.println(tag);
						if (map.containsKey(tag)) {
							String[] split2 = ((String)map.get(tag)).split("\t");
							String newStr = tag;
							for (int i = tagIndex + 1; i < split.length; i++) {
								newStr += "\t" + split2[i] + "," + split[i];
								
							}
							
							map.put(tag, newStr);
						} else {
							
							map.put(tag, str);
						}
					}
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				if (!key.equals("")) {
					String str = (String)map.get(key);
					String[] split = str.split("\t");
					//String newStr = "\"" + key + "\"";
					String newStr = "\"" + key + "\"";
					if (!str.contains("Infinity") && !str.contains("null")) {
						for (int i = 1; i < split.length; i++) {
							
							String[] split2 = split[i].split(",");
							double[] values = new double[split2.length];
							for (int j = 0; j < split2.length; j++) {
								values[j] = new Double(split2[j]);
							}
							
							newStr += "\t" + MathTools.max(values);
						}
						result.put(key, newStr);
						
						out.write(newStr + "\n");
					}
					//out.write(newStr + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static HashMap captureMedian(String inputFile, String outputFile, int tagIndex) {
		HashMap result = new HashMap();
		try {
			
			HashMap map = new HashMap();
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			out.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				if (!str.contains("Infinity") && !str.contains("null")) {
					String[] split = str.split("\t");
					if (map.containsKey(split[tagIndex])) {
						String[] split2 = ((String)map.get(split[tagIndex])).split("\t");
						String newStr = split[tagIndex];
						for (int i = 1; i < split.length; i++) {
							newStr += "\t" + split2[i] + "," + split[i];
							
						}
						
						map.put(split[tagIndex], newStr);
					} else {
						map.put(split[tagIndex], str);
					}
				}
			}
			in.close();
			
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				if (!key.equals("")) {
					String str = (String)map.get(key);
					String[] split = str.split("\t");
					String newStr = split[tagIndex];
					if (!str.contains("Infinity") && !str.contains("null")) {
						for (int i = 1; i < split.length; i++) {
							
							String[] split2 = split[i].split(",");
							double[] values = new double[split2.length];
							for (int j = 0; j < split2.length; j++) {
								values[j] = new Double(split2[j]);
							}
							
							newStr += "\t" + MathTools.median(values);
						}
						result.put(split[tagIndex], newStr);
						
						out.write(newStr + "\n");
					}
					//out.write(newStr + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
