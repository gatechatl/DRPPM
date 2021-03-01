package rnaseq.splicing.csiminer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import misc.CommandLine;

/**
 * Generates the custom matrix and custom heatmap
 * @author gatechatl
 *
 */
public class CSIMinerGenerateCustomHeatmapFromPercentileMatrix {

	public static String description() {
		return "Generate a custom heatmap for each gene from the percentile matrix.";
	}
	public static String type() {
		return "CSI-Miner";
	}
	public static String parameter_info() {
		return "[inputMatrixFile] [outputFolder]";
	}
	public static void execute(String[] args) {
		
		try {
			String inputMatrixFile = args[0];
			String outputFolder = args[1];

			File f = new File(outputFolder);
			if (!f.isDirectory()) {
				f.mkdir();
			}
			
			HashMap geneMap = new HashMap();
			FileInputStream fstream = new FileInputStream(inputMatrixFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			//out.write(header + "\tAnnotation\n");
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String geneName = split[0].split("_chr")[0].split("_ECM")[0];
				geneMap.put(geneName, geneName);
			}
			in.close();
			
			Iterator itr = geneMap.keySet().iterator();
			while (itr.hasNext()) {
				String geneSymbol = (String)itr.next();
				
				String outputFile = outputFolder + "/" + geneSymbol + ".matrix.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);	
				fstream = new FileInputStream(inputMatrixFile);
				din = new DataInputStream(fstream);
				in = new BufferedReader(new InputStreamReader(din));
				header = in.readLine();
				out.write(header + "\n");				
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String geneName = split[0].split("_chr")[0].split("_ECM")[0];
					if (geneName.equals(geneSymbol)) {
						out.write(str + "\n");
					}
				}
				in.close();
				out.close();
				
				String script = "drppm -GenerateExonExpressionHeatmapJavaScript " + outputFolder + "/" + geneSymbol + ".matrix.txt > " + outputFolder + "/" + geneSymbol + ".matrix.txt.html";
				CommandLine.executeCommand(script);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
