package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

public class Bam2BedConversion {

	public static void execute(String[] args) {
		
		String inputFile = args[0];
		System.out.println(generateScript(inputFile));
		
	}
	
	public static String generateScript(String inputFile1) {
		 
		LinkedList listFile = readFileList(inputFile1);
		String script = "";
		Iterator itr = listFile.iterator();		
		
		while (itr.hasNext()) {
			String file = (String)itr.next();
			String bedFile = file.split("/")[file.split("/").length - 1] + ".bed";
			script += "bamToBed -i " +  file + " > " + bedFile + "\n";
		}
		return script;
	}
	public static LinkedList readFileList(String inputFile) {
		
		LinkedList listFile = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					listFile.add(str.trim());
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFile;
	}
}
