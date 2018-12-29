package rnaseq.pcpa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Used for combining and filtering out polyA tails
 * @author tshaw
 *
 */
public class KeepPolyA {

	public static void execute(String[] args) {
		try {

			String outputFile = args[1];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			LinkedList list = new LinkedList();
			String fileName = args[0];
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				list.add(str);
			}
			in.close();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				fileName = (String)itr.next();
				fstream = new FileInputStream(fileName);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String tag = in.readLine();
					String seq = in.readLine();
					String plus = in.readLine();
					String qual = in.readLine();
					// check for polyAAA
					
					int countA = 0;
					int index = 0;
					for (int i = 0; i < seq.length(); i++) {
						String c = seq.substring(i, i + 1);
						if (c.equals("A")) {
							countA++;
						} else {
							index = i;
							countA = 0;
						}
					}
					if (countA > 15 && countA < 80) {
						out.write(tag + "\n");
						out.write(reverseComplement(seq.substring(0, index + 1)) + "\n");
						out.write(plus + "\n");
						out.write(reverseScore(qual.substring(0, index + 1)) + "\n");
					} else {
						String revSeq = reverseComplement(seq);
						String revQual = reverseScore(qual);
						countA = 0;
						index = 0;
						for (int i = 0; i < revSeq.length(); i++) {
							String c = revSeq.substring(i, i + 1);
							if (c.equals("A")) {
								countA++;
							} else {
								index = i;
								countA = 0;
							}
						}
						if (countA > 15 && countA < 80) {
							out.write(tag + "\n");
							out.write(reverseComplement(revSeq.substring(0, index + 1)) + "\n");
							out.write(plus + "\n");
							out.write(reverseScore(revQual.substring(0, index + 1)) + "\n");
						} 
					}
				}
				in.close();
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		//
		//+
		//
		//@HWI-ST1173:187:C4DB7ACXX:5:1305:11142:65152/2
		String tag = "name";
		String seq = "CTACCAAAAAAAACAAAAAAAAAAAAAGGGGGGGGGAAGGGTTGGGGGTGATCCCAGCCATTCATCCAAAGCCCCTCAGATAGTCCAGACATCCCTTGACC";
		String plus = "+";
		String qual = "CCCFFFFFHHHHHJIJJJJJJJJJJJIJJJJJIJJJJIJIIJJJJJIJJIIJIJHJJJJJJHHIJJJJJJJJJJJJJJHDDDDDDDDDDDDCDDDDDDB@9";
		// check for polyAAA
		
		int countA = 0;
		int index = 0;
		boolean found = false;
		for (int i = 0; i <= 15; i++) {
			String c = seq.substring(i, i + 1);
			if (c.equals("A")) {
				countA++;
			}
			
		}
		if (countA >= 11) {
			found = true;
		}
		if (found) {
			for (int i = 16; i < seq.length(); i++) {
				String c = seq.substring(i, i + 1);
				if (c.equals("A")) {
					countA++;
				} else {
					index = i;
					i = seq.length();
				}
			}			
			System.out.println(reverseComplement(seq.substring(index, seq.length())) + "\n");			
			System.out.println(reverseScore(qual.substring(index, seq.length())) + "\n");		
		}
		
		
		
	}
	public static String reverseScore(String input) {
		String result = "";
		for (int i = input.length() - 1; i >= 0; i--) {
			result += input.substring(i, i + 1);
		}
		return result;
	}
	public static String reverseComplement(String input) {
		String result = "";
		for (int i = input.length() - 1; i >= 0; i--) {
			String c = input.substring(i, i + 1).toUpperCase();
			if (c.equals("C")) {
				result += "G";
			} else if (c.equals("G")) {
				result += "C";
			} else if (c.equals("A")) {
				result += "T";
			} else if (c.equals("T")) {
				result += "A";
			} else {
				result += "N";
			}			
		}
		return result;
	}
}
