package proteomics.phospho.tools.motifs;

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

/**
 * Add relative quantification
 * @author tshaw
 *
 */
public class AddRelativeQuantificationForPredicted {

	public static void execute(String[] args) {
		try {
			String originalFile = args[0];
			String ascoreFile = args[1];
			String totalProteomeFile = args[2];
			String kinase_name = args[3];
			String motif_name = args[4];
			String groupInfo = args[5];
			String outputFile = args[6];
			
			//System.out.println("Running Grab Data From Ascore");
			HashMap ascore = grabDataFromAscore(ascoreFile, groupInfo);
			//System.out.println("Load Ascore File");
			HashMap total = grabDataFromTotal(totalProteomeFile, groupInfo);
			//System.out.println("Load Total Proteome File");

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			HashMap uniq = new HashMap();
			int count = 0;
			FileInputStream fstream = new FileInputStream(originalFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//String[] geneName_ids = split[6].split(",");
				String geneName = "";
				/*if (split[5].equals("Akt kinase substrate motif")) {
					geneName = "AKT1";
				}*/
				
				if (split[5].replaceAll(" ", "_").equals(motif_name)) {
					geneName = "AKT1";
				
					String peptide = split[3];
				
					//for (String geneName: geneName_ids) {
					//geneName = geneName.toUpperCase();
					if (!geneName.equals("")) {
						String ascoreData = (String)ascore.get(peptide);
						if (total.containsKey(kinase_name) && !geneName.equals("NA")) {
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
								double pearson = MathTools.PearsonCorrel(ascoreDataNum,  totalDataNum);
								double spearman = MathTools.SpearmanRank(ascoreDataNum,  totalDataNum);
								String tag = peptide + kinase_name + "_" + peptideStr;
								if (!uniq.containsKey(tag)) {
									out.write(str + "\t" + pearson + "\t" + spearman + "\t" + peptide + "\t" + ascoreData + "\t" + geneName + "_" + peptideStr + "\t" + totalData + "\n");									
									uniq.put(tag, tag);
								}
							}
						}
					}
				//}
				}
				count++;
				//System.out.println(count);
			}
			in.close();
			out.close();
			//System.out.println("Finish Appending");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap grabDataFromAscoreUniprot(String fileName, String groupInfo) {
		HashMap map = new HashMap();
		String[] groups = groupInfo.split(":");
		
		try {
			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = split[3];
				LinkedList list = MotifTools.convert(MotifTools.replaceTag(peptide.trim()));
				
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					String peptides = (String)itr.next();
					String data = ""; // the content of the data info
					//String data = split[7];
					
					//for (int i = 8; i <= 16; i++) {
						//data += "\t" + split[i];
					//}
					// buffer is for column where the index of the data starts
					int buffer = 7;
					LinkedList[] list_groups = new LinkedList[groups.length];
					for (int i = 0; i < groups.length; i++) {
						list_groups[i] = new LinkedList();
					}
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
					//System.out.println(data);
					map.put(peptides, data);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabDataFromAscore(String fileName, String groupInfo) {
		HashMap map = new HashMap();
		String[] groups = groupInfo.split(":");
		
		try {
			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String peptide = split[3];
				LinkedList list = MotifTools.convert(MotifTools.replaceTag(peptide.trim()));
				
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					String peptides = (String)itr.next();
					String data = ""; // the content of the data info
					//String data = split[7];
					
					//for (int i = 8; i <= 16; i++) {
						//data += "\t" + split[i];
					//}
					// buffer is for column where the index of the data starts
					int buffer = 7;
					LinkedList[] list_groups = new LinkedList[groups.length];
					for (int i = 0; i < groups.length; i++) {
						list_groups[i] = new LinkedList();
					}
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
					//System.out.println(data);
					map.put(peptides, data);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap grabDataFromTotalUniprot(String fileName, String groupInfo) {
		HashMap map = new HashMap();
		String[] groups = groupInfo.split(":");
		
		try {
						
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 3) {
					//String acc_id = split[1].split("\\|")[1];
					//String geneName = split[3].toUpperCase();
					String uniprot = split[1];
					String peptide = split[8];
					String uniprotName = split[1];
					if (split.length <= 23) {
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
						if (map.containsKey(uniprot)) {
							HashMap peptideHash = (HashMap)map.get(uniprot);
							peptideHash.put(uniprotName, data);
							map.put(uniprot, peptideHash);
						} else {
							HashMap peptideHash = new HashMap();
							peptideHash.put(uniprotName, data);
							map.put(uniprot, peptideHash);
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
	public static HashMap grabDataFromTotal(String fileName, String groupInfo) {
		HashMap map = new HashMap();
		String[] groups = groupInfo.split(":");
		
		try {
			
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 3) {
					//String acc_id = split[1].split("\\|")[1];
					String geneName = split[3].toUpperCase();
					String peptide = split[8];
					String uniprotName = split[1];
					if (split.length <= 23) {
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
						} else {
							HashMap peptideHash = new HashMap();
							peptideHash.put(uniprotName, data);
							map.put(geneName, peptideHash);
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
}
