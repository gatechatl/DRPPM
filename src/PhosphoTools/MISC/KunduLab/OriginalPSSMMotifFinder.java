package PhosphoTools.MISC.KunduLab;

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

import Statistics.General.MathTools;

/**
 * 
 * @author tshaw
 *
 */
public class OriginalPSSMMotifFinder {

	public static void main(String[] args) {
		
		try {
			
			//String[] listFile = {"Sec16A_NP_055681.1", "Sec23A_NP_006355.2", "Sec23B_NP_001166216.1", "Sec24A_NP_068817.1", "Sec24B_NP_006314.2", "Sec24C_NP_004913.2", "Sec24D_NP_055637.2"};
			String[] listFile = {"Sec16A_NP_055681.1"};
			String allSeq = "";
			for (String file: listFile) {
				HashMap map = new HashMap();
				String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\Fasta\\" + file + ".fasta"; //args[0];
				//String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\REFERENCE_FASTA\\HUMAN.fasta";
				
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));					
				while (in.ready()) {
					String str = in.readLine().trim();
					if (str.contains(">")) {
						
					} else {
						String seq = str.trim();
						allSeq += seq;						
					}
				}
				in.close();								
			}
				
			HashMap backFreq = getBackgroundFrequency(allSeq);
			Iterator itr2  = backFreq.keySet().iterator();
			while (itr2.hasNext()) {
				String aa = (String)itr2.next();
				double p = (Double)backFreq.get(aa);
				
			}
			//File f = new File("C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\ULK1_CLASP1-2_Project\\Convert2fasta");
			//for (File file: f.listFiles()) {
			for (String file: listFile) {
				String inputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\Fasta\\" + file + ".fasta"; //args[0];
				String pssmFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\ULK1_pssm_20151021.txt"; // args[1];			
				String outputFile = "C:\\Users\\tshaw\\Desktop\\PROTEOMICS\\Projects\\Kundu\\Fasta\\" + file + ".output"; //args[0];
				HashMap map = new HashMap();
				LinkedList pssm = readPSSM(pssmFile, 1);
				
				String geneName = "";
				
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				
				FileInputStream fstream = new FileInputStream(inputFile);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));					
				while (in.ready()) {
					String str = in.readLine().trim();
					if (str.contains(">")) {
						geneName = str;
					} else {
						if (map.containsKey(geneName)) {
							String seq = (String)map.get(geneName);
							map.put(geneName, seq + str.trim());
						} else {
							map.put(geneName, str.trim());
						}
					}
				}
				in.close();
				int id = 1;
				out.write("ID\tSite\tPeptide\tULK1PSSMScore\n");
				//System.out.println("Site\tPeptide\tULK1PSSMScore");
				int[] array = {2, 5, 6, 7};
				//int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
				Iterator itr = map.keySet().iterator();
				while (itr.hasNext()) {
					geneName = (String)itr.next();
					String seq = (String)map.get(geneName);
					
					for (int i = 0; i <= seq.length() - 10; i++) {
						String substring = seq.substring(i, i + 10);
						String phosphosite_aa = substring.substring(5, 6);
						if (substring.substring(5, 6).equals("S") || substring.substring(5, 6).equals("T")) {
							//System.out.println(id + "\t" + phosphosite_aa + (i + 6) + "\t" + substring + "\t" + pssm_scoring(substring, pssm, array, backFreq));
							out.write(id + "\t" + phosphosite_aa + (i + 6) + "\t" + substring + "\t" + score(substring, pssm, array) + "\n");
							id++;
						}					
					}
				}
				out.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static LinkedList readPSSM(String inputFile, double normalize) {
		LinkedList list = new LinkedList();
		try {

			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] split = in.readLine().split("\t");
			for (int i = 1; i < split.length; i++) {
				HashMap map = new HashMap();
				list.add(map);
			}
			while (in.ready()) {
				String str = in.readLine().trim();
				split = str.split("\t");
				for (int i = 1; i < split.length; i++) {
					HashMap map = (HashMap)list.get(i - 1);
					map.put(split[0], (new Double(split[i])) / normalize);					
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static double score(String seq, LinkedList pssm) {
		double score = 0;
		if (seq.length() != pssm.size()) {
			return -1; // not the same size
		}
		for (int i = 0; i < seq.length(); i++) {
			String aa = seq.substring(i, i + 1);
			HashMap map = (HashMap)pssm.get(i);			
			if (map.containsKey(aa)) {
				score += (Double)map.get(aa);
			} else {
				System.out.println("missing aa: " + aa);
			}
		}
		return score;
	}
	public static double pssm_scoring(String seq, LinkedList pssm) {
		double score = 0;
		if (seq.length() != pssm.size()) {
			return -1; // not the same size
		}
		for (int i = 0; i < seq.length(); i++) {
			String aa = seq.substring(i, i + 1);
			HashMap map = (HashMap)pssm.get(i);			
			if (map.containsKey(aa)) {
				score += MathTools.log2((Double)map.get(aa) / 0.05);
			} else {
				System.out.println("missing aa: " + aa);
			}
		}
		return score;
	}
	
	public static double score(String seq, LinkedList pssm, int[] location) {
		double score = 0;
		if (seq.length() != pssm.size()) {
			return -1; // not the same size
		}
		for (int i: location) {
			String aa = seq.substring(i, i + 1);
			HashMap map = (HashMap)pssm.get(i);			
			if (map.containsKey(aa)) {
				score += (Double)map.get(aa);
				//System.out.println(i + "\t" + aa + "\t" + map.get(aa));
			} else {
				System.out.println("missing aa: " + aa);
			}
			
		}
		return score;
	}
	public static double pssm_scoring(String seq, LinkedList pssm, int[] location) {
		double score = 0;
		if (seq.length() != pssm.size()) {
			return -1; // not the same size
		}
		for (int i: location) {
			String aa = seq.substring(i, i + 1);
			HashMap map = (HashMap)pssm.get(i);			
			if (map.containsKey(aa)) {
				score += MathTools.log2((Double)map.get(aa) / 0.05);
				//System.out.println(i + "\t" + aa + "\t" + map.get(aa));
			} else {
				System.out.println("missing aa: " + aa);
			}
			
		}
		return score;
	}
	
	public static double pssm_scoring(String seq, LinkedList pssm, int[] location, HashMap background_freq) {
		double score = 0;
		if (seq.length() != pssm.size()) {
			return -1; // not the same size
		}
		for (int i: location) {
			String aa = seq.substring(i, i + 1);
			HashMap map = (HashMap)pssm.get(i);			
			if (map.containsKey(aa) && background_freq.containsKey(aa)) {
				score += MathTools.log2((Double)map.get(aa) / (Double)background_freq.get(aa));
				//System.out.println(i + "\t" + aa + "\t" + map.get(aa));
			} else {
				System.out.println("missing aa: " + aa);
			}
			
		}
		return score;
	}
	public static HashMap getBackgroundFrequency(String seq) {
		HashMap map = new HashMap();
		for (int i = 0; i < seq.length(); i++) {
			String val = seq.substring(i, i + 1);
			if (map.containsKey(val)) {
				int count = (Integer)map.get(val);
				map.put(val, (count + 1));
			} else {
				map.put(val,  1);
			}
		}
		HashMap prob = new HashMap();
		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			String val = (String)itr.next();
			int count = (Integer)map.get(val);
			double count_d = new Double(count);
			prob.put(val, count_d / seq.length());
			System.out.println(val + "\t" + count_d / seq.length());
		}
		return prob;
	}
	public class PSSM {
		HashMap PROB = new HashMap();		
	}
	
	
}
