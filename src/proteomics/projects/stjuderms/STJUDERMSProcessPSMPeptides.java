package proteomics.projects.stjuderms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

public class STJUDERMSProcessPSMPeptides {

	public static String type() {
		return "PROTEOMICS";
	}
	public static String description() {
		return "Generate a fasta file and output table count of the PSMs";
	}
	public static String parameter_info() {
		return "[PSM.lst] [output FASTA] [output Table]";
	}
	public static void main(String[] args) {
		
		try {
			
			String inputFile = "/Users/4472414/Projects/Rhabdo_Proteomics/COMET/combined_pepxml.output.table.txt"; //args[0];
			String outputFasta = "/Users/4472414/Projects/Rhabdo_Proteomics/COMET/outputFasta.fa"; //args[1];
			String outputTable = "/Users/4472414/Projects/Rhabdo_Proteomics/COMET/outputTable.txt"; //args[2];
			
			FileWriter fwriter_fasta = new FileWriter(outputFasta);
			BufferedWriter out_fasta = new BufferedWriter(fwriter_fasta);
			
			FileWriter fwriter_table = new FileWriter(outputTable);
			BufferedWriter out_table = new BufferedWriter(fwriter_table);
			
			HashMap map = new HashMap();
			HashMap map2 = new HashMap();

			FileInputStream fstream2 = new FileInputStream(inputFile);
			DataInputStream din2 = new DataInputStream(fstream2);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(din2));
			
			while (in2.ready()) {
				String str2 = in2.readLine();
				
				String[] split2 = str2.split("\t");
				if (!split2[0].equals("outfile")) {
					double xcorr = new Double(split2[4]);
					double delta_xcorr = new Double(split2[5]);
					
					String peptide = split2[8].replaceAll("\"", "").replaceAll("\\.", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\*", "").replaceAll("%", "").replaceAll("$", "").replaceAll("#", "").replaceAll("@", "").replaceAll("-",  "").toUpperCase();
					String protein = split2[10];
					String target_decoy = split2[11];
					if (target_decoy.equals("target")) {
						map2.put(peptide, protein);
						System.out.println(peptide);
						if (map.containsKey(peptide)) {
							int count = (Integer)map.get(peptide);
							count++;
							map.put(peptide, count);
						} else {
							map.put(peptide, 1);
						}
					}
				}
			}
			in2.close();

			
			int index = 1;
			Iterator itr = map.keySet().iterator();
			while (itr.hasNext()) {
				String peptide = (String)itr.next();
				int count = (Integer)map.get(peptide);
				String gene = ((String)map2.get(peptide));
				gene = gene.replaceAll("\"", "").replaceAll("\\'",  "").trim();
				if (!gene.contains("Decoy")) {
					//String geneName = (String)GTFFile.geneid_clean2geneName.get(gene.split("\\.")[0]);
					out_table.write(peptide + "\t" + count + "\t" + gene + "\n");
					out_fasta.write(">" + index + "_" + gene + "_" + count + "\n");
					out_fasta.write(peptide + "\n");
				}
			}
			out_table.close();
			out_fasta.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
	