package GraphsFigures;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import MISC.RunRScript;

public class MultipleBarPlotGenerator {
	public static String type() {
		return "GRAPH";
	}
	public static String description() {
		return "Given a matrix table generate bar plot for each gene";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [geneListFile] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String geneNameFile = args[1];
			String outputFolder = args[2];
			File f = new File(outputFolder);
			if (!f.exists()) {
				System.out.println("Please generate a folder at + " + outputFolder + " before running this script");
				System.exit(0);
			}
			//String outputPng = args[2];
			//String type = args[3]; // PROTEOMICS, FPKM, LOG2
			
			HashMap geneName = new HashMap();
			FileInputStream fstream = new FileInputStream(geneNameFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			//String sample_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				geneName.put(str, str);
			}
			in.close();
			fstream = new FileInputStream(inputFile);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String sample_header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[0];
				if (geneName.containsKey(gene)) {
					double[] values = new double[split.length - 1];
					String[] samples = new String[split.length - 1];
					for (int i = 1; i < split.length; i++) {						
						values[i - 1] = new Double(split[i]);
						/*if (type.equals("PROTEOMICS")) {
							values[i - 1] = new Double(new Double(values[i - 1] * 100).intValue()) / 100;
						} else if (type.equals("RNASEQ")) {
							values[i - 1] = new Double(new Double(values[i - 1] * 1000).intValue()) / 1000;
						}*/
						samples[i - 1] = sample_header.split("\t")[i];
					}
					String script = BarPlotGenerator.generateBarPlot(values, samples, outputFolder + "/" + gene + ".png", gene);
					RunRScript.writeFile(gene + ".r", script);
					RunRScript.runRScript(gene + ".r");
				}
			}
			in.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
