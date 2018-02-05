package RNATools.PCPA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import misc.CommandLine;

public class ExtractPolyAReadsYuxinScript {

	/**
	 * Used for combining and filtering out polyA tails
	 * @author tshaw
	 *
	 */

	public static void execute(String[] args) {
		try {
			String fileName = args[0];
			String outputFile = args[1];
			String pathPerlScript = args[2];
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			out.close();
			
			LinkedList list = new LinkedList();
			
			FileInputStream fstream = new FileInputStream(fileName);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				list.add(str);
			}
			in.close();
			String tempFolder = UUID.randomUUID().toString();
			CommandLine.executeCommand("mkdir " + tempFolder);
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				fileName = (String)itr.next();
				String buffer = UUID.randomUUID().toString();
				String new_outputFile = buffer;
				CommandLine.executeCommand("perl " + pathPerlScript + "/trimPolyAT.pl " + fileName + " " + tempFolder + "/" + new_outputFile);
				CommandLine.executeCommand("cat " + tempFolder + "/" + new_outputFile + " >> " + outputFile);
				CommandLine.executeCommand("rm -rf " + tempFolder + "/" + new_outputFile);
			}			
			CommandLine.executeCommand("rm -rf " + tempFolder);
			//out.close();
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

