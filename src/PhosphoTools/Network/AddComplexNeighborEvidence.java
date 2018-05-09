package PhosphoTools.Network;

import idconversion.tools.Uniprot2GeneID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import network.NetworkTools;

/**
 * For each assigned kinase motif with a network score
 * 1.  For each known kinase motif if a node is directly connected to a predicted provide
 * 2.  For each predicted kinase motif, see if a substrate is connected to a nearby substrate.
 * 
 * For each known kinase substrate info examine how many contain the potein protein interaction
 * @author tshaw
 *
 */
public class AddComplexNeighborEvidence {

	public static void execute(String[] args) {
		
		try {
			
			Random rand = new Random();
			rand.setSeed(999);
			
			String Phosphosite_hrpd_motif_output_all_file = args[0];	
			String humanInteractiveDBFile = args[1];			
			String Phosphosite_kinase_substrate = args[2];
			String uniprot2geneIDFile = args[3];
			String motif_all_annotation_file = args[4];
			String outputFile = args[5];
			// need to convert these gene to a mouse version of the DB
			HashMap interactiveDB = NetworkTools.readHumInterDB(humanInteractiveDBFile);
			HashMap uniprot2geneID = Uniprot2GeneID.uniprot2geneID(uniprot2geneIDFile);
			
			//HashMap predicted_kinase2substrate = getKnownKinaseSubstrateList(Phosphosite_hrpd_motif_output_all_file, uniprot2geneID);
			
			HashMap known_kinase2substrate = getKnownKinaseSubstrateList(Phosphosite_kinase_substrate);			
			HashMap kinase2motifName = getKinase2MotifName(motif_all_annotation_file);
			HashMap motifName2kinase = getMotifName2Kinase(motif_all_annotation_file);
			
			HashMap motif_statistics = new HashMap();
			HashMap negative_dataset = new HashMap();
			try {
				
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				
				FileInputStream fstream = new FileInputStream(Phosphosite_hrpd_motif_output_all_file);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();				
					String[] split = str.split("\t");
					String uniprot = split[0].split("\\|")[1];
					String key = uniprot + "\t" + split[1];
					if (uniprot2geneID.containsKey(uniprot)) {
						String geneID = ((String)uniprot2geneID.get(uniprot)).toUpperCase();
						String motif_name = split[5];
						if (motifName2kinase.containsKey(motif_name)) {
							String kinases = (String)motifName2kinase.get(motif_name);
							for (String kinase: kinases.split(",")) {
								kinase = kinase.toUpperCase();								
								// check if present in annotated database already
								boolean checkIfPresentAlready = false;
								for (String known_kinase: split[6].split(",")) {
									if (known_kinase.toUpperCase().equals(kinase)) {
										checkIfPresentAlready = true;
									}
								}
								if (checkIfPresentAlready) {
									//out.write(str + "\t" + kinase + ":present\n");
								} //else {
								boolean found_local_substrate = false;
								if (interactiveDB.containsKey(geneID)) {
									LinkedList local_substrate_genes = (LinkedList)interactiveDB.get(geneID);
									
									LinkedList found_local_substrates = new LinkedList();
									Iterator itr = local_substrate_genes.iterator();
									while (itr.hasNext()) {
										String local_sub_gene = (String)itr.next();
										//System.out.println("neighbors: " + geneID + "\t" + local_sub_gene + "\t" + kinase);
										if (known_kinase2substrate.containsKey(kinase)) {
											LinkedList known_kinase_substrates = (LinkedList)known_kinase2substrate.get(kinase);
											/*Iterator itr2 = known_kinase_substrates.iterator();
											while (itr2.hasNext()) {
												String substrate = (String)itr2.next();
												//System.out.println(kinase + "\t" + substrate);
											}*/
											if (known_kinase_substrates.contains(local_sub_gene)) {
												found_local_substrate = true;
												found_local_substrates.add(local_sub_gene);
											}
										}
									}
									if (found_local_substrate) {
										String totalList = "";
										Iterator itr3 = found_local_substrates.iterator();
										while (itr3.hasNext()) {
											totalList += (String)itr3.next() + ",";
										}
										if (checkIfPresentAlready) {
											if (motif_statistics.containsKey(key)) {
												LinkedList list = (LinkedList)motif_statistics.get(key);
												list.add("present_found_neighbor");
												motif_statistics.put(key, list);
												
											} else {
												LinkedList list = new LinkedList();
												list.add("present_found_neighbor");
												motif_statistics.put(key, list);
											}
											//for (int i = 0; i < local_substrate_genes.size(); i++) {
												String rand_gene = getRandomGene(rand, known_kinase2substrate);											
												if (local_substrate_genes.contains(rand_gene)) {
													negative_dataset.put(key, "FOUND");												
												} else {
													if (!negative_dataset.containsKey(key)) {
														negative_dataset.put(key, "NOTFOUND");
													}
												}
											//}
											out.write(str + "\t" + kinase + ":present_found_neighbor-" + totalList + "\n");
										} else {
											out.write(str + "\t" + kinase + ":found_neighbor-" + totalList + "\n");
											if (motif_statistics.containsKey(key)) {
												LinkedList list = (LinkedList)motif_statistics.get(key);
												list.add("found_neighbor");
												motif_statistics.put(key, list);
											} else {
												LinkedList list = new LinkedList();
												list.add("found_neighbor");
												motif_statistics.put(key, list);
											}
										}
									} else {
										if (checkIfPresentAlready) {
											
											if (motif_statistics.containsKey(key)) {
												LinkedList list = (LinkedList)motif_statistics.get(key);
												list.add("present_but_no_neighbor");
												motif_statistics.put(key, list);
											} else {
												LinkedList list = new LinkedList();
												list.add("present_but_no_neighbor");
												motif_statistics.put(key, list);
											}
											//for (int i = 0; i < local_substrate_genes.size(); i++) {
												String rand_gene = getRandomGene(rand, known_kinase2substrate);											
												if (local_substrate_genes.contains(rand_gene)) {
													negative_dataset.put(key, "FOUND");												
												} else {
													if (!negative_dataset.containsKey(key)) {
														negative_dataset.put(key, "NOTFOUND");
													}
												}
											//}
											out.write(str + "\t" + kinase + ":present_but_no_neighbor\n");
											
										} else {
											out.write(str + "\t" + kinase + ":no_neighbor\n");
											if (motif_statistics.containsKey(key)) {
												LinkedList list = (LinkedList)motif_statistics.get(key);
												list.add("no_neighbor");
												motif_statistics.put(key, list);
											} else {
												LinkedList list = new LinkedList();
												list.add("no_neighbor");
												motif_statistics.put(key, list);
											}
										}
									}
								} else { // else not in interactiveDB
									if (checkIfPresentAlready) {
										out.write(str + "\t" + kinase + ":present_but_missing_in_interactiveDB\n");
										if (motif_statistics.containsKey(key)) {
											LinkedList list = (LinkedList)motif_statistics.get(key);
											list.add("present_but_missing_in_interactiveDB");
											motif_statistics.put(key, list);
										} else {
											LinkedList list = new LinkedList();
											list.add("present_but_missing_in_interactiveDB");
											motif_statistics.put(key, list);
										}
									} else {									
										out.write(str + "\t" + kinase + ":missing_in_interactiveDB\n");
										if (motif_statistics.containsKey(key)) {
											LinkedList list = (LinkedList)motif_statistics.get(key);
											list.add("missing_in_interactiveDB");
											motif_statistics.put(key, list);
										} else {
											LinkedList list = new LinkedList();
											list.add("missing_in_interactiveDB");
											motif_statistics.put(key, list);
										}
									}
								}
								//}							
							}					
						}
					} else { // check if contains uniprot key
						
						out.write(str + "\tMissingUniprot2GeneID\n");
					}
				}
				in.close();
				out.close();
				int present_found_neighbor = 0;
				int present_but_no_neighbor = 0;
				int found_neighbor = 0;
				int no_neighbor = 0;
				int present_but_missing_in_interactiveDB = 0;
				int missing_in_interactiveDB = 0;
				
				Iterator itr4 = motif_statistics.keySet().iterator();
				while (itr4.hasNext()) {
					String key = (String)itr4.next();
					LinkedList list = (LinkedList)motif_statistics.get(key);
					if (list.contains("present_found_neighbor")) {
						present_found_neighbor++;
					} else if (list.contains("present_but_no_neighbor")) {
						present_but_no_neighbor++;
					} else if (list.contains("found_neighbor")) {
						found_neighbor++;
					} else if (list.contains("no_neighbor")) {
						no_neighbor++;
					} else if (list.contains("present_but_missing_in_interactiveDB")) {
						present_but_missing_in_interactiveDB++;
					} else if (list.contains("missing_in_interactiveDB")) {
						missing_in_interactiveDB++;
					}
				}
				
				int count_neg_found = 0;
				int count_neg_not_found = 0;
				
				itr4 = negative_dataset.keySet().iterator();
				while (itr4.hasNext()) {
					String key = (String)itr4.next();
					String found_tag = (String)negative_dataset.get(key);
					if (found_tag.equals("FOUND")) {
						count_neg_found++;
					} else {
						count_neg_not_found++;
					}
				}
				System.out.println("### Network Neighbor Statistics ###");
				System.out.println("present_found_neighbor: " + present_found_neighbor);
				System.out.println("present_but_no_neighbor: " + present_but_no_neighbor);
				System.out.println("found_neighbor: " + found_neighbor);
				System.out.println("no_neighbor: " + no_neighbor);
				System.out.println("present_but_missing_in_interactiveDB: " + present_but_missing_in_interactiveDB);
				System.out.println("missing_in_interactiveDB: " + missing_in_interactiveDB);
				System.out.println("Sensitivity: " + new Double(present_found_neighbor) / (present_found_neighbor + present_but_no_neighbor));
				System.out.println("Specificity: " + new Double(count_neg_not_found) / (count_neg_found + count_neg_not_found));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getRandomGene(Random rand, HashMap map) {
		
		int n = rand.nextInt(map.size());
		int index = 0;
		String lastGene = "";
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String gene = (String)itr.next();
			if (n == index) {
				return gene;
			}
			lastGene = gene;
			index++;
		}
		return lastGene;
	}
	
	public static HashMap getMotifName2Kinase(String fileName) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String isKinaseMotif = split[5];
				if (isKinaseMotif.equals("yes")) {
					String motifName = split[1];
					map.put(motifName, split[2]);
					
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap getKinase2MotifName(String fileName) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String isKinaseMotif = split[5];
				if (isKinaseMotif.equals("yes")) {
					String motifName = split[1];
					for (String kinase: split[2].split(",")) {
						if (map.containsKey(kinase.toUpperCase())) {
							LinkedList list = (LinkedList)map.get(kinase.toUpperCase());
							if (!list.contains(motifName)) {
								list.add(motifName);
								map.put(kinase.toUpperCase(), list);
							}
							
						} else {
							LinkedList list = new LinkedList();
							list.add(motifName);
							map.put(kinase.toUpperCase(), list);
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
	
	/**
	 * Get all the substrate that is able to modify for a particular kinase
	 * @param fileName
	 * @return
	 */
	public static HashMap getKnownKinaseSubstrateList(String fileName) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String kinase = split[2].toUpperCase();
				String sub_geneName = split[8].toUpperCase();
				if (map.containsKey(kinase)) {
					LinkedList list = (LinkedList)map.get(kinase);
					list.add(sub_geneName);
					map.put(kinase, list);
				} else {
					LinkedList list = new LinkedList();
					list.add(sub_geneName);
					map.put(kinase, list);
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * Get all the substrate that is able to modify for a particular kinase
	 * @param fileName
	 * @return
	 */
	/*public static HashMap getKnownKinaseSubstrateList(String fileName, HashMap uniprot2geneID) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();				
				String[] split = str.split("\t");
				String uniprot = split[0].split("\\|")[1];
				String geneID = (String)uniprot2geneID.get(uniprot);
				String loc = split[1];
				for (String kinase: split[6].split(",")) {
					SubstrateInfo info = new SubstrateInfo();
					info.UniprotAcc = uniprot;
					info.GeneName = geneID;
					info.ModSite = loc;
					if (map.containsKey(kinase)) {
						LinkedList list = (LinkedList)map.get(kinase);
						list.add(info);
						map.put(kinase, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(info);
						map.put(kinase, list);
					}
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}*/
	
	public static class SubstrateInfo {
		
		public static String GeneName = "";
		public static String UniprotAcc = "";
		public static String ModSite = "";
	}
}
