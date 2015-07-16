package Metagenomic.Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;

public class ChemoProjectGenerateMetaFile {

	public static void execute(String[] args) {
		
		try {
			String inputFolder = args[0];
			String tag = args[1];
			String outputFile = args[2];
			

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			String header = "#SampleID\tBarcodeSequence\tLinkerPrimerSequence\tType\tTreatmentInfo\tDescription";
			out.write(header + "\n");
			File[] files = new File(inputFolder).listFiles();
			for (File file: files) {
				String fileName = file.getPath();
				int len = fileName.length();
				String name = file.getName();
				String prefix = name.split("_")[0];
				
				String tail = fileName.substring(len - tag.length(), len);
				//System.out.println(tail);
				if (tail.equals(tag)) {				
					String type = name.split("_")[1];
					String treatment = "NA";
					if (type.contains("A")) {
						treatment = "Before";
					} else if (type.contains("B")) {
						treatment = "During";
					} else {
						treatment = "After";
					}
					String leftPrimer = generateRandomNuc(12);
					String rightPrimer = generateRandomNuc(23);
					out.write("ID" + prefix + "\t" + leftPrimer + "\t" + rightPrimer + "\t" + type + "\t" + treatment + "\t" + name + "\n"); 
				}
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		System.out.println();
	}
	public static String generateRandomNuc(int length) {
		Random rand = new Random();
		String str = "";
		for (int i = 0; i < length; i++) {
			int val = rand.nextInt(4);
			if (val == 0) {
				str += "A";
			} else if (val == 1) {
				str += "T";
			} else if (val == 2) {
				str += "C";
			} else {
				str += "G";
			}
			//System.out.println(val);
		}
		return str;
	}
}
