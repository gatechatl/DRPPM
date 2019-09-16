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
public class AddRelativeQuantificationForPredictedAllJUMP {

	public static void execute(String[] args) {
		try {
			String originalFile = args[0];
			String ascoreFile = args[1];
			String totalProteomeFile = args[2];
			//String kinase_name = args[3];
			
			//String kinase_gene = args[3];
			//String motif_name = args[4];
			String motif_all_file = args[3];			
			HashMap kinase_map = MotifTools.grabKinase2Motif(motif_all_file);
			
			String groupInfo = args[4];
			String outputFolder = args[5];
			
			String useTotal = args[6].trim();  // this is a flag for indicating the data used to determine kinase activity.
			// useTotal = 1 is total proteome then phospho proteome
			// useTotal = 2 is phospho proteome then whole
			// useTotal = 3 is user defined then same as 1
			// useTotal = 4 is user defined then same as 2
			String specialKase = args[7]; // this contains a list of special case


			String SubstrateTag = "";
			String KinaseTag = "";
			String[] groups = groupInfo.split(":");
			for (int i = 0; i < groups.length; i++) {
				if (i == 0) {
					SubstrateTag += "Sub_" + (i + 1);
					KinaseTag += "Kin_" + (i + 1);
				} else {
					SubstrateTag += "\tSub_" + (i + 1);
					KinaseTag += "\tKin_" + (i + 1);
				}
			}
			//String outputFile = args[6];
			
			//System.out.println("Running Grab Data From Ascore");
			HashMap ascore = AddRelativeQuantificationJUMP.grabDataFromPhosphoProteome(ascoreFile, groupInfo);
			//System.out.println("Load Ascore File");
			//HashMap total = AddRelativeQuantificationJUMP.grabDataFromTotal(totalProteomeFile, groupInfo);
			//HashMap total = AddRelativeQuantificationJUMP.grabDataFromTotal(ascoreFile, groupInfo);
			HashMap kinase_activity_phospho = AddRelativeQuantificationJUMP.grabKinaseActivityFromPhospho(ascoreFile, groupInfo);
			HashMap kinase_activity_wholepro= AddRelativeQuantificationJUMP.grabKinaseActivityFromWholeProteome(totalProteomeFile, groupInfo);
			HashMap total = new HashMap();
			if (useTotal.equals("1")) {
				total = AddRelativeQuantificationJUMP.combineKinaseActivity(kinase_activity_wholepro, kinase_activity_phospho);
			} else if (useTotal.equals("2")) {
				total = AddRelativeQuantificationJUMP.combineKinaseActivity(kinase_activity_phospho, kinase_activity_wholepro);
			} else {
				total = kinase_activity_phospho;
			}
			
			//System.out.println("Load Total Proteome File");
			
			Iterator itr_kinase = kinase_map.keySet().iterator();
			while (itr_kinase.hasNext()) {
				String kinase_name = (String)itr_kinase.next();
				
				FileWriter fwriter = new FileWriter(outputFolder + "/Predicted_" + kinase_name + "_relative_quantification.txt");
				BufferedWriter out = new BufferedWriter(fwriter);				
				out.write("ProteinName\tPosition\tExtendedSeq\tOrigSeq\tPredictedMotif\tPredictedMotifName\tPhosphosite_KINASE\tPhophosite_GeneName\tPhosphosite_Accession\tPearsonCorrelation\tSpearmanCorrelation\tSubstrateUniqID\t" + SubstrateTag + "\tKinaseName\tKinaseUniqID\t" + KinaseTag + "\n");
				
				HashMap motif_map = (HashMap)kinase_map.get(kinase_name);
				Iterator itr_motif = motif_map.keySet().iterator();
				while (itr_motif.hasNext()) {
					String motif_name = (String)itr_motif.next();
					motif_name = motif_name.replaceAll(" ", "_");
					HashMap uniq = new HashMap();
					int count = 0;
					FileInputStream fstream = new FileInputStream(originalFile);
					DataInputStream din = new DataInputStream(fstream);
					BufferedReader in = new BufferedReader(new InputStreamReader(din));
					in.readLine(); // skip header
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
										//System.out.println(ascoreData);
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
											out.write(str + "\t" + pearson + "\t" + spearman + "\t" + peptide + "\t" + ascoreData + "\t" + kinase_name + "_" + peptideStr + "\t" + totalData + "\n");									
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
					
				} // while motif map
				out.close();											
			} // while kinase map
			//System.out.println("Finish Appending");
		} catch (Exception e) {
			e.printStackTrace();
		}
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
					int buffer = 16;
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
	
	/*public static HashMap grabDataFromTotal(String fileName, String groupInfo) {
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
	}*/
}
