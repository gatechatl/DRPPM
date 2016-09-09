package Integration.summarytable;

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

public class ComprehensiveSummaryTableSampleTypeSNVFusionFilter {

	public static String type() {
		return "INTEGRATION";
	}
	public static String description() {
		return "Generate a comprehensive table of SampleName Subtype SNV Fusion and other features";
	}
	
	public static String parameter_info() {
		return "[sampleInfoFile] [ciceroFile] [pairedSNPDetectFile] [genotypeFile] [fpkm_file] [cicerofilter] [excapfilter] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String sampleInfoFile = args[0]; // file sent from cristel
			String ciceroFile = args[1]; // file derived from cicero
			String pairedSNPDetectFile = args[2]; // file from SNPdetect post processing
			String genotypeFile = args[3];
			String fpkm_file = args[4];
			String cicerofilterFile = args[5];
			String excapfilterFile = args[6];
			String outputFile = args[7];
			String outputFile2 = args[8];
			File f = new File(outputFile);
			if (f.exists()) {
				System.out.println("File already exist. Please delete before running");
				System.exit(0);;
			}
			f = new File(outputFile2);
			if (f.exists()) {
				System.out.println("File already exist. Please delete before running");
				System.exit(0);;
			}
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			HashMap sample2RNASEQID = grabSampleName2RNASEQID(sampleInfoFile);
			HashMap sampleName = grabSampleName(sampleInfoFile);
			HashMap sample2EXCAPID = grabSampleName2EXCAPID(sampleInfoFile);
			HashMap RNASEQID2subtype = grabRNASEQID2Subtype(genotypeFile);
			LinkedList ciceroList = grabCiceroResult(ciceroFile);
			HashMap fpkm = grabFPKMResult(fpkm_file);
			HashMap snpDetect = grabSnpDetectResult(pairedSNPDetectFile);
			HashMap excapFilter = loadGeneList(excapfilterFile);
			HashMap ciceroFilter = loadGeneList(cicerofilterFile);
			
			// extract cicero result
			HashMap cicero_geneName = new HashMap();
			HashMap cicero_geneName_sampleName = new HashMap();
			Iterator itr = ciceroList.iterator();
			while (itr.hasNext()) {
				String line = (String)itr.next();
				String[] split = line.split("\t");
				String cicero_sampleName = split[0].split("_")[0];
				String cicero_geneName_L = split[1];
				String cicero_geneName_R = split[6];
				if (ciceroFilter.containsKey(cicero_geneName_L)) {
					cicero_geneName.put(cicero_geneName_L, cicero_geneName_L);
				}
				if (ciceroFilter.containsKey(cicero_geneName_R)) {
					cicero_geneName.put(cicero_geneName_R, cicero_geneName_R);
				}
				if (cicero_geneName_sampleName.containsKey(cicero_geneName_L)) {
					LinkedList list = (LinkedList)cicero_geneName_sampleName.get(cicero_geneName_L);
					if (!list.contains(cicero_sampleName)) {
						list.add(cicero_sampleName);
						cicero_geneName_sampleName.put(cicero_geneName_L, list);
					}
				} else {
					LinkedList list = new LinkedList();
					list.add(cicero_sampleName);
					cicero_geneName_sampleName.put(cicero_geneName_L, list);
				}
				if (cicero_geneName_sampleName.containsKey(cicero_geneName_R)) {
					LinkedList list = (LinkedList)cicero_geneName_sampleName.get(cicero_geneName_R);
					if (!list.contains(cicero_sampleName)) {
						list.add(cicero_sampleName);
						cicero_geneName_sampleName.put(cicero_geneName_R, list);
					}
				} else {
					LinkedList list = new LinkedList();
					list.add(cicero_sampleName);
					cicero_geneName_sampleName.put(cicero_geneName_R, list);
				}
			}
			
			// input sampleID
			out.write("SampleID");
			itr = sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String sampleID = (String)itr.next();
				out.write("\t" + sampleID);
			}
			out.write("\n");
			
