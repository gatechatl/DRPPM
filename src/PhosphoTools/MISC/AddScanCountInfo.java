package PhosphoTools.MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import PhosphoTools.MotifTools.MotifTools;
import Statistics.General.MathTools;

public class AddScanCountInfo {

	public static void execute(String[] args) {
		try {
			String originalFile = args[0];
			String idsum_file = args[1];
			String totalProteomeFile = args[2];			
			String outputFile = args[3];
			String output1K1S = args[4];
			HashMap tag2geneName = idsum2GeneName(idsum_file);			
			
			System.out.println("Running Grab Data From Ascore");
			HashMap total = grabPSMCountFromTotal(totalProteomeFile);
			System.out.println("Load Total Proteome File");

			FileWriter fwriter1k1s = new FileWriter(output1K1S);
			BufferedWriter out1k1s = new BufferedWriter(fwriter1k1s);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap kinase2substrate = new HashMap();
			HashMap uniq = new HashMap();
			int count = 0;
			FileInputStream fstream = new FileInputStream(originalFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine(); // skip header
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String uniprot = split[0];
				String[] geneName_ids = split[6].split(",");
				String peptide = split[3];
				for (String geneName: geneName_ids) {
					geneName = geneName.toUpperCase();
					if (!geneName.equals("")) {
						String sub_geneName = ((String)tag2geneName.get(uniprot)).toUpperCase();
						//System.out.println(sub_geneName + "\t" + split[6]);
						if (total.containsKey(sub_geneName)) {
						String ascoreData = (String)total.get(sub_geneName);
						if (total.containsKey(geneName) && !geneName.equals("NA")) {
							//HashMap peptideHash = (HashMap)total.get(geneName);
							//Iterator itr = peptideHash.keySet().iterator();
							//while (itr.hasNext()) {
								//String peptideStr = (String)itr.next();
								String totalData = (String)total.get(geneName);
								//String totalData = (String)peptideHash.get(peptideStr);
								//String totalData = (String)total.get(geneName);
								
								String[] subDataSplit = ascoreData.split("\t");
								double[] ascoreDataNum = new double[subDataSplit.length];
								for (int i = 0; i < subDataSplit.length; i++) {
									ascoreDataNum[i] = new Double(subDataSplit[i]);
								}
								String[] totalDataSplit = totalData.split("\t");
								double[] totalDataNum = new double[totalDataSplit.length];
								for (int i = 0; i < totalDataSplit.length; i++) {
									totalDataNum[i] = new Double(totalDataSplit[i]);
								}
								//double pearson = MathTools.PearsonCorrel(ascoreDataNum,  totalDataNum);
								//double spearman = MathTools.SpearmanRank(ascoreDataNum,  totalDataNum);
								//String tag = peptide + geneName + "_" + peptideStr;
								String tag = peptide + geneName;
								if (!uniq.containsKey(tag)) {
									
									
									if (kinase2substrate.containsKey(geneName)) {
										Kinase2Substrate ks = (Kinase2Substrate)kinase2substrate.get(geneName);
										ks.SubstrateName += "," + sub_geneName;
										ks.SubstrateScan += new Double(ascoreData);
										ks.totalSubstrate++;
										kinase2substrate.put(geneName, ks);
									} else {
										Kinase2Substrate ks = new Kinase2Substrate();
										ks.KinaseName = geneName;
										ks.KinaseScan = new Double(totalData);
										ks.SubstrateName = sub_geneName;
										ks.SubstrateScan = new Double(ascoreData);
										ks.totalSubstrate++;
										kinase2substrate.put(geneName, ks);
									}
									out.write(str + "\t" + sub_geneName + "\t" + ascoreData + "\t" + geneName + "\t" + totalData + "\n");
									
									uniq.put(tag, tag);
								}
							//}
							}
						}
					}
				}
				count++;
				//System.out.println(count);
			}
			in.close();
			out.close();
			
			Iterator itr = kinase2substrate.keySet().iterator();
			while (itr.hasNext()) {
				String kinase_name = (String)itr.next();
				Kinase2Substrate ks = (Kinase2Substrate)kinase2substrate.get(kinase_name);
				out1k1s.write(ks.KinaseName + "\t" + ks.KinaseScan + "\t" + Math.log(ks.KinaseScan) + "\t" + ks.SubstrateName + "\t" + (ks.SubstrateScan / ks.totalSubstrate) + "\t" + Math.log(ks.SubstrateScan / ks.totalSubstrate) + "\n");				
			}
			System.out.println("Finish Appending");
			out1k1s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static class Kinase2Substrate {
		public String KinaseName = "";
		public String SubstrateName = "";
		double KinaseScan = 0;
		double SubstrateScan = 0;
		int totalSubstrate = 0;
	}
	public static HashMap grabAScoreFromPeptide(String fileName) {
		HashMap map = new HashMap();		
		try {					
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String uniprot = split[0];
				String peptide = split[3];
				String loc1 = split[17];
				String score1 = split[18];
				String loc2 = split[19];
				String score2 = split[20];
				String loc3 = split[21];
				String score3 = split[22];
				
				LinkedList list = MotifTools.convert(MotifTools.replaceTag(peptide.trim()));
				
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					String peptides = (String)itr.next();					
					map.put(peptides, loc1 + "\t" + score1 + "\t" + loc2 + "\t" + score2 + "\t" + loc3 + "\t" + score3);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabUniprotNameFromPeptide(String fileName) {
		HashMap map = new HashMap();
		
		
		try {
			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String uniprot = split[0];
				String peptide = split[3];
				LinkedList list = MotifTools.convert(MotifTools.replaceTag(peptide.trim()));
				
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					String peptides = (String)itr.next();					
					map.put(peptides, uniprot);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap idsum2GeneName(String inputIDSumFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputIDSumFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split  = str.split("\t");
				if (split.length > 9) {
					String uniprot = split[2];
					String runNum = split[6];
					String scanNum = split[7];
					
					String gene_name = split[4];
					
					
					map.put(uniprot, gene_name);
				}
				
			}
			in.close();						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;		
	}
	
	public static HashMap grabPSMCountFromTotal(String fileName) {
		HashMap map = new HashMap();
		HashMap map_count = new HashMap();
		
		try {
			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 3) {
					//String acc_id = split[1].split("\\|")[1];
					String geneName = split[3].toUpperCase();
					String peptide = split[8];
					String uniprotName = split[1];
					String psmCount = split[4];
					
					if (checkGeneNameUniprotCombo(geneName, uniprotName)) {
						if (split.length <= 23) {
							if (geneName.equals("AKT1")) {
								System.out.println(str);
							}
							//System.out.println(str);
						} else {
							//String data = "";
							//String data = split[22];
							//for (int i = 23; i <= 31; i++) {
								//data += "\t" + split[i];
							//}
							
							// place the data into a linkedlist
							
							/*for (int j = 0; j < groups.length; j++) {
								double[] num = MathTools.convertListStr2Double(list_groups[j]);
								if (j == 0) {
									data += MathTools.mean(num) + "";
								} else {
									data += "\t" + MathTools.mean(num);
								}
							}*/
							//System.out.println(geneName + "\t" + data);
							if (map.containsKey(geneName)) {
								double old_psm_count = new Double((String)map.get(geneName));
								//HashMap peptideHash = (HashMap)map.get(geneName);
								//peptideHash.put(uniprotName, psmCount);
								//map.put(geneName, peptideHash);
								if (new Double(psmCount) > old_psm_count) {
									map.put(geneName, psmCount);
								}
								/*if (geneName.equals("AKT1")) {
									System.out.println("Should add to map");
								}*/
							} else {
								HashMap peptideHash = new HashMap();
								peptideHash.put(uniprotName, psmCount);
								//map.put(geneName, peptideHash);
								map.put(geneName, psmCount);
								/*if (geneName.equals("AKT1")) {
									System.out.println("Else Should add to map");
								}*/
							}
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
	 * Specify the specific genename to uniprot geneid combination
	 * @param geneName
	 * @param uniprot
	 * @return
	 */
	public static boolean checkGeneNameUniprotCombo(String geneName, String uniprot) {
	
		if (geneName.toUpperCase().trim().equals("AKT1")) {
			//System.out.println(uniprot);
			if (uniprot.contains("P31750")) {
				//System.out.println("Should find");
				return true;
			}
			if (uniprot.contains("P31749")) {
				//System.out.println("Should find");
				return true;
			}
			return false;
		}
		
		return true;
	}
}

