package rnaseq.splicing.mats308;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Somewhat a specialized comparison for Junmin Peng's AD Mouse Model
 * @author tshaw
 *
 */
public class GenerateOverlappingResults {

	public static void execute(String[] args) {
		
		try {
			 
			String path1 = args[0];
			String path2 = args[1];
			String path3 = args[2];
			String tag1 = args[3];
			String tag2 = args[4];
			String tag3 = args[5];
			double pvalue = new Double(args[6]);
			String outputFolder = args[7];
			
			String[] mats_files = {"A3SS.MATS.JunctionCountOnly.txt", "A3SS.MATS.ReadsOnTargetAndJunctionCounts.txt", "A5SS.MATS.JunctionCountOnly.txt", "A5SS.MATS.ReadsOnTargetAndJunctionCounts.txt", "MXE.MATS.JunctionCountOnly.txt", "MXE.MATS.ReadsOnTargetAndJunctionCounts.txt", "RI.MATS.JunctionCountOnly.txt", "RI.MATS.ReadsOnTargetAndJunctionCounts.txt", "SE.MATS.JunctionCountOnly.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt"};
			for (String file: mats_files) {
				HashMap map1 = differentialExpression(path1 + "/" + file + ".addGeneName.txt", pvalue);
				HashMap map2 = differentialExpression(path2 + "/" + file + ".addGeneName.txt", pvalue);
				HashMap map3 = differentialExpression(path3 + "/" + file + ".addGeneName.txt", pvalue);
				String file1Header = getHeader(path1 + "/" + file + ".addGeneName.txt"); // file1 through 3 have same header
				
				HashMap map1Uniq = getUniq(map1, map2, map3);
				HashMap map2Uniq = getUniq(map2, map3, map3);
				HashMap map3Uniq = getUniq(map3, map2, map1);
				HashMap map1map2Overlap = getOverlap(map1, map2);
				HashMap map1map3Overlap = getOverlap(map1, map3);
				HashMap map2map3Overlap = getOverlap(map2, map3);
				
				HashMap overlapAll = getOverlap(map1map2Overlap, map3);
				HashMap map1map2OverlapUniq = getUniq(map1map2Overlap, overlapAll);
				HashMap map1map3OverlapUniq = getUniq(map1map3Overlap, overlapAll);
				HashMap map2map3OverlapUniq = getUniq(map2map3Overlap, overlapAll);
				
				System.out.println();
				System.out.println(file);
				System.out.println(tag1 + "_uniq: " + map1Uniq.size());
				System.out.println(tag2 + "_uniq: " + map2Uniq.size());
				System.out.println(tag3 + "_uniq: " + map3Uniq.size());
				System.out.println("overlapAll: " + overlapAll.size());
				System.out.println(tag1 + tag2 + "_uniq: " + map1map2OverlapUniq.size());
				System.out.println(tag1 + tag3 + "_uniq: " + map1map3OverlapUniq.size());
				System.out.println(tag2 + tag3 + "_uniq: " + map2map3OverlapUniq.size());
				
				writeSummaryFile(outputFolder + "/" + tag1 + "_uniq_" + file, map1Uniq, map1, map2, map3, tag1, tag2, tag3, file1Header);
				writeSummaryFile(outputFolder + "/" + tag1 + "_uniq_" + file, map1Uniq, map1, map2, map3, tag1, tag2, tag3, file1Header);
				writeSummaryFile(outputFolder + "/" + tag1 + "_uniq_" + file, map1Uniq, map1, map2, map3, tag1, tag2, tag3, file1Header);
				writeSummaryFile(outputFolder + "/" + tag1 + tag2 + "_uniq_" + file, map1map2OverlapUniq, map1, map2, map3, tag1, tag2, tag3, file1Header);
				writeSummaryFile(outputFolder + "/" + tag1 + tag3 + "_uniq_" + file, map1map3OverlapUniq, map1, map2, map3, tag1, tag2, tag3, file1Header);
				writeSummaryFile(outputFolder + "/" + tag2 + tag3 + "_uniq_" + file, map2map3OverlapUniq, map1, map2, map3, tag1, tag2, tag3, file1Header);
				writeSummaryFile(outputFolder + "/" + "Overlap_" + file, overlapAll, map1, map2, map3, tag1, tag2, tag3, file1Header);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void writeSummaryFile(String outputFile, HashMap geneSetMap, HashMap map1, HashMap map2, HashMap map3, String tag1, String tag2, String tag3, String header) {
		try {
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("SampleName\t" + header + "\n");
			Iterator itr = geneSetMap.keySet().iterator();
			while (itr.hasNext()) {
				String key = (String)itr.next();
				if (map1.containsKey(key)) {
					LinkedList list = (LinkedList)map1.get(key);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						String str = (String)itr2.next();
						out.write(tag1 + "\t" + str + "\n");
					}
				}
				if (map2.containsKey(key)) {
					LinkedList list = (LinkedList)map2.get(key);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						String str = (String)itr2.next();
						out.write(tag2 + "\t" + str + "\n");
					}
				}
				if (map3.containsKey(key)) {
					LinkedList list = (LinkedList)map3.get(key);
					Iterator itr2 = list.iterator();
					while (itr2.hasNext()) {
						String str = (String)itr2.next();
						out.write(tag3 + "\t" + str + "\n");
					}
				}								
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Find genes uniq in hashmap map1 
	 * @param map1
	 * @param map2
	 * @param map3
	 */
	public static HashMap getOverlap(HashMap map1, HashMap map2) {
		HashMap map = new HashMap();
		Iterator itr = map1.keySet().iterator();
		while (itr.hasNext()) {
			String geneMap1 = (String)itr.next();
			if (map2.containsKey(geneMap1)) {
				map.put(geneMap1, map1.get(geneMap1));
			}
		}
		return map;
	}
	/**
	 * Find genes uniq in hashmap map1 
	 * @param map1
	 * @param map2
	 * @param map3
	 */
	public static HashMap getUniq(HashMap map1, HashMap map2) {
		HashMap map = new HashMap();
		Iterator itr = map1.keySet().iterator();
		while (itr.hasNext()) {
			String geneMap1 = (String)itr.next();
			if (!map2.containsKey(geneMap1)) {
				map.put(geneMap1, map1.get(geneMap1));
			}
		}
		return map;
	}
	/**
	 * Find genes uniq in hashmap map1 
	 * @param map1
	 * @param map2
	 * @param map3
	 */
	public static HashMap getUniq(HashMap map1, HashMap map2, HashMap map3) {
		HashMap map = new HashMap();
		Iterator itr = map1.keySet().iterator();
		while (itr.hasNext()) {
			String geneMap1 = (String)itr.next();
			if (!map2.containsKey(geneMap1) && !map3.containsKey(geneMap1)) {
				map.put(geneMap1, map1.get(geneMap1));
			}
		}
		return map;
	}
public static String getHeader(String fileName) {
		String header = "";
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return header;
	}
	public static HashMap differentialExpression(String fileName, double pvalue) {
		
		HashMap map = new HashMap();
		try {
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[2].toUpperCase();
				int index = 18;
				if (fileName.contains("MXE.MATS")) {
					index = 20;
				}
				if (new Double(split[index]) < pvalue) {
					if (map.containsKey(geneName)) {
						LinkedList list = (LinkedList)map.get(geneName);
						list.add(str);
						map.put(geneName, list);
					} else {
						LinkedList list = new LinkedList();
						list.add(str);
						map.put(geneName, list);
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
