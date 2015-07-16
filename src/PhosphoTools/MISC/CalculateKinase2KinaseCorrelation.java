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

/**
 * Use the original peptide information to append the original peptide information
 * for each result
 * @author tshaw
 *
 */
public class CalculateKinase2KinaseCorrelation {

	public static void execute(String[] args) {
		try {
			String inputMotifFile = args[0];
			String kinase_name = args[1]; //
			String totalProteome = args[2];
			String grouping = args[3];
			String outputFile = args[4]; // new motif file;
			
			FileWriter fwriter = new FileWriter(outputFile);
            BufferedWriter out = new BufferedWriter(fwriter);
            out.write("QueryKinase\tKinase\tPearson\tSpearman\tQueryPDGFRA\tQueryNTRK1_1\tQueryNTRK1_2\tQueryCNTRL\tKinasePDGFRA\tKinaseNTRK1_1\tKinaseNTRK1_2\tKinaseCNTRL\n");
            HashMap total = grabDataFromTotal(totalProteome, grouping);
            
            String queryDataStr = "";
			String[] queryDataSplit = null;
			double[] queryDataNum = null;
            HashMap query_kinase_peptide_hash = (HashMap)total.get(kinase_name);
			Iterator itr_kinase = query_kinase_peptide_hash.keySet().iterator();
			while (itr_kinase.hasNext()) {
				String query_peptide_str = (String)itr_kinase.next();
				String totalData = (String)query_kinase_peptide_hash.get(query_peptide_str);
				queryDataStr = totalData;
				queryDataSplit = totalData.split("\t");
				queryDataNum = new double[queryDataSplit.length];			
				for (int i = 0; i < queryDataNum.length; i++) {
					queryDataNum[i] = new Double(queryDataSplit[i]);
				}
			}
			HashMap map = readKinase(inputMotifFile);
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String kinase = ((String)itr.next()).toUpperCase();
				if (total.containsKey(kinase)) {
					String total_data_str = "";
					String[] totalDataSplit = null;
					double[] totalDataNum = null;
					HashMap kinase_peptide_hash = (HashMap)total.get(kinase);
					itr_kinase = kinase_peptide_hash.keySet().iterator();
					while (itr_kinase.hasNext()) {
						String peptideStr = (String)itr_kinase.next();
						String totalData = (String)kinase_peptide_hash.get(peptideStr);
						total_data_str = totalData;
						totalDataSplit = totalData.split("\t");
						totalDataNum = new double[totalDataSplit.length];
						for (int i = 0; i < totalDataSplit.length; i++) {
							totalDataNum[i] = new Double(totalDataSplit[i]);
						}						
					}					
					
					double pearson = MathTools.PearsonCorrel(queryDataNum,  totalDataNum);
					double spearman = MathTools.SpearmanRank(queryDataNum,  totalDataNum);
					out.write(kinase_name + "\t" + kinase + "\t" + pearson + "\t" + spearman + "\t" + queryDataStr + "\t" + total_data_str + "\n");
				}
			}
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static HashMap grabDataFromTotal(String fileName, String groupInfo) {
		HashMap map = new HashMap();
		String[] groups = groupInfo.split(":");
		
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
					
					
					if (checkGeneNameUniprotCombo(geneName, uniprotName)) {
						if (split.length <= 23) {
							if (geneName.equals("AKT1")) {
								System.out.println(str);
							}
							//System.out.println(str);
						} else {
							String data = "";
							//String data = split[22];
							//for (int i = 23; i <= 31; i++) {
								//data += "\t" + split[i];
							//}
							LinkedList[] list_groups = new LinkedList[groups.length];
							for (int i = 0; i < groups.length; i++) {
								list_groups[i] = new LinkedList();
							}
							int buffer = 22;
							
							// convert the data into a linkedlist
							for (int i = 0 + buffer; i <= 9 + buffer; i++) {
								for (int j = 0; j < groups.length; j++) {
									String[] split_group = groups[j].split(",");
									for (int k = 0; k < split_group.length; k++) {
										int group_id = new Integer(split_group[k].trim());
										if (group_id == (i - buffer)) {
											list_groups[j].add(split[i]);
										}
									}
								}
							}
							
							// place the data into a linkedlist
							
							for (int j = 0; j < groups.length; j++) {
								double[] num = MathTools.convertListStr2Double(list_groups[j]);
								if (j == 0) {
									data += MathTools.mean(num) + "";
								} else {
									data += "\t" + MathTools.mean(num);
								}
							}
							//System.out.println(geneName + "\t" + data);
							if (map.containsKey(geneName)) {
								HashMap peptideHash = (HashMap)map.get(geneName);
								peptideHash.put(uniprotName, data);
								map.put(geneName, peptideHash);
								if (geneName.equals("AKT1")) {
									System.out.println("Should add to map");
								}
							} else {
								HashMap peptideHash = new HashMap();
								peptideHash.put(uniprotName, data);
								map.put(geneName, peptideHash);
								if (geneName.equals("AKT1")) {
									System.out.println("Else Should add to map");
								}
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
	
	public static HashMap readKinase(String inputIDSumFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputIDSumFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				map.put(str, str);
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
					
					String uniq_name = uniprot + "." + runNum + "." + scanNum;
					map.put(uniq_name, gene_name);
				}
				
			}
			in.close();						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;		
	}
	public static HashMap idsum2JscoredJnQValue(String inputIDSumFile) {
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
					
					String mz = split[8];
					String z = split[9];
					String ppm = split[10];
					String jscore = split[11];
					String dJN = split[12];
					String qvalue = split[13];
					
					String uniq_name = uniprot + "." + runNum + "." + scanNum;
					//System.out.println(uniq_name);
					String stuff = mz + "\t" + z + "\t" + ppm + "\t" + jscore + "\t" + dJN + "\t" + qvalue;
					map.put(uniq_name, stuff);
				}
			}
			in.close();						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;		
	}
	public static HashMap peptide2inference(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split  = str.split("\t");
				String seq = MotifTools.cleanAll(split[3]);
				if (map.containsKey(seq)) {
					String name = (String)map.get(seq);
					name += "," + split[2];
					map.put(seq, name);
				} else {
					map.put(seq, split[2]);
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

