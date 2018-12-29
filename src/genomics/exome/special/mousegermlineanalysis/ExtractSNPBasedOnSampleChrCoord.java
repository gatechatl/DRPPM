package genomics.exome.special.mousegermlineanalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class ExtractSNPBasedOnSampleChrCoord {

	public static String parameter_info() {
		return "[inputFile] [sampleName] [chr] [coord start] [coord end]";
	}
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0];
			String sampleName = args[1];
			String chr = args[2];
			int start = new Integer(args[3]);
			int end = new Integer(args[4]);
			String outputFile = args[5];
			String outputFile_MAF = args[6];
						
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);
			
			FileWriter fwriter_maf = new FileWriter(outputFile_MAF);
			BufferedWriter out_maf = new BufferedWriter(fwriter_maf);
			
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();	
				String[] split = str.split("\t");
				String chr_ = split[3];
				String pos = split[4];
				String ref_allele = split[9];
				String alt_allele = split[10];
				String snp = chr + "." + pos + "." + ref_allele + "." + alt_allele;
				double ref_count = new Double(split[15]);
				double alt_count = new Double(split[17]);
				if (split[1].contains(sampleName)) {
					if (split[3].equals(chr)) {
						int loc = new Integer(split[4]);
						if (loc >= start && loc <= end) {
							out.write(snp + "\n");
							//System.out.println(str);
							out_maf.write(snp + "\t" + ref_count + "\t" + alt_count + "\t" + alt_count / (ref_count + alt_count) + "\n");
						}
					}
				}								
			}
			in.close();
			out.close();
			out_maf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
