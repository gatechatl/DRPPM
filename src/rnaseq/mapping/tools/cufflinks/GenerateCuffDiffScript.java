package rnaseq.mapping.tools.cufflinks;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class GenerateCuffDiffScript {

	public static void execute(String[] args) {
		
		try {
			
			String labels = args[0];
			String outputFolder = args[1];
			String reference = args[2];
			int num_threads = new Integer(args[3]);
			String lstbamFile1 = args[4];
			String lstbamFile2 = args[5];
			String bamFiles1 = convertList2Str(grabSamples(lstbamFile1));
			String bamFiles2 = convertList2Str(grabSamples(lstbamFile2));
			System.out.println("cuffdiff -L " + labels + " -o " + outputFolder + " -u " + reference + " -p " + num_threads + " " + bamFiles1 + " " + bamFiles2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String convertList2Str(LinkedList list) {
		String result = "";
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			String str = (String)itr.next();
			if (result.equals("")) {
				result += str;
			} else {
				result += "," + str;
			}
		}
		return result;
	}
	public static LinkedList grabSamples(String inputFile) {
		LinkedList list = new LinkedList();
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				list.add(str.trim());
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
