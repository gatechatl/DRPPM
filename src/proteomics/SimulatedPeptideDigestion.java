package proteomics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SimulatedPeptideDigestion {

	public static String type() {
		return "PROTEOMICS";
	}
	public static String description() {
		return "Simulate peptide digestion of fasta file";
	}
	public static String parameter_info() {
		return "[inputFile] [lowerLimitFragmentSize] [upperLimitFragmentSize] [type: LYSC or TRYPTIC] [outputFile]";
	}

	public static void main(String[] args) {
		
		try {
			
			String inputFastaFile = args[0];
			LinkedList list = readFasta(inputFastaFile);
			int lowerLimit = new Integer(args[1]);
			int upperLimit = new Integer(args[2]);
			String type = args[3];
			String outputFile = args[4];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			int i = 0;
			HashMap make_shorter = new HashMap();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				//System.out.println(i++);
				String seq = (String)itr.next();
				HashMap map = simulateDegrade(seq, lowerLimit, upperLimit, type);
				Iterator itr2 = map.keySet().iterator();
				while (itr2.hasNext()) {
					String key = (String)itr2.next();
					if (!make_shorter.containsKey(key)) {
						//System.out.println(key);
						out.write(key + "\n");
						make_shorter.put(key, key);
					}
					
				}
				//System.out.println(map.size());
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void execute(String[] args) {
		try {
			
			String inputFastaFile = args[0];
			LinkedList list = readFasta(inputFastaFile);
			int lowerLimit = new Integer(args[1]);
			int upperLimit = new Integer(args[2]);			
			String type = args[3];
			String outputFile = args[4];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			
			int i = 0;
			HashMap make_shorter = new HashMap();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				//System.out.println(i++);
				String seq = (String)itr.next();
				HashMap map = simulateDegrade(seq, lowerLimit, upperLimit, type);
				Iterator itr2 = map.keySet().iterator();
				while (itr2.hasNext()) {
					String key = (String)itr2.next();
					if (!make_shorter.containsKey(key)) {
						//System.out.println(key);
						i++;
						out.write(">" + i + "\n" + key + "\n");
						make_shorter.put(key, key);
					}
					
				}
				//System.out.println(map.size());
			}
			out.close();
			System.out.println("Finished Peptide Degradation");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public static LinkedList readFasta(String fileName) {
		LinkedList list = new LinkedList();
		try {
			String seq = "";
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (str.contains(">")) {
					list.add(seq);
					seq = "";
				} else {
					seq += str.trim();
				}
			}
			in.close();
			list.add(seq);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static HashMap simulateDegrade(String seq, int lowerLimit, int upperLimit, String type) {
		LinkedList order = new LinkedList();
		String temp = "";
		for (int i = 0; i < seq.length(); i++) {
			temp += seq.substring(i, i + 1);
			/*if (seq.substring(i, i + 1).equals("R")) {
				if (i + 1 < seq.length()) {
					if (!seq.substring(i + 1, i + 2).equals("P")) {
						order.add(temp);
						temp = "";					
					}
				} else {
					order.add(temp);
					temp = "";
				}
			}*/
			if (type.equals("LYSC")) {
				if (seq.substring(i, i + 1).equals("K")) {
					if (i + 1 < seq.length()) {
						if (!seq.substring(i + 1, i + 2).equals("P")) {
							order.add(temp);
							temp = "";					
						}
					} else {
						order.add(temp);
						temp = "";
					}
				}
			} else if (type.equals("TRYPTIC")){
				if (seq.substring(i, i + 1).equals("R")) {
					if (i + 1 < seq.length()) {
						if (!seq.substring(i + 1, i + 2).equals("P")) {
							order.add(temp);
							temp = "";					
						}
					} else {
						order.add(temp);
						temp = "";
					}
				}
				if (seq.substring(i, i + 1).equals("K")) {
					if (i + 1 < seq.length()) {
						if (!seq.substring(i + 1, i + 2).equals("P")) {
							order.add(temp);
							temp = "";					
						}
					} else {
						order.add(temp);
						temp = "";
					}
				}
			}
		}
		order.add(temp);
		HashMap map = new HashMap();
		
		String last5 = "";
		String last4 = "";
		String last3 = "";
		String last2 = "";
		String last1 = "";
		String current = "";
		Iterator itr = order.iterator();
		while (itr.hasNext()) {
			last5 = last4;
			last4 = last3;
			last3 = last2;
			last2 = last1;
			last1 = current;
			current = (String)itr.next();
			//System.out.println(current);
			
			if (current.length() > lowerLimit && current.length() < upperLimit) {
				map.put(current, "");
			}
			/*if ((last1 + current).length() < 50) {
				map.put(last1 + current, "");
			}
			if ((last2 + last1 + current).length() < 50) {
				map.put(last2 + last1 + current, "");
			}
			if ((last3 + last2 + last1 + current).length() < 50) {
				map.put(last3 + last2 + last1 + current, "");
			}
			if ((last4 + last3 + last2 + last1 + current).length() < 50) {
				map.put(last4 + last3 + last2 + last1 + current, "");
			}
			if ((last5 + last4 + last3 + last2 + last1 + current).length() < 50) {
				map.put(last5 + last4 + last3 + last2 + last1 + current, "");
			}*/
		}
		return map;
	}

}

