package RNAseqTools.AlternativeSplicing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import IDConversion.GTFFile;

/**
 * If an ensembl gtf file was used for MATS, it doesn't 
 * print out the geneName and this will replace that empty field
 * @author tshaw
 *
 */
public class AddGeneName2MATS {

	public static void execute(String[] args) {
		
		try {
			
			String filePath = args[0];
			String gtfFile = args[1];
			
			String[] mats_files = {"A3SS.MATS.JunctionCountOnly.txt", "A3SS.MATS.ReadsOnTargetAndJunctionCounts.txt", "A5SS.MATS.JunctionCountOnly.txt", "A5SS.MATS.ReadsOnTargetAndJunctionCounts.txt", "MXE.MATS.JunctionCountOnly.txt", "MXE.MATS.ReadsOnTargetAndJunctionCounts.txt", "RI.MATS.JunctionCountOnly.txt", "RI.MATS.ReadsOnTargetAndJunctionCounts.txt", "SE.MATS.JunctionCountOnly.txt", "SE.MATS.ReadsOnTargetAndJunctionCounts.txt"};
			
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
