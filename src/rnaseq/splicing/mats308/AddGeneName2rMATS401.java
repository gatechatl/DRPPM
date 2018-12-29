package rnaseq.splicing.mats308;

import idconversion.tools.GTFFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * If an ensembl gtf file was used for MATS, it doesn't 
 * print out the geneName and this will replace that empty field
 * @author tshaw
 *
 */
public class AddGeneName2rMATS401 {

	public static String type() {
		return "MATS";
	}
	public static String description() {
		return "For instances where gene name is absent, this script is necessary to replace the NAs with geneName based on gtf file.";
	}
	public static String parameter_info() {
		return "[rMATS Output Folder] [gtfFile]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String filePath = args[0];
			String gtfFile = args[1];
			
			String[] mats_files = {"MXE.MATS.JCEC.txt", "MXE.MATS.JC.txt", "RI.MATS.JCEC.txt", "RI.MATS.JC.txt", "SE.MATS.JCEC.txt", "SE.MATS.JC.txt", "A3SS.MATS.JCEC.txt", "A3SS.MATS.JC.txt", "A5SS.MATS.JCEC.txt", "A5SS.MATS.JC.txt"};
			
			GTFFile gtf = new GTFFile();
			gtf.initialize(gtfFile);
			System.out.println("Finished Reading GTF File");
			for (String mats_file: mats_files) {	
				System.out.println("Processing: " + mats_file);
				String outputFile = filePath + "/" + mats_file + ".addGeneName.txt";
				FileWriter fwriter = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fwriter);
				FileInputStream fstream = new FileInputStream(filePath + "/" + mats_file);
				DataInputStream din = new DataInputStream(fstream);
				BufferedReader in = new BufferedReader(new InputStreamReader(din));
				while (in.ready()) {
					String str = in.readLine();
					String[] split = str.split("\t");
					String ensembl_id = split[1].replaceAll("\"", "");
					if (gtf.geneid2geneName.containsKey(ensembl_id)) {
						String geneName = (String)gtf.geneid2geneName.get(ensembl_id);
						out.write(split[0] + "\t" + split[1] + "\t" + geneName);
						for (int i = 3; i < split.length; i++) {
							out.write("\t" + split[i]);
						}
						out.write("\n");
					} else {
						out.write(str + "\n");
					}
				}
				in.close();			
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
