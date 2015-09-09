package PhosphoTools.Network;

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

import IDConversion.Uniprot2GeneID;
import NetworkTools.NetworkTools;

/**
 * For each assigned kinase motif with a network score 1. For each known kinase
 * motif if a node is directly connected to a predicted provide 2. For each
 * predicted kinase motif, see if a substrate is connected to a nearby
 * substrate.
 * 
 * For each known kinase substrate info examine how many contain the potein
 * protein interaction
 * 
 * @author tshaw
 * 
 */
public class AddNetworkNeighborEvidence {

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
			String summaryFile = args[6];

			// need to convert these gene to a mouse version of the DB
			HashMap interactiveDB = NetworkTools
					.readHumInterDB(humanInteractiveDBFile);
			HashMap uniprot2geneID = Uniprot2GeneID
					.uniprot2geneID(uniprot2geneIDFile);

			// HashMap predicted_kinase2substrate =
			// getKnownKinaseSubstrateList(Phosphosite_hrpd_motif_output_all_file,
			// uniprot2geneID);

			System.out.println("Load Phosphosite");
			HashMap known_kinase2substrate = getKnownKinaseSubstrateList(Phosphosite_kinase_substrate);
			
			System.out.println("Load Known Kinase Motif");
			HashMap kinase2motifName = getKinase2MotifName(motif_all_annotation_file);						
			HashMap motifName2kinase = getMotifName2Kinase(motif_all_annotation_file);
			HashMap motif_statistics = new HashMap();
			HashMap negative_dataset = new HashMap();
			try {
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				out.write("ProteinName\tPosition\tExtendedSeq\tOrigSeq\tPredictedMotif\tPredictedMotifName\tPredictedGeneName\tPredictedPSSMPvalue\tTopPSSMHitGene\tTopPSSMPValue\tPhosphosite_KINASE\tPhophosite_GeneName\tPhosphosite_Accession\tPearsonCorrel\tPearsonPvalue\tSpearman\tKinaseCorrelName\tEvaluatedKinase\tNetworkAnnotation\tNeighboringGenes\n\n");
				// out.write("ProteinName\tPosition\tExtendedSeq\tOrigSeq\tPredictedMotif\tPredictedMotifName\tPhosphosite_KINASE\tPhophosite_GeneName\tPhosphosite_Accession\tEvaluatedKinase\tNetworkAnnotation\n");

				FileWriter summary_fwriter = new FileWriter(summaryFile);
				BufferedWriter summary_out = new BufferedWriter(summary_fwriter);

				int count = 0;
				HashMap uniq_protein_phosite = new HashMap();

				HashMap predicted_list = new HashMap(); // generate a predicted				
														// list
				FileInputStream fstream = new FileInputStream(
						Phosphosite_hrpd_motif_output_all_file);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				in.readLine(); // skip header
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String uniprot = split[0].split("\\|")[1];
					String key = uniprot + "\t" + split[1];
					String tag = split[5];
					uniq_protein_phosite.put(key, tag);
					if (uniprot2geneID.containsKey(uniprot)) {
						String geneID = ((String) uniprot2geneID.get(uniprot)).toUpperCase();
						String motif_name = split[5];
						//if (motifName2kinase.containsKey(motif_name)) {
						//	String kinases = (String) motifName2kinase.get(motif_name);
							//for (String kinase : kinases.split(",")) {
							for (String kinase : split[16].split(",")) {
								kinase = kinase.toUpperCase();
								if (!kinase.equals("NA")) {
									//String kinase = split[16].split(",")[0].toUpperCase();
									if (predicted_list.containsKey(kinase)) {
										LinkedList stuff = (LinkedList) predicted_list
												.get(kinase);
										if (!stuff.contains(geneID.toUpperCase())) {
											stuff.add(geneID.toUpperCase());
											predicted_list.put(kinase, stuff);
										}
									} else {
										LinkedList stuff = new LinkedList();
										stuff.add(geneID.toUpperCase());
										predicted_list.put(kinase, stuff);
									}
								}
							}
						//}
					}
					count++;
				}
				in.close();
				double total_count = count;
				count = 0;
				
