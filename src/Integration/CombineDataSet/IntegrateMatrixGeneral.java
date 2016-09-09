package Integration.CombineDataSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Combine kinase
 * @author tshaw
 *
 */
public class IntegrateMatrixGeneral {

	public static void main(String[] args) {
		try {

			/*
			String kinaseListFile = args[0]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\Transcription_Factor_List.txt";
			String kinaseFile = args[1]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\hongbo_phosphosite_matrix_normalized_whole_v4_20160323_IKAP.txt";
			String phosphosite_kinsub_file = args[2]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\Mouse_Kinase_Substrate.txt";
			String phosphoIDFile = args[3]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\id_all_pep_quan.txt";
			String wholeFile = args[4]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\corrected_data_permutatedP_FDR_trimmed.txt";
			String outputFile = args[5]; //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\TFInformation.txt";
*/
			
			String kinaseListFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\RibosomeProtein.txt";
			String kinaseFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\hongbo_phosphosite_matrix_normalized_whole_v4_20160323_IKAP.txt";
			String phosphosite_kinsub_file ="C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\Mouse_Kinase_Substrate.txt";
			String phosphoIDFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\id_all_pep_quan.txt";
			String wholeFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\corrected_data_permutatedP_FDR_trimmed.txt";
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\RibosomeProteinsInformation.txt";
			HashMap kinase_geneName = new HashMap();
			//String kinaseListFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\MM_Kinase_List.txt";

			FileInputStream fstream = new FileInputStream(kinaseListFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().trim();
				kinase_geneName.put(str.toUpperCase(), str.toUpperCase());
			}
			in.close();									
			/*
			HashMap pho_class = new HashMap();
			String pho_class_file = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\phospho_cluster_class.txt";
			fstream = new FileInputStream(pho_class_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = split[0];
				peptide = peptide.replaceAll("\\@", "#");				
				peptide = peptide.replaceAll("\\%", "#");
				peptide = peptide.replaceAll("\\*", "#");				
				peptide = peptide.split("\\.")[1];				
				pho_class.put(peptide, split[1]);
			}
			in.close();
			*/
			/*HashMap whl_class = new HashMap();
			String whl_class_file = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\wholeproteome_cluster_class.txt";
			fstream = new FileInputStream(whl_class_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				whl_class.put(split[0], split[1]);
				System.out.println(split[0]);
			}
			in.close();
			*/
			
			HashMap kinase_map = new HashMap();
			
			fstream = new FileInputStream(kinaseFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				kinase_map.put(split[0], str);
			}
			in.close();
			
			
			HashMap kinase_substrate = new HashMap();
			fstream = new FileInputStream(phosphosite_kinsub_file);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[0].toUpperCase();
				
				String tag = "";
				if (split[3].equals("CONVERT_UNIPROT")) {
					tag = split[5] + "_" + split[8];
				} else {
					tag = split[3] + "_" + split[4];
				}
				if (kinase_substrate.containsKey(tag)) {
					LinkedList list = (LinkedList)kinase_substrate.get(tag);
					if (!list.contains(kinase)) {
						list.add(kinase);
					}
					kinase_substrate.put(tag, list);					
				} else {
					LinkedList list = new LinkedList();					
					list.add(kinase);
					kinase_substrate.put(tag, list);
				}
			}
			in.close();
			
			HashMap peptide2accession = new HashMap();
			HashMap peptide2protein = new HashMap();
			HashMap peptide2kinase = new HashMap();
			HashMap protein2peptide = new HashMap();
			HashMap phosphoData = new HashMap();
			
			fstream = new FileInputStream(phosphoIDFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = split[0];
				peptide = peptide.replaceAll("\\@", "#");				
				peptide = peptide.replaceAll("\\%", "#");
				peptide = peptide.replaceAll("\\*", "#");				
				peptide = peptide.split("\\.")[1];
				
				String protein_name = split[2].split(":")[0];
				String accession = protein_name.split("\\|")[1];
				
				String sites = split[14];
				
				String uniq_site = accession + "_" + sites;
				if (!peptide2accession.containsKey(peptide)) {
					peptide2accession.put(peptide, uniq_site);
				}
				if (protein2peptide.containsKey(protein_name)) {
					LinkedList list = (LinkedList)protein2peptide.get(protein_name);
					if (!list.contains(peptide)) {
						list.add(peptide);
					}
					protein2peptide.put(protein_name, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(peptide);
					protein2peptide.put(protein_name, list);					
				}
				if (peptide2protein.containsKey(peptide)) {
					LinkedList list = (LinkedList)peptide2protein.get(peptide);
					if (!list.contains(protein_name + ":" + sites)) {
						list.add(protein_name + ":" + sites);
					}
					peptide2protein.put(peptide, list);					
				} else {
					LinkedList list = new LinkedList();
					list.add(protein_name + ":" + sites);
					peptide2protein.put(peptide, list);
				}
				
				LinkedList kinase_list = new LinkedList();
				for (String site: sites.split(",")) {
					String protein_site = accession + "_" + site;
					if (kinase_substrate.containsKey(protein_site)) {
						LinkedList list = (LinkedList)kinase_substrate.get(protein_site);
						Iterator itr = list.iterator();
						while (itr.hasNext()) {
							String kinase = (String)itr.next();
							if (!kinase_list.contains(kinase)) {
								kinase_list.add(kinase);
							}							
						}												
					}
				}
				
				String kinase_str = "";
				Iterator itr = kinase_list.iterator();
				while (itr.hasNext()) {
					String kinase = (String)itr.next();
					if (peptide2kinase.containsKey(peptide)) {
						LinkedList list = (LinkedList)peptide2kinase.get(peptide);
						if (!list.contains(kinase)) {
							list.add(kinase);
						}
						peptide2kinase.put(peptide, list);					
					} else {
						LinkedList list = new LinkedList();
						list.add(kinase);
						peptide2kinase.put(peptide, list);
					}					
				}	
				String line = "";
				for (int i = 16; i < split.length; i++) {
					if (line.equals("")) {
						line = split[i];
					} else {
						line += "\t" + split[i];
					}
				}
				
				String protein_sites = "";
				if (peptide2protein.containsKey(peptide)) {
					LinkedList list = (LinkedList)peptide2protein.get(peptide);
					itr = list.iterator();
					while (itr.hasNext()) {
						String protein_site = (String)itr.next();
						if (!protein_site.contains("tr\\|")) {
							protein_sites += protein_site + ",";
						}
					}
				}
				String kinases = "";
				if (peptide2kinase.containsKey(peptide)) {
					LinkedList list = (LinkedList)peptide2kinase.get(peptide);
					itr = list.iterator();
					while (itr.hasNext()) {
						String kinase = (String)itr.next();
						kinases += kinase + ",";
					}
				}
				if (kinases.equals("")) {
					kinases = "NA";
				}
				phosphoData.put(peptide, peptide + "\t" + protein_sites + "\t" + line + "\t" + kinases);
			}
			in.close();
			
			
			HashMap map = new HashMap();
			//String wholeFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\whl_proteome.gct";
			
			fstream = new FileInputStream(wholeFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String protein_name = split[1];
				if (protein2peptide.containsKey(protein_name)) {
					double new_avg = calcAvg(str);
					if (map.containsKey(split[0].toUpperCase())) {
						String orig_str = (String)map.get(split[0].toUpperCase());
						double orig_avg = calcAvg(orig_str);
						if (new_avg > orig_avg) {
							map.put(split[0].toUpperCase(), str);
						}
					} else {
						map.put(split[0].toUpperCase(), str);
					}
				} else {
					if (!map.containsKey(split[0])) {
						map.put(split[0].toUpperCase(), str);
					}
				}
			}
			in.close();
												
			/*if (map.containsKey("MTOR")) {
				System.out.println("Found mTOR");
			}*/
			//String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\KinaseInformation.txt";
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			kinase_geneName.keySet().iterator();
			Iterator itr = kinase_geneName.keySet().iterator();
			while (itr.hasNext()) {					
				String kinase = (String)itr.next();	
				String line = "";
				
				
				if (map.containsKey(kinase.toUpperCase())) {
					line = (String)map.get(kinase.toUpperCase());
					String[] split = line.split("\t");
					String protein_name = split[1];
					
					/*if (whl_class.containsKey(protein_name)) {
						
						line += "\tWPC" + whl_class.get(protein_name);
					} else {
						line += "\tNA";
					}*/
					line += "\tNA";
					
					if (protein2peptide.containsKey(protein_name)) {
						LinkedList peptides = (LinkedList)protein2peptide.get(protein_name);
						Iterator itr3 = peptides.iterator();						
						// each peptide
						while (itr3.hasNext()) {
							String peptide = (String)itr3.next();
						
							
							String phospho_line = (String)peptide2accession.get(peptide) + "\t" + (String)phosphoData.get(peptide);
							
							/*if (pho_class.containsKey(peptide)) {
								phospho_line += "\tPPC" + pho_class.get(peptide);
							} else {
								phospho_line += "\tNA";
							}*/
							phospho_line += "\tNA";
							String combine_line = line + "\t" + phospho_line;
							
							if (kinase_map.containsKey(kinase)) {
								String kinase_line = (String)kinase_map.get(kinase);
								combine_line += "\t" + kinase_line;
							} else {
								String kinase_line = "";
								for (int i = 0; i < 11; i++) {
									if (kinase_line.equals("")) {
										kinase_line += "NA";
									} else {
										kinase_line += "\tNA";
									}
								}
								combine_line += "\t" + kinase_line;
							}
							out.write(kinase + "\t" + combine_line + "\n");
						}
					} else {
						
						String phospho_line = "";
						for (int i = 0; i < 14; i++) {
							if (phospho_line.equals("")) {
								phospho_line = "NA";
							} else {
								phospho_line += "\t" + "NA";
							}
						}
						String combine_line = line + "\t" + phospho_line;
						
						if (kinase_map.containsKey(kinase)) {
							String kinase_line = (String)kinase_map.get(kinase);
							combine_line += "\t" + kinase_line;
						} else {
							String kinase_line = "";
							for (int i = 0; i < 11; i++) {
								if (kinase_line.equals("")) {
									kinase_line += "NA";
								} else {
									kinase_line += "\tNA";
								}
							}
							combine_line += "\t" + kinase_line;
						}
						out.write(kinase + "\t" + combine_line + "\n");
					}
					
					
				} else {
					for (int i = 0; i < 14 + 14; i++) {
						if (line.equals("")) {
							line += "NA";
						} else {
							line += "\tNA";
						}
					}
					if (kinase_map.containsKey(kinase)) {
						String kinase_line = (String)kinase_map.get(kinase);
						line += "\t" + kinase_line;
					} else {
						String kinase_line = "";
						for (int i = 0; i < 11; i++) {
							if (kinase_line.equals("")) {
								kinase_line += "NA";
							} else {
								kinase_line += "\tNA";
							}
						}
						line += "\t" + kinase_line;
					}
					
					out.write(kinase + "\t" + line + "\n");
					
				}
				
				/*if (kinase_map.containsKey(kinase)) {
					String kinase_line = (String)kinase_map.get(kinase);
					if (map.containsKey(kinase.toUpperCase())) {
						line = (String)map.get(kinase.toUpperCase());
						out.write(line + "\t" + kinase_line + "\n");					
					}				
				}*/
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static double calcAvg(String line) {
		String[] split = line.split("\t");
		double total = 0;
		for (int i = split.length - 10; i < split.length; i++) {
			total += new Double(split[i]);
		}
		return total / 10;
	}
}
