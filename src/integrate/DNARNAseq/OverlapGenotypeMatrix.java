package integrate.DNARNAseq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Based on the exome genotype matrix
 * Overlap it against RNA's genotype matrix
 * @author tshaw
 *
 */
public class OverlapGenotypeMatrix {

	public static String parameter_info() {
		return "[RNA_inputFile1] [Singleton_inputFile2] [outputFile]";
	}
	public static void execute(String[] args) {
		
		try {
			HashMap map = new HashMap();
			String inputFile = args[0];
			String inputFile2 = args[1];
			String outputFile = args[2];

			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] header = in.readLine().split("\t");			
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				for (int i = 0; i < header.length; i++) {
					if (header[i].contains("bam")) {
						map.put(header[i].split("_")[0] + "_" + split[0], split[i]);
						System.out.println(header[i].split("_")[0] + "_" + split[0] + "\t" + split[i]);
					}
				}				
			}
			in.close();			
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String orig_header = in.readLine();
			out.write(orig_header + "DNA#Mut\tDNA#Total\tRNA#Mut\tRNA#Total\n");
			header = orig_header.split("\t");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String sample = split[4].split("_")[0];
				String chr = "chr" + split[5];
				String pos = split[6];
				String mut_tumor = split[11];
				String ref_tumor = split[12];
				String refallele = split[15];
				String mutallele = split[16];
				String key = sample + "_" + chr + "." + pos + "." + refallele + "." + mutallele;
				String rna_maf_str = split[11] + "\t" + split[12];
				if (map.containsKey(key)) {
					String stuff = (String)map.get(key);
					String[] split2 = stuff.split("\\|");
					rna_maf_str += "\t" + split2[0] + "\t" + split2[1];
				} else {
					rna_maf_str += "\tNA\tNA";
				}
				
				out.write(str + "\t" + rna_maf_str + "\n");
				//System.out.println(chr + "." + pos + "." + refallele + "." + mutallele);
				/*for (int i = 0; i < header.length; i++) {
					if (header[i].contains("bam")) {
						
						String key = header[i].split("_")[0] + "_" + chr + "." + pos + "." + refallele + "." + mutallele;
						System.out.println("Other:" + key);
						if (map.containsKey(key)) {
							String rna = (String)map.get(key);
							System.out.println("Hit?");
							if (new_str.equals("")) {
								new_str = split[i] + "|" + rna;
							} else {
								new_str += "\t" + split[i] + "|" + rna;
							}
						} else {
							if (new_str.equals("")) {
								new_str = split[i];
							} else {
								new_str += "\t" + split[i];
							}
						}
					} else {
						if (new_str.equals("")) {
							new_str = split[i];
						} else {
							new_str += "\t" + split[i];
						}
					}
					
				}*/
				
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