			// input RNASEQID information			
			out.write("RNASEQID");
			itr = sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String sampleID = (String)itr.next();
				if (sample2RNASEQID.containsKey(sampleID)) {
					String RNASEQID = (String)sample2RNASEQID.get(sampleID);
					out.write("\t" + RNASEQID);
				} else {
					out.write("\t" + "NA");
				}
			}
			out.write("\n");
			
			// input EXCAPID information			
			out.write("EXCAPID");
			itr = sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String sampleID = (String)itr.next();
				if (sample2EXCAPID.containsKey(sampleID)) {
					String EXCAPID = (String)sample2EXCAPID.get(sampleID);
					out.write("\t" + EXCAPID);
				} else {
					out.write("\t" + "NA");
				}
			}
			out.write("\n");
			
			
			// input subtype information
			out.write("Subtype");
			itr = sampleName.keySet().iterator();
			while (itr.hasNext()) {
				String sampleID = (String)itr.next();
				if (sample2RNASEQID.containsKey(sampleID)) {
					String RNASEQID = (String)sample2RNASEQID.get(sampleID);
					String subtype = (String)RNASEQID2subtype.get(RNASEQID);
					out.write("\t" + subtype);
				} else {
					out.write("\t" + "NA");
				}
			}
			out.write("\n");
			
			HashMap fusion = new HashMap();
			HashMap expression = new HashMap();
			// input cicero result
			itr = cicero_geneName.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				
					out.write("Fusion:" + geneName);
					
					LinkedList list = (LinkedList)cicero_geneName_sampleName.get(geneName);
					Iterator itr2 = sampleName.keySet().iterator();
					while (itr2.hasNext()) {
						String sampleID = (String)itr2.next();
						if (sample2RNASEQID.containsKey(sampleID)) {
							String RNASEQID = (String)sample2RNASEQID.get(sampleID);
							if (list.contains(RNASEQID)) {
								fusion.put(geneName, geneName);
								out.write("\t1");
							} else {
								out.write("\t0");
							}
						} else {
							out.write("\t0");
						}
					}
					out.write("\n");
					
					/*
					out.write("FPKM: " + geneName);
					itr2 = sampleName.keySet().iterator();
					while (itr2.hasNext()) {
						String sampleID = (String)itr2.next();
						if (sample2RNASEQID.containsKey(sampleID)) {
							String RNASEQID = (String)sample2RNASEQID.get(sampleID);
							if (fpkm.containsKey(RNASEQID)) {
								HashMap gene = (HashMap)fpkm.get(RNASEQID);
								if (gene.containsKey(geneName)) {
									String value = (String)gene.get(geneName);
									double val = new Double(value);		
									val = new Double((new Double(val * 100)).intValue()) / 100;
									if (val > 80) {
										expression.put(geneName, geneName);
									}
									out.write("\t" + val);
								} else {
									out.write("\tNA");
								}
							} else {
								out.write("\tNA");
							}
						} else {
							out.write("\tNA");
						}
					}
					out.write("\n");
					*/
			}
			
			
			
			itr = fusion.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				if (expression.containsKey(geneName)) {
					out2.write(geneName + "\n");
				}
			}
			out2.close();
			
			HashMap exonCapGeneName = new HashMap();
			Iterator itr2 = snpDetect.keySet().iterator();
			while (itr2.hasNext()) {
				String name = (String)itr2.next();
				HashMap gene = (HashMap)snpDetect.get(name);
				Iterator itr3 = gene.keySet().iterator();
				while (itr3.hasNext()) {
					String geneName = (String)itr3.next();
					if (excapFilter.containsKey(geneName)) {
						exonCapGeneName.put(geneName, geneName);
					}
				}
			}
		
			itr = exonCapGeneName.keySet().iterator();
			while (itr.hasNext()) {
				String geneName = (String)itr.next();
				out.write("ExCap:" + geneName);				
				itr2 = sampleName.keySet().iterator();
				while (itr2.hasNext()) {
					String sampleID = (String)itr2.next();
					if (sample2EXCAPID.containsKey(sampleID)) {
						String EXCAPID = (String)sample2EXCAPID.get(sampleID);
						if (snpDetect.containsKey(EXCAPID)) {
							HashMap gene = (HashMap)snpDetect.get(EXCAPID);
							if (gene.containsKey(geneName)) {
								String type = (String)gene.get(geneName);
								out.write("\t" + type);
							} else {
								out.write("\tNA");
							}
						} else {
							out.write("\tNA");
						}
					} else {
						out.write("\tNA");
					}
				}
				out.write("\n");
			}
			
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap grabFPKMResult(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			String[] split_header = header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					String sampleName = split_header[i].split("_")[0];
					if (map.containsKey(sampleName)) {
						HashMap gene = (HashMap)map.get(sampleName);
						gene.put(split[0], split[i]);
						map.put(sampleName, gene);
					} else {
						HashMap gene = new HashMap();
						gene.put(split[0], split[i]);
						map.put(sampleName, gene);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap grabRNASEQID2Subtype(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				map.put(split[0].split("_")[0], split[2]);				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabSampleName2RNASEQID(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 1) {
					if (split[1].length() > 0) {
						map.put(split[0].replaceAll(" ", "_"), split[1].split("_")[0]);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static HashMap grabSampleName2EXCAPID(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				if (split.length > 3) {
					if (split[3].length() > 0) {
						map.put(split[0].replaceAll(" ", "_"), split[3].split("_")[0]);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static HashMap grabSampleName(String inputFile) {
		HashMap map = new HashMap();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				map.put(split[0].replaceAll(" ", "_"), split[0].replaceAll(" ", "_"));
				
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static String grabCiceroHeader(String inputFile) {
		String header = "";
		try {

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();			
			in.close();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}
	public static HashMap grabSnpDetectResult(String inputFile) {
		HashMap map = new HashMap();
		try {

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				String quality = split[1];
				String sampleName = split[2];
				String mutationType = split[5];
				double tumorMut = new Double(split[9]);
				double tumorGerm = new Double(split[10]);
				if (tumorMut / tumorGerm >= 0.1 && tumorMut >= 5 && quality.equals("SJHQ")) {
					if (map.containsKey(sampleName)) {
						HashMap gene = (HashMap)map.get(sampleName);
						if (gene.containsKey(geneName)) {
							String type = (String)gene.get(geneName);
							type += "," + mutationType;
							gene.put(geneName, type);
						} else {
							gene.put(geneName, mutationType);							
						}	
						map.put(sampleName, gene);
					} else {
						HashMap gene = new HashMap();
						gene.put(geneName, mutationType);
						map.put(sampleName, gene);
					}
				}
				
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	public static LinkedList grabCiceroResult(String inputFile) {
		LinkedList list = new LinkedList();
		try {

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				// coding and coding
				if (split[29].equals("HQ") && (split[5].equals("coding")) && (split[10].equals("coding")  || split[10].equals("5utr"))) {
					list.add(str);
				}
			}
			in.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static HashMap loadGeneList(String inputFile) {
		HashMap map = new HashMap();
		try {

			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine();
				map.put(str, str);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
