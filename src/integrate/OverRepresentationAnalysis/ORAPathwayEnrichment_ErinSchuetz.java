package integrate.OverRepresentationAnalysis;

import idconversion.cross_species.HumanMouseGeneNameConversion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import statistics.general.MathTools;

public class ORAPathwayEnrichment_ErinSchuetz {

	public static void main(String[] args) {
		
		try {
			String[] met_files = {"hmdb_csf_ALL.txt", "hmdb_csf_DN.txt", "hmdb_csf_UP.txt", "hmdb_plasma_ALL.txt", "hmdb_plasma_DN.txt", "hmdb_plasma_UP.txt", "hmdb_combined_ALL.txt"};
			String[] brain_microarray_files = {"KO_v_WT_brain.txt", "KO_v_WT_brain_DN.txt", "KO_v_WT_brain_UP.txt", "KO_v_WT_brain.txt", "KO_v_WT_brain_DN.txt", "KO_v_WT_brain_UP.txt", "KO_v_WT_COMBINED.txt"};
			String[] kidney_microarray_files = {"KO_v_WT_kidney.txt", "KO_v_WT_kidney_DN.txt", "KO_v_WT_kidney_UP.txt", "KO_v_WT_kidney.txt", "KO_v_WT_kidney_DN.txt", "KO_v_WT_kidney_UP.txt", "KO_v_WT_COMBINED.txt"};
			String[] liver_microarray_files = {"KO_v_WT_liver.txt", "KO_v_WT_liver_DN.txt", "KO_v_WT_liver_UP.txt", "KO_v_WT_liver.txt", "KO_v_WT_liver_DN.txt", "KO_v_WT_liver_UP.txt", "KO_v_WT_COMBINED.txt"};
			String type = "kidney";
			String[] output_files = {"ErinSchuetz_ORA_csf_" + type + "_ALL.txt", "ErinSchuetz_ORA_csf_" + type + "_DN.txt", "ErinSchuetz_ORA_csf_" + type + "_UP.txt", "ErinSchuetz_ORA_plasma_" + type + "_ALL.txt", "ErinSchuetz_ORA_plasma_" + type + "_DN.txt", "ErinSchuetz_ORA_plasma_" + type + "_UP.txt", "ErinSchuetz_ORA_ALL.txt"};
			//String[] output_files = {"ErinSchuetz_ORA_csf_" + type + "_ALL_MetOnly.txt", "ErinSchuetz_ORA_csf_" + type + "_DN_MetOnly.txt", "ErinSchuetz_ORA_csf_" + type + "_UP_MetOnly.txt", "ErinSchuetz_ORA_plasma_" + type + "_ALL_MetOnly.txt", "ErinSchuetz_ORA_plasma_" + type + "_DN_MetOnly.txt", "ErinSchuetz_ORA_plasma_" + type + "_UP_MetOnly.txt", "ErinSchuetz_ORA_ALL_MetOnly.txt"};
			
			for (int k = 0; k < met_files.length; k++) {
				String metInputFile = met_files[k];
				String microarrayInputFile = kidney_microarray_files[k];				
				//String microarrayInputFile = brain_microarray_files[k];
				//String microarrayInputFile = liver_microarray_files[k];				
				
				String outputFileName = output_files[k];
				String metabolomic_file = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\Metabolomic\\" + metInputFile;//args[0];
				String geneName_file = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\DAVID_Microarray_Analysis\\" + microarrayInputFile; //args[1];			
				/*
				String metabolomic_file = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\Metabolomic\\hmdb_plasma_DN.txt";//args[0];
				String geneName_file = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\DAVID_Microarray_Analysis\\KO_v_WT_kidney_DN.txt"; //args[1];
				*/
				String allFile = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\Metabolomic\\all_hmdb.txt";
				double metabolite_pvalue = new Double("0.05"); // args[2]); // metabolite pvalue
				double geneName_pvalue = new Double("0.05"); //args[3]); // metabolite pvalue
				String pathwayFile = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\NetworkAnalysis\\SMPDB\\Pathway_metabolites_geneName.txt";
				String hmdb2metnameFile = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\NetworkAnalysis\\SMPDB\\hmdb2metabolitename.txt";
				String geneName2Uniprot_File = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\NetworkAnalysis\\SMPDB\\proteins.txt"; 
				
				String mouse2human_file = "C:\\Users\\tshaw\\Desktop\\RNASEQ\\REFERENCE\\Mouse2Human\\hs_mm_homo_r66.txt";
				
				HashMap mouse2human = HumanMouseGeneNameConversion.mouse2human(mouse2human_file);
				String strict = "false";
				String outputFile = "C:\\Users\\tshaw\\Desktop\\METABOLOMICS\\Project\\ErinSchuetz\\ORA_Analysis\\" + outputFileName;
				
				boolean hmdb_only = strict.equals("true");
				
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("PathwayName\tfisherPvalue\tOverRepresentationFlag\tGeneList\tNumHit\tNumInPathway\tNumDiff\tNumBackground\tWebLink\n");
				HashMap diff_map = new HashMap();
				HashMap background = new HashMap();
				HashMap known_background = new HashMap();
				
				FileInputStream fstream = new FileInputStream(allFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));	
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					if (!str.equals("")) {
						if (hmdb_only) {
							if (str.contains("HMDB")) {
								background.put(str.trim(), str.trim());
							}
						} else {
							if (mouse2human.containsKey(str)) {
								str = (String)mouse2human.get(str);
							}
							background.put(str.trim(), str.trim());
						}
					}
				}
				in.close();
				System.out.println(background.size());
				
				HashMap pathway = new HashMap();
				
				String pathway_name = "";
				fstream = new FileInputStream(pathwayFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));		
				while (in.ready()) {
					String str = in.readLine();
					
					if (str.contains(">")) {
						pathway_name = str.replaceAll(">", "");
					} else {
						str = str.toUpperCase();
						if (background.containsKey(str.trim())) {
							known_background.put(str.trim(), str);
							if (pathway.containsKey(pathway_name)) {
								LinkedList list = (LinkedList)pathway.get(pathway_name);
								if (!list.contains(str.trim())) {
									list.add(str.trim());
								}							
								pathway.put(pathway_name, list);
							} else {
								LinkedList list = new LinkedList();
								list.add(str.trim());
								pathway.put(pathway_name, list);
							}
						}
					}
				}
				in.close();
				
