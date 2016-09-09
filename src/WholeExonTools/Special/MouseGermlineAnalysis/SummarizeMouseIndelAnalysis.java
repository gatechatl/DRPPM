package WholeExonTools.Special.MouseGermlineAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Given a list of files generate a table of putative indels that satisfy the quality tag and maf cutoff.
 * @author tshaw
 *
 */
public class SummarizeMouseIndelAnalysis {

	public static String description() {
		return "Given a list of files generate a table of putative indels that satisfy the quality tag and maf cutoff.";
	}
	public static String type() {
		return "INDEL";
	}
	public static String parameter_info() {
		return "[inputIndelFileLst] [qualityTag SJHQ,SJLQ] [MAF cutoff] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String qualityTag = args[1];
			double minMAF = new Double(args[2]);
			String outputFile = args[3];
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);						
			
			HashMap uniq = new HashMap();
			boolean header_once = true;
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			while (in.ready()) {
				String str = in.readLine();
				if (!str.trim().equals("")) {
					File f = new File(str);
					if (f.exists()) {
						FileInputStream fstream2 = new FileInputStream(str);
						DataInputStream din2 = new DataInputStream(fstream2);
						BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
						String header = in2.readLine();
						if (header_once) {
							out.write(header + "\n");
							header_once = false;
						}
						while (in2.ready()) {
							String str2 = in2.readLine();
							String[] split = str2.split("\t");
							double mut = new Double(split[9]);
							double total = new Double(split[10]);
							if (qualityTag.contains(split[1]) && mut / total > minMAF) {
								if (uniq.containsKey(str2)) {
									
								} else {
									out.write(str2 + "\n");
								}
								uniq.put(str2, str2);
							}						
						}
						in2.close();
					}
				}				
			}
			in.close();
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
