package RNAseqTools.SpliceFactorAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import Statistics.General.MathTools;

/**
 * Given a position weight matrix and a fasta sequence, provide the score.
 * 
 * @author tshaw
 * 
 */
public class SpliceFactorMotifScanner {

	public static String parameter_info() {
		return "[inputFile] [fastaFile] [cutoff] [outputFile]";
	}

	public static void main(String[] args) {
		
		try {
			String fastaFile = args[1];
			HashMap sequences = grab_sequences(fastaFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void execute(String[] args) {

		try {

			String inputFile = args[0];
			String fastaFile = args[1];
			double cutoff = new Double(args[2]);
			String outputFile = args[3];
			// String inputFile =
			// "C:\\Users\\tshaw\\Desktop\\RNASEQ\\REFERENCE\\PWM_RNABinding\\PWMDir\\all_PWMs.masta";
			HashMap sequences = grab_sequences(fastaFile);
			HashMap pwm_map = calculate_consensus(sequences);

			HashMap all = new HashMap();
			HashMap hit = new HashMap();
			String nametmp = "";
			// String seq =
			// "ACACATGCAACTTCGACTTGGTGGAGGACGGGCTCACTGCCATGGTGAGCGGGGGCGGGGACTGGGATGGTCTCCGGGACAGCGTGCCCCCCTTTACACCGGATTTCGAAGGTGCCACCGCACGGCTGGGCCGGGGTGGAGCAGGCGACTTCCGGACACATCCCTTCTTCTTTGGCCTCGACGAAGGGGTCCCTGAGGAGGCTCGAGACTTCATTCAGCGGTTGCTGTGTCCCCCGGAGAGAGCACCTCTCTCTGCCGCTGGTGG";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					nametmp = str;
					all.put(nametmp, nametmp);
				} else {
					String seq = str.replaceAll("T", "U");
					Iterator itr = pwm_map.keySet().iterator();
					while (itr.hasNext()) {
						String name = (String) itr.next();
						// System.out.println(name);
						LinkedList list = (LinkedList) pwm_map.get(name);
						double min = getMin(list);
						double max = getMax(list);
						for (int i = 0; i < seq.length() - list.size(); i++) {
							String sub_seq = seq.substring(i, i + list.size());
							double score = calc_pwm_score(sub_seq, list);
							double relative_score = (score - min) / (max - min);
							if (relative_score >= cutoff) {
								if (hit.containsKey(name)) {
									LinkedList pos = (LinkedList) hit.get(name);
									if (!pos.contains(nametmp)) {
										pos.add(nametmp);
									}
									hit.put(name, pos);
								} else {
									LinkedList pos = new LinkedList();
									if (!pos.contains(nametmp)) {
										pos.add(nametmp);
									}
									hit.put(name, pos);
								}
							}
							// System.out.println((i + 1) + "\t" +
							// relative_score + "\t" + score);
						}

					}
				}
			}
			in.close();

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.write("Name\tHit#\tTotal#\tProportion\n");
			Iterator itr = hit.keySet().iterator();
			while (itr.hasNext()) {
				String name = (String) itr.next();
				LinkedList list = (LinkedList) hit.get(name);
				// System.out.println(name + "\t" + list.size() + "\t" +
				// all.size());
				out.write(name + "\t" + list.size() + "\t" + all.size() + "\t"
						+ new Double(list.size()) / all.size() + "\n");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static double calc_pwm_score(String sub_seq, LinkedList list) {
		double total = 0;
		Iterator itr = list.iterator();
		int i = 0;
		while (itr.hasNext()) {
			HashMap map = (HashMap) itr.next();
			// for (int i = 0; i < sub_seq.length(); i++) {
			String s = sub_seq.substring(i, i + 1);
			if (map.containsKey(s)) {
				double value = (Double) map.get(s);
				total += value;
			} else {
				total += 0;
			}
			i++;
			// }
		}
		return total;
	}

	public static double getMin(LinkedList list) {
		double min = 0;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			HashMap map = (HashMap) itr.next();
			Iterator itr2 = map.keySet().iterator();
			double min_value = Double.MAX_VALUE;
			while (itr2.hasNext()) {
				String nuc = (String) itr2.next();
				double value = (Double) map.get(nuc);
				if (min_value > value)
					min_value = value;
			}
			min += min_value;
		}
		return min;
	}

	public static double getMax(LinkedList list) {
		double max = 0;
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			HashMap map = (HashMap) itr.next();
			Iterator itr2 = map.keySet().iterator();
			double max_value = Double.MIN_VALUE;
			while (itr2.hasNext()) {
				String nuc = (String) itr2.next();
				double value = (Double) map.get(nuc);
				if (max_value < value)
					max_value = value;
			}
			max += max_value;
		}
		return max;
	}

	/**
	 * Assumes motif is of length 6
	 * 
	 * @param sequences
	 * @return
	 */
	public static HashMap calculate_consensus(HashMap sequences) {
		HashMap map = new HashMap();
		Iterator itr = sequences.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String) itr.next();
			LinkedList list = (LinkedList) sequences.get(name);
			// assume motif is of length 6
			int length = 0;
			Iterator itr2 = list.iterator();
			while (itr2.hasNext()) {
				String seq = (String) itr2.next();
				length = seq.length();
			}

			HashMap motif = new HashMap();
			for (int i = 0; i < length; i++) {
				itr2 = list.iterator();
				while (itr2.hasNext()) {
					String seq = (String) itr2.next();
					String nuc = seq.substring(i, i + 1);
					if (motif.containsKey(i)) {
						HashMap proportion = (HashMap) motif.get(i);
						if (proportion.containsKey(nuc)) {
							double count = (Double) proportion.get(nuc);
							count = count + (1.0 / sequences.size());
							proportion.put(nuc, count);
						} else {
							proportion.put(nuc, (1.0 / sequences.size()));
						}
						motif.put(i, proportion);
					} else {
						HashMap proportion = new HashMap();
						proportion.put(nuc, (1.0 / sequences.size()));
						motif.put(i, proportion);
					}
				}
			}
			LinkedList list_motif = new LinkedList();
			for (int i = 0; i < length; i++) {
				HashMap proportion = (HashMap) motif.get(i);
				HashMap newproportion = new HashMap();
				Iterator itr3 = proportion.keySet().iterator();
				while (itr3.hasNext()) {
					String orig_nuc = (String)itr3.next();
					double value = (Double)proportion.get(orig_nuc);
					String nuc = orig_nuc.toUpperCase();					
					if (nuc.equals("U")) {
						value = MathTools.log2(value / 0.2179508);
					} else if (nuc.equals("T")) {
						value = MathTools.log2(value / 0.2179508);
					} else if (nuc.equals("A")) {
						value = MathTools.log2(value / 0.2591773);
					} else if (nuc.equals("C")) {
						value = MathTools.log2(value / 0.2602537);
					} else if (nuc.equals("G")) {
						value = MathTools.log2(value / 0.2625182);
					}
					newproportion.put(orig_nuc, value);
				}
				
				list_motif.add(newproportion);
			}
			map.put(name, list_motif);
		}
		return map;
	}

