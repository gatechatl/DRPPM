package WholeExonTools.MISC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Specialized program for combining SNP call file with 
 * germline alternative allele counts in other few germline 
 * samples into a single file
 * @author tshaw
 *
 */
public class AppendGermlineAlternativeAlleleCount {
	public static void execute(String[] args) {
		
		try {
			
			String inputFile = args[0]; // the matrix_combined_matrix_sample_x_cell_variant_count_first.tab
			String inputFile2 = args[1]; // batch4_somatic_snvs.txt
			String outputFile = args[2]; // output File
			
			FileWriter fwriter = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fwriter);

			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String[] headers = in.readLine().split("\t");			 
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String[] split2 = split[0].split("\\.");
				String chr = split2[0]; // index 3 
				String position = split2[1]; // index 4
				String orig_nuc = split2[2];
				String new_nuc = split2[3];
				for (int i = 1; i < split.length; i++) {
					map.put(headers[i] + "\t" + chr + "\t" + position + "\t" + orig_nuc + "\t" + new_nuc, split[i]);
				}
			}
			in.close();						
			
			fstream = new FileInputStream(inputFile2);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String headerFile = in.readLine();
			out.write(headerFile);
			for (int i = 1; i < headers.length; i++) {
				out.write("\t" + headers[i]);
			}
			out.write("\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String chr = "";
				if (split[3].contains("chr")) {
					chr = split[3];
				} else {
					chr = "chr" + split[3];
				}
				String position = split[4];
				String orig_nuc = "";
				String new_nuc = "";
				if (split[13].contains(">")) {
					orig_nuc = split[13].split(">")[0];
					new_nuc = split[13].split(">")[1];
				} else {
					orig_nuc = split[13];
					new_nuc = split[14];
				}
				
				out.write(str);
				for (int i = 1; i < headers.length; i++) {
					
					String tag = headers[i] + "\t" + chr + "\t" + position + "\t" + orig_nuc + "\t" + new_nuc;
					if (map.containsKey(tag)) {
						String value = (String)map.get(tag);
						String[] split2 = value.split("\\|");
						int total = (new Integer(split2[0]) + new Integer(split2[1]));
						if (total == 0) {
							total = -1;
						}
						out.write("\t=" + split2[0] + "/" + total);
					} else {
						out.write("\tNA");
					}					
				}
				out.write("\n");
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
