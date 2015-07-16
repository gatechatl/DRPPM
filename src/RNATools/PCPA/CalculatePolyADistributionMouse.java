package RNATools.PCPA;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import IDConversion.GTFFile;

public class CalculatePolyADistributionMouse {

	public static void execute(String[] args) {
		
		try {
			String fileName1 = args[0];
			String fileName2 = args[1];
			String outputFile = args[2];
			String fileName3 = args[3];
			String GTFfile = args[4];
			String ucsc2geneName = args[5];
			
			int limit = 1;
			// formally bin them
			HashMap geneList = new HashMap();
			HashMap polyA_evidence = new HashMap();
			
			HashMap geneList_3UTR = new HashMap();
			HashMap geneList_CDS = new HashMap();
			
			int totalCount = 0;
			// grab all the evidence with polyA reads
			FileInputStream fstream = new FileInputStream(fileName1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");				
				String geneID = split[4];
				String motif = split[7];
				double read = new Double(split[6]);
				String type = split[8];
				//if ((type.equals("3UTR") || type.equals("CDS")) && !motif.equals("none")) {
				//if (!motif.equals("none") && read > limit) {
				//if ((type.equals("3UTR") || type.equals("CDS")) && !motif.equals("none") && read > limit) {
				if (read > limit) {
					int count = new Integer(split[6]);
					//totalCount = totalCount + count;
					if (geneList.containsKey(geneID)) {
						int num = (Integer)geneList.get(geneID);
						geneList.put(geneID, num + count);
						
					} else {
						geneList.put(geneID, count);
					}
					polyA_evidence.put(split[0], str);
					if (type.equals("3UTR")) {
						geneList_3UTR.put(geneID, geneID);						
					} else if (type.equals("CDS")) {
						geneList_CDS.put(geneID, geneID);
					}
				}
			}
			in.close();
			
			HashMap ucsc2GeneName = new HashMap();
			fstream = new FileInputStream(ucsc2geneName);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				ucsc2GeneName.put(split[0], split[4]);
			}
			in.close();
			
			HashMap gtfMap = new HashMap();
			HashMap geneName2Ensembl = new HashMap();
			// overlap annotation with gtf file
			FileInputStream fstreamGTF = new FileInputStream(GTFfile);
			DataInputStream dinGTF = new DataInputStream(fstreamGTF);
			BufferedReader inGTF = new BufferedReader(new InputStreamReader(dinGTF));
			inGTF.readLine();
			while (inGTF.ready()) {
				String str = inGTF.readLine();
				String[] split = str.split("\t");
				String ensembl_id = GTFFile.grabMeta(split[8], "gene_id");
				String geneName = GTFFile.grabMeta(split[8], "gene_name");
				if (split[1].equals("protein_coding")) {
					if (geneName2Ensembl.containsKey(geneName)) {
						LinkedList list = (LinkedList)geneName2Ensembl.get(geneName);
						list.add(ensembl_id);
						geneName2Ensembl.put(geneName, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(ensembl_id);
						geneName2Ensembl.put(geneName, list);
					}
					if (gtfMap.containsKey(ensembl_id)) {
						GTFObject gtf = (GTFObject)gtfMap.get(ensembl_id);
						int start = new Integer(split[3]);
						int end = new Integer(split[4]);
						if (split[6].equals("+")) {
							if (gtf.end < end) {
								gtf.end = end;
							}
							if (gtf.start > start) {
								gtf.start = start;
							}
							gtfMap.put(ensembl_id, gtf);
						} else {
							if (gtf.end > end) {
								gtf.end = end;
							}
							if (gtf.start < start) {
								gtf.start = start;
							}
							gtfMap.put(ensembl_id, gtf);
						}
					} else {
						GTFObject gtf = new GTFObject();
						gtf.chr = split[0];
						gtf.direction = split[6];
						gtf.start = new Integer(split[3]);
						gtf.end = new Integer(split[4]);
						gtf.type = split[1];
						gtf.geneName = geneName;
						gtfMap.put(ensembl_id, gtf);
					}
				}
			}
			inGTF.close();
			
			HashMap annotation = new HashMap();
			// load the annotation information
			fstream = new FileInputStream(fileName2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String ucscName = split[0];
				String chr = split[1];
				String direction = split[2];
				int txstart = new Integer(split[3]);
				int txend = new Integer(split[4]);
				int codestart = new Integer(split[5]);
				int codeend = new Integer(split[6]);			
				
				HashMap copyannotation = (HashMap)annotation.clone();
				if (geneList.containsKey(split[0])) {
					boolean foundShorter = true;
					annotation.put(split[0], str);
					
					if (ucsc2GeneName.containsKey(split[0])) {
						String geneName = (String)ucsc2GeneName.get(split[0]);
						if (geneName2Ensembl.containsKey(geneName)) {
							LinkedList ensembls = (LinkedList)geneName2Ensembl.get(geneName);
							Iterator itr = ensembls.iterator();
							while (itr.hasNext()) {
								String ensembl = (String)itr.next();
								GTFObject gtf = (GTFObject)gtfMap.get(ensembl);
								
								String chr2 = gtf.chr;
								String direction2 = gtf.direction;
								int txstart2 = gtf.start;
								int txend2 = gtf.end;
								
								if (direction.equals("+")) {
									if (txend2 < txend) {
										//annotation.remove(ucscName2);
										//foundShorter = true;
										
										if (codeend > txend2) {
											codeend = txend2;
										}
										
										String[] split4 = str.split("\t");
										String newStr = split4[0];
										for (int i = 1; i < 3; i++) {
											newStr += "\t" + split4[i];
										}
										newStr += "\t" + txstart2;
										newStr += "\t" + txend2;
										newStr += "\t" + codestart;
										newStr += "\t" + codeend;
										for (int i = 7; i < split4.length; i++) {
											newStr += "\t" + split4[i];
										}
										//System.out.println(newStr);
										annotation.put(split[0], newStr);
									} else {
										foundShorter = false;
										//if new one is longer then don't do anything
									}
								} else {
									if (txstart2 > txstart) {
										//annotation.remove(ucscName2);
										//foundShorter = true;
										String[] split4 = str.split("\t");
										String newStr = split4[0];
										for (int i = 1; i < 3; i++) {
											newStr += "\t" + split4[i];
										}
										
										if (codestart < txstart2) {
											codestart = txstart2;
										}
										newStr += "\t" + txstart2;
										newStr += "\t" + txend2;
										newStr += "\t" + codestart;
										newStr += "\t" + codeend;
										for (int i = 7; i < split4.length; i++) {
											newStr += "\t" + split4[i];
										}
										//System.out.println(newStr);
										annotation.put(split[0], newStr);										
									} else {
										foundShorter = false;
										//if new one is longer then don't do anything
									}
								}
							}
						}						
					}
					/*Iterator itr = copyannotation.keySet().iterator();
					while (itr.hasNext()) {
						String name = (String)itr.next();
						String str2 = (String)annotation.get(name);
						String[] split2 = str2.split("\t");
						String ucscName2 = split2[0];
						String chr2 = split2[1];
						String direction2 = split2[2];
						int txstart2 = new Integer(split2[3]);
						int txend2 = new Integer(split2[4]);
						
						String codestart2 = split2[5];
						String codeend2 = split2[6];
						if (!ucscName.equals(ucscName2) && chr.equals(chr2) && direction.equals(direction2)) {
							if (overlap(txstart, txend, txstart2, txend2)) {
								if (direction.equals("+")) {
									if (txend2 > txend) {
										annotation.remove(ucscName2);
										//foundShorter = true;
										//annotation.put(split[0], str);
									} else {
										foundShorter = false;
										//if new one is longer then don't do anything
									}
								} else {
									if (txstart2 < txstart) {
										annotation.remove(ucscName2);
										//foundShorter = true;
										//annotation.put(split[0], str);										
									} else {
										foundShorter = false;
										//if new one is longer then don't do anything
									}
								}
							} else {
								annotation.put(split[0], str);
							}
						}
					}*/					
					/*if (foundShorter) {
						annotation.put(split[0], str);
					}*/
				}
				
			}
			in.close();
			
			
			
			System.out.println("Finish loading annotatoin: " + annotation.size());
			// count total used reads
			Iterator itr = polyA_evidence.keySet().iterator();
			while (itr.hasNext()) {
				String ID = (String)itr.next();
				String str = (String)polyA_evidence.get(ID);
				String[] split = str.split("\t");
				String geneID = split[4];
				String chr = split[1];				
				String dir = split[2];
				int reads = new Integer(split[6]);
				int loc = new Integer(split[3]);
				//if (annotation.containsKey(geneID) && geneList_3UTR.containsKey(geneID) && geneList_CDS.containsKey(geneID)) {
				if (annotation.containsKey(geneID)) {
					if (reads > limit) {
						totalCount += reads;
					}
				}
			}
			
				
			HashMap total_reads = new HashMap();
			// get total number of reads
			
			// total number of reads per gene
			fstream = new FileInputStream(fileName3);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String location = (new Integer(split[1]) + 100) + "";
				if (annotation.containsKey(split[3])) {
					if (split.length > 12) {
						int read = new Integer(split[12]);
						if (total_reads.containsKey(split[3] + "\t" + location)) {
							int total = (Integer)total_reads.get(split[3] + "\t" + location);
							total += read;
							total_reads.put(split[3] + "\t" + location, total);
						} else {
							total_reads.put(split[3] + "\t" + location, read);
						}
					}
				}
			}
			in.close();
			
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);			
			System.out.println("polyA_evidence: " + polyA_evidence.size());
			itr = polyA_evidence.keySet().iterator();
			while (itr.hasNext()) {
				String ID = (String)itr.next();
				String str = (String)polyA_evidence.get(ID);
				String[] split = str.split("\t");
				String geneID = split[4];
				String chr = split[1];				
				String dir = split[2];
				int reads = new Integer(split[6]);
				int loc = new Integer(split[3]);
				String type = split[8];
				//if (annotation.containsKey(geneID) && geneList_3UTR.containsKey(geneID) && geneList_CDS.containsKey(geneID)) {
				if (annotation.containsKey(geneID)) {
					String str2 = (String)annotation.get(geneID);
					String[] split2 = str2.split("\t");
					
					String chr2 = split2[1];
					String direction = split2[2];
					int txstart = new Integer(split2[3]);
					int txend = new Integer(split2[4]);
					int codestart = new Integer(split2[5]);
					int codeend = new Integer(split2[6]);
					boolean good = true;
					/*if ((type.equals("CDS") && direction.equals("+") && loc > codeend)) {
						good = false;
					}
					if ((type.equals("CDS") && direction.equals("-") && loc < codeend)) {
						good = false;
					}
					if ((type.equals("3UTR") && direction.equals("+") && loc > codeend)) {
						good = false;
					}
					if ((type.equals("3UTR") && direction.equals("-") && loc < codeend)) {
						good = false;
					}*/
					if (good) {
						double score = calc_proportion(direction, txstart, txend, codestart, codeend, loc);
						if (score > 200) {
							score = 200;
						}
						
						//if (type.equals("3UTR") && (type.equals("Tian_polyA_DB") || reads > 50)) {
						if (type.equals("3UTR") && (type.equals("Tian_polyA_DB"))) {
							score = 200;
						}
						if (score >= 0 && score <= 200) {
							for (int i = 0; i < reads; i++) {
								//System.out.println(loc + "\t" + geneID);
								if (total_reads.containsKey(geneID + "\t" + loc)) {
									//out.write(geneID + "\t" + score + "\t" + total_reads.get(geneID + "\t" + loc) + "\t" + geneList.get(geneID) + "\t" + "\n");
									out.write(geneID + "\t" + score + "\t" + total_reads.get(geneID + "\t" + loc) + "\t" + totalCount + "\t" + direction + "\t" + txstart + "\t" + txend + "\t" + codestart + "\t" + codeend + "\t" + loc + "\n");
									
								}
							}
						}
					}
				}
				
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean overlap(int a, int b, int c, int d) {
		if (a <= c && c <= b) {
			return true;
		}
		if (a <= d && d <= b) {
			return true;
		}
		if (c <= a && a <= d) {
			return true;
		}
		if (c <= b && b <= d) {
			return true;
		}
		return false;
	}
	public static class GTFObject {
		public String chr = "chrN"; 
		public int start = -1;
		public int end = -1;
		public String type = "";
		public String direction = "X";
		public String geneName = "";
	}
	/**
	 * 0 - 100 is coding region
	 * 101-200 is 3'UTR
	 * @param direction
	 * @param txstart
	 * @param txend
	 * @param codestart
	 * @param codeend
	 * @param query
	 * @return
	 */
	public static double calc_proportion(String direction, int txstart, int txend, int codestart, int codeend, int query) {
		double result = -1;
		if (direction.equals("+")) {
			double total_code = codeend - codestart;
			double total_3utr = txend - codeend;
			if (txend == codeend) {
				return 200;
			}
			if (total_code <= 0 || total_3utr <= 0) {
				return -1;
			}
			if (codestart <= query && query <= codeend) {
				result = 100 * (query - codestart) / total_code;
			}
			if (codeend < query) {
				result = 100 + 100 * (new Double(query - codeend) / total_3utr);
			}
		} else if (direction.equals("-")) {
			double total_code = codeend - codestart;
			double total_3utr = codestart - txstart;
			if (total_code <= 0 || total_3utr <= 0) {
				return -1;
			}
			if (codestart <= query && query <= codeend) {
				result = 100 * (codeend - query) / total_code;
			}
			if (query < codestart) {
				result = 100 + 100 * (new Double(codestart - query) / total_3utr);
			}
		}
		return result;
	}
}

