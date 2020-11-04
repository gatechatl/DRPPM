package rnaseq.splicing.alternativejuncpipeline.intronretention;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Has some problem with this approach
 * @author tshaw
 *
 */
public class DetectIntronRetention {

	public static void execute(String[] args) {
		
		try {
			
			String inputFile1 = args[0];			
			String bedFile = args[1];
			String tag = args[2];
			
			System.out.println(generateScript(inputFile1, bedFile, tag));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateScript(String inputFile1, String bedFile, String tag) {
		LinkedList outputFile = outputFileList(inputFile1, tag); 
		LinkedList listFile = readFileList(inputFile1);
		String script = "";
		Iterator itr = listFile.iterator();		
		Iterator itr_output = outputFile.iterator();
		while (itr.hasNext()) {
			String file = (String)itr.next();
			String outputFileName = (String)itr_output.next();
			script += "coverageBed -abam " +  file + " -b " + bedFile + " > " + outputFileName + "\n";
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
	public static LinkedList outputFileList(String inputFile, String tag) {
		
		LinkedList listFile = new LinkedList();
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					String[] split = str.trim().split("/");
					String file = split[split.length - 1];
					listFile.add(file + "." + tag);
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFile;
	}
}
