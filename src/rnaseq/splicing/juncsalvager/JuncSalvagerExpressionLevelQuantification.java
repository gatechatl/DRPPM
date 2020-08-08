package rnaseq.splicing.juncsalvager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Categorize the percentile of the expression after 1 FPKM cutoff
 * @author tshaw
 *
 */
public class JuncSalvagerExpressionLevelQuantification {

	public static String description() {
		return "Redo the ranking after 1 FPKM cutoff";
	}
	public static String type() {
		return "DATAMATRIX";
	}
	public static String parameter_info() {
		return "[inputPCGPFolder] [inputPCGPAnnotationFile] [inputGTExFolder] [inputGTExAnnotationFile] [outputComparison] [outputMetaAnalysis]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String fileList = args[0];
			String gtfFile = args[1];
			
			FileInputStream fstream = new FileInputStream(fileList);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				//System.out.println("drppm -")
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