				System.out.println("Preload some parts of the data");
				HashMap predicted_list_str = new HashMap();
				Iterator itr4 = predicted_list.keySet().iterator();
				while (itr4.hasNext()) {
					String kinase = (String) itr4.next();
					LinkedList stuff = (LinkedList) predicted_list.get(kinase);
					String total = "";
					Iterator itr5 = stuff.iterator();
					while (itr5.hasNext()) {
						String geneID = (String) itr5.next();
						if (total.equals("")) {
							total = geneID;
						} else {
							total += "," + geneID;
						}
					}
					predicted_list_str.put(kinase, total);
				}
				
				
				// System.out.println("Finished Loading Predicted_List: " +
				// count);
				
				
				System.out.println("Begin Searching");
				fstream = new FileInputStream(Phosphosite_hrpd_motif_output_all_file);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				in.readLine(); // skip header
				while (in.ready()) {
					String str = in.readLine();

					/*if (count % 10000 == 0) {
						System.out.println((new Double(new Double(count)
								/ total_count * 100)).intValue()
								+ "% Finished");
					}*/
					if (count % (total_count / 10 + 1) == 0) {
						System.out.println((new Double(new Double(count)
								/ total_count * 100)).intValue()
								+ "% Finished");
					}
					count++;
					String[] split = str.split("\t");
					String uniprot = split[0].split("\\|")[1];
					String key = uniprot + "\t" + split[1];
					
					if (uniprot2geneID.containsKey(uniprot)) {
						String geneID = ((String) uniprot2geneID.get(uniprot))
								.toUpperCase();
						String motif_name = split[5];
						
						//if (motifName2kinase.containsKey(motif_name)) {
							// String kinases =
							// (String)motifName2kinase.get(motif_name);
							// for (String kinase: kinases.split(",")) {
							for (String kinase: split[16].split(",")) {
								
								if (!kinase.equals("NA")) {	
									kinase = kinase.toUpperCase();
									
									//String kinase = split[16].split(",")[0];
									
									LinkedList predicted_gene_list = (LinkedList) predicted_list
											.get(kinase);
									
									
									
									// check if present in annotated motif database
									// already
									boolean checkIfPresentAlready = false;
									for (String known_kinase : split[6].split(",")) {
		
										if (known_kinase.toUpperCase().equals(kinase)) {
											checkIfPresentAlready = true;
										}
									}
									if (checkIfPresentAlready) {
										// out.write(str + "\t" + kinase +
										// ":present\n");
									} // else {
									boolean found_local_substrate = false;
									boolean found_local_predicted_substrate = false;
									if (interactiveDB.containsKey(geneID)) {
										LinkedList local_substrate_genes = (LinkedList) interactiveDB
												.get(geneID);
		
										LinkedList found_local_substrates = new LinkedList();
										LinkedList found_local_predicted_substrates = new LinkedList();
		
										Iterator itr = local_substrate_genes.iterator();
										while (itr.hasNext()) {
											String local_sub_gene = (String) itr.next();
											// System.out.println("neighbors: " + geneID
											// + "\t" + local_sub_gene + "\t" + kinase);
											
											if (known_kinase2substrate.containsKey(kinase)) {
												LinkedList known_kinase_substrates = (LinkedList) known_kinase2substrate.get(kinase);
												/*
												 * Iterator itr2 =
												 * known_kinase_substrates.iterator();
												 * while (itr2.hasNext()) { String
												 * substrate = (String)itr2.next();
												 * //System.out.println(kinase + "\t" +
												 * substrate); }
												 */
												if (known_kinase_substrates
														.contains(local_sub_gene)) {
													found_local_substrate = true;
													found_local_substrates
															.add(local_sub_gene);
												}
		
											}
											// predicted_gene_list shouldn't have NA
											// local_sub_gene shouldn't have NA
											if (predicted_gene_list
													.contains(local_sub_gene
															.toUpperCase())) {
												found_local_predicted_substrate = true;
												found_local_predicted_substrates
														.add(local_sub_gene
																.toUpperCase());
											}
										} // while loop
		
										if (found_local_substrate) {
											String totalList = "";
											Iterator itr3 = found_local_substrates.iterator();
											while (itr3.hasNext()) {
												totalList += (String) "[" + itr3.next() + "],";
											}
											itr3 = found_local_predicted_substrates.iterator();
											while (itr3.hasNext()) {
												totalList += (String) "(" + itr3.next() + "),";
											}
											
											if (checkIfPresentAlready) {
												if (motif_statistics.containsKey(key)) {
													LinkedList list = (LinkedList) motif_statistics
															.get(key);
													list.add("present_found_neighbor");
													motif_statistics.put(key, list);
		
												} else {
													LinkedList list = new LinkedList();
													list.add("present_found_neighbor");
													motif_statistics.put(key, list);
												}
												// for (int i = 0; i <
												// local_substrate_genes.size(); i++) {
												String rand_gene = getRandomGene(rand,
														known_kinase2substrate);
												if (local_substrate_genes
														.contains(rand_gene)) {
													negative_dataset.put(key, "FOUND");
												} else {
													if (!negative_dataset
															.containsKey(key)) {
														negative_dataset.put(key,
																"NOTFOUND");
													}
												}
												// }
												out.write(str + "\t" + kinase + "\t"
														//+ kinase
														//+ ":present_found_neighbor-"
														//+ ":found_neighbor-"
														+ "found_neighbor\t"
														+ totalList + "\n");
											} else {
												out.write(str + "\t" + kinase + "\t"
														//+ kinase + ":found_neighbor-"
														+ "found_neighbor\t"
														+ totalList + "\n");
												if (motif_statistics.containsKey(key)) {
													LinkedList list = (LinkedList) motif_statistics
															.get(key);
													list.add("found_neighbor");
													motif_statistics.put(key, list);
												} else {
													LinkedList list = new LinkedList();
													list.add("found_neighbor");
													motif_statistics.put(key, list);
												}
											}
										} else if (found_local_predicted_substrate) {
											
											String totalList = "";
											Iterator itr3 = found_local_substrates.iterator();
											while (itr3.hasNext()) {
												totalList += (String) "[" + itr3.next() + "],";
											}
											itr3 = found_local_predicted_substrates.iterator();
											while (itr3.hasNext()) {
												totalList += (String) "(" + itr3.next() + "),";
											}
											if (checkIfPresentAlready) {
												if (motif_statistics.containsKey(key)) {
													LinkedList list = (LinkedList) motif_statistics
															.get(key);
													list.add("present_found_predicted_neighbor");
													motif_statistics.put(key, list);
		
												} else {
													LinkedList list = new LinkedList();
													list.add("present_found_predicted_neighbor");
													motif_statistics.put(key, list);
												}
												if (kinase.equals("NA")) {
													System.out.println("Kinase is NA, this isn't right");
												}
												out.write(str + "\t" + kinase + "\t" + "found_predicted_neighbor\t"
														+ totalList + "\n");
											} else {
												if (motif_statistics.containsKey(key)) {
													LinkedList list = (LinkedList) motif_statistics
															.get(key);
													list.add("found_predicted_neighbor");
													motif_statistics.put(key, list);
		
												} else {
													LinkedList list = new LinkedList();
													list.add("found_predicted_neighbor");
													motif_statistics.put(key, list);
												}
												
												out.write(str + "\t" + kinase + "\t"
														//+ kinase
														//+ ":found_predicted_neighbor\n");
														+ "found_predicted_neighbor\t"
														+ totalList + "\n");
											}
										} else {
											if (checkIfPresentAlready) {
		
												if (motif_statistics.containsKey(key)) {
													LinkedList list = (LinkedList) motif_statistics
															.get(key);
													list.add("present_but_no_neighbor");
													motif_statistics.put(key, list);
												} else {
													LinkedList list = new LinkedList();
													list.add("present_but_no_neighbor");
													motif_statistics.put(key, list);
												}
												// for (int i = 0; i <
												// local_substrate_genes.size(); i++) {
		
												// }
												out.write(str + "\t" + kinase + "\t"
														//+ kinase
														//+ ":present_but_no_neighbor\n");
														+ "no_neighbor\tNA\n");
		
												String rand_gene = getRandomGene(rand,
														known_kinase2substrate);
												if (local_substrate_genes
														.contains(rand_gene)) {
													negative_dataset.put(key, "FOUND");
												} else {
													if (!negative_dataset
															.containsKey(key)) {
														negative_dataset.put(key,
																"NOTFOUND");
													}
												}
											} else {
												out.write(str + "\t" + kinase + "\t"
														//+ kinase + ":no_neighbor\n");
														+ "no_neighbor\tNA\n");
												if (motif_statistics.containsKey(key)) {
													LinkedList list = (LinkedList) motif_statistics
															.get(key);
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
											out.write(str
													+ "\t"
													+ kinase
													+ "\t"
													//+ kinase
													//+ ":present_but_missing_in_interactiveDB\n");
													+ "missing_in_interactiveDB\tNA\n");
											if (motif_statistics.containsKey(key)) {
												LinkedList list = (LinkedList) motif_statistics
														.get(key);
												list.add("present_but_missing_in_interactiveDB");
												motif_statistics.put(key, list);
											} else {
												LinkedList list = new LinkedList();
												list.add("present_but_missing_in_interactiveDB");
												motif_statistics.put(key, list);
											}
										} else {
											out.write(str + "\t" + kinase + "\t"
													//+ kinase
													+ ":missing_in_interactiveDB\tNA\n");
											if (motif_statistics.containsKey(key)) {
												LinkedList list = (LinkedList) motif_statistics
														.get(key);
												list.add("missing_in_interactiveDB");
												motif_statistics.put(key, list);
											} else {
												LinkedList list = new LinkedList();
												list.add("missing_in_interactiveDB");
												motif_statistics.put(key, list);
											}
										}
									}
								// }
								} // if kinase doesn't equal to NA
							} // for loop not necessary
						//}
					} else { // check if contains uniprot key

						out.write(str + "\t" + "NA"
								+ "\tMissingUniprot2GeneID\tNA\n");
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
				int found_predicted_neighbor = 0;
				int present_found_predicted_neighbor = 0;
				int other = 0;
				itr4 = motif_statistics.keySet().iterator();
				while (itr4.hasNext()) {
					String key = (String) itr4.next();
					LinkedList list = (LinkedList) motif_statistics.get(key);
					if (list.contains("present_found_neighbor")) {
						present_found_neighbor++;
					} else if (list
							.contains("present_found_predicted_neighbor")) {
						present_found_predicted_neighbor++;
					} else if (list.contains("present_but_no_neighbor")) {
						present_but_no_neighbor++;
					} else if (list.contains("found_neighbor")) {
						found_neighbor++;
					} else if (list.contains("found_predicted_neighbor")) {
						found_predicted_neighbor++;
					} else if (list.contains("no_neighbor")) {
						no_neighbor++;
					} else if (list
							.contains("present_but_missing_in_interactiveDB")) {
						present_but_missing_in_interactiveDB++;
					} else if (list.contains("missing_in_interactiveDB")) {
						missing_in_interactiveDB++;
					} else {
						other++;
					}
				}

				int count_neg_found = 0;
				int count_neg_not_found = 0;

				itr4 = negative_dataset.keySet().iterator();
				while (itr4.hasNext()) {
					String key = (String) itr4.next();
					String found_tag = (String) negative_dataset.get(key);
					if (found_tag.equals("FOUND")) {
						count_neg_found++;
					} else {
						count_neg_not_found++;
					}
				}
				double countPredicted = 0;
				itr4 = uniq_protein_phosite.keySet().iterator();
				while (itr4.hasNext()) {
					String key = (String) itr4.next();
					String tag = (String) uniq_protein_phosite.get(key);
					if (!tag.equals("NA")) {
						countPredicted++;
					}
				}
				double proportionFoundPredicted = new Double(
						(new Double(countPredicted
								/ uniq_protein_phosite.size() * 10000))
								.intValue()) / 100;
				System.out.println("### Network Neighbor Statistics For each Unique Protein Position ###");
				summary_out.write("### Network Neighbor Statistics For each Unique Protein Position ###\n");
				System.out.println("Present in Motif DB and Kin-Sub DB annotated its neighbor also substrate for same Kinase: "
								+ present_found_neighbor);
				summary_out.write("Present in Motif DB and DB annotated its neighbor also subtrate for same Kinase: "
								+ present_found_neighbor + "\n");
				System.out.println("Present in Motif DB and neighbor is also subtrate for same Kinase based on prediction: "
								+ present_found_predicted_neighbor);
				summary_out.write("Present in Motif DB and neighbor is also subtrate for same Kinase based on prediction: "
								+ present_found_predicted_neighbor + "\n");
				System.out.println("Present in Motif DB but its neighbor is not: "
								+ present_but_no_neighbor);
				summary_out.write("Present in Motif DB but its neighbor is not: "
								+ present_but_no_neighbor + "\n");
				System.out.println("Not in Motif DB (Shouldn't Happen). Its neighbor is substrate of same kinase based on DB: "
								+ found_neighbor);
				summary_out.write("Not in Motif DB (Shouldn't Happen). Its neighbor is substrate of same kinase based on DB: "
								+ found_neighbor + "\n");
				System.out.println("Not in Motif DB (Shouldn't Happen). Its neighbor is also substr for same kinase based on prediction: "
								+ found_predicted_neighbor);
				summary_out.write("Not in Motif DB (Shouldn't Happen). Its neighbor is also substr for same kinase based on prediction: "
								+ found_predicted_neighbor + "\n");
				System.out.println("Substrate is part of interactiveDB, but none of its neighbor has same kinase-substr: "
								+ no_neighbor);
				summary_out.write("Substrate is part of interactiveDB, but none of its neighbor has same kinase-substr: "
								+ no_neighbor + "\n");
				System.out.println("Present in Kin-Sub DB but it is not part of interactiveDB: "
								+ present_but_missing_in_interactiveDB);
				summary_out.write("Present in Kin-Sub DB but it is not part of interactiveDB: "
								+ present_but_missing_in_interactiveDB + "\n");
				System.out.println("Substrate is absent from interactiveDB: "
						+ missing_in_interactiveDB);
				summary_out.write("Substrate is absent from interactiveDB: "
						+ missing_in_interactiveDB + "\n");
				// System.out.println("Other: " + other);
				// summary_out.write("Other: " + other + "\n");

				System.out.println("\n#### Estimated Sensitivity and Specificity ####");
				summary_out.write("\n####Estimated Sensitivity and Specificity ####"
								+ "\n");
				System.out.println("Percentage of Phosphosite With Predicted Kinase-Substrate: "
								+ proportionFoundPredicted + "%");
				summary_out.write("Percentage of Phosphosite With Predicted Kinase-Substrate: "
								+ proportionFoundPredicted + "%" + "\n");
				System.out.println("Sensitivity: "
						+ new Double(present_found_neighbor)
						/ (present_found_neighbor + present_but_no_neighbor));
				summary_out.write("Sensitivity: "
						+ new Double(present_found_neighbor)
						/ (present_found_neighbor + present_but_no_neighbor)
						+ "\n");
				System.out.println("Specificity: "
						+ new Double(count_neg_not_found)
						/ (count_neg_found + count_neg_not_found));
				summary_out.write("Specificity: "
						+ new Double(count_neg_not_found)
						/ (count_neg_found + count_neg_not_found) + "\n");
				summary_out.close();

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
			String gene = (String) itr.next();
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
					if (map.containsKey(motifName)) {
						String stuff = (String) map.get(motifName);
						stuff += "," + split[2];
						map.put(motifName, stuff);
					} else {
						map.put(motifName, split[2]);
					}

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
					for (String kinase : split[2].split(",")) {
						if (map.containsKey(kinase.toUpperCase())) {
							LinkedList list = (LinkedList) map.get(kinase
									.toUpperCase());
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
	 * 
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
				if (!kinase.equals("NA")) {
					if (map.containsKey(kinase)) {
						LinkedList list = (LinkedList) map.get(kinase);
						list.add(sub_geneName);
						map.put(kinase, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(sub_geneName);
						map.put(kinase, list);
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
	 * 
	 * @param fileName
	 * @return
	 */
	/*
	 * public static HashMap getKnownKinaseSubstrateList(String fileName,
	 * HashMap uniprot2geneID) { HashMap map = new HashMap(); try {
	 * 
	 * FileInputStream fstream = new FileInputStream(fileName); DataInputStream
	 * din = new DataInputStream(fstream); BufferedReader in = new
	 * BufferedReader(new InputStreamReader(din)); while (in.ready()) { String
	 * str = in.readLine(); String[] split = str.split("\t"); String uniprot =
	 * split[0].split("\\|")[1]; String geneID =
	 * (String)uniprot2geneID.get(uniprot); String loc = split[1]; for (String
	 * kinase: split[6].split(",")) { SubstrateInfo info = new SubstrateInfo();
	 * info.UniprotAcc = uniprot; info.GeneName = geneID; info.ModSite = loc; if
	 * (map.containsKey(kinase)) { LinkedList list =
	 * (LinkedList)map.get(kinase); list.add(info); map.put(kinase, list); }
	 * else { LinkedList list = new LinkedList(); list.add(info);
	 * map.put(kinase, list); } } } in.close();
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return map; }
	 */

	public static class SubstrateInfo {

		public static String GeneName = "";
		public static String UniprotAcc = "";
		public static String ModSite = "";
	}
}
