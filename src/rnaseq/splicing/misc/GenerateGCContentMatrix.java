package rnaseq.splicing.misc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Read fasta file and generate GC content matrix 
 * @author tshaw
 *
 */
public class GenerateGCContentMatrix {

	public static String parameter_info() {
		return "[inputFastaQuery] [inputFastaBackground]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap seq_map = new HashMap();
			String inputFastaQuery = args[0];
			String inputFastaBackground = args[1];
			String name = "";
			FileInputStream fstream = new FileInputStream(inputFastaQuery);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					name = str;
				} else {
					if (seq_map.containsKey(name)) {
						String seq = (String)seq_map.get(name);
						seq += str;
						seq_map.put(name, seq);
					} else {
						seq_map.put(name, str);
					}
				}
			}
			in.close();
			
			System.out.println("Type\tValue");
			Iterator itr = seq_map.keySet().iterator();
			while (itr.hasNext()) {
				name = (String)itr.next();
				String seq = (String)seq_map.get(name);
				System.out.println("SkippedExon\t" + calculateGC(seq));
			}
			
			name = "";
			seq_map = new HashMap();
			
			fstream = new FileInputStream(inputFastaBackground);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));			
			while (in.ready()) {
				String str = in.readLine().trim();
				if (str.contains(">")) {
					name = str;
				} else {
					if (seq_map.containsKey(name)) {
						String seq = (String)seq_map.get(name);
						seq += str;
						seq_map.put(name, seq);
					} else {
						seq_map.put(name, str);
					}
				}
			}
			in.close();
			
			itr = seq_map.keySet().iterator();
			while (itr.hasNext()) {
				name = (String)itr.next();
				String seq = (String)seq_map.get(name);
				System.out.println("Background\t" + calculateGC(seq));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static double calculateGC(String seq) {
		double gc = 0;
		seq = seq.toUpperCase();
		for (int i = 0; i < seq.length(); i++) {
			if (seq.substring(i, i + 1).equals("G") || seq.substring(i, i + 1).equals("C")) {
				gc++;
			}
		}
		return gc / seq.length();
	}
}
