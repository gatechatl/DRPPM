package WholeExonTool.Summarize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Generate a matrix containing the 
 * @author tshaw
 *
 */
public class EXCAPSummary {

	public static String description() {
		return "Filters the exoncap file and get rid of ";
	}
	public static String type() {
		return "EXCAP";
	}
	public static String parameter_info() {
		return "[mutation_review_list] [outputFile] [outputFile_removed] [outputSummaryFile] [outputRecurrentGene] [outputPolyphen] [outputSIFT]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0]; // mutation review file list
			String outputFile = args[1];
			String outputFile_removed = args[2];
			String outputSummaryFile = args[3];
			String outputRecurrentGene = args[4];
			String forPolyphen = args[5];
			String forSIFT = args[6];
			
			FileWriter fwriter5 = new FileWriter(forSIFT);
			BufferedWriter out5 = new BufferedWriter(fwriter5);
			
			FileWriter fwriter4 = new FileWriter(forPolyphen);
			BufferedWriter out4 = new BufferedWriter(fwriter4);
			
			FileWriter fwriter3 = new FileWriter(outputRecurrentGene);
			BufferedWriter out3 = new BufferedWriter(fwriter3);
			
			FileWriter fwriter2 = new FileWriter(outputSummaryFile);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_removed = new FileWriter(outputFile_removed);
			BufferedWriter out_removed = new BufferedWriter(fwriter_removed);
			
			String result = "geneName\tSJQuality\tSample\tchr\tposition\tclasstype\tAAChange\tProteinGI\tmRNA_acc\tMutTumor"
					+ "\tTotalTumor\tMutNorm\tTotalNorm\tRef>Mut\tState\tFlanking\tMAF_Tumor\tNHLBI\tPCGP_Germline\tPCGP_somatic\tdbSNP\tCosmic\tCancerGeneConsensus\tSIFT_pred\tPolyphen2_HDIV_pred\tPolyphen2_HVAR_pred\tGSBClass\tReason\tManualReviewFlag\tFileName";
			
			out.write(result + "\n");
			out_removed.write(result + "\n");
			out3.write(result + "\n");
			HashMap sampleName = new HashMap();
			HashMap geneName = new HashMap();
			HashMap item = new HashMap();
			
			// filtered samples
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String snvFile = str.trim();
				String addTag = "";
				if (snvFile.contains("_R1")) {
					addTag = "_R1";
				}
				if (snvFile.contains("_R2")) {
					addTag = "_R2";
				}
				if (snvFile.contains("_R3")) {
					addTag = "_R3";
				}
				if (snvFile.contains("_D1")) {
					addTag = "_D1";
				}
				if (snvFile.contains("_D2")) {
					addTag = "_D2";
				}
				if (snvFile.contains("_D3")) {
					addTag = "_D3";
				}
				LinkedList list = readFile(snvFile, true);
				Iterator itr = list.iterator();
				while (itr.hasNext()) {
					String str2 = (String)itr.next();
					String[] split = str2.split("\t");
					sampleName.put(split[2], split[2]);
					if (geneName.containsKey(split[0])) {
						LinkedList sample = (LinkedList)geneName.get(split[0]);
						if (!sample.contains(split[2])) {
							sample.add(split[2]);
							geneName.put(split[0], sample);
						}
					} else {
						LinkedList sample = new LinkedList();
						if (!sample.contains(split[2])) {
							sample.add(split[2]);
							geneName.put(split[0], sample);
						}						
					}
					if (item.containsKey(split[0])) {
						LinkedList line = (LinkedList)item.get(split[0]);
						if (!line.contains(str2)) {
							line.add(str2);
							item.put(split[0], line);
						}
					} else {
						LinkedList line = new LinkedList();
						if (!line.contains(str2)) {
							line.add(str2);
							item.put(split[0], line);
						}
					}
					String[] split2 = str2.split("\t");
					String final_str2 = "";
					for (int i = 0; i < split2.length; i++) {
						if (i == 0) {
							final_str2 = split2[i];
						} else if (i == 2) {
							final_str2 += "\t" + split2[i] + addTag;
						} else {
							final_str2 += "\t" + split2[i];
						}
					}
					out.write(final_str2 + "\n");
				}
				
			}
			in.close();
			out.close();

			// unfiltered samples
			HashMap sampleName_filter = new HashMap();
			HashMap geneName_filter = new HashMap();
			HashMap item_filter = new HashMap();
			