				//System.out.println(known_background.size());
				fstream = new FileInputStream(metabolomic_file);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					double pvalue = new Double(split[4]);
					String metaboliteName = split[0].replaceAll("\"", "");
					if (pvalue < metabolite_pvalue && known_background.containsKey(metaboliteName)) {
						diff_map.put(metaboliteName, metaboliteName);
						//System.out.println(metaboliteName);
					}
					//background.put(metaboliteName, metaboliteName);
				}
				in.close();
				
				//System.out.println(diff_map.size());
				
				HashMap hmdb2pw = new HashMap();
				HashMap hmdb2metaname = new HashMap();
				fstream = new FileInputStream(hmdb2metnameFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));	
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					hmdb2metaname.put(split[0], split[1]);
					hmdb2pw.put(split[0], split[2]);
				}
				in.close();			
				
				HashMap gene2uniprot = new HashMap();
				fstream = new FileInputStream(geneName2Uniprot_File);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));	
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					if (gene2uniprot.containsKey(split[8])) {
						LinkedList list = (LinkedList)gene2uniprot.get(split[8]);
						list.add(split[3]);
						gene2uniprot.put(split[8], list);
					} else {
						LinkedList list = new LinkedList();
						list.add(split[3]);
						gene2uniprot.put(split[8], list);
					}
					
				}
				in.close();			
				
				//System.out.println(diff_map.size());
				
				fstream = new FileInputStream(geneName_file);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));	
				in.readLine();
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String geneName = (String)mouse2human.get(split[3]);
					//System.out.println(geneName);
					double pvalue = new Double(split[5]);
					//background.put(geneName, geneName);
					if (pvalue < geneName_pvalue && known_background.containsKey(geneName)) {
					//if (pvalue < geneName_pvalue) {
						diff_map.put(geneName, geneName);					
					}
				}
				in.close();
				//System.out.println(diff_map.size());
				//System.exit(0);
				
				
				Iterator itr = pathway.keySet().iterator();
				while (itr.hasNext()) {
					String pathway_str = (String)itr.next();
					int overlap = 0;
					int missing = 0;
					int diff = 0;
					String hits = "";
					
					String highlight = "";
					LinkedList list = (LinkedList)pathway.get(pathway_str);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						String object = (String)itr2.next();
						if (diff_map.containsKey(object)) {
							if (hmdb2metaname.containsKey(object)) {
								String pw = (String)hmdb2pw.get(object);							
								if (highlight.equals("")) {
									highlight = "?highlight[compounds][]=" + pw;
								} else {
									highlight += "&highlight[compounds][]=" + pw;
								}
								object = object + "[" + (String)hmdb2metaname.get(object) + "]";
								
							} else {
								
								if (gene2uniprot.containsKey(object)) {
									LinkedList list2 = (LinkedList)gene2uniprot.get(object);
									Iterator itr3 = list2.iterator();
									while (itr3.hasNext()) {
										String uniprot = (String)itr3.next();
										if (highlight.equals("")) {
											highlight = "?highlight[proteins][]=" + uniprot;
										} else {
											if (!highlight.contains(uniprot)) {
												highlight += "&highlight[proteins][]=" + uniprot;
											}
										}
									}
								}
							}
							if (hits.equals("")) {
								hits = object;
							} else {
								hits += "," + object;
							}
							overlap++;
						} else {
							missing++;
						}
						
					}
					diff = diff_map.size() - overlap;
					
					String link = "http://smpdb.ca/view/" + getSMP(pathway_str) + highlight;
					int background_set = known_background.size() - overlap - missing - diff;
					double pvalue = MathTools.fisherTest(overlap, missing, diff, background_set);
					
					if ((new Double(overlap) / missing) > (new Double(diff) / background_set)) {
						
						out.write(removeSMP(pathway_str) + "\t" + pvalue + "\t" + "YES" + "\t" + hits + "\t" + overlap + "\t" + missing + "\t" + diff + "\t" + background_set + "\t" + link + "\n");
						System.out.println(removeSMP(pathway_str) + "\t" + pvalue + "\t" + "YES" + "\t" + hits + "\t" + overlap + "\t" + missing + "\t" + diff + "\t" + background_set + "\t" + link);
					} else {
						out.write(removeSMP(pathway_str) + "\t" + pvalue + "\t" + "NO" + "\t" + hits + "\t" + overlap + "\t" + missing + "\t" + diff + "\t" + background_set + "\t" + link + "\n");
						System.out.println(removeSMP(pathway_str) + "\t" + pvalue + "\t" + "NO" + "\t" + hits + "\t" + overlap + "\t" + missing + "\t" + diff + "\t" + background_set + "\t" + link);
					}					
				
				}
				System.out.println(diff_map.size());
				out.close();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getSMP(String str) {
		String[] split = str.split("_");
		return split[split.length - 1];
	}
	public static String removeSMP(String str) {
		String[] split = str.split("_");
		String result = "";
		for (int i = 0; i < split.length - 1; i++) {
			if (result.equals("")) {
				result = split[i];
			} else {
				result += "_" + split[i];
			}
		}
		return result;
	}
}
