package proteomics.phospho.tools.evaluation;

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

import proteomics.phospho.tools.motifs.AddRelativeQuantificationForPredicted;
import proteomics.phospho.tools.motifs.MotifTools;
import statistics.general.MathTools;


/**
 * Purpose of this class is to generate the ROC curve 
 * for each kinase-substrate pair
 * @author tshaw
 *
 */
public class GenerateROCCurveRandomRandom {

	public static void execute(String[] args) {
		
		try {
			
			// step 1 calculate sensitivity
			// For each kinase and substrate pair, identify them in our data. 
			// For each kinase and substrate predicted pair, identify them in our data.
			// compare the validated to the identified and calculate a ratio for sensitivity.
			 
			String kinaseGeneName = args[0].toUpperCase(); // kinase that we will examine
			String kinase_substrate_file = args[1];
			String phospho_fasta_extended = args[2];
			String predicted_substrate_site_file = args[3]; //
			String kinase_motif_name_file = args[4]; // file containing the kinase motif name being queried
			String kinase_motif_name = readKinaseMotifName(kinase_motif_name_file);			
			String groupInfo = args[5];
			String ascoreFile = args[6];
			String totalProteomeFile = args[7];			
			String motifFile = args[8];
			String total_proteome_fasta = args[9];
			String outputFile = args[10];			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap map_motif = MotifTools.grabMotif(motifFile); 
			LinkedList query_name_list = MotifTools.grabFastaQueryName(total_proteome_fasta); 
			LinkedList query_list = MotifTools.grabFastaQuery(total_proteome_fasta); //"C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\SusanBaker_Mouse_Hong\\Analysis\\Differential_Peptide_Analysis\\AddFastSequence\\extended.fasta");
			
			
			HashMap hit = new HashMap();
			Iterator motif_itr = map_motif.keySet().iterator();
			while (motif_itr.hasNext()) {
				String motif = (String)motif_itr.next();
				String name = (String)map_motif.get(motif);
				if (name.equals(kinase_motif_name)) {
					double total = 0;
					double space = 0;
					double narrow = 0;
					Iterator itr_name = query_name_list.iterator();
					Iterator itr = query_list.iterator();
					while (itr.hasNext()) {
						String geneName = (String)itr_name.next();
						String seq = (String)itr.next();
						String type = "";
						
						seq = MotifTools.replaceTag(seq);
						// when counting motifs we disregard the phosphorylation location, or else can't really count
						/*seq = seq.replaceAll("Y", "Y*");
						seq = seq.replaceAll("T", "T*");
						seq = seq.replaceAll("S", "S*");*/
						boolean found = false;
						if (seq.contains("Y#")) {
							type = "Y";
						}
						if (seq.contains("S#")) {
							type = "S";
						}
						if (seq.contains("T#")) {
							type = "T";
						}
						double count = MotifTools.countMotif(seq, motif, false);
						if (count >= 1) {
							count = 1;
							
						}	
						if (count == 0) {
							hit.put(geneName, geneName);
						}
					}
				}
			}
									
			HashMap ascore = AddRelativeQuantificationForPredicted.grabDataFromAscore(ascoreFile, groupInfo);			
			HashMap total = AddRelativeQuantificationForPredicted.grabDataFromTotal(totalProteomeFile, groupInfo);
			HashMap uniprot_total = AddRelativeQuantificationForPredicted.grabDataFromTotalUniprot(totalProteomeFile, groupInfo);
			
			HashMap kinase2substrate = readKinase2Substrate(kinase_substrate_file, kinaseGeneName);
			HashMap phosphoHit = get_phospho_site(phospho_fasta_extended, kinase2substrate);
			HashMap predictedHit = GrabKinaseMotif(predicted_substrate_site_file, kinase_motif_name);
									
			int overlap = countOverlap(phosphoHit, predictedHit);
			HashMap filterPhosphoHit = filterPhosphoHit(phosphoHit, predictedHit);
			HashMap correlation = calculateCorrelation(filterPhosphoHit, ascore, total, kinaseGeneName);
			
			//HashMap correlation_total = calculateCorrelationTotal(total, uniprot_total, kinaseGeneName);
			HashMap correlation_total = calculateCorrelationRandom(total, uniprot_total);
			
			for (double spearman_cutoff = -1.0000000; spearman_cutoff <= 1.0000001; spearman_cutoff += 0.01) {
				int count = 0;
				int total_count = 0;
				Iterator itr = correlation.keySet().iterator();
				while (itr.hasNext()) {
					String key = (String)itr.next();
					double spearman = (Double)correlation.get(key);
					total_count++;
					if (spearman >= spearman_cutoff) {
						count++;
					}

				}
				//double sensitivity = new Double(count) / overlap;
				double sensitivity = new Double(count) / total_count;
				int total_neg = 0;
				int count_neg = 0;
				Iterator itr2 = hit.keySet().iterator();
				while (itr2.hasNext()) {
					String uniprot = (String)itr2.next();				
					if (correlation_total.containsKey(uniprot)) {
						double spearman_total = (Double)correlation_total.get(uniprot);
						if (spearman_total >= spearman_cutoff) {
							
						} else {
							count_neg++;
						}
						total_neg++;
					}
				}
				double specificity = new Double(count_neg) / total_neg;
				//System.out.println(count + "\t" + total_count);
				System.out.println(spearman_cutoff + "\t" + count + "\t" + total_count + "\t" + sensitivity + "\t" + count_neg + "\t" + total_neg + "\t" + specificity);
				out.write(spearman_cutoff + "\t" + sensitivity + "\t" + specificity + "\n");
				
				//System.out.println("Overlap Between Predicted and Validated: " + sensitivity);
			} // spearman_cutoff iterator
											
			out.close();
			System.out.println("Total Validated Hits: " + overlap);
			System.out.println("Total Validated: " + phosphoHit.size());			
			System.out.println("Total Predicted: " + predictedHit.size());
			System.out.println("Total in PhosphositePlus: " + kinase2substrate.size());
			System.out.println("Queries Kinase Motif: " + kinase_motif_name);
			System.out.println("Total Hit in Total Proteome: " + hit.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap calculateCorrelationRandom(HashMap total, HashMap totalUniprot) {
		HashMap map = new HashMap();
		try {
			
			Random rand = new Random();
			
			Iterator itr4 = total.keySet().iterator();
			while (itr4.hasNext()) {
				String kinase_name = (String)itr4.next();
				HashMap peptideHash = (HashMap)total.get(kinase_name);
				if (rand.nextDouble() < 0.05) {
				
					Iterator itr = peptideHash.keySet().iterator();
					while (itr.hasNext()) {
						String peptideStr = (String)itr.next();
						String totalData = (String)peptideHash.get(peptideStr);
						//String totalData = (String)total.get(geneName);			
						
						String[] totalDataSplit = totalData.split("\t");
						double[] totalDataNum = new double[totalDataSplit.length];
						for (int i = 0; i < totalDataSplit.length; i++) {
							totalDataNum[i] = new Double(totalDataSplit[i]);
						}
						
						Iterator itr2 = totalUniprot.keySet().iterator();
						while (itr2.hasNext()) {
							
							String uniprot = (String)itr2.next();
							HashMap peptideHash2 = (HashMap)totalUniprot.get(uniprot);
							double spearman = -1;
							Iterator itr3 = peptideHash2.keySet().iterator();
							while (itr3.hasNext()) {
								String peptideStr2 = (String)itr3.next();
								String totalData2 = (String)peptideHash2.get(peptideStr2);
								String[] totalDataSplit2 = totalData2.split("\t");
								double[] totalDataNum2 = new double[totalDataSplit2.length];
								for (int i = 0; i < totalDataSplit2.length; i++) {
									totalDataNum2[i] = new Double(totalDataSplit2[i]);
								}	
								//if (spearman < MathTools.SpearmanRank(totalDataNum2,  totalDataNum)) {
									spearman = MathTools.SpearmanRank(totalDataNum2,  totalDataNum);
								//}
							}
							if (rand.nextDouble() < 0.05) {
								
								map.put(uniprot, spearman);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap calculateCorrelationTotal(HashMap total, HashMap totalUniprot, String kinase_name) {
		HashMap map = new HashMap();
		try {
			
			HashMap peptideHash = (HashMap)total.get(kinase_name);
			Iterator itr = peptideHash.keySet().iterator();
			while (itr.hasNext()) {
				String peptideStr = (String)itr.next();
				String totalData = (String)peptideHash.get(peptideStr);
				//String totalData = (String)total.get(geneName);			
				
				String[] totalDataSplit = totalData.split("\t");
				double[] totalDataNum = new double[totalDataSplit.length];
				for (int i = 0; i < totalDataSplit.length; i++) {
					totalDataNum[i] = new Double(totalDataSplit[i]);
				}
				
				Iterator itr2 = totalUniprot.keySet().iterator();
				while (itr2.hasNext()) {
					
					String uniprot = (String)itr2.next();
					HashMap peptideHash2 = (HashMap)totalUniprot.get(uniprot);
					double spearman = -1;
					Iterator itr3 = peptideHash2.keySet().iterator();
					while (itr3.hasNext()) {
						String peptideStr2 = (String)itr3.next();
						String totalData2 = (String)peptideHash2.get(peptideStr2);
						String[] totalDataSplit2 = totalData2.split("\t");
						double[] totalDataNum2 = new double[totalDataSplit2.length];
						for (int i = 0; i < totalDataSplit2.length; i++) {
							totalDataNum2[i] = new Double(totalDataSplit2[i]);
						}	
						//if (spearman < MathTools.SpearmanRank(totalDataNum2,  totalDataNum)) {
							spearman = MathTools.SpearmanRank(totalDataNum2,  totalDataNum);
						//}
					}
					
					map.put(uniprot, spearman);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap calculateCorrelation(HashMap phosphoHit, HashMap ascore, HashMap total, String kinase_name) {
		HashMap map = new HashMap();
		try {
			
			Iterator itr2 = phosphoHit.keySet().iterator();
			while (itr2.hasNext()) {
				String key = (String)itr2.next();
				//System.out.println((String)phosphoHit.get(key));
				for (String orig_peptide : ((String)phosphoHit.get(key)).split("\t")) {
					double pearson = -1;
					double spearman = -1;
					
					LinkedList list = MotifTools.convert(MotifTools.replaceTag(orig_peptide.trim()));
					
					Iterator itr3 = list.iterator();
					while (itr3.hasNext()) {
						String peptide = (String)itr3.next();
						//System.out.println(peptide);
						String ascoreData = (String)ascore.get(peptide);
						HashMap peptideHash = (HashMap)total.get(kinase_name);
						Iterator itr = peptideHash.keySet().iterator();
						while (itr.hasNext()) {
							String peptideStr = (String)itr.next();
							String totalData = (String)peptideHash.get(peptideStr);
							//String totalData = (String)total.get(geneName);			
							String[] ascoreDataSplit = ascoreData.split("\t");
							double[] ascoreDataNum = new double[ascoreDataSplit.length];
							for (int i = 0; i < ascoreDataSplit.length; i++) {
								ascoreDataNum[i] = new Double(ascoreDataSplit[i]);
							}
							String[] totalDataSplit = totalData.split("\t");
							double[] totalDataNum = new double[totalDataSplit.length];
							for (int i = 0; i < totalDataSplit.length; i++) {
								totalDataNum[i] = new Double(totalDataSplit[i]);
							}
							pearson = MathTools.PearsonCorrel(ascoreDataNum,  totalDataNum);
							//if (spearman < MathTools.SpearmanRank(ascoreDataNum,  totalDataNum)) {
								spearman = MathTools.SpearmanRank(ascoreDataNum,  totalDataNum);
							//}
							//spearman = MathTools.SpearmanRank(ascoreDataNum,  totalDataNum);					
						}
						map.put(key, spearman);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static String grabExpressionData(String inputFile) {
		return "";
	}
	public static String readKinaseMotifName(String inputFile) {
		String result = "";
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.trim().equals("")) {
					result = str;
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static int countOverlap(HashMap phosphoHit, HashMap predictedHit) {
		int count = 0;
		Iterator itr = phosphoHit.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			if (predictedHit.containsKey(key)) {
				count++;
			} else {
				//System.out.println(key);
			}
		}
		return count;
	}
	
	public static HashMap filterPhosphoHit(HashMap phosphoHit, HashMap predictedHit) {
		HashMap map = new HashMap();
		int count = 0;
		Iterator itr = phosphoHit.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String)itr.next();
			if (predictedHit.containsKey(key)) {
				map.put(key, phosphoHit.get(key));
			} else {
				//System.out.println(key);
			}
		}
		return map;
	}
	/**
	 * Generate a hashmap of uniq phospho site
	 * @param inputFile
	 * @return
	 */
	public static HashMap GrabKinaseMotif(String inputFile, String kinase_motif_name) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[5].equals(kinase_motif_name)) {
					//System.out.println(str);
					String uniprot = split[0].split("\\|")[1];
					
					String new_loc = "";
					for (String loc : split[1].split(",")) {
						if (loc.contains("S")) {
							new_loc = "S" + loc.split("S")[0];
						}
						if (loc.contains("T")) {
							new_loc = "T" + loc.split("T")[0];					
						}
						if (loc.contains("Y")) {
							new_loc = "Y" + loc.split("Y")[0];
						}
						map.put(uniprot + "\t" + new_loc, "");
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
	 * Generate a hashmap of uniq phospho site
	 * @param inputFile
	 * @return
	 */
	public static HashMap get_phospho_site(String inputFile, HashMap substrate) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					String[] split = str.split("\t");
					String uniprot_id = split[0].split("\\|")[1];
					String new_loc = "";
					String seq = in.readLine();
					String peptide = seq.split("\t")[1];
					for (String loc : split[1].split(",")) {
						if (loc.contains("S")) {
							new_loc = "S" + loc.split("S")[0];
						}
						if (loc.contains("T")) {
							new_loc = "T" + loc.split("T")[0];					
						}
						if (loc.contains("Y")) {
							new_loc = "Y" + loc.split("Y")[0];
						}
						
						if (substrate.containsKey(uniprot_id + "\t" + new_loc)) {
							//System.out.println("get_phospho_site: " + uniprot_id + "\t" + new_loc);
							if (map.containsKey(uniprot_id + "\t" + new_loc)) {
								String orig_peptide = (String)map.get(uniprot_id + "\t" + new_loc);
								
								orig_peptide += "\t" + peptide;
								map.put(uniprot_id + "\t" + new_loc, orig_peptide);
							} else {
								map.put(uniprot_id + "\t" + new_loc, peptide);
							}
							
						}
					}										
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("Total found: " + map.size());
		return map;
	}
	public static HashMap readKinase2Substrate(String inputFile, String kinase) {
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split[0].toUpperCase().equals(kinase)) {
					
					String mouse_uniprot = split[3];
					String site = split[4];
					if (split[3].equals("CONVERT_UNIPROT")) {
						mouse_uniprot = split[5];
						site = split[8];
					}
					map.put(mouse_uniprot + "\t" + site, "");
					//System.out.println(mouse_uniprot + "\t" + site);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