			FileInputStream fstream_filter = new FileInputStream(inputFile);
			DataInputStream din_filter = new DataInputStream(fstream_filter);
			BufferedReader in_filter = new BufferedReader(new InputStreamReader(din_filter));
			while (in_filter.ready()) {
				String str = in_filter.readLine();
				String snvFile = str.trim();
				String addTag = "";
				if (snvFile.contains("_R1")) {
					addTag = "_R1";
				}
				if (snvFile.contains("_R2")) {
					addTag = "_R2";
				}
				if (snvFile.contains("_R3")) {
					addTag = "_R3";
				}
				if (snvFile.contains("_D1")) {
					addTag = "_D1";
				}
				if (snvFile.contains("_D2")) {
					addTag = "_D2";
				}
				if (snvFile.contains("_D3")) {
					addTag = "_D3";
				}
				LinkedList list_filter = readFile(snvFile, false);
				Iterator itr = list_filter.iterator();
				while (itr.hasNext()) {
					String str2 = (String)itr.next();
					String[] split = str2.split("\t");
					sampleName_filter.put(split[2], split[2]);
					if (geneName_filter.containsKey(split[0])) {
						LinkedList sample_filter = (LinkedList)geneName_filter.get(split[0]);
						if (!sample_filter.contains(split[2])) {
							sample_filter.add(split[2]);
							geneName_filter.put(split[0], sample_filter);
						}
					} else {
						LinkedList sample_filter = new LinkedList();
						if (!sample_filter.contains(split[2])) {
							sample_filter.add(split[2]);
							geneName_filter.put(split[0], sample_filter);
						}						
					}
					if (item_filter.containsKey(split[0])) {
						LinkedList line_filter = (LinkedList)item_filter.get(split[0]);
						if (!line_filter.contains(str2)) {
							line_filter.add(str2);
							item_filter.put(split[0], line_filter);
						}
					} else {
						LinkedList line_filter = new LinkedList();
						if (!line_filter.contains(str2)) {
							line_filter.add(str2);
							item_filter.put(split[0], line_filter);
						}
					}
					String[] split2 = str2.split("\t");
					String final_str2 = "";
					for (int i = 0; i < split2.length; i++) {
						if (i == 0) {
							final_str2 = split2[i];
						} else if (i == 2) {
							final_str2 += "\t" + split2[i] + addTag;
						} else {
							final_str2 += "\t" + split2[i];
						}
					}
					out_removed.write(final_str2 + "\n");
				}
				
			}
			in_filter.close();
			out_removed.close();

			
			
