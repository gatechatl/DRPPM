package PhosphoTools.MotifTools;

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
 * Add the kinase information based on phosphopeptide
 * @author tshaw
 *
 */
public class AddKinaseBasedOnPhosphosite {

	public static void execute(String[] args) {
		
		try {
			String phosphositeFile = args[0];	
			String ascoreFile = args[1];
			String organism_input = args[2].toUpperCase();
			int buffer = new Integer(args[3]); 
			String outputFile = args[4];			
			String summaryFile = args[5];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter summary_fwriter = new FileWriter(summaryFile);
			BufferedWriter summary_out = new BufferedWriter(summary_fwriter);
			
			out.write("ProteinName\tPosition\tExtendedSeq\tOrigSeq\tPredictedMotif\tPredictedMotifName\tPredictedGeneName\tPredictedPSSMPvalue\tTopPSSMHitGene\tTopPSSMPValue\tPhosphosite_KINASE\tPhophosite_GeneName\tPhosphosite_Accession\n");
			HashMap phosphosite_map = loadPhosphosite(phosphositeFile, organism_input);
			
			// check what each of these hashmap means
			HashMap total_avail = new HashMap();
			HashMap total_found = new HashMap();
			HashMap count_substrate_found = new HashMap();
			HashMap count_substrate_avail = loadPhosphositeProteinAvail(phosphositeFile, organism_input);
			HashMap count_entries_mapped = new HashMap();
			
			HashMap mapk1 = new HashMap();
			HashMap akt1 = new HashMap();
			int count = 0;
			int lineNum = 0;
			int totalLine = 0;
			FileInputStream fstream = new FileInputStream(ascoreFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				totalLine++;
			}
			in.close();
			fstream = new FileInputStream(ascoreFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				
				lineNum++;
				if (lineNum % (totalLine / 20 + 1) == 0) {
					System.out.println(new Double(new Double(lineNum) / totalLine * 100).intValue() + "% Finished");
				}
				String[] split = str.split("\t");
				String[] split2 = split[0].split("\\|");
				String accid = split2[1];
				String[] locs = split[1].split(",");
				
				String hit_KINASE_NAME = "";
				String hit_KINASE_GENENAME = "";
				String hit_KINASE_ACC = "";
				//System.out.println(str);
				for (int i = 0; i < locs.length; i++) {
					String loc_str = locs[i].replaceAll("\\*", "");
					String residue = "";
					boolean bad = false;
					if (loc_str.contains("S")) {
						residue = "S";
					} else if (loc_str.contains("T")) {
						residue = "T";
					} else if (loc_str.contains("Y")) {
						residue = "Y";
					} else {
						bad = true;
					}
					
					if (!bad) {
						
						loc_str = loc_str.replaceAll("S", "").replaceAll("T", "").replaceAll("Y", "");
						try {
							int loc = new Integer(loc_str);
							total_avail.put(split[0] + "\t" + loc + residue, "");
							//Iterator itr = phosphosite_map.keySet().iterator();
							//while (itr.hasNext()) {
								
								//KinSubPSite site = (KinSubPSite)itr.next();;
								
								
								//if (site.SUBSTRATE_ACCID.equals(accid) && loc == site.LOC && residue.equals(site.RESIDUE)) {
								for (int j = -buffer; j <= buffer; j++) {
									if (phosphosite_map.containsKey(accid + "_" + (loc + j))) {
										LinkedList list = (LinkedList)phosphosite_map.get(accid + "_" + loc);
										Iterator itr3 = list.iterator();
										while (itr3.hasNext()) {
											KinSubPSite site = (KinSubPSite)itr3.next();
											hit_KINASE_NAME += site.KINASE_NAME + ",";
											hit_KINASE_GENENAME += site.KINASE_GENENAME + ",";
											hit_KINASE_ACC += site.KINASE_ACCID + ",";
											
											if (site.KINASE_GENENAME.equals("Mapk1")) {
												mapk1.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);
											}
											if (site.KINASE_GENENAME.equals("Akt1")) {
												akt1.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);
											}
											
											count_substrate_found.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);										
											count_entries_mapped.put(site, site);
											total_found.put(split[0] + "\t" + loc + residue, "");
										}
									}
									/*if (site.SUBSTRATE_ACCID.equals(accid) && loc == (site.LOC + j)) {
										hit_KINASE_NAME += site.KINASE_NAME + ",";
										hit_KINASE_GENENAME += site.KINASE_GENENAME + ",";
										hit_KINASE_ACC += site.KINASE_ACCID + ",";
										
										if (site.KINASE_GENENAME.equals("Mapk1")) {
											mapk1.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);
										}
										if (site.KINASE_GENENAME.equals("Akt1")) {
											akt1.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);
										}
										
										count_substrate_found.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);										
										count_entries_mapped.put(site, site);
										total_found.put(split[0] + "\t" + loc + residue, "");
									}*/
								}
							//}
						} catch (Exception ex) {
							
						}
					}
				}
				count++;
				if (hit_KINASE_GENENAME.equals("")) {
					hit_KINASE_GENENAME = "NA";
				}
				if (hit_KINASE_ACC.equals("")) {
					hit_KINASE_ACC = "NA";
				}
				if (hit_KINASE_NAME.equals("")) {
					hit_KINASE_NAME = "NA";
				}
				//System.out.println(count + "\t" + str + "\t" + hit_KINASE_GENENAME + "\t" + hit_KINASE_NAME + "\t" + hit_KINASE_ACC + "\n");
				
				
				out.write(str + "\t" + hit_KINASE_GENENAME + "\t" + hit_KINASE_NAME + "\t" + hit_KINASE_ACC + "\n");
				
			}
			in.close();
			out.close();
			
			int count_phosphosite_map = 0;
			Iterator itr = phosphosite_map.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				count_phosphosite_map = count_phosphosite_map + ((LinkedList)phosphosite_map.get(key)).size();
			}
			
			double proportion_in_KSData = new Double(new Double(new Double(total_found.size()) / total_avail.size() * 10000).intValue()) / 100 ;
			//double proportion_in_phosphosite = new Double(new Double(new Double(count_entries_mapped.size()) / phosphosite_map.size() * 10000).intValue()) / 100;
			double proportion_in_phosphosite = new Double(new Double(new Double(count_entries_mapped.size()) / count_phosphosite_map * 10000).intValue()) / 100;
			
			/**
			 * Should write the following into a summary table
			 */
			System.out.println("#### Phosphosite summary table ####\n");
			summary_out.write("#### Phosphosite summary table ####\n\n");
			System.out.println("Number of sites assigned using Phosphosite");
			summary_out.write("Number of sites assigned using Phosphosite\n");
			System.out.println("KinaseSubstrate Total Available: " + total_avail.size());
			summary_out.write("KinaseSubstrate Total Available: " + total_avail.size() + "\n");
			System.out.println("KinaseSubstrate Total Found: " + total_found.size());
			summary_out.write("KinaseSubstrate Total Found: " + total_found.size() + "\n");
			System.out.println("Proportion of KinaseSubstrate Matched with Phosphosite: " + proportion_in_KSData + "%");
			summary_out.write("Proportion of KinaseSubstrate Matched with Phosphosite: " + proportion_in_KSData + "%\n");
			System.out.println();
			summary_out.write("\n");
			
			//System.out.println("Phosphosite Total Entries: " + phosphosite_map.size());
			//summary_out.write("Phosphosite Total Entries: " + phosphosite_map.size() + "\n");
			System.out.println("Phosphosite Total Entries: " + count_phosphosite_map);
			summary_out.write("Phosphosite Total Entries: " + count_phosphosite_map + "\n");
			System.out.println("Unique Substrates in Phosphosite : " + count_substrate_avail.size());
			summary_out.write("Unique Substrates in Phosphosite : " + count_substrate_avail.size() + "\n");
			System.out.println("Phosphosite Unique Substrates Found in Dataset: " + count_substrate_found.size());
			summary_out.write("Phosphosite Unique Substrates Found in Dataset: " + count_substrate_found.size() + "\n");
			System.out.println("Phosphosite Mapped Entries: " + count_entries_mapped.size());
			summary_out.write("Phosphosite Mapped Entries: " + count_entries_mapped.size() + "\n");					
			System.out.println("Proportion of Phosphosite that found a match: " + proportion_in_phosphosite + "%");
			summary_out.write("Proportion of Phosphosite that found a match: " + proportion_in_phosphosite + "%\n");
			summary_out.close();
			//System.out.println("Mapk1 mapped:" + mapk1.size());
			//System.out.println("Akt1 mapped:" + akt1.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updated to new phosphosite 
	 * @param phosphositeFile
	 * @param organism_input
	 * @return
	 */
	/*public static HashMap loadPhosphositeProteinAvail(String phosphositeFile, String organism_input) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(phosphositeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 10 && !split[0].equals("KinaseGeneName")) {
					KinSubPSite site = new KinSubPSite();
					String kinase_name = split[0];
					String kinase_genename = split[0];
					String kinase_accid = split[1];
					//String organism = split[10].toUpperCase();
					//String organism = split[10].toUpperCase();
					String substrate_genename = split[6].toUpperCase();
					String substrate_accid = split[3];
					String residue = split[4];
					String loc_str = split[4].replaceAll("S", "").replaceAll("Y", "").replaceAll("T", "");
					boolean bad = false;
					if (split[4].contains("S")) {
						residue = "S";
					} else if (split[4].contains("Y")) {
						residue = "Y";
					} else if (split[4].contains("T")) {
						residue = "T";
					} else {
						bad = true;
					}
					if (!bad) {
						int loc = new Integer(loc_str);
						//site.KINASE_NAME = kinase_name;
						//site.KINASE_GENENAME = kinase_genename;
						//site.KINASE_ACCID = kinase_accid;
						//site.SUBSTRATE_GENENAME = substrate_genename;
						site.SUBSTRATE_ACCID = substrate_accid;
						//site.ORGANISM = organism;
						//site.RESIDUE = residue;
						//site.LOC = loc;
						 
						
						//if (organism_input.toUpperCase().equals(organism)) {
							//map.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);
							//phosphosite_map.put(site, site);
						//}
						map.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);
					}
				}				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}*/
	public static HashMap loadPhosphositeProteinAvail(String phosphositeFile, String organism_input) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(phosphositeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 10 && !split[0].equals("KINASE")) {
					KinSubPSite site = new KinSubPSite();
					String kinase_name = split[0];
					String kinase_genename = split[2];
					String kinase_accid = split[1];
					String organism = split[10].toUpperCase();
					String substrate_genename = split[8];
					String substrate_accid = split[7];
					String residue = split[11];
					String loc_str = split[11].replaceAll("S", "").replaceAll("Y", "").replaceAll("T", "");
					boolean bad = false;
					if (split[11].contains("S")) {
						residue = "S";
					} else if (split[11].contains("Y")) {
						residue = "Y";
					} else if (split[11].contains("T")) {
						residue = "T";
					} else {
						bad = true;
					}
					if (!bad) {
						int loc = new Integer(loc_str);
						//site.KINASE_NAME = kinase_name;
						//site.KINASE_GENENAME = kinase_genename;
						//site.KINASE_ACCID = kinase_accid;
						//site.SUBSTRATE_GENENAME = substrate_genename;
						site.SUBSTRATE_ACCID = substrate_accid;
						//site.ORGANISM = organism;
						//site.RESIDUE = residue;
						//site.LOC = loc;
						if (organism_input.toUpperCase().equals(organism)) {
							map.put(site.SUBSTRATE_ACCID, site.SUBSTRATE_ACCID);
							//phosphosite_map.put(site, site);
						}
						
					}
				}				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap loadPhosphosite(String phosphositeFile, String organism_input) {
		HashMap phosphosite_map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(phosphositeFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 10 && !split[0].equals("KINASE")) {
					KinSubPSite site = new KinSubPSite();
					String kinase_name = split[0];
					String kinase_genename = split[2];
					String kinase_accid = split[1];
					String organism = split[10].toUpperCase();
					String substrate_genename = split[8];
					String substrate_accid = split[7];
					String residue = split[11];
					String loc_str = split[11].replaceAll("S", "").replaceAll("Y", "").replaceAll("T", "");
					boolean bad = false;
					if (split[11].contains("S")) {
						residue = "S";
					} else if (split[11].contains("Y")) {
						residue = "Y";
					} else if (split[11].contains("T")) {
						residue = "T";
					} else {
						bad = true;
					}
					if (!bad) {
						int loc = new Integer(loc_str);
						site.KINASE_NAME = kinase_name;
						site.KINASE_GENENAME = kinase_genename;
						site.KINASE_ACCID = kinase_accid;
						site.SUBSTRATE_GENENAME = substrate_genename;
						site.SUBSTRATE_ACCID = substrate_accid;
						site.ORGANISM = organism;
						site.RESIDUE = residue;
						site.LOC = loc;
						if (organism_input.equals(organism)) {							
							//phosphosite_map.put(site, site);
							if (phosphosite_map.containsKey(substrate_accid + "_" + loc)) {
								LinkedList item = (LinkedList)phosphosite_map.get(substrate_accid + "_" + loc);
								item.add(site);
								phosphosite_map.put(substrate_accid + "_" + loc, item);
							} else {
								LinkedList item = new LinkedList();
								item.add(site);
								phosphosite_map.put(substrate_accid + "_" + loc, item);
							}
						}
						
					}
				}				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phosphosite_map;
	}
	public static class KinSubPSite {
		public String KINASE_NAME = "";
		public String KINASE_GENENAME = "";
		public String KINASE_ACCID = "";
		public String ORGANISM = "";
		public String SUBSTRATE_GENENAME = "";
		public String SUBSTRATE_ACCID = "";
		public int LOC = -1;
		String RESIDUE = "";
	}
}
