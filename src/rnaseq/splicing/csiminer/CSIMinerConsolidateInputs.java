package rnaseq.splicing.csiminer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 * Consolidate the exon names. Output the shared exons.
 * @author gatechatl
 *
 */
public class CSIMinerConsolidateInputs {

	public static String description() {
		return "Consolidate the exon names. Output the shared exons.";
	}
	public static String type() {
		return "CSI-Miner";
	}
	public static String parameter_info() {
		return "[CSIminer_Exons_first_input_file] [CSIMiner_Exons_second_input_file] [first_output_file] [second_output_file]";
	}
	public static void execute(String[] args) {
		
		try {
			
			HashMap map = new HashMap();
			String inputFile1 = args[0];
			String inputFile2 = args[1];
			String outputFile1 = args[2];
			String outputFile2 = args[3];
			
			FileWriter fwriter1 = new FileWriter(outputFile1);
			BufferedWriter out1 = new BufferedWriter(fwriter1);	
			
			FileWriter fwriter2 = new FileWriter(outputFile2);
			BufferedWriter out2 = new BufferedWriter(fwriter2);	
			
			FileInputStream fstream = new FileInputStream(inputFile1);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String new_exon = split[0].replaceAll("_ECM_", "_").replaceAll("_NovelExon_", "_").replaceAll("_KnownExon_", "_").replaceAll("_Pecan_", "_");
				map.put(new_exon, new_exon);				
				
			}
			in.close();
			
			HashMap map2 = new HashMap();
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out2.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String new_exon = split[0].replaceAll("_ECM_", "_").replaceAll("_NovelExon_", "_").replaceAll("_KnownExon_", "_").replaceAll("_Pecan_", "_");
				
				if (map.containsKey(new_exon)) {
					out2.write(new_exon);
					for (int i = 1; i < split.length; i++) {
						out2.write("\t" + split[i]);
					}
					out2.write("\n");
					map2.put(new_exon, new_exon);
				}
			}
			in.close();
			out2.close();
			
			
			fstream = new FileInputStream(inputFile1);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			header = in.readLine();
			out1.write(header + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String new_exon = split[0].replaceAll("_ECM_", "_").replaceAll("_NovelExon_", "_").replaceAll("_KnownExon_", "_").replaceAll("_Pecan_", "_");				
				if (map2.containsKey(new_exon)) {
					out1.write(new_exon);
					for (int i = 1; i < split.length; i++) {
						out1.write("\t" + split[i]);
					}
					out1.write("\n");
				}
			}
			in.close();
			out1.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