			out2.write("GeneName");
			Iterator itr2 = sampleName.keySet().iterator();
			while (itr2.hasNext()) {
				String sample = (String)itr2.next();
				out2.write("\t" + sample);
			}
			out2.write("\tNumHit\n");
			Iterator itr = geneName.keySet().iterator();
			while (itr.hasNext()) {
				String gene = (String)itr.next();
				out2.write(gene);
				LinkedList sampleList = (LinkedList)geneName.get(gene);
				int hit = 0;
				int hitNonGerm = 0;
				boolean found = false;
				itr2 = sampleName.keySet().iterator();
				while (itr2.hasNext()) {
					String sample = (String)itr2.next();
					if (sampleList.contains(sample)) {
						found = true;
						hit++;
						
					}					
					out2.write("\t" + found);
					found = false;
				}
				out2.write("\t" + hit + "\n");
				
				if (hit >= 2) {
					LinkedList line = (LinkedList)item.get(gene);
					Iterator itr3 = line.iterator();
					while (itr3.hasNext()) {
						String str4 = (String)itr3.next();
						String[] split4 = str4.split("\t");
						if (!split4[14].contains("GERMLINE") && !split4[15].contains("GERMLINE")) {
							out3.write(str4 + "\n");
							
							if (split4[5].equals("missense")) {
								String aachange = split4[6];
								String germAA = aachange.substring(0, 1);
								String tumorAA = aachange.substring(aachange.length() - 1, aachange.length());
								int loc = new Integer(aachange.substring(1, aachange.length() - 1));
								out4.write(split4[8] + " " + loc + " " + germAA + " " + tumorAA + "\n");
								out5.write(split4[7] + "," + aachange + "\n");
							}
						}
					}
				}
			}
			out2.close();
			out3.close();
			out4.close();
			out5.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * If filter flag is true means to grab the filtered sample
	 * If filter flag is false means to grab the unfiltered sample 
	 * @param inputFile
	 * @param filter
	 * @return
	 */
	public static LinkedList readFile(String inputFile, boolean filter) {
		LinkedList list =new LinkedList();
		try {
			
			File f = new File(inputFile);
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] colnames = in.readLine().split("\t");
			int geneNameIndex = findCol(colnames, "GeneName");
			int SJQualityIndex = findCol(colnames, "SJQuality");
			int SampleIndex = findCol(colnames, "Sample");
			int ChrIndex = findCol(colnames, "Chr");
			
			int PositionIndex = findCol(colnames, "Pos");
			
			int ClassIndex = findCol(colnames, "Class");
			int AAChangeIndex = findCol(colnames, "AAChange");
			int ProteinGIIndex = findCol(colnames, "ProteinGI");
			int mRNA_accIndex = findCol(colnames, "mRNA_acc");
			int MutTumorIndex = findCol(colnames, "#Mutant_In_Tumor");
			int TotalTumorIndex = findCol(colnames, "#Total_In_Tumor");
			int MutNormIndex = findCol(colnames, "#Mutant_In_Normal");
			int TotalNormIndex = findCol(colnames, "#Total_In_Normal");
			int StateIndex = findCol(colnames, "State");
			int ReferenceAlleleIndex = findCol(colnames, "ReferenceAllele");
			int MutantAlleleIndex = findCol(colnames, "MutantAllele");
			int NHLBIIndex = findCol(colnames, "NHLBI");
			int PCGP_GermlineIndex = findCol(colnames, "PCGP_Germline");
			int PCGP_somaticIndex = findCol(colnames, "PCGP_somatic");
			int dbSNPIndex = findCol(colnames, "dbSNP");
			int CosmicIndex = findCol(colnames, "Cosmic");
			int CancerGeneConsensusIndex = findCol(colnames, "CancerGeneConsensus");
			int GSBClassIndex = findCol(colnames, "GSBClass");
			int ReasonIndex = findCol(colnames, "Reason");
			int FlankingIndex = findCol(colnames, "Flanking");
			int SIFT_predIndex = findCol(colnames, "SIFT_pred");
			int Polyphen2_HDIV_predIndex = findCol(colnames, "Polyphen2_HDIV_pred");
			int Polyphen2_HVAR_predIndex = findCol(colnames, "Polyphen2_HVAR_pred");
			//
			//String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = "NA";
				if (geneNameIndex >= 0) {
					geneName = split[geneNameIndex];
				}
				String SJQuality = "NA";
				if (SJQualityIndex >= 0) {
					SJQuality = split[SJQualityIndex];
				}
				String sample = "NA";
				if (SampleIndex >= 0) {
					sample = split[SampleIndex];
				}
				String chr = "NA";
				if (ChrIndex >= 0) {
					chr = split[ChrIndex];
				}
				String position = "NA";
				if (PositionIndex >= 0) {
					position = split[PositionIndex];
				}
				String classtype = "NA";
				if (ClassIndex >= 0) {
					classtype = split[ClassIndex];
				}
				String AAChange = "NA";
				if (AAChangeIndex >= 0) {
					AAChange = split[AAChangeIndex];
				}
				
				String ProteinGI = "NA";
				if (ProteinGIIndex >= 0) {
					ProteinGI = split[ProteinGIIndex];
				}
				String mRNA_acc = "NA";
				if (mRNA_accIndex >= 0) {
					mRNA_acc = split[mRNA_accIndex];
				}
				String MutTumor = "NA";
				if (MutTumorIndex >= 0) {
					MutTumor = split[MutTumorIndex];
				}
				String TotalTumor = "NA";
				if (TotalTumorIndex >= 0) {
					TotalTumor = split[TotalTumorIndex];
				}
				String MutNorm = "NA";
				if (MutNormIndex >= 0) {
					MutNorm = split[MutNormIndex];
				}
				String TotalNorm = "NA";
				if (TotalNormIndex >= 0) {
					TotalNorm = split[TotalNormIndex];
				}
				String State = "NA";
				if (StateIndex >= 0) {
					State = split[StateIndex];
				}
				
				String ReferenceAllele = "NA";
				if (ReferenceAlleleIndex >= 0) {
					ReferenceAllele = split[ReferenceAlleleIndex];
				}
				
				String MutantAllele = "NA";
				if (MutantAlleleIndex >= 0) {
					MutantAllele = split[MutantAlleleIndex];					
				}
				
				String NHLBI = "NA";
				if (NHLBIIndex >= 0) {
					NHLBI = split[NHLBIIndex];
				}
				
				String PCGP_Germline = "NA";
				if (PCGP_GermlineIndex >= 0) {
					PCGP_Germline = split[PCGP_GermlineIndex];					
				}
				
				String PCGP_somatic = "NA";
				if (PCGP_somaticIndex >= 0) {
					PCGP_somatic = split[PCGP_somaticIndex];
				}
				
				String dbSNP = "NA";
				if (dbSNPIndex >= 0) {
					dbSNP = split[dbSNPIndex];
				}
				
				String Cosmic = "NA";
				if (CosmicIndex >= 0) {
					Cosmic = split[CosmicIndex];
				}
				
				String CancerGeneConsensus = "NA";
				if (CancerGeneConsensusIndex >= 0) {
					CancerGeneConsensus = split[CancerGeneConsensusIndex];
				}
				
				String GSBClass = "NA";
				if (GSBClassIndex >= 0) {
					GSBClass = split[GSBClassIndex];
				}
				String Reason = "NA";
				if (ReasonIndex >= 0) {
					Reason = split[ReasonIndex];
				}
				String Flanking = "NA";
				if (FlankingIndex >= 0) {
					Flanking = split[FlankingIndex];
				}
				String SIFT_pred = "NA";
				if (SIFT_predIndex >= 0) {
					SIFT_pred = split[SIFT_predIndex];
					if (SIFT_pred.equals("D")) {
						SIFT_pred = "Damaging";
					} else if (SIFT_pred.equals("T")) {
						SIFT_pred = "Tolerated";
					}
				}
				String Polyphen2_HDIV_pred = "NA";
				if (Polyphen2_HDIV_predIndex >= 0) {
					Polyphen2_HDIV_pred = split[Polyphen2_HDIV_predIndex];
					if (Polyphen2_HDIV_pred.equals("D")) {
						Polyphen2_HDIV_pred = "Damaging";
					} else if (Polyphen2_HDIV_pred.equals("P")) {
						Polyphen2_HDIV_pred = "Possibly Damaging";
					} else if (Polyphen2_HDIV_pred.equals("B")) {
						Polyphen2_HDIV_pred = "Benign";
					}
				}
				String Polyphen2_HVAR_pred = "NA";
				if (Polyphen2_HVAR_predIndex >= 0) {
					Polyphen2_HVAR_pred = split[Polyphen2_HVAR_predIndex];
					if (Polyphen2_HVAR_pred.equals("D")) {
						Polyphen2_HVAR_pred = "Damaging";
					} else if (Polyphen2_HVAR_pred.equals("P")) {
						Polyphen2_HVAR_pred = "Possibly Damaging";
					} else if (Polyphen2_HVAR_pred.equals("B")) {
						Polyphen2_HVAR_pred = "Benign";
					}
				}
				String manualreviewFlag = "Fuzzy";
				double maf = new Double(MutTumor) / new Double(TotalTumor);
				
				if (maf > 0.1 && new Double(MutTumor) >= 5 && SJQuality.contains("SJH") && !State.contains("Bad")) {
					manualreviewFlag = "Good";
				} 
				//if (!SJQuality.contains("SJL") && !classtype.equals("silent") && !State.contains("Bad")) {
				if (!State.contains("Bad") && maf > 0.1 && new Double(MutTumor) >= 3 && filter) {
					String result = geneName + "\t" + SJQuality + "\t" + sample + "\t" + chr + "\t" + position + "\t"
							+ classtype + "\t" + AAChange + "\t" + ProteinGI + "\t" + mRNA_acc + "\t" + MutTumor + "\t" + TotalTumor + "\t"   
							+ MutNorm + "\t" + TotalNorm + "\t" + ReferenceAllele + ">" + MutantAllele + "\t" + State + "\t" + Flanking + "\t" + maf + "\t"
							+ NHLBI + "\t" + PCGP_Germline + "\t" + PCGP_somatic + "\t" + dbSNP + "\t" + Cosmic + "\t" + CancerGeneConsensus + "\t"
							+ SIFT_pred + "\t" + Polyphen2_HDIV_pred + "\t" + Polyphen2_HVAR_pred + "\t" + GSBClass + "\t" + Reason + "\t" + manualreviewFlag + "\t" + f.getName();
					list.add(result);
				} else if (!(!State.contains("Bad") && maf > 0.1 && new Double(MutTumor) >= 3) && !filter) {
					manualreviewFlag = "Bad";
					String result = geneName + "\t" + SJQuality + "\t" + sample + "\t" + chr + "\t" + position + "\t"
							+ classtype + "\t" + AAChange + "\t" + ProteinGI + "\t" + mRNA_acc + "\t" + MutTumor + "\t" + TotalTumor + "\t" 
							+ MutNorm + "\t" + TotalNorm + "\t" + ReferenceAllele + ">" + MutantAllele + "\t" + State + "\t" + Flanking + "\t" + maf + "\t" 
							+ NHLBI + "\t" + PCGP_Germline + "\t" + PCGP_somatic + "\t" + dbSNP + "\t" + Cosmic + "\t" + CancerGeneConsensus + "\t"
							+ SIFT_pred + "\t" + Polyphen2_HDIV_pred + "\t" + Polyphen2_HVAR_pred + "\t" + GSBClass + "\t" + Reason + "\t" + manualreviewFlag + "\t" + f.getName();
					list.add(result);					
				}
			
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static int findCol(String[] colnames, String tag) {
		for (int i = 0; i < colnames.length; i++) {
			if (colnames[i].toUpperCase().contains(tag.toUpperCase())) {
				return i;
			}
		}
		return -1;
	}
	public static String[] readHeader(String inputFile) {
		
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			String[] colnames = in.readLine().split("\t");
			in.close();
			return colnames;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

