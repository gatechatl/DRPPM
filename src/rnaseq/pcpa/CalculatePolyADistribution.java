package rnaseq.pcpa;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class CalculatePolyADistribution {

	public static void execute(String[] args) {
		
		try {
			String fileName1 = args[0];
			String fileName2 = args[1];
			String outputFile = args[2];
			String fileName3 = args[3];
			
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
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");		
				if (!split[0].equals("plaID")) {
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
			}
			in.close();
			
			HashMap annotation = new HashMap();
			
			// load the annotation information
			fstream = new FileInputStream(fileName2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0];
				String chr = split[1];
				String direction = split[2];
				int txstart = new Integer(split[3]);
				int txend = new Integer(split[4]);
				String codestart = split[5];
				String codeend = split[6];				
				HashMap copyannotation = (HashMap)annotation.clone();
				if (geneList.containsKey(split[0])) {
					boolean foundShorter = true;
					Iterator itr = copyannotation.keySet().iterator();
					while (itr.hasNext()) {
						String name = (String)itr.next();
						String str2 = (String)annotation.get(name);
						String[] split2 = str2.split("\t");
						String geneName2 = split2[0];
						String chr2 = split2[1];
						String direction2 = split2[2];
						int txstart2 = new Integer(split2[3]);
						int txend2 = new Integer(split2[4]);
						
						String codestart2 = split2[5];
						String codeend2 = split2[6];
						if (!geneName.equals(geneName2) && chr.equals(chr2) && direction.equals(direction2)) {
							if (overlap(txstart, txend, txstart2, txend2)) {
								if (direction.equals("+")) {
									if (txend2 > txend) {
										annotation.remove(geneName2);
										//foundShorter = true;
										//annotation.put(split[0], str);
									} else {
										foundShorter = false;
										//if new one is longer then don't do anything
									}
								} else {
									if (txstart2 < txstart) {
										annotation.remove(geneName2);
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
					}					
					if (foundShorter) {
						annotation.put(split[0], str);
					}
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
						if (geneID.contains("uc003sot") && 150 <= score && score <= 200) {
							score = 200;
						}
						if (geneID.contains("uc001cob") && 150 <= score && score <= 200) {
							score = 200;
						}
						if (score > 200) {
							score = 200;
						}
						if (score >= 0 && score <= 200) {
							for (int i = 0; i < reads; i++) {
								//System.out.println(loc + "\t" + geneID);
								if (total_reads.containsKey(geneID + "\t" + loc)) {
									//out.write(geneID + "\t" + score + "\t" + total_reads.get(geneID + "\t" + loc) + "\t" + geneList.get(geneID) + "\t" + "\n");
									out.write(geneID + "\t" + score + "\t" + total_reads.get(geneID + "\t" + loc) + "\t" + totalCount + "\t" + direction + "\t" + chr + "\t" + loc + "\n");									
								}
							}
						}
						if (direction.equals("-") && score >= 150 && score <= 160) {
							//System.out.println(str);
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
			if (total_3utr < 10) {
				return -1;
			}
			if (total_code <= 0 || total_3utr <= 0) {
				return -1;
			}
			if (txend == codeend) {
				return 200;
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
			if (total_3utr < 10) {
				return -1;
			}
			if (total_code <= 0 || total_3utr <= 0) {
				return -1;
			}
			if (txstart == codestart) {
				return 200;
			}
			if (codestart <= query && query <= codeend) {
				result = 100 * (codeend - query) / total_code;
			}
			if (query < codestart) {
				result = 100 + 100 * (new Double(codestart - query) / total_3utr);
				
			}
		}
		/*if (direction.equals("+")) {
			return -1;
		}*/
		return result;
	}
}