	/**
	 * Assumes motif is of length 6
	 * 
	 * @param sequences
	 * @return
	 */
	/*public static HashMap calculate_consensus(HashMap sequences) {
		HashMap map = new HashMap();
		Iterator itr = sequences.keySet().iterator();
		while (itr.hasNext()) {
			String name = (String) itr.next();
			LinkedList list = (LinkedList) sequences.get(name);
			// assume motif is of length 6
			int length = 0;
			Iterator itr2 = list.iterator();
			while (itr2.hasNext()) {
				String seq = (String) itr2.next();
				length = seq.length();
			}

			HashMap motif = new HashMap();
			for (int i = 0; i < length; i++) {
				itr2 = list.iterator();
				while (itr2.hasNext()) {
					String seq = (String) itr2.next();
					String nuc = seq.substring(i, i + 1);
					if (motif.containsKey(i)) {
						HashMap proportion = (HashMap) motif.get(i);
						if (proportion.containsKey(nuc)) {
							double count = (Double) proportion.get(nuc);
							count = count + (1.0 / sequences.size());
							proportion.put(nuc, count);
						} else {
							proportion.put(nuc, (1.0 / sequences.size()));
						}
						motif.put(i, proportion);
					} else {
						HashMap proportion = new HashMap();
						proportion.put(nuc, (1.0 / sequences.size()));
						motif.put(i, proportion);
					}
				}
			}
			LinkedList list_motif = new LinkedList();
			for (int i = 0; i < length; i++) {
				HashMap proportion = (HashMap) motif.get(i);
				list_motif.add(proportion);
			}
			map.put(name, list_motif);
		}
		return map;
	}*/

