package RNAseqTools.AlternativeSplicing;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class MATSScriptGenerator {

	public static void execute(String[] args) {
		
		try {
			
			String inputFile1 = args[0];
			String inputFile2 = args[1];
			String gtfFile = args[2];
			String outputFile = args[3];
			String pathOfPython = args[4];
			System.out.println(generateScript(inputFile1, inputFile2, gtfFile, outputFile, pathOfPython));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String generateScript(String inputFile1, String inputFile2, String gtfPath, String outputFile, String pythonPath) {
		//String script = "python " + pythonPath + " -b1 " + readFileList(inputFile1) + " -b2 " + readFileList(inputFile2) + " -gtf " + gtfPath + " -o " + outputFile + " -t paired -len 101 -a 8 -c 0.0001 -analysis U -expressionChange 10000";		
		String script = "python " + pythonPath + " -b1 " + readFileList(inputFile1) + " -b2 " + readFileList(inputFile2) + " -gtf " + gtfPath + " -o " + outputFile + " -t paired -len 101 -a 8 -c 0.0001 -analysis U";
		return script;
	}
	public static String readFileList(String inputFile) {
		String listFile = "";
		try {
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.equals("")) {
					if (listFile.equals("")) {
						listFile = str.trim();
					} else {
						listFile += "," + str.trim();
					}
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listFile;
	}
}
