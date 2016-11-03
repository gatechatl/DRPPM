package RNAseqTools.SingleCell.CellOfOrigin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Generate a matrix for the variant to hopefully resolve the cell of origin
 * @author tshaw
 *
 */
public class PostProcessingOfVariantMatrix {

	public static String description() {
		return "Generate a matrix for the variant to hopefully resolve the cell of origin";
	}
	public static String type() {
		return "SNV";
	}
	public static String parameter_info() {
		return "[variantMatrix] [snvTable] [outputFileRaw] [outputTrueFalseTable]";
	}
	public static void execute(String[] args) {
		
		try {
			String variantMatrix = args[0]; // the
			String snvTable = args[1]; // original snv table from SnpDetect
			// read the initial 
			String outputFileRaw = args[2];
			String outputFileBoolean = args[3];
			

			FileWriter fwriterRaw = new FileWriter(outputFileRaw);
			BufferedWriter outRaw = new BufferedWriter(fwriterRaw);
			

			FileWriter fwriter2 = new FileWriter(outputFileBoolean);
			BufferedWriter out2 = new BufferedWriter(fwriter2);
			
			HashMap map = new HashMap();
			FileInputStream fstream = new FileInputStream(snvTable);
			DataInputStream din = new DataInputStream(fstream);
			BufferedReader in = new BufferedReader(new InputStreamReader(din));
			String file1Header = in.readLine();
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				String gene = split[0];
				String qual = split[1];
				String type = split[5];
				String chr = split[3];
				String pos = split[4];
				String allele1 = split[13];
				String allele2 = split[14];
				String name = chr + "." + pos + "." + allele1 + "." + allele2;
				map.put(name, gene + "_" + qual + "_" + type);
			}
			in.close();
			
			fstream = new FileInputStream(variantMatrix);
			din = new DataInputStream(fstream);
			in = new BufferedReader(new InputStreamReader(din));
			String sampleHeader = in.readLine().replaceAll("Aligned.sortedByCoord.out.bam", "");
			outRaw.write(sampleHeader + "\n");
			out2.write(sampleHeader + "\n");
			while (in.ready()) {
				String str = in.readLine();
				String[] split = str.split("\t");
				boolean pass = false;
				if (map.containsKey(split[0])) {
					String meta = (String)map.get(split[0]);
	
					for (int i = 1; i < split.length; i++) {
						double mutallele = new Integer(split[i].split("\\|")[0]);
						double allele = new Integer(split[i].split("\\|")[1]);
						double total = mutallele + allele;
						if (mutallele >= 3 && mutallele / total >= 0.1) {
							pass = true;
							
						}
					}
					if (pass) {
						outRaw.write(meta + "_" + split[0]);
						out2.write(meta + "_" + split[0]);
						for (int i = 1; i < split.length; i++) {
							double mutallele = new Integer(split[i].split("\\|")[0]);
							double allele = new Integer(split[i].split("\\|")[1]);
							double total = mutallele + allele;
							outRaw.write("\t" + split[i]);
							if (mutallele >= 3 && mutallele / total >= 0.1) {
								out2.write("\t1");
							} else {
								out2.write("\t0");
							}
						}
						outRaw.write("\n");
						out2.write("\n");
					}
				}
				
					
			
			}
			in.close();
			outRaw.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