	public static HashMap grab_sequences(String inputFile) {
		HashMap map = new HashMap();
		try {
			String name = "";
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					if (str.contains(">")) {
						name = str.replaceAll(">", "");
					} else {
						if (map.containsKey(name)) {
							LinkedList list = (LinkedList) map.get(name);
							list.add(str.trim());
							map.put(name, list);
						} else {
							LinkedList list = new LinkedList();
							list.add(str.trim());
							map.put(name, list);
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
	 * Load the PWM
	 * 
	 * @param inputFile
	 * @return
	 */
	/*
	 * public static HashMap loadPWM(String inputFile) { HashMap pwm_map = new
	 * HashMap(); try {
	 * 
	 * 
	 * String name = ""; FileInputStream fstream = new
	 * FileInputStream(inputFile); DataInputStream din = new
	 * DataInputStream(fstream); BufferedReader in = new BufferedReader(new
	 * InputStreamReader(din)); while (in.ready()) { String str =
	 * in.readLine().trim();
	 * 
	 * if (str.contains(">")) { name = str.split(":")[str.split(":").length -
	 * 1];
	 * 
	 * // for nucleotide A str = in.readLine().trim(); LinkedList pwm = new
	 * LinkedList(); while (str.contains("  ")) { str = str.replaceAll("  ",
	 * " "); } String[] split = str.split(" "); for (int i = 0; i <
	 * split.length; i++) { HashMap map = new HashMap(); double value = new
	 * Double(split[i]); map.put("A", value); pwm.add(map); }
	 * 
	 * // for nucleotide C str = in.readLine().trim(); while
	 * (str.contains("  ")) { str = str.replaceAll("  ", " "); } LinkedList
	 * pwm_new = new LinkedList(); split = str.split(" "); Iterator itr =
	 * pwm.iterator(); int i = 0; while (itr.hasNext()) { HashMap map =
	 * (HashMap)itr.next(); double value = new Double(split[i]); map.put("C",
	 * value); pwm_new.add(map); i++; } pwm = pwm_new; pwm_map.put(name, pwm);
	 * 
	 * // for nucleotide G str = in.readLine().trim(); while
	 * (str.contains("  ")) { str = str.replaceAll("  ", " "); } pwm_new = new
	 * LinkedList(); split = str.split(" "); itr = pwm.iterator(); i = 0; while
	 * (itr.hasNext()) { HashMap map = (HashMap)itr.next(); double value = new
	 * Double(split[i]); map.put("G", value); pwm_new.add(map); i++; } pwm =
	 * pwm_new; pwm_map.put(name, pwm);
	 * 
	 * 
	 * // for nucleotide U str = in.readLine().trim(); while
	 * (str.contains("  ")) { str = str.replaceAll("  ", " "); } pwm_new = new
	 * LinkedList(); split = str.split(" "); itr = pwm.iterator(); i = 0; while
	 * (itr.hasNext()) { HashMap map = (HashMap)itr.next(); double value = new
	 * Double(split[i]); map.put("U", value); pwm_new.add(map); i++; } pwm =
	 * pwm_new; pwm_map.put(name, pwm);
	 * 
	 * } else {
	 * 
	 * } } in.close();
	 * 
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } return pwm_map; }
	 */
}
