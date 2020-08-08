package integrate.CombineDataSet;

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
 * 
 * @author tshaw
 *
 */
public class IntegrateMatrixSubstrateCentric {

	public static String parameter_info() {
		return "";
	}
	public static void main(String[] args) {
		
		try {
			
			//String wholeFile = args[0];
			HashMap map = new HashMap();
			String wholeFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\whl_proteome.gct";
			FileInputStream fstream = new FileInputStream(wholeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0] + "\t" + split[1], str);
			}
			in.close();
			
			String phosphosite_kinsub_file = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\Mouse_Kinase_Substrate.txt";
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
			
			
			HashMap peptide2protein = new HashMap();
			HashMap peptide2kinase = new HashMap();
			HashMap protein2peptide = new HashMap();
			HashMap phosphoData = new HashMap();
			String phosphoIDFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\id_all_pep_quan.txt";
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
						protein_sites += protein_site + ",";
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
				phosphoData.put(peptide, peptide + "\t" + protein_sites + "\t" + line + "\t" + kinases);
			}
			in.close();
			
			
			/*
			HashMap phosphoData = new HashMap();
			//String phosphoFile = args[1];
			String phosphoFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\phosphopep_quan.gct";
			fstream = new FileInputStream(phosphoFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				//String peptide = split[1];
				String peptide = split[1].replaceAll("\\@", "#");
				peptide = peptide.replaceAll("\\%", "#");
				peptide = peptide.replaceAll("\\*", "#");
				
				peptide = peptide.split("\\.")[1];
				//System.out.println(peptide);
				String protein_sites = "";
				String kinases = "";
				if (peptide2protein.containsKey(peptide)) {
					LinkedList list = (LinkedList)peptide2protein.get(peptide);
					Iterator itr = list.iterator();
					while (itr.hasNext()) {
						String protein_site = (String)itr.next();
						protein_sites += protein_site + ",";
					}
					
					if (peptide2kinase.containsKey(peptide)) {
						list = (LinkedList)peptide2kinase.get(peptide);
						itr = list.iterator();
						while (itr.hasNext()) {
							String kinase = (String)itr.next();
							kinases += kinase + ",";
						}
					}
				}
				if (protein_sites.equals("")) {
					protein_sites = "NA";
				}
				if (kinases.equals("")) {
					kinases = "NA";
				}
				String line = str + "\t" + protein_sites + "\t" + kinases;				
				phosphoData.put(peptide, line);
			}
			in.close();
			*/
			String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\HonboChi\\Paper's Figures\\Figure4_20160317\\SummaryMatrix.txt";
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String gene_protein = (String)itr.next();
				String geneName = gene_protein.split("\t")[0];
				String proteinName = gene_protein.split("\t")[1];
				String line = (String)map.get(gene_protein);
				if (protein2peptide.containsKey(proteinName)) {
					LinkedList peptides = (LinkedList)protein2peptide.get(proteinName);
					Iterator itr2 = peptides.iterator();
					while (itr2.hasNext()) {
						String peptide = (String)itr2.next();
						String peptide_line = (String)phosphoData.get(peptide);
						if (!phosphoData.containsKey(peptide)) {
							System.out.println(peptide);
						}
						out.write(line + "\t" + peptide_line + "\n");
					}				
				}
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
